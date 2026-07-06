package sweetie.leonware.api.event.events.player.world;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/event/events/player/world/PushEvent.class */
public class PushEvent extends Event<PushEventData> {
    private static final PushEvent instance = new PushEvent();

    @Generated
    public static PushEvent getInstance() {
        return instance;
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/event/events/player/world/PushEvent$PushEventData.class */
    public static final class PushEventData extends Record {
        private final PushingSource source;

        /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/event/events/player/world/PushEvent$PushEventData$PushingSource.class */
        public enum PushingSource {
            BLOCK,
            WATER,
            ENTITY
        }

        public PushEventData(PushingSource source) {
            this.source = source;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, PushEventData.class), PushEventData.class, "source", "FIELD:Lsweetie/leonware/api/event/events/player/world/PushEvent$PushEventData;->source:Lsweetie/leonware/api/event/events/player/world/PushEvent$PushEventData$PushingSource;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, PushEventData.class), PushEventData.class, "source", "FIELD:Lsweetie/leonware/api/event/events/player/world/PushEvent$PushEventData;->source:Lsweetie/leonware/api/event/events/player/world/PushEvent$PushEventData$PushingSource;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, PushEventData.class, Object.class), PushEventData.class, "source", "FIELD:Lsweetie/leonware/api/event/events/player/world/PushEvent$PushEventData;->source:Lsweetie/leonware/api/event/events/player/world/PushEvent$PushEventData$PushingSource;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public PushingSource source() {
            return this.source;
        }
    }
}
