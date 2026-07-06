// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.render;

import sweetie.leonware.api.system.configs.FriendManager;
import net.minecraft.class_310;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import sweetie.leonware.client.features.modules.render.CustomModelModule;
import net.minecraft.class_10055;
import sweetie.leonware.client.features.modules.render.CustomElytraModule;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.class_4597;
import net.minecraft.class_4587;
import org.spongepowered.asm.mixin.Unique;
import net.minecraft.class_979;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.class_583;
import net.minecraft.class_10034;

@Mixin({ class_979.class })
public abstract class MixinElytraFeatureRenderer<S extends class_10034, M extends class_583<S>>
{
    @Unique
    private boolean leonware$pushed;
    
    public MixinElytraFeatureRenderer() {
        this.leonware$pushed = false;
    }
    
    @Inject(method = { "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/BipedEntityRenderState;FF)V" }, at = { @At("HEAD") })
    private void preRenderElytra(final class_4587 matrices, final class_4597 vertexConsumers, final int light, final S state, final float limbAngle, final float limbDistance, final CallbackInfo ci) {
        this.leonware$pushed = false;
        final CustomElytraModule elytraMod = CustomElytraModule.getInstance();
        if (!elytraMod.isEnabled()) {
            return;
        }
        if (!(state instanceof class_10055)) {
            return;
        }
        final class_10055 playerState = (class_10055)state;
        final CustomModelModule customMod = CustomModelModule.getInstance();
        final boolean isCustomModelTarget = customMod.isEnabled() && this.leonware$isTarget(playerState, customMod);
        if (!isCustomModelTarget) {
            return;
        }
        float scale;
        float x;
        float y;
        float z;
        if (elytraMod.customModelElytra.getValue()) {
            scale = elytraMod.customScale.getValue();
            x = elytraMod.customX.getValue();
            y = elytraMod.customY.getValue();
            z = elytraMod.customZ.getValue();
        }
        else {
            scale = elytraMod.elytraScale.getValue();
            x = elytraMod.elytraX.getValue();
            y = elytraMod.elytraY.getValue();
            z = elytraMod.elytraZ.getValue();
        }
        matrices.method_22903();
        matrices.method_46416(x, y, z);
        matrices.method_22905(scale, scale, scale);
        this.leonware$pushed = true;
    }
    
    @Inject(method = { "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/BipedEntityRenderState;FF)V" }, at = { @At("RETURN") })
    private void postRenderElytra(final class_4587 matrices, final class_4597 vertexConsumers, final int light, final S state, final float limbAngle, final float limbDistance, final CallbackInfo ci) {
        if (this.leonware$pushed) {
            matrices.method_22909();
            this.leonware$pushed = false;
        }
    }
    
    @Unique
    private boolean leonware$isTarget(final class_10055 state, final CustomModelModule mod) {
        final class_310 mc = class_310.method_1551();
        return mc.field_1724 != null && state.field_53529 != null && (state.field_53529.equals(mc.method_1548().method_1676()) || (mod.friends.getValue() && FriendManager.getInstance().contains(state.field_53529)));
    }
}
