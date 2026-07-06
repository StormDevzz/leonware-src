// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement.noslow.modes;

import net.minecraft.class_9334;
import net.minecraft.class_1799;
import net.minecraft.class_2338;
import net.minecraft.class_2246;
import sweetie.leonware.api.utils.other.SlownessManager;
import sweetie.leonware.api.utils.player.InventoryUtil;
import net.minecraft.class_1802;
import net.minecraft.class_1268;
import sweetie.leonware.client.features.modules.movement.noslow.NoSlowMode;

public class NoSlowFuntime extends NoSlowMode
{
    private boolean swapped;
    private int savedOffhandSlot;
    
    public NoSlowFuntime() {
        this.swapped = false;
        this.savedOffhandSlot = -1;
    }
    
    @Override
    public String getName() {
        return "FunTime10052026";
    }
    
    @Override
    public void onUpdate() {
        if (NoSlowFuntime.mc.field_1724 == null) {
            return;
        }
        if (!NoSlowFuntime.mc.field_1724.method_6115() || !this.isConsumable(NoSlowFuntime.mc.field_1724.method_6030())) {
            this.restore();
            return;
        }
        if (NoSlowFuntime.mc.field_1724.method_6058() != class_1268.field_5808) {
            return;
        }
        if (this.swapped) {
            return;
        }
        if (NoSlowFuntime.mc.field_1724.method_6079().method_31574(class_1802.field_8399)) {
            return;
        }
        if (this.isOnSnowLayer()) {
            return;
        }
        final int crossbowSlot = InventoryUtil.findItem(class_1802.field_8399);
        if (crossbowSlot == -1) {
            return;
        }
        this.savedOffhandSlot = InventoryUtil.findItem(NoSlowFuntime.mc.field_1724.method_6079().method_7909());
        this.swapped = true;
        if (SlownessManager.isEnabled()) {
            SlownessManager.applySlowness(10L, () -> InventoryUtil.swapToOffhand(crossbowSlot));
        }
        else {
            InventoryUtil.swapToOffhand(crossbowSlot);
        }
    }
    
    private boolean isOnSnowLayer() {
        if (NoSlowFuntime.mc.field_1724 == null || NoSlowFuntime.mc.field_1687 == null) {
            return false;
        }
        final class_2338 below = NoSlowFuntime.mc.field_1724.method_24515().method_10074();
        return NoSlowFuntime.mc.field_1687.method_8320(below).method_27852(class_2246.field_10477);
    }
    
    @Override
    public void onTick() {
        if (NoSlowFuntime.mc.field_1724 == null) {
            return;
        }
        if (!NoSlowFuntime.mc.field_1724.method_6115() || !this.isConsumable(NoSlowFuntime.mc.field_1724.method_6030())) {
            this.restore();
        }
    }
    
    @Override
    public boolean slowingCancel() {
        return true;
    }
    
    public void onDisable() {
        this.restore();
    }
    
    private void restore() {
        if (!this.swapped) {
            return;
        }
        if (NoSlowFuntime.mc.field_1724 != null && this.savedOffhandSlot != -1) {
            if (SlownessManager.isEnabled()) {
                SlownessManager.applySlowness(10L, () -> InventoryUtil.swapToOffhand(this.savedOffhandSlot));
            }
            else {
                InventoryUtil.swapToOffhand(this.savedOffhandSlot);
            }
        }
        this.savedOffhandSlot = -1;
        this.swapped = false;
    }
    
    private boolean isConsumable(final class_1799 stack) {
        return stack != null && !stack.method_7960() && (stack.method_31574(class_1802.field_8574) || stack.method_31574(class_1802.field_8103) || stack.method_31574(class_1802.field_20417) || stack.method_57826(class_9334.field_50075));
    }
}
