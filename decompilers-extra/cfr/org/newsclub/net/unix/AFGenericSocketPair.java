/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.IOException;
import org.newsclub.net.unix.AFGenericDatagramChannel;
import org.newsclub.net.unix.AFGenericSelectorProvider;
import org.newsclub.net.unix.AFGenericSocketChannel;
import org.newsclub.net.unix.AFSocketPair;
import org.newsclub.net.unix.AFSomeSocket;

final class AFGenericSocketPair<T extends AFSomeSocket>
extends AFSocketPair<T> {
    AFGenericSocketPair(T socket1, T socket2) {
        super(socket1, socket2);
    }

    public static AFGenericSocketPair<AFGenericSocketChannel> open() throws IOException {
        return AFGenericSelectorProvider.provider().openSocketChannelPair();
    }

    public static AFGenericSocketPair<AFGenericDatagramChannel> openDatagram() throws IOException {
        return AFGenericSelectorProvider.provider().openDatagramChannelPair();
    }
}

