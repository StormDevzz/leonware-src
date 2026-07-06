package sweetie.leonware.api.system.interfaces;

import net.minecraft.class_2596;
import net.minecraft.class_310;
import net.minecraft.class_7204;
import sweetie.leonware.api.utils.other.NetworkUtil;
import sweetie.leonware.api.utils.other.TextUtil;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/system/interfaces/QuickImports.class */
public interface QuickImports {
    public static final class_310 mc = class_310.method_1551();

    default void print(String message) {
        TextUtil.sendMessage(message);
    }

    default void sendPacket(class_7204 packet) {
        NetworkUtil.sendPacket(packet);
    }

    default void sendSilentPacket(class_7204 packet) {
        NetworkUtil.sendSilentPacket(packet);
    }

    default void sendPacket(class_2596<?> packet) {
        NetworkUtil.sendPacket(packet);
    }

    default void sendSilentPacket(class_2596<?> packet) {
        NetworkUtil.sendSilentPacket(packet);
    }
}
