package org.newsclub.net.unix;

import java.io.IOException;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFUNIXServerSocketChannel.class */
public final class AFUNIXServerSocketChannel extends AFServerSocketChannel<AFUNIXSocketAddress> {
    AFUNIXServerSocketChannel(AFUNIXServerSocket socket) {
        super(socket, AFUNIXSelectorProvider.getInstance());
    }

    public static AFUNIXServerSocketChannel open() throws IOException {
        return AFUNIXServerSocket.newInstance().getChannel();
    }

    @Override // org.newsclub.net.unix.AFServerSocketChannel, java.nio.channels.ServerSocketChannel
    public AFUNIXSocketChannel accept() throws IOException {
        return (AFUNIXSocketChannel) super.accept();
    }
}
