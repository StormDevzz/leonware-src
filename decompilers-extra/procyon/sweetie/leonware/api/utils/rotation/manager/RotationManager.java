// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.rotation.manager;

import lombok.Generated;
import net.minecraft.class_3532;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.utils.task.TaskPriority;
import net.minecraft.class_1309;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import net.minecraft.class_2596;
import sweetie.leonware.api.event.events.other.RotationUpdateEvent;
import sweetie.leonware.api.event.events.client.GameLoopEvent;
import sweetie.leonware.api.event.events.player.other.PostRotationMovementInputEvent;
import sweetie.leonware.api.event.events.player.other.MovementInputEvent;
import net.minecraft.class_2708;
import net.minecraft.class_2828;
import sweetie.leonware.inject.accessors.AccessorPlayerInteractItemC2SPacket;
import net.minecraft.class_2886;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.Listener;
import net.minecraft.class_1297;
import sweetie.leonware.api.event.events.player.move.VelocityEvent;
import sweetie.leonware.api.utils.task.TaskProcessor;
import sweetie.leonware.api.system.interfaces.QuickImports;

public class RotationManager implements QuickImports
{
    private static final RotationManager instance;
    private RotationPlan lastRotationPlan;
    private final TaskProcessor<RotationPlan> rotationPlanRequestProcessor;
    private Rotation currentRotation;
    private Rotation previousRotation;
    private Rotation serverRotation;
    
    public RotationManager() {
        this.rotationPlanRequestProcessor = new TaskProcessor<RotationPlan>();
        this.serverRotation = Rotation.DEFAULT;
    }
    
