package sweetie.leonware.inject.other;

import net.minecraft.class_1041;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sweetie.leonware.api.event.events.other.FramebufferResizeEvent;
import sweetie.leonware.api.event.events.other.WindowResizeEvent;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/other/MixinWindow.class */
@Mixin({class_1041.class})
public class MixinWindow {

    @Shadow
    @Final
    private long field_5187;

    @Inject(method = {"onWindowSizeChanged"}, at = {@At("RETURN")})
    public void windowResizeHook(long window, int width, int height, CallbackInfo ci) {
        WindowResizeEvent.getInstance().call();
    }

    @Inject(method = {"onFramebufferSizeChanged"}, at = {@At("RETURN")})
    public void framebufferResizeHook(long window, int width, int height, CallbackInfo callbackInfo) {
        if (window == this.field_5187) {
            FramebufferResizeEvent.getInstance().call();
        }
    }
}
