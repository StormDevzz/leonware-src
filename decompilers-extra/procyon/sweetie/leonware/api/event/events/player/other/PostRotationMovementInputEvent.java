// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.event.events.player.other;

import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class PostRotationMovementInputEvent extends Event<PostRotationMovementInputEvent>
{
    private static final PostRotationMovementInputEvent instance;
    
    @Generated
    public static PostRotationMovementInputEvent getInstance() {
        return PostRotationMovementInputEvent.instance;
    }
    
    static {
        instance = new PostRotationMovementInputEvent();
    }
}
