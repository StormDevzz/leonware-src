package sweetie.leonware.inject.block;

import net.minecraft.class_2246;
import net.minecraft.class_2248;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sweetie.leonware.client.features.modules.movement.IceSpeedModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/block/MixinBlock.class */
@Mixin({class_2248.class})
public abstract class MixinBlock {
    @Inject(method = {"getSlipperiness"}, at = {@At("HEAD")}, cancellable = true)
    private void onGetSlipperiness(CallbackInfoReturnable<Float> cir) {
        IceSpeedModule mod = IceSpeedModule.getInstance();
        if (mod != null && mod.isEnabled()) {
            if (this == class_2246.field_10295 || this == class_2246.field_10225 || this == class_2246.field_10110 || this == class_2246.field_10384) {
                cir.setReturnValue(Float.valueOf(mod.speed.getValue().floatValue()));
            }
        }
    }
}
