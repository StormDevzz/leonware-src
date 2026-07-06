// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.io.Closeable;

abstract class CleanableState implements Closeable
{
    private final AtomicBoolean clean;
    
    protected CleanableState(final Object observed) {
        this.clean = new AtomicBoolean(false);
    }
    
    public final void runCleaner() {
        if (this.clean.compareAndSet(false, true)) {
            this.doClean();
        }
    }
    
    @Deprecated
    @Override
    protected final void finalize() {
        try {
            this.runCleaner();
        }
        catch (final Exception ex) {}
    }
    
    protected abstract void doClean();
    
    @Override
    public final void close() throws IOException {
        this.runCleaner();
    }
}
