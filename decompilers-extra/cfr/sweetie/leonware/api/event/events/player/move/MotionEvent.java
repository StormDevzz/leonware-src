/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.event.events.player.move;

import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class MotionEvent
extends Event<MotionEventData> {
    private static final MotionEvent instance = new MotionEvent();

    @Generated
    public static MotionEvent getInstance() {
        return instance;
    }

    public static class MotionEventData {
        private double x;
        private double y;
        private double z;
        private double yaw;
        private double pitch;
        private boolean ground;

        @Generated
        public double x() {
            return this.x;
        }

        @Generated
        public double y() {
            return this.y;
        }

        @Generated
        public double z() {
            return this.z;
        }

        @Generated
        public double yaw() {
            return this.yaw;
        }

        @Generated
        public double pitch() {
            return this.pitch;
        }

        @Generated
        public boolean ground() {
            return this.ground;
        }

        @Generated
        public MotionEventData x(double x) {
            this.x = x;
            return this;
        }

        @Generated
        public MotionEventData y(double y) {
            this.y = y;
            return this;
        }

        @Generated
        public MotionEventData z(double z) {
            this.z = z;
            return this;
        }

        @Generated
        public MotionEventData yaw(double yaw) {
            this.yaw = yaw;
            return this;
        }

        @Generated
        public MotionEventData pitch(double pitch) {
            this.pitch = pitch;
            return this;
        }

        @Generated
        public MotionEventData ground(boolean ground) {
            this.ground = ground;
            return this;
        }

        @Generated
        public MotionEventData(double x, double y, double z, double yaw, double pitch, boolean ground) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = yaw;
            this.pitch = pitch;
            this.ground = ground;
        }
    }
}

