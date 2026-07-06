package sweetie.leonware.inject.entity;

import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sweetie.leonware.api.event.events.player.move.TravelEvent;
import sweetie.leonware.api.event.events.player.world.AttackEvent;
import sweetie.leonware.api.system.backend.SharedClass;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.utils.rotation.manager.RotationPlan;
import sweetie.leonware.client.features.modules.movement.SprintModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/entity/MixinPlayerEntity.class */
@Mixin({class_1657.class})
public abstract class MixinPlayerEntity extends MixinLivingEntity {

    @Unique
    private float leonware$savedYaw;

    @Unique
    private float leonware$savedPitch;

    @Unique
    private boolean leonware$swimFixed = false;

    @Inject(method = {"attack"}, at = {@At("HEAD")})
    public void attackEventHook(class_1297 target, CallbackInfo ci) {
        if (SharedClass.player() != null && this == SharedClass.player()) {
            AttackEvent.getInstance().call(new AttackEvent.AttackEventData(target));
        }
    }

    @Inject(method = {"attack"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setSprinting(Z)V", shift = At.Shift.AFTER)})
    public void keepSprintHook(class_1297 target, CallbackInfo ci) {
        if (this != SharedClass.player()) {
            return;
        }
        SprintModule sprint = SprintModule.getInstance();
        if (sprint.isEnabled() && sprint.keepSprint.getValue().booleanValue()) {
            class_243 velocity = QuickImports.mc.field_1724.method_18798();
            QuickImports.mc.field_1724.method_18800(velocity.field_1352 / 0.6d, velocity.field_1351, velocity.field_1350 / 0.6d);
            QuickImports.mc.field_1724.method_5728(true);
        }
    }

    @Inject(method = {"travel"}, at = {@At("HEAD")})
    public void travelHook(class_243 movementInput, CallbackInfo ci) {
        if (this != SharedClass.player()) {
            return;
        }
        TravelEvent.getInstance().call();
        RotationManager rotationManager = RotationManager.getInstance();
        RotationPlan plan = rotationManager.getCurrentRotationPlan();
        Rotation rotation = rotationManager.getCurrentRotation();
        if (plan != null && plan.moveCorrection() && rotation != null && QuickImports.mc.field_1724.method_5869()) {
            this.leonware$savedYaw = QuickImports.mc.field_1724.method_36454();
            this.leonware$savedPitch = QuickImports.mc.field_1724.method_36455();
            QuickImports.mc.field_1724.method_36456(rotation.getYaw());
            QuickImports.mc.field_1724.method_36457(rotation.getPitch());
            this.leonware$swimFixed = true;
        }
    }

    @Inject(method = {"travel"}, at = {@At("RETURN")})
    public void travelReturnHook(class_243 movementInput, CallbackInfo ci) {
        if (this == SharedClass.player() && this.leonware$swimFixed) {
            QuickImports.mc.field_1724.method_36456(this.leonware$savedYaw);
            QuickImports.mc.field_1724.method_36457(this.leonware$savedPitch);
            this.leonware$swimFixed = false;
        }
    }
}
