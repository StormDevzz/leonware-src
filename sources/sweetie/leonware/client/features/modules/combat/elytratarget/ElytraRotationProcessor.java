package sweetie.leonware.client.features.modules.combat.elytratarget;

import java.util.Objects;
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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/elytratarget/ElytraRotationProcessor.class */
public class ElytraRotationProcessor extends Configurable implements QuickImports {
    private final ElytraTargetModule elytraTargetModule;
    private static final float BASE_YAW_SPEED = 45.0f;
    private static final float BASE_PITCH_SPEED = 35.0f;
    private static final int IDEAL_DISTANCE = 10;
    private final BooleanSetting sharpRotations;
    private final BooleanSetting autoDistance;
    private final ModeSetting rotateAt;
    private TargetPosition targetPositionMode = TargetPosition.CENTER;
    private final Supplier<TargetMovementPrediction> predict = TargetMovementPrediction::new;
    public final BooleanSetting customRotations = new BooleanSetting("Custom rotations").value((Boolean) true);

    @Generated
    public ModeSetting getRotateAt() {
        return this.rotateAt;
    }

    /* JADX WARN: Type inference failed for: r1v12, types: [sweetie.leonware.api.module.setting.ModeSetting] */
    /* JADX WARN: Type inference failed for: r1v6, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    public ElytraRotationProcessor(ElytraTargetModule elytraTargetModule) {
        BooleanSetting booleanSettingValue = new BooleanSetting("Sharp").value((Boolean) false);
        BooleanSetting booleanSetting = this.customRotations;
        Objects.requireNonNull(booleanSetting);
        this.sharpRotations = booleanSettingValue.setVisible(booleanSetting::getValue);
        this.autoDistance = new BooleanSetting("Auto distance").value((Boolean) true);
        this.rotateAt = new ModeSetting("Rotate at").value((Enum<?>) this.targetPositionMode).values(TargetPosition.values()).onAction2(() -> {
            this.targetPositionMode = (TargetPosition) Choice.getChoiceByName(getRotateAt().getValue(), TargetPosition.values());
        });
        this.elytraTargetModule = elytraTargetModule;
        addSettings(this.customRotations, this.sharpRotations, this.autoDistance, this.rotateAt);
        addSettings(this.predict.get().getSettings());
    }

    public boolean using() {
        return this.elytraTargetModule.isEnabled() && AuraModule.getInstance().target != null && mc.field_1724.method_6128();
    }

    public void processRotation() {
        class_1309 target = AuraModule.getInstance().target;
        if (using() && this.customRotations.getValue().booleanValue() && target != null) {
            Rotation targetRotation = calculateRotation(target);
            Rotation currentRotation = RotationManager.getInstance().getCurrentRotation();
            if (currentRotation == null) {
                currentRotation = new Rotation(mc.field_1724.method_36454(), mc.field_1724.method_36455());
            }
            Rotation processedRotation = process(currentRotation, targetRotation);
            RotationManager.getInstance().addRotation(new Rotation.VecRotation(processedRotation, getPredictedPos(target)), target, RotationStrategy.TARGET, TaskPriority.HIGH, this.elytraTargetModule);
        }
    }

    private Rotation process(Rotation currentRotation, Rotation targetRotation) {
        float fSin;
        float fSin2;
        Rotation delta = currentRotation.rotationDeltaTo(targetRotation);
        float deltaYaw = delta.getYaw();
        float deltaPitch = delta.getPitch();
        float difference = (float) Math.sqrt((deltaYaw * deltaYaw) + (deltaPitch * deltaPitch));
        long currentTime = System.currentTimeMillis();
        boolean shouldBoost = Math.sin(((double) currentTime) / 300.0d) > 0.8d;
        boolean isTargetBehind = Math.abs(deltaYaw) > 90.0f;
        float speedMultiplier = shouldBoost ? 2.0f : 1.2f;
        if (shouldBoost) {
            fSin = (float) ((Math.sin(((double) ((currentTime % 360) / 300.0f)) * 3.141592653589793d) * 0.800000011920929d) + 1.2000000476837158d);
        } else {
            fSin = 1.2f;
        }
        float smoothBoost = fSin;
        if (isTargetBehind) {
            fSin2 = (float) ((2.200000047683716d * Math.sin(currentTime / 150.0d) * 0.2d) + 1.0d);
        } else {
            fSin2 = 1.2f;
        }
        float backTargetMultiplier = fSin2;
        float speed = speedMultiplier * smoothBoost;
        float yawSpeed = getBaseYawSpeed() * speed * backTargetMultiplier;
        float pitchSpeed = getBasePitchSpeed() * speed;
        float microAdjustment = (float) ((Math.sin(currentTime / 80.0d) * 0.08d) + (Math.cos(currentTime / 120.0d) * 0.05d));
        float moveYaw = class_3532.method_15363(deltaYaw, -yawSpeed, yawSpeed);
        float movePitch = class_3532.method_15363(deltaPitch, -pitchSpeed, pitchSpeed);
        if (difference < 5.0f) {
            moveYaw += microAdjustment * 0.2f;
            movePitch += microAdjustment * 0.8f;
        }
        return new Rotation(currentRotation.getYaw() + moveYaw, class_3532.method_15363(currentRotation.getPitch() + movePitch, -90.0f, 90.0f));
    }

    public Rotation calculateRotation(class_1309 target) {
        class_243 targetPos = getPredictedPos(target);
        if (this.autoDistance.getValue().booleanValue()) {
            class_243 playerPos = mc.field_1724.method_19538();
            class_243 direction = targetPos.method_1020(playerPos).method_1029();
            double distance = playerPos.method_1025(direction);
            if (distance < 100.0d) {
                targetPos = targetPos.method_1020(direction.method_1021(10.0d - distance));
            }
        }
        return RotationUtil.rotationAt(targetPos);
    }

    public class_243 getPredictedPos(class_1309 target) {
        return this.predict.get().predictPosition(target, this.targetPositionMode.getPosition(target)).method_1019(getRandomDirectionVector().method_1021(4.0d));
    }

    private float getBaseYawSpeed() {
        return (this.sharpRotations.getValue().booleanValue() ? 67.5f : BASE_YAW_SPEED) / 3.0f;
    }

    private float getBasePitchSpeed() {
        return (this.sharpRotations.getValue().booleanValue() ? 52.5f : BASE_PITCH_SPEED) / 3.0f;
    }

    private class_243 getRandomDirectionVector() {
        double t = System.currentTimeMillis() / 1000.0d;
        return new class_243((Math.sin(t * 1.8d) * 0.04d) + ((Math.random() - 0.5d) * 0.02d), (Math.sin(t * 2.2d) * 0.03d) + ((Math.random() - 0.5d) * 0.015d), (Math.cos(t * 1.8d) * 0.04d) + ((Math.random() - 0.5d) * 0.02d));
    }
}
