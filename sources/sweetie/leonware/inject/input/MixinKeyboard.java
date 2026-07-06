package sweetie.leonware.inject.input;

import net.minecraft.class_309;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sweetie.leonware.api.event.events.client.KeyEvent;
import sweetie.leonware.api.system.backend.SharedClass;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/input/MixinKeyboard.class */
@Mixin({class_309.class})
public class MixinKeyboard {
    @Inject(method = {"onKey"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/option/InactivityFpsLimiter;onInput()V")})
    public void keyPressHook(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (SharedClass.player() == null) {
            return;
        }
        KeyEvent.getInstance().call(new KeyEvent.KeyEventData(key, action, false));
    }
}
