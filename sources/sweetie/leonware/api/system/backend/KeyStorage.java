package sweetie.leonware.api.system.backend;

import java.lang.reflect.Field;
import lombok.Generated;
import org.lwjgl.glfw.GLFW;
import sweetie.leonware.api.system.interfaces.QuickImports;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/system/backend/KeyStorage.class */
public final class KeyStorage implements QuickImports {
    @Generated
    private KeyStorage() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static boolean isPressed(int keyCode) {
        if (keyCode == -1 || keyCode == -999) {
            return false;
        }
        return keyCode <= -93 ? GLFW.glfwGetMouseButton(mc.method_22683().method_4490(), 100 + keyCode) == 1 : GLFW.glfwGetKey(mc.method_22683().method_4490(), keyCode) == 1;
    }

    public static int getBind(String s) {
        if (s == null || s.isEmpty() || s.equalsIgnoreCase("None")) {
            return -1;
        }
        if (s.startsWith("Mouse")) {
            try {
                int mouseIndex = Integer.parseInt(s.substring(5)) - 1;
                if (mouseIndex >= 0 && mouseIndex <= 7) {
                    return (-100) + mouseIndex;
                }
                return -1;
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        try {
            for (Field field : GLFW.class.getDeclaredFields()) {
                if (field.getName().startsWith("GLFW_KEY_") && field.getType() == Integer.TYPE) {
                    String fieldName = field.getName().substring("GLFW_KEY_".length());
                    if (formatKeyLabel(fieldName).equalsIgnoreCase(s) || fieldName.equalsIgnoreCase(s)) {
                        return field.getInt(null);
                    }
                }
            }
            return -1;
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
            return -1;
        }
    }

    public static String getBind(int key) {
        if (key == -1 || key == -999) {
            return "None";
        }
        for (int i = 0; i <= 7; i++) {
            if (key == (-100) + i) {
                return "Mouse" + (i + 1);
            }
        }
        try {
            for (Field field : GLFW.class.getDeclaredFields()) {
                if (field.getName().startsWith("GLFW_KEY_") && field.getType() == Integer.TYPE && field.getInt(null) == key) {
                    String fieldName = field.getName().substring("GLFW_KEY_".length());
                    return formatKeyLabel(fieldName);
                }
            }
            return "None";
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return "None";
        }
    }

    private static String formatKeyLabel(String input) {
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
        switch (input) {
            case "PRINT_SCREEN":
                return "PrintScreen";
            case "CAPS_LOCK":
                return "CapsLock";
            case "NUM_LOCK":
                return "NumLock";
            case "PAGE_UP":
                return "PageUp";
            case "PAGE_DOWN":
                return "PageDown";
            default:
                return formatWord(input.replace("_", ""));
        }
    }

    private static String formatWord(String raw) {
        if (raw == null || raw.isEmpty()) {
            return "";
        }
        String s = raw.toLowerCase().replace("accent", "").replace("control", "ctrl").replace("super", "Super").replace("minus", "Minus").replace("equals", "Equals");
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
