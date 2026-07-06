/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_243
 *  net.minecraft.class_2596
 *  net.minecraft.class_2708
 *  net.minecraft.class_2828
 *  net.minecraft.class_2886
 *  net.minecraft.class_3532
 */
package sweetie.leonware.api.utils.rotation.manager;

import lombok.Generated;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_243;
import net.minecraft.class_2596;
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
import sweetie.leonware.api.utils.rotation.manager.RotationPlan;
import sweetie.leonware.api.utils.rotation.manager.RotationStrategy;
import sweetie.leonware.api.utils.task.TaskPriority;
import sweetie.leonware.api.utils.task.TaskProcessor;
import sweetie.leonware.inject.accessors.AccessorPlayerInteractItemC2SPacket;

public class RotationManager
implements QuickImports {
    private static final RotationManager instance = new RotationManager();
    private RotationPlan lastRotationPlan;
    private final TaskProcessor<RotationPlan> rotationPlanRequestProcessor = new TaskProcessor();
    private Rotation currentRotation;
    private Rotation previousRotation;
    private Rotation serverRotation = Rotation.DEFAULT;

    public void load() {
        VelocityEvent.getInstance().subscribe(new Listener<VelocityEvent.VelocityEventData>(event -> {
            if (this.getCurrentRotationPlan() != null && this.getCurrentRotationPlan().moveCorrection()) {
                event.setVelocity(class_1297.method_18795((class_243)event.getMovementInput(), (float)event.getSpeed(), (float)this.getRotation().getYaw()));
            }
        }));
        PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(event -> {
            if (event.isSend()) {
                Rotation rotation;
                class_2596<?> patt1$temp;
                class_2596<?> patt0$temp = event.packet();
                if (patt0$temp instanceof class_2886) {
                    class_2886 useItem = (class_2886)patt0$temp;
                    if (this.currentRotation != null && RotationManager.mc.field_1724 != null) {
                        ((AccessorPlayerInteractItemC2SPacket)useItem).leonware$setYaw(this.currentRotation.getYaw());
                        ((AccessorPlayerInteractItemC2SPacket)useItem).leonware$setPitch(this.currentRotation.getPitch());
                        return;
                    }
                }
                if ((patt1$temp = event.packet()) instanceof class_2828) {
                    class_2828 packet = (class_2828)patt1$temp;
                    if (!packet.method_36172()) {
                        return;
                    }
                    rotation = new Rotation(packet.method_12271(1.0f), packet.method_12270(1.0f));
                } else {
                    class_2596<?> patt2$temp = event.packet();
                    if (patt2$temp instanceof class_2708) {
                        class_2708 packet = (class_2708)patt2$temp;
                        rotation = new Rotation(packet.comp_3228().comp_3150(), packet.comp_3228().comp_3151());
                    } else {
                        return;
                    }
                }
                if (!PacketEvent.getInstance().isCancel()) {
                    this.serverRotation = rotation;
                }
            }
        }));
        MovementInputEvent.getInstance().subscribe(new Listener<MovementInputEvent.MovementInputEventData>(event -> PostRotationMovementInputEvent.getInstance().call()));
        GameLoopEvent.getInstance().subscribe(new Listener<GameLoopEvent>(event -> {
            if (RotationManager.mc.field_1724 == null) {
                return;
            }
            RotationUpdateEvent.getInstance().call();
            this.update();
        }));
    }

    private void setRotation(Rotation value) {
        this.previousRotation = value == null ? (this.currentRotation != null ? this.currentRotation : (RotationManager.mc.field_1724 != null ? new Rotation(RotationManager.mc.field_1724.method_36454(), RotationManager.mc.field_1724.method_36455()) : Rotation.DEFAULT)) : this.currentRotation;
        this.currentRotation = value;
    }

    public Rotation getRotation() {
        if (RotationManager.mc.field_1724 == null) {
            return Rotation.DEFAULT;
        }
        return this.currentRotation != null ? this.currentRotation : RotationUtil.fromVec2f(RotationManager.mc.field_1724.method_5802());
    }

    public Rotation getPreviousRotation() {
        if (RotationManager.mc.field_1724 == null) {
            return Rotation.DEFAULT;
        }
        return this.previousRotation != null ? this.previousRotation : RotationUtil.fromVec2f(RotationManager.mc.field_1724.method_5802());
    }

    public RotationPlan getCurrentRotationPlan() {
        return this.rotationPlanRequestProcessor.fetchActiveTaskValue() != null ? this.rotationPlanRequestProcessor.fetchActiveTaskValue() : this.lastRotationPlan;
    }

    public void addRotation(Rotation.VecRotation vecRotation, class_1309 entity, RotationStrategy configurable, TaskPriority requestPriority, Module provider) {
        this.addRotation(configurable.createRotationPlan(vecRotation.rotation(), vecRotation.vec(), (class_1297)entity, provider), requestPriority, provider);
    }

    public void addRotation(Rotation rotation, RotationStrategy configurable, TaskPriority requestPriority, Module provider) {
        this.addRotation(configurable.createRotationPlan(rotation, provider), requestPriority, provider);
    }

    private void addRotation(RotationPlan plan, TaskPriority requestPriority, Module provider) {
        this.rotationPlanRequestProcessor.addTask(new TaskProcessor.Task<RotationPlan>(plan.ticksUntilReset(), requestPriority.getPriority(), provider, plan));
    }

    private void update() {
        double differenceFromCurrentToPlayer;
        RotationPlan activePlan = this.getCurrentRotationPlan();
        if (activePlan == null) {
            return;
        }
        Rotation playerRotation = RotationUtil.fromVec2f(RotationManager.mc.field_1724.method_5802());
        if (this.rotationPlanRequestProcessor.fetchActiveTaskValue() == null && (differenceFromCurrentToPlayer = this.computeRotationDifference(this.serverRotation, playerRotation)) < (double)activePlan.resetThreshold()) {
            if (this.currentRotation != null) {
                RotationManager.mc.field_1724.method_36456(this.currentRotation.getYaw() + this.computeAngleDifference(RotationManager.mc.field_1724.method_36454(), this.currentRotation.getYaw()));
                RotationManager.mc.field_1724.method_36457(this.currentRotation.getPitch() + this.computeAngleDifference(RotationManager.mc.field_1724.method_36455(), this.currentRotation.getPitch()));
            }
            this.setRotation(null);
            this.lastRotationPlan = null;
            return;
        }
        Rotation newRotation = activePlan.nextRotation(this.currentRotation != null ? this.currentRotation : playerRotation, this.rotationPlanRequestProcessor.fetchActiveTaskValue() == null).adjustSensitivity();
        this.setRotation(newRotation);
        if (activePlan.clientLook()) {
            RotationManager.mc.field_1724.method_36456(newRotation.getYaw());
            RotationManager.mc.field_1724.method_36457(newRotation.getPitch());
        }
        this.lastRotationPlan = activePlan;
        this.rotationPlanRequestProcessor.tick(1);
    }

    private double computeRotationDifference(Rotation a, Rotation b) {
        return Math.hypot(class_3532.method_15379((float)this.computeAngleDifference(a.getPitch(), b.getPitch())), class_3532.method_15379((float)(a.getPitch() - b.getPitch())));
    }

    private float computeAngleDifference(float a, float b) {
        return class_3532.method_15393((float)(a - b));
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
}

