/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_10185
 */
package sweetie.leonware.api.event.events.player.other;

import lombok.Generated;
import net.minecraft.class_10185;
import sweetie.leonware.api.event.events.Event;
import sweetie.leonware.api.utils.player.DirectionalInput;

public class MovementInputEvent
extends Event<MovementInputEventData> {
    private static final MovementInputEvent instance = new MovementInputEvent();

    @Generated
    public static MovementInputEvent getInstance() {
        return instance;
    }

    public static class MovementInputEventData {
        private final class_10185 playerInput;
        private boolean jump;
        private boolean sneak;
        private DirectionalInput directionalInput;

        @Generated
        public class_10185 getPlayerInput() {
            return this.playerInput;
        }

        @Generated
        public boolean isJump() {
            return this.jump;
        }

        @Generated
        public boolean isSneak() {
            return this.sneak;
        }

        @Generated
        public DirectionalInput getDirectionalInput() {
            return this.directionalInput;
        }

        @Generated
        public MovementInputEventData(class_10185 playerInput, boolean jump, boolean sneak, DirectionalInput directionalInput) {
            this.playerInput = playerInput;
            this.jump = jump;
            this.sneak = sneak;
            this.directionalInput = directionalInput;
        }

        @Generated
        public void setJump(boolean jump) {
            this.jump = jump;
        }

        @Generated
        public void setSneak(boolean sneak) {
            this.sneak = sneak;
        }

        @Generated
        public void setDirectionalInput(DirectionalInput directionalInput) {
            this.directionalInput = directionalInput;
        }
    }
}

