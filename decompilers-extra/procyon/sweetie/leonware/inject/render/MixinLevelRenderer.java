// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.render;

import net.minecraft.class_243;
import net.minecraft.class_4063;
import sweetie.leonware.client.features.modules.render.ShaderFogModule;
import net.minecraft.class_9958;
import net.minecraft.class_9909;
import org.joml.Matrix4fc;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import sweetie.leonware.client.features.modules.render.motionblur.MotionBlurModule;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.class_757;
import net.minecraft.class_4184;
import net.minecraft.class_9779;
import net.minecraft.class_9922;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Unique;
import org.joml.Matrix4f;
import net.minecraft.class_761;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_761.class })
public class MixinLevelRenderer
{
    @Unique
    private Matrix4f prevModelView;
    @Unique
    private Matrix4f prevProjection;
    @Unique
    private Vector3f prevCameraPos;
    
    public MixinLevelRenderer() {
        this.prevModelView = new Matrix4f();
        this.prevProjection = new Matrix4f();
        this.prevCameraPos = new Vector3f();
    }
    
    @Inject(method = { "render" }, at = { @At("HEAD") })
    private void setMatrices(final class_9922 allocator, final class_9779 tickCounter, final boolean renderBlockOutline, final class_4184 camera, final class_757 gameRenderer, final Matrix4f positionMatrix, final Matrix4f projectionMatrix, final CallbackInfo ci) {
        final float tickDelta = tickCounter.method_60637(true);
        MotionBlurModule.getInstance().shader.setFrameMotionBlur(positionMatrix, this.prevModelView, gameRenderer.method_22973(gameRenderer.method_3196(camera, tickDelta, true)), this.prevProjection, new Vector3f((float)(camera.method_19326().field_1352 % 30000.0), (float)(camera.method_19326().field_1351 % 30000.0), (float)(camera.method_19326().field_1350 % 30000.0)), this.prevCameraPos);
    }
    
    @Inject(method = { "render" }, at = { @At("RETURN") })
    private void setOldMatrices(final class_9922 allocator, final class_9779 tickCounter, final boolean renderBlockOutline, final class_4184 camera, final class_757 gameRenderer, final Matrix4f positionMatrix, final Matrix4f projectionMatrix, final CallbackInfo ci) {
        this.prevModelView = new Matrix4f((Matrix4fc)positionMatrix);
        final float tickDelta = tickCounter.method_60637(true);
        this.prevProjection = new Matrix4f((Matrix4fc)gameRenderer.method_22973(gameRenderer.method_3196(camera, tickDelta, true)));
        this.prevCameraPos = new Vector3f((float)(camera.method_19326().field_1352 % 30000.0), (float)(camera.method_19326().field_1351 % 30000.0), (float)(camera.method_19326().field_1350 % 30000.0));
    }
    
    @Inject(method = { "renderSky" }, at = { @At("HEAD") }, cancellable = true)
    private void onRenderSky(final class_9909 frameGraphBuilder, final class_4184 camera, final float tickDelta, final class_9958 fog, final CallbackInfo ci) {
        if (ShaderFogModule.getInstance() != null && ShaderFogModule.getInstance().isEnabled()) {
            ci.cancel();
        }
    }
    
    @Inject(method = { "renderClouds" }, at = { @At("HEAD") }, cancellable = true)
    private void onRenderClouds(final class_9909 frameGraphBuilder, final Matrix4f matrix4f, final Matrix4f matrix4f2, final class_4063 cloudRenderMode, final class_243 vec3d, final float f, final int i, final float f2, final CallbackInfo ci) {
        if (ShaderFogModule.getInstance() != null && ShaderFogModule.getInstance().isEnabled()) {
            ci.cancel();
        }
    }
}
