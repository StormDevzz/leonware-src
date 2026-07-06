// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.io.IOException;

public final class AFUNIXSocketPair<T extends AFSomeSocket> extends AFSocketPair<T>
{
    AFUNIXSocketPair(final T socket1, final T socket2) {
        super(socket1, socket2);
    }
    
    public static AFUNIXSocketPair<AFUNIXSocketChannel> open() throws IOException {
        return AFUNIXSelectorProvider.provider().openSocketChannelPair();
    }
    
    public static AFUNIXSocketPair<AFUNIXDatagramChannel> openDatagram() throws IOException {
        return AFUNIXSelectorProvider.provider().openDatagramChannelPair();
    }
    
    public static AFUNIXSocketPair<AFUNIXDatagramChannel> openDatagram(final AFSocketType type) throws IOException {
        return AFUNIXSelectorProvider.provider().openDatagramChannelPair(type);
    }
}
