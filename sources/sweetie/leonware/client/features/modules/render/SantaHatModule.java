package sweetie.leonware.client.features.modules.render;

import java.util.Objects;
import lombok.Generated;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/SantaHatModule.class */
@ModuleRegister(name = "Santa Hat", category = Category.RENDER)
public class SantaHatModule extends Module {
    private static final SantaHatModule instance = new SantaHatModule();
    public final ModeSetting mode = new ModeSetting("Texture").value("Standard").values("Standard", "Ukraine");
    public final BooleanSetting onlySelf = new BooleanSetting("Только на себя").value((Boolean) true);
    public final BooleanSetting friends;

    @Generated
    public static SantaHatModule getInstance() {
        return instance;
    }

    /* JADX WARN: Type inference failed for: r1v7, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    public SantaHatModule() {
        BooleanSetting booleanSettingValue = new BooleanSetting("И на друзей").value((Boolean) false);
        BooleanSetting booleanSetting = this.onlySelf;
        Objects.requireNonNull(booleanSetting);
        this.friends = booleanSettingValue.setVisible(booleanSetting::getValue);
        addSettings(this.onlySelf, this.friends, this.mode);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
    }
}
