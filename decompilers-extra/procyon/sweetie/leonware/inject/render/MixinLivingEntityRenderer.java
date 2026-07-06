// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.render;

import net.minecraft.class_156;
import net.minecraft.class_9851;
import net.minecraft.class_4668;
import net.minecraft.class_293;
import net.minecraft.class_290;
import sweetie.leonware.client.features.modules.render.CustomModelModule;
import net.minecraft.class_591;
import sweetie.leonware.api.system.configs.FriendManager;
import net.minecraft.class_1657;
import sweetie.leonware.client.features.modules.render.BadtripModule;
import net.minecraft.class_630;
import net.minecraft.class_572;
import sweetie.leonware.client.features.modules.render.BabyModelModule;
import net.minecraft.class_10055;
import net.minecraft.class_310;
import sweetie.leonware.client.features.modules.render.ChamsModule;
import net.minecraft.class_4597;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.class_4588;
import net.minecraft.class_4587;
import org.spongepowered.asm.mixin.injection.Redirect;
import sweetie.leonware.api.event.events.render.EntityColorEvent;
import org.jetbrains.annotations.Nullable;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import sweetie.leonware.api.utils.rotation.manager.RotationPlan;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import net.minecraft.class_3532;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.client.features.modules.movement.MoveFixModule;
import sweetie.leonware.api.system.backend.SharedClass;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.class_1921;
import net.minecraft.class_2960;
import java.util.function.Function;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.class_922;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.class_583;
import net.minecraft.class_10042;
import net.minecraft.class_1309;

@Mixin({ class_922.class })
public abstract class MixinLivingEntityRenderer<T extends class_1309, S extends class_10042, M extends class_583<? super S>>
{
    @Shadow
    protected M field_4737;
    @Unique
    private T leonware$lastEntity;
    @Unique
    private boolean leonware$badTripActive;
    @Unique
    private boolean leonware$isBabyActive;
    @Unique
    private boolean leonware$customModelHidden;
    @Unique
    private static final Function<class_2960, class_1921> LEONWARE_CHAMS_LAYER;
    
    public MixinLivingEntityRenderer() {
        this.leonware$badTripActive = false;
        this.leonware$isBabyActive = false;
        this.leonware$customModelHidden = false;
    }
    
    @Shadow
    public abstract class_2960 method_3885(final S p0);
    
    @Inject(method = { "updateRenderState" }, at = { @At("HEAD") })
    private void captureEntity(final T entity, final S state, final float tickDelta, final CallbackInfo ci) {
        this.leonware$lastEntity = entity;
    }
    
