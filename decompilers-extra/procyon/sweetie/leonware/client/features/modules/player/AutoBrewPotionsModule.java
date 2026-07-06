// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import net.minecraft.class_1657;
import java.util.Optional;
import net.minecraft.class_1847;
import net.minecraft.class_6880;
import net.minecraft.class_1842;
import net.minecraft.class_9334;
import net.minecraft.class_1844;
import net.minecraft.class_1713;
import net.minecraft.class_1802;
import net.minecraft.class_1799;
import net.minecraft.class_1792;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import net.minecraft.class_1268;
import net.minecraft.class_3965;
import sweetie.leonware.api.utils.task.TaskPriority;
import sweetie.leonware.api.utils.rotation.manager.RotationStrategy;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import net.minecraft.class_2350;
import java.util.List;
import java.util.Comparator;
import net.minecraft.class_2246;
import net.minecraft.class_2382;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import java.util.ArrayList;
import net.minecraft.class_1703;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import net.minecraft.class_1708;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.setting.Setting;
import java.util.HashMap;
import net.minecraft.class_2338;
import java.util.Map;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Auto Brew", category = Category.PLAYER)
public class AutoBrewPotionsModule extends Module
{
    private static final AutoBrewPotionsModule instance;
    private final SliderSetting range;
    private final SliderSetting strengthLimit;
    private final SliderSetting speedLimit;
    private final SliderSetting fireResLimit;
    private final ModeSetting modifier;
    private final BooleanSetting splash;
    private final Map<class_2338, Long> standCooldowns;
    private class_2338 currentStandPos;
    private String currentTargetType;
    private int delayTimer;
    private int fuelStep;
    private int ingStep;
    private int sourceSlot;
    private int tempEmptySlot;
    
    public AutoBrewPotionsModule() {
        this.range = new SliderSetting("\u0420\u0430\u0434\u0438\u0443\u0441").value(5.0f).range(1.0f, 7.0f).step(0.1f);
        this.strengthLimit = new SliderSetting("\u041b\u0438\u043c\u0438\u0442 \u0421\u0438\u043b\u044b").value(2.0f).range(0.0f, 10.0f).step(1.0f);
        this.speedLimit = new SliderSetting("\u041b\u0438\u043c\u0438\u0442 \u0421\u043a\u043e\u0440\u043e\u0441\u0442\u0438").value(2.0f).range(0.0f, 10.0f).step(1.0f);
        this.fireResLimit = new SliderSetting("\u041b\u0438\u043c\u0438\u0442 \u041e\u0433\u043d\u0435\u0441\u0442\u043e\u0439\u043a\u0438").value(2.0f).range(0.0f, 10.0f).step(1.0f);
        this.modifier = new ModeSetting("\u0423\u043b\u0443\u0447\u0448\u0435\u043d\u0438\u0435").values("\u041d\u0435\u0442", "\u0423\u0440\u043e\u0432\u0435\u043d\u044c 2", "\u0414\u043b\u0438\u0442\u0435\u043b\u044c\u043d\u043e\u0441\u0442\u044c").value("\u041d\u0435\u0442");
        this.splash = new BooleanSetting("\u0412\u0437\u0440\u044b\u0432\u043d\u044b\u0435").value(false);
        this.standCooldowns = new HashMap<class_2338, Long>();
        this.currentStandPos = null;
        this.currentTargetType = "\u0421\u0438\u043b\u0430";
        this.delayTimer = 0;
        this.fuelStep = 0;
        this.ingStep = 0;
        this.sourceSlot = -1;
        this.tempEmptySlot = -1;
        this.addSettings(this.range, this.strengthLimit, this.speedLimit, this.fireResLimit, this.modifier, this.splash);
    }
    
