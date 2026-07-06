// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.combat;

import net.minecraft.class_1293;
import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1799;
import java.util.stream.StreamSupport;
import net.minecraft.class_9334;
import net.minecraft.class_1844;
import net.minecraft.class_1828;
import java.util.Iterator;
import net.minecraft.class_1291;
import net.minecraft.class_6880;
import net.minecraft.class_1294;
import sweetie.leonware.api.utils.other.SlownessManager;
import sweetie.leonware.api.utils.player.InventoryUtil;
import sweetie.leonware.api.utils.task.TaskPriority;
import sweetie.leonware.api.utils.rotation.manager.RotationStrategy;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.module.setting.Setting;
import java.util.ArrayList;
import java.util.List;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Auto Buff", category = Category.COMBAT)
public class AutoBuffModule extends Module
{
    private static final AutoBuffModule instance;
    private final MultiBooleanSetting potions;
    private final SliderSetting health;
    private final ModeSetting mode;
    private final SliderSetting ticks;
    private final BooleanSetting autoDisable;
    private final BooleanSetting onlyOnGround;
    private final TimerUtil throwTimer;
    private final TimerUtil rotationTimer;
    private final List<String> toApply;
    private final List<String> toReapply;
    private float originalPitch;
    private int originalSlot;
    private int potionInvSlot;
    private int tempHotbarSlot;
    private boolean buffing;
    private boolean throwing;
    private boolean rotating;
    private boolean swapping;
    
    public AutoBuffModule() {
        this.potions = new MultiBooleanSetting("Potions").value(new BooleanSetting("Strength").value(true), new BooleanSetting("Speed").value(true), new BooleanSetting("Fire resistance").value(false), new BooleanSetting("Healing").value(true));
        this.health = new SliderSetting("Health").value(12.0f).range(1.0f, 20.0f).step(0.5f);
        this.mode = new ModeSetting("Mode").value("Legit").values("Legit", "Packet");
        this.ticks = new SliderSetting("Tick after spawn").value(10.0f).range(0.0f, 200.0f).step(5.0f);
        this.autoDisable = new BooleanSetting("Auto disable").value(false);
        this.onlyOnGround = new BooleanSetting("Only on ground").value(true);
        this.throwTimer = new TimerUtil();
        this.rotationTimer = new TimerUtil();
        this.toApply = new ArrayList<String>();
        this.toReapply = new ArrayList<String>();
        this.potionInvSlot = -1;
        this.tempHotbarSlot = -1;
        this.addSettings(this.potions, this.health, this.mode, this.ticks, this.autoDisable, this.onlyOnGround);
    }
    
    @Override
    public void onEvent() {
        final EventListener tick = TickEvent.getInstance().subscribe(new Listener<TickEvent>(0, e -> {
            if (AutoBuffModule.mc.field_1724 == null || AutoBuffModule.mc.field_1687 == null) {
                return;
            }
            else if (this.onlyOnGround.getValue() && !AutoBuffModule.mc.field_1724.method_24828()) {
                return;
            }
            else if (AutoBuffModule.mc.field_1724.field_6012 < this.ticks.getValue()) {
                return;
            }
            else {
                if (this.mode.is("Packet")) {
                    this.handlePacket();
                }
                else {
                    this.handleLegit();
                }
                return;
            }
        }));
        this.addEvents(tick);
    }
    
