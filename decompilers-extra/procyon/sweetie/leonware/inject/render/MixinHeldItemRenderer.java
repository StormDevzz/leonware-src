// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.render;

import sweetie.leonware.client.features.modules.render.ViewModelModule;
import net.minecraft.class_1306;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import sweetie.leonware.client.features.modules.render.SwingAnimationModule;
import net.minecraft.class_1806;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.class_7833;
import net.minecraft.class_3532;
import com.google.common.base.MoreObjects;
import net.minecraft.class_4597;
import net.minecraft.class_4587;
import net.minecraft.class_1268;
import net.minecraft.class_742;
import net.minecraft.class_746;
import net.minecraft.class_1799;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.class_759;
import org.spongepowered.asm.mixin.Mixin;
import sweetie.leonware.api.interfaces.IHeldItemRenderer;

@Mixin({ class_759.class })
public abstract class MixinHeldItemRenderer implements IHeldItemRenderer
{
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
    static class_759.class_5773 method_33303(final class_746 player) {
        return null;
    }
    
    @Shadow
    protected abstract void method_3228(final class_742 p0, final float p1, final float p2, final class_1268 p3, final float p4, final class_1799 p5, final float p6, final class_4587 p7, final class_4597 p8, final int p9);
    
    @Override
    public void leonware$renderShaderItem(final float tickDelta, final class_4587 matrices, final class_4597 vertexConsumers, final class_746 player, final int light) {
        final float f = player.method_6055(tickDelta);
        final class_1268 hand = (class_1268)MoreObjects.firstNonNull((Object)player.field_6266, (Object)class_1268.field_5808);
        final float g = player.method_61414(tickDelta);
        final class_759.class_5773 handRenderType = method_33303(player);
        final float h = class_3532.method_16439(tickDelta, player.field_3914, player.field_3916);
        final float i = class_3532.method_16439(tickDelta, player.field_3931, player.field_3932);
        matrices.method_22907(class_7833.field_40714.rotationDegrees((player.method_5695(tickDelta) - h) * 0.1f));
        matrices.method_22907(class_7833.field_40716.rotationDegrees((player.method_5705(tickDelta) - i) * 0.1f));
        if (handRenderType.field_28387) {
            final float j = (hand == class_1268.field_5808) ? f : 0.0f;
            final float k = 1.0f - class_3532.method_16439(tickDelta, this.field_4053, this.field_4043);
            this.method_3228((class_742)player, tickDelta, g, class_1268.field_5808, j, this.field_4047, k, matrices, vertexConsumers, light);
        }
        if (handRenderType.field_28388) {
            final float j = (hand == class_1268.field_5810) ? f : 0.0f;
            final float k = 1.0f - class_3532.method_16439(tickDelta, this.field_4051, this.field_4052);
            this.method_3228((class_742)player, tickDelta, g, class_1268.field_5810, j, this.field_4048, k, matrices, vertexConsumers, light);
        }
    }
    
    @Inject(method = { "renderFirstPersonItem" }, at = { @At("HEAD") }, cancellable = true)
    private void onRenderItem(final class_742 player, final float tickDelta, final float pitch, final class_1268 hand, final float swingProgress, final class_1799 item, final float equipProgress, final class_4587 matrices, final class_4597 vertexConsumers, final int light, final CallbackInfo ci) {
        if (!item.method_7960() && !(item.method_7909() instanceof class_1806)) {
            ci.cancel();
            SwingAnimationModule.getInstance().handleRenderItem(player, tickDelta, pitch, hand, swingProgress, item, equipProgress, matrices, vertexConsumers, light);
        }
    }
    
    @Inject(method = { "renderArmHoldingItem" }, at = { @At("HEAD") })
    private void onRenderArmHoldingItem(final class_4587 matrices, final class_4597 vertexConsumers, final int light, final float equipProgress, final float swingProgress, final class_1306 arm, final CallbackInfo ci) {
        final ViewModelModule vm = ViewModelModule.getInstance();
        if (!vm.isEnabled() || !vm.applyToHand.getValue()) {
            return;
        }
        matrices.method_22904((double)vm.handX.getValue(), (double)vm.handY.getValue(), (double)vm.handZ.getValue());
    }
}
