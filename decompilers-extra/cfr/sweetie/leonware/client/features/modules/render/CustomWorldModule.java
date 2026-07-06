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
import sweetie.leonware.api.module.setting.SliderSetting;

@ModuleRegister(name="Custom World", category=Category.RENDER)
public class CustomWorldModule
extends Module {
    private static final CustomWorldModule instance = new CustomWorldModule();
    public final ColorSetting skyColor = new ColorSetting("\u041e\u0431\u0449\u0438\u0439 \u0446\u0432\u0435\u0442").value(new Color(100, 150, 255));
    public final SliderSetting skyIntensity = new SliderSetting("\u0418\u043d\u0442\u0435\u043d\u0441\u0438\u0432\u043d\u043e\u0441\u0442\u044c").value(Float.valueOf(0.5f)).range(0.0f, 3.0f).step(0.01f);
    public final ColorSetting blockColor = new ColorSetting("\u0426\u0432\u0435\u0442 \u0431\u043b\u043e\u043a\u043e\u0432").value(new Color(255, 200, 100));
    public final SliderSetting blockIntensity = new SliderSetting("\u0418\u043d\u0442\u0435\u043d\u0441\u0438\u0432\u043d\u043e\u0441\u0442\u044c").value(Float.valueOf(0.5f)).range(0.0f, 3.0f).step(0.01f);
    public final ColorSetting ambientColor = new ColorSetting("\u0426\u0432\u0435\u0442 \u043e\u043a\u0440\u0443\u0436\u0435\u043d\u0438\u044f").value(new Color(150, 150, 200));
    public final SliderSetting ambientIntensity = new SliderSetting("\u0418\u043d\u0442\u0435\u043d\u0441\u0438\u0432\u043d\u043e\u0441\u0442\u044c").value(Float.valueOf(0.5f)).range(0.0f, 3.0f).step(0.01f);

    public CustomWorldModule() {
        this.addSettings(this.skyColor, this.skyIntensity, this.blockColor, this.blockIntensity, this.ambientColor, this.ambientIntensity);
    }

    @Override
    public void onEvent() {
    }

    @Generated
    public static CustomWorldModule getInstance() {
        return instance;
    }
}

