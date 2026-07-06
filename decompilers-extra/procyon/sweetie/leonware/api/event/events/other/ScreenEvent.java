// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.event.events.other;

import java.util.ArrayList;
import net.minecraft.class_4185;
import java.util.List;
import net.minecraft.class_437;
import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class ScreenEvent extends Event<ScreenEventData>
{
    private static final ScreenEvent instance;
    
    @Generated
    public static ScreenEvent getInstance() {
        return ScreenEvent.instance;
    }
    
    static {
        instance = new ScreenEvent();
    }
    
    public static class ScreenEventData
    {
        private final class_437 screen;
        private final List<class_4185> buttons;
        
        @Generated
        public class_437 screen() {
            return this.screen;
        }
        
        @Generated
        public List<class_4185> buttons() {
            return this.buttons;
        }
        
        @Generated
        public ScreenEventData(final class_437 screen) {
            this.buttons = new ArrayList<class_4185>();
            this.screen = screen;
        }
    }
}
