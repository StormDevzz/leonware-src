// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.client;

import org.spongepowered.asm.mixin.injection.ModifyArgs;
import net.minecraft.class_3532;
import sweetie.leonware.client.features.modules.render.FreeCamModule;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.class_1297;
import net.minecraft.class_1922;
import net.minecraft.class_4184;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_4184.class })
public class MixinCameraFreeCam
{
    private float currentTickDelta;
    
    public MixinCameraFreeCam() {
        this.currentTickDelta = 1.0f;
    }
    
    @Inject(method = { "update" }, at = { @At("HEAD") })
    private void capTickDelta(final class_1922 area, final class_1297 focusedEntity, final boolean thirdPerson, final boolean inverseView, final float tickDelta, final CallbackInfo ci) {
        this.currentTickDelta = tickDelta;
    }
    
    @ModifyArgs(method = { "update" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setPos(DDD)V"))
    private void freeCamPos(final Args args) {
        final FreeCamModule fc = FreeCamModule.getInstance();
        if (!fc.isEnabled()) {
            return;
        }
        final double x = class_3532.method_16436((double)this.currentTickDelta, fc.getPrevFakeX(), fc.getFakeX());
        final double y = class_3532.method_16436((double)this.currentTickDelta, fc.getPrevFakeY(), fc.getFakeY());
        final double z = class_3532.method_16436((double)this.currentTickDelta, fc.getPrevFakeZ(), fc.getFakeZ());
        args.set(0, (Object)x);
        args.set(1, (Object)y);
        args.set(2, (Object)z);
    }
    
    @Inject(method = { "update" }, at = { @At("TAIL") })
    private void freeCamNoClip(final class_1922 area, final class_1297 focusedEntity, final boolean thirdPerson, final boolean inverseView, final float tickDelta, final CallbackInfo ci) {
    }
}
