// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.rotation.rotations;

import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import sweetie.leonware.api.utils.math.MouseUtil;
import net.minecraft.class_1309;
import java.util.concurrent.ThreadLocalRandom;
import sweetie.leonware.api.utils.player.PlayerUtil;
import net.minecraft.class_3532;
import net.minecraft.class_1297;
import net.minecraft.class_243;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.combat.ClickScheduler;
import sweetie.leonware.api.utils.rotation.manager.RotationMode;

public class SpookyTimeRotation extends RotationMode
{
    private final ClickScheduler clickScheduler;
    private static final long HOLD_LAST_ROTATION_MS = 20L;
    private static final long RETURN_DURATION_MS = 70L;
    private static float attackSnapTicks;
    private static int count;
    private static boolean returningToPreviousRotation;
    private static long holdUntilTimeMs;
    private static boolean smoothingBackToPreviousRotation;
    private static long smoothingStartTimeMs;
    private static float savedStartYaw;
    private static float savedStartPitch;
    private static float savedEndYaw;
    private static float savedEndPitch;
    private static boolean hadTargetOnPreviousTick;
    
    public SpookyTimeRotation(final ClickScheduler clickScheduler) {
        super("Spooky Time");
        this.clickScheduler = clickScheduler;
    }
    
    @Override
    public Rotation process(final Rotation currentRotation, final Rotation targetRotation, final class_243 vec3d, final class_1297 entity) {
        final long currentTimeMs = System.currentTimeMillis();
        final boolean hasTarget = entity != null && vec3d != null;
        if (!hasTarget) {
            SpookyTimeRotation.attackSnapTicks = 0.0f;
            if (SpookyTimeRotation.hadTargetOnPreviousTick && !SpookyTimeRotation.returningToPreviousRotation && !SpookyTimeRotation.smoothingBackToPreviousRotation) {
                SpookyTimeRotation.returningToPreviousRotation = true;
                SpookyTimeRotation.holdUntilTimeMs = currentTimeMs + 20L;
                SpookyTimeRotation.savedStartYaw = currentRotation.getYaw();
                SpookyTimeRotation.savedStartPitch = currentRotation.getPitch();
                SpookyTimeRotation.savedEndYaw = SpookyTimeRotation.mc.field_1724.method_36454();
                SpookyTimeRotation.savedEndPitch = SpookyTimeRotation.mc.field_1724.method_36455();
            }
            SpookyTimeRotation.hadTargetOnPreviousTick = false;
            if (SpookyTimeRotation.returningToPreviousRotation && currentTimeMs < SpookyTimeRotation.holdUntilTimeMs) {
                return new Rotation(SpookyTimeRotation.savedStartYaw, SpookyTimeRotation.savedStartPitch);
            }
            if (SpookyTimeRotation.returningToPreviousRotation && !SpookyTimeRotation.smoothingBackToPreviousRotation) {
                SpookyTimeRotation.smoothingBackToPreviousRotation = true;
                SpookyTimeRotation.smoothingStartTimeMs = currentTimeMs;
            }
            if (!SpookyTimeRotation.smoothingBackToPreviousRotation) {
                return currentRotation;
            }
            final float returnProgress = class_3532.method_15363((currentTimeMs - SpookyTimeRotation.smoothingStartTimeMs) / 70.0f, 0.0f, 1.0f);
            final float easedProgress = returnProgress * (0.5f + 0.5f * returnProgress);
            final float interpolatedYaw = SpookyTimeRotation.savedStartYaw + easedProgress * class_3532.method_15393(SpookyTimeRotation.savedEndYaw - SpookyTimeRotation.savedStartYaw);
            final float interpolatedPitch = class_3532.method_15363(SpookyTimeRotation.savedStartPitch + easedProgress * (SpookyTimeRotation.savedEndPitch - SpookyTimeRotation.savedStartPitch), -89.0f, 90.0f);
            if (returnProgress >= 1.0f) {
                SpookyTimeRotation.returningToPreviousRotation = false;
                SpookyTimeRotation.smoothingBackToPreviousRotation = false;
                SpookyTimeRotation.holdUntilTimeMs = 0L;
                SpookyTimeRotation.smoothingStartTimeMs = 0L;
            }
            return new Rotation(interpolatedYaw, interpolatedPitch);
        }
        else {
            SpookyTimeRotation.hadTargetOnPreviousTick = true;
            SpookyTimeRotation.returningToPreviousRotation = false;
            SpookyTimeRotation.smoothingBackToPreviousRotation = false;
            SpookyTimeRotation.holdUntilTimeMs = 0L;
            SpookyTimeRotation.smoothingStartTimeMs = 0L;
            ++SpookyTimeRotation.count;
            final boolean isAttackTick = this.clickScheduler != null && this.clickScheduler.isOneTickBeforeAttack();
            final boolean canSeeTarget = PlayerUtil.canSee(vec3d);
            if (!canSeeTarget) {
                return this.processBlockBypass(currentRotation, vec3d, entity, isAttackTick);
            }
            return this.processOpenSight(currentRotation, entity);
        }
    }
    
