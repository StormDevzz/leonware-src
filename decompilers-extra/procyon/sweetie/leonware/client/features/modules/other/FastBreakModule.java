// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.other;

import lombok.Generated;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Fast Break", category = Category.OTHER)
public class FastBreakModule extends Module
{
    private static final FastBreakModule instance;
    
    @Override
    public void onEvent() {
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            FastBreakModule.mc.field_1761.field_3716 = 0;
            FastBreakModule.mc.field_1761.method_2925();
            return;
        }));
        this.addEvents(updateEvent);
    }
    
    @Generated
    public static FastBreakModule getInstance() {
        return FastBreakModule.instance;
    }
    
    static {
        instance = new FastBreakModule();
    }
}
