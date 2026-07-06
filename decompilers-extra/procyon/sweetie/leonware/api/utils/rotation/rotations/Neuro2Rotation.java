// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.rotation.rotations;

import sweetie.leonware.api.utils.math.MouseUtil;
import net.minecraft.class_3966;
import sweetie.leonware.api.utils.rotation.RaytracingUtil;
import sweetie.leonware.client.features.modules.combat.AuraModule;
import net.minecraft.class_3532;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import sweetie.leonware.api.utils.neuro.Neuro2DataCollector;
import net.minecraft.class_1297;
import net.minecraft.class_243;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.api.utils.neuro.Neuro2Model;
import sweetie.leonware.api.utils.rotation.manager.RotationMode;

public class Neuro2Rotation extends RotationMode
{
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
    
    public Neuro2Rotation(final Neuro2Model model) {
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
    
    @Override
    public Rotation process(final Rotation currentRotation, final Rotation targetRotation, final class_243 vec3d, final class_1297 entity) {
        if (Neuro2DataCollector.getInstance().isRecording()) {
            return new Rotation(Neuro2Rotation.mc.field_1724.method_36454(), Neuro2Rotation.mc.field_1724.method_36455());
        }
        if (entity != null && Neuro2Rotation.mc.field_1724 != null && this.model != null && this.model.isTrained()) {
            if (Float.isNaN(this.lastYaw)) {
                this.lastYaw = currentRotation.getYaw();
                this.lastPitch = currentRotation.getPitch();
            }
            this.leadInFactor = MathUtil.interpolate(this.leadInFactor, 1.0f, 0.05f);
            final Rotation perfect = RotationUtil.rotationAt(entity.method_19538().method_1031(0.0, (double)(entity.method_17682() / 2.0f), 0.0));
            final float perfectYaw = perfect.getYaw();
            final float perfectPitch = class_3532.method_15363(perfect.getPitch(), -90.0f, 90.0f);
            final float auraDistance = AuraModule.getInstance().getAttackDistance();
            final class_3966 hitResult = RaytracingUtil.raytraceEntity(auraDistance, currentRotation, false);
            final boolean hasTrace = hitResult != null && hitResult.method_17782() == entity;
            if (hasTrace) {
                this.slowYawTicks = MathUtil.interpolate(this.slowYawTicks, 0.75f, 0.1f);
                this.slowPitchTicks = MathUtil.interpolate(this.slowPitchTicks, 0.55f, 0.1f);
            }
            else {
                this.slowYawTicks = MathUtil.interpolate(this.slowYawTicks, 1.0f, 0.05f);
                this.slowPitchTicks = MathUtil.interpolate(this.slowPitchTicks, 1.0f, 0.05f);
            }
            final double distance = Neuro2Rotation.mc.field_1724.method_19538().method_1022(entity.method_19538());
            final double targetSpeed = entity.method_18798().method_37267();
            final float[] offsets = this.model.predict(distance, targetSpeed);
            final float offsetSmoothing = MathUtil.randomInRange(0.12f, 0.28f);
            this.smoothedYawOffset += (offsets[0] - this.smoothedYawOffset) * offsetSmoothing;
            this.smoothedPitchOffset += (offsets[1] - this.smoothedPitchOffset) * offsetSmoothing;
            float targetYaw = perfectYaw + this.smoothedYawOffset * this.leadInFactor;
            float targetPitch = perfectPitch + this.smoothedPitchOffset * this.leadInFactor;
            final long elapsed = System.currentTimeMillis() - this.noiseStartTime;
            final float yawNoise = (float)this.perlin(this.noiseOffY + elapsed * 2.8E-4) * 1.1f * this.noiseFactor;
            final float pitchNoise = (float)this.perlin(this.noiseOffP + elapsed * 2.8E-4) * 1.2f * this.noiseFactor;
            targetYaw += yawNoise;
            targetPitch += pitchNoise;
            final float totalDiff = Math.abs(class_3532.method_15393(targetYaw - perfectYaw)) + Math.abs(targetPitch - perfectPitch);
            if (totalDiff < 8.0f) {
                this.noiseFactor = Math.max(0.0f, this.noiseFactor - 0.045f);
            }
            final float yawDiff = class_3532.method_15393(targetYaw - this.lastYaw);
            final float pitchDiff = targetPitch - this.lastPitch;
            final float accel = MathUtil.randomInRange(0.36f, 0.52f) * this.slowYawTicks * this.leadInFactor;
            final float friction = MathUtil.randomInRange(0.48f, 0.68f);
            this.currentYawVelocity = (this.currentYawVelocity + yawDiff * accel) * friction;
            this.currentPitchVelocity = (this.currentPitchVelocity + pitchDiff * (accel * this.slowPitchTicks)) * friction;
            final float finalYaw = this.lastYaw + this.currentYawVelocity;
            final float finalPitch = class_3532.method_15363(this.lastPitch + this.currentPitchVelocity, -90.0f, 90.0f);
            this.lastYaw = finalYaw;
            this.lastPitch = finalPitch;
            return this.applyGcd(finalYaw, finalPitch, currentRotation);
        }
        this.leadInFactor = 0.0f;
        if (Float.isNaN(this.lastYaw)) {
            return currentRotation;
        }
        final float returnSpeed = 0.25f;
        this.lastYaw += class_3532.method_15393(Neuro2Rotation.mc.field_1724.method_36454() - this.lastYaw) * returnSpeed;
        this.lastPitch += (Neuro2Rotation.mc.field_1724.method_36455() - this.lastPitch) * returnSpeed;
        this.currentYawVelocity *= 0.6f;
        this.currentPitchVelocity *= 0.6f;
        final float recoveryYaw = this.lastYaw + this.currentYawVelocity;
        final float recoveryPitch = class_3532.method_15363(this.lastPitch + this.currentPitchVelocity, -90.0f, 90.0f);
        if (Math.abs(class_3532.method_15393(recoveryYaw - Neuro2Rotation.mc.field_1724.method_36454())) < 0.5f && Math.abs(recoveryPitch - Neuro2Rotation.mc.field_1724.method_36455()) < 0.5f) {
            this.lastYaw = Float.NaN;
            this.lastPitch = Float.NaN;
            return new Rotation(Neuro2Rotation.mc.field_1724.method_36454(), Neuro2Rotation.mc.field_1724.method_36455());
        }
        this.lastYaw = recoveryYaw;
        this.lastPitch = recoveryPitch;
        return this.applyGcd(recoveryYaw, recoveryPitch, currentRotation);
    }
    
    private Rotation applyGcd(final float newYaw, final float newPitch, final Rotation prev) {
        final float gcd = MouseUtil.getGCD();
        if (gcd <= 0.0f) {
            return new Rotation(newYaw, newPitch);
        }
        final float yaw = prev.getYaw() + Math.round(class_3532.method_15393(newYaw - prev.getYaw()) / gcd) * gcd;
        final float pitch = prev.getPitch() + Math.round((newPitch - prev.getPitch()) / gcd) * gcd;
        return new Rotation(yaw, class_3532.method_15363(pitch, -90.0f, 90.0f));
    }
    
    private double perlin(final double x) {
        final int xi = (int)Math.floor(x);
        final double xf = x - xi;
        final double u = xf * xf * xf * (xf * (xf * 6.0 - 15.0) + 10.0);
        return this.lerp(u, this.grad(this.hash(xi), xf), this.grad(this.hash(xi + 1), xf - 1.0));
    }
    
    private double lerp(final double t, final double a, final double b) {
        return a + t * (b - a);
    }
    
    private double grad(final int h, final double x) {
        return ((h & 0x1) == 0x0) ? x : (-x);
    }
    
    private int hash(int x) {
        x = (x >>> 16 ^ x) * 73244475;
        x = (x >>> 16 ^ x) * 73244475;
        return x >>> 16 ^ (x & 0xFF);
    }
}