    @Override
    public void onEvent() {
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (AutoBrewPotionsModule.mc.field_1724 == null || AutoBrewPotionsModule.mc.field_1687 == null) {
                return;
            }
            else if (this.delayTimer > 0) {
                --this.delayTimer;
                return;
            }
            else {
                final class_1703 patt0$temp = AutoBrewPotionsModule.mc.field_1724.field_7512;
                if (patt0$temp instanceof final class_1708 brewingHandler) {
                    this.handleBrewingGUI(brewingHandler);
                    return;
                }
                else {
                    this.fuelStep = 0;
                    this.ingStep = 0;
                    this.currentStandPos = null;
                    this.findAndOpenStand();
                    return;
                }
            }
        }));
        this.addEvents(updateEvent);
    }
    
    private void findAndOpenStand() {
        final List<class_2338> stands = new ArrayList<class_2338>();
        for (int r = class_3532.method_15386((float)this.range.getValue()), x = -r; x <= r; ++x) {
            for (int y = -r; y <= r; ++y) {
                for (int z = -r; z <= r; ++z) {
                    final class_2338 pos = AutoBrewPotionsModule.mc.field_1724.method_24515().method_10069(x, y, z);
                    if (AutoBrewPotionsModule.mc.field_1724.method_33571().method_1022(class_243.method_24953((class_2382)pos)) <= this.range.getValue() && AutoBrewPotionsModule.mc.field_1687.method_8320(pos).method_27852(class_2246.field_10333)) {
                        stands.add(pos);
                    }
                }
            }
        }
        if (stands.isEmpty()) {
            return;
        }
        stands.sort(Comparator.comparingDouble(p -> AutoBrewPotionsModule.mc.field_1724.method_33571().method_1025(class_243.method_24953((class_2382)p))));
        for (int i = 0; i < stands.size(); ++i) {
            final class_2338 pos2 = stands.get(i);
            final String type = this.getPotionTypeForIndex(i);
            if (!type.isEmpty()) {
                if (System.currentTimeMillis() - this.standCooldowns.getOrDefault(pos2, 0L) > 1200L) {
                    this.currentStandPos = pos2;
                    this.currentTargetType = type;
                    this.openStandGhost(pos2);
                    return;
                }
            }
        }
    }
    
    private String getPotionTypeForIndex(final int index) {
        final int s = this.strengthLimit.getValue().intValue();
        final int sp = this.speedLimit.getValue().intValue();
        final int f = this.fireResLimit.getValue().intValue();
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
    
    private void openStandGhost(final class_2338 pos) {
        final class_243 eyes = AutoBrewPotionsModule.mc.field_1724.method_33571();
        final class_243 center = class_243.method_24953((class_2382)pos);
        class_2350 bestFace = class_2350.field_11036;
        double bestDist = Double.MAX_VALUE;
        for (final class_2350 dir : class_2350.values()) {
            final class_243 faceCenter = center.method_1019(new class_243((double)dir.method_10148(), (double)dir.method_10164(), (double)dir.method_10165()).method_1021(0.5));
            final double dist = eyes.method_1025(faceCenter);
            if (dist < bestDist) {
                bestDist = dist;
                bestFace = dir;
            }
        }
        final class_243 hitVec = center.method_1019(new class_243((double)bestFace.method_10148(), (double)bestFace.method_10164(), (double)bestFace.method_10165()).method_1021(0.45));
        final Rotation rot = RotationUtil.rotationAt(hitVec);
        RotationManager.getInstance().addRotation(rot, RotationStrategy.TARGET, TaskPriority.NORMAL, this);
        final class_3965 hit = new class_3965(hitVec, bestFace, pos, false);
        AutoBrewPotionsModule.mc.field_1761.method_2896(AutoBrewPotionsModule.mc.field_1724, class_1268.field_5808, hit);
        AutoBrewPotionsModule.mc.field_1724.method_6104(class_1268.field_5808);
        this.standCooldowns.put(pos, System.currentTimeMillis());
        this.delayTimer = 6;
    }
    
    private void handleBrewingGUI(final class_1708 handler) {
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
            if (handler.method_7611(i).method_7677().method_7960()) {
                final int water = this.findWaterBottleInInventory(handler);
                if (water != -1) {
                    this.quickMove(water);
                    this.delayTimer = 3;
                    return;
                }
            }
        }
        final class_1799 bottle = handler.method_7611(0).method_7677();
        if (bottle.method_7960()) {
            this.safeClose();
            return;
        }
        final class_1792 nextIng = this.getNeededIngredient(bottle, this.currentTargetType);
        if (nextIng != null) {
            if (handler.method_7611(3).method_7677().method_7960()) {
                this.handleIngredientFix(handler, nextIng);
            }
        }
        else {
            boolean collected = false;
            for (int j = 0; j < 3; ++j) {
                final class_1799 stack = handler.method_7611(j).method_7677();
                if (!stack.method_7960() && this.isFinished(stack, this.currentTargetType)) {
                    this.quickMove(j);
                    collected = true;
                }
            }
            if (collected) {
                this.delayTimer = 4;
                return;
            }
            this.safeClose();
        }
    }
    
    private void handleFuelFix(final class_1708 handler) {
        switch (this.fuelStep) {
            case 0: {
                this.sourceSlot = this.findItemInInventory(class_1802.field_8183, handler);
                this.tempEmptySlot = this.findEmptyInventorySlot(handler);
                if (this.sourceSlot != -1 && this.tempEmptySlot != -1) {
                    this.click(this.sourceSlot, 0, class_1713.field_7790);
                    this.fuelStep = 1;
                    this.delayTimer = 3;
                    break;
                }
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
                break;
            }
        }
    }
    
    private void handleIngredientFix(final class_1708 handler, final class_1792 ingredient) {
        switch (this.ingStep) {
            case 0: {
                if (ingredient == null) {
                    return;
                }
                this.sourceSlot = this.findItemInInventory(ingredient, handler);
                if (this.sourceSlot != -1) {
                    this.click(this.sourceSlot, 0, class_1713.field_7790);
                    this.ingStep = 1;
                    this.delayTimer = 3;
                    break;
                }
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
                }
                else {
                    this.click(this.sourceSlot, 0, class_1713.field_7790);
                }
                this.delayTimer = 2;
                break;
            }
        }
    }
    
    private void safeClose() {
        if (!AutoBrewPotionsModule.mc.field_1724.field_7512.method_34255().method_7960()) {
            final int empty = this.findEmptyInventorySlot((class_1708)AutoBrewPotionsModule.mc.field_1724.field_7512);
            if (empty != -1) {
                this.click(empty, 0, class_1713.field_7790);
                this.delayTimer = 3;
                return;
            }
        }
        AutoBrewPotionsModule.mc.field_1724.method_7346();
        this.delayTimer = 6;
    }
    
    private class_1792 getNeededIngredient(final class_1799 stack, final String type) {
        final class_1844 p = (class_1844)stack.method_57825(class_9334.field_49651, (Object)class_1844.field_49274);
        final Optional<class_6880<class_1842>> opt = p.comp_2378();
        if (opt.isEmpty()) {
            return null;
        }
        final class_1842 pot = (class_1842)opt.get().comp_349();
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
            if (this.splash.getValue() && !stack.method_31574(class_1802.field_8436)) {
                return class_1802.field_8054;
            }
        }
        return null;
    }
    
    private boolean isFinished(final class_1799 stack, final String type) {
        final class_1844 p = (class_1844)stack.method_57825(class_9334.field_49651, (Object)class_1844.field_49274);
        if (p.comp_2378().isEmpty()) {
            return false;
        }
        final class_1842 pot = (class_1842)p.comp_2378().get().comp_349();
        if (!this.isCorrectEffect(pot, type)) {
            return false;
        }
        if (this.modifier.is("\u0423\u0440\u043e\u0432\u0435\u043d\u044c 2") && !type.equals("\u041e\u0433\u043d\u0435\u0441\u0442\u043e\u0439\u043a\u0430") && !this.isStrong(pot)) {
            return false;
        }
        if (this.modifier.is("\u0414\u043b\u0438\u0442\u0435\u043b\u044c\u043d\u043e\u0441\u0442\u044c") && !this.isLong(pot)) {
            return false;
        }
        if (this.splash.getValue()) {
            if (!stack.method_31574(class_1802.field_8436)) {
                return false;
            }
        }
        else if (!stack.method_31574(class_1802.field_8574)) {
            return false;
        }
        return true;
    }
    
    private boolean isCorrectEffect(final class_1842 pot, final String type) {
        if (type.equals("\u0421\u0438\u043b\u0430")) {
            return pot.toString().contains("strength");
        }
        if (type.equals("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c")) {
            return pot.toString().contains("swiftness");
        }
        return type.equals("\u041e\u0433\u043d\u0435\u0441\u0442\u043e\u0439\u043a\u0430") && pot.toString().contains("fire_resistance");
    }
    
    private boolean isStrong(final class_1842 pot) {
        return pot.toString().contains("strong");
    }
    
    private boolean isLong(final class_1842 pot) {
        return pot.toString().contains("long");
    }
    
    private int findItemInInventory(final class_1792 item, final class_1708 h) {
        for (int i = 5; i < 41; ++i) {
            if (h.method_7611(i).method_7677().method_31574(item)) {
                return i;
            }
        }
        return -1;
    }
    
    private int findWaterBottleInInventory(final class_1708 h) {
        for (int i = 5; i < 41; ++i) {
            final class_1799 s = h.method_7611(i).method_7677();
            if (s.method_31574(class_1802.field_8574)) {
                final class_1844 p = (class_1844)s.method_57825(class_9334.field_49651, (Object)class_1844.field_49274);
                if (p.comp_2378().isPresent() && p.comp_2378().get().comp_349() == class_1847.field_8991.comp_349()) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    private int findEmptyInventorySlot(final class_1708 h) {
        for (int i = 5; i < 41; ++i) {
            if (h.method_7611(i).method_7677().method_7960()) {
                return i;
            }
        }
        return -1;
    }
    
    private void click(final int slot, final int btn, final class_1713 type) {
        AutoBrewPotionsModule.mc.field_1761.method_2906(AutoBrewPotionsModule.mc.field_1724.field_7512.field_7763, slot, btn, type, (class_1657)AutoBrewPotionsModule.mc.field_1724);
    }
    
    private void quickMove(final int slot) {
        this.click(slot, 0, class_1713.field_7794);
    }
    
    @Generated
    public static AutoBrewPotionsModule getInstance() {
        return AutoBrewPotionsModule.instance;
    }
    
    static {
        instance = new AutoBrewPotionsModule();
    }
}
