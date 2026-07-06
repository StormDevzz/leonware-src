package sweetie.leonware.inject.render;

import net.minecraft.class_1058;
import net.minecraft.class_310;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_4603;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sweetie.leonware.client.features.modules.render.RemovalsModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/render/MixinInGameOverlayRenderer.class */
@Mixin({class_4603.class})
public class MixinInGameOverlayRenderer {
    @Inject(method = {"renderFireOverlay"}, at = {@At("HEAD")}, cancellable = true)
    private static void onRenderFireOverlay(class_4587 matrices, class_4597 vertexConsumers, CallbackInfo ci) {
        if (class_310.method_1551().field_1724 != null && RemovalsModule.getInstance().isFireOverlay()) {
            ci.cancel();
        }
    }

    @Inject(method = {"renderUnderwaterOverlay"}, at = {@At("HEAD")}, cancellable = true)
    private static void onRenderUnderwaterOverlay(class_310 client, class_4587 matrices, class_4597 vertexConsumers, CallbackInfo ci) {
        if (class_310.method_1551().field_1724 != null && RemovalsModule.getInstance().isWaterOverlay()) {
            ci.cancel();
        }
    }

    @Inject(method = {"renderInWallOverlay"}, at = {@At("HEAD")}, cancellable = true)
    private static void onRenderInWallOverlay(class_1058 sprite, class_4587 matrices, class_4597 vertexConsumers, CallbackInfo ci) {
        if (class_310.method_1551().field_1724 != null && RemovalsModule.getInstance().isInwallOverlay()) {
            ci.cancel();
        }
    }
}
