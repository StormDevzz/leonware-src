package eu.donyka.discord.connection.unix;

import java.io.File;
import java.io.IOException;
import org.newsclub.net.unix.AFUNIXSocket;
import org.newsclub.net.unix.AFUNIXSocketAddress;

/* JADX INFO: loaded from: leonware-0.0.3.jar:eu/donyka/discord/connection/unix/JUnixBackend.class */
public class JUnixBackend implements IUnixBackend {
    private AFUNIXSocket socket;

    @Override // eu.donyka.discord.connection.unix.IUnixBackend
    public void openPipe(String path) throws IOException {
        AFUNIXSocket socket = AFUNIXSocket.newInstance();
        try {
            socket.connect(AFUNIXSocketAddress.of(new File(path)));
            this.socket = socket;
        } catch (IOException e) {
            socket.close();
            throw e;
        }
    }

    @Override // eu.donyka.discord.connection.unix.IUnixBackend
    public void closePipe() throws IOException {
        if (this.socket != null) {
            this.socket.close();
        }
    }

    @Override // eu.donyka.discord.connection.unix.IUnixBackend
    public void write(byte[] bytes) throws IOException {
        if (this.socket == null || !this.socket.isConnected()) {
            return;
        }
        this.socket.getOutputStream().write(bytes);
    }

    @Override // eu.donyka.discord.connection.unix.IUnixBackend
    public int getAvailable() throws IOException {
        if (this.socket == null || !this.socket.isConnected()) {
            return -1;
        }
        return this.socket.getInputStream().available();
    }

    @Override // eu.donyka.discord.connection.unix.IUnixBackend
    public int read(byte[] bytes) throws IOException {
        if (this.socket == null || !this.socket.isConnected()) {
            return -1;
        }
        return this.socket.getInputStream().read(bytes);
    }

    @Override // eu.donyka.discord.connection.unix.IUnixBackend
    public boolean isConnected() {
        return this.socket != null && this.socket.isConnected();
    }
}
