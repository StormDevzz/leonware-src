// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "No Jump Delay", category = Category.MOVEMENT)
public class NoJumpDelayModule extends Module
{
    private static final NoJumpDelayModule instance;
    
    @Override
    public void onEvent() {
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> NoJumpDelayModule.mc.field_1724.field_6228 = 0));
        this.addEvents(updateEvent);
    }
    
    @Generated
    public static NoJumpDelayModule getInstance() {
        return NoJumpDelayModule.instance;
    }
    
    static {
        instance = new NoJumpDelayModule();
    }
}
