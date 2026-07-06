// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.event.events.player.move;

import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class MotionEvent extends Event<MotionEventData>
{
    private static final MotionEvent instance;
    
    @Generated
    public static MotionEvent getInstance() {
        return MotionEvent.instance;
    }
    
    static {
        instance = new MotionEvent();
    }
    
    public static class MotionEventData
    {
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
        public MotionEventData x(final double x) {
            this.x = x;
            return this;
        }
        
        @Generated
        public MotionEventData y(final double y) {
            this.y = y;
            return this;
        }
        
        @Generated
        public MotionEventData z(final double z) {
            this.z = z;
            return this;
        }
        
        @Generated
        public MotionEventData yaw(final double yaw) {
            this.yaw = yaw;
            return this;
        }
        
        @Generated
        public MotionEventData pitch(final double pitch) {
            this.pitch = pitch;
            return this;
        }
        
        @Generated
        public MotionEventData ground(final boolean ground) {
            this.ground = ground;
            return this;
        }
        
        @Generated
        public MotionEventData(final double x, final double y, final double z, final double yaw, final double pitch, final boolean ground) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = yaw;
            this.pitch = pitch;
            this.ground = ground;
        }
    }
}
