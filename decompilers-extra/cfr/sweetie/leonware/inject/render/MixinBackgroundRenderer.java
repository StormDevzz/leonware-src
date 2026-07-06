/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyReturnValue
 *  com.llamalad7.mixinextras.sugar.Local
 *  net.minecraft.class_1297
 *  net.minecraft.class_4184
 *  net.minecraft.class_758
 *  net.minecraft.class_9958
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package sweetie.leonware.inject.render;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.class_1297;
import net.minecraft.class_4184;
import net.minecraft.class_758;
import net.minecraft.class_9958;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sweetie.leonware.client.features.modules.render.AmbienceModule;
import sweetie.leonware.client.features.modules.render.RemovalsModule;

@Mixin(value={class_758.class})
public class MixinBackgroundRenderer {
    @Inject(method={"getFogModifier(Lnet/minecraft/entity/Entity;F)Lnet/minecraft/client/render/BackgroundRenderer$StatusEffectFogModifier;"}, at={@At(value="HEAD")}, cancellable=true)
    private static void onGetFogModifier(class_1297 entity, float tickDelta, CallbackInfoReturnable<Object> info) {
        if (RemovalsModule.getInstance().isBadEffects()) {
            info.setReturnValue(null);
        }
    }

    @ModifyReturnValue(method={"applyFog"}, at={@At(value="RETURN")})
    private static class_9958 onApplyFog(class_9958 original, @Local(argsOnly=true) class_4184 camera, @Local(argsOnly=true, ordinal=0) float viewDistance) {
        return AmbienceModule.getInstance().applyCustomFog(camera, viewDistance, original);
    }
}

