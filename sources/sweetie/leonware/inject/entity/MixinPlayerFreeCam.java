package sweetie.leonware.inject.entity;

import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sweetie.leonware.api.utils.player.MoveUtil;
import sweetie.leonware.client.features.modules.render.FreeCamModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/entity/MixinPlayerFreeCam.class */
@Mixin({class_746.class})
public class MixinPlayerFreeCam {
    @Inject(method = {"tickMovement"}, at = {@At("HEAD")}, cancellable = true)
    private void freeCamFreezeMovement(CallbackInfo ci) {
        FreeCamModule fc = FreeCamModule.getInstance();
        if (fc.isEnabled()) {
            class_746 player = (class_746) this;
            player.field_3913.field_3905 = 0.0f;
            player.field_3913.field_3907 = 0.0f;
            MoveUtil.updateMovementKeys();
            if (fc.freeze.getValue().booleanValue()) {
                player.method_18800(0.0d, 0.0d, 0.0d);
            }
            ci.cancel();
        }
    }
}
