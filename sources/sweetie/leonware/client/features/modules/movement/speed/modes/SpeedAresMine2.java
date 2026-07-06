package sweetie.leonware.client.features.modules.movement.speed.modes;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.class_1297;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import sweetie.leonware.api.interfaces.IBacktrackable;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.player.MoveUtil;
import sweetie.leonware.client.features.modules.combat.AuraModule;
import sweetie.leonware.client.features.modules.combat.BacktrackModule;
import sweetie.leonware.client.features.modules.movement.speed.SpeedMode;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/speed/modes/SpeedAresMine2.class */
public class SpeedAresMine2 extends SpeedMode {
    private final BooleanSetting pauseInLiquids = new BooleanSetting("Pause in liquid").value((Boolean) false);
    private final BooleanSetting pauseWhileSneaking = new BooleanSetting("Pause sneaking").value((Boolean) false);
    private final SliderSetting speedFactor = new SliderSetting("Speed factor").value(Float.valueOf(8.0f)).range(1.0f, 15.0f).step(0.1f);
    private final SliderSetting distance = new SliderSetting("Distance").value(Float.valueOf(3.0f)).range(0.5f, 5.0f).step(0.1f);
    private final BooleanSetting bypassDistance = new BooleanSetting("Bypass distance").value((Boolean) false);
    private final SliderSetting bypassDistanceValue = new SliderSetting("Bypass dist value").value(Float.valueOf(4.0f)).range(1.0f, 6.0f).step(0.1f);
    private final SliderSetting bypassAngle = new SliderSetting("Bypass angle").value(Float.valueOf(120.0f)).range(10.0f, 180.0f).step(5.0f);
    private final BooleanSetting waterDistance = new BooleanSetting("Water distance").value((Boolean) false);
    private final SliderSetting waterDistanceValue = new SliderSetting("Water dist value").value(Float.valueOf(2.0f)).range(0.5f, 6.0f).step(0.1f);
    private final BooleanSetting elytraDistance = new BooleanSetting("Elytra distance").value((Boolean) false);
    private final SliderSetting elytraDistanceValue = new SliderSetting("Elytra dist value").value(Float.valueOf(5.0f)).range(0.5f, 10.0f).step(0.1f);
    private final BooleanSetting predictMovement = new BooleanSetting("Predict movement").value((Boolean) true);
    private final SliderSetting predictionFactor = new SliderSetting("Prediction factor").value(Float.valueOf(2.0f)).range(1.0f, 5.0f).step(0.1f);
    private final BooleanSetting smoothMovement = new BooleanSetting("Smooth movement").value((Boolean) true);
    private final BooleanSetting backtrack = new BooleanSetting("Backtrack").value((Boolean) true);
    private final SliderSetting backtrackTicks = new SliderSetting("Backtrack ticks").value(Float.valueOf(3.0f)).range(1.0f, 10.0f).step(0.5f);
    private final SliderSetting backtrackDistance = new SliderSetting("Backtrack max dist").value(Float.valueOf(6.0f)).range(3.0f, 15.0f).step(0.5f);
    private final SliderSetting movementThreshold = new SliderSetting("Movement threshold").value(Float.valueOf(0.1f)).range(0.01f, 0.5f).step(0.01f);
    private final SliderSetting staticBoostRange = new SliderSetting("Static boost range").value(Float.valueOf(0.45f)).range(0.1f, 1.0f).step(0.05f);
    private final SliderSetting staticBoostForce = new SliderSetting("Static boost force").value(Float.valueOf(0.085f)).range(0.01f, 0.2f).step(0.005f);
    private int speedBoostCooldown = 0;
    private final SliderSetting backtrackBoostTicks = new SliderSetting("BT Boost cooldown").value(Float.valueOf(3.0f)).range(1.0f, 20.0f).step(1.0f);
    private class_1297 targetEntity = null;
    private class_243 lastTargetPos = null;
    private class_243 predictedPos = null;
    private double[] lastMotion = {0.0d, 0.0d};
    private final List<class_243> positionHistory = new ArrayList();
    private final List<Long> timeHistory = new ArrayList();
    private class_243 backtrackPos = null;
    private class_243 prevTargetPos = null;
    private double targetMovementSpeed = 0.0d;
    private boolean isTargetStatic = false;

    @Override // sweetie.leonware.api.system.backend.Choice, sweetie.leonware.api.module.setting.ModeSetting.NamedChoice
    public String getName() {
        return "AresMine2";
    }

