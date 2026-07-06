package org.newsclub.net.unix;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFGenericDatagramChannel.class */
final class AFGenericDatagramChannel extends AFDatagramChannel<AFGenericSocketAddress> implements AFGenericSocketExtensions {
    AFGenericDatagramChannel(AFGenericDatagramSocket socket) {
        super(AFGenericSelectorProvider.getInstance(), socket);
    }
}
