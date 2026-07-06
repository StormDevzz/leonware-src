package sweetie.leonware.api.utils.other;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_124;
import net.minecraft.class_2561;
import net.minecraft.class_2572;
import net.minecraft.class_2583;
import net.minecraft.class_2588;
import net.minecraft.class_5250;
import net.minecraft.class_5251;
import net.minecraft.class_8828;
import org.newsclub.net.unix.AFTIPCSocketAddress;
import sweetie.leonware.api.system.backend.ClientInfo;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.color.ColorUtil;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.render.fonts.MsdfGlyph;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/other/TextUtil.class */
public final class TextUtil implements QuickImports {
    @Generated
    private TextUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    public static List<MsdfGlyph.ColoredGlyph> parseTextToColoredGlyphs(class_2561 text) throws MatchException {
        List<MsdfGlyph.ColoredGlyph> result = new ArrayList<>();
        parseTextRecursive(text, -1, result);
        return result;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    private static void parseTextRecursive(class_2561 text, int currentColor, List<MsdfGlyph.ColoredGlyph> result) throws MatchException {
        class_2583 style = text.method_10866();
        int color = style.method_10973() != null ? style.method_10973().method_27716() | (-16777216) : currentColor;
        class_8828.class_2585 class_2585VarMethod_10851 = text.method_10851();
        String raw = "";
        if (class_2585VarMethod_10851 instanceof class_8828.class_2585) {
            try {
                String string = class_2585VarMethod_10851.comp_737();
                raw = string;
            } catch (Throwable th) {
                throw new MatchException(th.toString(), th);
            }
        } else if (class_2585VarMethod_10851 instanceof class_2588) {
            class_2588 translatable = (class_2588) class_2585VarMethod_10851;
            raw = translatable.method_11022();
        } else if (class_2585VarMethod_10851 instanceof class_2572) {
            class_2572 keybind = (class_2572) class_2585VarMethod_10851;
            raw = keybind.method_10901();
        }
        String raw2 = ReplaceUtil.replaceSymbols(raw);
        int i = 0;
        while (i < raw2.length()) {
            char c = raw2.charAt(i);
            if ((c == 42263 || c == 42277) && i + 1 < raw2.length()) {
                i++;
            } else {
                result.add(new MsdfGlyph.ColoredGlyph(c, color));
            }
            i++;
        }
        for (class_2561 sibling : text.method_10855()) {
            parseTextRecursive(sibling, color, result);
        }
    }

    public static List<MsdfGlyph.ColoredGlyph> parseMinecraftFormattedString(String text) {
        List<MsdfGlyph.ColoredGlyph> glyphs = new ArrayList<>();
        int currentColor = -1;
        int i = 0;
        while (i < text.length()) {
            char c = text.charAt(i);
            if (c == 167 && i + 1 < text.length()) {
                char code = text.charAt(i + 1);
                Integer color = getColorFromCode(code);
                if (color != null) {
                    currentColor = color.intValue() | (-16777216);
                }
                i++;
            } else {
                glyphs.add(new MsdfGlyph.ColoredGlyph(c, currentColor));
            }
            i++;
        }
        return glyphs;
    }

    private static Integer getColorFromCode(char code) {
        switch (Character.toLowerCase(code)) {
            case '0':
                return 0;
            case '1':
                return 170;
            case '2':
                return 43520;
            case '3':
                return 43690;
            case '4':
                return 11141120;
            case '5':
                return 11141290;
            case '6':
                return 16755200;
            case '7':
                return 11184810;
            case '8':
                return 5592405;
            case '9':
                return 5592575;
            case ':':
            case ';':
            case '<':
            case '=':
            case '>':
            case '?':
            case AFTIPCSocketAddress.TIPC_RESERVED_TYPES /* 64 */:
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
            case 'G':
            case 'H':
            case 'I':
            case 'J':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'S':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            case 'Z':
            case '[':
            case '\\':
            case ']':
            case '^':
            case '_':
            case '`':
            default:
                return null;
            case 'a':
                return 5635925;
            case 'b':
                return 5636095;
            case 'c':
                return 16733525;
            case 'd':
                return 16733695;
            case 'e':
                return 16777045;
            case 'f':
                return 16777215;
        }
    }

    public static String stripFormatting(String text) {
        return text == null ? "" : text.replaceAll("§.", "");
    }

    public static boolean hasFormatting(String text) {
        return text != null && text.contains("§");
    }

    public static void sendMessage(String message) {
        mc.field_1724.method_7353(class_2561.method_43470("").method_10852(gradient(ClientInfo.NAME, true)).method_10852(class_2561.method_43470(String.valueOf(class_124.field_1080) + " >> " + String.valueOf(class_124.field_1070) + message)), false);
    }

    public static String getDurationText(int ticks) {
        if (ticks == -1) {
            return "**:**";
        }
        int seconds = ticks / 20;
        int minutes = seconds / 60;
        int hours = minutes / 60;
        int minutes2 = minutes % 60;
        int remainingSeconds = seconds % 60;
        if (hours > 0) {
            return String.format("%d:%02d:%02d", Integer.valueOf(hours), Integer.valueOf(minutes2), Integer.valueOf(remainingSeconds));
        }
        if (minutes2 > 0) {
            return String.format("%d:%02d", Integer.valueOf(minutes2), Integer.valueOf(remainingSeconds));
        }
        return String.format("%ds", Integer.valueOf(seconds));
    }

    public static class_5250 gradient(String message, boolean bold) {
        class_5250 text = class_2561.method_43473();
        int length = message.length();
        for (int i = 0; i < length; i++) {
            Color color = ColorUtil.gradient(i / (length - 1), UIColors.primary(), UIColors.secondary());
            text.method_10852(class_2561.method_43470(String.valueOf(message.charAt(i))).method_10862(class_2583.field_24360.method_27703(class_5251.method_27717(color.getRGB())).method_10982(Boolean.valueOf(bold))));
        }
        return text;
    }
}
