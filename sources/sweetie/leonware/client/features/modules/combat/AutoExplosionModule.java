package sweetie.leonware.client.features.modules.combat;

import java.util.List;
import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1511;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/AutoExplosionModule.class */
@ModuleRegister(name = "Auto Explosion", category = Category.COMBAT)
public class AutoExplosionModule extends Module {
    private static final AutoExplosionModule instance = new AutoExplosionModule();
    private final ModeSetting aimMode = new ModeSetting("Aim mode").value("Instant").values("Instant", "Lony Grief");
    private final SliderSetting distance = new SliderSetting("Distance").value(Float.valueOf(3.0f)).range(2.5f, 6.0f).step(0.1f);
    private final SliderSetting placeDelay = new SliderSetting("Place delay").value(Float.valueOf(2.0f)).range(0.0f, 20.0f).step(1.0f);
    private final SliderSetting attackDelay = new SliderSetting("Attack delay").value(Float.valueOf(5.0f)).range(0.0f, 20.0f).step(1.0f);
    private final MultiBooleanSetting options = new MultiBooleanSetting("Options").value(new BooleanSetting("Raytrace").value((Boolean) true), new BooleanSetting("No suicide").value((Boolean) true), new BooleanSetting("Delayed swap back").value((Boolean) false));
    private final SliderSetting swapBackDelay = new SliderSetting("Swap back delay").value(Float.valueOf(5.0f)).range(1.0f, 20.0f).step(1.0f).setVisible(() -> {
        return Boolean.valueOf(this.options.isEnabled("Delayed swap back"));
    });
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

    @Generated
    public static AutoExplosionModule getInstance() {
        return instance;
    }

