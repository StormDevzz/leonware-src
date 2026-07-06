package eu.donyka.discord.connection;

import eu.donyka.discord.DiscordRPC;
import eu.donyka.discord.exceptions.NoDiscordClientException;
import eu.donyka.discord.exceptions.PipeAccessDenied;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/* JADX INFO: loaded from: leonware-0.0.3.jar:eu/donyka/discord/connection/WindowsConnection.class */
class WindowsConnection extends BaseConnection {
    private RandomAccessFile pipe;
    private boolean opened;

    WindowsConnection(DiscordRPC rpc) {
        super(rpc);
        this.pipe = null;
        this.opened = false;
    }

    @Override // eu.donyka.discord.connection.BaseConnection
    boolean isOpen() {
        return this.opened;
    }

    @Override // eu.donyka.discord.connection.BaseConnection
    boolean open() throws NoDiscordClientException, PipeAccessDenied {
        if (isOpen()) {
            throw new IllegalStateException("Connection is already opened");
        }
        for (int i = 0; i < 10; i++) {
            String pipePath = String.format("\\\\.\\pipe\\discord-ipc-%d", Integer.valueOf(i));
            try {
                if (new File(pipePath).exists()) {
                    this.pipe = new RandomAccessFile(pipePath, "rw");
                    this.opened = true;
                    return true;
                }
            } catch (FileNotFoundException e) {
                if (e.getMessage() == null) {
                    continue;
                } else if (e.getMessage().toLowerCase().contains("access is denied")) {
                    throw new PipeAccessDenied("Cannot access pipe " + String.format("\\\\.\\pipe\\discord-ipc-%d", Integer.valueOf(i)) + " due to permission errors. Ensure discord is NOT running in administrator mode!");
                }
            } catch (SecurityException e2) {
                throw new PipeAccessDenied("Failed to open RPC Connection, with error Access Denied. Is Discord running in Administrator mode?");
            }
        }
        throw new NoDiscordClientException();
    }

    @Override // eu.donyka.discord.connection.BaseConnection
    void close() {
        if (!isOpen()) {
            return;
        }
        try {
            this.pipe.close();
        } catch (Exception e) {
        }
        this.opened = false;
    }

    @Override // eu.donyka.discord.connection.BaseConnection
    boolean write(byte[] bytes) {
        if (!isOpen()) {
            return false;
        }
        try {
            this.pipe.write(bytes);
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
            long available = this.pipe.length() - this.pipe.getFilePointer();
            if (available < length) {
                return false;
            }
            int read = this.pipe.read(bytes, 0, length);
            if (read != length) {
                throw new IOException("Read less data than supplied. Expected: " + length + ". Got: " + read);
            }
            return true;
        } catch (IOException e) {
            close();
            return false;
        }
    }

    @Override // eu.donyka.discord.connection.BaseConnection
    public void register(String applicationId, String command) {
        String openCommand;
        String javaLibraryPath = System.getProperty("java.home");
        File javaExeFile = new File(javaLibraryPath.split(";")[0] + "/bin/java.exe");
        File javawExeFile = new File(javaLibraryPath.split(";")[0] + "/bin/javaw.exe");
        String javaExePath = javaExeFile.exists() ? javaExeFile.getAbsolutePath() : javawExeFile.exists() ? javawExeFile.getAbsolutePath() : null;
        if (javaExePath == null) {
            throw new RuntimeException("Unable to find java path");
        }
        if (command != null) {
            openCommand = command;
        } else {
            openCommand = javaExePath;
        }
        String protocolName = "discord-" + applicationId;
        String protocolDescription = "URL:Run game " + applicationId + " protocol";
        String keyName = "Software\\Classes\\" + protocolName;
        String iconKeyName = keyName + "\\DefaultIcon";
        String commandKeyName = keyName + "\\DefaultIcon";
        try {
            WinRegistry.createKey(keyName);
            WinRegistry.writeStringValue(keyName, "", protocolDescription);
            WinRegistry.writeStringValue(keyName, "URL Protocol", "\u0000");
            WinRegistry.createKey(iconKeyName);
            WinRegistry.writeStringValue(iconKeyName, "", javaExePath);
            WinRegistry.createKey(commandKeyName);
            WinRegistry.writeStringValue(commandKeyName, "", openCommand);
        } catch (Exception ex) {
            throw new RuntimeException("Unable to modify Discord registry keys", ex);
        }
    }

    @Override // eu.donyka.discord.connection.BaseConnection
    public void registerSteamGame(String applicationId, String steamId) {
        try {
            String steamPath = WinRegistry.readString();
            if (steamPath == null) {
                throw new RuntimeException("Steam exe path not found");
            }
            String command = "\"" + steamPath.replaceAll("/", "\\") + "\" steam://rungameid/" + steamId;
            register(applicationId, command);
        } catch (Exception ex) {
            throw new RuntimeException("Unable to register Steam game", ex);
        }
    }
}
