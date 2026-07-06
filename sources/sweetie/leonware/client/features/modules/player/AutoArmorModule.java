package sweetie.leonware.client.features.modules.player;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.Arrays;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_10192;
import net.minecraft.class_1268;
import net.minecraft.class_1304;
import net.minecraft.class_1713;
import net.minecraft.class_1738;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1887;
import net.minecraft.class_1890;
import net.minecraft.class_1893;
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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/player/AutoArmorModule.class */
@ModuleRegister(name = "Auto Armor", category = Category.PLAYER)
public class AutoArmorModule extends Module {
    private static final AutoArmorModule instance = new AutoArmorModule();
    private final ModeSetting headPriority = new ModeSetting("Голова").values("Защита", "Взрыв").value("Защита");
    private final ModeSetting bodyPriority = new ModeSetting("Тело").values("Защита", "Взрыв").value("Защита");
    private final ModeSetting legsPriority = new ModeSetting("Ноги").values("Защита", "Взрыв").value("Защита");
    private final ModeSetting feetPriority = new ModeSetting("Ботинки").values("Защита", "Взрыв").value("Защита");
    private final ModeSetting elytraPriority = new ModeSetting("Приоритет элитры").values("Нет", "Всегда", "Игнор").value("Игнор");
    private final SliderSetting delay = new SliderSetting("Задержка").value(Float.valueOf(5.0f)).range(0.0f, 10.0f).step(1.0f);
    private final BooleanSetting oldVersion = new BooleanSetting("Старые версии").value((Boolean) false);
    private final BooleanSetting pauseInventory = new BooleanSetting("Пауза в инвентаре").value((Boolean) false);
    private final BooleanSetting noMove = new BooleanSetting("Не двигаться").value((Boolean) false);
    private final BooleanSetting ignoreCurse = new BooleanSetting("Игнор проклятия").value((Boolean) true);
    private final BooleanSetting updatePacket = new BooleanSetting("Обновить пакет").value((Boolean) false);
    private final ModeSetting bypass = new ModeSetting("Обход").values("Без", "Спринт", "Клиентский").value("Без");
    private int tickDelay = 0;
    private final List<ArmorData> armorList = Arrays.asList(new ArmorData(class_1304.field_6166, 36), new ArmorData(class_1304.field_6172, 37), new ArmorData(class_1304.field_6174, 38), new ArmorData(class_1304.field_6169, 39));

    @Generated
    public static AutoArmorModule getInstance() {
        return instance;
    }

