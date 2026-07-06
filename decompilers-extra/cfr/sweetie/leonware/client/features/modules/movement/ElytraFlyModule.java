/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1297
 *  net.minecraft.class_1304
 *  net.minecraft.class_1657
 *  net.minecraft.class_1713
 *  net.minecraft.class_1792
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_243
 *  net.minecraft.class_2596
 *  net.minecraft.class_2815
 *  net.minecraft.class_2848
 *  net.minecraft.class_2848$class_2849
 */
package sweetie.leonware.client.features.modules.movement;

import java.util.concurrent.ThreadLocalRandom;
import lombok.Generated;
import net.minecraft.class_1297;
import net.minecraft.class_1304;
import net.minecraft.class_1657;
import net.minecraft.class_1713;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2815;
import net.minecraft.class_2848;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.utils.rotation.manager.RotationStrategy;
import sweetie.leonware.api.utils.task.TaskPriority;
import sweetie.leonware.client.features.modules.combat.AuraModule;

@ModuleRegister(name="Elytra Fly", category=Category.MOVEMENT)
public class ElytraFlyModule
extends Module {
    private static final ElytraFlyModule instance = new ElytraFlyModule();
    private final ModeSetting mode = new ModeSetting("Mode").values("Grim <=1.18.1", "LonyGrief", "GrimGlide").value("Grim <=1.18.1");
    private final BooleanSetting autoSwap = new BooleanSetting("Auto Swap").value(true);
    private final SliderSetting lonySpeed = new SliderSetting("LonyGrief Speed").value(Float.valueOf(0.36f)).range(0.1f, 1.0f).step(0.01f).setVisible(() -> this.mode.is("LonyGrief"));
    private final SliderSetting grimGlideForwardA = new SliderSetting("GrimGlide Forward A").value(Float.valueOf(0.087f)).range(0.01f, 0.2f).step(0.001f).setVisible(() -> this.mode.is("GrimGlide"));
    private final SliderSetting grimGlideForwardB = new SliderSetting("GrimGlide Forward B").value(Float.valueOf(0.09f)).range(0.01f, 0.2f).step(0.001f).setVisible(() -> this.mode.is("GrimGlide"));
    private final SliderSetting grimGlideBoostInterval = new SliderSetting("GrimGlide Boost Interval").value(Float.valueOf(40.0f)).range(10.0f, 100.0f).step(1.0f).setVisible(() -> this.mode.is("GrimGlide"));
    private long lastTickTime = 0L;
    private int ticksTwo = 0;
    private boolean swappedChest = false;

    public ElytraFlyModule() {
        this.addSettings(this.mode, this.autoSwap, this.lonySpeed, this.grimGlideForwardA, this.grimGlideForwardB, this.grimGlideBoostInterval);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.swappedChest = false;
        this.ticksTwo = 0;
        this.lastTickTime = System.currentTimeMillis();
        if (ElytraFlyModule.mc.field_1724 != null && ((Boolean)this.autoSwap.getValue()).booleanValue()) {
            int elytraSlot = this.getItemSlot(class_1802.field_8833);
            if (ElytraFlyModule.mc.field_1724.method_6118(class_1304.field_6174).method_7909() != class_1802.field_8833 && elytraSlot != -1) {
                this.swapToElytra(elytraSlot);
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (ElytraFlyModule.mc.field_1724 != null) {
            int chestplateSlot;
            ElytraFlyModule.mc.field_1690.field_1903.method_23481(false);
            if (((Boolean)this.autoSwap.getValue()).booleanValue() && this.swappedChest && ElytraFlyModule.mc.field_1724.method_6118(class_1304.field_6174).method_7909() == class_1802.field_8833 && (chestplateSlot = this.getChestplateSlot()) != -1) {
                this.swapChestSlot(chestplateSlot);
            }
        }
        this.swappedChest = false;
        this.ticksTwo = 0;
    }

    private boolean isElytraUsable(class_1799 stack) {
        return stack.method_31574(class_1802.field_8833) && stack.method_7919() < stack.method_7936() - 1;
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (ElytraFlyModule.mc.field_1724 == null || ElytraFlyModule.mc.field_1687 == null) {
                return;
            }
            if (this.mode.is("LonyGrief")) {
                this.handleLonyGrief();
                return;
            }
            if (this.mode.is("GrimGlide")) {
                this.handleGrimGlide();
                return;
            }
            this.handleGrimMode();
        }));
        this.addEvents(updateEvent);
    }

    private void handleGrimMode() {
        int elytraSlot;
        boolean hasElytra;
        class_1799 chestStack = ElytraFlyModule.mc.field_1724.method_6118(class_1304.field_6174);
        boolean bl = hasElytra = chestStack.method_7909() == class_1802.field_8833;
        if (!hasElytra && ((Boolean)this.autoSwap.getValue()).booleanValue() && (elytraSlot = this.getItemSlot(class_1802.field_8833)) != -1) {
            this.swapToElytra(elytraSlot);
            chestStack = ElytraFlyModule.mc.field_1724.method_6118(class_1304.field_6174);
            boolean bl2 = hasElytra = chestStack.method_7909() == class_1802.field_8833;
        }
        if (!hasElytra) {
            return;
        }
        if (!(ElytraFlyModule.mc.field_1724.method_31549().field_7479 || !ElytraFlyModule.mc.field_1724.method_24828() || ElytraFlyModule.mc.field_1724.method_5799() || ElytraFlyModule.mc.field_1724.method_5771() || ElytraFlyModule.mc.field_1690.field_1903.method_1434())) {
            ElytraFlyModule.mc.field_1724.method_6043();
        }
        if (!(ElytraFlyModule.mc.field_1724.method_31549().field_7479 || ElytraFlyModule.mc.field_1724.method_24828() || ElytraFlyModule.mc.field_1724.method_5799() || ElytraFlyModule.mc.field_1724.method_5771() || ElytraFlyModule.mc.field_1724.method_6128() || !this.isElytraUsable(chestStack))) {
            ElytraFlyModule.mc.field_1724.method_23669();
            ElytraFlyModule.mc.field_1724.field_3944.method_52787((class_2596)new class_2848((class_1297)ElytraFlyModule.mc.field_1724, class_2848.class_2849.field_12982));
        }
        if (ElytraFlyModule.mc.field_1690.field_1903.method_1434() && !ElytraFlyModule.mc.field_1724.method_6128()) {
            ElytraFlyModule.mc.field_1690.field_1903.method_23481(false);
        }
        ElytraFlyModule.mc.field_1690.field_1903.method_23481(true);
        if (!ElytraFlyModule.mc.field_1724.method_6128()) {
            return;
        }
        ElytraFlyModule.mc.field_1724.method_18800(0.0, ElytraFlyModule.mc.field_1724.method_18798().field_1351 + 0.02658, 0.0);
        AuraModule aura = AuraModule.getInstance();
        if (aura.isEnabled() && aura.target != null) {
            class_243 dir = aura.target.method_33571().method_1020(ElytraFlyModule.mc.field_1724.method_33571()).method_1029();
            float yaw = (float)(Math.toDegrees(Math.atan2(dir.field_1350, dir.field_1352)) - 90.0);
            float pitch = (float)(-Math.toDegrees(Math.atan2(dir.field_1351, Math.sqrt(dir.field_1352 * dir.field_1352 + dir.field_1350 * dir.field_1350))));
            RotationManager.getInstance().addRotation(new Rotation(yaw, pitch), RotationStrategy.TARGET, TaskPriority.HIGH, instance);
        }
    }

    private void handleLonyGrief() {
        class_1799 chestStack = ElytraFlyModule.mc.field_1724.method_6118(class_1304.field_6174);
        if (chestStack.method_7909() != class_1802.field_8833) {
            return;
        }
        if (ElytraFlyModule.mc.field_1724.method_24828()) {
            ElytraFlyModule.mc.field_1724.method_6043();
            ElytraFlyModule.mc.field_1724.method_18800(ElytraFlyModule.mc.field_1724.method_18798().field_1352, 0.42, ElytraFlyModule.mc.field_1724.method_18798().field_1350);
            ElytraFlyModule.mc.field_1724.method_36457(-90.0f);
        } else if (this.isElytraUsable(chestStack) && !ElytraFlyModule.mc.field_1724.method_6128()) {
            ElytraFlyModule.mc.field_1724.method_23669();
            this.sendPacket((class_2596<?>)new class_2848((class_1297)ElytraFlyModule.mc.field_1724, class_2848.class_2849.field_12982));
            ElytraFlyModule.mc.field_1724.method_18800(ElytraFlyModule.mc.field_1724.method_18798().field_1352, 0.5, ElytraFlyModule.mc.field_1724.method_18798().field_1350);
            ElytraFlyModule.mc.field_1724.method_36457(-90.0f);
        }
        ElytraFlyModule.mc.field_1724.method_36457(0.0f);
        if (!ElytraFlyModule.mc.field_1724.method_24828()) {
            ElytraFlyModule.mc.field_1724.method_18800(ElytraFlyModule.mc.field_1724.method_18798().field_1352, (double)((Float)this.lonySpeed.getValue()).floatValue(), ElytraFlyModule.mc.field_1724.method_18798().field_1350);
        }
    }

    private void handleGrimGlide() {
        int boostInterval;
        if (!ElytraFlyModule.mc.field_1724.method_6128()) {
            return;
        }
        ++this.ticksTwo;
        class_243 pos = ElytraFlyModule.mc.field_1724.method_19538();
        float yaw = ElytraFlyModule.mc.field_1724.method_36454();
        double forward = (ElytraFlyModule.mc.field_1724.field_6012 % 2 == 0 ? (Float)this.grimGlideForwardA.getValue() : (Float)this.grimGlideForwardB.getValue()).floatValue();
        double dx = -Math.sin(Math.toRadians(yaw)) * forward;
        double dz = Math.cos(Math.toRadians(yaw)) * forward;
        if (System.currentTimeMillis() - this.lastTickTime >= 40L) {
            ElytraFlyModule.mc.field_1724.method_5814(pos.method_10216() + dx, pos.method_10214(), pos.method_10215() + dz);
            this.lastTickTime = System.currentTimeMillis();
        }
        if (this.ticksTwo % (boostInterval = ((Float)this.grimGlideBoostInterval.getValue()).intValue()) == 0) {
            ElytraFlyModule.mc.field_1724.method_18800(dx * (double)ThreadLocalRandom.current().nextFloat(1.001f, 1.0021f), ElytraFlyModule.mc.field_1724.method_18798().field_1351 + 0.00600000075995922, dz * (double)ThreadLocalRandom.current().nextFloat(1.001f, 1.0021f));
        }
    }

    private void swapToElytra(int slot) {
        this.swapChestSlot(slot);
        this.swappedChest = true;
    }

    private void swapChestSlot(int slot) {
        if (ElytraFlyModule.mc.field_1724 == null || ElytraFlyModule.mc.field_1761 == null) {
            return;
        }
        int syncId = ElytraFlyModule.mc.field_1724.field_7512.field_7763;
        if (slot >= 36 && slot <= 44) {
            ElytraFlyModule.mc.field_1761.method_2906(syncId, 6, slot % 9, class_1713.field_7791, (class_1657)ElytraFlyModule.mc.field_1724);
        } else {
            int hotbarButton = ElytraFlyModule.mc.field_1724.method_31548().field_7545;
            ElytraFlyModule.mc.field_1761.method_2906(syncId, slot, hotbarButton, class_1713.field_7791, (class_1657)ElytraFlyModule.mc.field_1724);
            ElytraFlyModule.mc.field_1761.method_2906(syncId, 6, hotbarButton, class_1713.field_7791, (class_1657)ElytraFlyModule.mc.field_1724);
            ElytraFlyModule.mc.field_1761.method_2906(syncId, slot, hotbarButton, class_1713.field_7791, (class_1657)ElytraFlyModule.mc.field_1724);
        }
        ElytraFlyModule.mc.field_1724.field_3944.method_52787((class_2596)new class_2815(syncId));
    }

    private int getItemSlot(class_1792 item) {
        if (ElytraFlyModule.mc.field_1724 == null) {
            return -1;
        }
        for (int i = 0; i < 36; ++i) {
            if (ElytraFlyModule.mc.field_1724.method_31548().method_5438(i).method_7909() != item) continue;
            return i < 9 ? i + 36 : i;
        }
        return -1;
    }

    private int getChestplateSlot() {
        class_1792[] chestplates;
        for (class_1792 item : chestplates = new class_1792[]{class_1802.field_22028, class_1802.field_8058, class_1802.field_8678, class_1802.field_8523, class_1802.field_8873, class_1802.field_8577}) {
            int slot = this.getItemSlot(item);
            if (slot == -1) continue;
            return slot;
        }
        return -1;
    }

    @Generated
    public static ElytraFlyModule getInstance() {
        return instance;
    }
}

