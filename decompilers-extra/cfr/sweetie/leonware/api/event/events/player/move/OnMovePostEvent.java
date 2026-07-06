/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_243
 */
package sweetie.leonware.api.event.events.player.move;

import lombok.Generated;
import net.minecraft.class_243;
import sweetie.leonware.api.event.events.Event;

public class OnMovePostEvent
extends Event<OnMovePostEventData> {
    private static final OnMovePostEvent instance = new OnMovePostEvent();

    @Generated
    public static OnMovePostEvent getInstance() {
        return instance;
    }

    public static class OnMovePostEventData {
        private final float speed;
        private final class_243 movementInput;

        @Generated
        public float getSpeed() {
            return this.speed;
        }

        @Generated
        public class_243 getMovementInput() {
            return this.movementInput;
        }

        @Generated
        public OnMovePostEventData(float speed, class_243 movementInput) {
            this.speed = speed;
            this.movementInput = movementInput;
        }
    }
}

