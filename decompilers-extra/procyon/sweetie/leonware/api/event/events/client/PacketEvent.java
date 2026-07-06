// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.event.events.client;

import net.minecraft.class_2596;
import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class PacketEvent extends Event<PacketEventData>
{
    private static final PacketEvent instance;
    
    @Generated
    public static PacketEvent getInstance() {
        return PacketEvent.instance;
    }
    
    static {
        instance = new PacketEvent();
    }
    
    record PacketEventData(class_2596<?> packet, PacketType packetType) {
        public boolean isReceive() {
            return this.packetType == PacketType.RECEIVE;
        }
        
        public boolean isSend() {
            return this.packetType == PacketType.SEND;
        }
        
        public enum PacketType
        {
            SEND, 
            RECEIVE;
        }
    }
}
