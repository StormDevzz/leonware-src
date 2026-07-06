package org.ladysnake.satin.mixin.client.gl;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import java.nio.IntBuffer;
import net.minecraft.class_276;
import org.ladysnake.satin.api.experimental.ReadableDepthFramebuffer;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/mixin/client/gl/DepthGlFramebufferMixin.class */
@Mixin({class_276.class})
public abstract class DepthGlFramebufferMixin implements ReadableDepthFramebuffer {

    @Shadow
    @Final
    public boolean field_1478;

    @Shadow
    public int field_1482;

    @Shadow
    public int field_1481;
    private int satin$stillDepthTexture = -1;

    @Shadow
    public abstract void method_1235(boolean z);

    @Inject(method = {"initFbo"}, at = {@At(value = "FIELD", opcode = 181, target = "Lnet/minecraft/client/gl/Framebuffer;depthAttachment:I", shift = At.Shift.AFTER)})
    private void initFbo(int width, int height, CallbackInfo ci) {
        if (this.field_1478) {
            this.satin$stillDepthTexture = satin$setupDepthTexture();
        }
    }

    @Unique
    private int satin$setupDepthTexture() {
        int shadowMap = GL11.glGenTextures();
        RenderSystem.bindTexture(shadowMap);
        RenderSystem.texParameter(3553, 10240, 9729);
        RenderSystem.texParameter(3553, 10241, 9729);
        RenderSystem.texParameter(3553, 10242, 33071);
        RenderSystem.texParameter(3553, 10243, 33071);
        GlStateManager._texImage2D(3553, 0, 33190, this.field_1482, this.field_1481, 0, 6402, 5121, (IntBuffer) null);
        return shadowMap;
    }

    @Inject(method = {"delete"}, at = {@At(value = "FIELD", opcode = 180, target = "Lnet/minecraft/client/gl/Framebuffer;depthAttachment:I")})
    private void delete(CallbackInfo ci) {
        if (this.satin$stillDepthTexture > -1) {
            TextureUtil.releaseTextureId(this.satin$stillDepthTexture);
            this.satin$stillDepthTexture = -1;
        }
    }

    @Override // org.ladysnake.satin.api.experimental.ReadableDepthFramebuffer
    public int getStillDepthMap() {
        return this.satin$stillDepthTexture;
    }

    @Override // org.ladysnake.satin.api.experimental.ReadableDepthFramebuffer
    public void freezeDepthMap() {
        if (this.field_1478) {
            method_1235(false);
            RenderSystem.bindTexture(this.satin$stillDepthTexture);
            GL11.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, this.field_1482, this.field_1481);
        }
    }
}
