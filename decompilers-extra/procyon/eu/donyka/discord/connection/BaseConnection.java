// 
// Decompiled by Procyon v0.6.0
// 

package eu.donyka.discord.connection;

import lombok.Generated;
import eu.donyka.discord.exceptions.PipeAccessDenied;
import eu.donyka.discord.exceptions.NoDiscordClientException;
import eu.donyka.discord.exceptions.UnsupportedOsType;
import eu.donyka.discord.utils.OSDetector;
import eu.donyka.discord.DiscordRPC;

public abstract class BaseConnection
{
    private final DiscordRPC rpc;
    
    protected BaseConnection(final DiscordRPC rpc) {
        this.rpc = rpc;
    }
    
    public static BaseConnection createConnection(final DiscordRPC rpc) throws UnsupportedOsType {
        final OSDetector.OSType osType = OSDetector.INSTANCE.detectOs();
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
    
    public static void destroyConnection(final BaseConnection connection) {
        if (connection == null) {
            return;
        }
        connection.close();
    }
    
    abstract boolean isOpen();
    
    abstract boolean open() throws NoDiscordClientException, PipeAccessDenied;
    
    abstract void close();
    
    abstract boolean write(final byte[] p0);
    
    abstract boolean read(final byte[] p0, final int p1);
    
    public abstract void register(final String p0, final String p1);
    
    public abstract void registerSteamGame(final String p0, final String p1);
    
    @Generated
    public DiscordRPC getRpc() {
        return this.rpc;
    }
}
