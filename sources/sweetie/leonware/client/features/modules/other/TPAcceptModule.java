package sweetie.leonware.client.features.modules.other;

import lombok.Generated;
import net.minecraft.class_7439;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.system.configs.FriendManager;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/other/TPAcceptModule.class */
@ModuleRegister(name = "TP Accept", category = Category.OTHER)
public class TPAcceptModule extends Module {
    private static final TPAcceptModule instance = new TPAcceptModule();
    private final BooleanSetting friendsOnly = new BooleanSetting("Принимать друзей").value((Boolean) false);
    private final ModeSetting commandMode = new ModeSetting("Команда").value("tpyes").values("tpaccept", "tpyes");

    @Generated
    public static TPAcceptModule getInstance() {
        return instance;
    }

    @Generated
    public ModeSetting getCommandMode() {
        return this.commandMode;
    }

    public TPAcceptModule() {
        addSettings(this.friendsOnly, this.commandMode);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener(event -> {
            if (event.isReceive()) {
                class_7439 class_7439VarPacket = event.packet();
                if (class_7439VarPacket instanceof class_7439) {
                    class_7439 packet = class_7439VarPacket;
                    String message = packet.comp_763().getString();
                    String msgLower = message.toLowerCase();
                    boolean isTpRequest = msgLower.contains("телепортироваться") || msgLower.contains("телепортацию");
                    if (isTpRequest) {
                        String command = this.commandMode.getValue();
                        if (this.friendsOnly.getValue().booleanValue()) {
                            for (String name : FriendManager.getInstance().getData()) {
                                if (msgLower.contains(name.toLowerCase())) {
                                    mc.field_1724.field_3944.method_45730(command + " " + name);
                                    return;
                                }
                            }
                            return;
                        }
                        mc.field_1724.field_3944.method_45730(command);
                    }
                }
            }
        }));
        addEvents(packetEvent);
    }
}
