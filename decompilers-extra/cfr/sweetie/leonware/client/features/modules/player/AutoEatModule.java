/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1268
 *  net.minecraft.class_1792
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_1829
 *  net.minecraft.class_2868
 *  net.minecraft.class_9334
 */
package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1829;
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

@ModuleRegister(name="Auto Eat", category=Category.PLAYER)
public class AutoEatModule
extends Module {
    private static final AutoEatModule instance = new AutoEatModule();
    private final SliderSetting hunger = new SliderSetting("Hunger").value(Float.valueOf(14.0f)).range(0.0f, 20.0f).step(1.0f);
    private final MultiBooleanSetting blacklist = new MultiBooleanSetting("Blacklist").value(new BooleanSetting("Golden Apple").value(true), new BooleanSetting("Chorus").value(true), new BooleanSetting("Rotten Flesh").value(true));
    private boolean eating;
    private int prevSlot;

    public AutoEatModule() {
        this.addSettings(this.hunger, this.blacklist);
    }

    @Override
    public void onDisable() {
        if (this.eating) {
            this.stopEating();
        }
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (AutoEatModule.mc.field_1724 == null || AutoEatModule.mc.field_1687 == null) {
                return;
            }
            if (AutoEatModule.mc.field_1724.method_7344().method_7586() <= (int)((Float)this.hunger.getValue()).floatValue()) {
                boolean found = !this.isHandGood(class_1268.field_5808) && !this.isHandGood(class_1268.field_5810) ? this.switchToFood() : true;
                if (!found) {
                    if (this.eating) {
                        this.stopEating();
                    }
                    return;
                }
                this.startEating();
            } else if (this.eating) {
                this.stopEating();
            }
        }));
        this.addEvents(updateEvent);
    }

    private void startEating() {
        this.eating = true;
        AutoEatModule.mc.field_1690.field_1904.method_23481(true);
    }

    private void stopEating() {
        this.eating = false;
        AutoEatModule.mc.field_1690.field_1904.method_23481(false);
        if (AutoEatModule.mc.field_1724.method_31548().field_7545 != this.prevSlot) {
            AutoEatModule.mc.field_1724.method_31548().field_7545 = this.prevSlot;
            NetworkUtil.sendPacket(new class_2868(this.prevSlot));
        }
    }

    private boolean switchToFood() {
        class_1799 stack;
        int i;
        for (i = 0; i < 9; ++i) {
            stack = AutoEatModule.mc.field_1724.method_31548().method_5438(i);
            if (!this.isValidFood(stack)) continue;
            this.prevSlot = AutoEatModule.mc.field_1724.method_31548().field_7545;
            if (AutoEatModule.mc.field_1724.method_31548().field_7545 != i) {
                AutoEatModule.mc.field_1724.method_31548().field_7545 = i;
                NetworkUtil.sendPacket(new class_2868(i));
            }
            return true;
        }
        for (i = 9; i < 36; ++i) {
            stack = AutoEatModule.mc.field_1724.method_31548().method_5438(i);
            if (!this.isValidFood(stack)) continue;
            int targetSlot = this.findNonSwordHotbarSlot();
            if (targetSlot == -1) {
                return false;
            }
            this.prevSlot = AutoEatModule.mc.field_1724.method_31548().field_7545;
            InventoryUtil.swapSlots(i, targetSlot);
            if (AutoEatModule.mc.field_1724.method_31548().field_7545 != targetSlot) {
                AutoEatModule.mc.field_1724.method_31548().field_7545 = targetSlot;
                NetworkUtil.sendPacket(new class_2868(targetSlot));
            }
            return true;
        }
        return false;
    }

    private boolean isHandGood(class_1268 hand) {
        class_1799 stack = hand == class_1268.field_5808 ? AutoEatModule.mc.field_1724.method_6047() : AutoEatModule.mc.field_1724.method_6079();
        return this.isValidFood(stack);
    }

    private boolean isValidFood(class_1799 stack) {
        if (stack.method_7960()) {
            return false;
        }
        if (!stack.method_57353().method_57832(class_9334.field_50075)) {
            return false;
        }
        class_1792 item = stack.method_7909();
        if (this.blacklist.isEnabled("Golden Apple") && (item == class_1802.field_8463 || item == class_1802.field_8367)) {
            return false;
        }
        if (this.blacklist.isEnabled("Chorus") && item == class_1802.field_8233) {
            return false;
        }
        return !this.blacklist.isEnabled("Rotten Flesh") || item != class_1802.field_8511;
    }

    private int findNonSwordHotbarSlot() {
        int current = AutoEatModule.mc.field_1724.method_31548().field_7545;
        for (int i = 0; i < 9; ++i) {
            class_1799 stack;
            if (i == current || (stack = AutoEatModule.mc.field_1724.method_31548().method_5438(i)).method_7909() instanceof class_1829) continue;
            return i;
        }
        return -1;
    }

    @Generated
    public static AutoEatModule getInstance() {
        return instance;
    }
}

