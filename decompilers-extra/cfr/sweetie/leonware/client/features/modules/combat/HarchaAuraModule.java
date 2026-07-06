/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1297
 *  net.minecraft.class_1657
 *  net.minecraft.class_243
 *  net.minecraft.class_2596
 *  net.minecraft.class_2848
 *  net.minecraft.class_2848$class_2849
 *  net.minecraft.class_3532
 */
package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2848;
import net.minecraft.class_3532;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.utils.rotation.manager.RotationStrategy;
import sweetie.leonware.api.utils.task.TaskPriority;

@ModuleRegister(name="Harcha Aura", category=Category.COMBAT)
public class HarchaAuraModule
extends Module {
    private static final HarchaAuraModule instance = new HarchaAuraModule();
    private final SliderSetting range = new SliderSetting("\u0414\u0438\u0441\u0442\u0430\u043d\u0446\u0438\u044f").value(Float.valueOf(10.0f)).range(1.0f, 50.0f).step(1.0f);
    private final SliderSetting fov = new SliderSetting("\u0424\u041e\u0412").value(Float.valueOf(10.0f)).range(1.0f, 30.0f).step(1.0f);
    private final TimerUtil cooldownTimer = new TimerUtil();
    private int trackTicks;

    public HarchaAuraModule() {
        this.addSettings(this.range, this.fov);
    }

    @Override
    public void onEnable() {
        this.trackTicks = 0;
        this.cooldownTimer.reset();
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (HarchaAuraModule.mc.field_1724 == null || HarchaAuraModule.mc.field_1687 == null) {
                return;
            }
            if (this.trackTicks > 0) {
                --this.trackTicks;
                return;
            }
            if (!this.cooldownTimer.finished(45000L)) {
                return;
            }
            class_1657 target = this.findTarget();
            if (target == null) {
                return;
            }
            Rotation rot = this.calcRotation(target);
            RotationManager.getInstance().addRotation(rot, new RotationStrategy().ticksUntilReset(3).clientLook(true), TaskPriority.HIGH, this);
            if (HarchaAuraModule.mc.field_1724.method_5624()) {
                HarchaAuraModule.mc.field_1724.method_5728(false);
                HarchaAuraModule.mc.field_1690.field_1867.method_23481(false);
                mc.method_1562().method_52787((class_2596)new class_2848((class_1297)HarchaAuraModule.mc.field_1724, class_2848.class_2849.field_12985));
            }
            mc.method_1562().method_45730("spit");
            this.trackTicks = 3;
            this.cooldownTimer.reset();
        }));
        this.addEvents(updateEvent);
    }

    private class_1657 findTarget() {
        for (class_1657 player : HarchaAuraModule.mc.field_1687.method_18456()) {
            if (player == HarchaAuraModule.mc.field_1724 || !player.method_5805() || !this.isLookingAtUs(player) || !this.isInRange(player) || !HarchaAuraModule.mc.field_1724.method_6057((class_1297)player)) continue;
            return player;
        }
        return null;
    }

    private boolean isLookingAtUs(class_1657 other) {
        class_243 toUs = HarchaAuraModule.mc.field_1724.method_33571().method_1020(other.method_33571());
        if (toUs.method_1033() == 0.0) {
            return false;
        }
        toUs = toUs.method_1029();
        double yawRad = Math.toRadians(other.method_36454());
        double pitchRad = Math.toRadians(other.method_36455());
        class_243 look = new class_243(-Math.sin(yawRad) * Math.cos(pitchRad), -Math.sin(pitchRad), Math.cos(yawRad) * Math.cos(pitchRad)).method_1029();
        double angle = Math.toDegrees(Math.acos(class_3532.method_15350((double)look.method_1026(toUs), (double)-1.0, (double)1.0)));
        return angle <= (double)((Float)this.fov.getValue()).floatValue();
    }

    private boolean isInRange(class_1657 player) {
        class_243 eye = player.method_19538().method_1031(0.0, (double)player.method_18381(player.method_18376()), 0.0);
        double r = ((Float)this.range.getValue()).floatValue();
        return HarchaAuraModule.mc.field_1724.method_33571().method_1025(eye) <= r * r;
    }

    private Rotation calcRotation(class_1657 target) {
        class_243 from = HarchaAuraModule.mc.field_1724.method_33571();
        class_243 to = target.method_19538().method_1031(0.0, (double)target.method_18381(target.method_18376()), 0.0);
        double dX = to.field_1352 - from.field_1352;
        double dY = to.field_1351 - from.field_1351;
        double dZ = to.field_1350 - from.field_1350;
        double dXZ = Math.sqrt(dX * dX + dZ * dZ);
        float yaw = (float)Math.toDegrees(Math.atan2(dZ, dX)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(dY, dXZ)));
        return new Rotation(yaw, class_3532.method_15363((float)pitch, (float)-90.0f, (float)90.0f));
    }

    @Generated
    public static HarchaAuraModule getInstance() {
        return instance;
    }
}

