// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import sweetie.leonware.api.module.setting.Setting;
import java.awt.Color;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.ColorSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Custom World", category = Category.RENDER)
public class CustomWorldModule extends Module
{
    private static final CustomWorldModule instance;
    public final ColorSetting skyColor;
    public final SliderSetting skyIntensity;
    public final ColorSetting blockColor;
    public final SliderSetting blockIntensity;
    public final ColorSetting ambientColor;
    public final SliderSetting ambientIntensity;
    
    public CustomWorldModule() {
        this.skyColor = new ColorSetting("\u041e\u0431\u0449\u0438\u0439 \u0446\u0432\u0435\u0442").value(new Color(100, 150, 255));
        this.skyIntensity = new SliderSetting("\u0418\u043d\u0442\u0435\u043d\u0441\u0438\u0432\u043d\u043e\u0441\u0442\u044c").value(0.5f).range(0.0f, 3.0f).step(0.01f);
        this.blockColor = new ColorSetting("\u0426\u0432\u0435\u0442 \u0431\u043b\u043e\u043a\u043e\u0432").value(new Color(255, 200, 100));
        this.blockIntensity = new SliderSetting("\u0418\u043d\u0442\u0435\u043d\u0441\u0438\u0432\u043d\u043e\u0441\u0442\u044c").value(0.5f).range(0.0f, 3.0f).step(0.01f);
        this.ambientColor = new ColorSetting("\u0426\u0432\u0435\u0442 \u043e\u043a\u0440\u0443\u0436\u0435\u043d\u0438\u044f").value(new Color(150, 150, 200));
        this.ambientIntensity = new SliderSetting("\u0418\u043d\u0442\u0435\u043d\u0441\u0438\u0432\u043d\u043e\u0441\u0442\u044c").value(0.5f).range(0.0f, 3.0f).step(0.01f);
        this.addSettings(this.skyColor, this.skyIntensity, this.blockColor, this.blockIntensity, this.ambientColor, this.ambientIntensity);
    }
    
    @Override
    public void onEvent() {
    }
    
    @Generated
    public static CustomWorldModule getInstance() {
        return CustomWorldModule.instance;
    }
    
    static {
        instance = new CustomWorldModule();
    }
}
