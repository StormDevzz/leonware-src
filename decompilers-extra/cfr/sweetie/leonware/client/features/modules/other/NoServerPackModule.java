/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_2596
 *  net.minecraft.class_2720
 *  net.minecraft.class_2856
 *  net.minecraft.class_2856$class_2857
 */
package sweetie.leonware.client.features.modules.other;

import lombok.Generated;
import net.minecraft.class_2596;
import net.minecraft.class_2720;
import net.minecraft.class_2856;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;

@ModuleRegister(name="No Server Pack", category=Category.OTHER)
public class NoServerPackModule
extends Module {
    private static final NoServerPackModule instance = new NoServerPackModule();

    @Override
    public void onEvent() {
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(event -> {
            class_2596<?> patt0$temp;
            if (event.isReceive() && (patt0$temp = event.packet()) instanceof class_2720) {
                class_2720 packet = (class_2720)patt0$temp;
                this.sendPacket((class_2596<?>)new class_2856(packet.comp_2158(), class_2856.class_2857.field_13016));
                this.sendPacket((class_2596<?>)new class_2856(packet.comp_2158(), class_2856.class_2857.field_13017));
                PacketEvent.getInstance().setCancel(true);
            }
        }));
        this.addEvents(packetEvent);
    }

    @Generated
    public static NoServerPackModule getInstance() {
        return instance;
    }
}

