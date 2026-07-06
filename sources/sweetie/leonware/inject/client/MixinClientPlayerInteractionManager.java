package sweetie.leonware.inject.client;

import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_1297;
import net.minecraft.class_1531;
import net.minecraft.class_1533;
import net.minecraft.class_1534;
import net.minecraft.class_1657;
import net.minecraft.class_1713;
import net.minecraft.class_2199;
import net.minecraft.class_2248;
import net.minecraft.class_2269;
import net.minecraft.class_2272;
import net.minecraft.class_2286;
import net.minecraft.class_2304;
import net.minecraft.class_2323;
import net.minecraft.class_2328;
import net.minecraft.class_2331;
import net.minecraft.class_2349;
import net.minecraft.class_2362;
import net.minecraft.class_2401;
import net.minecraft.class_2406;
import net.minecraft.class_2428;
import net.minecraft.class_2462;
import net.minecraft.class_2533;
import net.minecraft.class_2680;
import net.minecraft.class_310;
import net.minecraft.class_3709;
import net.minecraft.class_3711;
import net.minecraft.class_3713;
import net.minecraft.class_3715;
import net.minecraft.class_3717;
import net.minecraft.class_3718;
import net.minecraft.class_3965;
import net.minecraft.class_3966;
import net.minecraft.class_636;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sweetie.leonware.api.event.events.player.world.ClickSlotEvent;
import sweetie.leonware.client.features.modules.player.MultiTaskModule;
import sweetie.leonware.client.features.modules.player.NoInteractModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/client/MixinClientPlayerInteractionManager.class */
@Mixin({class_636.class})
public class MixinClientPlayerInteractionManager {

    @Shadow
    @Final
    private class_310 field_3712;

    @Inject(method = {"clickSlot"}, at = {@At("HEAD")}, cancellable = true)
    public void onClickSlot(int syncId, int slotId, int button, class_1713 actionType, class_1657 player, CallbackInfo ci) {
        if (this.field_3712.field_1724 == null || this.field_3712.field_1687 == null) {
            return;
        }
        ClickSlotEvent.ClickSlotEventData event = new ClickSlotEvent.ClickSlotEventData(actionType, slotId, button, syncId);
        if (ClickSlotEvent.getInstance().call(event)) {
            ci.cancel();
        }
    }

    @Inject(method = {"interactBlock"}, at = {@At("HEAD")}, cancellable = true)
    private void onInteractBlock(class_746 player, class_1268 hand, class_3965 hitResult, CallbackInfoReturnable<class_1269> cir) {
        if ((!MultiTaskModule.getInstance().allowInteract() || !player.method_6115()) && NoInteractModule.getInstance().isEnabled()) {
            class_2680 state = player.method_37908().method_8320(hitResult.method_17777());
            class_2248 block = state.method_26204();
            if (state.method_31709()) {
                cir.setReturnValue(class_1269.field_5811);
            } else if (isWastefulBlock(block)) {
                cir.setReturnValue(class_1269.field_5811);
            }
        }
    }

    @Inject(method = {"interactEntity"}, at = {@At("HEAD")}, cancellable = true)
    private void onInteractEntity(class_1657 player, class_1297 entity, class_1268 hand, CallbackInfoReturnable<class_1269> cir) {
        if (NoInteractModule.getInstance().isEnabled() && isProtectedEntity(entity)) {
            cir.setReturnValue(class_1269.field_5811);
        }
    }

    @Inject(method = {"interactEntityAtLocation"}, at = {@At("HEAD")}, cancellable = true)
    private void onInteractEntityAtLocation(class_1657 player, class_1297 entity, class_3966 hitResult, class_1268 hand, CallbackInfoReturnable<class_1269> cir) {
        if (NoInteractModule.getInstance().isEnabled() && isProtectedEntity(entity)) {
            cir.setReturnValue(class_1269.field_5811);
        }
    }

    @Inject(method = {"attackEntity"}, at = {@At("HEAD")}, cancellable = true)
    private void onAttackEntity(class_1657 player, class_1297 target, CallbackInfo ci) {
        if (NoInteractModule.getInstance().isEnabled() && isProtectedEntity(target)) {
            ci.cancel();
        } else if (player.method_6115() && !MultiTaskModule.getInstance().allowAttack()) {
            ci.cancel();
        }
    }

    @Unique
    private boolean isWastefulBlock(class_2248 block) {
        return (block instanceof class_2304) || (block instanceof class_2199) || (block instanceof class_2331) || (block instanceof class_2269) || (block instanceof class_2401) || (block instanceof class_2323) || (block instanceof class_2533) || (block instanceof class_2349) || (block instanceof class_3709) || (block instanceof class_2428) || (block instanceof class_2462) || (block instanceof class_2286) || (block instanceof class_3713) || (block instanceof class_3718) || (block instanceof class_2406) || (block instanceof class_3711) || (block instanceof class_3717) || (block instanceof class_3715) || (block instanceof class_2272) || (block instanceof class_2328) || (block instanceof class_2362);
    }

    @Unique
    private boolean isProtectedEntity(class_1297 entity) {
        return (entity instanceof class_1531) || (entity instanceof class_1533) || (entity instanceof class_1534);
    }
}
