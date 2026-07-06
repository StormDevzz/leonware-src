/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1268
 *  net.minecraft.class_1707
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_2596
 *  net.minecraft.class_437
 *  net.minecraft.class_476
 *  net.minecraft.class_7439
 */
package sweetie.leonware.client.features.modules.other;

import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1707;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2596;
import net.minecraft.class_437;
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

@ModuleRegister(name="Joiner", category=Category.OTHER)
public class JoinerModule
extends Module {
    private static final JoinerModule instance = new JoinerModule();
    private boolean compassClick = false;
    private long last;
    private boolean restart;

    @Override
    public void toggle() {
        super.toggle();
        this.compassClick = false;
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            class_437 patt0$temp;
            if ((!this.compassClick || this.restart) && JoinerModule.mc.field_1755 == null) {
                int compassSlot = InventoryUtil.findItem(class_1802.field_8251, true);
                if (compassSlot == -1) {
                    return;
                }
                InventoryUtil.swapToSlot(compassSlot);
                InventoryUtil.useItem(class_1268.field_5808);
                this.compassClick = true;
            }
            if (this.compassClick && (patt0$temp = JoinerModule.mc.field_1755) instanceof class_476) {
                class_476 screen = (class_476)patt0$temp;
                for (int i = 0; i < ((class_1707)screen.method_17577()).field_7761.size(); ++i) {
                    class_1799 stack = ((class_1707)screen.method_17577()).method_7611(i).method_7677();
                    if (!stack.method_7964().getString().contains("\u0414\u0443\u044d\u043b\u0438")) continue;
                    InventoryUtil.swapSlots(i, 4);
                    this.compassClick = false;
                    this.restart = false;
                    break;
                }
            }
        }));
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(event -> {
            class_7439 packet;
            String message;
            if (!event.isReceive()) {
                return;
            }
            class_2596<?> patt0$temp = event.packet();
            if (patt0$temp instanceof class_7439 && ((message = (packet = (class_7439)patt0$temp).comp_763().getString()).contains("\u0412\u044b \u0443\u0436\u0435 \u043f\u043e\u0434\u043a\u043b\u044e\u0447\u0435\u043d\u044b \u043d\u0430 \u044d\u0442\u043e\u0442 \u0441\u0435\u0440\u0432\u0435\u0440") || message.contains("\u0421\u0435\u0440\u0432\u0435\u0440 \u0437\u0430\u043f\u043e\u043b\u043d\u0435\u043d") || message.contains("\u0412\u044b \u0431\u044b\u043b\u0438 \u043a\u0438\u043a\u043d\u0443\u0442\u044b \u0441 \u0441\u0435\u0440\u0432\u0435\u0440\u0430 1duels"))) {
                this.compassClick = false;
                this.restart = true;
                PacketEvent.getInstance().setCancel(true);
            }
        }));
        this.addEvents(updateEvent, packetEvent);
    }

    @Generated
    public static JoinerModule getInstance() {
        return instance;
    }
}

