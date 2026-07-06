// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.rotation.rotations;

import sweetie.leonware.api.utils.math.MathUtil;
import net.minecraft.class_3532;
import net.minecraft.class_1297;
import net.minecraft.class_243;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationMode;

public class ScaffoldLegitRotation extends RotationMode
{
    private float lastYaw;
    private float lastPitch;
    
    public ScaffoldLegitRotation() {
        super("ScaffoldLegit");
        this.lastYaw = Float.NaN;
        this.lastPitch = Float.NaN;
    }
    
    @Override
    public Rotation process(final Rotation current, final Rotation target, final class_243 vec, final class_1297 entity) {
        final float targetYaw = target.getYaw();
        final float targetPitch = target.getPitch();
        if (Float.isNaN(this.lastYaw)) {
            this.lastYaw = current.getYaw();
            this.lastPitch = current.getPitch();
        }
        final float yawDiff = class_3532.method_15393(targetYaw - this.lastYaw);
        final float pitchDiff = class_3532.method_15393(targetPitch - this.lastPitch);
        final float yawSpeed = MathUtil.randomInRange(60.0f, 90.0f);
        final float pitchSpeed = MathUtil.randomInRange(40.0f, 60.0f);
        float moveYaw = this.clampAbs(yawDiff, yawSpeed);
        float movePitch = this.clampAbs(pitchDiff, pitchSpeed);
        moveYaw += MathUtil.randomInRange(-0.05f, 0.05f);
        movePitch += MathUtil.randomInRange(-0.03f, 0.03f);
        float newYaw = this.lastYaw + moveYaw;
        float newPitch = this.lastPitch + movePitch;
        newYaw = class_3532.method_15393(newYaw);
        newPitch = class_3532.method_15363(newPitch, -90.0f, 90.0f);
        this.lastYaw = newYaw;
        this.lastPitch = newPitch;
        return new Rotation(newYaw, newPitch);
    }
    
    private float clampAbs(final float val, final float maxAbs) {
        return Math.max(-maxAbs, Math.min(maxAbs, val));
    }
}
