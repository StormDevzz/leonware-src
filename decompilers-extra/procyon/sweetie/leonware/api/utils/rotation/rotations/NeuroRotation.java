// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.rotation.rotations;

import sweetie.leonware.api.utils.animation.Easing;
import net.minecraft.class_5611;
import net.minecraft.class_3532;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import net.minecraft.class_1297;
import net.minecraft.class_243;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.neuro.AIPredictor;
import sweetie.leonware.api.utils.rotation.manager.RotationMode;

public class NeuroRotation extends RotationMode
{
    private final AIPredictor predictor;
    private final float yawSpeed;
    private final float pitchSpeed;
    Rotation aiRotation;
    Rotation prevRotation;
    
    public NeuroRotation(final AIPredictor predictor, final float yawSpeed, final float pitchSpeed) {
        super("Neuro");
        this.aiRotation = Rotation.DEFAULT;
        this.prevRotation = Rotation.DEFAULT;
        this.predictor = predictor;
        this.yawSpeed = yawSpeed;
        this.pitchSpeed = pitchSpeed;
    }
    
    @Override
    public Rotation process(final Rotation currentRotation, final Rotation targetRotation, final class_243 vec3d, final class_1297 entity) {
        this.prevRotation = this.aiRotation;
        if (entity == null || NeuroRotation.mc.field_1724 == null) {
            final float y = 13.333333f;
            final float p = 6.6666665f;
            final Rotation delta = RotationUtil.calculateDelta(currentRotation, targetRotation);
            return new Rotation(currentRotation.getYaw() + class_3532.method_15363(delta.getYaw(), -y, y), currentRotation.getPitch() + class_3532.method_15363(delta.getPitch(), -p, p));
        }
        return this.aiRotation = this.predictor.predict(entity, this.aiRotation, this.prevRotation, new class_5611(100.0f - this.yawSpeed, 100.0f - this.pitchSpeed));
    }
    
    public float sinWave(final double value, final double delayMS, final Easing easing) {
        return (float)(class_3532.method_15350(easing.apply((Math.sin(System.currentTimeMillis() / delayMS) + 1.0) / 2.0), 0.0, 1.0) * value);
    }
    
    public float cosWave(final double value, final double delayMS, final Easing easing) {
        return (float)(class_3532.method_15350(easing.apply((Math.cos(System.currentTimeMillis() / delayMS) + 1.0) / 2.0), 0.0, 1.0) * value);
    }
    
    public float sinWave(final double from, final double to, final double delayMS, final Easing easing) {
        return (float)(from + this.sinWave(to, delayMS, easing));
    }
    
    public float cosWave(final double from, final double to, final double delayMS, final Easing easing) {
        return (float)(from + this.cosWave(to, delayMS, easing));
    }
    
    public float calc(final float input, final float target, final double step) {
        return (float)(input + step * (target - input));
    }
}
