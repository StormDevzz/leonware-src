// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.render;

import org.spongepowered.asm.mixin.Unique;
import net.minecraft.class_4618;
import net.minecraft.class_4603;
import net.minecraft.class_4597;
import sweetie.leonware.api.interfaces.IHeldItemRenderer;
import net.minecraft.class_1934;
import net.minecraft.class_1309;
import net.minecraft.class_10366;
import net.minecraft.class_9916;
import net.minecraft.class_9960;
import net.minecraft.class_9922;
import sweetie.leonware.inject.accessors.IGameRenderer;
import net.minecraft.class_279;
import sweetie.leonware.api.utils.render.ShadersTicker;
import net.minecraft.class_276;
import net.minecraft.class_9909;
import sweetie.leonware.inject.accessors.IWorldRenderer;
import sweetie.leonware.client.features.modules.render.shaders.ShadersModule;
import sweetie.leonware.client.features.modules.render.CustomBobModule;
import sweetie.leonware.client.features.modules.render.AspectRatioModule;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.class_3532;
import sweetie.leonware.client.features.modules.render.RemovalsModule;
import org.spongepowered.asm.mixin.injection.Inject;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.utils.render.display.WorldRender;
import net.minecraft.class_7833;
import org.joml.Matrix4fc;
import com.mojang.blaze3d.systems.RenderSystem;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.class_9779;
import sweetie.leonware.api.utils.rotation.RaytracingUtil;
import net.minecraft.class_239;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import net.minecraft.class_243;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import sweetie.leonware.client.features.modules.combat.NoEntityTraceModule;
import sweetie.leonware.api.system.backend.SharedClass;
import net.minecraft.class_1297;
import net.minecraft.class_3966;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import sweetie.leonware.client.features.modules.render.CameraClipModule;
import net.minecraft.class_5498;
import net.minecraft.class_4587;
import net.minecraft.class_4184;
import net.minecraft.class_759;
import net.minecraft.class_765;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.class_310;
import net.minecraft.class_757;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_757.class })
public abstract class MixinGameRenderer
{
    @Shadow
    @Final
    class_310 field_4015;
    @Shadow
    @Final
    private class_765 field_4028;
    @Shadow
    @Final
    public class_759 field_4012;
    @Shadow
    private boolean field_4001;
    
    @Shadow
    protected abstract float method_3196(final class_4184 p0, final float p1, final boolean p2);
    
    @Shadow
    protected abstract void method_3198(final class_4587 p0, final float p1);
    
    @Shadow
    protected abstract void method_3186(final class_4587 p0, final float p1);
    
