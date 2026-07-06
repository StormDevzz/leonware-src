// 
// Decompiled by Procyon v0.6.0
// 

package eu.donyka.discord.connection;

import com.google.gson.GsonBuilder;
import java.util.Map;
import lombok.Generated;
import java.nio.charset.StandardCharsets;
import eu.donyka.discord.exceptions.PipeAccessDenied;
import eu.donyka.discord.exceptions.NoDiscordClientException;
import eu.donyka.discord.models.MessageFrame;
import eu.donyka.discord.enums.OpCode;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonObject;
import eu.donyka.discord.exceptions.UnsupportedOsType;
import java.util.concurrent.locks.ReentrantLock;
import eu.donyka.discord.DiscordRPC;
import java.util.concurrent.locks.Lock;
import eu.donyka.discord.enums.RPCState;
import eu.donyka.discord.enums.ErrorCode;
import eu.donyka.discord.models.User;
import java.util.function.Consumer;
import com.google.gson.Gson;

public class RPCConnection
{
    private static final Gson GSON;
    private final BaseConnection baseConnection;
    private Consumer<User> connectedCallback;
    private Consumer<ErrorInfo> disconnectedCallback;
    private final String appId;
    private ErrorCode lastErrorCode;
    private String lastErrorMessage;
    private RPCState state;
    private final Lock writeLock;
    private final DiscordRPC rpcClient;
    
    private RPCConnection(final String applicationId, final DiscordRPC rpc) throws UnsupportedOsType {
        this.baseConnection = BaseConnection.createConnection(rpc);
        this.state = RPCState.DISCONNECTED;
        this.rpcClient = rpc;
        this.connectedCallback = null;
        this.disconnectedCallback = null;
        this.appId = applicationId;
        this.lastErrorCode = ErrorCode.SUCCESS;
        this.lastErrorMessage = null;
        this.writeLock = new ReentrantLock();
    }
    
    public static RPCConnection create(final String applicationId, final DiscordRPC rpc) throws UnsupportedOsType {
        return new RPCConnection(applicationId, rpc);
    }
    
    public static void destroy(final RPCConnection connection) {
        connection.close();
    }
    
    public boolean isOpen() {
        return this.state == RPCState.CONNECTED && this.baseConnection.isOpen();
    }
    
    private String writeHandshake() {
        final JsonObject data = new JsonObject();
        data.add("v", new JsonPrimitive(1));
        data.add("client_id", new JsonPrimitive(this.appId));
        return data.toString();
    }
    
    public void open() throws NoDiscordClientException, PipeAccessDenied {
        if (this.state == RPCState.CONNECTED) {
            return;
        }
        if (this.state == RPCState.DISCONNECTED && !this.baseConnection.open()) {
            return;
        }
        if (this.state == RPCState.SENT_HANDSHAKE) {
            final JsonObject data = new JsonObject();
            if (this.read(data)) {
                final String cmd = (data.has("cmd") && !data.get("cmd").isJsonNull()) ? data.get("cmd").getAsString() : null;
                final String evt = (data.has("evt") && !data.get("evt").isJsonNull()) ? data.get("evt").getAsString() : null;
                if (cmd != null && evt != null && cmd.equals("DISPATCH") && evt.equals("READY")) {
                    this.state = RPCState.CONNECTED;
                    final JsonObject userData = data.get("data").getAsJsonObject().get("user").getAsJsonObject();
                    final User user = RPCConnection.GSON.fromJson(userData, User.class);
                    if (this.connectedCallback != null) {
                        this.connectedCallback.accept(user);
                    }
                }
            }
            return;
        }
        final MessageFrame messageFrame = new MessageFrame(OpCode.HANDSHAKE, this.writeHandshake());
        this.writeLock.lock();
        boolean success;
        try {
            success = this.baseConnection.write(messageFrame.write().array());
        }
        finally {
            this.writeLock.unlock();
        }
        if (success) {
            this.state = RPCState.SENT_HANDSHAKE;
        }
        else {
            this.close();
        }
    }
    
    private void close() {
        if (this.disconnectedCallback != null && (this.state == RPCState.CONNECTED || this.state == RPCState.SENT_HANDSHAKE)) {
            this.disconnectedCallback.accept(new ErrorInfo(this.lastErrorCode, this.lastErrorMessage));
        }
        BaseConnection.destroyConnection(this.baseConnection);
        this.state = RPCState.DISCONNECTED;
    }
    
    public boolean write(final byte[] bytes) {
        final MessageFrame messageFrame = new MessageFrame(OpCode.FRAME, new String(bytes, StandardCharsets.UTF_8));
        this.writeLock.lock();
        boolean success;
        try {
            success = this.baseConnection.write(messageFrame.write().array());
        }
        finally {
            this.writeLock.unlock();
        }
        if (!success) {
            this.close();
            return false;
        }
        return true;
    }
    
