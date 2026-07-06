// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import net.minecraft.class_238;
import java.util.List;
import net.minecraft.class_3965;
import net.minecraft.class_2350;
import net.minecraft.class_2382;
import sweetie.leonware.api.utils.rotation.rotations.InstantRotation;
import sweetie.leonware.api.utils.rotation.rotations.LonyGriefRotation;
import sweetie.leonware.api.utils.rotation.manager.RotationMode;
import net.minecraft.class_243;
import sweetie.leonware.api.utils.task.TaskPriority;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.utils.rotation.manager.RotationStrategy;
import sweetie.leonware.client.features.modules.movement.MoveFixModule;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import net.minecraft.class_3966;
import net.minecraft.class_1268;
import net.minecraft.class_1657;
import sweetie.leonware.api.utils.rotation.RaytracingUtil;
import java.util.Iterator;
import net.minecraft.class_1511;
import sweetie.leonware.api.utils.other.SlownessManager;
import sweetie.leonware.api.utils.player.InventoryUtil;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.world.BlockPlaceEvent;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import net.minecraft.class_2338;
import net.minecraft.class_1297;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Auto Explosion", category = Category.COMBAT)
public class AutoExplosionModule extends Module
{
    private static final AutoExplosionModule instance;
    private final ModeSetting aimMode;
    private final SliderSetting distance;
    private final SliderSetting placeDelay;
    private final SliderSetting attackDelay;
    private final MultiBooleanSetting options;
    private final SliderSetting swapBackDelay;
    private final TimerUtil attackTimer;
    private final TimerUtil placeTimer;
    private final TimerUtil swapBackTimer;
    private final TimerUtil rotationTimer;
    private class_1297 crystalEntity;
    private class_2338 obsidianPos;
    private int prevSlot;
    private int currentSlot;
    private int bestSlot;
    private boolean swapBack;
    private Runnable placeRunnable;
    
