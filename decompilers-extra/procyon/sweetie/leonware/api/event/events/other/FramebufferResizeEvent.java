// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.event.events.other;

import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class FramebufferResizeEvent extends Event<FramebufferResizeEvent>
{
    private static final FramebufferResizeEvent instance;
    
    @Generated
    public static FramebufferResizeEvent getInstance() {
        return FramebufferResizeEvent.instance;
    }
    
    static {
        instance = new FramebufferResizeEvent();
    }
}
