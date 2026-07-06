package sweetie.leonware.client.features.modules.movement;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import lombok.Generated;
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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/PacketFlyModule.class */
@ModuleRegister(name = "Packet Fly", category = Category.MOVEMENT)
public class PacketFlyModule extends Module {
    private static final PacketFlyModule instance = new PacketFlyModule();
    private final ModeSetting mode = new ModeSetting("Mode").value("Fast").values("Fast", "Factor", "Rubber", "Limit");
    private final ModeSetting type = new ModeSetting("Type").value("Preserve").values("Preserve", "Up", "Down", "Bounds");
    private final ModeSetting phase = new ModeSetting("Phase").value("Full").values("Full", "Off", "Semi");
    private final BooleanSetting limit = new BooleanSetting("Limit").value((Boolean) true);
    private final BooleanSetting antiKick = new BooleanSetting("AntiKick").value((Boolean) true);
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

    @Generated
    public static PacketFlyModule getInstance() {
        return instance;
    }

    /* JADX WARN: Type inference failed for: r1v17, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v22, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v27, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v52, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    public PacketFlyModule() {
        SliderSetting sliderSettingStep = new SliderSetting("Interval").value(Float.valueOf(4.0f)).range(1.0f, 50.0f).step(1.0f);
        BooleanSetting booleanSetting = this.antiKick;
        Objects.requireNonNull(booleanSetting);
        this.interval = sliderSettingStep.setVisible(booleanSetting::getValue);
        SliderSetting sliderSettingStep2 = new SliderSetting("UpInterval").value(Float.valueOf(20.0f)).range(1.0f, 50.0f).step(1.0f);
        BooleanSetting booleanSetting2 = this.antiKick;
        Objects.requireNonNull(booleanSetting2);
        this.upInterval = sliderSettingStep2.setVisible(booleanSetting2::getValue);
        SliderSetting sliderSettingStep3 = new SliderSetting("AnticKickOffset").value(Float.valueOf(0.04f)).range(0.008f, 1.0f).step(0.001f);
        BooleanSetting booleanSetting3 = this.antiKick;
        Objects.requireNonNull(booleanSetting3);
        this.anticKickOffset = sliderSettingStep3.setVisible(booleanSetting3::getValue);
        this.speed = new SliderSetting("Speed").value(Float.valueOf(1.0f)).range(0.0f, 10.0f).step(0.01f);
        this.upSpeed = new SliderSetting("UpSpeed").value(Float.valueOf(0.062f)).range(0.001f, 0.1f).step(0.001f);
        this.timer = new SliderSetting("Timer").value(Float.valueOf(1.0f)).range(0.1f, 5.0f).step(0.1f);
        this.increaseTicks = new SliderSetting("IncreaseTicks").value(Float.valueOf(1.0f)).range(1.0f, 20.0f).step(1.0f);
        this.factor = new SliderSetting("Factor").value(Float.valueOf(1.0f)).range(1.0f, 10.0f).step(0.1f);
        this.offset = new SliderSetting("Offset").value(Float.valueOf(1337.0f)).range(1.0f, 1337.0f).step(1.0f).setVisible(() -> {
            return Boolean.valueOf(this.type.is("Up") || this.type.is("Down"));
        });
        this.teleports = new ConcurrentHashMap<>();
        this.movePackets = new ArrayList<>();
        this.teleportId = -1;
        this.flip = false;
        addSettings(this.mode, this.type, this.phase, this.limit, this.antiKick, this.interval, this.upInterval, this.anticKickOffset, this.speed, this.upSpeed, this.timer, this.increaseTicks, this.factor, this.offset);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onEnable() {
        this.teleportId = -1;
        if (mc.field_1724 != null) {
            this.ticks = 0;
            this.teleportId = 0;
            this.movePackets.clear();
            this.teleports.clear();
        }
        this.factorTicks = 0;
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1724 == null || mc.field_1687 == null) {
                return;
            }
            if (event.isReceive()) {
                class_2708 class_2708VarPacket = event.packet();
                if (class_2708VarPacket instanceof class_2708) {
                    class_2708 pac = class_2708VarPacket;
                    Teleport teleport = this.teleports.remove(Integer.valueOf(pac.comp_3133()));
                    if (mc.field_1724.method_5805() && mc.field_1687.method_22340(mc.field_1724.method_24515()) && !(mc.field_1755 instanceof class_434) && !this.mode.is("Rubber") && teleport != null && teleport.x == pac.comp_3228().comp_3148().field_1352 && teleport.y == pac.comp_3228().comp_3148().field_1351 && teleport.z == pac.comp_3228().comp_3148().field_1350) {
                        PacketEvent.getInstance().setCancel(true);
                        return;
                    }
                    pac.comp_3228().comp_3150 = mc.field_1724.method_36454();
                    pac.comp_3228().comp_3151 = mc.field_1724.method_36455();
                    this.teleportId = pac.comp_3133();
                    return;
                }
                return;
            }
            if (event.isSend()) {
                class_2828 class_2828VarPacket = event.packet();
                if (class_2828VarPacket instanceof class_2828) {
                    class_2828 pac2 = class_2828VarPacket;
                    if (this.movePackets.contains(pac2)) {
                        this.movePackets.remove(pac2);
                    } else {
                        PacketEvent.getInstance().setCancel(true);
                    }
                }
            }
        }));
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event2 -> {
            if (mc.field_1724 == null || mc.field_1687 == null) {
                return;
            }
            this.teleports.entrySet().removeIf(entry -> {
                return System.currentTimeMillis() - ((Teleport) entry.getValue()).time > 30000;
            });
            if (this.timer.getValue().floatValue() != 1.0f) {
                TimerManager.getInstance().addTimer(this.timer.getValue().floatValue(), TaskPriority.NORMAL, this, 1);
            }
            mc.field_1724.method_18800(0.0d, 0.0d, 0.0d);
            if (!this.mode.is("Rubber") && this.teleportId == 0) {
                if (getTickCounter(4)) {
                    sendPackets(class_243.field_1353, false);
                    return;
                }
                return;
            }
            boolean insideBlock = mc.field_1687.method_20812(mc.field_1724, mc.field_1724.method_5829().method_1009(-0.0625d, -0.0625d, -0.0625d)).iterator().hasNext();
            double upMotion = 0.0d;
            if (mc.field_1690.field_1903.method_1434() && (insideBlock || !MoveUtil.isMoving())) {
                upMotion = (this.antiKick.getValue().booleanValue() && !insideBlock) ? getTickCounter(this.mode.is("Rubber") ? this.upInterval.getValue().intValue() / 2 : this.upInterval.getValue().intValue()) ? (-this.upSpeed.getValue().floatValue()) / 2.0f : this.upSpeed.getValue().floatValue() : this.upSpeed.getValue().floatValue();
            } else if (mc.field_1690.field_1832.method_1434()) {
                upMotion = -this.upSpeed.getValue().floatValue();
            } else if (this.antiKick.getValue().booleanValue() && !insideBlock) {
                upMotion = getTickCounter(this.interval.getValue().intValue()) ? -this.anticKickOffset.getValue().floatValue() : 0.0d;
            }
            if (this.phase.is("Full") && insideBlock && MoveUtil.isMoving() && upMotion != 0.0d) {
                upMotion = mc.field_1690.field_1903.method_1434() ? upMotion / 2.5d : upMotion / 1.5d;
            }
            double[] motion = forward((this.phase.is("Full") && insideBlock) ? 0.034444444444444444d : ((double) this.speed.getValue().floatValue()) * 0.26d);
            int factorInt = 1;
            if (this.mode.is("Factor") && mc.field_1724.field_6012 % this.increaseTicks.getValue().intValue() == 0) {
                factorInt = (int) Math.floor(this.factor.getValue().floatValue());
                this.factorTicks++;
                if (this.factorTicks > ((int) (20.0d / (((double) (this.factor.getValue().floatValue() - factorInt)) * 20.0d)))) {
                    factorInt++;
                    this.factorTicks = 0;
                }
            }
            for (int i = 1; i <= factorInt; i++) {
                if (this.mode.is("Limit")) {
                    if (mc.field_1724.field_6012 % 2 == 0) {
                        if (this.flip && upMotion >= 0.0d) {
                            this.flip = false;
                            upMotion = (-this.upSpeed.getValue().floatValue()) / 2.0f;
                        }
                        mc.field_1724.method_18800(motion[0] * ((double) i), upMotion * ((double) i), motion[1] * ((double) i));
                        sendPackets(mc.field_1724.method_18798(), !this.limit.getValue().booleanValue());
                    } else if (upMotion < 0.0d) {
                        this.flip = true;
                    }
                } else {
                    mc.field_1724.method_18800(motion[0] * ((double) i), upMotion * ((double) i), motion[1] * ((double) i));
                    sendPackets(mc.field_1724.method_18798(), !this.mode.is("Rubber"));
                }
            }
            if (this.phase.getValue() != "Off") {
                if (this.phase.is("Semi") || mc.field_1687.method_20812(mc.field_1724, mc.field_1724.method_5829().method_1009(-0.0625d, -0.0625d, -0.0625d)).iterator().hasNext()) {
                    mc.field_1724.field_5960 = true;
                }
            }
        }));
        addEvents(packetEvent, updateEvent);
    }

    private double[] forward(double speed) {
        float forward = mc.field_1724.field_3913.field_3905;
        float side = mc.field_1724.field_3913.field_3907;
        float yaw = mc.field_1724.field_5982 + ((mc.field_1724.method_36454() - mc.field_1724.field_5982) * mc.method_61966().method_60637(true));
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += forward > 0.0f ? -45 : 45;
            } else if (side < 0.0f) {
                yaw += forward > 0.0f ? 45 : -45;
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
        double posX = (((double) forward) * speed * cos) + (((double) side) * speed * sin);
        double posZ = ((((double) forward) * speed) * sin) - ((((double) side) * speed) * cos);
        return new double[]{posX, posZ};
    }

    public boolean getTickCounter(int n) {
        this.ticks++;
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
        class_243 vec3d3;
        vec3d3 = vec3d.method_1019(vec3d2);
        switch (this.type.getValue()) {
            case "Preserve":
                vec3d3 = vec3d3.method_1031(getWorldBorder(), 0.0d, getWorldBorder());
                break;
            case "Up":
                vec3d3 = vec3d3.method_1031(0.0d, this.offset.getValue().floatValue(), 0.0d);
                break;
            case "Down":
                vec3d3 = vec3d3.method_1031(0.0d, -this.offset.getValue().floatValue(), 0.0d);
                break;
            case "Bounds":
                vec3d3 = new class_243(vec3d3.field_1352, mc.field_1724.method_23318() <= 10.0d ? 255.0d : 1.0d, vec3d3.field_1350);
                break;
        }
        return vec3d3;
    }

    public void sendPackets(class_243 vec3d, boolean confirm) {
        class_243 motion = mc.field_1724.method_19538().method_1019(vec3d);
        class_243 rubberBand = getVectorByMode(vec3d, motion);
        class_2828 motionPacket = new class_2828.class_2829(motion.field_1352, motion.field_1351, motion.field_1350, mc.field_1724.method_24828(), mc.field_1724.field_5976);
        this.movePackets.add(motionPacket);
        sendPacket((class_2596<?>) motionPacket);
        class_2828 rubberBandPacket = new class_2828.class_2829(rubberBand.field_1352, rubberBand.field_1351, rubberBand.field_1350, mc.field_1724.method_24828(), mc.field_1724.field_5976);
        this.movePackets.add(rubberBandPacket);
        sendPacket((class_2596<?>) rubberBandPacket);
        if (confirm) {
            int i = this.teleportId + 1;
            this.teleportId = i;
            sendPacket((class_2596<?>) new class_2793(i));
            this.teleports.put(Integer.valueOf(this.teleportId), new Teleport(motion.field_1352, motion.field_1351, motion.field_1350, System.currentTimeMillis()));
        }
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/PacketFlyModule$Teleport.class */
    public static final class Teleport extends Record {
        private final double x;
        private final double y;
        private final double z;
        private final long time;

