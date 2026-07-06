// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.rotation.rotations;

import net.minecraft.class_3966;
import net.minecraft.class_3532;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.api.utils.rotation.RaytracingUtil;
import sweetie.leonware.client.features.modules.combat.AuraModule;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import net.minecraft.class_1297;
import net.minecraft.class_243;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationMode;

public class LonyGriefRotation extends RotationMode
{
    private static int ticksSinceLastJerk;
    private static int nextJerkTick;
    private static float yawSpeedMultiplier;
    private static float pitchSpeedMultiplier;
    private static float accelerationSpeed;
    private static float decelerationSpeed;
    
    public LonyGriefRotation() {
        super("Lony Grief");
    }
    
    @Override
    public Rotation process(final Rotation currentRotation, final Rotation targetRotation, final class_243 vec3d, final class_1297 entity) {
        final Rotation delta = RotationUtil.calculateDelta(currentRotation, targetRotation);
        float yawDelta = delta.getYaw();
        float pitchDelta = delta.getPitch();
        final float distance = AuraModule.getInstance().getAttackDistance() + AuraModule.getInstance().getPreDistance();
        final boolean hasTarget = entity != null;
        final class_3966 hitResult = RaytracingUtil.raytraceEntity(distance, currentRotation, false);
        final boolean hasTrace = hasTarget && hitResult != null && hitResult.method_17782() == entity;
        float accelerationSpeed = 0.03f;
        if (Math.abs(yawDelta) > 15.0f) {
            accelerationSpeed = MathUtil.randomInRange(0.06f, 0.32f);
            yawDelta *= 1.5f;
        }
        if (!hasTrace) {
            LonyGriefRotation.yawSpeedMultiplier = MathUtil.interpolate(LonyGriefRotation.yawSpeedMultiplier, 1.0f, accelerationSpeed);
            LonyGriefRotation.pitchSpeedMultiplier = MathUtil.interpolate(LonyGriefRotation.pitchSpeedMultiplier, 1.0f, accelerationSpeed);
        }
        else {
            final float decelerationSpeed = 0.55f;
            LonyGriefRotation.yawSpeedMultiplier = MathUtil.interpolate(LonyGriefRotation.yawSpeedMultiplier, 0.0f, decelerationSpeed);
            LonyGriefRotation.pitchSpeedMultiplier = MathUtil.interpolate(LonyGriefRotation.pitchSpeedMultiplier, 0.0f, decelerationSpeed);
        }
        final float baseYawSpeed = MathUtil.randomInRange(38.0f, 48.0f) / (LonyGriefRotation.mc.field_1724.method_6128() ? 3.0f : 1.0f);
        final float basePitchSpeed = MathUtil.randomInRange(12.0f, 19.0f) / (LonyGriefRotation.mc.field_1724.method_6128() ? 3.0f : 1.0f);
        final float distanceFactor = class_3532.method_15363(Math.abs(yawDelta) / 15.0f, 0.2f, 1.0f);
        final float yawSmoothMultiplier = LonyGriefRotation.yawSpeedMultiplier * LonyGriefRotation.yawSpeedMultiplier * (3.0f - 2.0f * LonyGriefRotation.yawSpeedMultiplier);
        final float pitchSmoothMultiplier = LonyGriefRotation.pitchSpeedMultiplier * LonyGriefRotation.pitchSpeedMultiplier * (3.0f - 2.0f * LonyGriefRotation.pitchSpeedMultiplier);
        final float yawSpeed = baseYawSpeed * yawSmoothMultiplier * distanceFactor;
        final float pitchSpeed = basePitchSpeed * pitchSmoothMultiplier;
        if (Math.abs(yawDelta) > 5.0f) {
            final float noise = (float)Math.sin(System.currentTimeMillis() / 20.0) * 5.0f;
            yawDelta += noise;
        }
        if (Math.abs(pitchDelta) > 3.0f) {
            final float pitchNoise = (float)Math.cos(System.currentTimeMillis() / 20.0) * 155.5f;
            pitchDelta += pitchNoise;
        }
        ++LonyGriefRotation.ticksSinceLastJerk;
        if (LonyGriefRotation.ticksSinceLastJerk >= LonyGriefRotation.nextJerkTick) {
            final boolean isFliking = Math.abs(yawDelta) > 15.0f;
            if (!isFliking && hasTrace) {
                final float jerkYaw = MathUtil.randomInRange(0.4f, 0.8f);
                final float jerkPitch = MathUtil.randomInRange(0.5f, 0.9f);
                yawDelta *= jerkYaw;
                pitchDelta *= jerkPitch;
            }
            LonyGriefRotation.ticksSinceLastJerk = 0;
            LonyGriefRotation.nextJerkTick = MathUtil.randomInRange(4, 10);
        }
        float clampedYawDelta = class_3532.method_15363(yawDelta, -yawSpeed, yawSpeed);
        final float clampedPitchDelta = class_3532.method_15363(pitchDelta, -pitchSpeed, pitchSpeed);
        if (hasTrace && Math.abs(clampedYawDelta) < 0.2f) {
            clampedYawDelta = 0.0f;
        }
        return new Rotation(currentRotation.getYaw() + clampedYawDelta, currentRotation.getPitch() + clampedPitchDelta);
    }
    
    static {
        LonyGriefRotation.ticksSinceLastJerk = 0;
        LonyGriefRotation.nextJerkTick = 8;
        LonyGriefRotation.yawSpeedMultiplier = 0.0f;
        LonyGriefRotation.pitchSpeedMultiplier = 0.0f;
        LonyGriefRotation.accelerationSpeed = 0.15f;
        LonyGriefRotation.decelerationSpeed = 0.35f;
    }
}
