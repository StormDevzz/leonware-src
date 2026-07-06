// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement.speed.modes;

import net.minecraft.class_3532;
import net.minecraft.class_1309;
import net.minecraft.class_238;
import sweetie.leonware.client.features.modules.combat.AuraModule;
import java.util.ArrayDeque;
import java.util.Iterator;
import sweetie.leonware.api.interfaces.IBacktrackable;
import net.minecraft.class_1657;
import sweetie.leonware.api.utils.player.MoveUtil;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.client.features.modules.combat.BacktrackModule;
import java.util.ArrayList;
import java.util.function.Supplier;
import java.util.List;
import net.minecraft.class_243;
import net.minecraft.class_1297;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.client.features.modules.movement.speed.SpeedMode;

public class SpeedAresMine2 extends SpeedMode
{
    private final BooleanSetting pauseInLiquids;
    private final BooleanSetting pauseWhileSneaking;
    private final SliderSetting speedFactor;
    private final SliderSetting distance;
    private final BooleanSetting bypassDistance;
    private final SliderSetting bypassDistanceValue;
    private final SliderSetting bypassAngle;
    private final BooleanSetting waterDistance;
    private final SliderSetting waterDistanceValue;
    private final BooleanSetting elytraDistance;
    private final SliderSetting elytraDistanceValue;
    private final BooleanSetting predictMovement;
    private final SliderSetting predictionFactor;
    private final BooleanSetting smoothMovement;
    private final BooleanSetting backtrack;
    private final SliderSetting backtrackTicks;
    private final SliderSetting backtrackDistance;
    private final SliderSetting movementThreshold;
    private final SliderSetting staticBoostRange;
    private final SliderSetting staticBoostForce;
    private int speedBoostCooldown;
    private final SliderSetting backtrackBoostTicks;
    private class_1297 targetEntity;
    private class_243 lastTargetPos;
    private class_243 predictedPos;
    private double[] lastMotion;
    private final List<class_243> positionHistory;
    private final List<Long> timeHistory;
    private class_243 backtrackPos;
    private class_243 prevTargetPos;
    private double targetMovementSpeed;
    private boolean isTargetStatic;
    
    @Override
    public String getName() {
        return "AresMine2";
    }
    
