/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2561
 *  net.minecraft.class_338
 *  net.minecraft.class_7469
 *  net.minecraft.class_7591
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package sweetie.leonware.inject.client;

import net.minecraft.class_2561;
import net.minecraft.class_338;
import net.minecraft.class_7469;
import net.minecraft.class_7591;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sweetie.leonware.client.ui.widget.WidgetManager;
import sweetie.leonware.client.ui.widget.overlay.NotifWidget;

@Mixin(value={class_338.class})
public class MixinChatHud {
    @Inject(method={"addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V"}, at={@At(value="HEAD")})
    private void onChat(class_2561 message, class_7469 signature, class_7591 indicator, CallbackInfo ci) {
        NotifWidget widget = WidgetManager.getInstance().getWidgets().stream().filter(w -> w instanceof NotifWidget).findFirst().orElse(null);
        if (widget == null || !widget.notifTypes.isEnabled("\u041f\u0440\u043e\u0441\u044c\u0431\u0430 \u043e \u043d\u0430\u0431\u043b\u044e\u0434\u0435\u043d\u0438\u0438")) {
            return;
        }
        String full = message.getString();
        String msg = full.toLowerCase();
        if (msg.contains("spec") || msg.contains("\u0441\u043f\u0435\u043a") || msg.contains("\u0441\u043f\u044d\u043a")) {
            int idx;
            String sender = "\u0418\u0433\u0440\u043e\u043a";
            if (full.contains(":")) {
                sender = full.substring(0, full.indexOf(":")).trim();
            } else if (full.contains(">") && (idx = full.indexOf(">")) > 0) {
                String temp = full.substring(0, idx).trim();
                if (temp.startsWith("<")) {
                    temp = temp.substring(1);
                }
                sender = temp;
            }
            widget.addNotif(sender + " \u043f\u0440\u043e\u0441\u0438\u0442 \u043e \u0441\u043f\u0435\u043a\u0435!");
        }
    }
}

