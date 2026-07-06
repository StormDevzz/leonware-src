// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.player;

import sweetie.leonware.api.system.interfaces.QuickImports;
import net.minecraft.class_2886;
import net.minecraft.class_1268;
import lombok.Generated;
import net.minecraft.class_1657;
import net.minecraft.class_1713;
import net.minecraft.class_1792;
import net.minecraft.class_1887;
import net.minecraft.class_9304;
import net.minecraft.class_1893;
import net.minecraft.class_6880;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.class_1890;
import net.minecraft.class_1738;
import net.minecraft.class_5134;
import net.minecraft.class_9285;
import net.minecraft.class_1802;
import net.minecraft.class_10192;
import net.minecraft.class_9334;
import java.util.Iterator;
import net.minecraft.class_1799;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.utils.other.SlownessManager;
import net.minecraft.class_2868;
import net.minecraft.class_2815;
import net.minecraft.class_2596;
import net.minecraft.class_1297;
import net.minecraft.class_2848;
import sweetie.leonware.api.utils.player.MoveUtil;
import sweetie.leonware.api.utils.other.NetworkUtil;
import sweetie.leonware.api.utils.player.InventoryUtil;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.setting.Setting;
import java.util.Arrays;
import net.minecraft.class_1304;
import java.util.List;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Auto Armor", category = Category.PLAYER)
public class AutoArmorModule extends Module
{
    private static final AutoArmorModule instance;
    private final ModeSetting headPriority;
    private final ModeSetting bodyPriority;
    private final ModeSetting legsPriority;
    private final ModeSetting feetPriority;
    private final ModeSetting elytraPriority;
    private final SliderSetting delay;
    private final BooleanSetting oldVersion;
    private final BooleanSetting pauseInventory;
    private final BooleanSetting noMove;
    private final BooleanSetting ignoreCurse;
    private final BooleanSetting updatePacket;
    private final ModeSetting bypass;
    private int tickDelay;
    private final List<ArmorData> armorList;
    
    public AutoArmorModule() {
        this.headPriority = new ModeSetting("\u0413\u043e\u043b\u043e\u0432\u0430").values("\u0417\u0430\u0449\u0438\u0442\u0430", "\u0412\u0437\u0440\u044b\u0432").value("\u0417\u0430\u0449\u0438\u0442\u0430");
        this.bodyPriority = new ModeSetting("\u0422\u0435\u043b\u043e").values("\u0417\u0430\u0449\u0438\u0442\u0430", "\u0412\u0437\u0440\u044b\u0432").value("\u0417\u0430\u0449\u0438\u0442\u0430");
        this.legsPriority = new ModeSetting("\u041d\u043e\u0433\u0438").values("\u0417\u0430\u0449\u0438\u0442\u0430", "\u0412\u0437\u0440\u044b\u0432").value("\u0417\u0430\u0449\u0438\u0442\u0430");
        this.feetPriority = new ModeSetting("\u0411\u043e\u0442\u0438\u043d\u043a\u0438").values("\u0417\u0430\u0449\u0438\u0442\u0430", "\u0412\u0437\u0440\u044b\u0432").value("\u0417\u0430\u0449\u0438\u0442\u0430");
        this.elytraPriority = new ModeSetting("\u041f\u0440\u0438\u043e\u0440\u0438\u0442\u0435\u0442 \u044d\u043b\u0438\u0442\u0440\u044b").values("\u041d\u0435\u0442", "\u0412\u0441\u0435\u0433\u0434\u0430", "\u0418\u0433\u043d\u043e\u0440").value("\u0418\u0433\u043d\u043e\u0440");
        this.delay = new SliderSetting("\u0417\u0430\u0434\u0435\u0440\u0436\u043a\u0430").value(5.0f).range(0.0f, 10.0f).step(1.0f);
        this.oldVersion = new BooleanSetting("\u0421\u0442\u0430\u0440\u044b\u0435 \u0432\u0435\u0440\u0441\u0438\u0438").value(false);
        this.pauseInventory = new BooleanSetting("\u041f\u0430\u0443\u0437\u0430 \u0432 \u0438\u043d\u0432\u0435\u043d\u0442\u0430\u0440\u0435").value(false);
        this.noMove = new BooleanSetting("\u041d\u0435 \u0434\u0432\u0438\u0433\u0430\u0442\u044c\u0441\u044f").value(false);
        this.ignoreCurse = new BooleanSetting("\u0418\u0433\u043d\u043e\u0440 \u043f\u0440\u043e\u043a\u043b\u044f\u0442\u0438\u044f").value(true);
        this.updatePacket = new BooleanSetting("\u041e\u0431\u043d\u043e\u0432\u0438\u0442\u044c \u043f\u0430\u043a\u0435\u0442").value(false);
        this.bypass = new ModeSetting("\u041e\u0431\u0445\u043e\u0434").values("\u0411\u0435\u0437", "\u0421\u043f\u0440\u0438\u043d\u0442", "\u041a\u043b\u0438\u0435\u043d\u0442\u0441\u043a\u0438\u0439").value("\u0411\u0435\u0437");
        this.tickDelay = 0;
        this.armorList = Arrays.asList(new ArmorData(class_1304.field_6166, 36), new ArmorData(class_1304.field_6172, 37), new ArmorData(class_1304.field_6174, 38), new ArmorData(class_1304.field_6169, 39));
        this.addSettings(this.headPriority, this.bodyPriority, this.legsPriority, this.feetPriority, this.elytraPriority, this.delay, this.oldVersion, this.pauseInventory, this.noMove, this.ignoreCurse, this.updatePacket, this.bypass);
    }
    
