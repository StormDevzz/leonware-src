// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.accessors;

import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.Mutable;
import net.minecraft.class_1309;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_1309.class })
public interface ILivingEntity
{
    @Mutable
    @Accessor("jumpingCooldown")
    void setJumpingCooldown(final int p0);
}
