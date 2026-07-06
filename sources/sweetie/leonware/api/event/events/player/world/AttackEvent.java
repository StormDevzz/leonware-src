package sweetie.leonware.api.event.events.player.world;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import lombok.Generated;
import net.minecraft.class_1297;
import sweetie.leonware.api.event.events.Event;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/event/events/player/world/AttackEvent.class */
public class AttackEvent extends Event<AttackEventData> {
    private static final AttackEvent instance = new AttackEvent();

    @Generated
    public static AttackEvent getInstance() {
        return instance;
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/event/events/player/world/AttackEvent$AttackEventData.class */
    public static final class AttackEventData extends Record {
        private final class_1297 entity;

        public AttackEventData(class_1297 entity) {
            this.entity = entity;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, AttackEventData.class), AttackEventData.class, "entity", "FIELD:Lsweetie/leonware/api/event/events/player/world/AttackEvent$AttackEventData;->entity:Lnet/minecraft/class_1297;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, AttackEventData.class), AttackEventData.class, "entity", "FIELD:Lsweetie/leonware/api/event/events/player/world/AttackEvent$AttackEventData;->entity:Lnet/minecraft/class_1297;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, AttackEventData.class, Object.class), AttackEventData.class, "entity", "FIELD:Lsweetie/leonware/api/event/events/player/world/AttackEvent$AttackEventData;->entity:Lnet/minecraft/class_1297;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public class_1297 entity() {
            return this.entity;
        }
    }
}
