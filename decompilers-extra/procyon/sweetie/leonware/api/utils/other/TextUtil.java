// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.other;

import lombok.Generated;
import java.awt.Color;
import net.minecraft.class_5251;
import sweetie.leonware.api.utils.color.ColorUtil;
import sweetie.leonware.api.utils.color.UIColors;
import net.minecraft.class_5250;
import net.minecraft.class_124;
import java.util.Iterator;
import net.minecraft.class_7417;
import net.minecraft.class_2583;
import net.minecraft.class_2588;
import net.minecraft.class_2572;
import net.minecraft.class_8828;
import java.util.ArrayList;
import sweetie.leonware.api.utils.render.fonts.MsdfGlyph;
import java.util.List;
import net.minecraft.class_2561;
import sweetie.leonware.api.system.interfaces.QuickImports;

public final class TextUtil implements QuickImports
{
    public static List<MsdfGlyph.ColoredGlyph> parseTextToColoredGlyphs(final class_2561 text) {
        final List<MsdfGlyph.ColoredGlyph> result = new ArrayList<MsdfGlyph.ColoredGlyph>();
        parseTextRecursive(text, -1, result);
        return result;
    }
    
    private static void parseTextRecursive(final class_2561 text, final int currentColor, final List<MsdfGlyph.ColoredGlyph> result) {
        final class_2583 style = text.method_10866();
        final int color = (style.method_10973() != null) ? (style.method_10973().method_27716() | 0xFF000000) : currentColor;
        final class_7417 content = text.method_10851();
        String raw = "";
        while (true) {
            if (content instanceof final class_8828.class_2585 class_2585) {
                try {
                    final String string = raw = class_2585.comp_737();
                    while (true) {
                        raw = ReplaceUtil.replaceSymbols(raw);
                        for (int i = 0; i < raw.length(); ++i) {
                            final char c = raw.charAt(i);
                            if ((c == '\ua517' || c == '\ua525') && i + 1 < raw.length()) {
                                ++i;
                            }
                            else {
                                result.add(new MsdfGlyph.ColoredGlyph(c, color));
                            }
                        }
                        for (final class_2561 sibling : text.method_10855()) {
                            parseTextRecursive(sibling, color, result);
                        }
                        return;
                        Label_0100: {
                            iftrue(Label_0122:)(!(content instanceof class_2572));
                        }
                        Block_6: {
                            break Block_6;
                            iftrue(Label_0100:)(!(content instanceof class_2588));
                            final class_2588 translatable = (class_2588)content;
                            raw = translatable.method_11022();
                            continue;
                        }
                        final class_2572 keybind = (class_2572)content;
                        raw = keybind.method_10901();
                        continue;
                    }
                }
                catch (final Throwable cause) {
                    throw new MatchException(cause.toString(), cause);
                }
                return;
            }
            continue;
        }
    }
    
    public static List<MsdfGlyph.ColoredGlyph> parseMinecraftFormattedString(final String text) {
        final List<MsdfGlyph.ColoredGlyph> glyphs = new ArrayList<MsdfGlyph.ColoredGlyph>();
        int currentColor = -1;
        for (int i = 0; i < text.length(); ++i) {
            final char c = text.charAt(i);
            if (c == '§' && i + 1 < text.length()) {
                final char code = text.charAt(i + 1);
                final Integer color = getColorFromCode(code);
                if (color != null) {
                    currentColor = (color | 0xFF000000);
                }
                ++i;
            }
            else {
                glyphs.add(new MsdfGlyph.ColoredGlyph(c, currentColor));
            }
        }
        return glyphs;
    }
    
    private static Integer getColorFromCode(final char code) {
        return switch (Character.toLowerCase(code)) {
            case '0' -> 0;
            case '1' -> 170;
            case '2' -> 43520;
            case '3' -> 43690;
            case '4' -> 11141120;
            case '5' -> 11141290;
            case '6' -> 16755200;
            case '7' -> 11184810;
            case '8' -> 5592405;
            case '9' -> 5592575;
            case 'a' -> 5635925;
            case 'b' -> 5636095;
            case 'c' -> 16733525;
            case 'd' -> 16733695;
            case 'e' -> 16777045;
            case 'f' -> 16777215;
            default -> null;
        };
    }
    
    public static String stripFormatting(final String text) {
        if (text == null) {
            return "";
        }
        return text.replaceAll("§.", "");
    }
    
    public static boolean hasFormatting(final String text) {
        return text != null && text.contains("§");
    }
    
    public static void sendMessage(final String message) {
        TextUtil.mc.field_1724.method_7353((class_2561)class_2561.method_43470("").method_10852((class_2561)gradient("LeonWare", true)).method_10852((class_2561)class_2561.method_43470(String.valueOf(class_124.field_1080) + " >> " + String.valueOf(class_124.field_1070) + message)), false);
    }
    
    public static String getDurationText(final int ticks) {
        if (ticks == -1) {
            return "**:**";
        }
        final int seconds = ticks / 20;
        int minutes = seconds / 60;
        final int hours = minutes / 60;
        minutes %= 60;
        final int remainingSeconds = seconds % 60;
        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, remainingSeconds);
        }
        if (minutes > 0) {
            return String.format("%d:%02d", minutes, remainingSeconds);
        }
        return String.format("%ds", seconds);
    }
    
    public static class_5250 gradient(final String message, final boolean bold) {
        final class_5250 text = class_2561.method_43473();
        for (int length = message.length(), i = 0; i < length; ++i) {
            final Color color = ColorUtil.gradient(i / (float)(length - 1), UIColors.primary(), UIColors.secondary());
            text.method_10852((class_2561)class_2561.method_43470(String.valueOf(message.charAt(i))).method_10862(class_2583.field_24360.method_27703(class_5251.method_27717(color.getRGB())).method_10982(Boolean.valueOf(bold))));
        }
        return text;
    }
    
    @Generated
    private TextUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
