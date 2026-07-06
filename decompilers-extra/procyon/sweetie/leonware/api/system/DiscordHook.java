// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.system;

import eu.donyka.discord.models.User;
import eu.donyka.discord.DiscordRPC;
import lombok.Generated;
import eu.donyka.discord.RPCHandler;
import sweetie.leonware.api.system.backend.ClientInfo;
import eu.donyka.discord.discord.RichPresence;
import sweetie.leonware.api.system.interfaces.QuickImports;

public final class DiscordHook implements QuickImports
{
    public static void startRPC() {
        try {
            RPCHandler.setOnReady(user -> {
                final RichPresence presence = RichPresence.builder().details("Version: " + ClientInfo.VERSION).largeImageKey("ava").largeImageText(user.getUsername()).build();
                RPCHandler.updatePresence(presence);
                return;
            });
            RPCHandler.setOnDisconnected(error -> System.out.println("RPC Disconnected: " + String.valueOf(error)));
            RPCHandler.setOnErrored(error -> System.out.println("RPC Errored: " + String.valueOf(error)));
            RPCHandler.startup("1492237117873721545", false);
        }
        catch (final Throwable $ex) {
            throw $ex;
        }
    }
    
    public static void stopRPC() {
        RPCHandler.shutdown();
    }
    
    @Generated
    private DiscordHook() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
