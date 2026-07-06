// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.other;

import sweetie.leonware.api.system.backend.KeyStorage;
import sweetie.leonware.client.features.modules.other.AuctionHelperModule;
import net.minecraft.class_3675;
import net.minecraft.class_310;
import sweetie.leonware.client.features.modules.other.MouseTweaksModule;
import sweetie.leonware.api.system.backend.SharedClass;
import net.minecraft.class_332;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import java.util.Iterator;
import net.minecraft.class_364;
import net.minecraft.class_4185;
import sweetie.leonware.api.event.events.other.ScreenEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.class_1713;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.class_1735;
import net.minecraft.class_2561;
import org.spongepowered.asm.mixin.Unique;
import sweetie.leonware.api.utils.math.TimerUtil;
import net.minecraft.class_465;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.class_3936;
import net.minecraft.class_437;
import net.minecraft.class_1703;

@Mixin({ class_465.class })
public abstract class MixinHandledScreen<T extends class_1703> extends class_437 implements class_3936<T>
{
    @Unique
    private final TimerUtil timerUtil;
    
    protected MixinHandledScreen(final class_2561 title) {
        super(title);
        this.timerUtil = new TimerUtil();
    }
    
    @Shadow
    protected abstract boolean method_2387(final class_1735 p0, final double p1, final double p2);
    
    @Shadow
    protected abstract void method_2383(final class_1735 p0, final int p1, final int p2, final class_1713 p3);
    
    @Inject(method = { "init" }, at = { @At("TAIL") })
    private void onInit(final CallbackInfo ci) {
        final ScreenEvent.ScreenEventData event = new ScreenEvent.ScreenEventData(this);
        ScreenEvent.getInstance().call(event);
        for (final class_4185 button : event.buttons()) {
            this.method_37063((class_364)button);
        }
    }
    
    @Inject(method = { "render" }, at = { @At("HEAD") })
    private void drawScreenHook(final class_332 context, final int mouseX, final int mouseY, final float delta, final CallbackInfo ci) {
        if (SharedClass.player() == null) {
            return;
        }
        for (int i = 0; i < SharedClass.player().field_7512.field_7761.size(); ++i) {
            final class_1735 slot = (class_1735)SharedClass.player().field_7512.field_7761.get(i);
            if (this.method_2387(slot, mouseX, mouseY) && slot.method_7682()) {
                final MouseTweaksModule mouseTweaks = MouseTweaksModule.getInstance();
                if (mouseTweaks.isEnabled() && this.shouldUse() && this.mouseIsHolding() && this.timerUtil.finished(mouseTweaks.delay.getValue().longValue())) {
                    this.method_2383(slot, slot.field_7874, 0, class_1713.field_7794);
                    this.timerUtil.reset();
                }
            }
        }
    }
    
    @Unique
    private boolean shouldUse() {
        return class_3675.method_15987(class_310.method_1551().method_22683().method_4490(), 340) || class_3675.method_15987(class_310.method_1551().method_22683().method_4490(), 344);
    }
    
    @Inject(method = { "drawSlot(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/screen/slot/Slot;)V" }, at = { @At("TAIL") })
    protected void drawSlotHook(final class_332 context, final class_1735 slot, final CallbackInfo ci) {
        final AuctionHelperModule module = AuctionHelperModule.getInstance();
        if (module.isEnabled()) {
            module.onRenderChest(context, slot);
        }
    }
    
    @Unique
    private boolean mouseIsHolding() {
        return KeyStorage.isPressed(-100);
    }
}
