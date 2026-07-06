// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.render;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import sweetie.leonware.client.features.modules.render.FreeCamModule;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.class_9779;
import net.minecraft.class_332;
import net.minecraft.class_329;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_329.class })
public class MixinHudFreeCam
{
    @Inject(method = { "renderHotbar" }, at = { @At("HEAD") }, cancellable = true)
    private void onRenderHotbar(final class_332 context, final class_9779 tickCounter, final CallbackInfo ci) {
        if (FreeCamModule.getInstance().shouldHideHotbar()) {
            ci.cancel();
        }
    }
    
    @Inject(method = { "renderExperienceBar" }, at = { @At("HEAD") }, cancellable = true)
    private void onRenderXP(final class_332 context, final int x, final CallbackInfo ci) {
        if (FreeCamModule.getInstance().shouldHideHotbar()) {
            ci.cancel();
        }
    }
    
    @Inject(method = { "renderStatusBars" }, at = { @At("HEAD") }, cancellable = true)
    private void onRenderStatusBars(final class_332 context, final CallbackInfo ci) {
        if (FreeCamModule.getInstance().shouldHideHotbar()) {
            ci.cancel();
        }
    }
}
