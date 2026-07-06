package sweetie.leonware.client.features.modules.render.motionblur;

import lombok.Generated;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/motionblur/MotionBlurModule.class */
@ModuleRegister(name = "Motion Blur", category = Category.RENDER)
public class MotionBlurModule extends Module {
    private static final MotionBlurModule instance = new MotionBlurModule();
    public static BlurAlgorithm blurAlgorithm = BlurAlgorithm.CENTERED;
    public final SliderSetting strength = new SliderSetting("Strength").value(Float.valueOf(-0.8f)).range(-2.0f, 2.0f).step(0.1f).onAction2(() -> {
        setMotionBlurStrength(getStrength().getValue().floatValue());
    });
    public final BooleanSetting useRRC = new BooleanSetting("Use refresh rate scaling").value((Boolean) true);
    public final ShaderMotionBlur shader = new ShaderMotionBlur(this);

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/motionblur/MotionBlurModule$BlurAlgorithm.class */
    public enum BlurAlgorithm {
        BACKWARDS,
        CENTERED
    }

    @Generated
    public static MotionBlurModule getInstance() {
        return instance;
    }

    @Generated
    public SliderSetting getStrength() {
        return this.strength;
    }

    /* JADX WARN: Type inference failed for: r1v4, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    public MotionBlurModule() {
        this.shader.registerShaderCallbacks();
        addSettings(this.strength, this.useRRC);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
    }

    private void setMotionBlurStrength(float strength) {
        this.shader.updateBlurStrength(strength);
    }
}
