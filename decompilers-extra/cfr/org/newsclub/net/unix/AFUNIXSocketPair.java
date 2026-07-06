/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.IOException;
import org.newsclub.net.unix.AFSocketPair;
import org.newsclub.net.unix.AFSocketType;
import org.newsclub.net.unix.AFSomeSocket;
import org.newsclub.net.unix.AFUNIXDatagramChannel;
import org.newsclub.net.unix.AFUNIXSelectorProvider;
import org.newsclub.net.unix.AFUNIXSocketChannel;

public final class AFUNIXSocketPair<T extends AFSomeSocket>
extends AFSocketPair<T> {
    AFUNIXSocketPair(T socket1, T socket2) {
        super(socket1, socket2);
    }

    public static AFUNIXSocketPair<AFUNIXSocketChannel> open() throws IOException {
        return AFUNIXSelectorProvider.provider().openSocketChannelPair();
    }

    public static AFUNIXSocketPair<AFUNIXDatagramChannel> openDatagram() throws IOException {
        return AFUNIXSelectorProvider.provider().openDatagramChannelPair();
    }

    public static AFUNIXSocketPair<AFUNIXDatagramChannel> openDatagram(AFSocketType type) throws IOException {
        return AFUNIXSelectorProvider.provider().openDatagramChannelPair(type);
    }
}

