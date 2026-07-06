/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_3532
 */
package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import net.minecraft.class_3532;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.system.backend.SharedClass;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.utils.rotation.manager.RotationPlan;
import sweetie.leonware.client.features.modules.combat.AuraModule;

@ModuleRegister(name="Move Fix", category=Category.MOVEMENT)
public class MoveFixModule
extends Module {
    private static final MoveFixModule instance = new MoveFixModule();
    private final ModeSetting mode = new ModeSetting("Mode").value("Focus").values("Focus", "Free");
    public final BooleanSetting targeting = new BooleanSetting("Targeting").value(true);
    private final ModeSetting renderRotation = new ModeSetting("Render Rotation").value("Server").values("Server", "Fake");
    private final ModeSetting fakeMode = new ModeSetting("Fake Mode").value("Spin").values("Spin", "360Flick").setVisible(() -> this.renderRotation.is("Fake"));
    private final SliderSetting spinSpeed = new SliderSetting("Spin Speed").value(Float.valueOf(10.0f)).range(1.0f, 50.0f).step(1.0f).setVisible(() -> this.renderRotation.is("Fake") && this.fakeMode.is("Spin"));
    private final ModeSetting flickTrigger = new ModeSetting("Flick Trigger").value("On Attack").values("On Attack", "Before Attack").setVisible(() -> this.renderRotation.is("Fake") && this.fakeMode.is("360Flick"));
    private final SliderSetting flickEveryN = new SliderSetting("Flick Every N").value(Float.valueOf(1.0f)).range(1.0f, 10.0f).step(1.0f).setVisible(() -> this.renderRotation.is("Fake") && this.fakeMode.is("360Flick"));
    private final SliderSetting flickSpeed = new SliderSetting("Flick Speed").value(Float.valueOf(1.0f)).range(0.3f, 3.0f).step(0.1f).setVisible(() -> this.renderRotation.is("Fake") && this.fakeMode.is("360Flick"));
    private final BooleanSetting smoothFlick = new BooleanSetting("Smooth Flick").value(true).setVisible(() -> this.renderRotation.is("Fake") && this.fakeMode.is("360Flick"));
    private final BooleanSetting onlyWithAura = new BooleanSetting("Only with Aura").value(false).setVisible(() -> this.renderRotation.is("Fake"));
    private static float spinYaw = 0.0f;
    private static long lastSpinTime = System.currentTimeMillis();
    private static boolean flickActive = false;
    private static float flickStartYaw = 0.0f;
    private static long flickStartTime = 0L;
    private static long lastFlickTriggerTime = 0L;
    private static int attackCounter = 0;

    public MoveFixModule() {
        this.addSettings(this.mode, this.targeting, this.renderRotation, this.fakeMode, this.spinSpeed, this.flickTrigger, this.flickEveryN, this.flickSpeed, this.smoothFlick, this.onlyWithAura);
    }

    @Override
    public void onEvent() {
    }

    @Override
    public void onDisable() {
        spinYaw = 0.0f;
        flickActive = false;
        attackCounter = 0;
        lastFlickTriggerTime = 0L;
    }

    public static boolean isTargeting() {
        RotationManager rotationManager = RotationManager.getInstance();
        RotationPlan plan = rotationManager.getCurrentRotationPlan();
        return plan != null && plan.provider() instanceof AuraModule && (Boolean)MoveFixModule.instance.targeting.getValue() != false;
    }

    public static boolean enabled() {
        return instance.isEnabled();
    }

    public static boolean isFree() {
        return MoveFixModule.instance.mode.is("Free");
    }

    public static boolean shouldUseFakeRotation() {
        if (!instance.isEnabled()) {
            return false;
        }
        if (!MoveFixModule.instance.renderRotation.is("Fake")) {
            return false;
        }
        if (((Boolean)MoveFixModule.instance.onlyWithAura.getValue()).booleanValue()) {
            return AuraModule.getInstance().target != null;
        }
        return true;
    }

    public static Rotation getFakeRotation() {
        if (!MoveFixModule.shouldUseFakeRotation()) {
            return null;
        }
        if (MoveFixModule.instance.fakeMode.is("Spin")) {
            return MoveFixModule.getSpinRotation();
        }
        if (MoveFixModule.instance.fakeMode.is("360Flick")) {
            return MoveFixModule.get360FlickRotation();
        }
        return null;
    }

    private static Rotation getSpinRotation() {
        long currentTime = System.currentTimeMillis();
        float deltaTime = (float)(currentTime - lastSpinTime) / 1000.0f;
        lastSpinTime = currentTime;
        spinYaw += ((Float)MoveFixModule.instance.spinSpeed.getValue()).floatValue() * deltaTime * 20.0f;
        spinYaw = class_3532.method_15393((float)spinYaw);
        float pitch = SharedClass.player() != null ? SharedClass.player().method_36455() : 0.0f;
        return new Rotation(spinYaw, pitch);
    }

    private static Rotation get360FlickRotation() {
        if (!AuraModule.getInstance().isEnabled() || AuraModule.getInstance().target == null) {
            flickActive = false;
            attackCounter = 0;
            float yaw = SharedClass.player() != null ? SharedClass.player().method_36454() : 0.0f;
            float pitch = SharedClass.player() != null ? SharedClass.player().method_36455() : 0.0f;
            return new Rotation(yaw, pitch);
        }
        long currentTime = System.currentTimeMillis();
        long baseFlickDuration = 280L;
        long flickDuration = (long)((float)baseFlickDuration / ((Float)MoveFixModule.instance.flickSpeed.getValue()).floatValue());
        boolean shouldTriggerFlick = false;
        if (MoveFixModule.instance.flickTrigger.is("On Attack")) {
            long timeSinceLastClick = AuraModule.getInstance().combatExecutor.combatManager().clickScheduler().lastClickPassed();
            if (timeSinceLastClick < 50L && currentTime - lastFlickTriggerTime > flickDuration + 200L) {
                shouldTriggerFlick = true;
            }
        } else if (MoveFixModule.instance.flickTrigger.is("Before Attack") && AuraModule.getInstance().combatExecutor.combatManager().clickScheduler().willClickAt(1) && !flickActive && currentTime - lastFlickTriggerTime > flickDuration + 200L) {
            shouldTriggerFlick = true;
        }
        if (shouldTriggerFlick && (float)(++attackCounter) >= ((Float)MoveFixModule.instance.flickEveryN.getValue()).floatValue()) {
            attackCounter = 0;
            flickActive = true;
            flickStartYaw = SharedClass.player() != null ? SharedClass.player().method_36454() : 0.0f;
            flickStartTime = currentTime;
            lastFlickTriggerTime = currentTime;
        }
        if (flickActive) {
            long elapsed = currentTime - flickStartTime;
            if (elapsed >= flickDuration) {
                flickActive = false;
                float yaw = SharedClass.player() != null ? SharedClass.player().method_36454() : 0.0f;
                float pitch = SharedClass.player() != null ? SharedClass.player().method_36455() : 0.0f;
                return new Rotation(yaw, pitch);
            }
            float progress = (float)elapsed / (float)flickDuration;
            if (((Boolean)MoveFixModule.instance.smoothFlick.getValue()).booleanValue()) {
                progress = MoveFixModule.easeInOutCubic(progress);
            }
            float currentYaw = flickStartYaw + 360.0f * progress;
            currentYaw = class_3532.method_15393((float)currentYaw);
            float pitch = SharedClass.player() != null ? SharedClass.player().method_36455() : 0.0f;
            return new Rotation(currentYaw, pitch);
        }
        float yaw = SharedClass.player() != null ? SharedClass.player().method_36454() : 0.0f;
        float pitch = SharedClass.player() != null ? SharedClass.player().method_36455() : 0.0f;
        return new Rotation(yaw, pitch);
    }

    private static float easeInOutCubic(float t) {
        return t < 0.5f ? 4.0f * t * t * t : 1.0f - (float)Math.pow(-2.0f * t + 2.0f, 3.0) / 2.0f;
    }

    @Generated
    public static MoveFixModule getInstance() {
        return instance;
    }

    @Generated
    public static float getSpinYaw() {
        return spinYaw;
    }
}

