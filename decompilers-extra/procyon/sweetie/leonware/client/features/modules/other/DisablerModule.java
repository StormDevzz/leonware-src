// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.other;

import lombok.Generated;
import net.minecraft.class_2596;
import net.minecraft.class_2828;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Disabler", category = Category.OTHER)
public class DisablerModule extends Module
{
    private static final DisablerModule instance;
    private final ModeSetting mode;
    
    public DisablerModule() {
        this.mode = new ModeSetting("\u0420\u0435\u0436\u0438\u043c").values("Rotation").value("Rotation");
        this.addSettings(this.mode);
    }
    
    @Override
    public void onEnable() {
        if (DisablerModule.mc.field_1724 == null) {
            this.disable();
            return;
        }
        final String s = this.mode.getValue();
        switch (s) {
            case "Rotation": {
                this.executeRotationDisabler();
                break;
            }
        }
        this.disable();
    }
    
    private void executeRotationDisabler() {
        final double x = DisablerModule.mc.field_1724.method_23317();
        final double y = DisablerModule.mc.field_1724.method_23318();
        final double z = DisablerModule.mc.field_1724.method_23321();
        final boolean onGround = DisablerModule.mc.field_1724.method_24828();
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
        return DisablerModule.instance;
    }
    
    static {
        instance = new DisablerModule();
    }
}
