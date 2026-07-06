/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1268
 *  net.minecraft.class_1297
 *  net.minecraft.class_1657
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_2374
 *  net.minecraft.class_243
 *  net.minecraft.class_2596
 *  net.minecraft.class_2626
 *  net.minecraft.class_2708
 *  net.minecraft.class_2743
 *  net.minecraft.class_2824
 *  net.minecraft.class_2828
 *  net.minecraft.class_2828$class_2830
 *  net.minecraft.class_2828$class_2831
 *  net.minecraft.class_2828$class_5911
 *  net.minecraft.class_2846
 *  net.minecraft.class_2846$class_2847
 *  net.minecraft.class_2879
 *  net.minecraft.class_2885
 *  net.minecraft.class_3532
 *  net.minecraft.class_3965
 *  net.minecraft.class_6374
 *  net.minecraft.class_8143
 */
package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2374;
import net.minecraft.class_243;
import net.minecraft.class_2596;
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
import sweetie.leonware.client.features.modules.combat.AuraModule;

@ModuleRegister(name="Velocity", category=Category.COMBAT)
public class VelocityModule
extends Module {
    private static final VelocityModule instance = new VelocityModule();
    private final ModeSetting knockback = new ModeSetting("Knockback").value("None").values("None", "Cancel", "Jump reset", "Matrix", "Matrix2", "Grim2371", "Grim", "NewGrim", "Grim+");
    private final ModeSetting swing = new ModeSetting("Swing").values("\u0411\u0435\u0437", "\u041e\u0431\u044b\u0447\u043d\u044b\u0439", "\u041f\u0430\u043a\u0435\u0442\u043d\u044b\u0439").value("\u041e\u0431\u044b\u0447\u043d\u044b\u0439").setVisible(() -> this.knockback.is("Grim2371"));
    private final SliderSetting grimPlusPackets = new SliderSetting("\u041f\u0430\u043a\u0435\u0442\u043e\u0432").value(Float.valueOf(4.0f)).range(1.0f, 6.0f).step(1.0f).setVisible(() -> this.knockback.is("Grim+"));
    private final SliderSetting grimPlusTicks = new SliderSetting("\u0422\u0438\u043a\u043e\u0432").value(Float.valueOf(1.0f)).range(1.0f, 6.0f).step(1.0f).setVisible(() -> this.knockback.is("Grim+"));
    private final BooleanSetting grimPlusOnlySprint = new BooleanSetting("\u0422\u043e\u043b\u044c\u043a\u043e \u0441\u043f\u0440\u0438\u043d\u0442").value(true).setVisible(() -> this.knockback.is("Grim+"));
    private boolean isFallDamage = false;
    private boolean matrixFlag = false;
    private double matrixVelX;
    private double matrixVelZ;
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
    private static final int MAX_FREEZE_TICKS = 20;
    private class_243 lastKnockback = class_243.field_1353;
    private boolean newGrimFlag = false;
    private int grimPlusReduceTicks = -1;

    public VelocityModule() {
        this.addSettings(this.knockback, this.swing, this.grimPlusPackets, this.grimPlusTicks, this.grimPlusOnlySprint);
    }

    @Override
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

    @Override
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

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            double speed;
            double yawRad;
            if (VelocityModule.mc.field_1724 == null || VelocityModule.mc.field_1687 == null) {
                return;
            }
            this.lastPitch = VelocityModule.mc.field_1724.method_36455();
            if (this.knockback.is("Matrix") && VelocityModule.mc.field_1724.field_6235 > 0 && !VelocityModule.mc.field_1724.method_24828()) {
                yawRad = VelocityModule.mc.field_1724.method_36454() * ((float)Math.PI / 180);
                speed = Math.sqrt(VelocityModule.mc.field_1724.method_18798().field_1352 * VelocityModule.mc.field_1724.method_18798().field_1352 + VelocityModule.mc.field_1724.method_18798().field_1350 * VelocityModule.mc.field_1724.method_18798().field_1350);
                VelocityModule.mc.field_1724.method_18800(-Math.sin(yawRad) * speed, VelocityModule.mc.field_1724.method_18798().field_1351, Math.cos(yawRad) * speed);
                VelocityModule.mc.field_1724.method_5728(VelocityModule.mc.field_1724.field_6012 % 2 != 0);
            }
            if (this.knockback.is("Grim2371")) {
                this.handleGrim2371Update();
            }
            if (this.knockback.is("Grim") && VelocityModule.mc.field_1724.field_6235 > 0 && this.lastKnockback.method_1027() > 0.01) {
                this.handleGrimCompensation();
            }
            if (this.knockback.is("Matrix2")) {
                if (VelocityModule.mc.field_1724.field_6235 > 0 && !VelocityModule.mc.field_1724.method_24828()) {
                    yawRad = VelocityModule.mc.field_1724.method_36454() * ((float)Math.PI / 180);
                    speed = Math.sqrt(VelocityModule.mc.field_1724.method_18798().field_1352 * VelocityModule.mc.field_1724.method_18798().field_1352 + VelocityModule.mc.field_1724.method_18798().field_1350 * VelocityModule.mc.field_1724.method_18798().field_1350);
                    VelocityModule.mc.field_1724.method_18800(-Math.sin(yawRad) * speed, VelocityModule.mc.field_1724.method_18798().field_1351, Math.cos(yawRad) * speed);
                    VelocityModule.mc.field_1724.method_5728(VelocityModule.mc.field_1724.field_6012 % 2 != 0);
                }
                if (VelocityModule.mc.field_1724.field_6235 > 0 && this.lastKnockback.method_1027() > 0.01) {
                    this.handleGrimCompensation();
                }
            }
            if (this.knockback.is("NewGrim") && this.newGrimFlag) {
                if (this.ccCooldown <= 0) {
                    VelocityModule.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2830(VelocityModule.mc.field_1724.method_23317(), VelocityModule.mc.field_1724.method_23318(), VelocityModule.mc.field_1724.method_23321(), VelocityModule.mc.field_1724.method_36454(), VelocityModule.mc.field_1724.method_36455(), VelocityModule.mc.field_1724.method_24828(), VelocityModule.mc.field_1724.field_5976));
                    VelocityModule.mc.field_1724.field_3944.method_52787((class_2596)new class_2846(class_2846.class_2847.field_12973, class_2338.method_49638((class_2374)VelocityModule.mc.field_1724.method_19538()), class_2350.field_11033));
                }
                this.newGrimFlag = false;
            }
            if (this.knockback.is("Grim+")) {
                this.handleGrimPlus();
            }
        }));
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(event -> {
            if (VelocityModule.mc.field_1724 == null) {
                return;
            }
            if (event.isReceive()) {
                this.handleReceivePacket((PacketEvent.PacketEventData)event);
            } else if (event.isSend()) {
                this.handleSendPacket((PacketEvent.PacketEventData)event);
            }
        }));
        EventListener moveInputEvent = MovementInputEvent.getInstance().subscribe(new Listener<MovementInputEvent.MovementInputEventData>(event -> {
            if (!this.knockback.is("Jump reset")) {
                return;
            }
            if (VelocityModule.mc.field_1724.field_6235 == 9 && VelocityModule.mc.field_1724.method_24828() && VelocityModule.mc.field_1724.method_5624() && !this.isFallDamage) {
                event.setJump(true);
            }
        }));
        this.addEvents(updateEvent, packetEvent, moveInputEvent);
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
        if (((Boolean)this.grimPlusOnlySprint.getValue()).booleanValue() && !VelocityModule.mc.field_1724.method_5624()) {
            this.grimPlusReduceTicks = -1;
            return;
        }
        int ticks = ((Float)this.grimPlusTicks.getValue()).intValue();
        int packets = ((Float)this.grimPlusPackets.getValue()).intValue();
        if (this.grimPlusReduceTicks < ticks) {
            for (int i = 0; i < packets; ++i) {
                VelocityModule.mc.field_1761.method_2918((class_1657)VelocityModule.mc.field_1724, (class_1297)aura.target);
                VelocityModule.mc.field_1724.method_6104(class_1268.field_5808);
                class_243 vel = VelocityModule.mc.field_1724.method_18798();
                VelocityModule.mc.field_1724.method_18800(vel.field_1352 * 0.6, vel.field_1351, vel.field_1350 * 0.6);
            }
            ++this.grimPlusReduceTicks;
        } else {
            this.grimPlusReduceTicks = -1;
        }
    }

    private void handleReceivePacket(PacketEvent.PacketEventData data) {
        class_2743 pac;
        if (this.ccCooldown > 0) {
            --this.ccCooldown;
            return;
        }
        Object object = data.packet();
        if (object instanceof class_2743 && (pac = (class_2743)object).method_11818() == VelocityModule.mc.field_1724.method_5628()) {
            switch ((String)this.knockback.getValue()) {
                case "Cancel": {
                    PacketEvent.getInstance().setCancel(true);
                    break;
                }
                case "Jump reset": {
                    this.isFallDamage = pac.method_11815() == 0.0 && pac.method_11816() == 0.0 && pac.method_11819() < 0.0;
                    break;
                }
                case "Matrix": {
                    if (!this.matrixFlag) {
                        this.matrixVelX = pac.method_11815();
                        this.matrixVelZ = pac.method_11819();
                        PacketEvent.getInstance().setCancel(true);
                        this.matrixFlag = true;
                        break;
                    }
                    this.matrixFlag = false;
                    PacketEvent.getInstance().setCancel(true);
                    VelocityModule.mc.field_1724.method_18800(VelocityModule.mc.field_1724.method_18798().field_1352 + this.matrixVelX / 8000.0 * -0.1, VelocityModule.mc.field_1724.method_18798().field_1351, VelocityModule.mc.field_1724.method_18798().field_1350 + this.matrixVelZ / 8000.0 * -0.1);
                    break;
                }
                case "Matrix2": {
                    this.lastKnockback = new class_243(pac.method_11815() / 8000.0, pac.method_11816() / 8000.0, pac.method_11819() / 8000.0);
                    if (!this.matrixFlag) {
                        this.matrixVelX = pac.method_11815();
                        this.matrixVelZ = pac.method_11819();
                        PacketEvent.getInstance().setCancel(true);
                        this.matrixFlag = true;
                        break;
                    }
                    this.matrixFlag = false;
                    PacketEvent.getInstance().setCancel(true);
                    VelocityModule.mc.field_1724.method_18800(VelocityModule.mc.field_1724.method_18798().field_1352 + this.matrixVelX / 8000.0 * -0.1, VelocityModule.mc.field_1724.method_18798().field_1351, VelocityModule.mc.field_1724.method_18798().field_1350 + this.matrixVelZ / 8000.0 * -0.1);
                    break;
                }
                case "Grim2371": {
                    if (!this.cancelNextVelocity) break;
                    PacketEvent.getInstance().setCancel(true);
                    this.delay = true;
                    this.cancelNextVelocity = false;
                    this.needClick = true;
                    break;
                }
                case "Grim": {
                    this.lastKnockback = new class_243(pac.method_11815() / 8000.0, pac.method_11816() / 8000.0, pac.method_11819() / 8000.0);
                    PacketEvent.getInstance().setCancel(true);
                    break;
                }
                case "NewGrim": {
                    PacketEvent.getInstance().setCancel(true);
                    this.newGrimFlag = true;
                    break;
                }
                case "Grim+": {
                    this.grimPlusReduceTicks = 0;
                }
            }
        }
        if (this.knockback.is("Grim2371")) {
            class_8143 damage;
            object = data.packet();
            if (object instanceof class_8143 && (damage = (class_8143)object).comp_1267() == VelocityModule.mc.field_1724.method_5628()) {
                this.cancelNextVelocity = true;
            }
            if ((object = data.packet()) instanceof class_2626) {
                class_2626 blockUpdate = (class_2626)object;
                if (this.waitForUpdate && blockUpdate.method_11309().equals((Object)VelocityModule.mc.field_1724.method_24515())) {
                    this.waitForPing = true;
                    this.needClick = false;
                    return;
                }
            }
        }
        if (data.packet() instanceof class_2708 && (this.knockback.is("Grim2371") || this.knockback.is("Grim") || this.knockback.is("NewGrim") || this.knockback.is("Grim+"))) {
            this.ccCooldown = 5;
        }
    }

    private void handleSendPacket(PacketEvent.PacketEventData data) {
        class_2828 movePacket;
        class_2596<?> class_25962;
        if (!this.knockback.is("Grim2371")) {
            return;
        }
        if (data.packet() instanceof class_2824 || data.packet() instanceof class_2885) {
            this.shouldSkip = true;
        }
        if ((class_25962 = data.packet()) instanceof class_2828 && (movePacket = (class_2828)class_25962).method_36171() && this.waitForUpdate) {
            PacketEvent.getInstance().setCancel(true);
        }
        if (data.packet() instanceof class_6374 && this.waitForPing) {
            this.waitForUpdate = false;
            this.waitForPing = false;
        }
    }

    private void handleGrimCompensation() {
        class_243 playerPos = VelocityModule.mc.field_1724.method_19538();
        class_243 predicted = playerPos.method_1019(this.lastKnockback.method_1021(1.5));
        class_243 eyePos = playerPos.method_1031(0.0, (double)VelocityModule.mc.field_1724.method_18381(VelocityModule.mc.field_1724.method_18376()), 0.0);
        class_243 diff = predicted.method_1020(eyePos);
        float yaw = (float)(Math.atan2(diff.field_1350, diff.field_1352) * 180.0 / Math.PI) - 90.0f;
        float yawDiff = class_3532.method_15393((float)(VelocityModule.mc.field_1724.method_36454() - yaw));
        class_243 velocity = VelocityModule.mc.field_1724.method_18798();
        if (Math.abs(yawDiff) <= 60.0f) {
            VelocityModule.mc.field_1724.method_18800(velocity.field_1352 + this.lastKnockback.field_1352 * -1.2, velocity.field_1351, velocity.field_1350 + this.lastKnockback.field_1350 * -1.2);
            if (VelocityModule.mc.field_1724.method_24828()) {
                VelocityModule.mc.field_1724.method_6043();
            }
        } else if (yawDiff > 120.0f || yawDiff < -120.0f) {
            VelocityModule.mc.field_1724.method_18800(velocity.field_1352 + this.lastKnockback.field_1352 * 1.3, velocity.field_1351, velocity.field_1350 + this.lastKnockback.field_1350 * 1.3);
        } else {
            VelocityModule.mc.field_1724.method_18800(velocity.field_1352 + this.lastKnockback.field_1352 * -0.8, velocity.field_1351, velocity.field_1350 + this.lastKnockback.field_1350 * -0.8);
        }
    }

    private void handleGrim2371Update() {
        if (this.needClick && !this.shouldSkip && !VelocityModule.mc.field_1724.method_6115()) {
            class_243 lookDown = new class_243(VelocityModule.mc.field_1724.method_23317(), VelocityModule.mc.field_1724.method_23318() - 1.0, VelocityModule.mc.field_1724.method_23321());
            class_2338 targetPos = VelocityModule.mc.field_1724.method_24515().method_10074();
            if (!VelocityModule.mc.field_1687.method_8320(targetPos).method_26215()) {
                this.hitResult = new class_3965(lookDown, class_2350.field_11036, targetPos, false);
            }
        }
        if (this.hitResult != null && this.delay) {
            this.delay = false;
            VelocityModule.mc.field_1761.method_2896(VelocityModule.mc.field_1724, class_1268.field_5808, this.hitResult);
            this.doSwing();
            if (this.lastPitch != 90.0f) {
                VelocityModule.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2831(VelocityModule.mc.field_1724.method_36454(), 90.0f, VelocityModule.mc.field_1724.method_24828(), VelocityModule.mc.field_1724.field_5976));
            } else {
                VelocityModule.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_5911(VelocityModule.mc.field_1724.method_24828(), VelocityModule.mc.field_1724.field_5976));
            }
            this.freezeTicks = 0;
            this.waitForUpdate = true;
            this.hitResult = null;
            this.needClick = false;
        }
        if (this.waitForUpdate) {
            ++this.freezeTicks;
            if (this.freezeTicks > 20) {
                this.waitForUpdate = false;
                this.waitForPing = false;
                this.needClick = false;
            }
        }
        this.shouldSkip = false;
    }

    private void doSwing() {
        switch ((String)this.swing.getValue()) {
            case "\u041e\u0431\u044b\u0447\u043d\u044b\u0439": {
                VelocityModule.mc.field_1724.method_6104(class_1268.field_5808);
                break;
            }
            case "\u041f\u0430\u043a\u0435\u0442\u043d\u044b\u0439": {
                VelocityModule.mc.field_1724.field_3944.method_52787((class_2596)new class_2879(class_1268.field_5808));
            }
        }
    }

    @Generated
    public static VelocityModule getInstance() {
        return instance;
    }
}

