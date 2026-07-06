// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.render;

import net.minecraft.class_1058;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import sweetie.leonware.client.features.modules.render.RemovalsModule;
import net.minecraft.class_310;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.class_4597;
import net.minecraft.class_4587;
import net.minecraft.class_4603;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_4603.class })
public class MixinInGameOverlayRenderer
{
    @Inject(method = { "renderFireOverlay" }, at = { @At("HEAD") }, cancellable = true)
    private static void onRenderFireOverlay(final class_4587 matrices, final class_4597 vertexConsumers, final CallbackInfo ci) {
        if (class_310.method_1551().field_1724 == null) {
            return;
        }
        if (RemovalsModule.getInstance().isFireOverlay()) {
            ci.cancel();
        }
    }
    
    @Inject(method = { "renderUnderwaterOverlay" }, at = { @At("HEAD") }, cancellable = true)
    private static void onRenderUnderwaterOverlay(final class_310 client, final class_4587 matrices, final class_4597 vertexConsumers, final CallbackInfo ci) {
        if (class_310.method_1551().field_1724 == null) {
            return;
        }
        if (RemovalsModule.getInstance().isWaterOverlay()) {
            ci.cancel();
        }
    }
    
    @Inject(method = { "renderInWallOverlay" }, at = { @At("HEAD") }, cancellable = true)
    private static void onRenderInWallOverlay(final class_1058 sprite, final class_4587 matrices, final class_4597 vertexConsumers, final CallbackInfo ci) {
        if (class_310.method_1551().field_1724 == null) {
            return;
        }
        if (RemovalsModule.getInstance().isInwallOverlay()) {
            ci.cancel();
        }
    }
}
