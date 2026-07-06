package org.ladysnake.satin.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.class_4184;
import net.minecraft.class_4604;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/api/event/EntitiesPreRenderCallback.class */
@FunctionalInterface
public interface EntitiesPreRenderCallback {

    @Deprecated
    public static final Event<EntitiesPreRenderCallback> EVENT = EventFactory.createArrayBacked(EntitiesPreRenderCallback.class, listeners -> {
        return (camera, frustum, tickDelta) -> {
            for (EntitiesPreRenderCallback handler : listeners) {
                handler.beforeEntitiesRender(camera, frustum, tickDelta);
            }
        };
    });

    void beforeEntitiesRender(class_4184 class_4184Var, class_4604 class_4604Var, float f);
}
