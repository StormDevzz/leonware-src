// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.other;

import lombok.Generated;
import java.util.Iterator;
import sweetie.leonware.api.utils.player.InventoryUtil;
import net.minecraft.class_1735;
import net.minecraft.class_1657;
import net.minecraft.class_1713;
import java.util.List;
import java.util.Collection;
import net.minecraft.class_2561;
import net.minecraft.class_4185;
import java.util.ArrayList;
import net.minecraft.class_1703;
import net.minecraft.class_437;
import net.minecraft.class_1707;
import net.minecraft.class_465;
import sweetie.leonware.api.system.backend.Pair;
import net.minecraft.class_490;
import sweetie.leonware.api.event.events.other.ScreenEvent;
import sweetie.leonware.api.system.interfaces.QuickImports;

public final class ScreenUtil implements QuickImports
{
    public static void drawButton(final ScreenEvent.ScreenEventData event) {
        final int buttonWidth = 80;
        final int buttonHeight = 20;
        final class_437 screen = event.screen();
        if (screen instanceof final class_490 inv) {
            final int x = (screen.field_22789 - buttonWidth) / 2;
            final int y = (screen.field_22790 - inv.field_2779) / 2 - buttonHeight - 5;
            addButtons(event, x, y, buttonWidth, buttonHeight, new Pair((A)"Drop all", () -> drop((class_465<?>)inv)));
        }
        else if (screen instanceof class_465) {
            final class_465<?> handledScreen = (class_465<?>)screen;
            final class_1703 method_17577 = handledScreen.method_17577();
            if (method_17577 instanceof final class_1707 genericHandler) {
                final String title = handledScreen.method_25440().getString();
                if (title.contains("\u0410\u0443\u043a\u0446\u0438\u043e\u043d") && title.contains("\u041f\u043e\u0438\u0441\u043a")) {
                    return;
                }
                final int rows = genericHandler.method_17388();
                final int backgroundHeight = 114 + rows * 18;
                final int buttonX = screen.field_22789 / 2 + 90;
                final int buttonY = (screen.field_22790 - backgroundHeight) / 2;
                addButtons(event, buttonX, buttonY, buttonWidth, buttonHeight, new Pair((A)"Steal all", () -> toInv(genericHandler)), new Pair((A)"From inv", () -> fromInv(genericHandler)));
            }
        }
    }
    
    @SafeVarargs
    private static void addButtons(final ScreenEvent.ScreenEventData event, final int x, final int y, final int width, final int height, final Pair<String, Runnable>... pairs) {
        final List<class_4185> buttons = new ArrayList<class_4185>();
        int offsetY = y;
        for (final Pair<String, Runnable> pair : pairs) {
            final class_4185 button = class_4185.method_46430((class_2561)class_2561.method_43470((String)pair.left()), b -> pair.right().run()).method_46434(x, offsetY, width, height).method_46431();
            buttons.add(button);
            offsetY += height + 5;
        }
        event.buttons().addAll(buttons);
    }
    
    private static void toInv(final class_1707 containerHandler) {
        for (int i = 0; i < containerHandler.method_7629().method_5439(); ++i) {
            final class_1735 slot = containerHandler.method_7611(i);
            if (slot.method_7681()) {
                ScreenUtil.mc.field_1761.method_2906(ScreenUtil.mc.field_1724.field_7512.field_7763, i, 0, class_1713.field_7794, (class_1657)ScreenUtil.mc.field_1724);
            }
        }
    }
    
    private static void fromInv(final class_1707 containerHandler) {
        for (int i = containerHandler.method_7629().method_5439(); i < containerHandler.field_7761.size(); ++i) {
            final class_1735 slot = containerHandler.method_7611(i);
            if (slot.method_7681()) {
                ScreenUtil.mc.field_1761.method_2906(ScreenUtil.mc.field_1724.field_7512.field_7763, i, 0, class_1713.field_7794, (class_1657)ScreenUtil.mc.field_1724);
            }
        }
    }
    
    private static void drop(final class_465<?> screen) {
        for (final class_1735 slot : screen.method_17577().field_7761) {
            if (slot.method_7677().method_7960()) {
                continue;
            }
            InventoryUtil.dropSlot(slot.field_7874);
        }
    }
    
    @Generated
    private ScreenUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
