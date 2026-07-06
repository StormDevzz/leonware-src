/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1268
 *  net.minecraft.class_1297
 *  net.minecraft.class_1511
 *  net.minecraft.class_1657
 *  net.minecraft.class_1802
 *  net.minecraft.class_2246
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_238
 *  net.minecraft.class_2382
 *  net.minecraft.class_243
 *  net.minecraft.class_3965
 *  net.minecraft.class_3966
 */
package sweetie.leonware.client.features.modules.combat;

import java.util.List;
import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1511;
import net.minecraft.class_1657;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_2382;
import net.minecraft.class_243;
import net.minecraft.class_3965;
import net.minecraft.class_3966;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.event.events.player.world.BlockPlaceEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.utils.other.SlownessManager;
import sweetie.leonware.api.utils.player.InventoryUtil;
import sweetie.leonware.api.utils.rotation.RaytracingUtil;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.utils.rotation.manager.RotationMode;
import sweetie.leonware.api.utils.rotation.manager.RotationStrategy;
import sweetie.leonware.api.utils.rotation.rotations.InstantRotation;
import sweetie.leonware.api.utils.rotation.rotations.LonyGriefRotation;
import sweetie.leonware.api.utils.task.TaskPriority;
import sweetie.leonware.client.features.modules.movement.MoveFixModule;

