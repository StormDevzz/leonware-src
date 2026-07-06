/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  net.minecraft.class_243
 *  net.minecraft.class_3532
 */
package sweetie.leonware.api.utils.rotation.rotations;

import net.minecraft.class_1297;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationMode;
import sweetie.leonware.api.utils.rotation.rotations.FunTimeRotation;

public class MatrixRotation
extends RotationMode {
    private static float lastYaw;
    private static float lastPitch;

    public MatrixRotation() {
        super("Matrix");
    }

    @Override
    public Rotation process(Rotation currentRotation, Rotation targetRotation, class_243 vec3d, class_1297 entity) {
        Rotation delta = RotationUtil.calculateDelta(currentRotation, targetRotation);
        float yawDelta = delta.getYaw();
        float pitchDelta = delta.getPitch();
        float yawSpeed = MathUtil.randomInRange(58, 60);
        float pitchSpeed = FunTimeRotation.attack || entity == null ? Math.abs(pitchDelta) : 0.333f;
        float pitchStep = Math.max(pitchDelta, (float)(2.0 + Math.random() * 2.0));
        pitchDelta = pitchDelta > 0.0f ? pitchStep : -pitchStep;
        float jitterPower = 3.0f;
        if (Math.abs(yawDelta - lastYaw) >= jitterPower) {
            float jitterApplier = jitterPower + 0.1f;
            yawDelta += yawDelta > 0.0f ? jitterApplier : -jitterApplier;
        }
        lastYaw = yawDelta;
        lastPitch = pitchDelta;
        return new Rotation(currentRotation.getYaw() + class_3532.method_15363((float)yawDelta, (float)(-yawSpeed), (float)yawSpeed), currentRotation.getPitch() + class_3532.method_15363((float)pitchDelta, (float)(-pitchSpeed), (float)pitchSpeed));
    }
}

