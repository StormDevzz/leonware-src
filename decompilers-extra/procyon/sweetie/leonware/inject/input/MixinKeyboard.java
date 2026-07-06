// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.input;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import sweetie.leonware.api.event.events.client.KeyEvent;
import sweetie.leonware.api.system.backend.SharedClass;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.class_309;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_309.class })
public class MixinKeyboard
{
    @Inject(method = { "onKey" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/option/InactivityFpsLimiter;onInput()V") })
    public void keyPressHook(final long window, final int key, final int scancode, final int action, final int modifiers, final CallbackInfo ci) {
        if (SharedClass.player() == null) {
            return;
        }
        KeyEvent.getInstance().call(new KeyEvent.KeyEventData(key, action, false));
    }
}
