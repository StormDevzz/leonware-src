// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.entity;

import org.spongepowered.asm.mixin.injection.ModifyArgs;
import sweetie.leonware.api.system.backend.Pair;
import net.minecraft.class_241;
import sweetie.leonware.client.features.modules.movement.nitrofirework.NitroFireworkModule;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import sweetie.leonware.api.utils.rotation.manager.RotationPlan;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.system.backend.SharedClass;
import net.minecraft.class_243;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.class_1309;
import net.minecraft.class_1671;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_1671.class })
public class MixinFireworkRocketEntity
{
    @Shadow
    private class_1309 field_7616;
    
    @Redirect(method = { "tick" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getRotationVector()Lnet/minecraft/util/math/Vec3d;"))
    public class_243 fixFireworkVelocity(final class_1309 instance) {
        if (instance != SharedClass.player()) {
            return instance.method_5720();
        }
        final RotationManager rotationManager = RotationManager.getInstance();
        final Rotation rotation = rotationManager.getRotation();
        final RotationPlan currentRotationPlan = rotationManager.getCurrentRotationPlan();
        if (currentRotationPlan == null) {
            return instance.method_5720();
        }
        return rotation.getVector();
    }
    
    @ModifyArgs(method = { "tick" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;add(DDD)Lnet/minecraft/util/math/Vec3d;", ordinal = 0))
    private void hookExtendedFirework(final Args args, @Local(ordinal = 0) final class_243 rotation, @Local(ordinal = 1) final class_243 velocity) {
        if (this.field_7616 != SharedClass.player() || !NitroFireworkModule.getInstance().isEnabled()) {
            return;
        }
        final Pair<Float, Float> pair = NitroFireworkModule.getInstance().currentMode.velocityValues();
        final float donichka = 0.1f;
        final float piska = 0.5f;
        final class_241 multiplier = new class_241((float)pair.left(), (float)pair.right());
        args.set(0, (Object)(rotation.field_1352 * donichka + (rotation.field_1352 * multiplier.field_1343 - velocity.field_1352) * piska));
        args.set(1, (Object)(rotation.field_1351 * donichka + (rotation.field_1351 * multiplier.field_1342 - velocity.field_1351) * piska));
        args.set(2, (Object)(rotation.field_1350 * donichka + (rotation.field_1350 * multiplier.field_1343 - velocity.field_1350) * piska));
    }
}
