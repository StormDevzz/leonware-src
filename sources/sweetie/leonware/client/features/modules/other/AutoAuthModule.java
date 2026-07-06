package sweetie.leonware.client.features.modules.other;

import lombok.Generated;
import net.minecraft.class_7439;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.utils.other.TextUtil;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/other/AutoAuthModule.class */
@ModuleRegister(name = "Auto Authorisation", category = Category.OTHER)
public class AutoAuthModule extends Module {
    private static final AutoAuthModule instance = new AutoAuthModule();
    private final String password = "123jscodeth321";

    @Generated
    public static AutoAuthModule getInstance() {
        return instance;
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener(data -> {
            if (data.isReceive()) {
                class_7439 class_7439VarPacket = data.packet();
                if (class_7439VarPacket instanceof class_7439) {
                    class_7439 chatPacket = class_7439VarPacket;
                    if (mc.method_1562() == null) {
                        return;
                    }
                    String content = chatPacket.comp_763().getString().toLowerCase();
                    if (content.contains("зарегистрируйтесь") || content.contains("/register") || content.contains("reg ")) {
                        mc.method_1562().method_45730("register %s %s".formatted("123jscodeth321", "123jscodeth321"));
                        TextUtil.sendMessage("Вы успешно зарегистрированы с паролем §a123jscodeth321");
                    }
                    if (content.contains("/login") || content.contains("авторизуйтесь")) {
                        mc.method_1562().method_45730("login %s".formatted("123jscodeth321"));
                        TextUtil.sendMessage("Вы успешно авторизованы с паролем §a123jscodeth321");
                    }
                }
            }
        }));
        addEvents(packetEvent);
    }
}
