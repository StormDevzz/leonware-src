// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.util.Iterator;
import java.util.Collection;
import java.io.IOException;
import java.util.ArrayList;
import java.lang.ref.WeakReference;
import java.util.List;
import java.io.Closeable;

public final class Closeables implements Closeable
{
    private boolean closed;
    private List<WeakReference<Closeable>> list;
    
    public Closeables() {
        this.closed = false;
    }
    
    public Closeables(final Closeable... closeable) {
        this.closed = false;
        this.list = new ArrayList<WeakReference<Closeable>>();
        for (final Closeable cl : closeable) {
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
    
    public void close(final IOException superException) throws IOException {
        IOException exc = superException;
        List<WeakReference<Closeable>> l;
        synchronized (this) {
            this.closed = true;
            l = this.list;
            if (l == null) {
                return;
            }
            l = new ArrayList<WeakReference<Closeable>>(l);
            this.list = null;
        }
        for (final WeakReference<Closeable> ref : l) {
            final Closeable cl = ref.get();
            if (cl == null) {
                continue;
            }
            try {
                cl.close();
            }
            catch (final IOException e) {
                if (exc == null) {
                    exc = e;
                }
                else {
                    exc.addSuppressed(e);
                }
            }
        }
        if (exc != null) {
            throw exc;
        }
    }
    
    public synchronized boolean add(final WeakReference<Closeable> closeable) {
        if (this.closed) {
            return false;
        }
        final Closeable cl = closeable.get();
        if (cl == null) {
            return false;
        }
        if (this.list == null) {
            this.list = new ArrayList<WeakReference<Closeable>>();
        }
        else {
            for (final WeakReference<Closeable> ref : this.list) {
                if (cl.equals(ref.get())) {
                    return false;
                }
            }
        }
        this.list.add(closeable);
        return true;
    }
    
    public synchronized boolean add(final Closeable closeable) {
        return this.add(new HardReference<Closeable>(closeable));
    }
    
    public synchronized boolean remove(final Closeable closeable) {
        if (this.list == null || closeable == null || this.closed) {
            return false;
        }
        final Iterator<WeakReference<Closeable>> it = this.list.iterator();
        while (it.hasNext()) {
            if (closeable.equals(it.next().get())) {
                it.remove();
                return true;
            }
        }
        return false;
    }
    
    private static final class HardReference<V> extends WeakReference<V>
    {
        private final V strongRef;
        
        HardReference(final V referent) {
            super(null);
            this.strongRef = referent;
        }
        
        @Override
        public V get() {
            return this.strongRef;
        }
    }
}
