package org.newsclub.net.unix;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.newsclub.net.unix.NamedIntegerBitmask;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/NamedIntegerBitmask.class */
@NonNullByDefault
public abstract class NamedIntegerBitmask<T extends NamedIntegerBitmask<T>> implements Serializable {
    private static final long serialVersionUID = 1;
    private final String name;
    private final int flags;

    /* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/NamedIntegerBitmask$Constructor.class */
    @FunctionalInterface
    protected interface Constructor<T extends NamedIntegerBitmask<T>> {
        T newInstance(String str, int i);
    }

    public abstract T combineWith(T t);

    protected NamedIntegerBitmask(String name, int flags) {
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
        int v = ((NamedIntegerBitmask) Objects.requireNonNull(flag)).value();
        return (this.flags & v) == v;
    }

    public final String toString() {
        return getClass().getName() + "(" + name() + ":" + value() + ")";
    }

    protected final T combineWith(T[] tArr, T t, Constructor<T> constructor, T t2) {
        return (T) resolve(tArr, t, constructor, value() | t2.value());
    }

    protected static final <T extends NamedIntegerBitmask<T>> T resolve(T[] tArr, T t, Constructor<T> constructor, int i) {
        if (i == 0) {
            return t;
        }
        ArrayList arrayList = new ArrayList();
        for (T t2 : tArr) {
            int iValue = t2.value();
            if (iValue == i) {
                return t2;
            }
            if ((i & iValue) == iValue) {
                arrayList.add(t2);
            }
        }
        return (T) resolve(tArr, t, constructor, (NamedIntegerBitmask[]) arrayList.toArray((NamedIntegerBitmask[]) Array.newInstance(t.getClass(), arrayList.size())));
    }

    protected static final <T extends NamedIntegerBitmask<T>> T resolve(T[] tArr, T t, Constructor<T> constructor, T[] tArr2) {
        int iValue = 0;
        int i = 0;
        T t2 = null;
        if (tArr2 != null) {
            for (T t3 : tArr2) {
                iValue |= t3.value();
                t2 = t3;
                i++;
            }
        }
        if (iValue == 0) {
            return t;
        }
        if (i == 1 && t2 != null) {
            return t2;
        }
        StringBuilder sb = new StringBuilder();
        for (T t4 : tArr2) {
            sb.append(t4.name());
            sb.append(',');
        }
        sb.setLength(sb.length() - 1);
        return (T) constructor.newInstance(sb.toString(), iValue);
    }
}
