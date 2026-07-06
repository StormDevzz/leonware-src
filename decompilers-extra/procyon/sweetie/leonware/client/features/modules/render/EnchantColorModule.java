// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.module.setting.Setting;
import java.awt.Color;
import sweetie.leonware.api.module.setting.ColorSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Enchant Color", category = Category.RENDER)
public class EnchantColorModule extends Module
{
    private static final EnchantColorModule instance;
    public final SliderSetting enchantSpeed;
    public final SliderSetting enchantSize;
    public final ModeSetting colorMode;
    public final ColorSetting customColor;
    public final SliderSetting alpha;
    
    public EnchantColorModule() {
        this.enchantSpeed = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c \u0447\u0430\u0440").value(1.0f).range(0.0f, 2.0f).step(0.1f);
        this.enchantSize = new SliderSetting("\u0420\u0430\u0437\u043c\u0435\u0440 \u0447\u0430\u0440").value(1.0f).range(0.01f, 5.0f).step(0.01f);
        this.colorMode = new ModeSetting("\u0426\u0432\u0435\u0442").values("Client", "Custom").value("Client");
        this.customColor = new ColorSetting("\u0421\u0432\u043e\u0439 \u0446\u0432\u0435\u0442").value(new Color(255, 255, 255)).setVisible(() -> this.colorMode.is("Custom"));
        this.alpha = new SliderSetting("\u041f\u0440\u043e\u0437\u0440\u0430\u0447\u043d\u043e\u0441\u0442\u044c").value(180.0f).range(0.0f, 255.0f).step(1.0f);
        this.addSettings(this.enchantSpeed, this.enchantSize, this.colorMode, this.customColor, this.alpha);
    }
    
    public static float[] getEnchantColor() {
        final EnchantColorModule mod = getInstance();
        Color resColor;
        if (mod.colorMode.is("Client")) {
            resColor = UIColors.primary();
        }
        else {
            resColor = mod.customColor.getValue();
        }
        final float r = resColor.getRed() / 255.0f;
        final float g = resColor.getGreen() / 255.0f;
        final float b = resColor.getBlue() / 255.0f;
        final float a = mod.alpha.getValue() / 255.0f;
        return new float[] { r, g, b, a };
    }
    
    @Override
    public void onEvent() {
    }
    
    @Generated
    public static EnchantColorModule getInstance() {
        return EnchantColorModule.instance;
    }
    
    static {
        instance = new EnchantColorModule();
    }
}
