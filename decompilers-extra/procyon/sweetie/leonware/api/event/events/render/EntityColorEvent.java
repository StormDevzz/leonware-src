// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.event.events.render;

import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class EntityColorEvent extends Event<EntityColorEventData>
{
    private static final EntityColorEvent instance;
    
    @Generated
    public static EntityColorEvent getInstance() {
        return EntityColorEvent.instance;
    }
    
    static {
        instance = new EntityColorEvent();
    }
    
    public static class EntityColorEventData
    {
        int color;
        
        @Generated
        public int color() {
            return this.color;
        }
        
        @Generated
        public EntityColorEventData color(final int color) {
            this.color = color;
            return this;
        }
        
        @Generated
        public EntityColorEventData(final int color) {
            this.color = color;
        }
    }
}
