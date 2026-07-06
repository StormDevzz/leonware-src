// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import net.minecraft.class_2879;
import net.minecraft.class_3532;
import net.minecraft.class_6374;
import net.minecraft.class_2885;
import net.minecraft.class_2824;
import net.minecraft.class_2708;
import net.minecraft.class_2626;
import net.minecraft.class_8143;
import net.minecraft.class_2743;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.events.player.other.MovementInputEvent;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.Listener;
import net.minecraft.class_2350;
import net.minecraft.class_2374;
import net.minecraft.class_2338;
import net.minecraft.class_2846;
import net.minecraft.class_2596;
import net.minecraft.class_2828;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.setting.Setting;
import net.minecraft.class_243;
import net.minecraft.class_3965;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Velocity", category = Category.COMBAT)
public class VelocityModule extends Module
{
    private static final VelocityModule instance;
    private final ModeSetting knockback;
    private final ModeSetting swing;
    private final SliderSetting grimPlusPackets;
    private final SliderSetting grimPlusTicks;
    private final BooleanSetting grimPlusOnlySprint;
    private boolean isFallDamage;
    private boolean matrixFlag;
    private double matrixVelX;
    private double matrixVelZ;
    private boolean cancelNextVelocity;
    private boolean delay;
    private boolean needClick;
    private boolean waitForUpdate;
    private boolean waitForPing;
    private boolean shouldSkip;
    private int freezeTicks;
    private int ccCooldown;
    private class_3965 hitResult;
    private float lastPitch;
    private static final int MAX_FREEZE_TICKS = 20;
    private class_243 lastKnockback;
    private boolean newGrimFlag;
    private int grimPlusReduceTicks;
    
