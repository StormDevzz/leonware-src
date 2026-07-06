package sweetie.leonware.client.features.modules.combat;

import java.util.Iterator;
import lombok.Generated;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_238;
import net.minecraft.class_265;
import net.minecraft.class_2824;
import net.minecraft.class_2828;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.SliderSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/MaceDamageModule.class */
@ModuleRegister(name = "Mace Damage", category = Category.COMBAT)
public class MaceDamageModule extends Module {
    private static final MaceDamageModule instance = new MaceDamageModule();
    private final SliderSetting fallHeight = new SliderSetting("FallHeight").value(Float.valueOf(22.0f)).range(1.0f, 170.0f).step(1.0f);

    @Generated
    public static MaceDamageModule getInstance() {
        return instance;
    }

    public MaceDamageModule() {
        addSettings(this.fallHeight);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener(event -> {
            if (event.isSend() && (event.packet() instanceof class_2824) && mc.field_1724 != null && mc.field_1687 != null) {
                class_1799 mainHand = mc.field_1724.method_6047();
                if (mainHand.method_7909() != class_1802.field_49814 || mc.field_1724.field_5976) {
                    return;
                }
                int height = determineHeight();
                double x = mc.field_1724.method_23317();
                double y = mc.field_1724.method_23318();
                double z = mc.field_1724.method_23321();
                if (height > 10) {
                    int times = (int) Math.ceil(Math.abs(((double) height) / 10.0d));
                    for (int i = 0; i < times; i++) {
                        sendPacket((class_2828) new class_2828.class_2829(x, y, z, false, false));
                    }
                } else {
                    for (int i2 = 0; i2 < 2; i2++) {
                        sendPacket((class_2828) new class_2828.class_2829(x, y, z, mc.field_1724.method_24828(), false));
                    }
                }
                sendPacket((class_2828) new class_2828.class_2829(x, y + ((double) height), z, false, false));
                sendPacket((class_2828) new class_2828.class_2829(x, y, z, false, false));
            }
        }));
        addEvents(packetEvent);
    }

    private int determineHeight() {
        class_238 boundingBox = mc.field_1724.method_5829();
        for (int i = (int) this.fallHeight.getValue().floatValue(); i >= 1; i--) {
            class_238 newBB = boundingBox.method_989(0.0d, i, 0.0d);
            boolean noCollision = true;
            Iterator it = mc.field_1687.method_20812(mc.field_1724, newBB).iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                class_265 shape = (class_265) it.next();
                if (!shape.method_1110()) {
                    noCollision = false;
                    break;
                }
            }
            if (noCollision) {
                return i;
            }
        }
        return 0;
    }

    private void sendPacket(class_2828 packet) {
        if (mc.method_1562() != null) {
            mc.method_1562().method_52787(packet);
        }
    }
}
