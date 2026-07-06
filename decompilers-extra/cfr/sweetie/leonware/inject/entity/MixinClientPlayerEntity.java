/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyExpressionValue
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.class_1313
 *  net.minecraft.class_243
 *  net.minecraft.class_638
 *  net.minecraft.class_742
 *  net.minecraft.class_744
 *  net.minecraft.class_746
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.Redirect
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package sweetie.leonware.inject.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.authlib.GameProfile;
import net.minecraft.class_1313;
import net.minecraft.class_243;
import net.minecraft.class_638;
import net.minecraft.class_742;
import net.minecraft.class_744;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sweetie.leonware.api.event.events.player.move.MotionEvent;
import sweetie.leonware.api.event.events.player.move.MoveEvent;
import sweetie.leonware.api.event.events.player.move.PostMotionEvent;
import sweetie.leonware.api.event.events.player.move.SprintEvent;
import sweetie.leonware.api.event.events.player.other.CloseScreenEvent;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.system.backend.SharedClass;
import sweetie.leonware.api.utils.player.DirectionalInput;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.utils.rotation.manager.RotationPlan;
import sweetie.leonware.client.features.modules.movement.noslow.NoSlowModule;
import sweetie.leonware.client.features.modules.player.MultiTaskModule;
import sweetie.leonware.client.features.modules.player.NoPushEntityModule;

@Mixin(value={class_746.class})
public class MixinClientPlayerEntity
extends class_742 {
    @Shadow
    public class_744 field_3913;

    public MixinClientPlayerEntity(class_638 world, GameProfile profile) {
        super(world, profile);
    }

    @Redirect(method={"tickMovement"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"), require=0)
    private boolean tickMovementHook(class_746 player) {
        boolean isUsingItem = player.method_6115();
        if (!isUsingItem) {
            return false;
        }
        NoSlowModule module = NoSlowModule.getInstance();
        if (!module.isEnabled()) {
            return true;
        }
        if (module.getMode().is("V3")) {
            return player.field_6012 % 2 != 0 || player.field_3913.field_54155.comp_3164();
        }
        return !module.doUseNoSlow();
    }

    @Inject(method={"sendMovementPackets"}, at={@At(value="TAIL")})
    private void leonware$onPostMotion(CallbackInfo ci) {
        PostMotionEvent.getInstance().call();
    }

    @Inject(method={"tick"}, at={@At(value="HEAD")})
    public void tickHook(CallbackInfo ci) {
        UpdateEvent.getInstance().call();
    }

    @Inject(method={"move"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/network/AbstractClientPlayerEntity;move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V")}, cancellable=true)
    public void moveHook(class_1313 movementType, class_243 movement, CallbackInfo ci) {
        MoveEvent.MoveEventData event = new MoveEvent.MoveEventData(movement.field_1352, movement.field_1351, movement.field_1350);
        if (MoveEvent.getInstance().call(event)) {
            super.method_5784(movementType, new class_243(event.getX(), event.getY(), event.getZ()));
            ci.cancel();
        }
    }

    @ModifyExpressionValue(method={"sendMovementPackets", "tick"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;getYaw()F")})
    private float silentRotationYaw(float original) {
        RotationManager rotationManager = RotationManager.getInstance();
        RotationPlan currentRotationPlan = rotationManager.getCurrentRotationPlan();
        if (currentRotationPlan == null) {
            return original;
        }
        Rotation rotation = rotationManager.getRotation();
        if (SharedClass.player() != null) {
            float yaw = rotation.getYaw();
            this.method_5847(yaw);
            this.method_5636(yaw);
        }
        return rotation.getYaw();
    }

    @ModifyExpressionValue(method={"sendMovementPackets", "tick"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;getPitch()F")})
    private float silentRotationPitch(float original) {
        RotationPlan rotation = RotationManager.getInstance().getCurrentRotationPlan();
        if (rotation == null) {
            return original;
        }
        Rotation rot = RotationManager.getInstance().getRotation();
        return rot.getPitch();
    }

    @Inject(method={"sendMovementPackets"}, at={@At(value="HEAD")}, cancellable=true)
    private void preMotion(CallbackInfo ci) {
        MotionEvent.MotionEventData event = new MotionEvent.MotionEventData(this.method_23317(), this.method_23318(), this.method_23321(), this.method_5705(1.0f), this.method_5695(1.0f), this.method_24828());
        if (MotionEvent.getInstance().call(event)) {
            ci.cancel();
        }
    }

    @ModifyExpressionValue(method={"tickMovement"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;canSprint()Z")})
    private boolean sprintEventTick(boolean original) {
        return this.sprintHook(original);
    }

    @ModifyExpressionValue(method={"tickMovement"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/option/KeyBinding;isPressed()Z")})
    private boolean sprintEventInput(boolean original) {
        return this.sprintHook(original);
    }

    @Inject(method={"pushOutOfBlocks"}, at={@At(value="HEAD")}, cancellable=true)
    private void noPushByBlocksHook(double x, double d, CallbackInfo ci) {
        if (NoPushEntityModule.getInstance().cancelPush(NoPushEntityModule.PushingSource.BLOCK)) {
            ci.cancel();
        }
    }

    @ModifyExpressionValue(method={"tickMovement"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z")}, require=0)
    private boolean tickMovementHook(boolean original) {
        if (NoSlowModule.getInstance().doUseNoSlow()) {
            return false;
        }
        return original;
    }

    @ModifyExpressionValue(method={"canStartSprinting"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z")})
    private boolean sprintAffectStartHook(boolean original) {
        if (NoSlowModule.getInstance().isEnabled()) {
            return false;
        }
        return original;
    }

    @Inject(method={"closeHandledScreen"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V")}, cancellable=true)
    private void onCloseHandledScreen(CallbackInfo ci) {
        if (CloseScreenEvent.getInstance().call()) {
            ci.cancel();
        }
    }

    @Unique
    private boolean sprintHook(boolean origin) {
        return SprintEvent.getInstance().call(new SprintEvent.SprintEventData(new DirectionalInput(this.field_3913)));
    }

    @Inject(method={"isUsingItem"}, at={@At(value="HEAD")}, cancellable=true)
    private void onIsUsingItem(CallbackInfoReturnable<Boolean> cir) {
        StackTraceElement[] stack;
        MultiTaskModule module = MultiTaskModule.getInstance();
        if (!module.isEnabled()) {
            return;
        }
        for (StackTraceElement el : stack = Thread.currentThread().getStackTrace()) {
            String method = el.getMethodName();
            String cls = el.getClassName();
            if (!cls.contains("ClientPlayerInteractionManager")) continue;
            if (method.contains("attackEntity") && module.allowAttack()) {
                cir.setReturnValue((Object)false);
                return;
            }
            if ((method.contains("updateBlockBreakingProgress") || method.contains("breakBlock") || method.contains("startBreakingBlock")) && module.allowBreak()) {
                cir.setReturnValue((Object)false);
                return;
            }
            if (!method.contains("interactBlock") || !module.allowInteract()) continue;
            cir.setReturnValue((Object)false);
            return;
        }
    }
}

