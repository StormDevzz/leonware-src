package sweetie.leonware.api.event.interfaces;

import sweetie.leonware.api.event.Listener;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/event/interfaces/Cacheable.class */
public interface Cacheable<T> {
    Listener<T>[] getCache();
}
