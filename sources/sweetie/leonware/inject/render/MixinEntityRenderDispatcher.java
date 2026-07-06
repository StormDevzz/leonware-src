package sweetie.leonware.inject.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_1297;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_898;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sweetie.leonware.client.features.modules.render.AmbienceModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/render/MixinEntityRenderDispatcher.class */
@Mixin({class_898.class})
public class MixinEntityRenderDispatcher {
    @Inject(method = {"render(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"}, at = {@At("HEAD")})
    private <E extends class_1297> void onRenderHead(E entity, double x, double y, double z, float tickDelta, class_4587 matrices, class_4597 vertexConsumers, int light, CallbackInfo ci) {
        AmbienceModule module = AmbienceModule.getInstance();
        if (module.isEntityTintActive()) {
            float[] tint = module.getEntityTint();
            RenderSystem.setShaderColor(tint[0], tint[1], tint[2], tint[3]);
        }
    }

    @Inject(method = {"render(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"}, at = {@At("RETURN")})
    private <E extends class_1297> void onRenderReturn(E entity, double x, double y, double z, float tickDelta, class_4587 matrices, class_4597 vertexConsumers, int light, CallbackInfo ci) {
        if (AmbienceModule.getInstance().isEntityTintActive()) {
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
}
