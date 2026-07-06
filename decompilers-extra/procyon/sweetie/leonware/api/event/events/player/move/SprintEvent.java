// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.event.events.player.move;

import sweetie.leonware.api.utils.player.DirectionalInput;
import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class SprintEvent extends Event<SprintEventData>
{
    private static final SprintEvent instance;
    
    @Override
    public boolean call(final SprintEventData any) {
        any.setSprint(false);
        super.call(any);
        return any.isSprint();
    }
    
    @Generated
    public static SprintEvent getInstance() {
        return SprintEvent.instance;
    }
    
    static {
        instance = new SprintEvent();
    }
    
    public static class SprintEventData
    {
        private boolean sprint;
        private final DirectionalInput directionalInput;
        
        @Generated
        public void setSprint(final boolean sprint) {
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
        public SprintEventData(final DirectionalInput directionalInput) {
            this.sprint = false;
            this.directionalInput = directionalInput;
        }
    }
}
