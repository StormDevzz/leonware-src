/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyReturnValue
 *  com.llamalad7.mixinextras.sugar.Local
 *  net.minecraft.class_638
 *  net.minecraft.class_6539
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 */
package sweetie.leonware.inject.render;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.class_638;
import net.minecraft.class_6539;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import sweetie.leonware.client.features.modules.render.AmbienceModule;

@Mixin(value={class_638.class})
public class MixinClientWorldAmbience {
    @ModifyReturnValue(method={"getSkyColor(Lnet/minecraft/util/math/Vec3d;F)I"}, at={@At(value="RETURN")})
    private int getSkyColor(int original) {
        return AmbienceModule.getInstance().applySkyColor(original);
    }

    @ModifyReturnValue(method={"getCloudsColor(F)I"}, at={@At(value="RETURN")})
    private int getCloudsColor(int original) {
        return AmbienceModule.getInstance().applyCloudsColor(original);
    }

    @ModifyReturnValue(method={"getColor(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/biome/ColorResolver;)I"}, at={@At(value="RETURN")})
    private int getBiomeColor(int original, @Local(argsOnly=true) class_6539 resolver) {
        return AmbienceModule.getInstance().applyBiomeColor(original, resolver);
    }
}