    public SpeedAresMine2(Supplier<Boolean> condition) {
        this.pauseInLiquids.setVisible(condition);
        this.pauseWhileSneaking.setVisible(condition);
        this.speedFactor.setVisible(condition);
        this.distance.setVisible(condition);
        this.bypassDistance.setVisible(condition);
        this.bypassDistanceValue.setVisible(() -> {
            return Boolean.valueOf(((Boolean) condition.get()).booleanValue() && this.bypassDistance.getValue().booleanValue());
        });
        this.bypassAngle.setVisible(() -> {
            return Boolean.valueOf(((Boolean) condition.get()).booleanValue() && this.bypassDistance.getValue().booleanValue());
        });
        this.waterDistance.setVisible(condition);
        this.waterDistanceValue.setVisible(() -> {
            return Boolean.valueOf(((Boolean) condition.get()).booleanValue() && this.waterDistance.getValue().booleanValue());
        });
        this.elytraDistance.setVisible(condition);
        this.elytraDistanceValue.setVisible(() -> {
            return Boolean.valueOf(((Boolean) condition.get()).booleanValue() && this.elytraDistance.getValue().booleanValue());
        });
        this.predictMovement.setVisible(condition);
        this.predictionFactor.setVisible(() -> {
            return Boolean.valueOf(((Boolean) condition.get()).booleanValue() && this.predictMovement.getValue().booleanValue());
        });
        this.smoothMovement.setVisible(condition);
        this.backtrack.setVisible(condition);
        this.backtrackTicks.setVisible(() -> {
            return Boolean.valueOf(((Boolean) condition.get()).booleanValue() && this.backtrack.getValue().booleanValue());
        });
        this.backtrackDistance.setVisible(() -> {
            return Boolean.valueOf(((Boolean) condition.get()).booleanValue() && this.backtrack.getValue().booleanValue());
        });
        this.movementThreshold.setVisible(condition);
        this.staticBoostRange.setVisible(condition);
        this.staticBoostForce.setVisible(condition);
        this.backtrackBoostTicks.setVisible(() -> {
            return Boolean.valueOf(((Boolean) condition.get()).booleanValue() && BacktrackModule.getInstance().isApplyToSpeed());
        });
        addSettings(this.pauseInLiquids, this.pauseWhileSneaking, this.speedFactor, this.distance, this.bypassDistance, this.bypassDistanceValue, this.bypassAngle, this.waterDistance, this.waterDistanceValue, this.elytraDistance, this.elytraDistanceValue, this.predictMovement, this.predictionFactor, this.smoothMovement, this.backtrack, this.backtrackTicks, this.backtrackDistance, this.movementThreshold, this.staticBoostRange, this.staticBoostForce, this.backtrackBoostTicks);
    }

    @Override // sweetie.leonware.client.features.modules.movement.speed.SpeedMode
    public void onEnable() {
        reset();
    }

    @Override // sweetie.leonware.client.features.modules.movement.speed.SpeedMode
    public void onDisable() {
        reset();
    }

    private void reset() {
        this.targetEntity = null;
        this.lastTargetPos = null;
        this.predictedPos = null;
        this.lastMotion = new double[]{0.0d, 0.0d};
        this.backtrackPos = null;
        this.prevTargetPos = null;
        this.targetMovementSpeed = 0.0d;
        this.isTargetStatic = false;
        this.positionHistory.clear();
        this.timeHistory.clear();
    }

    @Override // sweetie.leonware.client.features.modules.movement.speed.SpeedMode
    public void onUpdate() {
        if (mc.field_1724 == null || mc.field_1687 == null) {
            return;
        }
        if (mc.field_1724.method_52535() && this.pauseInLiquids.getValue().booleanValue()) {
            return;
        }
        if (mc.field_1724.method_5715() && this.pauseWhileSneaking.getValue().booleanValue()) {
            return;
        }
        updateGrimTarget();
    }

    @Override // sweetie.leonware.client.features.modules.movement.speed.SpeedMode
    public void onTravel() {
        if (mc.field_1724 == null || mc.field_1687 == null) {
            return;
        }
        if (mc.field_1724.method_52535() && this.pauseInLiquids.getValue().booleanValue()) {
            return;
        }
        if (!(mc.field_1724.method_5715() && this.pauseWhileSneaking.getValue().booleanValue()) && MoveUtil.isMoving()) {
            if (this.isTargetStatic && this.targetEntity != null) {
                handleStaticTargetBoost();
            } else {
                handleGrimDistanceMode();
            }
            applyBacktrackBoost2();
        }
    }

