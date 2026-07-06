package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1802;
import net.minecraft.class_304;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/AutoGAppleModule.class */
@ModuleRegister(name = "Auto GApple", category = Category.COMBAT)
public class AutoGAppleModule extends Module {
    private static final AutoGAppleModule instance = new AutoGAppleModule();
    private final SliderSetting health = new SliderSetting("Health").value(Float.valueOf(18.0f)).range(4.0f, 20.0f).step(1.0f);
    private final BooleanSetting useEnchanted = new BooleanSetting("Use enchanted").value((Boolean) true);
    private boolean active;

    @Generated
    public static AutoGAppleModule getInstance() {
        return instance;
    }

    public AutoGAppleModule() {
        addSettings(this.health, this.useEnchanted);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            boolean validItem = mc.field_1724.method_6079().method_7909() == class_1802.field_8463 || (this.useEnchanted.getValue().booleanValue() && mc.field_1724.method_6079().method_7909() == class_1802.field_8367);
            if (validItem && mc.field_1724.method_6032() <= this.health.getValue().floatValue()) {
                this.active = true;
                if (!mc.field_1724.method_6115()) {
                    mc.field_1761.method_2919(mc.field_1724, class_1268.field_5810);
                    class_304.method_1416(mc.field_1690.field_1904.method_1429(), true);
                    mc.field_1724.method_6019(class_1268.field_5810);
                    return;
                }
                return;
            }
            if (this.active && mc.field_1724.method_6115()) {
                mc.field_1761.method_2897(mc.field_1724);
                if (!mc.field_1729.method_1609() || mc.field_1755 != null) {
                    class_304.method_1416(mc.field_1690.field_1904.method_1429(), false);
                }
                this.active = false;
            }
        }));
        addEvents(updateEvent);
    }
}
