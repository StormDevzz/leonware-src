package sweetie.leonware.inject.render;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.class_638;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import sweetie.leonware.client.features.modules.render.AmbienceModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/render/MixinClientWorldProperties.class */
@Mixin({class_638.class_5271.class})
public class MixinClientWorldProperties {
    @ModifyReturnValue(method = {"getTimeOfDay()J"}, at = {@At("RETURN")})
    private long getTimeOfDay(long original) {
        return AmbienceModule.getInstance().getTime(original);
    }
}
