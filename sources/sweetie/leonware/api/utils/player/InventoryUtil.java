package sweetie.leonware.api.utils.player;

import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1304;
import net.minecraft.class_1713;
import net.minecraft.class_1743;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1829;
import net.minecraft.class_2371;
import net.minecraft.class_2596;
import net.minecraft.class_2815;
import net.minecraft.class_2868;
import net.minecraft.class_2886;
import net.minecraft.class_9362;
import org.newsclub.net.unix.AFVSOCKSocketAddress;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.system.backend.KeyStorage;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.other.NetworkUtil;
import sweetie.leonware.api.utils.other.SlownessManager;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.utils.rotation.manager.RotationStrategy;
import sweetie.leonware.api.utils.rotation.rotations.SmoothRotation;
import sweetie.leonware.api.utils.task.TaskPriority;
import sweetie.leonware.client.features.modules.movement.InventoryMoveModule;
import sweetie.leonware.client.features.modules.movement.MoveFixModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/player/InventoryUtil.class */
public final class InventoryUtil implements QuickImports {
    @Generated
    private InventoryUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void dropSlot(int slot) {
        if (mc.field_1724 == null || mc.field_1761 == null) {
            return;
        }
        int syncId = mc.field_1724.field_7512.field_7763;
        mc.field_1761.method_2906(syncId, slot, 1, class_1713.field_7795, mc.field_1724);
        if (!InventoryMoveModule.getInstance().isBasic()) {
            NetworkUtil.sendPacket((class_2596<?>) new class_2815(syncId));
        }
    }

    public static void swapToOffhand(int slot) {
        if (slot == -1 || mc.field_1724 == null || mc.field_1761 == null) {
            return;
        }
        int syncId = mc.field_1724.field_7512.field_7763;
        mc.field_1761.method_2906(syncId, slot, 40, class_1713.field_7791, mc.field_1724);
        mc.field_1724.field_3944.method_52787(new class_2815(syncId));
    }

    public static void swapSlots(int from, int to) {
        if (mc.field_1724 == null || mc.field_1761 == null) {
            return;
        }
        int syncId = mc.field_1724.field_7512.field_7763;
        int swapSlot = (to < 0 || to > 8) ? (from < 0 || from > 8) ? -1 : from : to;
        if (swapSlot == -1) {
            return;
        }
        mc.field_1761.method_2906(syncId, swapSlot == to ? from : to, swapSlot, class_1713.field_7791, mc.field_1724);
        if (!InventoryMoveModule.getInstance().isBasic()) {
            mc.field_1724.field_3944.method_52787(new class_2815(syncId));
        }
    }

    public static void swapSlotsFull(int from, int to) {
        if (mc.field_1724 == null || mc.field_1761 == null) {
            return;
        }
        int syncId = mc.field_1724.field_7512.field_7763;
        mc.field_1761.method_2906(syncId, from, to, class_1713.field_7791, mc.field_1724);
        if (!InventoryMoveModule.getInstance().isBasic()) {
            mc.field_1724.field_3944.method_52787(new class_2815(syncId));
        }
    }

    public static void useItem(class_1268 hand) {
        if (mc.field_1724 == null) {
            return;
        }
        RotationManager rotationManager = RotationManager.getInstance();
        float currentYaw = rotationManager.getCurrentRotationPlan() != null ? rotationManager.getRotation().getYaw() : mc.field_1724.method_36454();
        float currentPitch = rotationManager.getCurrentRotationPlan() != null ? rotationManager.getRotation().getPitch() : mc.field_1724.method_36455();
        NetworkUtil.sendPacket(s -> {
            return new class_2886(hand, s, currentYaw, currentPitch);
        });
        mc.field_1724.method_6104(hand);
    }

    public static void swapToSlot(int slot) {
        swapToSlot(slot, true);
    }

    public static void swapToSlot(int slot, boolean client) {
        if (mc.field_1724 == null || mc.field_1724.method_31548().field_7545 == slot) {
            return;
        }
        mc.field_1724.field_3944.method_52787(new class_2868(slot));
        if (client) {
            mc.field_1724.method_31548().field_7545 = slot;
        }
    }

    public static int findItem(class_1792 item, boolean inHotbar) {
        if (mc.field_1724 == null) {
            return -1;
        }
        class_2371<class_1799> main = mc.field_1724.method_31548().field_7547;
        int firstSlot = inHotbar ? 0 : 9;
        int lastSlot = inHotbar ? 9 : 36;
        int finalSlot = -1;
        for (int i = firstSlot; i < lastSlot; i++) {
            if (((class_1799) main.get(i)).method_7909() == item) {
                finalSlot = i;
            }
        }
        return finalSlot;
    }

