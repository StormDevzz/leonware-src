/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_2596
 *  net.minecraft.class_7202
 *  net.minecraft.class_7204
 *  net.minecraft.class_746
 */
package sweetie.leonware.api.utils.other;

import lombok.Generated;
import net.minecraft.class_2596;
import net.minecraft.class_7202;
import net.minecraft.class_7204;
import net.minecraft.class_746;
import sweetie.leonware.api.system.backend.SharedClass;

public final class NetworkUtil {
    private static boolean silentPacket = false;

    public static void sendPacket(class_2596<?> packet) {
        assert (NetworkUtil.player() != null);
        NetworkUtil.player().field_3944.method_52787(packet);
    }

    public static void sendPacket(class_7204 packet) {
        assert (NetworkUtil.player() != null);
        try (class_7202 ignored = NetworkUtil.player().field_17892.field_37951.method_41937();){
            int sequence = NetworkUtil.player().field_17892.field_37951.method_41942();
            NetworkUtil.player().field_3944.method_52787(packet.predict(sequence));
        }
    }

    public static void sendSilentPacket(class_2596<?> packet) {
        try {
            silentPacket = true;
            NetworkUtil.sendPacket(packet);
        }
        finally {
            silentPacket = false;
        }
    }

    public static void sendSilentPacket(class_7204 packet) {
        try {
            silentPacket = true;
            NetworkUtil.sendPacket(packet);
        }
        finally {
            silentPacket = false;
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
        return silentPacket;
    }
}