        public Teleport(double x, double y, double z, long time) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.time = time;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Teleport.class), Teleport.class, "x;y;z;time", "FIELD:Lsweetie/leonware/client/features/modules/movement/PacketFlyModule$Teleport;->x:D", "FIELD:Lsweetie/leonware/client/features/modules/movement/PacketFlyModule$Teleport;->y:D", "FIELD:Lsweetie/leonware/client/features/modules/movement/PacketFlyModule$Teleport;->z:D", "FIELD:Lsweetie/leonware/client/features/modules/movement/PacketFlyModule$Teleport;->time:J").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Teleport.class), Teleport.class, "x;y;z;time", "FIELD:Lsweetie/leonware/client/features/modules/movement/PacketFlyModule$Teleport;->x:D", "FIELD:Lsweetie/leonware/client/features/modules/movement/PacketFlyModule$Teleport;->y:D", "FIELD:Lsweetie/leonware/client/features/modules/movement/PacketFlyModule$Teleport;->z:D", "FIELD:Lsweetie/leonware/client/features/modules/movement/PacketFlyModule$Teleport;->time:J").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Teleport.class, Object.class), Teleport.class, "x;y;z;time", "FIELD:Lsweetie/leonware/client/features/modules/movement/PacketFlyModule$Teleport;->x:D", "FIELD:Lsweetie/leonware/client/features/modules/movement/PacketFlyModule$Teleport;->y:D", "FIELD:Lsweetie/leonware/client/features/modules/movement/PacketFlyModule$Teleport;->z:D", "FIELD:Lsweetie/leonware/client/features/modules/movement/PacketFlyModule$Teleport;->time:J").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public double x() {
            return this.x;
        }

        public double y() {
            return this.y;
        }

        public double z() {
            return this.z;
        }

        public long time() {
            return this.time;
        }
    }
}
