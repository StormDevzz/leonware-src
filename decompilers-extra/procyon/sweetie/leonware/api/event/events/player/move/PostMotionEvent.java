// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.event.events.player.move;

import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class PostMotionEvent extends Event<PostMotionEvent>
{
    private static final PostMotionEvent instance;
    
    @Generated
    public static PostMotionEvent getInstance() {
        return PostMotionEvent.instance;
    }
    
    static {
        instance = new PostMotionEvent();
    }
}
