// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.util.Collections;
import java.util.Set;
import java.io.IOException;
import java.net.SocketOption;
import java.net.SocketImpl;

abstract class SocketImplShim extends SocketImpl
{
    protected SocketImplShim() {
    }
    
    @Deprecated
    @Override
    protected final void finalize() {
        try {
            this.close();
        }
        catch (final Exception ex) {}
    }
    
    @Override
    protected <T> void setOption(final SocketOption<T> name, final T value) throws IOException {
        throw new IOException("Unsupported option");
    }
    
    @Override
    protected <T> T getOption(final SocketOption<T> name) throws IOException {
        throw new IOException("Unsupported option");
    }
    
    @Override
    protected Set<SocketOption<?>> supportedOptions() {
        return Collections.emptySet();
    }
}
