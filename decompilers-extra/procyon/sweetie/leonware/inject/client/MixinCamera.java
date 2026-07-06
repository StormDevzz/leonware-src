// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.client;

import sweetie.leonware.api.system.backend.SharedClass;
import sweetie.leonware.client.features.modules.render.BabyModelModule;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import net.minecraft.class_3532;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import org.spongepowered.asm.mixin.injection.Redirect;
import sweetie.leonware.client.features.modules.render.CameraClipModule;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.class_1297;
import net.minecraft.class_1922;
import net.minecraft.class_243;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.class_4184;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_4184.class })
public abstract class MixinCamera
{
    @Shadow
    private boolean field_18719;
    @Unique
    private float currentTickDelta;
    @Unique
    private class_243 smoothPos;
    
    public MixinCamera() {
        this.currentTickDelta = 1.0f;
    }
    
    @Shadow
    protected abstract float method_19318(final float p0);
    
    @Shadow
    protected abstract void method_19324(final float p0, final float p1, final float p2);
    
    @Inject(method = { "update" }, at = { @At("HEAD") })
    private void captureTickDelta(final class_1922 area, final class_1297 focusedEntity, final boolean thirdPerson, final boolean inverseView, final float tickDelta, final CallbackInfo ci) {
        this.currentTickDelta = tickDelta;
    }
    
    @Redirect(method = { "update" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;moveBy(FFF)V"))
    private void cameraClipMoveByHook(final class_4184 instance, final float x, final float y, final float z) {
        final CameraClipModule mod = CameraClipModule.getInstance();
        if (!this.field_18719 || !mod.isEnabled()) {
            this.method_19324(x, y, z);
            return;
        }
        final float finalDistance = mod.distance.getValue() * mod.zoom.getValue();
        this.method_19324(-this.method_19318(finalDistance), 0.0f, 0.0f);
    }
    
    @ModifyArgs(method = { "update" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setPos(DDD)V"))
    private void cameraClipSetPosHook(final Args args) {
        final CameraClipModule mod = CameraClipModule.getInstance();
        final boolean shouldSmooth = mod.isEnabled() && (this.field_18719 || mod.smoothFirstPerson.getValue());
        if (!shouldSmooth) {
            this.smoothPos = null;
            return;
        }
        final double targetX = (double)args.get(0);
        final double targetY = (double)args.get(1);
        final double targetZ = (double)args.get(2);
        if (this.smoothPos == null) {
            this.smoothPos = new class_243(targetX, targetY, targetZ);
        }
        final double smoothFactor = mod.smooth.getValue();
        this.smoothPos = new class_243(class_3532.method_16436(smoothFactor, this.smoothPos.field_1352, targetX), class_3532.method_16436(smoothFactor, this.smoothPos.field_1351, targetY), class_3532.method_16436(smoothFactor, this.smoothPos.field_1350, targetZ));
        args.set(0, (Object)this.smoothPos.field_1352);
        args.set(1, (Object)this.smoothPos.field_1351);
        args.set(2, (Object)this.smoothPos.field_1350);
    }
    
    @Inject(method = { "clipToSpace" }, at = { @At("HEAD") }, cancellable = true)
    private void cameraClipToSpaceHook(final float f, final CallbackInfoReturnable<Float> returnable) {
        if (CameraClipModule.getInstance().isEnabled()) {
            returnable.setReturnValue((Object)f);
        }
    }
    
    @Inject(method = { "update" }, at = { @At("TAIL") })
    private void updateBabyCameraHook(final class_1922 area, final class_1297 focusedEntity, final boolean thirdPerson, final boolean inverseView, final float tickDelta, final CallbackInfo ci) {
        final BabyModelModule mod = BabyModelModule.getInstance();
        if (!mod.isEnabled() || !mod.cameraOffset.getValue() || focusedEntity != SharedClass.player() || !thirdPerson) {
            return;
        }
        final float scale = mod.allScale.getValue();
        final double offset = 1.62 * (1.0f - scale);
        this.method_19324(0.0f, (float)(-offset), 0.0f);
    }
}
