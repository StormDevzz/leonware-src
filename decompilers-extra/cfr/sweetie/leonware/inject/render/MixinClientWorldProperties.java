/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyReturnValue
 *  net.minecraft.class_638$class_5271
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 */
package sweetie.leonware.inject.render;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.class_638;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import sweetie.leonware.client.features.modules.render.AmbienceModule;

@Mixin(value={class_638.class_5271.class})
public class MixinClientWorldProperties {
    @ModifyReturnValue(method={"getTimeOfDay()J"}, at={@At(value="RETURN")})
    private long getTimeOfDay(long original) {
        return AmbienceModule.getInstance().getTime(original);
    }
}

