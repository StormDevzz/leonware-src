// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.accessors;

import org.spongepowered.asm.mixin.gen.Invoker;
import net.minecraft.class_636;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_636.class })
public interface ClientPlayerInteractionManagerAccessor
{
    @Invoker("syncSelectedSlot")
    void leonware$syncSelectedSlot();
}
