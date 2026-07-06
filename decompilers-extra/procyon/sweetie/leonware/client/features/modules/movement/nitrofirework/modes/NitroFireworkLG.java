// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement.nitrofirework.modes;

import sweetie.leonware.api.utils.rotation.manager.Rotation;
import net.minecraft.class_238;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import net.minecraft.class_243;
import sweetie.leonware.client.features.modules.combat.elytratarget.ElytraTargetModule;
import sweetie.leonware.client.features.modules.combat.AuraModule;
import sweetie.leonware.api.system.backend.Pair;
import sweetie.leonware.api.module.setting.Setting;
import java.util.function.Supplier;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.client.features.modules.movement.nitrofirework.NitroFireworkMode;

public class NitroFireworkLG extends NitroFireworkMode
{
    private final ModeSetting mode;
    private final SliderSetting inTargetValue;
    private final MultiBooleanSetting options;
    private final BooleanSetting speedPlus;
    private final BooleanSetting distanceBasedSpeed;
    
    @Override
    public String getName() {
        return "Grim";
    }
    
    public NitroFireworkLG(final Supplier<Boolean> condition) {
        this.mode = new ModeSetting("Grim boost").value("Legends Grief").values("Legends Grief", "Really World");
        this.inTargetValue = new SliderSetting("In target value").value(3.0f).range(0.1f, 6.0f).step(0.1f);
        this.options = new MultiBooleanSetting("Options").value(new BooleanSetting("Vertical boost").value(true), new BooleanSetting("Extra speed").value(false));
        this.speedPlus = new BooleanSetting("Speed plus").value(true).setVisible(() -> !this.mode.is("Custom") && this.options.isEnabled("Extra speed"));
        this.distanceBasedSpeed = new BooleanSetting("Distance based speed").value(true);
        this.addSettings(this.mode, this.inTargetValue, this.options, this.speedPlus, this.distanceBasedSpeed);
        this.getSettings().forEach(setting -> setting.setVisible(condition));
    }
    
