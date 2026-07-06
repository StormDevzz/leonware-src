// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.render;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.class_6539;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import sweetie.leonware.client.features.modules.render.AmbienceModule;
import net.minecraft.class_638;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_638.class })
public class MixinClientWorldAmbience
{
    @ModifyReturnValue(method = { "getSkyColor(Lnet/minecraft/util/math/Vec3d;F)I" }, at = { @At("RETURN") })
    private int getSkyColor(final int original) {
        return AmbienceModule.getInstance().applySkyColor(original);
    }
    
    @ModifyReturnValue(method = { "getCloudsColor(F)I" }, at = { @At("RETURN") })
    private int getCloudsColor(final int original) {
        return AmbienceModule.getInstance().applyCloudsColor(original);
    }
    
    @ModifyReturnValue(method = { "getColor(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/biome/ColorResolver;)I" }, at = { @At("RETURN") })
    private int getBiomeColor(final int original, @Local(argsOnly = true) final class_6539 resolver) {
        return AmbienceModule.getInstance().applyBiomeColor(original, resolver);
    }
}
