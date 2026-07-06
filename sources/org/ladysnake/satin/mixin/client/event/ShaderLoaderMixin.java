package org.ladysnake.satin.mixin.client.event;

import net.minecraft.class_10151;
import net.minecraft.class_3300;
import net.minecraft.class_3695;
import org.ladysnake.satin.impl.ReloadableShaderEffectManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/mixin/client/event/ShaderLoaderMixin.class */
@Mixin({class_10151.class})
public class ShaderLoaderMixin {
    @Inject(method = {"apply(Lnet/minecraft/client/gl/ShaderLoader$Definitions;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V"}, at = {@At("RETURN")})
    private void loadSatinPrograms(class_10151.class_10153 definitions, class_3300 resourceManager, class_3695 profiler, CallbackInfo ci) {
        ReloadableShaderEffectManager.INSTANCE.reload(resourceManager);
    }
}
