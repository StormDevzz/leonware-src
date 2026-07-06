package sweetie.leonware.inject.other;

import net.minecraft.class_1922;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2404;
import net.minecraft.class_259;
import net.minecraft.class_265;
import net.minecraft.class_310;
import net.minecraft.class_3726;
import net.minecraft.class_4970;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sweetie.leonware.api.event.events.player.move.CollisionEvent;
import sweetie.leonware.client.features.modules.movement.JesusModule;
import sweetie.leonware.client.features.modules.movement.NoClipModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/other/MixinAbstractBlockState.class */
@Mixin({class_4970.class_4971.class})
public abstract class MixinAbstractBlockState {
    @Shadow
    public abstract class_2248 method_26204();

    @Inject(method = {"getOutlineShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;"}, at = {@At("HEAD")}, cancellable = true)
    private void onGetOutlineShape(class_1922 world, class_2338 pos, class_3726 context, CallbackInfoReturnable<class_265> cir) {
        NoClipModule noClip;
        class_310 mc = class_310.method_1551();
        if (mc != null && mc.field_1724 != null && (noClip = NoClipModule.getInstance()) != null && noClip.isEnabled() && noClip.getMode().is("Pearl Phase")) {
            class_2338 playerPos = class_2338.method_49638(mc.field_1724.method_19538());
            if (pos.equals(playerPos) || pos.equals(playerPos.method_10084())) {
                cir.setReturnValue(class_259.method_1073());
            }
        }
    }

    @Inject(method = {"getCollisionShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;"}, at = {@At("HEAD")}, cancellable = true)
    private void jesusFluidCollision(class_1922 world, class_2338 pos, class_3726 context, CallbackInfoReturnable<class_265> cir) {
        if (!(method_26204() instanceof class_2404) || class_310.method_1551() == null || class_310.method_1551().field_1724 == null) {
            return;
        }
        JesusModule mod = JesusModule.getInstance();
        if (mod.isEnabled() && mod.mode.is("Solid") && JesusModule.checkCollide()) {
            class_310 mc = class_310.method_1551();
            if (mc.field_1724.method_18798().field_1351 < 0.1d && pos.method_10264() < mc.field_1724.method_23318() - 0.05d) {
                if (mc.field_1724.method_5765()) {
                    cir.setReturnValue(class_259.method_1081(0.0d, 0.0d, 0.0d, 1.0d, 0.949999988079071d, 1.0d));
                } else {
                    cir.setReturnValue(class_259.method_1077());
                }
            }
        }
    }

    @Inject(method = {"getCollisionShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;"}, at = {@At("HEAD")}, cancellable = true)
    private void forceMineCollision(class_1922 world, class_2338 pos, class_3726 context, CallbackInfoReturnable<class_265> cir) {
        if (class_310.method_1551() == null || class_310.method_1551().field_1724 == null) {
            return;
        }
        NoClipModule mod = NoClipModule.getInstance();
        if (mod.isEnabled() && mod.getMode().is("ForceMine")) {
            class_310 mc = class_310.method_1551();
            class_2338 playerPos = class_2338.method_49638(mc.field_1724.method_19538());
            float xDelta = Math.abs(playerPos.method_10263() - pos.method_10263());
            float zDelta = Math.abs(playerPos.method_10260() - pos.method_10260());
            if (xDelta != 0.0f && zDelta != 0.0f && mod.getStrict().getValue().booleanValue()) {
                return;
            }
            if (pos.equals(playerPos.method_10074()) && !mc.field_1690.field_1832.method_1434()) {
                return;
            }
            cir.setReturnValue(class_259.method_1073());
        }
    }

    @Inject(method = {"getCollisionShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;"}, at = {@At("RETURN")}, cancellable = true)
    private void removeXZCollision(class_1922 world, class_2338 pos, class_3726 context, CallbackInfoReturnable<class_265> cir) {
        if (CollisionEvent.getInstance().call()) {
            class_265 original = (class_265) cir.getReturnValue();
            double minY = original.method_1091(class_2350.class_2351.field_11052);
            double maxY = original.method_1105(class_2350.class_2351.field_11052);
            if (minY >= maxY) {
                maxY = minY + 0.001d;
            }
            cir.setReturnValue(class_259.method_17786(class_259.method_1081(0.0d, minY, 0.0d, 0.0d, maxY, 0.0d), new class_265[0]));
        }
    }
}
