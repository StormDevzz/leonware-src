// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import java.util.ArrayDeque;
import sweetie.leonware.api.interfaces.IBacktrackable;
import sweetie.leonware.api.utils.rotation.rotations.NeuroRotation;
import sweetie.leonware.api.utils.rotation.rotations.MatrixRotation;
import sweetie.leonware.api.utils.rotation.rotations.FunTimeRotation;
import sweetie.leonware.api.utils.rotation.rotations.LonyGriefRotation;
import sweetie.leonware.api.utils.rotation.rotations.UniversalRotation;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.api.utils.rotation.rotations.SpookyTimeRotation;
import sweetie.leonware.api.utils.rotation.rotations.AimAssistRotation;
import sweetie.leonware.api.utils.rotation.rotations.InstantRotation;
import sweetie.leonware.api.utils.rotation.rotations.SmoothRotation;
import sweetie.leonware.api.utils.rotation.rotations.LegitSnapRotation;
import sweetie.leonware.api.utils.rotation.manager.RotationMode;
import sweetie.leonware.api.utils.task.TaskPriority;
import sweetie.leonware.client.features.modules.combat.elytratarget.ElytraTargetModule;
import sweetie.leonware.api.utils.rotation.manager.RotationStrategy;
import sweetie.leonware.client.features.modules.movement.MoveFixModule;
import sweetie.leonware.api.utils.rotation.rotations.ClientLookRotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import net.minecraft.class_3532;
import net.minecraft.class_1297;
import sweetie.leonware.client.features.modules.player.ElytraSwapModule;
import sweetie.leonware.api.utils.other.SlownessManager;
import sweetie.leonware.api.utils.player.InventoryUtil;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import net.minecraft.class_243;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import net.minecraft.class_2596;
import sweetie.leonware.api.utils.render.Render2DEngine;
import java.awt.Color;
import sweetie.leonware.api.event.events.render.Render2DEvent;
import sweetie.leonware.api.utils.rotation.misc.AuraUtil;
import sweetie.leonware.api.event.events.player.world.AttackEvent;
import sweetie.leonware.api.event.events.other.RotationUpdateEvent;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.events.client.DisconnectEvent;
import net.minecraft.class_1937;
import net.minecraft.class_2663;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.setting.Setting;
import java.util.function.Supplier;
import java.util.Objects;
import net.minecraft.class_1309;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.utils.rotation.rotations.Neuro2Rotation;
import sweetie.leonware.api.utils.neuro.Neuro2Model;
import sweetie.leonware.api.utils.combat.CombatExecutor;
import sweetie.leonware.api.utils.combat.TargetManager;
import sweetie.leonware.api.utils.neuro.AIPredictor;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Aura", category = Category.COMBAT)
public class AuraModule extends Module
{
    private static final AuraModule instance;
    private final AIPredictor predictor;
    private final TargetManager targetManager;
    public final CombatExecutor combatExecutor;
    private final Neuro2Model neuro2Model;
    private final Neuro2Rotation neuro2Rotation;
    private final ModeSetting aimMode;
    private final ModeSetting snapMode;
    private final SliderSetting distance;
    private final SliderSetting preDistance;
    private final SliderSetting fov;
    private final BooleanSetting renderFov;
    private final SliderSetting snapSpeed;
    private final SliderSetting snapSmoothness;
    private final SliderSetting smoothYawSpeed;
    private final SliderSetting smoothPitchSpeed;
    private final SliderSetting aimAssistYawMin;
    private final SliderSetting aimAssistYawMax;
    private final SliderSetting aimAssistPitchMin;
    private final SliderSetting aimAssistPitchMax;
    private final SliderSetting aimAssistTimeout;
    private final MultiBooleanSetting targets;
    public final MultiBooleanSetting options;
    public final ModeSetting pvpStyle;
    public final SliderSetting minCps;
    public final SliderSetting maxCps;
    public final ModeSetting clickPattern;
    private final ModeSetting critMode;
    private final BooleanSetting hitInBacktrack;
    private final BooleanSetting legitHits;
    private final SliderSetting legitDelayMin;
    private final SliderSetting legitDelayMax;
    private final BooleanSetting dontDropSprintInWater;
    private final BooleanSetting deathDisable;
    private final MultiBooleanSetting autoMaceOptions;
    private final SliderSetting minFallDistance;
    private final SliderSetting maceDelay;
    private final BooleanSetting clientLook;
    private final BooleanSetting elytraOverride;
    private final SliderSetting elytraDistance;
    private final SliderSetting elytraPreDistance;
    public class_1309 target;
    
