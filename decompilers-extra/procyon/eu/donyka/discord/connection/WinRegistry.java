// 
// Decompiled by Procyon v0.6.0
// 

package eu.donyka.discord.connection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.prefs.Preferences;

class WinRegistry
{
    static final int HKEY_CURRENT_USER = -2147483647;
    private static final int REG_SUCCESS = 0;
    private static final int KEY_ALL_ACCESS = 983103;
    private static final int KEY_READ = 131097;
    static final Preferences userRoot;
    private static final Class<? extends Preferences> userClass;
    private static final Method regOpenKey;
    private static final Method regCloseKey;
    private static final Method regQueryValueEx;
    private static final Method regCreateKeyEx;
    private static final Method regSetValueEx;
    
    private WinRegistry() {
    }
    
    static void createKey(final String key) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        final int[] ret = createKey(WinRegistry.userRoot, -2147483647, key);
        WinRegistry.regCloseKey.invoke(WinRegistry.userRoot, ret[0]);
        if (ret[1] != 0) {
            throw new IllegalArgumentException("rc=" + ret[1] + "  key=" + key);
        }
    }
    
    static void writeStringValue(final String key, final String valueName, final String value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        writeStringValue(WinRegistry.userRoot, -2147483647, key, valueName, value);
    }
    
    static String readString() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        final int[] handles = (int[])WinRegistry.regOpenKey.invoke(WinRegistry.userRoot, -2147483647, toCstr("Software\\\\Valve\\\\Steam"), 131097);
        if (handles[1] != 0) {
            return null;
        }
        final byte[] valb = (byte[])WinRegistry.regQueryValueEx.invoke(WinRegistry.userRoot, handles[0], toCstr("SteamExe"));
        WinRegistry.regCloseKey.invoke(WinRegistry.userRoot, handles[0]);
        return (valb != null) ? new String(valb).trim() : null;
    }
    
    private static int[] createKey(final Preferences root, final int hkey, final String key) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        return (int[])WinRegistry.regCreateKeyEx.invoke(root, hkey, toCstr(key));
    }
    
    private static void writeStringValue(final Preferences root, final int hkey, final String key, final String valueName, final String value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        final int[] handles = (int[])WinRegistry.regOpenKey.invoke(root, hkey, toCstr(key), 983103);
        WinRegistry.regSetValueEx.invoke(root, handles[0], toCstr(valueName), toCstr(value));
        WinRegistry.regCloseKey.invoke(root, handles[0]);
    }
    
    private static byte[] toCstr(final String str) {
        final byte[] result = new byte[str.length() + 1];
        for (int i = 0; i < str.length(); ++i) {
            result[i] = (byte)str.charAt(i);
        }
        result[str.length()] = 0;
        return result;
    }
    
    static {
        userRoot = Preferences.userRoot();
        userClass = WinRegistry.userRoot.getClass();
        try {
            (regOpenKey = WinRegistry.userClass.getDeclaredMethod("WindowsRegOpenKey", Integer.TYPE, byte[].class, Integer.TYPE)).setAccessible(true);
            (regCloseKey = WinRegistry.userClass.getDeclaredMethod("WindowsRegCloseKey", Integer.TYPE)).setAccessible(true);
            (regQueryValueEx = WinRegistry.userClass.getDeclaredMethod("WindowsRegQueryValueEx", Integer.TYPE, byte[].class)).setAccessible(true);
            (regCreateKeyEx = WinRegistry.userClass.getDeclaredMethod("WindowsRegCreateKeyEx", Integer.TYPE, byte[].class)).setAccessible(true);
            (regSetValueEx = WinRegistry.userClass.getDeclaredMethod("WindowsRegSetValueEx", Integer.TYPE, byte[].class, byte[].class)).setAccessible(true);
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
