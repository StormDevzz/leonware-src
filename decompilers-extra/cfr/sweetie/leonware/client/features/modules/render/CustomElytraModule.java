/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

@ModuleRegister(name="Custom Elytra", category=Category.RENDER)
public class CustomElytraModule
extends Module {
    private static final CustomElytraModule instance = new CustomElytraModule();
    public final SliderSetting elytraScale = new SliderSetting("\u0420\u0430\u0437\u043c\u0435\u0440 \u044d\u043b\u0438\u0442\u0440\u044b").value(Float.valueOf(1.0f)).range(0.1f, 3.0f).step(0.05f);
    public final SliderSetting elytraX = new SliderSetting("\u042d\u043b\u0438\u0442\u0440\u0430 X").value(Float.valueOf(0.0f)).range(-2.0f, 2.0f).step(0.01f);
    public final SliderSetting elytraY = new SliderSetting("\u042d\u043b\u0438\u0442\u0440\u0430 Y").value(Float.valueOf(0.0f)).range(-2.0f, 2.0f).step(0.01f);
    public final SliderSetting elytraZ = new SliderSetting("\u042d\u043b\u0438\u0442\u0440\u0430 Z").value(Float.valueOf(0.0f)).range(-2.0f, 2.0f).step(0.01f);
    public final BooleanSetting customModelElytra = new BooleanSetting("\u041d\u0430\u0441\u0442\u0440\u043e\u0439\u043a\u0438 \u0434\u043b\u044f \u043a\u0430\u0441\u0442\u043e\u043c \u043c\u043e\u0434\u0435\u043b\u0438").value(true);
    public final SliderSetting customScale = new SliderSetting("\u0420\u0430\u0437\u043c\u0435\u0440 \u044d\u043b\u0438\u0442\u0440\u044b").value(Float.valueOf(0.67f)).range(0.1f, 3.0f).step(0.05f).setVisible(this.customModelElytra::getValue);
    public final SliderSetting customX = new SliderSetting("\u042d\u043b\u0438\u0442\u0440\u0430 X").value(Float.valueOf(0.0f)).range(-2.0f, 2.0f).step(0.01f).setVisible(this.customModelElytra::getValue);
    public final SliderSetting customY = new SliderSetting("\u042d\u043b\u0438\u0442\u0440\u0430 Y").value(Float.valueOf(0.6f)).range(-2.0f, 2.0f).step(0.01f).setVisible(this.customModelElytra::getValue);
    public final SliderSetting customZ = new SliderSetting("\u042d\u043b\u0438\u0442\u0440\u0430 Z").value(Float.valueOf(0.12f)).range(-2.0f, 2.0f).step(0.01f).setVisible(this.customModelElytra::getValue);

    public CustomElytraModule() {
        this.addSettings(this.elytraScale, this.elytraX, this.elytraY, this.elytraZ, this.customModelElytra, this.customScale, this.customX, this.customY, this.customZ);
    }

    @Override
    public void onEvent() {
    }

    @Generated
    public static CustomElytraModule getInstance() {
        return instance;
    }
}

