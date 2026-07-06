package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import net.minecraft.class_1690;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/BoatFlyModule.class */
@ModuleRegister(name = "Boat Fly", category = Category.MOVEMENT)
public class BoatFlyModule extends Module {
    private static final BoatFlyModule instance = new BoatFlyModule();
    private class_1690 boat;
    private final BooleanSetting phase = new BooleanSetting("Phase").value((Boolean) false);
    private final SliderSetting speed = new SliderSetting("Speed").value(Float.valueOf(1.0f)).range(0.1f, 10.0f).step(0.1f);
    private final SliderSetting verticalSpeed = new SliderSetting("Vertical Speed").value(Float.valueOf(0.5f)).range(0.1f, 5.0f).step(0.1f);
    private final BooleanSetting cancelGravity = new BooleanSetting("Cancel Gravity").value((Boolean) true);
    private int ticksInBoat = 0;

    @Generated
    public static BoatFlyModule getInstance() {
        return instance;
    }

    public BoatFlyModule() {
        addSettings(this.phase, this.speed, this.verticalSpeed, this.cancelGravity);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1724 == null) {
                return;
            }
            if (!mc.field_1724.method_5765() || !(mc.field_1724.method_5854() instanceof class_1690)) {
                this.boat = null;
                this.ticksInBoat = 0;
                return;
            }
            this.boat = mc.field_1724.method_5854();
            this.ticksInBoat++;
            if (this.ticksInBoat < 5) {
                return;
            }
            handleControl();
        }));
        addEvents(tickEvent);
    }

    private void handleControl() {
        double motionY;
        if (this.boat == null) {
            return;
        }
        float yaw = mc.field_1724.method_36454();
        this.boat.method_36456(yaw);
        this.boat.method_18800(0.0d, 0.0d, 0.0d);
        double motionX = 0.0d;
        double motionZ = 0.0d;
        if (this.cancelGravity.getValue().booleanValue()) {
            motionY = 0.0d;
        } else {
            motionY = -0.04d;
        }
        if (mc.field_1690.field_1903.method_1434()) {
            motionY = this.verticalSpeed.getValue().floatValue();
        } else if (mc.field_1690.field_1832.method_1434() && !this.boat.method_24828()) {
            motionY = -this.verticalSpeed.getValue().floatValue();
        }
        if (mc.field_1690.field_1894.method_1434() || mc.field_1690.field_1881.method_1434() || mc.field_1690.field_1913.method_1434() || mc.field_1690.field_1849.method_1434()) {
            float rad = (float) Math.toRadians(yaw);
            float sin = -class_3532.method_15374(rad);
            float cos = class_3532.method_15362(rad);
            double forward = 0.0d;
            double strafe = 0.0d;
            if (mc.field_1690.field_1894.method_1434()) {
                forward = 1.0d;
            }
            if (mc.field_1690.field_1881.method_1434()) {
                forward = -1.0d;
            }
            if (mc.field_1690.field_1913.method_1434()) {
                strafe = 1.0d;
            }
            if (mc.field_1690.field_1849.method_1434()) {
                strafe = -1.0d;
            }
            motionX = ((forward * ((double) sin)) + (strafe * ((double) cos))) * ((double) this.speed.getValue().floatValue());
            motionZ = ((forward * ((double) cos)) - (strafe * ((double) sin))) * ((double) this.speed.getValue().floatValue());
        }
        if (this.phase.getValue().booleanValue()) {
            this.boat.field_5960 = true;
            mc.field_1724.field_5960 = true;
        } else {
            this.boat.field_5960 = false;
            mc.field_1724.field_5960 = false;
        }
        class_243 pos = this.boat.method_19538();
        this.boat.method_5814(pos.field_1352 + motionX, pos.field_1351 + motionY, pos.field_1350 + motionZ);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        if (this.boat != null) {
            this.boat.field_5960 = false;
        }
        if (mc.field_1724 != null) {
            mc.field_1724.field_5960 = false;
        }
        this.boat = null;
        this.ticksInBoat = 0;
    }
}
