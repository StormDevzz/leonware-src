/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.Object2IntMap$Entry
 *  lombok.Generated
 *  net.minecraft.class_10192
 *  net.minecraft.class_1268
 *  net.minecraft.class_1297
 *  net.minecraft.class_1304
 *  net.minecraft.class_1657
 *  net.minecraft.class_1713
 *  net.minecraft.class_1738
 *  net.minecraft.class_1792
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_1890
 *  net.minecraft.class_1893
 *  net.minecraft.class_2596
 *  net.minecraft.class_2815
 *  net.minecraft.class_2848
 *  net.minecraft.class_2848$class_2849
 *  net.minecraft.class_2868
 *  net.minecraft.class_2886
 *  net.minecraft.class_5134
 *  net.minecraft.class_6880
 *  net.minecraft.class_9285
 *  net.minecraft.class_9285$class_9287
 *  net.minecraft.class_9304
 *  net.minecraft.class_9334
 */
package sweetie.leonware.client.features.modules.player;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.Arrays;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_10192;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1304;
import net.minecraft.class_1657;
import net.minecraft.class_1713;
import net.minecraft.class_1738;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1890;
import net.minecraft.class_1893;
import net.minecraft.class_2596;
import net.minecraft.class_2815;
import net.minecraft.class_2848;
import net.minecraft.class_2868;
import net.minecraft.class_2886;
import net.minecraft.class_5134;
import net.minecraft.class_6880;
import net.minecraft.class_9285;
import net.minecraft.class_9304;
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
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.other.NetworkUtil;
import sweetie.leonware.api.utils.other.SlownessManager;
import sweetie.leonware.api.utils.player.InventoryUtil;
import sweetie.leonware.api.utils.player.MoveUtil;

