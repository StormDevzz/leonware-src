// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import sweetie.leonware.api.module.setting.Setting;
import java.util.function.Supplier;
import java.util.Objects;
import sweetie.leonware.api.module.setting.RunSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "View Model", category = Category.RENDER)
public class ViewModelModule extends Module
{
    private static final ViewModelModule instance;
    public final SliderSetting rightX;
    public final SliderSetting rightY;
    public final SliderSetting rightZ;
    public final SliderSetting leftX;
    public final SliderSetting leftY;
    public final SliderSetting leftZ;
    public final BooleanSetting applyToHand;
    public final SliderSetting handX;
    public final SliderSetting handY;
    public final SliderSetting handZ;
    private final RunSetting reset;
    
    public ViewModelModule() {
        this.rightX = new SliderSetting("Right X").value(0.0f).range(-2.0f, 2.0f).step(0.1f);
        this.rightY = new SliderSetting("Right Y").value(0.0f).range(-2.0f, 2.0f).step(0.1f);
        this.rightZ = new SliderSetting("Right Z").value(0.0f).range(-2.0f, 2.0f).step(0.1f);
        this.leftX = new SliderSetting("Left X").value(0.0f).range(-2.0f, 2.0f).step(0.1f);
        this.leftY = new SliderSetting("Left Y").value(0.0f).range(-2.0f, 2.0f).step(0.1f);
        this.leftZ = new SliderSetting("Left Z").value(0.0f).range(-2.0f, 2.0f).step(0.1f);
        this.applyToHand = new BooleanSetting("Apply to Hand").value(false);
        final SliderSetting step = new SliderSetting("Hand X").value(0.0f).range(-2.0f, 2.0f).step(0.1f);
        final BooleanSetting applyToHand = this.applyToHand;
        Objects.requireNonNull(applyToHand);
        this.handX = step.setVisible((Supplier<Boolean>)applyToHand::getValue);
        final SliderSetting step2 = new SliderSetting("Hand Y").value(0.0f).range(-2.0f, 2.0f).step(0.1f);
        final BooleanSetting applyToHand2 = this.applyToHand;
        Objects.requireNonNull(applyToHand2);
        this.handY = step2.setVisible((Supplier<Boolean>)applyToHand2::getValue);
        final SliderSetting step3 = new SliderSetting("Hand Z").value(0.0f).range(-2.0f, 2.0f).step(0.1f);
        final BooleanSetting applyToHand3 = this.applyToHand;
        Objects.requireNonNull(applyToHand3);
        this.handZ = step3.setVisible((Supplier<Boolean>)applyToHand3::getValue);
        this.reset = new RunSetting("Reset position").value(this::resetPos);
        this.addSettings(this.rightX, this.rightY, this.rightZ, this.leftX, this.leftY, this.leftZ, this.applyToHand, this.reset, this.handX, this.handY, this.handZ);
    }
    
    @Override
    public void onEvent() {
    }
    
    private void resetPos() {
        this.getSettings().forEach(setting -> {
            if (setting instanceof final SliderSetting f) {
                f.setValue(0.0f);
            }
        });
    }
    
    @Generated
    public static ViewModelModule getInstance() {
        return ViewModelModule.instance;
    }
    
    static {
        instance = new ViewModelModule();
    }
}
