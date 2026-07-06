/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.client.features.modules.render;

import java.awt.Color;
import lombok.Generated;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.ColorSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.color.UIColors;

@ModuleRegister(name="Enchant Color", category=Category.RENDER)
public class EnchantColorModule
extends Module {
    private static final EnchantColorModule instance = new EnchantColorModule();
    public final SliderSetting enchantSpeed = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c \u0447\u0430\u0440").value(Float.valueOf(1.0f)).range(0.0f, 2.0f).step(0.1f);
    public final SliderSetting enchantSize = new SliderSetting("\u0420\u0430\u0437\u043c\u0435\u0440 \u0447\u0430\u0440").value(Float.valueOf(1.0f)).range(0.01f, 5.0f).step(0.01f);
    public final ModeSetting colorMode = new ModeSetting("\u0426\u0432\u0435\u0442").values("Client", "Custom").value("Client");
    public final ColorSetting customColor = new ColorSetting("\u0421\u0432\u043e\u0439 \u0446\u0432\u0435\u0442").value(new Color(255, 255, 255)).setVisible(() -> this.colorMode.is("Custom"));
    public final SliderSetting alpha = new SliderSetting("\u041f\u0440\u043e\u0437\u0440\u0430\u0447\u043d\u043e\u0441\u0442\u044c").value(Float.valueOf(180.0f)).range(0.0f, 255.0f).step(1.0f);

    public EnchantColorModule() {
        this.addSettings(this.enchantSpeed, this.enchantSize, this.colorMode, this.customColor, this.alpha);
    }

    public static float[] getEnchantColor() {
        EnchantColorModule mod = EnchantColorModule.getInstance();
        Color resColor = mod.colorMode.is("Client") ? UIColors.primary() : (Color)mod.customColor.getValue();
        float r = (float)resColor.getRed() / 255.0f;
        float g = (float)resColor.getGreen() / 255.0f;
        float b = (float)resColor.getBlue() / 255.0f;
        float a = ((Float)mod.alpha.getValue()).floatValue() / 255.0f;
        return new float[]{r, g, b, a};
    }

    @Override
    public void onEvent() {
    }

    @Generated
    public static EnchantColorModule getInstance() {
        return instance;
    }
}

