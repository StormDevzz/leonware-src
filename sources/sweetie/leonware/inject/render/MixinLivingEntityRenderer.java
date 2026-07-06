package sweetie.leonware.inject.render;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import java.util.function.Function;
import net.minecraft.class_10042;
import net.minecraft.class_10055;
import net.minecraft.class_1309;
import net.minecraft.class_156;
import net.minecraft.class_1657;
import net.minecraft.class_1921;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4597;
import net.minecraft.class_4668;
import net.minecraft.class_572;
import net.minecraft.class_583;
import net.minecraft.class_591;
import net.minecraft.class_922;
import net.minecraft.class_9851;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sweetie.leonware.api.event.events.render.EntityColorEvent;
import sweetie.leonware.api.system.backend.SharedClass;
import sweetie.leonware.api.system.configs.FriendManager;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.utils.rotation.manager.RotationPlan;
import sweetie.leonware.client.features.modules.movement.MoveFixModule;
import sweetie.leonware.client.features.modules.render.BabyModelModule;
import sweetie.leonware.client.features.modules.render.BadtripModule;
import sweetie.leonware.client.features.modules.render.ChamsModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/render/MixinLivingEntityRenderer.class */
@Mixin({class_922.class})
public abstract class MixinLivingEntityRenderer<T extends class_1309, S extends class_10042, M extends class_583<? super S>> {

    @Shadow
    protected M field_4737;

    @Unique
    private T leonware$lastEntity;

    @Unique
    private boolean leonware$badTripActive = false;

    @Unique
    private boolean leonware$isBabyActive = false;

    @Unique
    private boolean leonware$customModelHidden = false;

    @Unique
    private static final Function<class_2960, class_1921> LEONWARE_CHAMS_LAYER = class_156.method_34866(texture -> {
        return class_1921.method_24048("leonware_chams", class_290.field_1580, class_293.class_5596.field_27382, 1536, class_1921.class_4688.method_23598().method_34578(class_4668.field_29452).method_34577(new class_4668.class_4683(texture, class_9851.field_52395, false)).method_23615(class_4668.field_21364).method_23603(class_4668.field_21345).method_23608(class_4668.field_21383).method_23611(class_4668.field_21385).method_23604(class_4668.field_21346).method_23617(false));
    });

    @Shadow
    public abstract class_2960 method_3885(S s);

    @Shadow
    @Nullable
    protected abstract class_1921 method_24302(S s, boolean z, boolean z2, boolean z3);

    @Inject(method = {"updateRenderState"}, at = {@At("HEAD")})
    private void captureEntity(T entity, S state, float tickDelta, CallbackInfo ci) {
        this.leonware$lastEntity = entity;
    }

