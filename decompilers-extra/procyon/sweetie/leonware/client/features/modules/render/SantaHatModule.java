// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import sweetie.leonware.api.module.setting.Setting;
import java.util.function.Supplier;
import java.util.Objects;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Santa Hat", category = Category.RENDER)
public class SantaHatModule extends Module
{
    private static final SantaHatModule instance;
    public final ModeSetting mode;
    public final BooleanSetting onlySelf;
    public final BooleanSetting friends;
    
    public SantaHatModule() {
        this.mode = new ModeSetting("Texture").value("Standard").values("Standard", "Ukraine");
        this.onlySelf = new BooleanSetting("\u0422\u043e\u043b\u044c\u043a\u043e \u043d\u0430 \u0441\u0435\u0431\u044f").value(true);
        final BooleanSetting value = new BooleanSetting("\u0418 \u043d\u0430 \u0434\u0440\u0443\u0437\u0435\u0439").value(false);
        final BooleanSetting onlySelf = this.onlySelf;
        Objects.requireNonNull(onlySelf);
        this.friends = value.setVisible((Supplier<Boolean>)onlySelf::getValue);
        this.addSettings(this.onlySelf, this.friends, this.mode);
    }
    
    @Override
    public void onEvent() {
    }
    
    @Generated
    public static SantaHatModule getInstance() {
        return SantaHatModule.instance;
    }
    
    static {
        instance = new SantaHatModule();
    }
}
