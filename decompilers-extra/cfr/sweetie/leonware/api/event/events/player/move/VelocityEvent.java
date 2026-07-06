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

public class VelocityEvent
extends Event<VelocityEventData> {
    private static final VelocityEvent instance = new VelocityEvent();

    @Generated
    public static VelocityEvent getInstance() {
        return instance;
    }

    public static class VelocityEventData {
        private class_243 movementInput;
        private float speed;
        private float yaw;
        private class_243 velocity;

        @Generated
        public class_243 getMovementInput() {
            return this.movementInput;
        }

        @Generated
        public float getSpeed() {
            return this.speed;
        }

        @Generated
        public float getYaw() {
            return this.yaw;
        }

        @Generated
        public class_243 getVelocity() {
            return this.velocity;
        }

        @Generated
        public VelocityEventData(class_243 movementInput, float speed, float yaw, class_243 velocity) {
            this.movementInput = movementInput;
            this.speed = speed;
            this.yaw = yaw;
            this.velocity = velocity;
        }

        @Generated
        public void setMovementInput(class_243 movementInput) {
            this.movementInput = movementInput;
        }

        @Generated
        public void setVelocity(class_243 velocity) {
            this.velocity = velocity;
        }
    }
}

