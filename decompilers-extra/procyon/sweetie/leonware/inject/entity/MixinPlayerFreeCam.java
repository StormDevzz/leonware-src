// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.entity;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import sweetie.leonware.api.utils.player.MoveUtil;
import sweetie.leonware.client.features.modules.render.FreeCamModule;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_746.class })
public class MixinPlayerFreeCam
{
    @Inject(method = { "tickMovement" }, at = { @At("HEAD") }, cancellable = true)
    private void freeCamFreezeMovement(final CallbackInfo ci) {
        final FreeCamModule fc = FreeCamModule.getInstance();
        if (!fc.isEnabled()) {
            return;
        }
        final class_746 player = (class_746)this;
        player.field_3913.field_3905 = 0.0f;
        player.field_3913.field_3907 = 0.0f;
        MoveUtil.updateMovementKeys();
        if (fc.freeze.getValue()) {
            player.method_18800(0.0, 0.0, 0.0);
        }
        ci.cancel();
    }
}
