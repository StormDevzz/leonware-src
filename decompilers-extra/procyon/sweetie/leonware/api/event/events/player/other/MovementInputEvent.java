// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.event.events.player.other;

import sweetie.leonware.api.utils.player.DirectionalInput;
import net.minecraft.class_10185;
import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class MovementInputEvent extends Event<MovementInputEventData>
{
    private static final MovementInputEvent instance;
    
    @Generated
    public static MovementInputEvent getInstance() {
        return MovementInputEvent.instance;
    }
    
    static {
        instance = new MovementInputEvent();
    }
    
    public static class MovementInputEventData
    {
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
        public MovementInputEventData(final class_10185 playerInput, final boolean jump, final boolean sneak, final DirectionalInput directionalInput) {
            this.playerInput = playerInput;
            this.jump = jump;
            this.sneak = sneak;
            this.directionalInput = directionalInput;
        }
        
        @Generated
        public void setJump(final boolean jump) {
            this.jump = jump;
        }
        
        @Generated
        public void setSneak(final boolean sneak) {
            this.sneak = sneak;
        }
        
        @Generated
        public void setDirectionalInput(final DirectionalInput directionalInput) {
            this.directionalInput = directionalInput;
        }
    }
}
