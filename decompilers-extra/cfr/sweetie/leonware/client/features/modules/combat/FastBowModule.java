/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1753
 *  net.minecraft.class_1764
 *  net.minecraft.class_1792
 *  net.minecraft.class_1835
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_2596
 *  net.minecraft.class_2846
 *  net.minecraft.class_2846$class_2847
 */
package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import net.minecraft.class_1753;
import net.minecraft.class_1764;
import net.minecraft.class_1792;
import net.minecraft.class_1835;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2596;
import net.minecraft.class_2846;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.SliderSetting;

@ModuleRegister(name="Fast Bow", category=Category.COMBAT)
public class FastBowModule
extends Module {
    private static final FastBowModule instance = new FastBowModule();
    private final SliderSetting minCharge = new SliderSetting("\u041c\u0438\u043d. \u043d\u0430\u0442\u044f\u0436\u0435\u043d\u0438\u0435").value(Float.valueOf(3.0f)).range(1.0f, 20.0f).step(1.0f);

    public FastBowModule() {
        this.addSettings(this.minCharge);
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (FastBowModule.mc.field_1724 == null || mc.method_1562() == null) {
                return;
            }
            if (!FastBowModule.mc.field_1724.method_6115()) {
                return;
            }
            class_1792 item = FastBowModule.mc.field_1724.method_6030().method_7909();
            if (!(item instanceof class_1753 || item instanceof class_1764 || item instanceof class_1835)) {
                return;
            }
            if (FastBowModule.mc.field_1724.method_6048() < ((Float)this.minCharge.getValue()).intValue()) {
                return;
            }
            mc.method_1562().method_52787((class_2596)new class_2846(class_2846.class_2847.field_12974, class_2338.field_10980, class_2350.field_11033));
            FastBowModule.mc.field_1724.method_6075();
        }));
        this.addEvents(updateEvent);
    }

    @Generated
    public static FastBowModule getInstance() {
        return instance;
    }
}

