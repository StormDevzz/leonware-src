package org.newsclub.net.unix;

import java.io.Closeable;
import java.io.IOException;
import java.util.Objects;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/CloseablePair.class */
public class CloseablePair<T extends Closeable> implements Closeable {
    private final T first;
    private final T second;
    private final Closeable alsoClose;

    public CloseablePair(T a, T b) {
        this(a, b, null);
    }

    public CloseablePair(T a, T b, Closeable alsoClose) {
        Objects.requireNonNull(a);
        Objects.requireNonNull(b);
        this.first = a;
        this.second = b;
        this.alsoClose = alsoClose;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public final void close() throws IOException {
        this.first.close();
        this.second.close();
        if (this.alsoClose != null) {
            this.alsoClose.close();
        }
    }

    public final T getFirst() {
        return this.first;
    }

    public final T getSecond() {
        return this.second;
    }
}
