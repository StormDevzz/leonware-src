package org.newsclub.net.unix;

import java.io.IOException;
import java.net.SocketAddress;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/SocketAddressFilter.class */
@FunctionalInterface
public interface SocketAddressFilter {
    SocketAddress apply(SocketAddress socketAddress) throws IOException;
}
