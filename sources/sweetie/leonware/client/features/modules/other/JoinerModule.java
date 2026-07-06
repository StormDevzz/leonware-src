package sweetie.leonware.client.features.modules.other;

import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_476;
import net.minecraft.class_7439;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.utils.player.InventoryUtil;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/other/JoinerModule.class */
@ModuleRegister(name = "Joiner", category = Category.OTHER)
public class JoinerModule extends Module {
    private static final JoinerModule instance = new JoinerModule();
    private boolean compassClick = false;
    private long last;
    private boolean restart;

    @Generated
    public static JoinerModule getInstance() {
        return instance;
    }

    @Override // sweetie.leonware.api.module.Module
    public void toggle() {
        super.toggle();
        this.compassClick = false;
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            if ((!this.compassClick || this.restart) && mc.field_1755 == null) {
                int compassSlot = InventoryUtil.findItem(class_1802.field_8251, true);
                if (compassSlot == -1) {
                    return;
                }
                InventoryUtil.swapToSlot(compassSlot);
                InventoryUtil.useItem(class_1268.field_5808);
                this.compassClick = true;
            }
            if (this.compassClick) {
                class_476 class_476Var = mc.field_1755;
                if (class_476Var instanceof class_476) {
                    class_476 screen = class_476Var;
                    for (int i = 0; i < screen.method_17577().field_7761.size(); i++) {
                        class_1799 stack = screen.method_17577().method_7611(i).method_7677();
                        if (stack.method_7964().getString().contains("Дуэли")) {
                            InventoryUtil.swapSlots(i, 4);
                            this.compassClick = false;
                            this.restart = false;
                            return;
                        }
                    }
                }
            }
        }));
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener(event2 -> {
            if (event2.isReceive()) {
                class_7439 class_7439VarPacket = event2.packet();
                if (class_7439VarPacket instanceof class_7439) {
                    class_7439 packet = class_7439VarPacket;
                    String message = packet.comp_763().getString();
                    if (message.contains("Вы уже подключены на этот сервер") || message.contains("Сервер заполнен") || message.contains("Вы были кикнуты с сервера 1duels")) {
                        this.compassClick = false;
                        this.restart = true;
                        PacketEvent.getInstance().setCancel(true);
                    }
                }
            }
        }));
        addEvents(updateEvent, packetEvent);
    }
}
