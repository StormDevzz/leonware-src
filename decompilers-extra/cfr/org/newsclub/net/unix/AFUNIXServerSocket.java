/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.net.SocketException;
import java.nio.file.Path;
import org.newsclub.net.unix.AFServerSocket;
import org.newsclub.net.unix.AFSocketImpl;
import org.newsclub.net.unix.AFUNIXServerSocketChannel;
import org.newsclub.net.unix.AFUNIXSocket;
import org.newsclub.net.unix.AFUNIXSocketAddress;
import org.newsclub.net.unix.AFUNIXSocketImpl;

public final class AFUNIXServerSocket
extends AFServerSocket<AFUNIXSocketAddress> {
    protected AFUNIXServerSocket() throws IOException {
    }

    AFUNIXServerSocket(FileDescriptor fdObj) throws IOException {
        super(fdObj);
    }

    protected AFUNIXServerSocketChannel newChannel() {
        return new AFUNIXServerSocketChannel(this);
    }

    @Override
    public AFUNIXServerSocketChannel getChannel() {
        return (AFUNIXServerSocketChannel)super.getChannel();
    }

    public static AFUNIXServerSocket newInstance() throws IOException {
        return (AFUNIXServerSocket)AFServerSocket.newInstance(AFUNIXServerSocket::new);
    }

    static AFUNIXServerSocket newInstance(FileDescriptor fdObj, int localPort, int remotePort) throws IOException {
        return (AFUNIXServerSocket)AFServerSocket.newInstance(AFUNIXServerSocket::new, fdObj, localPort, remotePort);
    }

    public static AFUNIXServerSocket bindOn(AFUNIXSocketAddress addr) throws IOException {
        return (AFUNIXServerSocket)AFServerSocket.bindOn(AFUNIXServerSocket::new, addr);
    }

    public static AFUNIXServerSocket bindOn(AFUNIXSocketAddress addr, boolean deleteOnClose) throws IOException {
        return (AFUNIXServerSocket)AFServerSocket.bindOn(AFUNIXServerSocket::new, addr, deleteOnClose);
    }

    public static AFUNIXServerSocket bindOn(File path, boolean deleteOnClose) throws IOException {
        return AFUNIXServerSocket.bindOn(path.toPath(), deleteOnClose);
    }

    public static AFUNIXServerSocket bindOn(Path path, boolean deleteOnClose) throws IOException {
        return (AFUNIXServerSocket)AFServerSocket.bindOn(AFUNIXServerSocket::new, AFUNIXSocketAddress.of(path), deleteOnClose);
    }

    public static AFUNIXServerSocket forceBindOn(AFUNIXSocketAddress forceAddr) throws IOException {
        return (AFUNIXServerSocket)AFServerSocket.forceBindOn(AFUNIXServerSocket::new, forceAddr);
    }

    @Override
    protected AFSocketImpl<AFUNIXSocketAddress> newImpl(FileDescriptor fdObj) throws SocketException {
        return new AFUNIXSocketImpl(fdObj);
    }

    protected AFUNIXSocket newSocketInstance() throws IOException {
        return AFUNIXSocket.newInstance();
    }

    @Override
    public AFUNIXSocket accept() throws IOException {
        return (AFUNIXSocket)super.accept();
    }
}

