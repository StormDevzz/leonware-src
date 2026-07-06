// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.event.events.player.move;

import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class CollisionEvent extends Event<CollisionEvent>
{
    private static final CollisionEvent instance;
    
    @Generated
    public static CollisionEvent getInstance() {
        return CollisionEvent.instance;
    }
    
    static {
        instance = new CollisionEvent();
    }
}
