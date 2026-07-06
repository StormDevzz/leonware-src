// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.event.events.player.move;

import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class TravelEvent extends Event<TravelEvent>
{
    private static final TravelEvent instance;
    
    @Generated
    public static TravelEvent getInstance() {
        return TravelEvent.instance;
    }
    
    static {
        instance = new TravelEvent();
    }
}