@ModuleRegister(name="Auto Armor", category=Category.PLAYER)
public class AutoArmorModule
extends Module {
    private static final AutoArmorModule instance = new AutoArmorModule();
    private final ModeSetting headPriority = new ModeSetting("\u0413\u043e\u043b\u043e\u0432\u0430").values("\u0417\u0430\u0449\u0438\u0442\u0430", "\u0412\u0437\u0440\u044b\u0432").value("\u0417\u0430\u0449\u0438\u0442\u0430");
    private final ModeSetting bodyPriority = new ModeSetting("\u0422\u0435\u043b\u043e").values("\u0417\u0430\u0449\u0438\u0442\u0430", "\u0412\u0437\u0440\u044b\u0432").value("\u0417\u0430\u0449\u0438\u0442\u0430");
    private final ModeSetting legsPriority = new ModeSetting("\u041d\u043e\u0433\u0438").values("\u0417\u0430\u0449\u0438\u0442\u0430", "\u0412\u0437\u0440\u044b\u0432").value("\u0417\u0430\u0449\u0438\u0442\u0430");
    private final ModeSetting feetPriority = new ModeSetting("\u0411\u043e\u0442\u0438\u043d\u043a\u0438").values("\u0417\u0430\u0449\u0438\u0442\u0430", "\u0412\u0437\u0440\u044b\u0432").value("\u0417\u0430\u0449\u0438\u0442\u0430");
    private final ModeSetting elytraPriority = new ModeSetting("\u041f\u0440\u0438\u043e\u0440\u0438\u0442\u0435\u0442 \u044d\u043b\u0438\u0442\u0440\u044b").values("\u041d\u0435\u0442", "\u0412\u0441\u0435\u0433\u0434\u0430", "\u0418\u0433\u043d\u043e\u0440").value("\u0418\u0433\u043d\u043e\u0440");
    private final SliderSetting delay = new SliderSetting("\u0417\u0430\u0434\u0435\u0440\u0436\u043a\u0430").value(Float.valueOf(5.0f)).range(0.0f, 10.0f).step(1.0f);
    private final BooleanSetting oldVersion = new BooleanSetting("\u0421\u0442\u0430\u0440\u044b\u0435 \u0432\u0435\u0440\u0441\u0438\u0438").value(false);
    private final BooleanSetting pauseInventory = new BooleanSetting("\u041f\u0430\u0443\u0437\u0430 \u0432 \u0438\u043d\u0432\u0435\u043d\u0442\u0430\u0440\u0435").value(false);
    private final BooleanSetting noMove = new BooleanSetting("\u041d\u0435 \u0434\u0432\u0438\u0433\u0430\u0442\u044c\u0441\u044f").value(false);
    private final BooleanSetting ignoreCurse = new BooleanSetting("\u0418\u0433\u043d\u043e\u0440 \u043f\u0440\u043e\u043a\u043b\u044f\u0442\u0438\u044f").value(true);
    private final BooleanSetting updatePacket = new BooleanSetting("\u041e\u0431\u043d\u043e\u0432\u0438\u0442\u044c \u043f\u0430\u043a\u0435\u0442").value(false);
    private final ModeSetting bypass = new ModeSetting("\u041e\u0431\u0445\u043e\u0434").values("\u0411\u0435\u0437", "\u0421\u043f\u0440\u0438\u043d\u0442", "\u041a\u043b\u0438\u0435\u043d\u0442\u0441\u043a\u0438\u0439").value("\u0411\u0435\u0437");
    private int tickDelay = 0;
    private final List<ArmorData> armorList = Arrays.asList(new ArmorData(class_1304.field_6166, 36), new ArmorData(class_1304.field_6172, 37), new ArmorData(class_1304.field_6174, 38), new ArmorData(class_1304.field_6169, 39));

    public AutoArmorModule() {
        this.addSettings(this.headPriority, this.bodyPriority, this.legsPriority, this.feetPriority, this.elytraPriority, this.delay, this.oldVersion, this.pauseInventory, this.noMove, this.ignoreCurse, this.updatePacket, this.bypass);
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (AutoArmorModule.mc.field_1724 == null || AutoArmorModule.mc.field_1687 == null) {
                return;
            }
            if (AutoArmorModule.mc.field_1755 != null && ((Boolean)this.pauseInventory.getValue()).booleanValue()) {
                return;
            }
            if (this.tickDelay-- > 0) {
                return;
            }
            this.armorList.forEach(ArmorData::reset);
            for (int i = 0; i < 36; ++i) {
                class_1799 stack = AutoArmorModule.mc.field_1724.method_31548().method_5438(i);
                int prot = this.getProtection(stack);
                if (prot <= 0) continue;
                for (ArmorData e : this.armorList) {
                    if (e.equipmentSlot != this.getEquipmentSlot(stack) || prot <= e.prevProtection || prot <= e.newProtection) continue;
                    e.newSlot = i;
                    e.newProtection = prot;
                }
            }
            for (ArmorData armorPiece : this.armorList) {
                int slot = armorPiece.newSlot;
                if (slot == -1) continue;
                if (!(armorPiece.prevProtection != -1 && ((Boolean)this.oldVersion.getValue()).booleanValue() || slot >= 9)) {
                    int prevSlot = AutoArmorModule.mc.field_1724.method_31548().field_7545;
                    InventoryUtil.swapToSlot(slot);
                    NetworkUtil.sendPacket(s -> new class_2886(class_1268.field_5808, s, AutoArmorModule.mc.field_1724.method_36454(), AutoArmorModule.mc.field_1724.method_36455()));
                    InventoryUtil.swapToSlot(prevSlot);
                } else {
                    if (MoveUtil.isMoving() && ((Boolean)this.noMove.getValue()).booleanValue()) {
                        return;
                    }
                    int newArmorSlot = slot < 9 ? 36 + slot : slot;
                    Runnable swapAction = () -> {
                        if (this.bypass.is("\u0421\u043f\u0440\u0438\u043d\u0442")) {
                            AutoArmorModule.mc.field_1724.field_3944.method_52787((class_2596)new class_2848((class_1297)AutoArmorModule.mc.field_1724, class_2848.class_2849.field_12985));
                        }
                        this.clickSlot(newArmorSlot);
                        this.clickSlot(armorPiece.armorSlot - 34 + (39 - armorPiece.armorSlot) * 2);
                        if (armorPiece.prevProtection != -1) {
                            this.clickSlot(newArmorSlot);
                        }
                        AutoArmorModule.mc.field_1724.field_3944.method_52787((class_2596)new class_2815(AutoArmorModule.mc.field_1724.field_7512.field_7763));
                        if (((Boolean)this.updatePacket.getValue()).booleanValue()) {
                            class_1799 armorStack = AutoArmorModule.mc.field_1724.method_31548().method_5438(armorPiece.armorSlot);
                            AutoArmorModule.mc.field_1724.method_31548().method_5447(armorPiece.armorSlot, armorStack);
                            AutoArmorModule.mc.field_1724.method_5673(armorPiece.equipmentSlot, armorStack);
                            AutoArmorModule.mc.field_1724.field_3944.method_52787((class_2596)new class_2868(AutoArmorModule.mc.field_1724.method_31548().field_7545));
                        }
                    };
                    if (this.bypass.is("\u041a\u043b\u0438\u0435\u043d\u0442\u0441\u043a\u0438\u0439")) {
                        SlownessManager.applySlowness(10L, swapAction);
                    } else {
                        swapAction.run();
                    }
                }
                this.tickDelay = ((Float)this.delay.getValue()).intValue();
                return;
            }
        }));
        this.addEvents(updateEvent);
    }

    private class_1304 getEquipmentSlot(class_1799 stack) {
        if (stack.method_7960() || !stack.method_57826(class_9334.field_54196)) {
            if (stack.method_31574(class_1802.field_8833)) {
                return class_1304.field_6174;
            }
            return null;
        }
        return ((class_10192)stack.method_57824(class_9334.field_54196)).comp_3174();
    }

    private boolean isElytraUsable(class_1799 stack) {
        return stack.method_31574(class_1802.field_8833) && stack.method_7919() < stack.method_7936() - 1;
    }

    private int getDefenseValue(class_1799 stack) {
        class_9285 mods = (class_9285)stack.method_57825(class_9334.field_49636, (Object)class_9285.field_49326);
        for (class_9285.class_9287 entry : mods.comp_2393()) {
            if (!entry.comp_2395().method_55838(class_5134.field_23724)) continue;
            return (int)entry.comp_2396().comp_2449();
        }
        return 0;
    }

    private int getToughnessValue(class_1799 stack) {
        class_9285 mods = (class_9285)stack.method_57825(class_9334.field_49636, (Object)class_9285.field_49326);
        for (class_9285.class_9287 entry : mods.comp_2393()) {
            if (!entry.comp_2395().method_55838(class_5134.field_23725)) continue;
            return (int)entry.comp_2396().comp_2449();
        }
        return 0;
    }

    private int getProtection(class_1799 is) {
        boolean isArmor = is.method_7909() instanceof class_1738;
        boolean isElytra = is.method_31574(class_1802.field_8833);
        if (!isArmor && !isElytra) {
            return is.method_7960() ? -1 : 0;
        }
        int prot = 0;
        class_1304 slot = this.getEquipmentSlot(is);
        if (isElytra) {
            if (!this.isElytraUsable(is)) {
                return 0;
            }
            if (this.elytraPriority.is("\u0412\u0441\u0435\u0433\u0434\u0430") || this.elytraPriority.is("\u0418\u0433\u043d\u043e\u0440") && this.isElytraUsable(AutoArmorModule.mc.field_1724.method_31548().method_5438(38))) {
                prot = 999;
            }
        }
        int blastMult = 1;
        int protMult = 1;
        if (slot == class_1304.field_6169) {
            if (this.headPriority.is("\u0417\u0430\u0449\u0438\u0442\u0430")) {
                protMult *= 2;
            } else {
                blastMult *= 2;
            }
        } else if (slot == class_1304.field_6174) {
            if (this.bodyPriority.is("\u0417\u0430\u0449\u0438\u0442\u0430")) {
                protMult *= 2;
            } else {
                blastMult *= 2;
            }
        } else if (slot == class_1304.field_6172) {
            if (this.legsPriority.is("\u0417\u0430\u0449\u0438\u0442\u0430")) {
                protMult *= 2;
            } else {
                blastMult *= 2;
            }
        } else if (slot == class_1304.field_6166) {
            if (this.feetPriority.is("\u0417\u0430\u0449\u0438\u0442\u0430")) {
                protMult *= 2;
            } else {
                blastMult *= 2;
            }
        }
        if (is.method_7942()) {
            class_9304 enchants = class_1890.method_57532((class_1799)is);
            for (Object2IntMap.Entry entry : enchants.method_57539()) {
                class_6880 enchEntry = (class_6880)entry.getKey();
                int level = entry.getIntValue();
                if (enchEntry.method_40225(class_1893.field_9111)) {
                    prot += level * protMult;
                    continue;
                }
                if (enchEntry.method_40225(class_1893.field_9107)) {
                    prot += level * blastMult;
                    continue;
                }
                if (!enchEntry.method_40225(class_1893.field_9113) || !((Boolean)this.ignoreCurse.getValue()).booleanValue()) continue;
                prot = -999;
            }
        }
        int baseProt = isArmor ? (this.getDefenseValue(is) + this.getToughnessValue(is)) * 10 : 0;
        return (baseProt + prot) * 10 + this.getMaterialValue(is);
    }

    private int getMaterialValue(class_1799 is) {
        class_1792 item = is.method_7909();
        if (item == class_1802.field_22027 || item == class_1802.field_22028 || item == class_1802.field_22029 || item == class_1802.field_22030) {
            return 5;
        }
        if (item == class_1802.field_8805 || item == class_1802.field_8058 || item == class_1802.field_8348 || item == class_1802.field_8285) {
            return 4;
        }
        if (item == class_1802.field_8743 || item == class_1802.field_8523 || item == class_1802.field_8396 || item == class_1802.field_8660) {
            return 3;
        }
        if (item == class_1802.field_8862 || item == class_1802.field_8678 || item == class_1802.field_8416 || item == class_1802.field_8753) {
            return 2;
        }
        if (item == class_1802.field_8267 || item == class_1802.field_8577 || item == class_1802.field_8570 || item == class_1802.field_8370) {
            return 1;
        }
        return 0;
    }

    private void clickSlot(int slot) {
        if (AutoArmorModule.mc.field_1724 == null || AutoArmorModule.mc.field_1761 == null) {
            return;
        }
        AutoArmorModule.mc.field_1761.method_2906(AutoArmorModule.mc.field_1724.field_7512.field_7763, slot, 0, class_1713.field_7790, (class_1657)AutoArmorModule.mc.field_1724);
    }

    @Generated
    public static AutoArmorModule getInstance() {
        return instance;
    }

    private class ArmorData {
        final class_1304 equipmentSlot;
        final int armorSlot;
        int prevProtection = -1;
        int newSlot = -1;
        int newProtection = -1;

        ArmorData(class_1304 equipmentSlot, int armorSlot) {
            this.equipmentSlot = equipmentSlot;
            this.armorSlot = armorSlot;
        }

        void reset() {
            this.prevProtection = AutoArmorModule.this.getProtection(QuickImports.mc.field_1724.method_31548().method_5438(this.armorSlot));
            this.newSlot = -1;
            this.newProtection = -1;
        }
    }
}

