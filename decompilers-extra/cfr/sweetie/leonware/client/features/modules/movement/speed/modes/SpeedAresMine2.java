/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_1657
 *  net.minecraft.class_238
 *  net.minecraft.class_243
 *  net.minecraft.class_3532
 */
package sweetie.leonware.client.features.modules.movement.speed.modes;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
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

public class SpeedAresMine2
extends SpeedMode {
    private final BooleanSetting pauseInLiquids = new BooleanSetting("Pause in liquid").value(false);
    private final BooleanSetting pauseWhileSneaking = new BooleanSetting("Pause sneaking").value(false);
    private final SliderSetting speedFactor = new SliderSetting("Speed factor").value(Float.valueOf(8.0f)).range(1.0f, 15.0f).step(0.1f);
    private final SliderSetting distance = new SliderSetting("Distance").value(Float.valueOf(3.0f)).range(0.5f, 5.0f).step(0.1f);
    private final BooleanSetting bypassDistance = new BooleanSetting("Bypass distance").value(false);
    private final SliderSetting bypassDistanceValue = new SliderSetting("Bypass dist value").value(Float.valueOf(4.0f)).range(1.0f, 6.0f).step(0.1f);
    private final SliderSetting bypassAngle = new SliderSetting("Bypass angle").value(Float.valueOf(120.0f)).range(10.0f, 180.0f).step(5.0f);
    private final BooleanSetting waterDistance = new BooleanSetting("Water distance").value(false);
    private final SliderSetting waterDistanceValue = new SliderSetting("Water dist value").value(Float.valueOf(2.0f)).range(0.5f, 6.0f).step(0.1f);
    private final BooleanSetting elytraDistance = new BooleanSetting("Elytra distance").value(false);
    private final SliderSetting elytraDistanceValue = new SliderSetting("Elytra dist value").value(Float.valueOf(5.0f)).range(0.5f, 10.0f).step(0.1f);
    private final BooleanSetting predictMovement = new BooleanSetting("Predict movement").value(true);
    private final SliderSetting predictionFactor = new SliderSetting("Prediction factor").value(Float.valueOf(2.0f)).range(1.0f, 5.0f).step(0.1f);
    private final BooleanSetting smoothMovement = new BooleanSetting("Smooth movement").value(true);
    private final BooleanSetting backtrack = new BooleanSetting("Backtrack").value(true);
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
    private double[] lastMotion = new double[]{0.0, 0.0};
    private final List<class_243> positionHistory = new ArrayList<class_243>();
    private final List<Long> timeHistory = new ArrayList<Long>();
    private class_243 backtrackPos = null;
    private class_243 prevTargetPos = null;
    private double targetMovementSpeed = 0.0;
    private boolean isTargetStatic = false;

    @Override
    public String getName() {
        return "AresMine2";
    }

    public SpeedAresMine2(Supplier<Boolean> condition) {
        this.pauseInLiquids.setVisible((Supplier)condition);
        this.pauseWhileSneaking.setVisible((Supplier)condition);
        this.speedFactor.setVisible((Supplier)condition);
        this.distance.setVisible((Supplier)condition);
        this.bypassDistance.setVisible((Supplier)condition);
        this.bypassDistanceValue.setVisible(() -> (Boolean)condition.get() != false && (Boolean)this.bypassDistance.getValue() != false);
        this.bypassAngle.setVisible(() -> (Boolean)condition.get() != false && (Boolean)this.bypassDistance.getValue() != false);
        this.waterDistance.setVisible((Supplier)condition);
        this.waterDistanceValue.setVisible(() -> (Boolean)condition.get() != false && (Boolean)this.waterDistance.getValue() != false);
        this.elytraDistance.setVisible((Supplier)condition);
        this.elytraDistanceValue.setVisible(() -> (Boolean)condition.get() != false && (Boolean)this.elytraDistance.getValue() != false);
        this.predictMovement.setVisible((Supplier)condition);
        this.predictionFactor.setVisible(() -> (Boolean)condition.get() != false && (Boolean)this.predictMovement.getValue() != false);
        this.smoothMovement.setVisible((Supplier)condition);
        this.backtrack.setVisible((Supplier)condition);
        this.backtrackTicks.setVisible(() -> (Boolean)condition.get() != false && (Boolean)this.backtrack.getValue() != false);
        this.backtrackDistance.setVisible(() -> (Boolean)condition.get() != false && (Boolean)this.backtrack.getValue() != false);
        this.movementThreshold.setVisible((Supplier)condition);
        this.staticBoostRange.setVisible((Supplier)condition);
        this.staticBoostForce.setVisible((Supplier)condition);
        this.backtrackBoostTicks.setVisible(() -> (Boolean)condition.get() != false && BacktrackModule.getInstance().isApplyToSpeed());
        this.addSettings(this.pauseInLiquids, this.pauseWhileSneaking, this.speedFactor, this.distance, this.bypassDistance, this.bypassDistanceValue, this.bypassAngle, this.waterDistance, this.waterDistanceValue, this.elytraDistance, this.elytraDistanceValue, this.predictMovement, this.predictionFactor, this.smoothMovement, this.backtrack, this.backtrackTicks, this.backtrackDistance, this.movementThreshold, this.staticBoostRange, this.staticBoostForce, this.backtrackBoostTicks);
    }

    @Override
    public void onEnable() {
        this.reset();
    }

    @Override
    public void onDisable() {
        this.reset();
    }

    private void reset() {
        this.targetEntity = null;
        this.lastTargetPos = null;
        this.predictedPos = null;
        this.lastMotion = new double[]{0.0, 0.0};
        this.backtrackPos = null;
        this.prevTargetPos = null;
        this.targetMovementSpeed = 0.0;
        this.isTargetStatic = false;
        this.positionHistory.clear();
        this.timeHistory.clear();
    }

    @Override
    public void onUpdate() {
        if (SpeedAresMine2.mc.field_1724 == null || SpeedAresMine2.mc.field_1687 == null) {
            return;
        }
        if (SpeedAresMine2.mc.field_1724.method_52535() && ((Boolean)this.pauseInLiquids.getValue()).booleanValue()) {
            return;
        }
        if (SpeedAresMine2.mc.field_1724.method_5715() && ((Boolean)this.pauseWhileSneaking.getValue()).booleanValue()) {
            return;
        }
        this.updateGrimTarget();
    }

    @Override
    public void onTravel() {
        if (SpeedAresMine2.mc.field_1724 == null || SpeedAresMine2.mc.field_1687 == null) {
            return;
        }
        if (SpeedAresMine2.mc.field_1724.method_52535() && ((Boolean)this.pauseInLiquids.getValue()).booleanValue()) {
            return;
        }
        if (SpeedAresMine2.mc.field_1724.method_5715() && ((Boolean)this.pauseWhileSneaking.getValue()).booleanValue()) {
            return;
        }
        if (!MoveUtil.isMoving()) {
            return;
        }
        if (this.isTargetStatic && this.targetEntity != null) {
            this.handleStaticTargetBoost();
        } else {
            this.handleGrimDistanceMode();
        }
        this.applyBacktrackBoost2();
    }

    private void applyBacktrackBoost2() {
        if (!BacktrackModule.getInstance().isApplyToSpeed()) {
            return;
        }
        if (this.speedBoostCooldown > 0) {
            --this.speedBoostCooldown;
            return;
        }
        for (class_1657 player : SpeedAresMine2.mc.field_1687.method_18456()) {
            BacktrackModule.Position backPos;
            IBacktrackable bt;
            ArrayDeque<BacktrackModule.Position> tracks;
            if (player == SpeedAresMine2.mc.field_1724 || !(player instanceof IBacktrackable) || (tracks = (bt = (IBacktrackable)player).leonware$getBackTracks()).isEmpty() || (backPos = tracks.peekFirst()) == null) continue;
            class_243 btPos = backPos.pos();
            double effectiveDist = this.getEffectiveDistance();
            if (!(SpeedAresMine2.mc.field_1724.method_5707(btPos) <= effectiveDist * effectiveDist)) continue;
            double[] dir = this.getDirectionToPoint(SpeedAresMine2.mc.field_1724.method_19538(), btPos, (double)((Float)this.speedFactor.getValue()).floatValue() * 0.01);
            SpeedAresMine2.mc.field_1724.method_5762(dir[0], 0.0, dir[1]);
            this.speedBoostCooldown = ((Float)this.backtrackBoostTicks.getValue()).intValue();
            break;
        }
    }

    private void updateGrimTarget() {
        AuraModule aura = AuraModule.getInstance();
        if (!aura.isEnabled() || aura.target == null) {
            this.targetEntity = null;
            this.lastTargetPos = null;
            this.predictedPos = null;
            this.backtrackPos = null;
            this.prevTargetPos = null;
            this.targetMovementSpeed = 0.0;
            this.isTargetStatic = false;
            this.positionHistory.clear();
            this.timeHistory.clear();
            return;
        }
        this.targetEntity = aura.target;
        if (this.targetEntity != null) {
            class_243 currentPos = this.targetEntity.method_19538();
            long currentTime = System.currentTimeMillis();
            if (this.prevTargetPos != null) {
                this.targetMovementSpeed = currentPos.method_1022(this.prevTargetPos);
                this.isTargetStatic = this.targetMovementSpeed < (double)((Float)this.movementThreshold.getValue()).floatValue();
            }
            this.prevTargetPos = currentPos;
            if (((Boolean)this.backtrack.getValue()).booleanValue() && !this.isTargetStatic) {
                this.updateBacktrackHistory(currentPos, currentTime);
            }
            if (this.lastTargetPos == null) {
                this.predictedPos = this.lastTargetPos = currentPos;
            } else {
                class_243 velocity = new class_243(currentPos.field_1352 - this.lastTargetPos.field_1352, currentPos.field_1351 - this.lastTargetPos.field_1351, currentPos.field_1350 - this.lastTargetPos.field_1350);
                if (((Boolean)this.predictMovement.getValue()).booleanValue() && !this.isTargetStatic) {
                    float factor = ((Float)this.predictionFactor.getValue()).floatValue();
                    this.predictedPos = currentPos.method_1031(velocity.field_1352 * (double)factor, velocity.field_1351 * (double)factor, velocity.field_1350 * (double)factor);
                    if (!SpeedAresMine2.mc.field_1687.method_8393((int)this.predictedPos.field_1352 >> 4, (int)this.predictedPos.field_1350 >> 4)) {
                        this.predictedPos = currentPos;
                    }
                } else {
                    this.predictedPos = currentPos;
                }
                this.lastTargetPos = currentPos;
            }
        }
    }

    private void handleStaticTargetBoost() {
        if (this.targetEntity == null) {
            return;
        }
        class_238 checkBox = SpeedAresMine2.mc.field_1724.method_5829().method_1014((double)((Float)this.staticBoostRange.getValue()).floatValue());
        if (!checkBox.method_994(this.targetEntity.method_5829())) {
            return;
        }
        class_243 directionToTarget = this.targetEntity.method_19538().method_1020(SpeedAresMine2.mc.field_1724.method_19538()).method_1029();
        double randomAngle = (Math.random() - 0.5) * 0.22;
        double sin = Math.sin(randomAngle);
        double cos = Math.cos(randomAngle);
        double nx = directionToTarget.field_1352 * cos - directionToTarget.field_1350 * sin;
        double nz = directionToTarget.field_1352 * sin + directionToTarget.field_1350 * cos;
        class_243 finalDir = new class_243(nx, 0.0, nz).method_1029();
        class_243 currentVel = SpeedAresMine2.mc.field_1724.method_18798();
        class_243 added = finalDir.method_1021((double)((Float)this.staticBoostForce.getValue()).floatValue());
        class_243 newVel = currentVel.method_1031(added.field_1352 * 0.88, 0.0, added.field_1350 * 0.88);
        SpeedAresMine2.mc.field_1724.method_18799(newVel);
    }

    private void updateBacktrackHistory(class_243 currentPos, long currentTime) {
        this.positionHistory.add(currentPos);
        this.timeHistory.add(currentTime);
        long maxAge = (long)(((Float)this.backtrackTicks.getValue()).floatValue() * 50.0f);
        while (!this.timeHistory.isEmpty() && currentTime - this.timeHistory.get(0) > maxAge) {
            this.positionHistory.remove(0);
            this.timeHistory.remove(0);
        }
        this.backtrackPos = this.findBestBacktrackPosition(currentPos);
    }

    private class_243 findBestBacktrackPosition(class_243 currentPos) {
        if (this.positionHistory.isEmpty() || SpeedAresMine2.mc.field_1724 == null) {
            return currentPos;
        }
        class_243 playerPos = SpeedAresMine2.mc.field_1724.method_19538();
        double maxBacktrackDist = ((Float)this.backtrackDistance.getValue()).floatValue();
        class_243 bestPos = currentPos;
        double bestDist = playerPos.method_1022(currentPos);
        for (int i = this.positionHistory.size() - 1; i >= 0; --i) {
            class_243 historyPos = this.positionHistory.get(i);
            double distToPlayer = playerPos.method_1022(historyPos);
            double distFromCurrent = currentPos.method_1022(historyPos);
            if (!(distFromCurrent <= maxBacktrackDist) || !(distToPlayer < bestDist)) continue;
            bestDist = distToPlayer;
            bestPos = historyPos;
        }
        return bestPos;
    }

    private float getEffectiveDistance() {
        if (this.targetEntity == null) {
            return ((Float)this.distance.getValue()).floatValue();
        }
        if (((Boolean)this.waterDistance.getValue()).booleanValue() && SpeedAresMine2.mc.field_1724.method_5799()) {
            return ((Float)this.waterDistanceValue.getValue()).floatValue();
        }
        if (((Boolean)this.elytraDistance.getValue()).booleanValue() && SpeedAresMine2.mc.field_1724.method_6128()) {
            return ((Float)this.elytraDistanceValue.getValue()).floatValue();
        }
        if (!((Boolean)this.bypassDistance.getValue()).booleanValue()) {
            return ((Float)this.distance.getValue()).floatValue();
        }
        class_1309 auraTarget = AuraModule.getInstance().target;
        if (auraTarget == null || auraTarget != this.targetEntity) {
            return ((Float)this.distance.getValue()).floatValue();
        }
        if (this.isTargetFacingAway((class_1297)auraTarget, ((Float)this.bypassAngle.getValue()).floatValue())) {
            return ((Float)this.bypassDistanceValue.getValue()).floatValue();
        }
        return ((Float)this.distance.getValue()).floatValue();
    }

    private boolean isTargetFacingAway(class_1297 target, float angleDeg) {
        class_243 toPlayer = SpeedAresMine2.mc.field_1724.method_19538().method_1020(target.method_19538()).method_1029();
        class_243 targetLook = target.method_5828(1.0f);
        double dot = toPlayer.field_1352 * targetLook.field_1352 + toPlayer.field_1351 * targetLook.field_1351 + toPlayer.field_1350 * targetLook.field_1350;
        return dot <= (double)class_3532.method_15362((float)((float)Math.toRadians(angleDeg)));
    }

    private void handleGrimDistanceMode() {
        if (this.targetEntity == null) {
            return;
        }
        if (SpeedAresMine2.mc.field_1724.field_6235 > 0) {
            return;
        }
        class_243 targetPos = (Boolean)this.backtrack.getValue() != false && this.backtrackPos != null ? this.backtrackPos : ((Boolean)this.predictMovement.getValue() != false && this.predictedPos != null ? this.predictedPos : this.targetEntity.method_19538());
        float effectiveDist = this.getEffectiveDistance();
        double dist = SpeedAresMine2.mc.field_1724.method_5707(targetPos);
        if (dist > (double)(effectiveDist * effectiveDist)) {
            return;
        }
        float slipperiness = SpeedAresMine2.mc.field_1687.method_8320(SpeedAresMine2.mc.field_1724.method_23314()).method_26204().method_9499();
        float horizontalFriction = SpeedAresMine2.mc.field_1724.method_24828() ? slipperiness * 0.91f : 0.91f;
        float verticalFriction = SpeedAresMine2.mc.field_1724.method_24828() ? slipperiness : 0.99f;
        double actualSpeed = (double)((Float)this.speedFactor.getValue()).floatValue() * 0.01 * (double)horizontalFriction * (double)verticalFriction;
        double[] directionMotion = this.getDirectionToPoint(SpeedAresMine2.mc.field_1724.method_19538(), targetPos, actualSpeed);
        if (((Boolean)this.smoothMovement.getValue()).booleanValue()) {
            double accelFactor = 0.6;
            directionMotion[0] = this.lastMotion[0] + (directionMotion[0] - this.lastMotion[0]) * accelFactor;
            directionMotion[1] = this.lastMotion[1] + (directionMotion[1] - this.lastMotion[1]) * accelFactor;
        }
        this.lastMotion[0] = directionMotion[0];
        this.lastMotion[1] = directionMotion[1];
        SpeedAresMine2.mc.field_1724.method_5762(directionMotion[0], 0.0, directionMotion[1]);
    }

    private double[] getDirectionToPoint(class_243 from, class_243 to, double speed) {
        double dx = to.field_1352 - from.field_1352;
        double dz = to.field_1350 - from.field_1350;
        double len = Math.sqrt(dx * dx + dz * dz);
        if (len == 0.0) {
            return new double[]{0.0, 0.0};
        }
        return new double[]{dx / len * speed, dz / len * speed};
    }
}

