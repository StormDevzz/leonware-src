/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyExpressionValue
 *  net.minecraft.class_1294
 *  net.minecraft.class_1297
 *  net.minecraft.class_1304
 *  net.minecraft.class_1309
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_2338
 *  net.minecraft.class_2374
 *  net.minecraft.class_2404
 *  net.minecraft.class_243
 *  net.minecraft.class_2596
 *  net.minecraft.class_2848
 *  net.minecraft.class_2848$class_2849
 *  net.minecraft.class_310
 *  net.minecraft.class_3417
 *  net.minecraft.class_3419
 *  net.minecraft.class_634
 *  net.minecraft.class_746
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.ModifyVariable
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package sweetie.leonware.inject.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1304;
import net.minecraft.class_1309;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2338;
import net.minecraft.class_2374;
import net.minecraft.class_2404;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2848;
import net.minecraft.class_310;
import net.minecraft.class_3417;
import net.minecraft.class_3419;
import net.minecraft.class_634;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sweetie.leonware.api.event.events.player.move.JumpEvent;
import sweetie.leonware.api.system.backend.SharedClass;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.utils.rotation.manager.RotationPlan;
import sweetie.leonware.client.features.modules.movement.ElytraRecastModule;
import sweetie.leonware.client.features.modules.movement.WaterSpeedModule;
import sweetie.leonware.client.features.modules.player.NoPushEntityModule;
import sweetie.leonware.client.features.modules.render.SwingAnimationModule;
import sweetie.leonware.inject.entity.MixinEntity;

