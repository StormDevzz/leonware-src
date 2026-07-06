/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1293
 *  net.minecraft.class_1294
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_1922
 *  net.minecraft.class_2338
 *  net.minecraft.class_2338$class_2339
 *  net.minecraft.class_2350
 *  net.minecraft.class_2350$class_2353
 *  net.minecraft.class_238
 *  net.minecraft.class_243
 *  net.minecraft.class_2596
 *  net.minecraft.class_2680
 *  net.minecraft.class_2708
 *  net.minecraft.class_2743
 *  net.minecraft.class_3486
 *  net.minecraft.class_3532
 */
package sweetie.leonware.client.features.modules.movement;

import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1922;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2680;
import net.minecraft.class_2708;
import net.minecraft.class_2743;
import net.minecraft.class_3486;
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
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.utils.player.MoveUtil;
import sweetie.leonware.client.features.modules.combat.AuraModule;

@ModuleRegister(name="Water Speed", category=Category.MOVEMENT)
public class WaterSpeedModule
extends Module {
    private static final WaterSpeedModule instance = new WaterSpeedModule();
    private final ModeSetting mode = new ModeSetting("\u0420\u0435\u0436\u0438\u043c").values("Intave", "Vanilla", "Wall", "HvH", "SprintE").value("Intave");
    private final ModeSetting wallMode = new ModeSetting("\u0420\u0435\u0436\u0438\u043c \u043e\u0442 \u0441\u0442\u0435\u043d\u044b").values("MetaHvH", "FunTime").value("MetaHvH").setVisible(() -> this.mode.is("Wall"));
    private final SliderSetting ftSpeed = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c FunTime").value(Float.valueOf(0.2f)).range(0.1f, 0.5f).step(0.01f).setVisible(() -> this.mode.is("Wall") && this.wallMode.is("FunTime"));
    private final SliderSetting vanillaSpeed = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c").value(Float.valueOf(0.4f)).range(0.1f, 1.0f).step(0.01f).setVisible(() -> this.mode.is("Vanilla"));
    private final SliderSetting ftBoost = new SliderSetting("\u0411\u0443\u0441\u0442 \u043a \u0441\u043a\u043e\u0440\u043e\u0441\u0442\u0438").value(Float.valueOf(1.0357f)).range(1.0f, 1.1f).step(5.0E-4f).setVisible(() -> this.mode.is("Intave"));
    private final BooleanSetting offInUse = new BooleanSetting("\u0421\u0442\u043e\u043f \u0435\u0441\u043b\u0438 \u043a\u0443\u0448\u0430\u0435\u043c").value(true).setVisible(() -> this.mode.is("Intave"));
    private final BooleanSetting hvhBoost = new BooleanSetting("\u0411\u0443\u0441\u0442").value(true).setVisible(() -> this.mode.is("HvH"));
    private final BooleanSetting hvhUpDown = new BooleanSetting("\u0412\u0432\u0435\u0440\u0445-\u0432\u043d\u0438\u0437").value(false).setVisible(() -> this.mode.is("HvH"));
    private final BooleanSetting hvhAntiFlag = new BooleanSetting("\u0410\u043d\u0442\u0438\u0424\u043b\u0430\u0433").value(true).setVisible(() -> this.mode.is("HvH"));
    private final SliderSetting hvhGeneralSpeed = new SliderSetting("\u041e\u0431\u0449\u0430\u044f \u0441\u043a\u043e\u0440\u043e\u0441\u0442\u044c").value(Float.valueOf(1.0f)).range(0.0f, 2.0f).step(0.05f).setVisible(() -> this.mode.is("HvH"));
    private final SliderSetting hvhBoostCooldown = new SliderSetting("\u041a\u0443\u043b\u0434\u0430\u0443\u043d \u0431\u0443\u0441\u0442\u0430").value(Float.valueOf(650.0f)).range(100.0f, 15000.0f).step(10.0f).setVisible(() -> this.mode.is("HvH") && (Boolean)this.hvhBoost.getValue() != false);
    private final SliderSetting hvhBoostDuration = new SliderSetting("\u0414\u043b\u0438\u0442\u0435\u043b\u044c\u043d\u043e\u0441\u0442\u044c \u0431\u0443\u0441\u0442\u0430").value(Float.valueOf(350.0f)).range(100.0f, 15000.0f).step(10.0f).setVisible(() -> this.mode.is("HvH") && (Boolean)this.hvhBoost.getValue() != false);
    private final SliderSetting hvhBoostSpeed = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c \u0431\u0443\u0441\u0442\u0430").value(Float.valueOf(0.17f)).range(0.05f, 1.0f).step(0.01f).setVisible(() -> this.mode.is("HvH") && (Boolean)this.hvhBoost.getValue() != false);
    private final SliderSetting hvhUpDownSpeed = new SliderSetting("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c \u0432\u0432\u0435\u0440\u0445/\u0432\u043d\u0438\u0437").value(Float.valueOf(0.2f)).range(0.05f, 1.0f).step(0.01f).setVisible(() -> this.mode.is("HvH") && (Boolean)this.hvhUpDown.getValue() != false);
    private final SliderSetting hvhAntiFlagTime = new SliderSetting("\u0412\u0440\u0435\u043c\u044f \u0410\u043d\u0442\u0438\u0424\u043b\u0430\u0433\u0430").value(Float.valueOf(7.0f)).range(0.5f, 10.0f).step(0.1f).setVisible(() -> this.mode.is("HvH") && (Boolean)this.hvhAntiFlag.getValue() != false);
    private final BooleanSetting hvhTarget = new BooleanSetting("\u0422\u0430\u0440\u0433\u0435\u0442").value(false).setVisible(() -> this.mode.is("HvH"));
    private final SliderSetting hvhTargetDistance = new SliderSetting("\u0414\u0438\u0441\u0442\u0430\u043d\u0446\u0438\u044f \u0434\u043e \u0446\u0435\u043b\u0438").value(Float.valueOf(1.0f)).range(0.1f, 5.0f).step(0.1f).setVisible(() -> this.mode.is("HvH") && (Boolean)this.hvhTarget.getValue() != false);
    private final BooleanSetting hvhVerticalTarget = new BooleanSetting("\u0412\u0435\u0440\u0442\u0438\u043a\u0430\u043b\u044c\u043d\u044b\u0439 \u0442\u0430\u0440\u0433\u0435\u0442").value(true).setVisible(() -> this.mode.is("HvH") && (Boolean)this.hvhTarget.getValue() != false);
    private final TimerUtil timer = new TimerUtil();
    private double wallVerticalBoost = 0.05;
    private double wallRadius;
    private double wallBoost;
    private float currentSpeed = 0.0f;
    private boolean boostActive = false;
    private long lastBoostTime = 0L;
    private long boostEndTime = 0L;
    private boolean antiFlagTriggered = false;
    private long flagTriggerTime = 0L;
    private float boostMultiplier = 0.0f;

    public WaterSpeedModule() {
        this.addSettings(this.mode, this.wallMode, this.ftSpeed, this.vanillaSpeed, this.ftBoost, this.offInUse, this.hvhBoost, this.hvhUpDown, this.hvhAntiFlag, this.hvhGeneralSpeed, this.hvhBoostCooldown, this.hvhBoostDuration, this.hvhBoostSpeed, this.hvhUpDownSpeed, this.hvhAntiFlagTime, this.hvhTarget, this.hvhTargetDistance, this.hvhVerticalTarget);
    }

    @Override
    public void onEnable() {
        this.timer.reset();
        this.resetHvH();
    }

    @Override
    public void onDisable() {
        this.resetHvH();
    }

    private void resetHvH() {
        this.boostActive = false;
        this.boostMultiplier = 0.0f;
        this.lastBoostTime = 0L;
        this.boostEndTime = 0L;
        this.antiFlagTriggered = false;
        this.flagTriggerTime = 0L;
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (WaterSpeedModule.mc.field_1724 == null || WaterSpeedModule.mc.field_1687 == null) {
                return;
            }
            switch ((String)this.mode.getValue()) {
                case "Intave": {
                    this.handleIntave();
                    break;
                }
                case "Vanilla": {
                    this.handleVanilla();
                    break;
                }
                case "Wall": {
                    this.handleWall();
                    break;
                }
                case "HvH": {
                    this.handleHvH();
                    break;
                }
                case "SprintE": {
                    this.handleSprintE();
                }
            }
        }));
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(data -> {
            if (!this.mode.is("HvH")) {
                return;
            }
            if (!data.isReceive()) {
                return;
            }
            if (!((Boolean)this.hvhAntiFlag.getValue()).booleanValue()) {
                return;
            }
            if (data.packet() instanceof class_2708) {
                this.triggerAntiFlag();
            } else {
                class_2743 packet;
                class_2596<?> patt0$temp = data.packet();
                if (patt0$temp instanceof class_2743 && (packet = (class_2743)patt0$temp).method_11818() == WaterSpeedModule.mc.field_1724.method_5628() && packet.method_11816() > 0.0) {
                    this.triggerAntiFlag();
                }
            }
        }));
        this.addEvents(updateEvent, packetEvent);
    }

    private void handleSprintE() {
        boolean backward;
        if (!WaterSpeedModule.mc.field_1724.method_5799()) {
            return;
        }
        boolean forward = WaterSpeedModule.mc.field_1724.field_3913.field_3905 > 0.0f;
        boolean bl = backward = WaterSpeedModule.mc.field_1724.field_3913.field_3905 < 0.0f;
        if (forward && !backward) {
            WaterSpeedModule.mc.field_1724.method_5728(true);
        }
    }

    private void handleIntave() {
        if (!WaterSpeedModule.mc.field_1724.method_5799()) {
            return;
        }
        boolean isMoving = MoveUtil.isMoving();
        if (isMoving) {
            this.timer.reset();
        }
        if (WaterSpeedModule.mc.field_1690.field_1894.method_1434()) {
            float speedMultiplier = this.getIntaveMultiplier();
            WaterSpeedModule.mc.field_1724.method_18800(WaterSpeedModule.mc.field_1724.method_18798().field_1352 * (double)speedMultiplier, WaterSpeedModule.mc.field_1724.method_18798().field_1351, WaterSpeedModule.mc.field_1724.method_18798().field_1350 * (double)speedMultiplier);
        }
        if (!WaterSpeedModule.mc.field_1724.field_5976 && !isMoving && this.timer.finished(300L)) {
            double yAnim = WaterSpeedModule.mc.field_1724.field_6012 % 3 == 0 ? -0.03 : 0.019;
            WaterSpeedModule.mc.field_1724.method_18800(WaterSpeedModule.mc.field_1724.method_18798().field_1352, WaterSpeedModule.mc.field_1724.method_18798().field_1351 + yAnim, WaterSpeedModule.mc.field_1724.method_18798().field_1350);
        }
    }

    private float getIntaveMultiplier() {
        String name;
        if (((Boolean)this.offInUse.getValue()).booleanValue() && WaterSpeedModule.mc.field_1724.method_6115()) {
            return 1.0f;
        }
        boolean hasDepthStrider = false;
        class_1799 boots = WaterSpeedModule.mc.field_1724.method_31548().method_7372(0);
        if (!boots.method_7960() && ((name = boots.method_7964().getString().toLowerCase()).contains("depth") || name.contains("aqua") || name.contains("water"))) {
            hasDepthStrider = true;
        }
        if (hasDepthStrider) {
            boolean hasHead = WaterSpeedModule.mc.field_1724.method_6079().method_7909() == class_1802.field_8575;
            return hasHead ? 1.04f : 1.043f;
        }
        return ((Float)this.ftBoost.getValue()).floatValue();
    }

    private void handleVanilla() {
        if (WaterSpeedModule.mc.field_1724.method_5799() && !WaterSpeedModule.mc.field_1724.method_5681() && MoveUtil.isMoving()) {
            MoveUtil.setSpeed(((Float)this.vanillaSpeed.getValue()).floatValue());
        }
    }

    private void handleWall() {
        if (this.wallMode.is("FunTime")) {
            this.wallRadius = 0.35;
            this.wallBoost = 0.05;
        } else if (this.wallMode.is("MetaHvH")) {
            this.wallRadius = 0.35;
            this.wallBoost = 0.4;
        }
        if (!WaterSpeedModule.mc.field_1724.method_5799()) {
            return;
        }
        if (!WaterSpeedModule.mc.field_1724.field_5976) {
            return;
        }
        if (!this.isWaterNearFeet()) {
            return;
        }
        class_2350 collisionFace = this.getCollisionFace();
        if (collisionFace == null) {
            return;
        }
        class_243 pushDir = new class_243((double)(-collisionFace.method_10148()), 0.0, (double)(-collisionFace.method_10165()));
        if (pushDir.method_1027() < 1.0E-6) {
            return;
        }
        double[] moveDir = this.calculateDirection(WaterSpeedModule.mc.field_1724.field_3913.field_3905, WaterSpeedModule.mc.field_1724.field_3913.field_3907, this.wallBoost);
        class_243 combined = new class_243(moveDir[0], 0.0, moveDir[1]).method_1019(pushDir.method_1029().method_1021(this.wallBoost * 0.6));
        class_243 velocity = WaterSpeedModule.mc.field_1724.method_18798();
        class_243 result = velocity.method_1019(combined);
        double vertical = Math.max(velocity.field_1351, this.wallVerticalBoost);
        WaterSpeedModule.mc.field_1724.method_18800(result.field_1352, vertical, result.field_1350);
        WaterSpeedModule.mc.field_1724.field_6017 = 0.0f;
    }

    private void handleHvH() {
        if (!WaterSpeedModule.mc.field_1724.method_5681()) {
            this.boostActive = false;
            this.updateBoost();
            return;
        }
        this.updateAntiFlag();
        if (((Boolean)this.hvhUpDown.getValue()).booleanValue()) {
            this.handleVerticalMovement();
        }
        if (!WaterSpeedModule.mc.field_1724.method_5624()) {
            this.boostActive = false;
            this.updateBoost();
            return;
        }
        this.currentSpeed = this.calculateHvHSpeed();
        long currentTime = System.currentTimeMillis();
        if (((Boolean)this.hvhBoost.getValue()).booleanValue() && !this.antiFlagTriggered) {
            if (this.boostEndTime > 0L && currentTime >= this.boostEndTime) {
                this.boostActive = false;
            }
            if (!this.boostActive && currentTime - this.lastBoostTime >= ((Float)this.hvhBoostCooldown.getValue()).longValue()) {
                this.lastBoostTime = currentTime;
                this.boostEndTime = currentTime + ((Float)this.hvhBoostDuration.getValue()).longValue();
                this.boostActive = true;
            }
        } else {
            this.boostActive = false;
        }
        this.updateBoost();
        float speed = this.currentSpeed * ((Float)this.hvhGeneralSpeed.getValue()).floatValue();
        if (((Boolean)this.hvhBoost.getValue()).booleanValue() && this.boostMultiplier > 0.01f) {
            speed += ((Float)this.hvhBoostSpeed.getValue()).floatValue() * this.boostMultiplier;
        }
        class_1309 target = this.getTarget();
        boolean shouldTarget = false;
        if (((Boolean)this.hvhTarget.getValue()).booleanValue()) {
            if (WaterSpeedModule.mc.field_1690.field_1894.method_1434() || WaterSpeedModule.mc.field_1690.field_1881.method_1434() || WaterSpeedModule.mc.field_1690.field_1913.method_1434() || WaterSpeedModule.mc.field_1690.field_1849.method_1434()) {
                shouldTarget = true;
            }
            if (target != null) {
                shouldTarget = true;
            }
        }
        if (shouldTarget && target != null) {
            double diffZ;
            float distance = ((Float)this.hvhTargetDistance.getValue()).floatValue();
            double yawRad = Math.toRadians(target.method_36454());
            double targetX = target.method_23317() - Math.sin(yawRad) * (double)distance;
            double targetZ = target.method_23321() + Math.cos(yawRad) * (double)distance;
            double diffX = targetX - WaterSpeedModule.mc.field_1724.method_23317();
            double dist = Math.sqrt(diffX * diffX + (diffZ = targetZ - WaterSpeedModule.mc.field_1724.method_23321()) * diffZ);
            if (dist > 0.01) {
                double playerY;
                double targetY;
                double diffY;
                double angle = Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0;
                double adjustedYaw = this.adjustYaw(angle, 0.0);
                double sin = -Math.sin(Math.toRadians(adjustedYaw));
                double cos = Math.cos(Math.toRadians(adjustedYaw));
                MoveUtil.setSpeed(speed);
                WaterSpeedModule.mc.field_1724.method_18800(sin * (double)speed, WaterSpeedModule.mc.field_1724.method_18798().field_1351, cos * (double)speed);
                if (((Boolean)this.hvhVerticalTarget.getValue()).booleanValue() && Math.abs(diffY = (targetY = target.method_23318() + (double)target.method_17682() / 2.0) - (playerY = WaterSpeedModule.mc.field_1724.method_23318() + (double)WaterSpeedModule.mc.field_1724.method_18381(WaterSpeedModule.mc.field_1724.method_18376()))) > 0.1) {
                    class_243 motion = WaterSpeedModule.mc.field_1724.method_18798();
                    WaterSpeedModule.mc.field_1724.method_18800(motion.field_1352, Math.signum(diffY) * (double)((Float)this.hvhUpDownSpeed.getValue()).floatValue(), motion.field_1350);
                }
                return;
            }
        }
        MoveUtil.setSpeed(speed);
    }

    private float calculateHvHSpeed() {
        String helmetName;
        class_1799 offhand = WaterSpeedModule.mc.field_1724.method_6079();
        String displayName = offhand.method_7964().getString();
        class_1293 speedEffect = WaterSpeedModule.mc.field_1724.method_6112(class_1294.field_5904);
        boolean hasLomtikDyni = displayName.contains("\u041b\u043e\u043c\u0442\u0438\u043a \u0414\u044b\u043d\u0438");
        if (hasLomtikDyni && speedEffect != null && speedEffect.method_5578() == 2) {
            return 0.68069994f;
        }
        float baseSpeed = speedEffect == null ? 0.4012f : (speedEffect.method_5578() == 2 ? 0.6372f : (speedEffect.method_5578() == 1 ? 0.59f : 0.4012f));
        class_1799 helmet = WaterSpeedModule.mc.field_1724.method_31548().method_7372(3);
        if (!helmet.method_7960() && helmet.method_7909() == class_1802.field_8575 && ((helmetName = helmet.method_7964().getString()).toLowerCase().contains("\u0271\u029f\u1d07\u1d21 \u1d04\u1d00\u0274\u1d1b\u028f") || helmetName.toLowerCase().contains("\u026f\u043b\u1d07\u028d \u1d04\u1d00\u043d\u0442\u044b") || helmetName.toLowerCase().contains("\u0448\u043b\u0435\u043c \u0441\u0430\u043d\u0442\u044b"))) {
            baseSpeed *= 0.85f;
        }
        return baseSpeed;
    }

    private void updateBoost() {
        float target = this.boostActive ? 1.0f : 0.0f;
        float step = ((Float)this.hvhBoostSpeed.getValue()).floatValue() * 0.4f;
        this.boostMultiplier += (target - this.boostMultiplier) * step;
        if (Math.abs(target - this.boostMultiplier) < 0.005f) {
            this.boostMultiplier = target;
        }
    }

    private void handleVerticalMovement() {
        if (WaterSpeedModule.mc.field_1724.method_36455() < -25.0f || WaterSpeedModule.mc.field_1724.method_36455() > 25.0f) {
            return;
        }
        float motion = 0.0f;
        if (WaterSpeedModule.mc.field_1690.field_1903.method_1434()) {
            motion = ((Float)this.hvhUpDownSpeed.getValue()).floatValue();
        } else if (WaterSpeedModule.mc.field_1690.field_1832.method_1434()) {
            motion = -((Float)this.hvhUpDownSpeed.getValue()).floatValue();
        }
        if (motion != 0.0f) {
            class_243 vel = WaterSpeedModule.mc.field_1724.method_18798();
            WaterSpeedModule.mc.field_1724.method_18800(vel.field_1352, (double)motion, vel.field_1350);
        }
    }

    private void triggerAntiFlag() {
        this.antiFlagTriggered = true;
        this.flagTriggerTime = System.currentTimeMillis();
        this.boostActive = false;
    }

    private void updateAntiFlag() {
        if (this.antiFlagTriggered) {
            long time = (long)(((Float)this.hvhAntiFlagTime.getValue()).floatValue() * 1000.0f);
            if (System.currentTimeMillis() - this.flagTriggerTime > time) {
                this.antiFlagTriggered = false;
            }
        }
    }

    private class_1309 getTarget() {
        AuraModule aura = AuraModule.getInstance();
        if (aura != null && aura.isEnabled() && aura.target != null) {
            return aura.target;
        }
        return null;
    }

    private boolean checkCollision(double yaw, double distance) {
        double yawRad = Math.toRadians(yaw);
        double x = WaterSpeedModule.mc.field_1724.method_23317() + -Math.sin(yawRad) * distance;
        double z = WaterSpeedModule.mc.field_1724.method_23321() + Math.cos(yawRad) * distance;
        class_2338 pos = class_2338.method_49637((double)x, (double)WaterSpeedModule.mc.field_1724.method_23318(), (double)z);
        if (WaterSpeedModule.mc.field_1687.method_8320(pos).method_26215()) {
            return false;
        }
        return !WaterSpeedModule.mc.field_1687.method_8320(pos).method_26220((class_1922)WaterSpeedModule.mc.field_1687, pos).method_1110();
    }

    private double adjustYaw(double yaw, double distance) {
        double[] offsets;
        for (double offset : offsets = new double[]{15.0, -15.0, 30.0, -30.0, 45.0, -45.0}) {
            if (this.checkCollision(yaw + offset, distance)) continue;
            return yaw + offset;
        }
        return yaw;
    }

    private class_2350 getCollisionFace() {
        class_238 box = WaterSpeedModule.mc.field_1724.method_5829();
        for (class_2350 dir : class_2350.class_2353.field_11062) {
            class_238 shifted = box.method_989((double)dir.method_10148() * 0.05, 0.0, (double)dir.method_10165() * 0.05);
            boolean hasCollision = WaterSpeedModule.mc.field_1687.method_20812((class_1297)WaterSpeedModule.mc.field_1724, shifted).iterator().hasNext();
            if (!hasCollision) continue;
            return dir;
        }
        return null;
    }

    private boolean isWaterNearFeet() {
        class_238 box = WaterSpeedModule.mc.field_1724.method_5829();
        int minX = class_3532.method_15357((double)(box.field_1323 - this.wallRadius));
        int maxX = class_3532.method_15357((double)(box.field_1320 + this.wallRadius));
        int minY = class_3532.method_15357((double)(box.field_1322 - 0.2));
        int maxY = class_3532.method_15357((double)(box.field_1322 + 0.2));
        int minZ = class_3532.method_15357((double)(box.field_1321 - this.wallRadius));
        int maxZ = class_3532.method_15357((double)(box.field_1324 + this.wallRadius));
        class_2338.class_2339 mutablePos = new class_2338.class_2339();
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    mutablePos.method_10103(x, y, z);
                    class_2680 state = WaterSpeedModule.mc.field_1687.method_8320((class_2338)mutablePos);
                    if (!state.method_26227().method_15767(class_3486.field_15517) && !state.method_26227().method_15767(class_3486.field_15518)) continue;
                    return true;
                }
            }
        }
        return false;
    }

    private double[] calculateDirection(float forward, float sideways, double distance) {
        float yaw = WaterSpeedModule.mc.field_1724.method_36454();
        float sinYaw = class_3532.method_15374((float)((float)Math.toRadians(yaw + 90.0f)));
        float cosYaw = class_3532.method_15362((float)((float)Math.toRadians(yaw + 90.0f)));
        double xMovement = (double)forward * distance * (double)cosYaw + (double)sideways * distance * (double)sinYaw;
        double zMovement = (double)forward * distance * (double)sinYaw - (double)sideways * distance * (double)cosYaw;
        return new double[]{xMovement, zMovement};
    }

    public static WaterSpeedModule getInstance() {
        return instance;
    }

    public ModeSetting getMode() {
        return this.mode;
    }
}

