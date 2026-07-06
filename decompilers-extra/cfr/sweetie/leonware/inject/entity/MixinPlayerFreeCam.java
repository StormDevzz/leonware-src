/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_746
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package sweetie.leonware.inject.entity;

import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sweetie.leonware.api.utils.player.MoveUtil;
import sweetie.leonware.client.features.modules.render.FreeCamModule;

@Mixin(value={class_746.class})
public class MixinPlayerFreeCam {
    @Inject(method={"tickMovement"}, at={@At(value="HEAD")}, cancellable=true)
    private void freeCamFreezeMovement(CallbackInfo ci) {
        FreeCamModule fc = FreeCamModule.getInstance();
        if (!fc.isEnabled()) {
            return;
        }
        class_746 player = (class_746)this;
        player.field_3913.field_3905 = 0.0f;
        player.field_3913.field_3907 = 0.0f;
        MoveUtil.updateMovementKeys();
        if (((Boolean)fc.freeze.getValue()).booleanValue()) {
            player.method_18800(0.0, 0.0, 0.0);
        }
        ci.cancel();
    }
}

