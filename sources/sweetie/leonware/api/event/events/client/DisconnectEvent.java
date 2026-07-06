package sweetie.leonware.api.event.events.client;

import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/event/events/client/DisconnectEvent.class */
public class DisconnectEvent extends Event<DisconnectData> {
    private static final DisconnectEvent instance = new DisconnectEvent();

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/event/events/client/DisconnectEvent$DisconnectData.class */
    public static class DisconnectData {
    }

    @Generated
    public static DisconnectEvent getInstance() {
        return instance;
    }
}
