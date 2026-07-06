/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_243
 */
package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import net.minecraft.class_243;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.SliderSetting;

@ModuleRegister(name="Fast Fall", category=Category.MOVEMENT)
public class FastFallModule
extends Module {
    private static final FastFallModule instance = new FastFallModule();
    private final SliderSetting speed = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c").value(Float.valueOf(1.0f)).range(0.1f, 5.0f).step(0.1f);

    public FastFallModule() {
        this.addSettings(this.speed);
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (FastFallModule.mc.field_1724 == null || FastFallModule.mc.field_1687 == null) {
                return;
            }
            if (FastFallModule.mc.field_1690.field_1903.method_1434()) {
                return;
            }
            if (FastFallModule.mc.field_1724.method_5771() || FastFallModule.mc.field_1724.method_5799()) {
                return;
            }
            if (FastFallModule.mc.field_1724.method_24828()) {
                class_243 currentVel = FastFallModule.mc.field_1724.method_18798();
                FastFallModule.mc.field_1724.method_18800(currentVel.field_1352, -((Float)this.speed.getValue()).doubleValue(), currentVel.field_1350);
            }
        }));
        this.addEvents(updateEvent);
    }

    @Generated
    public static FastFallModule getInstance() {
        return instance;
    }
}

