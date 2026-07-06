package sweetie.leonware.inject.entity;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.class_1309;
import net.minecraft.class_1671;
import net.minecraft.class_241;
import net.minecraft.class_243;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import sweetie.leonware.api.system.backend.Pair;
import sweetie.leonware.api.system.backend.SharedClass;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.utils.rotation.manager.RotationPlan;
import sweetie.leonware.client.features.modules.movement.nitrofirework.NitroFireworkModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/entity/MixinFireworkRocketEntity.class */
@Mixin({class_1671.class})
public class MixinFireworkRocketEntity {

    @Shadow
    private class_1309 field_7616;

    @Redirect(method = {"tick"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getRotationVector()Lnet/minecraft/util/math/Vec3d;"))
    public class_243 fixFireworkVelocity(class_1309 instance) {
        if (instance != SharedClass.player()) {
            return instance.method_5720();
        }
        RotationManager rotationManager = RotationManager.getInstance();
        Rotation rotation = rotationManager.getRotation();
        RotationPlan currentRotationPlan = rotationManager.getCurrentRotationPlan();
        if (currentRotationPlan == null) {
            return instance.method_5720();
        }
        return rotation.getVector();
    }

    @ModifyArgs(method = {"tick"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;add(DDD)Lnet/minecraft/util/math/Vec3d;", ordinal = 0))
    private void hookExtendedFirework(Args args, @Local(ordinal = 0) class_243 rotation, @Local(ordinal = 1) class_243 velocity) {
        if (this.field_7616 == SharedClass.player() && NitroFireworkModule.getInstance().isEnabled()) {
            Pair<Float, Float> pair = NitroFireworkModule.getInstance().currentMode.velocityValues();
            class_241 multiplier = new class_241(pair.left().floatValue(), pair.right().floatValue());
            args.set(0, Double.valueOf((rotation.field_1352 * ((double) 0.1f)) + (((rotation.field_1352 * ((double) multiplier.field_1343)) - velocity.field_1352) * ((double) 0.5f))));
            args.set(1, Double.valueOf((rotation.field_1351 * ((double) 0.1f)) + (((rotation.field_1351 * ((double) multiplier.field_1342)) - velocity.field_1351) * ((double) 0.5f))));
            args.set(2, Double.valueOf((rotation.field_1350 * ((double) 0.1f)) + (((rotation.field_1350 * ((double) multiplier.field_1343)) - velocity.field_1350) * ((double) 0.5f))));
        }
    }
}
