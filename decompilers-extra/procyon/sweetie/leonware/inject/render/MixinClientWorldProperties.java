// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.render;

import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import sweetie.leonware.client.features.modules.render.AmbienceModule;
import net.minecraft.class_638;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_638.class_5271.class })
public class MixinClientWorldProperties
{
    @ModifyReturnValue(method = { "getTimeOfDay()J" }, at = { @At("RETURN") })
    private long getTimeOfDay(final long original) {
        return AmbienceModule.getInstance().getTime(original);
    }
}
