// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.event.events.client;

import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class DisconnectEvent extends Event<DisconnectData>
{
    private static final DisconnectEvent instance;
    
    @Generated
    public static DisconnectEvent getInstance() {
        return DisconnectEvent.instance;
    }
    
    static {
        instance = new DisconnectEvent();
    }
    
    public static class DisconnectData
    {
    }
}
