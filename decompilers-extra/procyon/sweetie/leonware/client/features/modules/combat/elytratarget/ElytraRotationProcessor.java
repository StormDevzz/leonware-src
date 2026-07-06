// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.combat.elytratarget;

import lombok.Generated;
import net.minecraft.class_243;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import net.minecraft.class_3532;
import net.minecraft.class_1309;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.utils.task.TaskPriority;
import sweetie.leonware.api.utils.rotation.manager.RotationStrategy;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.client.features.modules.combat.AuraModule;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.system.backend.Choice;
import java.util.Objects;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import java.util.function.Supplier;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.system.backend.Configurable;

public class ElytraRotationProcessor extends Configurable implements QuickImports
{
    private final ElytraTargetModule elytraTargetModule;
    private TargetPosition targetPositionMode;
    private static final float BASE_YAW_SPEED = 45.0f;
    private static final float BASE_PITCH_SPEED = 35.0f;
    private static final int IDEAL_DISTANCE = 10;
    private final Supplier<TargetMovementPrediction> predict;
    public final BooleanSetting customRotations;
    private final BooleanSetting sharpRotations;
    private final BooleanSetting autoDistance;
    private final ModeSetting rotateAt;
    
    public ElytraRotationProcessor(final ElytraTargetModule elytraTargetModule) {
        this.targetPositionMode = TargetPosition.CENTER;
        this.predict = TargetMovementPrediction::new;
        this.customRotations = new BooleanSetting("Custom rotations").value(true);
        final BooleanSetting value = new BooleanSetting("Sharp").value(false);
        final BooleanSetting customRotations = this.customRotations;
        Objects.requireNonNull(customRotations);
        this.sharpRotations = value.setVisible((Supplier<Boolean>)customRotations::getValue);
        this.autoDistance = new BooleanSetting("Auto distance").value(true);
        this.rotateAt = new ModeSetting("Rotate at").value(this.targetPositionMode).values((Enum<?>[])TargetPosition.values()).onAction(() -> this.targetPositionMode = Choice.getChoiceByName(this.getRotateAt().getValue(), TargetPosition.values()));
        this.elytraTargetModule = elytraTargetModule;
        this.addSettings(this.customRotations, this.sharpRotations, this.autoDistance, this.rotateAt);
        this.addSettings(this.predict.get().getSettings());
    }
    
    public boolean using() {
        return this.elytraTargetModule.isEnabled() && AuraModule.getInstance().target != null && ElytraRotationProcessor.mc.field_1724.method_6128();
    }
    
    public void processRotation() {
        final class_1309 target = AuraModule.getInstance().target;
        if (!this.using() || !this.customRotations.getValue() || target == null) {
            return;
        }
        final Rotation targetRotation = this.calculateRotation(target);
        Rotation currentRotation = RotationManager.getInstance().getCurrentRotation();
        if (currentRotation == null) {
            currentRotation = new Rotation(ElytraRotationProcessor.mc.field_1724.method_36454(), ElytraRotationProcessor.mc.field_1724.method_36455());
        }
        final Rotation processedRotation = this.process(currentRotation, targetRotation);
        RotationManager.getInstance().addRotation(new Rotation.VecRotation(processedRotation, this.getPredictedPos(target)), target, RotationStrategy.TARGET, TaskPriority.HIGH, this.elytraTargetModule);
    }
    
    private Rotation process(final Rotation currentRotation, final Rotation targetRotation) {
        final Rotation delta = currentRotation.rotationDeltaTo(targetRotation);
        final float deltaYaw = delta.getYaw();
        final float deltaPitch = delta.getPitch();
        final float difference = (float)Math.sqrt(deltaYaw * deltaYaw + deltaPitch * deltaPitch);
        final long currentTime = System.currentTimeMillis();
        final boolean shouldBoost = Math.sin(currentTime / 300.0) > 0.8;
        final boolean isTargetBehind = Math.abs(deltaYaw) > 90.0f;
        final float speedMultiplier = shouldBoost ? 2.0f : 1.2f;
        final float smoothBoost = shouldBoost ? ((float)(Math.sin(currentTime % 360L / 300.0f * 3.141592653589793) * 0.800000011920929 + 1.2000000476837158)) : 1.2f;
        final float backTargetMultiplier = isTargetBehind ? ((float)(2.200000047683716 * Math.sin(currentTime / 150.0) * 0.2 + 1.0)) : 1.2f;
        final float speed = speedMultiplier * smoothBoost;
        final float yawSpeed = this.getBaseYawSpeed() * speed * backTargetMultiplier;
        final float pitchSpeed = this.getBasePitchSpeed() * speed;
        final float microAdjustment = (float)(Math.sin(currentTime / 80.0) * 0.08 + Math.cos(currentTime / 120.0) * 0.05);
        float moveYaw = class_3532.method_15363(deltaYaw, -yawSpeed, yawSpeed);
        float movePitch = class_3532.method_15363(deltaPitch, -pitchSpeed, pitchSpeed);
        if (difference < 5.0f) {
            moveYaw += microAdjustment * 0.2f;
            movePitch += microAdjustment * 0.8f;
        }
        return new Rotation(currentRotation.getYaw() + moveYaw, class_3532.method_15363(currentRotation.getPitch() + movePitch, -90.0f, 90.0f));
    }
    
    public Rotation calculateRotation(final class_1309 target) {
        class_243 targetPos = this.getPredictedPos(target);
        if (this.autoDistance.getValue()) {
            final class_243 playerPos = ElytraRotationProcessor.mc.field_1724.method_19538();
            final class_243 direction = targetPos.method_1020(playerPos).method_1029();
            final double distance = playerPos.method_1025(direction);
            if (distance < 100.0) {
                targetPos = targetPos.method_1020(direction.method_1021(10.0 - distance));
            }
        }
        return RotationUtil.rotationAt(targetPos);
    }
    
    public class_243 getPredictedPos(final class_1309 target) {
        return this.predict.get().predictPosition(target, this.targetPositionMode.getPosition(target)).method_1019(this.getRandomDirectionVector().method_1021(4.0));
    }
    
    private float getBaseYawSpeed() {
        return (this.sharpRotations.getValue() ? 67.5f : 45.0f) / 3.0f;
    }
    
    private float getBasePitchSpeed() {
        return (this.sharpRotations.getValue() ? 52.5f : 35.0f) / 3.0f;
    }
    
    private class_243 getRandomDirectionVector() {
        final double t = System.currentTimeMillis() / 1000.0;
        return new class_243(Math.sin(t * 1.8) * 0.04 + (Math.random() - 0.5) * 0.02, Math.sin(t * 2.2) * 0.03 + (Math.random() - 0.5) * 0.015, Math.cos(t * 1.8) * 0.04 + (Math.random() - 0.5) * 0.02);
    }
    
    @Generated
    public ModeSetting getRotateAt() {
        return this.rotateAt;
    }
}
