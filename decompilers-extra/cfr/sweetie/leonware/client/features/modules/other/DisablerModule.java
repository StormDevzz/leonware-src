/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_2596
 *  net.minecraft.class_2828$class_2829
 *  net.minecraft.class_2828$class_2831
 */
package sweetie.leonware.client.features.modules.other;

import lombok.Generated;
import net.minecraft.class_2596;
import net.minecraft.class_2828;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.ModeSetting;

@ModuleRegister(name="Disabler", category=Category.OTHER)
public class DisablerModule
extends Module {
    private static final DisablerModule instance = new DisablerModule();
    private final ModeSetting mode = new ModeSetting("\u0420\u0435\u0436\u0438\u043c").values("Rotation").value("Rotation");

    public DisablerModule() {
        this.addSettings(this.mode);
    }

    @Override
    public void onEnable() {
        if (DisablerModule.mc.field_1724 == null) {
            this.disable();
            return;
        }
        switch ((String)this.mode.getValue()) {
            case "Rotation": {
                this.executeRotationDisabler();
            }
        }
        this.disable();
    }

    private void executeRotationDisabler() {
        double x = DisablerModule.mc.field_1724.method_23317();
        double y = DisablerModule.mc.field_1724.method_23318();
        double z = DisablerModule.mc.field_1724.method_23321();
        boolean onGround = DisablerModule.mc.field_1724.method_24828();
        DisablerModule.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(x + 1.0, y + 3.0, z + 1.0, onGround, DisablerModule.mc.field_1724.field_5976));
        DisablerModule.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2831(Float.MAX_VALUE, 0.0f, onGround, DisablerModule.mc.field_1724.field_5976));
        DisablerModule.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(x + 2.0, y + 2.0, z + 2.0, onGround, DisablerModule.mc.field_1724.field_5976));
        DisablerModule.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2831(-3.4028235E38f, 0.0f, onGround, DisablerModule.mc.field_1724.field_5976));
    }

    @Override
    public void onEvent() {
    }

    @Generated
    public static DisablerModule getInstance() {
        return instance;
    }
}

