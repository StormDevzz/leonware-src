/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.client.features.modules.other;

import lombok.Generated;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.other.SoundUtil;

@ModuleRegister(name="Toggle Sounds", category=Category.OTHER)
public class ToggleSoundsModule
extends Module {
    private static final ToggleSoundsModule instance = new ToggleSoundsModule();
    private final ModeSetting sound = new ModeSetting("Sound").value("Blop").values("Smooth", "Celestial", "Nursultan", "Akrien", "Tech", "Blop");
    public final SliderSetting volume = new SliderSetting("Volume").value(Float.valueOf(60.0f)).range(1.0f, 100.0f).step(1.0f);

    public ToggleSoundsModule() {
        this.addSettings(this.sound, this.volume);
    }

    public static void playToggle(boolean state) {
        if (!instance.isEnabled()) {
            return;
        }
        SoundUtil.playSound(switch ((String)ToggleSoundsModule.instance.sound.getValue()) {
            case "Nursultan" -> {
                if (state) {
                    yield SoundUtil.ENABLE_NU_EVENT;
                }
                yield SoundUtil.DISABLE_NU_EVENT;
            }
            case "Celestial" -> {
                if (state) {
                    yield SoundUtil.ENABLE_CEL_EVENT;
                }
                yield SoundUtil.DISABLE_CEL_EVENT;
            }
            case "Akrien" -> {
                if (state) {
                    yield SoundUtil.ENABLE_AK_EVENT;
                }
                yield SoundUtil.DISABLE_AK_EVENT;
            }
            case "Tech" -> {
                if (state) {
                    yield SoundUtil.ENABLE_TECH_EVENT;
                }
                yield SoundUtil.DISABLE_TECH_EVENT;
            }
            case "Blop" -> {
                if (state) {
                    yield SoundUtil.ENABLE_BLOP_EVENT;
                }
                yield SoundUtil.DISABLE_BLOP_EVENT;
            }
            default -> state ? SoundUtil.ENABLE_SMOOTH_EVENT : SoundUtil.DISABLE_SMOOTH_EVENT;
        });
    }

    @Override
    public void onEvent() {
    }

    @Generated
    public static ToggleSoundsModule getInstance() {
        return instance;
    }
}

