/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package eu.donyka.discord.connection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import eu.donyka.discord.DiscordRPC;
import eu.donyka.discord.connection.BaseConnection;
import eu.donyka.discord.enums.ErrorCode;
import eu.donyka.discord.enums.OpCode;
import eu.donyka.discord.enums.RPCState;
import eu.donyka.discord.exceptions.NoDiscordClientException;
import eu.donyka.discord.exceptions.PipeAccessDenied;
import eu.donyka.discord.exceptions.UnsupportedOsType;
import eu.donyka.discord.models.MessageFrame;
import eu.donyka.discord.models.User;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import lombok.Generated;

public class RPCConnection {
    private static final Gson GSON = new GsonBuilder().serializeNulls().create();
    private final BaseConnection baseConnection;
    private Consumer<User> connectedCallback;
    private Consumer<ErrorInfo> disconnectedCallback;
    private final String appId;
    private ErrorCode lastErrorCode;
    private String lastErrorMessage;
    private RPCState state;
    private final Lock writeLock;
    private final DiscordRPC rpcClient;

    private RPCConnection(String applicationId, DiscordRPC rpc) throws UnsupportedOsType {
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

    public static RPCConnection create(String applicationId, DiscordRPC rpc) throws UnsupportedOsType {
        return new RPCConnection(applicationId, rpc);
    }

    public static void destroy(RPCConnection connection) {
        connection.close();
    }

    public boolean isOpen() {
        return this.state == RPCState.CONNECTED && this.baseConnection.isOpen();
    }

    private String writeHandshake() {
        JsonObject data = new JsonObject();
        data.add("v", new JsonPrimitive(1));
        data.add("client_id", new JsonPrimitive(this.appId));
        return data.toString();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void open() throws NoDiscordClientException, PipeAccessDenied {
        boolean success;
        if (this.state == RPCState.CONNECTED) {
            return;
        }
        if (this.state == RPCState.DISCONNECTED && !this.baseConnection.open()) {
            return;
        }
        if (this.state == RPCState.SENT_HANDSHAKE) {
            JsonObject data = new JsonObject();
            if (this.read(data)) {
                String evt;
                String cmd = data.has("cmd") && !data.get("cmd").isJsonNull() ? data.get("cmd").getAsString() : null;
                String string = evt = data.has("evt") && !data.get("evt").isJsonNull() ? data.get("evt").getAsString() : null;
                if (cmd != null && evt != null && cmd.equals("DISPATCH") && evt.equals("READY")) {
                    this.state = RPCState.CONNECTED;
                    JsonObject userData = data.get("data").getAsJsonObject().get("user").getAsJsonObject();
                    User user = GSON.fromJson((JsonElement)userData, User.class);
                    if (this.connectedCallback != null) {
                        this.connectedCallback.accept(user);
                    }
                }
            }
            return;
        }
        MessageFrame messageFrame = new MessageFrame(OpCode.HANDSHAKE, this.writeHandshake());
        this.writeLock.lock();
        try {
            success = this.baseConnection.write(messageFrame.write().array());
        }
        finally {
            this.writeLock.unlock();
        }
        if (success) {
            this.state = RPCState.SENT_HANDSHAKE;
        } else {
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

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean write(byte[] bytes) {
        boolean success;
        MessageFrame messageFrame = new MessageFrame(OpCode.FRAME, new String(bytes, StandardCharsets.UTF_8));
        this.writeLock.lock();
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

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean read(JsonObject jsonObject) {
        if (this.state != RPCState.CONNECTED && this.state != RPCState.SENT_HANDSHAKE) {
            return false;
        }
        MessageFrame messageFrame = new MessageFrame();
        block9: while (true) {
            boolean didRead;
            if (!(didRead = this.baseConnection.read(messageFrame.getHeaderBuffer(), messageFrame.getHeaderBuffer().length)) || !messageFrame.parseHeader()) {
                if (!this.baseConnection.isOpen()) {
                    this.lastErrorCode = ErrorCode.PIPE_CLOSED;
                    this.lastErrorMessage = "Pipe Closed";
                    this.close();
                }
                return false;
            }
            if (!(messageFrame.getLength() <= 0 || (didRead = this.baseConnection.read(messageFrame.getMessageBuffer(), messageFrame.getLength())) && messageFrame.parseMessage())) {
                this.lastErrorCode = ErrorCode.READ_CORRUPT;
                this.lastErrorMessage = "Partial data in frame";
                this.close();
                return false;
            }
            JsonObject object = GSON.fromJson(messageFrame.getMessage(), JsonObject.class);
            switch (messageFrame.getOpCode()) {
                case CLOSE: {
                    int error;
                    object.entrySet().forEach(entry -> jsonObject.add((String)entry.getKey(), (JsonElement)entry.getValue()));
                    int n = error = object.has("code") && !object.get("code").isJsonNull() ? object.get("code").getAsInt() : 0;
                    if (error == 1000) {
                        error = 4;
                    }
                    this.lastErrorCode = error >= ErrorCode.values().length ? ErrorCode.UNKNOWN : ErrorCode.values()[error];
                    this.lastErrorMessage = object.has("message") && !object.get("message").isJsonNull() ? object.get("message").getAsString() : "";
                    this.close();
                    return false;
                }
                case FRAME: {
                    object.entrySet().forEach(entry -> jsonObject.add((String)entry.getKey(), (JsonElement)entry.getValue()));
                    return true;
                }
                case PING: {
                    boolean success;
                    messageFrame.setOpCode(OpCode.PONG);
                    this.writeLock.lock();
                    try {
                        success = this.baseConnection.write(messageFrame.write().array());
                    }
                    finally {
                        this.writeLock.unlock();
                    }
                    if (success) continue block9;
                    this.close();
                    continue block9;
                }
                case PONG: {
                    continue block9;
                }
            }
            break;
        }
        this.lastErrorCode = ErrorCode.READ_CORRUPT;
        this.lastErrorMessage = "Bad IPC Frame";
        this.close();
        return false;
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
    public void setConnectedCallback(Consumer<User> connectedCallback) {
        this.connectedCallback = connectedCallback;
    }

    @Generated
    public void setDisconnectedCallback(Consumer<ErrorInfo> disconnectedCallback) {
        this.disconnectedCallback = disconnectedCallback;
    }

    @Generated
    public void setLastErrorCode(ErrorCode lastErrorCode) {
        this.lastErrorCode = lastErrorCode;
    }

    @Generated
    public void setLastErrorMessage(String lastErrorMessage) {
        this.lastErrorMessage = lastErrorMessage;
    }

    @Generated
    public void setState(RPCState state) {
        this.state = state;
    }

    public static class ErrorInfo {
        private final ErrorCode errorCode;
        private final String message;

        public ErrorInfo(ErrorCode errorCode, String message) {
            this.errorCode = errorCode;
            this.message = message;
        }

        public String toString() {
            return "ErrorCode: " + (Object)((Object)this.errorCode) + " - " + this.message;
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

