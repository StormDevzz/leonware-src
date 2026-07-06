package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import org.newsclub.net.unix.AFVSOCKSocketAddress;
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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/StrafeModule.class */
@ModuleRegister(name = "Strafe", category = Category.MOVEMENT)
public class StrafeModule extends Module {
    private static final StrafeModule instance = new StrafeModule();
    private final ModeSetting mode = new ModeSetting("Режим").values("Легитный", "MetaHvH", "Matrix7").value("Легитный");

    @Generated
    public static StrafeModule getInstance() {
        return instance;
    }

    public StrafeModule() {
        addSettings(this.mode);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener rotationUpdateEvent = RotationUpdateEvent.getInstance().subscribe(new Listener(event -> {
            if (this.mode.is("Легитный")) {
                RotationManager.getInstance().addRotation(new Rotation(mc.field_1724.method_36454() + dony(), mc.field_1724.method_36455()), RotationStrategy.TARGET, TaskPriority.LOWEST, this);
            }
        }));
        EventListener movementInputEvent = MovementInputEvent.getInstance().subscribe(new Listener(event2 -> {
            if (this.mode.is("Легитный")) {
                RotationPlan currentPlan = RotationManager.getInstance().getCurrentRotationPlan();
                if (currentPlan == null || currentPlan.provider() == this) {
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
                    event2.getDirectionalInput().setLeft(false);
                    event2.getDirectionalInput().setRight(false);
                    event2.getDirectionalInput().setBackwards(false);
                    event2.getDirectionalInput().setForwards(w || s || a || d);
                }
            }
        }));
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event3 -> {
            if (this.mode.is("MetaHvH")) {
                handleMetaHvH();
            } else if (this.mode.is("Matrix7")) {
                handleMatrix7();
            }
        }));
        addEvents(rotationUpdateEvent, movementInputEvent, updateEvent);
    }

    private void handleMetaHvH() {
        if (mc.field_1724 != null && !mc.field_1724.method_5799() && !mc.field_1724.method_5681() && !mc.field_1724.method_5869() && !FlightModule.getInstance().isVanillaFlying() && !mc.field_1724.method_6128() && MoveUtil.isMoving()) {
            float motion = 0.19f;
            class_1293 speedEffect = mc.field_1724.method_6112(class_1294.field_5904);
            if (speedEffect != null) {
                int amplifier = speedEffect.method_5578();
                switch (amplifier) {
                    case 0:
                        motion = 0.25f;
                        break;
                    case 1:
                        motion = 0.37f;
                        break;
                    case AFVSOCKSocketAddress.VMADDR_CID_HOST /* 2 */:
                        motion = 0.46f;
                        break;
                    case 3:
                        motion = 0.7f;
                        break;
                    default:
                        motion = 0.75f + ((amplifier - 3) * 0.05f);
                        break;
                }
            }
            if (mc.field_1690.field_1903.method_1434()) {
                motion += 0.1f;
            }
            MoveUtil.setSpeed(motion);
        }
    }

    private void handleMatrix7() {
        if (MoveUtil.isMoving()) {
            if (mc.field_1724.method_24828()) {
                mc.field_1724.method_18800(mc.field_1724.method_18798().field_1352, 0.419652d, mc.field_1724.method_18798().field_1350);
                MoveUtil.strafe();
            } else {
                double motionSquared = (mc.field_1724.method_18798().field_1352 * mc.field_1724.method_18798().field_1352) + (mc.field_1724.method_18798().field_1350 * mc.field_1724.method_18798().field_1350);
                if (motionSquared < 0.04d) {
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
            return d ? 45.0f : 0.0f;
        }
        if (s) {
            if (a) {
                return -135.0f;
            }
            return d ? 135.0f : 180.0f;
        }
        if (a) {
            return -90.0f;
        }
        return d ? 90.0f : 0.0f;
    }
}
