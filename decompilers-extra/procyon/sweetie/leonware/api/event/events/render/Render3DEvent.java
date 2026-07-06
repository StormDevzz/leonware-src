// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.event.events.render;

import net.minecraft.class_4587;
import lombok.Generated;
import sweetie.leonware.api.event.events.Event;

public class Render3DEvent extends Event<Render3DEventData>
{
    private static final Render3DEvent instance;
    
    @Generated
    public static Render3DEvent getInstance() {
        return Render3DEvent.instance;
    }
    
    static {
        instance = new Render3DEvent();
    }
    
    record Render3DEventData(class_4587 matrixStack, float partialTicks) {}
}
