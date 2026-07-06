/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import org.newsclub.net.unix.AFGenericSelectorProvider;
import org.newsclub.net.unix.AFGenericSocket;
import org.newsclub.net.unix.AFGenericSocketAddress;
import org.newsclub.net.unix.AFGenericSocketExtensions;
import org.newsclub.net.unix.AFSocketChannel;

final class AFGenericSocketChannel
extends AFSocketChannel<AFGenericSocketAddress>
implements AFGenericSocketExtensions {
    AFGenericSocketChannel(AFGenericSocket socket) {
        super(socket, AFGenericSelectorProvider.getInstance());
    }
}

