package sweetie.leonware.inject.client;

import net.minecraft.class_2535;
import net.minecraft.class_2547;
import net.minecraft.class_2596;
import net.minecraft.class_310;
import net.minecraft.class_7648;
import net.minecraft.class_8042;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.utils.other.NetworkUtil;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/inject/client/MixinClientConnection.class */
@Mixin({class_2535.class})
public class MixinClientConnection {
    @Inject(method = {"send(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/PacketCallbacks;Z)V"}, at = {@At("HEAD")}, cancellable = true)
    private void sendPackets(class_2596<?> packet, @Nullable class_7648 callbacks, boolean flush, CallbackInfo callbackInfo) {
        if (class_310.method_1551().field_1724 != null && class_310.method_1551().field_1687 != null && !NetworkUtil.silentPacket() && PacketEvent.getInstance().call(new PacketEvent.PacketEventData(packet, PacketEvent.PacketEventData.PacketType.SEND))) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = {"handlePacket"}, at = {@At("HEAD")}, cancellable = true)
    private static void receivePackets(class_2596<?> packet, class_2547 listener, CallbackInfo callbackInfo) {
        if (class_310.method_1551().field_1724 == null || class_310.method_1551().field_1687 == null) {
            return;
        }
        if (packet instanceof class_8042) {
            class_8042 bundlePacket = (class_8042) packet;
            for (class_2596<?> innerPacket : bundlePacket.method_48324()) {
                if (handleSinglePacket(innerPacket)) {
                    callbackInfo.cancel();
                    return;
                }
            }
            return;
        }
        if (handleSinglePacket(packet)) {
            callbackInfo.cancel();
        }
    }

    @Unique
    private static boolean handleSinglePacket(class_2596<?> packet) {
        return PacketEvent.getInstance().call(new PacketEvent.PacketEventData(packet, PacketEvent.PacketEventData.PacketType.RECEIVE));
    }
}
