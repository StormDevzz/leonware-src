/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_2338
 *  net.minecraft.class_3830
 */
package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import net.minecraft.class_2338;
import net.minecraft.class_3830;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.player.MoveUtil;

@ModuleRegister(name="Anti Sweet Berry", category=Category.MOVEMENT)
public class AntiSweetBerryModule
extends Module {
    private static final AntiSweetBerryModule instance = new AntiSweetBerryModule();
    private final SliderSetting boostFactor = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c").value(Float.valueOf(0.2f)).range(0.0f, 1.0f).step(0.01f);

    public AntiSweetBerryModule() {
        this.addSettings(this.boostFactor);
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (AntiSweetBerryModule.mc.field_1724 == null || AntiSweetBerryModule.mc.field_1687 == null) {
                return;
            }
            if (!this.isInSweetBerryBush()) {
                return;
            }
            double boost = ((Float)this.boostFactor.getValue()).floatValue();
            double[] dir = MoveUtil.forward(boost);
            AntiSweetBerryModule.mc.field_1724.method_18800(dir[0], AntiSweetBerryModule.mc.field_1724.method_18798().field_1351, dir[1]);
        }));
        this.addEvents(updateEvent);
    }

    private boolean isInSweetBerryBush() {
        if (AntiSweetBerryModule.mc.field_1687 == null || AntiSweetBerryModule.mc.field_1724 == null) {
            return false;
        }
        class_2338 pos = AntiSweetBerryModule.mc.field_1724.method_24515();
        return AntiSweetBerryModule.mc.field_1687.method_8320(pos).method_26204() instanceof class_3830 || AntiSweetBerryModule.mc.field_1687.method_8320(pos.method_10074()).method_26204() instanceof class_3830;
    }

    @Generated
    public static AntiSweetBerryModule getInstance() {
        return instance;
    }
}

