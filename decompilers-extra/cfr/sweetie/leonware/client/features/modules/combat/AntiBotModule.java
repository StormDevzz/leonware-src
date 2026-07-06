/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.ints.IntListIterator
 *  lombok.Generated
 *  net.minecraft.class_1297
 *  net.minecraft.class_1297$class_5529
 *  net.minecraft.class_1309
 *  net.minecraft.class_1657
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_2371
 *  net.minecraft.class_2561
 *  net.minecraft.class_2716
 *  net.minecraft.class_2777
 *  net.minecraft.class_640
 *  net.minecraft.class_745
 */
package sweetie.leonware.client.features.modules.combat;

import it.unimi.dsi.fastutil.ints.IntListIterator;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.Generated;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2371;
import net.minecraft.class_2561;
import net.minecraft.class_2716;
import net.minecraft.class_2777;
import net.minecraft.class_640;
import net.minecraft.class_745;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.event.events.player.world.AttackEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.client.features.modules.combat.AuraModule;

@ModuleRegister(name="Anti Bot", category=Category.COMBAT)
public class AntiBotModule
extends Module {
    private static final AntiBotModule instance = new AntiBotModule();
    public static final CopyOnWriteArrayList<class_1657> bots = new CopyOnWriteArrayList();
    public static final CopyOnWriteArrayList<Integer> validatedNonBots = new CopyOnWriteArrayList();
    private final HashMap<Integer, Integer> tpCount = new HashMap();
    private final BooleanSetting remove = new BooleanSetting("Remove").value(false);
    private final BooleanSetting onlyAura = new BooleanSetting("Only Aura").value(true);
    private final ModeSetting mode = new ModeSetting("Mode").values("UUIDCheck", "MotionCheck", "ZeroPing", "ReallyWorld", "Zamorozka", "LvmAC").value("UUIDCheck");
    private final SliderSetting checkTicks = new SliderSetting("Check Ticks").value(Float.valueOf(3.0f)).range(0.0f, 10.0f).step(1.0f).setVisible(() -> this.mode.is("MotionCheck"));
    private final TimerUtil clearTimer = new TimerUtil();
    private int ticks = 0;
    private int lastAttackedId = -1;
    private int attackTicks = 0;

    public AntiBotModule() {
        this.addSettings(this.remove, this.onlyAura, this.mode, this.checkTicks);
    }

    @Override
    public void onDisable() {
        bots.clear();
        validatedNonBots.clear();
        this.tpCount.clear();
        this.ticks = 0;
        this.lastAttackedId = -1;
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            block14: {
                block13: {
                    if (AntiBotModule.mc.field_1724 == null || AntiBotModule.mc.field_1687 == null) {
                        return;
                    }
                    if (!this.mode.is("Zamorozka")) break block13;
                    if (this.lastAttackedId == -1) break block14;
                    ++this.attackTicks;
                    class_1297 entity = AntiBotModule.mc.field_1687.method_8469(this.lastAttackedId);
                    if (entity instanceof class_1309) {
                        class_1309 living = (class_1309)entity;
                        if (living.field_6235 > 0) {
                            if (!validatedNonBots.contains(this.lastAttackedId)) {
                                validatedNonBots.add(this.lastAttackedId);
                                AntiBotModule.mc.field_1724.method_7353(class_2561.method_30163((String)("\u00a7c[AntiBot] \u00a7a" + living.method_5477().getString() + " \u0437\u0430\u0440\u0435\u0433\u0438\u0441\u0442\u0440\u0438\u0440\u043e\u0432\u0430\u043d \u043a\u0430\u043a \u0438\u0433\u0440\u043e\u043a!")), false);
                            }
                            this.lastAttackedId = -1;
                        }
                    }
                    if (this.attackTicks <= 10) break block14;
                    this.lastAttackedId = -1;
                    break block14;
                }
                if (((Boolean)this.onlyAura.getValue()).booleanValue()) {
                    class_1309 target = AuraModule.getInstance().target;
                    if (target instanceof class_1657) {
                        class_1657 p = (class_1657)target;
                        this.markAsBot(p);
                    }
                } else {
                    AntiBotModule.mc.field_1687.method_18456().forEach(this::markAsBot);
                }
                if (((Boolean)this.remove.getValue()).booleanValue()) {
                    for (class_1657 b : bots) {
                        try {
                            if (AntiBotModule.mc.field_1687.method_8469(b.method_5628()) == null) continue;
                            AntiBotModule.mc.field_1687.method_2945(b.method_5628(), class_1297.class_5529.field_26998);
                        }
                        catch (Exception exception) {}
                    }
                }
            }
            if (this.clearTimer.finished(10000L)) {
                bots.clear();
                if (!this.mode.is("Zamorozka")) {
                    validatedNonBots.clear();
                }
                this.ticks = 0;
                this.clearTimer.reset();
                this.tpCount.clear();
            }
        }));
        EventListener attackEvent = AttackEvent.getInstance().subscribe(new Listener<AttackEvent.AttackEventData>(event -> {
            if (this.mode.is("Zamorozka") && AntiBotModule.mc.field_1692 != null) {
                this.lastAttackedId = AntiBotModule.mc.field_1692.method_5628();
                this.attackTicks = 0;
            }
        }));
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(eventData -> {
            if (this.mode.is("LvmAC")) {
                Object patt1$temp;
                IntListIterator patt0$temp = eventData.packet();
                if (patt0$temp instanceof class_2716) {
                    class_2716 entitiesDestroyS2CPacket = (class_2716)patt0$temp;
                    patt0$temp = entitiesDestroyS2CPacket.method_36548().iterator();
                    while (patt0$temp.hasNext()) {
                        int id = (Integer)patt0$temp.next();
                        if (!this.tpCount.containsKey(id)) continue;
                        this.tpCount.clear();
                    }
                }
                if ((patt1$temp = eventData.packet()) instanceof class_2777) {
                    int tp;
                    class_2777 packet = (class_2777)patt1$temp;
                    if (!bots.isEmpty()) {
                        for (class_1657 entity : bots) {
                            if (entity.method_5628() != packet.comp_3237()) continue;
                            return;
                        }
                    }
                    if ((tp = this.tpCount.getOrDefault(packet.comp_3237(), 0).intValue()) > 9) {
                        try {
                            class_1297 entity = AntiBotModule.mc.field_1687.method_8469(packet.comp_3237());
                            if (entity instanceof class_1657) {
                                class_1657 playerEntity = (class_1657)entity;
                                this.addBot(playerEntity);
                            }
                        }
                        catch (NullPointerException nullPointerException) {
                            // empty catch block
                        }
                    }
                    this.tpCount.put(packet.comp_3237(), tp + 1);
                }
            }
        }));
        this.addEvents(updateEvent, attackEvent, packetEvent);
    }

    private void markAsBot(class_1657 ent) {
        if (bots.contains(ent)) {
            return;
        }
        switch ((String)this.mode.getValue()) {
            case "UUIDCheck": {
                if (ent.method_5667().equals(UUID.nameUUIDFromBytes(("OfflinePlayer:" + ent.method_5477().getString()).getBytes(StandardCharsets.UTF_8))) || !(ent instanceof class_745) || ent.method_5477().getString().contains("-")) break;
                this.addBot(ent);
                break;
            }
            case "MotionCheck": {
                double diffX = ent.method_23317() - ent.field_6014;
                double diffZ = ent.method_23321() - ent.field_5969;
                if (!(diffX * diffX + diffZ * diffZ > 0.5)) break;
                if (this.ticks >= ((Float)this.checkTicks.getValue()).intValue()) {
                    this.addBot(ent);
                }
                ++this.ticks;
                break;
            }
            case "ZeroPing": {
                if (AntiBotModule.mc.field_1724.field_3944 == null) {
                    return;
                }
                class_640 entry = AntiBotModule.mc.field_1724.field_3944.method_2871(ent.method_5667());
                if (entry == null || entry.method_2959() > 0) break;
                this.addBot(ent);
                break;
            }
            case "ReallyWorld": {
                if (this.isReallyWorldBot(ent)) {
                    if (bots.contains(ent)) break;
                    this.addBot(ent);
                    break;
                }
                if (!bots.contains(ent)) break;
                bots.remove(ent);
            }
        }
    }

    private boolean isReallyWorldBot(class_1657 entity) {
        boolean hasValidMainHand;
        boolean hasValidArmor;
        if (entity.method_31548() == null) {
            return false;
        }
        class_2371 armor = entity.method_31548().field_7548;
        if (armor.size() < 4) {
            return false;
        }
        class_1799 boots = (class_1799)armor.get(0);
        class_1799 leggings = (class_1799)armor.get(1);
        class_1799 chestplate = (class_1799)armor.get(2);
        class_1799 helmet = (class_1799)armor.get(3);
        if (boots.method_7960() || leggings.method_7960() || chestplate.method_7960() || helmet.method_7960()) {
            return false;
        }
        if (!(boots.method_7923() && leggings.method_7923() && chestplate.method_7923() && helmet.method_7923())) {
            return false;
        }
        if (!entity.method_6079().method_7960()) {
            return false;
        }
        boolean bl = hasValidArmor = boots.method_31574(class_1802.field_8370) || leggings.method_31574(class_1802.field_8570) || chestplate.method_31574(class_1802.field_8577) || helmet.method_31574(class_1802.field_8267) || boots.method_31574(class_1802.field_8660) || leggings.method_31574(class_1802.field_8396) || chestplate.method_31574(class_1802.field_8523) || helmet.method_31574(class_1802.field_8743) || boots.method_31574(class_1802.field_8285) || leggings.method_31574(class_1802.field_8348) || chestplate.method_31574(class_1802.field_8058) || helmet.method_31574(class_1802.field_8805);
        if (!hasValidArmor) {
            return false;
        }
        class_1799 mainHand = entity.method_6047();
        boolean bl2 = hasValidMainHand = mainHand.method_7960() || mainHand.method_31574(class_1802.field_8802) || mainHand.method_31574(class_1802.field_8371) || mainHand.method_31574(class_1802.field_8378) || mainHand.method_31574(class_1802.field_8475) || mainHand.method_31574(class_1802.field_8556) || mainHand.method_31574(class_1802.field_8403) || mainHand.method_31574(class_1802.field_8377) || mainHand.method_31574(class_1802.field_8810);
        if (!hasValidMainHand) {
            return false;
        }
        if (boots.method_7986() || leggings.method_7986() || chestplate.method_7986() || helmet.method_7986()) {
            return false;
        }
        return entity.method_7344() != null && entity.method_7344().method_7586() == 20;
    }

    private void addBot(class_1657 entity) {
        if (AntiBotModule.mc.field_1724 != null) {
            AntiBotModule.mc.field_1724.method_7353(class_2561.method_30163((String)("\u00a7c[AntiBot] \u00a7f" + entity.method_5477().getString() + " is a bot!")), false);
        }
        bots.add(entity);
    }

    public static boolean checkBot(class_1657 entity) {
        if (instance.isEnabled() && instance.getMode().is("Zamorozka")) {
            return !validatedNonBots.contains(entity.method_5628());
        }
        return bots.contains(entity);
    }

    @Generated
    public static AntiBotModule getInstance() {
        return instance;
    }

    @Generated
    public ModeSetting getMode() {
        return this.mode;
    }
}

