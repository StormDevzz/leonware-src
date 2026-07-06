/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.jdt.annotation.NonNull
 *  org.eclipse.jdt.annotation.NonNullByDefault
 *  org.eclipse.jdt.annotation.Nullable
 */
package org.newsclub.net.unix;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

@NonNullByDefault
public abstract class NamedIntegerBitmask<T extends NamedIntegerBitmask<T>>
implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String name;
    private final int flags;

    protected NamedIntegerBitmask(@Nullable String name, int flags) {
        this.name = name == null ? "UNDEFINED" : name;
        this.flags = flags;
    }

    public final String name() {
        return this.name;
    }

    public final int value() {
        return this.flags;
    }

    public final boolean hasFlag(T flag) {
        int v = ((NamedIntegerBitmask)Objects.requireNonNull(flag)).value();
        return (this.flags & v) == v;
    }

    public final String toString() {
        return this.getClass().getName() + "(" + this.name() + ":" + this.value() + ")";
    }

    protected final T combineWith(T[] allFlags, T flagsNone, Constructor<@NonNull T> constr, T other) {
        return (T)NamedIntegerBitmask.resolve(allFlags, flagsNone, constr, (int)(this.value() | ((NamedIntegerBitmask)other).value()));
    }

    public abstract T combineWith(T var1);

    protected static final <T extends NamedIntegerBitmask<T>> T resolve(T[] allFlags, T flagsNone, Constructor<T> constr, int v) {
        if (v == 0) {
            return flagsNone;
        }
        ArrayList<T> flags = new ArrayList<T>();
        for (T flag : allFlags) {
            int val = ((NamedIntegerBitmask)flag).value();
            if (val == v) {
                return flag;
            }
            if ((v & val) != val) continue;
            flags.add(flag);
        }
        return (T)NamedIntegerBitmask.resolve(allFlags, flagsNone, constr, (NamedIntegerBitmask[])flags.toArray((NamedIntegerBitmask[])Array.newInstance(flagsNone.getClass(), flags.size())));
    }

    protected static final <T extends NamedIntegerBitmask<T>> T resolve(T[] allFlags, T flagsNone, Constructor<T> constr, @NonNull T[] setFlags) {
        int flags = 0;
        int numFlagsSet = 0;
        T lastFlagSet = null;
        if (setFlags != null) {
            for (T flag : setFlags) {
                flags |= ((NamedIntegerBitmask)flag).value();
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
        StringBuilder sb = new StringBuilder();
        for (T flag : setFlags) {
            sb.append(((NamedIntegerBitmask)flag).name());
            sb.append(',');
        }
        sb.setLength(sb.length() - 1);
        return constr.newInstance(sb.toString(), flags);
    }

    @FunctionalInterface
    protected static interface Constructor<T extends NamedIntegerBitmask<T>> {
        public T newInstance(@Nullable String var1, int var2);
    }
}

