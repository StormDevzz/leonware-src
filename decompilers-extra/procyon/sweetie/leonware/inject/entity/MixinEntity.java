// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.entity;

import sweetie.leonware.client.features.modules.movement.BoatFlyModule;
import net.minecraft.class_1690;
import net.minecraft.class_1657;
import org.spongepowered.asm.mixin.injection.Redirect;
import sweetie.leonware.api.event.events.player.move.VelocityEvent;
import sweetie.leonware.client.features.modules.render.RemovalsModule;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import sweetie.leonware.client.features.modules.player.NoPushEntityModule;
import sweetie.leonware.api.system.backend.SharedClass;
import sweetie.leonware.client.features.modules.combat.HitBoxModule;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import sweetie.leonware.api.event.events.player.move.OnMovePostEvent;
import sweetie.leonware.api.system.interfaces.QuickImports;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.class_243;
import net.minecraft.class_1297;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_1297.class })
public abstract class MixinEntity
{
    @Inject(method = { "updateVelocity" }, at = { @At("TAIL") })
    private void leonware$onMovePost(final float speed, final class_243 movementInput, final CallbackInfo ci) {
        final class_1297 me = (class_1297)this;
        if (QuickImports.mc.field_1724 != null && me.method_5628() == QuickImports.mc.field_1724.method_5628()) {
            OnMovePostEvent.getInstance().call(new OnMovePostEvent.OnMovePostEventData(speed, movementInput));
        }
    }
    
    @Inject(method = { "getTargetingMargin" }, at = { @At("HEAD") }, cancellable = true)
    private void onGetTargetingMargin(final CallbackInfoReturnable<Float> cir) {
        final float expand = HitBoxModule.getExpand();
        if (expand != 0.0f) {
            cir.setReturnValue((Object)expand);
        }
    }
    
    @ModifyExpressionValue(method = { "updateMovementInFluid" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FluidState;getVelocity(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/math/Vec3d;") })
    private class_243 noPushInLiquidsHook(final class_243 original) {
        if (this != SharedClass.player()) {
            return original;
        }
        return NoPushEntityModule.getInstance().cancelPush(NoPushEntityModule.PushingSource.LIQUIDS) ? class_243.field_1353 : original;
    }
    
    @Inject(method = { "isGlowing" }, at = { @At("HEAD") }, cancellable = true)
    public void cancelGlowingHook(final CallbackInfoReturnable<Boolean> cir) {
        if (RemovalsModule.getInstance().isGlowEffect()) {
            cir.setReturnValue((Object)false);
        }
    }
    
    @ModifyExpressionValue(method = { "move" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isControlledByPlayer()Z") })
    private boolean fixFallDistance(final boolean original) {
        return this != SharedClass.player() && original;
    }
    
    @Redirect(method = { "updateVelocity" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;movementInputToVelocity(Lnet/minecraft/util/math/Vec3d;FF)Lnet/minecraft/util/math/Vec3d;"))
    public class_243 updateVelocityHook(final class_243 movementInput, final float speed, final float yaw) {
        if (this == SharedClass.player()) {
            final VelocityEvent.VelocityEventData event = new VelocityEvent.VelocityEventData(movementInput, speed, yaw, class_1297.method_18795(movementInput, speed, yaw));
            VelocityEvent.getInstance().call(event);
            return event.getVelocity();
        }
        return class_1297.method_18795(movementInput, speed, yaw);
    }
    
    @Inject(method = { "stopRiding" }, at = { @At("HEAD") }, cancellable = true)
    private void onStopRiding(final CallbackInfo ci) {
        final class_1297 entity = (class_1297)this;
        if (!(entity instanceof class_1657)) {
            return;
        }
        final class_1657 player = (class_1657)entity;
        if (!player.method_37908().method_8608()) {
            return;
        }
        final class_1297 vehicle = player.method_5854();
        if (!(vehicle instanceof class_1690)) {
            return;
        }
        final BoatFlyModule boatFly = BoatFlyModule.getInstance();
        if (boatFly == null || !boatFly.isEnabled()) {
            return;
        }
        if (!vehicle.method_24828()) {
            ci.cancel();
        }
    }
}
