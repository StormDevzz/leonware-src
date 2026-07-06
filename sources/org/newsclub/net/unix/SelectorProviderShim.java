package org.newsclub.net.unix;

import java.io.IOException;
import java.net.ProtocolFamily;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Objects;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/SelectorProviderShim.class */
abstract class SelectorProviderShim extends SelectorProvider {
    SelectorProviderShim() {
    }

    public SocketChannel openSocketChannel(ProtocolFamily family) throws IOException {
        Objects.requireNonNull(family);
        throw new UnsupportedOperationException("Protocol family not supported");
    }

    public ServerSocketChannel openServerSocketChannel(ProtocolFamily family) throws IOException {
        Objects.requireNonNull(family);
        throw new UnsupportedOperationException("Protocol family not supported");
    }
}
