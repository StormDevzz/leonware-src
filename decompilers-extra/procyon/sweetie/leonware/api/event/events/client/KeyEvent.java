// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.event.events.client;

import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class KeyEvent extends Event<KeyEventData>
{
    private static final KeyEvent instance;
    
    @Generated
    public static KeyEvent getInstance() {
        return KeyEvent.instance;
    }
    
    static {
        instance = new KeyEvent();
    }
    
    record KeyEventData(int key, int action, boolean mouse) {}
}
