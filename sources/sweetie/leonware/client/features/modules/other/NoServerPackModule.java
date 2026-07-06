package sweetie.leonware.client.features.modules.other;

import lombok.Generated;
import net.minecraft.class_2596;
import net.minecraft.class_2720;
import net.minecraft.class_2856;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/other/NoServerPackModule.class */
@ModuleRegister(name = "No Server Pack", category = Category.OTHER)
public class NoServerPackModule extends Module {
    private static final NoServerPackModule instance = new NoServerPackModule();

    @Generated
    public static NoServerPackModule getInstance() {
        return instance;
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener(event -> {
            if (event.isReceive()) {
                class_2720 class_2720VarPacket = event.packet();
                if (class_2720VarPacket instanceof class_2720) {
                    class_2720 packet = class_2720VarPacket;
                    sendPacket((class_2596<?>) new class_2856(packet.comp_2158(), class_2856.class_2857.field_13016));
                    sendPacket((class_2596<?>) new class_2856(packet.comp_2158(), class_2856.class_2857.field_13017));
                    PacketEvent.getInstance().setCancel(true);
                }
            }
        }));
        addEvents(packetEvent);
    }
}
