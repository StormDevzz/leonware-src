/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_238
 *  net.minecraft.class_243
 */
package sweetie.leonware.client.features.modules.movement.nitrofirework.modes;

import java.util.function.Supplier;
import net.minecraft.class_238;
import net.minecraft.class_243;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.system.backend.Pair;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.client.features.modules.combat.AuraModule;
import sweetie.leonware.client.features.modules.combat.elytratarget.ElytraTargetModule;
import sweetie.leonware.client.features.modules.movement.nitrofirework.NitroFireworkMode;

public class NitroFireworkLG
extends NitroFireworkMode {
    private final ModeSetting mode = new ModeSetting("Grim boost").value("Legends Grief").values("Legends Grief", "Really World");
    private final SliderSetting inTargetValue = new SliderSetting("In target value").value(Float.valueOf(3.0f)).range(0.1f, 6.0f).step(0.1f);
    private final MultiBooleanSetting options = new MultiBooleanSetting("Options").value(new BooleanSetting("Vertical boost").value(true), new BooleanSetting("Extra speed").value(false));
    private final BooleanSetting speedPlus = new BooleanSetting("Speed plus").value(true).setVisible(() -> !this.mode.is("Custom") && this.options.isEnabled("Extra speed"));
    private final BooleanSetting distanceBasedSpeed = new BooleanSetting("Distance based speed").value(true);

    @Override
    public String getName() {
        return "Grim";
    }

    public NitroFireworkLG(Supplier<Boolean> condition) {
        this.addSettings(this.mode, this.inTargetValue, this.options, this.speedPlus, this.distanceBasedSpeed);
        this.getSettings().forEach(setting -> setting.setVisible(condition));
    }

    @Override
    public Pair<Float, Float> velocityValues() {
        boolean isDiagonalYaw;
        Rotation rotation;
        boolean inTarget = false;
        AuraModule aura = AuraModule.getInstance();
        ElytraTargetModule elytraTarget = ElytraTargetModule.getInstance();
        class_243 targetVector = null;
        if (aura.target != null) {
            class_238 targetBB = aura.target.method_5829();
            class_243 bestCandidate = new class_243((targetBB.field_1323 + targetBB.field_1320) / 2.0, (targetBB.field_1322 + targetBB.field_1325) / 2.0, (targetBB.field_1321 + targetBB.field_1324) / 2.0);
            if (aura.target.method_6128() && elytraTarget.isEnabled()) {
                class_243 predictedPos = elytraTarget.elytraRotationProcessor.getPredictedPos(aura.target);
                if (predictedPos != null) {
                    bestCandidate = predictedPos;
                }
                if (((Boolean)this.distanceBasedSpeed.getValue()).booleanValue()) {
                    bestCandidate.method_1019(aura.target.method_18798().method_1021(0.5));
                }
            }
            targetVector = bestCandidate;
            if (NitroFireworkLG.mc.field_1724.method_33571().method_1022(bestCandidate) < (double)((Float)this.inTargetValue.getValue()).floatValue()) {
                inTarget = true;
            }
        }
        float yaw = (rotation = RotationManager.getInstance().getCurrentRotation()) != null ? rotation.getYaw() : NitroFireworkLG.mc.field_1724.method_36454();
        float pitch = rotation != null ? rotation.getPitch() : NitroFireworkLG.mc.field_1724.method_36455();
        float speed = 1.615f;
        int[] yawAngles = new int[]{45, 135, 225, 315};
        int[][] yawRanges = new int[][]{{13, 14, 18, 19, 20, 22, 23, 25, 26, 29}, {13, 14, 17, 18, 19, 23, 24, 28}, {13, 14, 17, 18, 19, 23, 24, 28}, {13, 14, 17, 18, 19, 23, 24, 28}};
        float[][] yawSpeeds = this.mode.is("Legends Grief") ? new float[][]{{2.0255f, 2.065f, 2.045f, 2.035f, 2.025f, 1.965f, 1.78f, 1.76f, 1.73f, 1.72f, 1.7f}, {2.0255f, 2.065f, 2.045f, 2.035f, 2.025f, 1.965f, 1.77f, 1.75f, 1.7f}, {2.0255f, 2.065f, 2.045f, 2.035f, 2.025f, 1.965f, 1.77f, 1.75f, 1.7f}, {2.0255f, 2.065f, 2.045f, 2.035f, 2.025f, 1.965f, 1.77f, 1.75f, 1.7f}} : new float[][]{{1.805f, 1.805f, 1.87f, 1.85f, 1.83f, 1.8f, 1.78f, 1.76f, 1.73f, 1.72f, 1.7f}, {1.805f, 1.805f, 1.87f, 1.85f, 1.82f, 1.8f, 1.77f, 1.75f, 1.7f}, {1.805f, 1.805f, 1.87f, 1.85f, 1.82f, 1.8f, 1.77f, 1.75f, 1.7f}, {1.805f, 1.805f, 1.87f, 1.85f, 1.82f, 1.8f, 1.77f, 1.75f, 1.7f}};
        for (int i = 0; i < yawAngles.length; ++i) {
            int currentYaw = yawAngles[i];
            int[] ranges = yawRanges[i];
            float[] speeds = yawSpeeds[i];
            if (this.isYawInRange(yaw, currentYaw, ranges[0])) {
                speed = pitch >= 1.0f && pitch <= 90.0f ? speeds[0] : speeds[1];
            }
            for (int j = 1; j < ranges.length; ++j) {
                if (!this.isYawInRange(yaw, currentYaw, ranges[j]) || this.isYawInRange(yaw, currentYaw, ranges[j - 1])) continue;
                speed = speeds[j + 1];
            }
        }
        if (pitch <= -30.0f || pitch >= 30.0f) {
            speed = 1.615f;
        }
        if (pitch <= -80.0f || pitch >= 80.0f) {
            speed = 1.715f;
        }
        boolean bl = isDiagonalYaw = this.isYawInRange(yaw, 45.0f, 20.0f) || this.isYawInRange(yaw, 135.0f, 20.0f) || this.isYawInRange(yaw, 225.0f, 20.0f) || this.isYawInRange(yaw, 315.0f, 20.0f);
        if (!isDiagonalYaw && (pitch >= 15.0f && pitch <= 35.0f || pitch <= -15.0f && pitch >= -35.0f)) {
            speed = 1.7f;
        }
        if (this.options.isEnabled("Extra speed") && (pitch >= 35.0f && pitch <= 60.0f || pitch <= -35.0f && pitch >= -52.0f)) {
            speed = (Boolean)this.speedPlus.getValue() != false ? 2.15f : 1.95f;
        }
        float targetSpeed = 1.5f;
        if (((Boolean)this.distanceBasedSpeed.getValue()).booleanValue() && targetVector != null) {
            float distanceFactor = (float)(NitroFireworkLG.mc.field_1724.method_33571().method_1022(targetVector) / (double)AuraModule.getInstance().getAttackDistance());
            targetSpeed = Math.max(1.3f, speed * distanceFactor);
        }
        float speedXZ = inTarget ? targetSpeed : speed;
        return new Pair<Float, Float>(Float.valueOf(speedXZ), Float.valueOf(this.options.isEnabled("Vertical boost") ? speedXZ : 1.5f));
    }

    public boolean isYawInRange(float yaw, float firstValue, float radiusValue) {
        yaw = (yaw % 360.0f + 360.0f) % 360.0f;
        float minValue = ((firstValue = (firstValue % 360.0f + 360.0f) % 360.0f) - radiusValue + 360.0f) % 360.0f;
        float maxValue = (firstValue + radiusValue) % 360.0f;
        if (minValue < maxValue) {
            return yaw >= minValue && yaw <= maxValue;
        }
        return yaw >= minValue || yaw <= maxValue;
    }
}

