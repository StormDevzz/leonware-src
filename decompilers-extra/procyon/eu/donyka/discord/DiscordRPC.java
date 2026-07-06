// 
// Decompiled by Procyon v0.6.0
// 

package eu.donyka.discord;

import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;
import com.google.gson.JsonObject;
import java.nio.charset.StandardCharsets;
import eu.donyka.discord.discord.RichPresence;
import eu.donyka.discord.exceptions.NoDiscordClientException;
import eu.donyka.discord.exceptions.PipeAccessDenied;
import eu.donyka.discord.exceptions.UnsupportedOsType;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicBoolean;
import eu.donyka.discord.enums.ErrorCode;
import eu.donyka.discord.connection.RPCConnection;
import eu.donyka.discord.models.User;
import java.util.function.Consumer;

public class DiscordRPC
{
    private boolean isDebugMode;
    private final boolean disableIoThread;
    private long pid;
    private long nonce;
    private Consumer<User> onReady;
    private Consumer<ErrorInfo> onDisconnected;
    private Consumer<ErrorInfo> onErrored;
    private RPCConnection rpcConnection;
    private ErrorCode lastErrorCode;
    private String lastErrorMessage;
    private ErrorCode lastDisconnectErrorCode;
    private String lastDisconnectErrorMessage;
    private final AtomicBoolean wasJustConnected;
    private final AtomicReference<User> connectedUser;
    private final AtomicBoolean wasJustDisconnected;
    private final AtomicBoolean gotErrorMessage;
    private final Queue<byte[]> sendQueue;
    private final Queue<byte[]> presenceQueue;
    private final AtomicBoolean keepRunning;
    private final Lock waitForIoMutex;
    private final Condition waitForIOActivity;
    private Thread ioThread;
    
    public DiscordRPC() {
        this(false);
    }
    
    public DiscordRPC(final boolean disableIoThread) {
        this.isDebugMode = false;
        this.disableIoThread = disableIoThread;
        this.pid = -1L;
        this.nonce = -1L;
        this.onReady = null;
        this.onDisconnected = null;
        this.onErrored = null;
        this.rpcConnection = null;
        this.wasJustConnected = new AtomicBoolean(false);
        this.connectedUser = new AtomicReference<User>();
        this.wasJustDisconnected = new AtomicBoolean(false);
        this.gotErrorMessage = new AtomicBoolean(false);
        this.sendQueue = new ConcurrentLinkedQueue<byte[]>();
        this.presenceQueue = new ConcurrentLinkedQueue<byte[]>();
        this.keepRunning = new AtomicBoolean(true);
        this.waitForIoMutex = new ReentrantLock(true);
        this.waitForIOActivity = this.waitForIoMutex.newCondition();
        this.ioThread = null;
    }
    
    public void init(final String applicationId, final boolean autoRegister) throws UnsupportedOsType, PipeAccessDenied {
        this.init(applicationId, autoRegister, null);
    }
    
    public void init(final String applicationId, final boolean autoRegister, final String optionalSteamId) throws UnsupportedOsType, PipeAccessDenied {
        if (this.rpcConnection != null) {
            return;
        }
        this.pid = this.getProcessId();
        try {
            this.rpcConnection = RPCConnection.create(applicationId, this);
        }
        catch (final UnsupportedOsType e) {
            throw e;
        }
        if (autoRegister) {
            if (optionalSteamId != null && !optionalSteamId.isEmpty()) {
                this.rpcConnection.getBaseConnection().registerSteamGame(applicationId, optionalSteamId);
            }
            else {
                this.rpcConnection.getBaseConnection().register(applicationId, null);
            }
        }
        this.rpcConnection.setConnectedCallback(user -> {
            this.wasJustConnected.set(true);
            this.connectedUser.set(user);
            if (this.onReady != null) {
                this.onReady.accept(user);
            }
            return;
        });
        this.rpcConnection.setDisconnectedCallback(errorInfo -> {
            this.lastDisconnectErrorCode = errorInfo.getErrorCode();
            this.lastDisconnectErrorMessage = errorInfo.getMessage();
            this.wasJustDisconnected.set(true);
            return;
        });
        if (!this.disableIoThread) {
            this.keepRunning.set(true);
            (this.ioThread = new Thread(() -> {
                try {
                    this.discordRpcIo();
                }
                catch (final NoDiscordClientException | PipeAccessDenied noDiscordClientException | PipeAccessDenied) {}
                return;
            })).setDaemon(true);
            this.ioThread.start();
        }
    }
    
    public void shutdown() {
        if (this.rpcConnection == null) {
            return;
        }
        this.rpcConnection.setDisconnectedCallback(null);
        this.rpcConnection.setConnectedCallback(null);
        this.onReady = null;
        this.onDisconnected = null;
        this.onErrored = null;
        if (!this.disableIoThread) {
            this.keepRunning.set(false);
            this.signalIoActivity();
            try {
                if (this.ioThread != null) {
                    this.ioThread.join();
                }
            }
            catch (final Exception ex) {}
        }
        RPCConnection.destroy(this.rpcConnection);
        this.rpcConnection = null;
    }
    
