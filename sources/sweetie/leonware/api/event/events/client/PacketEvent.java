package sweetie.leonware.api.event.events.client;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import lombok.Generated;
import net.minecraft.class_2596;
import sweetie.leonware.api.event.events.Event;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/event/events/client/PacketEvent.class */
public class PacketEvent extends Event<PacketEventData> {
    private static final PacketEvent instance = new PacketEvent();

    @Generated
    public static PacketEvent getInstance() {
        return instance;
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/event/events/client/PacketEvent$PacketEventData.class */
    public static final class PacketEventData extends Record {
        private final class_2596<?> packet;
        private final PacketType packetType;

        /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/event/events/client/PacketEvent$PacketEventData$PacketType.class */
        public enum PacketType {
            SEND,
            RECEIVE
        }

        public PacketEventData(class_2596<?> packet, PacketType packetType) {
            this.packet = packet;
            this.packetType = packetType;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, PacketEventData.class), PacketEventData.class, "packet;packetType", "FIELD:Lsweetie/leonware/api/event/events/client/PacketEvent$PacketEventData;->packet:Lnet/minecraft/class_2596;", "FIELD:Lsweetie/leonware/api/event/events/client/PacketEvent$PacketEventData;->packetType:Lsweetie/leonware/api/event/events/client/PacketEvent$PacketEventData$PacketType;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, PacketEventData.class), PacketEventData.class, "packet;packetType", "FIELD:Lsweetie/leonware/api/event/events/client/PacketEvent$PacketEventData;->packet:Lnet/minecraft/class_2596;", "FIELD:Lsweetie/leonware/api/event/events/client/PacketEvent$PacketEventData;->packetType:Lsweetie/leonware/api/event/events/client/PacketEvent$PacketEventData$PacketType;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, PacketEventData.class, Object.class), PacketEventData.class, "packet;packetType", "FIELD:Lsweetie/leonware/api/event/events/client/PacketEvent$PacketEventData;->packet:Lnet/minecraft/class_2596;", "FIELD:Lsweetie/leonware/api/event/events/client/PacketEvent$PacketEventData;->packetType:Lsweetie/leonware/api/event/events/client/PacketEvent$PacketEventData$PacketType;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public class_2596<?> packet() {
            return this.packet;
        }

        public PacketType packetType() {
            return this.packetType;
        }

        public boolean isReceive() {
            return this.packetType == PacketType.RECEIVE;
        }

        public boolean isSend() {
            return this.packetType == PacketType.SEND;
        }
    }
}
