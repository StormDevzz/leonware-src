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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/AutoBuffModule.class */
@ModuleRegister(name = "Auto Buff", category = Category.COMBAT)
public class AutoBuffModule extends Module {
    private static final AutoBuffModule instance = new AutoBuffModule();
    private float originalPitch;
    private int originalSlot;
    private boolean buffing;
    private boolean throwing;
    private boolean rotating;
    private boolean swapping;
    private final MultiBooleanSetting potions = new MultiBooleanSetting("Potions").value(new BooleanSetting("Strength").value((Boolean) true), new BooleanSetting("Speed").value((Boolean) true), new BooleanSetting("Fire resistance").value((Boolean) false), new BooleanSetting("Healing").value((Boolean) true));
    private final SliderSetting health = new SliderSetting("Health").value(Float.valueOf(12.0f)).range(1.0f, 20.0f).step(0.5f);
    private final ModeSetting mode = new ModeSetting("Mode").value("Legit").values("Legit", "Packet");
    private final SliderSetting ticks = new SliderSetting("Tick after spawn").value(Float.valueOf(10.0f)).range(0.0f, 200.0f).step(5.0f);
    private final BooleanSetting autoDisable = new BooleanSetting("Auto disable").value((Boolean) false);
    private final BooleanSetting onlyOnGround = new BooleanSetting("Only on ground").value((Boolean) true);
    private final TimerUtil throwTimer = new TimerUtil();
    private final TimerUtil rotationTimer = new TimerUtil();
    private final List<String> toApply = new ArrayList();
    private final List<String> toReapply = new ArrayList();
    private int potionInvSlot = -1;
    private int tempHotbarSlot = -1;

    @Generated
    public static AutoBuffModule getInstance() {
        return instance;
    }

    @Generated
    public boolean isBuffing() {
        return this.buffing;
    }

    public AutoBuffModule() {
        addSettings(this.potions, this.health, this.mode, this.ticks, this.autoDisable, this.onlyOnGround);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener tick = TickEvent.getInstance().subscribe(new Listener(0, e -> {
            if (mc.field_1724 == null || mc.field_1687 == null) {
                return;
            }
            if ((!this.onlyOnGround.getValue().booleanValue() || mc.field_1724.method_24828()) && mc.field_1724.field_6012 >= this.ticks.getValue().floatValue()) {
                if (this.mode.is("Packet")) {
                    handlePacket();
                } else {
                    handleLegit();
                }
            }
        }));
        addEvents(tick);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        if (this.swapping && this.potionInvSlot != -1 && this.tempHotbarSlot != -1) {
            swapBack();
        }
        this.swapping = false;
        this.rotating = false;
        this.buffing = false;
        this.throwing = false;
        this.toApply.clear();
        this.toReapply.clear();
    }

    private void handlePacket() {
        if (shouldBuff() && this.throwTimer.finished(250L)) {
            if (this.rotating && this.rotationTimer.finished(5L)) {
                throwAll();
                this.throwTimer.reset();
                this.throwing = true;
                this.rotating = false;
                return;
            }
            if (!this.throwing) {
                prepareThrow();
            } else {
                finish(true);
            }
        }
    }

    private void handleLegit() {
        if (this.buffing) {
            if (!this.throwing || !this.throwTimer.finished(10L)) {
                if (!this.rotating || !this.rotationTimer.finished(10 / 2)) {
                    if (!this.toApply.isEmpty() || !this.toReapply.isEmpty()) {
                        prepareNext();
                        return;
                    } else {
                        finish(false);
                        return;
                    }
                }
                usePotion();
                return;
            }
            resetThrow();
            return;
        }
        if (shouldBuff()) {
            startBuff();
        }
    }

    private void startBuff() {
        this.originalPitch = mc.field_1724.method_36455();
        this.originalSlot = mc.field_1724.method_31548().field_7545;
        rotateDown();
        prepareList();
        this.buffing = true;
    }

    private void rotateDown() {
        RotationManager.getInstance().addRotation(new Rotation(mc.field_1724.method_36454(), 90.0f), RotationStrategy.TARGET, TaskPriority.CRITICAL, this);
    }

    private void finish(boolean packet) {
        if (this.swapping && !packet) {
            swapBack();
        }
        if (!packet) {
            InventoryUtil.swapToSlot(this.originalSlot);
        }
        RotationManager.getInstance().addRotation(new Rotation(mc.field_1724.method_36454(), this.originalPitch), RotationStrategy.TARGET, TaskPriority.HIGH, this);
        if (packet) {
            this.throwing = false;
        } else {
            this.buffing = false;
        }
        if (this.autoDisable.getValue().booleanValue()) {
            if (packet || allBuffed()) {
                toggle();
            }
        }
    }

    private void resetThrow() {
        this.rotating = false;
        this.throwing = false;
        if (!this.swapping || this.potionInvSlot == -1 || this.tempHotbarSlot == -1) {
            return;
        }
        swapBack();
    }

