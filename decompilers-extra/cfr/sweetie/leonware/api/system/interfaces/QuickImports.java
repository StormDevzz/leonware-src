/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2596
 *  net.minecraft.class_310
 *  net.minecraft.class_7204
 */
package sweetie.leonware.api.system.interfaces;

import net.minecraft.class_2596;
import net.minecraft.class_310;
import net.minecraft.class_7204;
import sweetie.leonware.api.utils.other.NetworkUtil;
import sweetie.leonware.api.utils.other.TextUtil;

public interface QuickImports {
    public static final class_310 mc = class_310.method_1551();

    default public void print(String message) {
        TextUtil.sendMessage(message);
    }

    default public void sendPacket(class_7204 packet) {
        NetworkUtil.sendPacket(packet);
    }

    default public void sendSilentPacket(class_7204 packet) {
        NetworkUtil.sendSilentPacket(packet);
    }

    default public void sendPacket(class_2596<?> packet) {
        NetworkUtil.sendPacket(packet);
    }

    default public void sendSilentPacket(class_2596<?> packet) {
        NetworkUtil.sendSilentPacket(packet);
    }
}

