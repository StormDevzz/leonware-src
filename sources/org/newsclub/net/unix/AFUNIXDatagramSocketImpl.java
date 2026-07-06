package org.newsclub.net.unix;

import java.io.FileDescriptor;
import java.io.IOException;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFUNIXDatagramSocketImpl.class */
final class AFUNIXDatagramSocketImpl extends AFDatagramSocketImpl<AFUNIXSocketAddress> {
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
