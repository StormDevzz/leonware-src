// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.entity;

import org.spongepowered.asm.mixin.injection.ModifyVariable;
import net.minecraft.class_2374;
import net.minecraft.class_2338;
import net.minecraft.class_2404;
import sweetie.leonware.client.features.modules.movement.WaterSpeedModule;
import sweetie.leonware.api.system.interfaces.QuickImports;
import net.minecraft.class_243;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import sweetie.leonware.api.utils.rotation.manager.RotationPlan;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.client.features.modules.player.NoPushEntityModule;
import sweetie.leonware.api.event.events.player.move.JumpEvent;
import sweetie.leonware.client.features.modules.render.SwingAnimationModule;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.class_3419;
import net.minecraft.class_3417;
import net.minecraft.class_310;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import sweetie.leonware.client.features.modules.movement.ElytraRecastModule;
import sweetie.leonware.api.system.backend.SharedClass;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.class_1294;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1304;
import net.minecraft.class_634;
import net.minecraft.class_2596;
import net.minecraft.class_1297;
import net.minecraft.class_2848;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.class_1309;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_1309.class })
public abstract class MixinLivingEntity extends MixinEntity
{
    @Shadow
    public int field_6228;
    @Unique
    private boolean previousElytra;
    @Unique
    private boolean awaitingElytra;
    
    public MixinLivingEntity() {
        this.previousElytra = false;
        this.awaitingElytra = false;
    }
    
    @Shadow
    public abstract boolean method_6128();
    
    @Unique
    private boolean lew$castElytra(final class_746 player) {
        if (this.lew$checkElytra(player) && this.lew$checkFallFlyingIgnoreGround(player)) {
            final class_634 networkHandler = player.field_3944;
            if (networkHandler != null) {
                networkHandler.method_52787((class_2596)new class_2848((class_1297)player, class_2848.class_2849.field_12982));
            }
            return true;
        }
        return false;
    }
    
    @Unique
    private boolean lew$checkElytra(final class_746 player) {
        if (player.field_3913.field_54155.comp_3163() && !player.method_31549().field_7479 && !player.method_5765() && !player.method_6101()) {
            final class_1799 itemStack = player.method_6118(class_1304.field_6174);
            return itemStack.method_31574(class_1802.field_8833) && class_1309.method_63624(itemStack, class_1304.field_6174);
        }
        return false;
    }
    
    @Unique
    private boolean lew$checkFallFlyingIgnoreGround(final class_746 player) {
        if (player.method_5799() || player.method_6059(class_1294.field_5902)) {
            return false;
        }
        final class_1799 itemStack = player.method_6118(class_1304.field_6174);
        if (itemStack.method_31574(class_1802.field_8833) && class_1309.method_63624(itemStack, class_1304.field_6174)) {
            player.method_23669();
            return true;
        }
        return false;
    }
    
    @Inject(method = { "tickMovement" }, at = { @At("HEAD") })
    public void reduceCooldown(final CallbackInfo ci) {
        if (this != SharedClass.player()) {
            return;
        }
        final ElytraRecastModule mod = ElytraRecastModule.getInstance();
        if (mod.isEnabled() && mod.exploit.getValue() && this.field_6228 > 0) {
            this.field_6228 = 0;
        }
    }
    
    @Inject(method = { "tickMovement" }, at = { @At("TAIL") })
    public void recastIfLanded(final CallbackInfo ci) {
        if (class_310.method_1551().field_1724 == null || !(this instanceof class_746)) {
            return;
        }
        final ElytraRecastModule mod = ElytraRecastModule.getInstance();
        if (!mod.isEnabled() || !mod.exploit.getValue()) {
            return;
        }
        final class_746 player = (class_746)this;
        final boolean elytra = this.method_6128();
        if (this.awaitingElytra) {
            if (elytra) {
                this.awaitingElytra = false;
            }
        }
        else if (!elytra && this.previousElytra) {
            class_310.method_1551().method_1483().method_4875(class_3417.field_14572.comp_3319(), class_3419.field_15248);
            this.lew$castElytra(player);
            this.awaitingElytra = this.lew$checkElytra(player);
        }
        this.previousElytra = elytra;
    }
    
