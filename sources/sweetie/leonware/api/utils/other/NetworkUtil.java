package sweetie.leonware.api.utils.other;

import lombok.Generated;
import net.minecraft.class_2596;
import net.minecraft.class_7202;
import net.minecraft.class_7204;
import net.minecraft.class_746;
import sweetie.leonware.api.system.backend.SharedClass;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/other/NetworkUtil.class */
public final class NetworkUtil {
    private static boolean silentPacket;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !NetworkUtil.class.desiredAssertionStatus();
        silentPacket = false;
    }

    @Generated
    private NetworkUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @Generated
    public static boolean silentPacket() {
        return silentPacket;
    }

    public static void sendPacket(class_2596<?> packet) {
        if (!$assertionsDisabled && player() == null) {
            throw new AssertionError();
        }
        player().field_3944.method_52787(packet);
    }

    public static void sendPacket(class_7204 packet) {
        if (!$assertionsDisabled && player() == null) {
            throw new AssertionError();
        }
        class_7202 ignored = player().field_17892.field_37951.method_41937();
        try {
            int sequence = player().field_17892.field_37951.method_41942();
            player().field_3944.method_52787(packet.predict(sequence));
            if (ignored != null) {
                ignored.close();
            }
        } catch (Throwable th) {
            if (ignored != null) {
                try {
                    ignored.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public static void sendSilentPacket(class_2596<?> packet) {
        try {
            silentPacket = true;
            sendPacket(packet);
            silentPacket = false;
        } catch (Throwable th) {
            silentPacket = false;
            throw th;
        }
    }

    public static void sendSilentPacket(class_7204 packet) {
        try {
            silentPacket = true;
            sendPacket(packet);
            silentPacket = false;
        } catch (Throwable th) {
            silentPacket = false;
            throw th;
        }
    }

    private static class_746 player() {
        return SharedClass.player();
    }
}
