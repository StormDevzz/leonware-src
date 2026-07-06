/*
 * Decompiled with CFR 0.152.
 */
package sweetie.leonware.api.event.interfaces;

import sweetie.leonware.api.event.Listener;

public interface Cacheable<T> {
    public Listener<T>[] getCache();
}

