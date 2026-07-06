// 
// Decompiled by Procyon v0.6.0
// 

package eu.donyka.discord.utils;

public class OSDetector
{
    public static final OSDetector INSTANCE;
    
    public OSType detectOs() {
        final String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            return OSType.WINDOWS;
        }
        if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
            return OSType.LINUX;
        }
        if (osName.contains("mac")) {
            return OSType.MACOS;
        }
        return OSType.UNKNOWN;
    }
    
    static {
        INSTANCE = new OSDetector();
    }
    
    public enum OSType
    {
        WINDOWS, 
        LINUX, 
        MACOS, 
        UNKNOWN;
        
        public boolean isWindows() {
            return this == OSType.WINDOWS;
        }
        
        public boolean isLinux() {
            return this == OSType.LINUX;
        }
        
        public boolean isMac() {
            return this == OSType.MACOS;
        }
    }
}
