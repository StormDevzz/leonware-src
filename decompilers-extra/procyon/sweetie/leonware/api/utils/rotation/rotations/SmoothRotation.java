// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.rotation.rotations;

import sweetie.leonware.api.utils.rotation.RotationUtil;
import net.minecraft.class_1297;
import net.minecraft.class_243;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationMode;

public class SmoothRotation extends RotationMode
{
    private final float yawSpeed;
    private final float pitchSpeed;
    
    public SmoothRotation() {
        this(20.0f, 10.0f);
    }
    
    public SmoothRotation(final float yawSpeed, final float pitchSpeed) {
        super("Smooth");
        this.yawSpeed = yawSpeed;
        this.pitchSpeed = pitchSpeed;
    }
    
    @Override
    public Rotation process(final Rotation currentRotation, final Rotation targetRotation, final class_243 vec3d, final class_1297 entity) {
        final Rotation delta = RotationUtil.calculateDelta(currentRotation, targetRotation);
        final float yawDelta = delta.getYaw();
        final float pitchDelta = delta.getPitch();
        final float rotationDifference = (float)Math.hypot(Math.abs(yawDelta), Math.abs(pitchDelta));
        if (rotationDifference == 0.0f) {
            return currentRotation;
        }
        final float straightLineYaw = Math.abs(yawDelta / rotationDifference) * this.yawSpeed;
        final float straightLinePitch = Math.abs(pitchDelta / rotationDifference) * this.pitchSpeed;
        return new Rotation(currentRotation.getYaw() + Math.min(Math.max(yawDelta, -straightLineYaw), straightLineYaw), currentRotation.getPitch() + Math.min(Math.max(pitchDelta, -straightLinePitch), straightLinePitch));
    }
}
