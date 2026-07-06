package org.newsclub.net.unix;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.net.SocketException;
import java.nio.file.Path;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFUNIXServerSocket.class */
public final class AFUNIXServerSocket extends AFServerSocket<AFUNIXSocketAddress> {
    protected AFUNIXServerSocket() throws IOException {
    }

    AFUNIXServerSocket(FileDescriptor fdObj) throws IOException {
        super(fdObj);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.newsclub.net.unix.AFServerSocket
    public AFUNIXServerSocketChannel newChannel() {
        return new AFUNIXServerSocketChannel(this);
    }

    @Override // org.newsclub.net.unix.AFServerSocket, java.net.ServerSocket
    public AFUNIXServerSocketChannel getChannel() {
        return (AFUNIXServerSocketChannel) super.getChannel();
    }

    public static AFUNIXServerSocket newInstance() throws IOException {
        return (AFUNIXServerSocket) AFServerSocket.newInstance(AFUNIXServerSocket::new);
    }

    static AFUNIXServerSocket newInstance(FileDescriptor fdObj, int localPort, int remotePort) throws IOException {
        return (AFUNIXServerSocket) AFServerSocket.newInstance(AFUNIXServerSocket::new, fdObj, localPort, remotePort);
    }

    public static AFUNIXServerSocket bindOn(AFUNIXSocketAddress addr) throws IOException {
        return (AFUNIXServerSocket) AFServerSocket.bindOn(AFUNIXServerSocket::new, addr);
    }

    public static AFUNIXServerSocket bindOn(AFUNIXSocketAddress addr, boolean deleteOnClose) throws IOException {
        return (AFUNIXServerSocket) AFServerSocket.bindOn(AFUNIXServerSocket::new, addr, deleteOnClose);
    }

    public static AFUNIXServerSocket bindOn(File path, boolean deleteOnClose) throws IOException {
        return bindOn(path.toPath(), deleteOnClose);
    }

    public static AFUNIXServerSocket bindOn(Path path, boolean deleteOnClose) throws IOException {
        return (AFUNIXServerSocket) AFServerSocket.bindOn(AFUNIXServerSocket::new, AFUNIXSocketAddress.of(path), deleteOnClose);
    }

    public static AFUNIXServerSocket forceBindOn(AFUNIXSocketAddress forceAddr) throws IOException {
        return (AFUNIXServerSocket) AFServerSocket.forceBindOn(AFUNIXServerSocket::new, forceAddr);
    }

    @Override // org.newsclub.net.unix.AFServerSocket
    protected AFSocketImpl<AFUNIXSocketAddress> newImpl(FileDescriptor fdObj) throws SocketException {
        return new AFUNIXSocketImpl(fdObj);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.newsclub.net.unix.AFServerSocket
    public AFUNIXSocket newSocketInstance() throws IOException {
        return AFUNIXSocket.newInstance();
    }

    @Override // org.newsclub.net.unix.AFServerSocket, java.net.ServerSocket
    public AFUNIXSocket accept() throws IOException {
        return (AFUNIXSocket) super.accept();
    }
}
