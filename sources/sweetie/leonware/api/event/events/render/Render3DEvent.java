package sweetie.leonware.api.event.events.render;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import lombok.Generated;
import net.minecraft.class_4587;
import sweetie.leonware.api.event.events.Event;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/event/events/render/Render3DEvent.class */
public class Render3DEvent extends Event<Render3DEventData> {
    private static final Render3DEvent instance = new Render3DEvent();

    @Generated
    public static Render3DEvent getInstance() {
        return instance;
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/event/events/render/Render3DEvent$Render3DEventData.class */
    public static final class Render3DEventData extends Record {
        private final class_4587 matrixStack;
        private final float partialTicks;

        public Render3DEventData(class_4587 matrixStack, float partialTicks) {
            this.matrixStack = matrixStack;
            this.partialTicks = partialTicks;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Render3DEventData.class), Render3DEventData.class, "matrixStack;partialTicks", "FIELD:Lsweetie/leonware/api/event/events/render/Render3DEvent$Render3DEventData;->matrixStack:Lnet/minecraft/class_4587;", "FIELD:Lsweetie/leonware/api/event/events/render/Render3DEvent$Render3DEventData;->partialTicks:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Render3DEventData.class), Render3DEventData.class, "matrixStack;partialTicks", "FIELD:Lsweetie/leonware/api/event/events/render/Render3DEvent$Render3DEventData;->matrixStack:Lnet/minecraft/class_4587;", "FIELD:Lsweetie/leonware/api/event/events/render/Render3DEvent$Render3DEventData;->partialTicks:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Render3DEventData.class, Object.class), Render3DEventData.class, "matrixStack;partialTicks", "FIELD:Lsweetie/leonware/api/event/events/render/Render3DEvent$Render3DEventData;->matrixStack:Lnet/minecraft/class_4587;", "FIELD:Lsweetie/leonware/api/event/events/render/Render3DEvent$Render3DEventData;->partialTicks:F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public class_4587 matrixStack() {
            return this.matrixStack;
        }

        public float partialTicks() {
            return this.partialTicks;
        }
    }
}
