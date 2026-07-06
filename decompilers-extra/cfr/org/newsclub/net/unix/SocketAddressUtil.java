/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.net.SocketAddress;
import org.newsclub.net.unix.AFSupplier;
import org.newsclub.net.unix.AFUNIXSocketAddress;

final class SocketAddressUtil {
    private SocketAddressUtil() {
        throw new IllegalStateException("No instances");
    }

    static AFSupplier<AFUNIXSocketAddress> supplyAFUNIXSocketAddress(SocketAddress address) {
        return null;
    }
}

