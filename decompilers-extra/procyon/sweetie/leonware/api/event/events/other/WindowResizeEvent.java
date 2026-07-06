// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.event.events.other;

import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class WindowResizeEvent extends Event<WindowResizeEvent>
{
    private static final WindowResizeEvent instance;
    
    @Generated
    public static WindowResizeEvent getInstance() {
        return WindowResizeEvent.instance;
    }
    
    static {
        instance = new WindowResizeEvent();
    }
}
