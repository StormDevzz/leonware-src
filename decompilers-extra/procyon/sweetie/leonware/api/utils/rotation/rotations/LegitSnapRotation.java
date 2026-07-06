// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.rotation.rotations;

import sweetie.leonware.api.utils.math.MouseUtil;
import net.minecraft.class_3966;
import sweetie.leonware.api.utils.rotation.RaytracingUtil;
import sweetie.leonware.client.features.modules.combat.AuraModule;
import net.minecraft.class_3532;
import sweetie.leonware.api.utils.math.MathUtil;
import net.minecraft.class_1297;
import net.minecraft.class_243;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationMode;

public class LegitSnapRotation extends RotationMode
{
    private float yawVelocity;
    private float pitchVelocity;
    private float lastYawDelta;
    private float lastPitchDelta;
    private float curveOffsetYaw;
    private float curveOffsetPitch;
    private long lastTargetId;
    private float lastPlayerYaw;
    private float lastPlayerPitch;
    private final float speed;
    private final float smoothness;
    
    public LegitSnapRotation(final float speed, final float smoothness) {
        super("AlekDLC Cracked + Decrypted (easy)");
        this.yawVelocity = 0.0f;
        this.pitchVelocity = 0.0f;
        this.lastYawDelta = 0.0f;
        this.lastPitchDelta = 0.0f;
        this.curveOffsetYaw = 0.0f;
        this.curveOffsetPitch = 0.0f;
        this.lastTargetId = -1L;
        this.lastPlayerYaw = 0.0f;
        this.lastPlayerPitch = 0.0f;
        this.speed = speed;
        this.smoothness = smoothness;
    }
    
    @Override
    public Rotation process(final Rotation cur, final Rotation target, final class_243 vec3d, final class_1297 entity) {
        if (entity != null && entity.method_5628() != this.lastTargetId) {
            this.curveOffsetYaw = MathUtil.randomInRange(-2.5f, 2.5f);
            this.curveOffsetPitch = MathUtil.randomInRange(-1.0f, 1.0f);
            this.lastTargetId = entity.method_5628();
        }
        final float playerYawSpeed = class_3532.method_15393(LegitSnapRotation.mc.field_1724.method_36454() - this.lastPlayerYaw);
        final float playerPitchSpeed = LegitSnapRotation.mc.field_1724.method_36455() - this.lastPlayerPitch;
        final float yawDelta = class_3532.method_15393(target.getYaw() - cur.getYaw());
        final float pitchDelta = class_3532.method_15393(target.getPitch() - cur.getPitch());
        final float yawAbs = Math.abs(yawDelta);
        final float pitchAbs = Math.abs(pitchDelta);
        final float range = AuraModule.getInstance().getAttackDistance() + 1.0f;
        final class_3966 hitResult = RaytracingUtil.raytraceEntity(range, cur, false);
        final boolean hasTrace = entity != null && hitResult != null && hitResult.method_17782() == entity;
        final float traceMultiplier = hasTrace ? 0.35f : 1.0f;
        final float progress = (float)Math.pow(class_3532.method_15363(1.0f - yawAbs / 42.0f, 0.0f, 1.0f), 2.5);
        final float currentCurveYaw = this.curveOffsetYaw * (1.0f - progress);
        final float currentCurvePitch = this.curveOffsetPitch * (1.0f - progress);
        final float proximityFactor = class_3532.method_15363(yawAbs / 20.0f, 0.0f, 1.0f);
        final float dynamicSmooth = this.lerp(this.smoothness * 0.35f, this.smoothness, proximityFactor);
        final float yawAccel = this.getSnapAccel(yawAbs, this.lastYawDelta, dynamicSmooth) * traceMultiplier;
        final float pitchAccel = this.getSnapAccel(pitchAbs, this.lastPitchDelta, dynamicSmooth) * 0.8f * traceMultiplier;
        final float yawMax = this.getSnapSpeed(yawAbs, this.speed) * traceMultiplier;
        final float pitchMax = this.getSnapSpeed(pitchAbs, this.speed) * 0.8f * traceMultiplier;
        final float yawTarget = class_3532.method_15363(yawDelta + currentCurveYaw + playerYawSpeed * 0.06f, -yawMax, yawMax);
        final float pitchTarget = class_3532.method_15363(pitchDelta + currentCurvePitch + playerPitchSpeed * 0.04f, -pitchMax, pitchMax);
        this.yawVelocity = this.lerp(this.yawVelocity, yawTarget, yawAccel);
        this.pitchVelocity = this.lerp(this.pitchVelocity, pitchTarget, pitchAccel);
        if (yawAbs < 1.0f) {
            this.yawVelocity *= 0.3f;
        }
        if (pitchAbs < 0.8f) {
            this.pitchVelocity *= 0.25f;
        }
        final float finalYaw = cur.getYaw() + this.yawVelocity;
        final float finalPitch = cur.getPitch() + this.pitchVelocity;
        final Rotation result = this.applyGCD(new Rotation(finalYaw, class_3532.method_15363(finalPitch, -90.0f, 90.0f)), cur);
        this.lastYawDelta = yawDelta;
        this.lastPitchDelta = pitchDelta;
        this.lastPlayerYaw = LegitSnapRotation.mc.field_1724.method_36454();
        this.lastPlayerPitch = LegitSnapRotation.mc.field_1724.method_36455();
        return result;
    }
    
    private float getSnapSpeed(final float delta, final float speedMult) {
        if (delta > 45.0f) {
            return 35.0f * speedMult;
        }
        if (delta > 15.0f) {
            return 18.0f * speedMult;
        }
        return 8.5f * speedMult;
    }
    
    private float getSnapAccel(final float delta, final float lastDelta, final float smoothMult) {
        final boolean sameDir = delta * lastDelta >= 0.0f;
        if (!sameDir) {
            return 0.32f * smoothMult;
        }
        return class_3532.method_15363(delta / 35.0f, 0.16f, 0.42f) * smoothMult;
    }
    
    private Rotation applyGCD(final Rotation target, final Rotation prev) {
        final float gcd = MouseUtil.getGCD();
        final float deltaYaw = target.getYaw() - prev.getYaw();
        final float deltaPitch = target.getPitch() - prev.getPitch();
        return new Rotation(prev.getYaw() + Math.round(deltaYaw / gcd) * gcd, class_3532.method_15363(prev.getPitch() + Math.round(deltaPitch / gcd) * gcd, -90.0f, 90.0f));
    }
    
    private float lerp(final float from, final float to, final float t) {
        return from + (to - from) * t;
    }
}
