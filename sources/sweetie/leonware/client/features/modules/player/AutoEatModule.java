package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1829;
import net.minecraft.class_2596;
import net.minecraft.class_2868;
import net.minecraft.class_9334;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.other.NetworkUtil;
import sweetie.leonware.api.utils.player.InventoryUtil;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/player/AutoEatModule.class */
@ModuleRegister(name = "Auto Eat", category = Category.PLAYER)
public class AutoEatModule extends Module {
    private static final AutoEatModule instance = new AutoEatModule();
    private final SliderSetting hunger = new SliderSetting("Hunger").value(Float.valueOf(14.0f)).range(0.0f, 20.0f).step(1.0f);
    private final MultiBooleanSetting blacklist = new MultiBooleanSetting("Blacklist").value(new BooleanSetting("Golden Apple").value((Boolean) true), new BooleanSetting("Chorus").value((Boolean) true), new BooleanSetting("Rotten Flesh").value((Boolean) true));
    private boolean eating;
    private int prevSlot;

    @Generated
    public static AutoEatModule getInstance() {
        return instance;
    }

    public AutoEatModule() {
        addSettings(this.hunger, this.blacklist);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        if (this.eating) {
            stopEating();
        }
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            boolean found;
            if (mc.field_1724 == null || mc.field_1687 == null) {
                return;
            }
            if (mc.field_1724.method_7344().method_7586() > ((int) this.hunger.getValue().floatValue())) {
                if (this.eating) {
                    stopEating();
                    return;
                }
                return;
            }
            if (!isHandGood(class_1268.field_5808) && !isHandGood(class_1268.field_5810)) {
                found = switchToFood();
            } else {
                found = true;
            }
            if (!found) {
                if (this.eating) {
                    stopEating();
                    return;
                }
                return;
            }
            startEating();
        }));
        addEvents(updateEvent);
    }

    private void startEating() {
        this.eating = true;
        mc.field_1690.field_1904.method_23481(true);
    }

    private void stopEating() {
        this.eating = false;
        mc.field_1690.field_1904.method_23481(false);
        if (mc.field_1724.method_31548().field_7545 != this.prevSlot) {
            mc.field_1724.method_31548().field_7545 = this.prevSlot;
            NetworkUtil.sendPacket((class_2596<?>) new class_2868(this.prevSlot));
        }
    }

    private boolean switchToFood() {
        for (int i = 0; i < 9; i++) {
            class_1799 stack = mc.field_1724.method_31548().method_5438(i);
            if (isValidFood(stack)) {
                this.prevSlot = mc.field_1724.method_31548().field_7545;
                if (mc.field_1724.method_31548().field_7545 != i) {
                    mc.field_1724.method_31548().field_7545 = i;
                    NetworkUtil.sendPacket((class_2596<?>) new class_2868(i));
                    return true;
                }
                return true;
            }
        }
        for (int i2 = 9; i2 < 36; i2++) {
            class_1799 stack2 = mc.field_1724.method_31548().method_5438(i2);
            if (isValidFood(stack2)) {
                int targetSlot = findNonSwordHotbarSlot();
                if (targetSlot == -1) {
                    return false;
                }
                this.prevSlot = mc.field_1724.method_31548().field_7545;
                InventoryUtil.swapSlots(i2, targetSlot);
                if (mc.field_1724.method_31548().field_7545 != targetSlot) {
                    mc.field_1724.method_31548().field_7545 = targetSlot;
                    NetworkUtil.sendPacket((class_2596<?>) new class_2868(targetSlot));
                    return true;
                }
                return true;
            }
        }
        return false;
    }

    private boolean isHandGood(class_1268 hand) {
        class_1799 stack = hand == class_1268.field_5808 ? mc.field_1724.method_6047() : mc.field_1724.method_6079();
        return isValidFood(stack);
    }

    private boolean isValidFood(class_1799 stack) {
        if (stack.method_7960() || !stack.method_57353().method_57832(class_9334.field_50075)) {
            return false;
        }
        class_1792 item = stack.method_7909();
        if (this.blacklist.isEnabled("Golden Apple") && (item == class_1802.field_8463 || item == class_1802.field_8367)) {
            return false;
        }
        if (this.blacklist.isEnabled("Chorus") && item == class_1802.field_8233) {
            return false;
        }
        return (this.blacklist.isEnabled("Rotten Flesh") && item == class_1802.field_8511) ? false : true;
    }

    private int findNonSwordHotbarSlot() {
        int current = mc.field_1724.method_31548().field_7545;
        for (int i = 0; i < 9; i++) {
            if (i != current) {
                class_1799 stack = mc.field_1724.method_31548().method_5438(i);
                if (!(stack.method_7909() instanceof class_1829)) {
                    return i;
                }
            }
        }
        return -1;
    }
}
