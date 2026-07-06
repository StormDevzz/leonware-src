// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.client;

import sweetie.leonware.client.ui.widget.Widget;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import sweetie.leonware.client.ui.widget.WidgetManager;
import sweetie.leonware.client.ui.widget.overlay.NotifWidget;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.class_7591;
import net.minecraft.class_7469;
import net.minecraft.class_2561;
import net.minecraft.class_338;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_338.class })
public class MixinChatHud
{
    @Inject(method = { "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V" }, at = { @At("HEAD") })
    private void onChat(final class_2561 message, final class_7469 signature, final class_7591 indicator, final CallbackInfo ci) {
        final NotifWidget widget = WidgetManager.getInstance().getWidgets().stream().filter(w -> w instanceof NotifWidget).findFirst().orElse(null);
        if (widget == null || !widget.notifTypes.isEnabled("\u041f\u0440\u043e\u0441\u044c\u0431\u0430 \u043e \u043d\u0430\u0431\u043b\u044e\u0434\u0435\u043d\u0438\u0438")) {
            return;
        }
        final String full = message.getString();
        final String msg = full.toLowerCase();
        if (msg.contains("spec") || msg.contains("\u0441\u043f\u0435\u043a") || msg.contains("\u0441\u043f\u044d\u043a")) {
            String sender = "\u0418\u0433\u0440\u043e\u043a";
            if (full.contains(":")) {
                sender = full.substring(0, full.indexOf(":")).trim();
            }
            else if (full.contains(">")) {
                final int idx = full.indexOf(">");
                if (idx > 0) {
                    String temp = full.substring(0, idx).trim();
                    if (temp.startsWith("<")) {
                        temp = temp.substring(1);
                    }
                    sender = temp;
                }
            }
            widget.addNotif(sender + " \u043f\u0440\u043e\u0441\u0438\u0442 \u043e \u0441\u043f\u0435\u043a\u0435!");
        }
    }
}
