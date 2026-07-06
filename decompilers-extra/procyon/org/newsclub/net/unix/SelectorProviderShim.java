// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.nio.channels.ServerSocketChannel;
import java.io.IOException;
import java.util.Objects;
import java.nio.channels.SocketChannel;
import java.net.ProtocolFamily;
import java.nio.channels.spi.SelectorProvider;

abstract class SelectorProviderShim extends SelectorProvider
{
    @Override
    public SocketChannel openSocketChannel(final ProtocolFamily family) throws IOException {
        Objects.requireNonNull(family);
        throw new UnsupportedOperationException("Protocol family not supported");
    }
    
    @Override
    public ServerSocketChannel openServerSocketChannel(final ProtocolFamily family) throws IOException {
        Objects.requireNonNull(family);
        throw new UnsupportedOperationException("Protocol family not supported");
    }
}
