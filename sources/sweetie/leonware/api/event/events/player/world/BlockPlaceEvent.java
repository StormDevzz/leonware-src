package sweetie.leonware.api.event.events.player.world;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import lombok.Generated;
import net.minecraft.class_1309;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import sweetie.leonware.api.event.events.Event;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/event/events/player/world/BlockPlaceEvent.class */
public class BlockPlaceEvent extends Event<BlockPlaceEventData> {
    private static final BlockPlaceEvent instance = new BlockPlaceEvent();

    @Generated
    public static BlockPlaceEvent getInstance() {
        return instance;
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/event/events/player/world/BlockPlaceEvent$BlockPlaceEventData.class */
    public static final class BlockPlaceEventData extends Record {
        private final class_2248 block;
        private final class_2680 state;
        private final class_2338 pos;
        private final class_1309 placer;

        public BlockPlaceEventData(class_2248 block, class_2680 state, class_2338 pos, class_1309 placer) {
            this.block = block;
            this.state = state;
            this.pos = pos;
            this.placer = placer;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, BlockPlaceEventData.class), BlockPlaceEventData.class, "block;state;pos;placer", "FIELD:Lsweetie/leonware/api/event/events/player/world/BlockPlaceEvent$BlockPlaceEventData;->block:Lnet/minecraft/class_2248;", "FIELD:Lsweetie/leonware/api/event/events/player/world/BlockPlaceEvent$BlockPlaceEventData;->state:Lnet/minecraft/class_2680;", "FIELD:Lsweetie/leonware/api/event/events/player/world/BlockPlaceEvent$BlockPlaceEventData;->pos:Lnet/minecraft/class_2338;", "FIELD:Lsweetie/leonware/api/event/events/player/world/BlockPlaceEvent$BlockPlaceEventData;->placer:Lnet/minecraft/class_1309;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, BlockPlaceEventData.class), BlockPlaceEventData.class, "block;state;pos;placer", "FIELD:Lsweetie/leonware/api/event/events/player/world/BlockPlaceEvent$BlockPlaceEventData;->block:Lnet/minecraft/class_2248;", "FIELD:Lsweetie/leonware/api/event/events/player/world/BlockPlaceEvent$BlockPlaceEventData;->state:Lnet/minecraft/class_2680;", "FIELD:Lsweetie/leonware/api/event/events/player/world/BlockPlaceEvent$BlockPlaceEventData;->pos:Lnet/minecraft/class_2338;", "FIELD:Lsweetie/leonware/api/event/events/player/world/BlockPlaceEvent$BlockPlaceEventData;->placer:Lnet/minecraft/class_1309;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, BlockPlaceEventData.class, Object.class), BlockPlaceEventData.class, "block;state;pos;placer", "FIELD:Lsweetie/leonware/api/event/events/player/world/BlockPlaceEvent$BlockPlaceEventData;->block:Lnet/minecraft/class_2248;", "FIELD:Lsweetie/leonware/api/event/events/player/world/BlockPlaceEvent$BlockPlaceEventData;->state:Lnet/minecraft/class_2680;", "FIELD:Lsweetie/leonware/api/event/events/player/world/BlockPlaceEvent$BlockPlaceEventData;->pos:Lnet/minecraft/class_2338;", "FIELD:Lsweetie/leonware/api/event/events/player/world/BlockPlaceEvent$BlockPlaceEventData;->placer:Lnet/minecraft/class_1309;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public class_2248 block() {
            return this.block;
        }

        public class_2680 state() {
            return this.state;
        }

        public class_2338 pos() {
            return this.pos;
        }

        public class_1309 placer() {
            return this.placer;
        }
    }
}
