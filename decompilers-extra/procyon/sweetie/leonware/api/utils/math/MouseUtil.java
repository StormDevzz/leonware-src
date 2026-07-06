// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.math;

import lombok.Generated;
import sweetie.leonware.api.system.interfaces.QuickImports;

public final class MouseUtil implements QuickImports
{
    public static float getGCD() {
        final float sensitivity = ((Double)MouseUtil.mc.field_1690.method_42495().method_41753()).floatValue() * 0.6f + 0.2f;
        final float pow = sensitivity * sensitivity * sensitivity * 8.0f;
        return pow * 0.15f;
    }
    
    public static boolean isHovered(final float mouseX, final float mouseY, final float x, final float y, final float width, final float height) {
        return mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + height;
    }
    
    public static boolean isHovered(final double mouseX, final double mouseY, final double x, final double y, final double width, final double height) {
        return mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + height;
    }
    
    public static boolean isHovered(final float mouseX, final float mouseY, final float x, final float y, final float width, final float height, final float round) {
        if (mouseX < x || mouseY < y || mouseX > x + width || mouseY > y + height) {
            return false;
        }
        if (round <= 0.0f) {
            return true;
        }
        final float nearestX = Math.max(x + round, Math.min(mouseX, x + width - round));
        final float nearestY = Math.max(y + round, Math.min(mouseY, y + height - round));
        final float dx = mouseX - nearestX;
        final float dy = mouseY - nearestY;
        final float distanceSq = dx * dx + dy * dy;
        return distanceSq <= round * round;
    }
    
    public static boolean isHovered(final double mouseX, final double mouseY, final double x, final double y, final double width, final double height, final double round) {
        if (mouseX < x || mouseY < y || mouseX > x + width || mouseY > y + height) {
            return false;
        }
        if (round <= 0.0) {
            return true;
        }
        final double nearestX = Math.max(x + round, Math.min(mouseX, x + width - round));
        final double nearestY = Math.max(y + round, Math.min(mouseY, y + height - round));
        final double dx = mouseX - nearestX;
        final double dy = mouseY - nearestY;
        final double distanceSq = dx * dx + dy * dy;
        return distanceSq <= round * round;
    }
    
    @Generated
    private MouseUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