    @Override
    public void onEvent() {
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (AutoArmorModule.mc.field_1724 == null || AutoArmorModule.mc.field_1687 == null) {
                return;
            }
            else if (AutoArmorModule.mc.field_1755 != null && this.pauseInventory.getValue()) {
                return;
            }
            else if (this.tickDelay-- > 0) {
                return;
            }
            else {
                this.armorList.forEach(ArmorData::reset);
                for (int i = 0; i < 36; ++i) {
                    final class_1799 stack = AutoArmorModule.mc.field_1724.method_31548().method_5438(i);
                    final int prot = this.getProtection(stack);
                    if (prot > 0) {
                        this.armorList.iterator();
                        final Iterator iterator;
                        while (iterator.hasNext()) {
                            final ArmorData e = iterator.next();
                            if (e.equipmentSlot == this.getEquipmentSlot(stack) && prot > e.prevProtection && prot > e.newProtection) {
                                e.newSlot = i;
                                e.newProtection = prot;
                            }
                        }
                    }
                }
                this.armorList.iterator();
                final Iterator iterator2;
                while (iterator2.hasNext()) {
                    final ArmorData armorPiece = iterator2.next();
                    final int slot = armorPiece.newSlot;
                    if (slot == -1) {
                        continue;
                    }
                    else {
                        if ((armorPiece.prevProtection == -1 || !this.oldVersion.getValue()) && slot < 9) {
                            final int prevSlot = AutoArmorModule.mc.field_1724.method_31548().field_7545;
                            InventoryUtil.swapToSlot(slot);
                            NetworkUtil.sendPacket(s -> new class_2886(class_1268.field_5808, s, AutoArmorModule.mc.field_1724.method_36454(), AutoArmorModule.mc.field_1724.method_36455()));
                            InventoryUtil.swapToSlot(prevSlot);
                        }
                        else if (MoveUtil.isMoving() && this.noMove.getValue()) {
                            return;
                        }
                        else {
                            final int newArmorSlot = (slot < 9) ? (36 + slot) : slot;
                            final Runnable swapAction = () -> {
                                if (this.bypass.is("\u0421\u043f\u0440\u0438\u043d\u0442")) {
                                    AutoArmorModule.mc.field_1724.field_3944.method_52787((class_2596)new class_2848((class_1297)AutoArmorModule.mc.field_1724, class_2848.class_2849.field_12985));
                                }
                                this.clickSlot(newArmorSlot);
                                this.clickSlot(armorPiece.armorSlot - 34 + (39 - armorPiece.armorSlot) * 2);
                                if (armorPiece.prevProtection != -1) {
                                    this.clickSlot(newArmorSlot);
                                }
                                AutoArmorModule.mc.field_1724.field_3944.method_52787((class_2596)new class_2815(AutoArmorModule.mc.field_1724.field_7512.field_7763));
                                if (this.updatePacket.getValue()) {
                                    final class_1799 armorStack = AutoArmorModule.mc.field_1724.method_31548().method_5438(armorPiece.armorSlot);
                                    AutoArmorModule.mc.field_1724.method_31548().method_5447(armorPiece.armorSlot, armorStack);
                                    AutoArmorModule.mc.field_1724.method_5673(armorPiece.equipmentSlot, armorStack);
                                    AutoArmorModule.mc.field_1724.field_3944.method_52787((class_2596)new class_2868(AutoArmorModule.mc.field_1724.method_31548().field_7545));
                                }
                                return;
                            };
                            if (this.bypass.is("\u041a\u043b\u0438\u0435\u043d\u0442\u0441\u043a\u0438\u0439")) {
                                SlownessManager.applySlowness(10L, swapAction);
                            }
                            else {
                                swapAction.run();
                            }
                        }
                        this.tickDelay = this.delay.getValue().intValue();
                    }
                }
                return;
            }
        }));
        this.addEvents(updateEvent);
    }
    
    private class_1304 getEquipmentSlot(final class_1799 stack) {
        if (!stack.method_7960() && stack.method_57826(class_9334.field_54196)) {
            return ((class_10192)stack.method_57824(class_9334.field_54196)).comp_3174();
        }
        if (stack.method_31574(class_1802.field_8833)) {
            return class_1304.field_6174;
        }
        return null;
    }
    
    private boolean isElytraUsable(final class_1799 stack) {
        return stack.method_31574(class_1802.field_8833) && stack.method_7919() < stack.method_7936() - 1;
    }
    
    private int getDefenseValue(final class_1799 stack) {
        final class_9285 mods = (class_9285)stack.method_57825(class_9334.field_49636, (Object)class_9285.field_49326);
        for (final class_9285.class_9287 entry : mods.comp_2393()) {
            if (entry.comp_2395().method_55838(class_5134.field_23724)) {
                return (int)entry.comp_2396().comp_2449();
            }
        }
        return 0;
    }
    
    private int getToughnessValue(final class_1799 stack) {
        final class_9285 mods = (class_9285)stack.method_57825(class_9334.field_49636, (Object)class_9285.field_49326);
        for (final class_9285.class_9287 entry : mods.comp_2393()) {
            if (entry.comp_2395().method_55838(class_5134.field_23725)) {
                return (int)entry.comp_2396().comp_2449();
            }
        }
        return 0;
    }
    
    private int getProtection(final class_1799 is) {
        final boolean isArmor = is.method_7909() instanceof class_1738;
        final boolean isElytra = is.method_31574(class_1802.field_8833);
        if (!isArmor && !isElytra) {
            return is.method_7960() ? -1 : 0;
        }
        int prot = 0;
        final class_1304 slot = this.getEquipmentSlot(is);
        if (isElytra) {
            if (!this.isElytraUsable(is)) {
                return 0;
            }
            if (this.elytraPriority.is("\u0412\u0441\u0435\u0433\u0434\u0430") || (this.elytraPriority.is("\u0418\u0433\u043d\u043e\u0440") && this.isElytraUsable(AutoArmorModule.mc.field_1724.method_31548().method_5438(38)))) {
                prot = 999;
            }
        }
        int blastMult = 1;
        int protMult = 1;
        if (slot == class_1304.field_6169) {
            if (this.headPriority.is("\u0417\u0430\u0449\u0438\u0442\u0430")) {
                protMult *= 2;
            }
            else {
                blastMult *= 2;
            }
        }
        else if (slot == class_1304.field_6174) {
            if (this.bodyPriority.is("\u0417\u0430\u0449\u0438\u0442\u0430")) {
                protMult *= 2;
            }
            else {
                blastMult *= 2;
            }
        }
        else if (slot == class_1304.field_6172) {
            if (this.legsPriority.is("\u0417\u0430\u0449\u0438\u0442\u0430")) {
                protMult *= 2;
            }
            else {
                blastMult *= 2;
            }
        }
        else if (slot == class_1304.field_6166) {
            if (this.feetPriority.is("\u0417\u0430\u0449\u0438\u0442\u0430")) {
                protMult *= 2;
            }
            else {
                blastMult *= 2;
            }
        }
        if (is.method_7942()) {
            final class_9304 enchants = class_1890.method_57532(is);
            for (final Object2IntMap.Entry<class_6880<class_1887>> entry : enchants.method_57539()) {
                final class_6880<class_1887> enchEntry = (class_6880<class_1887>)entry.getKey();
                final int level = entry.getIntValue();
                if (enchEntry.method_40225(class_1893.field_9111)) {
                    prot += level * protMult;
                }
                else if (enchEntry.method_40225(class_1893.field_9107)) {
                    prot += level * blastMult;
                }
                else {
                    if (!enchEntry.method_40225(class_1893.field_9113) || !this.ignoreCurse.getValue()) {
                        continue;
                    }
                    prot = -999;
                }
            }
        }
        final int baseProt = isArmor ? ((this.getDefenseValue(is) + this.getToughnessValue(is)) * 10) : 0;
        return (baseProt + prot) * 10 + this.getMaterialValue(is);
    }
    
    private int getMaterialValue(final class_1799 is) {
        final class_1792 item = is.method_7909();
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
    
    private void clickSlot(final int slot) {
        if (AutoArmorModule.mc.field_1724 == null || AutoArmorModule.mc.field_1761 == null) {
            return;
        }
        AutoArmorModule.mc.field_1761.method_2906(AutoArmorModule.mc.field_1724.field_7512.field_7763, slot, 0, class_1713.field_7790, (class_1657)AutoArmorModule.mc.field_1724);
    }
    
    @Generated
    public static AutoArmorModule getInstance() {
        return AutoArmorModule.instance;
    }
    
    static {
        instance = new AutoArmorModule();
    }
    
    private class ArmorData
    {
        final class_1304 equipmentSlot;
        final int armorSlot;
        int prevProtection;
        int newSlot;
        int newProtection;
        
        ArmorData(final class_1304 equipmentSlot, final int armorSlot) {
            this.prevProtection = -1;
            this.newSlot = -1;
            this.newProtection = -1;
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
