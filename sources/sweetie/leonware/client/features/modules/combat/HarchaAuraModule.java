package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_2848;
import net.minecraft.class_3532;
import net.minecraft.class_746;
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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/HarchaAuraModule.class */
@ModuleRegister(name = "Harcha Aura", category = Category.COMBAT)
public class HarchaAuraModule extends Module {
    private static final HarchaAuraModule instance = new HarchaAuraModule();
    private final SliderSetting range = new SliderSetting("Дистанция").value(Float.valueOf(10.0f)).range(1.0f, 50.0f).step(1.0f);
    private final SliderSetting fov = new SliderSetting("ФОВ").value(Float.valueOf(10.0f)).range(1.0f, 30.0f).step(1.0f);
    private final TimerUtil cooldownTimer = new TimerUtil();
    private int trackTicks;

    @Generated
    public static HarchaAuraModule getInstance() {
        return instance;
    }

    public HarchaAuraModule() {
        addSettings(this.range, this.fov);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onEnable() {
        this.trackTicks = 0;
        this.cooldownTimer.reset();
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            class_1657 target;
            if (mc.field_1724 == null || mc.field_1687 == null) {
                return;
            }
            if (this.trackTicks > 0) {
                this.trackTicks--;
                return;
            }
            if (this.cooldownTimer.finished(45000L) && (target = findTarget()) != null) {
                Rotation rot = calcRotation(target);
                RotationManager.getInstance().addRotation(rot, new RotationStrategy().ticksUntilReset(3).clientLook(true), TaskPriority.HIGH, this);
                if (mc.field_1724.method_5624()) {
                    mc.field_1724.method_5728(false);
                    mc.field_1690.field_1867.method_23481(false);
                    mc.method_1562().method_52787(new class_2848(mc.field_1724, class_2848.class_2849.field_12985));
                }
                mc.method_1562().method_45730("spit");
                this.trackTicks = 3;
                this.cooldownTimer.reset();
            }
        }));
        addEvents(updateEvent);
    }

    private class_1657 findTarget() {
        for (class_746 class_746Var : mc.field_1687.method_18456()) {
            if (class_746Var != mc.field_1724 && class_746Var.method_5805() && isLookingAtUs(class_746Var) && isInRange(class_746Var) && mc.field_1724.method_6057(class_746Var)) {
                return class_746Var;
            }
        }
        return null;
    }

    private boolean isLookingAtUs(class_1657 other) {
        class_243 toUs = mc.field_1724.method_33571().method_1020(other.method_33571());
        if (toUs.method_1033() == 0.0d) {
            return false;
        }
        class_243 toUs2 = toUs.method_1029();
        double yawRad = Math.toRadians(other.method_36454());
        double pitchRad = Math.toRadians(other.method_36455());
        class_243 look = new class_243((-Math.sin(yawRad)) * Math.cos(pitchRad), -Math.sin(pitchRad), Math.cos(yawRad) * Math.cos(pitchRad)).method_1029();
        double angle = Math.toDegrees(Math.acos(class_3532.method_15350(look.method_1026(toUs2), -1.0d, 1.0d)));
        return angle <= ((double) this.fov.getValue().floatValue());
    }

    private boolean isInRange(class_1657 player) {
        class_243 eye = player.method_19538().method_1031(0.0d, player.method_18381(player.method_18376()), 0.0d);
        double r = this.range.getValue().floatValue();
        return mc.field_1724.method_33571().method_1025(eye) <= r * r;
    }

    private Rotation calcRotation(class_1657 target) {
        class_243 from = mc.field_1724.method_33571();
        class_243 to = target.method_19538().method_1031(0.0d, target.method_18381(target.method_18376()), 0.0d);
        double dX = to.field_1352 - from.field_1352;
        double dY = to.field_1351 - from.field_1351;
        double dZ = to.field_1350 - from.field_1350;
        double dXZ = Math.sqrt((dX * dX) + (dZ * dZ));
        float yaw = ((float) Math.toDegrees(Math.atan2(dZ, dX))) - 90.0f;
        float pitch = (float) (-Math.toDegrees(Math.atan2(dY, dXZ)));
        return new Rotation(yaw, class_3532.method_15363(pitch, -90.0f, 90.0f));
    }
}
