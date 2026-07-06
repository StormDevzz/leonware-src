package sweetie.leonware.api.event;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/event/EventListener.class */
public final class EventListener extends Record {
    private final Runnable action;

    public EventListener(Runnable action) {
        this.action = action;
    }

    @Override // java.lang.Record
    public final String toString() {
        return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, EventListener.class), EventListener.class, "action", "FIELD:Lsweetie/leonware/api/event/EventListener;->action:Ljava/lang/Runnable;").dynamicInvoker().invoke(this) /* invoke-custom */;
    }

    @Override // java.lang.Record
    public final int hashCode() {
        return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, EventListener.class), EventListener.class, "action", "FIELD:Lsweetie/leonware/api/event/EventListener;->action:Ljava/lang/Runnable;").dynamicInvoker().invoke(this) /* invoke-custom */;
    }

    @Override // java.lang.Record
    public final boolean equals(Object o) {
        return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, EventListener.class, Object.class), EventListener.class, "action", "FIELD:Lsweetie/leonware/api/event/EventListener;->action:Ljava/lang/Runnable;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
    }

    public Runnable action() {
        return this.action;
    }

    public void unsubscribe() {
        this.action.run();
    }
}
