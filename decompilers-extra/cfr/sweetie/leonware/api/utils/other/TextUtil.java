/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_124
 *  net.minecraft.class_2561
 *  net.minecraft.class_2572
 *  net.minecraft.class_2583
 *  net.minecraft.class_2588
 *  net.minecraft.class_5250
 *  net.minecraft.class_5251
 *  net.minecraft.class_7417
 *  net.minecraft.class_8828$class_2585
 */
package sweetie.leonware.api.utils.other;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_124;
import net.minecraft.class_2561;
import net.minecraft.class_2572;
import net.minecraft.class_2583;
import net.minecraft.class_2588;
import net.minecraft.class_5250;
import net.minecraft.class_5251;
import net.minecraft.class_7417;
import net.minecraft.class_8828;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.color.ColorUtil;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.other.ReplaceUtil;
import sweetie.leonware.api.utils.render.fonts.MsdfGlyph;

public final class TextUtil
implements QuickImports {
    public static List<MsdfGlyph.ColoredGlyph> parseTextToColoredGlyphs(class_2561 text) {
        ArrayList<MsdfGlyph.ColoredGlyph> result = new ArrayList<MsdfGlyph.ColoredGlyph>();
        TextUtil.parseTextRecursive(text, -1, result);
        return result;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static void parseTextRecursive(class_2561 text, int currentColor, List<MsdfGlyph.ColoredGlyph> result) {
        class_2583 style = text.method_10866();
        int color = style.method_10973() != null ? style.method_10973().method_27716() | 0xFF000000 : currentColor;
        class_7417 content = text.method_10851();
        String raw = "";
        if (content instanceof class_8828.class_2585) {
            class_8828.class_2585 class_25852 = (class_8828.class_2585)content;
            try {
                String string;
                String string2;
                raw = string2 = (string = class_25852.comp_737());
            }
            catch (Throwable throwable) {
                throw new MatchException(throwable.toString(), throwable);
            }
        } else if (content instanceof class_2588) {
            class_2588 translatable = (class_2588)content;
            raw = translatable.method_11022();
        } else if (content instanceof class_2572) {
            class_2572 keybind = (class_2572)content;
            raw = keybind.method_10901();
        }
        raw = ReplaceUtil.replaceSymbols(raw);
        for (int i = 0; i < raw.length(); ++i) {
            char c = raw.charAt(i);
            if ((c == '\ua517' || c == '\ua525') && i + 1 < raw.length()) {
                ++i;
                continue;
            }
            result.add(new MsdfGlyph.ColoredGlyph(c, color));
        }
        Iterator iterator = text.method_10855().iterator();
        while (iterator.hasNext()) {
            class_2561 sibling = (class_2561)iterator.next();
            TextUtil.parseTextRecursive(sibling, color, result);
        }
        return;
    }

    public static List<MsdfGlyph.ColoredGlyph> parseMinecraftFormattedString(String text) {
        ArrayList<MsdfGlyph.ColoredGlyph> glyphs = new ArrayList<MsdfGlyph.ColoredGlyph>();
        int currentColor = -1;
        for (int i = 0; i < text.length(); ++i) {
            char c = text.charAt(i);
            if (c == '\u00a7' && i + 1 < text.length()) {
                char code = text.charAt(i + 1);
                Integer color = TextUtil.getColorFromCode(code);
                if (color != null) {
                    currentColor = color | 0xFF000000;
                }
                ++i;
                continue;
            }
            glyphs.add(new MsdfGlyph.ColoredGlyph(c, currentColor));
        }
        return glyphs;
    }

    private static Integer getColorFromCode(char code) {
        return switch (Character.toLowerCase(code)) {
            case '0' -> 0;
            case '1' -> 170;
            case '2' -> 43520;
            case '3' -> 43690;
            case '4' -> 0xAA0000;
            case '5' -> 0xAA00AA;
            case '6' -> 0xFFAA00;
            case '7' -> 0xAAAAAA;
            case '8' -> 0x555555;
            case '9' -> 0x5555FF;
            case 'a' -> 0x55FF55;
            case 'b' -> 0x55FFFF;
            case 'c' -> 0xFF5555;
            case 'd' -> 0xFF55FF;
            case 'e' -> 0xFFFF55;
            case 'f' -> 0xFFFFFF;
            default -> null;
        };
    }

    public static String stripFormatting(String text) {
        if (text == null) {
            return "";
        }
        return text.replaceAll("\u00a7.", "");
    }

    public static boolean hasFormatting(String text) {
        return text != null && text.contains("\u00a7");
    }

    public static void sendMessage(String message) {
        TextUtil.mc.field_1724.method_7353((class_2561)class_2561.method_43470((String)"").method_10852((class_2561)TextUtil.gradient("LeonWare", true)).method_10852((class_2561)class_2561.method_43470((String)(String.valueOf(class_124.field_1080) + " >> " + String.valueOf(class_124.field_1070) + message))), false);
    }

    public static String getDurationText(int ticks) {
        if (ticks == -1) {
            return "**:**";
        }
        int seconds = ticks / 20;
        int minutes = seconds / 60;
        int hours = minutes / 60;
        minutes %= 60;
        int remainingSeconds = seconds % 60;
        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, remainingSeconds);
        }
        if (minutes > 0) {
            return String.format("%d:%02d", minutes, remainingSeconds);
        }
        return String.format("%ds", seconds);
    }

    public static class_5250 gradient(String message, boolean bold) {
        class_5250 text = class_2561.method_43473();
        int length = message.length();
        for (int i = 0; i < length; ++i) {
            Color color = ColorUtil.gradient((float)i / (float)(length - 1), UIColors.primary(), UIColors.secondary());
            text.method_10852((class_2561)class_2561.method_43470((String)String.valueOf(message.charAt(i))).method_10862(class_2583.field_24360.method_27703(class_5251.method_27717((int)color.getRGB())).method_10982(Boolean.valueOf(bold))));
        }
        return text;
    }

    @Generated
    private TextUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

