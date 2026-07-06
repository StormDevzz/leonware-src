package org.newsclub.net.unix;

import java.io.FileDescriptor;
import java.io.IOException;
import java.net.SocketException;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFGenericSocket.class */
final class AFGenericSocket extends AFSocket<AFGenericSocketAddress> implements AFGenericSocketExtensions {
    private static AFGenericSocketImplExtensions staticExtensions = null;

    AFGenericSocket(FileDescriptor fdObj, AFSocketFactory<AFGenericSocketAddress> factory) throws SocketException {
        super(new AFGenericSocketImpl(fdObj), factory);
    }

    private static synchronized AFGenericSocketImplExtensions getStaticImplExtensions() throws IOException {
        if (staticExtensions == null) {
            AFGenericSocket socket = new AFGenericSocket(null, null);
            try {
                staticExtensions = (AFGenericSocketImplExtensions) socket.getImplExtensions();
                socket.close();
            } catch (Throwable th) {
                try {
                    socket.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }
        return staticExtensions;
    }

    public static boolean isSupported() {
        return AFSocket.isSupported();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.newsclub.net.unix.AFSocket
    public AFGenericSocketChannel newChannel() {
        return new AFGenericSocketChannel(this);
    }

    public static AFGenericSocket newInstance() throws IOException {
        return (AFGenericSocket) AFSocket.newInstance(AFGenericSocket::new, (AFGenericSocketFactory) null);
    }

    static AFGenericSocket newInstance(AFGenericSocketFactory factory) throws SocketException {
        return (AFGenericSocket) AFSocket.newInstance(AFGenericSocket::new, factory);
    }

    public static AFGenericSocket newStrictInstance() throws IOException {
        return (AFGenericSocket) AFSocket.newInstance(AFGenericSocket::new, (AFGenericSocketFactory) null);
    }

    public static AFGenericSocket connectTo(AFGenericSocketAddress addr) throws IOException {
        return (AFGenericSocket) AFSocket.connectTo(AFGenericSocket::new, addr);
    }

    @Override // org.newsclub.net.unix.AFSocket, java.net.Socket
    public AFGenericSocketChannel getChannel() {
        return (AFGenericSocketChannel) super.getChannel();
    }
}
