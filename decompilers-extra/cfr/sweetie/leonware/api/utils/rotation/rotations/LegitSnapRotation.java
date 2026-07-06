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
import sweetie.leonware.api.utils.rotation.RaytracingUtil;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationMode;
import sweetie.leonware.client.features.modules.combat.AuraModule;

public class LegitSnapRotation
extends RotationMode {
    private float yawVelocity = 0.0f;
    private float pitchVelocity = 0.0f;
    private float lastYawDelta = 0.0f;
    private float lastPitchDelta = 0.0f;
    private float curveOffsetYaw = 0.0f;
    private float curveOffsetPitch = 0.0f;
    private long lastTargetId = -1L;
    private float lastPlayerYaw = 0.0f;
    private float lastPlayerPitch = 0.0f;
    private final float speed;
    private final float smoothness;

    public LegitSnapRotation(float speed, float smoothness) {
        super("AlekDLC Cracked + Decrypted (easy)");
        this.speed = speed;
        this.smoothness = smoothness;
    }

    @Override
    public Rotation process(Rotation cur, Rotation target, class_243 vec3d, class_1297 entity) {
        if (entity != null && (long)entity.method_5628() != this.lastTargetId) {
            this.curveOffsetYaw = MathUtil.randomInRange(-2.5f, 2.5f);
            this.curveOffsetPitch = MathUtil.randomInRange(-1.0f, 1.0f);
            this.lastTargetId = entity.method_5628();
        }
        float playerYawSpeed = class_3532.method_15393((float)(LegitSnapRotation.mc.field_1724.method_36454() - this.lastPlayerYaw));
        float playerPitchSpeed = LegitSnapRotation.mc.field_1724.method_36455() - this.lastPlayerPitch;
        float yawDelta = class_3532.method_15393((float)(target.getYaw() - cur.getYaw()));
        float pitchDelta = class_3532.method_15393((float)(target.getPitch() - cur.getPitch()));
        float yawAbs = Math.abs(yawDelta);
        float pitchAbs = Math.abs(pitchDelta);
        float range = AuraModule.getInstance().getAttackDistance() + 1.0f;
        class_3966 hitResult = RaytracingUtil.raytraceEntity(range, cur, false);
        boolean hasTrace = entity != null && hitResult != null && hitResult.method_17782() == entity;
        float traceMultiplier = hasTrace ? 0.35f : 1.0f;
        float progress = (float)Math.pow(class_3532.method_15363((float)(1.0f - yawAbs / 42.0f), (float)0.0f, (float)1.0f), 2.5);
        float currentCurveYaw = this.curveOffsetYaw * (1.0f - progress);
        float currentCurvePitch = this.curveOffsetPitch * (1.0f - progress);
        float proximityFactor = class_3532.method_15363((float)(yawAbs / 20.0f), (float)0.0f, (float)1.0f);
        float dynamicSmooth = this.lerp(this.smoothness * 0.35f, this.smoothness, proximityFactor);
        float yawAccel = this.getSnapAccel(yawAbs, this.lastYawDelta, dynamicSmooth) * traceMultiplier;
        float pitchAccel = this.getSnapAccel(pitchAbs, this.lastPitchDelta, dynamicSmooth) * 0.8f * traceMultiplier;
        float yawMax = this.getSnapSpeed(yawAbs, this.speed) * traceMultiplier;
        float pitchMax = this.getSnapSpeed(pitchAbs, this.speed) * 0.8f * traceMultiplier;
        float yawTarget = class_3532.method_15363((float)(yawDelta + currentCurveYaw + playerYawSpeed * 0.06f), (float)(-yawMax), (float)yawMax);
        float pitchTarget = class_3532.method_15363((float)(pitchDelta + currentCurvePitch + playerPitchSpeed * 0.04f), (float)(-pitchMax), (float)pitchMax);
        this.yawVelocity = this.lerp(this.yawVelocity, yawTarget, yawAccel);
        this.pitchVelocity = this.lerp(this.pitchVelocity, pitchTarget, pitchAccel);
        if (yawAbs < 1.0f) {
            this.yawVelocity *= 0.3f;
        }
        if (pitchAbs < 0.8f) {
            this.pitchVelocity *= 0.25f;
        }
        float finalYaw = cur.getYaw() + this.yawVelocity;
        float finalPitch = cur.getPitch() + this.pitchVelocity;
        Rotation result = this.applyGCD(new Rotation(finalYaw, class_3532.method_15363((float)finalPitch, (float)-90.0f, (float)90.0f)), cur);
        this.lastYawDelta = yawDelta;
        this.lastPitchDelta = pitchDelta;
        this.lastPlayerYaw = LegitSnapRotation.mc.field_1724.method_36454();
        this.lastPlayerPitch = LegitSnapRotation.mc.field_1724.method_36455();
        return result;
    }

    private float getSnapSpeed(float delta, float speedMult) {
        if (delta > 45.0f) {
            return 35.0f * speedMult;
        }
        if (delta > 15.0f) {
            return 18.0f * speedMult;
        }
        return 8.5f * speedMult;
    }

    private float getSnapAccel(float delta, float lastDelta, float smoothMult) {
        boolean sameDir;
        boolean bl = sameDir = delta * lastDelta >= 0.0f;
        if (!sameDir) {
            return 0.32f * smoothMult;
        }
        return class_3532.method_15363((float)(delta / 35.0f), (float)0.16f, (float)0.42f) * smoothMult;
    }

    private Rotation applyGCD(Rotation target, Rotation prev) {
        float gcd = MouseUtil.getGCD();
        float deltaYaw = target.getYaw() - prev.getYaw();
        float deltaPitch = target.getPitch() - prev.getPitch();
        return new Rotation(prev.getYaw() + (float)Math.round(deltaYaw / gcd) * gcd, class_3532.method_15363((float)(prev.getPitch() + (float)Math.round(deltaPitch / gcd) * gcd), (float)-90.0f, (float)90.0f));
    }

    private float lerp(float from, float to, float t) {
        return from + (to - from) * t;
    }
}

