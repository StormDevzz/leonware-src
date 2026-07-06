package sweetie.leonware.inject.input;

import net.minecraft.class_310;
import net.minecraft.class_312;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sweetie.leonware.api.event.events.client.KeyEvent;
import sweetie.leonware.api.system.backend.SharedClass;
import sweetie.leonware.api.system.draggable.DraggableManager;
import sweetie.leonware.client.ui.clickgui.ScreenClickGUI;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/input/MixinMouse.class */
@Mixin({class_312.class})
public class MixinMouse {
    @Inject(method = {"lockCursor"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V")}, cancellable = true)
    private void lockCursorHook(CallbackInfo ci) {
        if (class_310.method_1551().field_1755 instanceof ScreenClickGUI) {
            ci.cancel();
        }
    }

    @Inject(method = {"onMouseButton"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/option/InactivityFpsLimiter;onInput()V")})
    public void mousePressHook(long window, int button, int action, int mods, CallbackInfo ci) {
        if (SharedClass.player() == null) {
            return;
        }
        KeyEvent.getInstance().call(new KeyEvent.KeyEventData(button, action, true));
        DraggableManager.getInstance().getDraggables().forEach((s, draggable) -> {
            if (draggable.getModule().isEnabled()) {
                if (action == 0) {
                    draggable.onRelease(button);
                } else if (action == 1) {
                    draggable.onClick(button);
                }
            }
        });
    }
}
