// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.input;

import org.spongepowered.asm.mixin.Unique;
import net.minecraft.class_243;
import sweetie.leonware.api.utils.rotation.manager.RotationPlan;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import net.minecraft.class_746;
import sweetie.leonware.api.utils.player.MoveUtil;
import net.minecraft.class_3532;
import sweetie.leonware.client.features.modules.combat.AuraModule;
import sweetie.leonware.client.features.modules.movement.MoveFixModule;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.system.backend.SharedClass;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import sweetie.leonware.api.event.events.player.move.SprintEvent;
import sweetie.leonware.api.event.events.player.other.MovementInputEvent;
import sweetie.leonware.api.utils.player.DirectionalInput;
import net.minecraft.class_10185;
import net.minecraft.class_743;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_743.class })
public class MixinKeyboardInput extends MixinInput
{
    @ModifyExpressionValue(method = { "tick" }, at = { @At(value = "NEW", target = "(ZZZZZZZ)Lnet/minecraft/util/PlayerInput;") })
    private class_10185 onTick(final class_10185 original) {
        final MovementInputEvent.MovementInputEventData movementInputEvent = new MovementInputEvent.MovementInputEventData(original, original.comp_3163(), original.comp_3164(), new DirectionalInput(original));
        MovementInputEvent.getInstance().call(movementInputEvent);
        final DirectionalInput untransformedDirectionalInput = movementInputEvent.getDirectionalInput();
        final DirectionalInput directionalInput = this.transformDirection(untransformedDirectionalInput);
        final SprintEvent.SprintEventData sprintEvent = new SprintEvent.SprintEventData(directionalInput);
        SprintEvent.getInstance().call(sprintEvent);
        this.untransformed = new class_10185(untransformedDirectionalInput.isForwards(), untransformedDirectionalInput.isBackwards(), untransformedDirectionalInput.isLeft(), untransformedDirectionalInput.isRight(), original.comp_3163(), original.comp_3164(), sprintEvent.isSprint());
        return new class_10185(directionalInput.isForwards(), directionalInput.isBackwards(), directionalInput.isLeft(), directionalInput.isRight(), movementInputEvent.isJump(), movementInputEvent.isSneak(), sprintEvent.isSprint());
    }
    
    @Unique
    private DirectionalInput transformDirection(final DirectionalInput input) {
        final class_746 player = SharedClass.player();
        final RotationManager rotationManager = RotationManager.getInstance();
        final Rotation rotation = rotationManager.getCurrentRotation();
        final RotationPlan rotationPlan = rotationManager.getCurrentRotationPlan();
        if (rotationPlan == null || rotation == null || player == null || !rotationPlan.moveCorrection()) {
            return input;
        }
        final float z = class_743.method_40218(input.isForwards(), input.isBackwards());
        final float x = class_743.method_40218(input.isLeft(), input.isRight());
        final float yaw = rotation.getYaw();
        final float direction = player.method_36454();
        if (MoveFixModule.isTargeting() && z == 1.0f && x == 0.0f) {
            final AuraModule auraModule = AuraModule.getInstance();
            final class_243 position = (auraModule.target != null) ? auraModule.target.method_19538() : null;
            if (position == null) {
                return input;
            }
            final double deltaX = position.field_1352 - player.method_23317();
            final double deltaZ = position.field_1350 - player.method_23321();
            double angleToTarget = Math.toDegrees(Math.atan2(deltaZ, deltaX)) - 90.0;
            angleToTarget = class_3532.method_15338(angleToTarget);
            float bestForward = 0.0f;
            float bestStrafe = 0.0f;
            float minDifference = Float.MAX_VALUE;
            for (float forward = -1.0f; forward <= 1.0f; ++forward) {
                for (float strafe = -1.0f; strafe <= 1.0f; ++strafe) {
                    if (forward != 0.0f || strafe != 0.0f) {
                        double moveAngle = MoveUtil.direction(yaw, forward, strafe);
                        moveAngle = Math.toDegrees(moveAngle);
                        moveAngle = class_3532.method_15338(moveAngle);
                        double difference = Math.abs(class_3532.method_15338(angleToTarget - moveAngle));
                        difference = Math.min(difference, 360.0 - difference);
                        if (difference < minDifference) {
                            minDifference = (float)difference;
                            bestForward = forward;
                            bestStrafe = strafe;
                        }
                    }
                }
            }
            return new DirectionalInput(bestForward, bestStrafe);
        }
        else {
            if (rotationPlan.freeMoveCorrection()) {
                final float deltaYaw = direction - yaw;
                final float radians = deltaYaw * 0.017453292f;
                final float newX = x * class_3532.method_15362(radians) - z * class_3532.method_15374(radians);
                final float newZ = z * class_3532.method_15362(radians) + x * class_3532.method_15374(radians);
                final int movementSideways = Math.round(newX);
                final int movementForward = Math.round(newZ);
                return new DirectionalInput((float)movementForward, (float)movementSideways);
            }
            return input;
        }
    }
}
