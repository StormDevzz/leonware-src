/*
 * Decompiled with CFR 0.152.
 */
package sweetie.leonware.api.event.interfaces;

import sweetie.leonware.api.event.EventListener;

public interface Subscribable<L, T> {
    public EventListener subscribe(L var1);

    public void unsubscribe(L var1);
}

