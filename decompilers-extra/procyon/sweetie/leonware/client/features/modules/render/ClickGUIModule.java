// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import net.minecraft.class_437;
import sweetie.leonware.client.ui.clickgui.ScreenClickGUI;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Click GUI", category = Category.RENDER, bind = 96)
public class ClickGUIModule extends Module
{
    private static final ClickGUIModule instance;
    public final ModeSetting imageSetting;
    public final SliderSetting imageSizeSlider;
    public final SliderSetting imageXSlider;
    public final SliderSetting imageYSlider;
    
    public ClickGUIModule() {
        this.imageSetting = new ModeSetting("\u0418\u0437\u043e\u0431\u0440\u0430\u0436\u0435\u043d\u0438\u0435").values("\u041d\u0435\u0442\u0443", "\u041b\u0435\u043e\u043d", "\u0428\u043b\u044e\u0445\u0430", "\u0414\u0435\u0432\u043e\u0447\u043a\u04301", "\u0414\u0435\u0432\u043e\u0447\u043a\u04302", "\u0414\u0435\u0432\u043e\u0447\u043a\u04303", "\u0414\u0435\u0432\u043e\u0447\u043a\u04304", "\u0414\u0435\u0432\u043e\u0447\u043a\u04305", "\u0414\u0435\u0432\u043e\u0447\u043a\u04306", "\u0414\u0435\u0432\u043e\u0447\u043a\u04307", "\u0414\u0435\u0432\u043e\u0447\u043a\u04308", "\u0414\u0435\u0432\u043e\u0447\u043a\u04309", "\u0414\u0435\u0432\u043e\u0447\u043a\u043010", "\u0414\u0435\u0432\u043e\u0447\u043a\u043011", "\u0414\u0435\u0432\u043e\u0447\u043a\u043012", "\u0414\u0435\u0432\u043e\u0447\u043a\u043013", "\u0414\u0435\u0432\u043e\u0447\u043a\u043014", "\u0414\u0435\u0432\u043e\u0447\u043a\u043015", "\u0414\u0435\u0432\u043e\u0447\u043a\u043016", "\u0414\u0435\u0432\u043e\u0447\u043a\u043017").value("\u041d\u0435\u0442\u0443");
        this.imageSizeSlider = new SliderSetting("\u0420\u0430\u0437\u043c\u0435\u0440").value(250.0f).range(30.0f, 300.0f).step(1.0f);
        this.imageXSlider = new SliderSetting("X \u043f\u043e\u0437\u0438\u0446\u0438\u044f").value(0.5f).range(0.0f, 1.2f).step(0.01f);
        this.imageYSlider = new SliderSetting("Y \u043f\u043e\u0437\u0438\u0446\u0438\u044f").value(0.82f).range(0.0f, 1.2f).step(0.01f);
        this.addSettings(this.imageSetting, this.imageSizeSlider, this.imageXSlider, this.imageYSlider);
    }
    
    @Override
    public void onEnable() {
        if (ClickGUIModule.mc.field_1755 != null) {
            return;
        }
        ClickGUIModule.mc.method_1507((class_437)ScreenClickGUI.getInstance());
    }
    
    @Override
    public void onEvent() {
        this.toggle();
    }
    
    @Generated
    public static ClickGUIModule getInstance() {
        return ClickGUIModule.instance;
    }
    
    static {
        instance = new ClickGUIModule();
    }
}
