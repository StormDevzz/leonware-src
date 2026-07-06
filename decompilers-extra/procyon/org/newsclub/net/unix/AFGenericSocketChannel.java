// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

final class AFGenericSocketChannel extends AFSocketChannel<AFGenericSocketAddress> implements AFGenericSocketExtensions
{
    AFGenericSocketChannel(final AFGenericSocket socket) {
        super(socket, AFGenericSelectorProvider.getInstance());
    }
}
