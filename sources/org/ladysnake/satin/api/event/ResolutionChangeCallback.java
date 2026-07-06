package org.ladysnake.satin.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/api/event/ResolutionChangeCallback.class */
@FunctionalInterface
public interface ResolutionChangeCallback {
    public static final Event<ResolutionChangeCallback> EVENT = EventFactory.createArrayBacked(ResolutionChangeCallback.class, listeners -> {
        return (newWidth, newHeight) -> {
            for (ResolutionChangeCallback event : listeners) {
                event.onResolutionChanged(newWidth, newHeight);
            }
        };
    });

    void onResolutionChanged(int i, int i2);
}
