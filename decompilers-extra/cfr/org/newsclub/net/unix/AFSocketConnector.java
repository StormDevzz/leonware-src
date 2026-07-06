/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.IOException;
import org.newsclub.net.unix.AFSocket;
import org.newsclub.net.unix.AFSocketAddress;

public interface AFSocketConnector<A extends AFSocketAddress, T extends AFSocketAddress> {
    public AFSocket<? extends T> connect(A var1) throws IOException;
}

