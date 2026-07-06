// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.system.backend;

import lombok.Generated;
import java.lang.reflect.Field;
import org.lwjgl.glfw.GLFW;
import sweetie.leonware.api.system.interfaces.QuickImports;

public final class KeyStorage implements QuickImports
{
    public static boolean isPressed(final int keyCode) {
        if (keyCode == -1 || keyCode == -999) {
            return false;
        }
        if (keyCode <= -93) {
            return GLFW.glfwGetMouseButton(KeyStorage.mc.method_22683().method_4490(), 100 + keyCode) == 1;
        }
        return GLFW.glfwGetKey(KeyStorage.mc.method_22683().method_4490(), keyCode) == 1;
    }
    
    public static int getBind(final String s) {
        if (s == null || s.isEmpty()) {
            return -1;
        }
        if (s.equalsIgnoreCase("None")) {
            return -1;
        }
        if (s.startsWith("Mouse")) {
            try {
                final int mouseIndex = Integer.parseInt(s.substring(5)) - 1;
                if (mouseIndex >= 0 && mouseIndex <= 7) {
                    return -100 + mouseIndex;
                }
            }
            catch (final NumberFormatException ex) {}
            return -1;
        }
        try {
            for (final Field field : GLFW.class.getDeclaredFields()) {
                if (field.getName().startsWith("GLFW_KEY_") && field.getType() == Integer.TYPE) {
                    final String fieldName = field.getName().substring("GLFW_KEY_".length());
                    if (formatKeyLabel(fieldName).equalsIgnoreCase(s) || fieldName.equalsIgnoreCase(s)) {
                        return field.getInt(null);
                    }
                }
            }
        }
        catch (final IllegalAccessException e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    public static String getBind(final int key) {
        if (key == -1 || key == -999) {
            return "None";
        }
        for (int i = 0; i <= 7; ++i) {
            if (key == -100 + i) {
                return "Mouse" + (i + 1);
            }
        }
        try {
            for (final Field field : GLFW.class.getDeclaredFields()) {
                if (field.getName().startsWith("GLFW_KEY_") && field.getType() == Integer.TYPE && field.getInt(null) == key) {
                    final String fieldName = field.getName().substring("GLFW_KEY_".length());
                    return formatKeyLabel(fieldName);
                }
            }
        }
        catch (final IllegalAccessException e) {
            e.printStackTrace();
        }
        return "None";
    }
    
    private static String formatKeyLabel(final String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        if (input.startsWith("LEFT_")) {
            return "L" + formatWord(input.substring(5));
        }
        if (input.startsWith("RIGHT_")) {
            return "R" + formatWord(input.substring(6));
        }
        if (input.startsWith("KP_")) {
            return "Numpad" + formatWord(input.substring(3));
        }
        return switch (input) {
            case "PRINT_SCREEN" -> "PrintScreen";
            case "CAPS_LOCK" -> "CapsLock";
            case "NUM_LOCK" -> "NumLock";
            case "PAGE_UP" -> "PageUp";
            case "PAGE_DOWN" -> "PageDown";
            default -> formatWord(input.replace("_", ""));
        };
    }
    
    private static String formatWord(final String raw) {
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