    public VelocityModule() {
        this.knockback = new ModeSetting("Knockback").value("None").values("None", "Cancel", "Jump reset", "Matrix", "Matrix2", "Grim2371", "Grim", "NewGrim", "Grim+");
        this.swing = new ModeSetting("Swing").values("\u0411\u0435\u0437", "\u041e\u0431\u044b\u0447\u043d\u044b\u0439", "\u041f\u0430\u043a\u0435\u0442\u043d\u044b\u0439").value("\u041e\u0431\u044b\u0447\u043d\u044b\u0439").setVisible(() -> this.knockback.is("Grim2371"));
        this.grimPlusPackets = new SliderSetting("\u041f\u0430\u043a\u0435\u0442\u043e\u0432").value(4.0f).range(1.0f, 6.0f).step(1.0f).setVisible(() -> this.knockback.is("Grim+"));
        this.grimPlusTicks = new SliderSetting("\u0422\u0438\u043a\u043e\u0432").value(1.0f).range(1.0f, 6.0f).step(1.0f).setVisible(() -> this.knockback.is("Grim+"));
        this.grimPlusOnlySprint = new BooleanSetting("\u0422\u043e\u043b\u044c\u043a\u043e \u0441\u043f\u0440\u0438\u043d\u0442").value(true).setVisible(() -> this.knockback.is("Grim+"));
        this.isFallDamage = false;
        this.matrixFlag = false;
        this.cancelNextVelocity = false;
        this.delay = false;
        this.needClick = false;
        this.waitForUpdate = false;
        this.waitForPing = false;
        this.shouldSkip = false;
        this.freezeTicks = 0;
        this.ccCooldown = 0;
        this.hitResult = null;
        this.lastPitch = 0.0f;
        this.lastKnockback = class_243.field_1353;
        this.newGrimFlag = false;
        this.grimPlusReduceTicks = -1;
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
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (VelocityModule.mc.field_1724 == null || VelocityModule.mc.field_1687 == null) {
                return;
            }
            else {
                this.lastPitch = VelocityModule.mc.field_1724.method_36455();
                if (this.knockback.is("Matrix") && VelocityModule.mc.field_1724.field_6235 > 0 && !VelocityModule.mc.field_1724.method_24828()) {
                    final double yawRad = VelocityModule.mc.field_1724.method_36454() * 0.017453292f;
                    final double speed = Math.sqrt(VelocityModule.mc.field_1724.method_18798().field_1352 * VelocityModule.mc.field_1724.method_18798().field_1352 + VelocityModule.mc.field_1724.method_18798().field_1350 * VelocityModule.mc.field_1724.method_18798().field_1350);
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
                        final double yawRad2 = VelocityModule.mc.field_1724.method_36454() * 0.017453292f;
                        final double speed2 = Math.sqrt(VelocityModule.mc.field_1724.method_18798().field_1352 * VelocityModule.mc.field_1724.method_18798().field_1352 + VelocityModule.mc.field_1724.method_18798().field_1350 * VelocityModule.mc.field_1724.method_18798().field_1350);
                        VelocityModule.mc.field_1724.method_18800(-Math.sin(yawRad2) * speed2, VelocityModule.mc.field_1724.method_18798().field_1351, Math.cos(yawRad2) * speed2);
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
                return;
            }
        }));
        final EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(event -> {
            if (VelocityModule.mc.field_1724 == null) {
                return;
            }
            else {
                if (event.isReceive()) {
                    this.handleReceivePacket(event);
                }
                else if (event.isSend()) {
                    this.handleSendPacket(event);
                }
                return;
            }
        }));
        final EventListener moveInputEvent = MovementInputEvent.getInstance().subscribe(new Listener<MovementInputEvent.MovementInputEventData>(event -> {
            if (!this.knockback.is("Jump reset")) {
                return;
            }
            else {
                if (VelocityModule.mc.field_1724.field_6235 == 9 && VelocityModule.mc.field_1724.method_24828() && VelocityModule.mc.field_1724.method_5624() && !this.isFallDamage) {
                    event.setJump(true);
                }
                return;
            }
        }));
        this.addEvents(updateEvent, packetEvent, moveInputEvent);
    }
    
    private void handleGrimPlus() {
        if (this.grimPlusReduceTicks == -1) {
            return;
        }
        final AuraModule aura = AuraModule.getInstance();
        if (aura == null || !aura.isEnabled() || aura.target == null) {
            this.grimPlusReduceTicks = -1;
            return;
        }
        if (this.grimPlusOnlySprint.getValue() && !VelocityModule.mc.field_1724.method_5624()) {
            this.grimPlusReduceTicks = -1;
            return;
        }
        final int ticks = this.grimPlusTicks.getValue().intValue();
        final int packets = this.grimPlusPackets.getValue().intValue();
        if (this.grimPlusReduceTicks < ticks) {
            for (int i = 0; i < packets; ++i) {
                VelocityModule.mc.field_1761.method_2918((class_1657)VelocityModule.mc.field_1724, (class_1297)aura.target);
                VelocityModule.mc.field_1724.method_6104(class_1268.field_5808);
                final class_243 vel = VelocityModule.mc.field_1724.method_18798();
                VelocityModule.mc.field_1724.method_18800(vel.field_1352 * 0.6, vel.field_1351, vel.field_1350 * 0.6);
            }
            ++this.grimPlusReduceTicks;
        }
        else {
            this.grimPlusReduceTicks = -1;
        }
    }
    
    private void handleReceivePacket(final PacketEvent.PacketEventData data) {
        if (this.ccCooldown > 0) {
            --this.ccCooldown;
            return;
        }
        final class_2596<?> packet = data.packet();
        if (packet instanceof final class_2743 pac) {
            if (pac.method_11818() == VelocityModule.mc.field_1724.method_5628()) {
                final String s = this.knockback.getValue();
                switch (s) {
                    case "Cancel": {
                        PacketEvent.getInstance().setCancel(true);
                        break;
                    }
                    case "Jump reset": {
                        this.isFallDamage = (pac.method_11815() == 0.0 && pac.method_11816() == 0.0 && pac.method_11819() < 0.0);
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
                        if (this.cancelNextVelocity) {
                            PacketEvent.getInstance().setCancel(true);
                            this.delay = true;
                            this.cancelNextVelocity = false;
                            this.needClick = true;
                            break;
                        }
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
                        break;
                    }
                }
            }
        }
        if (this.knockback.is("Grim2371")) {
            final class_2596<?> packet2 = data.packet();
            if (packet2 instanceof final class_8143 damage) {
                if (damage.comp_1267() == VelocityModule.mc.field_1724.method_5628()) {
                    this.cancelNextVelocity = true;
                }
            }
            final class_2596<?> packet3 = data.packet();
            if (packet3 instanceof final class_2626 blockUpdate) {
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
    
    private void handleSendPacket(final PacketEvent.PacketEventData data) {
        if (!this.knockback.is("Grim2371")) {
            return;
        }
        if (data.packet() instanceof class_2824 || data.packet() instanceof class_2885) {
            this.shouldSkip = true;
        }
        final class_2596<?> packet = data.packet();
        if (packet instanceof final class_2828 movePacket) {
            if (movePacket.method_36171() && this.waitForUpdate) {
                PacketEvent.getInstance().setCancel(true);
            }
        }
        if (data.packet() instanceof class_6374 && this.waitForPing) {
            this.waitForUpdate = false;
            this.waitForPing = false;
        }
    }
    
    private void handleGrimCompensation() {
        final class_243 playerPos = VelocityModule.mc.field_1724.method_19538();
        final class_243 predicted = playerPos.method_1019(this.lastKnockback.method_1021(1.5));
        final class_243 eyePos = playerPos.method_1031(0.0, (double)VelocityModule.mc.field_1724.method_18381(VelocityModule.mc.field_1724.method_18376()), 0.0);
        final class_243 diff = predicted.method_1020(eyePos);
        final float yaw = (float)(Math.atan2(diff.field_1350, diff.field_1352) * 180.0 / 3.141592653589793) - 90.0f;
        final float yawDiff = class_3532.method_15393(VelocityModule.mc.field_1724.method_36454() - yaw);
        final class_243 velocity = VelocityModule.mc.field_1724.method_18798();
        if (Math.abs(yawDiff) <= 60.0f) {
            VelocityModule.mc.field_1724.method_18800(velocity.field_1352 + this.lastKnockback.field_1352 * -1.2, velocity.field_1351, velocity.field_1350 + this.lastKnockback.field_1350 * -1.2);
            if (VelocityModule.mc.field_1724.method_24828()) {
                VelocityModule.mc.field_1724.method_6043();
            }
        }
        else if (yawDiff > 120.0f || yawDiff < -120.0f) {
            VelocityModule.mc.field_1724.method_18800(velocity.field_1352 + this.lastKnockback.field_1352 * 1.3, velocity.field_1351, velocity.field_1350 + this.lastKnockback.field_1350 * 1.3);
        }
        else {
            VelocityModule.mc.field_1724.method_18800(velocity.field_1352 + this.lastKnockback.field_1352 * -0.8, velocity.field_1351, velocity.field_1350 + this.lastKnockback.field_1350 * -0.8);
        }
    }
    
    private void handleGrim2371Update() {
        if (this.needClick && !this.shouldSkip && !VelocityModule.mc.field_1724.method_6115()) {
            final class_243 lookDown = new class_243(VelocityModule.mc.field_1724.method_23317(), VelocityModule.mc.field_1724.method_23318() - 1.0, VelocityModule.mc.field_1724.method_23321());
            final class_2338 targetPos = VelocityModule.mc.field_1724.method_24515().method_10074();
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
            }
            else {
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
        final String s = this.swing.getValue();
        switch (s) {
            case "\u041e\u0431\u044b\u0447\u043d\u044b\u0439": {
                VelocityModule.mc.field_1724.method_6104(class_1268.field_5808);
                break;
            }
            case "\u041f\u0430\u043a\u0435\u0442\u043d\u044b\u0439": {
                VelocityModule.mc.field_1724.field_3944.method_52787((class_2596)new class_2879(class_1268.field_5808));
                break;
            }
        }
    }
    
    @Generated
    public static VelocityModule getInstance() {
        return VelocityModule.instance;
    }
    
    static {
        instance = new VelocityModule();
    }
}
