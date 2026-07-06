package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/InventoryAnimationModule.class */
@ModuleRegister(name = "Inventory Animation", category = Category.RENDER)
public class InventoryAnimationModule extends Module {
    private static final InventoryAnimationModule instance = new InventoryAnimationModule();
    public final ModeSetting animationType = new ModeSetting("Type").value("Scale").values("Scale", "Bounce", "SlideUp", "SlideDown", "SlideLeft", "SlideRight", "Flip", "Warp", "Glitch", "Fade");
    public final SliderSetting speed = new SliderSetting("Speed").value(Float.valueOf(10.0f)).range(1.0f, 30.0f).step(1.0f);
    public final BooleanSetting inventoryOnly = new BooleanSetting("InventoryOnly").value((Boolean) false);

    @Generated
    public static InventoryAnimationModule getInstance() {
        return instance;
    }

    public InventoryAnimationModule() {
        addSettings(this.animationType, this.speed, this.inventoryOnly);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
    }
}
