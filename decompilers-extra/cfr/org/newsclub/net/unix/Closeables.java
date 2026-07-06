/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.Closeable;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class Closeables
implements Closeable {
    private boolean closed = false;
    private List<WeakReference<Closeable>> list;

    public Closeables() {
    }

    public Closeables(Closeable ... closeable) {
        this.list = new ArrayList<WeakReference<Closeable>>();
        for (Closeable cl : closeable) {
            this.list.add(new HardReference<Closeable>(cl));
        }
    }

    @Override
    public void close() throws IOException {
        this.close(null);
    }

    public synchronized boolean isClosed() {
        return this.closed;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void close(IOException superException) throws IOException {
        List<WeakReference<Closeable>> l;
        IOException exc = superException;
        Closeables closeables = this;
        synchronized (closeables) {
            this.closed = true;
            l = this.list;
            if (l == null) {
                return;
            }
            l = new ArrayList<WeakReference<Closeable>>(l);
            this.list = null;
        }
        for (WeakReference weakReference : l) {
            Closeable cl = (Closeable)weakReference.get();
            if (cl == null) continue;
            try {
                cl.close();
            }
            catch (IOException e) {
                if (exc == null) {
                    exc = e;
                    continue;
                }
                exc.addSuppressed(e);
            }
        }
        if (exc != null) {
            throw exc;
        }
    }

    public synchronized boolean add(WeakReference<Closeable> closeable) {
        if (this.closed) {
            return false;
        }
        Closeable cl = (Closeable)closeable.get();
        if (cl == null) {
            return false;
        }
        if (this.list == null) {
            this.list = new ArrayList<WeakReference<Closeable>>();
        } else {
            for (WeakReference<Closeable> ref : this.list) {
                if (!cl.equals(ref.get())) continue;
                return false;
            }
        }
        this.list.add(closeable);
        return true;
    }

    public synchronized boolean add(Closeable closeable) {
        return this.add(new HardReference<Closeable>(closeable));
    }

    public synchronized boolean remove(Closeable closeable) {
        if (this.list == null || closeable == null || this.closed) {
            return false;
        }
        Iterator<WeakReference<Closeable>> it = this.list.iterator();
        while (it.hasNext()) {
            if (!closeable.equals(it.next().get())) continue;
            it.remove();
            return true;
        }
        return false;
    }

    private static final class HardReference<V>
    extends WeakReference<V> {
        private final V strongRef;

        HardReference(V referent) {
            super(null);
            this.strongRef = referent;
        }

        @Override
        public V get() {
            return this.strongRef;
        }
    }
}

