// 
// Decompiled by Procyon v0.6.0
// 

package eu.donyka.discord.connection.unix;

import java.io.IOException;
import java.net.SocketAddress;
import org.newsclub.net.unix.AFUNIXSocketAddress;
import java.io.File;
import org.newsclub.net.unix.AFUNIXSocket;

public class JUnixBackend implements IUnixBackend
{
    private AFUNIXSocket socket;
    
    @Override
    public void openPipe(final String path) throws IOException {
        final AFUNIXSocket socket = AFUNIXSocket.newInstance();
        try {
            socket.connect(AFUNIXSocketAddress.of(new File(path)));
            this.socket = socket;
        }
        catch (final IOException e) {
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
    public void write(final byte[] bytes) throws IOException {
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
    public int read(final byte[] bytes) throws IOException {
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
