package org.newsclub.net.unix;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/CleanableState.class */
abstract class CleanableState implements Closeable {
    private final AtomicBoolean clean = new AtomicBoolean(false);

    protected abstract void doClean();

    protected CleanableState(Object observed) {
    }

    public final void runCleaner() {
        if (this.clean.compareAndSet(false, true)) {
            doClean();
        }
    }

    @Deprecated
    protected final void finalize() {
        try {
            runCleaner();
        } catch (Exception e) {
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public final void close() throws IOException {
        runCleaner();
    }
}
