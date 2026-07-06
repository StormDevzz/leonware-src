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
import sweetie.leonware.api.utils.math.MouseUtil;
import sweetie.leonware.api.utils.neuro.Neuro2DataCollector;
import sweetie.leonware.api.utils.neuro.Neuro2Model;
import sweetie.leonware.api.utils.rotation.RaytracingUtil;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationMode;
import sweetie.leonware.client.features.modules.combat.AuraModule;

public class Neuro2Rotation
extends RotationMode {
    private final Neuro2Model model;
    private float lastYaw = Float.NaN;
    private float lastPitch = Float.NaN;
    private float smoothedYawOffset = 0.0f;
    private float smoothedPitchOffset = 0.0f;
    private float currentYawVelocity = 0.0f;
    private float currentPitchVelocity = 0.0f;
    private float leadInFactor = 0.0f;
    private float slowYawTicks = 1.0f;
    private float slowPitchTicks = 1.0f;
    private float noiseFactor = 1.0f;
    private long noiseStartTime = System.currentTimeMillis();
    private final double noiseOffY = MathUtil.randomInRange(0, 1000);
    private final double noiseOffP = MathUtil.randomInRange(0, 1000);

    public Neuro2Rotation(Neuro2Model model) {
        super("Neuro2");
        this.model = model;
    }

    @Override
    public Rotation process(Rotation currentRotation, Rotation targetRotation, class_243 vec3d, class_1297 entity) {
        boolean hasTrace;
        if (Neuro2DataCollector.getInstance().isRecording()) {
            return new Rotation(Neuro2Rotation.mc.field_1724.method_36454(), Neuro2Rotation.mc.field_1724.method_36455());
        }
        if (entity == null || Neuro2Rotation.mc.field_1724 == null || this.model == null || !this.model.isTrained()) {
            this.leadInFactor = 0.0f;
            if (Float.isNaN(this.lastYaw)) {
                return currentRotation;
            }
            float returnSpeed = 0.25f;
            this.lastYaw += class_3532.method_15393((float)(Neuro2Rotation.mc.field_1724.method_36454() - this.lastYaw)) * returnSpeed;
            this.lastPitch += (Neuro2Rotation.mc.field_1724.method_36455() - this.lastPitch) * returnSpeed;
            this.currentYawVelocity *= 0.6f;
            this.currentPitchVelocity *= 0.6f;
            float recoveryYaw = this.lastYaw + this.currentYawVelocity;
            float recoveryPitch = class_3532.method_15363((float)(this.lastPitch + this.currentPitchVelocity), (float)-90.0f, (float)90.0f);
            if (Math.abs(class_3532.method_15393((float)(recoveryYaw - Neuro2Rotation.mc.field_1724.method_36454()))) < 0.5f && Math.abs(recoveryPitch - Neuro2Rotation.mc.field_1724.method_36455()) < 0.5f) {
                this.lastYaw = Float.NaN;
                this.lastPitch = Float.NaN;
                return new Rotation(Neuro2Rotation.mc.field_1724.method_36454(), Neuro2Rotation.mc.field_1724.method_36455());
            }
            this.lastYaw = recoveryYaw;
            this.lastPitch = recoveryPitch;
            return this.applyGcd(recoveryYaw, recoveryPitch, currentRotation);
        }
        if (Float.isNaN(this.lastYaw)) {
            this.lastYaw = currentRotation.getYaw();
            this.lastPitch = currentRotation.getPitch();
        }
        this.leadInFactor = MathUtil.interpolate(this.leadInFactor, 1.0f, 0.05f);
        Rotation perfect = RotationUtil.rotationAt(entity.method_19538().method_1031(0.0, (double)(entity.method_17682() / 2.0f), 0.0));
        float perfectYaw = perfect.getYaw();
        float perfectPitch = class_3532.method_15363((float)perfect.getPitch(), (float)-90.0f, (float)90.0f);
        float auraDistance = AuraModule.getInstance().getAttackDistance();
        class_3966 hitResult = RaytracingUtil.raytraceEntity(auraDistance, currentRotation, false);
        boolean bl = hasTrace = hitResult != null && hitResult.method_17782() == entity;
        if (hasTrace) {
            this.slowYawTicks = MathUtil.interpolate(this.slowYawTicks, 0.75f, 0.1f);
            this.slowPitchTicks = MathUtil.interpolate(this.slowPitchTicks, 0.55f, 0.1f);
        } else {
            this.slowYawTicks = MathUtil.interpolate(this.slowYawTicks, 1.0f, 0.05f);
            this.slowPitchTicks = MathUtil.interpolate(this.slowPitchTicks, 1.0f, 0.05f);
        }
        double distance = Neuro2Rotation.mc.field_1724.method_19538().method_1022(entity.method_19538());
        double targetSpeed = entity.method_18798().method_37267();
        float[] offsets = this.model.predict(distance, targetSpeed);
        float offsetSmoothing = MathUtil.randomInRange(0.12f, 0.28f);
        this.smoothedYawOffset += (offsets[0] - this.smoothedYawOffset) * offsetSmoothing;
        this.smoothedPitchOffset += (offsets[1] - this.smoothedPitchOffset) * offsetSmoothing;
        float targetYaw = perfectYaw + this.smoothedYawOffset * this.leadInFactor;
        float targetPitch = perfectPitch + this.smoothedPitchOffset * this.leadInFactor;
        long elapsed = System.currentTimeMillis() - this.noiseStartTime;
        float yawNoise = (float)this.perlin(this.noiseOffY + (double)elapsed * 2.8E-4) * 1.1f * this.noiseFactor;
        float pitchNoise = (float)this.perlin(this.noiseOffP + (double)elapsed * 2.8E-4) * 1.2f * this.noiseFactor;
        float totalDiff = Math.abs(class_3532.method_15393((float)((targetYaw += yawNoise) - perfectYaw))) + Math.abs((targetPitch += pitchNoise) - perfectPitch);
        if (totalDiff < 8.0f) {
            this.noiseFactor = Math.max(0.0f, this.noiseFactor - 0.045f);
        }
        float yawDiff = class_3532.method_15393((float)(targetYaw - this.lastYaw));
        float pitchDiff = targetPitch - this.lastPitch;
        float accel = MathUtil.randomInRange(0.36f, 0.52f) * this.slowYawTicks * this.leadInFactor;
        float friction = MathUtil.randomInRange(0.48f, 0.68f);
        this.currentYawVelocity = (this.currentYawVelocity + yawDiff * accel) * friction;
        this.currentPitchVelocity = (this.currentPitchVelocity + pitchDiff * (accel * this.slowPitchTicks)) * friction;
        float finalYaw = this.lastYaw + this.currentYawVelocity;
        float finalPitch = class_3532.method_15363((float)(this.lastPitch + this.currentPitchVelocity), (float)-90.0f, (float)90.0f);
        this.lastYaw = finalYaw;
        this.lastPitch = finalPitch;
        return this.applyGcd(finalYaw, finalPitch, currentRotation);
    }

    private Rotation applyGcd(float newYaw, float newPitch, Rotation prev) {
        float gcd = MouseUtil.getGCD();
        if (gcd <= 0.0f) {
            return new Rotation(newYaw, newPitch);
        }
        float yaw = prev.getYaw() + (float)Math.round(class_3532.method_15393((float)(newYaw - prev.getYaw())) / gcd) * gcd;
        float pitch = prev.getPitch() + (float)Math.round((newPitch - prev.getPitch()) / gcd) * gcd;
        return new Rotation(yaw, class_3532.method_15363((float)pitch, (float)-90.0f, (float)90.0f));
    }

    private double perlin(double x) {
        int xi = (int)Math.floor(x);
        double xf = x - (double)xi;
        double u = xf * xf * xf * (xf * (xf * 6.0 - 15.0) + 10.0);
        return this.lerp(u, this.grad(this.hash(xi), xf), this.grad(this.hash(xi + 1), xf - 1.0));
    }

    private double lerp(double t, double a, double b) {
        return a + t * (b - a);
    }

    private double grad(int h, double x) {
        return (h & 1) == 0 ? x : -x;
    }

    private int hash(int x) {
        x = (x >>> 16 ^ x) * 73244475;
        x = (x >>> 16 ^ x) * 73244475;
        return x >>> 16 ^ x & 0xFF;
    }
}

