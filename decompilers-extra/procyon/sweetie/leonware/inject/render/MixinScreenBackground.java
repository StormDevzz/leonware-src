// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.render;

import net.minecraft.class_310;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import sweetie.leonware.client.features.modules.render.RemovalsModule;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.class_332;
import net.minecraft.class_437;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_437.class })
public abstract class MixinScreenBackground
{
    @Inject(method = { "renderInGameBackground" }, at = { @At("HEAD") }, cancellable = true)
    private void leonware$onRenderInGameBackground(final class_332 context, final CallbackInfo ci) {
        if (RemovalsModule.getInstance().isGuiBackground()) {
            ci.cancel();
        }
    }
    
    @Inject(method = { "renderBackground(Lnet/minecraft/client/gui/DrawContext;IIF)V" }, at = { @At("HEAD") }, cancellable = true)
    private void leonware$onRenderBackground(final class_332 context, final int mouseX, final int mouseY, final float delta, final CallbackInfo ci) {
        if (RemovalsModule.getInstance().isGuiBackground() && class_310.method_1551().field_1687 != null) {
            ci.cancel();
        }
    }
}
