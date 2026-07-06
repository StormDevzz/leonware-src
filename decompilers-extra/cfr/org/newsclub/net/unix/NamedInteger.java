/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.jdt.annotation.NonNullByDefault
 *  org.eclipse.jdt.annotation.Nullable
 */
package org.newsclub.net.unix;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

@NonNullByDefault
public class NamedInteger
implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String name;
    private final int id;

    protected NamedInteger(int id) {
        this("UNDEFINED", id);
    }

    protected NamedInteger(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public final String name() {
        return this.name;
    }

    public final int value() {
        return this.id;
    }

    public final String toString() {
        return this.name() + "(" + this.id + ")";
    }

    public final int hashCode() {
        return Objects.hash(this.id);
    }

    public final boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        NamedInteger other = (NamedInteger)obj;
        return this.id == other.value();
    }

    protected static final <T extends NamedInteger> T[] init(T[] values) {
        HashSet<Integer> seenValues = new HashSet<Integer>();
        for (T val : values) {
            if (seenValues.add(((NamedInteger)val).value())) continue;
            throw new IllegalStateException("Duplicate value: " + ((NamedInteger)val).value());
        }
        return values;
    }

    protected static final <T extends NamedInteger> T ofValue(T[] values, UndefinedValueConstructor<T> constr, int v) {
        for (T e : values) {
            if (((NamedInteger)e).value() != v) continue;
            return e;
        }
        return constr.newInstance(v);
    }

    @FunctionalInterface
    protected static interface UndefinedValueConstructor<T extends NamedInteger> {
        public T newInstance(int var1);
    }

    public static interface HasOfValue {
    }
}

