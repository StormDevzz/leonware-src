package sweetie.leonware.api.utils.rotation.rotations;

import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import sweetie.leonware.api.utils.combat.ClickScheduler;
import sweetie.leonware.api.utils.math.MouseUtil;
import sweetie.leonware.api.utils.player.PlayerUtil;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationMode;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/rotation/rotations/SpookyTimeRotation.class */
public class SpookyTimeRotation extends RotationMode {
    private final ClickScheduler clickScheduler;
    private static final long HOLD_LAST_ROTATION_MS = 20;
    private static final long RETURN_DURATION_MS = 70;
    private static float attackSnapTicks = 0.0f;
    private static int count = 0;
    private static boolean returningToPreviousRotation = false;
    private static long holdUntilTimeMs = 0;
    private static boolean smoothingBackToPreviousRotation = false;
    private static long smoothingStartTimeMs = 0;
    private static float savedStartYaw = 0.0f;
    private static float savedStartPitch = 0.0f;
    private static float savedEndYaw = 0.0f;
    private static float savedEndPitch = 0.0f;
    private static boolean hadTargetOnPreviousTick = false;

    public SpookyTimeRotation(ClickScheduler clickScheduler) {
        super("Spooky Time");
        this.clickScheduler = clickScheduler;
    }

    @Override // sweetie.leonware.api.utils.rotation.manager.RotationMode
    public Rotation process(Rotation currentRotation, Rotation targetRotation, class_243 vec3d, class_1297 entity) {
        long currentTimeMs = System.currentTimeMillis();
        boolean hasTarget = (entity == null || vec3d == null) ? false : true;
        if (!hasTarget) {
            attackSnapTicks = 0.0f;
            if (hadTargetOnPreviousTick && !returningToPreviousRotation && !smoothingBackToPreviousRotation) {
                returningToPreviousRotation = true;
                holdUntilTimeMs = currentTimeMs + HOLD_LAST_ROTATION_MS;
                savedStartYaw = currentRotation.getYaw();
                savedStartPitch = currentRotation.getPitch();
                savedEndYaw = mc.field_1724.method_36454();
                savedEndPitch = mc.field_1724.method_36455();
            }
            hadTargetOnPreviousTick = false;
            if (returningToPreviousRotation && currentTimeMs < holdUntilTimeMs) {
                return new Rotation(savedStartYaw, savedStartPitch);
            }
            if (returningToPreviousRotation && !smoothingBackToPreviousRotation) {
                smoothingBackToPreviousRotation = true;
                smoothingStartTimeMs = currentTimeMs;
            }
            if (!smoothingBackToPreviousRotation) {
                return currentRotation;
            }
            float returnProgress = class_3532.method_15363((currentTimeMs - smoothingStartTimeMs) / 70.0f, 0.0f, 1.0f);
            float easedProgress = returnProgress * (0.5f + (0.5f * returnProgress));
            float interpolatedYaw = savedStartYaw + (easedProgress * class_3532.method_15393(savedEndYaw - savedStartYaw));
            float interpolatedPitch = class_3532.method_15363(savedStartPitch + (easedProgress * (savedEndPitch - savedStartPitch)), -89.0f, 90.0f);
            if (returnProgress >= 1.0f) {
                returningToPreviousRotation = false;
                smoothingBackToPreviousRotation = false;
                holdUntilTimeMs = 0L;
                smoothingStartTimeMs = 0L;
            }
            return new Rotation(interpolatedYaw, interpolatedPitch);
        }
        hadTargetOnPreviousTick = true;
        returningToPreviousRotation = false;
        smoothingBackToPreviousRotation = false;
        holdUntilTimeMs = 0L;
        smoothingStartTimeMs = 0L;
        count++;
        boolean isAttackTick = this.clickScheduler != null && this.clickScheduler.isOneTickBeforeAttack();
        boolean canSeeTarget = PlayerUtil.canSee(vec3d);
        if (!canSeeTarget) {
            return processBlockBypass(currentRotation, vec3d, entity, isAttackTick);
        }
        return processOpenSight(currentRotation, entity);
    }

    private Rotation processBlockBypass(Rotation currentRotation, class_243 aimPoint, class_1297 entity, boolean isAttackTick) {
        float pitch;
        float angleX1;
        float angleY1;
        class_243 targetPos = aimPoint.method_1020(mc.field_1724.method_33571()).method_1029();
        if (isAttackTick) {
            attackSnapTicks = 6.0f;
        }
        boolean attack = false;
        if (attackSnapTicks > 0.0f) {
            attack = true;
            attackSnapTicks -= 1.0f;
        }
        int mode = count % 2;
        if (attack) {
            double h = Math.hypot(targetPos.field_1352, targetPos.field_1350);
            angleX1 = (float) class_3532.method_15350(-Math.toDegrees(Math.atan2(targetPos.field_1351, h)), -90.0d, 90.0d);
            angleY1 = (float) Math.toDegrees(Math.atan2(-targetPos.field_1352, targetPos.field_1350));
        } else {
            switch (mode) {
                case 0:
                    pitch = (-87.0f) + ThreadLocalRandom.current().nextFloat(-3.0f, 3.0f);
                    break;
                case 1:
                    pitch = 87.0f + ThreadLocalRandom.current().nextFloat(-3.0f, 3.0f);
                    break;
                default:
                    pitch = currentRotation.getPitch();
                    break;
            }
            angleX1 = pitch;
            angleY1 = class_3532.method_15393(currentRotation.getYaw() + ThreadLocalRandom.current().nextFloat(-3.0f, 3.0f));
        }
        rotLerp(angleY1, mc.field_1724.method_36454(), 0.7f);
        float lerpedPitch = rotLerp(angleX1, mc.field_1724.method_36455(), 0.7f);
        return new Rotation(angleY1, lerpedPitch);
    }

