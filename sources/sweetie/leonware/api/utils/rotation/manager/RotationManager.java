package sweetie.leonware.api.utils.rotation.manager;

import lombok.Generated;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_2708;
import net.minecraft.class_2828;
import net.minecraft.class_2886;
import net.minecraft.class_3532;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.GameLoopEvent;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.events.other.RotationUpdateEvent;
import sweetie.leonware.api.event.events.player.move.VelocityEvent;
import sweetie.leonware.api.event.events.player.other.MovementInputEvent;
import sweetie.leonware.api.event.events.player.other.PostRotationMovementInputEvent;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.task.TaskPriority;
import sweetie.leonware.api.utils.task.TaskProcessor;
import sweetie.leonware.inject.accessors.AccessorPlayerInteractItemC2SPacket;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/rotation/manager/RotationManager.class */
public class RotationManager implements QuickImports {
    private static final RotationManager instance = new RotationManager();
    private RotationPlan lastRotationPlan;
    private Rotation currentRotation;
    private Rotation previousRotation;
    private final TaskProcessor<RotationPlan> rotationPlanRequestProcessor = new TaskProcessor<>();
    private Rotation serverRotation = Rotation.DEFAULT;

    @Generated
    public void setLastRotationPlan(RotationPlan lastRotationPlan) {
        this.lastRotationPlan = lastRotationPlan;
    }

    @Generated
    public void setCurrentRotation(Rotation currentRotation) {
        this.currentRotation = currentRotation;
    }

    @Generated
    public void setPreviousRotation(Rotation previousRotation) {
        this.previousRotation = previousRotation;
    }

    @Generated
    public void setServerRotation(Rotation serverRotation) {
        this.serverRotation = serverRotation;
    }

    @Generated
    public static RotationManager getInstance() {
        return instance;
    }

    @Generated
    public RotationPlan getLastRotationPlan() {
        return this.lastRotationPlan;
    }

    @Generated
    public TaskProcessor<RotationPlan> getRotationPlanRequestProcessor() {
        return this.rotationPlanRequestProcessor;
    }

    @Generated
    public Rotation getCurrentRotation() {
        return this.currentRotation;
    }

    @Generated
    public Rotation getServerRotation() {
        return this.serverRotation;
    }

    public void load() {
        VelocityEvent.getInstance().subscribe(new Listener(event -> {
            if (getCurrentRotationPlan() != null && getCurrentRotationPlan().moveCorrection()) {
                event.setVelocity(class_1297.method_18795(event.getMovementInput(), event.getSpeed(), getRotation().getYaw()));
            }
        }));
        PacketEvent.getInstance().subscribe(new Listener(event2 -> {
            Rotation rotation;
            if (event2.isSend()) {
                AccessorPlayerInteractItemC2SPacket accessorPlayerInteractItemC2SPacketPacket = event2.packet();
                if (accessorPlayerInteractItemC2SPacketPacket instanceof class_2886) {
                    AccessorPlayerInteractItemC2SPacket accessorPlayerInteractItemC2SPacket = (class_2886) accessorPlayerInteractItemC2SPacketPacket;
                    if (this.currentRotation != null && mc.field_1724 != null) {
                        accessorPlayerInteractItemC2SPacket.leonware$setYaw(this.currentRotation.getYaw());
                        accessorPlayerInteractItemC2SPacket.leonware$setPitch(this.currentRotation.getPitch());
                        return;
                    }
                }
                class_2828 class_2828VarPacket = event2.packet();
                if (class_2828VarPacket instanceof class_2828) {
                    class_2828 packet = class_2828VarPacket;
                    if (!packet.method_36172()) {
                        return;
                    } else {
                        rotation = new Rotation(packet.method_12271(1.0f), packet.method_12270(1.0f));
                    }
                } else {
                    class_2708 class_2708VarPacket = event2.packet();
                    if (class_2708VarPacket instanceof class_2708) {
                        class_2708 packet2 = class_2708VarPacket;
                        rotation = new Rotation(packet2.comp_3228().comp_3150(), packet2.comp_3228().comp_3151());
                    } else {
                        return;
                    }
                }
                if (!PacketEvent.getInstance().isCancel()) {
                    this.serverRotation = rotation;
                }
            }
        }));
        MovementInputEvent.getInstance().subscribe(new Listener(event3 -> {
            PostRotationMovementInputEvent.getInstance().call();
        }));
        GameLoopEvent.getInstance().subscribe(new Listener(event4 -> {
            if (mc.field_1724 == null) {
                return;
            }
            RotationUpdateEvent.getInstance().call();
            update();
        }));
    }

