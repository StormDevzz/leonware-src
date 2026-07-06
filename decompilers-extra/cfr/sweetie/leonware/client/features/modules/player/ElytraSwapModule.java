/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1792
 *  net.minecraft.class_1802
 */
package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import net.minecraft.class_1792;
import net.minecraft.class_1802;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BindSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.system.backend.KeyStorage;
import sweetie.leonware.api.utils.other.SlownessManager;
import sweetie.leonware.api.utils.player.InventoryUtil;

@ModuleRegister(name="Elytra Swap", category=Category.PLAYER)
public class ElytraSwapModule
extends Module {
    private static final ElytraSwapModule instance = new ElytraSwapModule();
    private final BindSetting swapKey = new BindSetting("Swap key").value(-999);
    private final BindSetting launchKey = new BindSetting("Launch key").value(-999);
    private final BooleanSetting legit = new BooleanSetting("Legit").value(false).setVisible(() -> (Integer)this.launchKey.getValue() != -999);
    private final InventoryUtil.ItemUsage itemUsage = new InventoryUtil.ItemUsage(class_1802.field_8639, this);
    private boolean swapUsed = false;

    public ElytraSwapModule() {
        this.addSettings(this.swapKey, this.launchKey, this.legit);
        this.itemUsage.setUseRotation(false);
    }

    @Override
    public void onEvent() {
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener<TickEvent>(event -> this.handleMainLogic(!SlownessManager.isEnabled())));
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> this.handleMainLogic(SlownessManager.isEnabled())));
        this.addEvents(tickEvent, updateEvent);
    }

    private void handleMainLogic(boolean slow) {
        this.handleFireworkLaunch(slow);
        this.handleChestplateSwap(slow);
    }

    public void handleFireworkLaunch(boolean tick) {
        if (tick || !ElytraSwapModule.mc.field_1724.method_6128()) {
            return;
        }
        this.itemUsage.handleUse((Integer)this.launchKey.getValue(), (Boolean)this.legit.getValue());
    }

    public void handleChestplateSwap(boolean tick) {
        if (tick) {
            return;
        }
        if (KeyStorage.isPressed((Integer)this.swapKey.getValue())) {
            if (!this.swapUsed && ElytraSwapModule.mc.field_1755 == null) {
                if (this.slots() == -1) {
                    return;
                }
                if (SlownessManager.isEnabled()) {
                    SlownessManager.applySlowness(10L, () -> {
                        this.swapChestplate();
                        this.swapUsed = true;
                    });
                } else {
                    this.swapChestplate();
                    this.swapUsed = true;
                }
            }
        } else {
            this.swapUsed = false;
        }
    }

    public void swapChestplate() {
        if (ElytraSwapModule.mc.field_1724 == null || ElytraSwapModule.mc.field_1761 == null) {
            return;
        }
        if (InventoryUtil.hasElytraEquipped()) {
            int slot = this.slots();
            if (slot != -1) {
                if (slot >= 0 && slot <= 8) {
                    InventoryUtil.swapSlotsFull(6, slot);
                } else if (slot >= 36 && slot <= 44) {
                    int hotbarSlot = slot - 36;
                    InventoryUtil.swapSlotsFull(6, hotbarSlot);
                } else {
                    int emptySlot = InventoryUtil.findEmptySlot();
                    if (emptySlot == -1) {
                        emptySlot = InventoryUtil.findBestSlotInHotBar();
                    }
                    if (emptySlot != -1) {
                        InventoryUtil.swapSlots(slot, emptySlot);
                        InventoryUtil.swapSlotsFull(6, emptySlot);
                        InventoryUtil.swapSlots(slot, emptySlot);
                    }
                }
            }
        } else {
            int slot = this.slots();
            if (slot != -1) {
                if (slot >= 0 && slot <= 8) {
                    InventoryUtil.swapSlotsFull(6, slot);
                } else if (slot >= 36 && slot <= 44) {
                    int hotbarSlot = slot - 36;
                    InventoryUtil.swapSlotsFull(6, hotbarSlot);
                } else {
                    int emptySlot = InventoryUtil.findEmptySlot();
                    if (emptySlot == -1) {
                        emptySlot = InventoryUtil.findBestSlotInHotBar();
                    }
                    if (emptySlot != -1) {
                        InventoryUtil.swapSlots(slot, emptySlot);
                        InventoryUtil.swapSlotsFull(6, emptySlot);
                        InventoryUtil.swapSlots(slot, emptySlot);
                    }
                }
            }
        }
    }

    private int findBestSlotFor(class_1792 ... items) {
        for (class_1792 item : items) {
            int slot = InventoryUtil.findItem(item);
            if (slot == -1) continue;
            return slot;
        }
        return -1;
    }

    public int slots() {
        return InventoryUtil.hasElytraEquipped() ? this.findChestplateSlot() : this.findElytraSlot();
    }

    public int findElytraSlot() {
        return this.findBestSlotFor(class_1802.field_8833);
    }

    public int findChestplateSlot() {
        return this.findBestSlotFor(class_1802.field_22028, class_1802.field_8058, class_1802.field_8523, class_1802.field_8678, class_1802.field_8873, class_1802.field_8577);
    }

    @Generated
    public static ElytraSwapModule getInstance() {
        return instance;
    }
}

