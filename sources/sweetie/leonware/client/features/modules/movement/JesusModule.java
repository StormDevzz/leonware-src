package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_2404;
import net.minecraft.class_2596;
import net.minecraft.class_2680;
import net.minecraft.class_2828;
import net.minecraft.class_3532;
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
import sweetie.leonware.api.utils.other.NetworkUtil;
import sweetie.leonware.api.utils.player.MoveUtil;
import sweetie.leonware.inject.accessors.IPlayerMoveC2SPacket;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/JesusModule.class */
@ModuleRegister(name = "Jesus", category = Category.MOVEMENT)
public class JesusModule extends Module {
    private static final JesusModule instance = new JesusModule();
    public final ModeSetting mode = new ModeSetting("Режим").value("Solid").values("Solid", "Trampoline", "Dolphin", "Matrix Ground", "Matrix Zoom", "Matrix Solid");
    public final BooleanSetting glide = new BooleanSetting("Глайд").value((Boolean) false).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("Solid"));
    });
    public final BooleanSetting strict = new BooleanSetting("Строгий").value((Boolean) false).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("Solid"));
    });
    public final BooleanSetting boost = new BooleanSetting("Буст в лаве").value((Boolean) false).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("Trampoline"));
    });
    public final SliderSetting matrixSpeed = new SliderSetting("Скорость").value(Float.valueOf(1.0f)).range(0.1f, 5.0f).step(0.1f).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("Matrix Ground") || this.mode.is("Matrix Solid") || this.mode.is("Matrix Zoom"));
    });
    public final BooleanSetting matrixBoost = new BooleanSetting("Буст").value((Boolean) false).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("Matrix Ground"));
    });
    public final SliderSetting boostSpeed = new SliderSetting("Скорость буста").value(Float.valueOf(1.35f)).range(0.1f, 4.0f).step(0.01f).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("Matrix Ground") && this.matrixBoost.getValue().booleanValue());
    });
    public final SliderSetting boostTicks = new SliderSetting("Тики буста").value(Float.valueOf(2.0f)).range(1.0f, 30.0f).step(1.0f).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("Matrix Ground") && this.matrixBoost.getValue().booleanValue());
    });
    public final SliderSetting motionUp = new SliderSetting("Motion Up").value(Float.valueOf(0.42f)).range(0.1f, 2.0f).step(0.01f).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("Solid"));
    });
    private boolean jumping = false;
    private int glideCounter = 0;
    private float lastOffset = 0.0f;

    @Generated
    public static JesusModule getInstance() {
        return instance;
    }

    /* JADX WARN: Type inference failed for: r1v11, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v16, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v19, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v24, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v29, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v34, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v5, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v8, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    public JesusModule() {
        addSettings(this.mode, this.glide, this.strict, this.boost, this.matrixSpeed, this.matrixBoost, this.boostSpeed, this.boostTicks, this.motionUp);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        this.jumping = false;
        this.glideCounter = 0;
        this.lastOffset = 0.0f;
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(e -> {
            Float value;
            if (mc.field_1724 == null || mc.field_1687 == null) {
                return;
            }
            switch (this.mode.getValue()) {
                case "Solid":
                    if (!mc.field_1724.field_3913.field_54155.comp_3164() && !mc.field_1724.field_3913.field_54155.comp_3163() && isInLiquid()) {
                        mc.field_1724.method_18800(mc.field_1724.method_18798().field_1352, 0.1d, mc.field_1724.method_18798().field_1350);
                    }
                    if (this.glide.getValue().booleanValue() && isOnLiquid() && mc.field_1724.field_6017 < 3.0f && !mc.field_1724.field_3913.field_54155.comp_3163() && !isInLiquid() && !mc.field_1724.field_3913.field_54155.comp_3164()) {
                        double[] multipliers = {1.1d, 1.27d, 1.51d, 1.15d, 1.23d};
                        double mult = multipliers[this.glideCounter % multipliers.length];
                        mc.field_1724.method_18800(mc.field_1724.method_18798().field_1352 * mult, mc.field_1724.method_18798().field_1351, mc.field_1724.method_18798().field_1350 * mult);
                        this.glideCounter = (this.glideCounter + 1) % multipliers.length;
                        break;
                    }
                    break;
                case "Trampoline":
                    if (mc.field_1724.method_5799() || mc.field_1724.method_5771()) {
                        mc.field_1724.method_18800(mc.field_1724.method_18798().field_1352, 0.1d, mc.field_1724.method_18798().field_1350);
                    }
                    if (this.jumping && !mc.field_1724.method_31549().field_7479 && !mc.field_1724.method_5799()) {
                        if (mc.field_1724.method_18798().field_1351 < -0.3d || mc.field_1724.method_24828() || mc.field_1724.method_6101()) {
                            this.jumping = false;
                        } else {
                            double newY = (mc.field_1724.method_18798().field_1351 / 0.98d) + 0.08d;
                            mc.field_1724.method_18800(mc.field_1724.method_18798().field_1352, newY - 0.0312d, mc.field_1724.method_18798().field_1350);
                        }
                    }
                    class_2338 feetPos = class_2338.method_49637(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321());
                    class_2680 feetState = mc.field_1687.method_8320(feetPos);
                    boolean onFluidBlock = feetState.method_26204() instanceof class_2404;
                    if (onFluidBlock) {
                        if ((this.boost.getValue().booleanValue() || !mc.field_1724.method_5771()) && !mc.field_1724.method_5799() && mc.field_1724.method_18798().field_1351 < 0.2d) {
                            mc.field_1724.method_18800(mc.field_1724.method_18798().field_1352, 0.5d, mc.field_1724.method_18798().field_1350);
                            this.jumping = true;
                        }
                        break;
                    }
                    break;
                case "Dolphin":
                    if (mc.field_1724.method_5799() || mc.field_1724.method_5771()) {
                        mc.field_1724.method_18800(mc.field_1724.method_18798().field_1352 * 1.17d, mc.field_1724.method_18798().field_1351, mc.field_1724.method_18798().field_1350 * 1.17d);
                        if (mc.field_1724.field_5976) {
                            mc.field_1724.method_18800(mc.field_1724.method_18798().field_1352, 0.24d, mc.field_1724.method_18798().field_1350);
                        } else {
                            class_2338 above = class_2338.method_49637(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.0d, mc.field_1724.method_23321());
                            if (!mc.field_1687.method_8320(above).method_26215()) {
                                mc.field_1724.method_18800(mc.field_1724.method_18798().field_1352, mc.field_1724.method_18798().field_1351 + 0.04d, mc.field_1724.method_18798().field_1350);
                            }
                        }
                        break;
                    }
                    break;
                case "Matrix Ground":
                    if (isOnLiquidBlock()) {
                        if (!this.matrixBoost.getValue().booleanValue() || mc.field_1724.field_6012 % this.boostTicks.getValue().intValue() == 0) {
                            value = this.matrixSpeed.getValue();
                        } else {
                            value = this.boostSpeed.getValue();
                        }
                        MoveUtil.setSpeed(value.floatValue());
                        break;
                    }
                    break;
                case "Matrix Zoom":
                    if (isOnLiquidBlock()) {
                        MoveUtil.setSpeed(this.matrixSpeed.getValue().floatValue());
                    }
                    class_2338 abovePos = class_2338.method_49637(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.0E-7d, mc.field_1724.method_23321());
                    if (mc.field_1687.method_8320(abovePos).method_26204() instanceof class_2404) {
                        mc.field_1724.field_6017 = 0.0f;
                        mc.field_1724.method_18800(0.0d, 0.08d, 0.0d);
                        break;
                    }
                    break;
                case "Matrix Solid":
                    if (isOnLiquidBlock()) {
                        MoveUtil.setSpeed(mc.field_1724.field_6012 % 2 == 0 ? this.matrixSpeed.getValue().floatValue() : 0.10000000149011612d);
                        break;
                    }
                    break;
            }
        }));
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener(data -> {
            if (mc.field_1724 == null || mc.field_1687 == null || !data.isSend()) {
            }
            switch (this.mode.getValue()) {
                case "Solid":
                    if (isOnLiquid() && !isInLiquid() && !mc.field_1724.field_3913.field_54155.comp_3164() && mc.field_1724.field_6017 < 3.0f) {
                        IPlayerMoveC2SPacket iPlayerMoveC2SPacketPacket = data.packet();
                        if (!(iPlayerMoveC2SPacketPacket instanceof class_2828.class_2829)) {
                            IPlayerMoveC2SPacket iPlayerMoveC2SPacketPacket2 = data.packet();
                            if (iPlayerMoveC2SPacketPacket2 instanceof class_2828.class_2830) {
                                IPlayerMoveC2SPacket iPlayerMoveC2SPacket = (class_2828.class_2830) iPlayerMoveC2SPacketPacket2;
                                PacketEvent.getInstance().setCancel(true);
                                IPlayerMoveC2SPacket a = iPlayerMoveC2SPacket;
                                double x = a.getX();
                                double y = a.getY();
                                double z = a.getZ();
                                float yaw = a.getYaw();
                                float pitch = a.getPitch();
                                boolean og = iPlayerMoveC2SPacket.method_12273();
                                boolean hc = a.isHorizontalCollision();
                                if (this.strict.getValue().booleanValue()) {
                                    this.lastOffset += 0.12f;
                                    if (this.lastOffset > 0.4f) {
                                        this.lastOffset = 0.2f;
                                    }
                                    NetworkUtil.sendSilentPacket((class_2596<?>) new class_2828.class_2830(x, y - ((double) this.lastOffset), z, yaw, pitch, og, hc));
                                } else {
                                    double offsetY = mc.field_1724.field_6012 % 2 == 0 ? y - 0.05d : y;
                                    NetworkUtil.sendSilentPacket((class_2596<?>) new class_2828.class_2830(x, offsetY, z, yaw, pitch, og, hc));
                                }
                            }
                        } else {
                            IPlayerMoveC2SPacket iPlayerMoveC2SPacket2 = (class_2828.class_2829) iPlayerMoveC2SPacketPacket;
                            PacketEvent.getInstance().setCancel(true);
                            IPlayerMoveC2SPacket a2 = iPlayerMoveC2SPacket2;
                            double x2 = a2.getX();
                            double y2 = a2.getY();
                            double z2 = a2.getZ();
                            boolean og2 = iPlayerMoveC2SPacket2.method_12273();
                            boolean hc2 = a2.isHorizontalCollision();
                            if (this.strict.getValue().booleanValue()) {
                                this.lastOffset += 0.12f;
                                if (this.lastOffset > 0.4f) {
                                    this.lastOffset = 0.2f;
                                }
                                NetworkUtil.sendSilentPacket((class_2596<?>) new class_2828.class_2829(x2, y2 - ((double) this.lastOffset), z2, og2, hc2));
                            } else {
                                double offsetY2 = mc.field_1724.field_6012 % 2 == 0 ? y2 - 0.05d : y2;
                                NetworkUtil.sendSilentPacket((class_2596<?>) new class_2828.class_2829(x2, offsetY2, z2, og2, hc2));
                            }
                        }
                        break;
                    }
                    break;
                case "Matrix Ground":
                case "Matrix Solid":
                    if (isOnLiquidBlock()) {
                        IPlayerMoveC2SPacket iPlayerMoveC2SPacketPacket3 = data.packet();
                        if (!(iPlayerMoveC2SPacketPacket3 instanceof class_2828.class_2829)) {
                            IPlayerMoveC2SPacket iPlayerMoveC2SPacketPacket4 = data.packet();
                            if (iPlayerMoveC2SPacketPacket4 instanceof class_2828.class_2830) {
                                PacketEvent.getInstance().setCancel(true);
                                IPlayerMoveC2SPacket a3 = (class_2828.class_2830) iPlayerMoveC2SPacketPacket4;
                                double x3 = a3.getX();
                                double y3 = a3.getY();
                                double z3 = a3.getZ();
                                float yaw2 = a3.getYaw();
                                float pitch2 = a3.getPitch();
                                boolean hc3 = a3.isHorizontalCollision();
                                double offsetY3 = mc.field_1724.field_6012 % 2 == 0 ? y3 + 0.02d : y3 - 0.02d;
                                NetworkUtil.sendSilentPacket((class_2596<?>) new class_2828.class_2830(x3, offsetY3, z3, yaw2, pitch2, false, hc3));
                            }
                        } else {
                            PacketEvent.getInstance().setCancel(true);
                            IPlayerMoveC2SPacket a4 = (class_2828.class_2829) iPlayerMoveC2SPacketPacket3;
                            double x4 = a4.getX();
                            double y4 = a4.getY();
                            double z4 = a4.getZ();
                            boolean hc4 = a4.isHorizontalCollision();
                            double offsetY4 = mc.field_1724.field_6012 % 2 == 0 ? y4 + 0.02d : y4 - 0.02d;
                            NetworkUtil.sendSilentPacket((class_2596<?>) new class_2828.class_2829(x4, offsetY4, z4, false, hc4));
                        }
                        break;
                    }
                    break;
            }
        }));
        addEvents(updateEvent, packetEvent);
    }

    private boolean isOnLiquidBlock() {
        if (mc.field_1724 == null || mc.field_1687 == null) {
            return false;
        }
        class_2338 pos = class_2338.method_49637(mc.field_1724.method_23317(), mc.field_1724.method_23318() - 0.1d, mc.field_1724.method_23321());
        return mc.field_1687.method_8320(pos).method_26204() instanceof class_2404;
    }

    public static boolean isInLiquid() {
        if (mc.field_1724 == null || mc.field_1687 == null || mc.field_1724.field_6017 >= 3.0f) {
            return false;
        }
        class_238 bb = mc.field_1724.method_5829();
        int y = (int) bb.field_1322;
        boolean found = false;
        for (int x = class_3532.method_15357(bb.field_1323); x <= class_3532.method_15357(bb.field_1320); x++) {
            for (int z = class_3532.method_15357(bb.field_1321); z <= class_3532.method_15357(bb.field_1324); z++) {
                class_2680 state = mc.field_1687.method_8320(new class_2338(x, y, z));
                if (!state.method_26215()) {
                    if (!(state.method_26204() instanceof class_2404)) {
                        return false;
                    }
                    found = true;
                }
            }
        }
        return found;
    }

    public static boolean isOnLiquid() {
        if (mc.field_1724 == null || mc.field_1687 == null || mc.field_1724.field_6017 >= 3.0f) {
            return false;
        }
        class_238 bb = mc.field_1724.method_5829().method_989(0.0d, -0.05d, 0.0d);
        int y = (int) bb.field_1322;
        boolean found = false;
        for (int x = class_3532.method_15357(bb.field_1323); x <= class_3532.method_15357(bb.field_1320); x++) {
            for (int z = class_3532.method_15357(bb.field_1321); z <= class_3532.method_15357(bb.field_1324); z++) {
                class_2680 state = mc.field_1687.method_8320(new class_2338(x, y, z));
                if (!state.method_26215()) {
                    if (!(state.method_26204() instanceof class_2404)) {
                        return false;
                    }
                    found = true;
                }
            }
        }
        return found;
    }

    public static boolean checkCollide() {
        if (mc.field_1724 == null || mc.field_1724.method_5715()) {
            return false;
        }
        return (!mc.field_1724.method_5765() || mc.field_1724.method_5854() == null || mc.field_1724.method_5854().field_6017 < 3.0f) && mc.field_1724.field_6017 <= 3.0f;
    }
}
