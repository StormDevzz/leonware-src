package sweetie.leonware.client.features.modules.render;

import java.util.Objects;
import lombok.Generated;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/CustomElytraModule.class */
@ModuleRegister(name = "Custom Elytra", category = Category.RENDER)
public class CustomElytraModule extends Module {
    private static final CustomElytraModule instance = new CustomElytraModule();
    public final SliderSetting elytraScale = new SliderSetting("Размер элитры").value(Float.valueOf(1.0f)).range(0.1f, 3.0f).step(0.05f);
    public final SliderSetting elytraX = new SliderSetting("Элитра X").value(Float.valueOf(0.0f)).range(-2.0f, 2.0f).step(0.01f);
    public final SliderSetting elytraY = new SliderSetting("Элитра Y").value(Float.valueOf(0.0f)).range(-2.0f, 2.0f).step(0.01f);
    public final SliderSetting elytraZ = new SliderSetting("Элитра Z").value(Float.valueOf(0.0f)).range(-2.0f, 2.0f).step(0.01f);
    public final BooleanSetting customModelElytra = new BooleanSetting("Настройки для кастом модели").value((Boolean) true);
    public final SliderSetting customScale;
    public final SliderSetting customX;
    public final SliderSetting customY;
    public final SliderSetting customZ;

    @Generated
    public static CustomElytraModule getInstance() {
        return instance;
    }

    /* JADX WARN: Type inference failed for: r1v22, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v27, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v32, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v37, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    public CustomElytraModule() {
        SliderSetting sliderSettingStep = new SliderSetting("Размер элитры").value(Float.valueOf(0.67f)).range(0.1f, 3.0f).step(0.05f);
        BooleanSetting booleanSetting = this.customModelElytra;
        Objects.requireNonNull(booleanSetting);
        this.customScale = sliderSettingStep.setVisible(booleanSetting::getValue);
        SliderSetting sliderSettingStep2 = new SliderSetting("Элитра X").value(Float.valueOf(0.0f)).range(-2.0f, 2.0f).step(0.01f);
        BooleanSetting booleanSetting2 = this.customModelElytra;
        Objects.requireNonNull(booleanSetting2);
        this.customX = sliderSettingStep2.setVisible(booleanSetting2::getValue);
        SliderSetting sliderSettingStep3 = new SliderSetting("Элитра Y").value(Float.valueOf(0.6f)).range(-2.0f, 2.0f).step(0.01f);
        BooleanSetting booleanSetting3 = this.customModelElytra;
        Objects.requireNonNull(booleanSetting3);
        this.customY = sliderSettingStep3.setVisible(booleanSetting3::getValue);
        SliderSetting sliderSettingStep4 = new SliderSetting("Элитра Z").value(Float.valueOf(0.12f)).range(-2.0f, 2.0f).step(0.01f);
        BooleanSetting booleanSetting4 = this.customModelElytra;
        Objects.requireNonNull(booleanSetting4);
        this.customZ = sliderSettingStep4.setVisible(booleanSetting4::getValue);
        addSettings(this.elytraScale, this.elytraX, this.elytraY, this.elytraZ, this.customModelElytra, this.customScale, this.customX, this.customY, this.customZ);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
    }
}
