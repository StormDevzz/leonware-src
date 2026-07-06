package sweetie.leonware.api.utils.rotation.rotations;

import net.minecraft.class_1297;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationMode;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/rotation/rotations/ScaffoldLegitRotation.class */
public class ScaffoldLegitRotation extends RotationMode {
    private float lastYaw;
    private float lastPitch;

    public ScaffoldLegitRotation() {
        super("ScaffoldLegit");
        this.lastYaw = Float.NaN;
        this.lastPitch = Float.NaN;
    }

    @Override // sweetie.leonware.api.utils.rotation.manager.RotationMode
    public Rotation process(Rotation current, Rotation target, class_243 vec, class_1297 entity) {
        float targetYaw = target.getYaw();
        float targetPitch = target.getPitch();
        if (Float.isNaN(this.lastYaw)) {
            this.lastYaw = current.getYaw();
            this.lastPitch = current.getPitch();
        }
        float yawDiff = class_3532.method_15393(targetYaw - this.lastYaw);
        float pitchDiff = class_3532.method_15393(targetPitch - this.lastPitch);
        float yawSpeed = MathUtil.randomInRange(60.0f, 90.0f);
        float pitchSpeed = MathUtil.randomInRange(40.0f, 60.0f);
        float moveYaw = clampAbs(yawDiff, yawSpeed);
        float movePitch = clampAbs(pitchDiff, pitchSpeed);
        float moveYaw2 = moveYaw + MathUtil.randomInRange(-0.05f, 0.05f);
        float movePitch2 = movePitch + MathUtil.randomInRange(-0.03f, 0.03f);
        float newYaw = this.lastYaw + moveYaw2;
        float newPitch = this.lastPitch + movePitch2;
        float newYaw2 = class_3532.method_15393(newYaw);
        float newPitch2 = class_3532.method_15363(newPitch, -90.0f, 90.0f);
        this.lastYaw = newYaw2;
        this.lastPitch = newPitch2;
        return new Rotation(newYaw2, newPitch2);
    }

    private float clampAbs(float val, float maxAbs) {
        return Math.max(-maxAbs, Math.min(maxAbs, val));
    }
}
