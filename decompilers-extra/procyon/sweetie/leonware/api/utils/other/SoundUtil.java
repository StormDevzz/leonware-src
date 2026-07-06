// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.other;

import lombok.Generated;
import net.minecraft.class_1657;
import sweetie.leonware.client.features.modules.other.ToggleSoundsModule;
import net.minecraft.class_3419;
import net.minecraft.class_2378;
import net.minecraft.class_7923;
import net.minecraft.class_3414;
import net.minecraft.class_2960;
import sweetie.leonware.api.system.interfaces.QuickImports;

public final class SoundUtil implements QuickImports
{
    private static final class_2960 ENABLE_SMOOTH_SOUND;
    public static class_3414 ENABLE_SMOOTH_EVENT;
    private static final class_2960 DISABLE_SMOOTH_SOUND;
    public static class_3414 DISABLE_SMOOTH_EVENT;
    private static final class_2960 ENABLE_CEL_SOUND;
    public static class_3414 ENABLE_CEL_EVENT;
    private static final class_2960 DISABLE_CEL_SOUND;
    public static class_3414 DISABLE_CEL_EVENT;
    private static final class_2960 ENABLE_NU_SOUND;
    public static class_3414 ENABLE_NU_EVENT;
    private static final class_2960 DISABLE_NU_SOUND;
    public static class_3414 DISABLE_NU_EVENT;
    private static final class_2960 ENABLE_AK_SOUND;
    public static class_3414 ENABLE_AK_EVENT;
    private static final class_2960 DISABLE_AK_SOUND;
    public static class_3414 DISABLE_AK_EVENT;
    private static final class_2960 ENABLE_TECH_SOUND;
    public static class_3414 ENABLE_TECH_EVENT;
    private static final class_2960 DISABLE_TECH_SOUND;
    public static class_3414 DISABLE_TECH_EVENT;
    private static final class_2960 ENABLE_BLOP_SOUND;
    public static class_3414 ENABLE_BLOP_EVENT;
    private static final class_2960 DISABLE_BLOP_SOUND;
    public static class_3414 DISABLE_BLOP_EVENT;
    private static final class_2960 NEVERLOSE_SOUND;
    public static class_3414 NEVERLOSE_EVENT;
    private static final class_2960 SCHOOLBOY_SOUND;
    public static class_3414 SCHOOLBOY_EVENT;
    private static final class_2960 SCHOOLBOY2_SOUND;
    public static class_3414 SCHOOLBOY2_EVENT;
    private static final class_2960 WASTED_SOUND;
    public static class_3414 WASTED_EVENT;
    
    public static void load() {
        class_2378.method_10230(class_7923.field_41172, SoundUtil.ENABLE_SMOOTH_SOUND, (Object)SoundUtil.ENABLE_SMOOTH_EVENT);
        class_2378.method_10230(class_7923.field_41172, SoundUtil.DISABLE_SMOOTH_SOUND, (Object)SoundUtil.DISABLE_SMOOTH_EVENT);
        class_2378.method_10230(class_7923.field_41172, SoundUtil.ENABLE_CEL_SOUND, (Object)SoundUtil.ENABLE_CEL_EVENT);
        class_2378.method_10230(class_7923.field_41172, SoundUtil.DISABLE_CEL_SOUND, (Object)SoundUtil.DISABLE_CEL_EVENT);
        class_2378.method_10230(class_7923.field_41172, SoundUtil.ENABLE_NU_SOUND, (Object)SoundUtil.ENABLE_NU_EVENT);
        class_2378.method_10230(class_7923.field_41172, SoundUtil.DISABLE_NU_SOUND, (Object)SoundUtil.DISABLE_NU_EVENT);
        class_2378.method_10230(class_7923.field_41172, SoundUtil.ENABLE_AK_SOUND, (Object)SoundUtil.ENABLE_AK_EVENT);
        class_2378.method_10230(class_7923.field_41172, SoundUtil.DISABLE_AK_SOUND, (Object)SoundUtil.DISABLE_AK_EVENT);
        class_2378.method_10230(class_7923.field_41172, SoundUtil.ENABLE_TECH_SOUND, (Object)SoundUtil.ENABLE_TECH_EVENT);
        class_2378.method_10230(class_7923.field_41172, SoundUtil.DISABLE_TECH_SOUND, (Object)SoundUtil.DISABLE_TECH_EVENT);
        class_2378.method_10230(class_7923.field_41172, SoundUtil.ENABLE_BLOP_SOUND, (Object)SoundUtil.ENABLE_BLOP_EVENT);
        class_2378.method_10230(class_7923.field_41172, SoundUtil.DISABLE_BLOP_SOUND, (Object)SoundUtil.DISABLE_BLOP_EVENT);
        class_2378.method_10230(class_7923.field_41172, SoundUtil.NEVERLOSE_SOUND, (Object)SoundUtil.NEVERLOSE_EVENT);
        class_2378.method_10230(class_7923.field_41172, SoundUtil.SCHOOLBOY_SOUND, (Object)SoundUtil.SCHOOLBOY_EVENT);
        class_2378.method_10230(class_7923.field_41172, SoundUtil.SCHOOLBOY2_SOUND, (Object)SoundUtil.SCHOOLBOY2_EVENT);
        class_2378.method_10230(class_7923.field_41172, SoundUtil.WASTED_SOUND, (Object)SoundUtil.WASTED_EVENT);
    }
    
