package org.newsclub.net.unix;

import java.io.Closeable;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/Closeables.class */
public final class Closeables implements Closeable {
    private boolean closed;
    private List<WeakReference<Closeable>> list;

    public Closeables() {
        this.closed = false;
    }

    public Closeables(Closeable... closeable) {
        this.closed = false;
        this.list = new ArrayList();
        for (Closeable cl : closeable) {
            this.list.add(new HardReference(cl));
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        close(null);
    }

    public synchronized boolean isClosed() {
        return this.closed;
    }

    public void close(IOException superException) throws IOException {
        IOException exc = superException;
        synchronized (this) {
            this.closed = true;
            List<WeakReference<Closeable>> l = this.list;
            if (l == null) {
                return;
            }
            List<WeakReference<Closeable>> l2 = new ArrayList<>(l);
            this.list = null;
            for (WeakReference<Closeable> ref : l2) {
                Closeable cl = ref.get();
                if (cl != null) {
                    try {
                        cl.close();
                    } catch (IOException e) {
                        if (exc == null) {
                            exc = e;
                        } else {
                            exc.addSuppressed(e);
                        }
                    }
                }
            }
            if (exc != null) {
                throw exc;
            }
        }
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/Closeables$HardReference.class */
    private static final class HardReference<V> extends WeakReference<V> {
        private final V strongRef;

        HardReference(V referent) {
            super(null);
            this.strongRef = referent;
        }

        @Override // java.lang.ref.Reference
        public V get() {
            return this.strongRef;
        }
    }

    public synchronized boolean add(WeakReference<Closeable> closeable) {
        Closeable cl;
        if (this.closed || (cl = closeable.get()) == null) {
            return false;
        }
        if (this.list == null) {
            this.list = new ArrayList();
        } else {
            for (WeakReference<Closeable> ref : this.list) {
                if (cl.equals(ref.get())) {
                    return false;
                }
            }
        }
        this.list.add(closeable);
        return true;
    }

    public synchronized boolean add(Closeable closeable) {
        return add(new HardReference(closeable));
    }

    public synchronized boolean remove(Closeable closeable) {
        if (this.list == null || closeable == null || this.closed) {
            return false;
        }
        Iterator<WeakReference<Closeable>> it = this.list.iterator();
        while (it.hasNext()) {
            if (closeable.equals(it.next().get())) {
                it.remove();
                return true;
            }
        }
        return false;
    }
}