    private Rotation processBlockBypass(final Rotation currentRotation, final class_243 aimPoint, final class_1297 entity, final boolean isAttackTick) {
        final class_243 targetPos = aimPoint.method_1020(SpookyTimeRotation.mc.field_1724.method_33571()).method_1029();
        if (isAttackTick) {
            SpookyTimeRotation.attackSnapTicks = 6.0f;
        }
        boolean attack = false;
        if (SpookyTimeRotation.attackSnapTicks > 0.0f) {
            attack = true;
            --SpookyTimeRotation.attackSnapTicks;
        }
        final int mode = SpookyTimeRotation.count % 2;
        float angleX1;
        float angleY1;
        if (attack) {
            final double h = Math.hypot(targetPos.field_1352, targetPos.field_1350);
            angleX1 = (float)class_3532.method_15350(-Math.toDegrees(Math.atan2(targetPos.field_1351, h)), -90.0, 90.0);
            angleY1 = (float)Math.toDegrees(Math.atan2(-targetPos.field_1352, targetPos.field_1350));
        }
        else {
            angleX1 = switch (mode) {
                case 0 -> -87.0f + ThreadLocalRandom.current().nextFloat(-3.0f, 3.0f);
                case 1 -> 87.0f + ThreadLocalRandom.current().nextFloat(-3.0f, 3.0f);
                default -> currentRotation.getPitch();
            };
            angleY1 = class_3532.method_15393(currentRotation.getYaw() + ThreadLocalRandom.current().nextFloat(-3.0f, 3.0f));
        }
        final float lerpedYaw = rotLerp(angleY1, SpookyTimeRotation.mc.field_1724.method_36454(), 0.7f);
        final float lerpedPitch = rotLerp(angleX1, SpookyTimeRotation.mc.field_1724.method_36455(), 0.7f);
        return new Rotation(angleY1, lerpedPitch);
    }
    