    private Rotation processOpenSight(Rotation currentRotation, class_1297 entity) {
        if (!(entity instanceof class_1309)) {
            return currentRotation;
        }
        class_1309 livingEntity = (class_1309) entity;
        class_243 predictedPos = getPredictedAimPoint(livingEntity).method_1020(mc.field_1724.method_33571());
        float yawToTarget = (float) class_3532.method_15338(Math.toDegrees(Math.atan2(predictedPos.field_1350, predictedPos.field_1352)) - 90.0d);
        float pitchToTarget = (float) (-Math.toDegrees(Math.atan2(predictedPos.field_1351, Math.hypot(predictedPos.field_1352, predictedPos.field_1350))));
        float currentYaw = currentRotation.getYaw();
        float currentPitch = currentRotation.getPitch();
        float yawDelta = class_3532.method_15393(yawToTarget - currentYaw);
        float pitchDelta = class_3532.method_15393(pitchToTarget - currentPitch);
        float clampedYaw = Math.min(Math.max(Math.abs(yawDelta), 1.0f), 50.2f);
        float clampedPitch = Math.min(Math.max(Math.abs(pitchDelta), 1.0f), 16.2f);
        float targetYaw = currentYaw + (yawDelta > 0.0f ? clampedYaw : -clampedYaw);
        float targetPitch = currentPitch + (pitchDelta > 0.0f ? clampedPitch : -clampedPitch);
        float yaw = rotLerp(currentYaw, targetYaw, 0.977f);
        float pitch = rotLerp(currentPitch, targetPitch, 0.977f);
        float yaw2 = yaw + ThreadLocalRandom.current().nextFloat(-3.0f, 3.0f);
        float pitch2 = class_3532.method_15363(pitch + ThreadLocalRandom.current().nextFloat(-3.0f, 3.0f), -90.0f, 90.0f);
        float gcd = MouseUtil.getGCD();
        if (gcd > 0.0f) {
            yaw2 -= (yaw2 - currentYaw) % gcd;
            pitch2 -= (pitch2 - currentPitch) % gcd;
        }
        return new Rotation(yaw2, pitch2);
    }

    private static class_243 getPredictedAimPoint(class_1309 t) {
        float strength;
        double distanceToTarget = mc.field_1724.method_5739(t);
        float yaw = t.method_36454();
        if (distanceToTarget <= 2.0d) {
            strength = 0.19f;
        } else {
            float yawDelta = class_3532.method_15393(t.method_36454() - t.field_5982);
            yaw += yawDelta * 2.5f;
            strength = 0.15f;
        }
        double yawRad = Math.toRadians(yaw);
        class_243 forwardDirection = new class_243(-Math.sin(yawRad), 0.0d, Math.cos(yawRad));
        return t.method_19538().method_1019(forwardDirection.method_1021(strength)).method_1031(0.0d, ((double) t.method_17682()) * 0.7d, 0.0d);
    }

    private static float rotLerp(float from, float to, float progress) {
        return from + (class_3532.method_15393(to - from) * progress);
    }

    public static boolean stalin(class_1297 target) {
        if (mc.field_1687 == null || mc.field_1724 == null) {
            return false;
        }
        class_243 pos = target.method_19538();
        class_238 hitbox = target.method_5829();
        return (isAir(hitbox.field_1323 - ((double) 0.05f), pos.field_1351, hitbox.field_1321 - ((double) 0.05f)) && isAir(hitbox.field_1320 + ((double) 0.05f), pos.field_1351, hitbox.field_1321 - ((double) 0.05f)) && isAir(hitbox.field_1323 - ((double) 0.05f), pos.field_1351, hitbox.field_1324 + ((double) 0.05f)) && isAir(hitbox.field_1320 + ((double) 0.05f), pos.field_1351, hitbox.field_1324 + ((double) 0.05f))) ? false : true;
    }

    public static boolean isAir(double x, double y, double z) {
        return mc.field_1687 != null && mc.field_1687.method_8320(new class_2338(class_3532.method_15357(x), class_3532.method_15357(y), class_3532.method_15357(z))).method_26204() == class_2246.field_10124;
    }
}
