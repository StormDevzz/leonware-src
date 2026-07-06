/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_304
 *  net.minecraft.class_3675
 */
package sweetie.leonware.api.utils.other;

import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_304;
import net.minecraft.class_3675;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.player.MoveUtil;
import sweetie.leonware.client.features.modules.movement.InventoryMoveModule;

public final class SlownessManager
implements QuickImports {
    private static final List<Entry> entries = new ArrayList<Entry>(4);
    public static boolean slowed = false;

    public static boolean isEnabled() {
        InventoryMoveModule invMove = InventoryMoveModule.getInstance();
        return invMove.isLegit() || invMove.isAresMine();
    }

    public static void applySlowness(long totalMs, long delayMs, Runnable action) {
        long now = System.nanoTime();
        long execNs = now + delayMs * 1000000L;
        long endNs = now + totalMs * 1000000L;
        entries.add(new Entry(execNs, endNs, action));
        if (!slowed) {
            for (class_304 key : MoveUtil.getMovementKeys()) {
                key.method_23481(false);
            }
            slowed = true;
        }
    }

    public static void applySlowness(long totalMs, Runnable action) {
        SlownessManager.applySlowness(totalMs, 1L, action);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void tick() {
        if (!slowed && entries.isEmpty()) {
            return;
        }
        long now = System.nanoTime();
        ArrayList<Entry> toExecute = null;
        for (Entry e : entries) {
            if (e.executed || now < e.execNs) continue;
            if (toExecute == null) {
                toExecute = new ArrayList<Entry>(4);
            }
            toExecute.add(e);
        }
        if (toExecute != null) {
            for (Entry e : toExecute) {
                try {
                    if (e.action == null) continue;
                    e.action.run();
                }
                catch (Throwable t) {
                    t.printStackTrace();
                }
                finally {
                    e.executed = true;
                }
            }
        }
        for (int i = entries.size() - 1; i >= 0; --i) {
            Entry e;
            e = entries.get(i);
            if (!e.executed || now < e.endNs) continue;
            entries.remove(i);
        }
        if (entries.isEmpty() && slowed) {
            SlownessManager.resetKeys();
        }
    }

    private static void resetKeys() {
        if (SlownessManager.mc.field_1724 == null || SlownessManager.mc.field_1687 == null) {
            entries.clear();
            slowed = false;
            return;
        }
        long handle = mc.method_22683().method_4490();
        for (class_304 key : MoveUtil.getMovementKeys()) {
            key.method_23481(class_3675.method_15987((long)handle, (int)key.method_1429().method_1444()));
        }
        slowed = false;
    }

    @Generated
    private SlownessManager() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    private static final class Entry {
        final long execNs;
        final long endNs;
        final Runnable action;
        boolean executed = false;

        Entry(long execNs, long endNs, Runnable action) {
            this.execNs = execNs;
            this.endNs = endNs;
            this.action = action;
        }
    }
}

