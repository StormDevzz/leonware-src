// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.render;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import sweetie.leonware.client.features.modules.render.AmbienceModule;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.class_4184;
import net.minecraft.class_9958;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import sweetie.leonware.client.features.modules.render.RemovalsModule;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.class_1297;
import net.minecraft.class_758;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_758.class })
public class MixinBackgroundRenderer
{
    @Inject(method = { "getFogModifier(Lnet/minecraft/entity/Entity;F)Lnet/minecraft/client/render/BackgroundRenderer$StatusEffectFogModifier;" }, at = { @At("HEAD") }, cancellable = true)
    private static void onGetFogModifier(final class_1297 entity, final float tickDelta, final CallbackInfoReturnable<Object> info) {
        if (RemovalsModule.getInstance().isBadEffects()) {
            info.setReturnValue((Object)null);
        }
    }
    
    @ModifyReturnValue(method = { "applyFog" }, at = { @At("RETURN") })
    private static class_9958 onApplyFog(final class_9958 original, @Local(argsOnly = true) final class_4184 camera, @Local(argsOnly = true, ordinal = 0) final float viewDistance) {
        return AmbienceModule.getInstance().applyCustomFog(camera, viewDistance, original);
    }
}
