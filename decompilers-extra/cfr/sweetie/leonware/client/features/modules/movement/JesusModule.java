/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_2338
 *  net.minecraft.class_238
 *  net.minecraft.class_2404
 *  net.minecraft.class_2596
 *  net.minecraft.class_2680
 *  net.minecraft.class_2828$class_2829
 *  net.minecraft.class_2828$class_2830
 *  net.minecraft.class_3532
 */
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

@ModuleRegister(name="Jesus", category=Category.MOVEMENT)
public class JesusModule
extends Module {
    private static final JesusModule instance = new JesusModule();
    public final ModeSetting mode = new ModeSetting("\u0420\u0435\u0436\u0438\u043c").value("Solid").values("Solid", "Trampoline", "Dolphin", "Matrix Ground", "Matrix Zoom", "Matrix Solid");
    public final BooleanSetting glide = new BooleanSetting("\u0413\u043b\u0430\u0439\u0434").value(false).setVisible(() -> this.mode.is("Solid"));
    public final BooleanSetting strict = new BooleanSetting("\u0421\u0442\u0440\u043e\u0433\u0438\u0439").value(false).setVisible(() -> this.mode.is("Solid"));
    public final BooleanSetting boost = new BooleanSetting("\u0411\u0443\u0441\u0442 \u0432 \u043b\u0430\u0432\u0435").value(false).setVisible(() -> this.mode.is("Trampoline"));
    public final SliderSetting matrixSpeed = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c").value(Float.valueOf(1.0f)).range(0.1f, 5.0f).step(0.1f).setVisible(() -> this.mode.is("Matrix Ground") || this.mode.is("Matrix Solid") || this.mode.is("Matrix Zoom"));
    public final BooleanSetting matrixBoost = new BooleanSetting("\u0411\u0443\u0441\u0442").value(false).setVisible(() -> this.mode.is("Matrix Ground"));
    public final SliderSetting boostSpeed = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c \u0431\u0443\u0441\u0442\u0430").value(Float.valueOf(1.35f)).range(0.1f, 4.0f).step(0.01f).setVisible(() -> this.mode.is("Matrix Ground") && (Boolean)this.matrixBoost.getValue() != false);
    public final SliderSetting boostTicks = new SliderSetting("\u0422\u0438\u043a\u0438 \u0431\u0443\u0441\u0442\u0430").value(Float.valueOf(2.0f)).range(1.0f, 30.0f).step(1.0f).setVisible(() -> this.mode.is("Matrix Ground") && (Boolean)this.matrixBoost.getValue() != false);
    public final SliderSetting motionUp = new SliderSetting("Motion Up").value(Float.valueOf(0.42f)).range(0.1f, 2.0f).step(0.01f).setVisible(() -> this.mode.is("Solid"));
    private boolean jumping = false;
    private int glideCounter = 0;
    private float lastOffset = 0.0f;

    public JesusModule() {
        this.addSettings(this.mode, this.glide, this.strict, this.boost, this.matrixSpeed, this.matrixBoost, this.boostSpeed, this.boostTicks, this.motionUp);
    }

    @Override
    public void onDisable() {
        this.jumping = false;
        this.glideCounter = 0;
        this.lastOffset = 0.0f;
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(e -> {
            if (JesusModule.mc.field_1724 == null || JesusModule.mc.field_1687 == null) {
                return;
            }
            switch ((String)this.mode.getValue()) {
                case "Solid": {
                    if (!JesusModule.mc.field_1724.field_3913.field_54155.comp_3164() && !JesusModule.mc.field_1724.field_3913.field_54155.comp_3163() && JesusModule.isInLiquid()) {
                        JesusModule.mc.field_1724.method_18800(JesusModule.mc.field_1724.method_18798().field_1352, 0.1, JesusModule.mc.field_1724.method_18798().field_1350);
                    }
                    if (!((Boolean)this.glide.getValue()).booleanValue() || !JesusModule.isOnLiquid() || !(JesusModule.mc.field_1724.field_6017 < 3.0f) || JesusModule.mc.field_1724.field_3913.field_54155.comp_3163() || JesusModule.isInLiquid() || JesusModule.mc.field_1724.field_3913.field_54155.comp_3164()) break;
                    double[] multipliers = new double[]{1.1, 1.27, 1.51, 1.15, 1.23};
                    double mult = multipliers[this.glideCounter % multipliers.length];
                    JesusModule.mc.field_1724.method_18800(JesusModule.mc.field_1724.method_18798().field_1352 * mult, JesusModule.mc.field_1724.method_18798().field_1351, JesusModule.mc.field_1724.method_18798().field_1350 * mult);
                    this.glideCounter = (this.glideCounter + 1) % multipliers.length;
                    break;
                }
                case "Trampoline": {
                    class_2338 feetPos;
                    class_2680 feetState;
                    boolean onFluidBlock;
                    if (JesusModule.mc.field_1724.method_5799() || JesusModule.mc.field_1724.method_5771()) {
                        JesusModule.mc.field_1724.method_18800(JesusModule.mc.field_1724.method_18798().field_1352, 0.1, JesusModule.mc.field_1724.method_18798().field_1350);
                    }
                    if (this.jumping && !JesusModule.mc.field_1724.method_31549().field_7479 && !JesusModule.mc.field_1724.method_5799()) {
                        if (JesusModule.mc.field_1724.method_18798().field_1351 < -0.3 || JesusModule.mc.field_1724.method_24828() || JesusModule.mc.field_1724.method_6101()) {
                            this.jumping = false;
                        } else {
                            double newY = JesusModule.mc.field_1724.method_18798().field_1351 / 0.98 + 0.08;
                            JesusModule.mc.field_1724.method_18800(JesusModule.mc.field_1724.method_18798().field_1352, newY -= 0.0312, JesusModule.mc.field_1724.method_18798().field_1350);
                        }
                    }
                    if (!(onFluidBlock = (feetState = JesusModule.mc.field_1687.method_8320(feetPos = class_2338.method_49637((double)JesusModule.mc.field_1724.method_23317(), (double)JesusModule.mc.field_1724.method_23318(), (double)JesusModule.mc.field_1724.method_23321()))).method_26204() instanceof class_2404) || !((Boolean)this.boost.getValue()).booleanValue() && JesusModule.mc.field_1724.method_5771() || JesusModule.mc.field_1724.method_5799() || !(JesusModule.mc.field_1724.method_18798().field_1351 < 0.2)) break;
                    JesusModule.mc.field_1724.method_18800(JesusModule.mc.field_1724.method_18798().field_1352, 0.5, JesusModule.mc.field_1724.method_18798().field_1350);
                    this.jumping = true;
                    break;
                }
                case "Dolphin": {
                    if (!JesusModule.mc.field_1724.method_5799() && !JesusModule.mc.field_1724.method_5771()) break;
                    JesusModule.mc.field_1724.method_18800(JesusModule.mc.field_1724.method_18798().field_1352 * 1.17, JesusModule.mc.field_1724.method_18798().field_1351, JesusModule.mc.field_1724.method_18798().field_1350 * 1.17);
                    if (JesusModule.mc.field_1724.field_5976) {
                        JesusModule.mc.field_1724.method_18800(JesusModule.mc.field_1724.method_18798().field_1352, 0.24, JesusModule.mc.field_1724.method_18798().field_1350);
                        break;
                    }
                    class_2338 above = class_2338.method_49637((double)JesusModule.mc.field_1724.method_23317(), (double)(JesusModule.mc.field_1724.method_23318() + 1.0), (double)JesusModule.mc.field_1724.method_23321());
                    if (JesusModule.mc.field_1687.method_8320(above).method_26215()) break;
                    JesusModule.mc.field_1724.method_18800(JesusModule.mc.field_1724.method_18798().field_1352, JesusModule.mc.field_1724.method_18798().field_1351 + 0.04, JesusModule.mc.field_1724.method_18798().field_1350);
                    break;
                }
                case "Matrix Ground": {
                    if (!this.isOnLiquidBlock()) break;
                    MoveUtil.setSpeed((((Boolean)this.matrixBoost.getValue()).booleanValue() ? (JesusModule.mc.field_1724.field_6012 % ((Float)this.boostTicks.getValue()).intValue() == 0 ? (Float)this.matrixSpeed.getValue() : (Float)this.boostSpeed.getValue()) : (Float)this.matrixSpeed.getValue()).floatValue());
                    break;
                }
                case "Matrix Zoom": {
                    class_2338 abovePos;
                    if (this.isOnLiquidBlock()) {
                        MoveUtil.setSpeed(((Float)this.matrixSpeed.getValue()).floatValue());
                    }
                    if (!(JesusModule.mc.field_1687.method_8320(abovePos = class_2338.method_49637((double)JesusModule.mc.field_1724.method_23317(), (double)(JesusModule.mc.field_1724.method_23318() + 1.0E-7), (double)JesusModule.mc.field_1724.method_23321())).method_26204() instanceof class_2404)) break;
                    JesusModule.mc.field_1724.field_6017 = 0.0f;
                    JesusModule.mc.field_1724.method_18800(0.0, 0.08, 0.0);
                    break;
                }
                case "Matrix Solid": {
                    if (!this.isOnLiquidBlock()) break;
                    MoveUtil.setSpeed(JesusModule.mc.field_1724.field_6012 % 2 == 0 ? (double)((Float)this.matrixSpeed.getValue()).floatValue() : (double)0.1f);
                }
            }
        }));
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(data -> {
            if (JesusModule.mc.field_1724 == null || JesusModule.mc.field_1687 == null) {
                return;
            }
            if (!data.isSend()) {
                return;
            }
            switch ((String)this.mode.getValue()) {
                case "Solid": {
                    IPlayerMoveC2SPacket a;
                    if (!JesusModule.isOnLiquid() || JesusModule.isInLiquid()) {
                        return;
                    }
                    if (JesusModule.mc.field_1724.field_3913.field_54155.comp_3164()) {
                        return;
                    }
                    if (JesusModule.mc.field_1724.field_6017 >= 3.0f) {
                        return;
                    }
                    class_2596<?> patt0$temp = data.packet();
                    if (patt0$temp instanceof class_2828.class_2829) {
                        class_2828.class_2829 p = (class_2828.class_2829)patt0$temp;
                        PacketEvent.getInstance().setCancel(true);
                        a = (IPlayerMoveC2SPacket)p;
                        double x = a.getX();
                        double y = a.getY();
                        double z = a.getZ();
                        boolean og = p.method_12273();
                        boolean hc = a.isHorizontalCollision();
                        if (((Boolean)this.strict.getValue()).booleanValue()) {
                            this.lastOffset += 0.12f;
                            if (this.lastOffset > 0.4f) {
                                this.lastOffset = 0.2f;
                            }
                            NetworkUtil.sendSilentPacket(new class_2828.class_2829(x, y - (double)this.lastOffset, z, og, hc));
                            break;
                        }
                        double offsetY = JesusModule.mc.field_1724.field_6012 % 2 == 0 ? y - 0.05 : y;
                        NetworkUtil.sendSilentPacket(new class_2828.class_2829(x, offsetY, z, og, hc));
                        break;
                    }
                    class_2596<?> patt1$temp = data.packet();
                    if (!(patt1$temp instanceof class_2828.class_2830)) break;
                    class_2828.class_2830 p = (class_2828.class_2830)patt1$temp;
                    PacketEvent.getInstance().setCancel(true);
                    a = (IPlayerMoveC2SPacket)p;
                    double x = a.getX();
                    double y = a.getY();
                    double z = a.getZ();
                    float yaw = a.getYaw();
                    float pitch = a.getPitch();
                    boolean og = p.method_12273();
                    boolean hc = a.isHorizontalCollision();
                    if (((Boolean)this.strict.getValue()).booleanValue()) {
                        this.lastOffset += 0.12f;
                        if (this.lastOffset > 0.4f) {
                            this.lastOffset = 0.2f;
                        }
                        NetworkUtil.sendSilentPacket(new class_2828.class_2830(x, y - (double)this.lastOffset, z, yaw, pitch, og, hc));
                        break;
                    }
                    double offsetY = JesusModule.mc.field_1724.field_6012 % 2 == 0 ? y - 0.05 : y;
                    NetworkUtil.sendSilentPacket(new class_2828.class_2830(x, offsetY, z, yaw, pitch, og, hc));
                    break;
                }
                case "Matrix Ground": 
                case "Matrix Solid": {
                    IPlayerMoveC2SPacket a;
                    if (!this.isOnLiquidBlock()) {
                        return;
                    }
                    class_2596<?> patt0$temp = data.packet();
                    if (patt0$temp instanceof class_2828.class_2829) {
                        class_2828.class_2829 p = (class_2828.class_2829)patt0$temp;
                        PacketEvent.getInstance().setCancel(true);
                        a = (IPlayerMoveC2SPacket)p;
                        double x = a.getX();
                        double y = a.getY();
                        double z = a.getZ();
                        boolean hc = a.isHorizontalCollision();
                        double offsetY = JesusModule.mc.field_1724.field_6012 % 2 == 0 ? y + 0.02 : y - 0.02;
                        NetworkUtil.sendSilentPacket(new class_2828.class_2829(x, offsetY, z, false, hc));
                        break;
                    }
                    class_2596<?> patt1$temp = data.packet();
                    if (!(patt1$temp instanceof class_2828.class_2830)) break;
                    class_2828.class_2830 p = (class_2828.class_2830)patt1$temp;
                    PacketEvent.getInstance().setCancel(true);
                    a = (IPlayerMoveC2SPacket)p;
                    double x = a.getX();
                    double y = a.getY();
                    double z = a.getZ();
                    float yaw = a.getYaw();
                    float pitch = a.getPitch();
                    boolean hc = a.isHorizontalCollision();
                    double offsetY = JesusModule.mc.field_1724.field_6012 % 2 == 0 ? y + 0.02 : y - 0.02;
                    NetworkUtil.sendSilentPacket(new class_2828.class_2830(x, offsetY, z, yaw, pitch, false, hc));
                }
            }
        }));
        this.addEvents(updateEvent, packetEvent);
    }

    private boolean isOnLiquidBlock() {
        if (JesusModule.mc.field_1724 == null || JesusModule.mc.field_1687 == null) {
            return false;
        }
        class_2338 pos = class_2338.method_49637((double)JesusModule.mc.field_1724.method_23317(), (double)(JesusModule.mc.field_1724.method_23318() - 0.1), (double)JesusModule.mc.field_1724.method_23321());
        return JesusModule.mc.field_1687.method_8320(pos).method_26204() instanceof class_2404;
    }

    public static boolean isInLiquid() {
        if (JesusModule.mc.field_1724 == null || JesusModule.mc.field_1687 == null) {
            return false;
        }
        if (JesusModule.mc.field_1724.field_6017 >= 3.0f) {
            return false;
        }
        class_238 bb = JesusModule.mc.field_1724.method_5829();
        int y = (int)bb.field_1322;
        boolean found = false;
        for (int x = class_3532.method_15357((double)bb.field_1323); x <= class_3532.method_15357((double)bb.field_1320); ++x) {
            for (int z = class_3532.method_15357((double)bb.field_1321); z <= class_3532.method_15357((double)bb.field_1324); ++z) {
                class_2680 state = JesusModule.mc.field_1687.method_8320(new class_2338(x, y, z));
                if (state.method_26215()) continue;
                if (!(state.method_26204() instanceof class_2404)) {
                    return false;
                }
                found = true;
            }
        }
        return found;
    }

    public static boolean isOnLiquid() {
        if (JesusModule.mc.field_1724 == null || JesusModule.mc.field_1687 == null) {
            return false;
        }
        if (JesusModule.mc.field_1724.field_6017 >= 3.0f) {
            return false;
        }
        class_238 bb = JesusModule.mc.field_1724.method_5829().method_989(0.0, -0.05, 0.0);
        int y = (int)bb.field_1322;
        boolean found = false;
        for (int x = class_3532.method_15357((double)bb.field_1323); x <= class_3532.method_15357((double)bb.field_1320); ++x) {
            for (int z = class_3532.method_15357((double)bb.field_1321); z <= class_3532.method_15357((double)bb.field_1324); ++z) {
                class_2680 state = JesusModule.mc.field_1687.method_8320(new class_2338(x, y, z));
                if (state.method_26215()) continue;
                if (!(state.method_26204() instanceof class_2404)) {
                    return false;
                }
                found = true;
            }
        }
        return found;
    }

    public static boolean checkCollide() {
        if (JesusModule.mc.field_1724 == null) {
            return false;
        }
        if (JesusModule.mc.field_1724.method_5715()) {
            return false;
        }
        if (JesusModule.mc.field_1724.method_5765() && JesusModule.mc.field_1724.method_5854() != null && JesusModule.mc.field_1724.method_5854().field_6017 >= 3.0f) {
            return false;
        }
        return JesusModule.mc.field_1724.field_6017 <= 3.0f;
    }

    @Generated
    public static JesusModule getInstance() {
        return instance;
    }
}

