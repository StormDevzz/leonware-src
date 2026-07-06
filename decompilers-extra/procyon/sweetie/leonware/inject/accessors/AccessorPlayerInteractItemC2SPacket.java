// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.accessors;

import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.Mutable;
import net.minecraft.class_2886;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_2886.class })
public interface AccessorPlayerInteractItemC2SPacket
{
    @Mutable
    @Accessor("yaw")
    void leonware$setYaw(final float p0);
    
    @Mutable
    @Accessor("pitch")
    void leonware$setPitch(final float p0);
}
