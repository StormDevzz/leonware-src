// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.other;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import sweetie.leonware.api.system.client.TimerManager;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.class_9779;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_9779.class_9781.class })
public class MixinRenderTickCounter
{
    @Shadow
    private float field_51958;
    
    @Inject(method = { "beginRenderTick(J)I" }, at = { @At(value = "FIELD", target = "Lnet/minecraft/client/render/RenderTickCounter$Dynamic;lastFrameDuration:F", shift = At.Shift.AFTER) })
    private void timer(final CallbackInfoReturnable<Integer> callback) {
        final float customTimer = TimerManager.getInstance().getTimerSpeed();
        if (customTimer > 0.0f) {
            this.field_51958 *= customTimer;
        }
    }
}
