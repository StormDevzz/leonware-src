// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Bad Trip", category = Category.RENDER)
public class BadtripModule extends Module
{
    private static final BadtripModule instance;
    public final SliderSetting strength;
    
    public BadtripModule() {
        this.strength = new SliderSetting("\u0421\u0438\u043b\u0430 \u044d\u0444\u0444\u0435\u043a\u0442\u0430").value(0.5f).range(0.1f, 2.0f).step(0.1f);
        this.addSettings(this.strength);
    }
    
    @Override
    public void onEvent() {
    }
    
    @Generated
    public static BadtripModule getInstance() {
        return BadtripModule.instance;
    }
    
    static {
        instance = new BadtripModule();
    }
}
