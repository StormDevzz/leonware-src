/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1309
 *  net.minecraft.class_243
 *  net.minecraft.class_3532
 */
package sweetie.leonware.client.features.modules.combat.elytratarget;

import java.util.function.Supplier;
import lombok.Generated;
import net.minecraft.class_1309;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.system.backend.Choice;
import sweetie.leonware.api.system.backend.Configurable;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.utils.rotation.manager.RotationStrategy;
import sweetie.leonware.api.utils.task.TaskPriority;
import sweetie.leonware.client.features.modules.combat.AuraModule;
import sweetie.leonware.client.features.modules.combat.elytratarget.ElytraTargetModule;
import sweetie.leonware.client.features.modules.combat.elytratarget.TargetMovementPrediction;
import sweetie.leonware.client.features.modules.combat.elytratarget.TargetPosition;

public class ElytraRotationProcessor
extends Configurable
implements QuickImports {
    private final ElytraTargetModule elytraTargetModule;
    private TargetPosition targetPositionMode = TargetPosition.CENTER;
    private static final float BASE_YAW_SPEED = 45.0f;
    private static final float BASE_PITCH_SPEED = 35.0f;
    private static final int IDEAL_DISTANCE = 10;
    private final Supplier<TargetMovementPrediction> predict = TargetMovementPrediction::new;
    public final BooleanSetting customRotations = new BooleanSetting("Custom rotations").value(true);
    private final BooleanSetting sharpRotations = new BooleanSetting("Sharp").value(false).setVisible(this.customRotations::getValue);
    private final BooleanSetting autoDistance = new BooleanSetting("Auto distance").value(true);
    private final ModeSetting rotateAt = new ModeSetting("Rotate at").value(this.targetPositionMode).values(TargetPosition.values()).onAction(() -> {
        this.targetPositionMode = (TargetPosition)Choice.getChoiceByName((String)((String)this.getRotateAt().getValue()), (ModeSetting.NamedChoice[])TargetPosition.values());
    });

    public ElytraRotationProcessor(ElytraTargetModule elytraTargetModule) {
        this.elytraTargetModule = elytraTargetModule;
        this.addSettings(this.customRotations, this.sharpRotations, this.autoDistance, this.rotateAt);
        this.addSettings(this.predict.get().getSettings());
    }

    public boolean using() {
        return this.elytraTargetModule.isEnabled() && AuraModule.getInstance().target != null && ElytraRotationProcessor.mc.field_1724.method_6128();
    }

    public void processRotation() {
        class_1309 target = AuraModule.getInstance().target;
        if (!this.using() || !((Boolean)this.customRotations.getValue()).booleanValue() || target == null) {
            return;
        }
        Rotation targetRotation = this.calculateRotation(target);
        Rotation currentRotation = RotationManager.getInstance().getCurrentRotation();
        if (currentRotation == null) {
            currentRotation = new Rotation(ElytraRotationProcessor.mc.field_1724.method_36454(), ElytraRotationProcessor.mc.field_1724.method_36455());
        }
        Rotation processedRotation = this.process(currentRotation, targetRotation);
        RotationManager.getInstance().addRotation(new Rotation.VecRotation(processedRotation, this.getPredictedPos(target)), target, RotationStrategy.TARGET, TaskPriority.HIGH, this.elytraTargetModule);
    }

    private Rotation process(Rotation currentRotation, Rotation targetRotation) {
        Rotation delta = currentRotation.rotationDeltaTo(targetRotation);
        float deltaYaw = delta.getYaw();
        float deltaPitch = delta.getPitch();
        float difference = (float)Math.sqrt(deltaYaw * deltaYaw + deltaPitch * deltaPitch);
        long currentTime = System.currentTimeMillis();
        boolean shouldBoost = Math.sin((double)currentTime / 300.0) > 0.8;
        boolean isTargetBehind = Math.abs(deltaYaw) > 90.0f;
        float speedMultiplier = shouldBoost ? 2.0f : 1.2f;
        float smoothBoost = shouldBoost ? (float)(Math.sin((double)((float)(currentTime % 360L) / 300.0f) * Math.PI) * (double)0.8f + (double)1.2f) : 1.2f;
        float backTargetMultiplier = isTargetBehind ? (float)((double)2.2f * Math.sin((double)currentTime / 150.0) * 0.2 + 1.0) : 1.2f;
        float speed = speedMultiplier * smoothBoost;
        float yawSpeed = this.getBaseYawSpeed() * speed * backTargetMultiplier;
        float pitchSpeed = this.getBasePitchSpeed() * speed;
        float microAdjustment = (float)(Math.sin((double)currentTime / 80.0) * 0.08 + Math.cos((double)currentTime / 120.0) * 0.05);
        float moveYaw = class_3532.method_15363((float)deltaYaw, (float)(-yawSpeed), (float)yawSpeed);
        float movePitch = class_3532.method_15363((float)deltaPitch, (float)(-pitchSpeed), (float)pitchSpeed);
        if (difference < 5.0f) {
            moveYaw += microAdjustment * 0.2f;
            movePitch += microAdjustment * 0.8f;
        }
        return new Rotation(currentRotation.getYaw() + moveYaw, class_3532.method_15363((float)(currentRotation.getPitch() + movePitch), (float)-90.0f, (float)90.0f));
    }

    public Rotation calculateRotation(class_1309 target) {
        class_243 direction;
        class_243 playerPos;
        double distance;
        class_243 targetPos = this.getPredictedPos(target);
        if (((Boolean)this.autoDistance.getValue()).booleanValue() && (distance = (playerPos = ElytraRotationProcessor.mc.field_1724.method_19538()).method_1025(direction = targetPos.method_1020(playerPos).method_1029())) < 100.0) {
            targetPos = targetPos.method_1020(direction.method_1021(10.0 - distance));
        }
        return RotationUtil.rotationAt(targetPos);
    }

    public class_243 getPredictedPos(class_1309 target) {
        return this.predict.get().predictPosition(target, this.targetPositionMode.getPosition(target)).method_1019(this.getRandomDirectionVector().method_1021(4.0));
    }

    private float getBaseYawSpeed() {
        return ((Boolean)this.sharpRotations.getValue() != false ? 67.5f : 45.0f) / 3.0f;
    }

    private float getBasePitchSpeed() {
        return ((Boolean)this.sharpRotations.getValue() != false ? 52.5f : 35.0f) / 3.0f;
    }

    private class_243 getRandomDirectionVector() {
        double t = (double)System.currentTimeMillis() / 1000.0;
        return new class_243(Math.sin(t * 1.8) * 0.04 + (Math.random() - 0.5) * 0.02, Math.sin(t * 2.2) * 0.03 + (Math.random() - 0.5) * 0.015, Math.cos(t * 1.8) * 0.04 + (Math.random() - 0.5) * 0.02);
    }

    @Generated
    public ModeSetting getRotateAt() {
        return this.rotateAt;
    }
}

