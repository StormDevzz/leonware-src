/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.IOException;
import org.newsclub.net.unix.AFGenericSelectorProvider;
import org.newsclub.net.unix.AFGenericServerSocket;
import org.newsclub.net.unix.AFGenericSocketAddress;
import org.newsclub.net.unix.AFGenericSocketChannel;
import org.newsclub.net.unix.AFServerSocketChannel;

final class AFGenericServerSocketChannel
extends AFServerSocketChannel<AFGenericSocketAddress> {
    AFGenericServerSocketChannel(AFGenericServerSocket socket) {
        super(socket, AFGenericSelectorProvider.getInstance());
    }

    public static AFGenericServerSocketChannel open() throws IOException {
        return AFGenericServerSocket.newInstance().getChannel();
    }

    @Override
    public AFGenericSocketChannel accept() throws IOException {
        return (AFGenericSocketChannel)super.accept();
    }
}

