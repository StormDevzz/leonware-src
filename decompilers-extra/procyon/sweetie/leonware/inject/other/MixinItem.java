// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.other;

import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.system.backend.SharedClass;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import net.minecraft.class_3959;
import net.minecraft.class_1657;
import net.minecraft.class_1937;
import net.minecraft.class_243;
import net.minecraft.class_1792;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_1792.class })
public abstract class MixinItem
{
    @ModifyExpressionValue(method = { "raycast" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getRotationVector(FF)Lnet/minecraft/util/math/Vec3d;") })
    private static class_243 hookFixRotation(final class_243 original, final class_1937 world, final class_1657 player, final class_3959.class_242 fluidHandling) {
        final Rotation rotation = RotationManager.getInstance().getCurrentRotation();
        if (player == SharedClass.player() && rotation != null) {
            return rotation.getVector();
        }
        return original;
    }
}
