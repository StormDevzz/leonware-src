package org.ladysnake.satin.mixin.client.render;

import net.minecraft.class_4668;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/mixin/client/render/RenderPhaseAccessor.class */
@Mixin({class_4668.class})
public interface RenderPhaseAccessor {
    @Accessor
    String getName();
}