@Mixin(value={class_1309.class})
public abstract class MixinLivingEntity
extends MixinEntity {
    @Shadow
    public int field_6228;
    @Unique
    private boolean previousElytra = false;
    @Unique
    private boolean awaitingElytra = false;

    @Shadow
    public abstract boolean method_6128();

    @Unique
    private boolean lew$castElytra(class_746 player) {
        if (this.lew$checkElytra(player) && this.lew$checkFallFlyingIgnoreGround(player)) {
            class_634 networkHandler = player.field_3944;
            if (networkHandler != null) {
                networkHandler.method_52787((class_2596)new class_2848((class_1297)player, class_2848.class_2849.field_12982));
            }
            return true;
        }
        return false;
    }

    @Unique
    private boolean lew$checkElytra(class_746 player) {
        if (player.field_3913.field_54155.comp_3163() && !player.method_31549().field_7479 && !player.method_5765() && !player.method_6101()) {
            class_1799 itemStack = player.method_6118(class_1304.field_6174);
            return itemStack.method_31574(class_1802.field_8833) && class_1309.method_63624((class_1799)itemStack, (class_1304)class_1304.field_6174);
        }
        return false;
    }

    @Unique
    private boolean lew$checkFallFlyingIgnoreGround(class_746 player) {
        if (!player.method_5799() && !player.method_6059(class_1294.field_5902)) {
            class_1799 itemStack = player.method_6118(class_1304.field_6174);
            if (itemStack.method_31574(class_1802.field_8833) && class_1309.method_63624((class_1799)itemStack, (class_1304)class_1304.field_6174)) {
                player.method_23669();
                return true;
            }
            return false;
        }
        return false;
    }

    @Inject(method={"tickMovement"}, at={@At(value="HEAD")})
    public void reduceCooldown(CallbackInfo ci) {
        if (this != SharedClass.player()) {
            return;
        }
        ElytraRecastModule mod = ElytraRecastModule.getInstance();
        if (mod.isEnabled() && ((Boolean)mod.exploit.getValue()).booleanValue() && this.field_6228 > 0) {
            this.field_6228 = 0;
        }
    }

    @Inject(method={"tickMovement"}, at={@At(value="TAIL")})
    public void recastIfLanded(CallbackInfo ci) {
        if (class_310.method_1551().field_1724 == null || !(this instanceof class_746)) {
            return;
        }
        ElytraRecastModule mod = ElytraRecastModule.getInstance();
        if (!mod.isEnabled() || !((Boolean)mod.exploit.getValue()).booleanValue()) {
            return;
        }
        class_746 player = (class_746)this;
        boolean elytra = this.method_6128();
        if (this.awaitingElytra) {
            if (elytra) {
                this.awaitingElytra = false;
            }
        } else if (!elytra && this.previousElytra) {
            class_310.method_1551().method_1483().method_4875(class_3417.field_14572.comp_3319(), class_3419.field_15248);
            this.lew$castElytra(player);
            this.awaitingElytra = this.lew$checkElytra(player);
        }
        this.previousElytra = elytra;
    }

    @Inject(method={"getHandSwingDuration"}, at={@At(value="HEAD")}, cancellable=true)
    private void getArmSwingAnimationEnd(CallbackInfoReturnable<Integer> callbackInfoReturnable) {
        SwingAnimationModule swingAnim = SwingAnimationModule.getInstance();
        if (((Boolean)swingAnim.slow.getValue()).booleanValue() && swingAnim.isEnabled()) {
            callbackInfoReturnable.setReturnValue((Object)((Float)swingAnim.speed.getValue()).intValue());
        }
    }

    @Inject(method={"jump"}, at={@At(value="INVOKE", target="Lnet/minecraft/entity/LivingEntity;getJumpVelocity()F")})
    public void onJumping(CallbackInfo ci) {
        if (this != SharedClass.player()) {
            return;
        }
        JumpEvent.getInstance().call();
    }

    @Inject(method={"pushAwayFrom"}, at={@At(value="HEAD")}, cancellable=true)
    private void noPushHookByEntity(CallbackInfo ci) {
        if (NoPushEntityModule.getInstance().cancelPush(NoPushEntityModule.PushingSource.ENTITY)) {
            ci.cancel();
        }
    }

    @ModifyExpressionValue(method={"jump"}, at={@At(value="INVOKE", target="Lnet/minecraft/entity/LivingEntity;getYaw()F")})
    public float fixJumpVelocity(float original) {
        if (this != SharedClass.player()) {
            return original;
        }
        RotationManager rotationManager = RotationManager.getInstance();
        Rotation rotation = rotationManager.getRotation();
        RotationPlan currentRotationPlan = rotationManager.getCurrentRotationPlan();
        if (currentRotationPlan == null || !currentRotationPlan.moveCorrection()) {
            return original;
        }
        return rotation.getYaw();
    }

    @ModifyExpressionValue(method={"calcGlidingVelocity"}, at={@At(value="INVOKE", target="Lnet/minecraft/entity/LivingEntity;getPitch()F")})
    private float fixGlidingVelocity(float original) {
        if (this != SharedClass.player()) {
            return original;
        }
        RotationManager rotationManager = RotationManager.getInstance();
        Rotation rotation = rotationManager.getRotation();
        RotationPlan currentRotationPlan = rotationManager.getCurrentRotationPlan();
        if (currentRotationPlan == null || !currentRotationPlan.moveCorrection()) {
            return original;
        }
        return rotation.getPitch();
    }

    @ModifyExpressionValue(method={"calcGlidingVelocity"}, at={@At(value="INVOKE", target="Lnet/minecraft/entity/LivingEntity;getRotationVector()Lnet/minecraft/util/math/Vec3d;")})
    private class_243 fixGlidingVelocityVector(class_243 original) {
        if (this != SharedClass.player()) {
            return original;
        }
        RotationManager rotationManager = RotationManager.getInstance();
        Rotation rotation = rotationManager.getRotation();
        RotationPlan currentRotationPlan = rotationManager.getCurrentRotationPlan();
        if (currentRotationPlan == null || !currentRotationPlan.moveCorrection()) {
            return original;
        }
        return rotation.getVector();
    }

    @ModifyVariable(method={"setSprinting"}, at=@At(value="HEAD"), argsOnly=true, ordinal=0)
    private boolean setSprintingHook(boolean sprinting) {
        boolean inWater;
        if (QuickImports.mc.field_1724 == null || QuickImports.mc.field_1687 == null) {
            return sprinting;
        }
        WaterSpeedModule mod = WaterSpeedModule.getInstance();
        if (!mod.isEnabled() || !mod.getMode().is("SprintE")) {
            return sprinting;
        }
        boolean bl = inWater = QuickImports.mc.field_1724.method_5799() || QuickImports.mc.field_1687.method_8320(class_2338.method_49638((class_2374)QuickImports.mc.field_1724.method_19538().method_1031(0.0, -0.5, 0.0))).method_26204() instanceof class_2404;
        if (!inWater) {
            return sprinting;
        }
        boolean forward = QuickImports.mc.field_1724.field_3913.field_3905 > 0.0f;
        boolean backward = QuickImports.mc.field_1724.field_3913.field_3905 < 0.0f;
        return forward && !backward;
    }
}

