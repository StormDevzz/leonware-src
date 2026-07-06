// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.event;

import java.util.concurrent.ConcurrentSkipListSet;
import sweetie.leonware.api.event.interfaces.Notifiable;
import sweetie.leonware.api.event.interfaces.Subscribable;
import sweetie.leonware.api.event.interfaces.Cacheable;

public abstract class Flora<T> implements Cacheable<T>, Subscribable<Listener<T>, T>, Notifiable<T>
{
    private final ConcurrentSkipListSet<Listener<T>> listeners;
    private volatile Listener<T>[] cache;
    private volatile boolean rebuildCache;
    
    public Flora() {
        this.listeners = new ConcurrentSkipListSet<Listener<T>>();
        this.cache = new Listener[0];
        this.rebuildCache = true;
    }
    
    @Override
    public Listener<T>[] getCache() {
        if (this.rebuildCache) {
            this.cache = this.listeners.toArray(Listener[]::new);
            this.rebuildCache = false;
        }
        return this.cache;
    }
    
    @Override
    public EventListener subscribe(final Listener<T> listener) {
        this.listeners.add(listener);
        this.rebuildCache = true;
        return new EventListener(() -> this.unsubscribe((Listener<T>)listener));
    }
    
    @Override
    public void unsubscribe(final Listener<T> listener) {
        if (this.listeners.remove(listener)) {
            this.rebuildCache = true;
        }
    }
    
    @Override
    public void notify(final T event) {
        for (final Listener<T> tListener : this.getCache()) {
            tListener.getHandler().accept(event);
        }
    }
}
