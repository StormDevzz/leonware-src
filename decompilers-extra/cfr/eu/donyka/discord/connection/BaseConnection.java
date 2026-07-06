/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package eu.donyka.discord.connection;

import eu.donyka.discord.DiscordRPC;
import eu.donyka.discord.connection.MacOsConnection;
import eu.donyka.discord.connection.UnixConnection;
import eu.donyka.discord.connection.WindowsConnection;
import eu.donyka.discord.exceptions.NoDiscordClientException;
import eu.donyka.discord.exceptions.PipeAccessDenied;
import eu.donyka.discord.exceptions.UnsupportedOsType;
import eu.donyka.discord.utils.OSDetector;
import lombok.Generated;

public abstract class BaseConnection {
    private final DiscordRPC rpc;

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

    abstract boolean isOpen();

    abstract boolean open() throws NoDiscordClientException, PipeAccessDenied;

    abstract void close();

    abstract boolean write(byte[] var1);

    abstract boolean read(byte[] var1, int var2);

    public abstract void register(String var1, String var2);

    public abstract void registerSteamGame(String var1, String var2);

    @Generated
    public DiscordRPC getRpc() {
        return this.rpc;
    }
}

