package sweetie.leonware.inject.client;

import net.minecraft.class_1297;
import net.minecraft.class_1922;
import net.minecraft.class_3532;
import net.minecraft.class_4184;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import sweetie.leonware.client.features.modules.render.FreeCamModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/client/MixinCameraFreeCam.class */
@Mixin({class_4184.class})
public class MixinCameraFreeCam {
    private float currentTickDelta = 1.0f;

    @Inject(method = {"update"}, at = {@At("HEAD")})
    private void capTickDelta(class_1922 area, class_1297 focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        this.currentTickDelta = tickDelta;
    }

    @ModifyArgs(method = {"update"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setPos(DDD)V"))
    private void freeCamPos(Args args) {
        FreeCamModule fc = FreeCamModule.getInstance();
        if (fc.isEnabled()) {
            double x = class_3532.method_16436(this.currentTickDelta, fc.getPrevFakeX(), fc.getFakeX());
            double y = class_3532.method_16436(this.currentTickDelta, fc.getPrevFakeY(), fc.getFakeY());
            double z = class_3532.method_16436(this.currentTickDelta, fc.getPrevFakeZ(), fc.getFakeZ());
            args.set(0, Double.valueOf(x));
            args.set(1, Double.valueOf(y));
            args.set(2, Double.valueOf(z));
        }
    }

    @Inject(method = {"update"}, at = {@At("TAIL")})
    private void freeCamNoClip(class_1922 area, class_1297 focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
    }
}
