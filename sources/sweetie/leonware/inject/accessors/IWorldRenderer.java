package sweetie.leonware.inject.accessors;

import net.minecraft.class_276;
import net.minecraft.class_4599;
import net.minecraft.class_761;
import net.minecraft.class_9960;
import net.minecraft.class_9975;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/accessors/IWorldRenderer.class */
@Mixin({class_761.class})
public interface IWorldRenderer {
    @Accessor("bufferBuilders")
    class_4599 _getBufferBuilders();

    @Accessor("skyRendering")
    class_9975 _getSkyRendering();

    @Accessor("framebufferSet")
    class_9960 _getFrameBufferSet();

    @Accessor("entityOutlineFramebuffer")
    class_276 _getEntityOutlineFramebuffer();
}
