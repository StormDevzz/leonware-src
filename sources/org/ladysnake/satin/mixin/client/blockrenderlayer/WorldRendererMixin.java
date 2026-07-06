package org.ladysnake.satin.mixin.client.blockrenderlayer;

import net.minecraft.class_1921;
import net.minecraft.class_276;
import net.minecraft.class_3695;
import net.minecraft.class_4184;
import net.minecraft.class_4604;
import net.minecraft.class_761;
import net.minecraft.class_9779;
import net.minecraft.class_9925;
import net.minecraft.class_9958;
import org.joml.Matrix4f;
import org.ladysnake.satin.impl.BlockRenderLayerRegistry;
import org.newsclub.net.unix.AFVSOCKSocketAddress;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/mixin/client/blockrenderlayer/WorldRendererMixin.class */
@Mixin({class_761.class})
public abstract class WorldRendererMixin {
    @Shadow
    protected abstract void method_3251(class_1921 class_1921Var, double d, double d2, double d3, Matrix4f matrix4f, Matrix4f matrix4f2);

    @Inject(method = {"method_62214"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderLayer(Lnet/minecraft/client/render/RenderLayer;DDDLorg/joml/Matrix4f;Lorg/joml/Matrix4f;)V", ordinal = AFVSOCKSocketAddress.VMADDR_CID_HOST, shift = At.Shift.AFTER)})
    private void renderCustom(class_9958 fog, class_9779 renderTickCounter, class_4184 camera, class_3695 profiler, Matrix4f positionMatrix, Matrix4f projectionMatrix, class_9925<class_276> itemEntityFramebuffer, class_9925<class_276> mainFramebuffer, class_9925<class_276> weatherFramebuffer, class_9925<class_276> entityOutlineFramebuffer, boolean renderBlockOutline, class_4604 frustum, class_9925<class_276> translucentFramebuffer, CallbackInfo ci) {
        for (class_1921 layer : BlockRenderLayerRegistry.INSTANCE.getLayers()) {
            method_3251(layer, camera.method_19326().field_1352, camera.method_19326().field_1351, camera.method_19326().field_1350, positionMatrix, projectionMatrix);
        }
    }
}
