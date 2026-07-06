package eu.donyka.discord.connection;

import eu.donyka.discord.DiscordRPC;
import eu.donyka.discord.connection.unix.IUnixBackend;
import eu.donyka.discord.connection.unix.JUnixBackend;
import eu.donyka.discord.exceptions.NoDiscordClientException;
import eu.donyka.discord.exceptions.PipeAccessDenied;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

/* JADX INFO: loaded from: leonware-0.0.3.jar:eu/donyka/discord/connection/UnixConnection.class */
class UnixConnection extends BaseConnection {
    private IUnixBackend unixBackend;

    UnixConnection(DiscordRPC rpc) {
        super(rpc);
        this.unixBackend = new JUnixBackend();
    }

    @Override // eu.donyka.discord.connection.BaseConnection
    boolean isOpen() {
        return this.unixBackend != null && this.unixBackend.isConnected();
    }

    @Override // eu.donyka.discord.connection.BaseConnection
    boolean open() throws NoDiscordClientException, PipeAccessDenied {
        String pipeName = getTempPath() + "/discord-ipc-%s";
        if (isOpen()) {
            throw new IllegalStateException("Connection is already opened");
        }
        if (tryOpenConnection(pipeName)) {
            return true;
        }
        String nextPath = getAdditionalPaths();
        if (nextPath != null && !nextPath.isEmpty() && tryOpenConnection(nextPath + "/discord-ipc-%s")) {
            return true;
        }
        throw new NoDiscordClientException();
    }

    private boolean tryOpenConnection(String pipeName) {
        for (int i = 0; i < 10; i++) {
            try {
                File test = new File(String.format(pipeName, Integer.valueOf(i)));
                if (test.exists()) {
                    this.unixBackend.openPipe(String.format(pipeName, Integer.valueOf(i)));
                    return true;
                }
            } catch (Exception e) {
            }
        }
        return false;
    }

    @Override // eu.donyka.discord.connection.BaseConnection
    void close() {
        if (!isOpen()) {
            return;
        }
        try {
            this.unixBackend.closePipe();
        } catch (IOException e) {
        }
    }

    @Override // eu.donyka.discord.connection.BaseConnection
    boolean write(byte[] bytes) {
        if (!isOpen()) {
            return false;
        }
        try {
            this.unixBackend.write(bytes);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override // eu.donyka.discord.connection.BaseConnection
    boolean read(byte[] bytes, int length) {
        if (bytes == null || bytes.length == 0) {
            return bytes != null;
        }
        if (!isOpen()) {
            return false;
        }
        try {
            int available = this.unixBackend.getAvailable();
            if (available < length) {
                return false;
            }
            byte[] buf = new byte[length];
            int read = this.unixBackend.read(buf);
            if (read != length) {
                throw new IOException("Read less data than supplied. Expected: " + length + ". Got: " + read);
            }
            ByteBuffer buffer = ByteBuffer.wrap(buf);
            buffer.rewind();
            buffer.get(bytes, 0, length);
            return true;
        } catch (Exception e) {
            close();
            return false;
        }
    }

    @Override // eu.donyka.discord.connection.BaseConnection
    public void register(String applicationId, String command) {
        String home = System.getenv("HOME");
        if (home == null) {
            throw new RuntimeException("Unable to find user HOME directory");
        }
        if (command == null) {
            try {
                command = Files.readSymbolicLink(Paths.get("/proc/self/exe", new String[0])).toString();
            } catch (Exception ex) {
                throw new RuntimeException("Unable to get current exe path from /proc/self/exe", ex);
            }
        }
        String desktopFile = "[Desktop Entry]\nName=Game " + applicationId + "\nExec=" + command + " %%u\nType=Application\nNoDisplay=true\nCategories=Discord;Games;\nMimeType=x-scheme-handler/discord-" + applicationId + ";\n";
        String desktopFileName = "/discord-" + applicationId + ".desktop";
        String desktopFilePath = home + "/.local";
        if (!mkdir(desktopFilePath)) {
            throw new RuntimeException("Failed to create directory '" + desktopFilePath + "'");
        }
        String desktopFilePath2 = desktopFilePath + "/share";
        if (!mkdir(desktopFilePath2)) {
            throw new RuntimeException("Failed to create directory '" + desktopFilePath2 + "'");
        }
        String desktopFilePath3 = desktopFilePath2 + "/applications";
        if (!mkdir(desktopFilePath3)) {
            throw new RuntimeException("Failed to create directory '" + desktopFilePath3 + "'");
        }
        String desktopFilePath4 = desktopFilePath3 + desktopFileName;
        try {
            FileWriter fileWriter = new FileWriter(desktopFilePath4);
            Throwable th = null;
            try {
                try {
                    fileWriter.write(desktopFile);
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
                    String xdgMimeCommand = "xdg-mime default discord-" + applicationId + ".desktop x-scheme-handler/discord-" + applicationId;
                    try {
                        ProcessBuilder processBuilder = new ProcessBuilder(xdgMimeCommand.split(" "));
                        processBuilder.environment();
                        int result = processBuilder.start().waitFor();
                        if (result < 0) {
                            throw new Exception("xdg-mime returned " + result);
                        }
                    } catch (Exception ex2) {
                        throw new RuntimeException("Failed to register mime handler", ex2);
                    }
                } finally {
                }
            } finally {
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to write desktop info into '" + desktopFilePath4 + "'");
        }
    }

    @Override // eu.donyka.discord.connection.BaseConnection
    public void registerSteamGame(String applicationId, String steamId) {
        register(applicationId, "xdg-open steam://rungameid/" + steamId);
    }

    private String getTempPath() {
        String temp = System.getenv("XDG_RUNTIME_DIR");
        String temp2 = temp != null ? temp : System.getenv("TMPDIR");
        String temp3 = temp2 != null ? temp2 : System.getenv("TMP");
        String temp4 = temp3 != null ? temp3 : System.getenv("TEMP");
        return temp4 != null ? temp4 : "/tmp";
    }

    private String getAdditionalPaths() {
        String[] unixFolderPaths = {"/snap.discord", "/app/com.discordapp.Discord"};
        String path = getTempPath();
        for (String s : unixFolderPaths) {
            File f = new File(path, s);
            if (f.exists() && f.isDirectory() && f.list() != null && f.list().length > 0) {
                return f.getAbsolutePath();
            }
        }
        return null;
    }

    boolean mkdir(String path) {
        File file = new File(path);
        return (file.exists() && file.isDirectory()) || file.mkdir();
    }
}