    public void load() {
        VelocityEvent.getInstance().subscribe(new Listener<VelocityEvent.VelocityEventData>(event -> {
            if (this.getCurrentRotationPlan() != null && this.getCurrentRotationPlan().moveCorrection()) {
                event.setVelocity(class_1297.method_18795(event.getMovementInput(), event.getSpeed(), this.getRotation().getYaw()));
            }
            return;
        }));
        PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(event -> {
            if (event.isSend()) {
                final class_2596 patt0$temp = event.packet();
                if (patt0$temp instanceof final class_2886 useItem) {
                    if (this.currentRotation != null && RotationManager.mc.field_1724 != null) {
                        ((AccessorPlayerInteractItemC2SPacket)useItem).leonware$setYaw(this.currentRotation.getYaw());
                        ((AccessorPlayerInteractItemC2SPacket)useItem).leonware$setPitch(this.currentRotation.getPitch());
                        return;
                    }
                }
                final class_2596 patt1$temp = event.packet();
                Rotation rotation;
                if (patt1$temp instanceof final class_2828 packet) {
                    if (!packet.method_36172()) {
                        return;
                    }
                    else {
                        rotation = new Rotation(packet.method_12271(1.0f), packet.method_12270(1.0f));
                    }
                }
                else {
                    final class_2596 patt2$temp = event.packet();
                    if (patt2$temp instanceof final class_2708 packet2) {
                        rotation = new Rotation(packet2.comp_3228().comp_3150(), packet2.comp_3228().comp_3151());
                    }
                    else {
                        return;
                    }
                }
                if (!PacketEvent.getInstance().isCancel()) {
                    this.serverRotation = rotation;
                }
            }
            return;
        }));
        MovementInputEvent.getInstance().subscribe(new Listener<MovementInputEvent.MovementInputEventData>(event -> PostRotationMovementInputEvent.getInstance().call()));
        GameLoopEvent.getInstance().subscribe(new Listener<GameLoopEvent>(event -> {
            if (RotationManager.mc.field_1724 != null) {
                RotationUpdateEvent.getInstance().call();
                this.update();
            }
        }));
    }
    
    private void setRotation(final Rotation value) {
        this.previousRotation = ((value == null) ? ((this.currentRotation != null) ? this.currentRotation : ((RotationManager.mc.field_1724 != null) ? new Rotation(RotationManager.mc.field_1724.method_36454(), RotationManager.mc.field_1724.method_36455()) : Rotation.DEFAULT)) : this.currentRotation);
        this.currentRotation = value;
    }
    
    public Rotation getRotation() {
        if (RotationManager.mc.field_1724 == null) {
            return Rotation.DEFAULT;
        }
        return (this.currentRotation != null) ? this.currentRotation : RotationUtil.fromVec2f(RotationManager.mc.field_1724.method_5802());
    }
    
    public Rotation getPreviousRotation() {
        if (RotationManager.mc.field_1724 == null) {
            return Rotation.DEFAULT;
        }
        return (this.previousRotation != null) ? this.previousRotation : RotationUtil.fromVec2f(RotationManager.mc.field_1724.method_5802());
    }
    
    public RotationPlan getCurrentRotationPlan() {
        return (this.rotationPlanRequestProcessor.fetchActiveTaskValue() != null) ? this.rotationPlanRequestProcessor.fetchActiveTaskValue() : this.lastRotationPlan;
    }
    
    public void addRotation(final Rotation.VecRotation vecRotation, final class_1309 entity, final RotationStrategy configurable, final TaskPriority requestPriority, final Module provider) {
        this.addRotation(configurable.createRotationPlan(vecRotation.rotation(), vecRotation.vec(), (class_1297)entity, provider), requestPriority, provider);
    }
    
    public void addRotation(final Rotation rotation, final RotationStrategy configurable, final TaskPriority requestPriority, final Module provider) {
        this.addRotation(configurable.createRotationPlan(rotation, provider), requestPriority, provider);
    }
    
    private void addRotation(final RotationPlan plan, final TaskPriority requestPriority, final Module provider) {
        this.rotationPlanRequestProcessor.addTask(new TaskProcessor.Task<RotationPlan>(plan.ticksUntilReset(), requestPriority.getPriority(), provider, plan));
    }
    
    private void update() {
        final RotationPlan activePlan = this.getCurrentRotationPlan();
        if (activePlan == null) {
            return;
        }
        final Rotation playerRotation = RotationUtil.fromVec2f(RotationManager.mc.field_1724.method_5802());
        if (this.rotationPlanRequestProcessor.fetchActiveTaskValue() == null) {
            final double differenceFromCurrentToPlayer = this.computeRotationDifference(this.serverRotation, playerRotation);
            if (differenceFromCurrentToPlayer < activePlan.resetThreshold()) {
                if (this.currentRotation != null) {
                    RotationManager.mc.field_1724.method_36456(this.currentRotation.getYaw() + this.computeAngleDifference(RotationManager.mc.field_1724.method_36454(), this.currentRotation.getYaw()));
                    RotationManager.mc.field_1724.method_36457(this.currentRotation.getPitch() + this.computeAngleDifference(RotationManager.mc.field_1724.method_36455(), this.currentRotation.getPitch()));
                }
                this.setRotation(null);
                this.lastRotationPlan = null;
                return;
            }
        }
        final Rotation newRotation = activePlan.nextRotation((this.currentRotation != null) ? this.currentRotation : playerRotation, this.rotationPlanRequestProcessor.fetchActiveTaskValue() == null).adjustSensitivity();
        this.setRotation(newRotation);
        if (activePlan.clientLook()) {
            RotationManager.mc.field_1724.method_36456(newRotation.getYaw());
            RotationManager.mc.field_1724.method_36457(newRotation.getPitch());
        }
        this.lastRotationPlan = activePlan;
        this.rotationPlanRequestProcessor.tick(1);
    }
    
    private double computeRotationDifference(final Rotation a, final Rotation b) {
        return Math.hypot(class_3532.method_15379(this.computeAngleDifference(a.getPitch(), b.getPitch())), class_3532.method_15379(a.getPitch() - b.getPitch()));
    }
    
    private float computeAngleDifference(final float a, final float b) {
        return class_3532.method_15393(a - b);
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
    public void setLastRotationPlan(final RotationPlan lastRotationPlan) {
        this.lastRotationPlan = lastRotationPlan;
    }
    
    @Generated
    public void setCurrentRotation(final Rotation currentRotation) {
        this.currentRotation = currentRotation;
    }
    
    @Generated
    public void setPreviousRotation(final Rotation previousRotation) {
        this.previousRotation = previousRotation;
    }
    
    @Generated
    public void setServerRotation(final Rotation serverRotation) {
        this.serverRotation = serverRotation;
    }
    
    @Generated
    public static RotationManager getInstance() {
        return RotationManager.instance;
    }
    
    static {
        instance = new RotationManager();
    }
}
