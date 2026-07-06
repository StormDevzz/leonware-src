/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1792
 *  net.minecraft.class_1802
 */
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

@ModuleRegister(name="Item Swap", category=Category.COMBAT)
public class ItemSwapModule
extends Module {
    private static final ItemSwapModule instance = new ItemSwapModule();
    private final BindSetting swapKey = new BindSetting("Swap key").value(-999);
    private final ModeSetting firstItem = new ModeSetting("First item").value("Shield").values("Shield", "GApple", "Totem", "Ball");
    private final ModeSetting secondItem = new ModeSetting("Second item").value("GApple").values("Shield", "GApple", "Totem", "Ball");
    private boolean swapping = false;

    public ItemSwapModule() {
        this.addSettings(this.swapKey, this.firstItem, this.secondItem);
    }

    @Override
    public void onEvent() {
        EventListener keyEvent = KeyEvent.getInstance().subscribe(new Listener<KeyEvent.KeyEventData>(event -> {
            if (event.key() == ((Integer)this.swapKey.getValue()).intValue() && event.action() == 1 && ItemSwapModule.mc.field_1755 == null) {
                this.swapping = true;
            }
        }));
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener<TickEvent>(event -> {
            if (!SlownessManager.isEnabled()) {
                return;
            }
            this.performSwap();
        }));
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (SlownessManager.isEnabled()) {
                return;
            }
            this.performSwap();
        }));
        this.addEvents(keyEvent, tickEvent, updateEvent);
    }

    private void performSwap() {
        if (ItemSwapModule.mc.field_1687 == null || ItemSwapModule.mc.field_1724 == null || ItemSwapModule.mc.field_1761 == null || !this.swapping) {
            return;
        }
        class_1792 item = this.getItem();
        if (item == null) {
            this.print("\u041f\u0440\u0435\u0434\u043c\u0435\u0442 \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d");
            this.swapping = false;
            return;
        }
        int slot = InventoryUtil.findItem(item);
        if (slot == -1) {
            this.print("\u041f\u0440\u0435\u0434\u043c\u0435\u0442 \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d \u0432 \u0438\u043d\u0432\u0435\u043d\u0442\u0430\u0440\u0435");
            this.swapping = false;
            return;
        }
        if (SlownessManager.isEnabled()) {
            SlownessManager.applySlowness(10L, () -> this.swap(slot));
        } else {
            this.swap(slot);
        }
    }

    private class_1792 getItem() {
        class_1792 primary = this.getItemByMode((String)this.firstItem.getValue());
        class_1792 secondary = this.getItemByMode((String)this.secondItem.getValue());
        return ItemSwapModule.mc.field_1724.method_6079().method_7909() == primary ? secondary : primary;
    }

    private void swap(int slot) {
        if (ItemSwapModule.mc.field_1761 == null) {
            return;
        }
        this.print("\u0421\u0432\u0430\u043f\u043d\u0443\u043b \u043d\u0430 \"" + this.getItem().method_63680().getString() + "\"");
        if (SlownessManager.isEnabled()) {
            SlownessManager.applySlowness(10L, () -> InventoryUtil.swapToOffhand(slot));
        } else {
            InventoryUtil.swapToOffhand(slot);
        }
        this.swapping = false;
    }

    private class_1792 getItemByMode(String name) {
        return switch (name.toLowerCase()) {
            case "shield" -> class_1802.field_8255;
            case "ball" -> class_1802.field_8575;
            case "totem" -> class_1802.field_8288;
            case "gapple" -> class_1802.field_8463;
            default -> null;
        };
    }

    @Generated
    public static ItemSwapModule getInstance() {
        return instance;
    }
}

