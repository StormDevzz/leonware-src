package sweetie.leonware.inject.render;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.class_638;
import net.minecraft.class_6539;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import sweetie.leonware.client.features.modules.render.AmbienceModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/render/MixinClientWorldAmbience.class */
@Mixin({class_638.class})
public class MixinClientWorldAmbience {
    @ModifyReturnValue(method = {"getSkyColor(Lnet/minecraft/util/math/Vec3d;F)I"}, at = {@At("RETURN")})
    private int getSkyColor(int original) {
        return AmbienceModule.getInstance().applySkyColor(original);
    }

    @ModifyReturnValue(method = {"getCloudsColor(F)I"}, at = {@At("RETURN")})
    private int getCloudsColor(int original) {
        return AmbienceModule.getInstance().applyCloudsColor(original);
    }

    @ModifyReturnValue(method = {"getColor(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/biome/ColorResolver;)I"}, at = {@At("RETURN")})
    private int getBiomeColor(int original, @Local(argsOnly = true) class_6539 resolver) {
        return AmbienceModule.getInstance().applyBiomeColor(original, resolver);
    }
}
