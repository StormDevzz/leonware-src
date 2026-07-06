package sweetie.leonware.client.features.modules.movement;

import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1309;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/WaterSpeedModule.class */
@ModuleRegister(name = "Water Speed", category = Category.MOVEMENT)
public class WaterSpeedModule extends Module {
    private static final WaterSpeedModule instance = new WaterSpeedModule();
    private double wallRadius;
    private double wallBoost;
    private final ModeSetting mode = new ModeSetting("Режим").values("Intave", "Vanilla", "Wall", "HvH", "SprintE").value("Intave");
    private final ModeSetting wallMode = new ModeSetting("Режим от стены").values("MetaHvH", "FunTime").value("MetaHvH").setVisible(() -> {
        return Boolean.valueOf(this.mode.is("Wall"));
    });
    private final SliderSetting ftSpeed = new SliderSetting("Скорость FunTime").value(Float.valueOf(0.2f)).range(0.1f, 0.5f).step(0.01f).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("Wall") && this.wallMode.is("FunTime"));
    });
    private final SliderSetting vanillaSpeed = new SliderSetting("Скорость").value(Float.valueOf(0.4f)).range(0.1f, 1.0f).step(0.01f).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("Vanilla"));
    });
    private final SliderSetting ftBoost = new SliderSetting("Буст к скорости").value(Float.valueOf(1.0357f)).range(1.0f, 1.1f).step(5.0E-4f).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("Intave"));
    });
    private final BooleanSetting offInUse = new BooleanSetting("Стоп если кушаем").value((Boolean) true).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("Intave"));
    });
    private final BooleanSetting hvhBoost = new BooleanSetting("Буст").value((Boolean) true).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("HvH"));
    });
    private final BooleanSetting hvhUpDown = new BooleanSetting("Вверх-вниз").value((Boolean) false).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("HvH"));
    });
    private final BooleanSetting hvhAntiFlag = new BooleanSetting("АнтиФлаг").value((Boolean) true).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("HvH"));
    });
    private final SliderSetting hvhGeneralSpeed = new SliderSetting("Общая скорость").value(Float.valueOf(1.0f)).range(0.0f, 2.0f).step(0.05f).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("HvH"));
    });
    private final SliderSetting hvhBoostCooldown = new SliderSetting("Кулдаун буста").value(Float.valueOf(650.0f)).range(100.0f, 15000.0f).step(10.0f).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("HvH") && this.hvhBoost.getValue().booleanValue());
    });
    private final SliderSetting hvhBoostDuration = new SliderSetting("Длительность буста").value(Float.valueOf(350.0f)).range(100.0f, 15000.0f).step(10.0f).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("HvH") && this.hvhBoost.getValue().booleanValue());
    });
    private final SliderSetting hvhBoostSpeed = new SliderSetting("Скорость буста").value(Float.valueOf(0.17f)).range(0.05f, 1.0f).step(0.01f).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("HvH") && this.hvhBoost.getValue().booleanValue());
    });
    private final SliderSetting hvhUpDownSpeed = new SliderSetting("Скорость вверх/вниз").value(Float.valueOf(0.2f)).range(0.05f, 1.0f).step(0.01f).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("HvH") && this.hvhUpDown.getValue().booleanValue());
    });
    private final SliderSetting hvhAntiFlagTime = new SliderSetting("Время АнтиФлага").value(Float.valueOf(7.0f)).range(0.5f, 10.0f).step(0.1f).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("HvH") && this.hvhAntiFlag.getValue().booleanValue());
    });
    private final BooleanSetting hvhTarget = new BooleanSetting("Таргет").value((Boolean) false).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("HvH"));
    });
    private final SliderSetting hvhTargetDistance = new SliderSetting("Дистанция до цели").value(Float.valueOf(1.0f)).range(0.1f, 5.0f).step(0.1f).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("HvH") && this.hvhTarget.getValue().booleanValue());
    });
    private final BooleanSetting hvhVerticalTarget = new BooleanSetting("Вертикальный таргет").value((Boolean) true).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("HvH") && this.hvhTarget.getValue().booleanValue());
    });
    private final TimerUtil timer = new TimerUtil();
    private double wallVerticalBoost = 0.05d;
    private float currentSpeed = 0.0f;
    private boolean boostActive = false;
    private long lastBoostTime = 0;
    private long boostEndTime = 0;
    private boolean antiFlagTriggered = false;
    private long flagTriggerTime = 0;
    private float boostMultiplier = 0.0f;

    /* JADX WARN: Type inference failed for: r1v11, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v16, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v21, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v24, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v27, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v30, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v33, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v38, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v43, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v48, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v53, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v58, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v6, types: [sweetie.leonware.api.module.setting.ModeSetting] */
    /* JADX WARN: Type inference failed for: r1v63, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v66, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v71, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v74, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    public WaterSpeedModule() {
        addSettings(this.mode, this.wallMode, this.ftSpeed, this.vanillaSpeed, this.ftBoost, this.offInUse, this.hvhBoost, this.hvhUpDown, this.hvhAntiFlag, this.hvhGeneralSpeed, this.hvhBoostCooldown, this.hvhBoostDuration, this.hvhBoostSpeed, this.hvhUpDownSpeed, this.hvhAntiFlagTime, this.hvhTarget, this.hvhTargetDistance, this.hvhVerticalTarget);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onEnable() {
        this.timer.reset();
        resetHvH();
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        resetHvH();
    }

    private void resetHvH() {
        this.boostActive = false;
        this.boostMultiplier = 0.0f;
        this.lastBoostTime = 0L;
        this.boostEndTime = 0L;
        this.antiFlagTriggered = false;
        this.flagTriggerTime = 0L;
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1724 == null || mc.field_1687 == null) {
                return;
            }
            switch (this.mode.getValue()) {
                case "Intave":
                    handleIntave();
                    break;
                case "Vanilla":
                    handleVanilla();
                    break;
                case "Wall":
                    handleWall();
                    break;
                case "HvH":
                    handleHvH();
                    break;
                case "SprintE":
                    handleSprintE();
                    break;
            }
        }));
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener(data -> {
            if (this.mode.is("HvH") && data.isReceive() && this.hvhAntiFlag.getValue().booleanValue()) {
                if (data.packet() instanceof class_2708) {
                    triggerAntiFlag();
                    return;
                }
                class_2743 class_2743VarPacket = data.packet();
                if (class_2743VarPacket instanceof class_2743) {
                    class_2743 packet = class_2743VarPacket;
                    if (packet.method_11818() == mc.field_1724.method_5628() && packet.method_11816() > 0.0d) {
                        triggerAntiFlag();
                    }
                }
            }
        }));
        addEvents(updateEvent, packetEvent);
    }

    private void handleSprintE() {
        if (mc.field_1724.method_5799()) {
            boolean forward = mc.field_1724.field_3913.field_3905 > 0.0f;
            boolean backward = mc.field_1724.field_3913.field_3905 < 0.0f;
            if (forward && !backward) {
                mc.field_1724.method_5728(true);
            }
        }
    }

    private void handleIntave() {
        if (mc.field_1724.method_5799()) {
            boolean isMoving = MoveUtil.isMoving();
            if (isMoving) {
                this.timer.reset();
            }
            if (mc.field_1690.field_1894.method_1434()) {
                float speedMultiplier = getIntaveMultiplier();
                mc.field_1724.method_18800(mc.field_1724.method_18798().field_1352 * ((double) speedMultiplier), mc.field_1724.method_18798().field_1351, mc.field_1724.method_18798().field_1350 * ((double) speedMultiplier));
            }
            if (!mc.field_1724.field_5976 && !isMoving && this.timer.finished(300L)) {
                double yAnim = mc.field_1724.field_6012 % 3 == 0 ? -0.03d : 0.019d;
                mc.field_1724.method_18800(mc.field_1724.method_18798().field_1352, mc.field_1724.method_18798().field_1351 + yAnim, mc.field_1724.method_18798().field_1350);
            }
        }
    }

    private float getIntaveMultiplier() {
        if (this.offInUse.getValue().booleanValue() && mc.field_1724.method_6115()) {
            return 1.0f;
        }
        boolean hasDepthStrider = false;
        class_1799 boots = mc.field_1724.method_31548().method_7372(0);
        if (!boots.method_7960()) {
            String name = boots.method_7964().getString().toLowerCase();
            if (name.contains("depth") || name.contains("aqua") || name.contains("water")) {
                hasDepthStrider = true;
            }
        }
        if (hasDepthStrider) {
            boolean hasHead = mc.field_1724.method_6079().method_7909() == class_1802.field_8575;
            return hasHead ? 1.04f : 1.043f;
        }
        return this.ftBoost.getValue().floatValue();
    }

    private void handleVanilla() {
        if (mc.field_1724.method_5799() && !mc.field_1724.method_5681() && MoveUtil.isMoving()) {
            MoveUtil.setSpeed(this.vanillaSpeed.getValue().floatValue());
        }
    }

    private void handleWall() {
        class_2350 collisionFace;
        if (this.wallMode.is("FunTime")) {
            this.wallRadius = 0.35d;
            this.wallBoost = 0.05d;
        } else if (this.wallMode.is("MetaHvH")) {
            this.wallRadius = 0.35d;
            this.wallBoost = 0.4d;
        }
        if (mc.field_1724.method_5799() && mc.field_1724.field_5976 && isWaterNearFeet() && (collisionFace = getCollisionFace()) != null) {
            class_243 pushDir = new class_243(-collisionFace.method_10148(), 0.0d, -collisionFace.method_10165());
            if (pushDir.method_1027() < 1.0E-6d) {
                return;
            }
            double[] moveDir = calculateDirection(mc.field_1724.field_3913.field_3905, mc.field_1724.field_3913.field_3907, this.wallBoost);
            class_243 combined = new class_243(moveDir[0], 0.0d, moveDir[1]).method_1019(pushDir.method_1029().method_1021(this.wallBoost * 0.6d));
            class_243 velocity = mc.field_1724.method_18798();
            class_243 result = velocity.method_1019(combined);
            double vertical = Math.max(velocity.field_1351, this.wallVerticalBoost);
            mc.field_1724.method_18800(result.field_1352, vertical, result.field_1350);
            mc.field_1724.field_6017 = 0.0f;
        }
    }

    private void handleHvH() {
        if (!mc.field_1724.method_5681()) {
            this.boostActive = false;
            updateBoost();
            return;
        }
        updateAntiFlag();
        if (this.hvhUpDown.getValue().booleanValue()) {
            handleVerticalMovement();
        }
        if (!mc.field_1724.method_5624()) {
            this.boostActive = false;
            updateBoost();
            return;
        }
        this.currentSpeed = calculateHvHSpeed();
        long currentTime = System.currentTimeMillis();
        if (this.hvhBoost.getValue().booleanValue() && !this.antiFlagTriggered) {
            if (this.boostEndTime > 0 && currentTime >= this.boostEndTime) {
                this.boostActive = false;
            }
            if (!this.boostActive && currentTime - this.lastBoostTime >= this.hvhBoostCooldown.getValue().longValue()) {
                this.lastBoostTime = currentTime;
                this.boostEndTime = currentTime + this.hvhBoostDuration.getValue().longValue();
                this.boostActive = true;
            }
        } else {
            this.boostActive = false;
        }
        updateBoost();
        float speed = this.currentSpeed * this.hvhGeneralSpeed.getValue().floatValue();
        if (this.hvhBoost.getValue().booleanValue() && this.boostMultiplier > 0.01f) {
            speed += this.hvhBoostSpeed.getValue().floatValue() * this.boostMultiplier;
        }
        class_1309 target = getTarget();
        boolean shouldTarget = false;
        if (this.hvhTarget.getValue().booleanValue()) {
            if (mc.field_1690.field_1894.method_1434() || mc.field_1690.field_1881.method_1434() || mc.field_1690.field_1913.method_1434() || mc.field_1690.field_1849.method_1434()) {
                shouldTarget = true;
            }
            if (target != null) {
                shouldTarget = true;
            }
        }
        if (shouldTarget && target != null) {
            float distance = this.hvhTargetDistance.getValue().floatValue();
            double yawRad = Math.toRadians(target.method_36454());
            double targetX = target.method_23317() - (Math.sin(yawRad) * ((double) distance));
            double targetZ = target.method_23321() + (Math.cos(yawRad) * ((double) distance));
            double diffX = targetX - mc.field_1724.method_23317();
            double diffZ = targetZ - mc.field_1724.method_23321();
            double dist = Math.sqrt((diffX * diffX) + (diffZ * diffZ));
            if (dist > 0.01d) {
                double angle = Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0d;
                double adjustedYaw = adjustYaw(angle, 0.0d);
                double sin = -Math.sin(Math.toRadians(adjustedYaw));
                double cos = Math.cos(Math.toRadians(adjustedYaw));
                MoveUtil.setSpeed(speed);
                mc.field_1724.method_18800(sin * ((double) speed), mc.field_1724.method_18798().field_1351, cos * ((double) speed));
                if (this.hvhVerticalTarget.getValue().booleanValue()) {
                    double targetY = target.method_23318() + (((double) target.method_17682()) / 2.0d);
                    double playerY = mc.field_1724.method_23318() + ((double) mc.field_1724.method_18381(mc.field_1724.method_18376()));
                    double diffY = targetY - playerY;
                    if (Math.abs(diffY) > 0.1d) {
                        class_243 motion = mc.field_1724.method_18798();
                        mc.field_1724.method_18800(motion.field_1352, Math.signum(diffY) * ((double) this.hvhUpDownSpeed.getValue().floatValue()), motion.field_1350);
                        return;
                    }
                    return;
                }
                return;
            }
        }
        MoveUtil.setSpeed(speed);
    }

    private float calculateHvHSpeed() {
        float baseSpeed;
        class_1799 offhand = mc.field_1724.method_6079();
        String displayName = offhand.method_7964().getString();
        class_1293 speedEffect = mc.field_1724.method_6112(class_1294.field_5904);
        boolean hasLomtikDyni = displayName.contains("Ломтик Дыни");
        if (hasLomtikDyni && speedEffect != null && speedEffect.method_5578() == 2) {
            return 0.68069994f;
        }
        if (speedEffect == null) {
            baseSpeed = 0.4012f;
        } else if (speedEffect.method_5578() == 2) {
            baseSpeed = 0.6372f;
        } else if (speedEffect.method_5578() == 1) {
            baseSpeed = 0.59f;
        } else {
            baseSpeed = 0.4012f;
        }
        class_1799 helmet = mc.field_1724.method_31548().method_7372(3);
        if (!helmet.method_7960() && helmet.method_7909() == class_1802.field_8575) {
            String helmetName = helmet.method_7964().getString();
            if (helmetName.toLowerCase().contains("ɱʟᴇᴡ ᴄᴀɴᴛʏ") || helmetName.toLowerCase().contains("ɯлᴇʍ ᴄᴀнты") || helmetName.toLowerCase().contains("шлем санты")) {
                baseSpeed *= 0.85f;
            }
        }
        return baseSpeed;
    }

    private void updateBoost() {
        float target = this.boostActive ? 1.0f : 0.0f;
        float step = this.hvhBoostSpeed.getValue().floatValue() * 0.4f;
        this.boostMultiplier += (target - this.boostMultiplier) * step;
        if (Math.abs(target - this.boostMultiplier) < 0.005f) {
            this.boostMultiplier = target;
        }
    }

    private void handleVerticalMovement() {
        if (mc.field_1724.method_36455() < -25.0f || mc.field_1724.method_36455() > 25.0f) {
            return;
        }
        float motion = 0.0f;
        if (mc.field_1690.field_1903.method_1434()) {
            motion = this.hvhUpDownSpeed.getValue().floatValue();
        } else if (mc.field_1690.field_1832.method_1434()) {
            motion = -this.hvhUpDownSpeed.getValue().floatValue();
        }
        if (motion != 0.0f) {
            class_243 vel = mc.field_1724.method_18798();
            mc.field_1724.method_18800(vel.field_1352, motion, vel.field_1350);
        }
    }

    private void triggerAntiFlag() {
        this.antiFlagTriggered = true;
        this.flagTriggerTime = System.currentTimeMillis();
        this.boostActive = false;
    }

    private void updateAntiFlag() {
        if (this.antiFlagTriggered) {
            long time = (long) (this.hvhAntiFlagTime.getValue().floatValue() * 1000.0f);
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
        double x = mc.field_1724.method_23317() + ((-Math.sin(yawRad)) * distance);
        double z = mc.field_1724.method_23321() + (Math.cos(yawRad) * distance);
        class_2338 pos = class_2338.method_49637(x, mc.field_1724.method_23318(), z);
        return (mc.field_1687.method_8320(pos).method_26215() || mc.field_1687.method_8320(pos).method_26220(mc.field_1687, pos).method_1110()) ? false : true;
    }

    private double adjustYaw(double yaw, double distance) {
        double[] offsets = {15.0d, -15.0d, 30.0d, -30.0d, 45.0d, -45.0d};
        for (double offset : offsets) {
            if (!checkCollision(yaw + offset, distance)) {
                return yaw + offset;
            }
        }
        return yaw;
    }

    private class_2350 getCollisionFace() {
        class_238 box = mc.field_1724.method_5829();
        for (class_2350 dir : class_2350.class_2353.field_11062) {
            class_238 shifted = box.method_989(((double) dir.method_10148()) * 0.05d, 0.0d, ((double) dir.method_10165()) * 0.05d);
            boolean hasCollision = mc.field_1687.method_20812(mc.field_1724, shifted).iterator().hasNext();
            if (hasCollision) {
                return dir;
            }
        }
        return null;
    }

    private boolean isWaterNearFeet() {
        class_238 box = mc.field_1724.method_5829();
        int minX = class_3532.method_15357(box.field_1323 - this.wallRadius);
        int maxX = class_3532.method_15357(box.field_1320 + this.wallRadius);
        int minY = class_3532.method_15357(box.field_1322 - 0.2d);
        int maxY = class_3532.method_15357(box.field_1322 + 0.2d);
        int minZ = class_3532.method_15357(box.field_1321 - this.wallRadius);
        int maxZ = class_3532.method_15357(box.field_1324 + this.wallRadius);
        class_2338.class_2339 mutablePos = new class_2338.class_2339();
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    mutablePos.method_10103(x, y, z);
                    class_2680 state = mc.field_1687.method_8320(mutablePos);
                    if (state.method_26227().method_15767(class_3486.field_15517) || state.method_26227().method_15767(class_3486.field_15518)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private double[] calculateDirection(float forward, float sideways, double distance) {
        float yaw = mc.field_1724.method_36454();
        float sinYaw = class_3532.method_15374((float) Math.toRadians(yaw + 90.0f));
        float cosYaw = class_3532.method_15362((float) Math.toRadians(yaw + 90.0f));
        double xMovement = (((double) forward) * distance * ((double) cosYaw)) + (((double) sideways) * distance * ((double) sinYaw));
        double zMovement = ((((double) forward) * distance) * ((double) sinYaw)) - ((((double) sideways) * distance) * ((double) cosYaw));
        return new double[]{xMovement, zMovement};
    }

    public static WaterSpeedModule getInstance() {
        return instance;
    }

    public ModeSetting getMode() {
        return this.mode;
    }
}
