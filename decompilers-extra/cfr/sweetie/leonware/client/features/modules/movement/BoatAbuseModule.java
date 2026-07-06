/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1297
 *  net.minecraft.class_1690
 *  net.minecraft.class_238
 */
package sweetie.leonware.client.features.modules.movement;

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

@ModuleRegister(name="Boat Collision", category=Category.MOVEMENT)
public class BoatAbuseModule
extends Module {
    private static final BoatAbuseModule instance = new BoatAbuseModule();
    private final ModeSetting mode = new ModeSetting("\u0420\u0435\u0436\u0438\u043c").values("\u0412 \u043b\u043e\u0434\u043a\u0435", "\u041f\u0440\u0438 \u0441\u0442\u043e\u043b\u043a\u043d\u043e\u0432\u0435\u043d\u0438\u0438").value("\u0412 \u043b\u043e\u0434\u043a\u0435");
    private final BooleanSetting horizontalSpeedEnabled = new BooleanSetting("\u0413\u043e\u0440\u0438\u0437\u043e\u043d\u0442\u0430\u043b\u044c\u043d\u0430\u044f \u0441\u043a\u043e\u0440\u043e\u0441\u0442\u044c").value(true);
    private final SliderSetting horizontalSpeed = new SliderSetting("\u0417\u043d\u0430\u0447\u0435\u043d\u0438\u0435").value(Float.valueOf(2.0f)).range(0.1f, 10.0f).step(0.1f).setVisible(this.horizontalSpeedEnabled::getValue);
    private final BooleanSetting verticalSpeedEnabled = new BooleanSetting("\u0412\u0435\u0440\u0442\u0438\u043a\u0430\u043b\u044c\u043d\u0430\u044f \u0441\u043a\u043e\u0440\u043e\u0441\u0442\u044c").value(true);
    private final SliderSetting verticalSpeed = new SliderSetting("\u0417\u043d\u0430\u0447\u0435\u043d\u0438\u0435").value(Float.valueOf(1.0f)).range(0.1f, 5.0f).step(0.1f).setVisible(this.verticalSpeedEnabled::getValue);
    private boolean wasInVehicle = false;

    public BoatAbuseModule() {
        this.addSettings(this.mode, this.horizontalSpeedEnabled, this.horizontalSpeed, this.verticalSpeedEnabled, this.verticalSpeed);
    }

    @Override
    public void onEnable() {
        if (mc == null || BoatAbuseModule.mc.field_1724 == null) {
            super.onEnable();
            this.wasInVehicle = false;
            return;
        }
        super.onEnable();
        this.wasInVehicle = BoatAbuseModule.mc.field_1724.method_5765();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.wasInVehicle = false;
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (BoatAbuseModule.mc.field_1724 == null || BoatAbuseModule.mc.field_1687 == null) {
                return;
            }
            String currentMode = (String)this.mode.getValue();
            if (currentMode.equals("\u0412 \u043b\u043e\u0434\u043a\u0435")) {
                this.handleInVehicleMode();
            } else if (currentMode.equals("\u041f\u0440\u0438 \u0441\u0442\u043e\u043b\u043a\u043d\u043e\u0432\u0435\u043d\u0438\u0438")) {
                this.handleOnCollisionMode();
            }
        }));
        this.addEvents(updateEvent);
    }

    private void handleInVehicleMode() {
        boolean isInVehicle = BoatAbuseModule.mc.field_1724.method_5765();
        if (this.wasInVehicle && !isInVehicle) {
            this.applyBoost();
        }
        this.wasInVehicle = isInVehicle;
    }

    private void handleOnCollisionMode() {
        if (this.hasBoatNearby()) {
            double[] motion = this.getForwardMotion((Boolean)this.horizontalSpeedEnabled.getValue() != false ? ((Float)this.horizontalSpeed.getValue()).floatValue() : 0.0f);
            double verticalMotion = (Boolean)this.verticalSpeedEnabled.getValue() != false ? (double)((Float)this.verticalSpeed.getValue()).floatValue() : 0.0;
            BoatAbuseModule.mc.field_1724.method_5762(motion[0], verticalMotion, motion[1]);
        }
    }

    private void applyBoost() {
        double angle = Math.toRadians(BoatAbuseModule.mc.field_1724.method_36454());
        double sinAngle = Math.sin(angle);
        double cosAngle = Math.cos(angle);
        double horizontalVelocity = (Boolean)this.horizontalSpeedEnabled.getValue() != false ? (double)((Float)this.horizontalSpeed.getValue()).floatValue() : 0.0;
        double verticalVelocity = (Boolean)this.verticalSpeedEnabled.getValue() != false ? (double)((Float)this.verticalSpeed.getValue()).floatValue() : 0.0;
        BoatAbuseModule.mc.field_1724.method_18800(-sinAngle * horizontalVelocity, verticalVelocity, cosAngle * horizontalVelocity);
    }

    private boolean hasBoatNearby() {
        if (BoatAbuseModule.mc.field_1687 != null && BoatAbuseModule.mc.field_1724 != null) {
            class_238 checkBox = BoatAbuseModule.mc.field_1724.method_5829().method_1014(0.899);
            for (class_1297 entity : BoatAbuseModule.mc.field_1687.method_18112()) {
                if (entity == null || !(entity instanceof class_1690) || !checkBox.method_994(entity.method_5829())) continue;
                return true;
            }
        }
        return false;
    }

    private double[] getForwardMotion(float speed) {
        if (BoatAbuseModule.mc.field_1724 == null) {
            return new double[]{0.0, 0.0};
        }
        double yaw = Math.toRadians(BoatAbuseModule.mc.field_1724.method_36454());
        double sinYaw = Math.sin(yaw);
        double cosYaw = Math.cos(yaw);
        return new double[]{-sinYaw * (double)speed, cosYaw * (double)speed};
    }

    @Generated
    public static BoatAbuseModule getInstance() {
        return instance;
    }
}

