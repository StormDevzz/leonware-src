package sweetie.leonware.client.features.modules.combat;

import it.unimi.dsi.fastutil.ints.IntListIterator;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/AntiBotModule.class */
@ModuleRegister(name = "Anti Bot", category = Category.COMBAT)
public class AntiBotModule extends Module {
    private static final AntiBotModule instance = new AntiBotModule();
    public static final CopyOnWriteArrayList<class_1657> bots = new CopyOnWriteArrayList<>();
    public static final CopyOnWriteArrayList<Integer> validatedNonBots = new CopyOnWriteArrayList<>();
    private final HashMap<Integer, Integer> tpCount = new HashMap<>();
    private final BooleanSetting remove = new BooleanSetting("Remove").value((Boolean) false);
    private final BooleanSetting onlyAura = new BooleanSetting("Only Aura").value((Boolean) true);
    private final ModeSetting mode = new ModeSetting("Mode").values("UUIDCheck", "MotionCheck", "ZeroPing", "ReallyWorld", "Zamorozka", "LvmAC").value("UUIDCheck");
    private final SliderSetting checkTicks = new SliderSetting("Check Ticks").value(Float.valueOf(3.0f)).range(0.0f, 10.0f).step(1.0f).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("MotionCheck"));
    });
    private final TimerUtil clearTimer = new TimerUtil();
    private int ticks = 0;
    private int lastAttackedId = -1;
    private int attackTicks = 0;

    @Generated
    public static AntiBotModule getInstance() {
        return instance;
    }

    @Generated
    public ModeSetting getMode() {
        return this.mode;
    }

    /* JADX WARN: Type inference failed for: r1v12, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    public AntiBotModule() {
        addSettings(this.remove, this.onlyAura, this.mode, this.checkTicks);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        bots.clear();
        validatedNonBots.clear();
        this.tpCount.clear();
        this.ticks = 0;
        this.lastAttackedId = -1;
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1724 == null || mc.field_1687 == null) {
                return;
            }
            if (this.mode.is("Zamorozka")) {
                if (this.lastAttackedId != -1) {
                    this.attackTicks++;
                    class_1309 class_1309VarMethod_8469 = mc.field_1687.method_8469(this.lastAttackedId);
                    if (class_1309VarMethod_8469 instanceof class_1309) {
                        class_1309 living = class_1309VarMethod_8469;
                        if (living.field_6235 > 0) {
                            if (!validatedNonBots.contains(Integer.valueOf(this.lastAttackedId))) {
                                validatedNonBots.add(Integer.valueOf(this.lastAttackedId));
                                mc.field_1724.method_7353(class_2561.method_30163("§c[AntiBot] §a" + living.method_5477().getString() + " зарегистрирован как игрок!"), false);
                            }
                            this.lastAttackedId = -1;
                        }
                    }
                    if (this.attackTicks > 10) {
                        this.lastAttackedId = -1;
                    }
                }
            } else {
                if (this.onlyAura.getValue().booleanValue()) {
                    class_1309 target = AuraModule.getInstance().target;
                    if (target instanceof class_1657) {
                        class_1657 p = (class_1657) target;
                        markAsBot(p);
                    }
                } else {
                    mc.field_1687.method_18456().forEach((v1) -> {
                        markAsBot(v1);
                    });
                }
                if (this.remove.getValue().booleanValue()) {
                    for (class_1657 b : bots) {
                        try {
                            if (mc.field_1687.method_8469(b.method_5628()) != null) {
                                mc.field_1687.method_2945(b.method_5628(), class_1297.class_5529.field_26998);
                            }
                        } catch (Exception e) {
                        }
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
        EventListener attackEvent = AttackEvent.getInstance().subscribe(new Listener(event2 -> {
            if (this.mode.is("Zamorozka") && mc.field_1692 != null) {
                this.lastAttackedId = mc.field_1692.method_5628();
                this.attackTicks = 0;
            }
        }));
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener(eventData -> {
            if (this.mode.is("LvmAC")) {
                class_2716 class_2716VarPacket = eventData.packet();
                if (class_2716VarPacket instanceof class_2716) {
                    class_2716 entitiesDestroyS2CPacket = class_2716VarPacket;
                    IntListIterator it = entitiesDestroyS2CPacket.method_36548().iterator();
                    while (it.hasNext()) {
                        int id = ((Integer) it.next()).intValue();
                        if (this.tpCount.containsKey(Integer.valueOf(id))) {
                            this.tpCount.clear();
                        }
                    }
                }
                class_2777 class_2777VarPacket = eventData.packet();
                if (class_2777VarPacket instanceof class_2777) {
                    class_2777 packet = class_2777VarPacket;
                    if (!bots.isEmpty()) {
                        Iterator<class_1657> it2 = bots.iterator();
                        while (it2.hasNext()) {
                            if (it2.next().method_5628() == packet.comp_3237()) {
                                return;
                            }
                        }
                    }
                    int tp = this.tpCount.getOrDefault(Integer.valueOf(packet.comp_3237()), 0).intValue();
                    if (tp > 9) {
                        try {
                            class_1297 entity = mc.field_1687.method_8469(packet.comp_3237());
                            if (entity instanceof class_1657) {
                                class_1657 playerEntity = (class_1657) entity;
                                addBot(playerEntity);
                            }
                        } catch (NullPointerException e) {
                        }
                    }
                    this.tpCount.put(Integer.valueOf(packet.comp_3237()), Integer.valueOf(tp + 1));
                }
            }
        }));
        addEvents(updateEvent, attackEvent, packetEvent);
    }

    private void markAsBot(class_1657 ent) {
        class_640 entry;
        if (bots.contains(ent)) {
        }
        switch (this.mode.getValue()) {
            case "UUIDCheck":
                if (!ent.method_5667().equals(UUID.nameUUIDFromBytes(("OfflinePlayer:" + ent.method_5477().getString()).getBytes(StandardCharsets.UTF_8))) && (ent instanceof class_745) && !ent.method_5477().getString().contains("-")) {
                    addBot(ent);
                    break;
                }
                break;
            case "MotionCheck":
                double diffX = ent.method_23317() - ent.field_6014;
                double diffZ = ent.method_23321() - ent.field_5969;
                if ((diffX * diffX) + (diffZ * diffZ) > 0.5d) {
                    if (this.ticks >= this.checkTicks.getValue().intValue()) {
                        addBot(ent);
                    }
                    this.ticks++;
                    break;
                }
                break;
            case "ZeroPing":
                if (mc.field_1724.field_3944 != null && (entry = mc.field_1724.field_3944.method_2871(ent.method_5667())) != null && entry.method_2959() <= 0) {
                    addBot(ent);
                    break;
                }
                break;
            case "ReallyWorld":
                if (isReallyWorldBot(ent)) {
                    if (!bots.contains(ent)) {
                        addBot(ent);
                    }
                    break;
                } else {
                    if (bots.contains(ent)) {
                        bots.remove(ent);
                    }
                    break;
                }
                break;
        }
    }

    private boolean isReallyWorldBot(class_1657 entity) {
        if (entity.method_31548() == null) {
            return false;
        }
        class_2371<class_1799> armor = entity.method_31548().field_7548;
        if (armor.size() < 4) {
            return false;
        }
        class_1799 boots = (class_1799) armor.get(0);
        class_1799 leggings = (class_1799) armor.get(1);
        class_1799 chestplate = (class_1799) armor.get(2);
        class_1799 helmet = (class_1799) armor.get(3);
        if (boots.method_7960() || leggings.method_7960() || chestplate.method_7960() || helmet.method_7960() || !boots.method_7923() || !leggings.method_7923() || !chestplate.method_7923() || !helmet.method_7923() || !entity.method_6079().method_7960()) {
            return false;
        }
        boolean hasValidArmor = boots.method_31574(class_1802.field_8370) || leggings.method_31574(class_1802.field_8570) || chestplate.method_31574(class_1802.field_8577) || helmet.method_31574(class_1802.field_8267) || boots.method_31574(class_1802.field_8660) || leggings.method_31574(class_1802.field_8396) || chestplate.method_31574(class_1802.field_8523) || helmet.method_31574(class_1802.field_8743) || boots.method_31574(class_1802.field_8285) || leggings.method_31574(class_1802.field_8348) || chestplate.method_31574(class_1802.field_8058) || helmet.method_31574(class_1802.field_8805);
        if (!hasValidArmor) {
            return false;
        }
        class_1799 mainHand = entity.method_6047();
        boolean hasValidMainHand = mainHand.method_7960() || mainHand.method_31574(class_1802.field_8802) || mainHand.method_31574(class_1802.field_8371) || mainHand.method_31574(class_1802.field_8378) || mainHand.method_31574(class_1802.field_8475) || mainHand.method_31574(class_1802.field_8556) || mainHand.method_31574(class_1802.field_8403) || mainHand.method_31574(class_1802.field_8377) || mainHand.method_31574(class_1802.field_8810);
        return (!hasValidMainHand || boots.method_7986() || leggings.method_7986() || chestplate.method_7986() || helmet.method_7986() || entity.method_7344() == null || entity.method_7344().method_7586() != 20) ? false : true;
    }

    private void addBot(class_1657 entity) {
        if (mc.field_1724 != null) {
            mc.field_1724.method_7353(class_2561.method_30163("§c[AntiBot] §f" + entity.method_5477().getString() + " is a bot!"), false);
        }
        bots.add(entity);
    }

    public static boolean checkBot(class_1657 entity) {
        if (instance.isEnabled() && instance.getMode().is("Zamorozka")) {
            return !validatedNonBots.contains(Integer.valueOf(entity.method_5628()));
        }
        return bots.contains(entity);
    }
}
