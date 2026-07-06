/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.jdt.annotation.NonNull
 */
package org.newsclub.net.unix;

import java.io.Closeable;
import java.io.IOException;
import java.util.Objects;
import org.eclipse.jdt.annotation.NonNull;

public class CloseablePair<T extends Closeable>
implements Closeable {
    private final @NonNull T first;
    private final @NonNull T second;
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

    @Override
    public final void close() throws IOException {
        this.first.close();
        this.second.close();
        if (this.alsoClose != null) {
            this.alsoClose.close();
        }
    }

    public final @NonNull T getFirst() {
        return this.first;
    }

    public final @NonNull T getSecond() {
        return this.second;
    }
}

