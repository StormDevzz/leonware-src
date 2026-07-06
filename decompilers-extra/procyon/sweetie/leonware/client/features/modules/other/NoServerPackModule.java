// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.other;

import lombok.Generated;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import net.minecraft.class_2596;
import net.minecraft.class_2856;
import net.minecraft.class_2720;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "No Server Pack", category = Category.OTHER)
public class NoServerPackModule extends Module
{
    private static final NoServerPackModule instance;
    
    @Override
    public void onEvent() {
        final EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(event -> {
            if (event.isReceive()) {
                final class_2596 patt0$temp = event.packet();
                if (patt0$temp instanceof final class_2720 packet) {
                    this.sendPacket((class_2596<?>)new class_2856(packet.comp_2158(), class_2856.class_2857.field_13016));
                    this.sendPacket((class_2596<?>)new class_2856(packet.comp_2158(), class_2856.class_2857.field_13017));
                    PacketEvent.getInstance().setCancel(true);
                }
            }
            return;
        }));
        this.addEvents(packetEvent);
    }
    
    @Generated
    public static NoServerPackModule getInstance() {
        return NoServerPackModule.instance;
    }
    
    static {
        instance = new NoServerPackModule();
    }
}