    @Redirect(method = { "renderHand" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/Perspective;isFirstPerson()Z"))
    private boolean redirectIsFirstPerson(final class_5498 perspective) {
        final CameraClipModule mod = CameraClipModule.getInstance();
        return (mod.isEnabled() && mod.keepHandsInThirdPerson.getValue()) || perspective.method_31034();
    }
    
    @Shadow
    public abstract float method_3193();
    
    @ModifyExpressionValue(method = { "findCrosshairTarget" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileUtil;raycast(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;D)Lnet/minecraft/util/hit/EntityHitResult;") })
    private class_3966 traceEntityHook(final class_3966 original, final class_1297 camera, final double blockInteractionRange, final double entityInteractionRange, final float tickDelta) {
        if (original == null || original.method_17782() == null || SharedClass.player() == null) {
            return original;
        }
        if (NoEntityTraceModule.getInstance().shouldCancelResult(original.method_17782())) {
            return null;
        }
        return original;
    }
    
    @ModifyExpressionValue(method = { "findCrosshairTarget" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getRotationVec(F)Lnet/minecraft/util/math/Vec3d;") })
    private class_243 rotationVectorHook(final class_243 original, final class_1297 camera, final double blockInteractionRange, final double entityInteractionRange, final float tickDelta) {
        if (camera != SharedClass.player()) {
            return original;
        }
        final Rotation rotation = RotationManager.getInstance().getCurrentRotation();
        return (rotation != null) ? rotation.getVector() : original;
    }
    
    @ModifyExpressionValue(method = { "findCrosshairTarget" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;raycast(DFZ)Lnet/minecraft/util/hit/HitResult;") })
    private class_239 hookRaycast(final class_239 original, final class_1297 camera, final double blockInteractionRange, final double entityInteractionRange, final float tickDelta) {
        if (camera != SharedClass.player()) {
            return original;
        }
        final Rotation cameraRotation = new Rotation(camera.method_5705(tickDelta), camera.method_5695(tickDelta));
        Rotation rotation;
        if (RotationManager.getInstance().getCurrentRotation() != null) {
            rotation = RotationManager.getInstance().getCurrentRotation();
        }
        else {
            rotation = cameraRotation;
        }
        return (class_239)RaytracingUtil.raycast(rotation, Math.max(blockInteractionRange, entityInteractionRange), false, tickDelta);
    }
    
    @Inject(method = { "renderWorld" }, at = { @At(value = "FIELD", target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z", opcode = 180, ordinal = 0) })
    void render3D(final class_9779 renderTickCounter, final CallbackInfo ci) {
        if (SharedClass.player() == null || class_310.method_1551().field_1687 == null) {
            return;
        }
        final class_4587 matrixStack = new class_4587();
        final class_4184 camera = class_310.method_1551().field_1773.method_19418();
        RenderSystem.getModelViewStack().pushMatrix().mul((Matrix4fc)matrixStack.method_23760().method_23761());
        matrixStack.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
        matrixStack.method_22907(class_7833.field_40716.rotationDegrees(camera.method_19330() + 180.0f));
        WorldRender.setTranslation(matrixStack);
        Render3DEvent.getInstance().call(new Render3DEvent.Render3DEventData(matrixStack, renderTickCounter.method_60637(false)));
        RenderUtil.BOX.setup3DRender(matrixStack);
        RenderSystem.getModelViewStack().popMatrix();
    }
    
    @Inject(method = { "tiltViewWhenHurt" }, at = { @At("HEAD") }, cancellable = true)
    private void onTiltViewWhenHurt(final class_4587 matrices, final float tickDelta, final CallbackInfo ci) {
        if (class_310.method_1551().field_1724 == null) {
            return;
        }
        if (RemovalsModule.getInstance().isHurtCamera()) {
            ci.cancel();
        }
    }
    
    @Redirect(method = { "renderWorld" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;lerp(FFF)F"))
    private float onRenderWorld(final float delta, final float first, final float second) {
        if (class_310.method_1551().field_1724 == null) {
            return 0.0f;
        }
        if (RemovalsModule.getInstance().isBadEffects()) {
            return 0.0f;
        }
        return class_3532.method_16439(delta, first, second);
    }
    
    @Inject(method = { "getBasicProjectionMatrix" }, at = { @At("HEAD") }, cancellable = true)
    public void onGetBasicProjectionMatrix(final float fov, final CallbackInfoReturnable<Matrix4f> cir) {
        if (AspectRatioModule.getInstance().isEnabled()) {
            final Matrix4f matrix4f = new Matrix4f();
            final float aspectRatio = AspectRatioModule.getInstance().ratio.getValue();
            matrix4f.perspective((float)Math.toRadians(fov), aspectRatio, 0.05f, this.method_3193() * 4.0f);
            cir.setReturnValue((Object)matrix4f);
        }
    }
    
    @Inject(method = { "bobView" }, at = { @At("HEAD") }, cancellable = true)
    private void onBobView(final class_4587 matrices, final float tickDelta, final CallbackInfo ci) {
        final CustomBobModule customBob = CustomBobModule.getInstance();
        if (customBob.isEnabled()) {
            ci.cancel();
            final float total = customBob.txStrength.getValue() + customBob.tyStrength.getValue() + customBob.rxStrength.getValue() + customBob.rzStrength.getValue();
            if (total > 0.0f) {
                customBob.applyCustomBob(matrices);
            }
        }
    }
    
    @Inject(method = { "renderHand" }, at = { @At("HEAD") }, cancellable = true)
    private void leonware$onRenderHand(final class_4184 camera, final float tickDelta, final Matrix4f matrix4f, final CallbackInfo ci) {
        final ShadersModule shaders = ShadersModule.getInstance();
        if (shaders.isEnabled()) {
            final boolean shaderHands = shaders.isTargetHands();
            final class_9960 framebufferSet = ((IWorldRenderer)this.field_4015.field_1769)._getFrameBufferSet();
            final class_9909 frameGraphBuilder = new class_9909();
            framebufferSet.field_53091 = frameGraphBuilder.method_61914("main", (Object)this.field_4015.method_1522());
            framebufferSet.field_53097 = frameGraphBuilder.method_61914("entity_outline", (Object)((IWorldRenderer)this.field_4015.field_1769)._getEntityOutlineFramebuffer());
            final int frameBufferWidth = this.field_4015.method_1522().field_1482;
            final int frameBufferHeight = this.field_4015.method_1522().field_1481;
            final class_9916 renderPass = frameGraphBuilder.method_61911("main");
            framebufferSet.field_53091 = renderPass.method_61933(framebufferSet.field_53091);
            framebufferSet.field_53097 = renderPass.method_61933(framebufferSet.field_53097);
            renderPass.method_61929(() -> {
                ((class_276)framebufferSet.field_53097.get()).method_1236(1.0f, 1.0f, 1.0f, 0.0f);
                ((class_276)framebufferSet.field_53097.get()).method_1235(false);
                if (shaderHands) {
                    this.leonware$renderShaderHand(camera, tickDelta);
                }
                ((IWorldRenderer)this.field_4015.field_1769)._getBufferBuilders().method_23000().method_22993();
                ((IWorldRenderer)this.field_4015.field_1769)._getBufferBuilders().method_23003().method_23285();
                return;
            });
            ShadersTicker.getInstance().update(1.0f);
            shaders.loadShaders();
            shaders.drawShader(frameGraphBuilder, frameBufferWidth, frameBufferHeight, (class_279.class_9961)framebufferSet);
            frameGraphBuilder.method_61909((class_9922)((IGameRenderer)this.field_4015.field_1773)._getPool());
            this.field_4015.method_1522().method_1235(false);
            framebufferSet.method_62223();
            if (shaderHands) {
                ci.cancel();
            }
        }
    }
    
    @Unique
    private void leonware$renderShaderHand(final class_4184 camera, final float tickDelta) {
        if (!this.field_4001) {
            final Matrix4f matrix4f2 = ((class_757)this).method_22973(this.method_3196(camera, tickDelta, false));
            RenderSystem.setProjectionMatrix(matrix4f2, class_10366.field_54953);
            final class_4587 matrixStack = new class_4587();
            matrixStack.method_22903();
            this.method_3198(matrixStack, tickDelta);
            if (this.field_4015.field_1690.method_42448().method_41753()) {
                this.method_3186(matrixStack, tickDelta);
            }
            final boolean bl = this.field_4015.method_1560() instanceof class_1309 && ((class_1309)this.field_4015.method_1560()).method_6113();
            if (this.field_4015.field_1690.method_31044().method_31034() && !bl && !this.field_4015.field_1690.field_1842 && this.field_4015.field_1761.method_2920() != class_1934.field_9219) {
                this.field_4028.method_3316();
                final class_4618 outlineVertexConsumerProvider = ((IWorldRenderer)this.field_4015.field_1769)._getBufferBuilders().method_23003();
                outlineVertexConsumerProvider.method_23286(255, 255, 255, 255);
                ((IHeldItemRenderer)this.field_4012).leonware$renderShaderItem(tickDelta, matrixStack, (class_4597)outlineVertexConsumerProvider, this.field_4015.field_1724, this.field_4015.method_1561().method_23839((class_1297)this.field_4015.field_1724, tickDelta));
                this.field_4028.method_3315();
            }
            matrixStack.method_22909();
            if (this.field_4015.field_1690.method_31044().method_31034() && !bl) {
                class_4603.method_23067(this.field_4015, matrixStack, (class_4597)((IWorldRenderer)this.field_4015.field_1769)._getBufferBuilders().method_23000());
            }
        }
    }
}
