// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.other;

import net.minecraft.class_2350;
import sweetie.leonware.api.event.events.player.move.CollisionEvent;
import sweetie.leonware.client.features.modules.movement.JesusModule;
import net.minecraft.class_2404;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.class_259;
import net.minecraft.class_2374;
import sweetie.leonware.client.features.modules.movement.NoClipModule;
import net.minecraft.class_310;
import net.minecraft.class_265;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.class_3726;
import net.minecraft.class_2338;
import net.minecraft.class_1922;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.class_2248;
import net.minecraft.class_4970;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_4970.class_4971.class })
public abstract class MixinAbstractBlockState
{
    @Shadow
    public abstract class_2248 method_26204();
    
    @Inject(method = { "getOutlineShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;" }, at = { @At("HEAD") }, cancellable = true)
    private void onGetOutlineShape(final class_1922 world, final class_2338 pos, final class_3726 context, final CallbackInfoReturnable<class_265> cir) {
        final class_310 mc = class_310.method_1551();
        if (mc == null || mc.field_1724 == null) {
            return;
        }
        final NoClipModule noClip = NoClipModule.getInstance();
        if (noClip != null && noClip.isEnabled() && noClip.getMode().is("Pearl Phase")) {
            final class_2338 playerPos = class_2338.method_49638((class_2374)mc.field_1724.method_19538());
            if (pos.equals((Object)playerPos) || pos.equals((Object)playerPos.method_10084())) {
                cir.setReturnValue((Object)class_259.method_1073());
            }
        }
    }
    
    @Inject(method = { "getCollisionShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;" }, at = { @At("HEAD") }, cancellable = true)
    private void jesusFluidCollision(final class_1922 world, final class_2338 pos, final class_3726 context, final CallbackInfoReturnable<class_265> cir) {
        if (!(this.method_26204() instanceof class_2404)) {
            return;
        }
        if (class_310.method_1551() == null || class_310.method_1551().field_1724 == null) {
            return;
        }
        final JesusModule mod = JesusModule.getInstance();
        if (!mod.isEnabled() || !mod.mode.is("Solid")) {
            return;
        }
        if (!JesusModule.checkCollide()) {
            return;
        }
        final class_310 mc = class_310.method_1551();
        if (mc.field_1724.method_18798().field_1351 >= 0.1) {
            return;
        }
        if (pos.method_10264() >= mc.field_1724.method_23318() - 0.05) {
            return;
        }
        if (mc.field_1724.method_5765()) {
            cir.setReturnValue((Object)class_259.method_1081(0.0, 0.0, 0.0, 1.0, 0.949999988079071, 1.0));
        }
        else {
            cir.setReturnValue((Object)class_259.method_1077());
        }
    }
    
    @Inject(method = { "getCollisionShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;" }, at = { @At("HEAD") }, cancellable = true)
    private void forceMineCollision(final class_1922 world, final class_2338 pos, final class_3726 context, final CallbackInfoReturnable<class_265> cir) {
        if (class_310.method_1551() == null || class_310.method_1551().field_1724 == null) {
            return;
        }
        final NoClipModule mod = NoClipModule.getInstance();
        if (!mod.isEnabled() || !mod.getMode().is("ForceMine")) {
            return;
        }
        final class_310 mc = class_310.method_1551();
        final class_2338 playerPos = class_2338.method_49638((class_2374)mc.field_1724.method_19538());
        final float xDelta = (float)Math.abs(playerPos.method_10263() - pos.method_10263());
        final float zDelta = (float)Math.abs(playerPos.method_10260() - pos.method_10260());
        if (xDelta != 0.0f && zDelta != 0.0f && mod.getStrict().getValue()) {
            return;
        }
        if (pos.equals((Object)playerPos.method_10074()) && !mc.field_1690.field_1832.method_1434()) {
            return;
        }
        cir.setReturnValue((Object)class_259.method_1073());
    }
    
    @Inject(method = { "getCollisionShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;" }, at = { @At("RETURN") }, cancellable = true)
    private void removeXZCollision(final class_1922 world, final class_2338 pos, final class_3726 context, final CallbackInfoReturnable<class_265> cir) {
        if (CollisionEvent.getInstance().call()) {
            final class_265 original = (class_265)cir.getReturnValue();
            final double minY = original.method_1091(class_2350.class_2351.field_11052);
            double maxY = original.method_1105(class_2350.class_2351.field_11052);
            if (minY >= maxY) {
                maxY = minY + 0.001;
            }
            cir.setReturnValue((Object)class_259.method_17786(class_259.method_1081(0.0, minY, 0.0, 0.0, maxY, 0.0), new class_265[0]));
        }
    }
}
