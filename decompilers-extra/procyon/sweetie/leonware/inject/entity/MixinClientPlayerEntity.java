// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.entity;

import sweetie.leonware.client.features.modules.player.MultiTaskModule;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.Unique;
import sweetie.leonware.api.utils.player.DirectionalInput;
import sweetie.leonware.api.event.events.player.move.SprintEvent;
import sweetie.leonware.api.event.events.player.other.CloseScreenEvent;
import sweetie.leonware.client.features.modules.player.NoPushEntityModule;
import sweetie.leonware.api.event.events.player.move.MotionEvent;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationPlan;
import sweetie.leonware.api.system.backend.SharedClass;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.event.events.player.move.MoveEvent;
import net.minecraft.class_243;
import net.minecraft.class_1313;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import org.spongepowered.asm.mixin.injection.Inject;
import sweetie.leonware.api.event.events.player.move.PostMotionEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import sweetie.leonware.client.features.modules.movement.noslow.NoSlowModule;
import com.mojang.authlib.GameProfile;
import net.minecraft.class_638;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.class_744;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.class_742;

@Mixin({ class_746.class })
public class MixinClientPlayerEntity extends class_742
{
    @Shadow
    public class_744 field_3913;
    
    public MixinClientPlayerEntity(final class_638 world, final GameProfile profile) {
        super(world, profile);
    }
    
    @Redirect(method = { "tickMovement" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"), require = 0)
    private boolean tickMovementHook(final class_746 player) {
        final boolean isUsingItem = player.method_6115();
        if (!isUsingItem) {
            return false;
        }
        final NoSlowModule module = NoSlowModule.getInstance();
        if (!module.isEnabled()) {
            return true;
        }
        if (module.getMode().is("V3")) {
            return player.field_6012 % 2 != 0 || player.field_3913.field_54155.comp_3164();
        }
        return !module.doUseNoSlow();
    }
    
    @Inject(method = { "sendMovementPackets" }, at = { @At("TAIL") })
    private void leonware$onPostMotion(final CallbackInfo ci) {
        PostMotionEvent.getInstance().call();
    }
    
    @Inject(method = { "tick" }, at = { @At("HEAD") })
    public void tickHook(final CallbackInfo ci) {
        UpdateEvent.getInstance().call();
    }
    
    @Inject(method = { "move" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V") }, cancellable = true)
    public void moveHook(final class_1313 movementType, final class_243 movement, final CallbackInfo ci) {
        final MoveEvent.MoveEventData event = new MoveEvent.MoveEventData(movement.field_1352, movement.field_1351, movement.field_1350);
        if (MoveEvent.getInstance().call(event)) {
            super.method_5784(movementType, new class_243(event.getX(), event.getY(), event.getZ()));
            ci.cancel();
        }
    }
    
    @ModifyExpressionValue(method = { "sendMovementPackets", "tick" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getYaw()F") })
    private float silentRotationYaw(final float original) {
        final RotationManager rotationManager = RotationManager.getInstance();
        final RotationPlan currentRotationPlan = rotationManager.getCurrentRotationPlan();
        if (currentRotationPlan == null) {
            return original;
        }
        final Rotation rotation = rotationManager.getRotation();
        if (SharedClass.player() != null) {
            final float yaw = rotation.getYaw();
            this.method_5847(yaw);
            this.method_5636(yaw);
        }
        return rotation.getYaw();
    }
    
    @ModifyExpressionValue(method = { "sendMovementPackets", "tick" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getPitch()F") })
    private float silentRotationPitch(final float original) {
        final RotationPlan rotation = RotationManager.getInstance().getCurrentRotationPlan();
        if (rotation == null) {
            return original;
        }
        final Rotation rot = RotationManager.getInstance().getRotation();
        return rot.getPitch();
    }
    
    @Inject(method = { "sendMovementPackets" }, at = { @At("HEAD") }, cancellable = true)
    private void preMotion(final CallbackInfo ci) {
        final MotionEvent.MotionEventData event = new MotionEvent.MotionEventData(this.method_23317(), this.method_23318(), this.method_23321(), this.method_5705(1.0f), this.method_5695(1.0f), this.method_24828());
        if (MotionEvent.getInstance().call(event)) {
            ci.cancel();
        }
    }
    
    @ModifyExpressionValue(method = { "tickMovement" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;canSprint()Z") })
    private boolean sprintEventTick(final boolean original) {
        return this.sprintHook(original);
    }
    
    @ModifyExpressionValue(method = { "tickMovement" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/option/KeyBinding;isPressed()Z") })
    private boolean sprintEventInput(final boolean original) {
        return this.sprintHook(original);
    }
    
    @Inject(method = { "pushOutOfBlocks" }, at = { @At("HEAD") }, cancellable = true)
    private void noPushByBlocksHook(final double x, final double d, final CallbackInfo ci) {
        if (NoPushEntityModule.getInstance().cancelPush(NoPushEntityModule.PushingSource.BLOCK)) {
            ci.cancel();
        }
    }
    
    @ModifyExpressionValue(method = { "tickMovement" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z") }, require = 0)
    private boolean tickMovementHook(final boolean original) {
        return !NoSlowModule.getInstance().doUseNoSlow() && original;
    }
    
    @ModifyExpressionValue(method = { "canStartSprinting" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z") })
    private boolean sprintAffectStartHook(final boolean original) {
        return !NoSlowModule.getInstance().isEnabled() && original;
    }
    
    @Inject(method = { "closeHandledScreen" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V") }, cancellable = true)
    private void onCloseHandledScreen(final CallbackInfo ci) {
        if (CloseScreenEvent.getInstance().call()) {
            ci.cancel();
        }
    }
    
    @Unique
    private boolean sprintHook(final boolean origin) {
        return SprintEvent.getInstance().call(new SprintEvent.SprintEventData(new DirectionalInput(this.field_3913)));
    }
    
    @Inject(method = { "isUsingItem" }, at = { @At("HEAD") }, cancellable = true)
    private void onIsUsingItem(final CallbackInfoReturnable<Boolean> cir) {
        final MultiTaskModule module = MultiTaskModule.getInstance();
        if (!module.isEnabled()) {
            return;
        }
        final StackTraceElement[] stackTrace;
        final StackTraceElement[] stack = stackTrace = Thread.currentThread().getStackTrace();
        for (final StackTraceElement el : stackTrace) {
            final String method = el.getMethodName();
            final String cls = el.getClassName();
            if (cls.contains("ClientPlayerInteractionManager")) {
                if (method.contains("attackEntity") && module.allowAttack()) {
                    cir.setReturnValue((Object)false);
                    return;
                }
                if ((method.contains("updateBlockBreakingProgress") || method.contains("breakBlock") || method.contains("startBreakingBlock")) && module.allowBreak()) {
                    cir.setReturnValue((Object)false);
                    return;
                }
                if (method.contains("interactBlock") && module.allowInteract()) {
                    cir.setReturnValue((Object)false);
                    return;
                }
            }
        }
    }
}
