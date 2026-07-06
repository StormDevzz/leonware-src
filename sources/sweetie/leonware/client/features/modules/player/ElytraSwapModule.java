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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/player/ElytraSwapModule.class */
@ModuleRegister(name = "Elytra Swap", category = Category.PLAYER)
public class ElytraSwapModule extends Module {
    private static final ElytraSwapModule instance = new ElytraSwapModule();
    private final BindSetting swapKey = new BindSetting("Swap key").value((Integer) (-999));
    private final BindSetting launchKey = new BindSetting("Launch key").value((Integer) (-999));
    private final BooleanSetting legit = new BooleanSetting("Legit").value((Boolean) false).setVisible(() -> {
        return Boolean.valueOf(this.launchKey.getValue().intValue() != -999);
    });
    private final InventoryUtil.ItemUsage itemUsage = new InventoryUtil.ItemUsage(class_1802.field_8639, this);
    private boolean swapUsed = false;

    @Generated
    public static ElytraSwapModule getInstance() {
        return instance;
    }

    /* JADX WARN: Type inference failed for: r1v6, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    public ElytraSwapModule() {
        addSettings(this.swapKey, this.launchKey, this.legit);
        this.itemUsage.setUseRotation(false);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener(event -> {
            handleMainLogic(!SlownessManager.isEnabled());
        }));
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event2 -> {
            handleMainLogic(SlownessManager.isEnabled());
        }));
        addEvents(tickEvent, updateEvent);
    }

    private void handleMainLogic(boolean slow) {
        handleFireworkLaunch(slow);
        handleChestplateSwap(slow);
    }

    public void handleFireworkLaunch(boolean tick) {
        if (tick || !mc.field_1724.method_6128()) {
            return;
        }
        this.itemUsage.handleUse(this.launchKey.getValue().intValue(), this.legit.getValue().booleanValue());
    }

    public void handleChestplateSwap(boolean tick) {
        if (tick) {
            return;
        }
        if (KeyStorage.isPressed(this.swapKey.getValue().intValue())) {
            if (this.swapUsed || mc.field_1755 != null || slots() == -1) {
                return;
            }
            if (SlownessManager.isEnabled()) {
                SlownessManager.applySlowness(10L, () -> {
                    swapChestplate();
                    this.swapUsed = true;
                });
                return;
            } else {
                swapChestplate();
                this.swapUsed = true;
                return;
            }
        }
        this.swapUsed = false;
    }

    public void swapChestplate() {
        if (mc.field_1724 == null || mc.field_1761 == null) {
            return;
        }
        if (InventoryUtil.hasElytraEquipped()) {
            int slot = slots();
            if (slot != -1) {
                if (slot >= 0 && slot <= 8) {
                    InventoryUtil.swapSlotsFull(6, slot);
                    return;
                }
                if (slot >= 36 && slot <= 44) {
                    int hotbarSlot = slot - 36;
                    InventoryUtil.swapSlotsFull(6, hotbarSlot);
                    return;
                }
                int emptySlot = InventoryUtil.findEmptySlot();
                if (emptySlot == -1) {
                    emptySlot = InventoryUtil.findBestSlotInHotBar();
                }
                if (emptySlot != -1) {
                    InventoryUtil.swapSlots(slot, emptySlot);
                    InventoryUtil.swapSlotsFull(6, emptySlot);
                    InventoryUtil.swapSlots(slot, emptySlot);
                    return;
                }
                return;
            }
            return;
        }
        int slot2 = slots();
        if (slot2 != -1) {
            if (slot2 >= 0 && slot2 <= 8) {
                InventoryUtil.swapSlotsFull(6, slot2);
                return;
            }
            if (slot2 >= 36 && slot2 <= 44) {
                int hotbarSlot2 = slot2 - 36;
                InventoryUtil.swapSlotsFull(6, hotbarSlot2);
                return;
            }
            int emptySlot2 = InventoryUtil.findEmptySlot();
            if (emptySlot2 == -1) {
                emptySlot2 = InventoryUtil.findBestSlotInHotBar();
            }
            if (emptySlot2 != -1) {
                InventoryUtil.swapSlots(slot2, emptySlot2);
                InventoryUtil.swapSlotsFull(6, emptySlot2);
                InventoryUtil.swapSlots(slot2, emptySlot2);
            }
        }
    }

    private int findBestSlotFor(class_1792... items) {
        for (class_1792 item : items) {
            int slot = InventoryUtil.findItem(item);
            if (slot != -1) {
                return slot;
            }
        }
        return -1;
    }

    public int slots() {
        return InventoryUtil.hasElytraEquipped() ? findChestplateSlot() : findElytraSlot();
    }

    public int findElytraSlot() {
        return findBestSlotFor(class_1802.field_8833);
    }

    public int findChestplateSlot() {
        return findBestSlotFor(class_1802.field_22028, class_1802.field_8058, class_1802.field_8523, class_1802.field_8678, class_1802.field_8873, class_1802.field_8577);
    }
}
