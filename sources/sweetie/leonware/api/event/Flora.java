package sweetie.leonware.api.event;

import java.util.concurrent.ConcurrentSkipListSet;
import sweetie.leonware.api.event.interfaces.Cacheable;
import sweetie.leonware.api.event.interfaces.Notifiable;
import sweetie.leonware.api.event.interfaces.Subscribable;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/event/Flora.class */
public abstract class Flora<T> implements Cacheable<T>, Subscribable<Listener<T>, T>, Notifiable<T> {
    private final ConcurrentSkipListSet<Listener<T>> listeners = new ConcurrentSkipListSet<>();
    private volatile Listener<T>[] cache = new Listener[0];
    private volatile boolean rebuildCache = true;

    @Override // sweetie.leonware.api.event.interfaces.Cacheable
    public Listener<T>[] getCache() {
        if (this.rebuildCache) {
            this.cache = (Listener[]) this.listeners.toArray(x$0 -> {
                return new Listener[x$0];
            });
            this.rebuildCache = false;
        }
        return this.cache;
    }

    @Override // sweetie.leonware.api.event.interfaces.Subscribable
    public EventListener subscribe(Listener<T> listener) {
        this.listeners.add(listener);
        this.rebuildCache = true;
        return new EventListener(() -> {
            unsubscribe(listener);
        });
    }

    @Override // sweetie.leonware.api.event.interfaces.Subscribable
    public void unsubscribe(Listener<T> listener) {
        if (this.listeners.remove(listener)) {
            this.rebuildCache = true;
        }
    }

    @Override // sweetie.leonware.api.event.interfaces.Notifiable
    public void notify(T event) {
        for (Listener<T> tListener : getCache()) {
            tListener.getHandler().accept(event);
        }
    }
}
