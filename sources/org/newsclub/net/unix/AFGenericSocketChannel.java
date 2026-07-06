package org.newsclub.net.unix;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFGenericSocketChannel.class */
final class AFGenericSocketChannel extends AFSocketChannel<AFGenericSocketAddress> implements AFGenericSocketExtensions {
    AFGenericSocketChannel(AFGenericSocket socket) {
        super(socket, AFGenericSelectorProvider.getInstance());
    }
}
