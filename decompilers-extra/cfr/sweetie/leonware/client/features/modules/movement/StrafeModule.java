/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1293
 *  net.minecraft.class_1294
 */
package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.other.RotationUpdateEvent;
import sweetie.leonware.api.event.events.player.other.MovementInputEvent;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.utils.player.MoveUtil;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.utils.rotation.manager.RotationPlan;
import sweetie.leonware.api.utils.rotation.manager.RotationStrategy;
import sweetie.leonware.api.utils.task.TaskPriority;
import sweetie.leonware.client.features.modules.movement.fly.FlightModule;

@ModuleRegister(name="Strafe", category=Category.MOVEMENT)
public class StrafeModule
extends Module {
    private static final StrafeModule instance = new StrafeModule();
    private final ModeSetting mode = new ModeSetting("\u0420\u0435\u0436\u0438\u043c").values("\u041b\u0435\u0433\u0438\u0442\u043d\u044b\u0439", "MetaHvH", "Matrix7").value("\u041b\u0435\u0433\u0438\u0442\u043d\u044b\u0439");

    public StrafeModule() {
        this.addSettings(this.mode);
    }

    @Override
    public void onEvent() {
        EventListener rotationUpdateEvent = RotationUpdateEvent.getInstance().subscribe(new Listener<RotationUpdateEvent>(event -> {
            if (!this.mode.is("\u041b\u0435\u0433\u0438\u0442\u043d\u044b\u0439")) {
                return;
            }
            RotationManager.getInstance().addRotation(new Rotation(StrafeModule.mc.field_1724.method_36454() + this.dony(), StrafeModule.mc.field_1724.method_36455()), RotationStrategy.TARGET, TaskPriority.LOWEST, this);
        }));
        EventListener movementInputEvent = MovementInputEvent.getInstance().subscribe(new Listener<MovementInputEvent.MovementInputEventData>(event -> {
            if (!this.mode.is("\u041b\u0435\u0433\u0438\u0442\u043d\u044b\u0439")) {
                return;
            }
            RotationPlan currentPlan = RotationManager.getInstance().getCurrentRotationPlan();
            if (currentPlan != null && currentPlan.provider() != this) {
                return;
            }
            boolean w = MoveUtil.w();
            boolean s = MoveUtil.s();
            boolean a = MoveUtil.a();
            boolean d = MoveUtil.d();
            if (w && s) {
                s = false;
                w = false;
            }
            if (a && d) {
                d = false;
                a = false;
            }
            event.getDirectionalInput().setLeft(false);
            event.getDirectionalInput().setRight(false);
            event.getDirectionalInput().setBackwards(false);
            event.getDirectionalInput().setForwards(w || s || a || d);
        }));
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (this.mode.is("MetaHvH")) {
                this.handleMetaHvH();
            } else if (this.mode.is("Matrix7")) {
                this.handleMatrix7();
            }
        }));
        this.addEvents(rotationUpdateEvent, movementInputEvent, updateEvent);
    }

    private void handleMetaHvH() {
        if (StrafeModule.mc.field_1724 == null) {
            return;
        }
        if (StrafeModule.mc.field_1724.method_5799() || StrafeModule.mc.field_1724.method_5681() || StrafeModule.mc.field_1724.method_5869()) {
            return;
        }
        if (FlightModule.getInstance().isVanillaFlying()) {
            return;
        }
        if (!StrafeModule.mc.field_1724.method_6128() && MoveUtil.isMoving()) {
            float motion = 0.19f;
            class_1293 speedEffect = StrafeModule.mc.field_1724.method_6112(class_1294.field_5904);
            if (speedEffect != null) {
                int amplifier = speedEffect.method_5578();
                switch (amplifier) {
                    case 0: {
                        motion = 0.25f;
                        break;
                    }
                    case 1: {
                        motion = 0.37f;
                        break;
                    }
                    case 2: {
                        motion = 0.46f;
                        break;
                    }
                    case 3: {
                        motion = 0.7f;
                        break;
                    }
                    default: {
                        motion = 0.75f + (float)(amplifier - 3) * 0.05f;
                    }
                }
            }
            if (StrafeModule.mc.field_1690.field_1903.method_1434()) {
                motion += 0.1f;
            }
            MoveUtil.setSpeed(motion);
        }
    }

    private void handleMatrix7() {
        if (MoveUtil.isMoving()) {
            if (StrafeModule.mc.field_1724.method_24828()) {
                StrafeModule.mc.field_1724.method_18800(StrafeModule.mc.field_1724.method_18798().field_1352, 0.419652, StrafeModule.mc.field_1724.method_18798().field_1350);
                MoveUtil.strafe();
            } else {
                double motionSquared = StrafeModule.mc.field_1724.method_18798().field_1352 * StrafeModule.mc.field_1724.method_18798().field_1352 + StrafeModule.mc.field_1724.method_18798().field_1350 * StrafeModule.mc.field_1724.method_18798().field_1350;
                if (motionSquared < 0.04) {
                    MoveUtil.strafe();
                }
            }
        }
    }

    private float dony() {
        boolean w = MoveUtil.w();
        boolean s = MoveUtil.s();
        boolean a = MoveUtil.a();
        boolean d = MoveUtil.d();
        if (w && s) {
            w = false;
            s = false;
        }
        if (a && d) {
            a = false;
            d = false;
        }
        if (w) {
            if (a) {
                return -45.0f;
            }
            if (d) {
                return 45.0f;
            }
            return 0.0f;
        }
        if (s) {
            if (a) {
                return -135.0f;
            }
            if (d) {
                return 135.0f;
            }
            return 180.0f;
        }
        if (a) {
            return -90.0f;
        }
        if (d) {
            return 90.0f;
        }
        return 0.0f;
    }

    @Generated
    public static StrafeModule getInstance() {
        return instance;
    }
}

