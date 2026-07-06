package org.ladysnake.satin.impl;

import java.util.Objects;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.class_4587;
import net.minecraft.class_4604;
import org.ladysnake.satin.api.event.EntitiesPostRenderCallback;
import org.ladysnake.satin.api.event.EntitiesPreRenderCallback;
import org.ladysnake.satin.api.event.PostWorldRenderCallbackV3;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/impl/WorldRenderEventsDelegator.class */
public final class WorldRenderEventsDelegator implements WorldRenderEvents.BeforeEntities, WorldRenderEvents.AfterEntities, WorldRenderEvents.Last {
    public static final WorldRenderEventsDelegator INSTANCE = new WorldRenderEventsDelegator();

    public void beforeEntities(WorldRenderContext context) {
        ((EntitiesPreRenderCallback) EntitiesPreRenderCallback.EVENT.invoker()).beforeEntitiesRender(context.camera(), (class_4604) Objects.requireNonNull(context.frustum()), context.tickCounter().method_60637(false));
    }

    public void afterEntities(WorldRenderContext context) {
        ((EntitiesPostRenderCallback) EntitiesPostRenderCallback.EVENT.invoker()).onEntitiesRendered(context.camera(), (class_4604) Objects.requireNonNull(context.frustum()), context.tickCounter().method_60637(false));
    }

    public void onLast(WorldRenderContext context) {
        ((PostWorldRenderCallbackV3) PostWorldRenderCallbackV3.EVENT.invoker()).onWorldRendered((class_4587) Objects.requireNonNull(context.matrixStack()), context.projectionMatrix(), context.positionMatrix(), context.camera(), context.tickCounter().method_60637(false));
    }

    private WorldRenderEventsDelegator() {
    }
}
