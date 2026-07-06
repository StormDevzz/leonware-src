/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.IOException;
import java.net.SocketAddress;

@FunctionalInterface
public interface SocketAddressFilter {
    public SocketAddress apply(SocketAddress var1) throws IOException;
}