    public AuraModule() {
        this.predictor = new AIPredictor();
        this.targetManager = new TargetManager();
        this.combatExecutor = new CombatExecutor();
        this.neuro2Model = new Neuro2Model();
        this.neuro2Rotation = new Neuro2Rotation(this.neuro2Model);
        this.aimMode = new ModeSetting("Aim mode").value("Smooth").values("Smooth", "Snap", "HvH", "Aim Assist", "Matrix", "Vulcan", "Spooky Time", "Fun Time", "Holy World", "Lony Grief", "Neuro", "Neuro2").onAction(() -> {
            if (this.getAimMode().is("Neuro")) {
                if (this.predictor.isLoaded()) {
                    this.predictor.close();
                }
                this.loadModel();
            }
            else {
                this.predictor.close();
            }
            return;
        });
        this.snapMode = new ModeSetting("Snap mode").values("Smooth", "Legit").value("Smooth").setVisible(() -> this.aimMode.is("Snap"));
        this.distance = new SliderSetting("Distance").value(3.0f).range(2.5f, 6.0f).step(0.1f);
        this.preDistance = new SliderSetting("Pre distance").value(0.3f).range(0.0f, 3.0f).step(0.1f);
        this.fov = new SliderSetting("FOV").value(180.0f).range(1.0f, 180.0f).step(1.0f);
        this.renderFov = new BooleanSetting("Render FOV").value(false);
        this.snapSpeed = new SliderSetting("Snap Speed").value(1.0f).range(0.3f, 2.0f).step(0.05f).setVisible(() -> this.aimMode.is("Snap") && this.snapMode.is("Legit"));
        this.snapSmoothness = new SliderSetting("Snap Smoothness").value(1.0f).range(0.3f, 2.0f).step(0.05f).setVisible(() -> this.aimMode.is("Snap") && this.snapMode.is("Legit"));
        this.smoothYawSpeed = new SliderSetting("Smooth Yaw Speed").value(20.0f).range(1.0f, 180.0f).step(1.0f).setVisible(() -> this.aimMode.is("Smooth"));
        this.smoothPitchSpeed = new SliderSetting("Smooth Pitch Speed").value(10.0f).range(1.0f, 90.0f).step(1.0f).setVisible(() -> this.aimMode.is("Smooth"));
        this.aimAssistYawMin = new SliderSetting("AA Yaw Min").value(10.0f).range(1.0f, 180.0f).step(1.0f).setVisible(() -> this.aimMode.is("Aim Assist"));
        this.aimAssistYawMax = new SliderSetting("AA Yaw Max").value(20.0f).range(1.0f, 180.0f).step(1.0f).setVisible(() -> this.aimMode.is("Aim Assist"));
        this.aimAssistPitchMin = new SliderSetting("AA Pitch Min").value(5.0f).range(1.0f, 90.0f).step(1.0f).setVisible(() -> this.aimMode.is("Aim Assist"));
        this.aimAssistPitchMax = new SliderSetting("AA Pitch Max").value(10.0f).range(1.0f, 90.0f).step(1.0f).setVisible(() -> this.aimMode.is("Aim Assist"));
        this.aimAssistTimeout = new SliderSetting("AA Timeout (ms)").value(500.0f).range(0.0f, 1000.0f).step(10.0f).setVisible(() -> this.aimMode.is("Aim Assist"));
        this.targets = new MultiBooleanSetting("Targets").value(new BooleanSetting("Players").value(true), new BooleanSetting("Mobs").value(true), new BooleanSetting("Animals").value(true), new BooleanSetting("Ignore Teams").value(false), new BooleanSetting("Ignore Staff").value(false), new BooleanSetting("Ignore Bots").value(false), new BooleanSetting("Ignore Naked").value(false), new BooleanSetting("Ignore Simple Armor").value(false));
        this.options = this.combatExecutor.options();
        this.pvpStyle = new ModeSetting("PvP Style").values("1.12+", "1.8.x").value("1.12+");
        this.minCps = new SliderSetting("Min APS").value(10.0f).range(1.0f, 20.0f).step(1.0f).setVisible(() -> this.pvpStyle.is("1.8.x"));
        this.maxCps = new SliderSetting("Max APS").value(14.0f).range(1.0f, 20.0f).step(1.0f).setVisible(() -> this.pvpStyle.is("1.8.x"));
        this.clickPattern = new ModeSetting("Click Pattern").values("Random", "Static", "Jitter", "Interpolation", "Gaussian", "Legit").value("Random").setVisible(() -> this.pvpStyle.is("1.8.x"));
        this.critMode = new ModeSetting("\u0422\u0438\u043f \u043a\u0440\u0438\u0442\u0430").values("\u0411\u044b\u0441\u0442\u0440\u044b\u0439", "\u041b\u0435\u0433\u0438\u0442\u043d\u044b\u0439").value("\u0411\u044b\u0441\u0442\u0440\u044b\u0439").setVisible(() -> this.options.isEnabled("Only Crits"));
        this.hitInBacktrack = new BooleanSetting("Hit in Backtrack").value(false).setVisible(() -> BacktrackModule.getInstance().isEnabled() && !BacktrackModule.getInstance().isDelayP());
        this.legitHits = new BooleanSetting("Legit delay").value(false);
        final SliderSetting step = new SliderSetting("Legit Min").value(40.0f).range(0.0f, 150.0f).step(1.0f);
        final BooleanSetting legitHits = this.legitHits;
        Objects.requireNonNull(legitHits);
        this.legitDelayMin = step.setVisible((Supplier<Boolean>)legitHits::getValue);
        final SliderSetting step2 = new SliderSetting("Legit Max").value(120.0f).range(0.0f, 150.0f).step(1.0f);
        final BooleanSetting legitHits2 = this.legitHits;
        Objects.requireNonNull(legitHits2);
        this.legitDelayMax = step2.setVisible((Supplier<Boolean>)legitHits2::getValue);
        this.dontDropSprintInWater = new BooleanSetting("Save sprint in water").value(false);
        this.deathDisable = new BooleanSetting("Disable on Death").value(false);
        this.autoMaceOptions = new MultiBooleanSetting("Auto Mace").value(new BooleanSetting("Enabled").value(false), new BooleanSetting("Only Players").value(true), new BooleanSetting("Swap Elytra").value(false));
        this.minFallDistance = new SliderSetting("Min Fall Distance").value(3.0f).range(0.0f, 20.0f).step(0.5f).setVisible(() -> this.autoMaceOptions.isEnabled("Enabled"));
        this.maceDelay = new SliderSetting("Mace Delay").value(0.0f).range(0.0f, 5.0f).step(1.0f).setVisible(() -> this.autoMaceOptions.isEnabled("Enabled"));
        this.clientLook = new BooleanSetting("Client look").value(false);
        this.elytraOverride = new BooleanSetting("Elytra override").value(false);
        final SliderSetting step3 = new SliderSetting("Elytra distance").value(3.0f).range(2.5f, 6.0f).step(0.1f);
        final BooleanSetting elytraOverride = this.elytraOverride;
        Objects.requireNonNull(elytraOverride);
        this.elytraDistance = step3.setVisible((Supplier<Boolean>)elytraOverride::getValue);
        final SliderSetting step4 = new SliderSetting("Elytra pre distance").value(16.0f).range(0.0f, 32.0f).step(0.1f);
        final BooleanSetting elytraOverride2 = this.elytraOverride;
        Objects.requireNonNull(elytraOverride2);
        this.elytraPreDistance = step4.setVisible((Supplier<Boolean>)elytraOverride2::getValue);
        this.addSettings(this.aimMode, this.snapMode, this.distance, this.preDistance, this.fov, this.renderFov, this.smoothYawSpeed, this.smoothPitchSpeed, this.snapSpeed, this.snapSmoothness, this.aimAssistYawMin, this.aimAssistYawMax, this.aimAssistPitchMin, this.aimAssistPitchMax, this.aimAssistTimeout, this.targets, this.options, this.critMode, this.pvpStyle, this.minCps, this.maxCps, this.clickPattern, this.hitInBacktrack, this.legitHits, this.legitDelayMin, this.legitDelayMax, this.dontDropSprintInWater, this.autoMaceOptions, this.minFallDistance, this.maceDelay, this.clientLook, this.elytraOverride, this.elytraDistance, this.elytraPreDistance, this.deathDisable);
    }
    
