/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  net.minecraft.class_243
 *  net.minecraft.class_3532
 *  net.minecraft.class_5611
 */
package sweetie.leonware.api.utils.rotation.rotations;

import net.minecraft.class_1297;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.minecraft.class_5611;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.utils.neuro.AIPredictor;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationMode;

public class NeuroRotation
extends RotationMode {
    private final AIPredictor predictor;
    private final float yawSpeed;
    private final float pitchSpeed;
    Rotation aiRotation = Rotation.DEFAULT;
    Rotation prevRotation = Rotation.DEFAULT;

    public NeuroRotation(AIPredictor predictor, float yawSpeed, float pitchSpeed) {
        super("Neuro");
        this.predictor = predictor;
        this.yawSpeed = yawSpeed;
        this.pitchSpeed = pitchSpeed;
    }

    @Override
    public Rotation process(Rotation currentRotation, Rotation targetRotation, class_243 vec3d, class_1297 entity) {
        this.prevRotation = this.aiRotation;
        if (entity == null || NeuroRotation.mc.field_1724 == null) {
            float y = 13.333333f;
            float p = 6.6666665f;
            Rotation delta = RotationUtil.calculateDelta(currentRotation, targetRotation);
            return new Rotation(currentRotation.getYaw() + class_3532.method_15363((float)delta.getYaw(), (float)(-y), (float)y), currentRotation.getPitch() + class_3532.method_15363((float)delta.getPitch(), (float)(-p), (float)p));
        }
        this.aiRotation = this.predictor.predict(entity, this.aiRotation, this.prevRotation, new class_5611(100.0f - this.yawSpeed, 100.0f - this.pitchSpeed));
        return this.aiRotation;
    }

    public float sinWave(double value, double delayMS, Easing easing) {
        return (float)(class_3532.method_15350((double)easing.apply((Math.sin((double)System.currentTimeMillis() / delayMS) + 1.0) / 2.0), (double)0.0, (double)1.0) * value);
    }

    public float cosWave(double value, double delayMS, Easing easing) {
        return (float)(class_3532.method_15350((double)easing.apply((Math.cos((double)System.currentTimeMillis() / delayMS) + 1.0) / 2.0), (double)0.0, (double)1.0) * value);
    }

    public float sinWave(double from, double to, double delayMS, Easing easing) {
        return (float)(from + (double)this.sinWave(to, delayMS, easing));
    }

    public float cosWave(double from, double to, double delayMS, Easing easing) {
        return (float)(from + (double)this.cosWave(to, delayMS, easing));
    }

    public float calc(float input, float target, double step) {
        return (float)((double)input + step * (double)(target - input));
    }
}

