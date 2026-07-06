/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

abstract class CleanableState
implements Closeable {
    private final AtomicBoolean clean = new AtomicBoolean(false);

    protected CleanableState(Object observed) {
    }

    public final void runCleaner() {
        if (this.clean.compareAndSet(false, true)) {
            this.doClean();
        }
    }

    @Deprecated
    protected final void finalize() {
        try {
            this.runCleaner();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    protected abstract void doClean();

    @Override
    public final void close() throws IOException {
        this.runCleaner();
    }
}