    private void swapBack() {
        Runnable r = () -> {
            InventoryUtil.swapSlots(this.potionInvSlot, this.tempHotbarSlot);
        };
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
                return mc.field_1724.method_6032() <= this.health.getValue().floatValue() && hasPotion(class_1294.field_5915);
            }
            class_6880<class_1291> e = getEffect(p);
            return (e == null || mc.field_1724.method_6059(e) || !hasPotion(e)) ? false : true;
        });
    }

    private boolean allBuffed() {
        return this.potions.getList().stream().allMatch(p -> {
            if (p.equals("Healing")) {
                return mc.field_1724.method_6032() > this.health.getValue().floatValue() || !hasPotion(class_1294.field_5915);
            }
            class_6880<class_1291> e = getEffect(p);
            return e == null || mc.field_1724.method_6059(e) || !hasPotion(e);
        });
    }

    private void prepareList() {
        this.toApply.clear();
        this.toReapply.clear();
        for (String p : this.potions.getList()) {
            if (!p.equals("Healing")) {
                class_6880<class_1291> e = getEffect(p);
                if (e != null && !mc.field_1724.method_6059(e) && hasPotion(e)) {
                    this.toApply.add(p);
                }
            } else if (mc.field_1724.method_6032() <= this.health.getValue().floatValue() && hasPotion(class_1294.field_5915)) {
                this.toApply.add(p);
            }
        }
    }

    private class_6880<class_1291> getEffect(String p) {
        switch (p) {
            case "Strength":
                return class_1294.field_5910;
            case "Speed":
                return class_1294.field_5904;
            case "Fire resistance":
                return class_1294.field_5918;
            case "Healing":
                return class_1294.field_5915;
            default:
                return null;
        }
    }

    private boolean hasPotion(class_6880<class_1291> e) {
        return (findSlot(e, 0, 9) == -1 && findSlot(e, 9, 36) == -1) ? false : true;
    }

    private int findSlot(class_6880<class_1291> e, int from, int to) {
        for (int i = from; i < to; i++) {
            class_1799 s = mc.field_1724.method_31548().method_5438(i);
            if (s.method_7909() instanceof class_1828) {
                class_1844 p = (class_1844) s.method_57825(class_9334.field_49651, class_1844.field_49274);
                boolean match = StreamSupport.stream(p.method_57397().spliterator(), false).anyMatch(x -> {
                    return x.method_5579().equals(e);
                });
                if (match) {
                    return i;
                }
            }
        }
        return -1;
    }

    private void prepareNext() {
        String str;
        class_6880<class_1291> e;
        if (this.toApply.isEmpty()) {
            str = !this.toReapply.isEmpty() ? (String) this.toReapply.removeFirst() : null;
        } else {
            str = (String) this.toApply.removeFirst();
        }
        String p = str;
        if (p == null || (e = getEffect(p)) == null) {
            return;
        }
        if (p.equals("Healing") || !mc.field_1724.method_6059(e)) {
            int hot = findSlot(e, 0, 9);
            int inv = findSlot(e, 9, 36);
            if (hot == -1) {
                if (inv != -1) {
                    swapFromInventory(inv, e);
                }
            } else {
                InventoryUtil.swapToSlot(hot);
                prepareThrow();
            }
        }
    }

    private void swapFromInventory(int inv, class_6880<class_1291> e) {
        this.swapping = true;
        this.potionInvSlot = inv;
        int empty = InventoryUtil.findEmptySlot();
        this.tempHotbarSlot = (empty == -1 || empty >= 9) ? InventoryUtil.findBestSlotInHotBar() : empty;
        if (this.tempHotbarSlot == -1) {
            return;
        }
        Runnable r = () -> {
            InventoryUtil.swapSlots(this.potionInvSlot, this.tempHotbarSlot);
            InventoryUtil.swapToSlot(this.tempHotbarSlot);
            prepareThrow();
        };
        if (SlownessManager.isEnabled()) {
            SlownessManager.applySlowness(10L, r);
        } else {
            r.run();
        }
    }

    private void throwAll() {
        int old = mc.field_1724.method_31548().field_7545;
        for (String p : this.potions.getList()) {
            class_6880<class_1291> e = getEffect(p);
            if (e != null && (p.equals("Healing") || !mc.field_1724.method_6059(e))) {
                if (!p.equals("Healing") || mc.field_1724.method_6032() <= this.health.getValue().floatValue()) {
                    int hot = findSlot(e, 0, 9);
                    int inv = findSlot(e, 9, 36);
                    Runnable use = () -> {
                        InventoryUtil.useItem(class_1268.field_5808);
                    };
                    if (hot != -1) {
                        InventoryUtil.swapToSlot(hot);
                        use.run();
                    } else if (inv != -1) {
                        int best = InventoryUtil.findBestSlotInHotBar();
                        Runnable t = () -> {
                            InventoryUtil.swapSlots(inv, best);
                            InventoryUtil.swapToSlot(best);
                            use.run();
                            InventoryUtil.swapSlots(inv, best);
                        };
                        if (SlownessManager.isEnabled()) {
                            SlownessManager.applySlowness(10L, t);
                        } else {
                            t.run();
                        }
                    }
                }
            }
        }
        InventoryUtil.swapToSlot(old);
    }

    private void prepareThrow() {
        rotateDown();
        this.rotationTimer.reset();
        this.rotating = true;
    }

    private void usePotion() {
        InventoryUtil.useItem(class_1268.field_5808);
        this.throwTimer.reset();
        this.throwing = true;
        this.rotating = false;
    }
}
