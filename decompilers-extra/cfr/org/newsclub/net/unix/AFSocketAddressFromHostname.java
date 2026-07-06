/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.net.SocketAddress;
import java.net.SocketException;
import org.newsclub.net.unix.AFSocketAddress;

public interface AFSocketAddressFromHostname<A extends AFSocketAddress> {
    public SocketAddress addressFromHost(String var1, int var2) throws SocketException;

    default public boolean isHostnameSupported(String host) {
        return host != null;
    }
}