    @Override
    public Pair<Float, Float> velocityValues() {
        boolean inTarget = false;
        final AuraModule aura = AuraModule.getInstance();
        final ElytraTargetModule elytraTarget = ElytraTargetModule.getInstance();
        class_243 targetVector = null;
        if (aura.target != null) {
            final class_238 targetBB = aura.target.method_5829();
            class_243 bestCandidate = new class_243((targetBB.field_1323 + targetBB.field_1320) / 2.0, (targetBB.field_1322 + targetBB.field_1325) / 2.0, (targetBB.field_1321 + targetBB.field_1324) / 2.0);
            if (aura.target.method_6128() && elytraTarget.isEnabled()) {
                final class_243 predictedPos = elytraTarget.elytraRotationProcessor.getPredictedPos(aura.target);
                if (predictedPos != null) {
                    bestCandidate = predictedPos;
                }
                if (this.distanceBasedSpeed.getValue()) {
                    bestCandidate.method_1019(aura.target.method_18798().method_1021(0.5));
                }
            }
            targetVector = bestCandidate;
            if (NitroFireworkLG.mc.field_1724.method_33571().method_1022(bestCandidate) < this.inTargetValue.getValue()) {
                inTarget = true;
            }
        }
        final Rotation rotation = RotationManager.getInstance().getCurrentRotation();
        final float yaw = (rotation != null) ? rotation.getYaw() : NitroFireworkLG.mc.field_1724.method_36454();
        final float pitch = (rotation != null) ? rotation.getPitch() : NitroFireworkLG.mc.field_1724.method_36455();
        float speed = 1.615f;
        final int[] yawAngles = { 45, 135, 225, 315 };
        final int[][] yawRanges = { { 13, 14, 18, 19, 20, 22, 23, 25, 26, 29 }, { 13, 14, 17, 18, 19, 23, 24, 28 }, { 13, 14, 17, 18, 19, 23, 24, 28 }, { 13, 14, 17, 18, 19, 23, 24, 28 } };
        float[][] yawSpeeds;
        if (this.mode.is("Legends Grief")) {
            yawSpeeds = new float[][] { { 2.0255f, 2.065f, 2.045f, 2.035f, 2.025f, 1.965f, 1.78f, 1.76f, 1.73f, 1.72f, 1.7f }, { 2.0255f, 2.065f, 2.045f, 2.035f, 2.025f, 1.965f, 1.77f, 1.75f, 1.7f }, { 2.0255f, 2.065f, 2.045f, 2.035f, 2.025f, 1.965f, 1.77f, 1.75f, 1.7f }, { 2.0255f, 2.065f, 2.045f, 2.035f, 2.025f, 1.965f, 1.77f, 1.75f, 1.7f } };
        }
        else {
            yawSpeeds = new float[][] { { 1.805f, 1.805f, 1.87f, 1.85f, 1.83f, 1.8f, 1.78f, 1.76f, 1.73f, 1.72f, 1.7f }, { 1.805f, 1.805f, 1.87f, 1.85f, 1.82f, 1.8f, 1.77f, 1.75f, 1.7f }, { 1.805f, 1.805f, 1.87f, 1.85f, 1.82f, 1.8f, 1.77f, 1.75f, 1.7f }, { 1.805f, 1.805f, 1.87f, 1.85f, 1.82f, 1.8f, 1.77f, 1.75f, 1.7f } };
        }
        for (int i = 0; i < yawAngles.length; ++i) {
            final int currentYaw = yawAngles[i];
            final int[] ranges = yawRanges[i];
            final float[] speeds = yawSpeeds[i];
            if (this.isYawInRange(yaw, (float)currentYaw, (float)ranges[0])) {
                if (pitch >= 1.0f && pitch <= 90.0f) {
                    speed = speeds[0];
                }
                else {
                    speed = speeds[1];
                }
            }
            for (int j = 1; j < ranges.length; ++j) {
                if (this.isYawInRange(yaw, (float)currentYaw, (float)ranges[j]) && !this.isYawInRange(yaw, (float)currentYaw, (float)ranges[j - 1])) {
                    speed = speeds[j + 1];
                }
            }
        }
        if (pitch <= -30.0f || pitch >= 30.0f) {
            speed = 1.615f;
        }
        if (pitch <= -80.0f || pitch >= 80.0f) {
            speed = 1.715f;
        }
        final boolean isDiagonalYaw = this.isYawInRange(yaw, 45.0f, 20.0f) || this.isYawInRange(yaw, 135.0f, 20.0f) || this.isYawInRange(yaw, 225.0f, 20.0f) || this.isYawInRange(yaw, 315.0f, 20.0f);
        if (!isDiagonalYaw && ((pitch >= 15.0f && pitch <= 35.0f) || (pitch <= -15.0f && pitch >= -35.0f))) {
            speed = 1.7f;
        }
        if (this.options.isEnabled("Extra speed") && ((pitch >= 35.0f && pitch <= 60.0f) || (pitch <= -35.0f && pitch >= -52.0f))) {
            if (this.speedPlus.getValue()) {
                speed = 2.15f;
            }
            else {
                speed = 1.95f;
            }
        }
        float targetSpeed = 1.5f;
        if (this.distanceBasedSpeed.getValue() && targetVector != null) {
            final float distanceFactor = (float)(NitroFireworkLG.mc.field_1724.method_33571().method_1022(targetVector) / AuraModule.getInstance().getAttackDistance());
            targetSpeed = Math.max(1.3f, speed * distanceFactor);
        }
        final float speedXZ = inTarget ? targetSpeed : speed;
        return new Pair<Float, Float>(speedXZ, this.options.isEnabled("Vertical boost") ? speedXZ : 1.5f);
    }
    
    public boolean isYawInRange(float yaw, float firstValue, final float radiusValue) {
        yaw = (yaw % 360.0f + 360.0f) % 360.0f;
        firstValue = (firstValue % 360.0f + 360.0f) % 360.0f;
        final float minValue = (firstValue - radiusValue + 360.0f) % 360.0f;
        final float maxValue = (firstValue + radiusValue) % 360.0f;
        if (minValue < maxValue) {
            return yaw >= minValue && yaw <= maxValue;
        }
        return yaw >= minValue || yaw <= maxValue;
    }
}
