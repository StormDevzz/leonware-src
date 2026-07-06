// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.event.events.player.world;

import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class PushEvent extends Event<PushEventData>
{
    private static final PushEvent instance;
    
    @Generated
    public static PushEvent getInstance() {
        return PushEvent.instance;
    }
    
    static {
        instance = new PushEvent();
    }
    
    record PushEventData(PushingSource source) {
        public enum PushingSource
        {
            BLOCK, 
            WATER, 
            ENTITY;
        }
    }
}
