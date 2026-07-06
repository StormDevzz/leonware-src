// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

final class UngrowableSet<T> implements Set<T>
{
    private final Set<T> set;
    
    UngrowableSet(final Set<T> set) {
        this.set = set;
    }
    
    @Override
    public int size() {
        return this.set.size();
    }
    
    @Override
    public boolean isEmpty() {
        return this.set.isEmpty();
    }
    
    @Override
    public boolean contains(final Object o) {
        return this.set.contains(o);
    }
    
    @Override
    public Iterator<T> iterator() {
        return this.set.iterator();
    }
    
    @Override
    public Object[] toArray() {
        return this.set.toArray();
    }
    
    @Override
    public <E> E[] toArray(final E[] a) {
        return this.set.toArray(a);
    }
    
    @Override
    public boolean add(final T e) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean remove(final Object o) {
        return this.set.remove(o);
    }
    
    @Override
    public boolean containsAll(final Collection<?> c) {
        return this.set.containsAll(c);
    }
    
    @Override
    public boolean addAll(final Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean retainAll(final Collection<?> c) {
        return this.set.retainAll(c);
    }
    
    @Override
    public boolean removeAll(final Collection<?> c) {
        return this.set.removeAll(c);
    }
    
    @Override
    public void clear() {
        this.set.clear();
    }
}
