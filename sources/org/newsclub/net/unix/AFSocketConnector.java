package org.newsclub.net.unix;

import java.io.IOException;
import org.newsclub.net.unix.AFSocketAddress;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFSocketConnector.class */
public interface AFSocketConnector<A extends AFSocketAddress, T extends AFSocketAddress> {
    AFSocket<? extends T> connect(A a) throws IOException;
}
