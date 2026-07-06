/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1268
 *  net.minecraft.class_1657
 *  net.minecraft.class_1703
 *  net.minecraft.class_1708
 *  net.minecraft.class_1713
 *  net.minecraft.class_1792
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_1842
 *  net.minecraft.class_1844
 *  net.minecraft.class_1847
 *  net.minecraft.class_2246
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_2382
 *  net.minecraft.class_243
 *  net.minecraft.class_3532
 *  net.minecraft.class_3965
 *  net.minecraft.class_6880
 *  net.minecraft.class_9334
 */
package sweetie.leonware.client.features.modules.player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1657;
import net.minecraft.class_1703;
import net.minecraft.class_1708;
import net.minecraft.class_1713;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1842;
import net.minecraft.class_1844;
import net.minecraft.class_1847;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2382;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.minecraft.class_3965;
import net.minecraft.class_6880;
import net.minecraft.class_9334;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.utils.rotation.manager.RotationStrategy;
import sweetie.leonware.api.utils.task.TaskPriority;

@ModuleRegister(name="Auto Brew", category=Category.PLAYER)
public class AutoBrewPotionsModule
extends Module {
    private static final AutoBrewPotionsModule instance = new AutoBrewPotionsModule();
    private final SliderSetting range = new SliderSetting("\u0420\u0430\u0434\u0438\u0443\u0441").value(Float.valueOf(5.0f)).range(1.0f, 7.0f).step(0.1f);
    private final SliderSetting strengthLimit = new SliderSetting("\u041b\u0438\u043c\u0438\u0442 \u0421\u0438\u043b\u044b").value(Float.valueOf(2.0f)).range(0.0f, 10.0f).step(1.0f);
    private final SliderSetting speedLimit = new SliderSetting("\u041b\u0438\u043c\u0438\u0442 \u0421\u043a\u043e\u0440\u043e\u0441\u0442\u0438").value(Float.valueOf(2.0f)).range(0.0f, 10.0f).step(1.0f);
    private final SliderSetting fireResLimit = new SliderSetting("\u041b\u0438\u043c\u0438\u0442 \u041e\u0433\u043d\u0435\u0441\u0442\u043e\u0439\u043a\u0438").value(Float.valueOf(2.0f)).range(0.0f, 10.0f).step(1.0f);
    private final ModeSetting modifier = new ModeSetting("\u0423\u043b\u0443\u0447\u0448\u0435\u043d\u0438\u0435").values("\u041d\u0435\u0442", "\u0423\u0440\u043e\u0432\u0435\u043d\u044c 2", "\u0414\u043b\u0438\u0442\u0435\u043b\u044c\u043d\u043e\u0441\u0442\u044c").value("\u041d\u0435\u0442");
    private final BooleanSetting splash = new BooleanSetting("\u0412\u0437\u0440\u044b\u0432\u043d\u044b\u0435").value(false);
    private final Map<class_2338, Long> standCooldowns = new HashMap<class_2338, Long>();
    private class_2338 currentStandPos = null;
    private String currentTargetType = "\u0421\u0438\u043b\u0430";
    private int delayTimer = 0;
    private int fuelStep = 0;
    private int ingStep = 0;
    private int sourceSlot = -1;
    private int tempEmptySlot = -1;

    public AutoBrewPotionsModule() {
        this.addSettings(this.range, this.strengthLimit, this.speedLimit, this.fireResLimit, this.modifier, this.splash);
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (AutoBrewPotionsModule.mc.field_1724 == null || AutoBrewPotionsModule.mc.field_1687 == null) {
                return;
            }
            if (this.delayTimer > 0) {
                --this.delayTimer;
                return;
            }
            class_1703 patt0$temp = AutoBrewPotionsModule.mc.field_1724.field_7512;
            if (patt0$temp instanceof class_1708) {
                class_1708 brewingHandler = (class_1708)patt0$temp;
                this.handleBrewingGUI(brewingHandler);
                return;
            }
            this.fuelStep = 0;
            this.ingStep = 0;
            this.currentStandPos = null;
            this.findAndOpenStand();
        }));
        this.addEvents(updateEvent);
    }

    private void findAndOpenStand() {
        ArrayList<class_2338> stands = new ArrayList<class_2338>();
        int r = class_3532.method_15386((float)((Float)this.range.getValue()).floatValue());
        for (int x = -r; x <= r; ++x) {
            for (int y = -r; y <= r; ++y) {
                for (int z = -r; z <= r; ++z) {
                    class_2338 pos = AutoBrewPotionsModule.mc.field_1724.method_24515().method_10069(x, y, z);
                    if (!(AutoBrewPotionsModule.mc.field_1724.method_33571().method_1022(class_243.method_24953((class_2382)pos)) <= (double)((Float)this.range.getValue()).floatValue()) || !AutoBrewPotionsModule.mc.field_1687.method_8320(pos).method_27852(class_2246.field_10333)) continue;
                    stands.add(pos);
                }
            }
        }
        if (stands.isEmpty()) {
            return;
        }
        stands.sort(Comparator.comparingDouble(p -> AutoBrewPotionsModule.mc.field_1724.method_33571().method_1025(class_243.method_24953((class_2382)p))));
        for (int i = 0; i < stands.size(); ++i) {
            class_2338 pos = (class_2338)stands.get(i);
            String type = this.getPotionTypeForIndex(i);
            if (type.isEmpty() || System.currentTimeMillis() - this.standCooldowns.getOrDefault(pos, 0L) <= 1200L) continue;
            this.currentStandPos = pos;
            this.currentTargetType = type;
            this.openStandGhost(pos);
            return;
        }
    }

    private String getPotionTypeForIndex(int index) {
        int s = ((Float)this.strengthLimit.getValue()).intValue();
        int sp = ((Float)this.speedLimit.getValue()).intValue();
        int f = ((Float)this.fireResLimit.getValue()).intValue();
        if (index < s) {
            return "\u0421\u0438\u043b\u0430";
        }
        if (index < s + sp) {
            return "\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c";
        }
        if (index < s + sp + f) {
            return "\u041e\u0433\u043d\u0435\u0441\u0442\u043e\u0439\u043a\u0430";
        }
        return "";
    }

    private void openStandGhost(class_2338 pos) {
        class_243 eyes = AutoBrewPotionsModule.mc.field_1724.method_33571();
        class_243 center = class_243.method_24953((class_2382)pos);
        class_2350 bestFace = class_2350.field_11036;
        double bestDist = Double.MAX_VALUE;
        for (class_2350 dir : class_2350.values()) {
            class_243 faceCenter = center.method_1019(new class_243((double)dir.method_10148(), (double)dir.method_10164(), (double)dir.method_10165()).method_1021(0.5));
            double dist = eyes.method_1025(faceCenter);
            if (!(dist < bestDist)) continue;
            bestDist = dist;
            bestFace = dir;
        }
        class_243 hitVec = center.method_1019(new class_243((double)bestFace.method_10148(), (double)bestFace.method_10164(), (double)bestFace.method_10165()).method_1021(0.45));
        Rotation rot = RotationUtil.rotationAt(hitVec);
        RotationManager.getInstance().addRotation(rot, RotationStrategy.TARGET, TaskPriority.NORMAL, this);
        class_3965 hit = new class_3965(hitVec, bestFace, pos, false);
        AutoBrewPotionsModule.mc.field_1761.method_2896(AutoBrewPotionsModule.mc.field_1724, class_1268.field_5808, hit);
        AutoBrewPotionsModule.mc.field_1724.method_6104(class_1268.field_5808);
        this.standCooldowns.put(pos, System.currentTimeMillis());
        this.delayTimer = 6;
    }

    private void handleBrewingGUI(class_1708 handler) {
        if (handler.method_17378() > 0) {
            this.safeClose();
            return;
        }
        if (handler.method_17377() <= 0 || this.fuelStep > 0) {
            this.handleFuelFix(handler);
            return;
        }
        if (this.ingStep > 0) {
            this.handleIngredientFix(handler, null);
            return;
        }
        for (int i = 0; i < 3; ++i) {
            int water;
            if (!handler.method_7611(i).method_7677().method_7960() || (water = this.findWaterBottleInInventory(handler)) == -1) continue;
            this.quickMove(water);
            this.delayTimer = 3;
            return;
        }
        class_1799 bottle = handler.method_7611(0).method_7677();
        if (bottle.method_7960()) {
            this.safeClose();
            return;
        }
        class_1792 nextIng = this.getNeededIngredient(bottle, this.currentTargetType);
        if (nextIng != null) {
            if (handler.method_7611(3).method_7677().method_7960()) {
                this.handleIngredientFix(handler, nextIng);
                return;
            }
        } else {
            boolean collected = false;
            for (int i = 0; i < 3; ++i) {
                class_1799 stack = handler.method_7611(i).method_7677();
                if (stack.method_7960() || !this.isFinished(stack, this.currentTargetType)) continue;
                this.quickMove(i);
                collected = true;
            }
            if (collected) {
                this.delayTimer = 4;
                return;
            }
            this.safeClose();
        }
    }

    private void handleFuelFix(class_1708 handler) {
        switch (this.fuelStep) {
            case 0: {
                this.sourceSlot = this.findItemInInventory(class_1802.field_8183, handler);
                this.tempEmptySlot = this.findEmptyInventorySlot(handler);
                if (this.sourceSlot == -1 || this.tempEmptySlot == -1) break;
                this.click(this.sourceSlot, 0, class_1713.field_7790);
                this.fuelStep = 1;
                this.delayTimer = 3;
                break;
            }
            case 1: {
                this.click(this.tempEmptySlot, 1, class_1713.field_7790);
                this.fuelStep = 2;
                this.delayTimer = 3;
                break;
            }
            case 2: {
                this.click(this.sourceSlot, 0, class_1713.field_7790);
                this.fuelStep = 3;
                this.delayTimer = 3;
                break;
            }
            case 3: {
                this.click(this.tempEmptySlot, 0, class_1713.field_7790);
                this.fuelStep = 4;
                this.delayTimer = 3;
                break;
            }
            case 4: {
                this.click(4, 0, class_1713.field_7790);
                this.fuelStep = 0;
                this.delayTimer = 3;
            }
        }
    }

    private void handleIngredientFix(class_1708 handler, class_1792 ingredient) {
        switch (this.ingStep) {
            case 0: {
                if (ingredient == null) {
                    return;
                }
                this.sourceSlot = this.findItemInInventory(ingredient, handler);
                if (this.sourceSlot == -1) break;
                this.click(this.sourceSlot, 0, class_1713.field_7790);
                this.ingStep = 1;
                this.delayTimer = 3;
                break;
            }
            case 1: {
                this.click(3, 1, class_1713.field_7790);
                this.ingStep = 2;
                this.delayTimer = 3;
                break;
            }
            case 2: {
                this.click(this.sourceSlot, 0, class_1713.field_7790);
                this.ingStep = 3;
                this.delayTimer = 3;
                break;
            }
            case 3: {
                if (handler.method_34255().method_7960()) {
                    this.ingStep = 0;
                } else {
                    this.click(this.sourceSlot, 0, class_1713.field_7790);
                }
                this.delayTimer = 2;
            }
        }
    }

    private void safeClose() {
        int empty;
        if (!AutoBrewPotionsModule.mc.field_1724.field_7512.method_34255().method_7960() && (empty = this.findEmptyInventorySlot((class_1708)AutoBrewPotionsModule.mc.field_1724.field_7512)) != -1) {
            this.click(empty, 0, class_1713.field_7790);
            this.delayTimer = 3;
            return;
        }
        AutoBrewPotionsModule.mc.field_1724.method_7346();
        this.delayTimer = 6;
    }

    private class_1792 getNeededIngredient(class_1799 stack, String type) {
        class_1844 p = (class_1844)stack.method_57825(class_9334.field_49651, (Object)class_1844.field_49274);
        Optional opt = p.comp_2378();
        if (opt.isEmpty()) {
            return null;
        }
        class_1842 pot = (class_1842)((class_6880)opt.get()).comp_349();
        if (pot == class_1847.field_8991.comp_349()) {
            return class_1802.field_8790;
        }
        if (pot == class_1847.field_8999.comp_349()) {
            if (type.equals("\u0421\u0438\u043b\u0430")) {
                return class_1802.field_8183;
            }
            if (type.equals("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c")) {
                return class_1802.field_8479;
            }
            if (type.equals("\u041e\u0433\u043d\u0435\u0441\u0442\u043e\u0439\u043a\u0430")) {
                return class_1802.field_8135;
            }
        }
        if (this.isCorrectEffect(pot, type)) {
            if (this.modifier.is("\u0423\u0440\u043e\u0432\u0435\u043d\u044c 2") && !type.equals("\u041e\u0433\u043d\u0435\u0441\u0442\u043e\u0439\u043a\u0430") && !this.isStrong(pot)) {
                return class_1802.field_8601;
            }
            if (this.modifier.is("\u0414\u043b\u0438\u0442\u0435\u043b\u044c\u043d\u043e\u0441\u0442\u044c") && !this.isLong(pot)) {
                return class_1802.field_8725;
            }
            if (((Boolean)this.splash.getValue()).booleanValue() && !stack.method_31574(class_1802.field_8436)) {
                return class_1802.field_8054;
            }
        }
        return null;
    }

    private boolean isFinished(class_1799 stack, String type) {
        class_1844 p = (class_1844)stack.method_57825(class_9334.field_49651, (Object)class_1844.field_49274);
        if (p.comp_2378().isEmpty()) {
            return false;
        }
        class_1842 pot = (class_1842)((class_6880)p.comp_2378().get()).comp_349();
        if (!this.isCorrectEffect(pot, type)) {
            return false;
        }
        if (this.modifier.is("\u0423\u0440\u043e\u0432\u0435\u043d\u044c 2") && !type.equals("\u041e\u0433\u043d\u0435\u0441\u0442\u043e\u0439\u043a\u0430") && !this.isStrong(pot)) {
            return false;
        }
        if (this.modifier.is("\u0414\u043b\u0438\u0442\u0435\u043b\u044c\u043d\u043e\u0441\u0442\u044c") && !this.isLong(pot)) {
            return false;
        }
        return !((Boolean)this.splash.getValue() != false ? !stack.method_31574(class_1802.field_8436) : !stack.method_31574(class_1802.field_8574));
    }

    private boolean isCorrectEffect(class_1842 pot, String type) {
        if (type.equals("\u0421\u0438\u043b\u0430")) {
            return pot.toString().contains("strength");
        }
        if (type.equals("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c")) {
            return pot.toString().contains("swiftness");
        }
        if (type.equals("\u041e\u0433\u043d\u0435\u0441\u0442\u043e\u0439\u043a\u0430")) {
            return pot.toString().contains("fire_resistance");
        }
        return false;
    }

    private boolean isStrong(class_1842 pot) {
        return pot.toString().contains("strong");
    }

    private boolean isLong(class_1842 pot) {
        return pot.toString().contains("long");
    }

    private int findItemInInventory(class_1792 item, class_1708 h) {
        for (int i = 5; i < 41; ++i) {
            if (!h.method_7611(i).method_7677().method_31574(item)) continue;
            return i;
        }
        return -1;
    }

    private int findWaterBottleInInventory(class_1708 h) {
        for (int i = 5; i < 41; ++i) {
            class_1844 p;
            class_1799 s = h.method_7611(i).method_7677();
            if (!s.method_31574(class_1802.field_8574) || !(p = (class_1844)s.method_57825(class_9334.field_49651, (Object)class_1844.field_49274)).comp_2378().isPresent() || ((class_6880)p.comp_2378().get()).comp_349() != class_1847.field_8991.comp_349()) continue;
            return i;
        }
        return -1;
    }

    private int findEmptyInventorySlot(class_1708 h) {
        for (int i = 5; i < 41; ++i) {
            if (!h.method_7611(i).method_7677().method_7960()) continue;
            return i;
        }
        return -1;
    }

    private void click(int slot, int btn, class_1713 type) {
        AutoBrewPotionsModule.mc.field_1761.method_2906(AutoBrewPotionsModule.mc.field_1724.field_7512.field_7763, slot, btn, type, (class_1657)AutoBrewPotionsModule.mc.field_1724);
    }

    private void quickMove(int slot) {
        this.click(slot, 0, class_1713.field_7794);
    }

    @Generated
    public static AutoBrewPotionsModule getInstance() {
        return instance;
    }
}

