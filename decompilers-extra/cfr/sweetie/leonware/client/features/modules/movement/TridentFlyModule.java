/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1802
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_2596
 *  net.minecraft.class_2846
 *  net.minecraft.class_2846$class_2847
 */
package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import net.minecraft.class_1802;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2596;
import net.minecraft.class_2846;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

@ModuleRegister(name="Trident Fly", category=Category.MOVEMENT)
public class TridentFlyModule
extends Module {
    private static final TridentFlyModule instance = new TridentFlyModule();
    private final BooleanSetting allowNoWater = new BooleanSetting("Allow No Water").value(true);
    private final BooleanSetting instant = new BooleanSetting("Instant").value(true);
    private final BooleanSetting spam = new BooleanSetting("Spam").value(false);
    private final SliderSetting ticks = new SliderSetting("Ticks").value(Float.valueOf(3.0f)).range(0.0f, 20.0f).step(1.0f).setVisible(this.spam::getValue);
    private boolean wasUsingTrident = false;

    public TridentFlyModule() {
        this.addSettings(this.allowNoWater, this.instant, this.spam, this.ticks);
    }

    @Override
    public void onEvent() {
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener<TickEvent>(event -> this.handleTridentSpam()));
        this.addEvents(tickEvent);
    }

    private void handleTridentSpam() {
        boolean isUsingTrident;
        if (!((Boolean)this.spam.getValue()).booleanValue() || TridentFlyModule.mc.field_1724 == null) {
            return;
        }
        boolean bl = isUsingTrident = TridentFlyModule.mc.field_1724.method_6115() && TridentFlyModule.mc.field_1724.method_6047().method_7909() == class_1802.field_8547;
        if (isUsingTrident && !this.wasUsingTrident) {
            this.wasUsingTrident = true;
        } else if (this.wasUsingTrident && !isUsingTrident) {
            this.wasUsingTrident = false;
        } else if (isUsingTrident && (float)TridentFlyModule.mc.field_1724.method_6048() >= ((Float)this.ticks.getValue()).floatValue()) {
            this.sendPacket(new class_2846(class_2846.class_2847.field_12974, class_2338.field_10980, class_2350.field_11033));
            TridentFlyModule.mc.field_1724.method_6075();
            this.wasUsingTrident = false;
        }
    }

    public boolean shouldAllowNoWater() {
        return this.isEnabled() && (Boolean)this.allowNoWater.getValue() != false;
    }

    public boolean shouldInstantPullback() {
        return this.isEnabled() && (Boolean)this.instant.getValue() != false;
    }

    private void sendPacket(class_2846 packet) {
        if (mc.method_1562() != null) {
            mc.method_1562().method_52787((class_2596)packet);
        }
    }

    @Generated
    public static TridentFlyModule getInstance() {
        return instance;
    }
}

