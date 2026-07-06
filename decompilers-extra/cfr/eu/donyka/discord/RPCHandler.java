/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package eu.donyka.discord;

import eu.donyka.discord.DiscordRPC;
import eu.donyka.discord.discord.RichPresence;
import eu.donyka.discord.discord.RichPresenceBuilder;
import eu.donyka.discord.models.User;
import java.util.function.Consumer;
import lombok.Generated;

public class RPCHandler {
    private static DiscordRPC rpc;
    private static boolean running;
    private static Consumer<User> onReady;
    private static Consumer<DiscordRPC.ErrorInfo> onDisconnected;
    private static Consumer<DiscordRPC.ErrorInfo> onErrored;

    public static void startup(String applicationId, boolean autoRegister) {
        RPCHandler.startup(applicationId, autoRegister, null);
    }

    public static void startup(String applicationId, boolean autoRegister, String optionalSteamId) {
        if (running) {
            RPCHandler.shutdown();
        }
        rpc = new DiscordRPC();
        rpc.setOnReady(user -> {
            if (onReady != null) {
                onReady.accept((User)user);
            }
        });
        rpc.setOnDisconnected(error -> {
            if (onDisconnected != null) {
                onDisconnected.accept((DiscordRPC.ErrorInfo)error);
            }
        });
        rpc.setOnErrored(error -> {
            if (onErrored != null) {
                onErrored.accept((DiscordRPC.ErrorInfo)error);
            }
        });
        try {
            rpc.init(applicationId, autoRegister, optionalSteamId);
            running = true;
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to initialize Discord RPC", e);
        }
    }

    public static void setOnReady(Consumer<User> onReady) {
        RPCHandler.onReady = onReady;
    }

    public static void setOnDisconnected(Consumer<DiscordRPC.ErrorInfo> onDisconnected) {
        RPCHandler.onDisconnected = onDisconnected;
    }

    public static void setOnErrored(Consumer<DiscordRPC.ErrorInfo> onErrored) {
        RPCHandler.onErrored = onErrored;
    }

    public static void updatePresence(RichPresence presence) {
        if (rpc != null && running) {
            rpc.updatePresence(presence);
        }
    }

    public static void updatePresence(Consumer<RichPresenceBuilder> builder) {
        RichPresenceBuilder presenceBuilder = RichPresenceBuilder.builder();
        builder.accept(presenceBuilder);
        RPCHandler.updatePresence(presenceBuilder.build());
    }

    public static void shutdown() {
        if (!running) {
            return;
        }
        running = false;
        if (rpc != null) {
            rpc.shutdown();
            rpc = null;
        }
        onReady = null;
        onDisconnected = null;
        onErrored = null;
    }

    @Generated
    public static boolean isRunning() {
        return running;
    }

    static {
        running = false;
    }
}

