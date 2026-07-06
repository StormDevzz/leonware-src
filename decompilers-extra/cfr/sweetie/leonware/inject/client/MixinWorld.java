/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1937
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package sweetie.leonware.inject.client;

import net.minecraft.class_1937;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.system.backend.Choice;
import sweetie.leonware.client.features.modules.render.AmbienceModule;

@Mixin(value={class_1937.class})
public class MixinWorld {
    @Inject(method={"getRainGradient"}, cancellable=true, at={@At(value="HEAD")})
    private void overrideWeather(float delta, CallbackInfoReturnable<Float> cir) {
        AmbienceModule module = AmbienceModule.getInstance();
        AmbienceModule.Weather weather = (AmbienceModule.Weather)Choice.getChoiceByName((String)((String)module.weather.getValue()), (ModeSetting.NamedChoice[])AmbienceModule.Weather.values());
        if (module.isEnabled()) {
            switch (weather) {
                case SUNNY: {
                    cir.setReturnValue((Object)Float.valueOf(0.0f));
                    break;
                }
                case RAINY: 
                case THUNDER: {
                    cir.setReturnValue((Object)Float.valueOf(1.0f));
                    break;
                }
                case SNOWY: {
                    cir.setReturnValue((Object)Float.valueOf(0.9f));
                }
            }
        }
    }

    @Inject(method={"getThunderGradient"}, cancellable=true, at={@At(value="HEAD")})
    private void overrideThunder(float delta, CallbackInfoReturnable<Float> cir) {
        AmbienceModule module = AmbienceModule.getInstance();
        AmbienceModule.Weather weather = (AmbienceModule.Weather)Choice.getChoiceByName((String)((String)module.weather.getValue()), (ModeSetting.NamedChoice[])AmbienceModule.Weather.values());
        if (module.isEnabled()) {
            switch (weather) {
                case SUNNY: 
                case RAINY: 
                case SNOWY: {
                    cir.setReturnValue((Object)Float.valueOf(0.0f));
                    break;
                }
                case THUNDER: {
                    cir.setReturnValue((Object)Float.valueOf(1.0f));
                }
            }
        }
    }
}

