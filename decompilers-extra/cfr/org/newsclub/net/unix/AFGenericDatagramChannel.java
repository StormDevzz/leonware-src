/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import org.newsclub.net.unix.AFDatagramChannel;
import org.newsclub.net.unix.AFGenericDatagramSocket;
import org.newsclub.net.unix.AFGenericSelectorProvider;
import org.newsclub.net.unix.AFGenericSocketAddress;
import org.newsclub.net.unix.AFGenericSocketExtensions;

final class AFGenericDatagramChannel
extends AFDatagramChannel<AFGenericSocketAddress>
implements AFGenericSocketExtensions {
    AFGenericDatagramChannel(AFGenericDatagramSocket socket) {
        super(AFGenericSelectorProvider.getInstance(), socket);
    }
}

