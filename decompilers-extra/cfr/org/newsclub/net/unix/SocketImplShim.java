/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.IOException;
import java.net.SocketImpl;
import java.net.SocketOption;
import java.util.Collections;
import java.util.Set;

abstract class SocketImplShim
extends SocketImpl {
    protected SocketImplShim() {
    }

    @Deprecated
    protected final void finalize() {
        try {
            this.close();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    @Override
    protected <T> void setOption(SocketOption<T> name, T value) throws IOException {
        throw new IOException("Unsupported option");
    }

    @Override
    protected <T> T getOption(SocketOption<T> name) throws IOException {
        throw new IOException("Unsupported option");
    }

    @Override
    protected Set<SocketOption<?>> supportedOptions() {
        return Collections.emptySet();
    }
}

