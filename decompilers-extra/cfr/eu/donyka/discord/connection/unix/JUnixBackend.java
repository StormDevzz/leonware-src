/*
 * Decompiled with CFR 0.152.
 */
package eu.donyka.discord.connection.unix;

import eu.donyka.discord.connection.unix.IUnixBackend;
import java.io.File;
import java.io.IOException;
import org.newsclub.net.unix.AFUNIXSocket;
import org.newsclub.net.unix.AFUNIXSocketAddress;

public class JUnixBackend
implements IUnixBackend {
    private AFUNIXSocket socket;

    @Override
    public void openPipe(String path) throws IOException {
        AFUNIXSocket socket = AFUNIXSocket.newInstance();
        try {
            socket.connect(AFUNIXSocketAddress.of(new File(path)));
            this.socket = socket;
        }
        catch (IOException e) {
            socket.close();
            throw e;
        }
    }

    @Override
    public void closePipe() throws IOException {
        if (this.socket != null) {
            this.socket.close();
        }
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        if (this.socket == null || !this.socket.isConnected()) {
            return;
        }
        this.socket.getOutputStream().write(bytes);
    }

    @Override
    public int getAvailable() throws IOException {
        if (this.socket == null || !this.socket.isConnected()) {
            return -1;
        }
        return this.socket.getInputStream().available();
    }

    @Override
    public int read(byte[] bytes) throws IOException {
        if (this.socket == null || !this.socket.isConnected()) {
            return -1;
        }
        return this.socket.getInputStream().read(bytes);
    }

    @Override
    public boolean isConnected() {
        return this.socket != null && this.socket.isConnected();
    }
}