    private void setRotation(Rotation value) {
        Rotation rotation = (value == null && this.currentRotation == null) ? mc.field_1724 != null ? new Rotation(mc.field_1724.method_36454(), mc.field_1724.method_36455()) : Rotation.DEFAULT : this.currentRotation;
        this.previousRotation = rotation;
        this.currentRotation = value;
    }

    public Rotation getRotation() {
        return mc.field_1724 == null ? Rotation.DEFAULT : this.currentRotation != null ? this.currentRotation : RotationUtil.fromVec2f(mc.field_1724.method_5802());
    }

    public Rotation getPreviousRotation() {
        return mc.field_1724 == null ? Rotation.DEFAULT : this.previousRotation != null ? this.previousRotation : RotationUtil.fromVec2f(mc.field_1724.method_5802());
    }

    public RotationPlan getCurrentRotationPlan() {
        return this.rotationPlanRequestProcessor.fetchActiveTaskValue() != null ? this.rotationPlanRequestProcessor.fetchActiveTaskValue() : this.lastRotationPlan;
    }

    public void addRotation(Rotation.VecRotation vecRotation, class_1309 entity, RotationStrategy configurable, TaskPriority requestPriority, Module provider) {
        addRotation(configurable.createRotationPlan(vecRotation.rotation(), vecRotation.vec(), entity, provider), requestPriority, provider);
    }

    public void addRotation(Rotation rotation, RotationStrategy configurable, TaskPriority requestPriority, Module provider) {
        addRotation(configurable.createRotationPlan(rotation, provider), requestPriority, provider);
    }

    private void addRotation(RotationPlan plan, TaskPriority requestPriority, Module provider) {
        this.rotationPlanRequestProcessor.addTask(new TaskProcessor.Task<>(plan.ticksUntilReset(), requestPriority.getPriority(), provider, plan));
    }

    private void update() {
        RotationPlan activePlan = getCurrentRotationPlan();
        if (activePlan == null) {
            return;
        }
        Rotation playerRotation = RotationUtil.fromVec2f(mc.field_1724.method_5802());
        if (this.rotationPlanRequestProcessor.fetchActiveTaskValue() == null) {
            double differenceFromCurrentToPlayer = computeRotationDifference(this.serverRotation, playerRotation);
            if (differenceFromCurrentToPlayer < activePlan.resetThreshold()) {
                if (this.currentRotation != null) {
                    mc.field_1724.method_36456(this.currentRotation.getYaw() + computeAngleDifference(mc.field_1724.method_36454(), this.currentRotation.getYaw()));
                    mc.field_1724.method_36457(this.currentRotation.getPitch() + computeAngleDifference(mc.field_1724.method_36455(), this.currentRotation.getPitch()));
                }
                setRotation(null);
                this.lastRotationPlan = null;
                return;
            }
        }
        Rotation newRotation = activePlan.nextRotation(this.currentRotation != null ? this.currentRotation : playerRotation, this.rotationPlanRequestProcessor.fetchActiveTaskValue() == null).adjustSensitivity();
        setRotation(newRotation);
        if (activePlan.clientLook()) {
            mc.field_1724.method_36456(newRotation.getYaw());
            mc.field_1724.method_36457(newRotation.getPitch());
        }
        this.lastRotationPlan = activePlan;
        this.rotationPlanRequestProcessor.tick(1);
    }

    private double computeRotationDifference(Rotation a, Rotation b) {
        return Math.hypot(class_3532.method_15379(computeAngleDifference(a.getPitch(), b.getPitch())), class_3532.method_15379(a.getPitch() - b.getPitch()));
    }

    private float computeAngleDifference(float a, float b) {
        return class_3532.method_15393(a - b);
    }
}
