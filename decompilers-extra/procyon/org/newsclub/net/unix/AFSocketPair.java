// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.io.Closeable;

public abstract class AFSocketPair<T extends AFSomeSocket> extends CloseablePair<T>
{
    protected AFSocketPair(final T a, final T b) {
        super(a, b);
    }
    
    protected AFSocketPair(final T a, final T b, final Closeable alsoClose) {
        super(a, b, alsoClose);
    }
    
    public final T getSocket1() {
        return this.getFirst();
    }
    
    public final T getSocket2() {
        return this.getSecond();
    }
}
