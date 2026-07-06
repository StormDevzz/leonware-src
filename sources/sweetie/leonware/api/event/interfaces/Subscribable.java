package sweetie.leonware.api.event.interfaces;

import sweetie.leonware.api.event.EventListener;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/event/interfaces/Subscribable.class */
public interface Subscribable<L, T> {
    EventListener subscribe(L l);

    void unsubscribe(L l);
}
