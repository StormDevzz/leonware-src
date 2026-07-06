/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_266
 *  net.minecraft.class_310
 *  net.minecraft.class_329
 *  net.minecraft.class_332
 *  net.minecraft.class_9779
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package sweetie.leonware.inject.render;

import net.minecraft.class_266;
import net.minecraft.class_310;
import net.minecraft.class_329;
import net.minecraft.class_332;
import net.minecraft.class_9779;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sweetie.leonware.api.event.events.render.Render2DEvent;
import sweetie.leonware.api.utils.render.KawaseBlurProgram;
import sweetie.leonware.api.utils.render.Render2DEngine;
import sweetie.leonware.client.features.modules.render.CustomCrosshairModule;
import sweetie.leonware.client.features.modules.render.InterfaceModule;
import sweetie.leonware.client.features.modules.render.RemovalsModule;

@Mixin(value={class_329.class})
public class MixinInGameHud {
    @Inject(method={"render"}, at={@At(value="HEAD")})
    public void renderHook(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
        KawaseBlurProgram.render(context.method_51448());
        Render2DEvent.getInstance().call(new Render2DEvent.Render2DEventData(context, context.method_51448(), tickCounter.method_60637(false)));
        Render2DEngine.onRender(context);
    }

    @Inject(method={"renderCrosshair"}, at={@At(value="HEAD")}, cancellable=true)
    private void onRenderCrosshair(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
        if (CustomCrosshairModule.getInstance().isEnabled()) {
            ci.cancel();
        }
    }

    @Inject(method={"renderStatusEffectOverlay"}, at={@At(value="HEAD")}, cancellable=true)
    private void renderStatusEffectOverlay(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
        if (InterfaceModule.getInstance().widgets.isEnabled("Potions")) {
            ci.cancel();
        }
    }

    @Inject(method={"renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void renderScoreboardSidebar(class_332 context, class_266 objective, CallbackInfo ci) {
        if (class_310.method_1551().field_1724 == null) {
            return;
        }
        if (RemovalsModule.getInstance().isScoreboard()) {
            ci.cancel();
        }
    }
}

