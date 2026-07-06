/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  net.minecraft.class_1922
 *  net.minecraft.class_3532
 *  net.minecraft.class_4184
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.ModifyArgs
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.invoke.arg.Args
 */
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

@Mixin(value={class_4184.class})
public class MixinCameraFreeCam {
    private float currentTickDelta = 1.0f;

    @Inject(method={"update"}, at={@At(value="HEAD")})
    private void capTickDelta(class_1922 area, class_1297 focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        this.currentTickDelta = tickDelta;
    }

    @ModifyArgs(method={"update"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/render/Camera;setPos(DDD)V"))
    private void freeCamPos(Args args) {
        FreeCamModule fc = FreeCamModule.getInstance();
        if (!fc.isEnabled()) {
            return;
        }
        double x = class_3532.method_16436((double)this.currentTickDelta, (double)fc.getPrevFakeX(), (double)fc.getFakeX());
        double y = class_3532.method_16436((double)this.currentTickDelta, (double)fc.getPrevFakeY(), (double)fc.getFakeY());
        double z = class_3532.method_16436((double)this.currentTickDelta, (double)fc.getPrevFakeZ(), (double)fc.getFakeZ());
        args.set(0, (Object)x);
        args.set(1, (Object)y);
        args.set(2, (Object)z);
    }

    @Inject(method={"update"}, at={@At(value="TAIL")})
    private void freeCamNoClip(class_1922 area, class_1297 focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
    }
}

