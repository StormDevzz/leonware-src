// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.event.events.player.move;

import net.minecraft.class_243;
import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class VelocityEvent extends Event<VelocityEventData>
{
    private static final VelocityEvent instance;
    
    @Generated
    public static VelocityEvent getInstance() {
        return VelocityEvent.instance;
    }
    
    static {
        instance = new VelocityEvent();
    }
    
    public static class VelocityEventData
    {
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
        public VelocityEventData(final class_243 movementInput, final float speed, final float yaw, final class_243 velocity) {
            this.movementInput = movementInput;
            this.speed = speed;
            this.yaw = yaw;
            this.velocity = velocity;
        }
        
        @Generated
        public void setMovementInput(final class_243 movementInput) {
            this.movementInput = movementInput;
        }
        
        @Generated
        public void setVelocity(final class_243 velocity) {
            this.velocity = velocity;
        }
    }
}