    public SpeedAresMine2(final Supplier<Boolean> condition) {
        this.pauseInLiquids = new BooleanSetting("Pause in liquid").value(false);
        this.pauseWhileSneaking = new BooleanSetting("Pause sneaking").value(false);
        this.speedFactor = new SliderSetting("Speed factor").value(8.0f).range(1.0f, 15.0f).step(0.1f);
        this.distance = new SliderSetting("Distance").value(3.0f).range(0.5f, 5.0f).step(0.1f);
        this.bypassDistance = new BooleanSetting("Bypass distance").value(false);
        this.bypassDistanceValue = new SliderSetting("Bypass dist value").value(4.0f).range(1.0f, 6.0f).step(0.1f);
        this.bypassAngle = new SliderSetting("Bypass angle").value(120.0f).range(10.0f, 180.0f).step(5.0f);
        this.waterDistance = new BooleanSetting("Water distance").value(false);
        this.waterDistanceValue = new SliderSetting("Water dist value").value(2.0f).range(0.5f, 6.0f).step(0.1f);
        this.elytraDistance = new BooleanSetting("Elytra distance").value(false);
        this.elytraDistanceValue = new SliderSetting("Elytra dist value").value(5.0f).range(0.5f, 10.0f).step(0.1f);
        this.predictMovement = new BooleanSetting("Predict movement").value(true);
        this.predictionFactor = new SliderSetting("Prediction factor").value(2.0f).range(1.0f, 5.0f).step(0.1f);
        this.smoothMovement = new BooleanSetting("Smooth movement").value(true);
        this.backtrack = new BooleanSetting("Backtrack").value(true);
        this.backtrackTicks = new SliderSetting("Backtrack ticks").value(3.0f).range(1.0f, 10.0f).step(0.5f);
        this.backtrackDistance = new SliderSetting("Backtrack max dist").value(6.0f).range(3.0f, 15.0f).step(0.5f);
        this.movementThreshold = new SliderSetting("Movement threshold").value(0.1f).range(0.01f, 0.5f).step(0.01f);
        this.staticBoostRange = new SliderSetting("Static boost range").value(0.45f).range(0.1f, 1.0f).step(0.05f);
        this.staticBoostForce = new SliderSetting("Static boost force").value(0.085f).range(0.01f, 0.2f).step(0.005f);
        this.speedBoostCooldown = 0;
        this.backtrackBoostTicks = new SliderSetting("BT Boost cooldown").value(3.0f).range(1.0f, 20.0f).step(1.0f);
        this.targetEntity = null;
        this.lastTargetPos = null;
        this.predictedPos = null;
        this.lastMotion = new double[] { 0.0, 0.0 };
        this.positionHistory = new ArrayList<class_243>();
        this.timeHistory = new ArrayList<Long>();
        this.backtrackPos = null;
        this.prevTargetPos = null;
        this.targetMovementSpeed = 0.0;
        this.isTargetStatic = false;
        this.pauseInLiquids.setVisible(condition);
        this.pauseWhileSneaking.setVisible(condition);
        this.speedFactor.setVisible(condition);
        this.distance.setVisible(condition);
        this.bypassDistance.setVisible(condition);
        this.bypassDistanceValue.setVisible(() -> condition.get() && this.bypassDistance.getValue());
        this.bypassAngle.setVisible(() -> condition.get() && this.bypassDistance.getValue());
        this.waterDistance.setVisible(condition);
        this.waterDistanceValue.setVisible(() -> condition.get() && this.waterDistance.getValue());
        this.elytraDistance.setVisible(condition);
        this.elytraDistanceValue.setVisible(() -> condition.get() && this.elytraDistance.getValue());
        this.predictMovement.setVisible(condition);
        this.predictionFactor.setVisible(() -> condition.get() && this.predictMovement.getValue());
        this.smoothMovement.setVisible(condition);
        this.backtrack.setVisible(condition);
        this.backtrackTicks.setVisible(() -> condition.get() && this.backtrack.getValue());
        this.backtrackDistance.setVisible(() -> condition.get() && this.backtrack.getValue());
        this.movementThreshold.setVisible(condition);
        this.staticBoostRange.setVisible(condition);
        this.staticBoostForce.setVisible(condition);
        this.backtrackBoostTicks.setVisible(() -> condition.get() && BacktrackModule.getInstance().isApplyToSpeed());
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
        this.lastMotion = new double[] { 0.0, 0.0 };
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
        if (SpeedAresMine2.mc.field_1724.method_52535() && this.pauseInLiquids.getValue()) {
            return;
        }
        if (SpeedAresMine2.mc.field_1724.method_5715() && this.pauseWhileSneaking.getValue()) {
            return;
        }
        this.updateGrimTarget();
    }
    
