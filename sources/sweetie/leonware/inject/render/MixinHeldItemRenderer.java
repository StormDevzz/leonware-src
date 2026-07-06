package sweetie.leonware.inject.render;

import com.google.common.base.MoreObjects;
import net.minecraft.class_1268;
import net.minecraft.class_1306;
import net.minecraft.class_1799;
import net.minecraft.class_1806;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_742;
import net.minecraft.class_746;
import net.minecraft.class_759;
import net.minecraft.class_7833;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sweetie.leonware.api.interfaces.IHeldItemRenderer;
import sweetie.leonware.client.features.modules.render.SwingAnimationModule;
import sweetie.leonware.client.features.modules.render.ViewModelModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/render/MixinHeldItemRenderer.class */
@Mixin({class_759.class})
public abstract class MixinHeldItemRenderer implements IHeldItemRenderer {

    @Shadow
    private float field_4053;

    @Shadow
    private float field_4043;

    @Shadow
    private class_1799 field_4047;

    @Shadow
    private float field_4051;

    @Shadow
    private float field_4052;

    @Shadow
    private class_1799 field_4048;

    @Shadow
    protected abstract void method_3228(class_742 class_742Var, float f, float f2, class_1268 class_1268Var, float f3, class_1799 class_1799Var, float f4, class_4587 class_4587Var, class_4597 class_4597Var, int i);

    @Shadow
    static class_759.class_5773 method_33303(class_746 player) {
        return null;
    }

    @Override // sweetie.leonware.api.interfaces.IHeldItemRenderer
    public void leonware$renderShaderItem(float tickDelta, class_4587 matrices, class_4597 vertexConsumers, class_746 player, int light) {
        float f = player.method_6055(tickDelta);
        class_1268 hand = (class_1268) MoreObjects.firstNonNull(player.field_6266, class_1268.field_5808);
        float g = player.method_61414(tickDelta);
        class_759.class_5773 handRenderType = method_33303(player);
        float h = class_3532.method_16439(tickDelta, player.field_3914, player.field_3916);
        float i = class_3532.method_16439(tickDelta, player.field_3931, player.field_3932);
        matrices.method_22907(class_7833.field_40714.rotationDegrees((player.method_5695(tickDelta) - h) * 0.1f));
        matrices.method_22907(class_7833.field_40716.rotationDegrees((player.method_5705(tickDelta) - i) * 0.1f));
        if (handRenderType.field_28387) {
            float j = hand == class_1268.field_5808 ? f : 0.0f;
            float k = 1.0f - class_3532.method_16439(tickDelta, this.field_4053, this.field_4043);
            method_3228(player, tickDelta, g, class_1268.field_5808, j, this.field_4047, k, matrices, vertexConsumers, light);
        }
        if (handRenderType.field_28388) {
            float j2 = hand == class_1268.field_5810 ? f : 0.0f;
            float k2 = 1.0f - class_3532.method_16439(tickDelta, this.field_4051, this.field_4052);
            method_3228(player, tickDelta, g, class_1268.field_5810, j2, this.field_4048, k2, matrices, vertexConsumers, light);
        }
    }

    @Inject(method = {"renderFirstPersonItem"}, at = {@At("HEAD")}, cancellable = true)
    private void onRenderItem(class_742 player, float tickDelta, float pitch, class_1268 hand, float swingProgress, class_1799 item, float equipProgress, class_4587 matrices, class_4597 vertexConsumers, int light, CallbackInfo ci) {
        if (!item.method_7960() && !(item.method_7909() instanceof class_1806)) {
            ci.cancel();
            SwingAnimationModule.getInstance().handleRenderItem(player, tickDelta, pitch, hand, swingProgress, item, equipProgress, matrices, vertexConsumers, light);
        }
    }

    @Inject(method = {"renderArmHoldingItem"}, at = {@At("HEAD")})
    private void onRenderArmHoldingItem(class_4587 matrices, class_4597 vertexConsumers, int light, float equipProgress, float swingProgress, class_1306 arm, CallbackInfo ci) {
        ViewModelModule vm = ViewModelModule.getInstance();
        if (vm.isEnabled() && vm.applyToHand.getValue().booleanValue()) {
            matrices.method_22904(vm.handX.getValue().doubleValue(), vm.handY.getValue().doubleValue(), vm.handZ.getValue().doubleValue());
        }
    }
}
