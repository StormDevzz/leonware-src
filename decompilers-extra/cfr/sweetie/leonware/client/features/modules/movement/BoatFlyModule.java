/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1690
 *  net.minecraft.class_243
 *  net.minecraft.class_3532
 */
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

@ModuleRegister(name="Boat Fly", category=Category.MOVEMENT)
public class BoatFlyModule
extends Module {
    private static final BoatFlyModule instance = new BoatFlyModule();
    private final BooleanSetting phase = new BooleanSetting("Phase").value(false);
    private final SliderSetting speed = new SliderSetting("Speed").value(Float.valueOf(1.0f)).range(0.1f, 10.0f).step(0.1f);
    private final SliderSetting verticalSpeed = new SliderSetting("Vertical Speed").value(Float.valueOf(0.5f)).range(0.1f, 5.0f).step(0.1f);
    private final BooleanSetting cancelGravity = new BooleanSetting("Cancel Gravity").value(true);
    private class_1690 boat;
    private int ticksInBoat = 0;

    public BoatFlyModule() {
        this.addSettings(this.phase, this.speed, this.verticalSpeed, this.cancelGravity);
    }

    @Override
    public void onEvent() {
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener<TickEvent>(event -> {
            if (BoatFlyModule.mc.field_1724 == null) {
                return;
            }
            if (!BoatFlyModule.mc.field_1724.method_5765() || !(BoatFlyModule.mc.field_1724.method_5854() instanceof class_1690)) {
                this.boat = null;
                this.ticksInBoat = 0;
                return;
            }
            this.boat = (class_1690)BoatFlyModule.mc.field_1724.method_5854();
            ++this.ticksInBoat;
            if (this.ticksInBoat < 5) {
                return;
            }
            this.handleControl();
        }));
        this.addEvents(tickEvent);
    }

    private void handleControl() {
        if (this.boat == null) {
            return;
        }
        float yaw = BoatFlyModule.mc.field_1724.method_36454();
        this.boat.method_36456(yaw);
        this.boat.method_18800(0.0, 0.0, 0.0);
        double motionX = 0.0;
        double motionY = 0.0;
        double motionZ = 0.0;
        motionY = (Boolean)this.cancelGravity.getValue() != false ? 0.0 : -0.04;
        if (BoatFlyModule.mc.field_1690.field_1903.method_1434()) {
            motionY = ((Float)this.verticalSpeed.getValue()).floatValue();
        } else if (BoatFlyModule.mc.field_1690.field_1832.method_1434() && !this.boat.method_24828()) {
            motionY = -((Float)this.verticalSpeed.getValue()).floatValue();
        }
        if (BoatFlyModule.mc.field_1690.field_1894.method_1434() || BoatFlyModule.mc.field_1690.field_1881.method_1434() || BoatFlyModule.mc.field_1690.field_1913.method_1434() || BoatFlyModule.mc.field_1690.field_1849.method_1434()) {
            float rad = (float)Math.toRadians(yaw);
            float sin = -class_3532.method_15374((float)rad);
            float cos = class_3532.method_15362((float)rad);
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
            motionX = (forward * (double)sin + strafe * (double)cos) * (double)((Float)this.speed.getValue()).floatValue();
            motionZ = (forward * (double)cos - strafe * (double)sin) * (double)((Float)this.speed.getValue()).floatValue();
        }
        if (((Boolean)this.phase.getValue()).booleanValue()) {
            this.boat.field_5960 = true;
            BoatFlyModule.mc.field_1724.field_5960 = true;
        } else {
            this.boat.field_5960 = false;
            BoatFlyModule.mc.field_1724.field_5960 = false;
        }
        class_243 pos = this.boat.method_19538();
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
        return instance;
    }
}

