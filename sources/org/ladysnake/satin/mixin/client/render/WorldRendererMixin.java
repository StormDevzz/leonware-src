package org.ladysnake.satin.mixin.client.render;

import net.minecraft.class_761;
import org.ladysnake.satin.api.event.WorldRendererReloadCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: leonware-0.0.3.jar:META-INF/jars/satin-3.0.0-alpha.1.jar:org/ladysnake/satin/mixin/client/render/WorldRendererMixin.class */
@Mixin({class_761.class})
public abstract class WorldRendererMixin {
    @Inject(method = {"reload()V"}, at = {@At("RETURN")})
    private void reloadShaders(CallbackInfo ci) {
        ((WorldRendererReloadCallback) WorldRendererReloadCallback.EVENT.invoker()).onRendererReload((class_761) this);
    }
}
