/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.event.events.player.move;

import lombok.Generated;
import sweetie.leonware.api.event.events.Event;
import sweetie.leonware.api.utils.player.DirectionalInput;

public class SprintEvent
extends Event<SprintEventData> {
    private static final SprintEvent instance = new SprintEvent();

    @Override
    public boolean call(SprintEventData any) {
        any.setSprint(false);
        super.call(any);
        return any.isSprint();
    }

    @Generated
    public static SprintEvent getInstance() {
        return instance;
    }

    public static class SprintEventData {
        private boolean sprint = false;
        private final DirectionalInput directionalInput;

        @Generated
        public void setSprint(boolean sprint) {
            this.sprint = sprint;
        }

        @Generated
        public boolean isSprint() {
            return this.sprint;
        }

        @Generated
        public DirectionalInput getDirectionalInput() {
            return this.directionalInput;
        }

        @Generated
        public SprintEventData(DirectionalInput directionalInput) {
            this.directionalInput = directionalInput;
        }
    }
}

