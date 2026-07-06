package sweetie.leonware.api.event.events;

import lombok.Generated;
import sweetie.leonware.api.event.Flora;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/event/events/Event.class */
public class Event<T> extends Flora<T> {
    private boolean cancel = false;

    @Generated
    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    @Generated
    public boolean isCancel() {
        return this.cancel;
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected T getSelf() {
        return this;
    }

    public boolean call() {
        this.cancel = false;
        notify(getSelf());
        return this.cancel;
    }

    public boolean call(T any) {
        this.cancel = false;
        notify(any);
        return this.cancel;
    }
}