    public static int findItem(class_1792 input) {
        if (mc.field_1724 == null) {
            return -1;
        }
        class_2371<class_1799> main = mc.field_1724.method_31548().field_7547;
        int slot = -1;
        int i = 0;
        while (true) {
            if (i >= 36) {
                break;
            }
            class_1799 stack = (class_1799) main.get(i);
            if (stack.method_7909() != input) {
                i++;
            } else {
                slot = i;
                break;
            }
        }
        if (slot < 9 && slot != -1) {
            slot += 36;
        }
        return slot;
    }

    public static boolean hasElytraEquipped() {
        return mc.field_1724.method_6118(class_1304.field_6174).method_31574(class_1802.field_8833);
    }

    public static int findAxeInInventory(boolean inHotbar) {
        if (mc.field_1724 == null) {
            return -1;
        }
        class_2371<class_1799> main = mc.field_1724.method_31548().field_7547;
        int firstSlot = inHotbar ? 0 : 9;
        int lastSlot = inHotbar ? 9 : 36;
        for (int i = firstSlot; i < lastSlot; i++) {
            if (((class_1799) main.get(i)).method_7909() instanceof class_1743) {
                return i;
            }
        }
        return -1;
    }

    public static int findBestSlotInHotBar() {
        int emptySlot = findEmptySlot();
        return emptySlot != -1 ? emptySlot : findNonSwordSlot();
    }

    public static int findEmptySlot() {
        if (mc.field_1724 == null) {
            return -1;
        }
        class_2371<class_1799> main = mc.field_1724.method_31548().field_7547;
        int currentItem = mc.field_1724.method_31548().field_7545;
        for (int i = 0; i < 9; i++) {
            if (((class_1799) main.get(i)).method_7960() && currentItem != i) {
                return i;
            }
        }
        return -1;
    }

    public static int findMaceBetter() {
        if (mc.field_1724 == null) {
            return -1;
        }
        int bestSlot = -1;
        boolean bestIsHotbar = false;
        int i = 0;
        while (i < 36) {
            class_1799 stack = mc.field_1724.method_31548().method_5438(i);
            if (!stack.method_7960() && (stack.method_7909() instanceof class_9362)) {
                boolean isHotbar = i < 9;
                boolean isBetter = (isHotbar && !bestIsHotbar) || (isHotbar == bestIsHotbar && bestSlot == -1);
                if (isBetter) {
                    bestSlot = i;
                    bestIsHotbar = isHotbar;
                }
            }
            i++;
        }
        return bestSlot;
    }

    public static int findAxeBetter() {
        if (mc.field_1724 == null) {
            return -1;
        }
        int hotbarSlot = -1;
        int hotbarTier = -1;
        int invSlot = -1;
        int invTier = -1;
        for (int i = 0; i < 36; i++) {
            class_1799 stack = mc.field_1724.method_31548().method_5438(i);
            if (!stack.method_7960() && (stack.method_7909() instanceof class_1743)) {
                int tier = getAxeTier(stack.method_7909());
                if (i < 9) {
                    if (tier > hotbarTier) {
                        hotbarTier = tier;
                        hotbarSlot = i;
                    }
                } else if (tier > invTier) {
                    invTier = tier;
                    invSlot = i;
                }
            }
        }
        return hotbarSlot != -1 ? hotbarSlot : invSlot;
    }

    private static int getAxeTier(class_1792 item) {
        if (item == class_1802.field_22025) {
            return 5;
        }
        if (item == class_1802.field_8556) {
            return 4;
        }
        if (item == class_1802.field_8475) {
            return 3;
        }
        if (item == class_1802.field_8825) {
            return 2;
        }
        if (item == class_1802.field_8062) {
            return 1;
        }
        return item == class_1802.field_8406 ? 0 : -1;
    }

    private static int findNonSwordSlot() {
        if (mc.field_1724 == null) {
            return -1;
        }
        class_2371<class_1799> main = mc.field_1724.method_31548().field_7547;
        int currentItem = mc.field_1724.method_31548().field_7545;
        for (int i = 0; i < 9; i++) {
            class_1799 stack = (class_1799) main.get(i);
            if (!(stack.method_7909() instanceof class_1829) && currentItem != i) {
                return i;
            }
        }
        return -1;
    }

    public static int countNonEnchantedTotems() {
        if (mc.field_1724 == null) {
            return -1;
        }
        int count = 0;
        class_2371<class_1799> main = mc.field_1724.method_31548().field_7547;
        for (int i = 0; i < 36; i++) {
            class_1799 stack = (class_1799) main.get(i);
            if (stack.method_31574(class_1802.field_8288) && !stack.method_7942()) {
                count++;
            }
        }
        return count;
    }

