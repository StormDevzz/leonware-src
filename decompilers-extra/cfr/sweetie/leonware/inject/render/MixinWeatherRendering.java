/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyExpressionValue
 *  com.llamalad7.mixinextras.injector.ModifyReturnValue
 *  net.minecraft.class_1959$class_1963
 *  net.minecraft.class_9976
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 */
package sweetie.leonware.inject.render;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.class_1959;
import net.minecraft.class_9976;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import sweetie.leonware.client.features.modules.render.AmbienceModule;

@Mixin(value={class_9976.class})
public abstract class MixinWeatherRendering {
    @ModifyExpressionValue(method={"addParticlesAndSound"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/world/ClientWorld;getRainGradient(F)F")})
    private float ambientPrecipitation(float original) {
        AmbienceModule moduleCustomAmbience = AmbienceModule.getInstance();
        if (moduleCustomAmbience.isEnabled() && moduleCustomAmbience.weather.is(AmbienceModule.Weather.SNOWY)) {
            return 0.0f;
        }
        return original;
    }

    @ModifyReturnValue(method={"getPrecipitationAt"}, at={@At(value="RETURN", ordinal=1)})
    private class_1959.class_1963 modifyBiomePrecipitation(class_1959.class_1963 original) {
        AmbienceModule moduleOverrideWeather = AmbienceModule.getInstance();
        if (moduleOverrideWeather.isEnabled() && moduleOverrideWeather.weather.is(AmbienceModule.Weather.SNOWY)) {
            return class_1959.class_1963.field_9383;
        }
        return original;
    }
}

