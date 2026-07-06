package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.client.ui.clickgui.ScreenClickGUI;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/ClickGUIModule.class */
@ModuleRegister(name = "Click GUI", category = Category.RENDER, bind = 96)
public class ClickGUIModule extends Module {
    private static final ClickGUIModule instance = new ClickGUIModule();
    public final ModeSetting imageSetting = new ModeSetting("Изображение").values("Нету", "Леон", "Шлюха", "Девочка1", "Девочка2", "Девочка3", "Девочка4", "Девочка5", "Девочка6", "Девочка7", "Девочка8", "Девочка9", "Девочка10", "Девочка11", "Девочка12", "Девочка13", "Девочка14", "Девочка15", "Девочка16", "Девочка17").value("Нету");
    public final SliderSetting imageSizeSlider = new SliderSetting("Размер").value(Float.valueOf(250.0f)).range(30.0f, 300.0f).step(1.0f);
    public final SliderSetting imageXSlider = new SliderSetting("X позиция").value(Float.valueOf(0.5f)).range(0.0f, 1.2f).step(0.01f);
    public final SliderSetting imageYSlider = new SliderSetting("Y позиция").value(Float.valueOf(0.82f)).range(0.0f, 1.2f).step(0.01f);

    @Generated
    public static ClickGUIModule getInstance() {
        return instance;
    }

    public ClickGUIModule() {
        addSettings(this.imageSetting, this.imageSizeSlider, this.imageXSlider, this.imageYSlider);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onEnable() {
        if (mc.field_1755 != null) {
            return;
        }
        mc.method_1507(ScreenClickGUI.getInstance());
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        toggle();
    }
}
