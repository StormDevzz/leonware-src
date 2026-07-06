package org.newsclub.net.unix;

import java.io.IOException;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFGenericServerSocketChannel.class */
final class AFGenericServerSocketChannel extends AFServerSocketChannel<AFGenericSocketAddress> {
    AFGenericServerSocketChannel(AFGenericServerSocket socket) {
        super(socket, AFGenericSelectorProvider.getInstance());
    }

    public static AFGenericServerSocketChannel open() throws IOException {
        return AFGenericServerSocket.newInstance().getChannel();
    }

    @Override // org.newsclub.net.unix.AFServerSocketChannel, java.nio.channels.ServerSocketChannel
    public AFGenericSocketChannel accept() throws IOException {
        return (AFGenericSocketChannel) super.accept();
    }
}