    /* JADX WARN: Type inference failed for: r1v21, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    public AutoExplosionModule() {
        addSettings(this.aimMode, this.distance, this.placeDelay, this.attackDelay, this.options, this.swapBackDelay);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onEnable() {
        reset();
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        reset();
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener blockPlaceEvent = BlockPlaceEvent.getInstance().subscribe(new Listener(event -> {
            handlePlaceEvent(event);
        }));
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener(event2 -> {
            handleTickEvent();
        }));
        addEvents(tickEvent, blockPlaceEvent);
    }

    private void handlePlaceEvent(BlockPlaceEvent.BlockPlaceEventData event) {
        if (event.state().method_26204() == class_2246.field_10540 || event.state().method_26204() == class_2246.field_9987) {
            this.obsidianPos = event.pos();
            boolean isOffhand = mc.field_1724.method_6079().method_7909() == class_1802.field_8301;
            int slotInv = InventoryUtil.findItem(class_1802.field_8301, false);
            int slotHb = InventoryUtil.findItem(class_1802.field_8301, true);
            this.bestSlot = InventoryUtil.findEmptySlot();
            if (this.options.isEnabled("Delayed swap back") && mc.field_1724.method_6047().method_7909() == class_1802.field_8301) {
                this.swapBackTimer.reset();
            }
            if (isOffhand) {
                if (this.obsidianPos != null) {
                    this.placeRunnable = () -> {
                        placeCrystal(this.bestSlot, this.obsidianPos);
                    };
                    this.placeTimer.reset();
                    return;
                }
                return;
            }
            if (slotHb == -1 && slotInv != -1 && this.bestSlot != -1) {
                this.placeRunnable = () -> {
                    if (SlownessManager.isEnabled()) {
                        SlownessManager.applySlowness(10L, () -> {
                            funnyBabah(slotInv);
                        });
                    } else {
                        funnyBabah(slotInv);
                    }
                };
                this.placeTimer.reset();
            } else if (slotHb != -1) {
                this.placeRunnable = () -> {
                    if (this.obsidianPos == null) {
                        return;
                    }
                    this.prevSlot = mc.field_1724.method_31548().field_7545;
                    placeCrystal(slotHb, this.obsidianPos);
                    if (this.options.isEnabled("Delayed swap back")) {
                        this.swapBackTimer.reset();
                        this.swapBack = true;
                        this.currentSlot = mc.field_1724.method_31548().field_7545;
                    } else {
                        mc.field_1724.method_31548().field_7545 = this.prevSlot;
                    }
                };
                this.placeTimer.reset();
            }
        }
    }

    private void handleTickEvent() {
        if (this.crystalEntity != null && !this.crystalEntity.method_5805()) {
            reset();
        }
        if (this.placeRunnable != null && this.placeTimer.finished(this.placeDelay.getValue().floatValue() * 50.0f)) {
            this.placeRunnable.run();
            this.placeTimer.reset();
            this.placeRunnable = null;
        }
        if (this.obsidianPos != null && this.attackTimer.finished(this.attackDelay.getValue().floatValue() * 50.0f)) {
            for (class_1511 crystal : findCrystals(this.obsidianPos)) {
                if (isValid(crystal)) {
                    attackCrystal(crystal);
                }
            }
        }
        if (this.options.isEnabled("Delayed swap back") && this.swapBack) {
            int playerCurrentSlot = mc.field_1724.method_31548().field_7545;
            if (playerCurrentSlot != this.currentSlot && playerCurrentSlot != this.prevSlot) {
                this.swapBack = false;
            } else if (this.swapBackTimer.finished(this.swapBackDelay.getValue().floatValue() * 50.0f)) {
                mc.field_1724.method_31548().field_7545 = this.prevSlot;
                this.swapBack = false;
            }
        }
    }

    private void attackCrystal(class_1297 entity) {
        if (isValid(entity) && mc.field_1724.method_7261(1.0f) >= 1.0f) {
            class_3966 hitResult = RaytracingUtil.raytraceEntity(this.distance.getValue().floatValue(), rotate(entity), false);
            boolean successRaytarce = (hitResult != null && hitResult.method_17782() == entity) || !this.options.isEnabled("Raytrace");
            if (successRaytarce) {
                mc.field_1761.method_2918(mc.field_1724, entity);
                mc.field_1724.method_6104(class_1268.field_5808);
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
        RotationStrategy configurable = new RotationStrategy(getAimMode(), MoveFixModule.enabled(), MoveFixModule.isFree()).ticksUntilReset(this.attackDelay.getValue().intValue());
        RotationManager.getInstance().addRotation(rotations, configurable, TaskPriority.REQUIRED, this);
        return rotations;
    }

    private RotationMode getAimMode() {
        switch (this.aimMode.getValue()) {
            case "Lony Grief":
                return new LonyGriefRotation();
            default:
                return new InstantRotation();
        }
    }

    private void placeCrystal(int slot, class_2338 pos) {
        boolean isOffhand = mc.field_1724.method_6079().method_7909() == class_1802.field_8301;
        class_243 center = class_243.method_24953(pos);
        class_3965 hitResult = new class_3965(center, class_2350.field_11036, pos, false);
        if (isOffhand) {
            if (mc.field_1761.method_2896(mc.field_1724, class_1268.field_5810, hitResult).method_23665()) {
                mc.field_1724.method_6104(class_1268.field_5810);
            }
        } else {
            mc.field_1724.method_31548().field_7545 = slot;
            if (mc.field_1761.method_2896(mc.field_1724, class_1268.field_5808, hitResult).method_23665() && mc.field_1724.method_6047().method_7909() == class_1802.field_8301) {
                mc.field_1724.method_6104(class_1268.field_5808);
            }
        }
    }

    private boolean isValid(class_1297 entity) {
        if (entity == null || this.obsidianPos == null || !entity.method_5805()) {
            return false;
        }
        return (!this.options.isEnabled("No suicide") || mc.field_1724.method_23318() <= ((double) this.obsidianPos.method_10264())) && mc.field_1724.method_33571().method_1022(RotationUtil.getSpot(entity)) < ((double) this.distance.getValue().floatValue());
    }

    private List<class_1511> findCrystals(class_2338 pos) {
        return mc.field_1687.method_8390(class_1511.class, new class_238(pos).method_1009(1.0d, 2.0d, 1.0d), endCrystalEntity -> {
            return endCrystalEntity != null && endCrystalEntity.method_5805();
        });
    }

    private void funnyBabah(int slot) {
        InventoryUtil.swapSlots(slot, this.bestSlot);
        if (this.obsidianPos != null) {
            this.prevSlot = mc.field_1724.method_31548().field_7545;
            placeCrystal(this.bestSlot, this.obsidianPos);
            if (this.options.isEnabled("Delayed swap back")) {
                this.swapBackTimer.reset();
                this.swapBack = true;
                this.currentSlot = mc.field_1724.method_31548().field_7545;
            } else {
                mc.field_1724.method_31548().field_7545 = this.prevSlot;
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
}
