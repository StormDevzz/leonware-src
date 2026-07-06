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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/client/MixinCamera.class */
@Mixin({class_4184.class})
public abstract class MixinCamera {

    @Shadow
    private boolean field_18719;

    @Unique
    private float currentTickDelta = 1.0f;

    @Unique
    private class_243 smoothPos;

    @Shadow
    protected abstract float method_19318(float f);

    @Shadow
    protected abstract void method_19324(float f, float f2, float f3);

    @Inject(method = {"update"}, at = {@At("HEAD")})
    private void captureTickDelta(class_1922 area, class_1297 focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        this.currentTickDelta = tickDelta;
    }

    @Redirect(method = {"update"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;moveBy(FFF)V"))
    private void cameraClipMoveByHook(class_4184 instance, float x, float y, float z) {
        CameraClipModule mod = CameraClipModule.getInstance();
        if (!this.field_18719 || !mod.isEnabled()) {
            method_19324(x, y, z);
        } else {
            float finalDistance = mod.distance.getValue().floatValue() * mod.zoom.getValue().floatValue();
            method_19324(-method_19318(finalDistance), 0.0f, 0.0f);
        }
    }

    @ModifyArgs(method = {"update"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setPos(DDD)V"))
    private void cameraClipSetPosHook(Args args) {
        CameraClipModule mod = CameraClipModule.getInstance();
        boolean shouldSmooth = mod.isEnabled() && (this.field_18719 || mod.smoothFirstPerson.getValue().booleanValue());
        if (!shouldSmooth) {
            this.smoothPos = null;
            return;
        }
        double targetX = ((Double) args.get(0)).doubleValue();
        double targetY = ((Double) args.get(1)).doubleValue();
        double targetZ = ((Double) args.get(2)).doubleValue();
        if (this.smoothPos == null) {
            this.smoothPos = new class_243(targetX, targetY, targetZ);
        }
        double smoothFactor = mod.smooth.getValue().doubleValue();
        this.smoothPos = new class_243(class_3532.method_16436(smoothFactor, this.smoothPos.field_1352, targetX), class_3532.method_16436(smoothFactor, this.smoothPos.field_1351, targetY), class_3532.method_16436(smoothFactor, this.smoothPos.field_1350, targetZ));
        args.set(0, Double.valueOf(this.smoothPos.field_1352));
        args.set(1, Double.valueOf(this.smoothPos.field_1351));
        args.set(2, Double.valueOf(this.smoothPos.field_1350));
    }

    @Inject(method = {"clipToSpace"}, at = {@At("HEAD")}, cancellable = true)
    private void cameraClipToSpaceHook(float f, CallbackInfoReturnable<Float> returnable) {
        if (CameraClipModule.getInstance().isEnabled()) {
            returnable.setReturnValue(Float.valueOf(f));
        }
    }

    @Inject(method = {"update"}, at = {@At("TAIL")})
    private void updateBabyCameraHook(class_1922 area, class_1297 focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        BabyModelModule mod = BabyModelModule.getInstance();
        if (mod.isEnabled() && mod.cameraOffset.getValue().booleanValue() && focusedEntity == SharedClass.player() && thirdPerson) {
            float scale = mod.allScale.getValue().floatValue();
            double offset = 1.62d * ((double) (1.0f - scale));
            method_19324(0.0f, (float) (-offset), 0.0f);
        }
    }
}
