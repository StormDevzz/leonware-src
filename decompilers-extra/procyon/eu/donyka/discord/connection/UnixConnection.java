// 
// Decompiled by Procyon v0.6.0
// 

package eu.donyka.discord.connection;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.io.File;
import eu.donyka.discord.exceptions.PipeAccessDenied;
import eu.donyka.discord.exceptions.NoDiscordClientException;
import eu.donyka.discord.connection.unix.JUnixBackend;
import eu.donyka.discord.DiscordRPC;
import eu.donyka.discord.connection.unix.IUnixBackend;

class UnixConnection extends BaseConnection
{
    private IUnixBackend unixBackend;
    
    UnixConnection(final DiscordRPC rpc) {
        super(rpc);
        this.unixBackend = new JUnixBackend();
    }
    
    @Override
    boolean isOpen() {
        return this.unixBackend != null && this.unixBackend.isConnected();
    }
    
    @Override
    boolean open() throws NoDiscordClientException, PipeAccessDenied {
        final String pipeName = this.getTempPath() + "/discord-ipc-%s";
        if (this.isOpen()) {
            throw new IllegalStateException("Connection is already opened");
        }
        if (this.tryOpenConnection(pipeName)) {
            return true;
        }
        final String nextPath = this.getAdditionalPaths();
        if (nextPath != null && !nextPath.isEmpty() && this.tryOpenConnection(nextPath + "/discord-ipc-%s")) {
            return true;
        }
        throw new NoDiscordClientException();
    }
    
    private boolean tryOpenConnection(final String pipeName) {
        for (int i = 0; i < 10; ++i) {
            try {
                final File test = new File(String.format(pipeName, i));
                if (test.exists()) {
                    this.unixBackend.openPipe(String.format(pipeName, i));
                    return true;
                }
            }
            catch (final Exception ex) {}
        }
        return false;
    }
    
    @Override
    void close() {
        if (!this.isOpen()) {
            return;
        }
        try {
            this.unixBackend.closePipe();
        }
        catch (final IOException ex) {}
    }
    
    @Override
    boolean write(final byte[] bytes) {
        if (!this.isOpen()) {
            return false;
        }
        try {
            this.unixBackend.write(bytes);
            return true;
        }
        catch (final Exception e) {
            return false;
        }
    }
    
    @Override
    boolean read(final byte[] bytes, final int length) {
        if (bytes == null || bytes.length == 0) {
            return bytes != null;
        }
        if (!this.isOpen()) {
            return false;
        }
        try {
            final int available = this.unixBackend.getAvailable();
            if (available < length) {
                return false;
            }
            final byte[] buf = new byte[length];
            final int read = this.unixBackend.read(buf);
            if (read != length) {
                throw new IOException("Read less data than supplied. Expected: " + length + ". Got: " + read);
            }
            final ByteBuffer buffer = ByteBuffer.wrap(buf);
            buffer.rewind();
            buffer.get(bytes, 0, length);
            return true;
        }
        catch (final Exception e) {
            this.close();
            return false;
        }
    }
    
    @Override
    public void register(final String applicationId, String command) {
        final String home = System.getenv("HOME");
        if (home == null) {
            throw new RuntimeException("Unable to find user HOME directory");
        }
        if (command == null) {
            try {
                command = Files.readSymbolicLink(Paths.get("/proc/self/exe", new String[0])).toString();
            }
            catch (final Exception ex) {
                throw new RuntimeException("Unable to get current exe path from /proc/self/exe", ex);
            }
        }
        final String desktopFile = "[Desktop Entry]\nName=Game " + applicationId + "\nExec=" + command + " %%u\nType=Application\nNoDisplay=true\nCategories=Discord;Games;\nMimeType=x-scheme-handler/discord-" + applicationId + ";\n";
        final String desktopFileName = "/discord-" + applicationId + ".desktop";
        String desktopFilePath = home + "/.local";
        if (!this.mkdir(desktopFilePath)) {
            throw new RuntimeException("Failed to create directory '" + desktopFilePath + "'");
        }
        desktopFilePath += "/share";
        if (!this.mkdir(desktopFilePath)) {
            throw new RuntimeException("Failed to create directory '" + desktopFilePath + "'");
        }
        desktopFilePath += "/applications";
        if (!this.mkdir(desktopFilePath)) {
            throw new RuntimeException("Failed to create directory '" + desktopFilePath + "'");
        }
        desktopFilePath += desktopFileName;
        try (final FileWriter fileWriter = new FileWriter(desktopFilePath)) {
            fileWriter.write(desktopFile);
        }
        catch (final Exception ex2) {
            throw new RuntimeException("Failed to write desktop info into '" + desktopFilePath + "'");
        }
        final String xdgMimeCommand = "xdg-mime default discord-" + applicationId + ".desktop x-scheme-handler/discord-" + applicationId;
        try {
            final ProcessBuilder processBuilder = new ProcessBuilder(xdgMimeCommand.split(" "));
            processBuilder.environment();
            final int result = processBuilder.start().waitFor();
            if (result < 0) {
                throw new Exception("xdg-mime returned " + result);
            }
        }
        catch (final Exception ex3) {
            throw new RuntimeException("Failed to register mime handler", ex3);
        }
    }
    
    @Override
    public void registerSteamGame(final String applicationId, final String steamId) {
        this.register(applicationId, "xdg-open steam://rungameid/" + steamId);
    }
    
    private String getTempPath() {
        String temp = System.getenv("XDG_RUNTIME_DIR");
        temp = ((temp != null) ? temp : System.getenv("TMPDIR"));
        temp = ((temp != null) ? temp : System.getenv("TMP"));
        temp = ((temp != null) ? temp : System.getenv("TEMP"));
        temp = ((temp != null) ? temp : "/tmp");
        return temp;
    }
    
    private String getAdditionalPaths() {
        final String[] unixFolderPaths = { "/snap.discord", "/app/com.discordapp.Discord" };
        final String path = this.getTempPath();
        for (final String s : unixFolderPaths) {
            final File f = new File(path, s);
            if (f.exists() && f.isDirectory() && f.list() != null && f.list().length > 0) {
                return f.getAbsolutePath();
            }
        }
        return null;
    }
    
    boolean mkdir(final String path) {
        final File file = new File(path);
        return (file.exists() && file.isDirectory()) || file.mkdir();
    }
}
