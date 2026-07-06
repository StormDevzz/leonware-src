package org.ladysnake.satin.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import org.joml.Matrix4f;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/api/event/PostWorldRenderCallbackV3.class */
@FunctionalInterface
public interface PostWorldRenderCallbackV3 {

    @Deprecated
    public static final Event<PostWorldRenderCallbackV3> EVENT = EventFactory.createArrayBacked(PostWorldRenderCallbackV3.class, listeners -> {
        return (matrices, projectionMat, modelViewMath, camera, tickDelta) -> {
            ((PostWorldRenderCallbackV2) PostWorldRenderCallbackV2.EVENT.invoker()).onWorldRendered(matrices, camera, tickDelta);
            for (PostWorldRenderCallbackV3 handler : listeners) {
                handler.onWorldRendered(matrices, projectionMat, modelViewMath, camera, tickDelta);
            }
        };
    });

    void onWorldRendered(class_4587 class_4587Var, Matrix4f matrix4f, Matrix4f matrix4f2, class_4184 class_4184Var, float f);
}
