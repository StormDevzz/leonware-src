// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import net.minecraft.class_304;
import net.minecraft.class_1657;
import net.minecraft.class_1268;
import net.minecraft.class_1802;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Auto GApple", category = Category.COMBAT)
public class AutoGAppleModule extends Module
{
    private static final AutoGAppleModule instance;
    private final SliderSetting health;
    private final BooleanSetting useEnchanted;
    private boolean active;
    
    public AutoGAppleModule() {
        this.health = new SliderSetting("Health").value(18.0f).range(4.0f, 20.0f).step(1.0f);
        this.useEnchanted = new BooleanSetting("Use enchanted").value(true);
        this.addSettings(this.health, this.useEnchanted);
    }
    
    @Override
    public void onEvent() {
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            final boolean validItem = AutoGAppleModule.mc.field_1724.method_6079().method_7909() == class_1802.field_8463 || (this.useEnchanted.getValue() && AutoGAppleModule.mc.field_1724.method_6079().method_7909() == class_1802.field_8367);
            if (validItem && AutoGAppleModule.mc.field_1724.method_6032() <= this.health.getValue()) {
                this.active = true;
                if (!AutoGAppleModule.mc.field_1724.method_6115()) {
                    AutoGAppleModule.mc.field_1761.method_2919((class_1657)AutoGAppleModule.mc.field_1724, class_1268.field_5810);
                    class_304.method_1416(AutoGAppleModule.mc.field_1690.field_1904.method_1429(), true);
                    AutoGAppleModule.mc.field_1724.method_6019(class_1268.field_5810);
                }
            }
            else if (this.active && AutoGAppleModule.mc.field_1724.method_6115()) {
                AutoGAppleModule.mc.field_1761.method_2897((class_1657)AutoGAppleModule.mc.field_1724);
                if (!AutoGAppleModule.mc.field_1729.method_1609() || AutoGAppleModule.mc.field_1755 != null) {
                    class_304.method_1416(AutoGAppleModule.mc.field_1690.field_1904.method_1429(), false);
                }
                this.active = false;
            }
            return;
        }));
        this.addEvents(updateEvent);
    }
    
    @Generated
    public static AutoGAppleModule getInstance() {
        return AutoGAppleModule.instance;
    }
    
    static {
        instance = new AutoGAppleModule();
    }
}
