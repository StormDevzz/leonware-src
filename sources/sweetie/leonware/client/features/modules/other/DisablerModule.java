package sweetie.leonware.client.features.modules.other;

import lombok.Generated;
import net.minecraft.class_2828;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.ModeSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/other/DisablerModule.class */
@ModuleRegister(name = "Disabler", category = Category.OTHER)
public class DisablerModule extends Module {
    private static final DisablerModule instance = new DisablerModule();
    private final ModeSetting mode = new ModeSetting("Режим").values("Rotation").value("Rotation");

    @Generated
    public static DisablerModule getInstance() {
        return instance;
    }

    public DisablerModule() {
        addSettings(this.mode);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onEnable() {
        if (mc.field_1724 == null) {
            disable();
            return;
        }
        switch (this.mode.getValue()) {
            case "Rotation":
                executeRotationDisabler();
                break;
        }
        disable();
    }

    private void executeRotationDisabler() {
        double x = mc.field_1724.method_23317();
        double y = mc.field_1724.method_23318();
        double z = mc.field_1724.method_23321();
        boolean onGround = mc.field_1724.method_24828();
        mc.field_1724.field_3944.method_52787(new class_2828.class_2829(x + 1.0d, y + 3.0d, z + 1.0d, onGround, mc.field_1724.field_5976));
        mc.field_1724.field_3944.method_52787(new class_2828.class_2831(Float.MAX_VALUE, 0.0f, onGround, mc.field_1724.field_5976));
        mc.field_1724.field_3944.method_52787(new class_2828.class_2829(x + 2.0d, y + 2.0d, z + 2.0d, onGround, mc.field_1724.field_5976));
        mc.field_1724.field_3944.method_52787(new class_2828.class_2831(-3.4028235E38f, 0.0f, onGround, mc.field_1724.field_5976));
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
    }
}
