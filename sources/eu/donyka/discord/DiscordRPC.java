package eu.donyka.discord;

import com.google.gson.JsonObject;
import eu.donyka.discord.connection.RPCConnection;
import eu.donyka.discord.discord.RichPresence;
import eu.donyka.discord.enums.ErrorCode;
import eu.donyka.discord.exceptions.NoDiscordClientException;
import eu.donyka.discord.exceptions.PipeAccessDenied;
import eu.donyka.discord.exceptions.UnsupportedOsType;
import eu.donyka.discord.models.User;
import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/* JADX INFO: loaded from: leonware-0.0.3.jar:eu/donyka/discord/DiscordRPC.class */
public class DiscordRPC {
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

    public DiscordRPC(boolean disableIoThread) {
        this.isDebugMode = false;
        this.disableIoThread = disableIoThread;
        this.pid = -1L;
        this.nonce = -1L;
        this.onReady = null;
        this.onDisconnected = null;
        this.onErrored = null;
        this.rpcConnection = null;
        this.wasJustConnected = new AtomicBoolean(false);
        this.connectedUser = new AtomicReference<>();
        this.wasJustDisconnected = new AtomicBoolean(false);
        this.gotErrorMessage = new AtomicBoolean(false);
        this.sendQueue = new ConcurrentLinkedQueue();
        this.presenceQueue = new ConcurrentLinkedQueue();
        this.keepRunning = new AtomicBoolean(true);
        this.waitForIoMutex = new ReentrantLock(true);
        this.waitForIOActivity = this.waitForIoMutex.newCondition();
        this.ioThread = null;
    }

    public void init(String applicationId, boolean autoRegister) throws UnsupportedOsType, PipeAccessDenied {
        init(applicationId, autoRegister, null);
    }

    public void init(String applicationId, boolean autoRegister, String optionalSteamId) throws UnsupportedOsType, PipeAccessDenied {
        if (this.rpcConnection != null) {
            return;
        }
        this.pid = getProcessId();
        try {
            this.rpcConnection = RPCConnection.create(applicationId, this);
            if (autoRegister) {
                if (optionalSteamId != null && !optionalSteamId.isEmpty()) {
                    this.rpcConnection.getBaseConnection().registerSteamGame(applicationId, optionalSteamId);
                } else {
                    this.rpcConnection.getBaseConnection().register(applicationId, null);
                }
            }
            this.rpcConnection.setConnectedCallback(user -> {
                this.wasJustConnected.set(true);
                this.connectedUser.set(user);
                if (this.onReady != null) {
                    this.onReady.accept(user);
                }
            });
            this.rpcConnection.setDisconnectedCallback(errorInfo -> {
                this.lastDisconnectErrorCode = errorInfo.getErrorCode();
                this.lastDisconnectErrorMessage = errorInfo.getMessage();
                this.wasJustDisconnected.set(true);
            });
            if (!this.disableIoThread) {
                this.keepRunning.set(true);
                this.ioThread = new Thread(() -> {
                    try {
                        discordRpcIo();
                    } catch (NoDiscordClientException | PipeAccessDenied e) {
                    }
                });
                this.ioThread.setDaemon(true);
                this.ioThread.start();
            }
        } catch (UnsupportedOsType e) {
            throw e;
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
            signalIoActivity();
            try {
                if (this.ioThread != null) {
                    this.ioThread.join();
                }
            } catch (Exception e) {
            }
        }
        RPCConnection.destroy(this.rpcConnection);
        this.rpcConnection = null;
    }

    public void updatePresence(RichPresence richPresence) {
        if (richPresence == null) {
            richPresence = RichPresence.builder().build();
        }
        long j = this.pid;
        long j2 = this.nonce;
        this.nonce = j2 + 1;
        JsonObject data = richPresence.toJson(j, j2);
        this.presenceQueue.offer(data.toString().getBytes(StandardCharsets.UTF_8));
        signalIoActivity();
    }

    public void runCallbacks() {
        if (this.rpcConnection == null) {
            return;
        }
        boolean wasDisconnected = this.wasJustDisconnected.getAndSet(false);
        boolean isConnected = this.rpcConnection.isOpen();
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
                updateConnection();
            } catch (NoDiscordClientException | PipeAccessDenied e) {
            }
            runCallbacks();
            this.waitForIoMutex.lock();
            try {
                this.waitForIOActivity.await(500L, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e2) {
            } finally {
                this.waitForIoMutex.unlock();
            }
        }
    }

    private void signalIoActivity() {
        this.waitForIoMutex.lock();
        try {
            this.waitForIOActivity.signalAll();
        } catch (Exception e) {
        } finally {
            this.waitForIoMutex.unlock();
        }
    }

    public void updateConnection() throws NoDiscordClientException, PipeAccessDenied {
        if (this.rpcConnection == null) {
            return;
        }
        if (!this.rpcConnection.isOpen()) {
            this.rpcConnection.open();
            return;
        }
        JsonObject message = new JsonObject();
        while (this.rpcConnection.read(message)) {
            String evtName = (!message.has("evt") || message.get("evt").isJsonNull()) ? null : message.get("evt").getAsString();
            String nonce = (!message.has("nonce") || message.get("nonce").isJsonNull()) ? null : message.get("nonce").getAsString();
            if (nonce != null && evtName != null && evtName.equals("ERROR")) {
                JsonObject data = message.get("data").getAsJsonObject();
                int error = data.get("code").getAsInt();
                this.lastErrorCode = error >= ErrorCode.values().length ? ErrorCode.UNKNOWN : ErrorCode.values()[error];
                this.lastErrorMessage = data.has("message") ? data.get("message").getAsString() : "";
                this.gotErrorMessage.set(true);
            }
        }
        if (!this.presenceQueue.isEmpty()) {
            while (true) {
                byte[] bytes = this.presenceQueue.peek();
                if (bytes == null || !this.rpcConnection.write(bytes)) {
                    break;
                } else {
                    this.presenceQueue.poll();
                }
            }
        }
        if (this.sendQueue.isEmpty()) {
            return;
        }
        while (true) {
            byte[] bytes2 = this.sendQueue.poll();
            if (bytes2 != null) {
                this.rpcConnection.write(bytes2);
            } else {
                return;
            }
        }
    }

    private long getProcessId() {
        String jvmName = ManagementFactory.getRuntimeMXBean().getName();
        int index = jvmName.indexOf(64);
        if (index < 1) {
            return -1L;
        }
        try {
            return Long.parseLong(jvmName.substring(0, index));
        } catch (NumberFormatException e) {
            return -1L;
        }
    }

    public void setOnReady(Consumer<User> onReady) {
        this.onReady = onReady;
    }

    public void setOnDisconnected(Consumer<ErrorInfo> onDisconnected) {
        this.onDisconnected = onDisconnected;
    }

    public void setOnErrored(Consumer<ErrorInfo> onErrored) {
        this.onErrored = onErrored;
    }

    public void setDebugMode(boolean debugMode) {
        this.isDebugMode = debugMode;
    }

    public boolean isDebugMode() {
        return this.isDebugMode;
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:eu/donyka/discord/DiscordRPC$ErrorInfo.class */
    public static class ErrorInfo {
        private final ErrorCode errorCode;
        private final String message;

        public ErrorInfo(ErrorCode errorCode, String message) {
            this.errorCode = errorCode;
            this.message = message;
        }

        public ErrorCode getErrorCode() {
            return this.errorCode;
        }

        public String getMessage() {
            return this.message;
        }

        public String toString() {
            return "ErrorCode: " + this.errorCode + " - " + this.message;
        }
    }
}
