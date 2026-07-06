package sweetie.leonware.api.event.events.client;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/event/events/client/KeyEvent.class */
public class KeyEvent extends Event<KeyEventData> {
    private static final KeyEvent instance = new KeyEvent();

    @Generated
    public static KeyEvent getInstance() {
        return instance;
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/event/events/client/KeyEvent$KeyEventData.class */
    public static final class KeyEventData extends Record {
        private final int key;
        private final int action;
        private final boolean mouse;

        public KeyEventData(int key, int action, boolean mouse) {
            this.key = key;
            this.action = action;
            this.mouse = mouse;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, KeyEventData.class), KeyEventData.class, "key;action;mouse", "FIELD:Lsweetie/leonware/api/event/events/client/KeyEvent$KeyEventData;->key:I", "FIELD:Lsweetie/leonware/api/event/events/client/KeyEvent$KeyEventData;->action:I", "FIELD:Lsweetie/leonware/api/event/events/client/KeyEvent$KeyEventData;->mouse:Z").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, KeyEventData.class), KeyEventData.class, "key;action;mouse", "FIELD:Lsweetie/leonware/api/event/events/client/KeyEvent$KeyEventData;->key:I", "FIELD:Lsweetie/leonware/api/event/events/client/KeyEvent$KeyEventData;->action:I", "FIELD:Lsweetie/leonware/api/event/events/client/KeyEvent$KeyEventData;->mouse:Z").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, KeyEventData.class, Object.class), KeyEventData.class, "key;action;mouse", "FIELD:Lsweetie/leonware/api/event/events/client/KeyEvent$KeyEventData;->key:I", "FIELD:Lsweetie/leonware/api/event/events/client/KeyEvent$KeyEventData;->action:I", "FIELD:Lsweetie/leonware/api/event/events/client/KeyEvent$KeyEventData;->mouse:Z").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public int key() {
            return this.key;
        }

        public int action() {
            return this.action;
        }

        public boolean mouse() {
            return this.mouse;
        }
    }
}
