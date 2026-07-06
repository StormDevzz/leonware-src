// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement;

import java.util.Map;
import lombok.Generated;
import net.minecraft.class_2793;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.class_2596;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.utils.player.MoveUtil;
import net.minecraft.class_1297;
import net.minecraft.class_243;
import sweetie.leonware.api.utils.task.TaskPriority;
import sweetie.leonware.api.system.client.TimerManager;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.event.Listener;
import net.minecraft.class_434;
import net.minecraft.class_2708;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.module.setting.Setting;
import java.util.function.Supplier;
import java.util.Objects;
import net.minecraft.class_2828;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Packet Fly", category = Category.MOVEMENT)
public class PacketFlyModule extends Module
{
    private static final PacketFlyModule instance;
    private final ModeSetting mode;
    private final ModeSetting type;
    private final ModeSetting phase;
    private final BooleanSetting limit;
    private final BooleanSetting antiKick;
    private final SliderSetting interval;
    private final SliderSetting upInterval;
    private final SliderSetting anticKickOffset;
    private final SliderSetting speed;
    private final SliderSetting upSpeed;
    private final SliderSetting timer;
    private final SliderSetting increaseTicks;
    private final SliderSetting factor;
    private final SliderSetting offset;
    private final ConcurrentHashMap<Integer, Teleport> teleports;
    private final ArrayList<class_2828> movePackets;
    private int ticks;
    private int factorTicks;
    private int teleportId;
    private boolean flip;
    
