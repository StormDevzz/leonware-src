package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import net.minecraft.class_1753;
import net.minecraft.class_1764;
import net.minecraft.class_1792;
import net.minecraft.class_1835;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2846;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.SliderSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/FastBowModule.class */
@ModuleRegister(name = "Fast Bow", category = Category.COMBAT)
public class FastBowModule extends Module {
    private static final FastBowModule instance = new FastBowModule();
    private final SliderSetting minCharge = new SliderSetting("Мин. натяжение").value(Float.valueOf(3.0f)).range(1.0f, 20.0f).step(1.0f);

    @Generated
    public static FastBowModule getInstance() {
        return instance;
    }

    public FastBowModule() {
        addSettings(this.minCharge);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1724 == null || mc.method_1562() == null || !mc.field_1724.method_6115()) {
                return;
            }
            class_1792 item = mc.field_1724.method_6030().method_7909();
            if (((item instanceof class_1753) || (item instanceof class_1764) || (item instanceof class_1835)) && mc.field_1724.method_6048() >= this.minCharge.getValue().intValue()) {
                mc.method_1562().method_52787(new class_2846(class_2846.class_2847.field_12974, class_2338.field_10980, class_2350.field_11033));
                mc.field_1724.method_6075();
            }
        }));
        addEvents(updateEvent);
    }
}
