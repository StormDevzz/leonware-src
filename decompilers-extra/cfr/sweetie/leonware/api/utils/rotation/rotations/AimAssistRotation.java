/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  net.minecraft.class_243
 *  net.minecraft.class_310
 *  net.minecraft.class_3532
 *  net.minecraft.class_3966
 */
package sweetie.leonware.api.utils.rotation.rotations;

import net.minecraft.class_1297;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_3966;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.api.utils.math.MouseUtil;
import sweetie.leonware.api.utils.rotation.RaytracingUtil;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.utils.rotation.manager.RotationMode;

public class AimAssistRotation
extends RotationMode {
    private final float yawMin;
    private final float yawMax;
    private final float pitchMin;
    private final float pitchMax;
    private final float timeoutMs;
    private long lockedSince = -1L;
    private boolean locked = false;
    private class_1297 lastEntity = null;
    private float driftYaw = 0.0f;
    private float driftPitch = 0.0f;
    private long nextDrift = 0L;
    private float prevMoveYaw = 0.0f;
    private float prevMovePitch = 0.0f;
    private float humanCurvePhase = 0.0f;
    private float humanCurveFreq = 0.0f;
    private float humanCurveAmp = 0.0f;
    private long nextCurveReset = 0L;
    private float overshootYaw = 0.0f;
    private float overshootPitch = 0.0f;
    private boolean overshooting = false;
    private int overshootTicks = 0;
    private float burstSpeed = 1.0f;
    private long nextBurstChange = 0L;
    private double noiseT = 0.0;
    private final double noiseStep = 0.037 + Math.random() * 0.02;
    private final class_310 mc = class_310.method_1551();

    public AimAssistRotation(float yawMin, float yawMax, float pitchMin, float pitchMax, float timeoutMs) {
        super("Aim Assist");
        this.yawMin = yawMin;
        this.yawMax = yawMax;
        this.pitchMin = pitchMin;
        this.pitchMax = pitchMax;
        this.timeoutMs = timeoutMs;
    }

    @Override
    public Rotation process(Rotation currentRotation, Rotation targetRotation, class_243 targetVec, class_1297 entity) {
        boolean raytrace;
        Rotation playerRot;
        Rotation aimTarget;
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
        if ((aimTarget = this.getClosestPointRotation(entity, playerRot = new Rotation(this.mc.field_1724.method_36454(), this.mc.field_1724.method_36455()))) == null) {
            aimTarget = targetRotation;
        }
        if (raytrace = this.isRaytracingTarget(entity)) {
            if (this.lockedSince == -1L) {
                this.lockedSince = System.currentTimeMillis();
            }
            if (this.timeoutMs > 0.0f && (float)(System.currentTimeMillis() - this.lockedSince) >= this.timeoutMs) {
                this.locked = true;
            }
        } else {
            this.lockedSince = -1L;
            this.locked = false;
        }
        if (this.locked) {
            return this.applyGcd(this.applyDrift(playerRot), playerRot);
        }
        this.updateBurstSpeed();
        this.updateHumanCurve();
        this.noiseT += this.noiseStep;
        Rotation delta = RotationUtil.calculateDelta(playerRot, aimTarget);
        float yawDelta = delta.getYaw();
        float pitchDelta = delta.getPitch();
        float absYaw = Math.abs(yawDelta);
        float absPitch = Math.abs(pitchDelta);
        float proximity = this.calculateProximity(absYaw);
        float speedYaw = MathUtil.randomInRange(this.yawMin, this.yawMax) / 100.0f * proximity * this.burstSpeed;
        float speedPitch = MathUtil.randomInRange(this.pitchMin, this.pitchMax) / 100.0f * proximity * this.burstSpeed;
        float targetMoveYaw = this.clamp(yawDelta * speedYaw, -absYaw, absYaw);
        float targetMovePitch = this.clamp(pitchDelta * speedPitch, -absPitch, absPitch);
        float lerpBase = 0.45f + proximity * 0.2f;
        float lerpNoise = (float)(this.perlinNoise(this.noiseT) * 0.12);
        float lerpFactor = class_3532.method_15363((float)(lerpBase + lerpNoise + MathUtil.randomInRange(-0.04f, 0.04f)), (float)0.15f, (float)0.92f);
        float moveYaw = this.prevMoveYaw + (targetMoveYaw - this.prevMoveYaw) * lerpFactor;
        float movePitch = this.prevMovePitch + (targetMovePitch - this.prevMovePitch) * lerpFactor;
        moveYaw += (float)(this.perlinNoise(this.noiseT * 1.7 + 100.0) * (double)this.humanCurveAmp * 0.6);
        movePitch += (float)(this.perlinNoise(this.noiseT * 1.3 + 200.0) * (double)this.humanCurveAmp * 0.3);
        moveYaw += (float)(Math.sin(this.humanCurvePhase) * (double)this.humanCurveAmp * (double)0.4f);
        movePitch += (float)(Math.cos((double)this.humanCurvePhase * 0.7) * (double)this.humanCurveAmp * (double)0.2f);
        if (this.overshooting && this.overshootTicks > 0) {
            moveYaw += this.overshootYaw * ((float)this.overshootTicks / 4.0f);
            movePitch += this.overshootPitch * ((float)this.overshootTicks / 4.0f);
            --this.overshootTicks;
            if (this.overshootTicks <= 0) {
                this.overshooting = false;
            }
        } else if (!this.overshooting && absYaw < 2.0f && Math.random() < 0.04) {
            this.overshooting = true;
            this.overshootTicks = MathUtil.randomInRange(2, 5);
            this.overshootYaw = MathUtil.randomInRange(-0.15f, 0.15f);
            this.overshootPitch = MathUtil.randomInRange(-0.08f, 0.08f);
        }
        this.prevMoveYaw = moveYaw += MathUtil.randomInRange(-0.008f, 0.008f);
        this.prevMovePitch = movePitch += MathUtil.randomInRange(-0.004f, 0.004f);
        Rotation raw = new Rotation(playerRot.getYaw() + moveYaw, class_3532.method_15363((float)(playerRot.getPitch() + movePitch), (float)-90.0f, (float)90.0f));
        return this.applyGcd(raw, playerRot);
    }

    private void updateBurstSpeed() {
        long now = System.currentTimeMillis();
        if (now > this.nextBurstChange) {
            this.burstSpeed = 0.7f + (float)(Math.random() * 0.65);
            this.nextBurstChange = now + (long)MathUtil.randomInRange(120, 380);
        }
    }

    private void updateHumanCurve() {
        long now = System.currentTimeMillis();
        if (now > this.nextCurveReset) {
            this.humanCurveFreq = 0.03f + MathUtil.randomInRange(0.0f, 0.04f);
            this.humanCurveAmp = MathUtil.randomInRange(0.005f, 0.025f);
            this.nextCurveReset = now + (long)MathUtil.randomInRange(200, 600);
        }
        this.humanCurvePhase += this.humanCurveFreq + MathUtil.randomInRange(-0.003f, 0.003f);
    }

    private double perlinNoise(double t) {
        int ti = (int)Math.floor(t);
        double f = t - (double)ti;
        double u = f * f * (3.0 - 2.0 * f);
        double a = this.pseudoRand(ti);
        double b = this.pseudoRand(ti + 1);
        return a + (b - a) * u;
    }

    private double pseudoRand(int n) {
        n = n << 13 ^ n;
        return 1.0 - (double)(n * (n * n * 15731 + 789221) + 1376312589 & Integer.MAX_VALUE) / 1.073741824E9;
    }

    private Rotation applyGcd(Rotation target, Rotation from) {
        double gcd = MouseUtil.getGCD();
        if (gcd <= 0.0) {
            return target;
        }
        float deltaYaw = target.getYaw() - from.getYaw();
        float deltaPitch = target.getPitch() - from.getPitch();
        float snappedYaw = from.getYaw() + (float)((double)Math.round((double)deltaYaw / gcd) * gcd);
        float snappedPitch = from.getPitch() + (float)((double)Math.round((double)deltaPitch / gcd) * gcd);
        return new Rotation(snappedYaw, class_3532.method_15363((float)snappedPitch, (float)-90.0f, (float)90.0f));
    }

    private Rotation getClosestPointRotation(class_1297 entity, Rotation playerRot) {
        if (entity == null) {
            return null;
        }
        class_243 eyes = this.mc.field_1724.method_33571();
        class_243 look = playerRot.getVector();
        class_243 end = eyes.method_1019(look.method_1021(6.0));
        class_243 closest = entity.method_5829().method_992(eyes, end).orElse(null);
        if (closest == null) {
            class_243 c = entity.method_5829().method_1005();
            class_243 top = new class_243(c.field_1352, entity.method_5829().field_1325 - 0.1, c.field_1350);
            class_243 bot = new class_243(c.field_1352, entity.method_5829().field_1322 + 0.1, c.field_1350);
            closest = top.method_1025(end) < bot.method_1025(end) ? top : bot;
        }
        return RotationUtil.fromVec3d(closest.method_1020(eyes));
    }

    private boolean isRaytracingTarget(class_1297 entity) {
        if (entity == null) {
            return false;
        }
        Rotation cur = RotationManager.getInstance().getRotation();
        class_3966 hit = RaytracingUtil.raytraceEntity(6.0, cur, false);
        return hit != null && hit.method_17782() == entity;
    }

    private Rotation applyDrift(Rotation base) {
        long now = System.currentTimeMillis();
        if (now > this.nextDrift) {
            this.driftYaw = MathUtil.randomInRange(-0.07f, 0.07f);
            this.driftPitch = MathUtil.randomInRange(-0.035f, 0.035f);
            this.nextDrift = now + (long)MathUtil.randomInRange(50, 170);
        }
        float noiseYaw = (float)(this.perlinNoise(this.noiseT * 2.1 + 50.0) * 0.015);
        float noisePitch = (float)(this.perlinNoise(this.noiseT * 1.9 + 80.0) * 0.008);
        return new Rotation(base.getYaw() + this.driftYaw + noiseYaw, class_3532.method_15363((float)(base.getPitch() + this.driftPitch + noisePitch), (float)-90.0f, (float)90.0f));
    }

    private float calculateProximity(float absYaw) {
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

    private float clamp(float val, float min, float max) {
        if (min > max) {
            float t = min;
            min = max;
            max = t;
        }
        return Math.max(min, Math.min(max, val));
    }
}

