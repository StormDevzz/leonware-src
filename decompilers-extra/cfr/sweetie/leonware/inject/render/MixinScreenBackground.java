/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_310
 *  net.minecraft.class_332
 *  net.minecraft.class_437
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package sweetie.leonware.inject.render;

import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_437;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sweetie.leonware.client.features.modules.render.RemovalsModule;

@Mixin(value={class_437.class})
public abstract class MixinScreenBackground {
    @Inject(method={"renderInGameBackground"}, at={@At(value="HEAD")}, cancellable=true)
    private void leonware$onRenderInGameBackground(class_332 context, CallbackInfo ci) {
        if (RemovalsModule.getInstance().isGuiBackground()) {
            ci.cancel();
        }
    }

    @Inject(method={"renderBackground(Lnet/minecraft/client/gui/DrawContext;IIF)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void leonware$onRenderBackground(class_332 context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (RemovalsModule.getInstance().isGuiBackground() && class_310.method_1551().field_1687 != null) {
            ci.cancel();
        }
    }
}

