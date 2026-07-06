// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.block;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.class_2246;
import sweetie.leonware.client.features.modules.movement.IceSpeedModule;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.class_2248;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_2248.class })
public abstract class MixinBlock
{
    @Inject(method = { "getSlipperiness" }, at = { @At("HEAD") }, cancellable = true)
    private void onGetSlipperiness(final CallbackInfoReturnable<Float> cir) {
        final IceSpeedModule mod = IceSpeedModule.getInstance();
        if (mod != null && mod.isEnabled()) {
            final Object block = this;
            if (block == class_2246.field_10295 || block == class_2246.field_10225 || block == class_2246.field_10110 || block == class_2246.field_10384) {
                cir.setReturnValue((Object)mod.speed.getValue());
            }
        }
    }
}
