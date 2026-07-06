package sweetie.leonware.client.features.modules.other;

import lombok.Generated;
import net.minecraft.class_3414;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.other.SoundUtil;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/other/ToggleSoundsModule.class */
@ModuleRegister(name = "Toggle Sounds", category = Category.OTHER)
public class ToggleSoundsModule extends Module {
    private static final ToggleSoundsModule instance = new ToggleSoundsModule();
    private final ModeSetting sound = new ModeSetting("Sound").value("Blop").values("Smooth", "Celestial", "Nursultan", "Akrien", "Tech", "Blop");
    public final SliderSetting volume = new SliderSetting("Volume").value(Float.valueOf(60.0f)).range(1.0f, 100.0f).step(1.0f);

    @Generated
    public static ToggleSoundsModule getInstance() {
        return instance;
    }

    public ToggleSoundsModule() {
        addSettings(this.sound, this.volume);
    }

    public static void playToggle(boolean state) {
        class_3414 class_3414Var;
        if (instance.isEnabled()) {
            switch (instance.sound.getValue()) {
                case "Nursultan":
                    if (!state) {
                        class_3414Var = SoundUtil.DISABLE_NU_EVENT;
                        break;
                    } else {
                        class_3414Var = SoundUtil.ENABLE_NU_EVENT;
                        break;
                    }
                    break;
                case "Celestial":
                    if (!state) {
                        class_3414Var = SoundUtil.DISABLE_CEL_EVENT;
                        break;
                    } else {
                        class_3414Var = SoundUtil.ENABLE_CEL_EVENT;
                        break;
                    }
                    break;
                case "Akrien":
                    if (!state) {
                        class_3414Var = SoundUtil.DISABLE_AK_EVENT;
                        break;
                    } else {
                        class_3414Var = SoundUtil.ENABLE_AK_EVENT;
                        break;
                    }
                    break;
                case "Tech":
                    if (!state) {
                        class_3414Var = SoundUtil.DISABLE_TECH_EVENT;
                        break;
                    } else {
                        class_3414Var = SoundUtil.ENABLE_TECH_EVENT;
                        break;
                    }
                    break;
                case "Blop":
                    if (!state) {
                        class_3414Var = SoundUtil.DISABLE_BLOP_EVENT;
                        break;
                    } else {
                        class_3414Var = SoundUtil.ENABLE_BLOP_EVENT;
                        break;
                    }
                    break;
                default:
                    if (!state) {
                        class_3414Var = SoundUtil.DISABLE_SMOOTH_EVENT;
                        break;
                    } else {
                        class_3414Var = SoundUtil.ENABLE_SMOOTH_EVENT;
                        break;
                    }
                    break;
            }
            SoundUtil.playSound(class_3414Var);
        }
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
    }
}
