package org.newsclub.net.unix;

import java.io.IOException;
import org.newsclub.net.unix.AFSocketAddress;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFServerSocketConnector.class */
public interface AFServerSocketConnector<A extends AFSocketAddress, T extends AFSocketAddress> {
    AFServerSocket<? extends T> bind(A a) throws IOException;
}
