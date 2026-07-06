/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_10185
 *  net.minecraft.class_744
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 */
package sweetie.leonware.inject.input;

import net.minecraft.class_10185;
import net.minecraft.class_744;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import sweetie.leonware.api.system.interfaces.IPlayerInput;

@Mixin(value={class_744.class})
public abstract class MixinInput
implements IPlayerInput {
    @Unique
    protected class_10185 untransformed = class_10185.field_54098;

    @Override
    public class_10185 evelina$getUntransformed() {
        return this.untransformed;
    }
}

