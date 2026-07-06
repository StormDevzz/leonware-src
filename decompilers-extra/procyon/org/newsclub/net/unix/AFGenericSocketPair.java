// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.io.IOException;

final class AFGenericSocketPair<T extends AFSomeSocket> extends AFSocketPair<T>
{
    AFGenericSocketPair(final T socket1, final T socket2) {
        super(socket1, socket2);
    }
    
    public static AFGenericSocketPair<AFGenericSocketChannel> open() throws IOException {
        return AFGenericSelectorProvider.provider().openSocketChannelPair();
    }
    
    public static AFGenericSocketPair<AFGenericDatagramChannel> openDatagram() throws IOException {
        return AFGenericSelectorProvider.provider().openDatagramChannelPair();
    }
}
