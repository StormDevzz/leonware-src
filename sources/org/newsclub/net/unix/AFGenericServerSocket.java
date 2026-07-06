package org.newsclub.net.unix;

import java.io.FileDescriptor;
import java.io.IOException;
import java.net.SocketException;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFGenericServerSocket.class */
final class AFGenericServerSocket extends AFServerSocket<AFGenericSocketAddress> {
    AFGenericServerSocket(FileDescriptor fdObj) throws IOException {
        super(fdObj);
    }

    @Override // org.newsclub.net.unix.AFServerSocket
    protected AFServerSocketChannel<AFGenericSocketAddress> newChannel() {
        return new AFGenericServerSocketChannel(this);
    }

    @Override // org.newsclub.net.unix.AFServerSocket, java.net.ServerSocket
    public AFGenericServerSocketChannel getChannel() {
        return (AFGenericServerSocketChannel) super.getChannel();
    }

    public static AFGenericServerSocket newInstance() throws IOException {
        return (AFGenericServerSocket) AFServerSocket.newInstance(AFGenericServerSocket::new);
    }

    static AFGenericServerSocket newInstance(FileDescriptor fdObj, int localPort, int remotePort) throws IOException {
        return (AFGenericServerSocket) AFServerSocket.newInstance(AFGenericServerSocket::new, fdObj, localPort, remotePort);
    }

    public static AFGenericServerSocket bindOn(AFGenericSocketAddress addr) throws IOException {
        return (AFGenericServerSocket) AFServerSocket.bindOn(AFGenericServerSocket::new, addr);
    }

    public static AFGenericServerSocket bindOn(AFGenericSocketAddress addr, boolean deleteOnClose) throws IOException {
        return (AFGenericServerSocket) AFServerSocket.bindOn(AFGenericServerSocket::new, addr, deleteOnClose);
    }

    public static AFGenericServerSocket forceBindOn(AFGenericSocketAddress forceAddr) throws IOException {
        return (AFGenericServerSocket) AFServerSocket.forceBindOn(AFGenericServerSocket::new, forceAddr);
    }

    @Override // org.newsclub.net.unix.AFServerSocket
    protected AFSocketImpl<AFGenericSocketAddress> newImpl(FileDescriptor fdObj) throws SocketException {
        return new AFGenericSocketImpl(fdObj);
    }

    @Override // org.newsclub.net.unix.AFServerSocket
    protected AFSocket<AFGenericSocketAddress> newSocketInstance() throws IOException {
        return AFGenericSocket.newInstance();
    }

    @Override // org.newsclub.net.unix.AFServerSocket, java.net.ServerSocket
    public AFGenericSocket accept() throws IOException {
        return (AFGenericSocket) super.accept();
    }
}
