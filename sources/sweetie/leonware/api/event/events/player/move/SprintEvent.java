package sweetie.leonware.api.event.events.player.move;

import lombok.Generated;
import sweetie.leonware.api.event.events.Event;
import sweetie.leonware.api.utils.player.DirectionalInput;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/event/events/player/move/SprintEvent.class */
public class SprintEvent extends Event<SprintEventData> {
    private static final SprintEvent instance = new SprintEvent();

    @Generated
    public static SprintEvent getInstance() {
        return instance;
    }

    @Override // sweetie.leonware.api.event.events.Event
    public boolean call(SprintEventData any) {
        any.setSprint(false);
        super.call(any);
        return any.isSprint();
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/event/events/player/move/SprintEvent$SprintEventData.class */
    public static class SprintEventData {
        private boolean sprint = false;
        private final DirectionalInput directionalInput;

        @Generated
        public void setSprint(boolean sprint) {
            this.sprint = sprint;
        }

        @Generated
        public SprintEventData(DirectionalInput directionalInput) {
            this.directionalInput = directionalInput;
        }

        @Generated
        public boolean isSprint() {
            return this.sprint;
        }

        @Generated
        public DirectionalInput getDirectionalInput() {
            return this.directionalInput;
        }
    }
}
