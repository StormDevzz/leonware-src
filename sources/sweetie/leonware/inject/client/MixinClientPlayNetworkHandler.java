package sweetie.leonware.inject.client;

import net.minecraft.class_310;
import net.minecraft.class_634;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sweetie.leonware.LeonWare;
import sweetie.leonware.api.command.CommandManager;
import sweetie.leonware.api.system.DiscordHook;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/client/MixinClientPlayNetworkHandler.class */
@Mixin({class_634.class})
public class MixinClientPlayNetworkHandler {
    @Inject(method = {"sendChatMessage"}, at = {@At("HEAD")}, cancellable = true)
    private void sendChatMessage(String content, CallbackInfo ci) {
        if (LeonWare.isUnhooked && content.equals(LeonWare.unhookCode)) {
            LeonWare.isUnhooked = false;
            LeonWare.unhookCode = "";
            LeonWare.unhookSnapshot.forEach((module, wasEnabled) -> {
                if (wasEnabled.booleanValue()) {
                    module.setEnabled(true, false);
                }
            });
            LeonWare.unhookSnapshot.clear();
            DiscordHook.startRPC();
            class_310.method_1551().method_24288();
            ci.cancel();
            return;
        }
        CommandManager.getInstance().executeCommands(content, ci);
    }
}
