package sweetie.leonware.inject.client;

import net.minecraft.class_1937;
import org.newsclub.net.unix.AFVSOCKSocketAddress;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sweetie.leonware.api.system.backend.Choice;
import sweetie.leonware.client.features.modules.render.AmbienceModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/client/MixinWorld.class */
@Mixin({class_1937.class})
public class MixinWorld {
    @Inject(method = {"getRainGradient"}, cancellable = true, at = {@At("HEAD")})
    private void overrideWeather(float delta, CallbackInfoReturnable<Float> cir) {
        AmbienceModule module = AmbienceModule.getInstance();
        AmbienceModule.Weather weather = (AmbienceModule.Weather) Choice.getChoiceByName(module.weather.getValue(), AmbienceModule.Weather.values());
        if (module.isEnabled()) {
            switch (AnonymousClass1.$SwitchMap$sweetie$leonware$client$features$modules$render$AmbienceModule$Weather[weather.ordinal()]) {
                case 1:
                    cir.setReturnValue(Float.valueOf(0.0f));
                    break;
                case AFVSOCKSocketAddress.VMADDR_CID_HOST /* 2 */:
                case 3:
                    cir.setReturnValue(Float.valueOf(1.0f));
                    break;
                case 4:
                    cir.setReturnValue(Float.valueOf(0.9f));
                    break;
            }
        }
    }

    /* JADX INFO: renamed from: sweetie.leonware.inject.client.MixinWorld$1, reason: invalid class name */
    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/client/MixinWorld$1.class */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$sweetie$leonware$client$features$modules$render$AmbienceModule$Weather = new int[AmbienceModule.Weather.values().length];

        static {
            try {
                $SwitchMap$sweetie$leonware$client$features$modules$render$AmbienceModule$Weather[AmbienceModule.Weather.SUNNY.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$sweetie$leonware$client$features$modules$render$AmbienceModule$Weather[AmbienceModule.Weather.RAINY.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$sweetie$leonware$client$features$modules$render$AmbienceModule$Weather[AmbienceModule.Weather.THUNDER.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$sweetie$leonware$client$features$modules$render$AmbienceModule$Weather[AmbienceModule.Weather.SNOWY.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    @Inject(method = {"getThunderGradient"}, cancellable = true, at = {@At("HEAD")})
    private void overrideThunder(float delta, CallbackInfoReturnable<Float> cir) {
        AmbienceModule module = AmbienceModule.getInstance();
        AmbienceModule.Weather weather = (AmbienceModule.Weather) Choice.getChoiceByName(module.weather.getValue(), AmbienceModule.Weather.values());
        if (module.isEnabled()) {
            switch (AnonymousClass1.$SwitchMap$sweetie$leonware$client$features$modules$render$AmbienceModule$Weather[weather.ordinal()]) {
                case 1:
                case AFVSOCKSocketAddress.VMADDR_CID_HOST /* 2 */:
                case 4:
                    cir.setReturnValue(Float.valueOf(0.0f));
                    break;
                case 3:
                    cir.setReturnValue(Float.valueOf(1.0f));
                    break;
            }
        }
    }
}
