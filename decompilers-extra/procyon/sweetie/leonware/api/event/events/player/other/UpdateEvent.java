// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.event.events.player.other;

import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class UpdateEvent extends Event<UpdateEvent>
{
    private static final UpdateEvent instance;
    
    @Generated
    public static UpdateEvent getInstance() {
        return UpdateEvent.instance;
    }
    
    static {
        instance = new UpdateEvent();
    }
}