    @Inject(method = { "getHandSwingDuration" }, at = { @At("HEAD") }, cancellable = true)
    private void getArmSwingAnimationEnd(final CallbackInfoReturnable<Integer> callbackInfoReturnable) {
        final SwingAnimationModule swingAnim = SwingAnimationModule.getInstance();
        if (swingAnim.slow.getValue() && swingAnim.isEnabled()) {
            callbackInfoReturnable.setReturnValue((Object)swingAnim.speed.getValue().intValue());
        }
    }
    
    @Inject(method = { "jump" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getJumpVelocity()F") })
    public void onJumping(final CallbackInfo ci) {
        if (this != SharedClass.player()) {
            return;
        }
        JumpEvent.getInstance().call();
    }
    
    @Inject(method = { "pushAwayFrom" }, at = { @At("HEAD") }, cancellable = true)
    private void noPushHookByEntity(final CallbackInfo ci) {
        if (NoPushEntityModule.getInstance().cancelPush(NoPushEntityModule.PushingSource.ENTITY)) {
            ci.cancel();
        }
    }
    
    @ModifyExpressionValue(method = { "jump" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getYaw()F") })
    public float fixJumpVelocity(final float original) {
        if (this != SharedClass.player()) {
            return original;
        }
        final RotationManager rotationManager = RotationManager.getInstance();
        final Rotation rotation = rotationManager.getRotation();
        final RotationPlan currentRotationPlan = rotationManager.getCurrentRotationPlan();
        if (currentRotationPlan == null || !currentRotationPlan.moveCorrection()) {
            return original;
        }
        return rotation.getYaw();
    }
    
    @ModifyExpressionValue(method = { "calcGlidingVelocity" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getPitch()F") })
    private float fixGlidingVelocity(final float original) {
        if (this != SharedClass.player()) {
            return original;
        }
        final RotationManager rotationManager = RotationManager.getInstance();
        final Rotation rotation = rotationManager.getRotation();
        final RotationPlan currentRotationPlan = rotationManager.getCurrentRotationPlan();
        if (currentRotationPlan == null || !currentRotationPlan.moveCorrection()) {
            return original;
        }
        return rotation.getPitch();
    }
    
    @ModifyExpressionValue(method = { "calcGlidingVelocity" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getRotationVector()Lnet/minecraft/util/math/Vec3d;") })
    private class_243 fixGlidingVelocityVector(final class_243 original) {
        if (this != SharedClass.player()) {
            return original;
        }
        final RotationManager rotationManager = RotationManager.getInstance();
        final Rotation rotation = rotationManager.getRotation();
        final RotationPlan currentRotationPlan = rotationManager.getCurrentRotationPlan();
        if (currentRotationPlan == null || !currentRotationPlan.moveCorrection()) {
            return original;
        }
        return rotation.getVector();
    }
    
    @ModifyVariable(method = { "setSprinting" }, at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private boolean setSprintingHook(final boolean sprinting) {
        if (QuickImports.mc.field_1724 == null || QuickImports.mc.field_1687 == null) {
            return sprinting;
        }
        final WaterSpeedModule mod = WaterSpeedModule.getInstance();
        if (!mod.isEnabled() || !mod.getMode().is("SprintE")) {
            return sprinting;
        }
        final boolean inWater = QuickImports.mc.field_1724.method_5799() || QuickImports.mc.field_1687.method_8320(class_2338.method_49638((class_2374)QuickImports.mc.field_1724.method_19538().method_1031(0.0, -0.5, 0.0))).method_26204() instanceof class_2404;
        if (!inWater) {
            return sprinting;
        }
        final boolean forward = QuickImports.mc.field_1724.field_3913.field_3905 > 0.0f;
        final boolean backward = QuickImports.mc.field_1724.field_3913.field_3905 < 0.0f;
        return forward && !backward;
    }
}
