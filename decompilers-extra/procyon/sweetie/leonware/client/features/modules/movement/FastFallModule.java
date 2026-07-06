// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import net.minecraft.class_243;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Fast Fall", category = Category.MOVEMENT)
public class FastFallModule extends Module
{
    private static final FastFallModule instance;
    private final SliderSetting speed;
    
    public FastFallModule() {
        this.speed = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c").value(1.0f).range(0.1f, 5.0f).step(0.1f);
        this.addSettings(this.speed);
    }
    
    @Override
    public void onEvent() {
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (FastFallModule.mc.field_1724 == null || FastFallModule.mc.field_1687 == null) {
                return;
            }
            else if (FastFallModule.mc.field_1690.field_1903.method_1434()) {
                return;
            }
            else if (FastFallModule.mc.field_1724.method_5771() || FastFallModule.mc.field_1724.method_5799()) {
                return;
            }
            else {
                if (FastFallModule.mc.field_1724.method_24828()) {
                    final class_243 currentVel = FastFallModule.mc.field_1724.method_18798();
                    FastFallModule.mc.field_1724.method_18800(currentVel.field_1352, -this.speed.getValue(), currentVel.field_1350);
                }
                return;
            }
        }));
        this.addEvents(updateEvent);
    }
    
    @Generated
    public static FastFallModule getInstance() {
        return FastFallModule.instance;
    }
    
    static {
        instance = new FastFallModule();
    }
}
