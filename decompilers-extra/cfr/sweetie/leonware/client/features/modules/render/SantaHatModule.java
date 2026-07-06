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
import sweetie.leonware.api.module.setting.ModeSetting;

@ModuleRegister(name="Santa Hat", category=Category.RENDER)
public class SantaHatModule
extends Module {
    private static final SantaHatModule instance = new SantaHatModule();
    public final ModeSetting mode = new ModeSetting("Texture").value("Standard").values("Standard", "Ukraine");
    public final BooleanSetting onlySelf = new BooleanSetting("\u0422\u043e\u043b\u044c\u043a\u043e \u043d\u0430 \u0441\u0435\u0431\u044f").value(true);
    public final BooleanSetting friends = new BooleanSetting("\u0418 \u043d\u0430 \u0434\u0440\u0443\u0437\u0435\u0439").value(false).setVisible(this.onlySelf::getValue);

    public SantaHatModule() {
        this.addSettings(this.onlySelf, this.friends, this.mode);
    }

    @Override
    public void onEvent() {
    }

    @Generated
    public static SantaHatModule getInstance() {
        return instance;
    }
}

