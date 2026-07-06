/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import org.newsclub.net.unix.AFGenericSocket;
import org.newsclub.net.unix.AFGenericSocketAddress;
import org.newsclub.net.unix.AFSocketFactory;

abstract class AFGenericSocketFactory
extends AFSocketFactory<AFGenericSocketAddress> {
    protected AFGenericSocketFactory() {
    }

    @Override
    public final Socket createSocket() throws SocketException {
        return this.configure(AFGenericSocket.newInstance(this));
    }

    protected final AFGenericSocket connectTo(AFGenericSocketAddress addr) throws IOException {
        return this.configure(AFGenericSocket.connectTo(addr));
    }

    protected AFGenericSocket configure(AFGenericSocket sock) throws SocketException {
        return sock;
    }
}

