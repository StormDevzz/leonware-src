// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.other;

import lombok.Generated;
import sweetie.leonware.api.system.configs.FriendManager;
import net.minecraft.class_310;
import sweetie.leonware.client.features.modules.other.StreamerModule;
import net.minecraft.class_124;
import java.util.Iterator;
import net.minecraft.class_2583;
import net.minecraft.class_7417;
import java.util.regex.Pattern;
import net.minecraft.class_8828;
import net.minecraft.class_5250;
import net.minecraft.class_2561;

public final class ReplaceUtil
{
    public static class_2561 replace(final class_2561 input, final String target, final String replacement) {
        if (input == null || target == null || replacement == null) {
            return input;
        }
        final class_5250 result = class_2561.method_43473().method_10862(input.method_10866());
        appendReplaced(result, input, target, replacement);
        return (class_2561)result;
    }
    
    private static void appendReplaced(final class_5250 result, final class_2561 current, final String target, final String replacement) {
        final class_7417 content = current.method_10851();
        final class_2583 style = current.method_10866();
        if (content instanceof final class_8828.class_2585 literal) {
            final Pattern pattern = Pattern.compile(Pattern.quote(target), 2);
            final String replaced = pattern.matcher(literal.comp_737()).replaceAll(replacement);
            result.method_10852((class_2561)class_2561.method_43470(replaced).method_10862(style));
        }
        for (final class_2561 sibling : current.method_10855()) {
            appendReplaced(result, sibling, target, replacement);
        }
    }
    
    public static String replaceSymbols(final String string) {
        return string.replaceAll("\ua517", String.valueOf(class_124.field_1078) + "MODER").replaceAll("\ua525", String.valueOf(class_124.field_1078) + "ST.MODER").replaceAll("\ua521", String.valueOf(class_124.field_1076) + "MODER+").replaceAll("\ua500", String.valueOf(class_124.field_1080) + "PLAYER").replaceAll("\ua509", String.valueOf(class_124.field_1054) + "HELPER").replaceAll("\u25c6", "@").replaceAll("\u2503", "|").replaceAll("\ua533", String.valueOf(class_124.field_1075) + "ML.ADMIN").replaceAll("\ua505", String.valueOf(class_124.field_1061) + "Y" + String.valueOf(class_124.field_1068)).replaceAll("\ua502", String.valueOf(class_124.field_1078) + "D.MODER").replaceAll("\ua560", String.valueOf(class_124.field_1054) + "D.HELPER").replaceAll("\ua544", String.valueOf(class_124.field_1061) + "DRACULA").replaceAll("\ua516", String.valueOf(class_124.field_1075) + "OVERLORD").replaceAll("\ua548", String.valueOf(class_124.field_1060) + "COBRA").replaceAll("\ua528", String.valueOf(class_124.field_1076) + "DRAGON").replaceAll("\ua524", String.valueOf(class_124.field_1061) + "IMPERATOR").replaceAll("\ua520", String.valueOf(class_124.field_1065) + "MAGISTER").replaceAll("\ua504", String.valueOf(class_124.field_1078) + "HERO").replaceAll("\ua512", String.valueOf(class_124.field_1060) + "AVENGER").replaceAll("\ua552", String.valueOf(class_124.field_1068) + "RABBIT").replaceAll("\ua508", String.valueOf(class_124.field_1054) + "TITAN").replaceAll("\ua540", String.valueOf(class_124.field_1077) + "HYDRA").replaceAll("\ua536", String.valueOf(class_124.field_1065) + "TIGER").replaceAll("\ua532", String.valueOf(class_124.field_1064) + "BULL").replaceAll("\ua556", String.valueOf(class_124.field_1074) + "BUNNY").replaceAll("\ua557\ua558", String.valueOf(class_124.field_1054) + "SPONSOR").replaceAll("\ud83d\udd25", "@").replaceAll("\u1d00", "A").replaceAll("\u0299", "B").replaceAll("\u1d04", "C").replaceAll("\u1d05", "D").replaceAll("\u1d07", "E").replaceAll("\u0493", "F").replaceAll("\u0262", "G").replaceAll("\u029c", "H").replaceAll("\u026a", "I").replaceAll("\u1d0a", "J").replaceAll("\u1d0b", "K").replaceAll("\u029f", "L").replaceAll("\u1d0d", "M").replaceAll("\u0274", "N").replaceAll("\ua731", "S").replaceAll("\u1d0f", "O").replaceAll("\u1d18", "P").replaceAll("\u01eb", "Q").replaceAll("\u0280", "R").replaceAll("\u1d1b", "T").replaceAll("\u1d1c", "U").replaceAll("\u1d20", "V").replaceAll("\u1d21", "W").replaceAll("\ua730", "F").replaceAll("\u028f", "Y").replaceAll("\u1d22", "Z");
    }
    
