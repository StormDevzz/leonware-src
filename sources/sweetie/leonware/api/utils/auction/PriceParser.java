package sweetie.leonware.api.utils.auction;

import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1836;
import net.minecraft.class_2561;
import org.newsclub.net.unix.AFVSOCKSocketAddress;
import sweetie.leonware.api.system.interfaces.QuickImports;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/auction/PriceParser.class */
public class PriceParser implements QuickImports {
    public ParseModeChoice currentMode = ParseModeChoice.FUN_TIME;

    public int getPrice(class_1799 stack) {
        for (class_2561 text : stack.method_7950(class_1792.class_9635.field_51353, mc.field_1724, class_1836.field_41070)) {
            String str = text.getString().replace("§r", "").replace("¤", "").trim();
            if (this.currentMode == ParseModeChoice.ARES_MINE) {
                if (str.contains("Цена в долларах:")) {
                    String cleaned = str.replaceAll("Цена в долларах:", "").replaceAll("[▯⌷⎕\\s]", "").replaceAll("[^0-9]", "").trim();
                    try {
                        return Integer.parseInt(cleaned);
                    } catch (NumberFormatException e) {
                    }
                } else {
                    continue;
                }
            } else {
                String textPrice = getStr();
                if (str.startsWith(textPrice)) {
                    try {
                        return Integer.parseInt(str.replace(textPrice, "").replace(",", "").replace(".", "").replace(" ", "").trim());
                    } catch (NumberFormatException e2) {
                    }
                } else {
                    continue;
                }
            }
        }
        return -1;
    }

    /* JADX INFO: renamed from: sweetie.leonware.api.utils.auction.PriceParser$1, reason: invalid class name */
    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/auction/PriceParser$1.class */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$sweetie$leonware$api$utils$auction$ParseModeChoice = new int[ParseModeChoice.values().length];

        static {
            try {
                $SwitchMap$sweetie$leonware$api$utils$auction$ParseModeChoice[ParseModeChoice.FUN_TIME.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$sweetie$leonware$api$utils$auction$ParseModeChoice[ParseModeChoice.SPOOKY_TIME.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$sweetie$leonware$api$utils$auction$ParseModeChoice[ParseModeChoice.HOLY_WORLD.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$sweetie$leonware$api$utils$auction$ParseModeChoice[ParseModeChoice.REALLY_WORLD.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    private String getStr() {
        switch (AnonymousClass1.$SwitchMap$sweetie$leonware$api$utils$auction$ParseModeChoice[this.currentMode.ordinal()]) {
            case 1:
                return "$ Ценa $";
            case AFVSOCKSocketAddress.VMADDR_CID_HOST /* 2 */:
                return "$ Цена: $";
            case 3:
                return "▍ Цена за 1 ед.:";
            case 4:
                return "Цена:";
            default:
                return "";
        }
    }
}
