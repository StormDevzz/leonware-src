package sweetie.leonware.client.features.modules.player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Generated;
import net.minecraft.class_1268;
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
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.minecraft.class_3965;
import net.minecraft.class_6880;
import net.minecraft.class_9334;
import org.newsclub.net.unix.AFVSOCKSocketAddress;
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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/player/AutoBrewPotionsModule.class */
@ModuleRegister(name = "Auto Brew", category = Category.PLAYER)
public class AutoBrewPotionsModule extends Module {
    private static final AutoBrewPotionsModule instance = new AutoBrewPotionsModule();
    private final SliderSetting range = new SliderSetting("Радиус").value(Float.valueOf(5.0f)).range(1.0f, 7.0f).step(0.1f);
    private final SliderSetting strengthLimit = new SliderSetting("Лимит Силы").value(Float.valueOf(2.0f)).range(0.0f, 10.0f).step(1.0f);
    private final SliderSetting speedLimit = new SliderSetting("Лимит Скорости").value(Float.valueOf(2.0f)).range(0.0f, 10.0f).step(1.0f);
    private final SliderSetting fireResLimit = new SliderSetting("Лимит Огнестойки").value(Float.valueOf(2.0f)).range(0.0f, 10.0f).step(1.0f);
    private final ModeSetting modifier = new ModeSetting("Улучшение").values("Нет", "Уровень 2", "Длительность").value("Нет");
    private final BooleanSetting splash = new BooleanSetting("Взрывные").value((Boolean) false);
    private final Map<class_2338, Long> standCooldowns = new HashMap();
    private class_2338 currentStandPos = null;
    private String currentTargetType = "Сила";
    private int delayTimer = 0;
    private int fuelStep = 0;
    private int ingStep = 0;
    private int sourceSlot = -1;
    private int tempEmptySlot = -1;

    @Generated
    public static AutoBrewPotionsModule getInstance() {
        return instance;
    }

