// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.player;

import sweetie.leonware.api.utils.other.SlownessManager;
import sweetie.leonware.api.system.backend.KeyStorage;
import sweetie.leonware.api.utils.task.TaskPriority;
import sweetie.leonware.api.utils.rotation.manager.RotationMode;
import sweetie.leonware.api.utils.rotation.manager.RotationStrategy;
import sweetie.leonware.client.features.modules.movement.MoveFixModule;
import sweetie.leonware.api.utils.rotation.rotations.SmoothRotation;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.module.Module;
import net.minecraft.class_2886;
import lombok.Generated;
import net.minecraft.class_1829;
import net.minecraft.class_9362;
import net.minecraft.class_1743;
import net.minecraft.class_1802;
import net.minecraft.class_1304;
import net.minecraft.class_2371;
import net.minecraft.class_1799;
import net.minecraft.class_1792;
import net.minecraft.class_2868;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import net.minecraft.class_1268;
import net.minecraft.class_2596;
import sweetie.leonware.api.utils.other.NetworkUtil;
import net.minecraft.class_2815;
import sweetie.leonware.client.features.modules.movement.InventoryMoveModule;
import net.minecraft.class_1657;
import net.minecraft.class_1713;
import sweetie.leonware.api.system.interfaces.QuickImports;

public final class InventoryUtil implements QuickImports
{
    public static void dropSlot(final int slot) {
        if (InventoryUtil.mc.field_1724 == null || InventoryUtil.mc.field_1761 == null) {
            return;
        }
        final int syncId = InventoryUtil.mc.field_1724.field_7512.field_7763;
        InventoryUtil.mc.field_1761.method_2906(syncId, slot, 1, class_1713.field_7795, (class_1657)InventoryUtil.mc.field_1724);
        if (!InventoryMoveModule.getInstance().isBasic()) {
            NetworkUtil.sendPacket((class_2596<?>)new class_2815(syncId));
        }
    }
    
    public static void swapToOffhand(final int slot) {
        if (slot == -1) {
            return;
        }
        if (InventoryUtil.mc.field_1724 == null || InventoryUtil.mc.field_1761 == null) {
            return;
        }
        final int syncId = InventoryUtil.mc.field_1724.field_7512.field_7763;
        InventoryUtil.mc.field_1761.method_2906(syncId, slot, 40, class_1713.field_7791, (class_1657)InventoryUtil.mc.field_1724);
        InventoryUtil.mc.field_1724.field_3944.method_52787((class_2596)new class_2815(syncId));
    }
    
    public static void swapSlots(final int from, final int to) {
        if (InventoryUtil.mc.field_1724 == null || InventoryUtil.mc.field_1761 == null) {
            return;
        }
        final int syncId = InventoryUtil.mc.field_1724.field_7512.field_7763;
        final int swapSlot = (to >= 0 && to <= 8) ? to : ((from >= 0 && from <= 8) ? from : -1);
        if (swapSlot == -1) {
            return;
        }
        InventoryUtil.mc.field_1761.method_2906(syncId, (swapSlot == to) ? from : to, swapSlot, class_1713.field_7791, (class_1657)InventoryUtil.mc.field_1724);
        if (!InventoryMoveModule.getInstance().isBasic()) {
            InventoryUtil.mc.field_1724.field_3944.method_52787((class_2596)new class_2815(syncId));
        }
    }
    
    public static void swapSlotsFull(final int from, final int to) {
        if (InventoryUtil.mc.field_1724 == null || InventoryUtil.mc.field_1761 == null) {
            return;
        }
        final int syncId = InventoryUtil.mc.field_1724.field_7512.field_7763;
        InventoryUtil.mc.field_1761.method_2906(syncId, from, to, class_1713.field_7791, (class_1657)InventoryUtil.mc.field_1724);
        if (!InventoryMoveModule.getInstance().isBasic()) {
            InventoryUtil.mc.field_1724.field_3944.method_52787((class_2596)new class_2815(syncId));
        }
    }
    