    public AutoArmorModule() {
        addSettings(this.headPriority, this.bodyPriority, this.legsPriority, this.feetPriority, this.elytraPriority, this.delay, this.oldVersion, this.pauseInventory, this.noMove, this.ignoreCurse, this.updatePacket, this.bypass);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1724 == null || mc.field_1687 == null) {
                return;
            }
            if (mc.field_1755 == null || !this.pauseInventory.getValue().booleanValue()) {
                int i = this.tickDelay;
                this.tickDelay = i - 1;
                if (i > 0) {
                    return;
                }
                this.armorList.forEach((v0) -> {
                    v0.reset();
                });
                for (int i2 = 0; i2 < 36; i2++) {
                    class_1799 stack = mc.field_1724.method_31548().method_5438(i2);
                    int prot = getProtection(stack);
                    if (prot > 0) {
                        for (ArmorData e : this.armorList) {
                            if (e.equipmentSlot == getEquipmentSlot(stack) && prot > e.prevProtection && prot > e.newProtection) {
                                e.newSlot = i2;
                                e.newProtection = prot;
                            }
                        }
                    }
                }
                for (ArmorData armorPiece : this.armorList) {
                    int slot = armorPiece.newSlot;
                    if (slot != -1) {
                        if ((armorPiece.prevProtection == -1 || !this.oldVersion.getValue().booleanValue()) && slot < 9) {
                            int prevSlot = mc.field_1724.method_31548().field_7545;
                            InventoryUtil.swapToSlot(slot);
                            NetworkUtil.sendPacket(s -> {
                                return new class_2886(class_1268.field_5808, s, mc.field_1724.method_36454(), mc.field_1724.method_36455());
                            });
                            InventoryUtil.swapToSlot(prevSlot);
                        } else {
                            if (MoveUtil.isMoving() && this.noMove.getValue().booleanValue()) {
                                return;
                            }
                            int newArmorSlot = slot < 9 ? 36 + slot : slot;
                            Runnable swapAction = () -> {
                                if (this.bypass.is("Спринт")) {
                                    mc.field_1724.field_3944.method_52787(new class_2848(mc.field_1724, class_2848.class_2849.field_12985));
                                }
                                clickSlot(newArmorSlot);
                                clickSlot((armorPiece.armorSlot - 34) + ((39 - armorPiece.armorSlot) * 2));
                                if (armorPiece.prevProtection != -1) {
                                    clickSlot(newArmorSlot);
                                }
                                mc.field_1724.field_3944.method_52787(new class_2815(mc.field_1724.field_7512.field_7763));
                                if (this.updatePacket.getValue().booleanValue()) {
                                    class_1799 armorStack = mc.field_1724.method_31548().method_5438(armorPiece.armorSlot);
                                    mc.field_1724.method_31548().method_5447(armorPiece.armorSlot, armorStack);
                                    mc.field_1724.method_5673(armorPiece.equipmentSlot, armorStack);
                                    mc.field_1724.field_3944.method_52787(new class_2868(mc.field_1724.method_31548().field_7545));
                                }
                            };
                            if (this.bypass.is("Клиентский")) {
                                SlownessManager.applySlowness(10L, swapAction);
                            } else {
                                swapAction.run();
                            }
                        }
                        this.tickDelay = this.delay.getValue().intValue();
                        return;
                    }
                }
            }
        }));
        addEvents(updateEvent);
    }

    private class_1304 getEquipmentSlot(class_1799 stack) {
        if (stack.method_7960() || !stack.method_57826(class_9334.field_54196)) {
            if (stack.method_31574(class_1802.field_8833)) {
                return class_1304.field_6174;
            }
            return null;
        }
        return ((class_10192) stack.method_57824(class_9334.field_54196)).comp_3174();
    }

    private boolean isElytraUsable(class_1799 stack) {
        return stack.method_31574(class_1802.field_8833) && stack.method_7919() < stack.method_7936() - 1;
    }

    private int getDefenseValue(class_1799 stack) {
        class_9285 mods = (class_9285) stack.method_57825(class_9334.field_49636, class_9285.field_49326);
        for (class_9285.class_9287 entry : mods.comp_2393()) {
            if (entry.comp_2395().method_55838(class_5134.field_23724)) {
                return (int) entry.comp_2396().comp_2449();
            }
        }
        return 0;
    }

    private int getToughnessValue(class_1799 stack) {
        class_9285 mods = (class_9285) stack.method_57825(class_9334.field_49636, class_9285.field_49326);
        for (class_9285.class_9287 entry : mods.comp_2393()) {
            if (entry.comp_2395().method_55838(class_5134.field_23725)) {
                return (int) entry.comp_2396().comp_2449();
            }
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
        class_1304 slot = getEquipmentSlot(is);
        if (isElytra) {
            if (!isElytraUsable(is)) {
                return 0;
            }
            if (this.elytraPriority.is("Всегда") || (this.elytraPriority.is("Игнор") && isElytraUsable(mc.field_1724.method_31548().method_5438(38)))) {
                prot = 999;
            }
        }
        int blastMult = 1;
        int protMult = 1;
        if (slot == class_1304.field_6169) {
            if (!this.headPriority.is("Защита")) {
                blastMult = 1 * 2;
            } else {
                protMult = 1 * 2;
            }
        } else if (slot == class_1304.field_6174) {
            if (!this.bodyPriority.is("Защита")) {
                blastMult = 1 * 2;
            } else {
                protMult = 1 * 2;
            }
        } else if (slot == class_1304.field_6172) {
            if (!this.legsPriority.is("Защита")) {
                blastMult = 1 * 2;
            } else {
                protMult = 1 * 2;
            }
        } else if (slot == class_1304.field_6166) {
            if (!this.feetPriority.is("Защита")) {
                blastMult = 1 * 2;
            } else {
                protMult = 1 * 2;
            }
        }
        if (is.method_7942()) {
            class_9304 enchants = class_1890.method_57532(is);
            for (Object2IntMap.Entry<class_6880<class_1887>> entry : enchants.method_57539()) {
                class_6880<class_1887> enchEntry = (class_6880) entry.getKey();
                int level = entry.getIntValue();
                if (enchEntry.method_40225(class_1893.field_9111)) {
                    prot += level * protMult;
                } else if (enchEntry.method_40225(class_1893.field_9107)) {
                    prot += level * blastMult;
                } else if (enchEntry.method_40225(class_1893.field_9113) && this.ignoreCurse.getValue().booleanValue()) {
                    prot = -999;
                }
            }
        }
        int baseProt = isArmor ? (getDefenseValue(is) + getToughnessValue(is)) * 10 : 0;
        return ((baseProt + prot) * 10) + getMaterialValue(is);
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
        return (item == class_1802.field_8267 || item == class_1802.field_8577 || item == class_1802.field_8570 || item == class_1802.field_8370) ? 1 : 0;
    }

    private void clickSlot(int slot) {
        if (mc.field_1724 == null || mc.field_1761 == null) {
            return;
        }
        mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, slot, 0, class_1713.field_7790, mc.field_1724);
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/player/AutoArmorModule$ArmorData.class */
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
