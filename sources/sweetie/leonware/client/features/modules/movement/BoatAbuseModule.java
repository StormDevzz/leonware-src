package sweetie.leonware.client.features.modules.movement;

import java.util.Objects;
import lombok.Generated;
import net.minecraft.class_1297;
import net.minecraft.class_1690;
import net.minecraft.class_238;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/BoatAbuseModule.class */
@ModuleRegister(name = "Boat Collision", category = Category.MOVEMENT)
public class BoatAbuseModule extends Module {
    private static final BoatAbuseModule instance = new BoatAbuseModule();
    private final ModeSetting mode = new ModeSetting("Режим").values("В лодке", "При столкновении").value("В лодке");
    private final BooleanSetting horizontalSpeedEnabled = new BooleanSetting("Горизонтальная скорость").value((Boolean) true);
    private final SliderSetting horizontalSpeed;
    private final BooleanSetting verticalSpeedEnabled;
    private final SliderSetting verticalSpeed;
    private boolean wasInVehicle;

    @Generated
    public static BoatAbuseModule getInstance() {
        return instance;
    }

    /* JADX WARN: Type inference failed for: r1v16, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v9, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    public BoatAbuseModule() {
        SliderSetting sliderSettingStep = new SliderSetting("Значение").value(Float.valueOf(2.0f)).range(0.1f, 10.0f).step(0.1f);
        BooleanSetting booleanSetting = this.horizontalSpeedEnabled;
        Objects.requireNonNull(booleanSetting);
        this.horizontalSpeed = sliderSettingStep.setVisible(booleanSetting::getValue);
        this.verticalSpeedEnabled = new BooleanSetting("Вертикальная скорость").value((Boolean) true);
        SliderSetting sliderSettingStep2 = new SliderSetting("Значение").value(Float.valueOf(1.0f)).range(0.1f, 5.0f).step(0.1f);
        BooleanSetting booleanSetting2 = this.verticalSpeedEnabled;
        Objects.requireNonNull(booleanSetting2);
        this.verticalSpeed = sliderSettingStep2.setVisible(booleanSetting2::getValue);
        this.wasInVehicle = false;
        addSettings(this.mode, this.horizontalSpeedEnabled, this.horizontalSpeed, this.verticalSpeedEnabled, this.verticalSpeed);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onEnable() {
        if (mc == null || mc.field_1724 == null) {
            super.onEnable();
            this.wasInVehicle = false;
        } else {
            super.onEnable();
            this.wasInVehicle = mc.field_1724.method_5765();
        }
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        super.onDisable();
        this.wasInVehicle = false;
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1724 == null || mc.field_1687 == null) {
                return;
            }
            String currentMode = this.mode.getValue();
            if (currentMode.equals("В лодке")) {
                handleInVehicleMode();
            } else if (currentMode.equals("При столкновении")) {
                handleOnCollisionMode();
            }
        }));
        addEvents(updateEvent);
    }

    private void handleInVehicleMode() {
        boolean isInVehicle = mc.field_1724.method_5765();
        if (this.wasInVehicle && !isInVehicle) {
            applyBoost();
        }
        this.wasInVehicle = isInVehicle;
    }

    private void handleOnCollisionMode() {
        if (hasBoatNearby()) {
            double[] motion = getForwardMotion(this.horizontalSpeedEnabled.getValue().booleanValue() ? this.horizontalSpeed.getValue().floatValue() : 0.0f);
            double verticalMotion = this.verticalSpeedEnabled.getValue().booleanValue() ? this.verticalSpeed.getValue().floatValue() : 0.0d;
            mc.field_1724.method_5762(motion[0], verticalMotion, motion[1]);
        }
    }

    private void applyBoost() {
        double angle = Math.toRadians(mc.field_1724.method_36454());
        double sinAngle = Math.sin(angle);
        double cosAngle = Math.cos(angle);
        double horizontalVelocity = this.horizontalSpeedEnabled.getValue().booleanValue() ? this.horizontalSpeed.getValue().floatValue() : 0.0d;
        double verticalVelocity = this.verticalSpeedEnabled.getValue().booleanValue() ? this.verticalSpeed.getValue().floatValue() : 0.0d;
        mc.field_1724.method_18800((-sinAngle) * horizontalVelocity, verticalVelocity, cosAngle * horizontalVelocity);
    }

    private boolean hasBoatNearby() {
        if (mc.field_1687 != null && mc.field_1724 != null) {
            class_238 checkBox = mc.field_1724.method_5829().method_1014(0.899d);
            for (class_1297 entity : mc.field_1687.method_18112()) {
                if (entity != null && (entity instanceof class_1690) && checkBox.method_994(entity.method_5829())) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private double[] getForwardMotion(float speed) {
        if (mc.field_1724 == null) {
            return new double[]{0.0d, 0.0d};
        }
        double yaw = Math.toRadians(mc.field_1724.method_36454());
        double sinYaw = Math.sin(yaw);
        double cosYaw = Math.cos(yaw);
        return new double[]{(-sinYaw) * ((double) speed), cosYaw * ((double) speed)};
    }
}
