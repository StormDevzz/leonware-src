// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.event.events.client;

import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class TickEvent extends Event<TickEvent>
{
    private static final TickEvent instance;
    
    @Generated
    public static TickEvent getInstance() {
        return TickEvent.instance;
    }
    
    static {
        instance = new TickEvent();
    }
}
