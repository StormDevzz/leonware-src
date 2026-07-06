/*
 * Decompiled with CFR 0.152.
 */
package eu.donyka.discord.connection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.prefs.Preferences;

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

    private WinRegistry() {
    }

    static void createKey(String key) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        int[] ret = WinRegistry.createKey(userRoot, -2147483647, key);
        regCloseKey.invoke((Object)userRoot, ret[0]);
        if (ret[1] != 0) {
            throw new IllegalArgumentException("rc=" + ret[1] + "  key=" + key);
        }
    }

    static void writeStringValue(String key, String valueName, String value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        WinRegistry.writeStringValue(userRoot, -2147483647, key, valueName, value);
    }

    static String readString() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        int[] handles = (int[])regOpenKey.invoke((Object)userRoot, -2147483647, WinRegistry.toCstr("Software\\\\Valve\\\\Steam"), 131097);
        if (handles[1] != 0) {
            return null;
        }
        byte[] valb = (byte[])regQueryValueEx.invoke((Object)userRoot, handles[0], WinRegistry.toCstr("SteamExe"));
        regCloseKey.invoke((Object)userRoot, handles[0]);
        return valb != null ? new String(valb).trim() : null;
    }

    private static int[] createKey(Preferences root, int hkey, String key) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        return (int[])regCreateKeyEx.invoke((Object)root, hkey, WinRegistry.toCstr(key));
    }

    private static void writeStringValue(Preferences root, int hkey, String key, String valueName, String value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        int[] handles = (int[])regOpenKey.invoke((Object)root, hkey, WinRegistry.toCstr(key), 983103);
        regSetValueEx.invoke((Object)root, handles[0], WinRegistry.toCstr(valueName), WinRegistry.toCstr(value));
        regCloseKey.invoke((Object)root, handles[0]);
    }

    private static byte[] toCstr(String str) {
        byte[] result = new byte[str.length() + 1];
        for (int i = 0; i < str.length(); ++i) {
            result[i] = (byte)str.charAt(i);
        }
        result[str.length()] = 0;
        return result;
    }

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
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

