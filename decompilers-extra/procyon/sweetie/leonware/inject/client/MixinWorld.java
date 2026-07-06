// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.client;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import sweetie.leonware.api.system.backend.Choice;
import sweetie.leonware.client.features.modules.render.AmbienceModule;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.class_1937;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_1937.class })
public class MixinWorld
{
    @Inject(method = { "getRainGradient" }, cancellable = true, at = { @At("HEAD") })
    private void overrideWeather(final float delta, final CallbackInfoReturnable<Float> cir) {
        final AmbienceModule module = AmbienceModule.getInstance();
        final AmbienceModule.Weather weather = Choice.getChoiceByName(module.weather.getValue(), AmbienceModule.Weather.values());
        if (module.isEnabled()) {
            switch (weather) {
                case SUNNY: {
                    cir.setReturnValue((Object)0.0f);
                    break;
                }
                case RAINY:
                case THUNDER: {
                    cir.setReturnValue((Object)1.0f);
                    break;
                }
                case SNOWY: {
                    cir.setReturnValue((Object)0.9f);
                    break;
                }
            }
        }
    }
    
    @Inject(method = { "getThunderGradient" }, cancellable = true, at = { @At("HEAD") })
    private void overrideThunder(final float delta, final CallbackInfoReturnable<Float> cir) {
        final AmbienceModule module = AmbienceModule.getInstance();
        final AmbienceModule.Weather weather = Choice.getChoiceByName(module.weather.getValue(), AmbienceModule.Weather.values());
        if (module.isEnabled()) {
            switch (weather) {
                case SUNNY:
                case RAINY:
                case SNOWY: {
                    cir.setReturnValue((Object)0.0f);
                    break;
                }
                case THUNDER: {
                    cir.setReturnValue((Object)1.0f);
                    break;
                }
            }
        }
    }
}
