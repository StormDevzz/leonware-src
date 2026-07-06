package org.newsclub.net.unix;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.eclipse.jdt.annotation.NonNullByDefault;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/NamedInteger.class */
@NonNullByDefault
public class NamedInteger implements Serializable {
    private static final long serialVersionUID = 1;
    private final String name;
    private final int id;

    /* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/NamedInteger$HasOfValue.class */
    public interface HasOfValue {
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/NamedInteger$UndefinedValueConstructor.class */
    @FunctionalInterface
    protected interface UndefinedValueConstructor<T extends NamedInteger> {
        T newInstance(int i);
    }

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
        return name() + "(" + this.id + ")";
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(this.id));
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        NamedInteger other = (NamedInteger) obj;
        return this.id == other.value();
    }

    protected static final <T extends NamedInteger> T[] init(T[] values) {
        Set<Integer> seenValues = new HashSet<>();
        for (T val : values) {
            if (!seenValues.add(Integer.valueOf(val.value()))) {
                throw new IllegalStateException("Duplicate value: " + val.value());
            }
        }
        return values;
    }

    protected static final <T extends NamedInteger> T ofValue(T[] tArr, UndefinedValueConstructor<T> undefinedValueConstructor, int i) {
        for (T t : tArr) {
            if (t.value() == i) {
                return t;
            }
        }
        return (T) undefinedValueConstructor.newInstance(i);
    }
}
