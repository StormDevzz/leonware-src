/*
 * Decompiled with CFR 0.152.
 */
package sweetie.leonware.api.event;

import java.util.concurrent.ConcurrentSkipListSet;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.interfaces.Cacheable;
import sweetie.leonware.api.event.interfaces.Notifiable;
import sweetie.leonware.api.event.interfaces.Subscribable;

public abstract class Flora<T>
implements Cacheable<T>,
Subscribable<Listener<T>, T>,
Notifiable<T> {
    private final ConcurrentSkipListSet<Listener<T>> listeners = new ConcurrentSkipListSet();
    private volatile Listener<T>[] cache = new Listener[0];
    private volatile boolean rebuildCache = true;

    @Override
    public Listener<T>[] getCache() {
        if (this.rebuildCache) {
            this.cache = (Listener[])this.listeners.toArray(Listener[]::new);
            this.rebuildCache = false;
        }
        return this.cache;
    }

    @Override
    public EventListener subscribe(Listener<T> listener) {
        this.listeners.add(listener);
        this.rebuildCache = true;
        return new EventListener(() -> this.unsubscribe(listener));
    }

    @Override
    public void unsubscribe(Listener<T> listener) {
        if (this.listeners.remove(listener)) {
            this.rebuildCache = true;
        }
    }

    @Override
    public void notify(T event) {
        for (Listener<T> tListener : this.getCache()) {
            tListener.getHandler().accept(event);
        }
    }
}

