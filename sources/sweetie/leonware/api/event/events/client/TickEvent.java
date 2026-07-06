package sweetie.leonware.api.event.events.client;

import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/event/events/client/TickEvent.class */
public class TickEvent extends Event<TickEvent> {
    private static final TickEvent instance = new TickEvent();

    @Generated
    public static TickEvent getInstance() {
        return instance;
    }
}