    @ModifyExpressionValue(method = {"updateRenderState*"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getLerpedPitch(F)F")})
    private float updateVisualPitch(float original, class_1309 entity, S state, float tickDelta) {
        Rotation fakeRot;
        if (entity != SharedClass.player()) {
            return original;
        }
        if (MoveFixModule.shouldUseFakeRotation() && (fakeRot = MoveFixModule.getFakeRotation()) != null) {
            return fakeRot.getPitch();
        }
        RotationManager rotationManager = RotationManager.getInstance();
        Rotation rotation = rotationManager.getRotation();
        Rotation rotationPrev = rotationManager.getPreviousRotation();
        RotationPlan currentRotationPlan = rotationManager.getCurrentRotationPlan();
        return currentRotationPlan == null ? original : class_3532.method_17821(tickDelta, rotationPrev.getPitch(), rotation.getPitch());
    }

    @ModifyExpressionValue(method = {"updateRenderState*"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;clampBodyYaw(Lnet/minecraft/entity/LivingEntity;FF)F")})
    private float updateVisualBodyYaw(float original, class_1309 entity, S state, float tickDelta) {
        Rotation fake;
        if (entity != SharedClass.player()) {
            return original;
        }
        if (MoveFixModule.shouldUseFakeRotation() && (fake = MoveFixModule.getFakeRotation()) != null) {
            return fake.getYaw();
        }
        RotationManager rm = RotationManager.getInstance();
        return rm.getCurrentRotationPlan() == null ? original : class_3532.method_17821(tickDelta, rm.getPreviousRotation().getYaw(), rm.getRotation().getYaw());
    }

    @ModifyExpressionValue(method = {"updateRenderState*"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;lerpAngleDegrees(FFF)F")})
    private float updateVisualYaw(float original, class_1309 entity, S state, float tickDelta) {
        Rotation fakeRot;
        if (entity != SharedClass.player()) {
            return original;
        }
        if (MoveFixModule.shouldUseFakeRotation() && (fakeRot = MoveFixModule.getFakeRotation()) != null) {
            return fakeRot.getYaw();
        }
        RotationManager rotationManager = RotationManager.getInstance();
        Rotation rotation = rotationManager.getRotation();
        Rotation rotationPrev = rotationManager.getPreviousRotation();
        RotationPlan currentRotationPlan = rotationManager.getCurrentRotationPlan();
        return currentRotationPlan == null ? original : class_3532.method_17821(tickDelta, rotationPrev.getYaw(), rotation.getYaw());
    }

    @Redirect(method = {"render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;getRenderLayer(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;ZZZ)Lnet/minecraft/client/render/RenderLayer;"))
    private class_1921 renderHook(class_922 instance, S state, boolean showBody, boolean translucent, boolean showOutline) {
        if (!translucent && ((class_10042) state).field_53329 == 0.6f) {
            EntityColorEvent.EntityColorEventData eventData = new EntityColorEvent.EntityColorEventData(-1);
            boolean modified = EntityColorEvent.getInstance().call(eventData);
            if (modified && eventData.color() != -1) {
                translucent = true;
            }
        }
        return method_24302(state, showBody, translucent, showOutline);
    }

    @Redirect(method = {"render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;III)V"))
    private void renderModelHook(class_583<?> instance, class_4587 matrixStack, class_4588 vertexConsumer, int light, int overlay, int color, @Local(ordinal = 0, argsOnly = true) class_10042 renderState, @Local(argsOnly = true) class_4597 vcp, @Local class_1921 renderLayer) {
        EntityColorEvent.EntityColorEventData event = new EntityColorEvent.EntityColorEventData(color);
        if (renderState.field_53461) {
            EntityColorEvent.getInstance().call(event);
        }
        int finalColor = event.color();
        ChamsModule chams = ChamsModule.getInstance();
        if (chams.isEnabled() && leonware$isChamsTarget(renderState)) {
            class_2960 texture = method_3885(renderState);
            class_4597.class_4598 entityConsumers = class_310.method_1551().method_22940().method_23000();
            class_4588 hiddenConsumer = entityConsumers.getBuffer(LEONWARE_CHAMS_LAYER.apply(texture));
            instance.method_62100(matrixStack, hiddenConsumer, light, overlay, chams.getHiddenArgb());
            entityConsumers.method_22994(LEONWARE_CHAMS_LAYER.apply(texture));
            class_4588 freshConsumer = vcp.getBuffer(renderLayer);
            instance.method_62100(matrixStack, freshConsumer, light, overlay, chams.getVisibleArgb());
            return;
        }
        instance.method_62100(matrixStack, vertexConsumer, light, overlay, finalColor);
    }

    @Unique
    private boolean leonware$isChamsTarget(class_10042 renderState) {
        ChamsModule chams = ChamsModule.getInstance();
        if (chams.isOnlyPlayers()) {
            return renderState instanceof class_10055;
        }
        return true;
    }

    @Inject(method = {"render"}, at = {@At("HEAD")})
    private void onRenderBabyPre(S state, class_4587 matrixStack, class_4597 vertexConsumerProvider, int light, CallbackInfo ci) {
        BabyModelModule mod = BabyModelModule.getInstance();
        if (mod.isEnabled() && (state instanceof class_10055) && this.leonware$lastEntity != null && leonware$shouldScale(mod, this.leonware$lastEntity)) {
            float scale = mod.allScale.getValue().floatValue();
            float hScale = mod.headScale.getValue().floatValue();
            matrixStack.method_22903();
            matrixStack.method_22905(scale, scale, scale);
            BabyModelModule.currentShouldScale = true;
            this.leonware$isBabyActive = true;
            class_572<?> class_572Var = this.field_4737;
            if (class_572Var instanceof class_572) {
                class_572<?> bipedModel = class_572Var;
                float yOffset = (hScale - 1.0f) * 4.0f;
                bipedModel.field_3398.field_3656 -= yOffset;
                bipedModel.field_3394.field_3656 -= yOffset;
                bipedModel.field_3398.field_37938 = hScale;
                bipedModel.field_3398.field_37939 = hScale;
                bipedModel.field_3398.field_37940 = hScale;
                bipedModel.field_3394.field_37938 = hScale;
                bipedModel.field_3394.field_37939 = hScale;
                bipedModel.field_3394.field_37940 = hScale;
            }
        }
    }

    @Inject(method = {"render"}, at = {@At("HEAD")})
    private void onRenderBadTripHead(S state, class_4587 matrices, class_4597 vertexConsumers, int light, CallbackInfo ci) {
        BadtripModule mod = BadtripModule.getInstance();
        if (mod.isEnabled() && this.leonware$lastEntity != null && ((class_1309) this.leonware$lastEntity).field_6235 > 0) {
            matrices.method_22903();
            this.leonware$badTripActive = true;
            float tickDelta = class_310.method_1551().method_61966().method_60637(false);
            float smoothHurt = ((class_1309) this.leonware$lastEntity).field_6235 - tickDelta;
            float hurtProgress = smoothHurt / 10.0f;
            float anim = 1.0f - ((float) Math.pow(1.0f - (1.0f - hurtProgress), 3.0d));
            float strength = mod.strength.getValue().floatValue();
            float scaleXZ = 1.0f + (strength * (1.0f - anim));
            float scaleY = Math.max(0.4f, anim);
            matrices.method_22905(scaleXZ, scaleY, scaleXZ);
        }
    }

    @Inject(method = {"render"}, at = {@At("RETURN")})
    private void onRenderAllPost(S state, class_4587 matrices, class_4597 vertexConsumers, int light, CallbackInfo ci) {
        if (this.leonware$isBabyActive) {
            class_572<?> class_572Var = this.field_4737;
            if (class_572Var instanceof class_572) {
                class_572<?> bipedModel = class_572Var;
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
    private boolean leonware$shouldScale(BabyModelModule mod, class_1309 entity) {
        boolean isSelf = entity == SharedClass.player();
        boolean isFriend = (entity instanceof class_1657) && FriendManager.getInstance().contains(entity.method_5477().getString());
        if (mod.onlySelf.getValue().booleanValue()) {
            return isSelf || (isFriend && mod.friends.getValue().booleanValue());
        }
        return entity instanceof class_1657;
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x00bd  */
    @org.spongepowered.asm.mixin.injection.Inject(method = {"render"}, at = {@org.spongepowered.asm.mixin.injection.At("HEAD")})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void onRenderCustomModelPre(S r4, net.minecraft.class_4587 r5, net.minecraft.class_4597 r6, int r7, org.spongepowered.asm.mixin.injection.callback.CallbackInfo r8) {
        /*
            Method dump skipped, instruction units count: 334
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: sweetie.leonware.inject.render.MixinLivingEntityRenderer.onRenderCustomModelPre(net.minecraft.class_10042, net.minecraft.class_4587, net.minecraft.class_4597, int, org.spongepowered.asm.mixin.injection.callback.CallbackInfo):void");
    }

    @Inject(method = {"render"}, at = {@At("RETURN")})
    private void onRenderCustomModelPost(S state, class_4587 matrices, class_4597 vcp, int light, CallbackInfo ci) {
        if (this.leonware$customModelHidden) {
            class_591 class_591Var = this.field_4737;
            if (class_591Var instanceof class_591) {
                class_591 pm = class_591Var;
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
    }
}
