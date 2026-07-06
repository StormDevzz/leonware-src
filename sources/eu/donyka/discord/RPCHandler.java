package eu.donyka.discord;

import eu.donyka.discord.DiscordRPC;
import eu.donyka.discord.discord.RichPresence;
import eu.donyka.discord.discord.RichPresenceBuilder;
import eu.donyka.discord.models.User;
import java.util.function.Consumer;
import lombok.Generated;

/* JADX INFO: loaded from: leonware-0.0.3.jar:eu/donyka/discord/RPCHandler.class */
public class RPCHandler {
    private static DiscordRPC rpc;
    private static boolean running = false;
    private static Consumer<User> onReady;
    private static Consumer<DiscordRPC.ErrorInfo> onDisconnected;
    private static Consumer<DiscordRPC.ErrorInfo> onErrored;

    @Generated
    public static boolean isRunning() {
        return running;
    }

    public static void startup(String applicationId, boolean autoRegister) {
        startup(applicationId, autoRegister, null);
    }

    public static void startup(String applicationId, boolean autoRegister, String optionalSteamId) {
        if (running) {
            shutdown();
        }
        rpc = new DiscordRPC();
        rpc.setOnReady(user -> {
            if (onReady != null) {
                onReady.accept(user);
            }
        });
        rpc.setOnDisconnected(error -> {
            if (onDisconnected != null) {
                onDisconnected.accept(error);
            }
        });
        rpc.setOnErrored(error2 -> {
            if (onErrored != null) {
                onErrored.accept(error2);
            }
        });
        try {
            rpc.init(applicationId, autoRegister, optionalSteamId);
            running = true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Discord RPC", e);
        }
    }

    public static void setOnReady(Consumer<User> onReady2) {
        onReady = onReady2;
    }

    public static void setOnDisconnected(Consumer<DiscordRPC.ErrorInfo> onDisconnected2) {
        onDisconnected = onDisconnected2;
    }

    public static void setOnErrored(Consumer<DiscordRPC.ErrorInfo> onErrored2) {
        onErrored = onErrored2;
    }

    public static void updatePresence(RichPresence presence) {
        if (rpc != null && running) {
            rpc.updatePresence(presence);
        }
    }

    public static void updatePresence(Consumer<RichPresenceBuilder> builder) {
        RichPresenceBuilder presenceBuilder = RichPresenceBuilder.builder();
        builder.accept(presenceBuilder);
        updatePresence(presenceBuilder.build());
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
}
