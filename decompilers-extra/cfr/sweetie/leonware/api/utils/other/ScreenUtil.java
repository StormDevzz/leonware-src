/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1657
 *  net.minecraft.class_1703
 *  net.minecraft.class_1707
 *  net.minecraft.class_1713
 *  net.minecraft.class_1735
 *  net.minecraft.class_2561
 *  net.minecraft.class_4185
 *  net.minecraft.class_437
 *  net.minecraft.class_465
 *  net.minecraft.class_490
 */
package sweetie.leonware.api.utils.other;

import java.util.ArrayList;
import lombok.Generated;
import net.minecraft.class_1657;
import net.minecraft.class_1703;
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

public final class ScreenUtil
implements QuickImports {
    public static void drawButton(ScreenEvent.ScreenEventData event) {
        class_465 handledScreen;
        class_1703 x2;
        int buttonWidth = 80;
        int buttonHeight = 20;
        class_437 screen = event.screen();
        if (screen instanceof class_490) {
            class_490 inv = (class_490)screen;
            int x2 = (screen.field_22789 - buttonWidth) / 2;
            int y = (screen.field_22790 - inv.field_2779) / 2 - buttonHeight - 5;
            ScreenUtil.addButtons(event, x2, y, buttonWidth, buttonHeight, new Pair<String, Runnable>("Drop all", () -> ScreenUtil.drop(inv)));
        } else if (screen instanceof class_465 && (x2 = (handledScreen = (class_465)screen).method_17577()) instanceof class_1707) {
            class_1707 genericHandler = (class_1707)x2;
            String title = handledScreen.method_25440().getString();
            if (title.contains("\u0410\u0443\u043a\u0446\u0438\u043e\u043d") && title.contains("\u041f\u043e\u0438\u0441\u043a")) {
                return;
            }
            int rows = genericHandler.method_17388();
            int backgroundHeight = 114 + rows * 18;
            int buttonX = screen.field_22789 / 2 + 90;
            int buttonY = (screen.field_22790 - backgroundHeight) / 2;
            ScreenUtil.addButtons(event, buttonX, buttonY, buttonWidth, buttonHeight, new Pair<String, Runnable>("Steal all", () -> ScreenUtil.toInv(genericHandler)), new Pair<String, Runnable>("From inv", () -> ScreenUtil.fromInv(genericHandler)));
        }
    }

    @SafeVarargs
    private static void addButtons(ScreenEvent.ScreenEventData event, int x, int y, int width, int height, Pair<String, Runnable> ... pairs) {
        ArrayList<class_4185> buttons = new ArrayList<class_4185>();
        int offsetY = y;
        for (Pair<String, Runnable> pair : pairs) {
            class_4185 button = class_4185.method_46430((class_2561)class_2561.method_43470((String)pair.left()), b -> ((Runnable)pair.right()).run()).method_46434(x, offsetY, width, height).method_46431();
            buttons.add(button);
            offsetY += height + 5;
        }
        event.buttons().addAll(buttons);
    }

    private static void toInv(class_1707 containerHandler) {
        for (int i = 0; i < containerHandler.method_7629().method_5439(); ++i) {
            class_1735 slot = containerHandler.method_7611(i);
            if (!slot.method_7681()) continue;
            ScreenUtil.mc.field_1761.method_2906(ScreenUtil.mc.field_1724.field_7512.field_7763, i, 0, class_1713.field_7794, (class_1657)ScreenUtil.mc.field_1724);
        }
    }

    private static void fromInv(class_1707 containerHandler) {
        for (int i = containerHandler.method_7629().method_5439(); i < containerHandler.field_7761.size(); ++i) {
            class_1735 slot = containerHandler.method_7611(i);
            if (!slot.method_7681()) continue;
            ScreenUtil.mc.field_1761.method_2906(ScreenUtil.mc.field_1724.field_7512.field_7763, i, 0, class_1713.field_7794, (class_1657)ScreenUtil.mc.field_1724);
        }
    }

    private static void drop(class_465<?> screen) {
        for (class_1735 slot : screen.method_17577().field_7761) {
            if (slot.method_7677().method_7960()) continue;
            InventoryUtil.dropSlot(slot.field_7874);
        }
    }

    @Generated
    private ScreenUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