    public static class_2561 replaceSymbols(class_2561 text) {
        if (text.getString().contains("\ua517")) {
            text = replace(text, "\ua517", String.valueOf(class_124.field_1078) + "MODER");
        }
        if (text.getString().contains("\ua525")) {
            text = replace(text, "\ua525", String.valueOf(class_124.field_1078) + "ST.MODER");
        }
        if (text.getString().contains("\ua521")) {
            text = replace(text, "\ua521", String.valueOf(class_124.field_1076) + "MODER+");
        }
        if (text.getString().contains("\ua500")) {
            text = replace(text, "\ua500", String.valueOf(class_124.field_1080) + "PLAYER");
        }
        if (text.getString().contains("\ua509")) {
            text = replace(text, "\ua509", String.valueOf(class_124.field_1054) + "HELPER");
        }
        if (text.getString().contains("\u25c6")) {
            text = replace(text, "\u25c6", "@");
        }
        if (text.getString().contains("\u2503")) {
            text = replace(text, "\u2503", "|");
        }
        if (text.getString().contains("\ua533")) {
            text = replace(text, "\ua533", String.valueOf(class_124.field_1075) + "ML.ADMIN");
        }
        if (text.getString().contains("\ua505")) {
            text = replace(text, "\ua505", String.valueOf(class_124.field_1061) + "Y" + String.valueOf(class_124.field_1068));
        }
        if (text.getString().contains("\ua502")) {
            text = replace(text, "\ua502", String.valueOf(class_124.field_1078) + "D.MODER");
        }
        if (text.getString().contains("\ua560")) {
            text = replace(text, "\ua560", String.valueOf(class_124.field_1054) + "D.HELPER");
        }
        if (text.getString().contains("\ua544")) {
            text = replace(text, "\ua544", String.valueOf(class_124.field_1061) + "DRACULA");
        }
        if (text.getString().contains("\ua516")) {
            text = replace(text, "\ua516", String.valueOf(class_124.field_1075) + "OVERLORD");
        }
        if (text.getString().contains("\ua548")) {
            text = replace(text, "\ua548", String.valueOf(class_124.field_1060) + "COBRA");
        }
        if (text.getString().contains("\ua528")) {
            text = replace(text, "\ua528", String.valueOf(class_124.field_1076) + "DRAGON");
        }
        if (text.getString().contains("\ua524")) {
            text = replace(text, "\ua524", String.valueOf(class_124.field_1061) + "IMPERATOR");
        }
        if (text.getString().contains("\ua520")) {
            text = replace(text, "\ua520", String.valueOf(class_124.field_1065) + "MAGISTER");
        }
        if (text.getString().contains("\ua504")) {
            text = replace(text, "\ua504", String.valueOf(class_124.field_1078) + "HERO");
        }
        if (text.getString().contains("\ua512")) {
            text = replace(text, "\ua512", String.valueOf(class_124.field_1060) + "AVENGER");
        }
        if (text.getString().contains("\ua552")) {
            text = replace(text, "\ua552", String.valueOf(class_124.field_1068) + "RABBIT");
        }
        if (text.getString().contains("\ua508")) {
            text = replace(text, "\ua508", String.valueOf(class_124.field_1054) + "TITAN");
        }
        if (text.getString().contains("\ua540")) {
            text = replace(text, "\ua540", String.valueOf(class_124.field_1077) + "HYDRA");
        }
        if (text.getString().contains("\ua536")) {
            text = replace(text, "\ua536", String.valueOf(class_124.field_1065) + "TIGER");
        }
        if (text.getString().contains("\ua532")) {
            text = replace(text, "\ua532", String.valueOf(class_124.field_1064) + "BULL");
        }
        if (text.getString().contains("\ua556")) {
            text = replace(text, "\ua556", String.valueOf(class_124.field_1074) + "BUNNY");
        }
        if (text.getString().contains("\ua557\ua558")) {
            text = replace(text, "\ua557\ua558", String.valueOf(class_124.field_1054) + "SPONSOR");
        }
        if (text.getString().contains("\ud83d\udd25")) {
            text = replace(text, "\ud83d\udd25", "@");
        }
        if (text.getString().contains("\u1d00")) {
            text = replace(text, "\u1d00", "A");
        }
        if (text.getString().contains("\u0299")) {
            text = replace(text, "\u0299", "B");
        }
        if (text.getString().contains("\u1d04")) {
            text = replace(text, "\u1d04", "C");
        }
        if (text.getString().contains("\u1d05")) {
            text = replace(text, "\u1d05", "D");
        }
        if (text.getString().contains("\u1d07")) {
            text = replace(text, "\u1d07", "E");
        }
        if (text.getString().contains("\u0493")) {
            text = replace(text, "\u0493", "F");
        }
        if (text.getString().contains("\u0262")) {
            text = replace(text, "\u0262", "G");
        }
        if (text.getString().contains("\u029c")) {
            text = replace(text, "\u029c", "H");
        }
        if (text.getString().contains("\u026a")) {
            text = replace(text, "\u026a", "I");
        }
        if (text.getString().contains("\u1d0a")) {
            text = replace(text, "\u1d0a", "J");
        }
        if (text.getString().contains("\u1d0b")) {
            text = replace(text, "\u1d0b", "K");
        }
        if (text.getString().contains("\u029f")) {
            text = replace(text, "\u029f", "L");
        }
        if (text.getString().contains("\u1d0d")) {
            text = replace(text, "\u1d0d", "M");
        }
        if (text.getString().contains("\u0274")) {
            text = replace(text, "\u0274", "N");
        }
        if (text.getString().contains("\ua731")) {
            text = replace(text, "\ua731", "S");
        }
        if (text.getString().contains("\u1d0f")) {
            text = replace(text, "\u1d0f", "O");
        }
        if (text.getString().contains("\u1d18")) {
            text = replace(text, "\u1d18", "P");
        }
        if (text.getString().contains("\u01eb")) {
            text = replace(text, "\u01eb", "Q");
        }
        if (text.getString().contains("\u0280")) {
            text = replace(text, "\u0280", "R");
        }
        if (text.getString().contains("\u1d1b")) {
            text = replace(text, "\u1d1b", "T");
        }
        if (text.getString().contains("\u1d1c")) {
            text = replace(text, "\u1d1c", "U");
        }
        if (text.getString().contains("\u1d20")) {
            text = replace(text, "\u1d20", "V");
        }
        if (text.getString().contains("\u1d21")) {
            text = replace(text, "\u1d21", "W");
        }
        if (text.getString().contains("\ua730")) {
            text = replace(text, "\ua730", "F");
        }
        if (text.getString().contains("\u028f")) {
            text = replace(text, "\u028f", "Y");
        }
        if (text.getString().contains("\u1d22")) {
            text = replace(text, "\u1d22", "Z");
        }
        return text;
    }
    
