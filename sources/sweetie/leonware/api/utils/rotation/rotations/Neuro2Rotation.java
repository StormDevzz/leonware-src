package sweetie.leonware.api.utils.rotation.rotations;

import net.minecraft.class_1297;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.minecraft.class_3966;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.api.utils.math.MouseUtil;
import sweetie.leonware.api.utils.neuro.Neuro2DataCollector;
import sweetie.leonware.api.utils.neuro.Neuro2Model;
import sweetie.leonware.api.utils.rotation.RaytracingUtil;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationMode;
import sweetie.leonware.client.features.modules.combat.AuraModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/rotation/rotations/Neuro2Rotation.class */
public class Neuro2Rotation extends RotationMode {
    private final Neuro2Model model;
    private float lastYaw;
    private float lastPitch;
    private float smoothedYawOffset;
    private float smoothedPitchOffset;
    private float currentYawVelocity;
    private float currentPitchVelocity;
    private float leadInFactor;
    private float slowYawTicks;
    private float slowPitchTicks;
    private float noiseFactor;
    private long noiseStartTime;
    private final double noiseOffY;
    private final double noiseOffP;

    public Neuro2Rotation(Neuro2Model model) {
        super("Neuro2");
        this.lastYaw = Float.NaN;
        this.lastPitch = Float.NaN;
        this.smoothedYawOffset = 0.0f;
        this.smoothedPitchOffset = 0.0f;
        this.currentYawVelocity = 0.0f;
        this.currentPitchVelocity = 0.0f;
        this.leadInFactor = 0.0f;
        this.slowYawTicks = 1.0f;
        this.slowPitchTicks = 1.0f;
        this.noiseFactor = 1.0f;
        this.noiseStartTime = System.currentTimeMillis();
        this.noiseOffY = MathUtil.randomInRange(0, 1000);
        this.noiseOffP = MathUtil.randomInRange(0, 1000);
        this.model = model;
    }

