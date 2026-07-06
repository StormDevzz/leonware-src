/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_329
 *  net.minecraft.class_332
 *  net.minecraft.class_9779
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package sweetie.leonware.inject.render;

import net.minecraft.class_329;
import net.minecraft.class_332;
import net.minecraft.class_9779;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sweetie.leonware.client.features.modules.render.FreeCamModule;

@Mixin(value={class_329.class})
public class MixinHudFreeCam {
    @Inject(method={"renderHotbar"}, at={@At(value="HEAD")}, cancellable=true)
    private void onRenderHotbar(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
        if (FreeCamModule.getInstance().shouldHideHotbar()) {
            ci.cancel();
        }
    }

    @Inject(method={"renderExperienceBar"}, at={@At(value="HEAD")}, cancellable=true)
    private void onRenderXP(class_332 context, int x, CallbackInfo ci) {
        if (FreeCamModule.getInstance().shouldHideHotbar()) {
            ci.cancel();
        }
    }

    @Inject(method={"renderStatusBars"}, at={@At(value="HEAD")}, cancellable=true)
    private void onRenderStatusBars(class_332 context, CallbackInfo ci) {
        if (FreeCamModule.getInstance().shouldHideHotbar()) {
            ci.cancel();
        }
    }
}