    private void applyBacktrackBoost2() {
        BacktrackModule.Position backPos;
        if (BacktrackModule.getInstance().isApplyToSpeed()) {
            if (this.speedBoostCooldown > 0) {
                this.speedBoostCooldown--;
                return;
            }
            for (IBacktrackable iBacktrackable : mc.field_1687.method_18456()) {
                if (iBacktrackable != mc.field_1724 && (iBacktrackable instanceof IBacktrackable)) {
                    IBacktrackable bt = iBacktrackable;
                    ArrayDeque<BacktrackModule.Position> tracks = bt.leonware$getBackTracks();
                    if (!tracks.isEmpty() && (backPos = tracks.peekFirst()) != null) {
                        class_243 btPos = backPos.pos();
                        double effectiveDist = getEffectiveDistance();
                        if (mc.field_1724.method_5707(btPos) <= effectiveDist * effectiveDist) {
                            double[] dir = getDirectionToPoint(mc.field_1724.method_19538(), btPos, ((double) this.speedFactor.getValue().floatValue()) * 0.01d);
                            mc.field_1724.method_5762(dir[0], 0.0d, dir[1]);
                            this.speedBoostCooldown = this.backtrackBoostTicks.getValue().intValue();
                            return;
                        }
                    }
                }
            }
        }
    }

    private void updateGrimTarget() {
        AuraModule aura = AuraModule.getInstance();
        if (aura.isEnabled() && aura.target != null) {
            this.targetEntity = aura.target;
            if (this.targetEntity != null) {
                class_243 currentPos = this.targetEntity.method_19538();
                long currentTime = System.currentTimeMillis();
                if (this.prevTargetPos != null) {
                    this.targetMovementSpeed = currentPos.method_1022(this.prevTargetPos);
                    this.isTargetStatic = this.targetMovementSpeed < ((double) this.movementThreshold.getValue().floatValue());
                }
                this.prevTargetPos = currentPos;
                if (this.backtrack.getValue().booleanValue() && !this.isTargetStatic) {
                    updateBacktrackHistory(currentPos, currentTime);
                }
                if (this.lastTargetPos == null) {
                    this.lastTargetPos = currentPos;
                    this.predictedPos = currentPos;
                    return;
                }
                class_243 velocity = new class_243(currentPos.field_1352 - this.lastTargetPos.field_1352, currentPos.field_1351 - this.lastTargetPos.field_1351, currentPos.field_1350 - this.lastTargetPos.field_1350);
                if (this.predictMovement.getValue().booleanValue() && !this.isTargetStatic) {
                    float factor = this.predictionFactor.getValue().floatValue();
                    this.predictedPos = currentPos.method_1031(velocity.field_1352 * ((double) factor), velocity.field_1351 * ((double) factor), velocity.field_1350 * ((double) factor));
                    if (!mc.field_1687.method_8393(((int) this.predictedPos.field_1352) >> 4, ((int) this.predictedPos.field_1350) >> 4)) {
                        this.predictedPos = currentPos;
                    }
                } else {
                    this.predictedPos = currentPos;
                }
                this.lastTargetPos = currentPos;
                return;
            }
            return;
        }
        this.targetEntity = null;
        this.lastTargetPos = null;
        this.predictedPos = null;
        this.backtrackPos = null;
        this.prevTargetPos = null;
        this.targetMovementSpeed = 0.0d;
        this.isTargetStatic = false;
        this.positionHistory.clear();
        this.timeHistory.clear();
    }

    private void handleStaticTargetBoost() {
        if (this.targetEntity == null) {
            return;
        }
        class_238 checkBox = mc.field_1724.method_5829().method_1014(this.staticBoostRange.getValue().floatValue());
        if (checkBox.method_994(this.targetEntity.method_5829())) {
            class_243 directionToTarget = this.targetEntity.method_19538().method_1020(mc.field_1724.method_19538()).method_1029();
            double randomAngle = (Math.random() - 0.5d) * 0.22d;
            double sin = Math.sin(randomAngle);
            double cos = Math.cos(randomAngle);
            double nx = (directionToTarget.field_1352 * cos) - (directionToTarget.field_1350 * sin);
            double nz = (directionToTarget.field_1352 * sin) + (directionToTarget.field_1350 * cos);
            class_243 finalDir = new class_243(nx, 0.0d, nz).method_1029();
            class_243 currentVel = mc.field_1724.method_18798();
            class_243 added = finalDir.method_1021(this.staticBoostForce.getValue().floatValue());
            class_243 newVel = currentVel.method_1031(added.field_1352 * 0.88d, 0.0d, added.field_1350 * 0.88d);
            mc.field_1724.method_18799(newVel);
        }
    }

    private void updateBacktrackHistory(class_243 currentPos, long currentTime) {
        this.positionHistory.add(currentPos);
        this.timeHistory.add(Long.valueOf(currentTime));
        long maxAge = (long) (this.backtrackTicks.getValue().floatValue() * 50.0f);
        while (!this.timeHistory.isEmpty() && currentTime - this.timeHistory.get(0).longValue() > maxAge) {
            this.positionHistory.remove(0);
            this.timeHistory.remove(0);
        }
        this.backtrackPos = findBestBacktrackPosition(currentPos);
    }

