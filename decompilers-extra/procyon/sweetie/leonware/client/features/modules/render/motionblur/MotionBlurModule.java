// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render.motionblur;

import lombok.Generated;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Motion Blur", category = Category.RENDER)
public class MotionBlurModule extends Module
{
    private static final MotionBlurModule instance;
    public final ShaderMotionBlur shader;
    public final SliderSetting strength;
    public final BooleanSetting useRRC;
    public static BlurAlgorithm blurAlgorithm;
    
    public MotionBlurModule() {
        this.strength = new SliderSetting("Strength").value(-0.8f).range(-2.0f, 2.0f).step(0.1f).onAction(() -> this.setMotionBlurStrength(this.getStrength().getValue()));
        this.useRRC = new BooleanSetting("Use refresh rate scaling").value(true);
        (this.shader = new ShaderMotionBlur(this)).registerShaderCallbacks();
        this.addSettings(this.strength, this.useRRC);
    }
    
    @Override
    public void onEvent() {
    }
    
    private void setMotionBlurStrength(final float strength) {
        this.shader.updateBlurStrength(strength);
    }
    
    @Generated
    public static MotionBlurModule getInstance() {
        return MotionBlurModule.instance;
    }
    
    @Generated
    public SliderSetting getStrength() {
        return this.strength;
    }
    
    static {
        instance = new MotionBlurModule();
        MotionBlurModule.blurAlgorithm = BlurAlgorithm.CENTERED;
    }
    
    public enum BlurAlgorithm
    {
        BACKWARDS, 
        CENTERED;
    }
}
