/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.SliderSetting;

@ModuleRegister(name="Ice Speed", category=Category.MOVEMENT)
public class IceSpeedModule
extends Module {
    private static final IceSpeedModule instance = new IceSpeedModule();
    public final SliderSetting speed = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c").value(Float.valueOf(0.4f)).range(0.1f, 1.5f).step(0.01f);

    public IceSpeedModule() {
        this.addSettings(this.speed);
    }

    @Override
    public void onEvent() {
    }

    @Generated
    public static IceSpeedModule getInstance() {
        return instance;
    }
}

