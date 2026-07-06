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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/rotation/rotations/LegitSnapRotation.class */
public class LegitSnapRotation extends RotationMode {
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

    public LegitSnapRotation(float speed, float smoothness) {
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

    @Override // sweetie.leonware.api.utils.rotation.manager.RotationMode
    public Rotation process(Rotation cur, Rotation target, class_243 vec3d, class_1297 entity) {
        if (entity != null && entity.method_5628() != this.lastTargetId) {
            this.curveOffsetYaw = MathUtil.randomInRange(-2.5f, 2.5f);
            this.curveOffsetPitch = MathUtil.randomInRange(-1.0f, 1.0f);
            this.lastTargetId = entity.method_5628();
        }
        float playerYawSpeed = class_3532.method_15393(mc.field_1724.method_36454() - this.lastPlayerYaw);
        float playerPitchSpeed = mc.field_1724.method_36455() - this.lastPlayerPitch;
        float yawDelta = class_3532.method_15393(target.getYaw() - cur.getYaw());
        float pitchDelta = class_3532.method_15393(target.getPitch() - cur.getPitch());
        float yawAbs = Math.abs(yawDelta);
        float pitchAbs = Math.abs(pitchDelta);
        float range = AuraModule.getInstance().getAttackDistance() + 1.0f;
        class_3966 hitResult = RaytracingUtil.raytraceEntity(range, cur, false);
        boolean hasTrace = (entity == null || hitResult == null || hitResult.method_17782() != entity) ? false : true;
        float traceMultiplier = hasTrace ? 0.35f : 1.0f;
        float progress = (float) Math.pow(class_3532.method_15363(1.0f - (yawAbs / 42.0f), 0.0f, 1.0f), 2.5d);
        float currentCurveYaw = this.curveOffsetYaw * (1.0f - progress);
        float currentCurvePitch = this.curveOffsetPitch * (1.0f - progress);
        float proximityFactor = class_3532.method_15363(yawAbs / 20.0f, 0.0f, 1.0f);
        float dynamicSmooth = lerp(this.smoothness * 0.35f, this.smoothness, proximityFactor);
        float yawAccel = getSnapAccel(yawAbs, this.lastYawDelta, dynamicSmooth) * traceMultiplier;
        float pitchAccel = getSnapAccel(pitchAbs, this.lastPitchDelta, dynamicSmooth) * 0.8f * traceMultiplier;
        float yawMax = getSnapSpeed(yawAbs, this.speed) * traceMultiplier;
        float pitchMax = getSnapSpeed(pitchAbs, this.speed) * 0.8f * traceMultiplier;
        float yawTarget = class_3532.method_15363(yawDelta + currentCurveYaw + (playerYawSpeed * 0.06f), -yawMax, yawMax);
        float pitchTarget = class_3532.method_15363(pitchDelta + currentCurvePitch + (playerPitchSpeed * 0.04f), -pitchMax, pitchMax);
        this.yawVelocity = lerp(this.yawVelocity, yawTarget, yawAccel);
        this.pitchVelocity = lerp(this.pitchVelocity, pitchTarget, pitchAccel);
        if (yawAbs < 1.0f) {
            this.yawVelocity *= 0.3f;
        }
        if (pitchAbs < 0.8f) {
            this.pitchVelocity *= 0.25f;
        }
        float finalYaw = cur.getYaw() + this.yawVelocity;
        float finalPitch = cur.getPitch() + this.pitchVelocity;
        Rotation result = applyGCD(new Rotation(finalYaw, class_3532.method_15363(finalPitch, -90.0f, 90.0f)), cur);
        this.lastYawDelta = yawDelta;
        this.lastPitchDelta = pitchDelta;
        this.lastPlayerYaw = mc.field_1724.method_36454();
        this.lastPlayerPitch = mc.field_1724.method_36455();
        return result;
    }

    private float getSnapSpeed(float delta, float speedMult) {
        return delta > 45.0f ? 35.0f * speedMult : delta > 15.0f ? 18.0f * speedMult : 8.5f * speedMult;
    }

    private float getSnapAccel(float delta, float lastDelta, float smoothMult) {
        boolean sameDir = delta * lastDelta >= 0.0f;
        return !sameDir ? 0.32f * smoothMult : class_3532.method_15363(delta / 35.0f, 0.16f, 0.42f) * smoothMult;
    }

    private Rotation applyGCD(Rotation target, Rotation prev) {
        float gcd = MouseUtil.getGCD();
        float deltaYaw = target.getYaw() - prev.getYaw();
        float deltaPitch = target.getPitch() - prev.getPitch();
        return new Rotation(prev.getYaw() + (Math.round(deltaYaw / gcd) * gcd), class_3532.method_15363(prev.getPitch() + (Math.round(deltaPitch / gcd) * gcd), -90.0f, 90.0f));
    }

    private float lerp(float from, float to, float t) {
        return from + ((to - from) * t);
    }
}
