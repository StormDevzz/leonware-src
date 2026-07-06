// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.render;

import sweetie.leonware.api.system.configs.FriendManager;
import net.minecraft.class_310;
import net.minecraft.class_7833;
import net.minecraft.class_1306;
import net.minecraft.class_10444;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import sweetie.leonware.client.features.modules.render.CustomModelModule;
import net.minecraft.class_10055;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.class_10426;
import net.minecraft.class_4597;
import net.minecraft.class_4587;
import org.spongepowered.asm.mixin.Unique;
import net.minecraft.class_989;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_989.class })
public class MixinHeldItemFeatureRenderer
{
    @Unique
    private boolean leonware$shouldAdjust;
    
    public MixinHeldItemFeatureRenderer() {
        this.leonware$shouldAdjust = false;
    }
    
    @Inject(method = { "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/ArmedEntityRenderState;FF)V" }, at = { @At("HEAD") }, cancellable = true)
    private void onRenderHead(final class_4587 matrices, final class_4597 vertexConsumers, final int light, final class_10426 state, final float limbAngle, final float limbDistance, final CallbackInfo ci) {
        this.leonware$shouldAdjust = false;
        if (!(state instanceof class_10055)) {
            return;
        }
        final class_10055 playerState = (class_10055)state;
        final CustomModelModule mod = CustomModelModule.getInstance();
        if (!mod.isEnabled()) {
            return;
        }
        if (mod.model.is("Amogus") && mod.amogusHideItems.getValue() && this.leonware$isCustomModelTarget(playerState)) {
            ci.cancel();
            return;
        }
        if (mod.model.is("Leon 2D") && mod.leonHideArms.getValue() && this.leonware$isCustomModelTarget(playerState)) {
            ci.cancel();
            return;
        }
        if (this.leonware$shouldAdjustItems(playerState)) {
            this.leonware$shouldAdjust = true;
            matrices.method_22903();
            matrices.method_46416(0.0f, 0.3f, 0.0f);
        }
    }
    
    @Inject(method = { "renderItem" }, at = { @At("HEAD") })
    private void onRenderItem(final class_10426 entityState, final class_10444 itemState, final class_1306 arm, final class_4587 matrices, final class_4597 vertexConsumers, final int light, final CallbackInfo ci) {
        if (!this.leonware$shouldAdjust) {
            return;
        }
        final CustomModelModule mod = CustomModelModule.getInstance();
        if (mod.model.is("Freddy Bear")) {
            if (arm == class_1306.field_6183) {
                matrices.method_46416((float)mod.rightX.getValue(), (float)mod.rightY.getValue(), (float)mod.rightZ.getValue());
                matrices.method_22907(class_7833.field_40718.rotationDegrees((float)mod.rightRot.getValue()));
            }
            else {
                matrices.method_46416((float)mod.leftX.getValue(), (float)mod.leftY.getValue(), (float)mod.leftZ.getValue());
                matrices.method_22907(class_7833.field_40718.rotationDegrees((float)mod.leftRot.getValue()));
            }
        }
        else if (mod.model.is("CrazyRabbit")) {
            if (arm == class_1306.field_6183) {
                matrices.method_46416(-0.15f, 0.0f, 0.0f);
            }
            else {
                matrices.method_46416(0.3f, 0.0f, 0.0f);
            }
        }
    }
    
    @Inject(method = { "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/ArmedEntityRenderState;FF)V" }, at = { @At("RETURN") })
    private void onRenderReturn(final class_4587 matrices, final class_4597 vertexConsumers, final int light, final class_10426 state, final float limbAngle, final float limbDistance, final CallbackInfo ci) {
        if (this.leonware$shouldAdjust) {
            matrices.method_22909();
            this.leonware$shouldAdjust = false;
        }
    }
    
    @Unique
    private boolean leonware$shouldAdjustItems(final class_10055 state) {
        final CustomModelModule mod = CustomModelModule.getInstance();
        return mod.isEnabled() && (mod.model.is("CrazyRabbit") || mod.model.is("Freddy Bear")) && this.leonware$isCustomModelTarget(state);
    }
    
    @Unique
    private boolean leonware$isCustomModelTarget(final class_10055 state) {
        final class_310 mc = class_310.method_1551();
        if (mc.field_1724 == null || state.field_53529 == null) {
            return false;
        }
        final CustomModelModule mod = CustomModelModule.getInstance();
        final boolean isSelf = state.field_53529.equals(mc.method_1548().method_1676());
        return isSelf || (mod.friends.getValue() && FriendManager.getInstance().contains(state.field_53529));
    }
}
