// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.event.events.other;

import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class RotationUpdateEvent extends Event<RotationUpdateEvent>
{
    private static final RotationUpdateEvent instance;
    
    @Generated
    public static RotationUpdateEvent getInstance() {
        return RotationUpdateEvent.instance;
    }
    
    static {
        instance = new RotationUpdateEvent();
    }
}
