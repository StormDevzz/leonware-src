/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1268
 *  net.minecraft.class_1304
 *  net.minecraft.class_1657
 *  net.minecraft.class_1713
 *  net.minecraft.class_1743
 *  net.minecraft.class_1792
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_1829
 *  net.minecraft.class_2371
 *  net.minecraft.class_2596
 *  net.minecraft.class_2815
 *  net.minecraft.class_2868
 *  net.minecraft.class_2886
 *  net.minecraft.class_9362
 */
package sweetie.leonware.api.utils.player;

import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1304;
import net.minecraft.class_1657;
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

public final class InventoryUtil
implements QuickImports {
    public static void dropSlot(int slot) {
        if (InventoryUtil.mc.field_1724 == null || InventoryUtil.mc.field_1761 == null) {
            return;
        }
        int syncId = InventoryUtil.mc.field_1724.field_7512.field_7763;
        InventoryUtil.mc.field_1761.method_2906(syncId, slot, 1, class_1713.field_7795, (class_1657)InventoryUtil.mc.field_1724);
        if (!InventoryMoveModule.getInstance().isBasic()) {
            NetworkUtil.sendPacket(new class_2815(syncId));
        }
    }

    public static void swapToOffhand(int slot) {
        if (slot == -1) {
            return;
        }
        if (InventoryUtil.mc.field_1724 == null || InventoryUtil.mc.field_1761 == null) {
            return;
        }
        int syncId = InventoryUtil.mc.field_1724.field_7512.field_7763;
        InventoryUtil.mc.field_1761.method_2906(syncId, slot, 40, class_1713.field_7791, (class_1657)InventoryUtil.mc.field_1724);
        InventoryUtil.mc.field_1724.field_3944.method_52787((class_2596)new class_2815(syncId));
    }

    public static void swapSlots(int from, int to) {
        int swapSlot;
        if (InventoryUtil.mc.field_1724 == null || InventoryUtil.mc.field_1761 == null) {
            return;
        }
        int syncId = InventoryUtil.mc.field_1724.field_7512.field_7763;
        int n = to >= 0 && to <= 8 ? to : (swapSlot = from >= 0 && from <= 8 ? from : -1);
        if (swapSlot == -1) {
            return;
        }
        InventoryUtil.mc.field_1761.method_2906(syncId, swapSlot == to ? from : to, swapSlot, class_1713.field_7791, (class_1657)InventoryUtil.mc.field_1724);
        if (!InventoryMoveModule.getInstance().isBasic()) {
            InventoryUtil.mc.field_1724.field_3944.method_52787((class_2596)new class_2815(syncId));
        }
    }

    public static void swapSlotsFull(int from, int to) {
        if (InventoryUtil.mc.field_1724 == null || InventoryUtil.mc.field_1761 == null) {
            return;
        }
        int syncId = InventoryUtil.mc.field_1724.field_7512.field_7763;
        InventoryUtil.mc.field_1761.method_2906(syncId, from, to, class_1713.field_7791, (class_1657)InventoryUtil.mc.field_1724);
        if (!InventoryMoveModule.getInstance().isBasic()) {
            InventoryUtil.mc.field_1724.field_3944.method_52787((class_2596)new class_2815(syncId));
        }
    }

    public static void useItem(class_1268 hand) {
        if (InventoryUtil.mc.field_1724 == null) {
            return;
        }
        RotationManager rotationManager = RotationManager.getInstance();
        float currentYaw = rotationManager.getCurrentRotationPlan() != null ? rotationManager.getRotation().getYaw() : InventoryUtil.mc.field_1724.method_36454();
        float currentPitch = rotationManager.getCurrentRotationPlan() != null ? rotationManager.getRotation().getPitch() : InventoryUtil.mc.field_1724.method_36455();
        NetworkUtil.sendPacket(s -> new class_2886(hand, s, currentYaw, currentPitch));
        InventoryUtil.mc.field_1724.method_6104(hand);
    }

    public static void swapToSlot(int slot) {
        InventoryUtil.swapToSlot(slot, true);
    }

    public static void swapToSlot(int slot, boolean client) {
        if (InventoryUtil.mc.field_1724 == null) {
            return;
        }
        if (InventoryUtil.mc.field_1724.method_31548().field_7545 == slot) {
            return;
        }
        InventoryUtil.mc.field_1724.field_3944.method_52787((class_2596)new class_2868(slot));
        if (client) {
            InventoryUtil.mc.field_1724.method_31548().field_7545 = slot;
        }
    }

    public static int findItem(class_1792 item, boolean inHotbar) {
        if (InventoryUtil.mc.field_1724 == null) {
            return -1;
        }
        class_2371 main = InventoryUtil.mc.field_1724.method_31548().field_7547;
        int firstSlot = inHotbar ? 0 : 9;
        int lastSlot = inHotbar ? 9 : 36;
        int finalSlot = -1;
        for (int i = firstSlot; i < lastSlot; ++i) {
            if (((class_1799)main.get(i)).method_7909() != item) continue;
            finalSlot = i;
        }
        return finalSlot;
    }

    public static int findItem(class_1792 input) {
        if (InventoryUtil.mc.field_1724 == null) {
            return -1;
        }
        class_2371 main = InventoryUtil.mc.field_1724.method_31548().field_7547;
        int slot = -1;
        for (int i = 0; i < 36; ++i) {
            class_1799 stack = (class_1799)main.get(i);
            if (stack.method_7909() != input) continue;
            slot = i;
            break;
        }
        if (slot < 9 && slot != -1) {
            slot += 36;
        }
        return slot;
    }

    public static boolean hasElytraEquipped() {
        return InventoryUtil.mc.field_1724.method_6118(class_1304.field_6174).method_31574(class_1802.field_8833);
    }

    public static int findAxeInInventory(boolean inHotbar) {
        if (InventoryUtil.mc.field_1724 == null) {
            return -1;
        }
        class_2371 main = InventoryUtil.mc.field_1724.method_31548().field_7547;
        int firstSlot = inHotbar ? 0 : 9;
        int lastSlot = inHotbar ? 9 : 36;
        for (int i = firstSlot; i < lastSlot; ++i) {
            if (!(((class_1799)main.get(i)).method_7909() instanceof class_1743)) continue;
            return i;
        }
        return -1;
    }

    public static int findBestSlotInHotBar() {
        int emptySlot = InventoryUtil.findEmptySlot();
        return emptySlot != -1 ? emptySlot : InventoryUtil.findNonSwordSlot();
    }

    public static int findEmptySlot() {
        if (InventoryUtil.mc.field_1724 == null) {
            return -1;
        }
        class_2371 main = InventoryUtil.mc.field_1724.method_31548().field_7547;
        int currentItem = InventoryUtil.mc.field_1724.method_31548().field_7545;
        for (int i = 0; i < 9; ++i) {
            if (!((class_1799)main.get(i)).method_7960() || currentItem == i) continue;
            return i;
        }
        return -1;
    }

    public static int findMaceBetter() {
        if (InventoryUtil.mc.field_1724 == null) {
            return -1;
        }
        int bestSlot = -1;
        boolean bestIsHotbar = false;
        for (int i = 0; i < 36; ++i) {
            boolean isBetter;
            class_1799 stack = InventoryUtil.mc.field_1724.method_31548().method_5438(i);
            if (stack.method_7960() || !(stack.method_7909() instanceof class_9362)) continue;
            boolean isHotbar = i < 9;
            boolean bl = isBetter = isHotbar && !bestIsHotbar || isHotbar == bestIsHotbar && bestSlot == -1;
            if (!isBetter) continue;
            bestSlot = i;
            bestIsHotbar = isHotbar;
        }
        return bestSlot;
    }

    public static int findAxeBetter() {
        if (InventoryUtil.mc.field_1724 == null) {
            return -1;
        }
        int hotbarSlot = -1;
        int hotbarTier = -1;
        int invSlot = -1;
        int invTier = -1;
        for (int i = 0; i < 36; ++i) {
            class_1799 stack = InventoryUtil.mc.field_1724.method_31548().method_5438(i);
            if (stack.method_7960() || !(stack.method_7909() instanceof class_1743)) continue;
            int tier = InventoryUtil.getAxeTier(stack.method_7909());
            if (i < 9) {
                if (tier <= hotbarTier) continue;
                hotbarTier = tier;
                hotbarSlot = i;
                continue;
            }
            if (tier <= invTier) continue;
            invTier = tier;
            invSlot = i;
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
        if (item == class_1802.field_8406) {
            return 0;
        }
        return -1;
    }

    private static int findNonSwordSlot() {
        if (InventoryUtil.mc.field_1724 == null) {
            return -1;
        }
        class_2371 main = InventoryUtil.mc.field_1724.method_31548().field_7547;
        int currentItem = InventoryUtil.mc.field_1724.method_31548().field_7545;
        for (int i = 0; i < 9; ++i) {
            class_1799 stack = (class_1799)main.get(i);
            if (stack.method_7909() instanceof class_1829 || currentItem == i) continue;
            return i;
        }
        return -1;
    }

    public static int countNonEnchantedTotems() {
        if (InventoryUtil.mc.field_1724 == null) {
            return -1;
        }
        int count = 0;
        class_2371 main = InventoryUtil.mc.field_1724.method_31548().field_7547;
        for (int i = 0; i < 36; ++i) {
            class_1799 stack = (class_1799)main.get(i);
            if (!stack.method_31574(class_1802.field_8288) || stack.method_7942()) continue;
            ++count;
        }
        return count;
    }

    public static int findBestTotemSlot(boolean saveEnchanted) {
        class_2371 main = InventoryUtil.mc.field_1724.method_31548().field_7547;
        for (int i = 0; i < 36; ++i) {
            class_1799 stack = (class_1799)main.get(i);
            if (!stack.method_31574(class_1802.field_8288) || saveEnchanted && stack.method_7942()) continue;
            return i < 9 ? i + 36 : i;
        }
        return -1;
    }

    public static int findMaceInInventory(boolean inHotbar) {
        if (InventoryUtil.mc.field_1724 == null) {
            return -1;
        }
        class_2371 main = InventoryUtil.mc.field_1724.method_31548().field_7547;
        int firstSlot = inHotbar ? 0 : 9;
        int lastSlot = inHotbar ? 9 : 36;
        for (int i = firstSlot; i < lastSlot; ++i) {
            if (!(((class_1799)main.get(i)).method_7909() instanceof class_9362)) continue;
            return i;
        }
        return -1;
    }

    @Generated
    private InventoryUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

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

        public void onDisable() {
            this.usageState = UsageState.IDLE;
            this.stateDelay = 0;
        }

        public void updateCustomRotation(Rotation rotation) {
            this.customRotation = rotation;
        }

        public void applyRotation() {
            if (!this.useRotation) {
                return;
            }
            float yaw = this.customRotation.getYaw() != Float.MIN_VALUE ? this.customRotation.getYaw() : QuickImports.mc.field_1724.method_36454();
            float pitch = this.customRotation.getPitch() != Float.MIN_VALUE ? this.customRotation.getPitch() : QuickImports.mc.field_1724.method_36455();
            RotationStrategy configurable = new RotationStrategy(new SmoothRotation(), MoveFixModule.enabled());
            RotationManager.getInstance().addRotation(new Rotation(yaw, pitch), configurable, TaskPriority.REQUIRED, this.provider);
        }

        public void handleUse(boolean isLegit) {
            if (QuickImports.mc.field_1755 == null && this.pendingAction != null) {
                this.pendingAction.run();
            }
            if (QuickImports.mc.field_1724.method_7357().method_7904(this.item.method_7854())) {
                return;
            }
            if (isLegit) {
                this.pendingAction = () -> this.executeLegitMode(-1);
            } else {
                this.executePacketMode();
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
                this.pendingAction = () -> this.executeLegitMode(bind);
            } else {
                this.executePacketMode();
            }
        }

        public void executePacketMode() {
            int invSlot = InventoryUtil.findItem(this.item, false);
            int hbSlot = InventoryUtil.findItem(this.item, true);
            if (QuickImports.mc.field_1724.method_6079().method_31574(this.item)) {
                this.applyRotation();
                InventoryUtil.useItem(class_1268.field_5810);
                this.forceUse = true;
                return;
            }
            if (QuickImports.mc.field_1724.method_6047().method_31574(this.item)) {
                this.applyRotation();
                InventoryUtil.useItem(class_1268.field_5808);
                this.forceUse = true;
                return;
            }
            int oldSlot = QuickImports.mc.field_1724.method_31548().field_7545;
            int bestSlot = InventoryUtil.findBestSlotInHotBar();
            if (hbSlot != -1) {
                this.applyRotation();
                InventoryUtil.swapToSlot(hbSlot);
                InventoryUtil.useItem(class_1268.field_5808);
                InventoryUtil.swapToSlot(oldSlot);
                this.forceUse = true;
            } else if (invSlot != -1) {
                Runnable runnable = () -> {
                    this.applyRotation();
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
                case 0: {
                    if (bind != -1 && !KeyStorage.isPressed(bind)) {
                        return;
                    }
                    this.targetSlot = InventoryUtil.findItem(this.item, true);
                    this.previousSlot = QuickImports.mc.field_1724.method_31548().field_7545;
                    if (this.targetSlot == -1) {
                        this.targetSlot = InventoryUtil.findItem(this.item, false);
                        if (this.targetSlot == -1) {
                            return;
                        }
                        this.usageState = UsageState.SWAP_ITEM;
                        this.stateDelay = 1;
                        break;
                    }
                    QuickImports.mc.field_1724.method_31548().field_7545 = this.targetSlot;
                    this.usageState = UsageState.USE_ITEM;
                    this.stateDelay = 1;
                    break;
                }
                case 1: {
                    if (this.stateDelay-- > 0) {
                        return;
                    }
                    int hotbarSlot = InventoryUtil.findBestSlotInHotBar();
                    if (hotbarSlot != -1) {
                        Runnable toUseItem = () -> {
                            InventoryUtil.swapSlots(this.targetSlot, hotbarSlot);
                            QuickImports.mc.field_1724.method_31548().field_7545 = this.targetSlot = hotbarSlot;
                            InventoryUtil.swapToSlot(this.targetSlot);
                            this.usageState = UsageState.USE_ITEM;
                            this.stateDelay = 1;
                        };
                        if (SlownessManager.isEnabled()) {
                            SlownessManager.applySlowness(10L, toUseItem);
                            break;
                        }
                        toUseItem.run();
                        break;
                    }
                    this.usageState = UsageState.IDLE;
                    break;
                }
                case 2: {
                    if (this.stateDelay-- > 0) {
                        return;
                    }
                    this.applyRotation();
                    InventoryUtil.useItem(class_1268.field_5808);
                    this.usageState = UsageState.RESTORE_SLOT;
                    this.stateDelay = 1;
                    break;
                }
                case 3: {
                    if (this.stateDelay-- > 0) {
                        return;
                    }
                    QuickImports.mc.field_1724.method_31548().field_7545 = this.previousSlot;
                    this.usageState = UsageState.IDLE;
                    this.pendingAction = null;
                    this.forceUse = true;
                }
            }
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

        @Generated
        public ItemUsage(class_1792 item, Module provider) {
            this.item = item;
            this.provider = provider;
        }

        @Generated
        public void setUseRotation(boolean useRotation) {
            this.useRotation = useRotation;
        }

        public static enum UsageState {
            IDLE,
            SWAP_ITEM,
            USE_ITEM,
            RESTORE_SLOT;

        }
    }
}

