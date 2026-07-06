// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.client;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import sweetie.leonware.api.event.events.client.DisconnectEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.class_9812;
import net.minecraft.class_8673;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_8673.class })
public class MixinClientCommonNetworkHandler
{
    @Inject(method = { "onDisconnected" }, at = { @At("HEAD") })
    private void onDisconnect(final class_9812 info, final CallbackInfo ci) {
        DisconnectEvent.getInstance().call(new DisconnectEvent.DisconnectData());
    }
}
