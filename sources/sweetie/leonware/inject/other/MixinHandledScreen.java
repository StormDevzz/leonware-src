package sweetie.leonware.inject.other;

import net.minecraft.class_1703;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_3675;
import net.minecraft.class_3936;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import net.minecraft.class_465;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sweetie.leonware.api.event.events.other.ScreenEvent;
import sweetie.leonware.api.system.backend.KeyStorage;
import sweetie.leonware.api.system.backend.SharedClass;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.client.features.modules.other.AuctionHelperModule;
import sweetie.leonware.client.features.modules.other.MouseTweaksModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/other/MixinHandledScreen.class */
@Mixin({class_465.class})
public abstract class MixinHandledScreen<T extends class_1703> extends class_437 implements class_3936<T> {

    @Unique
    private final TimerUtil timerUtil;

    @Shadow
    protected abstract boolean method_2387(class_1735 class_1735Var, double d, double d2);

    @Shadow
    protected abstract void method_2383(class_1735 class_1735Var, int i, int i2, class_1713 class_1713Var);

    protected MixinHandledScreen(class_2561 title) {
        super(title);
        this.timerUtil = new TimerUtil();
    }

    @Inject(method = {"init"}, at = {@At("TAIL")})
    private void onInit(CallbackInfo ci) {
        ScreenEvent.ScreenEventData event = new ScreenEvent.ScreenEventData(this);
        ScreenEvent.getInstance().call(event);
        for (class_4185 button : event.buttons()) {
            method_37063(button);
        }
    }

    @Inject(method = {"render"}, at = {@At("HEAD")})
    private void drawScreenHook(class_332 context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (SharedClass.player() == null) {
            return;
        }
        for (int i = 0; i < SharedClass.player().field_7512.field_7761.size(); i++) {
            class_1735 slot = (class_1735) SharedClass.player().field_7512.field_7761.get(i);
            if (method_2387(slot, mouseX, mouseY) && slot.method_7682()) {
                MouseTweaksModule mouseTweaks = MouseTweaksModule.getInstance();
                if (mouseTweaks.isEnabled() && shouldUse() && mouseIsHolding() && this.timerUtil.finished(mouseTweaks.delay.getValue().longValue())) {
                    method_2383(slot, slot.field_7874, 0, class_1713.field_7794);
                    this.timerUtil.reset();
                }
            }
        }
    }

    @Unique
    private boolean shouldUse() {
        return class_3675.method_15987(class_310.method_1551().method_22683().method_4490(), 340) || class_3675.method_15987(class_310.method_1551().method_22683().method_4490(), 344);
    }

    @Inject(method = {"drawSlot(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/screen/slot/Slot;)V"}, at = {@At("TAIL")})
    protected void drawSlotHook(class_332 context, class_1735 slot, CallbackInfo ci) {
        AuctionHelperModule module = AuctionHelperModule.getInstance();
        if (module.isEnabled()) {
            module.onRenderChest(context, slot);
        }
    }

    @Unique
    private boolean mouseIsHolding() {
        return KeyStorage.isPressed(-100);
    }
}
