package eu.donyka.discord.connection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.prefs.Preferences;

/* JADX INFO: loaded from: leonware-0.0.3.jar:eu/donyka/discord/connection/WinRegistry.class */
class WinRegistry {
    static final int HKEY_CURRENT_USER = -2147483647;
    private static final int REG_SUCCESS = 0;
    private static final int KEY_ALL_ACCESS = 983103;
    private static final int KEY_READ = 131097;
    static final Preferences userRoot = Preferences.userRoot();
    private static final Class<? extends Preferences> userClass = userRoot.getClass();
    private static final Method regOpenKey;
    private static final Method regCloseKey;
    private static final Method regQueryValueEx;
    private static final Method regCreateKeyEx;
    private static final Method regSetValueEx;

    static {
        try {
            regOpenKey = userClass.getDeclaredMethod("WindowsRegOpenKey", Integer.TYPE, byte[].class, Integer.TYPE);
            regOpenKey.setAccessible(true);
            regCloseKey = userClass.getDeclaredMethod("WindowsRegCloseKey", Integer.TYPE);
            regCloseKey.setAccessible(true);
            regQueryValueEx = userClass.getDeclaredMethod("WindowsRegQueryValueEx", Integer.TYPE, byte[].class);
            regQueryValueEx.setAccessible(true);
            regCreateKeyEx = userClass.getDeclaredMethod("WindowsRegCreateKeyEx", Integer.TYPE, byte[].class);
            regCreateKeyEx.setAccessible(true);
            regSetValueEx = userClass.getDeclaredMethod("WindowsRegSetValueEx", Integer.TYPE, byte[].class, byte[].class);
            regSetValueEx.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private WinRegistry() {
    }

    static void createKey(String key) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        int[] ret = createKey(userRoot, HKEY_CURRENT_USER, key);
        regCloseKey.invoke(userRoot, Integer.valueOf(ret[0]));
        if (ret[1] != 0) {
            throw new IllegalArgumentException("rc=" + ret[1] + "  key=" + key);
        }
    }

    static void writeStringValue(String key, String valueName, String value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        writeStringValue(userRoot, HKEY_CURRENT_USER, key, valueName, value);
    }

    static String readString() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        int[] handles = (int[]) regOpenKey.invoke(userRoot, Integer.valueOf(HKEY_CURRENT_USER), toCstr("Software\\\\Valve\\\\Steam"), Integer.valueOf(KEY_READ));
        if (handles[1] != 0) {
            return null;
        }
        byte[] valb = (byte[]) regQueryValueEx.invoke(userRoot, Integer.valueOf(handles[0]), toCstr("SteamExe"));
        regCloseKey.invoke(userRoot, Integer.valueOf(handles[0]));
        if (valb != null) {
            return new String(valb).trim();
        }
        return null;
    }

    private static int[] createKey(Preferences root, int hkey, String key) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return (int[]) regCreateKeyEx.invoke(root, Integer.valueOf(hkey), toCstr(key));
    }

    private static void writeStringValue(Preferences root, int hkey, String key, String valueName, String value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        int[] handles = (int[]) regOpenKey.invoke(root, Integer.valueOf(hkey), toCstr(key), Integer.valueOf(KEY_ALL_ACCESS));
        regSetValueEx.invoke(root, Integer.valueOf(handles[0]), toCstr(valueName), toCstr(value));
        regCloseKey.invoke(root, Integer.valueOf(handles[0]));
    }

    private static byte[] toCstr(String str) {
        byte[] result = new byte[str.length() + 1];
        for (int i = 0; i < str.length(); i++) {
            result[i] = (byte) str.charAt(i);
        }
        result[str.length()] = 0;
        return result;
    }
}
