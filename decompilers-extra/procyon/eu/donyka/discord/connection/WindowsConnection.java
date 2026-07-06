// 
// Decompiled by Procyon v0.6.0
// 

package eu.donyka.discord.connection;

import java.io.IOException;
import eu.donyka.discord.exceptions.NoDiscordClientException;
import java.io.FileNotFoundException;
import eu.donyka.discord.exceptions.PipeAccessDenied;
import java.io.File;
import eu.donyka.discord.DiscordRPC;
import java.io.RandomAccessFile;

class WindowsConnection extends BaseConnection
{
    private RandomAccessFile pipe;
    private boolean opened;
    
    WindowsConnection(final DiscordRPC rpc) {
        super(rpc);
        this.pipe = null;
        this.opened = false;
    }
    
    @Override
    boolean isOpen() {
        return this.opened;
    }
    
    @Override
    boolean open() throws NoDiscordClientException, PipeAccessDenied {
        final String pipeName = "\\\\.\\pipe\\discord-ipc-%d";
        if (this.isOpen()) {
            throw new IllegalStateException("Connection is already opened");
        }
        for (int i = 0; i < 10; ++i) {
            final String pipePath = String.format(pipeName, i);
            try {
                if (new File(pipePath).exists()) {
                    this.pipe = new RandomAccessFile(pipePath, "rw");
                    return this.opened = true;
                }
            }
            catch (final FileNotFoundException e) {
                if (e.getMessage() != null && e.getMessage().toLowerCase().contains("access is denied")) {
                    throw new PipeAccessDenied("Cannot access pipe " + String.format(pipeName, i) + " due to permission errors. Ensure discord is NOT running in administrator mode!");
                }
            }
            catch (final SecurityException sec) {
                throw new PipeAccessDenied("Failed to open RPC Connection, with error Access Denied. Is Discord running in Administrator mode?");
            }
        }
        throw new NoDiscordClientException();
    }
    
    @Override
    void close() {
        if (!this.isOpen()) {
            return;
        }
        try {
            this.pipe.close();
        }
        catch (final Exception ex) {}
        this.opened = false;
    }
    
    @Override
    boolean write(final byte[] bytes) {
        if (!this.isOpen()) {
            return false;
        }
        try {
            this.pipe.write(bytes);
            return true;
        }
        catch (final Exception ignored) {
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
            final long available = this.pipe.length() - this.pipe.getFilePointer();
            if (available < length) {
                return false;
            }
            final int read = this.pipe.read(bytes, 0, length);
            if (read != length) {
                throw new IOException("Read less data than supplied. Expected: " + length + ". Got: " + read);
            }
            return true;
        }
        catch (final IOException e) {
            this.close();
            return false;
        }
    }
    
    @Override
    public void register(final String applicationId, final String command) {
        final String javaLibraryPath = System.getProperty("java.home");
        final File javaExeFile = new File(javaLibraryPath.split(";")[0] + "/bin/java.exe");
        final File javawExeFile = new File(javaLibraryPath.split(";")[0] + "/bin/javaw.exe");
        final String javaExePath = javaExeFile.exists() ? javaExeFile.getAbsolutePath() : (javawExeFile.exists() ? javawExeFile.getAbsolutePath() : null);
        if (javaExePath == null) {
            throw new RuntimeException("Unable to find java path");
        }
        String openCommand;
        if (command != null) {
            openCommand = command;
        }
        else {
            openCommand = javaExePath;
        }
        final String protocolName = "discord-" + applicationId;
        final String protocolDescription = "URL:Run game " + applicationId + " protocol";
        final String keyName = "Software\\Classes\\" + protocolName;
        final String iconKeyName = keyName + "\\DefaultIcon";
        final String commandKeyName = keyName + "\\DefaultIcon";
        try {
            WinRegistry.createKey(keyName);
            WinRegistry.writeStringValue(keyName, "", protocolDescription);
            WinRegistry.writeStringValue(keyName, "URL Protocol", "\u0000");
            WinRegistry.createKey(iconKeyName);
            WinRegistry.writeStringValue(iconKeyName, "", javaExePath);
            WinRegistry.createKey(commandKeyName);
            WinRegistry.writeStringValue(commandKeyName, "", openCommand);
        }
        catch (final Exception ex) {
            throw new RuntimeException("Unable to modify Discord registry keys", ex);
        }
    }
    
    @Override
    public void registerSteamGame(final String applicationId, final String steamId) {
        try {
            String steamPath = WinRegistry.readString();
            if (steamPath == null) {
                throw new RuntimeException("Steam exe path not found");
            }
            steamPath = steamPath.replaceAll("/", "\\");
            final String command = "\"" + steamPath + "\" steam://rungameid/" + steamId;
            this.register(applicationId, command);
        }
        catch (final Exception ex) {
            throw new RuntimeException("Unable to register Steam game", ex);
        }
    }
}
