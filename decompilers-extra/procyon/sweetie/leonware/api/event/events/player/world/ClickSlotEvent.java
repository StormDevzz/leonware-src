// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.event.events.player.world;

import net.minecraft.class_1713;
import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class ClickSlotEvent extends Event<ClickSlotEventData>
{
    private static final ClickSlotEvent instance;
    
    @Generated
    public static ClickSlotEvent getInstance() {
        return ClickSlotEvent.instance;
    }
    
    static {
        instance = new ClickSlotEvent();
    }
    
    record ClickSlotEventData(class_1713 slotActionType, int slot, int button, int id) {}
}
