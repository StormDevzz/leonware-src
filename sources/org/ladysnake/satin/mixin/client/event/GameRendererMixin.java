package org.ladysnake.satin.mixin.client.event;

import javax.annotation.Nullable;
import net.minecraft.class_1297;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_757;
import net.minecraft.class_9779;
import net.minecraft.class_9960;
import org.ladysnake.satin.api.event.PickEntityShaderCallback;
import org.ladysnake.satin.api.event.ShaderEffectRenderCallback;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/mixin/client/event/GameRendererMixin.class */
@Mixin({class_757.class})
public abstract class GameRendererMixin {

    @Shadow
    @Nullable
    private class_2960 field_53898;

    @Shadow
    @Final
    private class_310 field_4015;

    @Shadow
    protected abstract void method_62904(class_2960 class_2960Var);

    @Inject(at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;drawEntityOutlinesFramebuffer()V", shift = At.Shift.AFTER)}, method = {"render"})
    private void hookShaderRender(class_9779 tickCounter, boolean tick, CallbackInfo ci) {
        ((ShaderEffectRenderCallback) ShaderEffectRenderCallback.EVENT.invoker()).renderShaderEffects(tickCounter.method_60637(tick));
    }

    @Inject(method = {"onCameraEntitySet"}, at = {@At("RETURN")}, require = 0)
    private void useCustomEntityShader(@Nullable class_1297 entity, CallbackInfo info) {
        if (this.field_53898 == null) {
            ((PickEntityShaderCallback) PickEntityShaderCallback.EVENT.invoker()).pickEntityShader(entity, loc -> {
                method_62904(loc);
            }, () -> {
                return this.field_4015.method_62887().method_62941(this.field_53898, class_9960.field_53902);
            });
        }
    }
}