    public AutoBrewPotionsModule() {
        addSettings(this.range, this.strengthLimit, this.speedLimit, this.fireResLimit, this.modifier, this.splash);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1724 == null || mc.field_1687 == null) {
                return;
            }
            if (this.delayTimer > 0) {
                this.delayTimer--;
                return;
            }
            class_1703 patt0$temp = mc.field_1724.field_7512;
            if (patt0$temp instanceof class_1708) {
                class_1708 brewingHandler = (class_1708) patt0$temp;
                handleBrewingGUI(brewingHandler);
            } else {
                this.fuelStep = 0;
                this.ingStep = 0;
                this.currentStandPos = null;
                findAndOpenStand();
            }
        }));
        addEvents(updateEvent);
    }

    private void findAndOpenStand() {
        List<class_2338> stands = new ArrayList<>();
        int r = class_3532.method_15386(this.range.getValue().floatValue());
        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                for (int z = -r; z <= r; z++) {
                    class_2338 pos = mc.field_1724.method_24515().method_10069(x, y, z);
                    if (mc.field_1724.method_33571().method_1022(class_243.method_24953(pos)) <= this.range.getValue().floatValue() && mc.field_1687.method_8320(pos).method_27852(class_2246.field_10333)) {
                        stands.add(pos);
                    }
                }
            }
        }
        if (stands.isEmpty()) {
            return;
        }
        stands.sort(Comparator.comparingDouble(p -> {
            return mc.field_1724.method_33571().method_1025(class_243.method_24953(p));
        }));
        for (int i = 0; i < stands.size(); i++) {
            class_2338 pos2 = stands.get(i);
            String type = getPotionTypeForIndex(i);
            if (!type.isEmpty() && System.currentTimeMillis() - this.standCooldowns.getOrDefault(pos2, 0L).longValue() > 1200) {
                this.currentStandPos = pos2;
                this.currentTargetType = type;
                openStandGhost(pos2);
                return;
            }
        }
    }

    private String getPotionTypeForIndex(int index) {
        int s = this.strengthLimit.getValue().intValue();
        int sp = this.speedLimit.getValue().intValue();
        int f = this.fireResLimit.getValue().intValue();
        return index < s ? "Сила" : index < s + sp ? "Скорость" : index < (s + sp) + f ? "Огнестойка" : "";
    }

    private void openStandGhost(class_2338 pos) {
        class_243 eyes = mc.field_1724.method_33571();
        class_243 center = class_243.method_24953(pos);
        class_2350 bestFace = class_2350.field_11036;
        double bestDist = Double.MAX_VALUE;
        for (class_2350 dir : class_2350.values()) {
            class_243 faceCenter = center.method_1019(new class_243(dir.method_10148(), dir.method_10164(), dir.method_10165()).method_1021(0.5d));
            double dist = eyes.method_1025(faceCenter);
            if (dist < bestDist) {
                bestDist = dist;
                bestFace = dir;
            }
        }
        class_243 hitVec = center.method_1019(new class_243(bestFace.method_10148(), bestFace.method_10164(), bestFace.method_10165()).method_1021(0.45d));
        Rotation rot = RotationUtil.rotationAt(hitVec);
        RotationManager.getInstance().addRotation(rot, RotationStrategy.TARGET, TaskPriority.NORMAL, this);
        class_3965 hit = new class_3965(hitVec, bestFace, pos, false);
        mc.field_1761.method_2896(mc.field_1724, class_1268.field_5808, hit);
        mc.field_1724.method_6104(class_1268.field_5808);
        this.standCooldowns.put(pos, Long.valueOf(System.currentTimeMillis()));
        this.delayTimer = 6;
    }

    private void handleBrewingGUI(class_1708 handler) {
        int water;
        if (handler.method_17378() > 0) {
            safeClose();
            return;
        }
        if (handler.method_17377() <= 0 || this.fuelStep > 0) {
            handleFuelFix(handler);
            return;
        }
        if (this.ingStep > 0) {
            handleIngredientFix(handler, null);
            return;
        }
        for (int i = 0; i < 3; i++) {
            if (handler.method_7611(i).method_7677().method_7960() && (water = findWaterBottleInInventory(handler)) != -1) {
                quickMove(water);
                this.delayTimer = 3;
                return;
            }
        }
        class_1799 bottle = handler.method_7611(0).method_7677();
        if (bottle.method_7960()) {
            safeClose();
            return;
        }
        class_1792 nextIng = getNeededIngredient(bottle, this.currentTargetType);
        if (nextIng != null) {
            if (handler.method_7611(3).method_7677().method_7960()) {
                handleIngredientFix(handler, nextIng);
                return;
            }
            return;
        }
        boolean collected = false;
        for (int i2 = 0; i2 < 3; i2++) {
            class_1799 stack = handler.method_7611(i2).method_7677();
            if (!stack.method_7960() && isFinished(stack, this.currentTargetType)) {
                quickMove(i2);
                collected = true;
            }
        }
        if (collected) {
            this.delayTimer = 4;
        } else {
            safeClose();
        }
    }

    private void handleFuelFix(class_1708 handler) {
        switch (this.fuelStep) {
            case 0:
                this.sourceSlot = findItemInInventory(class_1802.field_8183, handler);
                this.tempEmptySlot = findEmptyInventorySlot(handler);
                if (this.sourceSlot != -1 && this.tempEmptySlot != -1) {
                    click(this.sourceSlot, 0, class_1713.field_7790);
                    this.fuelStep = 1;
                    this.delayTimer = 3;
                    break;
                }
                break;
            case 1:
                click(this.tempEmptySlot, 1, class_1713.field_7790);
                this.fuelStep = 2;
                this.delayTimer = 3;
                break;
            case AFVSOCKSocketAddress.VMADDR_CID_HOST /* 2 */:
                click(this.sourceSlot, 0, class_1713.field_7790);
                this.fuelStep = 3;
                this.delayTimer = 3;
                break;
            case 3:
                click(this.tempEmptySlot, 0, class_1713.field_7790);
                this.fuelStep = 4;
                this.delayTimer = 3;
                break;
            case 4:
                click(4, 0, class_1713.field_7790);
                this.fuelStep = 0;
                this.delayTimer = 3;
                break;
        }
    }

    private void handleIngredientFix(class_1708 handler, class_1792 ingredient) {
        switch (this.ingStep) {
            case 0:
                if (ingredient != null) {
                    this.sourceSlot = findItemInInventory(ingredient, handler);
                    if (this.sourceSlot != -1) {
                        click(this.sourceSlot, 0, class_1713.field_7790);
                        this.ingStep = 1;
                        this.delayTimer = 3;
                    }
                    break;
                }
                break;
            case 1:
                click(3, 1, class_1713.field_7790);
                this.ingStep = 2;
                this.delayTimer = 3;
                break;
            case AFVSOCKSocketAddress.VMADDR_CID_HOST /* 2 */:
                click(this.sourceSlot, 0, class_1713.field_7790);
                this.ingStep = 3;
                this.delayTimer = 3;
                break;
            case 3:
                if (handler.method_34255().method_7960()) {
                    this.ingStep = 0;
                } else {
                    click(this.sourceSlot, 0, class_1713.field_7790);
                }
                this.delayTimer = 2;
                break;
        }
    }

    private void safeClose() {
        int empty;
        if (!mc.field_1724.field_7512.method_34255().method_7960() && (empty = findEmptyInventorySlot((class_1708) mc.field_1724.field_7512)) != -1) {
            click(empty, 0, class_1713.field_7790);
            this.delayTimer = 3;
        } else {
            mc.field_1724.method_7346();
            this.delayTimer = 6;
        }
    }

    private class_1792 getNeededIngredient(class_1799 stack, String type) {
        class_1844 p = (class_1844) stack.method_57825(class_9334.field_49651, class_1844.field_49274);
        Optional<class_6880<class_1842>> opt = p.comp_2378();
        if (opt.isEmpty()) {
            return null;
        }
        class_1842 pot = (class_1842) opt.get().comp_349();
        if (pot == class_1847.field_8991.comp_349()) {
            return class_1802.field_8790;
        }
        if (pot == class_1847.field_8999.comp_349()) {
            if (type.equals("Сила")) {
                return class_1802.field_8183;
            }
            if (type.equals("Скорость")) {
                return class_1802.field_8479;
            }
            if (type.equals("Огнестойка")) {
                return class_1802.field_8135;
            }
        }
        if (isCorrectEffect(pot, type)) {
            if (this.modifier.is("Уровень 2") && !type.equals("Огнестойка") && !isStrong(pot)) {
                return class_1802.field_8601;
            }
            if (this.modifier.is("Длительность") && !isLong(pot)) {
                return class_1802.field_8725;
            }
            if (!this.splash.getValue().booleanValue() || stack.method_31574(class_1802.field_8436)) {
                return null;
            }
            return class_1802.field_8054;
        }
        return null;
    }

    private boolean isFinished(class_1799 stack, String type) {
        class_1844 p = (class_1844) stack.method_57825(class_9334.field_49651, class_1844.field_49274);
        if (p.comp_2378().isEmpty()) {
            return false;
        }
        class_1842 pot = (class_1842) ((class_6880) p.comp_2378().get()).comp_349();
        if (!isCorrectEffect(pot, type)) {
            return false;
        }
        if (this.modifier.is("Уровень 2") && !type.equals("Огнестойка") && !isStrong(pot)) {
            return false;
        }
        if (!this.modifier.is("Длительность") || isLong(pot)) {
            return this.splash.getValue().booleanValue() ? stack.method_31574(class_1802.field_8436) : stack.method_31574(class_1802.field_8574);
        }
        return false;
    }

    private boolean isCorrectEffect(class_1842 pot, String type) {
        if (type.equals("Сила")) {
            return pot.toString().contains("strength");
        }
        if (type.equals("Скорость")) {
            return pot.toString().contains("swiftness");
        }
        if (type.equals("Огнестойка")) {
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
        for (int i = 5; i < 41; i++) {
            if (h.method_7611(i).method_7677().method_31574(item)) {
                return i;
            }
        }
        return -1;
    }

    private int findWaterBottleInInventory(class_1708 h) {
        for (int i = 5; i < 41; i++) {
            class_1799 s = h.method_7611(i).method_7677();
            if (s.method_31574(class_1802.field_8574)) {
                class_1844 p = (class_1844) s.method_57825(class_9334.field_49651, class_1844.field_49274);
                if (p.comp_2378().isPresent() && ((class_6880) p.comp_2378().get()).comp_349() == class_1847.field_8991.comp_349()) {
                    return i;
                }
            }
        }
        return -1;
    }

    private int findEmptyInventorySlot(class_1708 h) {
        for (int i = 5; i < 41; i++) {
            if (h.method_7611(i).method_7677().method_7960()) {
                return i;
            }
        }
        return -1;
    }

    private void click(int slot, int btn, class_1713 type) {
        mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, slot, btn, type, mc.field_1724);
    }

    private void quickMove(int slot) {
        click(slot, 0, class_1713.field_7794);
    }
}
