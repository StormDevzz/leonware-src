// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.combat;

import it.unimi.dsi.fastutil.ints.IntListIterator;
import lombok.Generated;
import net.minecraft.class_2371;
import net.minecraft.class_1802;
import net.minecraft.class_1799;
import net.minecraft.class_640;
import net.minecraft.class_745;
import java.util.UUID;
import java.nio.charset.StandardCharsets;
import net.minecraft.class_2596;
import java.util.Iterator;
import sweetie.leonware.api.event.EventListener;
import net.minecraft.class_2777;
import net.minecraft.class_2716;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.events.player.world.AttackEvent;
import sweetie.leonware.api.event.Listener;
import net.minecraft.class_1297;
import java.util.function.Consumer;
import net.minecraft.class_2561;
import net.minecraft.class_1309;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import java.util.HashMap;
import net.minecraft.class_1657;
import java.util.concurrent.CopyOnWriteArrayList;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Anti Bot", category = Category.COMBAT)
public class AntiBotModule extends Module
{
    private static final AntiBotModule instance;
    public static final CopyOnWriteArrayList<class_1657> bots;
    public static final CopyOnWriteArrayList<Integer> validatedNonBots;
    private final HashMap<Integer, Integer> tpCount;
    private final BooleanSetting remove;
    private final BooleanSetting onlyAura;
    private final ModeSetting mode;
    private final SliderSetting checkTicks;
    private final TimerUtil clearTimer;
    private int ticks;
    private int lastAttackedId;
    private int attackTicks;
    
    public AntiBotModule() {
        this.tpCount = new HashMap<Integer, Integer>();
        this.remove = new BooleanSetting("Remove").value(false);
        this.onlyAura = new BooleanSetting("Only Aura").value(true);
        this.mode = new ModeSetting("Mode").values("UUIDCheck", "MotionCheck", "ZeroPing", "ReallyWorld", "Zamorozka", "LvmAC").value("UUIDCheck");
        this.checkTicks = new SliderSetting("Check Ticks").value(3.0f).range(0.0f, 10.0f).step(1.0f).setVisible(() -> this.mode.is("MotionCheck"));
        this.clearTimer = new TimerUtil();
        this.ticks = 0;
        this.lastAttackedId = -1;
        this.attackTicks = 0;
        this.addSettings(this.remove, this.onlyAura, this.mode, this.checkTicks);
    }
    
    @Override
    public void onDisable() {
        AntiBotModule.bots.clear();
        AntiBotModule.validatedNonBots.clear();
        this.tpCount.clear();
        this.ticks = 0;
        this.lastAttackedId = -1;
    }
    
