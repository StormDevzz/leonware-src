package sweetie.leonware.api.system.backend;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import lombok.Generated;
import net.minecraft.class_310;
import net.minecraft.class_337;
import net.minecraft.class_345;
import net.minecraft.class_638;
import net.minecraft.class_746;
import sweetie.leonware.api.system.interfaces.QuickImports;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/system/backend/SharedClass.class */
public final class SharedClass implements QuickImports {
    @Generated
    private SharedClass() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static class_746 player() {
        return class_310.method_1551().field_1724;
    }

    public static class_638 world() {
        return class_310.method_1551().field_1687;
    }

    public static boolean inPvp() {
        if (mc == null || mc.field_1705 == null) {
            return false;
        }
        class_337 bossOverlayGui = mc.field_1705.method_1740();
        Map<UUID, class_345> bossBars = bossOverlayGui.field_2060;
        for (class_345 bossInfo : bossBars.values()) {
            String nameStrLower = bossInfo.method_5414().getString().toLowerCase(Locale.ROOT);
            if (nameStrLower.contains("pvp") || nameStrLower.contains("пвп")) {
                return true;
            }
        }
        return false;
    }

    public static boolean openFolder(String path) {
        Process p;
        String os = getOSName().toLowerCase();
        try {
            if (os.contains("win")) {
                Runtime.getRuntime().exec("explorer \"" + path + "\"");
                return true;
            }
            if (os.contains("mac")) {
                Runtime.getRuntime().exec(new String[]{"open", path});
                return true;
            }
            if (os.contains("lin")) {
                String[] fileManagers = {"thunar", "dolphin", "nautilus", "nemo", "pcmanfm", "caja", "konqueror"};
                for (String manager : fileManagers) {
                    try {
                        p = Runtime.getRuntime().exec(new String[]{manager, path});
                    } catch (IOException e) {
                    }
                    if (p.isAlive()) {
                        return true;
                    }
                }
            }
            return false;
        } catch (IOException e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public static String getOSName() {
        if (System.getProperty("java.vendor").toLowerCase().contains("android") || System.getProperty("java.vm.vendor").toLowerCase().contains("android")) {
            return "Android";
        }
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            return "Windows";
        }
        if (osName.contains("mac")) {
            return "MacOS";
        }
        if (osName.contains("lin")) {
            return "Linux";
        }
        if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
            return "Linux/Unix";
        }
        return "Unknown";
    }
}
