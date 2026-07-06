package sweetie.leonware.inject.render;

import net.minecraft.class_10034;
import net.minecraft.class_10055;
import net.minecraft.class_310;
import net.minecraft.class_572;
import net.minecraft.class_591;
import net.minecraft.class_630;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sweetie.leonware.api.system.configs.FriendManager;
import sweetie.leonware.client.features.modules.render.BabyModelModule;
import sweetie.leonware.client.features.modules.render.CustomModelModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/render/MixinBipedEntityModel.class */
@Mixin({class_572.class})
public abstract class MixinBipedEntityModel {
    @Accessor("head")
    public abstract class_630 leonware$getHead();

    @Inject(method = {"setAngles(Lnet/minecraft/client/render/entity/state/BipedEntityRenderState;)V"}, at = {@At("HEAD")})
    private void onSetAnglesHead(class_10034 state, CallbackInfo ci) {
        leonware$applyHeadScale();
    }

    @Inject(method = {"setAngles(Lnet/minecraft/client/render/entity/state/BipedEntityRenderState;)V"}, at = {@At("TAIL")})
    private void onSetAnglesTail(class_10034 state, CallbackInfo ci) {
        leonware$applyHeadScale();
        if (state instanceof class_10055) {
            class_10055 ps = (class_10055) state;
            if (leonware$shouldHideForRabbit(ps)) {
                class_591 class_591Var = (class_572) this;
                boolean leon2D = CustomModelModule.getInstance().model.is("Leon 2D");
                boolean showArms = leon2D && !CustomModelModule.getInstance().leonHideArms.getValue().booleanValue();
                ((class_572) class_591Var).field_3398.field_3665 = false;
                ((class_572) class_591Var).field_3394.field_3665 = false;
                ((class_572) class_591Var).field_3391.field_3665 = false;
                ((class_572) class_591Var).field_3397.field_3665 = false;
                ((class_572) class_591Var).field_3392.field_3665 = false;
                ((class_572) class_591Var).field_27433.field_3665 = showArms;
                ((class_572) class_591Var).field_3401.field_3665 = showArms;
                if (class_591Var instanceof class_591) {
                    class_591 ppm = class_591Var;
                    ppm.field_3483.field_3665 = false;
                    ppm.field_3484.field_3665 = showArms;
                    ppm.field_3486.field_3665 = showArms;
                    ppm.field_3482.field_3665 = false;
                    ppm.field_3479.field_3665 = false;
                }
            }
        }
    }

    @Unique
    private void leonware$applyHeadScale() {
        BabyModelModule mod = BabyModelModule.getInstance();
        if (mod.isEnabled() && BabyModelModule.currentShouldScale) {
            float s = mod.headScale.getValue().floatValue();
            class_630 head = leonware$getHead();
            head.field_37938 = s;
            head.field_37939 = s;
            head.field_37940 = s;
        }
    }

    @Unique
    private boolean leonware$shouldHideForRabbit(class_10055 state) {
        CustomModelModule mod = CustomModelModule.getInstance();
        if (!mod.isEnabled()) {
            return false;
        }
        if (!mod.model.is("CrazyRabbit") && !mod.model.is("Freddy Bear") && !mod.model.is("Amogus") && !mod.model.is("Leon 2D")) {
            return false;
        }
        class_310 mc = class_310.method_1551();
        if (mc.field_1724 == null || state.field_53529 == null) {
            return false;
        }
        boolean isSelf = state.field_53529.equals(mc.method_1548().method_1676());
        if (isSelf) {
            return true;
        }
        return mod.friends.getValue().booleanValue() && FriendManager.getInstance().contains(state.field_53529);
    }
}
