// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.accessors;

import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.class_283;
import java.util.List;
import net.minecraft.class_279;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_279.class })
public interface IPostEffectProcessor
{
    @Accessor("passes")
    List<class_283> leonware$getPasses();
}
