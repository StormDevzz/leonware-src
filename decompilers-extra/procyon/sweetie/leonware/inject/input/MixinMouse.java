// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.input;

import sweetie.leonware.api.system.draggable.Draggable;
import sweetie.leonware.api.system.draggable.DraggableManager;
import sweetie.leonware.api.event.events.client.KeyEvent;
import sweetie.leonware.api.system.backend.SharedClass;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.class_310;
import sweetie.leonware.client.ui.clickgui.ScreenClickGUI;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.class_312;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_312.class })
public class MixinMouse
{
    @Inject(method = { "lockCursor" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V") }, cancellable = true)
    private void lockCursorHook(final CallbackInfo ci) {
        if (class_310.method_1551().field_1755 instanceof ScreenClickGUI) {
            ci.cancel();
        }
    }
    
    @Inject(method = { "onMouseButton" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/option/InactivityFpsLimiter;onInput()V") })
    public void mousePressHook(final long window, final int button, final int action, final int mods, final CallbackInfo ci) {
        if (SharedClass.player() == null) {
            return;
        }
        KeyEvent.getInstance().call(new KeyEvent.KeyEventData(button, action, true));
        DraggableManager.getInstance().getDraggables().forEach((s, draggable) -> {
            if (draggable.getModule().isEnabled()) {
                if (action == 0) {
                    draggable.onRelease(button);
                }
                else if (action == 1) {
                    draggable.onClick(button);
                }
            }
        });
    }
}
