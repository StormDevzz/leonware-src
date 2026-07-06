package sweetie.leonware.client.features.modules.render;

import java.util.Objects;
import lombok.Generated;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.RunSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/ViewModelModule.class */
@ModuleRegister(name = "View Model", category = Category.RENDER)
public class ViewModelModule extends Module {
    private static final ViewModelModule instance = new ViewModelModule();
    public final SliderSetting rightX = new SliderSetting("Right X").value(Float.valueOf(0.0f)).range(-2.0f, 2.0f).step(0.1f);
    public final SliderSetting rightY = new SliderSetting("Right Y").value(Float.valueOf(0.0f)).range(-2.0f, 2.0f).step(0.1f);
    public final SliderSetting rightZ = new SliderSetting("Right Z").value(Float.valueOf(0.0f)).range(-2.0f, 2.0f).step(0.1f);
    public final SliderSetting leftX = new SliderSetting("Left X").value(Float.valueOf(0.0f)).range(-2.0f, 2.0f).step(0.1f);
    public final SliderSetting leftY = new SliderSetting("Left Y").value(Float.valueOf(0.0f)).range(-2.0f, 2.0f).step(0.1f);
    public final SliderSetting leftZ = new SliderSetting("Left Z").value(Float.valueOf(0.0f)).range(-2.0f, 2.0f).step(0.1f);
    public final BooleanSetting applyToHand = new BooleanSetting("Apply to Hand").value((Boolean) false);
    public final SliderSetting handX;
    public final SliderSetting handY;
    public final SliderSetting handZ;
    private final RunSetting reset;

    @Generated
    public static ViewModelModule getInstance() {
        return instance;
    }

    /* JADX WARN: Type inference failed for: r1v30, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v35, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v40, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    public ViewModelModule() {
        SliderSetting sliderSettingStep = new SliderSetting("Hand X").value(Float.valueOf(0.0f)).range(-2.0f, 2.0f).step(0.1f);
        BooleanSetting booleanSetting = this.applyToHand;
        Objects.requireNonNull(booleanSetting);
        this.handX = sliderSettingStep.setVisible(booleanSetting::getValue);
        SliderSetting sliderSettingStep2 = new SliderSetting("Hand Y").value(Float.valueOf(0.0f)).range(-2.0f, 2.0f).step(0.1f);
        BooleanSetting booleanSetting2 = this.applyToHand;
        Objects.requireNonNull(booleanSetting2);
        this.handY = sliderSettingStep2.setVisible(booleanSetting2::getValue);
        SliderSetting sliderSettingStep3 = new SliderSetting("Hand Z").value(Float.valueOf(0.0f)).range(-2.0f, 2.0f).step(0.1f);
        BooleanSetting booleanSetting3 = this.applyToHand;
        Objects.requireNonNull(booleanSetting3);
        this.handZ = sliderSettingStep3.setVisible(booleanSetting3::getValue);
        this.reset = new RunSetting("Reset position").value(this::resetPos);
        addSettings(this.rightX, this.rightY, this.rightZ, this.leftX, this.leftY, this.leftZ, this.applyToHand, this.reset, this.handX, this.handY, this.handZ);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
    }

    private void resetPos() {
        getSettings().forEach(setting -> {
            if (setting instanceof SliderSetting) {
                SliderSetting f = (SliderSetting) setting;
                f.setValue(Float.valueOf(0.0f));
            }
        });
    }
}
