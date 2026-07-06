/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1657
 *  net.minecraft.class_1713
 *  net.minecraft.class_1735
 *  net.minecraft.class_1792
 */
package sweetie.leonware.client.features.modules.other;

import lombok.Generated;
import net.minecraft.class_1657;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_1792;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.world.ClickSlotEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.system.backend.KeyStorage;

@ModuleRegister(name="Mouse Tweaks", category=Category.OTHER)
public class MouseTweaksModule
extends Module {
    private static final MouseTweaksModule instance = new MouseTweaksModule();
    public final SliderSetting delay = new SliderSetting("Delay").value(Float.valueOf(0.0f)).range(0.0f, 200.0f).step(5.0f);
    private boolean stop = false;

    public MouseTweaksModule() {
        this.addSettings(this.delay);
    }

    @Override
    public void onEvent() {
        EventListener clickSlotEvent = ClickSlotEvent.getInstance().subscribe(new Listener<ClickSlotEvent.ClickSlotEventData>(event -> {
            if ((KeyStorage.isPressed(340) || KeyStorage.isPressed(344)) && (KeyStorage.isPressed(341) || KeyStorage.isPressed(345)) && event.slotActionType() == class_1713.field_7795 && !this.stop) {
                class_1792 copy = ((class_1735)MouseTweaksModule.mc.field_1724.field_7512.field_7761.get(event.slot())).method_7677().method_7909();
                this.stop = true;
                for (int i2 = 0; i2 < MouseTweaksModule.mc.field_1724.field_7512.field_7761.size(); ++i2) {
                    if (((class_1735)MouseTweaksModule.mc.field_1724.field_7512.field_7761.get(i2)).method_7677().method_7909() != copy) continue;
                    MouseTweaksModule.mc.field_1761.method_2906(MouseTweaksModule.mc.field_1724.field_7512.field_7763, i2, 1, class_1713.field_7795, (class_1657)MouseTweaksModule.mc.field_1724);
                }
                this.stop = false;
            }
        }));
        this.addEvents(clickSlotEvent);
    }

    @Generated
    public static MouseTweaksModule getInstance() {
        return instance;
    }
}

