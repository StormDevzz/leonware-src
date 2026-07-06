// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import net.minecraft.class_3532;
import net.minecraft.class_243;
import java.util.Iterator;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import net.minecraft.class_1657;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import net.minecraft.class_2596;
import net.minecraft.class_1297;
import net.minecraft.class_2848;
import sweetie.leonware.api.utils.task.TaskPriority;
import sweetie.leonware.api.utils.rotation.manager.RotationStrategy;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Harcha Aura", category = Category.COMBAT)
public class HarchaAuraModule extends Module
{
    private static final HarchaAuraModule instance;
    private final SliderSetting range;
    private final SliderSetting fov;
    private final TimerUtil cooldownTimer;
    private int trackTicks;
    
    public HarchaAuraModule() {
        this.range = new SliderSetting("\u0414\u0438\u0441\u0442\u0430\u043d\u0446\u0438\u044f").value(10.0f).range(1.0f, 50.0f).step(1.0f);
        this.fov = new SliderSetting("\u0424\u041e\u0412").value(10.0f).range(1.0f, 30.0f).step(1.0f);
        this.cooldownTimer = new TimerUtil();
        this.addSettings(this.range, this.fov);
    }
    
    @Override
    public void onEnable() {
        this.trackTicks = 0;
        this.cooldownTimer.reset();
    }
    
    @Override
    public void onEvent() {
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (HarchaAuraModule.mc.field_1724 == null || HarchaAuraModule.mc.field_1687 == null) {
                return;
            }
            else if (this.trackTicks > 0) {
                --this.trackTicks;
                return;
            }
            else if (!this.cooldownTimer.finished(45000L)) {
                return;
            }
            else {
                final class_1657 target = this.findTarget();
                if (target == null) {
                    return;
                }
                else {
                    final Rotation rot = this.calcRotation(target);
                    RotationManager.getInstance().addRotation(rot, new RotationStrategy().ticksUntilReset(3).clientLook(true), TaskPriority.HIGH, this);
                    if (HarchaAuraModule.mc.field_1724.method_5624()) {
                        HarchaAuraModule.mc.field_1724.method_5728(false);
                        HarchaAuraModule.mc.field_1690.field_1867.method_23481(false);
                        HarchaAuraModule.mc.method_1562().method_52787((class_2596)new class_2848((class_1297)HarchaAuraModule.mc.field_1724, class_2848.class_2849.field_12985));
                    }
                    HarchaAuraModule.mc.method_1562().method_45730("spit");
                    this.trackTicks = 3;
                    this.cooldownTimer.reset();
                    return;
                }
            }
        }));
        this.addEvents(updateEvent);
    }
    
    private class_1657 findTarget() {
        for (final class_1657 player : HarchaAuraModule.mc.field_1687.method_18456()) {
            if (player == HarchaAuraModule.mc.field_1724) {
                continue;
            }
            if (!player.method_5805()) {
                continue;
            }
            if (!this.isLookingAtUs(player)) {
                continue;
            }
            if (!this.isInRange(player)) {
                continue;
            }
            if (!HarchaAuraModule.mc.field_1724.method_6057((class_1297)player)) {
                continue;
            }
            return player;
        }
        return null;
    }
    
    private boolean isLookingAtUs(final class_1657 other) {
        class_243 toUs = HarchaAuraModule.mc.field_1724.method_33571().method_1020(other.method_33571());
        if (toUs.method_1033() == 0.0) {
            return false;
        }
        toUs = toUs.method_1029();
        final double yawRad = Math.toRadians(other.method_36454());
        final double pitchRad = Math.toRadians(other.method_36455());
        final class_243 look = new class_243(-Math.sin(yawRad) * Math.cos(pitchRad), -Math.sin(pitchRad), Math.cos(yawRad) * Math.cos(pitchRad)).method_1029();
        final double angle = Math.toDegrees(Math.acos(class_3532.method_15350(look.method_1026(toUs), -1.0, 1.0)));
        return angle <= this.fov.getValue();
    }
    
    private boolean isInRange(final class_1657 player) {
        final class_243 eye = player.method_19538().method_1031(0.0, (double)player.method_18381(player.method_18376()), 0.0);
        final double r = this.range.getValue();
        return HarchaAuraModule.mc.field_1724.method_33571().method_1025(eye) <= r * r;
    }
    
    private Rotation calcRotation(final class_1657 target) {
        final class_243 from = HarchaAuraModule.mc.field_1724.method_33571();
        final class_243 to = target.method_19538().method_1031(0.0, (double)target.method_18381(target.method_18376()), 0.0);
        final double dX = to.field_1352 - from.field_1352;
        final double dY = to.field_1351 - from.field_1351;
        final double dZ = to.field_1350 - from.field_1350;
        final double dXZ = Math.sqrt(dX * dX + dZ * dZ);
        final float yaw = (float)Math.toDegrees(Math.atan2(dZ, dX)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(dY, dXZ)));
        return new Rotation(yaw, class_3532.method_15363(pitch, -90.0f, 90.0f));
    }
    
    @Generated
    public static HarchaAuraModule getInstance() {
        return HarchaAuraModule.instance;
    }
    
    static {
        instance = new HarchaAuraModule();
    }
}
