package sweetie.leonware.api.event.events.player.world;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import lombok.Generated;
import net.minecraft.class_1713;
import sweetie.leonware.api.event.events.Event;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/event/events/player/world/ClickSlotEvent.class */
public class ClickSlotEvent extends Event<ClickSlotEventData> {
    private static final ClickSlotEvent instance = new ClickSlotEvent();

    @Generated
    public static ClickSlotEvent getInstance() {
        return instance;
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/event/events/player/world/ClickSlotEvent$ClickSlotEventData.class */
    public static final class ClickSlotEventData extends Record {
        private final class_1713 slotActionType;
        private final int slot;
        private final int button;
        private final int id;

        public ClickSlotEventData(class_1713 slotActionType, int slot, int button, int id) {
            this.slotActionType = slotActionType;
            this.slot = slot;
            this.button = button;
            this.id = id;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, ClickSlotEventData.class), ClickSlotEventData.class, "slotActionType;slot;button;id", "FIELD:Lsweetie/leonware/api/event/events/player/world/ClickSlotEvent$ClickSlotEventData;->slotActionType:Lnet/minecraft/class_1713;", "FIELD:Lsweetie/leonware/api/event/events/player/world/ClickSlotEvent$ClickSlotEventData;->slot:I", "FIELD:Lsweetie/leonware/api/event/events/player/world/ClickSlotEvent$ClickSlotEventData;->button:I", "FIELD:Lsweetie/leonware/api/event/events/player/world/ClickSlotEvent$ClickSlotEventData;->id:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, ClickSlotEventData.class), ClickSlotEventData.class, "slotActionType;slot;button;id", "FIELD:Lsweetie/leonware/api/event/events/player/world/ClickSlotEvent$ClickSlotEventData;->slotActionType:Lnet/minecraft/class_1713;", "FIELD:Lsweetie/leonware/api/event/events/player/world/ClickSlotEvent$ClickSlotEventData;->slot:I", "FIELD:Lsweetie/leonware/api/event/events/player/world/ClickSlotEvent$ClickSlotEventData;->button:I", "FIELD:Lsweetie/leonware/api/event/events/player/world/ClickSlotEvent$ClickSlotEventData;->id:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, ClickSlotEventData.class, Object.class), ClickSlotEventData.class, "slotActionType;slot;button;id", "FIELD:Lsweetie/leonware/api/event/events/player/world/ClickSlotEvent$ClickSlotEventData;->slotActionType:Lnet/minecraft/class_1713;", "FIELD:Lsweetie/leonware/api/event/events/player/world/ClickSlotEvent$ClickSlotEventData;->slot:I", "FIELD:Lsweetie/leonware/api/event/events/player/world/ClickSlotEvent$ClickSlotEventData;->button:I", "FIELD:Lsweetie/leonware/api/event/events/player/world/ClickSlotEvent$ClickSlotEventData;->id:I").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public class_1713 slotActionType() {
            return this.slotActionType;
        }

        public int slot() {
            return this.slot;
        }

        public int button() {
            return this.button;
        }

        public int id() {
            return this.id;
        }
    }
}
