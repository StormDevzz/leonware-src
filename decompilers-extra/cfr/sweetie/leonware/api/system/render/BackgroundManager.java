/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2960
 */
package sweetie.leonware.api.system.render;

import net.minecraft.class_2960;
import sweetie.leonware.api.system.files.FileUtil;

public class BackgroundManager {
    public static class_2960 MAIN_BACKGROUND;

    public static void load() {
        MAIN_BACKGROUND = FileUtil.getImage("leon/global");
    }
}

