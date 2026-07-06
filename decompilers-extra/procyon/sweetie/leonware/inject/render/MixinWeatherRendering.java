// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.render;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.class_1959;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import sweetie.leonware.client.features.modules.render.AmbienceModule;
import net.minecraft.class_9976;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_9976.class })
public abstract class MixinWeatherRendering
{
    @ModifyExpressionValue(method = { "addParticlesAndSound" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getRainGradient(F)F") })
    private float ambientPrecipitation(final float original) {
        final AmbienceModule moduleCustomAmbience = AmbienceModule.getInstance();
        if (moduleCustomAmbience.isEnabled() && moduleCustomAmbience.weather.is(AmbienceModule.Weather.SNOWY)) {
            return 0.0f;
        }
        return original;
    }
    
    @ModifyReturnValue(method = { "getPrecipitationAt" }, at = { @At(value = "RETURN", ordinal = 1) })
    private class_1959.class_1963 modifyBiomePrecipitation(final class_1959.class_1963 original) {
        final AmbienceModule moduleOverrideWeather = AmbienceModule.getInstance();
        if (moduleOverrideWeather.isEnabled() && moduleOverrideWeather.weather.is(AmbienceModule.Weather.SNOWY)) {
            return class_1959.class_1963.field_9383;
        }
        return original;
    }
}
