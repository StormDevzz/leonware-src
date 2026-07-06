package org.newsclub.net.unix;

import java.io.IOException;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFUNIXProtocolFamily.class */
public enum AFUNIXProtocolFamily implements AFProtocolFamily {
    UNIX;

    @Override // org.newsclub.net.unix.AFProtocolFamily
    public AFDatagramChannel<?> openDatagramChannel() throws IOException {
        return AFUNIXDatagramChannel.open();
    }

    @Override // org.newsclub.net.unix.AFProtocolFamily
    public AFServerSocketChannel<?> openServerSocketChannel() throws IOException {
        return AFUNIXServerSocketChannel.open();
    }

    @Override // org.newsclub.net.unix.AFProtocolFamily
    public AFSocketChannel<?> openSocketChannel() throws IOException {
        return AFUNIXSocketChannel.open();
    }
}
