// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import net.minecraft.class_1792;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import net.minecraft.class_2596;
import net.minecraft.class_2350;
import net.minecraft.class_2338;
import net.minecraft.class_2846;
import net.minecraft.class_1835;
import net.minecraft.class_1764;
import net.minecraft.class_1753;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Fast Bow", category = Category.COMBAT)
public class FastBowModule extends Module
{
    private static final FastBowModule instance;
    private final SliderSetting minCharge;
    
    public FastBowModule() {
        this.minCharge = new SliderSetting("\u041c\u0438\u043d. \u043d\u0430\u0442\u044f\u0436\u0435\u043d\u0438\u0435").value(3.0f).range(1.0f, 20.0f).step(1.0f);
        this.addSettings(this.minCharge);
    }
    
    @Override
    public void onEvent() {
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (FastBowModule.mc.field_1724 == null || FastBowModule.mc.method_1562() == null) {
                return;
            }
            else if (!FastBowModule.mc.field_1724.method_6115()) {
                return;
            }
            else {
                final class_1792 item = FastBowModule.mc.field_1724.method_6030().method_7909();
                if (!(item instanceof class_1753) && !(item instanceof class_1764) && !(item instanceof class_1835)) {
                    return;
                }
                else if (FastBowModule.mc.field_1724.method_6048() < this.minCharge.getValue().intValue()) {
                    return;
                }
                else {
                    FastBowModule.mc.method_1562().method_52787((class_2596)new class_2846(class_2846.class_2847.field_12974, class_2338.field_10980, class_2350.field_11033));
                    FastBowModule.mc.field_1724.method_6075();
                    return;
                }
            }
        }));
        this.addEvents(updateEvent);
    }
    
    @Generated
    public static FastBowModule getInstance() {
        return FastBowModule.instance;
    }
    
    static {
        instance = new FastBowModule();
    }
}
