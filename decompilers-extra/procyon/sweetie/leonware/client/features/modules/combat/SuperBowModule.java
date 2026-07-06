// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import net.minecraft.class_2828;
import net.minecraft.class_2596;
import net.minecraft.class_1297;
import net.minecraft.class_2848;
import net.minecraft.class_2846;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Super Bow", category = Category.COMBAT)
public class SuperBowModule extends Module
{
    private static final SuperBowModule instance;
    private final SliderSetting power;
    
    public SuperBowModule() {
        this.power = new SliderSetting("Power").value(30.0f).range(1.0f, 200.0f).step(1.0f);
        this.addSettings(this.power);
    }
    
    @Override
    public void onEvent() {
        final EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(data -> {
            if (!data.isSend()) {
                return;
            }
            else if (SuperBowModule.mc.field_1724 == null || SuperBowModule.mc.method_1562() == null) {
                return;
            }
            else {
                final class_2596 patt0$temp = data.packet();
                if (patt0$temp instanceof final class_2846 p) {
                    if (p.method_12363() == class_2846.class_2847.field_12974) {
                        SuperBowModule.mc.method_1562().method_52787((class_2596)new class_2848((class_1297)SuperBowModule.mc.field_1724, class_2848.class_2849.field_12981));
                        for (int i = 0; i < this.power.getValue().intValue(); ++i) {
                            SuperBowModule.mc.method_1562().method_52787((class_2596)new class_2828.class_2829(SuperBowModule.mc.field_1724.method_23317(), SuperBowModule.mc.field_1724.method_23318() - 1.0E-9, SuperBowModule.mc.field_1724.method_23321(), true, true));
                            SuperBowModule.mc.method_1562().method_52787((class_2596)new class_2828.class_2829(SuperBowModule.mc.field_1724.method_23317(), SuperBowModule.mc.field_1724.method_23318() + 1.0E-9, SuperBowModule.mc.field_1724.method_23321(), false, false));
                        }
                    }
                }
                return;
            }
        }));
        this.addEvents(packetEvent);
    }
    
    @Generated
    public static SuperBowModule getInstance() {
        return SuperBowModule.instance;
    }
    
    static {
        instance = new SuperBowModule();
    }
}
