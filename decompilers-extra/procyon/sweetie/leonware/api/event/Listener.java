// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.event;

import lombok.Generated;
import java.util.function.Consumer;

public class Listener<T> implements Comparable<Listener<T>>
{
    private static int counter;
    private final int priority;
    private final Consumer<T> handler;
    private final long id;
    
    public Listener(final int priority, final Consumer<T> handler) {
        this.priority = priority;
        this.handler = handler;
        this.id = Listener.counter++;
    }
    
    public Listener(final Consumer<T> handler) {
        this(0, handler);
    }
    
    @Override
    public int compareTo(final Listener<T> other) {
        final int prioCompare = Integer.compare(other.priority, this.priority);
        return (prioCompare != 0) ? prioCompare : Long.compare(this.id, other.id);
    }
    
    @Generated
    public int getPriority() {
        return this.priority;
    }
    
    @Generated
    public Consumer<T> getHandler() {
        return this.handler;
    }
    
    static {
        Listener.counter = 0;
    }
}
