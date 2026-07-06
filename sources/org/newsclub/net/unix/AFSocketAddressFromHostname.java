package org.newsclub.net.unix;

import java.net.SocketAddress;
import java.net.SocketException;
import org.newsclub.net.unix.AFSocketAddress;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFSocketAddressFromHostname.class */
public interface AFSocketAddressFromHostname<A extends AFSocketAddress> {
    SocketAddress addressFromHost(String str, int i) throws SocketException;

    default boolean isHostnameSupported(String host) {
        return host != null;
    }
}
