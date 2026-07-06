package org.ladysnake.satin.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.class_761;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/api/event/WorldRendererReloadCallback.class */
@FunctionalInterface
public interface WorldRendererReloadCallback {
    public static final Event<WorldRendererReloadCallback> EVENT = EventFactory.createArrayBacked(WorldRendererReloadCallback.class, listeners -> {
        return renderer -> {
            for (WorldRendererReloadCallback event : listeners) {
                event.onRendererReload(renderer);
            }
        };
    });

    void onRendererReload(class_761 class_761Var);
}
