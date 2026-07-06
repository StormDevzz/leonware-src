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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/EnchantColorModule.class */
@ModuleRegister(name = "Enchant Color", category = Category.RENDER)
public class EnchantColorModule extends Module {
    private static final EnchantColorModule instance = new EnchantColorModule();
    public final SliderSetting enchantSpeed = new SliderSetting("Скорость чар").value(Float.valueOf(1.0f)).range(0.0f, 2.0f).step(0.1f);
    public final SliderSetting enchantSize = new SliderSetting("Размер чар").value(Float.valueOf(1.0f)).range(0.01f, 5.0f).step(0.01f);
    public final ModeSetting colorMode = new ModeSetting("Цвет").values("Client", "Custom").value("Client");
    public final ColorSetting customColor = new ColorSetting("Свой цвет").value(new Color(255, 255, 255)).setVisible(() -> {
        return Boolean.valueOf(this.colorMode.is("Custom"));
    });
    public final SliderSetting alpha = new SliderSetting("Прозрачность").value(Float.valueOf(180.0f)).range(0.0f, 255.0f).step(1.0f);

    @Generated
    public static EnchantColorModule getInstance() {
        return instance;
    }

    /* JADX WARN: Type inference failed for: r1v13, types: [sweetie.leonware.api.module.setting.ColorSetting] */
    public EnchantColorModule() {
        addSettings(this.enchantSpeed, this.enchantSize, this.colorMode, this.customColor, this.alpha);
    }

    public static float[] getEnchantColor() {
        Color resColor;
        EnchantColorModule mod = getInstance();
        if (mod.colorMode.is("Client")) {
            resColor = UIColors.primary();
        } else {
            resColor = mod.customColor.getValue();
        }
        float r = resColor.getRed() / 255.0f;
        float g = resColor.getGreen() / 255.0f;
        float b = resColor.getBlue() / 255.0f;
        float a = mod.alpha.getValue().floatValue() / 255.0f;
        return new float[]{r, g, b, a};
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
    }
}
