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

@ModuleRegister(name="Baby Model", category=Category.RENDER)
public class BabyModelModule
extends Module {
    public static boolean currentShouldScale = false;
    private static final BabyModelModule instance = new BabyModelModule();
    public final BooleanSetting onlySelf = new BooleanSetting("\u0422\u043e\u043b\u044c\u043a\u043e \u043d\u0430 \u0441\u0435\u0431\u044f").value(true);
    public final BooleanSetting friends = new BooleanSetting("\u0418 \u043d\u0430 \u0434\u0440\u0443\u0437\u0435\u0439").value(false).setVisible(this.onlySelf::getValue);
    public final SliderSetting allScale = new SliderSetting("\u0420\u0430\u0437\u043c\u0435\u0440 \u0442\u0435\u043b\u0430").value(Float.valueOf(0.5f)).range(0.1f, 2.0f).step(0.1f);
    public final SliderSetting headScale = new SliderSetting("\u0420\u0430\u0437\u043c\u0435\u0440 \u0433\u043e\u043b\u043e\u0432\u044b").value(Float.valueOf(1.0f)).range(0.1f, 3.0f).step(0.1f);
    public final BooleanSetting cameraOffset = new BooleanSetting("\u0421\u043c\u0435\u0449\u0435\u043d\u0438\u0435 \u043a\u0430\u043c\u0435\u0440\u044b").value(true);

    public BabyModelModule() {
        this.addSettings(this.onlySelf, this.friends, this.allScale, this.headScale, this.cameraOffset);
    }

    @Override
    public void onEvent() {
    }

    @Generated
    public static BabyModelModule getInstance() {
        return instance;
    }
}

