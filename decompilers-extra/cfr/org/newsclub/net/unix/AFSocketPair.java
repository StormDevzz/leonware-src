/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.jdt.annotation.NonNull
 */
package org.newsclub.net.unix;

import java.io.Closeable;
import org.eclipse.jdt.annotation.NonNull;
import org.newsclub.net.unix.AFSomeSocket;
import org.newsclub.net.unix.CloseablePair;

public abstract class AFSocketPair<T extends AFSomeSocket>
extends CloseablePair<T> {
    protected AFSocketPair(T a, T b) {
        super(a, b);
    }

    protected AFSocketPair(T a, T b, Closeable alsoClose) {
        super(a, b, alsoClose);
    }

    public final @NonNull T getSocket1() {
        return (T)((AFSomeSocket)this.getFirst());
    }

    public final @NonNull T getSocket2() {
        return (T)((AFSomeSocket)this.getSecond());
    }
}

