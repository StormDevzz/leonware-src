package org.ladysnake.satin.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.class_310;
import net.minecraft.class_4184;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/api/event/PostWorldRenderCallback.class */
@FunctionalInterface
public interface PostWorldRenderCallback {

    @Deprecated
    public static final Event<PostWorldRenderCallback> EVENT = EventFactory.createArrayBacked(PostWorldRenderCallback.class, listeners -> {
        return (camera, tickDelta) -> {
            class_310.method_1551().method_1522().freezeDepthMap();
            for (PostWorldRenderCallback handler : listeners) {
                handler.onWorldRendered(camera, tickDelta);
            }
        };
    });

    void onWorldRendered(class_4184 class_4184Var, float f);
}
