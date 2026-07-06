// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.util.Collection;
import java.lang.reflect.Array;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.Map;
import java.util.Set;

final class MapValueSet<T, V> implements Set<T>
{
    private final Map<T, V> map;
    private final ValueSupplier<V> valueSupplier;
    private final V removedSentinel;
    
    MapValueSet(final Map<? extends T, V> map, final ValueSupplier<V> valueSupplier, final V removedSentinel) {
        this.valueSupplier = Objects.requireNonNull(valueSupplier);
        this.removedSentinel = removedSentinel;
        this.map = (Map<T, V>)map;
    }
    
    public void markRemoved(final T elem) {
        if (this.removedSentinel == null) {
            this.map.remove(elem);
        }
        else {
            this.map.put(elem, this.removedSentinel);
        }
    }
    
    public void markAllRemoved() {
        if (this.removedSentinel == null) {
            this.map.clear();
        }
        else {
            for (final Map.Entry<T, V> en : this.map.entrySet()) {
                en.setValue(this.removedSentinel);
            }
        }
    }
    
    private V getValue() {
        return Objects.requireNonNull(this.valueSupplier.supplyValue());
    }
    
    @Override
    public int size() {
        final V val = this.getValue();
        if (val.equals(this.removedSentinel)) {
            return 0;
        }
        int size = 0;
        for (final Map.Entry<T, V> en : this.map.entrySet()) {
            if (val.equals(en.getValue())) {
                ++size;
            }
        }
        return size;
    }
    
    @Override
    public boolean isEmpty() {
        final V val = this.getValue();
        if (val.equals(this.removedSentinel)) {
            return true;
        }
        for (final Map.Entry<T, V> en : this.map.entrySet()) {
            if (val.equals(en.getValue())) {
                return false;
            }
        }
        return true;
    }
    
    private boolean isDefinitelyEmpty() {
        return this.getValue().equals(this.removedSentinel);
    }
    
    @Override
    public boolean contains(final Object o) {
        return !this.isDefinitelyEmpty() && this.getValue().equals(this.map.get(o));
    }
    
    @Override
    public Iterator<T> iterator() {
        if (this.isDefinitelyEmpty()) {
            return Collections.emptyIterator();
        }
        final Iterator<Map.Entry<T, V>> mapit = this.map.entrySet().iterator();
        final V val = this.getValue();
        return new Iterator<T>() {
            Map.Entry<T, V> nextObj = null;
            Map.Entry<T, V> currentObj = null;
            
            @Override
            public boolean hasNext() {
                if (this.nextObj != null) {
                    return true;
                }
                while (mapit.hasNext()) {
                    final Map.Entry<T, V> en = mapit.next();
                    if (val.equals(en.getValue())) {
                        this.nextObj = en;
                        return true;
                    }
                }
                return false;
            }
            
            @Override
            public T next() {
                this.currentObj = null;
                if (this.nextObj == null && !this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final T next = this.nextObj.getKey();
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
                MapValueSet.this.markRemoved(this.currentObj.getKey());
                this.currentObj = null;
            }
        };
    }
    
    @Override
    public Object[] toArray() {
        return this.toArray(new Object[this.size()]);
    }
    
    @Override
    public <E> E[] toArray(final E[] a) {
        final int size = this.size();
        if (a.length < size) {
            return (E[])this.toArray((Object[])Array.newInstance(a.getClass().getComponentType(), size));
        }
        int i = 0;
        for (final T elem : this) {
            a[i++] = (E)elem;
        }
        if (i < a.length) {
            a[i] = null;
        }
        return a;
    }
    
    public boolean update(final T e) {
        if (this.map.containsKey(e)) {
            this.map.put(e, this.getValue());
            return true;
        }
        return false;
    }
    
    @Override
    public boolean add(final T e) {
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
    public boolean remove(final Object o) {
        if (this.isDefinitelyEmpty() || !this.map.containsKey(o)) {
            return false;
        }
        this.markRemoved(o);
        return true;
    }
    
    @Override
    public boolean containsAll(final Collection<?> c) {
        if (this.isDefinitelyEmpty()) {
            return c.isEmpty();
        }
        for (final Object obj : c) {
            if (!this.contains(obj)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean addAll(final Collection<? extends T> c) {
        boolean changed = false;
        for (final T elem : c) {
            changed |= this.add(elem);
        }
        return changed;
    }
    
    @Override
    public boolean retainAll(final Collection<?> c) {
        boolean changed = false;
        final Iterator<T> it = this.iterator();
        while (it.hasNext()) {
            final T elem = it.next();
            if (!c.contains(elem)) {
                it.remove();
                changed = true;
            }
        }
        return changed;
    }
    
    @Override
    public boolean removeAll(final Collection<?> c) {
        if (this.isDefinitelyEmpty()) {
            return false;
        }
        boolean changed = false;
        for (final Object obj : c) {
            changed |= this.remove(obj);
        }
        return changed;
    }
    
    @Override
    public void clear() {
        final V val = this.getValue();
        if (val.equals(this.removedSentinel)) {
            return;
        }
        for (final Map.Entry<T, V> en : this.map.entrySet()) {
            if (val.equals(en.getValue())) {
                this.markRemoved(en.getKey());
            }
        }
    }
    
    @FunctionalInterface
    interface ValueSupplier<V>
    {
        V supplyValue();
    }
}
