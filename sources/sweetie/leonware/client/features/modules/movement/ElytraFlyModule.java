package sweetie.leonware.client.features.modules.movement;

import java.util.concurrent.ThreadLocalRandom;
import lombok.Generated;
import net.minecraft.class_1304;
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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/ElytraFlyModule.class */
@ModuleRegister(name = "Elytra Fly", category = Category.MOVEMENT)
public class ElytraFlyModule extends Module {
    private static final ElytraFlyModule instance = new ElytraFlyModule();
    private final ModeSetting mode = new ModeSetting("Mode").values("Grim <=1.18.1", "LonyGrief", "GrimGlide").value("Grim <=1.18.1");
    private final BooleanSetting autoSwap = new BooleanSetting("Auto Swap").value((Boolean) true);
    private final SliderSetting lonySpeed = new SliderSetting("LonyGrief Speed").value(Float.valueOf(0.36f)).range(0.1f, 1.0f).step(0.01f).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("LonyGrief"));
    });
    private final SliderSetting grimGlideForwardA = new SliderSetting("GrimGlide Forward A").value(Float.valueOf(0.087f)).range(0.01f, 0.2f).step(0.001f).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("GrimGlide"));
    });
    private final SliderSetting grimGlideForwardB = new SliderSetting("GrimGlide Forward B").value(Float.valueOf(0.09f)).range(0.01f, 0.2f).step(0.001f).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("GrimGlide"));
    });
    private final SliderSetting grimGlideBoostInterval = new SliderSetting("GrimGlide Boost Interval").value(Float.valueOf(40.0f)).range(10.0f, 100.0f).step(1.0f).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("GrimGlide"));
    });
    private long lastTickTime = 0;
    private int ticksTwo = 0;
    private boolean swappedChest = false;

    @Generated
    public static ElytraFlyModule getInstance() {
        return instance;
    }

    /* JADX WARN: Type inference failed for: r1v14, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v19, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v24, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v9, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    public ElytraFlyModule() {
        addSettings(this.mode, this.autoSwap, this.lonySpeed, this.grimGlideForwardA, this.grimGlideForwardB, this.grimGlideBoostInterval);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onEnable() {
        super.onEnable();
        this.swappedChest = false;
        this.ticksTwo = 0;
        this.lastTickTime = System.currentTimeMillis();
        if (mc.field_1724 != null && this.autoSwap.getValue().booleanValue()) {
            int elytraSlot = getItemSlot(class_1802.field_8833);
            if (mc.field_1724.method_6118(class_1304.field_6174).method_7909() != class_1802.field_8833 && elytraSlot != -1) {
                swapToElytra(elytraSlot);
            }
        }
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        int chestplateSlot;
        super.onDisable();
        if (mc.field_1724 != null) {
            mc.field_1690.field_1903.method_23481(false);
            if (this.autoSwap.getValue().booleanValue() && this.swappedChest && mc.field_1724.method_6118(class_1304.field_6174).method_7909() == class_1802.field_8833 && (chestplateSlot = getChestplateSlot()) != -1) {
                swapChestSlot(chestplateSlot);
            }
        }
        this.swappedChest = false;
        this.ticksTwo = 0;
    }

    private boolean isElytraUsable(class_1799 stack) {
        return stack.method_31574(class_1802.field_8833) && stack.method_7919() < stack.method_7936() - 1;
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1724 == null || mc.field_1687 == null) {
                return;
            }
            if (this.mode.is("LonyGrief")) {
                handleLonyGrief();
            } else if (this.mode.is("GrimGlide")) {
                handleGrimGlide();
            } else {
                handleGrimMode();
            }
        }));
        addEvents(updateEvent);
    }

    private void handleGrimMode() {
        int elytraSlot;
        class_1799 chestStack = mc.field_1724.method_6118(class_1304.field_6174);
        boolean hasElytra = chestStack.method_7909() == class_1802.field_8833;
        if (!hasElytra && this.autoSwap.getValue().booleanValue() && (elytraSlot = getItemSlot(class_1802.field_8833)) != -1) {
            swapToElytra(elytraSlot);
            chestStack = mc.field_1724.method_6118(class_1304.field_6174);
            hasElytra = chestStack.method_7909() == class_1802.field_8833;
        }
        if (hasElytra) {
            if (!mc.field_1724.method_31549().field_7479 && mc.field_1724.method_24828() && !mc.field_1724.method_5799() && !mc.field_1724.method_5771() && !mc.field_1690.field_1903.method_1434()) {
                mc.field_1724.method_6043();
            }
            if (!mc.field_1724.method_31549().field_7479 && !mc.field_1724.method_24828() && !mc.field_1724.method_5799() && !mc.field_1724.method_5771() && !mc.field_1724.method_6128() && isElytraUsable(chestStack)) {
                mc.field_1724.method_23669();
                mc.field_1724.field_3944.method_52787(new class_2848(mc.field_1724, class_2848.class_2849.field_12982));
            }
            if (mc.field_1690.field_1903.method_1434() && !mc.field_1724.method_6128()) {
                mc.field_1690.field_1903.method_23481(false);
            }
            mc.field_1690.field_1903.method_23481(true);
            if (mc.field_1724.method_6128()) {
                mc.field_1724.method_18800(0.0d, mc.field_1724.method_18798().field_1351 + 0.02658d, 0.0d);
                AuraModule aura = AuraModule.getInstance();
                if (aura.isEnabled() && aura.target != null) {
                    class_243 dir = aura.target.method_33571().method_1020(mc.field_1724.method_33571()).method_1029();
                    float yaw = (float) (Math.toDegrees(Math.atan2(dir.field_1350, dir.field_1352)) - 90.0d);
                    float pitch = (float) (-Math.toDegrees(Math.atan2(dir.field_1351, Math.sqrt((dir.field_1352 * dir.field_1352) + (dir.field_1350 * dir.field_1350)))));
                    RotationManager.getInstance().addRotation(new Rotation(yaw, pitch), RotationStrategy.TARGET, TaskPriority.HIGH, instance);
                }
            }
        }
    }

    private void handleLonyGrief() {
        class_1799 chestStack = mc.field_1724.method_6118(class_1304.field_6174);
        if (chestStack.method_7909() != class_1802.field_8833) {
            return;
        }
        if (mc.field_1724.method_24828()) {
            mc.field_1724.method_6043();
            mc.field_1724.method_18800(mc.field_1724.method_18798().field_1352, 0.42d, mc.field_1724.method_18798().field_1350);
            mc.field_1724.method_36457(-90.0f);
        } else if (isElytraUsable(chestStack) && !mc.field_1724.method_6128()) {
            mc.field_1724.method_23669();
            sendPacket((class_2596<?>) new class_2848(mc.field_1724, class_2848.class_2849.field_12982));
            mc.field_1724.method_18800(mc.field_1724.method_18798().field_1352, 0.5d, mc.field_1724.method_18798().field_1350);
            mc.field_1724.method_36457(-90.0f);
        }
        mc.field_1724.method_36457(0.0f);
        if (!mc.field_1724.method_24828()) {
            mc.field_1724.method_18800(mc.field_1724.method_18798().field_1352, this.lonySpeed.getValue().floatValue(), mc.field_1724.method_18798().field_1350);
        }
    }

    private void handleGrimGlide() {
        Float value;
        if (mc.field_1724.method_6128()) {
            this.ticksTwo++;
            class_243 pos = mc.field_1724.method_19538();
            float yaw = mc.field_1724.method_36454();
            if (mc.field_1724.field_6012 % 2 == 0) {
                value = this.grimGlideForwardA.getValue();
            } else {
                value = this.grimGlideForwardB.getValue();
            }
            double forward = value.floatValue();
            double dx = (-Math.sin(Math.toRadians(yaw))) * forward;
            double dz = Math.cos(Math.toRadians(yaw)) * forward;
            if (System.currentTimeMillis() - this.lastTickTime >= 40) {
                mc.field_1724.method_5814(pos.method_10216() + dx, pos.method_10214(), pos.method_10215() + dz);
                this.lastTickTime = System.currentTimeMillis();
            }
            int boostInterval = this.grimGlideBoostInterval.getValue().intValue();
            if (this.ticksTwo % boostInterval == 0) {
                mc.field_1724.method_18800(dx * ((double) ThreadLocalRandom.current().nextFloat(1.001f, 1.0021f)), mc.field_1724.method_18798().field_1351 + 0.00600000075995922d, dz * ((double) ThreadLocalRandom.current().nextFloat(1.001f, 1.0021f)));
            }
        }
    }

    private void swapToElytra(int slot) {
        swapChestSlot(slot);
        this.swappedChest = true;
    }

    private void swapChestSlot(int slot) {
        if (mc.field_1724 == null || mc.field_1761 == null) {
            return;
        }
        int syncId = mc.field_1724.field_7512.field_7763;
        if (slot >= 36 && slot <= 44) {
            mc.field_1761.method_2906(syncId, 6, slot % 9, class_1713.field_7791, mc.field_1724);
        } else {
            int hotbarButton = mc.field_1724.method_31548().field_7545;
            mc.field_1761.method_2906(syncId, slot, hotbarButton, class_1713.field_7791, mc.field_1724);
            mc.field_1761.method_2906(syncId, 6, hotbarButton, class_1713.field_7791, mc.field_1724);
            mc.field_1761.method_2906(syncId, slot, hotbarButton, class_1713.field_7791, mc.field_1724);
        }
        mc.field_1724.field_3944.method_52787(new class_2815(syncId));
    }

    private int getItemSlot(class_1792 item) {
        if (mc.field_1724 == null) {
            return -1;
        }
        int i = 0;
        while (i < 36) {
            if (mc.field_1724.method_31548().method_5438(i).method_7909() == item) {
                return i < 9 ? i + 36 : i;
            }
            i++;
        }
        return -1;
    }

    private int getChestplateSlot() {
        class_1792[] chestplates = {class_1802.field_22028, class_1802.field_8058, class_1802.field_8678, class_1802.field_8523, class_1802.field_8873, class_1802.field_8577};
        for (class_1792 item : chestplates) {
            int slot = getItemSlot(item);
            if (slot != -1) {
                return slot;
            }
        }
        return -1;
    }
}
