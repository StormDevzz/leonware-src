/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_2596
 *  net.minecraft.class_7439
 */
package sweetie.leonware.client.features.modules.other;

import lombok.Generated;
import net.minecraft.class_2596;
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

@ModuleRegister(name="TP Accept", category=Category.OTHER)
public class TPAcceptModule
extends Module {
    private static final TPAcceptModule instance = new TPAcceptModule();
    private final BooleanSetting friendsOnly = new BooleanSetting("\u041f\u0440\u0438\u043d\u0438\u043c\u0430\u0442\u044c \u0434\u0440\u0443\u0437\u0435\u0439").value(false);
    private final ModeSetting commandMode = new ModeSetting("\u041a\u043e\u043c\u0430\u043d\u0434\u0430").value("tpyes").values("tpaccept", "tpyes");

    public TPAcceptModule() {
        this.addSettings(this.friendsOnly, this.commandMode);
    }

    @Override
    public void onEvent() {
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(event -> {
            class_2596<?> patt0$temp;
            if (event.isReceive() && (patt0$temp = event.packet()) instanceof class_7439) {
                boolean isTpRequest;
                class_7439 packet = (class_7439)patt0$temp;
                String message = packet.comp_763().getString();
                String msgLower = message.toLowerCase();
                boolean bl = isTpRequest = msgLower.contains("\u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c\u0441\u044f") || msgLower.contains("\u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0430\u0446\u0438\u044e");
                if (!isTpRequest) {
                    return;
                }
                String command = (String)this.commandMode.getValue();
                if (((Boolean)this.friendsOnly.getValue()).booleanValue()) {
                    for (String name : FriendManager.getInstance().getData()) {
                        if (!msgLower.contains(name.toLowerCase())) continue;
                        TPAcceptModule.mc.field_1724.field_3944.method_45730(command + " " + name);
                        break;
                    }
                } else {
                    TPAcceptModule.mc.field_1724.field_3944.method_45730(command);
                }
            }
        }));
        this.addEvents(packetEvent);
    }

    @Generated
    public static TPAcceptModule getInstance() {
        return instance;
    }

    @Generated
    public ModeSetting getCommandMode() {
        return this.commandMode;
    }
}

