package org.newsclub.net.unix;

import java.io.IOException;
import java.net.SocketImpl;
import java.net.SocketOption;
import java.util.Collections;
import java.util.Set;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/SocketImplShim.class */
abstract class SocketImplShim extends SocketImpl {
    protected SocketImplShim() {
    }

    @Deprecated
    protected final void finalize() {
        try {
            close();
        } catch (Exception e) {
        }
    }

    @Override // java.net.SocketImpl
    protected <T> void setOption(SocketOption<T> name, T value) throws IOException {
        throw new IOException("Unsupported option");
    }

    @Override // java.net.SocketImpl
    protected <T> T getOption(SocketOption<T> name) throws IOException {
        throw new IOException("Unsupported option");
    }

    @Override // java.net.SocketImpl
    protected Set<SocketOption<?>> supportedOptions() {
        return Collections.emptySet();
    }
}
