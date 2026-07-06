/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyExpressionValue
 *  net.minecraft.class_10185
 *  net.minecraft.class_243
 *  net.minecraft.class_3532
 *  net.minecraft.class_743
 *  net.minecraft.class_746
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 */
package sweetie.leonware.inject.input;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.class_10185;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.minecraft.class_743;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import sweetie.leonware.api.event.events.player.move.SprintEvent;
import sweetie.leonware.api.event.events.player.other.MovementInputEvent;
import sweetie.leonware.api.system.backend.SharedClass;
import sweetie.leonware.api.utils.player.DirectionalInput;
import sweetie.leonware.api.utils.player.MoveUtil;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.utils.rotation.manager.RotationPlan;
import sweetie.leonware.client.features.modules.combat.AuraModule;
import sweetie.leonware.client.features.modules.movement.MoveFixModule;
import sweetie.leonware.inject.input.MixinInput;

@Mixin(value={class_743.class})
public class MixinKeyboardInput
extends MixinInput {
    @ModifyExpressionValue(method={"tick"}, at={@At(value="NEW", target="(ZZZZZZZ)Lnet/minecraft/util/PlayerInput;")})
    private class_10185 onTick(class_10185 original) {
        MovementInputEvent.MovementInputEventData movementInputEvent = new MovementInputEvent.MovementInputEventData(original, original.comp_3163(), original.comp_3164(), new DirectionalInput(original));
        MovementInputEvent.getInstance().call(movementInputEvent);
        DirectionalInput untransformedDirectionalInput = movementInputEvent.getDirectionalInput();
        DirectionalInput directionalInput = this.transformDirection(untransformedDirectionalInput);
        SprintEvent.SprintEventData sprintEvent = new SprintEvent.SprintEventData(directionalInput);
        SprintEvent.getInstance().call(sprintEvent);
        this.untransformed = new class_10185(untransformedDirectionalInput.isForwards(), untransformedDirectionalInput.isBackwards(), untransformedDirectionalInput.isLeft(), untransformedDirectionalInput.isRight(), original.comp_3163(), original.comp_3164(), sprintEvent.isSprint());
        return new class_10185(directionalInput.isForwards(), directionalInput.isBackwards(), directionalInput.isLeft(), directionalInput.isRight(), movementInputEvent.isJump(), movementInputEvent.isSneak(), sprintEvent.isSprint());
    }

    @Unique
    private DirectionalInput transformDirection(DirectionalInput input) {
        class_746 player = SharedClass.player();
        RotationManager rotationManager = RotationManager.getInstance();
        Rotation rotation = rotationManager.getCurrentRotation();
        RotationPlan rotationPlan = rotationManager.getCurrentRotationPlan();
        if (rotationPlan == null || rotation == null || player == null || !rotationPlan.moveCorrection()) {
            return input;
        }
        float z = class_743.method_40218((boolean)input.isForwards(), (boolean)input.isBackwards());
        float x = class_743.method_40218((boolean)input.isLeft(), (boolean)input.isRight());
        float yaw = rotation.getYaw();
        float direction = player.method_36454();
        if (MoveFixModule.isTargeting() && z == 1.0f && x == 0.0f) {
            class_243 position;
            AuraModule auraModule = AuraModule.getInstance();
            class_243 class_2432 = position = auraModule.target != null ? auraModule.target.method_19538() : null;
            if (position == null) {
                return input;
            }
            double deltaX = position.field_1352 - player.method_23317();
            double deltaZ = position.field_1350 - player.method_23321();
            double angleToTarget = Math.toDegrees(Math.atan2(deltaZ, deltaX)) - 90.0;
            angleToTarget = class_3532.method_15338((double)angleToTarget);
            float bestForward = 0.0f;
            float bestStrafe = 0.0f;
            float minDifference = Float.MAX_VALUE;
            for (float forward = -1.0f; forward <= 1.0f; forward += 1.0f) {
                for (float strafe = -1.0f; strafe <= 1.0f; strafe += 1.0f) {
                    if (forward == 0.0f && strafe == 0.0f) continue;
                    double moveAngle = MoveUtil.direction(yaw, forward, strafe);
                    moveAngle = Math.toDegrees(moveAngle);
                    moveAngle = class_3532.method_15338((double)moveAngle);
                    double difference = Math.abs(class_3532.method_15338((double)(angleToTarget - moveAngle)));
                    if (!((difference = Math.min(difference, 360.0 - difference)) < (double)minDifference)) continue;
                    minDifference = (float)difference;
                    bestForward = forward;
                    bestStrafe = strafe;
                }
            }
            return new DirectionalInput(bestForward, bestStrafe);
        }
        if (rotationPlan.freeMoveCorrection()) {
            float deltaYaw = direction - yaw;
            float radians = deltaYaw * ((float)Math.PI / 180);
            float newX = x * class_3532.method_15362((float)radians) - z * class_3532.method_15374((float)radians);
            float newZ = z * class_3532.method_15362((float)radians) + x * class_3532.method_15374((float)radians);
            int movementSideways = Math.round(newX);
            int movementForward = Math.round(newZ);
            return new DirectionalInput(movementForward, movementSideways);
        }
        return input;
    }
}

