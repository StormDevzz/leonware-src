package sweetie.leonware.client.features.modules.render;

import java.awt.Color;
import lombok.Generated;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.ColorSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/CustomWorldModule.class */
@ModuleRegister(name = "Custom World", category = Category.RENDER)
public class CustomWorldModule extends Module {
    private static final CustomWorldModule instance = new CustomWorldModule();
    public final ColorSetting skyColor = new ColorSetting("Общий цвет").value(new Color(100, 150, 255));
    public final SliderSetting skyIntensity = new SliderSetting("Интенсивность").value(Float.valueOf(0.5f)).range(0.0f, 3.0f).step(0.01f);
    public final ColorSetting blockColor = new ColorSetting("Цвет блоков").value(new Color(255, 200, 100));
    public final SliderSetting blockIntensity = new SliderSetting("Интенсивность").value(Float.valueOf(0.5f)).range(0.0f, 3.0f).step(0.01f);
    public final ColorSetting ambientColor = new ColorSetting("Цвет окружения").value(new Color(150, 150, 200));
    public final SliderSetting ambientIntensity = new SliderSetting("Интенсивность").value(Float.valueOf(0.5f)).range(0.0f, 3.0f).step(0.01f);

    @Generated
    public static CustomWorldModule getInstance() {
        return instance;
    }

    public CustomWorldModule() {
        addSettings(this.skyColor, this.skyIntensity, this.blockColor, this.blockIntensity, this.ambientColor, this.ambientIntensity);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
    }
}
