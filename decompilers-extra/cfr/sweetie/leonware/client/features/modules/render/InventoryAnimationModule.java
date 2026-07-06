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
import sweetie.leonware.api.module.setting.SliderSetting;

@ModuleRegister(name="Inventory Animation", category=Category.RENDER)
public class InventoryAnimationModule
extends Module {
    private static final InventoryAnimationModule instance = new InventoryAnimationModule();
    public final ModeSetting animationType = new ModeSetting("Type").value("Scale").values("Scale", "Bounce", "SlideUp", "SlideDown", "SlideLeft", "SlideRight", "Flip", "Warp", "Glitch", "Fade");
    public final SliderSetting speed = new SliderSetting("Speed").value(Float.valueOf(10.0f)).range(1.0f, 30.0f).step(1.0f);
    public final BooleanSetting inventoryOnly = new BooleanSetting("InventoryOnly").value(false);

    public InventoryAnimationModule() {
        this.addSettings(this.animationType, this.speed, this.inventoryOnly);
    }

    @Override
    public void onEvent() {
    }

    @Generated
    public static InventoryAnimationModule getInstance() {
        return instance;
    }
}

