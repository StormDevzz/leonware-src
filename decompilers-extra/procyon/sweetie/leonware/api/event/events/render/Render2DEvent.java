// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.event.events.render;

import net.minecraft.class_4587;
import net.minecraft.class_332;
import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class Render2DEvent extends Event<Render2DEventData>
{
    private static final Render2DEvent instance;
    
    @Generated
    public static Render2DEvent getInstance() {
        return Render2DEvent.instance;
    }
    
    static {
        instance = new Render2DEvent();
    }
    
    record Render2DEventData(class_332 context, class_4587 matrixStack, float partialTicks) {}
}
