// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.other;

import lombok.Generated;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import net.minecraft.class_437;
import net.minecraft.class_418;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Auto Respawn", category = Category.OTHER)
public class AutoRespawnModule extends Module
{
    private static final AutoRespawnModule instance;
    
    @Override
    public void onEvent() {
        final EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener<TickEvent>(event -> {
            if (AutoRespawnModule.mc.field_1755 instanceof class_418 && AutoRespawnModule.mc.field_1724.field_6213 > 2) {
                AutoRespawnModule.mc.field_1724.method_7331();
                AutoRespawnModule.mc.method_1507((class_437)null);
            }
            return;
        }));
        this.addEvents(tickEvent);
    }
    
    @Generated
    public static AutoRespawnModule getInstance() {
        return AutoRespawnModule.instance;
    }
    
    static {
        instance = new AutoRespawnModule();
    }
}
