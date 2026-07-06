// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.nio.channels.SocketChannel;
import java.io.IOException;

final class AFGenericServerSocketChannel extends AFServerSocketChannel<AFGenericSocketAddress>
{
    AFGenericServerSocketChannel(final AFGenericServerSocket socket) {
        super(socket, AFGenericSelectorProvider.getInstance());
    }
    
    public static AFGenericServerSocketChannel open() throws IOException {
        return AFGenericServerSocket.newInstance().getChannel();
    }
    
    @Override
    public AFGenericSocketChannel accept() throws IOException {
        return (AFGenericSocketChannel)super.accept();
    }
}
