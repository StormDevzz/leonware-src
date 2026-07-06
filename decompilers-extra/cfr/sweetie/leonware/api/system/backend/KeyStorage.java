/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.lwjgl.glfw.GLFW
 */
package sweetie.leonware.api.system.backend;

import java.lang.reflect.Field;
import lombok.Generated;
import org.lwjgl.glfw.GLFW;
import sweetie.leonware.api.system.interfaces.QuickImports;

public final class KeyStorage
implements QuickImports {
    public static boolean isPressed(int keyCode) {
        if (keyCode == -1 || keyCode == -999) {
            return false;
        }
        if (keyCode <= -93) {
            return GLFW.glfwGetMouseButton((long)mc.method_22683().method_4490(), (int)(100 + keyCode)) == 1;
        }
        return GLFW.glfwGetKey((long)mc.method_22683().method_4490(), (int)keyCode) == 1;
    }

    public static int getBind(String s) {
        if (s == null || s.isEmpty()) {
            return -1;
        }
        if (s.equalsIgnoreCase("None")) {
            return -1;
        }
        if (s.startsWith("Mouse")) {
            try {
                int mouseIndex = Integer.parseInt(s.substring(5)) - 1;
                if (mouseIndex >= 0 && mouseIndex <= 7) {
                    return -100 + mouseIndex;
                }
            }
            catch (NumberFormatException mouseIndex) {
                // empty catch block
            }
            return -1;
        }
        try {
            for (Field field : GLFW.class.getDeclaredFields()) {
                String fieldName;
                if (!field.getName().startsWith("GLFW_KEY_") || field.getType() != Integer.TYPE || !KeyStorage.formatKeyLabel(fieldName = field.getName().substring("GLFW_KEY_".length())).equalsIgnoreCase(s) && !fieldName.equalsIgnoreCase(s)) continue;
                return field.getInt(null);
            }
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String getBind(int key) {
        if (key == -1 || key == -999) {
            return "None";
        }
        for (int i = 0; i <= 7; ++i) {
            if (key != -100 + i) continue;
            return "Mouse" + (i + 1);
        }
        try {
            for (Field field : GLFW.class.getDeclaredFields()) {
                if (!field.getName().startsWith("GLFW_KEY_") || field.getType() != Integer.TYPE || field.getInt(null) != key) continue;
                String fieldName = field.getName().substring("GLFW_KEY_".length());
                return KeyStorage.formatKeyLabel(fieldName);
            }
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return "None";
    }

    private static String formatKeyLabel(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        if (input.startsWith("LEFT_")) {
            return "L" + KeyStorage.formatWord(input.substring(5));
        }
        if (input.startsWith("RIGHT_")) {
            return "R" + KeyStorage.formatWord(input.substring(6));
        }
        if (input.startsWith("KP_")) {
            return "Numpad" + KeyStorage.formatWord(input.substring(3));
        }
        return switch (input) {
            case "PRINT_SCREEN" -> "PrintScreen";
            case "CAPS_LOCK" -> "CapsLock";
            case "NUM_LOCK" -> "NumLock";
            case "PAGE_UP" -> "PageUp";
            case "PAGE_DOWN" -> "PageDown";
            default -> KeyStorage.formatWord(input.replace("_", ""));
        };
    }

    private static String formatWord(String raw) {
        if (raw == null || raw.isEmpty()) {
            return "";
        }
        String s = raw.toLowerCase();
        s = s.replace("accent", "");
        s = s.replace("control", "ctrl");
        s = s.replace("super", "Super");
        s = s.replace("minus", "Minus");
        s = s.replace("equals", "Equals");
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    @Generated
    private KeyStorage() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

