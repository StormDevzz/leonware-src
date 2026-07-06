/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1922
 *  net.minecraft.class_2248
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350$class_2351
 *  net.minecraft.class_2374
 *  net.minecraft.class_2404
 *  net.minecraft.class_259
 *  net.minecraft.class_265
 *  net.minecraft.class_310
 *  net.minecraft.class_3726
 *  net.minecraft.class_4970$class_4971
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package sweetie.leonware.inject.other;

import net.minecraft.class_1922;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2374;
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

@Mixin(value={class_4970.class_4971.class})
public abstract class MixinAbstractBlockState {
    @Shadow
    public abstract class_2248 method_26204();

    @Inject(method={"getOutlineShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;"}, at={@At(value="HEAD")}, cancellable=true)
    private void onGetOutlineShape(class_1922 world, class_2338 pos, class_3726 context, CallbackInfoReturnable<class_265> cir) {
        class_2338 playerPos;
        class_310 mc = class_310.method_1551();
        if (mc == null || mc.field_1724 == null) {
            return;
        }
        NoClipModule noClip = NoClipModule.getInstance();
        if (noClip != null && noClip.isEnabled() && noClip.getMode().is("Pearl Phase") && (pos.equals((Object)(playerPos = class_2338.method_49638((class_2374)mc.field_1724.method_19538()))) || pos.equals((Object)playerPos.method_10084()))) {
            cir.setReturnValue((Object)class_259.method_1073());
        }
    }

    @Inject(method={"getCollisionShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;"}, at={@At(value="HEAD")}, cancellable=true)
    private void jesusFluidCollision(class_1922 world, class_2338 pos, class_3726 context, CallbackInfoReturnable<class_265> cir) {
        if (!(this.method_26204() instanceof class_2404)) {
            return;
        }
        if (class_310.method_1551() == null || class_310.method_1551().field_1724 == null) {
            return;
        }
        JesusModule mod = JesusModule.getInstance();
        if (!mod.isEnabled() || !mod.mode.is("Solid")) {
            return;
        }
        if (!JesusModule.checkCollide()) {
            return;
        }
        class_310 mc = class_310.method_1551();
        if (mc.field_1724.method_18798().field_1351 >= 0.1) {
            return;
        }
        if ((double)pos.method_10264() >= mc.field_1724.method_23318() - 0.05) {
            return;
        }
        if (mc.field_1724.method_5765()) {
            cir.setReturnValue((Object)class_259.method_1081((double)0.0, (double)0.0, (double)0.0, (double)1.0, (double)0.95f, (double)1.0));
        } else {
            cir.setReturnValue((Object)class_259.method_1077());
        }
    }

    @Inject(method={"getCollisionShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;"}, at={@At(value="HEAD")}, cancellable=true)
    private void forceMineCollision(class_1922 world, class_2338 pos, class_3726 context, CallbackInfoReturnable<class_265> cir) {
        if (class_310.method_1551() == null || class_310.method_1551().field_1724 == null) {
            return;
        }
        NoClipModule mod = NoClipModule.getInstance();
        if (!mod.isEnabled() || !mod.getMode().is("ForceMine")) {
            return;
        }
        class_310 mc = class_310.method_1551();
        class_2338 playerPos = class_2338.method_49638((class_2374)mc.field_1724.method_19538());
        float xDelta = Math.abs(playerPos.method_10263() - pos.method_10263());
        float zDelta = Math.abs(playerPos.method_10260() - pos.method_10260());
        if (xDelta != 0.0f && zDelta != 0.0f && ((Boolean)mod.getStrict().getValue()).booleanValue()) {
            return;
        }
        if (pos.equals((Object)playerPos.method_10074()) && !mc.field_1690.field_1832.method_1434()) {
            return;
        }
        cir.setReturnValue((Object)class_259.method_1073());
    }

    @Inject(method={"getCollisionShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;"}, at={@At(value="RETURN")}, cancellable=true)
    private void removeXZCollision(class_1922 world, class_2338 pos, class_3726 context, CallbackInfoReturnable<class_265> cir) {
        if (CollisionEvent.getInstance().call()) {
            double maxY;
            class_265 original = (class_265)cir.getReturnValue();
            double minY = original.method_1091(class_2350.class_2351.field_11052);
            if (minY >= (maxY = original.method_1105(class_2350.class_2351.field_11052))) {
                maxY = minY + 0.001;
            }
            cir.setReturnValue((Object)class_259.method_17786((class_265)class_259.method_1081((double)0.0, (double)minY, (double)0.0, (double)0.0, (double)maxY, (double)0.0), (class_265[])new class_265[0]));
        }
    }
}