    @Override
    public void onTravel() {
        if (SpeedAresMine2.mc.field_1724 == null || SpeedAresMine2.mc.field_1687 == null) {
            return;
        }
        if (SpeedAresMine2.mc.field_1724.method_52535() && this.pauseInLiquids.getValue()) {
            return;
        }
        if (SpeedAresMine2.mc.field_1724.method_5715() && this.pauseWhileSneaking.getValue()) {
            return;
        }
        if (!MoveUtil.isMoving()) {
            return;
        }
        if (this.isTargetStatic && this.targetEntity != null) {
            this.handleStaticTargetBoost();
        }
        else {
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
        for (final class_1657 player : SpeedAresMine2.mc.field_1687.method_18456()) {
            if (player == SpeedAresMine2.mc.field_1724) {
                continue;
            }
            if (!(player instanceof IBacktrackable)) {
                continue;
            }
            final IBacktrackable bt = (IBacktrackable)player;
            final ArrayDeque<BacktrackModule.Position> tracks = bt.leonware$getBackTracks();
            if (tracks.isEmpty()) {
                continue;
            }
            final BacktrackModule.Position backPos = tracks.peekFirst();
            if (backPos == null) {
                continue;
            }
            final class_243 btPos = backPos.pos();
            final double effectiveDist = this.getEffectiveDistance();
            if (SpeedAresMine2.mc.field_1724.method_5707(btPos) <= effectiveDist * effectiveDist) {
                final double[] dir = this.getDirectionToPoint(SpeedAresMine2.mc.field_1724.method_19538(), btPos, this.speedFactor.getValue() * 0.01);
                SpeedAresMine2.mc.field_1724.method_5762(dir[0], 0.0, dir[1]);
                this.speedBoostCooldown = this.backtrackBoostTicks.getValue().intValue();
                break;
            }
        }
    }
    
    private void updateGrimTarget() {
        final AuraModule aura = AuraModule.getInstance();
        if (aura.isEnabled() && aura.target != null) {
            this.targetEntity = (class_1297)aura.target;
            if (this.targetEntity != null) {
                final class_243 currentPos = this.targetEntity.method_19538();
                final long currentTime = System.currentTimeMillis();
                if (this.prevTargetPos != null) {
                    this.targetMovementSpeed = currentPos.method_1022(this.prevTargetPos);
                    this.isTargetStatic = (this.targetMovementSpeed < this.movementThreshold.getValue());
                }
                this.prevTargetPos = currentPos;
                if (this.backtrack.getValue() && !this.isTargetStatic) {
                    this.updateBacktrackHistory(currentPos, currentTime);
                }
                if (this.lastTargetPos == null) {
                    final class_243 class_243 = currentPos;
                    this.lastTargetPos = class_243;
                    this.predictedPos = class_243;
                }
                else {
                    final class_243 velocity = new class_243(currentPos.field_1352 - this.lastTargetPos.field_1352, currentPos.field_1351 - this.lastTargetPos.field_1351, currentPos.field_1350 - this.lastTargetPos.field_1350);
                    if (this.predictMovement.getValue() && !this.isTargetStatic) {
                        final float factor = this.predictionFactor.getValue();
                        this.predictedPos = currentPos.method_1031(velocity.field_1352 * factor, velocity.field_1351 * factor, velocity.field_1350 * factor);
                        if (!SpeedAresMine2.mc.field_1687.method_8393((int)this.predictedPos.field_1352 >> 4, (int)this.predictedPos.field_1350 >> 4)) {
                            this.predictedPos = currentPos;
                        }
                    }
                    else {
                        this.predictedPos = currentPos;
                    }
                    this.lastTargetPos = currentPos;
                }
            }
            return;
        }
        this.targetEntity = null;
        this.lastTargetPos = null;
        this.predictedPos = null;
        this.backtrackPos = null;
        this.prevTargetPos = null;
        this.targetMovementSpeed = 0.0;
        this.isTargetStatic = false;
        this.positionHistory.clear();
        this.timeHistory.clear();
    }
    
    private void handleStaticTargetBoost() {
        if (this.targetEntity == null) {
            return;
        }
        final class_238 checkBox = SpeedAresMine2.mc.field_1724.method_5829().method_1014((double)this.staticBoostRange.getValue());
        if (!checkBox.method_994(this.targetEntity.method_5829())) {
            return;
        }
        final class_243 directionToTarget = this.targetEntity.method_19538().method_1020(SpeedAresMine2.mc.field_1724.method_19538()).method_1029();
        final double randomAngle = (Math.random() - 0.5) * 0.22;
        final double sin = Math.sin(randomAngle);
        final double cos = Math.cos(randomAngle);
        final double nx = directionToTarget.field_1352 * cos - directionToTarget.field_1350 * sin;
        final double nz = directionToTarget.field_1352 * sin + directionToTarget.field_1350 * cos;
        final class_243 finalDir = new class_243(nx, 0.0, nz).method_1029();
        final class_243 currentVel = SpeedAresMine2.mc.field_1724.method_18798();
        final class_243 added = finalDir.method_1021((double)this.staticBoostForce.getValue());
        final class_243 newVel = currentVel.method_1031(added.field_1352 * 0.88, 0.0, added.field_1350 * 0.88);
        SpeedAresMine2.mc.field_1724.method_18799(newVel);
    }
    
    private void updateBacktrackHistory(final class_243 currentPos, final long currentTime) {
        this.positionHistory.add(currentPos);
        this.timeHistory.add(currentTime);
        final long maxAge = (long)(this.backtrackTicks.getValue() * 50.0f);
        while (!this.timeHistory.isEmpty() && currentTime - this.timeHistory.get(0) > maxAge) {
            this.positionHistory.remove(0);
            this.timeHistory.remove(0);
        }
        this.backtrackPos = this.findBestBacktrackPosition(currentPos);
    }
    
    private class_243 findBestBacktrackPosition(final class_243 currentPos) {
        if (this.positionHistory.isEmpty() || SpeedAresMine2.mc.field_1724 == null) {
            return currentPos;
        }
        final class_243 playerPos = SpeedAresMine2.mc.field_1724.method_19538();
        final double maxBacktrackDist = this.backtrackDistance.getValue();
        class_243 bestPos = currentPos;
        double bestDist = playerPos.method_1022(currentPos);
        for (int i = this.positionHistory.size() - 1; i >= 0; --i) {
            final class_243 historyPos = this.positionHistory.get(i);
            final double distToPlayer = playerPos.method_1022(historyPos);
            final double distFromCurrent = currentPos.method_1022(historyPos);
            if (distFromCurrent <= maxBacktrackDist && distToPlayer < bestDist) {
                bestDist = distToPlayer;
                bestPos = historyPos;
            }
        }
        return bestPos;
    }
    
    private float getEffectiveDistance() {
        if (this.targetEntity == null) {
            return this.distance.getValue();
        }
        if (this.waterDistance.getValue() && SpeedAresMine2.mc.field_1724.method_5799()) {
            return this.waterDistanceValue.getValue();
        }
        if (this.elytraDistance.getValue() && SpeedAresMine2.mc.field_1724.method_6128()) {
            return this.elytraDistanceValue.getValue();
        }
        if (!this.bypassDistance.getValue()) {
            return this.distance.getValue();
        }
        final class_1309 auraTarget = AuraModule.getInstance().target;
        if (auraTarget == null || auraTarget != this.targetEntity) {
            return this.distance.getValue();
        }
        if (this.isTargetFacingAway((class_1297)auraTarget, this.bypassAngle.getValue())) {
            return this.bypassDistanceValue.getValue();
        }
        return this.distance.getValue();
    }
    
    private boolean isTargetFacingAway(final class_1297 target, final float angleDeg) {
        final class_243 toPlayer = SpeedAresMine2.mc.field_1724.method_19538().method_1020(target.method_19538()).method_1029();
        final class_243 targetLook = target.method_5828(1.0f);
        final double dot = toPlayer.field_1352 * targetLook.field_1352 + toPlayer.field_1351 * targetLook.field_1351 + toPlayer.field_1350 * targetLook.field_1350;
        return dot <= class_3532.method_15362((float)Math.toRadians(angleDeg));
    }
    
    private void handleGrimDistanceMode() {
        if (this.targetEntity == null) {
            return;
        }
        if (SpeedAresMine2.mc.field_1724.field_6235 > 0) {
            return;
        }
        class_243 targetPos;
        if (this.backtrack.getValue() && this.backtrackPos != null) {
            targetPos = this.backtrackPos;
        }
        else if (this.predictMovement.getValue() && this.predictedPos != null) {
            targetPos = this.predictedPos;
        }
        else {
            targetPos = this.targetEntity.method_19538();
        }
        final float effectiveDist = this.getEffectiveDistance();
        final double dist = SpeedAresMine2.mc.field_1724.method_5707(targetPos);
        if (dist > effectiveDist * effectiveDist) {
            return;
        }
        final float slipperiness = SpeedAresMine2.mc.field_1687.method_8320(SpeedAresMine2.mc.field_1724.method_23314()).method_26204().method_9499();
        final float horizontalFriction = SpeedAresMine2.mc.field_1724.method_24828() ? (slipperiness * 0.91f) : 0.91f;
        final float verticalFriction = SpeedAresMine2.mc.field_1724.method_24828() ? slipperiness : 0.99f;
        final double actualSpeed = this.speedFactor.getValue() * 0.01 * horizontalFriction * verticalFriction;
        final double[] directionMotion = this.getDirectionToPoint(SpeedAresMine2.mc.field_1724.method_19538(), targetPos, actualSpeed);
        if (this.smoothMovement.getValue()) {
            final double accelFactor = 0.6;
            directionMotion[0] = this.lastMotion[0] + (directionMotion[0] - this.lastMotion[0]) * accelFactor;
            directionMotion[1] = this.lastMotion[1] + (directionMotion[1] - this.lastMotion[1]) * accelFactor;
        }
        this.lastMotion[0] = directionMotion[0];
        this.lastMotion[1] = directionMotion[1];
        SpeedAresMine2.mc.field_1724.method_5762(directionMotion[0], 0.0, directionMotion[1]);
    }
    
    private double[] getDirectionToPoint(final class_243 from, final class_243 to, final double speed) {
        final double dx = to.field_1352 - from.field_1352;
        final double dz = to.field_1350 - from.field_1350;
        final double len = Math.sqrt(dx * dx + dz * dz);
        if (len == 0.0) {
            return new double[] { 0.0, 0.0 };
        }
        return new double[] { dx / len * speed, dz / len * speed };
    }
}
