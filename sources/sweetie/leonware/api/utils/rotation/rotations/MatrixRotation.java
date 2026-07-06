package sweetie.leonware.api.utils.rotation.rotations;

import net.minecraft.class_1297;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationMode;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/rotation/rotations/MatrixRotation.class */
public class MatrixRotation extends RotationMode {
    private static float lastYaw;
    private static float lastPitch;

    public MatrixRotation() {
        super("Matrix");
    }

    @Override // sweetie.leonware.api.utils.rotation.manager.RotationMode
    public Rotation process(Rotation currentRotation, Rotation targetRotation, class_243 vec3d, class_1297 entity) {
        Rotation delta = RotationUtil.calculateDelta(currentRotation, targetRotation);
        float yawDelta = delta.getYaw();
        float pitchDelta = delta.getPitch();
        float yawSpeed = MathUtil.randomInRange(58, 60);
        float pitchSpeed = (FunTimeRotation.attack || entity == null) ? Math.abs(pitchDelta) : 0.333f;
        float pitchStep = Math.max(pitchDelta, (float) (2.0d + (Math.random() * 2.0d)));
        float pitchDelta2 = pitchDelta > 0.0f ? pitchStep : -pitchStep;
        if (Math.abs(yawDelta - lastYaw) >= 3.0f) {
            float jitterApplier = 3.0f + 0.1f;
            yawDelta += yawDelta > 0.0f ? jitterApplier : -jitterApplier;
        }
        lastYaw = yawDelta;
        lastPitch = pitchDelta2;
        return new Rotation(currentRotation.getYaw() + class_3532.method_15363(yawDelta, -yawSpeed, yawSpeed), currentRotation.getPitch() + class_3532.method_15363(pitchDelta2, -pitchSpeed, pitchSpeed));
    }
}
