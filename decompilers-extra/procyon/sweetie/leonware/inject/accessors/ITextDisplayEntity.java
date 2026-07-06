// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.accessors;

import org.spongepowered.asm.mixin.gen.Invoker;
import net.minecraft.class_2561;
import net.minecraft.class_8113;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_8113.class_8123.class })
public interface ITextDisplayEntity
{
    @Invoker("getText")
    class_2561 invokeGetText();
}