    @Override
    public void onEvent() {
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (AntiBotModule.mc.field_1724 == null || AntiBotModule.mc.field_1687 == null) {
                return;
            }
            else {
                if (this.mode.is("Zamorozka")) {
                    if (this.lastAttackedId != -1) {
                        ++this.attackTicks;
                        final class_1297 entity = AntiBotModule.mc.field_1687.method_8469(this.lastAttackedId);
                        if (entity instanceof final class_1309 living) {
                            if (living.field_6235 > 0) {
                                if (!AntiBotModule.validatedNonBots.contains(this.lastAttackedId)) {
                                    AntiBotModule.validatedNonBots.add(this.lastAttackedId);
                                    AntiBotModule.mc.field_1724.method_7353(class_2561.method_30163("§c[AntiBot] §a" + living.method_5477().getString() + " \u0437\u0430\u0440\u0435\u0433\u0438\u0441\u0442\u0440\u0438\u0440\u043e\u0432\u0430\u043d \u043a\u0430\u043a \u0438\u0433\u0440\u043e\u043a!"), false);
                                }
                                this.lastAttackedId = -1;
                            }
                        }
                        if (this.attackTicks > 10) {
                            this.lastAttackedId = -1;
                        }
                    }
                }
                else {
                    if (this.onlyAura.getValue()) {
                        final class_1309 target = AuraModule.getInstance().target;
                        if (target instanceof final class_1657 p) {
                            this.markAsBot(p);
                        }
                    }
                    else {
                        AntiBotModule.mc.field_1687.method_18456().forEach(this::markAsBot);
                    }
                    if (this.remove.getValue()) {
                        AntiBotModule.bots.iterator();
                        final Iterator iterator;
                        while (iterator.hasNext()) {
                            final class_1657 b = iterator.next();
                            try {
                                if (AntiBotModule.mc.field_1687.method_8469(b.method_5628()) != null) {
                                    AntiBotModule.mc.field_1687.method_2945(b.method_5628(), class_1297.class_5529.field_26998);
                                }
                                else {
                                    continue;
                                }
                            }
                            catch (final Exception ex) {}
                        }
                    }
                }
                if (this.clearTimer.finished(10000L)) {
                    AntiBotModule.bots.clear();
                    if (!this.mode.is("Zamorozka")) {
                        AntiBotModule.validatedNonBots.clear();
                    }
                    this.ticks = 0;
                    this.clearTimer.reset();
                    this.tpCount.clear();
                }
                return;
            }
        }));
        final EventListener attackEvent = AttackEvent.getInstance().subscribe(new Listener<AttackEvent.AttackEventData>(event -> {
            if (this.mode.is("Zamorozka") && AntiBotModule.mc.field_1692 != null) {
                this.lastAttackedId = AntiBotModule.mc.field_1692.method_5628();
                this.attackTicks = 0;
            }
            return;
        }));
        final EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(eventData -> {
            if (this.mode.is("LvmAC")) {
                final class_2596 patt0$temp = eventData.packet();
                if (patt0$temp instanceof final class_2716 entitiesDestroyS2CPacket) {
                    entitiesDestroyS2CPacket.method_36548().iterator();
                    final Iterator iterator2;
                    while (iterator2.hasNext()) {
                        final int id = (int)iterator2.next();
                        if (this.tpCount.containsKey(id)) {
                            this.tpCount.clear();
                        }
                    }
                }
                final class_2596 patt1$temp = eventData.packet();
                if (patt1$temp instanceof final class_2777 packet) {
                    if (!AntiBotModule.bots.isEmpty()) {
                        AntiBotModule.bots.iterator();
                        final Iterator iterator3;
                        while (iterator3.hasNext()) {
                            final class_1657 entity2 = iterator3.next();
                            if (entity2.method_5628() == packet.comp_3237()) {
                                return;
                            }
                        }
                    }
                    final int tp = this.tpCount.getOrDefault(packet.comp_3237(), 0);
                    if (tp > 9) {
                        try {
                            final class_1297 entity3 = AntiBotModule.mc.field_1687.method_8469(packet.comp_3237());
                            if (entity3 instanceof final class_1657 playerEntity) {
                                this.addBot(playerEntity);
                            }
                        }
                        catch (final NullPointerException ex2) {}
                    }
                    this.tpCount.put(packet.comp_3237(), tp + 1);
                }
            }
            return;
        }));
        this.addEvents(updateEvent, attackEvent, packetEvent);
    }
    
    private void markAsBot(final class_1657 ent) {
        if (AntiBotModule.bots.contains(ent)) {
            return;
        }
        final String s = this.mode.getValue();
        switch (s) {
            case "UUIDCheck": {
                if (!ent.method_5667().equals(UUID.nameUUIDFromBytes(("OfflinePlayer:" + ent.method_5477().getString()).getBytes(StandardCharsets.UTF_8))) && ent instanceof class_745 && !ent.method_5477().getString().contains("-")) {
                    this.addBot(ent);
                    break;
                }
                break;
            }
            case "MotionCheck": {
                final double diffX = ent.method_23317() - ent.field_6014;
                final double diffZ = ent.method_23321() - ent.field_5969;
                if (diffX * diffX + diffZ * diffZ > 0.5) {
                    if (this.ticks >= this.checkTicks.getValue().intValue()) {
                        this.addBot(ent);
                    }
                    ++this.ticks;
                }
                break;
            }
            case "ZeroPing": {
                if (AntiBotModule.mc.field_1724.field_3944 == null) {
                    return;
                }
                final class_640 entry = AntiBotModule.mc.field_1724.field_3944.method_2871(ent.method_5667());
                if (entry != null && entry.method_2959() <= 0) {
                    this.addBot(ent);
                }
                break;
            }
            case "ReallyWorld": {
                if (this.isReallyWorldBot(ent)) {
                    if (!AntiBotModule.bots.contains(ent)) {
                        this.addBot(ent);
                        break;
                    }
                    break;
                }
                else {
                    if (AntiBotModule.bots.contains(ent)) {
                        AntiBotModule.bots.remove(ent);
                        break;
                    }
                    break;
                }
                break;
            }
        }
    }
    
    private boolean isReallyWorldBot(final class_1657 entity) {
        if (entity.method_31548() == null) {
            return false;
        }
        final class_2371<class_1799> armor = (class_2371<class_1799>)entity.method_31548().field_7548;
        if (armor.size() < 4) {
            return false;
        }
        final class_1799 boots = (class_1799)armor.get(0);
        final class_1799 leggings = (class_1799)armor.get(1);
        final class_1799 chestplate = (class_1799)armor.get(2);
        final class_1799 helmet = (class_1799)armor.get(3);
        if (boots.method_7960() || leggings.method_7960() || chestplate.method_7960() || helmet.method_7960()) {
            return false;
        }
        if (!boots.method_7923() || !leggings.method_7923() || !chestplate.method_7923() || !helmet.method_7923()) {
            return false;
        }
        if (!entity.method_6079().method_7960()) {
            return false;
        }
        final boolean hasValidArmor = boots.method_31574(class_1802.field_8370) || leggings.method_31574(class_1802.field_8570) || chestplate.method_31574(class_1802.field_8577) || helmet.method_31574(class_1802.field_8267) || boots.method_31574(class_1802.field_8660) || leggings.method_31574(class_1802.field_8396) || chestplate.method_31574(class_1802.field_8523) || helmet.method_31574(class_1802.field_8743) || boots.method_31574(class_1802.field_8285) || leggings.method_31574(class_1802.field_8348) || chestplate.method_31574(class_1802.field_8058) || helmet.method_31574(class_1802.field_8805);
        if (!hasValidArmor) {
            return false;
        }
        final class_1799 mainHand = entity.method_6047();
        final boolean hasValidMainHand = mainHand.method_7960() || mainHand.method_31574(class_1802.field_8802) || mainHand.method_31574(class_1802.field_8371) || mainHand.method_31574(class_1802.field_8378) || mainHand.method_31574(class_1802.field_8475) || mainHand.method_31574(class_1802.field_8556) || mainHand.method_31574(class_1802.field_8403) || mainHand.method_31574(class_1802.field_8377) || mainHand.method_31574(class_1802.field_8810);
        return hasValidMainHand && !boots.method_7986() && !leggings.method_7986() && !chestplate.method_7986() && !helmet.method_7986() && entity.method_7344() != null && entity.method_7344().method_7586() == 20;
    }
    
    private void addBot(final class_1657 entity) {
        if (AntiBotModule.mc.field_1724 != null) {
            AntiBotModule.mc.field_1724.method_7353(class_2561.method_30163("§c[AntiBot] §f" + entity.method_5477().getString() + " is a bot!"), false);
        }
        AntiBotModule.bots.add(entity);
    }
    
    public static boolean checkBot(final class_1657 entity) {
        if (AntiBotModule.instance.isEnabled() && AntiBotModule.instance.getMode().is("Zamorozka")) {
            return !AntiBotModule.validatedNonBots.contains(entity.method_5628());
        }
        return AntiBotModule.bots.contains(entity);
    }
    
    @Generated
    public static AntiBotModule getInstance() {
        return AntiBotModule.instance;
    }
    
    @Generated
    public ModeSetting getMode() {
        return this.mode;
    }
    
    static {
        instance = new AntiBotModule();
        bots = new CopyOnWriteArrayList<class_1657>();
        validatedNonBots = new CopyOnWriteArrayList<Integer>();
    }
}
