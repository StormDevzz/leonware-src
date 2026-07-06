package sweetie.leonware.api.event;

import java.util.function.Consumer;
import lombok.Generated;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/event/Listener.class */
public class Listener<T> implements Comparable<Listener<T>> {
    private static int counter = 0;
    private final int priority;
    private final Consumer<T> handler;
    private final long id;

    @Generated
    public int getPriority() {
        return this.priority;
    }

    @Generated
    public Consumer<T> getHandler() {
        return this.handler;
    }

    public Listener(int priority, Consumer<T> handler) {
        this.priority = priority;
        this.handler = handler;
        int i = counter;
        counter = i + 1;
        this.id = i;
    }

    public Listener(Consumer<T> handler) {
        this(0, handler);
    }

    @Override // java.lang.Comparable
    public int compareTo(Listener<T> other) {
        int prioCompare = Integer.compare(other.priority, this.priority);
        return prioCompare != 0 ? prioCompare : Long.compare(this.id, other.id);
    }
}
