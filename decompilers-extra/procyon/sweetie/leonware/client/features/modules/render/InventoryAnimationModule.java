// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Inventory Animation", category = Category.RENDER)
public class InventoryAnimationModule extends Module
{
    private static final InventoryAnimationModule instance;
    public final ModeSetting animationType;
    public final SliderSetting speed;
    public final BooleanSetting inventoryOnly;
    
    public InventoryAnimationModule() {
        this.animationType = new ModeSetting("Type").value("Scale").values("Scale", "Bounce", "SlideUp", "SlideDown", "SlideLeft", "SlideRight", "Flip", "Warp", "Glitch", "Fade");
        this.speed = new SliderSetting("Speed").value(10.0f).range(1.0f, 30.0f).step(1.0f);
        this.inventoryOnly = new BooleanSetting("InventoryOnly").value(false);
        this.addSettings(this.animationType, this.speed, this.inventoryOnly);
    }
    
    @Override
    public void onEvent() {
    }
    
    @Generated
    public static InventoryAnimationModule getInstance() {
        return InventoryAnimationModule.instance;
    }
    
    static {
        instance = new InventoryAnimationModule();
    }
}
