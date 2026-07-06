// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.rotation.rotations;

import net.minecraft.class_3532;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import net.minecraft.class_1297;
import net.minecraft.class_243;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationMode;

public class MatrixRotation extends RotationMode
{
    private static float lastYaw;
    private static float lastPitch;
    
    public MatrixRotation() {
        super("Matrix");
    }
    
    @Override
    public Rotation process(final Rotation currentRotation, final Rotation targetRotation, final class_243 vec3d, final class_1297 entity) {
        final Rotation delta = RotationUtil.calculateDelta(currentRotation, targetRotation);
        float yawDelta = delta.getYaw();
        float pitchDelta = delta.getPitch();
        final float yawSpeed = (float)MathUtil.randomInRange(58, 60);
        final float pitchSpeed = (FunTimeRotation.attack || entity == null) ? Math.abs(pitchDelta) : 0.333f;
        final float pitchStep = Math.max(pitchDelta, (float)(2.0 + Math.random() * 2.0));
        pitchDelta = ((pitchDelta > 0.0f) ? pitchStep : (-pitchStep));
        final float jitterPower = 3.0f;
        if (Math.abs(yawDelta - MatrixRotation.lastYaw) >= jitterPower) {
            final float jitterApplier = jitterPower + 0.1f;
            yawDelta += ((yawDelta > 0.0f) ? jitterApplier : (-jitterApplier));
        }
        MatrixRotation.lastYaw = yawDelta;
        MatrixRotation.lastPitch = pitchDelta;
        return new Rotation(currentRotation.getYaw() + class_3532.method_15363(yawDelta, -yawSpeed, yawSpeed), currentRotation.getPitch() + class_3532.method_15363(pitchDelta, -pitchSpeed, pitchSpeed));
    }
}
