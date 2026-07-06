// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.inject.client;

import org.spongepowered.asm.mixin.Unique;
import java.util.Iterator;
import net.minecraft.class_8042;
import net.minecraft.class_2547;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.utils.other.NetworkUtil;
import net.minecraft.class_310;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.jetbrains.annotations.Nullable;
import net.minecraft.class_7648;
import net.minecraft.class_2596;
import net.minecraft.class_2535;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ class_2535.class })
public class MixinClientConnection
{
    @Inject(method = { "send(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/PacketCallbacks;Z)V" }, at = { @At("HEAD") }, cancellable = true)
    private void sendPackets(final class_2596<?> packet, @Nullable final class_7648 callbacks, final boolean flush, final CallbackInfo callbackInfo) {
        if (class_310.method_1551().field_1724 == null || class_310.method_1551().field_1687 == null || NetworkUtil.silentPacket()) {
            return;
        }
        if (PacketEvent.getInstance().call(new PacketEvent.PacketEventData(packet, PacketEvent.PacketEventData.PacketType.SEND))) {
            callbackInfo.cancel();
        }
    }
    
    @Inject(method = { "handlePacket" }, at = { @At("HEAD") }, cancellable = true)
    private static void receivePackets(final class_2596<?> packet, final class_2547 listener, final CallbackInfo callbackInfo) {
        if (class_310.method_1551().field_1724 == null || class_310.method_1551().field_1687 == null) {
            return;
        }
        if (packet instanceof final class_8042 bundlePacket) {
            for (final class_2596<?> innerPacket : bundlePacket.method_48324()) {
                if (handleSinglePacket(innerPacket)) {
                    callbackInfo.cancel();
                }
            }
        }
        else if (handleSinglePacket(packet)) {
            callbackInfo.cancel();
        }
    }
    
    @Unique
    private static boolean handleSinglePacket(final class_2596<?> packet) {
        return PacketEvent.getInstance().call(new PacketEvent.PacketEventData(packet, PacketEvent.PacketEventData.PacketType.RECEIVE));
    }
}
