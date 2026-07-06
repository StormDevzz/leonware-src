package org.newsclub.net.unix;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFGenericSocketFactory.class */
abstract class AFGenericSocketFactory extends AFSocketFactory<AFGenericSocketAddress> {
    protected AFGenericSocketFactory() {
    }

    @Override // org.newsclub.net.unix.AFSocketFactory, javax.net.SocketFactory
    public final Socket createSocket() throws SocketException {
        return configure(AFGenericSocket.newInstance(this));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.newsclub.net.unix.AFSocketFactory
    public final AFGenericSocket connectTo(AFGenericSocketAddress addr) throws IOException {
        return configure(AFGenericSocket.connectTo(addr));
    }

    protected AFGenericSocket configure(AFGenericSocket sock) throws SocketException {
        return sock;
    }
}
