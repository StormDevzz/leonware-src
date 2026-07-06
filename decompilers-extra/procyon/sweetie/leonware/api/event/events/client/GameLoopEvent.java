// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.event.events.client;

import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class GameLoopEvent extends Event<GameLoopEvent>
{
    private static final GameLoopEvent instance;
    
    @Generated
    public static GameLoopEvent getInstance() {
        return GameLoopEvent.instance;
    }
    
    static {
        instance = new GameLoopEvent();
    }
}
