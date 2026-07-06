/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1268
 *  net.minecraft.class_1291
 *  net.minecraft.class_1294
 *  net.minecraft.class_1799
 *  net.minecraft.class_1828
 *  net.minecraft.class_1844
 *  net.minecraft.class_6880
 *  net.minecraft.class_9334
 */
package sweetie.leonware.client.features.modules.combat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;
import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1291;
import net.minecraft.class_1294;
import net.minecraft.class_1799;
import net.minecraft.class_1828;
import net.minecraft.class_1844;
import net.minecraft.class_6880;
import net.minecraft.class_9334;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.TickEvent;
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
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.utils.rotation.manager.RotationStrategy;
import sweetie.leonware.api.utils.task.TaskPriority;

@ModuleRegister(name="Auto Buff", category=Category.COMBAT)
public class AutoBuffModule
extends Module {
    private static final AutoBuffModule instance = new AutoBuffModule();
    private final MultiBooleanSetting potions = new MultiBooleanSetting("Potions").value(new BooleanSetting("Strength").value(true), new BooleanSetting("Speed").value(true), new BooleanSetting("Fire resistance").value(false), new BooleanSetting("Healing").value(true));
    private final SliderSetting health = new SliderSetting("Health").value(Float.valueOf(12.0f)).range(1.0f, 20.0f).step(0.5f);
    private final ModeSetting mode = new ModeSetting("Mode").value("Legit").values("Legit", "Packet");
    private final SliderSetting ticks = new SliderSetting("Tick after spawn").value(Float.valueOf(10.0f)).range(0.0f, 200.0f).step(5.0f);
    private final BooleanSetting autoDisable = new BooleanSetting("Auto disable").value(false);
    private final BooleanSetting onlyOnGround = new BooleanSetting("Only on ground").value(true);
    private final TimerUtil throwTimer = new TimerUtil();
    private final TimerUtil rotationTimer = new TimerUtil();
    private final List<String> toApply = new ArrayList<String>();
    private final List<String> toReapply = new ArrayList<String>();
    private float originalPitch;
    private int originalSlot;
    private int potionInvSlot = -1;
    private int tempHotbarSlot = -1;
    private boolean buffing;
    private boolean throwing;
    private boolean rotating;
    private boolean swapping;

    public AutoBuffModule() {
        this.addSettings(this.potions, this.health, this.mode, this.ticks, this.autoDisable, this.onlyOnGround);
    }

    @Override
    public void onEvent() {
        EventListener tick = TickEvent.getInstance().subscribe(new Listener<TickEvent>(0, e -> {
            if (AutoBuffModule.mc.field_1724 == null || AutoBuffModule.mc.field_1687 == null) {
                return;
            }
            if (((Boolean)this.onlyOnGround.getValue()).booleanValue() && !AutoBuffModule.mc.field_1724.method_24828()) {
                return;
            }
            if ((float)AutoBuffModule.mc.field_1724.field_6012 < ((Float)this.ticks.getValue()).floatValue()) {
                return;
            }
            if (this.mode.is("Packet")) {
                this.handlePacket();
            } else {
                this.handleLegit();
            }
        }));
        this.addEvents(tick);
    }

    @Override
    public void onDisable() {
        if (this.swapping && this.potionInvSlot != -1 && this.tempHotbarSlot != -1) {
            this.swapBack();
        }
        this.swapping = false;
        this.rotating = false;
        this.buffing = false;
        this.throwing = false;
        this.toApply.clear();
        this.toReapply.clear();
    }

    private void handlePacket() {
        if (!this.shouldBuff() || !this.throwTimer.finished(250L)) {
            return;
        }
        if (this.rotating && this.rotationTimer.finished(5L)) {
            this.throwAll();
            this.throwTimer.reset();
            this.throwing = true;
            this.rotating = false;
        } else if (this.throwing) {
            this.finish(true);
        } else {
            this.prepareThrow();
        }
    }

    private void handleLegit() {
        long d = 10L;
        if (this.buffing) {
            if (this.throwing && this.throwTimer.finished(d)) {
                this.resetThrow();
            } else if (this.rotating && this.rotationTimer.finished(d / 2L)) {
                this.usePotion();
            } else if (this.toApply.isEmpty() && this.toReapply.isEmpty()) {
                this.finish(false);
            } else {
                this.prepareNext();
            }
            return;
        }
        if (this.shouldBuff()) {
            this.startBuff();
        }
    }

    private void startBuff() {
        this.originalPitch = AutoBuffModule.mc.field_1724.method_36455();
        this.originalSlot = AutoBuffModule.mc.field_1724.method_31548().field_7545;
        this.rotateDown();
        this.prepareList();
        this.buffing = true;
    }

    private void rotateDown() {
        RotationManager.getInstance().addRotation(new Rotation(AutoBuffModule.mc.field_1724.method_36454(), 90.0f), RotationStrategy.TARGET, TaskPriority.CRITICAL, this);
    }

    private void finish(boolean packet) {
        if (this.swapping && !packet) {
            this.swapBack();
        }
        if (!packet) {
            InventoryUtil.swapToSlot(this.originalSlot);
        }
        RotationManager.getInstance().addRotation(new Rotation(AutoBuffModule.mc.field_1724.method_36454(), this.originalPitch), RotationStrategy.TARGET, TaskPriority.HIGH, this);
        if (!packet) {
            this.buffing = false;
        } else {
            this.throwing = false;
        }
        if (((Boolean)this.autoDisable.getValue()).booleanValue() && (packet || this.allBuffed())) {
            this.toggle();
        }
    }

    private void resetThrow() {
        this.rotating = false;
        this.throwing = false;
        if (this.swapping && this.potionInvSlot != -1 && this.tempHotbarSlot != -1) {
            this.swapBack();
        }
    }

    private void swapBack() {
        Runnable r = () -> InventoryUtil.swapSlots(this.potionInvSlot, this.tempHotbarSlot);
        if (SlownessManager.isEnabled()) {
            SlownessManager.applySlowness(10L, r);
        } else {
            r.run();
        }
        this.swapping = false;
        this.tempHotbarSlot = -1;
        this.potionInvSlot = -1;
    }

    private boolean shouldBuff() {
        return this.potions.getList().stream().anyMatch(p -> {
            if (p.equals("Healing")) {
                return AutoBuffModule.mc.field_1724.method_6032() <= ((Float)this.health.getValue()).floatValue() && this.hasPotion((class_6880<class_1291>)class_1294.field_5915);
            }
            class_6880<class_1291> e = this.getEffect((String)p);
            return e != null && !AutoBuffModule.mc.field_1724.method_6059(e) && this.hasPotion(e);
        });
    }

    private boolean allBuffed() {
        return this.potions.getList().stream().allMatch(p -> {
            if (p.equals("Healing")) {
                return AutoBuffModule.mc.field_1724.method_6032() > ((Float)this.health.getValue()).floatValue() || !this.hasPotion((class_6880<class_1291>)class_1294.field_5915);
            }
            class_6880<class_1291> e = this.getEffect((String)p);
            return e == null || AutoBuffModule.mc.field_1724.method_6059(e) || !this.hasPotion(e);
        });
    }

    private void prepareList() {
        this.toApply.clear();
        this.toReapply.clear();
        for (String p : this.potions.getList()) {
            if (p.equals("Healing")) {
                if (!(AutoBuffModule.mc.field_1724.method_6032() <= ((Float)this.health.getValue()).floatValue()) || !this.hasPotion((class_6880<class_1291>)class_1294.field_5915)) continue;
                this.toApply.add(p);
                continue;
            }
            class_6880<class_1291> e = this.getEffect(p);
            if (e == null || AutoBuffModule.mc.field_1724.method_6059(e) || !this.hasPotion(e)) continue;
            this.toApply.add(p);
        }
    }

    private class_6880<class_1291> getEffect(String p) {
        return switch (p) {
            case "Strength" -> class_1294.field_5910;
            case "Speed" -> class_1294.field_5904;
            case "Fire resistance" -> class_1294.field_5918;
            case "Healing" -> class_1294.field_5915;
            default -> null;
        };
    }

    private boolean hasPotion(class_6880<class_1291> e) {
        return this.findSlot(e, 0, 9) != -1 || this.findSlot(e, 9, 36) != -1;
    }

    private int findSlot(class_6880<class_1291> e, int from, int to) {
        for (int i = from; i < to; ++i) {
            class_1844 p;
            boolean match;
            class_1799 s = AutoBuffModule.mc.field_1724.method_31548().method_5438(i);
            if (!(s.method_7909() instanceof class_1828) || !(match = StreamSupport.stream((p = (class_1844)s.method_57825(class_9334.field_49651, (Object)class_1844.field_49274)).method_57397().spliterator(), false).anyMatch(x -> x.method_5579().equals((Object)e)))) continue;
            return i;
        }
        return -1;
    }

    private void prepareNext() {
        String p;
        String string = !this.toApply.isEmpty() ? this.toApply.removeFirst() : (p = !this.toReapply.isEmpty() ? this.toReapply.removeFirst() : null);
        if (p == null) {
            return;
        }
        class_6880<class_1291> e = this.getEffect(p);
        if (e == null) {
            return;
        }
        if (!p.equals("Healing") && AutoBuffModule.mc.field_1724.method_6059(e)) {
            return;
        }
        int hot = this.findSlot(e, 0, 9);
        int inv = this.findSlot(e, 9, 36);
        if (hot != -1) {
            InventoryUtil.swapToSlot(hot);
            this.prepareThrow();
        } else if (inv != -1) {
            this.swapFromInventory(inv, e);
        }
    }

    private void swapFromInventory(int inv, class_6880<class_1291> e) {
        this.swapping = true;
        this.potionInvSlot = inv;
        int empty = InventoryUtil.findEmptySlot();
        int n = this.tempHotbarSlot = empty != -1 && empty < 9 ? empty : InventoryUtil.findBestSlotInHotBar();
        if (this.tempHotbarSlot == -1) {
            return;
        }
        Runnable r = () -> {
            InventoryUtil.swapSlots(this.potionInvSlot, this.tempHotbarSlot);
            InventoryUtil.swapToSlot(this.tempHotbarSlot);
            this.prepareThrow();
        };
        if (SlownessManager.isEnabled()) {
            SlownessManager.applySlowness(10L, r);
        } else {
            r.run();
        }
    }

    private void throwAll() {
        int old = AutoBuffModule.mc.field_1724.method_31548().field_7545;
        for (String p : this.potions.getList()) {
            class_6880<class_1291> e = this.getEffect(p);
            if (e == null || !p.equals("Healing") && AutoBuffModule.mc.field_1724.method_6059(e) || p.equals("Healing") && AutoBuffModule.mc.field_1724.method_6032() > ((Float)this.health.getValue()).floatValue()) continue;
            int hot = this.findSlot(e, 0, 9);
            int inv = this.findSlot(e, 9, 36);
            Runnable use = () -> InventoryUtil.useItem(class_1268.field_5808);
            if (hot != -1) {
                InventoryUtil.swapToSlot(hot);
                use.run();
                continue;
            }
            if (inv == -1) continue;
            int best = InventoryUtil.findBestSlotInHotBar();
            Runnable t = () -> {
                InventoryUtil.swapSlots(inv, best);
                InventoryUtil.swapToSlot(best);
                use.run();
                InventoryUtil.swapSlots(inv, best);
            };
            if (SlownessManager.isEnabled()) {
                SlownessManager.applySlowness(10L, t);
                continue;
            }
            t.run();
        }
        InventoryUtil.swapToSlot(old);
    }

    private void prepareThrow() {
        this.rotateDown();
        this.rotationTimer.reset();
        this.rotating = true;
    }

    private void usePotion() {
        InventoryUtil.useItem(class_1268.field_5808);
        this.throwTimer.reset();
        this.throwing = true;
        this.rotating = false;
    }

    @Generated
    public static AutoBuffModule getInstance() {
        return instance;
    }

    @Generated
    public boolean isBuffing() {
        return this.buffing;
    }
}

