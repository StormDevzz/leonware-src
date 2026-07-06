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

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/MapValueSet.class */
final class MapValueSet<T, V> implements Set<T> {
    private final Map<T, V> map;
    private final ValueSupplier<V> valueSupplier;
    private final V removedSentinel;

    /* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/MapValueSet$ValueSupplier.class */
    @FunctionalInterface
    interface ValueSupplier<V> {
        V supplyValue();
    }

    /* JADX WARN: Multi-variable type inference failed */
    MapValueSet(Map<? extends T, V> map, ValueSupplier<V> valueSupplier, V removedSentinel) {
        this.valueSupplier = (ValueSupplier) Objects.requireNonNull(valueSupplier);
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
            return;
        }
        for (Map.Entry<T, V> en : this.map.entrySet()) {
            en.setValue(this.removedSentinel);
        }
    }

    private V getValue() {
        return (V) Objects.requireNonNull(this.valueSupplier.supplyValue());
    }

    @Override // java.util.Set, java.util.Collection
    public int size() {
        V val = getValue();
        if (val.equals(this.removedSentinel)) {
            return 0;
        }
        int size = 0;
        for (Map.Entry<T, V> en : this.map.entrySet()) {
            if (val.equals(en.getValue())) {
                size++;
            }
        }
        return size;
    }

    @Override // java.util.Set, java.util.Collection
    public boolean isEmpty() {
        V val = getValue();
        if (val.equals(this.removedSentinel)) {
            return true;
        }
        for (Map.Entry<T, V> en : this.map.entrySet()) {
            if (val.equals(en.getValue())) {
                return false;
            }
        }
        return true;
    }

    private boolean isDefinitelyEmpty() {
        return getValue().equals(this.removedSentinel);
    }

    @Override // java.util.Set, java.util.Collection
    public boolean contains(Object o) {
        if (isDefinitelyEmpty()) {
            return false;
        }
        return getValue().equals(this.map.get(o));
    }

    @Override // java.util.Set, java.util.Collection, java.lang.Iterable
    public Iterator<T> iterator() {
        if (isDefinitelyEmpty()) {
            return Collections.emptyIterator();
        }
        final Iterator<Map.Entry<T, V>> mapit = this.map.entrySet().iterator();
        final V val = getValue();
        return new Iterator<T>(this) { // from class: org.newsclub.net.unix.MapValueSet.1
            Map.Entry<T, V> nextObj = null;
            Map.Entry<T, V> currentObj = null;
            final /* synthetic */ MapValueSet this$0;

            {
                this.this$0 = this;
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                if (this.nextObj != null) {
                    return true;
                }
                while (mapit.hasNext()) {
                    Map.Entry<T, V> en = (Map.Entry) mapit.next();
                    if (val.equals(en.getValue())) {
                        this.nextObj = en;
                        return true;
                    }
                }
                return false;
            }

            @Override // java.util.Iterator
            public T next() {
                this.currentObj = null;
                if (this.nextObj == null && !hasNext()) {
                    throw new NoSuchElementException();
                }
                T next = this.nextObj.getKey();
                if (val.equals(this.nextObj.getValue())) {
                    this.currentObj = this.nextObj;
                    this.nextObj = null;
                    return next;
                }
                throw new ConcurrentModificationException();
            }

            @Override // java.util.Iterator
            public void remove() {
                if (this.currentObj == null) {
                    throw new IllegalStateException();
                }
                this.this$0.markRemoved(this.currentObj.getKey());
                this.currentObj = null;
            }
        };
    }

    @Override // java.util.Set, java.util.Collection
    public Object[] toArray() {
        return toArray(new Object[size()]);
    }

    @Override // java.util.Set, java.util.Collection
    public <E> E[] toArray(E[] eArr) {
        int size = size();
        if (eArr.length < size) {
            return (E[]) toArray((Object[]) Array.newInstance(eArr.getClass().getComponentType(), size));
        }
        int i = 0;
        Iterator<T> it = iterator();
        while (it.hasNext()) {
            int i2 = i;
            i++;
            eArr[i2] = it.next();
        }
        if (i < eArr.length) {
            eArr[i] = 0;
        }
        return eArr;
    }

    public boolean update(T e) {
        if (this.map.containsKey(e)) {
            this.map.put(e, getValue());
            return true;
        }
        return false;
    }

    @Override // java.util.Set, java.util.Collection
    public boolean add(T e) {
        if (contains(e)) {
            return false;
        }
        if (update(e)) {
            return true;
        }
        this.map.put(e, getValue());
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Set, java.util.Collection
    public boolean remove(Object o) {
        if (isDefinitelyEmpty() || !this.map.containsKey(o)) {
            return false;
        }
        markRemoved(o);
        return true;
    }

    @Override // java.util.Set, java.util.Collection
    public boolean containsAll(Collection<?> c) {
        if (isDefinitelyEmpty()) {
            return c.isEmpty();
        }
        for (Object obj : c) {
            if (!contains(obj)) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.Set, java.util.Collection
    public boolean addAll(Collection<? extends T> c) {
        boolean changed = false;
        for (T elem : c) {
            changed |= add(elem);
        }
        return changed;
    }

    @Override // java.util.Set, java.util.Collection
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Iterator<T> it = iterator();
        while (it.hasNext()) {
            T elem = it.next();
            if (!c.contains(elem)) {
                it.remove();
                changed = true;
            }
        }
        return changed;
    }

    @Override // java.util.Set, java.util.Collection
    public boolean removeAll(Collection<?> c) {
        if (isDefinitelyEmpty()) {
            return false;
        }
        boolean changed = false;
        for (Object obj : c) {
            changed |= remove(obj);
        }
        return changed;
    }

    @Override // java.util.Set, java.util.Collection
    public void clear() {
        V val = getValue();
        if (val.equals(this.removedSentinel)) {
            return;
        }
        for (Map.Entry<T, V> en : this.map.entrySet()) {
            if (val.equals(en.getValue())) {
                markRemoved(en.getKey());
            }
        }
    }
}
