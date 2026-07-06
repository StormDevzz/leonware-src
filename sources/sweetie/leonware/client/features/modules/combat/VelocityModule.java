package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_2626;
import net.minecraft.class_2708;
import net.minecraft.class_2743;
import net.minecraft.class_2824;
import net.minecraft.class_2828;
import net.minecraft.class_2846;
import net.minecraft.class_2879;
import net.minecraft.class_2885;
import net.minecraft.class_3532;
import net.minecraft.class_3965;
import net.minecraft.class_6374;
import net.minecraft.class_8143;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.events.player.other.MovementInputEvent;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/VelocityModule.class */
@ModuleRegister(name = "Velocity", category = Category.COMBAT)
public class VelocityModule extends Module {
    private static final VelocityModule instance = new VelocityModule();
    private double matrixVelX;
    private double matrixVelZ;
    private static final int MAX_FREEZE_TICKS = 20;
    private final ModeSetting knockback = new ModeSetting("Knockback").value("None").values("None", "Cancel", "Jump reset", "Matrix", "Matrix2", "Grim2371", "Grim", "NewGrim", "Grim+");
    private final ModeSetting swing = new ModeSetting("Swing").values("Без", "Обычный", "Пакетный").value("Обычный").setVisible(() -> {
        return Boolean.valueOf(this.knockback.is("Grim2371"));
    });
    private final SliderSetting grimPlusPackets = new SliderSetting("Пакетов").value(Float.valueOf(4.0f)).range(1.0f, 6.0f).step(1.0f).setVisible(() -> {
        return Boolean.valueOf(this.knockback.is("Grim+"));
    });
    private final SliderSetting grimPlusTicks = new SliderSetting("Тиков").value(Float.valueOf(1.0f)).range(1.0f, 6.0f).step(1.0f).setVisible(() -> {
        return Boolean.valueOf(this.knockback.is("Grim+"));
    });
    private final BooleanSetting grimPlusOnlySprint = new BooleanSetting("Только спринт").value((Boolean) true).setVisible(() -> {
        return Boolean.valueOf(this.knockback.is("Grim+"));
    });
    private boolean isFallDamage = false;
    private boolean matrixFlag = false;
    private boolean cancelNextVelocity = false;
    private boolean delay = false;
    private boolean needClick = false;
    private boolean waitForUpdate = false;
    private boolean waitForPing = false;
    private boolean shouldSkip = false;
    private int freezeTicks = 0;
    private int ccCooldown = 0;
    private class_3965 hitResult = null;
    private float lastPitch = 0.0f;
    private class_243 lastKnockback = class_243.field_1353;
    private boolean newGrimFlag = false;
    private int grimPlusReduceTicks = -1;

    @Generated
    public static VelocityModule getInstance() {
        return instance;
    }

