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
import sweetie.leonware.api.module.setting.SliderSetting;

@ModuleRegister(name="Bad Trip", category=Category.RENDER)
public class BadtripModule
extends Module {
    private static final BadtripModule instance = new BadtripModule();
    public final SliderSetting strength = new SliderSetting("\u0421\u0438\u043b\u0430 \u044d\u0444\u0444\u0435\u043a\u0442\u0430").value(Float.valueOf(0.5f)).range(0.1f, 2.0f).step(0.1f);

    public BadtripModule() {
        this.addSettings(this.strength);
    }

    @Override
    public void onEvent() {
    }

    @Generated
    public static BadtripModule getInstance() {
        return instance;
    }
}

