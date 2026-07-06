package sweetie.leonware.api.event.events.client;

import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/event/events/client/GameLoopEvent.class */
public class GameLoopEvent extends Event<GameLoopEvent> {
    private static final GameLoopEvent instance = new GameLoopEvent();

    @Generated
    public static GameLoopEvent getInstance() {
        return instance;
    }
}
