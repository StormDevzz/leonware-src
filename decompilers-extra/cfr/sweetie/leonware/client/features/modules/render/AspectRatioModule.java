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

@ModuleRegister(name="Aspect Ratio", category=Category.RENDER)
public class AspectRatioModule
extends Module {
    private static final AspectRatioModule instance = new AspectRatioModule();
    public final SliderSetting ratio = new SliderSetting("Ratio").value(Float.valueOf(1.33f)).range(0.5f, 3.0f).step(0.01f);

    public AspectRatioModule() {
        this.addSettings(this.ratio);
    }

    @Override
    public void onEvent() {
    }

    @Generated
    public static AspectRatioModule getInstance() {
        return instance;
    }
}

