/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_10034
 *  net.minecraft.class_10055
 *  net.minecraft.class_310
 *  net.minecraft.class_4587
 *  net.minecraft.class_4597
 *  net.minecraft.class_572
 *  net.minecraft.class_970
 *  org.spongepowered.asm.mixin.Mixin
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
import net.minecraft.class_572;
import net.minecraft.class_970;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sweetie.leonware.api.system.configs.FriendManager;
import sweetie.leonware.client.features.modules.render.CustomModelModule;

@Mixin(value={class_970.class})
public abstract class MixinArmorFeatureRenderer<S extends class_10034, M extends class_572<S>, A extends class_572<S>> {
    @Inject(method={"render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/BipedEntityRenderState;FF)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void hideArmor(class_4587 matrices, class_4597 vertexConsumers, int light, S state, float limbAngle, float limbDistance, CallbackInfo ci) {
        class_10055 playerState;
        if (state instanceof class_10055 && this.shouldHide(playerState = (class_10055)state)) {
            ci.cancel();
        }
    }

    private boolean shouldHide(class_10055 state) {
        CustomModelModule mod = CustomModelModule.getInstance();
        if (!mod.isEnabled()) {
            return false;
        }
        if (!(mod.model.is("CrazyRabbit") || mod.model.is("Freddy Bear") || mod.model.is("Amogus") || mod.model.is("Leon 2D"))) {
            return false;
        }
        class_310 mc = class_310.method_1551();
        if (mc.field_1724 == null || state.field_53529 == null) {
            return false;
        }
        boolean isSelf = state.field_53529.equals(mc.method_1548().method_1676());
        if (isSelf) {
            return true;
        }
        return (Boolean)mod.friends.getValue() != false && FriendManager.getInstance().contains(state.field_53529);
    }
}