    public boolean read(final JsonObject jsonObject) {
        if (this.state != RPCState.CONNECTED && this.state != RPCState.SENT_HANDSHAKE) {
            return false;
        }
        final MessageFrame messageFrame = new MessageFrame();
        while (true) {
            boolean didRead = this.baseConnection.read(messageFrame.getHeaderBuffer(), messageFrame.getHeaderBuffer().length);
            if (!didRead || !messageFrame.parseHeader()) {
                if (!this.baseConnection.isOpen()) {
                    this.lastErrorCode = ErrorCode.PIPE_CLOSED;
                    this.lastErrorMessage = "Pipe Closed";
                    this.close();
                }
                return false;
            }
            if (messageFrame.getLength() > 0) {
                didRead = this.baseConnection.read(messageFrame.getMessageBuffer(), messageFrame.getLength());
                if (!didRead || !messageFrame.parseMessage()) {
                    this.lastErrorCode = ErrorCode.READ_CORRUPT;
                    this.lastErrorMessage = "Partial data in frame";
                    this.close();
                    return false;
                }
            }
            final JsonObject object = RPCConnection.GSON.fromJson(messageFrame.getMessage(), JsonObject.class);
            switch (messageFrame.getOpCode()) {
                case CLOSE: {
                    object.entrySet().forEach(entry -> jsonObject.add(entry.getKey(), entry.getValue()));
                    int error = (object.has("code") && !object.get("code").isJsonNull()) ? object.get("code").getAsInt() : 0;
                    if (error == 1000) {
                        error = 4;
                    }
                    this.lastErrorCode = ((error >= ErrorCode.values().length) ? ErrorCode.UNKNOWN : ErrorCode.values()[error]);
                    this.lastErrorMessage = ((object.has("message") && !object.get("message").isJsonNull()) ? object.get("message").getAsString() : "");
                    this.close();
                    return false;
                }
                case FRAME: {
                    object.entrySet().forEach(entry -> jsonObject.add(entry.getKey(), entry.getValue()));
                    return true;
                }
                case PING: {
                    messageFrame.setOpCode(OpCode.PONG);
                    this.writeLock.lock();
                    boolean success;
                    try {
                        success = this.baseConnection.write(messageFrame.write().array());
                    }
                    finally {
                        this.writeLock.unlock();
                    }
                    if (!success) {
                        this.close();
                        continue;
                    }
                    continue;
                }
                case PONG: {
                    continue;
                }
                default: {
                    this.lastErrorCode = ErrorCode.READ_CORRUPT;
                    this.lastErrorMessage = "Bad IPC Frame";
                    this.close();
                    return false;
                }
            }
        }
    }
    
    @Generated
    public BaseConnection getBaseConnection() {
        return this.baseConnection;
    }
    
    @Generated
    public Consumer<User> getConnectedCallback() {
        return this.connectedCallback;
    }
    
    @Generated
    public Consumer<ErrorInfo> getDisconnectedCallback() {
        return this.disconnectedCallback;
    }
    
    @Generated
    public String getAppId() {
        return this.appId;
    }
    
    @Generated
    public ErrorCode getLastErrorCode() {
        return this.lastErrorCode;
    }
    
    @Generated
    public String getLastErrorMessage() {
        return this.lastErrorMessage;
    }
    
    @Generated
    public RPCState getState() {
        return this.state;
    }
    
    @Generated
    public Lock getWriteLock() {
        return this.writeLock;
    }
    
    @Generated
    public DiscordRPC getRpcClient() {
        return this.rpcClient;
    }
    
    @Generated
    public void setConnectedCallback(final Consumer<User> connectedCallback) {
        this.connectedCallback = connectedCallback;
    }
    
    @Generated
    public void setDisconnectedCallback(final Consumer<ErrorInfo> disconnectedCallback) {
        this.disconnectedCallback = disconnectedCallback;
    }
    
    @Generated
    public void setLastErrorCode(final ErrorCode lastErrorCode) {
        this.lastErrorCode = lastErrorCode;
    }
    
    @Generated
    public void setLastErrorMessage(final String lastErrorMessage) {
        this.lastErrorMessage = lastErrorMessage;
    }
    
    @Generated
    public void setState(final RPCState state) {
        this.state = state;
    }
    
    static {
        GSON = new GsonBuilder().serializeNulls().create();
    }
    
    public static class ErrorInfo
    {
        private final ErrorCode errorCode;
        private final String message;
        
        public ErrorInfo(final ErrorCode errorCode, final String message) {
            this.errorCode = errorCode;
            this.message = message;
        }
        
        @Override
        public String toString() {
            return "ErrorCode: " + this.errorCode + " - " + this.message;
        }
        
        @Generated
        public ErrorCode getErrorCode() {
            return this.errorCode;
        }
        
        @Generated
        public String getMessage() {
            return this.message;
        }
    }
}
