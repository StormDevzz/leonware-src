/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.FileDescriptor;
import java.io.IOException;
import org.newsclub.net.unix.AFDatagramSocket;
import org.newsclub.net.unix.AFGenericDatagramChannel;
import org.newsclub.net.unix.AFGenericDatagramSocketImpl;
import org.newsclub.net.unix.AFGenericSocketAddress;
import org.newsclub.net.unix.AFGenericSocketExtensions;
import org.newsclub.net.unix.AFGenericSocketImplExtensions;
import org.newsclub.net.unix.AFSocketType;

final class AFGenericDatagramSocket
extends AFDatagramSocket<AFGenericSocketAddress>
implements AFGenericSocketExtensions {
    AFGenericDatagramSocket(FileDescriptor fd) throws IOException {
        this(fd, AFSocketType.SOCK_DGRAM);
    }

    AFGenericDatagramSocket(FileDescriptor fd, AFSocketType socketType) throws IOException {
        super(new AFGenericDatagramSocketImpl(fd, socketType));
    }

    protected AFGenericDatagramChannel newChannel() {
        return new AFGenericDatagramChannel(this);
    }

    public static AFGenericDatagramSocket newInstance() throws IOException {
        return (AFGenericDatagramSocket)AFGenericDatagramSocket.newInstance(AFGenericDatagramSocket::new);
    }

    public static AFGenericDatagramSocket newInstance(AFSocketType socketType) throws IOException {
        return (AFGenericDatagramSocket)AFGenericDatagramSocket.newInstance((FileDescriptor fd) -> new AFGenericDatagramSocket(fd, socketType));
    }

    @Override
    public AFGenericDatagramChannel getChannel() {
        return (AFGenericDatagramChannel)super.getChannel();
    }

    protected AFGenericSocketImplExtensions getImplExtensions() {
        return (AFGenericSocketImplExtensions)super.getImplExtensions();
    }

    @Override
    protected AFDatagramSocket<AFGenericSocketAddress> newDatagramSocketInstance() throws IOException {
        return new AFGenericDatagramSocket(null);
    }
}

