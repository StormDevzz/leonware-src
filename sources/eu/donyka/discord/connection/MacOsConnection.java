package eu.donyka.discord.connection;

import eu.donyka.discord.DiscordRPC;
import java.io.File;
import java.io.FileWriter;

/* JADX INFO: loaded from: leonware-0.0.3.jar:eu/donyka/discord/connection/MacOsConnection.class */
class MacOsConnection extends UnixConnection {
    MacOsConnection(DiscordRPC rpc) {
        super(rpc);
    }

    @Override // eu.donyka.discord.connection.UnixConnection, eu.donyka.discord.connection.BaseConnection
    public void register(String applicationId, String command) {
        if (command != null) {
            try {
                registerCommand(applicationId, command);
            } catch (Exception ex) {
                throw new RuntimeException("Failed to register command", ex);
            }
        }
    }

    @Override // eu.donyka.discord.connection.UnixConnection, eu.donyka.discord.connection.BaseConnection
    public void registerSteamGame(String applicationId, String steamId) {
        register(applicationId, "steam://rungameid/" + steamId);
    }

    private void registerCommand(String applicationId, String command) {
        String home = System.getenv("HOME");
        if (home == null) {
            throw new RuntimeException("Unable to find user HOME directory");
        }
        String path = home + "/Library/Application Support/discord";
        if (!mkdir(path)) {
            throw new RuntimeException("Failed to create directory '" + path + "'");
        }
        String path2 = path + "/games";
        if (!mkdir(path2)) {
            throw new RuntimeException("Failed to create directory '" + path2 + "'");
        }
        String path3 = path2 + "/" + applicationId + ".json";
        try {
            FileWriter fileWriter = new FileWriter(path3);
            Throwable th = null;
            try {
                try {
                    fileWriter.write("{\"command\": \"" + command + "\"}");
                    if (fileWriter != null) {
                        if (0 != 0) {
                            try {
                                fileWriter.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        } else {
                            fileWriter.close();
                        }
                    }
                } finally {
                }
            } finally {
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to write game info into '" + path3 + "'");
        }
    }

    @Override // eu.donyka.discord.connection.UnixConnection
    boolean mkdir(String path) {
        File file = new File(path);
        return (file.exists() && file.isDirectory()) || file.mkdir();
    }
}
