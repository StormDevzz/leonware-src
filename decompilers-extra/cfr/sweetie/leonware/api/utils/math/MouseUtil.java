/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.utils.math;

import lombok.Generated;
import sweetie.leonware.api.system.interfaces.QuickImports;

public final class MouseUtil
implements QuickImports {
    public static float getGCD() {
        float sensitivity = ((Double)MouseUtil.mc.field_1690.method_42495().method_41753()).floatValue() * 0.6f + 0.2f;
        float pow = sensitivity * sensitivity * sensitivity * 8.0f;
        return pow * 0.15f;
    }

    public static boolean isHovered(float mouseX, float mouseY, float x, float y, float width, float height) {
        return mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + height;
    }

    public static boolean isHovered(double mouseX, double mouseY, double x, double y, double width, double height) {
        return mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + height;
    }

    public static boolean isHovered(float mouseX, float mouseY, float x, float y, float width, float height, float round) {
        float nearestY;
        float dy;
        if (mouseX < x || mouseY < y || mouseX > x + width || mouseY > y + height) {
            return false;
        }
        if (round <= 0.0f) {
            return true;
        }
        float nearestX = Math.max(x + round, Math.min(mouseX, x + width - round));
        float dx = mouseX - nearestX;
        float distanceSq = dx * dx + (dy = mouseY - (nearestY = Math.max(y + round, Math.min(mouseY, y + height - round)))) * dy;
        return distanceSq <= round * round;
    }

    public static boolean isHovered(double mouseX, double mouseY, double x, double y, double width, double height, double round) {
        double nearestY;
        double dy;
        if (mouseX < x || mouseY < y || mouseX > x + width || mouseY > y + height) {
            return false;
        }
        if (round <= 0.0) {
            return true;
        }
        double nearestX = Math.max(x + round, Math.min(mouseX, x + width - round));
        double dx = mouseX - nearestX;
        double distanceSq = dx * dx + (dy = mouseY - (nearestY = Math.max(y + round, Math.min(mouseY, y + height - round)))) * dy;
        return distanceSq <= round * round;
    }

    @Generated
    private MouseUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

