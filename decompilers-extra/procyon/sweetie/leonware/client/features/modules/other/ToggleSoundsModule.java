// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.other;

import lombok.Generated;
import net.minecraft.class_3414;
import sweetie.leonware.api.utils.other.SoundUtil;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Toggle Sounds", category = Category.OTHER)
public class ToggleSoundsModule extends Module
{
    private static final ToggleSoundsModule instance;
    private final ModeSetting sound;
    public final SliderSetting volume;
    
    public ToggleSoundsModule() {
        this.sound = new ModeSetting("Sound").value("Blop").values("Smooth", "Celestial", "Nursultan", "Akrien", "Tech", "Blop");
        this.volume = new SliderSetting("Volume").value(60.0f).range(1.0f, 100.0f).step(1.0f);
        this.addSettings(this.sound, this.volume);
    }
    
    public static void playToggle(final boolean state) {
        if (!ToggleSoundsModule.instance.isEnabled()) {
            return;
        }
        final String s = ToggleSoundsModule.instance.sound.getValue();
        SoundUtil.playSound(switch (s) {
            case "Nursultan" -> state ? SoundUtil.ENABLE_NU_EVENT : SoundUtil.DISABLE_NU_EVENT;
            case "Celestial" -> state ? SoundUtil.ENABLE_CEL_EVENT : SoundUtil.DISABLE_CEL_EVENT;
            case "Akrien" -> state ? SoundUtil.ENABLE_AK_EVENT : SoundUtil.DISABLE_AK_EVENT;
            case "Tech" -> state ? SoundUtil.ENABLE_TECH_EVENT : SoundUtil.DISABLE_TECH_EVENT;
            case "Blop" -> state ? SoundUtil.ENABLE_BLOP_EVENT : SoundUtil.DISABLE_BLOP_EVENT;
            default -> state ? SoundUtil.ENABLE_SMOOTH_EVENT : SoundUtil.DISABLE_SMOOTH_EVENT;
        });
    }
    
    @Override
    public void onEvent() {
    }
    
    @Generated
    public static ToggleSoundsModule getInstance() {
        return ToggleSoundsModule.instance;
    }
    
    static {
        instance = new ToggleSoundsModule();
    }
}
