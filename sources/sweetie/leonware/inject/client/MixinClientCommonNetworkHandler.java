package sweetie.leonware.inject.client;

import net.minecraft.class_8673;
import net.minecraft.class_9812;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sweetie.leonware.api.event.events.client.DisconnectEvent;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/client/MixinClientCommonNetworkHandler.class */
@Mixin({class_8673.class})
public class MixinClientCommonNetworkHandler {
    @Inject(method = {"onDisconnected"}, at = {@At("HEAD")})
    private void onDisconnect(class_9812 info, CallbackInfo ci) {
        DisconnectEvent.getInstance().call(new DisconnectEvent.DisconnectData());
    }
}
