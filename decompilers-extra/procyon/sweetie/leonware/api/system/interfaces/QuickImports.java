// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.system.interfaces;

import net.minecraft.class_2596;
import sweetie.leonware.api.utils.other.NetworkUtil;
import net.minecraft.class_7204;
import sweetie.leonware.api.utils.other.TextUtil;
import net.minecraft.class_310;

public interface QuickImports
{
    public static final class_310 mc = class_310.method_1551();
    
    default void print(final String message) {
        TextUtil.sendMessage(message);
    }
    
    default void sendPacket(final class_7204 packet) {
        NetworkUtil.sendPacket(packet);
    }
    
    default void sendSilentPacket(final class_7204 packet) {
        NetworkUtil.sendSilentPacket(packet);
    }
    
    default void sendPacket(final class_2596<?> packet) {
        NetworkUtil.sendPacket(packet);
    }
    
    default void sendSilentPacket(final class_2596<?> packet) {
        NetworkUtil.sendSilentPacket(packet);
    }
}
