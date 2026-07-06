// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.other;

import sweetie.leonware.api.event.events.other.FramebufferResizeEvent;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import sweetie.leonware.api.event.events.other.WindowResizeEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.class_1041;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_1041.class })
public class MixinWindow
{
    @Shadow
    @Final
    private long field_5187;
    
    @Inject(method = { "onWindowSizeChanged" }, at = { @At("RETURN") })
    public void windowResizeHook(final long window, final int width, final int height, final CallbackInfo ci) {
        WindowResizeEvent.getInstance().call();
    }
    
    @Inject(method = { "onFramebufferSizeChanged" }, at = { @At("RETURN") })
    public void framebufferResizeHook(final long window, final int width, final int height, final CallbackInfo callbackInfo) {
        if (window == this.field_5187) {
            FramebufferResizeEvent.getInstance().call();
        }
    }
}
