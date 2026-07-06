package sweetie.leonware.inject.render;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.class_1296;
import net.minecraft.class_1297;
import net.minecraft.class_1427;
import net.minecraft.class_1511;
import net.minecraft.class_1542;
import net.minecraft.class_1588;
import net.minecraft.class_1657;
import net.minecraft.class_279;
import net.minecraft.class_310;
import net.minecraft.class_4597;
import net.minecraft.class_4599;
import net.minecraft.class_4618;
import net.minecraft.class_761;
import net.minecraft.class_9848;
import net.minecraft.class_9909;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sweetie.leonware.client.features.modules.render.shaders.ShadersModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/render/MixinWorldRendererShaders.class */
@Mixin({class_761.class})
public abstract class MixinWorldRendererShaders {

    @Shadow
    @Final
    private class_4599 field_20951;

    @Shadow
    @Final
    private class_310 field_4088;

    @Redirect(method = {"getEntitiesToRender"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;hasOutline(Lnet/minecraft/entity/Entity;)Z"))
    public boolean leonware$modifyHasOutline(class_310 instance, class_1297 entity) {
        if (ShadersModule.getInstance().isEnabled()) {
            return true;
        }
        return instance.method_27022(entity);
    }

    @ModifyArg(method = {"renderEntities"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderEntity(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V"), index = 6)
    public class_4597 leonware$modifyRenderEntity(class_4597 vertexConsumers, @Local class_1297 entity) {
        ShadersModule shaders = ShadersModule.getInstance();
        if (shaders.isEnabled() && leonware$isAllowedEntity(entity)) {
            class_4618 outlineVertexConsumerProvider = this.field_20951.method_23003();
            int color = entity.method_22861();
            outlineVertexConsumerProvider.method_23286(class_9848.method_61327(color), class_9848.method_61329(color), class_9848.method_61331(color), 255);
            return outlineVertexConsumerProvider;
        }
        return vertexConsumers;
    }

    @Inject(method = {"canDrawEntityOutlines"}, at = {@At("HEAD")}, cancellable = true)
    public void leonware$modifyCanDrawEntityOutlines(CallbackInfoReturnable<Boolean> cir) {
        if (ShadersModule.getInstance().isEnabled()) {
            cir.setReturnValue(true);
        }
    }

    @Redirect(method = {"render"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/PostEffectProcessor;render(Lnet/minecraft/client/render/FrameGraphBuilder;IILnet/minecraft/client/gl/PostEffectProcessor$FramebufferSet;)V", ordinal = 0))
    public void leonware$modifyRenderOutlinePostProcessor(class_279 instance, class_9909 builder, int textureWidth, int textureHeight, class_279.class_9961 framebufferSet) {
        if (!ShadersModule.getInstance().isEnabled()) {
            instance.method_62234(builder, textureWidth, textureHeight, framebufferSet);
        }
    }

    @Unique
    private boolean leonware$isAllowedEntity(class_1297 entity) {
        ShadersModule shaders = ShadersModule.getInstance();
        if (shaders.isTargetPlayers() && (entity instanceof class_1657) && entity != this.field_4088.field_1724) {
            return true;
        }
        if (shaders.isTargetSelf() && entity == this.field_4088.field_1724) {
            return true;
        }
        if (shaders.isTargetItems() && (entity instanceof class_1542)) {
            return true;
        }
        if (shaders.isTargetHostiles() && (entity instanceof class_1588)) {
            return true;
        }
        if (shaders.isTargetPassive() && (entity instanceof class_1296) && !(entity instanceof class_1427)) {
            return true;
        }
        if (shaders.isTargetPassive() && (entity instanceof class_1427)) {
            return true;
        }
        return shaders.isTargetCrystals() && (entity instanceof class_1511);
    }
}
