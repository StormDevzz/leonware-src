// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.accessors;

import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.class_284;
import java.util.Map;
import net.minecraft.class_5944;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_5944.class })
public interface IShaderProgram
{
    @Accessor("uniformsByName")
    Map<String, class_284> leonware$getUniforms();
}