    public PacketFlyModule() {
        this.mode = new ModeSetting("Mode").value("Fast").values("Fast", "Factor", "Rubber", "Limit");
        this.type = new ModeSetting("Type").value("Preserve").values("Preserve", "Up", "Down", "Bounds");
        this.phase = new ModeSetting("Phase").value("Full").values("Full", "Off", "Semi");
        this.limit = new BooleanSetting("Limit").value(true);
        this.antiKick = new BooleanSetting("AntiKick").value(true);
        final SliderSetting step = new SliderSetting("Interval").value(4.0f).range(1.0f, 50.0f).step(1.0f);
        final BooleanSetting antiKick = this.antiKick;
        Objects.requireNonNull(antiKick);
        this.interval = step.setVisible((Supplier<Boolean>)antiKick::getValue);
        final SliderSetting step2 = new SliderSetting("UpInterval").value(20.0f).range(1.0f, 50.0f).step(1.0f);
        final BooleanSetting antiKick2 = this.antiKick;
        Objects.requireNonNull(antiKick2);
        this.upInterval = step2.setVisible((Supplier<Boolean>)antiKick2::getValue);
        final SliderSetting step3 = new SliderSetting("AnticKickOffset").value(0.04f).range(0.008f, 1.0f).step(0.001f);
        final BooleanSetting antiKick3 = this.antiKick;
        Objects.requireNonNull(antiKick3);
        this.anticKickOffset = step3.setVisible((Supplier<Boolean>)antiKick3::getValue);
        this.speed = new SliderSetting("Speed").value(1.0f).range(0.0f, 10.0f).step(0.01f);
        this.upSpeed = new SliderSetting("UpSpeed").value(0.062f).range(0.001f, 0.1f).step(0.001f);
        this.timer = new SliderSetting("Timer").value(1.0f).range(0.1f, 5.0f).step(0.1f);
        this.increaseTicks = new SliderSetting("IncreaseTicks").value(1.0f).range(1.0f, 20.0f).step(1.0f);
        this.factor = new SliderSetting("Factor").value(1.0f).range(1.0f, 10.0f).step(0.1f);
        this.offset = new SliderSetting("Offset").value(1337.0f).range(1.0f, 1337.0f).step(1.0f).setVisible(() -> this.type.is("Up") || this.type.is("Down"));
        this.teleports = new ConcurrentHashMap<Integer, Teleport>();
        this.movePackets = new ArrayList<class_2828>();
        this.teleportId = -1;
        this.flip = false;
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
        final EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(event -> {
            if (PacketFlyModule.mc.field_1724 == null || PacketFlyModule.mc.field_1687 == null) {
                return;
            }
            else {
                if (event.isReceive()) {
                    final class_2596 patt0$temp = event.packet();
                    if (patt0$temp instanceof final class_2708 pac) {
                        final Teleport teleport = this.teleports.remove(pac.comp_3133());
                        if (PacketFlyModule.mc.field_1724.method_5805() && PacketFlyModule.mc.field_1687.method_22340(PacketFlyModule.mc.field_1724.method_24515()) && !(PacketFlyModule.mc.field_1755 instanceof class_434) && !this.mode.is("Rubber") && teleport != null && teleport.x == pac.comp_3228().comp_3148().field_1352 && teleport.y == pac.comp_3228().comp_3148().field_1351 && teleport.z == pac.comp_3228().comp_3148().field_1350) {
                            PacketEvent.getInstance().setCancel(true);
                        }
                        else {
                            pac.comp_3228().comp_3150 = PacketFlyModule.mc.field_1724.method_36454();
                            pac.comp_3228().comp_3151 = PacketFlyModule.mc.field_1724.method_36455();
                            this.teleportId = pac.comp_3133();
                        }
                    }
                }
                else if (event.isSend()) {
                    final class_2596 patt0$temp2 = event.packet();
                    if (patt0$temp2 instanceof final class_2828 pac2) {
                        if (this.movePackets.contains(pac2)) {
                            this.movePackets.remove(pac2);
                        }
                        else {
                            PacketEvent.getInstance().setCancel(true);
                        }
                    }
                }
                return;
            }
        }));
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (PacketFlyModule.mc.field_1724 == null || PacketFlyModule.mc.field_1687 == null) {
                return;
            }
            else {
                this.teleports.entrySet().removeIf(entry -> System.currentTimeMillis() - entry.getValue().time > 30000L);
                if (this.timer.getValue() != 1.0f) {
                    TimerManager.getInstance().addTimer(this.timer.getValue(), TaskPriority.NORMAL, this, 1);
                }
                PacketFlyModule.mc.field_1724.method_18800(0.0, 0.0, 0.0);
                if (!this.mode.is("Rubber") && this.teleportId == 0) {
                    if (this.getTickCounter(4)) {
                        this.sendPackets(class_243.field_1353, false);
                    }
                    return;
                }
                else {
                    final boolean insideBlock = PacketFlyModule.mc.field_1687.method_20812((class_1297)PacketFlyModule.mc.field_1724, PacketFlyModule.mc.field_1724.method_5829().method_1009(-0.0625, -0.0625, -0.0625)).iterator().hasNext();
                    double upMotion = 0.0;
                    if (PacketFlyModule.mc.field_1690.field_1903.method_1434() && (insideBlock || !MoveUtil.isMoving())) {
                        if (this.antiKick.getValue() && !insideBlock) {
                            upMotion = (this.getTickCounter(this.mode.is("Rubber") ? (this.upInterval.getValue().intValue() / 2) : this.upInterval.getValue().intValue()) ? (-this.upSpeed.getValue() / 2.0f) : ((double)this.upSpeed.getValue()));
                        }
                        else {
                            upMotion = this.upSpeed.getValue();
                        }
                    }
                    else if (PacketFlyModule.mc.field_1690.field_1832.method_1434()) {
                        upMotion = -this.upSpeed.getValue();
                    }
                    else if (this.antiKick.getValue() && !insideBlock) {
                        upMotion = (double)(this.getTickCounter(this.interval.getValue().intValue()) ? (-this.anticKickOffset.getValue()) : 0.0);
                    }
                    if (this.phase.is("Full") && insideBlock && MoveUtil.isMoving() && upMotion != 0.0) {
                        upMotion = (PacketFlyModule.mc.field_1690.field_1903.method_1434() ? (upMotion / 2.5) : (upMotion / 1.5));
                    }
                    final double[] motion = this.forward((this.phase.is("Full") && insideBlock) ? 0.034444444444444444 : (this.speed.getValue() * 0.26));
                    int factorInt = 1;
                    if (this.mode.is("Factor") && PacketFlyModule.mc.field_1724.field_6012 % this.increaseTicks.getValue().intValue() == 0) {
                        factorInt = (int)Math.floor(this.factor.getValue());
                        ++this.factorTicks;
                        if (this.factorTicks > (int)(20.0 / ((this.factor.getValue() - factorInt) * 20.0))) {
                            ++factorInt;
                            this.factorTicks = 0;
                        }
                    }
                    for (int i = 1; i <= factorInt; ++i) {
                        if (this.mode.is("Limit")) {
                            if (PacketFlyModule.mc.field_1724.field_6012 % 2 == 0) {
                                if (this.flip && upMotion >= 0.0) {
                                    this.flip = false;
                                    upMotion = -this.upSpeed.getValue() / 2.0f;
                                }
                                PacketFlyModule.mc.field_1724.method_18800(motion[0] * i, upMotion * i, motion[1] * i);
                                this.sendPackets(PacketFlyModule.mc.field_1724.method_18798(), !this.limit.getValue());
                            }
                            else if (upMotion < 0.0) {
                                this.flip = true;
                            }
                        }
                        else {
                            PacketFlyModule.mc.field_1724.method_18800(motion[0] * i, upMotion * i, motion[1] * i);
                            this.sendPackets(PacketFlyModule.mc.field_1724.method_18798(), !this.mode.is("Rubber"));
                        }
                    }
                    if (this.phase.getValue() != "Off" && (this.phase.is("Semi") || PacketFlyModule.mc.field_1687.method_20812((class_1297)PacketFlyModule.mc.field_1724, PacketFlyModule.mc.field_1724.method_5829().method_1009(-0.0625, -0.0625, -0.0625)).iterator().hasNext())) {
                        PacketFlyModule.mc.field_1724.field_5960 = true;
                    }
                    return;
                }
            }
        }));
        this.addEvents(packetEvent, updateEvent);
    }
    
    private double[] forward(final double speed) {
        float forward = PacketFlyModule.mc.field_1724.field_3913.field_3905;
        float side = PacketFlyModule.mc.field_1724.field_3913.field_3907;
        float yaw = PacketFlyModule.mc.field_1724.field_5982 + (PacketFlyModule.mc.field_1724.method_36454() - PacketFlyModule.mc.field_1724.field_5982) * PacketFlyModule.mc.method_61966().method_60637(true);
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += ((forward > 0.0f) ? -45 : 45);
            }
            else if (side < 0.0f) {
                yaw += ((forward > 0.0f) ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            }
            else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        final double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        final double posX = forward * speed * cos + side * speed * sin;
        final double posZ = forward * speed * sin - side * speed * cos;
        return new double[] { posX, posZ };
    }
    
    public boolean getTickCounter(final int n) {
        ++this.ticks;
        if (this.ticks >= n) {
            this.ticks = 0;
            return true;
        }
        return false;
    }
    
    private int getWorldBorder() {
        if (PacketFlyModule.mc.method_1542()) {
            return 1;
        }
        final int n = ThreadLocalRandom.current().nextInt(29000000);
        if (ThreadLocalRandom.current().nextBoolean()) {
            return n;
        }
        return -n;
    }
    
    public class_243 getVectorByMode(final class_243 vec3d, final class_243 vec3d2) {
        class_243 vec3d3 = vec3d.method_1019(vec3d2);
        final String s = this.type.getValue();
        switch (s) {
            case "Preserve": {
                vec3d3 = vec3d3.method_1031((double)this.getWorldBorder(), 0.0, (double)this.getWorldBorder());
                break;
            }
            case "Up": {
                vec3d3 = vec3d3.method_1031(0.0, (double)this.offset.getValue(), 0.0);
                break;
            }
            case "Down": {
                vec3d3 = vec3d3.method_1031(0.0, (double)(-this.offset.getValue()), 0.0);
                break;
            }
            case "Bounds": {
                vec3d3 = new class_243(vec3d3.field_1352, (PacketFlyModule.mc.field_1724.method_23318() <= 10.0) ? 255.0 : 1.0, vec3d3.field_1350);
                break;
            }
        }
        return vec3d3;
    }
    
    public void sendPackets(final class_243 vec3d, final boolean confirm) {
        final class_243 motion = PacketFlyModule.mc.field_1724.method_19538().method_1019(vec3d);
        final class_243 rubberBand = this.getVectorByMode(vec3d, motion);
        final class_2828 motionPacket = (class_2828)new class_2828.class_2829(motion.field_1352, motion.field_1351, motion.field_1350, PacketFlyModule.mc.field_1724.method_24828(), PacketFlyModule.mc.field_1724.field_5976);
        this.movePackets.add(motionPacket);
        this.sendPacket((class_2596<?>)motionPacket);
        final class_2828 rubberBandPacket = (class_2828)new class_2828.class_2829(rubberBand.field_1352, rubberBand.field_1351, rubberBand.field_1350, PacketFlyModule.mc.field_1724.method_24828(), PacketFlyModule.mc.field_1724.field_5976);
        this.movePackets.add(rubberBandPacket);
        this.sendPacket((class_2596<?>)rubberBandPacket);
        if (confirm) {
            this.sendPacket((class_2596<?>)new class_2793(++this.teleportId));
            this.teleports.put(this.teleportId, new Teleport(motion.field_1352, motion.field_1351, motion.field_1350, System.currentTimeMillis()));
        }
    }
    
    @Generated
    public static PacketFlyModule getInstance() {
        return PacketFlyModule.instance;
    }
    
    static {
        instance = new PacketFlyModule();
    }
    
    record Teleport(double x, double y, double z, long time) {}
}
