/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyExpressionValue
 *  com.llamalad7.mixinextras.sugar.Local
 *  net.minecraft.class_10042
 *  net.minecraft.class_10055
 *  net.minecraft.class_1309
 *  net.minecraft.class_156
 *  net.minecraft.class_1657
 *  net.minecraft.class_1921
 *  net.minecraft.class_1921$class_4688
 *  net.minecraft.class_290
 *  net.minecraft.class_293
 *  net.minecraft.class_293$class_5596
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  net.minecraft.class_3532
 *  net.minecraft.class_4587
 *  net.minecraft.class_4588
 *  net.minecraft.class_4597
 *  net.minecraft.class_4597$class_4598
 *  net.minecraft.class_4668
 *  net.minecraft.class_4668$class_4683
 *  net.minecraft.class_4668$class_5939
 *  net.minecraft.class_572
 *  net.minecraft.class_583
 *  net.minecraft.class_591
 *  net.minecraft.class_922
 *  net.minecraft.class_9851
 *  org.jetbrains.annotations.Nullable
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.Redirect
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
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
import sweetie.leonware.client.features.modules.render.CustomModelModule;

@Mixin(value={class_922.class})
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
    private static final Function<class_2960, class_1921> LEONWARE_CHAMS_LAYER = class_156.method_34866(texture -> class_1921.method_24048((String)"leonware_chams", (class_293)class_290.field_1580, (class_293.class_5596)class_293.class_5596.field_27382, (int)1536, (class_1921.class_4688)class_1921.class_4688.method_23598().method_34578(class_4668.field_29452).method_34577((class_4668.class_5939)new class_4668.class_4683(texture, class_9851.field_52395, false)).method_23615(class_4668.field_21364).method_23603(class_4668.field_21345).method_23608(class_4668.field_21383).method_23611(class_4668.field_21385).method_23604(class_4668.field_21346).method_23617(false)));

    @Shadow
    public abstract class_2960 method_3885(S var1);

    @Inject(method={"updateRenderState"}, at={@At(value="HEAD")})
    private void captureEntity(T entity, S state, float tickDelta, CallbackInfo ci) {
        this.leonware$lastEntity = entity;
    }

    @ModifyExpressionValue(method={"updateRenderState*"}, at={@At(value="INVOKE", target="Lnet/minecraft/entity/LivingEntity;getLerpedPitch(F)F")})
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
        if (currentRotationPlan == null) {
            return original;
        }
        return class_3532.method_17821((float)tickDelta, (float)rotationPrev.getPitch(), (float)rotation.getPitch());
    }

    @ModifyExpressionValue(method={"updateRenderState*"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/render/entity/LivingEntityRenderer;clampBodyYaw(Lnet/minecraft/entity/LivingEntity;FF)F")})
    private float updateVisualBodyYaw(float original, class_1309 entity, S state, float tickDelta) {
        Rotation fake;
        if (entity != SharedClass.player()) {
            return original;
        }
        if (MoveFixModule.shouldUseFakeRotation() && (fake = MoveFixModule.getFakeRotation()) != null) {
            return fake.getYaw();
        }
        RotationManager rm = RotationManager.getInstance();
        if (rm.getCurrentRotationPlan() == null) {
            return original;
        }
        return class_3532.method_17821((float)tickDelta, (float)rm.getPreviousRotation().getYaw(), (float)rm.getRotation().getYaw());
    }

    @ModifyExpressionValue(method={"updateRenderState*"}, at={@At(value="INVOKE", target="Lnet/minecraft/util/math/MathHelper;lerpAngleDegrees(FFF)F")})
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
        if (currentRotationPlan == null) {
            return original;
        }
        return class_3532.method_17821((float)tickDelta, (float)rotationPrev.getYaw(), (float)rotation.getYaw());
    }

    @Shadow
    @Nullable
    protected abstract class_1921 method_24302(S var1, boolean var2, boolean var3, boolean var4);

    @Redirect(method={"render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/render/entity/LivingEntityRenderer;getRenderLayer(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;ZZZ)Lnet/minecraft/client/render/RenderLayer;"))
    private class_1921 renderHook(class_922 instance, S state, boolean showBody, boolean translucent, boolean showOutline) {
        if (!translucent && ((class_10042)state).field_53329 == 0.6f) {
            int defaultColor = -1;
            EntityColorEvent.EntityColorEventData eventData = new EntityColorEvent.EntityColorEventData(defaultColor);
            boolean modified = EntityColorEvent.getInstance().call(eventData);
            if (modified && eventData.color() != defaultColor) {
                translucent = true;
            }
        }
        return this.method_24302(state, showBody, translucent, showOutline);
    }

    @Redirect(method={"render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;III)V"))
    private void renderModelHook(class_583<?> instance, class_4587 matrixStack, class_4588 vertexConsumer, int light, int overlay, int color, @Local(ordinal=0, argsOnly=true) class_10042 renderState, @Local(argsOnly=true) class_4597 vcp, @Local class_1921 renderLayer) {
        EntityColorEvent.EntityColorEventData event = new EntityColorEvent.EntityColorEventData(color);
        if (renderState.field_53461) {
            EntityColorEvent.getInstance().call(event);
        }
        int finalColor = event.color();
        ChamsModule chams = ChamsModule.getInstance();
        if (chams.isEnabled() && this.leonware$isChamsTarget(renderState)) {
            class_2960 texture = this.method_3885(renderState);
            class_4597.class_4598 entityConsumers = class_310.method_1551().method_22940().method_23000();
            class_4588 hiddenConsumer = entityConsumers.getBuffer(LEONWARE_CHAMS_LAYER.apply(texture));
            instance.method_62100(matrixStack, hiddenConsumer, light, overlay, chams.getHiddenArgb());
            entityConsumers.method_22994(LEONWARE_CHAMS_LAYER.apply(texture));
            class_4588 freshConsumer = vcp.getBuffer(renderLayer);
            instance.method_62100(matrixStack, freshConsumer, light, overlay, chams.getVisibleArgb());
        } else {
            instance.method_62100(matrixStack, vertexConsumer, light, overlay, finalColor);
        }
    }

    @Unique
    private boolean leonware$isChamsTarget(class_10042 renderState) {
        ChamsModule chams = ChamsModule.getInstance();
        if (chams.isOnlyPlayers()) {
            return renderState instanceof class_10055;
        }
        return true;
    }

    @Inject(method={"render"}, at={@At(value="HEAD")})
    private void onRenderBabyPre(S state, class_4587 matrixStack, class_4597 vertexConsumerProvider, int light, CallbackInfo ci) {
        BabyModelModule mod = BabyModelModule.getInstance();
        if (!mod.isEnabled()) {
            return;
        }
        if (state instanceof class_10055 && this.leonware$lastEntity != null && this.leonware$shouldScale(mod, (class_1309)this.leonware$lastEntity)) {
            float scale = ((Float)mod.allScale.getValue()).floatValue();
            float hScale = ((Float)mod.headScale.getValue()).floatValue();
            matrixStack.method_22903();
            matrixStack.method_22905(scale, scale, scale);
            BabyModelModule.currentShouldScale = true;
            this.leonware$isBabyActive = true;
            M m = this.field_4737;
            if (m instanceof class_572) {
                class_572 bipedModel = (class_572)m;
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

    @Inject(method={"render"}, at={@At(value="HEAD")})
    private void onRenderBadTripHead(S state, class_4587 matrices, class_4597 vertexConsumers, int light, CallbackInfo ci) {
        BadtripModule mod = BadtripModule.getInstance();
        if (mod.isEnabled() && this.leonware$lastEntity != null && ((class_1309)this.leonware$lastEntity).field_6235 > 0) {
            matrices.method_22903();
            this.leonware$badTripActive = true;
            float tickDelta = class_310.method_1551().method_61966().method_60637(false);
            float smoothHurt = (float)((class_1309)this.leonware$lastEntity).field_6235 - tickDelta;
            float hurtProgress = smoothHurt / 10.0f;
            float anim = 1.0f - (float)Math.pow(1.0f - (1.0f - hurtProgress), 3.0);
            float strength = ((Float)mod.strength.getValue()).floatValue();
            float scaleXZ = 1.0f + strength * (1.0f - anim);
            float scaleY = Math.max(0.4f, anim);
            matrices.method_22905(scaleXZ, scaleY, scaleXZ);
        }
    }

    @Inject(method={"render"}, at={@At(value="RETURN")})
    private void onRenderAllPost(S state, class_4587 matrices, class_4597 vertexConsumers, int light, CallbackInfo ci) {
        if (this.leonware$isBabyActive) {
            M m = this.field_4737;
            if (m instanceof class_572) {
                class_572 bipedModel = (class_572)m;
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
        boolean isFriend;
        boolean isSelf = entity == SharedClass.player();
        boolean bl = isFriend = entity instanceof class_1657 && FriendManager.getInstance().contains(entity.method_5477().getString());
        if (((Boolean)mod.onlySelf.getValue()).booleanValue()) {
            return isSelf || isFriend && (Boolean)mod.friends.getValue() != false;
        }
        return entity instanceof class_1657;
    }

    /*
     * Unable to fully structure code
     */
    @Inject(method={"render"}, at={@At(value="HEAD")})
    private void onRenderCustomModelPre(S state, class_4587 matrices, class_4597 vcp, int light, CallbackInfo ci) {
        if (!(state instanceof class_10055)) {
            return;
        }
        if (this.leonware$lastEntity == null) {
            return;
        }
        var7_6 = this.field_4737;
        if (!(var7_6 instanceof class_591)) {
            return;
        }
        pm = (class_591)var7_6;
        mod = CustomModelModule.getInstance();
        if (!mod.isEnabled()) {
            return;
        }
        if (!(mod.model.is("CrazyRabbit") || mod.model.is("Freddy Bear") || mod.model.is("Amogus") || mod.model.is("Leon 2D"))) {
            return;
        }
        mc = class_310.method_1551();
        if (mc.field_1724 == null) {
            return;
        }
        isSelf = this.leonware$lastEntity == mc.field_1724;
        var12_10 = this.leonware$lastEntity;
        if (!(var12_10 instanceof class_1657)) ** GOTO lbl-1000
        p = (class_1657)var12_10;
        if (FriendManager.getInstance().contains(p.method_5477().getString())) {
            v0 = true;
        } else lbl-1000:
        // 2 sources

        {
            v0 = isFriend = false;
        }
        if (!(isSelf || ((Boolean)mod.friends.getValue()).booleanValue() && isFriend)) {
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

    @Inject(method={"render"}, at={@At(value="RETURN")})
    private void onRenderCustomModelPost(S state, class_4587 matrices, class_4597 vcp, int light, CallbackInfo ci) {
        if (!this.leonware$customModelHidden) {
            return;
        }
        M m = this.field_4737;
        if (m instanceof class_591) {
            class_591 pm = (class_591)m;
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

