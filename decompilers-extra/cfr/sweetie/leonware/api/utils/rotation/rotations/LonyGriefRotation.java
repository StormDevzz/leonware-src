/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  net.minecraft.class_243
 *  net.minecraft.class_3532
 *  net.minecraft.class_3966
 */
package sweetie.leonware.api.utils.rotation.rotations;

import net.minecraft.class_1297;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.minecraft.class_3966;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.api.utils.rotation.RaytracingUtil;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationMode;
import sweetie.leonware.client.features.modules.combat.AuraModule;

public class LonyGriefRotation
extends RotationMode {
    private static int ticksSinceLastJerk = 0;
    private static int nextJerkTick = 8;
    private static float yawSpeedMultiplier = 0.0f;
    private static float pitchSpeedMultiplier = 0.0f;
    private static float accelerationSpeed = 0.15f;
    private static float decelerationSpeed = 0.35f;

    public LonyGriefRotation() {
        super("Lony Grief");
    }

    @Override
    public Rotation process(Rotation currentRotation, Rotation targetRotation, class_243 vec3d, class_1297 entity) {
        Rotation delta = RotationUtil.calculateDelta(currentRotation, targetRotation);
        float yawDelta = delta.getYaw();
        float pitchDelta = delta.getPitch();
        float distance = AuraModule.getInstance().getAttackDistance() + AuraModule.getInstance().getPreDistance();
        boolean hasTarget = entity != null;
        class_3966 hitResult = RaytracingUtil.raytraceEntity(distance, currentRotation, false);
        boolean hasTrace = hasTarget && hitResult != null && hitResult.method_17782() == entity;
        float accelerationSpeed = 0.03f;
        if (Math.abs(yawDelta) > 15.0f) {
            accelerationSpeed = MathUtil.randomInRange(0.06f, 0.32f);
            yawDelta *= 1.5f;
        }
        if (!hasTrace) {
            yawSpeedMultiplier = MathUtil.interpolate(yawSpeedMultiplier, 1.0f, accelerationSpeed);
            pitchSpeedMultiplier = MathUtil.interpolate(pitchSpeedMultiplier, 1.0f, accelerationSpeed);
        } else {
            float decelerationSpeed = 0.55f;
            yawSpeedMultiplier = MathUtil.interpolate(yawSpeedMultiplier, 0.0f, decelerationSpeed);
            pitchSpeedMultiplier = MathUtil.interpolate(pitchSpeedMultiplier, 0.0f, decelerationSpeed);
        }
        float baseYawSpeed = MathUtil.randomInRange(38.0f, 48.0f) / (LonyGriefRotation.mc.field_1724.method_6128() ? 3.0f : 1.0f);
        float basePitchSpeed = MathUtil.randomInRange(12.0f, 19.0f) / (LonyGriefRotation.mc.field_1724.method_6128() ? 3.0f : 1.0f);
        float distanceFactor = class_3532.method_15363((float)(Math.abs(yawDelta) / 15.0f), (float)0.2f, (float)1.0f);
        float yawSmoothMultiplier = yawSpeedMultiplier * yawSpeedMultiplier * (3.0f - 2.0f * yawSpeedMultiplier);
        float pitchSmoothMultiplier = pitchSpeedMultiplier * pitchSpeedMultiplier * (3.0f - 2.0f * pitchSpeedMultiplier);
        float yawSpeed = baseYawSpeed * yawSmoothMultiplier * distanceFactor;
        float pitchSpeed = basePitchSpeed * pitchSmoothMultiplier;
        if (Math.abs(yawDelta) > 5.0f) {
            float noise = (float)Math.sin((double)System.currentTimeMillis() / 20.0) * 5.0f;
            yawDelta += noise;
        }
        if (Math.abs(pitchDelta) > 3.0f) {
            float pitchNoise = (float)Math.cos((double)System.currentTimeMillis() / 20.0) * 155.5f;
            pitchDelta += pitchNoise;
        }
        if (++ticksSinceLastJerk >= nextJerkTick) {
            boolean isFliking;
            boolean bl = isFliking = Math.abs(yawDelta) > 15.0f;
            if (!isFliking && hasTrace) {
                float jerkYaw = MathUtil.randomInRange(0.4f, 0.8f);
                float jerkPitch = MathUtil.randomInRange(0.5f, 0.9f);
                yawDelta *= jerkYaw;
                pitchDelta *= jerkPitch;
            }
            ticksSinceLastJerk = 0;
            nextJerkTick = MathUtil.randomInRange(4, 10);
        }
        float clampedYawDelta = class_3532.method_15363((float)yawDelta, (float)(-yawSpeed), (float)yawSpeed);
        float clampedPitchDelta = class_3532.method_15363((float)pitchDelta, (float)(-pitchSpeed), (float)pitchSpeed);
        if (hasTrace && Math.abs(clampedYawDelta) < 0.2f) {
            clampedYawDelta = 0.0f;
        }
        return new Rotation(currentRotation.getYaw() + clampedYawDelta, currentRotation.getPitch() + clampedPitchDelta);
    }
}

