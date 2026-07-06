package sweetie.leonware.api.event.events.render;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import lombok.Generated;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import sweetie.leonware.api.event.events.Event;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/event/events/render/Render2DEvent.class */
public class Render2DEvent extends Event<Render2DEventData> {
    private static final Render2DEvent instance = new Render2DEvent();

    @Generated
    public static Render2DEvent getInstance() {
        return instance;
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/event/events/render/Render2DEvent$Render2DEventData.class */
    public static final class Render2DEventData extends Record {
        private final class_332 context;
        private final class_4587 matrixStack;
        private final float partialTicks;

        public Render2DEventData(class_332 context, class_4587 matrixStack, float partialTicks) {
            this.context = context;
            this.matrixStack = matrixStack;
            this.partialTicks = partialTicks;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Render2DEventData.class), Render2DEventData.class, "context;matrixStack;partialTicks", "FIELD:Lsweetie/leonware/api/event/events/render/Render2DEvent$Render2DEventData;->context:Lnet/minecraft/class_332;", "FIELD:Lsweetie/leonware/api/event/events/render/Render2DEvent$Render2DEventData;->matrixStack:Lnet/minecraft/class_4587;", "FIELD:Lsweetie/leonware/api/event/events/render/Render2DEvent$Render2DEventData;->partialTicks:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Render2DEventData.class), Render2DEventData.class, "context;matrixStack;partialTicks", "FIELD:Lsweetie/leonware/api/event/events/render/Render2DEvent$Render2DEventData;->context:Lnet/minecraft/class_332;", "FIELD:Lsweetie/leonware/api/event/events/render/Render2DEvent$Render2DEventData;->matrixStack:Lnet/minecraft/class_4587;", "FIELD:Lsweetie/leonware/api/event/events/render/Render2DEvent$Render2DEventData;->partialTicks:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Render2DEventData.class, Object.class), Render2DEventData.class, "context;matrixStack;partialTicks", "FIELD:Lsweetie/leonware/api/event/events/render/Render2DEvent$Render2DEventData;->context:Lnet/minecraft/class_332;", "FIELD:Lsweetie/leonware/api/event/events/render/Render2DEvent$Render2DEventData;->matrixStack:Lnet/minecraft/class_4587;", "FIELD:Lsweetie/leonware/api/event/events/render/Render2DEvent$Render2DEventData;->partialTicks:F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public class_332 context() {
            return this.context;
        }

        public class_4587 matrixStack() {
            return this.matrixStack;
        }

        public float partialTicks() {
            return this.partialTicks;
        }
    }
}
