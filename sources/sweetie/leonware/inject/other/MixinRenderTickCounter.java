package sweetie.leonware.inject.other;

import net.minecraft.class_9779;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sweetie.leonware.api.system.client.TimerManager;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/other/MixinRenderTickCounter.class */
@Mixin({class_9779.class_9781.class})
public class MixinRenderTickCounter {

    @Shadow
    private float field_51958;

    @Inject(method = {"beginRenderTick(J)I"}, at = {@At(value = "FIELD", target = "Lnet/minecraft/client/render/RenderTickCounter$Dynamic;lastFrameDuration:F", shift = At.Shift.AFTER)})
    private void timer(CallbackInfoReturnable<Integer> callback) {
        float customTimer = TimerManager.getInstance().getTimerSpeed();
        if (customTimer > 0.0f) {
            this.field_51958 *= customTimer;
        }
    }
}
