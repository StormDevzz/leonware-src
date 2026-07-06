package sweetie.leonware.api.utils.rotation.rotations;

import net.minecraft.class_1297;
import net.minecraft.class_243;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationMode;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/rotation/rotations/SmoothRotation.class */
public class SmoothRotation extends RotationMode {
    private final float yawSpeed;
    private final float pitchSpeed;

    public SmoothRotation() {
        this(20.0f, 10.0f);
    }

    public SmoothRotation(float yawSpeed, float pitchSpeed) {
        super("Smooth");
        this.yawSpeed = yawSpeed;
        this.pitchSpeed = pitchSpeed;
    }

    @Override // sweetie.leonware.api.utils.rotation.manager.RotationMode
    public Rotation process(Rotation currentRotation, Rotation targetRotation, class_243 vec3d, class_1297 entity) {
        Rotation delta = RotationUtil.calculateDelta(currentRotation, targetRotation);
        float yawDelta = delta.getYaw();
        float pitchDelta = delta.getPitch();
        float rotationDifference = (float) Math.hypot(Math.abs(yawDelta), Math.abs(pitchDelta));
        if (rotationDifference == 0.0f) {
            return currentRotation;
        }
        float straightLineYaw = Math.abs(yawDelta / rotationDifference) * this.yawSpeed;
        float straightLinePitch = Math.abs(pitchDelta / rotationDifference) * this.pitchSpeed;
        return new Rotation(currentRotation.getYaw() + Math.min(Math.max(yawDelta, -straightLineYaw), straightLineYaw), currentRotation.getPitch() + Math.min(Math.max(pitchDelta, -straightLinePitch), straightLinePitch));
    }
}
