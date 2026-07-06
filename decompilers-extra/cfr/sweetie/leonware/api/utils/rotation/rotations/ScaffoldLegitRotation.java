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
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationMode;

public class ScaffoldLegitRotation
extends RotationMode {
    private float lastYaw = Float.NaN;
    private float lastPitch = Float.NaN;

    public ScaffoldLegitRotation() {
        super("ScaffoldLegit");
    }

    @Override
    public Rotation process(Rotation current, Rotation target, class_243 vec, class_1297 entity) {
        float targetYaw = target.getYaw();
        float targetPitch = target.getPitch();
        if (Float.isNaN(this.lastYaw)) {
            this.lastYaw = current.getYaw();
            this.lastPitch = current.getPitch();
        }
        float yawDiff = class_3532.method_15393((float)(targetYaw - this.lastYaw));
        float pitchDiff = class_3532.method_15393((float)(targetPitch - this.lastPitch));
        float yawSpeed = MathUtil.randomInRange(60.0f, 90.0f);
        float pitchSpeed = MathUtil.randomInRange(40.0f, 60.0f);
        float moveYaw = this.clampAbs(yawDiff, yawSpeed);
        float movePitch = this.clampAbs(pitchDiff, pitchSpeed);
        float newYaw = this.lastYaw + (moveYaw += MathUtil.randomInRange(-0.05f, 0.05f));
        float newPitch = this.lastPitch + (movePitch += MathUtil.randomInRange(-0.03f, 0.03f));
        newYaw = class_3532.method_15393((float)newYaw);
        newPitch = class_3532.method_15363((float)newPitch, (float)-90.0f, (float)90.0f);
        this.lastYaw = newYaw;
        this.lastPitch = newPitch;
        return new Rotation(newYaw, newPitch);
    }

    private float clampAbs(float val, float maxAbs) {
        return Math.max(-maxAbs, Math.min(maxAbs, val));
    }
}

