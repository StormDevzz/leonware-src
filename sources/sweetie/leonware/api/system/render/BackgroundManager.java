package sweetie.leonware.api.system.render;

import net.minecraft.class_2960;
import sweetie.leonware.api.system.files.FileUtil;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/system/render/BackgroundManager.class */
public class BackgroundManager {
    public static class_2960 MAIN_BACKGROUND;

    public static void load() {
        MAIN_BACKGROUND = FileUtil.getImage("leon/global");
    }
}
