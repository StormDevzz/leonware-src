/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_2246
 *  net.minecraft.class_2338
 *  net.minecraft.class_238
 *  net.minecraft.class_243
 *  net.minecraft.class_3532
 */
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

public class SpookyTimeRotation
extends RotationMode {
    private final ClickScheduler clickScheduler;
    private static final long HOLD_LAST_ROTATION_MS = 20L;
    private static final long RETURN_DURATION_MS = 70L;
    private static float attackSnapTicks = 0.0f;
    private static int count = 0;
    private static boolean returningToPreviousRotation = false;
    private static long holdUntilTimeMs = 0L;
    private static boolean smoothingBackToPreviousRotation = false;
    private static long smoothingStartTimeMs = 0L;
    private static float savedStartYaw = 0.0f;
    private static float savedStartPitch = 0.0f;
    private static float savedEndYaw = 0.0f;
    private static float savedEndPitch = 0.0f;
    private static boolean hadTargetOnPreviousTick = false;

    public SpookyTimeRotation(ClickScheduler clickScheduler) {
        super("Spooky Time");
        this.clickScheduler = clickScheduler;
    }

    @Override
    public Rotation process(Rotation currentRotation, Rotation targetRotation, class_243 vec3d, class_1297 entity) {
        boolean hasTarget;
        long currentTimeMs = System.currentTimeMillis();
        boolean bl = hasTarget = entity != null && vec3d != null;
        if (!hasTarget) {
            attackSnapTicks = 0.0f;
            if (hadTargetOnPreviousTick && !returningToPreviousRotation && !smoothingBackToPreviousRotation) {
                returningToPreviousRotation = true;
                holdUntilTimeMs = currentTimeMs + 20L;
                savedStartYaw = currentRotation.getYaw();
                savedStartPitch = currentRotation.getPitch();
                savedEndYaw = SpookyTimeRotation.mc.field_1724.method_36454();
                savedEndPitch = SpookyTimeRotation.mc.field_1724.method_36455();
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
            float returnProgress = class_3532.method_15363((float)((float)(currentTimeMs - smoothingStartTimeMs) / 70.0f), (float)0.0f, (float)1.0f);
            float easedProgress = returnProgress * (0.5f + 0.5f * returnProgress);
            float interpolatedYaw = savedStartYaw + easedProgress * class_3532.method_15393((float)(savedEndYaw - savedStartYaw));
            float interpolatedPitch = class_3532.method_15363((float)(savedStartPitch + easedProgress * (savedEndPitch - savedStartPitch)), (float)-89.0f, (float)90.0f);
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
        ++count;
        boolean isAttackTick = this.clickScheduler != null && this.clickScheduler.isOneTickBeforeAttack();
        boolean canSeeTarget = PlayerUtil.canSee(vec3d);
        if (!canSeeTarget) {
            return this.processBlockBypass(currentRotation, vec3d, entity, isAttackTick);
        }
        return this.processOpenSight(currentRotation, entity);
    }

    private Rotation processBlockBypass(Rotation currentRotation, class_243 aimPoint, class_1297 entity, boolean isAttackTick) {
        float angleY1;
        float angleX1;
        class_243 targetPos = aimPoint.method_1020(SpookyTimeRotation.mc.field_1724.method_33571()).method_1029();
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
            angleX1 = (float)class_3532.method_15350((double)(-Math.toDegrees(Math.atan2(targetPos.field_1351, h))), (double)-90.0, (double)90.0);
            angleY1 = (float)Math.toDegrees(Math.atan2(-targetPos.field_1352, targetPos.field_1350));
        } else {
            angleX1 = switch (mode) {
                case 0 -> -87.0f + ThreadLocalRandom.current().nextFloat(-3.0f, 3.0f);
                case 1 -> 87.0f + ThreadLocalRandom.current().nextFloat(-3.0f, 3.0f);
                default -> currentRotation.getPitch();
            };
            angleY1 = class_3532.method_15393((float)(currentRotation.getYaw() + ThreadLocalRandom.current().nextFloat(-3.0f, 3.0f)));
        }
        float lerpedYaw = SpookyTimeRotation.rotLerp(angleY1, SpookyTimeRotation.mc.field_1724.method_36454(), 0.7f);
        float lerpedPitch = SpookyTimeRotation.rotLerp(angleX1, SpookyTimeRotation.mc.field_1724.method_36455(), 0.7f);
        return new Rotation(angleY1, lerpedPitch);
    }

    private Rotation processOpenSight(Rotation currentRotation, class_1297 entity) {
        if (!(entity instanceof class_1309)) {
            return currentRotation;
        }
        class_1309 livingEntity = (class_1309)entity;
        class_243 predictedPos = SpookyTimeRotation.getPredictedAimPoint(livingEntity).method_1020(SpookyTimeRotation.mc.field_1724.method_33571());
        float yawToTarget = (float)class_3532.method_15338((double)(Math.toDegrees(Math.atan2(predictedPos.field_1350, predictedPos.field_1352)) - 90.0));
        float pitchToTarget = (float)(-Math.toDegrees(Math.atan2(predictedPos.field_1351, Math.hypot(predictedPos.field_1352, predictedPos.field_1350))));
        float currentYaw = currentRotation.getYaw();
        float currentPitch = currentRotation.getPitch();
        float yawDelta = class_3532.method_15393((float)(yawToTarget - currentYaw));
        float pitchDelta = class_3532.method_15393((float)(pitchToTarget - currentPitch));
        float clampedYaw = Math.min(Math.max(Math.abs(yawDelta), 1.0f), 50.2f);
        float clampedPitch = Math.min(Math.max(Math.abs(pitchDelta), 1.0f), 16.2f);
        float targetYaw = currentYaw + (yawDelta > 0.0f ? clampedYaw : -clampedYaw);
        float targetPitch = currentPitch + (pitchDelta > 0.0f ? clampedPitch : -clampedPitch);
        float yaw = SpookyTimeRotation.rotLerp(currentYaw, targetYaw, 0.977f);
        float pitch = SpookyTimeRotation.rotLerp(currentPitch, targetPitch, 0.977f);
        yaw += ThreadLocalRandom.current().nextFloat(-3.0f, 3.0f);
        pitch += ThreadLocalRandom.current().nextFloat(-3.0f, 3.0f);
        pitch = class_3532.method_15363((float)pitch, (float)-90.0f, (float)90.0f);
        float gcd = MouseUtil.getGCD();
        if (gcd > 0.0f) {
            yaw -= (yaw - currentYaw) % gcd;
            pitch -= (pitch - currentPitch) % gcd;
        }
        return new Rotation(yaw, pitch);
    }

    private static class_243 getPredictedAimPoint(class_1309 t) {
        float strength;
        double distanceToTarget = SpookyTimeRotation.mc.field_1724.method_5739((class_1297)t);
        float yaw = t.method_36454();
        if (distanceToTarget <= 2.0) {
            strength = 0.19f;
        } else {
            float yawDelta = class_3532.method_15393((float)(t.method_36454() - t.field_5982));
            yaw += yawDelta * 2.5f;
            strength = 0.15f;
        }
        double yawRad = Math.toRadians(yaw);
        class_243 forwardDirection = new class_243(-Math.sin(yawRad), 0.0, Math.cos(yawRad));
        return t.method_19538().method_1019(forwardDirection.method_1021((double)strength)).method_1031(0.0, (double)t.method_17682() * 0.7, 0.0);
    }

    private static float rotLerp(float from, float to, float progress) {
        return from + class_3532.method_15393((float)(to - from)) * progress;
    }

    public static boolean stalin(class_1297 target) {
        if (SpookyTimeRotation.mc.field_1687 == null || SpookyTimeRotation.mc.field_1724 == null) {
            return false;
        }
        class_243 pos = target.method_19538();
        class_238 hitbox = target.method_5829();
        float off = 0.05f;
        return !SpookyTimeRotation.isAir(hitbox.field_1323 - (double)off, pos.field_1351, hitbox.field_1321 - (double)off) || !SpookyTimeRotation.isAir(hitbox.field_1320 + (double)off, pos.field_1351, hitbox.field_1321 - (double)off) || !SpookyTimeRotation.isAir(hitbox.field_1323 - (double)off, pos.field_1351, hitbox.field_1324 + (double)off) || !SpookyTimeRotation.isAir(hitbox.field_1320 + (double)off, pos.field_1351, hitbox.field_1324 + (double)off);
    }

    public static boolean isAir(double x, double y, double z) {
        if (SpookyTimeRotation.mc.field_1687 == null) {
            return false;
        }
        return SpookyTimeRotation.mc.field_1687.method_8320(new class_2338(class_3532.method_15357((double)x), class_3532.method_15357((double)y), class_3532.method_15357((double)z))).method_26204() == class_2246.field_10124;
    }
}