    @Override
    public void onDisable() {
        if (this.swapping && this.potionInvSlot != -1 && this.tempHotbarSlot != -1) {
            this.swapBack();
        }
        final boolean b = false;
        this.swapping = b;
        this.rotating = b;
        this.buffing = b;
        this.throwing = b;
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
        }
        else if (this.throwing) {
            this.finish(true);
        }
        else {
            this.prepareThrow();
        }
    }
    
    private void handleLegit() {
        final long d = 10L;
        if (this.buffing) {
            if (this.throwing && this.throwTimer.finished(d)) {
                this.resetThrow();
            }
            else if (this.rotating && this.rotationTimer.finished(d / 2L)) {
                this.usePotion();
            }
            else if (this.toApply.isEmpty() && this.toReapply.isEmpty()) {
                this.finish(false);
            }
            else {
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
    
    private void finish(final boolean packet) {
        if (this.swapping && !packet) {
            this.swapBack();
        }
        if (!packet) {
            InventoryUtil.swapToSlot(this.originalSlot);
        }
        RotationManager.getInstance().addRotation(new Rotation(AutoBuffModule.mc.field_1724.method_36454(), this.originalPitch), RotationStrategy.TARGET, TaskPriority.HIGH, this);
        if (!packet) {
            this.buffing = false;
        }
        else {
            this.throwing = false;
        }
        if (this.autoDisable.getValue() && (packet || this.allBuffed())) {
            this.toggle();
        }
    }
    
    private void resetThrow() {
        final boolean b = false;
        this.rotating = b;
        this.throwing = b;
        if (this.swapping && this.potionInvSlot != -1 && this.tempHotbarSlot != -1) {
            this.swapBack();
        }
    }
    
    private void swapBack() {
        final Runnable r = () -> InventoryUtil.swapSlots(this.potionInvSlot, this.tempHotbarSlot);
        if (SlownessManager.isEnabled()) {
            SlownessManager.applySlowness(10L, r);
        }
        else {
            r.run();
        }
        this.swapping = false;
        final int n = -1;
        this.tempHotbarSlot = n;
        this.potionInvSlot = n;
    }
    
    private boolean shouldBuff() {
        return this.potions.getList().stream().anyMatch(p -> {
            if (p.equals("Healing")) {
                return AutoBuffModule.mc.field_1724.method_6032() <= this.health.getValue() && this.hasPotion((class_6880<class_1291>)class_1294.field_5915);
            }
            else {
                final class_6880<class_1291> e = this.getEffect(p);
                return e != null && !AutoBuffModule.mc.field_1724.method_6059((class_6880)e) && this.hasPotion(e);
            }
        });
    }
    
    private boolean allBuffed() {
        return this.potions.getList().stream().allMatch(p -> {
            if (p.equals("Healing")) {
                return AutoBuffModule.mc.field_1724.method_6032() > this.health.getValue() || !this.hasPotion((class_6880<class_1291>)class_1294.field_5915);
            }
            else {
                final class_6880<class_1291> e = this.getEffect(p);
                return e == null || AutoBuffModule.mc.field_1724.method_6059((class_6880)e) || !this.hasPotion(e);
            }
        });
    }
    
    private void prepareList() {
        this.toApply.clear();
        this.toReapply.clear();
        for (final String p : this.potions.getList()) {
            if (p.equals("Healing")) {
                if (AutoBuffModule.mc.field_1724.method_6032() > this.health.getValue() || !this.hasPotion((class_6880<class_1291>)class_1294.field_5915)) {
                    continue;
                }
                this.toApply.add(p);
            }
            else {
                final class_6880<class_1291> e = this.getEffect(p);
                if (e == null || AutoBuffModule.mc.field_1724.method_6059((class_6880)e) || !this.hasPotion(e)) {
                    continue;
                }
                this.toApply.add(p);
            }
        }
    }
    
    private class_6880<class_1291> getEffect(final String p) {
        return switch (p) {
            case "Strength" -> class_1294.field_5910;
            case "Speed" -> class_1294.field_5904;
            case "Fire resistance" -> class_1294.field_5918;
            case "Healing" -> class_1294.field_5915;
            default -> null;
        };
    }
    
    private boolean hasPotion(final class_6880<class_1291> e) {
        return this.findSlot(e, 0, 9) != -1 || this.findSlot(e, 9, 36) != -1;
    }
    
    private int findSlot(final class_6880<class_1291> e, final int from, final int to) {
        for (int i = from; i < to; ++i) {
            final class_1799 s = AutoBuffModule.mc.field_1724.method_31548().method_5438(i);
            if (s.method_7909() instanceof class_1828) {
                final class_1844 p = (class_1844)s.method_57825(class_9334.field_49651, (Object)class_1844.field_49274);
                final boolean match = StreamSupport.stream(p.method_57397().spliterator(), false).anyMatch(x -> x.method_5579().equals((Object)e));
                if (match) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    private void prepareNext() {
        final String p = this.toApply.isEmpty() ? (this.toReapply.isEmpty() ? null : this.toReapply.removeFirst()) : this.toApply.removeFirst();
        if (p == null) {
            return;
        }
        final class_6880<class_1291> e = this.getEffect(p);
        if (e == null) {
            return;
        }
        if (!p.equals("Healing") && AutoBuffModule.mc.field_1724.method_6059((class_6880)e)) {
            return;
        }
        final int hot = this.findSlot(e, 0, 9);
        final int inv = this.findSlot(e, 9, 36);
        if (hot != -1) {
            InventoryUtil.swapToSlot(hot);
            this.prepareThrow();
        }
        else if (inv != -1) {
            this.swapFromInventory(inv, e);
        }
    }
    
    private void swapFromInventory(final int inv, final class_6880<class_1291> e) {
        this.swapping = true;
        this.potionInvSlot = inv;
        final int empty = InventoryUtil.findEmptySlot();
        this.tempHotbarSlot = ((empty != -1 && empty < 9) ? empty : InventoryUtil.findBestSlotInHotBar());
        if (this.tempHotbarSlot == -1) {
            return;
        }
        final Runnable r = () -> {
            InventoryUtil.swapSlots(this.potionInvSlot, this.tempHotbarSlot);
            InventoryUtil.swapToSlot(this.tempHotbarSlot);
            this.prepareThrow();
            return;
        };
        if (SlownessManager.isEnabled()) {
            SlownessManager.applySlowness(10L, r);
        }
        else {
            r.run();
        }
    }
    
    private void throwAll() {
        final int old = AutoBuffModule.mc.field_1724.method_31548().field_7545;
        for (final String p : this.potions.getList()) {
            final class_6880<class_1291> e = this.getEffect(p);
            if (e == null) {
                continue;
            }
            if (!p.equals("Healing") && AutoBuffModule.mc.field_1724.method_6059((class_6880)e)) {
                continue;
            }
            if (p.equals("Healing") && AutoBuffModule.mc.field_1724.method_6032() > this.health.getValue()) {
                continue;
            }
            final int hot = this.findSlot(e, 0, 9);
            final int inv = this.findSlot(e, 9, 36);
            final Runnable use = () -> InventoryUtil.useItem(class_1268.field_5808);
            if (hot != -1) {
                InventoryUtil.swapToSlot(hot);
                use.run();
            }
            else {
                if (inv == -1) {
                    continue;
                }
                final int best = InventoryUtil.findBestSlotInHotBar();
                final Runnable t = () -> {
                    InventoryUtil.swapSlots(inv, best);
                    InventoryUtil.swapToSlot(best);
                    use.run();
                    InventoryUtil.swapSlots(inv, best);
                    return;
                };
                if (SlownessManager.isEnabled()) {
                    SlownessManager.applySlowness(10L, t);
                }
                else {
                    t.run();
                }
            }
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
        return AutoBuffModule.instance;
    }
    
    @Generated
    public boolean isBuffing() {
        return this.buffing;
    }
    
    static {
        instance = new AutoBuffModule();
    }
}
