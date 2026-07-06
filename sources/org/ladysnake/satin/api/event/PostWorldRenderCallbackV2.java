package org.ladysnake.satin.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.class_4184;
import net.minecraft.class_4587;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/api/event/PostWorldRenderCallbackV2.class */
@FunctionalInterface
public interface PostWorldRenderCallbackV2 {

    @Deprecated
    public static final Event<PostWorldRenderCallbackV2> EVENT = EventFactory.createArrayBacked(PostWorldRenderCallbackV2.class, listeners -> {
        return (posingStack, camera, tickDelta) -> {
            ((PostWorldRenderCallback) PostWorldRenderCallback.EVENT.invoker()).onWorldRendered(camera, tickDelta);
            for (PostWorldRenderCallbackV2 handler : listeners) {
                handler.onWorldRendered(posingStack, camera, tickDelta);
            }
        };
    });

    void onWorldRendered(class_4587 class_4587Var, class_4184 class_4184Var, float f);
}
