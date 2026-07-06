// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.module.setting.Setting;
import net.minecraft.class_1690;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Boat Fly", category = Category.MOVEMENT)
public class BoatFlyModule extends Module
{
    private static final BoatFlyModule instance;
    private final BooleanSetting phase;
    private final SliderSetting speed;
    private final SliderSetting verticalSpeed;
    private final BooleanSetting cancelGravity;
    private class_1690 boat;
    private int ticksInBoat;
    
    public BoatFlyModule() {
        this.phase = new BooleanSetting("Phase").value(false);
        this.speed = new SliderSetting("Speed").value(1.0f).range(0.1f, 10.0f).step(0.1f);
        this.verticalSpeed = new SliderSetting("Vertical Speed").value(0.5f).range(0.1f, 5.0f).step(0.1f);
        this.cancelGravity = new BooleanSetting("Cancel Gravity").value(true);
        this.ticksInBoat = 0;
        this.addSettings(this.phase, this.speed, this.verticalSpeed, this.cancelGravity);
    }
    
    @Override
    public void onEvent() {
        final EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener<TickEvent>(event -> {
            if (BoatFlyModule.mc.field_1724 == null) {
                return;
            }
            else if (!BoatFlyModule.mc.field_1724.method_5765() || !(BoatFlyModule.mc.field_1724.method_5854() instanceof class_1690)) {
                this.boat = null;
                this.ticksInBoat = 0;
                return;
            }
            else {
                this.boat = (class_1690)BoatFlyModule.mc.field_1724.method_5854();
                ++this.ticksInBoat;
                if (this.ticksInBoat < 5) {
                    return;
                }
                else {
                    this.handleControl();
                    return;
                }
            }
        }));
        this.addEvents(tickEvent);
    }
    
    private void handleControl() {
        if (this.boat == null) {
            return;
        }
        final float yaw = BoatFlyModule.mc.field_1724.method_36454();
        this.boat.method_36456(yaw);
        this.boat.method_18800(0.0, 0.0, 0.0);
        double motionX = 0.0;
        double motionY = 0.0;
        double motionZ = 0.0;
        if (this.cancelGravity.getValue()) {
            motionY = 0.0;
        }
        else {
            motionY = -0.04;
        }
        if (BoatFlyModule.mc.field_1690.field_1903.method_1434()) {
            motionY = this.verticalSpeed.getValue();
        }
        else if (BoatFlyModule.mc.field_1690.field_1832.method_1434() && !this.boat.method_24828()) {
            motionY = -this.verticalSpeed.getValue();
        }
        if (BoatFlyModule.mc.field_1690.field_1894.method_1434() || BoatFlyModule.mc.field_1690.field_1881.method_1434() || BoatFlyModule.mc.field_1690.field_1913.method_1434() || BoatFlyModule.mc.field_1690.field_1849.method_1434()) {
            final float rad = (float)Math.toRadians(yaw);
            final float sin = -class_3532.method_15374(rad);
            final float cos = class_3532.method_15362(rad);
            double forward = 0.0;
            double strafe = 0.0;
            if (BoatFlyModule.mc.field_1690.field_1894.method_1434()) {
                forward = 1.0;
            }
            if (BoatFlyModule.mc.field_1690.field_1881.method_1434()) {
                forward = -1.0;
            }
            if (BoatFlyModule.mc.field_1690.field_1913.method_1434()) {
                strafe = 1.0;
            }
            if (BoatFlyModule.mc.field_1690.field_1849.method_1434()) {
                strafe = -1.0;
            }
            motionX = (forward * sin + strafe * cos) * this.speed.getValue();
            motionZ = (forward * cos - strafe * sin) * this.speed.getValue();
        }
        if (this.phase.getValue()) {
            this.boat.field_5960 = true;
            BoatFlyModule.mc.field_1724.field_5960 = true;
        }
        else {
            this.boat.field_5960 = false;
            BoatFlyModule.mc.field_1724.field_5960 = false;
        }
        final class_243 pos = this.boat.method_19538();
        this.boat.method_5814(pos.field_1352 + motionX, pos.field_1351 + motionY, pos.field_1350 + motionZ);
    }
    
    @Override
    public void onDisable() {
        if (this.boat != null) {
            this.boat.field_5960 = false;
        }
        if (BoatFlyModule.mc.field_1724 != null) {
            BoatFlyModule.mc.field_1724.field_5960 = false;
        }
        this.boat = null;
        this.ticksInBoat = 0;
    }
    
    @Generated
    public static BoatFlyModule getInstance() {
        return BoatFlyModule.instance;
    }
    
    static {
        instance = new BoatFlyModule();
    }
}
