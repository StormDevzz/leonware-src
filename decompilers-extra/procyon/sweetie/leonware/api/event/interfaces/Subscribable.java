// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.event.interfaces;

import sweetie.leonware.api.event.EventListener;

public interface Subscribable<L, T>
{
    EventListener subscribe(final L p0);
    
    void unsubscribe(final L p0);
}
