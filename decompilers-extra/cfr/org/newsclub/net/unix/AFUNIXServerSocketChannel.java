/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.IOException;
import org.newsclub.net.unix.AFServerSocketChannel;
import org.newsclub.net.unix.AFUNIXSelectorProvider;
import org.newsclub.net.unix.AFUNIXServerSocket;
import org.newsclub.net.unix.AFUNIXSocketAddress;
import org.newsclub.net.unix.AFUNIXSocketChannel;

public final class AFUNIXServerSocketChannel
extends AFServerSocketChannel<AFUNIXSocketAddress> {
    AFUNIXServerSocketChannel(AFUNIXServerSocket socket) {
        super(socket, AFUNIXSelectorProvider.getInstance());
    }

    public static AFUNIXServerSocketChannel open() throws IOException {
        return AFUNIXServerSocket.newInstance().getChannel();
    }

    @Override
    public AFUNIXSocketChannel accept() throws IOException {
        return (AFUNIXSocketChannel)super.accept();
    }
}

