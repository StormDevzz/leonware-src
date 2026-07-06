// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.render;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import com.mojang.blaze3d.systems.RenderSystem;
import sweetie.leonware.client.features.modules.render.AmbienceModule;
import net.minecraft.class_1297;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.class_4597;
import net.minecraft.class_4587;
import net.minecraft.class_898;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_898.class })
public class MixinEntityRenderDispatcher
{
    @Inject(method = { "render(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V" }, at = { @At("HEAD") })
    private <E extends class_1297> void onRenderHead(final E entity, final double x, final double y, final double z, final float tickDelta, final class_4587 matrices, final class_4597 vertexConsumers, final int light, final CallbackInfo ci) {
        final AmbienceModule module = AmbienceModule.getInstance();
        if (module.isEntityTintActive()) {
            final float[] tint = module.getEntityTint();
            RenderSystem.setShaderColor(tint[0], tint[1], tint[2], tint[3]);
        }
    }
    
    @Inject(method = { "render(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V" }, at = { @At("RETURN") })
    private <E extends class_1297> void onRenderReturn(final E entity, final double x, final double y, final double z, final float tickDelta, final class_4587 matrices, final class_4597 vertexConsumers, final int light, final CallbackInfo ci) {
        if (AmbienceModule.getInstance().isEntityTintActive()) {
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
}
