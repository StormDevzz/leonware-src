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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/rotation/rotations/AimAssistRotation.class */
public class AimAssistRotation extends RotationMode {
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

    public AimAssistRotation(float yawMin, float yawMax, float pitchMin, float pitchMax, float timeoutMs) {
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
        this.noiseT = 0.0d;
        this.noiseStep = 0.037d + (Math.random() * 0.02d);
        this.mc = class_310.method_1551();
        this.yawMin = yawMin;
        this.yawMax = yawMax;
        this.pitchMin = pitchMin;
        this.pitchMax = pitchMax;
        this.timeoutMs = timeoutMs;
    }

    @Override // sweetie.leonware.api.utils.rotation.manager.RotationMode
    public Rotation process(Rotation currentRotation, Rotation targetRotation, class_243 targetVec, class_1297 entity) {
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
        Rotation playerRot = new Rotation(this.mc.field_1724.method_36454(), this.mc.field_1724.method_36455());
        Rotation aimTarget = getClosestPointRotation(entity, playerRot);
        if (aimTarget == null) {
            aimTarget = targetRotation;
        }
        boolean raytrace = isRaytracingTarget(entity);
        if (raytrace) {
            if (this.lockedSince == -1) {
                this.lockedSince = System.currentTimeMillis();
            }
            if (this.timeoutMs > 0.0f && System.currentTimeMillis() - this.lockedSince >= this.timeoutMs) {
                this.locked = true;
            }
        } else {
            this.lockedSince = -1L;
            this.locked = false;
        }
        if (this.locked) {
            return applyGcd(applyDrift(playerRot), playerRot);
        }
        updateBurstSpeed();
        updateHumanCurve();
        this.noiseT += this.noiseStep;
        Rotation delta = RotationUtil.calculateDelta(playerRot, aimTarget);
        float yawDelta = delta.getYaw();
        float pitchDelta = delta.getPitch();
        float absYaw = Math.abs(yawDelta);
        float absPitch = Math.abs(pitchDelta);
        float proximity = calculateProximity(absYaw);
        float speedYaw = (MathUtil.randomInRange(this.yawMin, this.yawMax) / 100.0f) * proximity * this.burstSpeed;
        float speedPitch = (MathUtil.randomInRange(this.pitchMin, this.pitchMax) / 100.0f) * proximity * this.burstSpeed;
        float targetMoveYaw = clamp(yawDelta * speedYaw, -absYaw, absYaw);
        float targetMovePitch = clamp(pitchDelta * speedPitch, -absPitch, absPitch);
        float lerpBase = 0.45f + (proximity * 0.2f);
        float lerpNoise = (float) (perlinNoise(this.noiseT) * 0.12d);
        float lerpFactor = class_3532.method_15363(lerpBase + lerpNoise + MathUtil.randomInRange(-0.04f, 0.04f), 0.15f, 0.92f);
        float moveYaw = this.prevMoveYaw + ((targetMoveYaw - this.prevMoveYaw) * lerpFactor);
        float movePitch = this.prevMovePitch + ((targetMovePitch - this.prevMovePitch) * lerpFactor);
        float moveYaw2 = moveYaw + ((float) (perlinNoise((this.noiseT * 1.7d) + 100.0d) * ((double) this.humanCurveAmp) * 0.6d));
        float movePitch2 = movePitch + ((float) (perlinNoise((this.noiseT * 1.3d) + 200.0d) * ((double) this.humanCurveAmp) * 0.3d));
        float moveYaw3 = moveYaw2 + ((float) (Math.sin(this.humanCurvePhase) * ((double) this.humanCurveAmp) * 0.4000000059604645d));
        float movePitch3 = movePitch2 + ((float) (Math.cos(((double) this.humanCurvePhase) * 0.7d) * ((double) this.humanCurveAmp) * 0.20000000298023224d));
        if (this.overshooting && this.overshootTicks > 0) {
            moveYaw3 += this.overshootYaw * (this.overshootTicks / 4.0f);
            movePitch3 += this.overshootPitch * (this.overshootTicks / 4.0f);
            this.overshootTicks--;
            if (this.overshootTicks <= 0) {
                this.overshooting = false;
            }
        } else if (!this.overshooting && absYaw < 2.0f && Math.random() < 0.04d) {
            this.overshooting = true;
            this.overshootTicks = MathUtil.randomInRange(2, 5);
            this.overshootYaw = MathUtil.randomInRange(-0.15f, 0.15f);
            this.overshootPitch = MathUtil.randomInRange(-0.08f, 0.08f);
        }
        float moveYaw4 = moveYaw3 + MathUtil.randomInRange(-0.008f, 0.008f);
        float movePitch4 = movePitch3 + MathUtil.randomInRange(-0.004f, 0.004f);
        this.prevMoveYaw = moveYaw4;
        this.prevMovePitch = movePitch4;
        Rotation raw = new Rotation(playerRot.getYaw() + moveYaw4, class_3532.method_15363(playerRot.getPitch() + movePitch4, -90.0f, 90.0f));
        return applyGcd(raw, playerRot);
    }

