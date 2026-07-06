package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import net.minecraft.class_1792;
import net.minecraft.class_1802;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.KeyEvent;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BindSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.utils.other.SlownessManager;
import sweetie.leonware.api.utils.player.InventoryUtil;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/ItemSwapModule.class */
@ModuleRegister(name = "Item Swap", category = Category.COMBAT)
public class ItemSwapModule extends Module {
    private static final ItemSwapModule instance = new ItemSwapModule();
    private final BindSetting swapKey = new BindSetting("Swap key").value((Integer) (-999));
    private final ModeSetting firstItem = new ModeSetting("First item").value("Shield").values("Shield", "GApple", "Totem", "Ball");
    private final ModeSetting secondItem = new ModeSetting("Second item").value("GApple").values("Shield", "GApple", "Totem", "Ball");
    private boolean swapping = false;

    @Generated
    public static ItemSwapModule getInstance() {
        return instance;
    }

    public ItemSwapModule() {
        addSettings(this.swapKey, this.firstItem, this.secondItem);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener keyEvent = KeyEvent.getInstance().subscribe(new Listener(event -> {
            if (event.key() == this.swapKey.getValue().intValue() && event.action() == 1 && mc.field_1755 == null) {
                this.swapping = true;
            }
        }));
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener(event2 -> {
            if (SlownessManager.isEnabled()) {
                performSwap();
            }
        }));
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event3 -> {
            if (SlownessManager.isEnabled()) {
                return;
            }
            performSwap();
        }));
        addEvents(keyEvent, tickEvent, updateEvent);
    }

    private void performSwap() {
        if (mc.field_1687 == null || mc.field_1724 == null || mc.field_1761 == null || !this.swapping) {
            return;
        }
        class_1792 item = getItem();
        if (item == null) {
            print("Предмет не найден");
            this.swapping = false;
            return;
        }
        int slot = InventoryUtil.findItem(item);
        if (slot == -1) {
            print("Предмет не найден в инвентаре");
            this.swapping = false;
        } else if (SlownessManager.isEnabled()) {
            SlownessManager.applySlowness(10L, () -> {
                swap(slot);
            });
        } else {
            swap(slot);
        }
    }

    private class_1792 getItem() {
        class_1792 primary = getItemByMode(this.firstItem.getValue());
        class_1792 secondary = getItemByMode(this.secondItem.getValue());
        return mc.field_1724.method_6079().method_7909() == primary ? secondary : primary;
    }

    private void swap(int slot) {
        if (mc.field_1761 == null) {
            return;
        }
        print("Свапнул на \"" + getItem().method_63680().getString() + "\"");
        if (SlownessManager.isEnabled()) {
            SlownessManager.applySlowness(10L, () -> {
                InventoryUtil.swapToOffhand(slot);
            });
        } else {
            InventoryUtil.swapToOffhand(slot);
        }
        this.swapping = false;
    }

    private class_1792 getItemByMode(String name) {
        switch (name.toLowerCase()) {
            case "shield":
                return class_1802.field_8255;
            case "ball":
                return class_1802.field_8575;
            case "totem":
                return class_1802.field_8288;
            case "gapple":
                return class_1802.field_8463;
            default:
                return null;
        }
    }
}
