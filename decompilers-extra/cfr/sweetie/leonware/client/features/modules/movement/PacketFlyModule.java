/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1297
 *  net.minecraft.class_243
 *  net.minecraft.class_2596
 *  net.minecraft.class_2708
 *  net.minecraft.class_2793
 *  net.minecraft.class_2828
 *  net.minecraft.class_2828$class_2829
 *  net.minecraft.class_434
 */
package sweetie.leonware.client.features.modules.movement;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import lombok.Generated;
import net.minecraft.class_1297;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2708;
import net.minecraft.class_2793;
import net.minecraft.class_2828;
import net.minecraft.class_434;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.system.client.TimerManager;
import sweetie.leonware.api.utils.player.MoveUtil;
import sweetie.leonware.api.utils.task.TaskPriority;

@ModuleRegister(name="Packet Fly", category=Category.MOVEMENT)
public class PacketFlyModule
extends Module {
    private static final PacketFlyModule instance = new PacketFlyModule();
    private final ModeSetting mode = new ModeSetting("Mode").value("Fast").values("Fast", "Factor", "Rubber", "Limit");
    private final ModeSetting type = new ModeSetting("Type").value("Preserve").values("Preserve", "Up", "Down", "Bounds");
    private final ModeSetting phase = new ModeSetting("Phase").value("Full").values("Full", "Off", "Semi");
    private final BooleanSetting limit = new BooleanSetting("Limit").value(true);
    private final BooleanSetting antiKick = new BooleanSetting("AntiKick").value(true);
    private final SliderSetting interval = new SliderSetting("Interval").value(Float.valueOf(4.0f)).range(1.0f, 50.0f).step(1.0f).setVisible(this.antiKick::getValue);
    private final SliderSetting upInterval = new SliderSetting("UpInterval").value(Float.valueOf(20.0f)).range(1.0f, 50.0f).step(1.0f).setVisible(this.antiKick::getValue);
    private final SliderSetting anticKickOffset = new SliderSetting("AnticKickOffset").value(Float.valueOf(0.04f)).range(0.008f, 1.0f).step(0.001f).setVisible(this.antiKick::getValue);
    private final SliderSetting speed = new SliderSetting("Speed").value(Float.valueOf(1.0f)).range(0.0f, 10.0f).step(0.01f);
    private final SliderSetting upSpeed = new SliderSetting("UpSpeed").value(Float.valueOf(0.062f)).range(0.001f, 0.1f).step(0.001f);
    private final SliderSetting timer = new SliderSetting("Timer").value(Float.valueOf(1.0f)).range(0.1f, 5.0f).step(0.1f);
    private final SliderSetting increaseTicks = new SliderSetting("IncreaseTicks").value(Float.valueOf(1.0f)).range(1.0f, 20.0f).step(1.0f);
    private final SliderSetting factor = new SliderSetting("Factor").value(Float.valueOf(1.0f)).range(1.0f, 10.0f).step(0.1f);
    private final SliderSetting offset = new SliderSetting("Offset").value(Float.valueOf(1337.0f)).range(1.0f, 1337.0f).step(1.0f).setVisible(() -> this.type.is("Up") || this.type.is("Down"));
    private final ConcurrentHashMap<Integer, Teleport> teleports = new ConcurrentHashMap();
    private final ArrayList<class_2828> movePackets = new ArrayList();
    private int ticks;
    private int factorTicks;
    private int teleportId = -1;
    private boolean flip = false;

    public PacketFlyModule() {
        this.addSettings(this.mode, this.type, this.phase, this.limit, this.antiKick, this.interval, this.upInterval, this.anticKickOffset, this.speed, this.upSpeed, this.timer, this.increaseTicks, this.factor, this.offset);
    }

    @Override
    public void onEnable() {
        this.teleportId = -1;
        if (PacketFlyModule.mc.field_1724 != null) {
            this.ticks = 0;
            this.teleportId = 0;
            this.movePackets.clear();
            this.teleports.clear();
        }
        this.factorTicks = 0;
    }

    @Override
    public void onEvent() {
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(event -> {
            class_2596<?> patt0$temp;
            if (PacketFlyModule.mc.field_1724 == null || PacketFlyModule.mc.field_1687 == null) {
                return;
            }
            if (event.isReceive()) {
                class_2596<?> patt0$temp2 = event.packet();
                if (patt0$temp2 instanceof class_2708) {
                    class_2708 pac = (class_2708)patt0$temp2;
                    Teleport teleport = this.teleports.remove(pac.comp_3133());
                    if (PacketFlyModule.mc.field_1724.method_5805() && PacketFlyModule.mc.field_1687.method_22340(PacketFlyModule.mc.field_1724.method_24515()) && !(PacketFlyModule.mc.field_1755 instanceof class_434) && !this.mode.is("Rubber") && teleport != null && teleport.x == pac.comp_3228().comp_3148().field_1352 && teleport.y == pac.comp_3228().comp_3148().field_1351 && teleport.z == pac.comp_3228().comp_3148().field_1350) {
                        PacketEvent.getInstance().setCancel(true);
                        return;
                    }
                    pac.comp_3228().comp_3150 = PacketFlyModule.mc.field_1724.method_36454();
                    pac.comp_3228().comp_3151 = PacketFlyModule.mc.field_1724.method_36455();
                    this.teleportId = pac.comp_3133();
                }
            } else if (event.isSend() && (patt0$temp = event.packet()) instanceof class_2828) {
                class_2828 pac = (class_2828)patt0$temp;
                if (this.movePackets.contains(pac)) {
                    this.movePackets.remove(pac);
                    return;
                }
                PacketEvent.getInstance().setCancel(true);
            }
        }));
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (PacketFlyModule.mc.field_1724 == null || PacketFlyModule.mc.field_1687 == null) {
                return;
            }
            this.teleports.entrySet().removeIf(entry -> System.currentTimeMillis() - ((Teleport)entry.getValue()).time > 30000L);
            if (((Float)this.timer.getValue()).floatValue() != 1.0f) {
                TimerManager.getInstance().addTimer(((Float)this.timer.getValue()).floatValue(), TaskPriority.NORMAL, this, 1);
            }
            PacketFlyModule.mc.field_1724.method_18800(0.0, 0.0, 0.0);
            if (!this.mode.is("Rubber") && this.teleportId == 0) {
                if (this.getTickCounter(4)) {
                    this.sendPackets(class_243.field_1353, false);
                }
                return;
            }
            boolean insideBlock = PacketFlyModule.mc.field_1687.method_20812((class_1297)PacketFlyModule.mc.field_1724, PacketFlyModule.mc.field_1724.method_5829().method_1009(-0.0625, -0.0625, -0.0625)).iterator().hasNext();
            double upMotion = 0.0;
            if (PacketFlyModule.mc.field_1690.field_1903.method_1434() && (insideBlock || !MoveUtil.isMoving())) {
                upMotion = ((Boolean)this.antiKick.getValue()).booleanValue() && !insideBlock ? (this.getTickCounter(this.mode.is("Rubber") ? ((Float)this.upInterval.getValue()).intValue() / 2 : ((Float)this.upInterval.getValue()).intValue()) ? (double)(-((Float)this.upSpeed.getValue()).floatValue() / 2.0f) : (double)((Float)this.upSpeed.getValue()).floatValue()) : (double)((Float)this.upSpeed.getValue()).floatValue();
            } else if (PacketFlyModule.mc.field_1690.field_1832.method_1434()) {
                upMotion = -((Float)this.upSpeed.getValue()).floatValue();
            } else if (((Boolean)this.antiKick.getValue()).booleanValue() && !insideBlock) {
                double d = upMotion = this.getTickCounter(((Float)this.interval.getValue()).intValue()) ? (double)(-((Float)this.anticKickOffset.getValue()).floatValue()) : 0.0;
            }
            if (this.phase.is("Full") && insideBlock && MoveUtil.isMoving() && upMotion != 0.0) {
                upMotion = PacketFlyModule.mc.field_1690.field_1903.method_1434() ? upMotion / 2.5 : upMotion / 1.5;
            }
            double[] motion = this.forward(this.phase.is("Full") && insideBlock ? 0.034444444444444444 : (double)((Float)this.speed.getValue()).floatValue() * 0.26);
            int factorInt = 1;
            if (this.mode.is("Factor") && PacketFlyModule.mc.field_1724.field_6012 % ((Float)this.increaseTicks.getValue()).intValue() == 0) {
                factorInt = (int)Math.floor(((Float)this.factor.getValue()).floatValue());
                ++this.factorTicks;
                if (this.factorTicks > (int)(20.0 / ((double)(((Float)this.factor.getValue()).floatValue() - (float)factorInt) * 20.0))) {
                    ++factorInt;
                    this.factorTicks = 0;
                }
            }
            for (int i = 1; i <= factorInt; ++i) {
                if (this.mode.is("Limit")) {
                    if (PacketFlyModule.mc.field_1724.field_6012 % 2 == 0) {
                        if (this.flip && upMotion >= 0.0) {
                            this.flip = false;
                            upMotion = -((Float)this.upSpeed.getValue()).floatValue() / 2.0f;
                        }
                        PacketFlyModule.mc.field_1724.method_18800(motion[0] * (double)i, upMotion * (double)i, motion[1] * (double)i);
                        this.sendPackets(PacketFlyModule.mc.field_1724.method_18798(), (Boolean)this.limit.getValue() == false);
                        continue;
                    }
                    if (!(upMotion < 0.0)) continue;
                    this.flip = true;
                    continue;
                }
                PacketFlyModule.mc.field_1724.method_18800(motion[0] * (double)i, upMotion * (double)i, motion[1] * (double)i);
                this.sendPackets(PacketFlyModule.mc.field_1724.method_18798(), !this.mode.is("Rubber"));
            }
            if (this.phase.getValue() != "Off" && (this.phase.is("Semi") || PacketFlyModule.mc.field_1687.method_20812((class_1297)PacketFlyModule.mc.field_1724, PacketFlyModule.mc.field_1724.method_5829().method_1009(-0.0625, -0.0625, -0.0625)).iterator().hasNext())) {
                PacketFlyModule.mc.field_1724.field_5960 = true;
            }
        }));
        this.addEvents(packetEvent, updateEvent);
    }

    private double[] forward(double speed) {
        float forward = PacketFlyModule.mc.field_1724.field_3913.field_3905;
        float side = PacketFlyModule.mc.field_1724.field_3913.field_3907;
        float yaw = PacketFlyModule.mc.field_1724.field_5982 + (PacketFlyModule.mc.field_1724.method_36454() - PacketFlyModule.mc.field_1724.field_5982) * mc.method_61966().method_60637(true);
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += (float)(forward > 0.0f ? -45 : 45);
            } else if (side < 0.0f) {
                yaw += (float)(forward > 0.0f ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        double posX = (double)forward * speed * cos + (double)side * speed * sin;
        double posZ = (double)forward * speed * sin - (double)side * speed * cos;
        return new double[]{posX, posZ};
    }

    public boolean getTickCounter(int n) {
        ++this.ticks;
        if (this.ticks >= n) {
            this.ticks = 0;
            return true;
        }
        return false;
    }

    private int getWorldBorder() {
        if (mc.method_1542()) {
            return 1;
        }
        int n = ThreadLocalRandom.current().nextInt(29000000);
        if (ThreadLocalRandom.current().nextBoolean()) {
            return n;
        }
        return -n;
    }

    public class_243 getVectorByMode(class_243 vec3d, class_243 vec3d2) {
        class_243 vec3d3 = vec3d.method_1019(vec3d2);
        switch ((String)this.type.getValue()) {
            case "Preserve": {
                vec3d3 = vec3d3.method_1031((double)this.getWorldBorder(), 0.0, (double)this.getWorldBorder());
                break;
            }
            case "Up": {
                vec3d3 = vec3d3.method_1031(0.0, (double)((Float)this.offset.getValue()).floatValue(), 0.0);
                break;
            }
            case "Down": {
                vec3d3 = vec3d3.method_1031(0.0, (double)(-((Float)this.offset.getValue()).floatValue()), 0.0);
                break;
            }
            case "Bounds": {
                vec3d3 = new class_243(vec3d3.field_1352, PacketFlyModule.mc.field_1724.method_23318() <= 10.0 ? 255.0 : 1.0, vec3d3.field_1350);
            }
        }
        return vec3d3;
    }

    public void sendPackets(class_243 vec3d, boolean confirm) {
        class_243 motion = PacketFlyModule.mc.field_1724.method_19538().method_1019(vec3d);
        class_243 rubberBand = this.getVectorByMode(vec3d, motion);
        class_2828.class_2829 motionPacket = new class_2828.class_2829(motion.field_1352, motion.field_1351, motion.field_1350, PacketFlyModule.mc.field_1724.method_24828(), PacketFlyModule.mc.field_1724.field_5976);
        this.movePackets.add((class_2828)motionPacket);
        this.sendPacket((class_2596<?>)motionPacket);
        class_2828.class_2829 rubberBandPacket = new class_2828.class_2829(rubberBand.field_1352, rubberBand.field_1351, rubberBand.field_1350, PacketFlyModule.mc.field_1724.method_24828(), PacketFlyModule.mc.field_1724.field_5976);
        this.movePackets.add((class_2828)rubberBandPacket);
        this.sendPacket((class_2596<?>)rubberBandPacket);
        if (confirm) {
            this.sendPacket((class_2596<?>)new class_2793(++this.teleportId));
            this.teleports.put(this.teleportId, new Teleport(motion.field_1352, motion.field_1351, motion.field_1350, System.currentTimeMillis()));
        }
    }

    @Generated
    public static PacketFlyModule getInstance() {
        return instance;
    }

    public record Teleport(double x, double y, double z, long time) {
    }
}