    public static void useItem(final class_1268 hand) {
        if (InventoryUtil.mc.field_1724 == null) {
            return;
        }
        final RotationManager rotationManager = RotationManager.getInstance();
        final float currentYaw = (rotationManager.getCurrentRotationPlan() != null) ? rotationManager.getRotation().getYaw() : InventoryUtil.mc.field_1724.method_36454();
        final float currentPitch = (rotationManager.getCurrentRotationPlan() != null) ? rotationManager.getRotation().getPitch() : InventoryUtil.mc.field_1724.method_36455();
        NetworkUtil.sendPacket(s -> new class_2886(hand, s, currentYaw, currentPitch));
        InventoryUtil.mc.field_1724.method_6104(hand);
    }
    
    public static void swapToSlot(final int slot) {
        swapToSlot(slot, true);
    }
    
    public static void swapToSlot(final int slot, final boolean client) {
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
    
    public static int findItem(final class_1792 item, final boolean inHotbar) {
        if (InventoryUtil.mc.field_1724 == null) {
            return -1;
        }
        final class_2371<class_1799> main = (class_2371<class_1799>)InventoryUtil.mc.field_1724.method_31548().field_7547;
        final int firstSlot = inHotbar ? 0 : 9;
        final int lastSlot = inHotbar ? 9 : 36;
        int finalSlot = -1;
        for (int i = firstSlot; i < lastSlot; ++i) {
            if (((class_1799)main.get(i)).method_7909() == item) {
                finalSlot = i;
            }
        }
        return finalSlot;
    }
    
    public static int findItem(final class_1792 input) {
        if (InventoryUtil.mc.field_1724 == null) {
            return -1;
        }
        final class_2371<class_1799> main = (class_2371<class_1799>)InventoryUtil.mc.field_1724.method_31548().field_7547;
        int slot = -1;
        for (int i = 0; i < 36; ++i) {
            final class_1799 stack = (class_1799)main.get(i);
            if (stack.method_7909() == input) {
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
        return InventoryUtil.mc.field_1724.method_6118(class_1304.field_6174).method_31574(class_1802.field_8833);
    }
    
    public static int findAxeInInventory(final boolean inHotbar) {
        if (InventoryUtil.mc.field_1724 == null) {
            return -1;
        }
        final class_2371<class_1799> main = (class_2371<class_1799>)InventoryUtil.mc.field_1724.method_31548().field_7547;
        final int firstSlot = inHotbar ? 0 : 9;
        for (int lastSlot = inHotbar ? 9 : 36, i = firstSlot; i < lastSlot; ++i) {
            if (((class_1799)main.get(i)).method_7909() instanceof class_1743) {
                return i;
            }
        }
        return -1;
    }
    
    public static int findBestSlotInHotBar() {
        final int emptySlot = findEmptySlot();
        return (emptySlot != -1) ? emptySlot : findNonSwordSlot();
    }
    
    public static int findEmptySlot() {
        if (InventoryUtil.mc.field_1724 == null) {
            return -1;
        }
        final class_2371<class_1799> main = (class_2371<class_1799>)InventoryUtil.mc.field_1724.method_31548().field_7547;
        final int currentItem = InventoryUtil.mc.field_1724.method_31548().field_7545;
        for (int i = 0; i < 9; ++i) {
            if (((class_1799)main.get(i)).method_7960() && currentItem != i) {
                return i;
            }
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
            final class_1799 stack = InventoryUtil.mc.field_1724.method_31548().method_5438(i);
            if (!stack.method_7960()) {
                if (stack.method_7909() instanceof class_9362) {
                    final boolean isHotbar = i < 9;
                    final boolean isBetter = (isHotbar && !bestIsHotbar) || (isHotbar == bestIsHotbar && bestSlot == -1);
                    if (isBetter) {
                        bestSlot = i;
                        bestIsHotbar = isHotbar;
                    }
                }
            }
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
            final class_1799 stack = InventoryUtil.mc.field_1724.method_31548().method_5438(i);
            if (!stack.method_7960()) {
                if (stack.method_7909() instanceof class_1743) {
                    final int tier = getAxeTier(stack.method_7909());
                    if (i < 9) {
                        if (tier > hotbarTier) {
                            hotbarTier = tier;
                            hotbarSlot = i;
                        }
                    }
                    else if (tier > invTier) {
                        invTier = tier;
                        invSlot = i;
                    }
                }
            }
        }
        return (hotbarSlot != -1) ? hotbarSlot : invSlot;
    }
    
    private static int getAxeTier(final class_1792 item) {
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
        final class_2371<class_1799> main = (class_2371<class_1799>)InventoryUtil.mc.field_1724.method_31548().field_7547;
        final int currentItem = InventoryUtil.mc.field_1724.method_31548().field_7545;
        for (int i = 0; i < 9; ++i) {
            final class_1799 stack = (class_1799)main.get(i);
            if (!(stack.method_7909() instanceof class_1829) && currentItem != i) {
                return i;
            }
        }
        return -1;
    }
    
    public static int countNonEnchantedTotems() {
        if (InventoryUtil.mc.field_1724 == null) {
            return -1;
        }
        int count = 0;
        final class_2371<class_1799> main = (class_2371<class_1799>)InventoryUtil.mc.field_1724.method_31548().field_7547;
        for (int i = 0; i < 36; ++i) {
            final class_1799 stack = (class_1799)main.get(i);
            if (stack.method_31574(class_1802.field_8288) && !stack.method_7942()) {
                ++count;
            }
        }
        return count;
    }
    
    public static int findBestTotemSlot(final boolean saveEnchanted) {
        final class_2371<class_1799> main = (class_2371<class_1799>)InventoryUtil.mc.field_1724.method_31548().field_7547;
        for (int i = 0; i < 36; ++i) {
            final class_1799 stack = (class_1799)main.get(i);
            if (stack.method_31574(class_1802.field_8288) && (!saveEnchanted || !stack.method_7942())) {
                return (i < 9) ? (i + 36) : i;
            }
        }
        return -1;
    }
    
    public static int findMaceInInventory(final boolean inHotbar) {
        if (InventoryUtil.mc.field_1724 == null) {
            return -1;
        }
        final class_2371<class_1799> main = (class_2371<class_1799>)InventoryUtil.mc.field_1724.method_31548().field_7547;
        final int firstSlot = inHotbar ? 0 : 9;
        for (int lastSlot = inHotbar ? 9 : 36, i = firstSlot; i < lastSlot; ++i) {
            if (((class_1799)main.get(i)).method_7909() instanceof class_9362) {
                return i;
            }
        }
        return -1;
    }
    
    @Generated
    private InventoryUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    public static class ItemUsage
    {
        private final class_1792 item;
        private final Module provider;
        private boolean forceUse;
        private boolean useRotation;
        private Rotation customRotation;
        private int targetSlot;
        private int previousSlot;
        private int stateDelay;
        private Runnable pendingAction;
        private UsageState usageState;
        
        public void onDisable() {
            this.usageState = UsageState.IDLE;
            this.stateDelay = 0;
        }
        
        public void updateCustomRotation(final Rotation rotation) {
            this.customRotation = rotation;
        }
        
        public void applyRotation() {
            if (!this.useRotation) {
                return;
            }
            final float yaw = (this.customRotation.getYaw() != Float.MIN_VALUE) ? this.customRotation.getYaw() : QuickImports.mc.field_1724.method_36454();
            final float pitch = (this.customRotation.getPitch() != Float.MIN_VALUE) ? this.customRotation.getPitch() : QuickImports.mc.field_1724.method_36455();
            final RotationStrategy configurable = new RotationStrategy(new SmoothRotation(), MoveFixModule.enabled());
            RotationManager.getInstance().addRotation(new Rotation(yaw, pitch), configurable, TaskPriority.REQUIRED, this.provider);
        }
        
        public void handleUse(final boolean isLegit) {
            if (QuickImports.mc.field_1755 == null && this.pendingAction != null) {
                this.pendingAction.run();
            }
            if (QuickImports.mc.field_1724.method_7357().method_7904(this.item.method_7854())) {
                return;
            }
            if (isLegit) {
                this.pendingAction = (() -> this.executeLegitMode(-1));
            }
            else {
                this.executePacketMode();
            }
            this.forceUse = false;
        }
        
        public void handleUse(final int bind, final boolean isLegit) {
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
                this.pendingAction = (() -> this.executeLegitMode(bind));
            }
            else {
                this.executePacketMode();
            }
        }
        
        public void executePacketMode() {
            final int invSlot = InventoryUtil.findItem(this.item, false);
            final int hbSlot = InventoryUtil.findItem(this.item, true);
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
            final int oldSlot = QuickImports.mc.field_1724.method_31548().field_7545;
            final int bestSlot = InventoryUtil.findBestSlotInHotBar();
            if (hbSlot != -1) {
                this.applyRotation();
                InventoryUtil.swapToSlot(hbSlot);
                InventoryUtil.useItem(class_1268.field_5808);
                InventoryUtil.swapToSlot(oldSlot);
                this.forceUse = true;
            }
            else if (invSlot != -1) {
                final Runnable runnable = () -> {
                    this.applyRotation();
                    InventoryUtil.swapSlots(invSlot, bestSlot);
                    InventoryUtil.swapToSlot(bestSlot);
                    InventoryUtil.useItem(class_1268.field_5808);
                    InventoryUtil.swapToSlot(oldSlot);
                    InventoryUtil.swapSlots(invSlot, bestSlot);
                    this.forceUse = true;
                    return;
                };
                if (SlownessManager.isEnabled()) {
                    SlownessManager.applySlowness(10L, runnable);
                }
                else {
                    runnable.run();
                }
            }
        }
        
        public void executeLegitMode(final int bind) {
            switch (this.usageState.ordinal()) {
                case 0: {
                    if (bind != -1 && !KeyStorage.isPressed(bind)) {
                        return;
                    }
                    this.targetSlot = InventoryUtil.findItem(this.item, true);
                    this.previousSlot = QuickImports.mc.field_1724.method_31548().field_7545;
                    if (this.targetSlot != -1) {
                        QuickImports.mc.field_1724.method_31548().field_7545 = this.targetSlot;
                        this.usageState = UsageState.USE_ITEM;
                        this.stateDelay = 1;
                        break;
                    }
                    this.targetSlot = InventoryUtil.findItem(this.item, false);
                    if (this.targetSlot == -1) {
                        return;
                    }
                    this.usageState = UsageState.SWAP_ITEM;
                    this.stateDelay = 1;
                    break;
                }
                case 1: {
                    if (this.stateDelay-- > 0) {
                        return;
                    }
                    final int hotbarSlot = InventoryUtil.findBestSlotInHotBar();
                    if (hotbarSlot != -1) {
                        final Runnable toUseItem = () -> {
                            InventoryUtil.swapSlots(this.targetSlot, hotbarSlot);
                            this.targetSlot = hotbarSlot;
                            QuickImports.mc.field_1724.method_31548().field_7545 = this.targetSlot;
                            InventoryUtil.swapToSlot(this.targetSlot);
                            this.usageState = UsageState.USE_ITEM;
                            this.stateDelay = 1;
                            return;
                        };
                        if (SlownessManager.isEnabled()) {
                            SlownessManager.applySlowness(10L, toUseItem);
                        }
                        else {
                            toUseItem.run();
                        }
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
                    break;
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
        public ItemUsage(final class_1792 item, final Module provider) {
            this.forceUse = false;
            this.useRotation = true;
            this.customRotation = new Rotation(Float.MIN_VALUE, Float.MIN_VALUE);
            this.targetSlot = -1;
            this.previousSlot = -1;
            this.stateDelay = 0;
            this.pendingAction = null;
            this.usageState = UsageState.IDLE;
            this.item = item;
            this.provider = provider;
        }
        
        @Generated
        public void setUseRotation(final boolean useRotation) {
            this.useRotation = useRotation;
        }
        
        public enum UsageState
        {
            IDLE, 
            SWAP_ITEM, 
            USE_ITEM, 
            RESTORE_SLOT;
        }
    }
}
