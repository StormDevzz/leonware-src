package sweetie.leonware.client.features.modules.render;

import java.util.Objects;
import lombok.Generated;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/BabyModelModule.class */
@ModuleRegister(name = "Baby Model", category = Category.RENDER)
public class BabyModelModule extends Module {
    public static boolean currentShouldScale = false;
    private static final BabyModelModule instance = new BabyModelModule();
    public final BooleanSetting onlySelf = new BooleanSetting("Только на себя").value((Boolean) true);
    public final BooleanSetting friends;
    public final SliderSetting allScale;
    public final SliderSetting headScale;
    public final BooleanSetting cameraOffset;

    @Generated
    public static BabyModelModule getInstance() {
        return instance;
    }

    /* JADX WARN: Type inference failed for: r1v4, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    public BabyModelModule() {
        BooleanSetting booleanSettingValue = new BooleanSetting("И на друзей").value((Boolean) false);
        BooleanSetting booleanSetting = this.onlySelf;
        Objects.requireNonNull(booleanSetting);
        this.friends = booleanSettingValue.setVisible(booleanSetting::getValue);
        this.allScale = new SliderSetting("Размер тела").value(Float.valueOf(0.5f)).range(0.1f, 2.0f).step(0.1f);
        this.headScale = new SliderSetting("Размер головы").value(Float.valueOf(1.0f)).range(0.1f, 3.0f).step(0.1f);
        this.cameraOffset = new BooleanSetting("Смещение камеры").value((Boolean) true);
        addSettings(this.onlySelf, this.friends, this.allScale, this.headScale, this.cameraOffset);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
    }
}
