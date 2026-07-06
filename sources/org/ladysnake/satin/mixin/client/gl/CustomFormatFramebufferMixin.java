package org.ladysnake.satin.mixin.client.gl;

import net.minecraft.class_276;
import org.ladysnake.satin.impl.CustomFormatFramebuffers;
import org.newsclub.net.unix.AFVSOCKSocketAddress;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/mixin/client/gl/CustomFormatFramebufferMixin.class */
@Mixin({class_276.class})
public abstract class CustomFormatFramebufferMixin {

    @Unique
    private int satin$format = 32856;

    @Inject(method = {"<init>"}, at = {@At("TAIL")})
    private void satin$setFormat(boolean useDepth, CallbackInfo ci) {
        CustomFormatFramebuffers.TextureFormat format = CustomFormatFramebuffers.getCustomFormat();
        if (format != null) {
            this.satin$format = format.value;
            CustomFormatFramebuffers.clearCustomFormat();
        }
    }

    @ModifyArg(method = {"initFbo"}, slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/Framebuffer;setTexFilter(IZ)V"), to = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlStateManager;_glBindFramebuffer(II)V")), at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlStateManager;_texImage2D(IIIIIIIILjava/nio/IntBuffer;)V"), index = AFVSOCKSocketAddress.VMADDR_CID_HOST)
    private int satin$modifyInternalFormat(int internalFormat) {
        return this.satin$format;
    }
}
