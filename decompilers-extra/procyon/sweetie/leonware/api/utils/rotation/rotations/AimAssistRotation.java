// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.rotation.rotations;

import net.minecraft.class_3966;
import sweetie.leonware.api.utils.rotation.RaytracingUtil;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.utils.math.MouseUtil;
import net.minecraft.class_3532;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import net.minecraft.class_243;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import net.minecraft.class_310;
import net.minecraft.class_1297;
import sweetie.leonware.api.utils.rotation.manager.RotationMode;

public class AimAssistRotation extends RotationMode
{
    private final float yawMin;
    private final float yawMax;
    private final float pitchMin;
    private final float pitchMax;
    private final float timeoutMs;
    private long lockedSince;
    private boolean locked;
    private class_1297 lastEntity;
    private float driftYaw;
    private float driftPitch;
    private long nextDrift;
    private float prevMoveYaw;
    private float prevMovePitch;
    private float humanCurvePhase;
    private float humanCurveFreq;
    private float humanCurveAmp;
    private long nextCurveReset;
    private float overshootYaw;
    private float overshootPitch;
    private boolean overshooting;
    private int overshootTicks;
    private float burstSpeed;
    private long nextBurstChange;
    private double noiseT;
    private final double noiseStep;
    private final class_310 mc;
    
    public AimAssistRotation(final float yawMin, final float yawMax, final float pitchMin, final float pitchMax, final float timeoutMs) {
        super("Aim Assist");
        this.lockedSince = -1L;
        this.locked = false;
        this.lastEntity = null;
        this.driftYaw = 0.0f;
        this.driftPitch = 0.0f;
        this.nextDrift = 0L;
        this.prevMoveYaw = 0.0f;
        this.prevMovePitch = 0.0f;
        this.humanCurvePhase = 0.0f;
        this.humanCurveFreq = 0.0f;
        this.humanCurveAmp = 0.0f;
        this.nextCurveReset = 0L;
        this.overshootYaw = 0.0f;
        this.overshootPitch = 0.0f;
        this.overshooting = false;
        this.overshootTicks = 0;
        this.burstSpeed = 1.0f;
        this.nextBurstChange = 0L;
        this.noiseT = 0.0;
        this.noiseStep = 0.037 + Math.random() * 0.02;
        this.mc = class_310.method_1551();
        this.yawMin = yawMin;
        this.yawMax = yawMax;
        this.pitchMin = pitchMin;
        this.pitchMax = pitchMax;
        this.timeoutMs = timeoutMs;
    }
    
