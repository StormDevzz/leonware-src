/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_2596
 *  net.minecraft.class_2708
 *  net.minecraft.class_2735
 */
package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import net.minecraft.class_2596;
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

@ModuleRegister(name="No Desync", category=Category.PLAYER)
public class NoDesyncModule
extends Module {
    private static final NoDesyncModule instance = new NoDesyncModule();
    private final BooleanSetting noRotate = new BooleanSetting("No rotate").value(true);

    public NoDesyncModule() {
        this.addSettings(this.noRotate);
    }

    @Override
    public void onEvent() {
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(event -> {
            this.handleItemSwapFix((PacketEvent.PacketEventData)event);
            this.handleNoRotate((PacketEvent.PacketEventData)event);
        }));
        this.addEvents(packetEvent);
    }

    private void handleItemSwapFix(PacketEvent.PacketEventData event) {
        class_2596<?> class_25962 = event.packet();
        if (class_25962 instanceof class_2735) {
            class_2735 packet = (class_2735)class_25962;
            if (event.isReceive()) {
                PacketEvent.getInstance().setCancel(true);
                InventoryUtil.swapToSlot(NoDesyncModule.mc.field_1724.method_31548().field_7545);
            }
        }
    }

    private void handleNoRotate(PacketEvent.PacketEventData event) {
        class_2596<?> class_25962;
        if (((Boolean)this.noRotate.getValue()).booleanValue() && (class_25962 = event.packet()) instanceof class_2708) {
            class_2708 packet = (class_2708)class_25962;
            if (event.isReceive()) {
                packet.comp_3228().comp_3151 = NoDesyncModule.mc.field_1724.method_36455();
                packet.comp_3228().comp_3150 = NoDesyncModule.mc.field_1724.method_36454();
            }
        }
    }

    @Generated
    public static NoDesyncModule getInstance() {
        return instance;
    }
}

