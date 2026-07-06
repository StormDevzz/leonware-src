// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.client;

import sweetie.leonware.api.module.Module;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import sweetie.leonware.api.command.CommandManager;
import net.minecraft.class_310;
import sweetie.leonware.api.system.DiscordHook;
import sweetie.leonware.LeonWare;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.class_634;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_634.class })
public class MixinClientPlayNetworkHandler
{
    @Inject(method = { "sendChatMessage" }, at = { @At("HEAD") }, cancellable = true)
    private void sendChatMessage(final String content, final CallbackInfo ci) {
        if (LeonWare.isUnhooked && content.equals(LeonWare.unhookCode)) {
            LeonWare.isUnhooked = false;
            LeonWare.unhookCode = "";
            LeonWare.unhookSnapshot.forEach((module, wasEnabled) -> {
                if (wasEnabled) {
                    module.setEnabled(true, false);
                }
                return;
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
