// 
// Decompiled by Procyon v0.6.0
// 

package eu.donyka.discord;

import lombok.Generated;
import eu.donyka.discord.discord.RichPresenceBuilder;
import eu.donyka.discord.discord.RichPresence;
import eu.donyka.discord.models.User;
import java.util.function.Consumer;

public class RPCHandler
{
    private static DiscordRPC rpc;
    private static boolean running;
    private static Consumer<User> onReady;
    private static Consumer<DiscordRPC.ErrorInfo> onDisconnected;
    private static Consumer<DiscordRPC.ErrorInfo> onErrored;
    
    public static void startup(final String applicationId, final boolean autoRegister) {
        startup(applicationId, autoRegister, null);
    }
    
    public static void startup(final String applicationId, final boolean autoRegister, final String optionalSteamId) {
        if (RPCHandler.running) {
            shutdown();
        }
        (RPCHandler.rpc = new DiscordRPC()).setOnReady(user -> {
            if (RPCHandler.onReady != null) {
                RPCHandler.onReady.accept(user);
            }
            return;
        });
        RPCHandler.rpc.setOnDisconnected(error -> {
            if (RPCHandler.onDisconnected != null) {
                RPCHandler.onDisconnected.accept(error);
            }
            return;
        });
        RPCHandler.rpc.setOnErrored(error -> {
            if (RPCHandler.onErrored != null) {
                RPCHandler.onErrored.accept(error);
            }
            return;
        });
        try {
            RPCHandler.rpc.init(applicationId, autoRegister, optionalSteamId);
            RPCHandler.running = true;
        }
        catch (final Exception e) {
            throw new RuntimeException("Failed to initialize Discord RPC", e);
        }
    }
    
    public static void setOnReady(final Consumer<User> onReady) {
        RPCHandler.onReady = onReady;
    }
    
    public static void setOnDisconnected(final Consumer<DiscordRPC.ErrorInfo> onDisconnected) {
        RPCHandler.onDisconnected = onDisconnected;
    }
    
    public static void setOnErrored(final Consumer<DiscordRPC.ErrorInfo> onErrored) {
        RPCHandler.onErrored = onErrored;
    }
    
    public static void updatePresence(final RichPresence presence) {
        if (RPCHandler.rpc != null && RPCHandler.running) {
            RPCHandler.rpc.updatePresence(presence);
        }
    }
    
    public static void updatePresence(final Consumer<RichPresenceBuilder> builder) {
        final RichPresenceBuilder presenceBuilder = RichPresenceBuilder.builder();
        builder.accept(presenceBuilder);
        updatePresence(presenceBuilder.build());
    }
    
    public static void shutdown() {
        if (!RPCHandler.running) {
            return;
        }
        RPCHandler.running = false;
        if (RPCHandler.rpc != null) {
            RPCHandler.rpc.shutdown();
            RPCHandler.rpc = null;
        }
        RPCHandler.onReady = null;
        RPCHandler.onDisconnected = null;
        RPCHandler.onErrored = null;
    }
    
    @Generated
    public static boolean isRunning() {
        return RPCHandler.running;
    }
    
    static {
        RPCHandler.running = false;
    }
}
