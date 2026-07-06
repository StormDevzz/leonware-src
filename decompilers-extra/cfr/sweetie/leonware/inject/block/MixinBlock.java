/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2246
 *  net.minecraft.class_2248
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package sweetie.leonware.inject.block;

import net.minecraft.class_2246;
import net.minecraft.class_2248;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sweetie.leonware.client.features.modules.movement.IceSpeedModule;

@Mixin(value={class_2248.class})
public abstract class MixinBlock {
    @Inject(method={"getSlipperiness"}, at={@At(value="HEAD")}, cancellable=true)
    private void onGetSlipperiness(CallbackInfoReturnable<Float> cir) {
        MixinBlock block;
        IceSpeedModule mod = IceSpeedModule.getInstance();
        if (mod != null && mod.isEnabled() && ((block = this) == class_2246.field_10295 || block == class_2246.field_10225 || block == class_2246.field_10110 || block == class_2246.field_10384)) {
            cir.setReturnValue((Object)Float.valueOf(((Float)mod.speed.getValue()).floatValue()));
        }
    }
}