    @Override
    public Rotation process(final Rotation currentRotation, final Rotation targetRotation, final class_243 targetVec, final class_1297 entity) {
        if (this.mc.field_1724 == null) {
            return currentRotation;
        }
        if (entity != this.lastEntity) {
            this.lockedSince = -1L;
            this.locked = false;
            this.prevMoveYaw = 0.0f;
            this.prevMovePitch = 0.0f;
            this.overshooting = false;
            this.overshootTicks = 0;
            this.burstSpeed = 1.0f;
            this.humanCurvePhase = 0.0f;
            this.lastEntity = entity;
        }
        final Rotation playerRot = new Rotation(this.mc.field_1724.method_36454(), this.mc.field_1724.method_36455());
        Rotation aimTarget = this.getClosestPointRotation(entity, playerRot);
        if (aimTarget == null) {
            aimTarget = targetRotation;
        }
        final boolean raytrace = this.isRaytracingTarget(entity);
        if (raytrace) {
            if (this.lockedSince == -1L) {
                this.lockedSince = System.currentTimeMillis();
            }
            if (this.timeoutMs > 0.0f && System.currentTimeMillis() - this.lockedSince >= this.timeoutMs) {
                this.locked = true;
            }
        }
        else {
            this.lockedSince = -1L;
            this.locked = false;
        }
        if (this.locked) {
            return this.applyGcd(this.applyDrift(playerRot), playerRot);
        }
        this.updateBurstSpeed();
        this.updateHumanCurve();
        this.noiseT += this.noiseStep;
        final Rotation delta = RotationUtil.calculateDelta(playerRot, aimTarget);
        final float yawDelta = delta.getYaw();
        final float pitchDelta = delta.getPitch();
        final float absYaw = Math.abs(yawDelta);
        final float absPitch = Math.abs(pitchDelta);
        final float proximity = this.calculateProximity(absYaw);
        final float speedYaw = MathUtil.randomInRange(this.yawMin, this.yawMax) / 100.0f * proximity * this.burstSpeed;
        final float speedPitch = MathUtil.randomInRange(this.pitchMin, this.pitchMax) / 100.0f * proximity * this.burstSpeed;
        final float targetMoveYaw = this.clamp(yawDelta * speedYaw, -absYaw, absYaw);
        final float targetMovePitch = this.clamp(pitchDelta * speedPitch, -absPitch, absPitch);
        final float lerpBase = 0.45f + proximity * 0.2f;
        final float lerpNoise = (float)(this.perlinNoise(this.noiseT) * 0.12);
        final float lerpFactor = class_3532.method_15363(lerpBase + lerpNoise + MathUtil.randomInRange(-0.04f, 0.04f), 0.15f, 0.92f);
        float moveYaw = this.prevMoveYaw + (targetMoveYaw - this.prevMoveYaw) * lerpFactor;
        float movePitch = this.prevMovePitch + (targetMovePitch - this.prevMovePitch) * lerpFactor;
        moveYaw += (float)(this.perlinNoise(this.noiseT * 1.7 + 100.0) * this.humanCurveAmp * 0.6);
        movePitch += (float)(this.perlinNoise(this.noiseT * 1.3 + 200.0) * this.humanCurveAmp * 0.3);
        moveYaw += (float)(Math.sin(this.humanCurvePhase) * this.humanCurveAmp * 0.4000000059604645);
        movePitch += (float)(Math.cos(this.humanCurvePhase * 0.7) * this.humanCurveAmp * 0.20000000298023224);
        if (this.overshooting && this.overshootTicks > 0) {
            moveYaw += this.overshootYaw * (this.overshootTicks / 4.0f);
            movePitch += this.overshootPitch * (this.overshootTicks / 4.0f);
            --this.overshootTicks;
            if (this.overshootTicks <= 0) {
                this.overshooting = false;
            }
        }
        else if (!this.overshooting && absYaw < 2.0f && Math.random() < 0.04) {
            this.overshooting = true;
            this.overshootTicks = MathUtil.randomInRange(2, 5);
            this.overshootYaw = MathUtil.randomInRange(-0.15f, 0.15f);
            this.overshootPitch = MathUtil.randomInRange(-0.08f, 0.08f);
        }
        moveYaw += MathUtil.randomInRange(-0.008f, 0.008f);
        movePitch += MathUtil.randomInRange(-0.004f, 0.004f);
        this.prevMoveYaw = moveYaw;
        this.prevMovePitch = movePitch;
        final Rotation raw = new Rotation(playerRot.getYaw() + moveYaw, class_3532.method_15363(playerRot.getPitch() + movePitch, -90.0f, 90.0f));
        return this.applyGcd(raw, playerRot);
    }
    
    private void updateBurstSpeed() {
        final long now = System.currentTimeMillis();
        if (now > this.nextBurstChange) {
            this.burstSpeed = 0.7f + (float)(Math.random() * 0.65);
            this.nextBurstChange = now + MathUtil.randomInRange(120, 380);
        }
    }
    
    private void updateHumanCurve() {
        final long now = System.currentTimeMillis();
        if (now > this.nextCurveReset) {
            this.humanCurveFreq = 0.03f + MathUtil.randomInRange(0.0f, 0.04f);
            this.humanCurveAmp = MathUtil.randomInRange(0.005f, 0.025f);
            this.nextCurveReset = now + MathUtil.randomInRange(200, 600);
        }
        this.humanCurvePhase += this.humanCurveFreq + MathUtil.randomInRange(-0.003f, 0.003f);
    }
    
