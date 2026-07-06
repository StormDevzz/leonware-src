// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.nio.channels.SocketChannel;
import java.io.IOException;

public final class AFUNIXServerSocketChannel extends AFServerSocketChannel<AFUNIXSocketAddress>
{
    AFUNIXServerSocketChannel(final AFUNIXServerSocket socket) {
        super(socket, AFUNIXSelectorProvider.getInstance());
    }
    
    public static AFUNIXServerSocketChannel open() throws IOException {
        return AFUNIXServerSocket.newInstance().getChannel();
    }
    
    @Override
    public AFUNIXSocketChannel accept() throws IOException {
        return (AFUNIXSocketChannel)super.accept();
    }
}
