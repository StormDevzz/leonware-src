/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  net.minecraft.class_1922
 *  net.minecraft.class_243
 *  net.minecraft.class_3532
 *  net.minecraft.class_4184
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.ModifyArgs
 *  org.spongepowered.asm.mixin.injection.Redirect
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 *  org.spongepowered.asm.mixin.injection.invoke.arg.Args
 */
package sweetie.leonware.inject.client;

import net.minecraft.class_1297;
import net.minecraft.class_1922;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.minecraft.class_4184;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import sweetie.leonware.api.system.backend.SharedClass;
import sweetie.leonware.client.features.modules.render.BabyModelModule;
import sweetie.leonware.client.features.modules.render.CameraClipModule;

@Mixin(value={class_4184.class})
public abstract class MixinCamera {
    @Shadow
    private boolean field_18719;
    @Unique
    private float currentTickDelta = 1.0f;
    @Unique
    private class_243 smoothPos;

    @Shadow
    protected abstract float method_19318(float var1);

    @Shadow
    protected abstract void method_19324(float var1, float var2, float var3);

    @Inject(method={"update"}, at={@At(value="HEAD")})
    private void captureTickDelta(class_1922 area, class_1297 focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        this.currentTickDelta = tickDelta;
    }

    @Redirect(method={"update"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/render/Camera;moveBy(FFF)V"))
    private void cameraClipMoveByHook(class_4184 instance, float x, float y, float z) {
        CameraClipModule mod = CameraClipModule.getInstance();
        if (!this.field_18719 || !mod.isEnabled()) {
            this.method_19324(x, y, z);
            return;
        }
        float finalDistance = ((Float)mod.distance.getValue()).floatValue() * ((Float)mod.zoom.getValue()).floatValue();
        this.method_19324(-this.method_19318(finalDistance), 0.0f, 0.0f);
    }

    @ModifyArgs(method={"update"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/render/Camera;setPos(DDD)V"))
    private void cameraClipSetPosHook(Args args) {
        boolean shouldSmooth;
        CameraClipModule mod = CameraClipModule.getInstance();
        boolean bl = shouldSmooth = mod.isEnabled() && (this.field_18719 || (Boolean)mod.smoothFirstPerson.getValue() != false);
        if (!shouldSmooth) {
            this.smoothPos = null;
            return;
        }
        double targetX = (Double)args.get(0);
        double targetY = (Double)args.get(1);
        double targetZ = (Double)args.get(2);
        if (this.smoothPos == null) {
            this.smoothPos = new class_243(targetX, targetY, targetZ);
        }
        double smoothFactor = ((Float)mod.smooth.getValue()).doubleValue();
        this.smoothPos = new class_243(class_3532.method_16436((double)smoothFactor, (double)this.smoothPos.field_1352, (double)targetX), class_3532.method_16436((double)smoothFactor, (double)this.smoothPos.field_1351, (double)targetY), class_3532.method_16436((double)smoothFactor, (double)this.smoothPos.field_1350, (double)targetZ));
        args.set(0, (Object)this.smoothPos.field_1352);
        args.set(1, (Object)this.smoothPos.field_1351);
        args.set(2, (Object)this.smoothPos.field_1350);
    }

    @Inject(method={"clipToSpace"}, at={@At(value="HEAD")}, cancellable=true)
    private void cameraClipToSpaceHook(float f, CallbackInfoReturnable<Float> returnable) {
        if (CameraClipModule.getInstance().isEnabled()) {
            returnable.setReturnValue((Object)Float.valueOf(f));
        }
    }

    @Inject(method={"update"}, at={@At(value="TAIL")})
    private void updateBabyCameraHook(class_1922 area, class_1297 focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        BabyModelModule mod = BabyModelModule.getInstance();
        if (!(mod.isEnabled() && ((Boolean)mod.cameraOffset.getValue()).booleanValue() && focusedEntity == SharedClass.player() && thirdPerson)) {
            return;
        }
        float scale = ((Float)mod.allScale.getValue()).floatValue();
        double offset = 1.62 * (double)(1.0f - scale);
        this.method_19324(0.0f, (float)(-offset), 0.0f);
    }
}

