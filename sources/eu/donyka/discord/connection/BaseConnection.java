package eu.donyka.discord.connection;

import eu.donyka.discord.DiscordRPC;
import eu.donyka.discord.exceptions.NoDiscordClientException;
import eu.donyka.discord.exceptions.PipeAccessDenied;
import eu.donyka.discord.exceptions.UnsupportedOsType;
import eu.donyka.discord.utils.OSDetector;
import lombok.Generated;

/* JADX INFO: loaded from: leonware-0.0.3.jar:eu/donyka/discord/connection/BaseConnection.class */
public abstract class BaseConnection {
    private final DiscordRPC rpc;

    abstract boolean isOpen();

    abstract boolean open() throws NoDiscordClientException, PipeAccessDenied;

    abstract void close();

    abstract boolean write(byte[] bArr);

    abstract boolean read(byte[] bArr, int i);

    public abstract void register(String str, String str2);

    public abstract void registerSteamGame(String str, String str2);

    @Generated
    public DiscordRPC getRpc() {
        return this.rpc;
    }

    protected BaseConnection(DiscordRPC rpc) {
        this.rpc = rpc;
    }

    public static BaseConnection createConnection(DiscordRPC rpc) throws UnsupportedOsType {
        OSDetector.OSType osType = OSDetector.INSTANCE.detectOs();
        if (osType.isWindows()) {
            return new WindowsConnection(rpc);
        }
        if (osType.isLinux()) {
            return new UnixConnection(rpc);
        }
        if (osType.isMac()) {
            return new MacOsConnection(rpc);
        }
        throw new UnsupportedOsType(osType.name());
    }

    public static void destroyConnection(BaseConnection connection) {
        if (connection == null) {
            return;
        }
        connection.close();
    }
}
