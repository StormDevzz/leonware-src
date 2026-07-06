// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.render;

import sweetie.leonware.api.system.configs.FriendManager;
import net.minecraft.class_310;
import sweetie.leonware.client.features.modules.render.CustomModelModule;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.class_10055;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.class_4597;
import net.minecraft.class_4587;
import net.minecraft.class_970;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.class_572;
import net.minecraft.class_10034;

@Mixin({ class_970.class })
public abstract class MixinArmorFeatureRenderer<S extends class_10034, M extends class_572<S>, A extends class_572<S>>
{
    @Inject(method = { "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/BipedEntityRenderState;FF)V" }, at = { @At("HEAD") }, cancellable = true)
    private void hideArmor(final class_4587 matrices, final class_4597 vertexConsumers, final int light, final S state, final float limbAngle, final float limbDistance, final CallbackInfo ci) {
        if (state instanceof final class_10055 playerState) {
            if (this.shouldHide(playerState)) {
                ci.cancel();
            }
        }
    }
    
    private boolean shouldHide(final class_10055 state) {
        final CustomModelModule mod = CustomModelModule.getInstance();
        if (!mod.isEnabled()) {
            return false;
        }
        if (!mod.model.is("CrazyRabbit") && !mod.model.is("Freddy Bear") && !mod.model.is("Amogus") && !mod.model.is("Leon 2D")) {
            return false;
        }
        final class_310 mc = class_310.method_1551();
        if (mc.field_1724 == null || state.field_53529 == null) {
            return false;
        }
        final boolean isSelf = state.field_53529.equals(mc.method_1548().method_1676());
        return isSelf || (mod.friends.getValue() && FriendManager.getInstance().contains(state.field_53529));
    }
}
