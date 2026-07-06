/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.FileDescriptor;
import java.io.IOException;
import java.net.SocketException;
import org.newsclub.net.unix.AFGenericServerSocketChannel;
import org.newsclub.net.unix.AFGenericSocket;
import org.newsclub.net.unix.AFGenericSocketAddress;
import org.newsclub.net.unix.AFGenericSocketImpl;
import org.newsclub.net.unix.AFServerSocket;
import org.newsclub.net.unix.AFServerSocketChannel;
import org.newsclub.net.unix.AFSocket;
import org.newsclub.net.unix.AFSocketImpl;

final class AFGenericServerSocket
extends AFServerSocket<AFGenericSocketAddress> {
    AFGenericServerSocket(FileDescriptor fdObj) throws IOException {
        super(fdObj);
    }

    @Override
    protected AFServerSocketChannel<AFGenericSocketAddress> newChannel() {
        return new AFGenericServerSocketChannel(this);
    }

    @Override
    public AFGenericServerSocketChannel getChannel() {
        return (AFGenericServerSocketChannel)super.getChannel();
    }

    public static AFGenericServerSocket newInstance() throws IOException {
        return (AFGenericServerSocket)AFServerSocket.newInstance(AFGenericServerSocket::new);
    }

    static AFGenericServerSocket newInstance(FileDescriptor fdObj, int localPort, int remotePort) throws IOException {
        return (AFGenericServerSocket)AFServerSocket.newInstance(AFGenericServerSocket::new, fdObj, localPort, remotePort);
    }

    public static AFGenericServerSocket bindOn(AFGenericSocketAddress addr) throws IOException {
        return (AFGenericServerSocket)AFServerSocket.bindOn(AFGenericServerSocket::new, addr);
    }

    public static AFGenericServerSocket bindOn(AFGenericSocketAddress addr, boolean deleteOnClose) throws IOException {
        return (AFGenericServerSocket)AFServerSocket.bindOn(AFGenericServerSocket::new, addr, deleteOnClose);
    }

    public static AFGenericServerSocket forceBindOn(AFGenericSocketAddress forceAddr) throws IOException {
        return (AFGenericServerSocket)AFServerSocket.forceBindOn(AFGenericServerSocket::new, forceAddr);
    }

    @Override
    protected AFSocketImpl<AFGenericSocketAddress> newImpl(FileDescriptor fdObj) throws SocketException {
        return new AFGenericSocketImpl(fdObj);
    }

    @Override
    protected AFSocket<AFGenericSocketAddress> newSocketInstance() throws IOException {
        return AFGenericSocket.newInstance();
    }

    @Override
    public AFGenericSocket accept() throws IOException {
        return (AFGenericSocket)super.accept();
    }
}

