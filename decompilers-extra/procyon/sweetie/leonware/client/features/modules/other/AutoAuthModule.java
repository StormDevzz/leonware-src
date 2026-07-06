// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.other;

import lombok.Generated;
import net.minecraft.class_2596;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.utils.other.TextUtil;
import net.minecraft.class_7439;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Auto Authorisation", category = Category.OTHER)
public class AutoAuthModule extends Module
{
    private static final AutoAuthModule instance;
    private final String password = "123jscodeth321";
    
    @Override
    public void onEvent() {
        final EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(data -> {
            if (!data.isReceive()) {
                return;
            }
            else {
                final class_2596<?> packet = data.packet();
                if (packet instanceof final class_7439 chatPacket) {
                    if (AutoAuthModule.mc.method_1562() != null) {
                        final String content = chatPacket.comp_763().getString().toLowerCase();
                        if (content.contains("\u0437\u0430\u0440\u0435\u0433\u0438\u0441\u0442\u0440\u0438\u0440\u0443\u0439\u0442\u0435\u0441\u044c") || content.contains("/register") || content.contains("reg ")) {
                            AutoAuthModule.mc.method_1562().method_45730("register %s %s".formatted("123jscodeth321", "123jscodeth321"));
                            TextUtil.sendMessage("\u0412\u044b \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u0437\u0430\u0440\u0435\u0433\u0438\u0441\u0442\u0440\u0438\u0440\u043e\u0432\u0430\u043d\u044b \u0441 \u043f\u0430\u0440\u043e\u043b\u0435\u043c §a123jscodeth321");
                        }
                        if (content.contains("/login") || content.contains("\u0430\u0432\u0442\u043e\u0440\u0438\u0437\u0443\u0439\u0442\u0435\u0441\u044c")) {
                            AutoAuthModule.mc.method_1562().method_45730("login %s".formatted("123jscodeth321"));
                            TextUtil.sendMessage("\u0412\u044b \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u0430\u0432\u0442\u043e\u0440\u0438\u0437\u043e\u0432\u0430\u043d\u044b \u0441 \u043f\u0430\u0440\u043e\u043b\u0435\u043c §a123jscodeth321");
                        }
                    }
                }
                return;
            }
        }));
        this.addEvents(packetEvent);
    }
    
    @Generated
    public static AutoAuthModule getInstance() {
        return AutoAuthModule.instance;
    }
    
    static {
        instance = new AutoAuthModule();
    }
}
