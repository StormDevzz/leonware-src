// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.accessors;

import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.class_9920;
import net.minecraft.class_757;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_757.class })
public interface IGameRenderer
{
    @Accessor("pool")
    class_9920 _getPool();
}
