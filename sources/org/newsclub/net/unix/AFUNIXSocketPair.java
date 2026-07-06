package org.newsclub.net.unix;

import java.io.IOException;
import org.newsclub.net.unix.AFSomeSocket;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFUNIXSocketPair.class */
public final class AFUNIXSocketPair<T extends AFSomeSocket> extends AFSocketPair<T> {
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