    public static int findBestTotemSlot(boolean saveEnchanted) {
        class_2371<class_1799> main = mc.field_1724.method_31548().field_7547;
        int i = 0;
        while (i < 36) {
            class_1799 stack = (class_1799) main.get(i);
            if (stack.method_31574(class_1802.field_8288) && (!saveEnchanted || !stack.method_7942())) {
                return i < 9 ? i + 36 : i;
            }
            i++;
        }
        return -1;
    }

    public static int findMaceInInventory(boolean inHotbar) {
        if (mc.field_1724 == null) {
            return -1;
        }
        class_2371<class_1799> main = mc.field_1724.method_31548().field_7547;
        int firstSlot = inHotbar ? 0 : 9;
        int lastSlot = inHotbar ? 9 : 36;
        for (int i = firstSlot; i < lastSlot; i++) {
            if (((class_1799) main.get(i)).method_7909() instanceof class_9362) {
                return i;
            }
        }
        return -1;
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/player/InventoryUtil$ItemUsage.class */
    public static class ItemUsage {
        private final class_1792 item;
        private final Module provider;
        private boolean forceUse = false;
        private boolean useRotation = true;
        private Rotation customRotation = new Rotation(Float.MIN_VALUE, Float.MIN_VALUE);
        private int targetSlot = -1;
        private int previousSlot = -1;
        private int stateDelay = 0;
        private Runnable pendingAction = null;
        private UsageState usageState = UsageState.IDLE;

        /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/player/InventoryUtil$ItemUsage$UsageState.class */
        public enum UsageState {
            IDLE,
            SWAP_ITEM,
            USE_ITEM,
            RESTORE_SLOT
        }

        @Generated
        public ItemUsage(class_1792 item, Module provider) {
            this.item = item;
            this.provider = provider;
        }

        @Generated
        public class_1792 getItem() {
            return this.item;
        }

        @Generated
        public Module getProvider() {
            return this.provider;
        }

        @Generated
        public boolean isForceUse() {
            return this.forceUse;
        }

        @Generated
        public boolean isUseRotation() {
            return this.useRotation;
        }

        @Generated
        public void setUseRotation(boolean useRotation) {
            this.useRotation = useRotation;
        }

        @Generated
        public Rotation getCustomRotation() {
            return this.customRotation;
        }

        @Generated
        public int getTargetSlot() {
            return this.targetSlot;
        }

        @Generated
        public int getPreviousSlot() {
            return this.previousSlot;
        }

        @Generated
        public int getStateDelay() {
            return this.stateDelay;
        }

        @Generated
        public Runnable getPendingAction() {
            return this.pendingAction;
        }

        @Generated
        public UsageState getUsageState() {
            return this.usageState;
        }

        public void onDisable() {
            this.usageState = UsageState.IDLE;
            this.stateDelay = 0;
        }

        public void updateCustomRotation(Rotation rotation) {
            this.customRotation = rotation;
        }

        public void applyRotation() {
            if (this.useRotation) {
                float yaw = this.customRotation.getYaw() != Float.MIN_VALUE ? this.customRotation.getYaw() : QuickImports.mc.field_1724.method_36454();
                float pitch = this.customRotation.getPitch() != Float.MIN_VALUE ? this.customRotation.getPitch() : QuickImports.mc.field_1724.method_36455();
                RotationStrategy configurable = new RotationStrategy(new SmoothRotation(), MoveFixModule.enabled());
                RotationManager.getInstance().addRotation(new Rotation(yaw, pitch), configurable, TaskPriority.REQUIRED, this.provider);
            }
        }

        public void handleUse(boolean isLegit) {
            if (QuickImports.mc.field_1755 == null && this.pendingAction != null) {
                this.pendingAction.run();
            }
            if (QuickImports.mc.field_1724.method_7357().method_7904(this.item.method_7854())) {
                return;
            }
            if (isLegit) {
                this.pendingAction = () -> {
                    executeLegitMode(-1);
                };
            } else {
                executePacketMode();
            }
            this.forceUse = false;
        }

        public void handleUse(int bind, boolean isLegit) {
            if (!isLegit) {
                this.pendingAction = null;
            }
            if (QuickImports.mc.field_1755 == null && this.pendingAction != null) {
                this.pendingAction.run();
            }
            if (!KeyStorage.isPressed(bind) && QuickImports.mc.field_1755 == null) {
                this.forceUse = false;
                return;
            }
            if (this.forceUse || QuickImports.mc.field_1755 != null || QuickImports.mc.field_1724.method_7357().method_7904(this.item.method_7854())) {
                return;
            }
            if (isLegit) {
                this.pendingAction = () -> {
                    executeLegitMode(bind);
                };
            } else {
                executePacketMode();
            }
        }

        public void executePacketMode() {
            int invSlot = InventoryUtil.findItem(this.item, false);
            int hbSlot = InventoryUtil.findItem(this.item, true);
            if (QuickImports.mc.field_1724.method_6079().method_31574(this.item)) {
                applyRotation();
                InventoryUtil.useItem(class_1268.field_5810);
                this.forceUse = true;
                return;
            }
            if (QuickImports.mc.field_1724.method_6047().method_31574(this.item)) {
                applyRotation();
                InventoryUtil.useItem(class_1268.field_5808);
                this.forceUse = true;
                return;
            }
            int oldSlot = QuickImports.mc.field_1724.method_31548().field_7545;
            int bestSlot = InventoryUtil.findBestSlotInHotBar();
            if (hbSlot != -1) {
                applyRotation();
                InventoryUtil.swapToSlot(hbSlot);
                InventoryUtil.useItem(class_1268.field_5808);
                InventoryUtil.swapToSlot(oldSlot);
                this.forceUse = true;
                return;
            }
            if (invSlot != -1) {
                Runnable runnable = () -> {
                    applyRotation();
                    InventoryUtil.swapSlots(invSlot, bestSlot);
                    InventoryUtil.swapToSlot(bestSlot);
                    InventoryUtil.useItem(class_1268.field_5808);
                    InventoryUtil.swapToSlot(oldSlot);
                    InventoryUtil.swapSlots(invSlot, bestSlot);
                    this.forceUse = true;
                };
                if (SlownessManager.isEnabled()) {
                    SlownessManager.applySlowness(10L, runnable);
                } else {
                    runnable.run();
                }
            }
        }

        public void executeLegitMode(int bind) {
            switch (this.usageState.ordinal()) {
                case 0:
                    if (bind == -1 || KeyStorage.isPressed(bind)) {
                        this.targetSlot = InventoryUtil.findItem(this.item, true);
                        this.previousSlot = QuickImports.mc.field_1724.method_31548().field_7545;
                        if (this.targetSlot == -1) {
                            this.targetSlot = InventoryUtil.findItem(this.item, false);
                            if (this.targetSlot != -1) {
                                this.usageState = UsageState.SWAP_ITEM;
                                this.stateDelay = 1;
                                break;
                            }
                        } else {
                            QuickImports.mc.field_1724.method_31548().field_7545 = this.targetSlot;
                            this.usageState = UsageState.USE_ITEM;
                            this.stateDelay = 1;
                            break;
                        }
                    }
                    break;
                case 1:
                    int i = this.stateDelay;
                    this.stateDelay = i - 1;
                    if (i <= 0) {
                        int hotbarSlot = InventoryUtil.findBestSlotInHotBar();
                        if (hotbarSlot != -1) {
                            Runnable toUseItem = () -> {
                                InventoryUtil.swapSlots(this.targetSlot, hotbarSlot);
                                this.targetSlot = hotbarSlot;
                                QuickImports.mc.field_1724.method_31548().field_7545 = this.targetSlot;
                                InventoryUtil.swapToSlot(this.targetSlot);
                                this.usageState = UsageState.USE_ITEM;
                                this.stateDelay = 1;
                            };
                            if (SlownessManager.isEnabled()) {
                                SlownessManager.applySlowness(10L, toUseItem);
                            } else {
                                toUseItem.run();
                            }
                        } else {
                            this.usageState = UsageState.IDLE;
                        }
                        break;
                    }
                    break;
                case AFVSOCKSocketAddress.VMADDR_CID_HOST /* 2 */:
                    int i2 = this.stateDelay;
                    this.stateDelay = i2 - 1;
                    if (i2 <= 0) {
                        applyRotation();
                        InventoryUtil.useItem(class_1268.field_5808);
                        this.usageState = UsageState.RESTORE_SLOT;
                        this.stateDelay = 1;
                        break;
                    }
                    break;
                case 3:
                    int i3 = this.stateDelay;
                    this.stateDelay = i3 - 1;
                    if (i3 <= 0) {
                        QuickImports.mc.field_1724.method_31548().field_7545 = this.previousSlot;
                        this.usageState = UsageState.IDLE;
                        this.pendingAction = null;
                        this.forceUse = true;
                        break;
                    }
                    break;
            }
        }
    }
}
