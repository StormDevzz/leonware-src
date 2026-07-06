package sweetie.leonware.inject.render;

import net.minecraft.class_329;
import net.minecraft.class_332;
import net.minecraft.class_9779;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sweetie.leonware.client.features.modules.render.FreeCamModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/render/MixinHudFreeCam.class */
@Mixin({class_329.class})
public class MixinHudFreeCam {
    @Inject(method = {"renderHotbar"}, at = {@At("HEAD")}, cancellable = true)
    private void onRenderHotbar(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
        if (FreeCamModule.getInstance().shouldHideHotbar()) {
            ci.cancel();
        }
    }

    @Inject(method = {"renderExperienceBar"}, at = {@At("HEAD")}, cancellable = true)
    private void onRenderXP(class_332 context, int x, CallbackInfo ci) {
        if (FreeCamModule.getInstance().shouldHideHotbar()) {
            ci.cancel();
        }
    }

    @Inject(method = {"renderStatusBars"}, at = {@At("HEAD")}, cancellable = true)
    private void onRenderStatusBars(class_332 context, CallbackInfo ci) {
        if (FreeCamModule.getInstance().shouldHideHotbar()) {
            ci.cancel();
        }
    }
}