    private double perlinNoise(final double t) {
        final int ti = (int)Math.floor(t);
        final double f = t - ti;
        final double u = f * f * (3.0 - 2.0 * f);
        final double a = this.pseudoRand(ti);
        final double b = this.pseudoRand(ti + 1);
        return a + (b - a) * u;
    }
    
    private double pseudoRand(int n) {
        n ^= n << 13;
        return 1.0 - (n * (n * n * 15731 + 789221) + 1376312589 & Integer.MAX_VALUE) / 1.073741824E9;
    }
    
    private Rotation applyGcd(final Rotation target, final Rotation from) {
        final double gcd = MouseUtil.getGCD();
        if (gcd <= 0.0) {
            return target;
        }
        final float deltaYaw = target.getYaw() - from.getYaw();
        final float deltaPitch = target.getPitch() - from.getPitch();
        final float snappedYaw = from.getYaw() + (float)(Math.round(deltaYaw / gcd) * gcd);
        final float snappedPitch = from.getPitch() + (float)(Math.round(deltaPitch / gcd) * gcd);
        return new Rotation(snappedYaw, class_3532.method_15363(snappedPitch, -90.0f, 90.0f));
    }
    
    private Rotation getClosestPointRotation(final class_1297 entity, final Rotation playerRot) {
        if (entity == null) {
            return null;
        }
        final class_243 eyes = this.mc.field_1724.method_33571();
        final class_243 look = playerRot.getVector();
        final class_243 end = eyes.method_1019(look.method_1021(6.0));
        class_243 closest = entity.method_5829().method_992(eyes, end).orElse(null);
        if (closest == null) {
            final class_243 c = entity.method_5829().method_1005();
            final class_243 top = new class_243(c.field_1352, entity.method_5829().field_1325 - 0.1, c.field_1350);
            final class_243 bot = new class_243(c.field_1352, entity.method_5829().field_1322 + 0.1, c.field_1350);
            closest = ((top.method_1025(end) < bot.method_1025(end)) ? top : bot);
        }
        return RotationUtil.fromVec3d(closest.method_1020(eyes));
    }
    
    private boolean isRaytracingTarget(final class_1297 entity) {
        if (entity == null) {
            return false;
        }
        final Rotation cur = RotationManager.getInstance().getRotation();
        final class_3966 hit = RaytracingUtil.raytraceEntity(6.0, cur, false);
        return hit != null && hit.method_17782() == entity;
    }
    
    private Rotation applyDrift(final Rotation base) {
        final long now = System.currentTimeMillis();
        if (now > this.nextDrift) {
            this.driftYaw = MathUtil.randomInRange(-0.07f, 0.07f);
            this.driftPitch = MathUtil.randomInRange(-0.035f, 0.035f);
            this.nextDrift = now + MathUtil.randomInRange(50, 170);
        }
        final float noiseYaw = (float)(this.perlinNoise(this.noiseT * 2.1 + 50.0) * 0.015);
        final float noisePitch = (float)(this.perlinNoise(this.noiseT * 1.9 + 80.0) * 0.008);
        return new Rotation(base.getYaw() + this.driftYaw + noiseYaw, class_3532.method_15363(base.getPitch() + this.driftPitch + noisePitch, -90.0f, 90.0f));
    }
    
    private float calculateProximity(final float absYaw) {
        if (absYaw < 1.0f) {
            return 0.12f;
        }
        if (absYaw < 3.0f) {
            return 0.28f;
        }
        if (absYaw < 8.0f) {
            return 0.5f;
        }
        if (absYaw < 20.0f) {
            return 0.7f;
        }
        if (absYaw < 45.0f) {
            return 0.85f;
        }
        return 0.93f;
    }
    
    private float clamp(final float val, float min, float max) {
        if (min > max) {
            final float t = min;
            min = max;
            max = t;
        }
        return Math.max(min, Math.min(max, val));
    }
}
