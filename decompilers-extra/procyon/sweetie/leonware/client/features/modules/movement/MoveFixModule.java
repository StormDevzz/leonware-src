// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import sweetie.leonware.api.system.backend.SharedClass;
import net.minecraft.class_3532;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationPlan;
import sweetie.leonware.client.features.modules.combat.AuraModule;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Move Fix", category = Category.MOVEMENT)
public class MoveFixModule extends Module
{
    private static final MoveFixModule instance;
    private final ModeSetting mode;
    public final BooleanSetting targeting;
    private final ModeSetting renderRotation;
    private final ModeSetting fakeMode;
    private final SliderSetting spinSpeed;
    private final ModeSetting flickTrigger;
    private final SliderSetting flickEveryN;
    private final SliderSetting flickSpeed;
    private final BooleanSetting smoothFlick;
    private final BooleanSetting onlyWithAura;
    private static float spinYaw;
    private static long lastSpinTime;
    private static boolean flickActive;
    private static float flickStartYaw;
    private static long flickStartTime;
    private static long lastFlickTriggerTime;
    private static int attackCounter;
    
    public MoveFixModule() {
        this.mode = new ModeSetting("Mode").value("Focus").values("Focus", "Free");
        this.targeting = new BooleanSetting("Targeting").value(true);
        this.renderRotation = new ModeSetting("Render Rotation").value("Server").values("Server", "Fake");
        this.fakeMode = new ModeSetting("Fake Mode").value("Spin").values("Spin", "360Flick").setVisible(() -> this.renderRotation.is("Fake"));
        this.spinSpeed = new SliderSetting("Spin Speed").value(10.0f).range(1.0f, 50.0f).step(1.0f).setVisible(() -> this.renderRotation.is("Fake") && this.fakeMode.is("Spin"));
        this.flickTrigger = new ModeSetting("Flick Trigger").value("On Attack").values("On Attack", "Before Attack").setVisible(() -> this.renderRotation.is("Fake") && this.fakeMode.is("360Flick"));
        this.flickEveryN = new SliderSetting("Flick Every N").value(1.0f).range(1.0f, 10.0f).step(1.0f).setVisible(() -> this.renderRotation.is("Fake") && this.fakeMode.is("360Flick"));
        this.flickSpeed = new SliderSetting("Flick Speed").value(1.0f).range(0.3f, 3.0f).step(0.1f).setVisible(() -> this.renderRotation.is("Fake") && this.fakeMode.is("360Flick"));
        this.smoothFlick = new BooleanSetting("Smooth Flick").value(true).setVisible(() -> this.renderRotation.is("Fake") && this.fakeMode.is("360Flick"));
        this.onlyWithAura = new BooleanSetting("Only with Aura").value(false).setVisible(() -> this.renderRotation.is("Fake"));
        this.addSettings(this.mode, this.targeting, this.renderRotation, this.fakeMode, this.spinSpeed, this.flickTrigger, this.flickEveryN, this.flickSpeed, this.smoothFlick, this.onlyWithAura);
    }
    
    @Override
    public void onEvent() {
    }
    
    @Override
    public void onDisable() {
        MoveFixModule.spinYaw = 0.0f;
        MoveFixModule.flickActive = false;
        MoveFixModule.attackCounter = 0;
        MoveFixModule.lastFlickTriggerTime = 0L;
    }
    
    public static boolean isTargeting() {
        final RotationManager rotationManager = RotationManager.getInstance();
        final RotationPlan plan = rotationManager.getCurrentRotationPlan();
        return plan != null && plan.provider() instanceof AuraModule && MoveFixModule.instance.targeting.getValue();
    }
    
    public static boolean enabled() {
        return MoveFixModule.instance.isEnabled();
    }
    
    public static boolean isFree() {
        return MoveFixModule.instance.mode.is("Free");
    }
    
    public static boolean shouldUseFakeRotation() {
        return MoveFixModule.instance.isEnabled() && MoveFixModule.instance.renderRotation.is("Fake") && (!MoveFixModule.instance.onlyWithAura.getValue() || AuraModule.getInstance().target != null);
    }
    
    public static Rotation getFakeRotation() {
        if (!shouldUseFakeRotation()) {
            return null;
        }
        if (MoveFixModule.instance.fakeMode.is("Spin")) {
            return getSpinRotation();
        }
        if (MoveFixModule.instance.fakeMode.is("360Flick")) {
            return get360FlickRotation();
        }
        return null;
    }
    
