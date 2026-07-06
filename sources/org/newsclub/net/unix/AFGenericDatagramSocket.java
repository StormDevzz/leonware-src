package org.newsclub.net.unix;

import java.io.FileDescriptor;
import java.io.IOException;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFGenericDatagramSocket.class */
final class AFGenericDatagramSocket extends AFDatagramSocket<AFGenericSocketAddress> implements AFGenericSocketExtensions {
    AFGenericDatagramSocket(FileDescriptor fd) throws IOException {
        this(fd, AFSocketType.SOCK_DGRAM);
    }

    AFGenericDatagramSocket(FileDescriptor fd, AFSocketType socketType) throws IOException {
        super(new AFGenericDatagramSocketImpl(fd, socketType));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.newsclub.net.unix.AFDatagramSocket
    public AFGenericDatagramChannel newChannel() {
        return new AFGenericDatagramChannel(this);
    }

    public static AFGenericDatagramSocket newInstance() throws IOException {
        return (AFGenericDatagramSocket) newInstance(AFGenericDatagramSocket::new);
    }

    public static AFGenericDatagramSocket newInstance(AFSocketType socketType) throws IOException {
        return (AFGenericDatagramSocket) newInstance(fd -> {
            return new AFGenericDatagramSocket(fd, socketType);
        });
    }

    @Override // org.newsclub.net.unix.AFDatagramSocket, java.net.DatagramSocket
    public AFGenericDatagramChannel getChannel() {
        return (AFGenericDatagramChannel) super.getChannel();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.newsclub.net.unix.AFDatagramSocket
    public AFGenericSocketImplExtensions getImplExtensions() {
        return (AFGenericSocketImplExtensions) super.getImplExtensions();
    }

    @Override // org.newsclub.net.unix.AFDatagramSocket
    protected AFDatagramSocket<AFGenericSocketAddress> newDatagramSocketInstance() throws IOException {
        return new AFGenericDatagramSocket(null);
    }
}
