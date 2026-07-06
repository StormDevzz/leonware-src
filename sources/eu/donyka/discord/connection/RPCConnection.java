package eu.donyka.discord.connection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import eu.donyka.discord.DiscordRPC;
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

/* JADX INFO: loaded from: leonware-0.0.3.jar:eu/donyka/discord/connection/RPCConnection.class */
public class RPCConnection {
    private static final Gson GSON = new GsonBuilder().serializeNulls().create();
    private final BaseConnection baseConnection;
    private final String appId;
    private final DiscordRPC rpcClient;
    private RPCState state = RPCState.DISCONNECTED;
    private Consumer<User> connectedCallback = null;
    private Consumer<ErrorInfo> disconnectedCallback = null;
    private ErrorCode lastErrorCode = ErrorCode.SUCCESS;
    private String lastErrorMessage = null;
    private final Lock writeLock = new ReentrantLock();

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

    private RPCConnection(String applicationId, DiscordRPC rpc) throws UnsupportedOsType {
        this.baseConnection = BaseConnection.createConnection(rpc);
        this.rpcClient = rpc;
        this.appId = applicationId;
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
        data.add("v", new JsonPrimitive((Number) 1));
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
            JsonObject data = new JsonObject();
            if (read(data)) {
                String cmd = (!data.has("cmd") || data.get("cmd").isJsonNull()) ? null : data.get("cmd").getAsString();
                String evt = (!data.has("evt") || data.get("evt").isJsonNull()) ? null : data.get("evt").getAsString();
                if (cmd != null && evt != null && cmd.equals("DISPATCH") && evt.equals("READY")) {
                    this.state = RPCState.CONNECTED;
                    JsonObject userData = data.get("data").getAsJsonObject().get("user").getAsJsonObject();
                    User user = (User) GSON.fromJson((JsonElement) userData, User.class);
                    if (this.connectedCallback != null) {
                        this.connectedCallback.accept(user);
                        return;
                    }
                    return;
                }
                return;
            }
            return;
        }
        MessageFrame messageFrame = new MessageFrame(OpCode.HANDSHAKE, writeHandshake());
        this.writeLock.lock();
        try {
            boolean success = this.baseConnection.write(messageFrame.write().array());
            this.writeLock.unlock();
            if (success) {
                this.state = RPCState.SENT_HANDSHAKE;
            } else {
                close();
            }
        } catch (Throwable th) {
            this.writeLock.unlock();
            throw th;
        }
    }

    private void close() {
        if (this.disconnectedCallback != null && (this.state == RPCState.CONNECTED || this.state == RPCState.SENT_HANDSHAKE)) {
            this.disconnectedCallback.accept(new ErrorInfo(this.lastErrorCode, this.lastErrorMessage));
        }
        BaseConnection.destroyConnection(this.baseConnection);
        this.state = RPCState.DISCONNECTED;
    }

    public boolean write(byte[] bytes) {
        MessageFrame messageFrame = new MessageFrame(OpCode.FRAME, new String(bytes, StandardCharsets.UTF_8));
        this.writeLock.lock();
        try {
            boolean success = this.baseConnection.write(messageFrame.write().array());
            this.writeLock.unlock();
            if (!success) {
                close();
                return false;
            }
            return true;
        } catch (Throwable th) {
            this.writeLock.unlock();
            throw th;
        }
    }

    /* JADX WARN: Incorrect condition in loop: B:10:0x0030 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean read(com.google.gson.JsonObject r5) {
        /*
            Method dump skipped, instruction units count: 464
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: eu.donyka.discord.connection.RPCConnection.read(com.google.gson.JsonObject):boolean");
    }

    /* JADX INFO: renamed from: eu.donyka.discord.connection.RPCConnection$1, reason: invalid class name */
    /* JADX INFO: loaded from: leonware-0.0.3.jar:eu/donyka/discord/connection/RPCConnection$1.class */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$eu$donyka$discord$enums$OpCode = new int[OpCode.values().length];

        static {
            try {
                $SwitchMap$eu$donyka$discord$enums$OpCode[OpCode.CLOSE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$eu$donyka$discord$enums$OpCode[OpCode.FRAME.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$eu$donyka$discord$enums$OpCode[OpCode.PING.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$eu$donyka$discord$enums$OpCode[OpCode.PONG.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$eu$donyka$discord$enums$OpCode[OpCode.HANDSHAKE.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:eu/donyka/discord/connection/RPCConnection$ErrorInfo.class */
    public static class ErrorInfo {
        private final ErrorCode errorCode;
        private final String message;

        @Generated
        public ErrorCode getErrorCode() {
            return this.errorCode;
        }

        @Generated
        public String getMessage() {
            return this.message;
        }

        public ErrorInfo(ErrorCode errorCode, String message) {
            this.errorCode = errorCode;
            this.message = message;
        }

        public String toString() {
            return "ErrorCode: " + this.errorCode + " - " + this.message;
        }
    }
}
