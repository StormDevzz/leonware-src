package sweetie.leonware.api.utils.other;

import lombok.Generated;
import net.minecraft.class_2378;
import net.minecraft.class_2960;
import net.minecraft.class_3414;
import net.minecraft.class_3419;
import net.minecraft.class_7923;
import sweetie.leonware.api.system.backend.ClientInfo;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.client.features.modules.other.ToggleSoundsModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/other/SoundUtil.class */
public final class SoundUtil implements QuickImports {
    private static final class_2960 ENABLE_SMOOTH_SOUND = class_2960.method_60654(path() + "smooth_on");
    public static class_3414 ENABLE_SMOOTH_EVENT = class_3414.method_47908(ENABLE_SMOOTH_SOUND);
    private static final class_2960 DISABLE_SMOOTH_SOUND = class_2960.method_60654(path() + "smooth_off");
    public static class_3414 DISABLE_SMOOTH_EVENT = class_3414.method_47908(DISABLE_SMOOTH_SOUND);
    private static final class_2960 ENABLE_CEL_SOUND = class_2960.method_60654(path() + "celestial_on");
    public static class_3414 ENABLE_CEL_EVENT = class_3414.method_47908(ENABLE_CEL_SOUND);
    private static final class_2960 DISABLE_CEL_SOUND = class_2960.method_60654(path() + "celestial_off");
    public static class_3414 DISABLE_CEL_EVENT = class_3414.method_47908(DISABLE_CEL_SOUND);
    private static final class_2960 ENABLE_NU_SOUND = class_2960.method_60654(path() + "nursultan_on");
    public static class_3414 ENABLE_NU_EVENT = class_3414.method_47908(ENABLE_NU_SOUND);
    private static final class_2960 DISABLE_NU_SOUND = class_2960.method_60654(path() + "nursultan_off");
    public static class_3414 DISABLE_NU_EVENT = class_3414.method_47908(DISABLE_NU_SOUND);
    private static final class_2960 ENABLE_AK_SOUND = class_2960.method_60654(path() + "akrien_on");
    public static class_3414 ENABLE_AK_EVENT = class_3414.method_47908(ENABLE_AK_SOUND);
    private static final class_2960 DISABLE_AK_SOUND = class_2960.method_60654(path() + "akrien_off");
    public static class_3414 DISABLE_AK_EVENT = class_3414.method_47908(DISABLE_AK_SOUND);
    private static final class_2960 ENABLE_TECH_SOUND = class_2960.method_60654(path() + "tech_on");
    public static class_3414 ENABLE_TECH_EVENT = class_3414.method_47908(ENABLE_TECH_SOUND);
    private static final class_2960 DISABLE_TECH_SOUND = class_2960.method_60654(path() + "tech_off");
    public static class_3414 DISABLE_TECH_EVENT = class_3414.method_47908(DISABLE_TECH_SOUND);
    private static final class_2960 ENABLE_BLOP_SOUND = class_2960.method_60654(path() + "blop_on");
    public static class_3414 ENABLE_BLOP_EVENT = class_3414.method_47908(ENABLE_BLOP_SOUND);
    private static final class_2960 DISABLE_BLOP_SOUND = class_2960.method_60654(path() + "blop_off");
    public static class_3414 DISABLE_BLOP_EVENT = class_3414.method_47908(DISABLE_BLOP_SOUND);
    private static final class_2960 NEVERLOSE_SOUND = class_2960.method_60654(path() + "neverlose");
    public static class_3414 NEVERLOSE_EVENT = class_3414.method_47908(NEVERLOSE_SOUND);
    private static final class_2960 SCHOOLBOY_SOUND = class_2960.method_60654(path() + "schoolboy");
    public static class_3414 SCHOOLBOY_EVENT = class_3414.method_47908(SCHOOLBOY_SOUND);
    private static final class_2960 SCHOOLBOY2_SOUND = class_2960.method_60654(path() + "schoolboy2");
    public static class_3414 SCHOOLBOY2_EVENT = class_3414.method_47908(SCHOOLBOY2_SOUND);
    private static final class_2960 WASTED_SOUND = class_2960.method_60654(path() + "wasted");
    public static class_3414 WASTED_EVENT = class_3414.method_47908(WASTED_SOUND);

    @Generated
    private SoundUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void load() {
        class_2378.method_10230(class_7923.field_41172, ENABLE_SMOOTH_SOUND, ENABLE_SMOOTH_EVENT);
        class_2378.method_10230(class_7923.field_41172, DISABLE_SMOOTH_SOUND, DISABLE_SMOOTH_EVENT);
        class_2378.method_10230(class_7923.field_41172, ENABLE_CEL_SOUND, ENABLE_CEL_EVENT);
        class_2378.method_10230(class_7923.field_41172, DISABLE_CEL_SOUND, DISABLE_CEL_EVENT);
        class_2378.method_10230(class_7923.field_41172, ENABLE_NU_SOUND, ENABLE_NU_EVENT);
        class_2378.method_10230(class_7923.field_41172, DISABLE_NU_SOUND, DISABLE_NU_EVENT);
        class_2378.method_10230(class_7923.field_41172, ENABLE_AK_SOUND, ENABLE_AK_EVENT);
        class_2378.method_10230(class_7923.field_41172, DISABLE_AK_SOUND, DISABLE_AK_EVENT);
        class_2378.method_10230(class_7923.field_41172, ENABLE_TECH_SOUND, ENABLE_TECH_EVENT);
        class_2378.method_10230(class_7923.field_41172, DISABLE_TECH_SOUND, DISABLE_TECH_EVENT);
        class_2378.method_10230(class_7923.field_41172, ENABLE_BLOP_SOUND, ENABLE_BLOP_EVENT);
        class_2378.method_10230(class_7923.field_41172, DISABLE_BLOP_SOUND, DISABLE_BLOP_EVENT);
        class_2378.method_10230(class_7923.field_41172, NEVERLOSE_SOUND, NEVERLOSE_EVENT);
        class_2378.method_10230(class_7923.field_41172, SCHOOLBOY_SOUND, SCHOOLBOY_EVENT);
        class_2378.method_10230(class_7923.field_41172, SCHOOLBOY2_SOUND, SCHOOLBOY2_EVENT);
        class_2378.method_10230(class_7923.field_41172, WASTED_SOUND, WASTED_EVENT);
    }

    public static void playSound(class_3414 sound) {
        if (mc.field_1724 != null && mc.field_1687 != null && mc.method_1560() != null) {
            mc.field_1687.method_8396(mc.field_1724, mc.method_1560().method_24515(), sound, class_3419.field_15245, ToggleSoundsModule.getInstance().volume.getValue().floatValue() / 100.0f, 1.0f);
        }
    }

    private static String path() {
        return ClientInfo.NAME.toLowerCase() + ":";
    }
}
