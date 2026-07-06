/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_276
 *  net.minecraft.class_4599
 *  net.minecraft.class_761
 *  net.minecraft.class_9960
 *  net.minecraft.class_9975
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package sweetie.leonware.inject.accessors;

import net.minecraft.class_276;
import net.minecraft.class_4599;
import net.minecraft.class_761;
import net.minecraft.class_9960;
import net.minecraft.class_9975;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_761.class})
public interface IWorldRenderer {
    @Accessor(value="bufferBuilders")
    public class_4599 _getBufferBuilders();

    @Accessor(value="skyRendering")
    public class_9975 _getSkyRendering();

    @Accessor(value="framebufferSet")
    public class_9960 _getFrameBufferSet();

    @Accessor(value="entityOutlineFramebuffer")
    public class_276 _getEntityOutlineFramebuffer();
}

