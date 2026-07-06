package org.ladysnake.satin.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/api/event/ShaderEffectRenderCallback.class */
public interface ShaderEffectRenderCallback {
    public static final Event<ShaderEffectRenderCallback> EVENT = EventFactory.createArrayBacked(ShaderEffectRenderCallback.class, listeners -> {
        return tickDelta -> {
            for (ShaderEffectRenderCallback handler : listeners) {
                handler.renderShaderEffects(tickDelta);
            }
        };
    });

    void renderShaderEffects(float f);
}
