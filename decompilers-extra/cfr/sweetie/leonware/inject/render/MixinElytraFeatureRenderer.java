/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_10034
 *  net.minecraft.class_10055
 *  net.minecraft.class_310
 *  net.minecraft.class_4587
 *  net.minecraft.class_4597
 *  net.minecraft.class_583
 *  net.minecraft.class_979
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package sweetie.leonware.inject.render;

import net.minecraft.class_10034;
import net.minecraft.class_10055;
import net.minecraft.class_310;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_583;
import net.minecraft.class_979;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sweetie.leonware.api.system.configs.FriendManager;
import sweetie.leonware.client.features.modules.render.CustomElytraModule;
import sweetie.leonware.client.features.modules.render.CustomModelModule;

@Mixin(value={class_979.class})
public abstract class MixinElytraFeatureRenderer<S extends class_10034, M extends class_583<S>> {
    @Unique
    private boolean leonware$pushed = false;

    @Inject(method={"render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/BipedEntityRenderState;FF)V"}, at={@At(value="HEAD")})
    private void preRenderElytra(class_4587 matrices, class_4597 vertexConsumers, int light, S state, float limbAngle, float limbDistance, CallbackInfo ci) {
        float z;
        float y;
        float x;
        float scale;
        boolean isCustomModelTarget;
        this.leonware$pushed = false;
        CustomElytraModule elytraMod = CustomElytraModule.getInstance();
        if (!elytraMod.isEnabled()) {
            return;
        }
        if (!(state instanceof class_10055)) {
            return;
        }
        class_10055 playerState = (class_10055)state;
        CustomModelModule customMod = CustomModelModule.getInstance();
        boolean bl = isCustomModelTarget = customMod.isEnabled() && this.leonware$isTarget(playerState, customMod);
        if (!isCustomModelTarget) {
            return;
        }
        if (((Boolean)elytraMod.customModelElytra.getValue()).booleanValue()) {
            scale = ((Float)elytraMod.customScale.getValue()).floatValue();
            x = ((Float)elytraMod.customX.getValue()).floatValue();
            y = ((Float)elytraMod.customY.getValue()).floatValue();
            z = ((Float)elytraMod.customZ.getValue()).floatValue();
        } else {
            scale = ((Float)elytraMod.elytraScale.getValue()).floatValue();
            x = ((Float)elytraMod.elytraX.getValue()).floatValue();
            y = ((Float)elytraMod.elytraY.getValue()).floatValue();
            z = ((Float)elytraMod.elytraZ.getValue()).floatValue();
        }
        matrices.method_22903();
        matrices.method_46416(x, y, z);
        matrices.method_22905(scale, scale, scale);
        this.leonware$pushed = true;
    }

    @Inject(method={"render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/BipedEntityRenderState;FF)V"}, at={@At(value="RETURN")})
    private void postRenderElytra(class_4587 matrices, class_4597 vertexConsumers, int light, S state, float limbAngle, float limbDistance, CallbackInfo ci) {
        if (this.leonware$pushed) {
            matrices.method_22909();
            this.leonware$pushed = false;
        }
    }

    @Unique
    private boolean leonware$isTarget(class_10055 state, CustomModelModule mod) {
        class_310 mc = class_310.method_1551();
        if (mc.field_1724 == null || state.field_53529 == null) {
            return false;
        }
        return state.field_53529.equals(mc.method_1548().method_1676()) || (Boolean)mod.friends.getValue() != false && FriendManager.getInstance().contains(state.field_53529);
    }
}

