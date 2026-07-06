/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.MoreObjects
 *  net.minecraft.class_1268
 *  net.minecraft.class_1306
 *  net.minecraft.class_1799
 *  net.minecraft.class_1806
 *  net.minecraft.class_3532
 *  net.minecraft.class_4587
 *  net.minecraft.class_4597
 *  net.minecraft.class_742
 *  net.minecraft.class_746
 *  net.minecraft.class_759
 *  net.minecraft.class_759$class_5773
 *  net.minecraft.class_7833
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
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

@Mixin(value={class_759.class})
public abstract class MixinHeldItemRenderer
implements IHeldItemRenderer {
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
    static class_759.class_5773 method_33303(class_746 player) {
        return null;
    }

    @Shadow
    protected abstract void method_3228(class_742 var1, float var2, float var3, class_1268 var4, float var5, class_1799 var6, float var7, class_4587 var8, class_4597 var9, int var10);

    @Override
    public void leonware$renderShaderItem(float tickDelta, class_4587 matrices, class_4597 vertexConsumers, class_746 player, int light) {
        float k;
        float j;
        float f = player.method_6055(tickDelta);
        class_1268 hand = (class_1268)MoreObjects.firstNonNull((Object)player.field_6266, (Object)class_1268.field_5808);
        float g = player.method_61414(tickDelta);
        class_759.class_5773 handRenderType = MixinHeldItemRenderer.method_33303(player);
        float h = class_3532.method_16439((float)tickDelta, (float)player.field_3914, (float)player.field_3916);
        float i = class_3532.method_16439((float)tickDelta, (float)player.field_3931, (float)player.field_3932);
        matrices.method_22907(class_7833.field_40714.rotationDegrees((player.method_5695(tickDelta) - h) * 0.1f));
        matrices.method_22907(class_7833.field_40716.rotationDegrees((player.method_5705(tickDelta) - i) * 0.1f));
        if (handRenderType.field_28387) {
            j = hand == class_1268.field_5808 ? f : 0.0f;
            k = 1.0f - class_3532.method_16439((float)tickDelta, (float)this.field_4053, (float)this.field_4043);
            this.method_3228((class_742)player, tickDelta, g, class_1268.field_5808, j, this.field_4047, k, matrices, vertexConsumers, light);
        }
        if (handRenderType.field_28388) {
            j = hand == class_1268.field_5810 ? f : 0.0f;
            k = 1.0f - class_3532.method_16439((float)tickDelta, (float)this.field_4051, (float)this.field_4052);
            this.method_3228((class_742)player, tickDelta, g, class_1268.field_5810, j, this.field_4048, k, matrices, vertexConsumers, light);
        }
    }

    @Inject(method={"renderFirstPersonItem"}, at={@At(value="HEAD")}, cancellable=true)
    private void onRenderItem(class_742 player, float tickDelta, float pitch, class_1268 hand, float swingProgress, class_1799 item, float equipProgress, class_4587 matrices, class_4597 vertexConsumers, int light, CallbackInfo ci) {
        if (!item.method_7960() && !(item.method_7909() instanceof class_1806)) {
            ci.cancel();
            SwingAnimationModule.getInstance().handleRenderItem(player, tickDelta, pitch, hand, swingProgress, item, equipProgress, matrices, vertexConsumers, light);
        }
    }

    @Inject(method={"renderArmHoldingItem"}, at={@At(value="HEAD")})
    private void onRenderArmHoldingItem(class_4587 matrices, class_4597 vertexConsumers, int light, float equipProgress, float swingProgress, class_1306 arm, CallbackInfo ci) {
        ViewModelModule vm = ViewModelModule.getInstance();
        if (!vm.isEnabled() || !((Boolean)vm.applyToHand.getValue()).booleanValue()) {
            return;
        }
        matrices.method_22904(((Float)vm.handX.getValue()).doubleValue(), ((Float)vm.handY.getValue()).doubleValue(), ((Float)vm.handZ.getValue()).doubleValue());
    }
}

