/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_2596
 */
package sweetie.leonware.api.event.events.client;

import lombok.Generated;
import net.minecraft.class_2596;
import sweetie.leonware.api.event.events.Event;

public class PacketEvent
extends Event<PacketEventData> {
    private static final PacketEvent instance = new PacketEvent();

    @Generated
    public static PacketEvent getInstance() {
        return instance;
    }

    public record PacketEventData(class_2596<?> packet, PacketType packetType) {
        public boolean isReceive() {
            return this.packetType == PacketType.RECEIVE;
        }

        public boolean isSend() {
            return this.packetType == PacketType.SEND;
        }

        public static enum PacketType {
            SEND,
            RECEIVE;

        }
    }
}

