package eu.donyka.discord.utils;

/* JADX INFO: loaded from: leonware-0.0.3.jar:eu/donyka/discord/utils/OSDetector.class */
public class OSDetector {
    public static final OSDetector INSTANCE = new OSDetector();

    public OSType detectOs() {
        String osName = System.getProperty("os.name").toLowerCase();
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

    /* JADX INFO: loaded from: leonware-0.0.3.jar:eu/donyka/discord/utils/OSDetector$OSType.class */
    public enum OSType {
        WINDOWS,
        LINUX,
        MACOS,
        UNKNOWN;

        public boolean isWindows() {
            return this == WINDOWS;
        }

        public boolean isLinux() {
            return this == LINUX;
        }

        public boolean isMac() {
            return this == MACOS;
        }
    }
}