    private void updateBurstSpeed() {
        long now = System.currentTimeMillis();
        if (now > this.nextBurstChange) {
            this.burstSpeed = 0.7f + ((float) (Math.random() * 0.65d));
            this.nextBurstChange = now + ((long) MathUtil.randomInRange(120, 380));
        }
    }

    private void updateHumanCurve() {
        long now = System.currentTimeMillis();
        if (now > this.nextCurveReset) {
            this.humanCurveFreq = 0.03f + MathUtil.randomInRange(0.0f, 0.04f);
            this.humanCurveAmp = MathUtil.randomInRange(0.005f, 0.025f);
            this.nextCurveReset = now + ((long) MathUtil.randomInRange(200, 600));
        }
        this.humanCurvePhase += this.humanCurveFreq + MathUtil.randomInRange(-0.003f, 0.003f);
    }

    private double perlinNoise(double t) {
        int ti = (int) Math.floor(t);
        double f = t - ((double) ti);
        double u = f * f * (3.0d - (2.0d * f));
        double a = pseudoRand(ti);
        double b = pseudoRand(ti + 1);
        return a + ((b - a) * u);
    }

    private double pseudoRand(int n) {
        int n2 = (n << 13) ^ n;
        return 1.0d - (((double) (((n2 * (((n2 * n2) * 15731) + 789221)) + 1376312589) & Integer.MAX_VALUE)) / 1.073741824E9d);
    }

    private Rotation applyGcd(Rotation target, Rotation from) {
        double gcd = MouseUtil.getGCD();
        if (gcd <= 0.0d) {
            return target;
        }
        float deltaYaw = target.getYaw() - from.getYaw();
        float deltaPitch = target.getPitch() - from.getPitch();
        float snappedYaw = from.getYaw() + ((float) (Math.round(((double) deltaYaw) / gcd) * gcd));
        float snappedPitch = from.getPitch() + ((float) (Math.round(((double) deltaPitch) / gcd) * gcd));
        return new Rotation(snappedYaw, class_3532.method_15363(snappedPitch, -90.0f, 90.0f));
    }

    private Rotation getClosestPointRotation(class_1297 entity, Rotation playerRot) {
        if (entity == null) {
            return null;
        }
        class_243 eyes = this.mc.field_1724.method_33571();
        class_243 look = playerRot.getVector();
        class_243 end = eyes.method_1019(look.method_1021(6.0d));
        class_243 closest = (class_243) entity.method_5829().method_992(eyes, end).orElse(null);
        if (closest == null) {
            class_243 c = entity.method_5829().method_1005();
            class_243 top = new class_243(c.field_1352, entity.method_5829().field_1325 - 0.1d, c.field_1350);
            class_243 bot = new class_243(c.field_1352, entity.method_5829().field_1322 + 0.1d, c.field_1350);
            closest = top.method_1025(end) < bot.method_1025(end) ? top : bot;
        }
        return RotationUtil.fromVec3d(closest.method_1020(eyes));
    }

    private boolean isRaytracingTarget(class_1297 entity) {
        if (entity == null) {
            return false;
        }
        Rotation cur = RotationManager.getInstance().getRotation();
        class_3966 hit = RaytracingUtil.raytraceEntity(6.0d, cur, false);
        return hit != null && hit.method_17782() == entity;
    }

    private Rotation applyDrift(Rotation base) {
        long now = System.currentTimeMillis();
        if (now > this.nextDrift) {
            this.driftYaw = MathUtil.randomInRange(-0.07f, 0.07f);
            this.driftPitch = MathUtil.randomInRange(-0.035f, 0.035f);
            this.nextDrift = now + ((long) MathUtil.randomInRange(50, 170));
        }
        float noiseYaw = (float) (perlinNoise((this.noiseT * 2.1d) + 50.0d) * 0.015d);
        float noisePitch = (float) (perlinNoise((this.noiseT * 1.9d) + 80.0d) * 0.008d);
        return new Rotation(base.getYaw() + this.driftYaw + noiseYaw, class_3532.method_15363(base.getPitch() + this.driftPitch + noisePitch, -90.0f, 90.0f));
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
        return absYaw < 45.0f ? 0.85f : 0.93f;
    }

    private float clamp(float val, float min, float max) {
        if (min > max) {
            min = max;
            max = min;
        }
        return Math.max(min, Math.min(max, val));
    }
}
