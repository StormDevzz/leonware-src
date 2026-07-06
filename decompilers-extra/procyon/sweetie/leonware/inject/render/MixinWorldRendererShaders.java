// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.render;

import org.spongepowered.asm.mixin.Unique;
import net.minecraft.class_1511;
import net.minecraft.class_1427;
import net.minecraft.class_1296;
import net.minecraft.class_1588;
import net.minecraft.class_1542;
import net.minecraft.class_1657;
import net.minecraft.class_9909;
import net.minecraft.class_279;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import net.minecraft.class_4618;
import net.minecraft.class_9848;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.class_4597;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import sweetie.leonware.client.features.modules.render.shaders.ShadersModule;
import net.minecraft.class_1297;
import net.minecraft.class_310;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.class_4599;
import net.minecraft.class_761;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_761.class })
public abstract class MixinWorldRendererShaders
{
    @Shadow
    @Final
    private class_4599 field_20951;
    @Shadow
    @Final
    private class_310 field_4088;
    
    @Redirect(method = { "getEntitiesToRender" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;hasOutline(Lnet/minecraft/entity/Entity;)Z"))
    public boolean leonware$modifyHasOutline(final class_310 instance, final class_1297 entity) {
        return ShadersModule.getInstance().isEnabled() || instance.method_27022(entity);
    }
    
    @ModifyArg(method = { "renderEntities" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderEntity(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V"), index = 6)
    public class_4597 leonware$modifyRenderEntity(final class_4597 vertexConsumers, @Local final class_1297 entity) {
        final ShadersModule shaders = ShadersModule.getInstance();
        if (shaders.isEnabled() && this.leonware$isAllowedEntity(entity)) {
            final class_4618 outlineVertexConsumerProvider = this.field_20951.method_23003();
            final int color = entity.method_22861();
            outlineVertexConsumerProvider.method_23286(class_9848.method_61327(color), class_9848.method_61329(color), class_9848.method_61331(color), 255);
            return (class_4597)outlineVertexConsumerProvider;
        }
        return vertexConsumers;
    }
    
    @Inject(method = { "canDrawEntityOutlines" }, at = { @At("HEAD") }, cancellable = true)
    public void leonware$modifyCanDrawEntityOutlines(final CallbackInfoReturnable<Boolean> cir) {
        if (ShadersModule.getInstance().isEnabled()) {
            cir.setReturnValue((Object)true);
        }
    }
    
    @Redirect(method = { "render" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/PostEffectProcessor;render(Lnet/minecraft/client/render/FrameGraphBuilder;IILnet/minecraft/client/gl/PostEffectProcessor$FramebufferSet;)V", ordinal = 0))
    public void leonware$modifyRenderOutlinePostProcessor(final class_279 instance, final class_9909 builder, final int textureWidth, final int textureHeight, final class_279.class_9961 framebufferSet) {
        if (!ShadersModule.getInstance().isEnabled()) {
            instance.method_62234(builder, textureWidth, textureHeight, framebufferSet);
        }
    }
    
    @Unique
    private boolean leonware$isAllowedEntity(final class_1297 entity) {
        final ShadersModule shaders = ShadersModule.getInstance();
        return (shaders.isTargetPlayers() && entity instanceof class_1657 && entity != this.field_4088.field_1724) || (shaders.isTargetSelf() && entity == this.field_4088.field_1724) || (shaders.isTargetItems() && entity instanceof class_1542) || (shaders.isTargetHostiles() && entity instanceof class_1588) || (shaders.isTargetPassive() && entity instanceof class_1296 && !(entity instanceof class_1427)) || (shaders.isTargetPassive() && entity instanceof class_1427) || (shaders.isTargetCrystals() && entity instanceof class_1511);
    }
}
