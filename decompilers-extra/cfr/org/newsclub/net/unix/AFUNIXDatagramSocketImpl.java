/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.FileDescriptor;
import java.io.IOException;
import org.newsclub.net.unix.AFDatagramSocketImpl;
import org.newsclub.net.unix.AFSocketType;
import org.newsclub.net.unix.AFUNIXSocketAddress;
import org.newsclub.net.unix.AFUNIXSocketCredentials;
import org.newsclub.net.unix.NativeUnixSocket;

final class AFUNIXDatagramSocketImpl
extends AFDatagramSocketImpl<AFUNIXSocketAddress> {
    AFUNIXDatagramSocketImpl(FileDescriptor fd) throws IOException {
        this(fd, AFSocketType.SOCK_DGRAM);
    }

    AFUNIXDatagramSocketImpl(FileDescriptor fd, AFSocketType socketType) throws IOException {
        super(AFUNIXSocketAddress.AF_UNIX, fd, socketType);
    }

    AFUNIXSocketCredentials getPeerCredentials() throws IOException {
        return NativeUnixSocket.peerCredentials(this.fd, new AFUNIXSocketCredentials());
    }
}

