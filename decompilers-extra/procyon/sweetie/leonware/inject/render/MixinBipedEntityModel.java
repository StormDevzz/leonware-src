// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.render;

import sweetie.leonware.api.system.configs.FriendManager;
import net.minecraft.class_310;
import org.spongepowered.asm.mixin.Unique;
import sweetie.leonware.client.features.modules.render.BabyModelModule;
import net.minecraft.class_591;
import sweetie.leonware.client.features.modules.render.CustomModelModule;
import net.minecraft.class_10055;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.class_10034;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.class_630;
import net.minecraft.class_572;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_572.class })
public abstract class MixinBipedEntityModel
{
    @Accessor("head")
    public abstract class_630 leonware$getHead();
    
    @Inject(method = { "setAngles(Lnet/minecraft/client/render/entity/state/BipedEntityRenderState;)V" }, at = { @At("HEAD") })
    private void onSetAnglesHead(final class_10034 state, final CallbackInfo ci) {
        this.leonware$applyHeadScale();
    }
    
    @Inject(method = { "setAngles(Lnet/minecraft/client/render/entity/state/BipedEntityRenderState;)V" }, at = { @At("TAIL") })
    private void onSetAnglesTail(final class_10034 state, final CallbackInfo ci) {
        this.leonware$applyHeadScale();
        if (!(state instanceof class_10055)) {
            return;
        }
        final class_10055 ps = (class_10055)state;
        if (!this.leonware$shouldHideForRabbit(ps)) {
            return;
        }
        final class_572<?> model = (class_572<?>)this;
        final boolean leon2D = CustomModelModule.getInstance().model.is("Leon 2D");
        final boolean showArms = leon2D && !CustomModelModule.getInstance().leonHideArms.getValue();
        model.field_3398.field_3665 = false;
        model.field_3394.field_3665 = false;
        model.field_3391.field_3665 = false;
        model.field_3397.field_3665 = false;
        model.field_3392.field_3665 = false;
        model.field_27433.field_3665 = showArms;
        model.field_3401.field_3665 = showArms;
        if (model instanceof final class_591 ppm) {
            ppm.field_3483.field_3665 = false;
            ppm.field_3484.field_3665 = showArms;
            ppm.field_3486.field_3665 = showArms;
            ppm.field_3482.field_3665 = false;
            ppm.field_3479.field_3665 = false;
        }
    }
    
    @Unique
    private void leonware$applyHeadScale() {
        final BabyModelModule mod = BabyModelModule.getInstance();
        if (!mod.isEnabled() || !BabyModelModule.currentShouldScale) {
            return;
        }
        final float s = mod.headScale.getValue();
        final class_630 head = this.leonware$getHead();
        head.field_37938 = s;
        head.field_37939 = s;
        head.field_37940 = s;
    }
    
    @Unique
    private boolean leonware$shouldHideForRabbit(final class_10055 state) {
        final CustomModelModule mod = CustomModelModule.getInstance();
        if (!mod.isEnabled()) {
            return false;
        }
        if (!mod.model.is("CrazyRabbit") && !mod.model.is("Freddy Bear") && !mod.model.is("Amogus") && !mod.model.is("Leon 2D")) {
            return false;
        }
        final class_310 mc = class_310.method_1551();
        if (mc.field_1724 == null || state.field_53529 == null) {
            return false;
        }
        final boolean isSelf = state.field_53529.equals(mc.method_1548().method_1676());
        return isSelf || (mod.friends.getValue() && FriendManager.getInstance().contains(state.field_53529));
    }
}
