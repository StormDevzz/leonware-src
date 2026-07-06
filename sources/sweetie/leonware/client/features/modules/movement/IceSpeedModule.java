package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.SliderSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/IceSpeedModule.class */
@ModuleRegister(name = "Ice Speed", category = Category.MOVEMENT)
public class IceSpeedModule extends Module {
    private static final IceSpeedModule instance = new IceSpeedModule();
    public final SliderSetting speed = new SliderSetting("Скорость").value(Float.valueOf(0.4f)).range(0.1f, 1.5f).step(0.01f);

    @Generated
    public static IceSpeedModule getInstance() {
        return instance;
    }

    public IceSpeedModule() {
        addSettings(this.speed);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
    }
}
