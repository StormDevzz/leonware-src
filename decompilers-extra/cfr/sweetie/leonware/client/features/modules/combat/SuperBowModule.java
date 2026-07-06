/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1297
 *  net.minecraft.class_2596
 *  net.minecraft.class_2828$class_2829
 *  net.minecraft.class_2846
 *  net.minecraft.class_2846$class_2847
 *  net.minecraft.class_2848
 *  net.minecraft.class_2848$class_2849
 */
package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import net.minecraft.class_1297;
import net.minecraft.class_2596;
import net.minecraft.class_2828;
import net.minecraft.class_2846;
import net.minecraft.class_2848;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.SliderSetting;

@ModuleRegister(name="Super Bow", category=Category.COMBAT)
public class SuperBowModule
extends Module {
    private static final SuperBowModule instance = new SuperBowModule();
    private final SliderSetting power = new SliderSetting("Power").value(Float.valueOf(30.0f)).range(1.0f, 200.0f).step(1.0f);

    public SuperBowModule() {
        this.addSettings(this.power);
    }

    @Override
    public void onEvent() {
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(data -> {
            class_2846 p;
            if (!data.isSend()) {
                return;
            }
            if (SuperBowModule.mc.field_1724 == null || mc.method_1562() == null) {
                return;
            }
            class_2596<?> patt0$temp = data.packet();
            if (patt0$temp instanceof class_2846 && (p = (class_2846)patt0$temp).method_12363() == class_2846.class_2847.field_12974) {
                mc.method_1562().method_52787((class_2596)new class_2848((class_1297)SuperBowModule.mc.field_1724, class_2848.class_2849.field_12981));
                for (int i = 0; i < ((Float)this.power.getValue()).intValue(); ++i) {
                    mc.method_1562().method_52787((class_2596)new class_2828.class_2829(SuperBowModule.mc.field_1724.method_23317(), SuperBowModule.mc.field_1724.method_23318() - 1.0E-9, SuperBowModule.mc.field_1724.method_23321(), true, true));
                    mc.method_1562().method_52787((class_2596)new class_2828.class_2829(SuperBowModule.mc.field_1724.method_23317(), SuperBowModule.mc.field_1724.method_23318() + 1.0E-9, SuperBowModule.mc.field_1724.method_23321(), false, false));
                }
            }
        }));
        this.addEvents(packetEvent);
    }

    @Generated
    public static SuperBowModule getInstance() {
        return instance;
    }
}

