// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.util.Set;
import java.util.HashSet;
import java.util.Objects;
import org.eclipse.jdt.annotation.NonNullByDefault;
import java.io.Serializable;

@NonNullByDefault
public class NamedInteger implements Serializable
{
    private static final long serialVersionUID = 1L;
    private final String name;
    private final int id;
    
    protected NamedInteger(final int id) {
        this("UNDEFINED", id);
    }
    
    protected NamedInteger(final String name, final int id) {
        this.name = name;
        this.id = id;
    }
    
    public final String name() {
        return this.name;
    }
    
    public final int value() {
        return this.id;
    }
    
    @Override
    public final String toString() {
        return this.name() + "(" + this.id + ")";
    }
    
    @Override
    public final int hashCode() {
        return Objects.hash(this.id);
    }
    
    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final NamedInteger other = (NamedInteger)obj;
        return this.id == other.value();
    }
    
    protected static final <T extends NamedInteger> T[] init(final T[] values) {
        final Set<Integer> seenValues = new HashSet<Integer>();
        for (final T val : values) {
            if (!seenValues.add(val.value())) {
                throw new IllegalStateException("Duplicate value: " + val.value());
            }
        }
        return values;
    }
    
    protected static final <T extends NamedInteger> T ofValue(final T[] values, final UndefinedValueConstructor<T> constr, final int v) {
        for (final T e : values) {
            if (e.value() == v) {
                return e;
            }
        }
        return constr.newInstance(v);
    }
    
    @FunctionalInterface
    protected interface UndefinedValueConstructor<T extends NamedInteger>
    {
        T newInstance(final int p0);
    }
    
    public interface HasOfValue
    {
    }
}
