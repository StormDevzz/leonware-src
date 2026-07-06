// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.render;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import sweetie.leonware.client.features.modules.render.AmbienceModule;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.blaze3d.systems.RenderSystem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ RenderSystem.class })
public class MixinRenderSystem
{
    @Inject(method = { "clearColor" }, at = { @At("HEAD") }, cancellable = true)
    private static void fogColor(final float red, final float green, final float blue, final float alpha, final CallbackInfo ci) {
        if (AmbienceModule.getInstance().applyBackgroundColor()) {
            ci.cancel();
        }
    }
}
