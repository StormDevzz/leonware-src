// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.other;

import lombok.Generated;
import net.minecraft.class_3675;
import java.util.Iterator;
import java.util.ArrayList;
import net.minecraft.class_304;
import sweetie.leonware.api.utils.player.MoveUtil;
import sweetie.leonware.client.features.modules.movement.InventoryMoveModule;
import java.util.List;
import sweetie.leonware.api.system.interfaces.QuickImports;

public final class SlownessManager implements QuickImports
{
    private static final List<Entry> entries;
    public static boolean slowed;
    
    public static boolean isEnabled() {
        final InventoryMoveModule invMove = InventoryMoveModule.getInstance();
        return invMove.isLegit() || invMove.isAresMine();
    }
    
    public static void applySlowness(final long totalMs, final long delayMs, final Runnable action) {
        final long now = System.nanoTime();
        final long execNs = now + delayMs * 1000000L;
        final long endNs = now + totalMs * 1000000L;
        SlownessManager.entries.add(new Entry(execNs, endNs, action));
        if (!SlownessManager.slowed) {
            for (final class_304 key : MoveUtil.getMovementKeys()) {
                key.method_23481(false);
            }
            SlownessManager.slowed = true;
        }
    }
    
    public static void applySlowness(final long totalMs, final Runnable action) {
        applySlowness(totalMs, 1L, action);
    }
    
    public static void tick() {
        if (!SlownessManager.slowed && SlownessManager.entries.isEmpty()) {
            return;
        }
        final long now = System.nanoTime();
        List<Entry> toExecute = null;
        for (final Entry e : SlownessManager.entries) {
            if (!e.executed && now >= e.execNs) {
                if (toExecute == null) {
                    toExecute = new ArrayList<Entry>(4);
                }
                toExecute.add(e);
            }
        }
        if (toExecute != null) {
            for (final Entry e : toExecute) {
                try {
                    if (e.action == null) {
                        continue;
                    }
                    e.action.run();
                }
                catch (final Throwable t) {
                    t.printStackTrace();
                }
                finally {
                    e.executed = true;
                }
            }
        }
        for (int i = SlownessManager.entries.size() - 1; i >= 0; --i) {
            final Entry e = SlownessManager.entries.get(i);
            if (e.executed && now >= e.endNs) {
                SlownessManager.entries.remove(i);
            }
        }
        if (SlownessManager.entries.isEmpty() && SlownessManager.slowed) {
            resetKeys();
        }
    }
    
    private static void resetKeys() {
        if (SlownessManager.mc.field_1724 == null || SlownessManager.mc.field_1687 == null) {
            SlownessManager.entries.clear();
            SlownessManager.slowed = false;
            return;
        }
        final long handle = SlownessManager.mc.method_22683().method_4490();
        for (final class_304 key : MoveUtil.getMovementKeys()) {
            key.method_23481(class_3675.method_15987(handle, key.method_1429().method_1444()));
        }
        SlownessManager.slowed = false;
    }
    
    @Generated
    private SlownessManager() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    static {
        entries = new ArrayList<Entry>(4);
        SlownessManager.slowed = false;
    }
    
    private static final class Entry
    {
        final long execNs;
        final long endNs;
        final Runnable action;
        boolean executed;
        
        Entry(final long execNs, final long endNs, final Runnable action) {
            this.executed = false;
            this.execNs = execNs;
            this.endNs = endNs;
            this.action = action;
        }
    }
}
