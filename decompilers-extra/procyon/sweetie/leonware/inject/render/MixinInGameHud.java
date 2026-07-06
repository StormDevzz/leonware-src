// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.render;

import sweetie.leonware.client.features.modules.render.RemovalsModule;
import net.minecraft.class_310;
import net.minecraft.class_266;
import sweetie.leonware.client.features.modules.render.InterfaceModule;
import sweetie.leonware.client.features.modules.render.CustomCrosshairModule;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import sweetie.leonware.api.utils.render.Render2DEngine;
import sweetie.leonware.api.event.events.render.Render2DEvent;
import sweetie.leonware.api.utils.render.KawaseBlurProgram;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.class_9779;
import net.minecraft.class_332;
import net.minecraft.class_329;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_329.class })
public class MixinInGameHud
{
    @Inject(method = { "render" }, at = { @At("HEAD") })
    public void renderHook(final class_332 context, final class_9779 tickCounter, final CallbackInfo ci) {
        KawaseBlurProgram.render(context.method_51448());
        Render2DEvent.getInstance().call(new Render2DEvent.Render2DEventData(context, context.method_51448(), tickCounter.method_60637(false)));
        Render2DEngine.onRender(context);
    }
    
    @Inject(method = { "renderCrosshair" }, at = { @At("HEAD") }, cancellable = true)
    private void onRenderCrosshair(final class_332 context, final class_9779 tickCounter, final CallbackInfo ci) {
        if (CustomCrosshairModule.getInstance().isEnabled()) {
            ci.cancel();
        }
    }
    
    @Inject(method = { "renderStatusEffectOverlay" }, at = { @At("HEAD") }, cancellable = true)
    private void renderStatusEffectOverlay(final class_332 context, final class_9779 tickCounter, final CallbackInfo ci) {
        if (InterfaceModule.getInstance().widgets.isEnabled("Potions")) {
            ci.cancel();
        }
    }
    
    @Inject(method = { "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V" }, at = { @At("HEAD") }, cancellable = true)
    private void renderScoreboardSidebar(final class_332 context, final class_266 objective, final CallbackInfo ci) {
        if (class_310.method_1551().field_1724 == null) {
            return;
        }
        if (RemovalsModule.getInstance().isScoreboard()) {
            ci.cancel();
        }
    }
}
