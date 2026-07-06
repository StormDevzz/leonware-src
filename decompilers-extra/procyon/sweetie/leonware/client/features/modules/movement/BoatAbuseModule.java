// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import java.util.Iterator;
import net.minecraft.class_238;
import net.minecraft.class_1690;
import net.minecraft.class_1297;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.setting.Setting;
import java.util.function.Supplier;
import java.util.Objects;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Boat Collision", category = Category.MOVEMENT)
public class BoatAbuseModule extends Module
{
    private static final BoatAbuseModule instance;
    private final ModeSetting mode;
    private final BooleanSetting horizontalSpeedEnabled;
    private final SliderSetting horizontalSpeed;
    private final BooleanSetting verticalSpeedEnabled;
    private final SliderSetting verticalSpeed;
    private boolean wasInVehicle;
    
    public BoatAbuseModule() {
        this.mode = new ModeSetting("\u0420\u0435\u0436\u0438\u043c").values("\u0412 \u043b\u043e\u0434\u043a\u0435", "\u041f\u0440\u0438 \u0441\u0442\u043e\u043b\u043a\u043d\u043e\u0432\u0435\u043d\u0438\u0438").value("\u0412 \u043b\u043e\u0434\u043a\u0435");
        this.horizontalSpeedEnabled = new BooleanSetting("\u0413\u043e\u0440\u0438\u0437\u043e\u043d\u0442\u0430\u043b\u044c\u043d\u0430\u044f \u0441\u043a\u043e\u0440\u043e\u0441\u0442\u044c").value(true);
        final SliderSetting step = new SliderSetting("\u0417\u043d\u0430\u0447\u0435\u043d\u0438\u0435").value(2.0f).range(0.1f, 10.0f).step(0.1f);
        final BooleanSetting horizontalSpeedEnabled = this.horizontalSpeedEnabled;
        Objects.requireNonNull(horizontalSpeedEnabled);
        this.horizontalSpeed = step.setVisible((Supplier<Boolean>)horizontalSpeedEnabled::getValue);
        this.verticalSpeedEnabled = new BooleanSetting("\u0412\u0435\u0440\u0442\u0438\u043a\u0430\u043b\u044c\u043d\u0430\u044f \u0441\u043a\u043e\u0440\u043e\u0441\u0442\u044c").value(true);
        final SliderSetting step2 = new SliderSetting("\u0417\u043d\u0430\u0447\u0435\u043d\u0438\u0435").value(1.0f).range(0.1f, 5.0f).step(0.1f);
        final BooleanSetting verticalSpeedEnabled = this.verticalSpeedEnabled;
        Objects.requireNonNull(verticalSpeedEnabled);
        this.verticalSpeed = step2.setVisible((Supplier<Boolean>)verticalSpeedEnabled::getValue);
        this.wasInVehicle = false;
        this.addSettings(this.mode, this.horizontalSpeedEnabled, this.horizontalSpeed, this.verticalSpeedEnabled, this.verticalSpeed);
    }
    
    @Override
    public void onEnable() {
        if (BoatAbuseModule.mc == null || BoatAbuseModule.mc.field_1724 == null) {
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
        final EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (BoatAbuseModule.mc.field_1724 == null || BoatAbuseModule.mc.field_1687 == null) {
                return;
            }
            else {
                final String currentMode = this.mode.getValue();
                if (currentMode.equals("\u0412 \u043b\u043e\u0434\u043a\u0435")) {
                    this.handleInVehicleMode();
                }
                else if (currentMode.equals("\u041f\u0440\u0438 \u0441\u0442\u043e\u043b\u043a\u043d\u043e\u0432\u0435\u043d\u0438\u0438")) {
                    this.handleOnCollisionMode();
                }
                return;
            }
        }));
        this.addEvents(updateEvent);
    }
    
    private void handleInVehicleMode() {
        final boolean isInVehicle = BoatAbuseModule.mc.field_1724.method_5765();
        if (this.wasInVehicle && !isInVehicle) {
            this.applyBoost();
        }
        this.wasInVehicle = isInVehicle;
    }
    
    private void handleOnCollisionMode() {
        if (this.hasBoatNearby()) {
            final double[] motion = this.getForwardMotion(((boolean)this.horizontalSpeedEnabled.getValue()) ? ((float)this.horizontalSpeed.getValue()) : 0.0f);
            final double verticalMotion = (double)(this.verticalSpeedEnabled.getValue() ? this.verticalSpeed.getValue() : 0.0);
            BoatAbuseModule.mc.field_1724.method_5762(motion[0], verticalMotion, motion[1]);
        }
    }
    
    private void applyBoost() {
        final double angle = Math.toRadians(BoatAbuseModule.mc.field_1724.method_36454());
        final double sinAngle = Math.sin(angle);
        final double cosAngle = Math.cos(angle);
        final double horizontalVelocity = (double)(this.horizontalSpeedEnabled.getValue() ? this.horizontalSpeed.getValue() : 0.0);
        final double verticalVelocity = (double)(this.verticalSpeedEnabled.getValue() ? this.verticalSpeed.getValue() : 0.0);
        BoatAbuseModule.mc.field_1724.method_18800(-sinAngle * horizontalVelocity, verticalVelocity, cosAngle * horizontalVelocity);
    }
    
    private boolean hasBoatNearby() {
        if (BoatAbuseModule.mc.field_1687 != null && BoatAbuseModule.mc.field_1724 != null) {
            final class_238 checkBox = BoatAbuseModule.mc.field_1724.method_5829().method_1014(0.899);
            for (final class_1297 entity : BoatAbuseModule.mc.field_1687.method_18112()) {
                if (entity != null && entity instanceof class_1690 && checkBox.method_994(entity.method_5829())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private double[] getForwardMotion(final float speed) {
        if (BoatAbuseModule.mc.field_1724 == null) {
            return new double[] { 0.0, 0.0 };
        }
        final double yaw = Math.toRadians(BoatAbuseModule.mc.field_1724.method_36454());
        final double sinYaw = Math.sin(yaw);
        final double cosYaw = Math.cos(yaw);
        return new double[] { -sinYaw * speed, cosYaw * speed };
    }
    
    @Generated
    public static BoatAbuseModule getInstance() {
        return BoatAbuseModule.instance;
    }
    
    static {
        instance = new BoatAbuseModule();
    }
}
