/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1657
 *  net.minecraft.class_2378
 *  net.minecraft.class_2960
 *  net.minecraft.class_3414
 *  net.minecraft.class_3419
 *  net.minecraft.class_7923
 */
package sweetie.leonware.api.utils.other;

import lombok.Generated;
import net.minecraft.class_1657;
import net.minecraft.class_2378;
import net.minecraft.class_2960;
import net.minecraft.class_3414;
import net.minecraft.class_3419;
import net.minecraft.class_7923;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.client.features.modules.other.ToggleSoundsModule;

public final class SoundUtil
implements QuickImports {
    private static final class_2960 ENABLE_SMOOTH_SOUND = class_2960.method_60654((String)(SoundUtil.path() + "smooth_on"));
    public static class_3414 ENABLE_SMOOTH_EVENT = class_3414.method_47908((class_2960)ENABLE_SMOOTH_SOUND);
    private static final class_2960 DISABLE_SMOOTH_SOUND = class_2960.method_60654((String)(SoundUtil.path() + "smooth_off"));
    public static class_3414 DISABLE_SMOOTH_EVENT = class_3414.method_47908((class_2960)DISABLE_SMOOTH_SOUND);
    private static final class_2960 ENABLE_CEL_SOUND = class_2960.method_60654((String)(SoundUtil.path() + "celestial_on"));
    public static class_3414 ENABLE_CEL_EVENT = class_3414.method_47908((class_2960)ENABLE_CEL_SOUND);
    private static final class_2960 DISABLE_CEL_SOUND = class_2960.method_60654((String)(SoundUtil.path() + "celestial_off"));
    public static class_3414 DISABLE_CEL_EVENT = class_3414.method_47908((class_2960)DISABLE_CEL_SOUND);
    private static final class_2960 ENABLE_NU_SOUND = class_2960.method_60654((String)(SoundUtil.path() + "nursultan_on"));
    public static class_3414 ENABLE_NU_EVENT = class_3414.method_47908((class_2960)ENABLE_NU_SOUND);
    private static final class_2960 DISABLE_NU_SOUND = class_2960.method_60654((String)(SoundUtil.path() + "nursultan_off"));
    public static class_3414 DISABLE_NU_EVENT = class_3414.method_47908((class_2960)DISABLE_NU_SOUND);
    private static final class_2960 ENABLE_AK_SOUND = class_2960.method_60654((String)(SoundUtil.path() + "akrien_on"));
    public static class_3414 ENABLE_AK_EVENT = class_3414.method_47908((class_2960)ENABLE_AK_SOUND);
    private static final class_2960 DISABLE_AK_SOUND = class_2960.method_60654((String)(SoundUtil.path() + "akrien_off"));
    public static class_3414 DISABLE_AK_EVENT = class_3414.method_47908((class_2960)DISABLE_AK_SOUND);
    private static final class_2960 ENABLE_TECH_SOUND = class_2960.method_60654((String)(SoundUtil.path() + "tech_on"));
    public static class_3414 ENABLE_TECH_EVENT = class_3414.method_47908((class_2960)ENABLE_TECH_SOUND);
    private static final class_2960 DISABLE_TECH_SOUND = class_2960.method_60654((String)(SoundUtil.path() + "tech_off"));
    public static class_3414 DISABLE_TECH_EVENT = class_3414.method_47908((class_2960)DISABLE_TECH_SOUND);
    private static final class_2960 ENABLE_BLOP_SOUND = class_2960.method_60654((String)(SoundUtil.path() + "blop_on"));
    public static class_3414 ENABLE_BLOP_EVENT = class_3414.method_47908((class_2960)ENABLE_BLOP_SOUND);
    private static final class_2960 DISABLE_BLOP_SOUND = class_2960.method_60654((String)(SoundUtil.path() + "blop_off"));
    public static class_3414 DISABLE_BLOP_EVENT = class_3414.method_47908((class_2960)DISABLE_BLOP_SOUND);
    private static final class_2960 NEVERLOSE_SOUND = class_2960.method_60654((String)(SoundUtil.path() + "neverlose"));
    public static class_3414 NEVERLOSE_EVENT = class_3414.method_47908((class_2960)NEVERLOSE_SOUND);
    private static final class_2960 SCHOOLBOY_SOUND = class_2960.method_60654((String)(SoundUtil.path() + "schoolboy"));
    public static class_3414 SCHOOLBOY_EVENT = class_3414.method_47908((class_2960)SCHOOLBOY_SOUND);
    private static final class_2960 SCHOOLBOY2_SOUND = class_2960.method_60654((String)(SoundUtil.path() + "schoolboy2"));
    public static class_3414 SCHOOLBOY2_EVENT = class_3414.method_47908((class_2960)SCHOOLBOY2_SOUND);
    private static final class_2960 WASTED_SOUND = class_2960.method_60654((String)(SoundUtil.path() + "wasted"));
    public static class_3414 WASTED_EVENT = class_3414.method_47908((class_2960)WASTED_SOUND);

    public static void load() {
        class_2378.method_10230((class_2378)class_7923.field_41172, (class_2960)ENABLE_SMOOTH_SOUND, (Object)ENABLE_SMOOTH_EVENT);
        class_2378.method_10230((class_2378)class_7923.field_41172, (class_2960)DISABLE_SMOOTH_SOUND, (Object)DISABLE_SMOOTH_EVENT);
        class_2378.method_10230((class_2378)class_7923.field_41172, (class_2960)ENABLE_CEL_SOUND, (Object)ENABLE_CEL_EVENT);
        class_2378.method_10230((class_2378)class_7923.field_41172, (class_2960)DISABLE_CEL_SOUND, (Object)DISABLE_CEL_EVENT);
        class_2378.method_10230((class_2378)class_7923.field_41172, (class_2960)ENABLE_NU_SOUND, (Object)ENABLE_NU_EVENT);
        class_2378.method_10230((class_2378)class_7923.field_41172, (class_2960)DISABLE_NU_SOUND, (Object)DISABLE_NU_EVENT);
        class_2378.method_10230((class_2378)class_7923.field_41172, (class_2960)ENABLE_AK_SOUND, (Object)ENABLE_AK_EVENT);
        class_2378.method_10230((class_2378)class_7923.field_41172, (class_2960)DISABLE_AK_SOUND, (Object)DISABLE_AK_EVENT);
        class_2378.method_10230((class_2378)class_7923.field_41172, (class_2960)ENABLE_TECH_SOUND, (Object)ENABLE_TECH_EVENT);
        class_2378.method_10230((class_2378)class_7923.field_41172, (class_2960)DISABLE_TECH_SOUND, (Object)DISABLE_TECH_EVENT);
        class_2378.method_10230((class_2378)class_7923.field_41172, (class_2960)ENABLE_BLOP_SOUND, (Object)ENABLE_BLOP_EVENT);
        class_2378.method_10230((class_2378)class_7923.field_41172, (class_2960)DISABLE_BLOP_SOUND, (Object)DISABLE_BLOP_EVENT);
        class_2378.method_10230((class_2378)class_7923.field_41172, (class_2960)NEVERLOSE_SOUND, (Object)NEVERLOSE_EVENT);
        class_2378.method_10230((class_2378)class_7923.field_41172, (class_2960)SCHOOLBOY_SOUND, (Object)SCHOOLBOY_EVENT);
        class_2378.method_10230((class_2378)class_7923.field_41172, (class_2960)SCHOOLBOY2_SOUND, (Object)SCHOOLBOY2_EVENT);
        class_2378.method_10230((class_2378)class_7923.field_41172, (class_2960)WASTED_SOUND, (Object)WASTED_EVENT);
    }

    public static void playSound(class_3414 sound) {
        if (SoundUtil.mc.field_1724 != null && SoundUtil.mc.field_1687 != null && mc.method_1560() != null) {
            SoundUtil.mc.field_1687.method_8396((class_1657)SoundUtil.mc.field_1724, mc.method_1560().method_24515(), sound, class_3419.field_15245, ((Float)ToggleSoundsModule.getInstance().volume.getValue()).floatValue() / 100.0f, 1.0f);
        }
    }

    private static String path() {
        return "LeonWare".toLowerCase() + ":";
    }

    @Generated
    private SoundUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

