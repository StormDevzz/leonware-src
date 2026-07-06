// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.event.events.player.world;

import net.minecraft.class_1297;
import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class AttackEvent extends Event<AttackEventData>
{
    private static final AttackEvent instance;
    
    @Generated
    public static AttackEvent getInstance() {
        return AttackEvent.instance;
    }
    
    static {
        instance = new AttackEvent();
    }
    
    record AttackEventData(class_1297 entity) {}
}
