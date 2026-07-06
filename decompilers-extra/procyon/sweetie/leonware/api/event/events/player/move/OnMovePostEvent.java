// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.event.events.player.move;

import net.minecraft.class_243;
import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class OnMovePostEvent extends Event<OnMovePostEventData>
{
    private static final OnMovePostEvent instance;
    
    @Generated
    public static OnMovePostEvent getInstance() {
        return OnMovePostEvent.instance;
    }
    
    static {
        instance = new OnMovePostEvent();
    }
    
    public static class OnMovePostEventData
    {
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
        public OnMovePostEventData(final float speed, final class_243 movementInput) {
            this.speed = speed;
            this.movementInput = movementInput;
        }
    }
}