@ModuleRegister(name="Auto Explosion", category=Category.COMBAT)
public class AutoExplosionModule
extends Module {
    private static final AutoExplosionModule instance = new AutoExplosionModule();
    private final ModeSetting aimMode = new ModeSetting("Aim mode").value("Instant").values("Instant", "Lony Grief");
    private final SliderSetting distance = new SliderSetting("Distance").value(Float.valueOf(3.0f)).range(2.5f, 6.0f).step(0.1f);
    private final SliderSetting placeDelay = new SliderSetting("Place delay").value(Float.valueOf(2.0f)).range(0.0f, 20.0f).step(1.0f);
    private final SliderSetting attackDelay = new SliderSetting("Attack delay").value(Float.valueOf(5.0f)).range(0.0f, 20.0f).step(1.0f);
    private final MultiBooleanSetting options = new MultiBooleanSetting("Options").value(new BooleanSetting("Raytrace").value(true), new BooleanSetting("No suicide").value(true), new BooleanSetting("Delayed swap back").value(false));
    private final SliderSetting swapBackDelay = new SliderSetting("Swap back delay").value(Float.valueOf(5.0f)).range(1.0f, 20.0f).step(1.0f).setVisible(() -> this.options.isEnabled("Delayed swap back"));
    private final TimerUtil attackTimer = new TimerUtil();
    private final TimerUtil placeTimer = new TimerUtil();
    private final TimerUtil swapBackTimer = new TimerUtil();
    private final TimerUtil rotationTimer = new TimerUtil();
    private class_1297 crystalEntity = null;
    private class_2338 obsidianPos = null;
    private int prevSlot = -1;
    private int currentSlot = -1;
    private int bestSlot = -1;
    private boolean swapBack = false;
    private Runnable placeRunnable = null;

    public AutoExplosionModule() {
        this.addSettings(this.aimMode, this.distance, this.placeDelay, this.attackDelay, this.options, this.swapBackDelay);
    }

    @Override
    public void onEnable() {
        this.reset();
    }

    @Override
    public void onDisable() {
        this.reset();
    }

    @Override
    public void onEvent() {
        EventListener blockPlaceEvent = BlockPlaceEvent.getInstance().subscribe(new Listener<BlockPlaceEvent.BlockPlaceEventData>(event -> this.handlePlaceEvent((BlockPlaceEvent.BlockPlaceEventData)event)));
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener<TickEvent>(event -> this.handleTickEvent()));
        this.addEvents(tickEvent, blockPlaceEvent);
    }

    private void handlePlaceEvent(BlockPlaceEvent.BlockPlaceEventData event) {
        if (event.state().method_26204() == class_2246.field_10540 || event.state().method_26204() == class_2246.field_9987) {
            this.obsidianPos = event.pos();
            boolean isOffhand = AutoExplosionModule.mc.field_1724.method_6079().method_7909() == class_1802.field_8301;
            int slotInv = InventoryUtil.findItem(class_1802.field_8301, false);
            int slotHb = InventoryUtil.findItem(class_1802.field_8301, true);
            this.bestSlot = InventoryUtil.findEmptySlot();
            if (this.options.isEnabled("Delayed swap back") && AutoExplosionModule.mc.field_1724.method_6047().method_7909() == class_1802.field_8301) {
                this.swapBackTimer.reset();
            }
            if (isOffhand) {
                if (this.obsidianPos != null) {
                    this.placeRunnable = () -> this.placeCrystal(this.bestSlot, this.obsidianPos);
                    this.placeTimer.reset();
                }
            } else if (slotHb == -1 && slotInv != -1 && this.bestSlot != -1) {
                this.placeRunnable = () -> {
                    if (SlownessManager.isEnabled()) {
                        SlownessManager.applySlowness(10L, () -> this.funnyBabah(slotInv));
                    } else {
                        this.funnyBabah(slotInv);
                    }
                };
                this.placeTimer.reset();
            } else if (slotHb != -1) {
                this.placeRunnable = () -> {
                    if (this.obsidianPos == null) {
                        return;
                    }
                    this.prevSlot = AutoExplosionModule.mc.field_1724.method_31548().field_7545;
                    this.placeCrystal(slotHb, this.obsidianPos);
                    if (this.options.isEnabled("Delayed swap back")) {
                        this.swapBackTimer.reset();
                        this.swapBack = true;
                        this.currentSlot = AutoExplosionModule.mc.field_1724.method_31548().field_7545;
                    } else {
                        AutoExplosionModule.mc.field_1724.method_31548().field_7545 = this.prevSlot;
                    }
                };
                this.placeTimer.reset();
            }
        }
    }

    private void handleTickEvent() {
        if (this.crystalEntity != null && !this.crystalEntity.method_5805()) {
            this.reset();
        }
        if (this.placeRunnable != null && this.placeTimer.finished(((Float)this.placeDelay.getValue()).floatValue() * 50.0f)) {
            this.placeRunnable.run();
            this.placeTimer.reset();
            this.placeRunnable = null;
        }
        if (this.obsidianPos != null && this.attackTimer.finished(((Float)this.attackDelay.getValue()).floatValue() * 50.0f)) {
            for (class_1511 crystal : this.findCrystals(this.obsidianPos)) {
                if (!this.isValid((class_1297)crystal)) continue;
                this.attackCrystal((class_1297)crystal);
            }
        }
        if (this.options.isEnabled("Delayed swap back") && this.swapBack) {
            int playerCurrentSlot = AutoExplosionModule.mc.field_1724.method_31548().field_7545;
            if (playerCurrentSlot != this.currentSlot && playerCurrentSlot != this.prevSlot) {
                this.swapBack = false;
                return;
            }
            if (this.swapBackTimer.finished(((Float)this.swapBackDelay.getValue()).floatValue() * 50.0f)) {
                AutoExplosionModule.mc.field_1724.method_31548().field_7545 = this.prevSlot;
                this.swapBack = false;
            }
        }
    }

    private void attackCrystal(class_1297 entity) {
        if (this.isValid(entity) && AutoExplosionModule.mc.field_1724.method_7261(1.0f) >= 1.0f) {
            boolean successRaytarce;
            class_3966 hitResult = RaytracingUtil.raytraceEntity(((Float)this.distance.getValue()).floatValue(), this.rotate(entity), false);
            boolean bl = successRaytarce = hitResult != null && hitResult.method_17782() == entity || !this.options.isEnabled("Raytrace");
            if (successRaytarce) {
                AutoExplosionModule.mc.field_1761.method_2918((class_1657)AutoExplosionModule.mc.field_1724, entity);
                AutoExplosionModule.mc.field_1724.method_6104(class_1268.field_5808);
                this.attackTimer.reset();
                this.crystalEntity = entity;
            }
        }
        if (!entity.method_5805()) {
            this.crystalEntity = null;
            this.obsidianPos = null;
        }
    }

    private Rotation rotate(class_1297 entity) {
        class_243 targetPos = RotationUtil.getSpot(entity);
        Rotation rotations = RotationUtil.rotationAt(targetPos);
        RotationStrategy configurable = new RotationStrategy(this.getAimMode(), MoveFixModule.enabled(), MoveFixModule.isFree()).ticksUntilReset(((Float)this.attackDelay.getValue()).intValue());
        RotationManager.getInstance().addRotation(rotations, configurable, TaskPriority.REQUIRED, this);
        return rotations;
    }

    private RotationMode getAimMode() {
        return switch ((String)this.aimMode.getValue()) {
            case "Lony Grief" -> new LonyGriefRotation();
            default -> new InstantRotation();
        };
    }

    private void placeCrystal(int slot, class_2338 pos) {
        boolean isOffhand = AutoExplosionModule.mc.field_1724.method_6079().method_7909() == class_1802.field_8301;
        class_243 center = class_243.method_24953((class_2382)pos);
        class_3965 hitResult = new class_3965(center, class_2350.field_11036, pos, false);
        if (isOffhand) {
            if (AutoExplosionModule.mc.field_1761.method_2896(AutoExplosionModule.mc.field_1724, class_1268.field_5810, hitResult).method_23665()) {
                AutoExplosionModule.mc.field_1724.method_6104(class_1268.field_5810);
            }
        } else {
            AutoExplosionModule.mc.field_1724.method_31548().field_7545 = slot;
            if (AutoExplosionModule.mc.field_1761.method_2896(AutoExplosionModule.mc.field_1724, class_1268.field_5808, hitResult).method_23665() && AutoExplosionModule.mc.field_1724.method_6047().method_7909() == class_1802.field_8301) {
                AutoExplosionModule.mc.field_1724.method_6104(class_1268.field_5808);
            }
        }
    }

    private boolean isValid(class_1297 entity) {
        if (entity == null || this.obsidianPos == null || !entity.method_5805()) {
            return false;
        }
        if (this.options.isEnabled("No suicide") && AutoExplosionModule.mc.field_1724.method_23318() > (double)this.obsidianPos.method_10264()) {
            return false;
        }
        return AutoExplosionModule.mc.field_1724.method_33571().method_1022(RotationUtil.getSpot(entity)) < (double)((Float)this.distance.getValue()).floatValue();
    }

    private List<class_1511> findCrystals(class_2338 pos) {
        return AutoExplosionModule.mc.field_1687.method_8390(class_1511.class, new class_238(pos).method_1009(1.0, 2.0, 1.0), endCrystalEntity -> endCrystalEntity != null && endCrystalEntity.method_5805());
    }

    private void funnyBabah(int slot) {
        InventoryUtil.swapSlots(slot, this.bestSlot);
        if (this.obsidianPos != null) {
            this.prevSlot = AutoExplosionModule.mc.field_1724.method_31548().field_7545;
            this.placeCrystal(this.bestSlot, this.obsidianPos);
            if (this.options.isEnabled("Delayed swap back")) {
                this.swapBackTimer.reset();
                this.swapBack = true;
                this.currentSlot = AutoExplosionModule.mc.field_1724.method_31548().field_7545;
            } else {
                AutoExplosionModule.mc.field_1724.method_31548().field_7545 = this.prevSlot;
            }
        }
        InventoryUtil.swapSlots(this.bestSlot, slot);
    }

    private void reset() {
        this.crystalEntity = null;
        this.obsidianPos = null;
        this.prevSlot = -1;
        this.bestSlot = -1;
        this.swapBack = false;
        this.currentSlot = -1;
        this.placeTimer.reset();
        this.attackTimer.reset();
        this.swapBackTimer.reset();
        this.rotationTimer.reset();
        this.placeRunnable = null;
    }

    @Generated
    public static AutoExplosionModule getInstance() {
        return instance;
    }
}

