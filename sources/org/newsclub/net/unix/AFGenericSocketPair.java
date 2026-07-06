package org.newsclub.net.unix;

import java.io.IOException;
import org.newsclub.net.unix.AFSomeSocket;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFGenericSocketPair.class */
final class AFGenericSocketPair<T extends AFSomeSocket> extends AFSocketPair<T> {
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
