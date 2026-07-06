package org.ladysnake.satin.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.class_4184;
import net.minecraft.class_4604;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/api/event/EntitiesPostRenderCallback.class */
@FunctionalInterface
public interface EntitiesPostRenderCallback {

    @Deprecated
    public static final Event<EntitiesPostRenderCallback> EVENT = EventFactory.createArrayBacked(EntitiesPostRenderCallback.class, listeners -> {
        return (camera, frustum, tickDelta) -> {
            for (EntitiesPostRenderCallback handler : listeners) {
                handler.onEntitiesRendered(camera, frustum, tickDelta);
            }
        };
    });

    void onEntitiesRendered(class_4184 class_4184Var, class_4604 class_4604Var, float f);
}