    private Rotation processOpenSight(final Rotation currentRotation, final class_1297 entity) {
        if (entity instanceof final class_1309 livingEntity) {
            final class_243 predictedPos = getPredictedAimPoint(livingEntity).method_1020(SpookyTimeRotation.mc.field_1724.method_33571());
            final float yawToTarget = (float)class_3532.method_15338(Math.toDegrees(Math.atan2(predictedPos.field_1350, predictedPos.field_1352)) - 90.0);
            final float pitchToTarget = (float)(-Math.toDegrees(Math.atan2(predictedPos.field_1351, Math.hypot(predictedPos.field_1352, predictedPos.field_1350))));
            final float currentYaw = currentRotation.getYaw();
            final float currentPitch = currentRotation.getPitch();
            final float yawDelta = class_3532.method_15393(yawToTarget - currentYaw);
            final float pitchDelta = class_3532.method_15393(pitchToTarget - currentPitch);
            final float clampedYaw = Math.min(Math.max(Math.abs(yawDelta), 1.0f), 50.2f);
            final float clampedPitch = Math.min(Math.max(Math.abs(pitchDelta), 1.0f), 16.2f);
            final float targetYaw = currentYaw + ((yawDelta > 0.0f) ? clampedYaw : (-clampedYaw));
            final float targetPitch = currentPitch + ((pitchDelta > 0.0f) ? clampedPitch : (-clampedPitch));
            float yaw = rotLerp(currentYaw, targetYaw, 0.977f);
            float pitch = rotLerp(currentPitch, targetPitch, 0.977f);
            yaw += ThreadLocalRandom.current().nextFloat(-3.0f, 3.0f);
            pitch += ThreadLocalRandom.current().nextFloat(-3.0f, 3.0f);
            pitch = class_3532.method_15363(pitch, -90.0f, 90.0f);
            final float gcd = MouseUtil.getGCD();
            if (gcd > 0.0f) {
                yaw -= (yaw - currentYaw) % gcd;
                pitch -= (pitch - currentPitch) % gcd;
            }
            return new Rotation(yaw, pitch);
        }
        return currentRotation;
    }
    
    private static class_243 getPredictedAimPoint(final class_1309 t) {
        final double distanceToTarget = SpookyTimeRotation.mc.field_1724.method_5739((class_1297)t);
        float yaw = t.method_36454();
        float strength;
        if (distanceToTarget <= 2.0) {
            strength = 0.19f;
        }
        else {
            final float yawDelta = class_3532.method_15393(t.method_36454() - t.field_5982);
            yaw += yawDelta * 2.5f;
            strength = 0.15f;
        }
        final double yawRad = Math.toRadians(yaw);
        final class_243 forwardDirection = new class_243(-Math.sin(yawRad), 0.0, Math.cos(yawRad));
        return t.method_19538().method_1019(forwardDirection.method_1021((double)strength)).method_1031(0.0, t.method_17682() * 0.7, 0.0);
    }
    
    private static float rotLerp(final float from, final float to, final float progress) {
        return from + class_3532.method_15393(to - from) * progress;
    }
    
    public static boolean stalin(final class_1297 target) {
        if (SpookyTimeRotation.mc.field_1687 == null || SpookyTimeRotation.mc.field_1724 == null) {
            return false;
        }
        final class_243 pos = target.method_19538();
        final class_238 hitbox = target.method_5829();
        final float off = 0.05f;
        return !isAir(hitbox.field_1323 - off, pos.field_1351, hitbox.field_1321 - off) || !isAir(hitbox.field_1320 + off, pos.field_1351, hitbox.field_1321 - off) || !isAir(hitbox.field_1323 - off, pos.field_1351, hitbox.field_1324 + off) || !isAir(hitbox.field_1320 + off, pos.field_1351, hitbox.field_1324 + off);
    }
    
    public static boolean isAir(final double x, final double y, final double z) {
        return SpookyTimeRotation.mc.field_1687 != null && SpookyTimeRotation.mc.field_1687.method_8320(new class_2338(class_3532.method_15357(x), class_3532.method_15357(y), class_3532.method_15357(z))).method_26204() == class_2246.field_10124;
    }
    
    static {
        SpookyTimeRotation.attackSnapTicks = 0.0f;
        SpookyTimeRotation.count = 0;
        SpookyTimeRotation.returningToPreviousRotation = false;
        SpookyTimeRotation.holdUntilTimeMs = 0L;
        SpookyTimeRotation.smoothingBackToPreviousRotation = false;
        SpookyTimeRotation.smoothingStartTimeMs = 0L;
        SpookyTimeRotation.savedStartYaw = 0.0f;
        SpookyTimeRotation.savedStartPitch = 0.0f;
        SpookyTimeRotation.savedEndYaw = 0.0f;
        SpookyTimeRotation.savedEndPitch = 0.0f;
        SpookyTimeRotation.hadTargetOnPreviousTick = false;
    }
}