    public void updatePresence(RichPresence richPresence) {
        if (richPresence == null) {
            richPresence = RichPresence.builder().build();
        }
        final JsonObject data = richPresence.toJson(this.pid, this.nonce++);
        this.presenceQueue.offer(data.toString().getBytes(StandardCharsets.UTF_8));
        this.signalIoActivity();
    }
    
    public void runCallbacks() {
        if (this.rpcConnection == null) {
            return;
        }
        final boolean wasDisconnected = this.wasJustDisconnected.getAndSet(false);
        final boolean isConnected = this.rpcConnection.isOpen();
        if (isConnected && wasDisconnected && this.onDisconnected != null) {
            this.onDisconnected.accept(new ErrorInfo(this.lastDisconnectErrorCode, this.lastDisconnectErrorMessage));
        }
        if (this.wasJustConnected.getAndSet(false) && this.onReady != null) {
            this.onReady.accept(this.connectedUser.get());
        }
        if (this.gotErrorMessage.getAndSet(false) && this.onErrored != null) {
            this.onErrored.accept(new ErrorInfo(this.lastErrorCode, this.lastErrorMessage));
        }
        if (!isConnected && wasDisconnected && this.onDisconnected != null) {
            this.onDisconnected.accept(new ErrorInfo(this.lastDisconnectErrorCode, this.lastDisconnectErrorMessage));
        }
    }
    
    private void discordRpcIo() throws NoDiscordClientException, PipeAccessDenied {
        while (this.keepRunning.get()) {
            try {
                this.updateConnection();
            }
            catch (final NoDiscordClientException | PipeAccessDenied noDiscordClientException | PipeAccessDenied) {}
            this.runCallbacks();
            this.waitForIoMutex.lock();
            try {
                this.waitForIOActivity.await(500L, TimeUnit.MILLISECONDS);
            }
            catch (final InterruptedException ex) {}
            finally {
                this.waitForIoMutex.unlock();
            }
        }
    }
    
    private void signalIoActivity() {
        this.waitForIoMutex.lock();
        try {
            this.waitForIOActivity.signalAll();
        }
        catch (final Exception ex) {}
        finally {
            this.waitForIoMutex.unlock();
        }
    }
    
    public void updateConnection() throws NoDiscordClientException, PipeAccessDenied {
        if (this.rpcConnection == null) {
            return;
        }
        if (!this.rpcConnection.isOpen()) {
            this.rpcConnection.open();
        }
        else {
            final JsonObject message = new JsonObject();
            while (this.rpcConnection.read(message)) {
                final String evtName = (message.has("evt") && !message.get("evt").isJsonNull()) ? message.get("evt").getAsString() : null;
                final String nonce = (message.has("nonce") && !message.get("nonce").isJsonNull()) ? message.get("nonce").getAsString() : null;
                if (nonce != null && evtName != null && evtName.equals("ERROR")) {
                    final JsonObject data = message.get("data").getAsJsonObject();
                    final int error = data.get("code").getAsInt();
                    this.lastErrorCode = ((error >= ErrorCode.values().length) ? ErrorCode.UNKNOWN : ErrorCode.values()[error]);
                    this.lastErrorMessage = (data.has("message") ? data.get("message").getAsString() : "");
                    this.gotErrorMessage.set(true);
                }
            }
            if (!this.presenceQueue.isEmpty()) {
                byte[] bytes;
                while ((bytes = this.presenceQueue.peek()) != null) {
                    if (!this.rpcConnection.write(bytes)) {
                        break;
                    }
                    this.presenceQueue.poll();
                }
            }
            if (!this.sendQueue.isEmpty()) {
                byte[] bytes;
                while ((bytes = this.sendQueue.poll()) != null) {
                    this.rpcConnection.write(bytes);
                }
            }
        }
    }
    
    private long getProcessId() {
        final String jvmName = ManagementFactory.getRuntimeMXBean().getName();
        final int index = jvmName.indexOf(64);
        if (index < 1) {
            return -1L;
        }
        try {
            return Long.parseLong(jvmName.substring(0, index));
        }
        catch (final NumberFormatException e) {
            return -1L;
        }
    }
    
    public void setOnReady(final Consumer<User> onReady) {
        this.onReady = onReady;
    }
    
    public void setOnDisconnected(final Consumer<ErrorInfo> onDisconnected) {
        this.onDisconnected = onDisconnected;
    }
    
    public void setOnErrored(final Consumer<ErrorInfo> onErrored) {
        this.onErrored = onErrored;
    }
    
    public void setDebugMode(final boolean debugMode) {
        this.isDebugMode = debugMode;
    }
    
    public boolean isDebugMode() {
        return this.isDebugMode;
    }
    
    public static class ErrorInfo
    {
        private final ErrorCode errorCode;
        private final String message;
        
        public ErrorInfo(final ErrorCode errorCode, final String message) {
            this.errorCode = errorCode;
            this.message = message;
        }
        
        public ErrorCode getErrorCode() {
            return this.errorCode;
        }
        
        public String getMessage() {
            return this.message;
        }
        
        @Override
        public String toString() {
            return "ErrorCode: " + this.errorCode + " - " + this.message;
        }
    }
}
