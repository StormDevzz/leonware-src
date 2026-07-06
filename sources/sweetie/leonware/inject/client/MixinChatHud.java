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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/client/MixinChatHud.class */
@Mixin({class_338.class})
public class MixinChatHud {
    @Inject(method = {"addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V"}, at = {@At("HEAD")})
    private void onChat(class_2561 message, class_7469 signature, class_7591 indicator, CallbackInfo ci) {
        int idx;
        NotifWidget widget = (NotifWidget) WidgetManager.getInstance().getWidgets().stream().filter(w -> {
            return w instanceof NotifWidget;
        }).findFirst().orElse(null);
        if (widget == null || !widget.notifTypes.isEnabled("Просьба о наблюдении")) {
            return;
        }
        String full = message.getString();
        String msg = full.toLowerCase();
        if (msg.contains("spec") || msg.contains("спек") || msg.contains("спэк")) {
            String sender = "Игрок";
            if (full.contains(":")) {
                sender = full.substring(0, full.indexOf(":")).trim();
            } else if (full.contains(">") && (idx = full.indexOf(">")) > 0) {
                String temp = full.substring(0, idx).trim();
                if (temp.startsWith("<")) {
                    temp = temp.substring(1);
                }
                sender = temp;
            }
            widget.addNotif(sender + " просит о спеке!");
        }
    }
}
