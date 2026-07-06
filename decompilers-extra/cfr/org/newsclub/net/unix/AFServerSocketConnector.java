/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.IOException;
import org.newsclub.net.unix.AFServerSocket;
import org.newsclub.net.unix.AFSocketAddress;

public interface AFServerSocketConnector<A extends AFSocketAddress, T extends AFSocketAddress> {
    public AFServerSocket<? extends T> bind(A var1) throws IOException;
}

