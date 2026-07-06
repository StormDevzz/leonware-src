// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Night Vision", category = Category.RENDER)
public class NightVisionModule extends Module
{
    private static final NightVisionModule instance;
    
    @Override
    public void onDisable() {
        this.remove();
    }
    
    @Override
    public void onEvent() {
        final EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener<TickEvent>(event -> this.add()));
        this.addEvents(tickEvent);
    }
    
    private void remove() {
        if (NightVisionModule.mc.field_1724 == null) {
            return;
        }
        NightVisionModule.mc.field_1724.method_6016(class_1294.field_5925);
    }
    
    private void add() {
        if (NightVisionModule.mc.field_1724 == null) {
            return;
        }
        NightVisionModule.mc.field_1724.method_6092(new class_1293(class_1294.field_5925, -1, 0, false, false, false));
    }
    
    @Generated
    public static NightVisionModule getInstance() {
        return NightVisionModule.instance;
    }
    
    static {
        instance = new NightVisionModule();
    }
}
