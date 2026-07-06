// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.other;

import lombok.Generated;
import sweetie.leonware.api.system.backend.SharedClass;
import net.minecraft.class_746;
import net.minecraft.class_7202;
import net.minecraft.class_7204;
import net.minecraft.class_2596;

public final class NetworkUtil
{
    private static boolean silentPacket;
    
    public static void sendPacket(final class_2596<?> packet) {
        assert player() != null;
        player().field_3944.method_52787((class_2596)packet);
    }
    
    public static void sendPacket(final class_7204 packet) {
        assert player() != null;
        try (final class_7202 ignored = player().field_17892.field_37951.method_41937()) {
            final int sequence = player().field_17892.field_37951.method_41942();
            player().field_3944.method_52787(packet.predict(sequence));
        }
    }
    
    public static void sendSilentPacket(final class_2596<?> packet) {
        try {
            NetworkUtil.silentPacket = true;
            sendPacket(packet);
        }
        finally {
            NetworkUtil.silentPacket = false;
        }
    }
    
    public static void sendSilentPacket(final class_7204 packet) {
        try {
            NetworkUtil.silentPacket = true;
            sendPacket(packet);
        }
        finally {
            NetworkUtil.silentPacket = false;
        }
    }
    
    private static class_746 player() {
        return SharedClass.player();
    }
    
    @Generated
    private NetworkUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    @Generated
    public static boolean silentPacket() {
        return NetworkUtil.silentPacket;
    }
    
    static {
        NetworkUtil.silentPacket = false;
    }
}