    private static Rotation getSpinRotation() {
        final long currentTime = System.currentTimeMillis();
        final float deltaTime = (currentTime - MoveFixModule.lastSpinTime) / 1000.0f;
        MoveFixModule.lastSpinTime = currentTime;
        MoveFixModule.spinYaw += MoveFixModule.instance.spinSpeed.getValue() * deltaTime * 20.0f;
        MoveFixModule.spinYaw = class_3532.method_15393(MoveFixModule.spinYaw);
        final float pitch = (SharedClass.player() != null) ? SharedClass.player().method_36455() : 0.0f;
        return new Rotation(MoveFixModule.spinYaw, pitch);
    }
    
    private static Rotation get360FlickRotation() {
        if (!AuraModule.getInstance().isEnabled() || AuraModule.getInstance().target == null) {
            MoveFixModule.flickActive = false;
            MoveFixModule.attackCounter = 0;
            final float yaw = (SharedClass.player() != null) ? SharedClass.player().method_36454() : 0.0f;
            final float pitch = (SharedClass.player() != null) ? SharedClass.player().method_36455() : 0.0f;
            return new Rotation(yaw, pitch);
        }
        final long currentTime = System.currentTimeMillis();
        final long baseFlickDuration = 280L;
        final long flickDuration = (long)(baseFlickDuration / MoveFixModule.instance.flickSpeed.getValue());
        boolean shouldTriggerFlick = false;
        if (MoveFixModule.instance.flickTrigger.is("On Attack")) {
            final long timeSinceLastClick = AuraModule.getInstance().combatExecutor.combatManager().clickScheduler().lastClickPassed();
            if (timeSinceLastClick < 50L && currentTime - MoveFixModule.lastFlickTriggerTime > flickDuration + 200L) {
                shouldTriggerFlick = true;
            }
        }
        else if (MoveFixModule.instance.flickTrigger.is("Before Attack") && AuraModule.getInstance().combatExecutor.combatManager().clickScheduler().willClickAt(1) && !MoveFixModule.flickActive && currentTime - MoveFixModule.lastFlickTriggerTime > flickDuration + 200L) {
            shouldTriggerFlick = true;
        }
        if (shouldTriggerFlick) {
            ++MoveFixModule.attackCounter;
            if (MoveFixModule.attackCounter >= MoveFixModule.instance.flickEveryN.getValue()) {
                MoveFixModule.attackCounter = 0;
                MoveFixModule.flickActive = true;
                MoveFixModule.flickStartYaw = ((SharedClass.player() != null) ? SharedClass.player().method_36454() : 0.0f);
                MoveFixModule.flickStartTime = currentTime;
                MoveFixModule.lastFlickTriggerTime = currentTime;
            }
        }
        if (!MoveFixModule.flickActive) {
            final float yaw2 = (SharedClass.player() != null) ? SharedClass.player().method_36454() : 0.0f;
            final float pitch2 = (SharedClass.player() != null) ? SharedClass.player().method_36455() : 0.0f;
            return new Rotation(yaw2, pitch2);
        }
        final long elapsed = currentTime - MoveFixModule.flickStartTime;
        if (elapsed >= flickDuration) {
            MoveFixModule.flickActive = false;
            final float yaw3 = (SharedClass.player() != null) ? SharedClass.player().method_36454() : 0.0f;
            final float pitch3 = (SharedClass.player() != null) ? SharedClass.player().method_36455() : 0.0f;
            return new Rotation(yaw3, pitch3);
        }
        float progress = elapsed / (float)flickDuration;
        if (MoveFixModule.instance.smoothFlick.getValue()) {
            progress = easeInOutCubic(progress);
        }
        float currentYaw = MoveFixModule.flickStartYaw + 360.0f * progress;
        currentYaw = class_3532.method_15393(currentYaw);
        final float pitch4 = (SharedClass.player() != null) ? SharedClass.player().method_36455() : 0.0f;
        return new Rotation(currentYaw, pitch4);
    }
    
    private static float easeInOutCubic(final float t) {
        return (t < 0.5f) ? (4.0f * t * t * t) : (1.0f - (float)Math.pow(-2.0f * t + 2.0f, 3.0) / 2.0f);
    }
    
    @Generated
    public static MoveFixModule getInstance() {
        return MoveFixModule.instance;
    }
    
    @Generated
    public static float getSpinYaw() {
        return MoveFixModule.spinYaw;
    }
    
    static {
        instance = new MoveFixModule();
        MoveFixModule.spinYaw = 0.0f;
        MoveFixModule.lastSpinTime = System.currentTimeMillis();
        MoveFixModule.flickActive = false;
        MoveFixModule.flickStartYaw = 0.0f;
        MoveFixModule.flickStartTime = 0L;
        MoveFixModule.lastFlickTriggerTime = 0L;
        MoveFixModule.attackCounter = 0;
    }
}
