package org.newsclub.net.unix;

import java.io.Closeable;
import org.newsclub.net.unix.AFSomeSocket;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFSocketPair.class */
public abstract class AFSocketPair<T extends AFSomeSocket> extends CloseablePair<T> {
    protected AFSocketPair(T a, T b) {
        super(a, b);
    }

    protected AFSocketPair(T a, T b, Closeable alsoClose) {
        super(a, b, alsoClose);
    }

    public final T getSocket1() {
        return getFirst();
    }

    public final T getSocket2() {
        return getSecond();
    }
}
