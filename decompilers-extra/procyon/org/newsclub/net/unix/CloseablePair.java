// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.io.IOException;
import java.util.Objects;
import java.io.Closeable;

public class CloseablePair<T extends Closeable> implements Closeable
{
    private final T first;
    private final T second;
    private final Closeable alsoClose;
    
    public CloseablePair(final T a, final T b) {
        this(a, b, null);
    }
    
    public CloseablePair(final T a, final T b, final Closeable alsoClose) {
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
    
    public final T getFirst() {
        return this.first;
    }
    
    public final T getSecond() {
        return this.second;
    }
}
