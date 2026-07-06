// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.event.events.player.other;

import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class CloseScreenEvent extends Event<CloseScreenEvent>
{
    private static final CloseScreenEvent instance;
    
    @Generated
    public static CloseScreenEvent getInstance() {
        return CloseScreenEvent.instance;
    }
    
    static {
        instance = new CloseScreenEvent();
    }
}
