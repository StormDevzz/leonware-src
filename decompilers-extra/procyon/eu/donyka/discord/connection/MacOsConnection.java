// 
// Decompiled by Procyon v0.6.0
// 

package eu.donyka.discord.connection;

import java.io.File;
import java.io.FileWriter;
import eu.donyka.discord.DiscordRPC;

class MacOsConnection extends UnixConnection
{
    MacOsConnection(final DiscordRPC rpc) {
        super(rpc);
    }
    
    @Override
    public void register(final String applicationId, final String command) {
        try {
            if (command != null) {
                this.registerCommand(applicationId, command);
            }
        }
        catch (final Exception ex) {
            throw new RuntimeException("Failed to register command", ex);
        }
    }
    
    @Override
    public void registerSteamGame(final String applicationId, final String steamId) {
        this.register(applicationId, "steam://rungameid/" + steamId);
    }
    
    private void registerCommand(final String applicationId, final String command) {
        final String home = System.getenv("HOME");
        if (home == null) {
            throw new RuntimeException("Unable to find user HOME directory");
        }
        String path = home + "/Library/Application Support/discord";
        if (!this.mkdir(path)) {
            throw new RuntimeException("Failed to create directory '" + path + "'");
        }
        path += "/games";
        if (!this.mkdir(path)) {
            throw new RuntimeException("Failed to create directory '" + path + "'");
        }
        path = path + "/" + applicationId + ".json";
        try (final FileWriter fileWriter = new FileWriter(path)) {
            fileWriter.write("{\"command\": \"" + command + "\"}");
        }
        catch (final Exception ex) {
            throw new RuntimeException("Failed to write game info into '" + path + "'");
        }
    }
    
    @Override
    boolean mkdir(final String path) {
        final File file = new File(path);
        return (file.exists() && file.isDirectory()) || file.mkdir();
    }
}
