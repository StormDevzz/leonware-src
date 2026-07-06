/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_310
 *  net.minecraft.class_634
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
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

@Mixin(value={class_634.class})
public class MixinClientPlayNetworkHandler {
    @Inject(method={"sendChatMessage"}, at={@At(value="HEAD")}, cancellable=true)
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

