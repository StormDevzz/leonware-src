package sweetie.leonware.inject.input;

import net.minecraft.class_10185;
import net.minecraft.class_744;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import sweetie.leonware.api.system.interfaces.IPlayerInput;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/input/MixinInput.class */
@Mixin({class_744.class})
public abstract class MixinInput implements IPlayerInput {

    @Unique
    protected class_10185 untransformed = class_10185.field_54098;

    @Override // sweetie.leonware.api.system.interfaces.IPlayerInput
    public class_10185 evelina$getUntransformed() {
        return this.untransformed;
    }
}
