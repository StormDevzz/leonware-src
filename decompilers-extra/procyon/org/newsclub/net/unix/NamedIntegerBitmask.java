// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.util.List;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;
import org.eclipse.jdt.annotation.NonNullByDefault;
import java.io.Serializable;

@NonNullByDefault
public abstract class NamedIntegerBitmask<T extends NamedIntegerBitmask<T>> implements Serializable
{
    private static final long serialVersionUID = 1L;
    private final String name;
    private final int flags;
    
    protected NamedIntegerBitmask(final String name, final int flags) {
        this.name = ((name == null) ? "UNDEFINED" : name);
        this.flags = flags;
    }
    
    public final String name() {
        return this.name;
    }
    
    public final int value() {
        return this.flags;
    }
    
    public final boolean hasFlag(final T flag) {
        final int v = Objects.requireNonNull(flag).value();
        return (this.flags & v) == v;
    }
    
    @Override
    public final String toString() {
        return this.getClass().getName() + "(" + this.name() + ":" + this.value() + ")";
    }
    
    protected final T combineWith(final T[] allFlags, final T flagsNone, final Constructor<T> constr, final T other) {
        return resolve(allFlags, flagsNone, constr, this.value() | other.value());
    }
    
    public abstract T combineWith(final T p0);
    
    protected static final <T extends NamedIntegerBitmask<T>> T resolve(final T[] allFlags, final T flagsNone, final Constructor<T> constr, final int v) {
        if (v == 0) {
            return flagsNone;
        }
        final List<T> flags = new ArrayList<T>();
        for (final T flag : allFlags) {
            final int val = flag.value();
            if (val == v) {
                return flag;
            }
            if ((v & val) == val) {
                flags.add(flag);
            }
        }
        return resolve(allFlags, flagsNone, constr, (T[])flags.toArray((T[])Array.newInstance(flagsNone.getClass(), flags.size())));
    }
    
    protected static final <T extends NamedIntegerBitmask<T>> T resolve(final T[] allFlags, final T flagsNone, final Constructor<T> constr, final T[] setFlags) {
        int flags = 0;
        int numFlagsSet = 0;
        T lastFlagSet = null;
        if (setFlags != null) {
            for (final T flag : setFlags) {
                flags |= flag.value();
                lastFlagSet = flag;
                ++numFlagsSet;
            }
        }
        if (flags == 0) {
            return flagsNone;
        }
        if (numFlagsSet == 1 && lastFlagSet != null) {
            return lastFlagSet;
        }
        final StringBuilder sb = new StringBuilder();
        for (final T flag2 : setFlags) {
            sb.append(flag2.name());
            sb.append(',');
        }
        sb.setLength();
        return constr.newInstance(sb.toString(), flags);
    }
    
    @FunctionalInterface
    protected interface Constructor<T extends NamedIntegerBitmask<T>>
    {
        T newInstance(final String p0, final int p1);
    }
}
