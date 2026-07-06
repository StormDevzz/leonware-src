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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/rotation/rotations/NeuroRotation.class */
public class NeuroRotation extends RotationMode {
    private final AIPredictor predictor;
    private final float yawSpeed;
    private final float pitchSpeed;
    Rotation aiRotation;
    Rotation prevRotation;

    public NeuroRotation(AIPredictor predictor, float yawSpeed, float pitchSpeed) {
        super("Neuro");
        this.aiRotation = Rotation.DEFAULT;
        this.prevRotation = Rotation.DEFAULT;
        this.predictor = predictor;
        this.yawSpeed = yawSpeed;
        this.pitchSpeed = pitchSpeed;
    }

    @Override // sweetie.leonware.api.utils.rotation.manager.RotationMode
    public Rotation process(Rotation currentRotation, Rotation targetRotation, class_243 vec3d, class_1297 entity) {
        this.prevRotation = this.aiRotation;
        if (entity == null || mc.field_1724 == null) {
            Rotation delta = RotationUtil.calculateDelta(currentRotation, targetRotation);
            return new Rotation(currentRotation.getYaw() + class_3532.method_15363(delta.getYaw(), -13.333333f, 13.333333f), currentRotation.getPitch() + class_3532.method_15363(delta.getPitch(), -6.6666665f, 6.6666665f));
        }
        this.aiRotation = this.predictor.predict(entity, this.aiRotation, this.prevRotation, new class_5611(100.0f - this.yawSpeed, 100.0f - this.pitchSpeed));
        return this.aiRotation;
    }

    public float sinWave(double value, double delayMS, Easing easing) {
        return (float) (class_3532.method_15350(easing.apply((Math.sin(System.currentTimeMillis() / delayMS) + 1.0d) / 2.0d), 0.0d, 1.0d) * value);
    }

    public float cosWave(double value, double delayMS, Easing easing) {
        return (float) (class_3532.method_15350(easing.apply((Math.cos(System.currentTimeMillis() / delayMS) + 1.0d) / 2.0d), 0.0d, 1.0d) * value);
    }

    public float sinWave(double from, double to, double delayMS, Easing easing) {
        return (float) (from + ((double) sinWave(to, delayMS, easing)));
    }

    public float cosWave(double from, double to, double delayMS, Easing easing) {
        return (float) (from + ((double) cosWave(to, delayMS, easing)));
    }

    public float calc(float input, float target, double step) {
        return (float) (((double) input) + (step * ((double) (target - input))));
    }
}
