// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.other;

import lombok.Generated;
import net.minecraft.class_2596;
import net.minecraft.class_1799;
import net.minecraft.class_437;
import sweetie.leonware.api.event.EventListener;
import net.minecraft.class_7439;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.Listener;
import net.minecraft.class_1707;
import net.minecraft.class_476;
import net.minecraft.class_1268;
import sweetie.leonware.api.utils.player.InventoryUtil;
import net.minecraft.class_1802;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Joiner", category = Category.OTHER)
public class JoinerModule extends Module
{
    private static final JoinerModule instance;
    private boolean compassClick;
    private long last;
    private boolean restart;
    
    public JoinerModule() {
        this.compassClick = false;
    }
    
    @Override
    public void toggle() {
        super.toggle();
        this.compassClick = false;
    }
    
    @Override
    public void onEvent() {
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if ((!this.compassClick || this.restart) && JoinerModule.mc.field_1755 == null) {
                final int compassSlot = InventoryUtil.findItem(class_1802.field_8251, true);
                if (compassSlot == -1) {
                    return;
                }
                else {
                    InventoryUtil.swapToSlot(compassSlot);
                    InventoryUtil.useItem(class_1268.field_5808);
                    this.compassClick = true;
                }
            }
            if (this.compassClick) {
                final class_437 patt0$temp = JoinerModule.mc.field_1755;
                if (patt0$temp instanceof final class_476 screen) {
                    int i = 0;
                    while (i < ((class_1707)screen.method_17577()).field_7761.size()) {
                        final class_1799 stack = ((class_1707)screen.method_17577()).method_7611(i).method_7677();
                        if (stack.method_7964().getString().contains("\u0414\u0443\u044d\u043b\u0438")) {
                            InventoryUtil.swapSlots(i, 4);
                            this.compassClick = false;
                            this.restart = false;
                            break;
                        }
                        else {
                            ++i;
                        }
                    }
                }
            }
            return;
        }));
        final EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(event -> {
            if (!event.isReceive()) {
                return;
            }
            else {
                final class_2596 patt0$temp2 = event.packet();
                if (patt0$temp2 instanceof final class_7439 packet) {
                    final String message = packet.comp_763().getString();
                    if (message.contains("\u0412\u044b \u0443\u0436\u0435 \u043f\u043e\u0434\u043a\u043b\u044e\u0447\u0435\u043d\u044b \u043d\u0430 \u044d\u0442\u043e\u0442 \u0441\u0435\u0440\u0432\u0435\u0440") || message.contains("\u0421\u0435\u0440\u0432\u0435\u0440 \u0437\u0430\u043f\u043e\u043b\u043d\u0435\u043d") || message.contains("\u0412\u044b \u0431\u044b\u043b\u0438 \u043a\u0438\u043a\u043d\u0443\u0442\u044b \u0441 \u0441\u0435\u0440\u0432\u0435\u0440\u0430 1duels")) {
                        this.compassClick = false;
                        this.restart = true;
                        PacketEvent.getInstance().setCancel(true);
                    }
                }
                return;
            }
        }));
        this.addEvents(updateEvent, packetEvent);
    }
    
    @Generated
    public static JoinerModule getInstance() {
        return JoinerModule.instance;
    }
    
    static {
        instance = new JoinerModule();
    }
}
