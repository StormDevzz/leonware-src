/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1297
 *  net.minecraft.class_243
 */
package sweetie.leonware.client.features.modules.movement.fly.modes;

import java.util.function.Supplier;
import lombok.Generated;
import net.minecraft.class_1297;
import net.minecraft.class_243;
import sweetie.leonware.api.event.events.player.move.MotionEvent;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.utils.rotation.manager.RotationPlan;
import sweetie.leonware.api.utils.rotation.manager.RotationStrategy;
import sweetie.leonware.api.utils.rotation.rotations.SmoothRotation;
import sweetie.leonware.api.utils.task.TaskPriority;
import sweetie.leonware.client.features.modules.movement.fly.FlightMode;
import sweetie.leonware.client.features.modules.movement.fly.FlightModule;

public class FlightGrim
extends FlightMode {
    public BypassType bypassType;
    private final FlightModule module;
    private TimerUtil ticks = new TimerUtil();
    private long speedRampStartTime = 0L;
    private boolean isSpeedRamping = false;
    private final ModeSetting grimType = new ModeSetting("Grim mode").value(BypassType.VERTICAL_ELYTRA).values(BypassType.values()).onAction(() -> {
        this.bypassType = switch ((String)this.getGrimType().getValue()) {
            case "Glide elytra" -> BypassType.GLIDE_ELYTRA;
            default -> BypassType.VERTICAL_ELYTRA;
        };
    });

    @Override
    public String getName() {
        return "Grim";
    }

    public FlightGrim(Supplier<Boolean> condition, FlightModule module) {
        this.grimType.setVisible((Supplier)condition);
        this.module = module;
        this.addSettings(this.grimType);
    }

    @Override
    public void onUpdate() {
        if (this.bypassType == BypassType.GLIDE_ELYTRA) {
            return;
        }
        if (FlightGrim.mc.field_1724.method_6128() && (FlightGrim.mc.field_1724.method_18798().field_1351 > 0.08 || FlightGrim.mc.field_1724.field_6017 > 0.1f) && FlightGrim.mc.field_1724.method_18798().field_1352 <= 0.01 && FlightGrim.mc.field_1724.method_18798().field_1350 <= 0.01) {
            boolean validPitch;
            RotationStrategy rotationStrategy = new RotationStrategy(new SmoothRotation(), true);
            FlightGrim.mc.field_1724.method_18798().field_1350 = 0.0;
            FlightGrim.mc.field_1724.method_18798().field_1352 = 0.0;
            RotationManager rotationManager = RotationManager.getInstance();
            Rotation rotation = rotationManager.getRotation();
            RotationPlan configurable = rotationManager.getCurrentRotationPlan();
            float pitch = configurable != null ? rotation.getPitch() : FlightGrim.mc.field_1724.method_36455();
            boolean bl = validPitch = FlightGrim.mc.field_1724.method_36455() >= -30.0f && FlightGrim.mc.field_1724.method_36455() <= 30.0f;
            if (!this.isSpeedRamping) {
                this.speedRampStartTime = System.currentTimeMillis();
                this.isSpeedRamping = true;
            }
            long rampDuration = 100L;
            long elapsed = System.currentTimeMillis() - this.speedRampStartTime;
            float progress = Math.min((float)elapsed / (float)rampDuration, 1.0f);
            double currentBaseSpeed = 0.05 * (double)progress;
            double maxAddedSpeed = 0.06;
            double maxVerticalSpeed = 1.11;
            float normalizedPitch = pitch / 90.0f;
            double speedAddition = maxAddedSpeed * (double)normalizedPitch * (double)normalizedPitch;
            double superKuniMan = currentBaseSpeed + speedAddition;
            FlightGrim.mc.field_1724.method_18798().field_1351 += superKuniMan;
            if (FlightGrim.mc.field_1724.method_18798().field_1351 >= maxVerticalSpeed) {
                FlightGrim.mc.field_1724.method_18798().field_1351 = maxVerticalSpeed;
            }
            if (!validPitch) {
                RotationManager.getInstance().addRotation(new Rotation(FlightGrim.mc.field_1724.method_36454(), 0.0f), rotationStrategy, TaskPriority.NORMAL, this.module);
            }
        } else {
            this.isSpeedRamping = false;
        }
    }

    @Override
    public void onMotion(MotionEvent.MotionEventData event) {
        float doni;
        if (this.bypassType == BypassType.VERTICAL_ELYTRA || !FlightGrim.mc.field_1724.method_6128()) {
            return;
        }
        class_243 pos = FlightGrim.mc.field_1724.method_19538();
        float yaw = FlightGrim.mc.field_1724.method_36454();
        double forward = 6.087;
        double motion = MathUtil.getEntityBPS((class_1297)FlightGrim.mc.field_1724);
        float f = doni = mc.method_1562().method_45734() != null && FlightGrim.mc.method_1562().method_45734().field_3761.contains("reallyworld") ? 48.0f : 52.0f;
        if (motion >= (double)doni) {
            forward = 0.0;
            motion = 0.0;
        }
        double dx = -Math.sin(Math.toRadians(yaw)) * forward;
        double dz = Math.cos(Math.toRadians(yaw)) * forward;
        FlightGrim.mc.field_1724.method_18800(dx * (double)MathUtil.randomInRange(1.1f, 1.21f), FlightGrim.mc.field_1724.method_18798().field_1351 - (double)0.02f, dz * (double)MathUtil.randomInRange(1.1f, 1.21f));
        if (this.ticks.finished(50L)) {
            FlightGrim.mc.field_1724.method_5814(pos.field_1352 + dx, pos.field_1351, pos.field_1350 + dz);
            this.ticks.reset();
        }
        FlightGrim.mc.field_1724.method_18800(dx * (double)MathUtil.randomInRange(1.1f, 1.21f), FlightGrim.mc.field_1724.method_18798().field_1351 + (double)0.016f, dz * (double)MathUtil.randomInRange(1.1f, 1.21f));
    }

    @Generated
    public ModeSetting getGrimType() {
        return this.grimType;
    }

    public static enum BypassType implements ModeSetting.NamedChoice
    {
        VERTICAL_ELYTRA("Vertical elytra"),
        GLIDE_ELYTRA("Glide elytra");

        private final String name;

        private BypassType(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }
}

