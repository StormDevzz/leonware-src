/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1703
 *  net.minecraft.class_1713
 *  net.minecraft.class_1735
 *  net.minecraft.class_2561
 *  net.minecraft.class_310
 *  net.minecraft.class_332
 *  net.minecraft.class_364
 *  net.minecraft.class_3675
 *  net.minecraft.class_3936
 *  net.minecraft.class_4185
 *  net.minecraft.class_437
 *  net.minecraft.class_465
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package sweetie.leonware.inject.other;

import net.minecraft.class_1703;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_364;
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

@Mixin(value={class_465.class})
public abstract class MixinHandledScreen<T extends class_1703>
extends class_437
implements class_3936<T> {
    @Unique
    private final TimerUtil timerUtil = new TimerUtil();

    protected MixinHandledScreen(class_2561 title) {
        super(title);
    }

    @Shadow
    protected abstract boolean method_2387(class_1735 var1, double var2, double var4);

    @Shadow
    protected abstract void method_2383(class_1735 var1, int var2, int var3, class_1713 var4);

    @Inject(method={"init"}, at={@At(value="TAIL")})
    private void onInit(CallbackInfo ci) {
        ScreenEvent.ScreenEventData event = new ScreenEvent.ScreenEventData(this);
        ScreenEvent.getInstance().call(event);
        for (class_4185 button : event.buttons()) {
            this.method_37063((class_364)button);
        }
    }

    @Inject(method={"render"}, at={@At(value="HEAD")})
    private void drawScreenHook(class_332 context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (SharedClass.player() == null) {
            return;
        }
        for (int i = 0; i < SharedClass.player().field_7512.field_7761.size(); ++i) {
            MouseTweaksModule mouseTweaks;
            class_1735 slot = (class_1735)SharedClass.player().field_7512.field_7761.get(i);
            if (!this.method_2387(slot, mouseX, mouseY) || !slot.method_7682() || !(mouseTweaks = MouseTweaksModule.getInstance()).isEnabled() || !this.shouldUse() || !this.mouseIsHolding() || !this.timerUtil.finished(((Float)mouseTweaks.delay.getValue()).longValue())) continue;
            this.method_2383(slot, slot.field_7874, 0, class_1713.field_7794);
            this.timerUtil.reset();
        }
    }

    @Unique
    private boolean shouldUse() {
        return class_3675.method_15987((long)class_310.method_1551().method_22683().method_4490(), (int)340) || class_3675.method_15987((long)class_310.method_1551().method_22683().method_4490(), (int)344);
    }

    @Inject(method={"drawSlot(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/screen/slot/Slot;)V"}, at={@At(value="TAIL")})
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