    private class_243 findBestBacktrackPosition(class_243 currentPos) {
        if (this.positionHistory.isEmpty() || mc.field_1724 == null) {
            return currentPos;
        }
        class_243 playerPos = mc.field_1724.method_19538();
        double maxBacktrackDist = this.backtrackDistance.getValue().floatValue();
        class_243 bestPos = currentPos;
        double bestDist = playerPos.method_1022(currentPos);
        for (int i = this.positionHistory.size() - 1; i >= 0; i--) {
            class_243 historyPos = this.positionHistory.get(i);
            double distToPlayer = playerPos.method_1022(historyPos);
            double distFromCurrent = currentPos.method_1022(historyPos);
            if (distFromCurrent <= maxBacktrackDist && distToPlayer < bestDist) {
                bestDist = distToPlayer;
                bestPos = historyPos;
            }
        }
        return bestPos;
    }

    private float getEffectiveDistance() {
        if (this.targetEntity == null) {
            return this.distance.getValue().floatValue();
        }
        if (this.waterDistance.getValue().booleanValue() && mc.field_1724.method_5799()) {
            return this.waterDistanceValue.getValue().floatValue();
        }
        if (this.elytraDistance.getValue().booleanValue() && mc.field_1724.method_6128()) {
            return this.elytraDistanceValue.getValue().floatValue();
        }
        if (!this.bypassDistance.getValue().booleanValue()) {
            return this.distance.getValue().floatValue();
        }
        class_1297 class_1297Var = AuraModule.getInstance().target;
        if (class_1297Var == null || class_1297Var != this.targetEntity) {
            return this.distance.getValue().floatValue();
        }
        if (isTargetFacingAway(class_1297Var, this.bypassAngle.getValue().floatValue())) {
            return this.bypassDistanceValue.getValue().floatValue();
        }
        return this.distance.getValue().floatValue();
    }

    private boolean isTargetFacingAway(class_1297 target, float angleDeg) {
        class_243 toPlayer = mc.field_1724.method_19538().method_1020(target.method_19538()).method_1029();
        class_243 targetLook = target.method_5828(1.0f);
        double dot = (toPlayer.field_1352 * targetLook.field_1352) + (toPlayer.field_1351 * targetLook.field_1351) + (toPlayer.field_1350 * targetLook.field_1350);
        return dot <= ((double) class_3532.method_15362((float) Math.toRadians((double) angleDeg)));
    }

    private void handleGrimDistanceMode() {
        class_243 targetPos;
        if (this.targetEntity != null && mc.field_1724.field_6235 <= 0) {
            if (this.backtrack.getValue().booleanValue() && this.backtrackPos != null) {
                targetPos = this.backtrackPos;
            } else if (this.predictMovement.getValue().booleanValue() && this.predictedPos != null) {
                targetPos = this.predictedPos;
            } else {
                targetPos = this.targetEntity.method_19538();
            }
            float effectiveDist = getEffectiveDistance();
            double dist = mc.field_1724.method_5707(targetPos);
            if (dist > effectiveDist * effectiveDist) {
                return;
            }
            float slipperiness = mc.field_1687.method_8320(mc.field_1724.method_23314()).method_26204().method_9499();
            float horizontalFriction = mc.field_1724.method_24828() ? slipperiness * 0.91f : 0.91f;
            float verticalFriction = mc.field_1724.method_24828() ? slipperiness : 0.99f;
            double actualSpeed = ((double) this.speedFactor.getValue().floatValue()) * 0.01d * ((double) horizontalFriction) * ((double) verticalFriction);
            double[] directionMotion = getDirectionToPoint(mc.field_1724.method_19538(), targetPos, actualSpeed);
            if (this.smoothMovement.getValue().booleanValue()) {
                directionMotion[0] = this.lastMotion[0] + ((directionMotion[0] - this.lastMotion[0]) * 0.6d);
                directionMotion[1] = this.lastMotion[1] + ((directionMotion[1] - this.lastMotion[1]) * 0.6d);
            }
            this.lastMotion[0] = directionMotion[0];
            this.lastMotion[1] = directionMotion[1];
            mc.field_1724.method_5762(directionMotion[0], 0.0d, directionMotion[1]);
        }
    }

    private double[] getDirectionToPoint(class_243 from, class_243 to, double speed) {
        double dx = to.field_1352 - from.field_1352;
        double dz = to.field_1350 - from.field_1350;
        double len = Math.sqrt((dx * dx) + (dz * dz));
        return len == 0.0d ? new double[]{0.0d, 0.0d} : new double[]{(dx / len) * speed, (dz / len) * speed};
    }
}
