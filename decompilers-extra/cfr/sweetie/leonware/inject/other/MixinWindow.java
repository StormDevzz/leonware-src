/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1041
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
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

@Mixin(value={class_1041.class})
public class MixinWindow {
    @Shadow
    @Final
    private long field_5187;

    @Inject(method={"onWindowSizeChanged"}, at={@At(value="RETURN")})
    public void windowResizeHook(long window, int width, int height, CallbackInfo ci) {
        WindowResizeEvent.getInstance().call();
    }

    @Inject(method={"onFramebufferSizeChanged"}, at={@At(value="RETURN")})
    public void framebufferResizeHook(long window, int width, int height, CallbackInfo callbackInfo) {
        if (window == this.field_5187) {
            FramebufferResizeEvent.getInstance().call();
        }
    }
}

