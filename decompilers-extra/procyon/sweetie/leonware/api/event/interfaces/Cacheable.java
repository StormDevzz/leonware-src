// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.event.interfaces;

import sweetie.leonware.api.event.Listener;

public interface Cacheable<T>
{
    Listener<T>[] getCache();
}
