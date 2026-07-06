/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_243
 *  net.minecraft.class_4063
 *  net.minecraft.class_4184
 *  net.minecraft.class_757
 *  net.minecraft.class_761
 *  net.minecraft.class_9779
 *  net.minecraft.class_9909
 *  net.minecraft.class_9922
 *  net.minecraft.class_9958
 *  org.joml.Matrix4f
 *  org.joml.Matrix4fc
 *  org.joml.Vector3f
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package sweetie.leonware.inject.render;

import net.minecraft.class_243;
import net.minecraft.class_4063;
import net.minecraft.class_4184;
import net.minecraft.class_757;
import net.minecraft.class_761;
import net.minecraft.class_9779;
import net.minecraft.class_9909;
import net.minecraft.class_9922;
import net.minecraft.class_9958;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sweetie.leonware.client.features.modules.render.ShaderFogModule;
import sweetie.leonware.client.features.modules.render.motionblur.MotionBlurModule;

@Mixin(value={class_761.class})
public class MixinLevelRenderer {
    @Unique
    private Matrix4f prevModelView = new Matrix4f();
    @Unique
    private Matrix4f prevProjection = new Matrix4f();
    @Unique
    private Vector3f prevCameraPos = new Vector3f();

    @Inject(method={"render"}, at={@At(value="HEAD")})
    private void setMatrices(class_9922 allocator, class_9779 tickCounter, boolean renderBlockOutline, class_4184 camera, class_757 gameRenderer, Matrix4f positionMatrix, Matrix4f projectionMatrix, CallbackInfo ci) {
        float tickDelta = tickCounter.method_60637(true);
        MotionBlurModule.getInstance().shader.setFrameMotionBlur(positionMatrix, this.prevModelView, gameRenderer.method_22973(gameRenderer.method_3196(camera, tickDelta, true)), this.prevProjection, new Vector3f((float)(camera.method_19326().field_1352 % 30000.0), (float)(camera.method_19326().field_1351 % 30000.0), (float)(camera.method_19326().field_1350 % 30000.0)), this.prevCameraPos);
    }

    @Inject(method={"render"}, at={@At(value="RETURN")})
    private void setOldMatrices(class_9922 allocator, class_9779 tickCounter, boolean renderBlockOutline, class_4184 camera, class_757 gameRenderer, Matrix4f positionMatrix, Matrix4f projectionMatrix, CallbackInfo ci) {
        this.prevModelView = new Matrix4f((Matrix4fc)positionMatrix);
        float tickDelta = tickCounter.method_60637(true);
        this.prevProjection = new Matrix4f((Matrix4fc)gameRenderer.method_22973(gameRenderer.method_3196(camera, tickDelta, true)));
        this.prevCameraPos = new Vector3f((float)(camera.method_19326().field_1352 % 30000.0), (float)(camera.method_19326().field_1351 % 30000.0), (float)(camera.method_19326().field_1350 % 30000.0));
    }

    @Inject(method={"renderSky"}, at={@At(value="HEAD")}, cancellable=true)
    private void onRenderSky(class_9909 frameGraphBuilder, class_4184 camera, float tickDelta, class_9958 fog, CallbackInfo ci) {
        if (ShaderFogModule.getInstance() != null && ShaderFogModule.getInstance().isEnabled()) {
            ci.cancel();
        }
    }

    @Inject(method={"renderClouds"}, at={@At(value="HEAD")}, cancellable=true)
    private void onRenderClouds(class_9909 frameGraphBuilder, Matrix4f matrix4f, Matrix4f matrix4f2, class_4063 cloudRenderMode, class_243 vec3d, float f, int i, float f2, CallbackInfo ci) {
        if (ShaderFogModule.getInstance() != null && ShaderFogModule.getInstance().isEnabled()) {
            ci.cancel();
        }
    }
}

