// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.other;

import lombok.Generated;
import java.util.Iterator;
import net.minecraft.class_2596;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.system.configs.FriendManager;
import net.minecraft.class_7439;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "TP Accept", category = Category.OTHER)
public class TPAcceptModule extends Module
{
    private static final TPAcceptModule instance;
    private final BooleanSetting friendsOnly;
    private final ModeSetting commandMode;
    
    public TPAcceptModule() {
        this.friendsOnly = new BooleanSetting("\u041f\u0440\u0438\u043d\u0438\u043c\u0430\u0442\u044c \u0434\u0440\u0443\u0437\u0435\u0439").value(false);
        this.commandMode = new ModeSetting("\u041a\u043e\u043c\u0430\u043d\u0434\u0430").value("tpyes").values("tpaccept", "tpyes");
        this.addSettings(this.friendsOnly, this.commandMode);
    }
    
    @Override
    public void onEvent() {
        final EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(event -> {
            if (event.isReceive()) {
                final class_2596 patt0$temp = event.packet();
                if (patt0$temp instanceof final class_7439 packet) {
                    final String message = packet.comp_763().getString();
                    final String msgLower = message.toLowerCase();
                    final boolean isTpRequest = msgLower.contains("\u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c\u0441\u044f") || msgLower.contains("\u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0430\u0446\u0438\u044e");
                    if (!(!isTpRequest)) {
                        final String command = this.commandMode.getValue();
                        if (this.friendsOnly.getValue()) {
                            FriendManager.getInstance().getData().iterator();
                            final Iterator iterator;
                            while (iterator.hasNext()) {
                                final String name = iterator.next();
                                if (msgLower.contains(name.toLowerCase())) {
                                    TPAcceptModule.mc.field_1724.field_3944.method_45730(command + " " + name);
                                    break;
                                }
                            }
                        }
                        else {
                            TPAcceptModule.mc.field_1724.field_3944.method_45730(command);
                        }
                    }
                }
            }
            return;
        }));
        this.addEvents(packetEvent);
    }
    
    @Generated
    public static TPAcceptModule getInstance() {
        return TPAcceptModule.instance;
    }
    
    @Generated
    public ModeSetting getCommandMode() {
        return this.commandMode;
    }
    
    static {
        instance = new TPAcceptModule();
    }
}