    public static void playSound(final class_3414 sound) {
        if (SoundUtil.mc.field_1724 != null && SoundUtil.mc.field_1687 != null && SoundUtil.mc.method_1560() != null) {
            SoundUtil.mc.field_1687.method_8396((class_1657)SoundUtil.mc.field_1724, SoundUtil.mc.method_1560().method_24515(), sound, class_3419.field_15245, ToggleSoundsModule.getInstance().volume.getValue() / 100.0f, 1.0f);
        }
    }
    
    private static String path() {
        return "LeonWare".toLowerCase();
    }
    
    @Generated
    private SoundUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    static {
        ENABLE_SMOOTH_SOUND = class_2960.method_60654(path() + "smooth_on");
        SoundUtil.ENABLE_SMOOTH_EVENT = class_3414.method_47908(SoundUtil.ENABLE_SMOOTH_SOUND);
        DISABLE_SMOOTH_SOUND = class_2960.method_60654(path() + "smooth_off");
        SoundUtil.DISABLE_SMOOTH_EVENT = class_3414.method_47908(SoundUtil.DISABLE_SMOOTH_SOUND);
        ENABLE_CEL_SOUND = class_2960.method_60654(path() + "celestial_on");
        SoundUtil.ENABLE_CEL_EVENT = class_3414.method_47908(SoundUtil.ENABLE_CEL_SOUND);
        DISABLE_CEL_SOUND = class_2960.method_60654(path() + "celestial_off");
        SoundUtil.DISABLE_CEL_EVENT = class_3414.method_47908(SoundUtil.DISABLE_CEL_SOUND);
        ENABLE_NU_SOUND = class_2960.method_60654(path() + "nursultan_on");
        SoundUtil.ENABLE_NU_EVENT = class_3414.method_47908(SoundUtil.ENABLE_NU_SOUND);
        DISABLE_NU_SOUND = class_2960.method_60654(path() + "nursultan_off");
        SoundUtil.DISABLE_NU_EVENT = class_3414.method_47908(SoundUtil.DISABLE_NU_SOUND);
        ENABLE_AK_SOUND = class_2960.method_60654(path() + "akrien_on");
        SoundUtil.ENABLE_AK_EVENT = class_3414.method_47908(SoundUtil.ENABLE_AK_SOUND);
        DISABLE_AK_SOUND = class_2960.method_60654(path() + "akrien_off");
        SoundUtil.DISABLE_AK_EVENT = class_3414.method_47908(SoundUtil.DISABLE_AK_SOUND);
        ENABLE_TECH_SOUND = class_2960.method_60654(path() + "tech_on");
        SoundUtil.ENABLE_TECH_EVENT = class_3414.method_47908(SoundUtil.ENABLE_TECH_SOUND);
        DISABLE_TECH_SOUND = class_2960.method_60654(path() + "tech_off");
        SoundUtil.DISABLE_TECH_EVENT = class_3414.method_47908(SoundUtil.DISABLE_TECH_SOUND);
        ENABLE_BLOP_SOUND = class_2960.method_60654(path() + "blop_on");
        SoundUtil.ENABLE_BLOP_EVENT = class_3414.method_47908(SoundUtil.ENABLE_BLOP_SOUND);
        DISABLE_BLOP_SOUND = class_2960.method_60654(path() + "blop_off");
        SoundUtil.DISABLE_BLOP_EVENT = class_3414.method_47908(SoundUtil.DISABLE_BLOP_SOUND);
        NEVERLOSE_SOUND = class_2960.method_60654(path() + "neverlose");
        SoundUtil.NEVERLOSE_EVENT = class_3414.method_47908(SoundUtil.NEVERLOSE_SOUND);
        SCHOOLBOY_SOUND = class_2960.method_60654(path() + "schoolboy");
        SoundUtil.SCHOOLBOY_EVENT = class_3414.method_47908(SoundUtil.SCHOOLBOY_SOUND);
        SCHOOLBOY2_SOUND = class_2960.method_60654(path() + "schoolboy2");
        SoundUtil.SCHOOLBOY2_EVENT = class_3414.method_47908(SoundUtil.SCHOOLBOY2_SOUND);
        WASTED_SOUND = class_2960.method_60654(path() + "wasted");
        SoundUtil.WASTED_EVENT = class_3414.method_47908(SoundUtil.WASTED_SOUND);
    }
}
