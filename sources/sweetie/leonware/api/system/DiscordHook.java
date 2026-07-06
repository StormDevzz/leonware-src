package sweetie.leonware.api.system;

import eu.donyka.discord.RPCHandler;
import eu.donyka.discord.discord.RichPresence;
import lombok.Generated;
import sweetie.leonware.api.system.backend.ClientInfo;
import sweetie.leonware.api.system.interfaces.QuickImports;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/system/DiscordHook.class */
public final class DiscordHook implements QuickImports {
    @Generated
    private DiscordHook() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void startRPC() {
        RPCHandler.setOnReady(user -> {
            RichPresence presence = RichPresence.builder().details("Version: " + ClientInfo.VERSION).largeImageKey("ava").largeImageText(user.getUsername()).build();
            RPCHandler.updatePresence(presence);
        });
        RPCHandler.setOnDisconnected(error -> {
            System.out.println("RPC Disconnected: " + String.valueOf(error));
        });
        RPCHandler.setOnErrored(error2 -> {
            System.out.println("RPC Errored: " + String.valueOf(error2));
        });
        RPCHandler.startup("1492237117873721545", false);
    }

    public static void stopRPC() {
        RPCHandler.shutdown();
    }
}
