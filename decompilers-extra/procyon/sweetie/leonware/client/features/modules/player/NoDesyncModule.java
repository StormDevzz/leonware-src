// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import net.minecraft.class_2708;
import net.minecraft.class_2596;
import sweetie.leonware.api.utils.player.InventoryUtil;
import net.minecraft.class_2735;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "No Desync", category = Category.PLAYER)
public class NoDesyncModule extends Module
{
    private static final NoDesyncModule instance;
    private final BooleanSetting noRotate;
    
    public NoDesyncModule() {
        this.noRotate = new BooleanSetting("No rotate").value(true);
        this.addSettings(this.noRotate);
    }
    
    @Override
    public void onEvent() {
        final EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(event -> {
            this.handleItemSwapFix(event);
            this.handleNoRotate(event);
            return;
        }));
        this.addEvents(packetEvent);
    }
    
    private void handleItemSwapFix(final PacketEvent.PacketEventData event) {
        final class_2596<?> packet2 = event.packet();
        if (packet2 instanceof final class_2735 packet) {
            if (event.isReceive()) {
                PacketEvent.getInstance().setCancel(true);
                InventoryUtil.swapToSlot(NoDesyncModule.mc.field_1724.method_31548().field_7545);
            }
        }
    }
    
    private void handleNoRotate(final PacketEvent.PacketEventData event) {
        if (this.noRotate.getValue()) {
            final class_2596<?> packet2 = event.packet();
            if (packet2 instanceof final class_2708 packet) {
                if (event.isReceive()) {
                    packet.comp_3228().comp_3151 = NoDesyncModule.mc.field_1724.method_36455();
                    packet.comp_3228().comp_3150 = NoDesyncModule.mc.field_1724.method_36454();
                }
            }
        }
    }
    
    @Generated
    public static NoDesyncModule getInstance() {
        return NoDesyncModule.instance;
    }
    
    static {
        instance = new NoDesyncModule();
    }
}
