/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1657
 *  net.minecraft.class_1792$class_9635
 *  net.minecraft.class_1799
 *  net.minecraft.class_1836
 *  net.minecraft.class_2561
 */
package sweetie.leonware.api.utils.auction;

import net.minecraft.class_1657;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1836;
import net.minecraft.class_2561;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.auction.ParseModeChoice;

public class PriceParser
implements QuickImports {
    public ParseModeChoice currentMode = ParseModeChoice.FUN_TIME;

    public int getPrice(class_1799 stack) {
        for (class_2561 text : stack.method_7950(class_1792.class_9635.field_51353, (class_1657)PriceParser.mc.field_1724, (class_1836)class_1836.field_41070)) {
            String str = text.getString().replace("\u00a7r", "").replace("\u00a4", "").trim();
            if (this.currentMode == ParseModeChoice.ARES_MINE) {
                if (!str.contains("\u0426\u0435\u043d\u0430 \u0432 \u0434\u043e\u043b\u043b\u0430\u0440\u0430\u0445:")) continue;
                String cleaned = str.replaceAll("\u0426\u0435\u043d\u0430 \u0432 \u0434\u043e\u043b\u043b\u0430\u0440\u0430\u0445:", "").replaceAll("[\u25af\u2337\u2395\\s]", "").replaceAll("[^0-9]", "").trim();
                try {
                    return Integer.parseInt(cleaned);
                }
                catch (NumberFormatException numberFormatException) {
                    continue;
                }
            }
            String textPrice = this.getStr();
            if (!str.startsWith(textPrice)) continue;
            try {
                return Integer.parseInt(str.replace(textPrice, "").replace(",", "").replace(".", "").replace(" ", "").trim());
            }
            catch (NumberFormatException numberFormatException) {
            }
        }
        return -1;
    }

    private String getStr() {
        return switch (this.currentMode) {
            case ParseModeChoice.FUN_TIME -> "$ \u0426\u0435\u043da $";
            case ParseModeChoice.SPOOKY_TIME -> "$ \u0426\u0435\u043d\u0430: $";
            case ParseModeChoice.HOLY_WORLD -> "\u258d \u0426\u0435\u043d\u0430 \u0437\u0430 1 \u0435\u0434.:";
            case ParseModeChoice.REALLY_WORLD -> "\u0426\u0435\u043d\u0430:";
            default -> "";
        };
    }
}