    public static String protectedString(final String text) {
        final StreamerModule streamerMode = StreamerModule.getInstance();
        String finalText = text;
        if (streamerMode.isEnabled()) {
            if (streamerMode.getHide().isEnabled("Name")) {
                finalText = finalText.replace(class_310.method_1551().method_1548().method_1676(), streamerMode.getProtectedName());
                if (streamerMode.getHide().isEnabled("Hide friends")) {
                    for (final String friendName : FriendManager.getInstance().getData()) {
                        finalText = finalText.replace(friendName, streamerMode.getProtectedFriendName(friendName));
                    }
                }
            }
            if (streamerMode.getHide().isEnabled("No Fun Time")) {
                final String t = finalText.toLowerCase();
                final boolean hasFunTime = (t.contains("fun") || t.contains("time")) && !t.contains("r");
                final boolean isModeName = t.contains("18032025") || t.contains("01052026") || t.contains("10052026");
                if (hasFunTime && !isModeName) {
                    finalText = finalText.replaceAll("(?i)fun", "Von").replaceAll("(?i)time", "Tam");
                }
                if (!isModeName && (t.contains("\u0444\u0430\u0439\u043c") || (t.contains("\u0442\u0430\u0439\u043c") && !t.contains("\u0440")))) {
                    finalText = finalText.replaceAll("(?i)fun", "\u0412\u043e\u043d").replaceAll("(?i)time", "\u0422\u0430\u043c");
                }
            }
        }
        return finalText;
    }
    
    @Generated
    private ReplaceUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