    public float getPreDistance() {
        return (AuraModule.mc.field_1724.method_6128() && this.elytraOverride.getValue()) ? this.elytraPreDistance.getValue() : this.preDistance.getValue();
    }
    
    public float getAttackDistance() {
        return (AuraModule.mc.field_1724.method_6128() && this.elytraOverride.getValue()) ? this.elytraDistance.getValue() : this.distance.getValue();
    }
    
    @Override
    public void onDisable() {
        this.targetManager.releaseTarget();
        this.target = null;
        this.predictor.close();
        this.combatExecutor.combatManager().reset();
    }
    
    @Override
    public void onEnable() {
        this.targetManager.releaseTarget();
        this.target = null;
        this.combatExecutor.combatManager().reset();
        if (this.aimMode.is("Neuro") && !this.predictor.isLoaded()) {
            this.loadModel();
        }
    }
    
    public void loadModel() {
        this.predictor.loadModel("Default");
    }
    
    @Override
    public void onEvent() {
        this.predictor.onEvent();
        final EventListener eventUpdate = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> this.updateEventHandler()));
        final EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(eventData -> {
            final class_2596 patt0$temp = eventData.packet();
            if (patt0$temp instanceof final class_2663 pac) {
                if (pac.method_11470() == 3 && pac.method_11469((class_1937)AuraModule.mc.field_1687) == AuraModule.mc.field_1724 && this.deathDisable.getValue()) {
                    this.disable();
                }
            }
            return;
        }));
        final EventListener disconnectEvent = DisconnectEvent.getInstance().subscribe(new Listener<DisconnectEvent.DisconnectData>(event -> this.disable()));
        this.addEvents(this.predictor.getEventListeners());
        this.addEvents(eventUpdate, packetEvent, disconnectEvent, RotationUpdateEvent.getInstance().subscribe(new Listener<RotationUpdateEvent>(e -> this.postRotMoveEventHandler())), AttackEvent.getInstance().subscribe(new Listener<AttackEvent.AttackEventData>(e -> AuraUtil.onAttack(this.aimMode.getValue()))), Render2DEvent.getInstance().subscribe(new Listener<Render2DEvent.Render2DEventData>(event -> {
            if (this.renderFov.getValue() && AuraModule.mc.field_1724 != null) {
                final float radius = this.fov.getValue() / 180.0f * (AuraModule.mc.method_22683().method_4486() / 2.0f);
                final float x = AuraModule.mc.method_22683().method_4486() / 2.0f;
                final float y = AuraModule.mc.method_22683().method_4502() / 2.0f;
                Render2DEngine.drawCircle(event.matrixStack(), x, y, radius, 1.0f, new Color(255, 255, 255, 100).getRGB());
            }
        })));
    }
    
    private void postRotMoveEventHandler() {
        if (this.target == null || AutoBuffModule.getInstance().isBuffing()) {
            return;
        }
        final class_243 attackVector = this.getTargetVector(this.target);
        final Rotation rotation = RotationUtil.fromVec3d(attackVector.method_1020(AuraModule.mc.field_1724.method_33571()));
        this.rotateToTarget(this.target, attackVector, rotation);
    }
    
    private void updateEventHandler() {
        if (AutoBuffModule.getInstance().isBuffing()) {
            this.targetManager.releaseTarget();
            this.target = null;
            this.combatExecutor.combatManager().configurable(null);
            return;
        }
        this.target = this.updateTarget();
        if (this.target != null && this.autoMaceOptions.isEnabled("Enabled") && this.autoMaceOptions.isEnabled("Swap Elytra")) {
            final boolean hasMace = InventoryUtil.findMaceBetter() != -1;
            final boolean isFalling = !AuraModule.mc.field_1724.method_24828() && AuraModule.mc.field_1724.method_18798().field_1351 < -0.1;
            if (hasMace && isFalling && InventoryUtil.hasElytraEquipped()) {
                if (SlownessManager.isEnabled()) {
                    SlownessManager.applySlowness(15L, () -> ElytraSwapModule.getInstance().swapChestplate());
                }
                else {
                    ElytraSwapModule.getInstance().swapChestplate();
                }
            }
        }
        if (this.target == null) {
            this.combatExecutor.combatManager().configurable(null);
            return;
        }
        final class_243 targetPosition = this.getTargetVector(this.target);
        if (targetPosition.method_1022(AuraModule.mc.field_1724.method_33571()) > this.getAttackDistance() + this.getPreDistance()) {
            this.targetManager.releaseTarget();
            this.combatExecutor.combatManager().configurable(null);
            return;
        }
        this.attackTarget(this.target);
    }
    
    private class_1309 updateTarget() {
        final TargetManager.EntityFilter filter = new TargetManager.EntityFilter(this.targets.getList());
        this.targetManager.searchTargets(AuraModule.mc.field_1687.method_18112(), this.getAttackDistance() + this.getPreDistance());
        this.targetManager.validateTarget(entity -> {
            if (!filter.isValid(entity)) {
                return false;
            }
            else {
                final class_243 targetVec = this.getTargetVector(entity);
                final Rotation needed = RotationUtil.fromVec3d(targetVec.method_1020(AuraModule.mc.field_1724.method_33571()));
                final float yawDiff = Math.abs(class_3532.method_15393(AuraModule.mc.field_1724.method_36454() - needed.getYaw()));
                return yawDiff <= this.fov.getValue();
            }
        });
        return this.targetManager.getCurrentTarget();
    }
    
    private void attackTarget(final class_1309 target) {
        if (target == null) {
            return;
        }
        this.combatExecutor.combatManager().configurable(new CombatExecutor.CombatConfigurable(target, RotationManager.getInstance().getRotation(), this.getAttackDistance(), this.options.getList()).withMace(this.autoMaceOptions.isEnabled("Enabled"), Math.round(this.maceDelay.getValue()), this.minFallDistance.getValue(), this.autoMaceOptions.isEnabled("Only Players"), this.autoMaceOptions.isEnabled("Swap Elytra")));
        final class_243 targetPosition = this.getTargetVector(target);
        if (AuraModule.mc.field_1724.method_33571().method_1022(RotationUtil.rayCastBox((class_1297)target, targetPosition)) > this.getAttackDistance()) {
            return;
        }
        this.combatExecutor.performAttack();
    }
    
    private void rotateToTarget(final class_1309 target, final class_243 targetVec, Rotation rotation) {
        final boolean isAimAssist = this.aimMode.is("Aim Assist");
        RotationMode mode = this.getRotationMode();
        if (this.clientLook.getValue() && !isAimAssist) {
            mode = new ClientLookRotation(mode, 0.35f);
        }
        final RotationStrategy configurable = new RotationStrategy(mode, MoveFixModule.enabled(), MoveFixModule.isFree()).clientLook(this.clientLook.getValue() || isAimAssist);
        final boolean noHitRule = !this.combatExecutor.combatManager().canAttack();
        if (this.usingElytraTarget() && ElytraTargetModule.getInstance().elytraRotationProcessor.customRotations.getValue()) {
            return;
        }
        if (noHitRule && this.aimMode.is("Snap")) {
            if (!MoveFixModule.getInstance().isEnabled() || !MoveFixModule.getInstance().targeting.getValue()) {
                return;
            }
            rotation = new Rotation(AuraModule.mc.field_1724.method_36454(), AuraModule.mc.field_1724.method_36455());
        }
        RotationManager.getInstance().addRotation(new Rotation.VecRotation(rotation, targetVec), target, configurable, TaskPriority.HIGH, this);
    }
    
    private RotationMode getRotationMode() {
        final String s = this.aimMode.getValue();
        return switch (s) {
            case "Snap" -> this.snapMode.is("Legit") ? new LegitSnapRotation(this.snapSpeed.getValue(), this.snapSmoothness.getValue()) : new SmoothRotation(this.smoothYawSpeed.getValue(), this.smoothPitchSpeed.getValue());
            case "HvH" -> new InstantRotation();
            case "Aim Assist" -> new AimAssistRotation(this.aimAssistYawMin.getValue(), this.aimAssistYawMax.getValue(), this.aimAssistPitchMin.getValue(), this.aimAssistPitchMax.getValue(), this.aimAssistTimeout.getValue());
            case "Spooky Time" -> new SpookyTimeRotation(this.combatExecutor.combatManager().clickScheduler());
            case "Holy World" -> new UniversalRotation((float)MathUtil.randomInRange(125, 155), (float)MathUtil.randomInRange(30, 60), true, true);
            case "Vulcan" -> new UniversalRotation(120.0f, 60.0f, false, false);
            case "Lony Grief" -> new LonyGriefRotation();
            case "Fun Time" -> new FunTimeRotation();
            case "Matrix" -> new MatrixRotation();
            case "Neuro" -> new NeuroRotation(this.predictor, 70.0f, 10.0f);
            case "Neuro2" -> this.neuro2Rotation;
            default -> new SmoothRotation(this.smoothYawSpeed.getValue(), this.smoothPitchSpeed.getValue());
        };
    }
    
    private class_243 getTargetVector(final class_1309 target) {
        if (target == null) {
            return class_243.field_1353;
        }
        if (this.usingElytraTarget()) {
            return ElytraTargetModule.getInstance().elytraRotationProcessor.getPredictedPos(target);
        }
        if (this.hitInBacktrack.getValue() && BacktrackModule.getInstance().isEnabled() && target instanceof IBacktrackable) {
            final IBacktrackable backtrackable = (IBacktrackable)target;
            final ArrayDeque<BacktrackModule.Position> tracks = backtrackable.leonware$getBackTracks();
            if (!tracks.isEmpty()) {
                final BacktrackModule.Position backtrackPos = tracks.peekFirst();
                if (backtrackPos != null) {
                    return backtrackPos.pos().method_1031(0.0, (double)(target.method_17682() / 2.0f), 0.0);
                }
            }
        }
        return target.method_19538().method_1031(0.0, (double)(target.method_17682() / 2.0f), 0.0);
    }
    
    private boolean usingElytraTarget() {
        return ElytraTargetModule.getInstance().isEnabled() && AuraModule.mc.field_1724.method_6128();
    }
    
    public boolean isLegitCrit() {
        return this.critMode.is("\u041b\u0435\u0433\u0438\u0442\u043d\u044b\u0439");
    }
    
    public boolean isLegitHits() {
        return this.legitHits.getValue();
    }
    
    public float getLegitDelayMin() {
        return this.legitDelayMin.getValue();
    }
    
    public float getLegitDelayMax() {
        return this.legitDelayMax.getValue();
    }
    
    public boolean isHitInBacktrack() {
        return this.hitInBacktrack.getValue() && !BacktrackModule.getInstance().isDelayP();
    }
    
    public boolean isDontDropSprintInWater() {
        return this.dontDropSprintInWater.getValue();
    }
    
    @Generated
    public static AuraModule getInstance() {
        return AuraModule.instance;
    }
    
    @Generated
    public Neuro2Model getNeuro2Model() {
        return this.neuro2Model;
    }
    
    @Generated
    public ModeSetting getAimMode() {
        return this.aimMode;
    }
    
    static {
        instance = new AuraModule();
    }
}
