/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.event;

import java.util.function.Consumer;
import lombok.Generated;

public class Listener<T>
implements Comparable<Listener<T>> {
    private static int counter = 0;
    private final int priority;
    private final Consumer<T> handler;
    private final long id;

    public Listener(int priority, Consumer<T> handler) {
        this.priority = priority;
        this.handler = handler;
        this.id = counter++;
    }

    public Listener(Consumer<T> handler) {
        this(0, handler);
    }

    @Override
    public int compareTo(Listener<T> other) {
        int prioCompare = Integer.compare(other.priority, this.priority);
        return prioCompare != 0 ? prioCompare : Long.compare(this.id, other.id);
    }

    @Generated
    public int getPriority() {
        return this.priority;
    }

    @Generated
    public Consumer<T> getHandler() {
        return this.handler;
    }
}

