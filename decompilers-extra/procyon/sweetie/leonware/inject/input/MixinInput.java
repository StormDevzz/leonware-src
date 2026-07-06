// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.input;

import org.spongepowered.asm.mixin.Unique;
import net.minecraft.class_10185;
import net.minecraft.class_744;
import org.spongepowered.asm.mixin.Mixin;
import sweetie.leonware.api.system.interfaces.IPlayerInput;

@Mixin({ class_744.class })
public abstract class MixinInput implements IPlayerInput
{
    @Unique
    protected class_10185 untransformed;
    
    public MixinInput() {
        this.untransformed = class_10185.field_54098;
    }
    
    @Override
    public class_10185 evelina$getUntransformed() {
        return this.untransformed;
    }
}
