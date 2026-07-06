// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

final class AFGenericDatagramChannel extends AFDatagramChannel<AFGenericSocketAddress> implements AFGenericSocketExtensions
{
    AFGenericDatagramChannel(final AFGenericDatagramSocket socket) {
        super(AFGenericSelectorProvider.getInstance(), socket);
    }
}
