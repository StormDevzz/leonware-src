package sweetie.leonware.api.utils.other;

import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_304;
import net.minecraft.class_3675;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.player.MoveUtil;
import sweetie.leonware.client.features.modules.movement.InventoryMoveModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/other/SlownessManager.class */
public final class SlownessManager implements QuickImports {
    private static final List<Entry> entries = new ArrayList(4);
    public static boolean slowed = false;

    @Generated
    private SlownessManager() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static boolean isEnabled() {
        InventoryMoveModule invMove = InventoryMoveModule.getInstance();
        return invMove.isLegit() || invMove.isAresMine();
    }

    public static void applySlowness(long totalMs, long delayMs, Runnable action) {
        long now = System.nanoTime();
        long execNs = now + (delayMs * 1000000);
        long endNs = now + (totalMs * 1000000);
        entries.add(new Entry(execNs, endNs, action));
        if (!slowed) {
            for (class_304 key : MoveUtil.getMovementKeys()) {
                key.method_23481(false);
            }
            slowed = true;
        }
    }

    public static void applySlowness(long totalMs, Runnable action) {
        applySlowness(totalMs, 1L, action);
    }

    public static void tick() {
        if (slowed || !entries.isEmpty()) {
            long now = System.nanoTime();
            List<Entry> toExecute = null;
            for (Entry e : entries) {
                if (!e.executed && now >= e.execNs) {
                    if (toExecute == null) {
                        toExecute = new ArrayList<>(4);
                    }
                    toExecute.add(e);
                }
            }
            if (toExecute != null) {
                for (Entry e2 : toExecute) {
                    try {
                        try {
                            if (e2.action != null) {
                                e2.action.run();
                            }
                            e2.executed = true;
                        } catch (Throwable t) {
                            t.printStackTrace();
                            e2.executed = true;
                        }
                    } catch (Throwable th) {
                        e2.executed = true;
                        throw th;
                    }
                }
            }
            for (int i = entries.size() - 1; i >= 0; i--) {
                Entry e3 = entries.get(i);
                if (e3.executed && now >= e3.endNs) {
                    entries.remove(i);
                }
            }
            if (entries.isEmpty() && slowed) {
                resetKeys();
            }
        }
    }

    private static void resetKeys() {
        if (mc.field_1724 == null || mc.field_1687 == null) {
            entries.clear();
            slowed = false;
            return;
        }
        long handle = mc.method_22683().method_4490();
        for (class_304 key : MoveUtil.getMovementKeys()) {
            key.method_23481(class_3675.method_15987(handle, key.method_1429().method_1444()));
        }
        slowed = false;
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/other/SlownessManager$Entry.class */
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