    @ModifyExpressionValue(method = { "updateRenderState*" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getLerpedPitch(F)F") })
    private float updateVisualPitch(final float original, final class_1309 entity, final S state, final float tickDelta) {
        if (entity != SharedClass.player()) {
            return original;
        }
        if (MoveFixModule.shouldUseFakeRotation()) {
            final Rotation fakeRot = MoveFixModule.getFakeRotation();
            if (fakeRot != null) {
                return fakeRot.getPitch();
            }
        }
        final RotationManager rotationManager = RotationManager.getInstance();
        final Rotation rotation = rotationManager.getRotation();
        final Rotation rotationPrev = rotationManager.getPreviousRotation();
        final RotationPlan currentRotationPlan = rotationManager.getCurrentRotationPlan();
        if (currentRotationPlan == null) {
            return original;
        }
        return class_3532.method_17821(tickDelta, rotationPrev.getPitch(), rotation.getPitch());
    }
    
    @ModifyExpressionValue(method = { "updateRenderState*" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;clampBodyYaw(Lnet/minecraft/entity/LivingEntity;FF)F") })
    private float updateVisualBodyYaw(final float original, final class_1309 entity, final S state, final float tickDelta) {
        if (entity != SharedClass.player()) {
            return original;
        }
        if (MoveFixModule.shouldUseFakeRotation()) {
            final Rotation fake = MoveFixModule.getFakeRotation();
            if (fake != null) {
                return fake.getYaw();
            }
        }
        final RotationManager rm = RotationManager.getInstance();
        if (rm.getCurrentRotationPlan() == null) {
            return original;
        }
        return class_3532.method_17821(tickDelta, rm.getPreviousRotation().getYaw(), rm.getRotation().getYaw());
    }
    
    @ModifyExpressionValue(method = { "updateRenderState*" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;lerpAngleDegrees(FFF)F") })
    private float updateVisualYaw(final float original, final class_1309 entity, final S state, final float tickDelta) {
        if (entity != SharedClass.player()) {
            return original;
        }
        if (MoveFixModule.shouldUseFakeRotation()) {
            final Rotation fakeRot = MoveFixModule.getFakeRotation();
            if (fakeRot != null) {
                return fakeRot.getYaw();
            }
        }
        final RotationManager rotationManager = RotationManager.getInstance();
        final Rotation rotation = rotationManager.getRotation();
        final Rotation rotationPrev = rotationManager.getPreviousRotation();
        final RotationPlan currentRotationPlan = rotationManager.getCurrentRotationPlan();
        if (currentRotationPlan == null) {
            return original;
        }
        return class_3532.method_17821(tickDelta, rotationPrev.getYaw(), rotation.getYaw());
    }
    
    @Shadow
    @Nullable
    protected abstract class_1921 method_24302(final S p0, final boolean p1, final boolean p2, final boolean p3);
    
    @Redirect(method = { "render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;getRenderLayer(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;ZZZ)Lnet/minecraft/client/render/RenderLayer;"))
    private class_1921 renderHook(final class_922 instance, final S state, final boolean showBody, boolean translucent, final boolean showOutline) {
        if (!translucent && state.field_53329 == 0.6f) {
            final int defaultColor = -1;
            final EntityColorEvent.EntityColorEventData eventData = new EntityColorEvent.EntityColorEventData(defaultColor);
            final boolean modified = EntityColorEvent.getInstance().call(eventData);
            if (modified && eventData.color() != defaultColor) {
                translucent = true;
            }
        }
        return this.method_24302(state, showBody, translucent, showOutline);
    }
    
    @Redirect(method = { "render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;III)V"))
    private void renderModelHook(final class_583<?> instance, final class_4587 matrixStack, final class_4588 vertexConsumer, final int light, final int overlay, final int color, @Local(ordinal = 0, argsOnly = true) final class_10042 renderState, @Local(argsOnly = true) final class_4597 vcp, @Local final class_1921 renderLayer) {
        final EntityColorEvent.EntityColorEventData event = new EntityColorEvent.EntityColorEventData(color);
        if (renderState.field_53461) {
            EntityColorEvent.getInstance().call(event);
        }
        final int finalColor = event.color();
        final ChamsModule chams = ChamsModule.getInstance();
        if (chams.isEnabled() && this.leonware$isChamsTarget(renderState)) {
            final class_2960 texture = this.method_3885(renderState);
            final class_4597.class_4598 entityConsumers = class_310.method_1551().method_22940().method_23000();
            final class_4588 hiddenConsumer = entityConsumers.getBuffer((class_1921)MixinLivingEntityRenderer.LEONWARE_CHAMS_LAYER.apply(texture));
            instance.method_62100(matrixStack, hiddenConsumer, light, overlay, chams.getHiddenArgb());
            entityConsumers.method_22994((class_1921)MixinLivingEntityRenderer.LEONWARE_CHAMS_LAYER.apply(texture));
            final class_4588 freshConsumer = vcp.getBuffer(renderLayer);
            instance.method_62100(matrixStack, freshConsumer, light, overlay, chams.getVisibleArgb());
        }
        else {
            instance.method_62100(matrixStack, vertexConsumer, light, overlay, finalColor);
        }
    }
    
    @Unique
    private boolean leonware$isChamsTarget(final class_10042 renderState) {
        final ChamsModule chams = ChamsModule.getInstance();
        return !chams.isOnlyPlayers() || renderState instanceof class_10055;
    }
    
    @Inject(method = { "render" }, at = { @At("HEAD") })
    private void onRenderBabyPre(final S state, final class_4587 matrixStack, final class_4597 vertexConsumerProvider, final int light, final CallbackInfo ci) {
        final BabyModelModule mod = BabyModelModule.getInstance();
        if (!mod.isEnabled()) {
            return;
        }
        if (state instanceof class_10055 && this.leonware$lastEntity != null && this.leonware$shouldScale(mod, this.leonware$lastEntity)) {
            final float scale = mod.allScale.getValue();
            final float hScale = mod.headScale.getValue();
            matrixStack.method_22903();
            matrixStack.method_22905(scale, scale, scale);
            BabyModelModule.currentShouldScale = true;
            this.leonware$isBabyActive = true;
            final class_583<? super S> field_4737 = this.field_4737;
            if (field_4737 instanceof class_572) {
                final class_572<?> bipedModel = (class_572<?>)field_4737;
                final float yOffset = (hScale - 1.0f) * 4.0f;
                final class_630 field_4738 = bipedModel.field_3398;
                field_4738.field_3656 -= yOffset;
                final class_630 field_4739 = bipedModel.field_3394;
                field_4739.field_3656 -= yOffset;
                bipedModel.field_3398.field_37938 = hScale;
                bipedModel.field_3398.field_37939 = hScale;
                bipedModel.field_3398.field_37940 = hScale;
                bipedModel.field_3394.field_37938 = hScale;
                bipedModel.field_3394.field_37939 = hScale;
                bipedModel.field_3394.field_37940 = hScale;
            }
        }
    }
    
    @Inject(method = { "render" }, at = { @At("HEAD") })
    private void onRenderBadTripHead(final S state, final class_4587 matrices, final class_4597 vertexConsumers, final int light, final CallbackInfo ci) {
        final BadtripModule mod = BadtripModule.getInstance();
        if (mod.isEnabled() && this.leonware$lastEntity != null && this.leonware$lastEntity.field_6235 > 0) {
            matrices.method_22903();
            this.leonware$badTripActive = true;
            final float tickDelta = class_310.method_1551().method_61966().method_60637(false);
            final float smoothHurt = this.leonware$lastEntity.field_6235 - tickDelta;
            final float hurtProgress = smoothHurt / 10.0f;
            final float anim = 1.0f - (float)Math.pow(1.0f - (1.0f - hurtProgress), 3.0);
            final float strength = mod.strength.getValue();
            final float scaleXZ = 1.0f + strength * (1.0f - anim);
            final float scaleY = Math.max(0.4f, anim);
            matrices.method_22905(scaleXZ, scaleY, scaleXZ);
        }
    }
    
    @Inject(method = { "render" }, at = { @At("RETURN") })
    private void onRenderAllPost(final S state, final class_4587 matrices, final class_4597 vertexConsumers, final int light, final CallbackInfo ci) {
        if (this.leonware$isBabyActive) {
            final class_583<? super S> field_4737 = this.field_4737;
            if (field_4737 instanceof class_572) {
                final class_572<?> bipedModel = (class_572<?>)field_4737;
                bipedModel.field_3398.field_37938 = 1.0f;
                bipedModel.field_3398.field_37939 = 1.0f;
                bipedModel.field_3398.field_37940 = 1.0f;
                bipedModel.field_3398.field_3656 = 0.0f;
                bipedModel.field_3394.field_37938 = 1.0f;
                bipedModel.field_3394.field_37939 = 1.0f;
                bipedModel.field_3394.field_37940 = 1.0f;
                bipedModel.field_3394.field_3656 = 0.0f;
            }
            matrices.method_22909();
            this.leonware$isBabyActive = false;
            BabyModelModule.currentShouldScale = false;
        }
        if (this.leonware$badTripActive) {
            matrices.method_22909();
            this.leonware$badTripActive = false;
        }
    }
    
    @Unique
    private boolean leonware$shouldScale(final BabyModelModule mod, final class_1309 entity) {
        final boolean isSelf = entity == SharedClass.player();
        final boolean isFriend = entity instanceof class_1657 && FriendManager.getInstance().contains(entity.method_5477().getString());
        if (mod.onlySelf.getValue()) {
            return isSelf || (isFriend && mod.friends.getValue());
        }
        return entity instanceof class_1657;
    }
    
    @Inject(method = { "render" }, at = { @At("HEAD") })
    private void onRenderCustomModelPre(final S state, final class_4587 matrices, final class_4597 vcp, final int light, final CallbackInfo ci) {
        if (!(state instanceof class_10055)) {
            return;
        }
        if (this.leonware$lastEntity == null) {
            return;
        }
        final class_583<? super S> field_4737 = this.field_4737;
        if (!(field_4737 instanceof class_591)) {
            return;
        }
        final class_591 pm = (class_591)field_4737;
        final CustomModelModule mod = CustomModelModule.getInstance();
        if (!mod.isEnabled()) {
            return;
        }
        if (!mod.model.is("CrazyRabbit") && !mod.model.is("Freddy Bear") && !mod.model.is("Amogus") && !mod.model.is("Leon 2D")) {
            return;
        }
        final class_310 mc = class_310.method_1551();
        if (mc.field_1724 == null) {
            return;
        }
        final boolean isSelf = this.leonware$lastEntity == mc.field_1724;
        final class_1309 leonware$lastEntity = this.leonware$lastEntity;
        boolean b = false;
        Label_0190: {
            if (leonware$lastEntity instanceof final class_1657 p) {
                if (FriendManager.getInstance().contains(p.method_5477().getString())) {
                    b = true;
                    break Label_0190;
                }
            }
            b = false;
        }
        final boolean isFriend = b;
        if (!isSelf && (!mod.friends.getValue() || !isFriend)) {
            return;
        }
        pm.field_3398.field_3665 = false;
        pm.field_3394.field_3665 = false;
        pm.field_3391.field_3665 = false;
        pm.field_3483.field_3665 = false;
        pm.field_27433.field_3665 = false;
        pm.field_3484.field_3665 = false;
        pm.field_3401.field_3665 = false;
        pm.field_3486.field_3665 = false;
        pm.field_3397.field_3665 = false;
        pm.field_3482.field_3665 = false;
        pm.field_3392.field_3665 = false;
        pm.field_3479.field_3665 = false;
        this.leonware$customModelHidden = true;
    }
    
    @Inject(method = { "render" }, at = { @At("RETURN") })
    private void onRenderCustomModelPost(final S state, final class_4587 matrices, final class_4597 vcp, final int light, final CallbackInfo ci) {
        if (!this.leonware$customModelHidden) {
            return;
        }
        final class_583<? super S> field_4737 = this.field_4737;
        if (field_4737 instanceof final class_591 pm) {
            pm.field_3398.field_3665 = true;
            pm.field_3394.field_3665 = true;
            pm.field_3391.field_3665 = true;
            pm.field_3483.field_3665 = true;
            pm.field_27433.field_3665 = true;
            pm.field_3484.field_3665 = true;
            pm.field_3401.field_3665 = true;
            pm.field_3486.field_3665 = true;
            pm.field_3397.field_3665 = true;
            pm.field_3482.field_3665 = true;
            pm.field_3392.field_3665 = true;
            pm.field_3479.field_3665 = true;
        }
        this.leonware$customModelHidden = false;
    }
    
    static {
        LEONWARE_CHAMS_LAYER = class_156.method_34866(texture -> class_1921.method_24048("leonware_chams", class_290.field_1580, class_293.class_5596.field_27382, 1536, class_1921.class_4688.method_23598().method_34578(class_4668.field_29452).method_34577((class_4668.class_5939)new class_4668.class_4683(texture, class_9851.field_52395, false)).method_23615(class_4668.field_21364).method_23603(class_4668.field_21345).method_23608(class_4668.field_21383).method_23611(class_4668.field_21385).method_23604(class_4668.field_21346).method_23617(false)));
    }
}
