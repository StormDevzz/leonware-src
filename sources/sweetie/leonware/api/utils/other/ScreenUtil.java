package sweetie.leonware.api.utils.other;

import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_1707;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_2561;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import net.minecraft.class_465;
import net.minecraft.class_490;
import sweetie.leonware.api.event.events.other.ScreenEvent;
import sweetie.leonware.api.system.backend.Pair;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.player.InventoryUtil;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/other/ScreenUtil.class */
public final class ScreenUtil implements QuickImports {
    @Generated
    private ScreenUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void drawButton(ScreenEvent.ScreenEventData event) {
        class_490 class_490VarScreen = event.screen();
        if (class_490VarScreen instanceof class_490) {
            class_490 inv = class_490VarScreen;
            int x = (((class_437) class_490VarScreen).field_22789 - 80) / 2;
            int y = (((((class_437) class_490VarScreen).field_22790 - inv.field_2779) / 2) - 20) - 5;
            addButtons(event, x, y, 80, 20, new Pair("Drop all", () -> {
                drop(inv);
            }));
            return;
        }
        if (class_490VarScreen instanceof class_465) {
            class_465<?> handledScreen = (class_465) class_490VarScreen;
            class_1707 class_1707VarMethod_17577 = handledScreen.method_17577();
            if (class_1707VarMethod_17577 instanceof class_1707) {
                class_1707 genericHandler = class_1707VarMethod_17577;
                String title = handledScreen.method_25440().getString();
                if (title.contains("Аукцион") && title.contains("Поиск")) {
                    return;
                }
                int rows = genericHandler.method_17388();
                int backgroundHeight = 114 + (rows * 18);
                int buttonX = (((class_437) class_490VarScreen).field_22789 / 2) + 90;
                int buttonY = (((class_437) class_490VarScreen).field_22790 - backgroundHeight) / 2;
                addButtons(event, buttonX, buttonY, 80, 20, new Pair("Steal all", () -> {
                    toInv(genericHandler);
                }), new Pair("From inv", () -> {
                    fromInv(genericHandler);
                }));
            }
        }
    }

    @SafeVarargs
    private static void addButtons(ScreenEvent.ScreenEventData event, int x, int y, int width, int height, Pair<String, Runnable>... pairs) {
        List<class_4185> buttons = new ArrayList<>();
        int offsetY = y;
        for (Pair<String, Runnable> pair : pairs) {
            class_4185 button = class_4185.method_46430(class_2561.method_43470(pair.left()), b -> {
                ((Runnable) pair.right()).run();
            }).method_46434(x, offsetY, width, height).method_46431();
            buttons.add(button);
            offsetY += height + 5;
        }
        event.buttons().addAll(buttons);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void toInv(class_1707 containerHandler) {
        for (int i = 0; i < containerHandler.method_7629().method_5439(); i++) {
            class_1735 slot = containerHandler.method_7611(i);
            if (slot.method_7681()) {
                mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, i, 0, class_1713.field_7794, mc.field_1724);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void fromInv(class_1707 containerHandler) {
        for (int i = containerHandler.method_7629().method_5439(); i < containerHandler.field_7761.size(); i++) {
            class_1735 slot = containerHandler.method_7611(i);
            if (slot.method_7681()) {
                mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, i, 0, class_1713.field_7794, mc.field_1724);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void drop(class_465<?> screen) {
        for (class_1735 slot : screen.method_17577().field_7761) {
            if (!slot.method_7677().method_7960()) {
                InventoryUtil.dropSlot(slot.field_7874);
            }
        }
    }
}