    /* JADX WARN: Type inference failed for: r1v11, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v16, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v19, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v6, types: [sweetie.leonware.api.module.setting.ModeSetting] */
    public VelocityModule() {
        addSettings(this.knockback, this.swing, this.grimPlusPackets, this.grimPlusTicks, this.grimPlusOnlySprint);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onEnable() {
        super.onEnable();
        this.matrixFlag = false;
        this.cancelNextVelocity = false;
        this.delay = false;
        this.needClick = false;
        this.waitForUpdate = false;
        this.waitForPing = false;
        this.hitResult = null;
        this.shouldSkip = false;
        this.freezeTicks = 0;
        this.ccCooldown = 0;
        this.newGrimFlag = false;
        this.lastKnockback = class_243.field_1353;
        this.grimPlusReduceTicks = -1;
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        super.onDisable();
        this.hitResult = null;
        this.waitForUpdate = false;
        this.waitForPing = false;
        this.delay = false;
        this.matrixFlag = false;
        this.newGrimFlag = false;
        this.lastKnockback = class_243.field_1353;
        this.grimPlusReduceTicks = -1;
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1724 == null || mc.field_1687 == null) {
                return;
            }
            this.lastPitch = mc.field_1724.method_36455();
            if (this.knockback.is("Matrix") && mc.field_1724.field_6235 > 0 && !mc.field_1724.method_24828()) {
                double yawRad = mc.field_1724.method_36454() * 0.017453292f;
                double speed = Math.sqrt((mc.field_1724.method_18798().field_1352 * mc.field_1724.method_18798().field_1352) + (mc.field_1724.method_18798().field_1350 * mc.field_1724.method_18798().field_1350));
                mc.field_1724.method_18800((-Math.sin(yawRad)) * speed, mc.field_1724.method_18798().field_1351, Math.cos(yawRad) * speed);
                mc.field_1724.method_5728(mc.field_1724.field_6012 % 2 != 0);
            }
            if (this.knockback.is("Grim2371")) {
                handleGrim2371Update();
            }
            if (this.knockback.is("Grim") && mc.field_1724.field_6235 > 0 && this.lastKnockback.method_1027() > 0.01d) {
                handleGrimCompensation();
            }
            if (this.knockback.is("Matrix2")) {
                if (mc.field_1724.field_6235 > 0 && !mc.field_1724.method_24828()) {
                    double yawRad2 = mc.field_1724.method_36454() * 0.017453292f;
                    double speed2 = Math.sqrt((mc.field_1724.method_18798().field_1352 * mc.field_1724.method_18798().field_1352) + (mc.field_1724.method_18798().field_1350 * mc.field_1724.method_18798().field_1350));
                    mc.field_1724.method_18800((-Math.sin(yawRad2)) * speed2, mc.field_1724.method_18798().field_1351, Math.cos(yawRad2) * speed2);
                    mc.field_1724.method_5728(mc.field_1724.field_6012 % 2 != 0);
                }
                if (mc.field_1724.field_6235 > 0 && this.lastKnockback.method_1027() > 0.01d) {
                    handleGrimCompensation();
                }
            }
            if (this.knockback.is("NewGrim") && this.newGrimFlag) {
                if (this.ccCooldown <= 0) {
                    mc.field_1724.field_3944.method_52787(new class_2828.class_2830(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), mc.field_1724.method_36454(), mc.field_1724.method_36455(), mc.field_1724.method_24828(), mc.field_1724.field_5976));
                    mc.field_1724.field_3944.method_52787(new class_2846(class_2846.class_2847.field_12973, class_2338.method_49638(mc.field_1724.method_19538()), class_2350.field_11033));
                }
                this.newGrimFlag = false;
            }
            if (this.knockback.is("Grim+")) {
                handleGrimPlus();
            }
        }));
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener(event2 -> {
            if (mc.field_1724 == null) {
                return;
            }
            if (!event2.isReceive()) {
                if (event2.isSend()) {
                    handleSendPacket(event2);
                    return;
                }
                return;
            }
            handleReceivePacket(event2);
        }));
        EventListener moveInputEvent = MovementInputEvent.getInstance().subscribe(new Listener(event3 -> {
            if (this.knockback.is("Jump reset") && mc.field_1724.field_6235 == 9 && mc.field_1724.method_24828() && mc.field_1724.method_5624() && !this.isFallDamage) {
                event3.setJump(true);
            }
        }));
        addEvents(updateEvent, packetEvent, moveInputEvent);
    }

    private void handleGrimPlus() {
        if (this.grimPlusReduceTicks == -1) {
            return;
        }
        AuraModule aura = AuraModule.getInstance();
        if (aura == null || !aura.isEnabled() || aura.target == null) {
            this.grimPlusReduceTicks = -1;
            return;
        }
        if (this.grimPlusOnlySprint.getValue().booleanValue() && !mc.field_1724.method_5624()) {
            this.grimPlusReduceTicks = -1;
            return;
        }
        int ticks = this.grimPlusTicks.getValue().intValue();
        int packets = this.grimPlusPackets.getValue().intValue();
        if (this.grimPlusReduceTicks < ticks) {
            for (int i = 0; i < packets; i++) {
                mc.field_1761.method_2918(mc.field_1724, aura.target);
                mc.field_1724.method_6104(class_1268.field_5808);
                class_243 vel = mc.field_1724.method_18798();
                mc.field_1724.method_18800(vel.field_1352 * 0.6d, vel.field_1351, vel.field_1350 * 0.6d);
            }
            this.grimPlusReduceTicks++;
            return;
        }
        this.grimPlusReduceTicks = -1;
    }

    private void handleReceivePacket(PacketEvent.PacketEventData data) {
        if (this.ccCooldown > 0) {
            this.ccCooldown--;
            return;
        }
        class_2743 class_2743VarPacket = data.packet();
        if (class_2743VarPacket instanceof class_2743) {
            class_2743 pac = class_2743VarPacket;
            if (pac.method_11818() == mc.field_1724.method_5628()) {
                switch (this.knockback.getValue()) {
                    case "Cancel":
                        PacketEvent.getInstance().setCancel(true);
                        break;
                    case "Jump reset":
                        this.isFallDamage = pac.method_11815() == 0.0d && pac.method_11816() == 0.0d && pac.method_11819() < 0.0d;
                        break;
                    case "Matrix":
                        if (!this.matrixFlag) {
                            this.matrixVelX = pac.method_11815();
                            this.matrixVelZ = pac.method_11819();
                            PacketEvent.getInstance().setCancel(true);
                            this.matrixFlag = true;
                            break;
                        } else {
                            this.matrixFlag = false;
                            PacketEvent.getInstance().setCancel(true);
                            mc.field_1724.method_18800(mc.field_1724.method_18798().field_1352 + ((this.matrixVelX / 8000.0d) * (-0.1d)), mc.field_1724.method_18798().field_1351, mc.field_1724.method_18798().field_1350 + ((this.matrixVelZ / 8000.0d) * (-0.1d)));
                            break;
                        }
                        break;
                    case "Matrix2":
                        this.lastKnockback = new class_243(pac.method_11815() / 8000.0d, pac.method_11816() / 8000.0d, pac.method_11819() / 8000.0d);
                        if (!this.matrixFlag) {
                            this.matrixVelX = pac.method_11815();
                            this.matrixVelZ = pac.method_11819();
                            PacketEvent.getInstance().setCancel(true);
                            this.matrixFlag = true;
                            break;
                        } else {
                            this.matrixFlag = false;
                            PacketEvent.getInstance().setCancel(true);
                            mc.field_1724.method_18800(mc.field_1724.method_18798().field_1352 + ((this.matrixVelX / 8000.0d) * (-0.1d)), mc.field_1724.method_18798().field_1351, mc.field_1724.method_18798().field_1350 + ((this.matrixVelZ / 8000.0d) * (-0.1d)));
                            break;
                        }
                        break;
                    case "Grim2371":
                        if (this.cancelNextVelocity) {
                            PacketEvent.getInstance().setCancel(true);
                            this.delay = true;
                            this.cancelNextVelocity = false;
                            this.needClick = true;
                            break;
                        }
                        break;
                    case "Grim":
                        this.lastKnockback = new class_243(pac.method_11815() / 8000.0d, pac.method_11816() / 8000.0d, pac.method_11819() / 8000.0d);
                        PacketEvent.getInstance().setCancel(true);
                        break;
                    case "NewGrim":
                        PacketEvent.getInstance().setCancel(true);
                        this.newGrimFlag = true;
                        break;
                    case "Grim+":
                        this.grimPlusReduceTicks = 0;
                        break;
                }
            }
        }
        if (this.knockback.is("Grim2371")) {
            class_8143 class_8143VarPacket = data.packet();
            if (class_8143VarPacket instanceof class_8143) {
                class_8143 damage = class_8143VarPacket;
                if (damage.comp_1267() == mc.field_1724.method_5628()) {
                    this.cancelNextVelocity = true;
                }
            }
            class_2626 class_2626VarPacket = data.packet();
            if (class_2626VarPacket instanceof class_2626) {
                class_2626 blockUpdate = class_2626VarPacket;
                if (this.waitForUpdate && blockUpdate.method_11309().equals(mc.field_1724.method_24515())) {
                    this.waitForPing = true;
                    this.needClick = false;
                    return;
                }
            }
        }
        if (data.packet() instanceof class_2708) {
            if (this.knockback.is("Grim2371") || this.knockback.is("Grim") || this.knockback.is("NewGrim") || this.knockback.is("Grim+")) {
                this.ccCooldown = 5;
            }
        }
    }

    private void handleSendPacket(PacketEvent.PacketEventData data) {
        if (this.knockback.is("Grim2371")) {
            if ((data.packet() instanceof class_2824) || (data.packet() instanceof class_2885)) {
                this.shouldSkip = true;
            }
            class_2828 class_2828VarPacket = data.packet();
            if (class_2828VarPacket instanceof class_2828) {
                class_2828 movePacket = class_2828VarPacket;
                if (movePacket.method_36171() && this.waitForUpdate) {
                    PacketEvent.getInstance().setCancel(true);
                }
            }
            if ((data.packet() instanceof class_6374) && this.waitForPing) {
                this.waitForUpdate = false;
                this.waitForPing = false;
            }
        }
    }

    private void handleGrimCompensation() {
        class_243 playerPos = mc.field_1724.method_19538();
        class_243 predicted = playerPos.method_1019(this.lastKnockback.method_1021(1.5d));
        class_243 eyePos = playerPos.method_1031(0.0d, mc.field_1724.method_18381(mc.field_1724.method_18376()), 0.0d);
        class_243 diff = predicted.method_1020(eyePos);
        float yaw = ((float) ((Math.atan2(diff.field_1350, diff.field_1352) * 180.0d) / 3.141592653589793d)) - 90.0f;
        float yawDiff = class_3532.method_15393(mc.field_1724.method_36454() - yaw);
        class_243 velocity = mc.field_1724.method_18798();
        if (Math.abs(yawDiff) <= 60.0f) {
            mc.field_1724.method_18800(velocity.field_1352 + (this.lastKnockback.field_1352 * (-1.2d)), velocity.field_1351, velocity.field_1350 + (this.lastKnockback.field_1350 * (-1.2d)));
            if (mc.field_1724.method_24828()) {
                mc.field_1724.method_6043();
                return;
            }
            return;
        }
        if (yawDiff > 120.0f || yawDiff < -120.0f) {
            mc.field_1724.method_18800(velocity.field_1352 + (this.lastKnockback.field_1352 * 1.3d), velocity.field_1351, velocity.field_1350 + (this.lastKnockback.field_1350 * 1.3d));
        } else {
            mc.field_1724.method_18800(velocity.field_1352 + (this.lastKnockback.field_1352 * (-0.8d)), velocity.field_1351, velocity.field_1350 + (this.lastKnockback.field_1350 * (-0.8d)));
        }
    }

    private void handleGrim2371Update() {
        if (this.needClick && !this.shouldSkip && !mc.field_1724.method_6115()) {
            class_243 lookDown = new class_243(mc.field_1724.method_23317(), mc.field_1724.method_23318() - 1.0d, mc.field_1724.method_23321());
            class_2338 targetPos = mc.field_1724.method_24515().method_10074();
            if (!mc.field_1687.method_8320(targetPos).method_26215()) {
                this.hitResult = new class_3965(lookDown, class_2350.field_11036, targetPos, false);
            }
        }
        if (this.hitResult != null && this.delay) {
            this.delay = false;
            mc.field_1761.method_2896(mc.field_1724, class_1268.field_5808, this.hitResult);
            doSwing();
            if (this.lastPitch != 90.0f) {
                mc.field_1724.field_3944.method_52787(new class_2828.class_2831(mc.field_1724.method_36454(), 90.0f, mc.field_1724.method_24828(), mc.field_1724.field_5976));
            } else {
                mc.field_1724.field_3944.method_52787(new class_2828.class_5911(mc.field_1724.method_24828(), mc.field_1724.field_5976));
            }
            this.freezeTicks = 0;
            this.waitForUpdate = true;
            this.hitResult = null;
            this.needClick = false;
        }
        if (this.waitForUpdate) {
            this.freezeTicks++;
            if (this.freezeTicks > 20) {
                this.waitForUpdate = false;
                this.waitForPing = false;
                this.needClick = false;
            }
        }
        this.shouldSkip = false;
    }

    private void doSwing() {
        switch (this.swing.getValue()) {
            case "Обычный":
                mc.field_1724.method_6104(class_1268.field_5808);
                break;
            case "Пакетный":
                mc.field_1724.field_3944.method_52787(new class_2879(class_1268.field_5808));
                break;
        }
    }
}
