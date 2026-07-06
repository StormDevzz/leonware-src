package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import net.minecraft.class_2708;
import net.minecraft.class_2735;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.utils.player.InventoryUtil;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/player/NoDesyncModule.class */
@ModuleRegister(name = "No Desync", category = Category.PLAYER)
public class NoDesyncModule extends Module {
    private static final NoDesyncModule instance = new NoDesyncModule();
    private final BooleanSetting noRotate = new BooleanSetting("No rotate").value((Boolean) true);

    @Generated
    public static NoDesyncModule getInstance() {
        return instance;
    }

    public NoDesyncModule() {
        addSettings(this.noRotate);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener(event -> {
            handleItemSwapFix(event);
            handleNoRotate(event);
        }));
        addEvents(packetEvent);
    }

    private void handleItemSwapFix(PacketEvent.PacketEventData event) {
        if ((event.packet() instanceof class_2735) && event.isReceive()) {
            PacketEvent.getInstance().setCancel(true);
            InventoryUtil.swapToSlot(mc.field_1724.method_31548().field_7545);
        }
    }

    private void handleNoRotate(PacketEvent.PacketEventData event) {
        if (this.noRotate.getValue().booleanValue()) {
            class_2708 class_2708VarPacket = event.packet();
            if (class_2708VarPacket instanceof class_2708) {
                class_2708 packet = class_2708VarPacket;
                if (event.isReceive()) {
                    packet.comp_3228().comp_3151 = mc.field_1724.method_36455();
                    packet.comp_3228().comp_3150 = mc.field_1724.method_36454();
                }
            }
        }
    }
}
