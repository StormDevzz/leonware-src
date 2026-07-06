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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/MoveFixModule.class */
@ModuleRegister(name = "Move Fix", category = Category.MOVEMENT)
public class MoveFixModule extends Module {
    private final ModeSetting mode = new ModeSetting("Mode").value("Focus").values("Focus", "Free");
    public final BooleanSetting targeting = new BooleanSetting("Targeting").value((Boolean) true);
    private final ModeSetting renderRotation = new ModeSetting("Render Rotation").value("Server").values("Server", "Fake");
    private final ModeSetting fakeMode = new ModeSetting("Fake Mode").value("Spin").values("Spin", "360Flick").setVisible(() -> {
        return Boolean.valueOf(this.renderRotation.is("Fake"));
    });
    private final SliderSetting spinSpeed = new SliderSetting("Spin Speed").value(Float.valueOf(10.0f)).range(1.0f, 50.0f).step(1.0f).setVisible(() -> {
        return Boolean.valueOf(this.renderRotation.is("Fake") && this.fakeMode.is("Spin"));
    });
    private final ModeSetting flickTrigger = new ModeSetting("Flick Trigger").value("On Attack").values("On Attack", "Before Attack").setVisible(() -> {
        return Boolean.valueOf(this.renderRotation.is("Fake") && this.fakeMode.is("360Flick"));
    });
    private final SliderSetting flickEveryN = new SliderSetting("Flick Every N").value(Float.valueOf(1.0f)).range(1.0f, 10.0f).step(1.0f).setVisible(() -> {
        return Boolean.valueOf(this.renderRotation.is("Fake") && this.fakeMode.is("360Flick"));
    });
    private final SliderSetting flickSpeed = new SliderSetting("Flick Speed").value(Float.valueOf(1.0f)).range(0.3f, 3.0f).step(0.1f).setVisible(() -> {
        return Boolean.valueOf(this.renderRotation.is("Fake") && this.fakeMode.is("360Flick"));
    });
    private final BooleanSetting smoothFlick = new BooleanSetting("Smooth Flick").value((Boolean) true).setVisible(() -> {
        return Boolean.valueOf(this.renderRotation.is("Fake") && this.fakeMode.is("360Flick"));
    });
    private final BooleanSetting onlyWithAura = new BooleanSetting("Only with Aura").value((Boolean) false).setVisible(() -> {
        return Boolean.valueOf(this.renderRotation.is("Fake"));
    });
    private static final MoveFixModule instance = new MoveFixModule();
    private static float spinYaw = 0.0f;
    private static long lastSpinTime = System.currentTimeMillis();
    private static boolean flickActive = false;
    private static float flickStartYaw = 0.0f;
    private static long flickStartTime = 0;
    private static long lastFlickTriggerTime = 0;
    private static int attackCounter = 0;

    @Generated
    public static MoveFixModule getInstance() {
        return instance;
    }

    @Generated
    public static float getSpinYaw() {
        return spinYaw;
    }

    /* JADX WARN: Type inference failed for: r1v11, types: [sweetie.leonware.api.module.setting.ModeSetting] */
    /* JADX WARN: Type inference failed for: r1v16, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v20, types: [sweetie.leonware.api.module.setting.ModeSetting] */
    /* JADX WARN: Type inference failed for: r1v25, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v30, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v33, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v36, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    public MoveFixModule() {
        addSettings(this.mode, this.targeting, this.renderRotation, this.fakeMode, this.spinSpeed, this.flickTrigger, this.flickEveryN, this.flickSpeed, this.smoothFlick, this.onlyWithAura);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        spinYaw = 0.0f;
        flickActive = false;
        attackCounter = 0;
        lastFlickTriggerTime = 0L;
    }

    public static boolean isTargeting() {
        RotationManager rotationManager = RotationManager.getInstance();
        RotationPlan plan = rotationManager.getCurrentRotationPlan();
        return plan != null && (plan.provider() instanceof AuraModule) && instance.targeting.getValue().booleanValue();
    }

    public static boolean enabled() {
        return instance.isEnabled();
    }

    public static boolean isFree() {
        return instance.mode.is("Free");
    }

    public static boolean shouldUseFakeRotation() {
        if (instance.isEnabled() && instance.renderRotation.is("Fake")) {
            return (instance.onlyWithAura.getValue().booleanValue() && AuraModule.getInstance().target == null) ? false : true;
        }
        return false;
    }

    public static Rotation getFakeRotation() {
        if (!shouldUseFakeRotation()) {
            return null;
        }
        if (instance.fakeMode.is("Spin")) {
            return getSpinRotation();
        }
        if (instance.fakeMode.is("360Flick")) {
            return get360FlickRotation();
        }
        return null;
    }

    private static Rotation getSpinRotation() {
        long currentTime = System.currentTimeMillis();
        float deltaTime = (currentTime - lastSpinTime) / 1000.0f;
        lastSpinTime = currentTime;
        spinYaw += instance.spinSpeed.getValue().floatValue() * deltaTime * 20.0f;
        spinYaw = class_3532.method_15393(spinYaw);
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
        long flickDuration = (long) (280 / instance.flickSpeed.getValue().floatValue());
        boolean shouldTriggerFlick = false;
        if (instance.flickTrigger.is("On Attack")) {
            long timeSinceLastClick = AuraModule.getInstance().combatExecutor.combatManager().clickScheduler().lastClickPassed();
            if (timeSinceLastClick < 50 && currentTime - lastFlickTriggerTime > flickDuration + 200) {
                shouldTriggerFlick = true;
            }
        } else if (instance.flickTrigger.is("Before Attack") && AuraModule.getInstance().combatExecutor.combatManager().clickScheduler().willClickAt(1) && !flickActive && currentTime - lastFlickTriggerTime > flickDuration + 200) {
            shouldTriggerFlick = true;
        }
        if (shouldTriggerFlick) {
            attackCounter++;
            if (attackCounter >= instance.flickEveryN.getValue().floatValue()) {
                attackCounter = 0;
                flickActive = true;
                flickStartYaw = SharedClass.player() != null ? SharedClass.player().method_36454() : 0.0f;
                flickStartTime = currentTime;
                lastFlickTriggerTime = currentTime;
            }
        }
        if (flickActive) {
            long elapsed = currentTime - flickStartTime;
            if (elapsed >= flickDuration) {
                flickActive = false;
                float yaw2 = SharedClass.player() != null ? SharedClass.player().method_36454() : 0.0f;
                float pitch2 = SharedClass.player() != null ? SharedClass.player().method_36455() : 0.0f;
                return new Rotation(yaw2, pitch2);
            }
            float progress = elapsed / flickDuration;
            if (instance.smoothFlick.getValue().booleanValue()) {
                progress = easeInOutCubic(progress);
            }
            float currentYaw = flickStartYaw + (360.0f * progress);
            float currentYaw2 = class_3532.method_15393(currentYaw);
            float pitch3 = SharedClass.player() != null ? SharedClass.player().method_36455() : 0.0f;
            return new Rotation(currentYaw2, pitch3);
        }
        float yaw3 = SharedClass.player() != null ? SharedClass.player().method_36454() : 0.0f;
        float pitch4 = SharedClass.player() != null ? SharedClass.player().method_36455() : 0.0f;
        return new Rotation(yaw3, pitch4);
    }

    private static float easeInOutCubic(float t) {
        return t < 0.5f ? 4.0f * t * t * t : 1.0f - (((float) Math.pow(((-2.0f) * t) + 2.0f, 3.0d)) / 2.0f);
    }
}
