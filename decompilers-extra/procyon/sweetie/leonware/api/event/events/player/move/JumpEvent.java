// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.event.events.player.move;

import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class JumpEvent extends Event<JumpEvent>
{
    private static final JumpEvent instance;
    
    @Generated
    public static JumpEvent getInstance() {
        return JumpEvent.instance;
    }
    
    static {
        instance = new JumpEvent();
    }
}
