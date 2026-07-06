package org.ladysnake.satin.mixin.client.render;

import net.minecraft.class_757;
import net.minecraft.class_9920;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/mixin/client/render/GameRendererAccessor.class */
@Mixin({class_757.class})
public interface GameRendererAccessor {
    @Accessor
    class_9920 getPool();
}
