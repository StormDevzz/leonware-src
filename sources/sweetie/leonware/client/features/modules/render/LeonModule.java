package sweetie.leonware.client.features.modules.render;

import java.util.Objects;
import lombok.Generated;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/LeonModule.class */
@ModuleRegister(name = "Leons Hat", category = Category.RENDER)
public class LeonModule extends Module {
    private static final LeonModule instance = new LeonModule();
    public final BooleanSetting onlySelf = new BooleanSetting("Только на себя").value((Boolean) true);
    public final BooleanSetting friends;

    @Generated
    public static LeonModule getInstance() {
        return instance;
    }

    /* JADX WARN: Type inference failed for: r1v4, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    public LeonModule() {
        BooleanSetting booleanSettingValue = new BooleanSetting("И на друзей").value((Boolean) false);
        BooleanSetting booleanSetting = this.onlySelf;
        Objects.requireNonNull(booleanSetting);
        this.friends = booleanSettingValue.setVisible(booleanSetting::getValue);
        addSettings(this.onlySelf, this.friends);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
    }
}
