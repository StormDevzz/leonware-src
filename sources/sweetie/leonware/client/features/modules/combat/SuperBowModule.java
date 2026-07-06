package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import net.minecraft.class_2828;
import net.minecraft.class_2846;
import net.minecraft.class_2848;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.SliderSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/SuperBowModule.class */
@ModuleRegister(name = "Super Bow", category = Category.COMBAT)
public class SuperBowModule extends Module {
    private static final SuperBowModule instance = new SuperBowModule();
    private final SliderSetting power = new SliderSetting("Power").value(Float.valueOf(30.0f)).range(1.0f, 200.0f).step(1.0f);

    @Generated
    public static SuperBowModule getInstance() {
        return instance;
    }

    public SuperBowModule() {
        addSettings(this.power);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener(data -> {
            if (!data.isSend() || mc.field_1724 == null || mc.method_1562() == null) {
                return;
            }
            class_2846 class_2846VarPacket = data.packet();
            if (class_2846VarPacket instanceof class_2846) {
                class_2846 p = class_2846VarPacket;
                if (p.method_12363() == class_2846.class_2847.field_12974) {
                    mc.method_1562().method_52787(new class_2848(mc.field_1724, class_2848.class_2849.field_12981));
                    for (int i = 0; i < this.power.getValue().intValue(); i++) {
                        mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() - 1.0E-9d, mc.field_1724.method_23321(), true, true));
                        mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.0E-9d, mc.field_1724.method_23321(), false, false));
                    }
                }
            }
        }));
        addEvents(packetEvent);
    }
}
