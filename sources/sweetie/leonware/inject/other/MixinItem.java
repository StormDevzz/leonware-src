package sweetie.leonware.inject.other;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.class_1657;
import net.minecraft.class_1792;
import net.minecraft.class_1937;
import net.minecraft.class_243;
import net.minecraft.class_3959;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import sweetie.leonware.api.system.backend.SharedClass;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/other/MixinItem.class */
@Mixin({class_1792.class})
public abstract class MixinItem {
    @ModifyExpressionValue(method = {"raycast"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getRotationVector(FF)Lnet/minecraft/util/math/Vec3d;")})
    private static class_243 hookFixRotation(class_243 original, class_1937 world, class_1657 player, class_3959.class_242 fluidHandling) {
        Rotation rotation = RotationManager.getInstance().getCurrentRotation();
        if (player == SharedClass.player() && rotation != null) {
            return rotation.getVector();
        }
        return original;
    }
}