    @Override // sweetie.leonware.api.utils.rotation.manager.RotationMode
    public Rotation process(Rotation currentRotation, Rotation targetRotation, class_243 vec3d, class_1297 entity) {
        if (Neuro2DataCollector.getInstance().isRecording()) {
            return new Rotation(mc.field_1724.method_36454(), mc.field_1724.method_36455());
        }
        if (entity == null || mc.field_1724 == null || this.model == null || !this.model.isTrained()) {
            this.leadInFactor = 0.0f;
            if (Float.isNaN(this.lastYaw)) {
                return currentRotation;
            }
            this.lastYaw += class_3532.method_15393(mc.field_1724.method_36454() - this.lastYaw) * 0.25f;
            this.lastPitch += (mc.field_1724.method_36455() - this.lastPitch) * 0.25f;
            this.currentYawVelocity *= 0.6f;
            this.currentPitchVelocity *= 0.6f;
            float recoveryYaw = this.lastYaw + this.currentYawVelocity;
            float recoveryPitch = class_3532.method_15363(this.lastPitch + this.currentPitchVelocity, -90.0f, 90.0f);
            if (Math.abs(class_3532.method_15393(recoveryYaw - mc.field_1724.method_36454())) < 0.5f && Math.abs(recoveryPitch - mc.field_1724.method_36455()) < 0.5f) {
                this.lastYaw = Float.NaN;
                this.lastPitch = Float.NaN;
                return new Rotation(mc.field_1724.method_36454(), mc.field_1724.method_36455());
            }
            this.lastYaw = recoveryYaw;
            this.lastPitch = recoveryPitch;
            return applyGcd(recoveryYaw, recoveryPitch, currentRotation);
        }
        if (Float.isNaN(this.lastYaw)) {
            this.lastYaw = currentRotation.getYaw();
            this.lastPitch = currentRotation.getPitch();
        }
        this.leadInFactor = MathUtil.interpolate(this.leadInFactor, 1.0f, 0.05f);
        Rotation perfect = RotationUtil.rotationAt(entity.method_19538().method_1031(0.0d, entity.method_17682() / 2.0f, 0.0d));
        float perfectYaw = perfect.getYaw();
        float perfectPitch = class_3532.method_15363(perfect.getPitch(), -90.0f, 90.0f);
        float auraDistance = AuraModule.getInstance().getAttackDistance();
        class_3966 hitResult = RaytracingUtil.raytraceEntity(auraDistance, currentRotation, false);
        boolean hasTrace = hitResult != null && hitResult.method_17782() == entity;
        if (hasTrace) {
            this.slowYawTicks = MathUtil.interpolate(this.slowYawTicks, 0.75f, 0.1f);
            this.slowPitchTicks = MathUtil.interpolate(this.slowPitchTicks, 0.55f, 0.1f);
        } else {
            this.slowYawTicks = MathUtil.interpolate(this.slowYawTicks, 1.0f, 0.05f);
            this.slowPitchTicks = MathUtil.interpolate(this.slowPitchTicks, 1.0f, 0.05f);
        }
        double distance = mc.field_1724.method_19538().method_1022(entity.method_19538());
        double targetSpeed = entity.method_18798().method_37267();
        float[] offsets = this.model.predict(distance, targetSpeed);
        float offsetSmoothing = MathUtil.randomInRange(0.12f, 0.28f);
        this.smoothedYawOffset += (offsets[0] - this.smoothedYawOffset) * offsetSmoothing;
        this.smoothedPitchOffset += (offsets[1] - this.smoothedPitchOffset) * offsetSmoothing;
        float targetYaw = perfectYaw + (this.smoothedYawOffset * this.leadInFactor);
        float targetPitch = perfectPitch + (this.smoothedPitchOffset * this.leadInFactor);
        long elapsed = System.currentTimeMillis() - this.noiseStartTime;
        float yawNoise = ((float) perlin(this.noiseOffY + (elapsed * 2.8E-4d))) * 1.1f * this.noiseFactor;
        float pitchNoise = ((float) perlin(this.noiseOffP + (elapsed * 2.8E-4d))) * 1.2f * this.noiseFactor;
        float targetYaw2 = targetYaw + yawNoise;
        float targetPitch2 = targetPitch + pitchNoise;
        float totalDiff = Math.abs(class_3532.method_15393(targetYaw2 - perfectYaw)) + Math.abs(targetPitch2 - perfectPitch);
        if (totalDiff < 8.0f) {
            this.noiseFactor = Math.max(0.0f, this.noiseFactor - 0.045f);
        }
        float yawDiff = class_3532.method_15393(targetYaw2 - this.lastYaw);
        float pitchDiff = targetPitch2 - this.lastPitch;
        float accel = MathUtil.randomInRange(0.36f, 0.52f) * this.slowYawTicks * this.leadInFactor;
        float friction = MathUtil.randomInRange(0.48f, 0.68f);
        this.currentYawVelocity = (this.currentYawVelocity + (yawDiff * accel)) * friction;
        this.currentPitchVelocity = (this.currentPitchVelocity + (pitchDiff * accel * this.slowPitchTicks)) * friction;
        float finalYaw = this.lastYaw + this.currentYawVelocity;
        float finalPitch = class_3532.method_15363(this.lastPitch + this.currentPitchVelocity, -90.0f, 90.0f);
        this.lastYaw = finalYaw;
        this.lastPitch = finalPitch;
        return applyGcd(finalYaw, finalPitch, currentRotation);
    }

    private Rotation applyGcd(float newYaw, float newPitch, Rotation prev) {
        float gcd = MouseUtil.getGCD();
        if (gcd <= 0.0f) {
            return new Rotation(newYaw, newPitch);
        }
        float yaw = prev.getYaw() + (Math.round(class_3532.method_15393(newYaw - prev.getYaw()) / gcd) * gcd);
        float pitch = prev.getPitch() + (Math.round((newPitch - prev.getPitch()) / gcd) * gcd);
        return new Rotation(yaw, class_3532.method_15363(pitch, -90.0f, 90.0f));
    }

    private double perlin(double x) {
        int xi = (int) Math.floor(x);
        double xf = x - ((double) xi);
        double u = xf * xf * xf * ((xf * ((xf * 6.0d) - 15.0d)) + 10.0d);
        return lerp(u, grad(hash(xi), xf), grad(hash(xi + 1), xf - 1.0d));
    }

    private double lerp(double t, double a, double b) {
        return a + (t * (b - a));
    }

    private double grad(int h, double x) {
        return (h & 1) == 0 ? x : -x;
    }

    private int hash(int x) {
        int x2 = ((x >>> 16) ^ x) * 73244475;
        int x3 = ((x2 >>> 16) ^ x2) * 73244475;
        return (x3 >>> 16) ^ (x3 & 255);
    }
}
