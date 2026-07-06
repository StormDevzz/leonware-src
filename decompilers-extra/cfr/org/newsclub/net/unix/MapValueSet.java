/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.jdt.annotation.NonNull
 */
package org.newsclub.net.unix;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import org.eclipse.jdt.annotation.NonNull;

final class MapValueSet<T, V>
implements Set<T> {
    private final Map<T, V> map;
    private final ValueSupplier<@NonNull V> valueSupplier;
    private final V removedSentinel;

    MapValueSet(Map<? extends T, V> map, ValueSupplier<@NonNull V> valueSupplier, V removedSentinel) {
        this.valueSupplier = Objects.requireNonNull(valueSupplier);
        this.removedSentinel = removedSentinel;
        this.map = map;
    }

    public void markRemoved(T elem) {
        if (this.removedSentinel == null) {
            this.map.remove(elem);
        } else {
            this.map.put(elem, this.removedSentinel);
        }
    }

    public void markAllRemoved() {
        if (this.removedSentinel == null) {
            this.map.clear();
        } else {
            for (Map.Entry<T, V> en : this.map.entrySet()) {
                en.setValue(this.removedSentinel);
            }
        }
    }

    private @NonNull V getValue() {
        return Objects.requireNonNull(this.valueSupplier.supplyValue());
    }

    @Override
    public int size() {
        V val = this.getValue();
        if (val.equals(this.removedSentinel)) {
            return 0;
        }
        int size = 0;
        for (Map.Entry<T, V> en : this.map.entrySet()) {
            if (!val.equals(en.getValue())) continue;
            ++size;
        }
        return size;
    }

    @Override
    public boolean isEmpty() {
        V val = this.getValue();
        if (val.equals(this.removedSentinel)) {
            return true;
        }
        for (Map.Entry<T, V> en : this.map.entrySet()) {
            if (!val.equals(en.getValue())) continue;
            return false;
        }
        return true;
    }

    private boolean isDefinitelyEmpty() {
        return this.getValue().equals(this.removedSentinel);
    }

    @Override
    public boolean contains(Object o) {
        if (this.isDefinitelyEmpty()) {
            return false;
        }
        return this.getValue().equals(this.map.get(o));
    }

    @Override
    public Iterator<T> iterator() {
        if (this.isDefinitelyEmpty()) {
            return Collections.emptyIterator();
        }
        Iterator<Map.Entry<T, V>> mapit = this.map.entrySet().iterator();
        V val = this.getValue();
        return new Iterator<T>(){
            Map.Entry<T, V> nextObj = null;
            Map.Entry<T, V> currentObj = null;
            final /* synthetic */ MapValueSet this$0;
            {
                this.this$0 = this$0;
            }

            @Override
            public boolean hasNext() {
                if (this.nextObj != null) {
                    return true;
                }
                while (mapit.hasNext()) {
                    Map.Entry en = (Map.Entry)mapit.next();
                    if (!val.equals(en.getValue())) continue;
                    this.nextObj = en;
                    return true;
                }
                return false;
            }

            @Override
            public T next() {
                this.currentObj = null;
                if (this.nextObj == null && !this.hasNext()) {
                    throw new NoSuchElementException();
                }
                Object next = this.nextObj.getKey();
                if (val.equals(this.nextObj.getValue())) {
                    this.currentObj = this.nextObj;
                    this.nextObj = null;
                    return next;
                }
                throw new ConcurrentModificationException();
            }

            @Override
            public void remove() {
                if (this.currentObj == null) {
                    throw new IllegalStateException();
                }
                this.this$0.markRemoved(this.currentObj.getKey());
                this.currentObj = null;
            }
        };
    }

    @Override
    public Object[] toArray() {
        return this.toArray((E[])new Object[this.size()]);
    }

    @Override
    public <E> E[] toArray(E[] a) {
        int size = this.size();
        if (a.length < size) {
            return this.toArray((E[])((Object[])Array.newInstance(a.getClass().getComponentType(), size)));
        }
        int i = 0;
        for (T elem : this) {
            a[i++] = elem;
        }
        if (i < a.length) {
            a[i] = null;
        }
        return a;
    }

    public boolean update(T e) {
        if (this.map.containsKey(e)) {
            this.map.put(e, this.getValue());
            return true;
        }
        return false;
    }

    @Override
    public boolean add(T e) {
        if (this.contains(e)) {
            return false;
        }
        if (this.update(e)) {
            return true;
        }
        this.map.put(e, this.getValue());
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (this.isDefinitelyEmpty() || !this.map.containsKey(o)) {
            return false;
        }
        this.markRemoved(o);
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (this.isDefinitelyEmpty()) {
            return c.isEmpty();
        }
        for (Object obj : c) {
            if (this.contains(obj)) continue;
            return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean changed = false;
        for (T elem : c) {
            changed |= this.add(elem);
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Iterator<T> it = this.iterator();
        while (it.hasNext()) {
            T elem = it.next();
            if (c.contains(elem)) continue;
            it.remove();
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (this.isDefinitelyEmpty()) {
            return false;
        }
        boolean changed = false;
        for (Object obj : c) {
            changed |= this.remove(obj);
        }
        return changed;
    }

    @Override
    public void clear() {
        V val = this.getValue();
        if (val.equals(this.removedSentinel)) {
            return;
        }
        for (Map.Entry<T, V> en : this.map.entrySet()) {
            if (!val.equals(en.getValue())) continue;
            this.markRemoved(en.getKey());
        }
    }

    @FunctionalInterface
    static interface ValueSupplier<V> {
        public V supplyValue();
    }
}

