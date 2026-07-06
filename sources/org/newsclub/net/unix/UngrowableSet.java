package org.newsclub.net.unix;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/UngrowableSet.class */
final class UngrowableSet<T> implements Set<T> {
    private final Set<T> set;

    UngrowableSet(Set<T> set) {
        this.set = set;
    }

    @Override // java.util.Set, java.util.Collection
    public int size() {
        return this.set.size();
    }

    @Override // java.util.Set, java.util.Collection
    public boolean isEmpty() {
        return this.set.isEmpty();
    }

    @Override // java.util.Set, java.util.Collection
    public boolean contains(Object o) {
        return this.set.contains(o);
    }

    @Override // java.util.Set, java.util.Collection, java.lang.Iterable
    public Iterator<T> iterator() {
        return this.set.iterator();
    }

    @Override // java.util.Set, java.util.Collection
    public Object[] toArray() {
        return this.set.toArray();
    }

    @Override // java.util.Set, java.util.Collection
    public <E> E[] toArray(E[] eArr) {
        return (E[]) this.set.toArray(eArr);
    }

    @Override // java.util.Set, java.util.Collection
    public boolean add(T e) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Set, java.util.Collection
    public boolean remove(Object o) {
        return this.set.remove(o);
    }

    @Override // java.util.Set, java.util.Collection
    public boolean containsAll(Collection<?> c) {
        return this.set.containsAll(c);
    }

    @Override // java.util.Set, java.util.Collection
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Set, java.util.Collection
    public boolean retainAll(Collection<?> c) {
        return this.set.retainAll(c);
    }

    @Override // java.util.Set, java.util.Collection
    public boolean removeAll(Collection<?> c) {
        return this.set.removeAll(c);
    }

    @Override // java.util.Set, java.util.Collection
    public void clear() {
        this.set.clear();
    }
}
