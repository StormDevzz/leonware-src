/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.FileDescriptor;
import java.io.IOException;
import java.net.SocketException;
import org.newsclub.net.unix.AFGenericSocketAddress;
import org.newsclub.net.unix.AFGenericSocketChannel;
import org.newsclub.net.unix.AFGenericSocketExtensions;
import org.newsclub.net.unix.AFGenericSocketFactory;
import org.newsclub.net.unix.AFGenericSocketImpl;
import org.newsclub.net.unix.AFGenericSocketImplExtensions;
import org.newsclub.net.unix.AFSocket;
import org.newsclub.net.unix.AFSocketFactory;

final class AFGenericSocket
extends AFSocket<AFGenericSocketAddress>
implements AFGenericSocketExtensions {
    private static AFGenericSocketImplExtensions staticExtensions = null;

    AFGenericSocket(FileDescriptor fdObj, AFSocketFactory<AFGenericSocketAddress> factory) throws SocketException {
        super(new AFGenericSocketImpl(fdObj), factory);
    }

    private static synchronized AFGenericSocketImplExtensions getStaticImplExtensions() throws IOException {
        if (staticExtensions == null) {
            try (AFGenericSocket socket = new AFGenericSocket(null, null);){
                staticExtensions = (AFGenericSocketImplExtensions)socket.getImplExtensions();
            }
        }
        return staticExtensions;
    }

    public static boolean isSupported() {
        return AFSocket.isSupported();
    }

    protected AFGenericSocketChannel newChannel() {
        return new AFGenericSocketChannel(this);
    }

    public static AFGenericSocket newInstance() throws IOException {
        return (AFGenericSocket)AFSocket.newInstance(AFGenericSocket::new, null);
    }

    static AFGenericSocket newInstance(AFGenericSocketFactory factory) throws SocketException {
        return (AFGenericSocket)AFSocket.newInstance(AFGenericSocket::new, factory);
    }

    public static AFGenericSocket newStrictInstance() throws IOException {
        return (AFGenericSocket)AFSocket.newInstance(AFGenericSocket::new, null);
    }

    public static AFGenericSocket connectTo(AFGenericSocketAddress addr) throws IOException {
        return (AFGenericSocket)AFSocket.connectTo(AFGenericSocket::new, addr);
    }

    @Override
    public AFGenericSocketChannel getChannel() {
        return (AFGenericSocketChannel)super.getChannel();
    }
}

