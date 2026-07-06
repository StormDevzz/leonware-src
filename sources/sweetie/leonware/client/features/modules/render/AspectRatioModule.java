package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.SliderSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/AspectRatioModule.class */
@ModuleRegister(name = "Aspect Ratio", category = Category.RENDER)
public class AspectRatioModule extends Module {
    private static final AspectRatioModule instance = new AspectRatioModule();
    public final SliderSetting ratio = new SliderSetting("Ratio").value(Float.valueOf(1.33f)).range(0.5f, 3.0f).step(0.01f);

    @Generated
    public static AspectRatioModule getInstance() {
        return instance;
    }

    public AspectRatioModule() {
        addSettings(this.ratio);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
    }
}
