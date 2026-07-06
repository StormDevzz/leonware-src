package sweetie.leonware.inject.render;

import net.minecraft.class_10055;
import net.minecraft.class_10426;
import net.minecraft.class_10444;
import net.minecraft.class_1306;
import net.minecraft.class_310;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_7833;
import net.minecraft.class_989;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sweetie.leonware.api.system.configs.FriendManager;
import sweetie.leonware.client.features.modules.render.CustomModelModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/render/MixinHeldItemFeatureRenderer.class */
@Mixin({class_989.class})
public class MixinHeldItemFeatureRenderer {

    @Unique
    private boolean leonware$shouldAdjust = false;

    @Inject(method = {"render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/ArmedEntityRenderState;FF)V"}, at = {@At("HEAD")}, cancellable = true)
    private void onRenderHead(class_4587 matrices, class_4597 vertexConsumers, int light, class_10426 state, float limbAngle, float limbDistance, CallbackInfo ci) {
        this.leonware$shouldAdjust = false;
        if (state instanceof class_10055) {
            class_10055 playerState = (class_10055) state;
            CustomModelModule mod = CustomModelModule.getInstance();
            if (mod.isEnabled()) {
                if (mod.model.is("Amogus") && mod.amogusHideItems.getValue().booleanValue() && leonware$isCustomModelTarget(playerState)) {
                    ci.cancel();
                    return;
                }
                if (mod.model.is("Leon 2D") && mod.leonHideArms.getValue().booleanValue() && leonware$isCustomModelTarget(playerState)) {
                    ci.cancel();
                } else if (leonware$shouldAdjustItems(playerState)) {
                    this.leonware$shouldAdjust = true;
                    matrices.method_22903();
                    matrices.method_46416(0.0f, 0.3f, 0.0f);
                }
            }
        }
    }

    @Inject(method = {"renderItem"}, at = {@At("HEAD")})
    private void onRenderItem(class_10426 entityState, class_10444 itemState, class_1306 arm, class_4587 matrices, class_4597 vertexConsumers, int light, CallbackInfo ci) {
        if (this.leonware$shouldAdjust) {
            CustomModelModule mod = CustomModelModule.getInstance();
            if (mod.model.is("Freddy Bear")) {
                if (arm == class_1306.field_6183) {
                    matrices.method_46416(mod.rightX.getValue().floatValue(), mod.rightY.getValue().floatValue(), mod.rightZ.getValue().floatValue());
                    matrices.method_22907(class_7833.field_40718.rotationDegrees(mod.rightRot.getValue().floatValue()));
                    return;
                } else {
                    matrices.method_46416(mod.leftX.getValue().floatValue(), mod.leftY.getValue().floatValue(), mod.leftZ.getValue().floatValue());
                    matrices.method_22907(class_7833.field_40718.rotationDegrees(mod.leftRot.getValue().floatValue()));
                    return;
                }
            }
            if (mod.model.is("CrazyRabbit")) {
                if (arm == class_1306.field_6183) {
                    matrices.method_46416(-0.15f, 0.0f, 0.0f);
                } else {
                    matrices.method_46416(0.3f, 0.0f, 0.0f);
                }
            }
        }
    }

    @Inject(method = {"render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/ArmedEntityRenderState;FF)V"}, at = {@At("RETURN")})
    private void onRenderReturn(class_4587 matrices, class_4597 vertexConsumers, int light, class_10426 state, float limbAngle, float limbDistance, CallbackInfo ci) {
        if (this.leonware$shouldAdjust) {
            matrices.method_22909();
            this.leonware$shouldAdjust = false;
        }
    }

    @Unique
    private boolean leonware$shouldAdjustItems(class_10055 state) {
        CustomModelModule mod = CustomModelModule.getInstance();
        if (!mod.isEnabled()) {
            return false;
        }
        if (mod.model.is("CrazyRabbit") || mod.model.is("Freddy Bear")) {
            return leonware$isCustomModelTarget(state);
        }
        return false;
    }

    @Unique
    private boolean leonware$isCustomModelTarget(class_10055 state) {
        class_310 mc = class_310.method_1551();
        if (mc.field_1724 == null || state.field_53529 == null) {
            return false;
        }
        CustomModelModule mod = CustomModelModule.getInstance();
        boolean isSelf = state.field_53529.equals(mc.method_1548().method_1676());
        if (isSelf) {
            return true;
        }
        return mod.friends.getValue().booleanValue() && FriendManager.getInstance().contains(state.field_53529);
    }
}