    public AutoExplosionModule() {
        this.aimMode = new ModeSetting("Aim mode").value("Instant").values("Instant", "Lony Grief");
        this.distance = new SliderSetting("Distance").value(3.0f).range(2.5f, 6.0f).step(0.1f);
        this.placeDelay = new SliderSetting("Place delay").value(2.0f).range(0.0f, 20.0f).step(1.0f);
        this.attackDelay = new SliderSetting("Attack delay").value(5.0f).range(0.0f, 20.0f).step(1.0f);
        this.options = new MultiBooleanSetting("Options").value(new BooleanSetting("Raytrace").value(true), new BooleanSetting("No suicide").value(true), new BooleanSetting("Delayed swap back").value(false));
        this.swapBackDelay = new SliderSetting("Swap back delay").value(5.0f).range(1.0f, 20.0f).step(1.0f).setVisible(() -> this.options.isEnabled("Delayed swap back"));
        this.attackTimer = new TimerUtil();
        this.placeTimer = new TimerUtil();
        this.swapBackTimer = new TimerUtil();
        this.rotationTimer = new TimerUtil();
        this.crystalEntity = null;
        this.obsidianPos = null;
        this.prevSlot = -1;
        this.currentSlot = -1;
        this.bestSlot = -1;
        this.swapBack = false;
        this.placeRunnable = null;
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
        final EventListener blockPlaceEvent = BlockPlaceEvent.getInstance().subscribe(new Listener<BlockPlaceEvent.BlockPlaceEventData>(event -> this.handlePlaceEvent(event)));
        final EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener<TickEvent>(event -> this.handleTickEvent()));
        this.addEvents(tickEvent, blockPlaceEvent);
    }
    
    private void handlePlaceEvent(final BlockPlaceEvent.BlockPlaceEventData event) {
        if (event.state().method_26204() == class_2246.field_10540 || event.state().method_26204() == class_2246.field_9987) {
            this.obsidianPos = event.pos();
            final boolean isOffhand = AutoExplosionModule.mc.field_1724.method_6079().method_7909() == class_1802.field_8301;
            final int slotInv = InventoryUtil.findItem(class_1802.field_8301, false);
            final int slotHb = InventoryUtil.findItem(class_1802.field_8301, true);
            this.bestSlot = InventoryUtil.findEmptySlot();
            if (this.options.isEnabled("Delayed swap back") && AutoExplosionModule.mc.field_1724.method_6047().method_7909() == class_1802.field_8301) {
                this.swapBackTimer.reset();
            }
            if (isOffhand) {
                if (this.obsidianPos != null) {
                    this.placeRunnable = (() -> this.placeCrystal(this.bestSlot, this.obsidianPos));
                    this.placeTimer.reset();
                }
            }
            else if (slotHb == -1 && slotInv != -1 && this.bestSlot != -1) {
                this.placeRunnable = (() -> {
                    if (SlownessManager.isEnabled()) {
                        SlownessManager.applySlowness(10L, () -> this.funnyBabah(slotInv));
                    }
                    else {
                        this.funnyBabah(slotInv);
                    }
                    return;
                });
                this.placeTimer.reset();
            }
            else if (slotHb != -1) {
                this.placeRunnable = (() -> {
                    if (this.obsidianPos == null) {
                        return;
                    }
                    else {
                        this.prevSlot = AutoExplosionModule.mc.field_1724.method_31548().field_7545;
                        this.placeCrystal(slotHb, this.obsidianPos);
                        if (this.options.isEnabled("Delayed swap back")) {
                            this.swapBackTimer.reset();
                            this.swapBack = true;
                            this.currentSlot = AutoExplosionModule.mc.field_1724.method_31548().field_7545;
                        }
                        else {
                            AutoExplosionModule.mc.field_1724.method_31548().field_7545 = this.prevSlot;
                        }
                        return;
                    }
                });
                this.placeTimer.reset();
            }
        }
    }
    
    private void handleTickEvent() {
        if (this.crystalEntity != null && !this.crystalEntity.method_5805()) {
            this.reset();
        }
        if (this.placeRunnable != null && this.placeTimer.finished(this.placeDelay.getValue() * 50.0f)) {
            this.placeRunnable.run();
            this.placeTimer.reset();
            this.placeRunnable = null;
        }
        if (this.obsidianPos != null && this.attackTimer.finished(this.attackDelay.getValue() * 50.0f)) {
            for (final class_1511 crystal : this.findCrystals(this.obsidianPos)) {
                if (this.isValid((class_1297)crystal)) {
                    this.attackCrystal((class_1297)crystal);
                }
            }
        }
        if (this.options.isEnabled("Delayed swap back") && this.swapBack) {
            final int playerCurrentSlot = AutoExplosionModule.mc.field_1724.method_31548().field_7545;
            if (playerCurrentSlot != this.currentSlot && playerCurrentSlot != this.prevSlot) {
                this.swapBack = false;
                return;
            }
            if (this.swapBackTimer.finished(this.swapBackDelay.getValue() * 50.0f)) {
                AutoExplosionModule.mc.field_1724.method_31548().field_7545 = this.prevSlot;
                this.swapBack = false;
            }
        }
    }
    
    private void attackCrystal(final class_1297 entity) {
        if (this.isValid(entity) && AutoExplosionModule.mc.field_1724.method_7261(1.0f) >= 1.0f) {
            final class_3966 hitResult = RaytracingUtil.raytraceEntity(this.distance.getValue(), this.rotate(entity), false);
            final boolean successRaytarce = (hitResult != null && hitResult.method_17782() == entity) || !this.options.isEnabled("Raytrace");
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
    
    private Rotation rotate(final class_1297 entity) {
        final class_243 targetPos = RotationUtil.getSpot(entity);
        final Rotation rotations = RotationUtil.rotationAt(targetPos);
        final RotationStrategy configurable = new RotationStrategy(this.getAimMode(), MoveFixModule.enabled(), MoveFixModule.isFree()).ticksUntilReset(this.attackDelay.getValue().intValue());
        RotationManager.getInstance().addRotation(rotations, configurable, TaskPriority.REQUIRED, this);
        return rotations;
    }
    
    private RotationMode getAimMode() {
        final String s = this.aimMode.getValue();
        return switch (s) {
            case "Lony Grief" -> new LonyGriefRotation();
            default -> new InstantRotation();
        };
    }
    
    private void placeCrystal(final int slot, final class_2338 pos) {
        final boolean isOffhand = AutoExplosionModule.mc.field_1724.method_6079().method_7909() == class_1802.field_8301;
        final class_243 center = class_243.method_24953((class_2382)pos);
        final class_3965 hitResult = new class_3965(center, class_2350.field_11036, pos, false);
        if (isOffhand) {
            if (AutoExplosionModule.mc.field_1761.method_2896(AutoExplosionModule.mc.field_1724, class_1268.field_5810, hitResult).method_23665()) {
                AutoExplosionModule.mc.field_1724.method_6104(class_1268.field_5810);
            }
        }
        else {
            AutoExplosionModule.mc.field_1724.method_31548().field_7545 = slot;
            if (AutoExplosionModule.mc.field_1761.method_2896(AutoExplosionModule.mc.field_1724, class_1268.field_5808, hitResult).method_23665() && AutoExplosionModule.mc.field_1724.method_6047().method_7909() == class_1802.field_8301) {
                AutoExplosionModule.mc.field_1724.method_6104(class_1268.field_5808);
            }
        }
    }
    
    private boolean isValid(final class_1297 entity) {
        return entity != null && this.obsidianPos != null && entity.method_5805() && (!this.options.isEnabled("No suicide") || AutoExplosionModule.mc.field_1724.method_23318() <= this.obsidianPos.method_10264()) && AutoExplosionModule.mc.field_1724.method_33571().method_1022(RotationUtil.getSpot(entity)) < this.distance.getValue();
    }
    
    private List<class_1511> findCrystals(final class_2338 pos) {
        return AutoExplosionModule.mc.field_1687.method_8390((Class)class_1511.class, new class_238(pos).method_1009(1.0, 2.0, 1.0), endCrystalEntity -> endCrystalEntity != null && endCrystalEntity.method_5805());
    }
    
    private void funnyBabah(final int slot) {
        InventoryUtil.swapSlots(slot, this.bestSlot);
        if (this.obsidianPos != null) {
            this.prevSlot = AutoExplosionModule.mc.field_1724.method_31548().field_7545;
            this.placeCrystal(this.bestSlot, this.obsidianPos);
            if (this.options.isEnabled("Delayed swap back")) {
                this.swapBackTimer.reset();
                this.swapBack = true;
                this.currentSlot = AutoExplosionModule.mc.field_1724.method_31548().field_7545;
            }
            else {
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
        return AutoExplosionModule.instance;
    }
    
    static {
        instance = new AutoExplosionModule();
    }
}
