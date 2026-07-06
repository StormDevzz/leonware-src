package sweetie.leonware.client.features.modules.movement.noslow.modes;

import net.minecraft.class_1268;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_9334;
import sweetie.leonware.api.utils.other.SlownessManager;
import sweetie.leonware.api.utils.player.InventoryUtil;
import sweetie.leonware.client.features.modules.movement.noslow.NoSlowMode;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/noslow/modes/NoSlowFuntime.class */
public class NoSlowFuntime extends NoSlowMode {
    private boolean swapped = false;
    private int savedOffhandSlot = -1;

    @Override // sweetie.leonware.api.system.backend.Choice, sweetie.leonware.api.module.setting.ModeSetting.NamedChoice
    public String getName() {
        return "FunTime10052026";
    }

    @Override // sweetie.leonware.client.features.modules.movement.noslow.NoSlowMode
    public void onUpdate() {
        int crossbowSlot;
        if (mc.field_1724 == null) {
            return;
        }
        if (!mc.field_1724.method_6115() || !isConsumable(mc.field_1724.method_6030())) {
            restore();
            return;
        }
        if (mc.field_1724.method_6058() != class_1268.field_5808 || this.swapped || mc.field_1724.method_6079().method_31574(class_1802.field_8399) || isOnSnowLayer() || (crossbowSlot = InventoryUtil.findItem(class_1802.field_8399)) == -1) {
            return;
        }
        this.savedOffhandSlot = InventoryUtil.findItem(mc.field_1724.method_6079().method_7909());
        this.swapped = true;
        if (SlownessManager.isEnabled()) {
            SlownessManager.applySlowness(10L, () -> {
                InventoryUtil.swapToOffhand(crossbowSlot);
            });
        } else {
            InventoryUtil.swapToOffhand(crossbowSlot);
        }
    }

    private boolean isOnSnowLayer() {
        if (mc.field_1724 == null || mc.field_1687 == null) {
            return false;
        }
        class_2338 below = mc.field_1724.method_24515().method_10074();
        return mc.field_1687.method_8320(below).method_27852(class_2246.field_10477);
    }

    @Override // sweetie.leonware.client.features.modules.movement.noslow.NoSlowMode
    public void onTick() {
        if (mc.field_1724 == null) {
            return;
        }
        if (!mc.field_1724.method_6115() || !isConsumable(mc.field_1724.method_6030())) {
            restore();
        }
    }

    @Override // sweetie.leonware.client.features.modules.movement.noslow.NoSlowMode
    public boolean slowingCancel() {
        return true;
    }

    public void onDisable() {
        restore();
    }

    private void restore() {
        if (this.swapped) {
            if (mc.field_1724 != null && this.savedOffhandSlot != -1) {
                if (SlownessManager.isEnabled()) {
                    SlownessManager.applySlowness(10L, () -> {
                        InventoryUtil.swapToOffhand(this.savedOffhandSlot);
                    });
                } else {
                    InventoryUtil.swapToOffhand(this.savedOffhandSlot);
                }
            }
            this.savedOffhandSlot = -1;
            this.swapped = false;
        }
    }

    private boolean isConsumable(class_1799 stack) {
        if (stack == null || stack.method_7960()) {
            return false;
        }
        return stack.method_31574(class_1802.field_8574) || stack.method_31574(class_1802.field_8103) || stack.method_31574(class_1802.field_20417) || stack.method_57826(class_9334.field_50075);
    }
}
