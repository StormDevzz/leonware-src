package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.SliderSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/BadtripModule.class */
@ModuleRegister(name = "Bad Trip", category = Category.RENDER)
public class BadtripModule extends Module {
    private static final BadtripModule instance = new BadtripModule();
    public final SliderSetting strength = new SliderSetting("Сила эффекта").value(Float.valueOf(0.5f)).range(0.1f, 2.0f).step(0.1f);

    @Generated
    public static BadtripModule getInstance() {
        return instance;
    }

    public BadtripModule() {
        addSettings(this.strength);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
    }
}
