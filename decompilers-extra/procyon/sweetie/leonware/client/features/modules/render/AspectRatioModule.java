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

@ModuleRegister(name = "Aspect Ratio", category = Category.RENDER)
public class AspectRatioModule extends Module
{
    private static final AspectRatioModule instance;
    public final SliderSetting ratio;
    
    public AspectRatioModule() {
        this.ratio = new SliderSetting("Ratio").value(1.33f).range(0.5f, 3.0f).step(0.01f);
        this.addSettings(this.ratio);
    }
    
    @Override
    public void onEvent() {
    }
    
    @Generated
    public static AspectRatioModule getInstance() {
        return AspectRatioModule.instance;
    }
    
    static {
        instance = new AspectRatioModule();
    }
}
