// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.system.render;

import sweetie.leonware.api.system.files.FileUtil;
import net.minecraft.class_2960;

public class BackgroundManager
{
    public static class_2960 MAIN_BACKGROUND;
    
    public static void load() {
        BackgroundManager.MAIN_BACKGROUND = FileUtil.getImage("leon/global");
    }
}
