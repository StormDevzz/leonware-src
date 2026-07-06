/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_8673
 *  net.minecraft.class_9812
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package sweetie.leonware.inject.client;

import net.minecraft.class_8673;
import net.minecraft.class_9812;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sweetie.leonware.api.event.events.client.DisconnectEvent;

@Mixin(value={class_8673.class})
public class MixinClientCommonNetworkHandler {
    @Inject(method={"onDisconnected"}, at={@At(value="HEAD")})
    private void onDisconnect(class_9812 info, CallbackInfo ci) {
        DisconnectEvent.getInstance().call(new DisconnectEvent.DisconnectData());
    }
}

