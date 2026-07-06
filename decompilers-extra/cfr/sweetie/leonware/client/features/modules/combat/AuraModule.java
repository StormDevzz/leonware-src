/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_1937
 *  net.minecraft.class_243
 *  net.minecraft.class_2596
 *  net.minecraft.class_2663
 *  net.minecraft.class_3532
 */
package sweetie.leonware.client.features.modules.combat;

import java.awt.Color;
import java.util.ArrayDeque;
import lombok.Generated;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1937;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2663;
import net.minecraft.class_3532;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.DisconnectEvent;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.events.other.RotationUpdateEvent;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.event.events.player.world.AttackEvent;
import sweetie.leonware.api.event.events.render.Render2DEvent;
import sweetie.leonware.api.interfaces.IBacktrackable;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.combat.CombatExecutor;
import sweetie.leonware.api.utils.combat.TargetManager;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.api.utils.neuro.AIPredictor;
import sweetie.leonware.api.utils.neuro.Neuro2Model;
import sweetie.leonware.api.utils.other.SlownessManager;
import sweetie.leonware.api.utils.player.InventoryUtil;
import sweetie.leonware.api.utils.render.Render2DEngine;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.utils.rotation.manager.RotationMode;
import sweetie.leonware.api.utils.rotation.manager.RotationStrategy;
import sweetie.leonware.api.utils.rotation.misc.AuraUtil;
import sweetie.leonware.api.utils.rotation.rotations.AimAssistRotation;
import sweetie.leonware.api.utils.rotation.rotations.ClientLookRotation;
import sweetie.leonware.api.utils.rotation.rotations.FunTimeRotation;
import sweetie.leonware.api.utils.rotation.rotations.InstantRotation;
import sweetie.leonware.api.utils.rotation.rotations.LegitSnapRotation;
import sweetie.leonware.api.utils.rotation.rotations.LonyGriefRotation;
import sweetie.leonware.api.utils.rotation.rotations.MatrixRotation;
import sweetie.leonware.api.utils.rotation.rotations.Neuro2Rotation;
import sweetie.leonware.api.utils.rotation.rotations.NeuroRotation;
import sweetie.leonware.api.utils.rotation.rotations.SmoothRotation;
import sweetie.leonware.api.utils.rotation.rotations.SpookyTimeRotation;
import sweetie.leonware.api.utils.rotation.rotations.UniversalRotation;
import sweetie.leonware.api.utils.task.TaskPriority;
import sweetie.leonware.client.features.modules.combat.AutoBuffModule;
import sweetie.leonware.client.features.modules.combat.BacktrackModule;
import sweetie.leonware.client.features.modules.combat.elytratarget.ElytraTargetModule;
import sweetie.leonware.client.features.modules.movement.MoveFixModule;
import sweetie.leonware.client.features.modules.player.ElytraSwapModule;

@ModuleRegister(name="Aura", category=Category.COMBAT)
public class AuraModule
extends Module {
    private static final AuraModule instance = new AuraModule();
    private final AIPredictor predictor = new AIPredictor();
    private final TargetManager targetManager = new TargetManager();
    public final CombatExecutor combatExecutor = new CombatExecutor();
    private final Neuro2Model neuro2Model = new Neuro2Model();
    private final Neuro2Rotation neuro2Rotation = new Neuro2Rotation(this.neuro2Model);
    private final ModeSetting aimMode = new ModeSetting("Aim mode").value("Smooth").values("Smooth", "Snap", "HvH", "Aim Assist", "Matrix", "Vulcan", "Spooky Time", "Fun Time", "Holy World", "Lony Grief", "Neuro", "Neuro2").onAction(() -> {
        if (this.getAimMode().is("Neuro")) {
            if (this.predictor.isLoaded()) {
                this.predictor.close();
            }
            this.loadModel();
        } else {
            this.predictor.close();
        }
    });
    private final ModeSetting snapMode = new ModeSetting("Snap mode").values("Smooth", "Legit").value("Smooth").setVisible(() -> this.aimMode.is("Snap"));
    private final SliderSetting distance = new SliderSetting("Distance").value(Float.valueOf(3.0f)).range(2.5f, 6.0f).step(0.1f);
    private final SliderSetting preDistance = new SliderSetting("Pre distance").value(Float.valueOf(0.3f)).range(0.0f, 3.0f).step(0.1f);
    private final SliderSetting fov = new SliderSetting("FOV").value(Float.valueOf(180.0f)).range(1.0f, 180.0f).step(1.0f);
    private final BooleanSetting renderFov = new BooleanSetting("Render FOV").value(false);
    private final SliderSetting snapSpeed = new SliderSetting("Snap Speed").value(Float.valueOf(1.0f)).range(0.3f, 2.0f).step(0.05f).setVisible(() -> this.aimMode.is("Snap") && this.snapMode.is("Legit"));
    private final SliderSetting snapSmoothness = new SliderSetting("Snap Smoothness").value(Float.valueOf(1.0f)).range(0.3f, 2.0f).step(0.05f).setVisible(() -> this.aimMode.is("Snap") && this.snapMode.is("Legit"));
    private final SliderSetting smoothYawSpeed = new SliderSetting("Smooth Yaw Speed").value(Float.valueOf(20.0f)).range(1.0f, 180.0f).step(1.0f).setVisible(() -> this.aimMode.is("Smooth"));
    private final SliderSetting smoothPitchSpeed = new SliderSetting("Smooth Pitch Speed").value(Float.valueOf(10.0f)).range(1.0f, 90.0f).step(1.0f).setVisible(() -> this.aimMode.is("Smooth"));
    private final SliderSetting aimAssistYawMin = new SliderSetting("AA Yaw Min").value(Float.valueOf(10.0f)).range(1.0f, 180.0f).step(1.0f).setVisible(() -> this.aimMode.is("Aim Assist"));
    private final SliderSetting aimAssistYawMax = new SliderSetting("AA Yaw Max").value(Float.valueOf(20.0f)).range(1.0f, 180.0f).step(1.0f).setVisible(() -> this.aimMode.is("Aim Assist"));
    private final SliderSetting aimAssistPitchMin = new SliderSetting("AA Pitch Min").value(Float.valueOf(5.0f)).range(1.0f, 90.0f).step(1.0f).setVisible(() -> this.aimMode.is("Aim Assist"));
    private final SliderSetting aimAssistPitchMax = new SliderSetting("AA Pitch Max").value(Float.valueOf(10.0f)).range(1.0f, 90.0f).step(1.0f).setVisible(() -> this.aimMode.is("Aim Assist"));
    private final SliderSetting aimAssistTimeout = new SliderSetting("AA Timeout (ms)").value(Float.valueOf(500.0f)).range(0.0f, 1000.0f).step(10.0f).setVisible(() -> this.aimMode.is("Aim Assist"));
    private final MultiBooleanSetting targets = new MultiBooleanSetting("Targets").value(new BooleanSetting("Players").value(true), new BooleanSetting("Mobs").value(true), new BooleanSetting("Animals").value(true), new BooleanSetting("Ignore Teams").value(false), new BooleanSetting("Ignore Staff").value(false), new BooleanSetting("Ignore Bots").value(false), new BooleanSetting("Ignore Naked").value(false), new BooleanSetting("Ignore Simple Armor").value(false));
    public final MultiBooleanSetting options = this.combatExecutor.options();
    public final ModeSetting pvpStyle = new ModeSetting("PvP Style").values("1.12+", "1.8.x").value("1.12+");
    public final SliderSetting minCps = new SliderSetting("Min APS").value(Float.valueOf(10.0f)).range(1.0f, 20.0f).step(1.0f).setVisible(() -> this.pvpStyle.is("1.8.x"));
    public final SliderSetting maxCps = new SliderSetting("Max APS").value(Float.valueOf(14.0f)).range(1.0f, 20.0f).step(1.0f).setVisible(() -> this.pvpStyle.is("1.8.x"));
    public final ModeSetting clickPattern = new ModeSetting("Click Pattern").values("Random", "Static", "Jitter", "Interpolation", "Gaussian", "Legit").value("Random").setVisible(() -> this.pvpStyle.is("1.8.x"));
    private final ModeSetting critMode = new ModeSetting("\u0422\u0438\u043f \u043a\u0440\u0438\u0442\u0430").values("\u0411\u044b\u0441\u0442\u0440\u044b\u0439", "\u041b\u0435\u0433\u0438\u0442\u043d\u044b\u0439").value("\u0411\u044b\u0441\u0442\u0440\u044b\u0439").setVisible(() -> this.options.isEnabled("Only Crits"));
    private final BooleanSetting hitInBacktrack = new BooleanSetting("Hit in Backtrack").value(false).setVisible(() -> BacktrackModule.getInstance().isEnabled() && !BacktrackModule.getInstance().isDelayP());
    private final BooleanSetting legitHits = new BooleanSetting("Legit delay").value(false);
    private final SliderSetting legitDelayMin = new SliderSetting("Legit Min").value(Float.valueOf(40.0f)).range(0.0f, 150.0f).step(1.0f).setVisible(this.legitHits::getValue);
    private final SliderSetting legitDelayMax = new SliderSetting("Legit Max").value(Float.valueOf(120.0f)).range(0.0f, 150.0f).step(1.0f).setVisible(this.legitHits::getValue);
    private final BooleanSetting dontDropSprintInWater = new BooleanSetting("Save sprint in water").value(false);
    private final BooleanSetting deathDisable = new BooleanSetting("Disable on Death").value(false);
    private final MultiBooleanSetting autoMaceOptions = new MultiBooleanSetting("Auto Mace").value(new BooleanSetting("Enabled").value(false), new BooleanSetting("Only Players").value(true), new BooleanSetting("Swap Elytra").value(false));
    private final SliderSetting minFallDistance = new SliderSetting("Min Fall Distance").value(Float.valueOf(3.0f)).range(0.0f, 20.0f).step(0.5f).setVisible(() -> this.autoMaceOptions.isEnabled("Enabled"));
    private final SliderSetting maceDelay = new SliderSetting("Mace Delay").value(Float.valueOf(0.0f)).range(0.0f, 5.0f).step(1.0f).setVisible(() -> this.autoMaceOptions.isEnabled("Enabled"));
    private final BooleanSetting clientLook = new BooleanSetting("Client look").value(false);
    private final BooleanSetting elytraOverride = new BooleanSetting("Elytra override").value(false);
    private final SliderSetting elytraDistance = new SliderSetting("Elytra distance").value(Float.valueOf(3.0f)).range(2.5f, 6.0f).step(0.1f).setVisible(this.elytraOverride::getValue);
    private final SliderSetting elytraPreDistance = new SliderSetting("Elytra pre distance").value(Float.valueOf(16.0f)).range(0.0f, 32.0f).step(0.1f).setVisible(this.elytraOverride::getValue);
    public class_1309 target;

    public AuraModule() {
        this.addSettings(this.aimMode, this.snapMode, this.distance, this.preDistance, this.fov, this.renderFov, this.smoothYawSpeed, this.smoothPitchSpeed, this.snapSpeed, this.snapSmoothness, this.aimAssistYawMin, this.aimAssistYawMax, this.aimAssistPitchMin, this.aimAssistPitchMax, this.aimAssistTimeout, this.targets, this.options, this.critMode, this.pvpStyle, this.minCps, this.maxCps, this.clickPattern, this.hitInBacktrack, this.legitHits, this.legitDelayMin, this.legitDelayMax, this.dontDropSprintInWater, this.autoMaceOptions, this.minFallDistance, this.maceDelay, this.clientLook, this.elytraOverride, this.elytraDistance, this.elytraPreDistance, this.deathDisable);
    }

    public float getPreDistance() {
        return (AuraModule.mc.field_1724.method_6128() && (Boolean)this.elytraOverride.getValue() != false ? (Float)this.elytraPreDistance.getValue() : (Float)this.preDistance.getValue()).floatValue();
    }

    public float getAttackDistance() {
        return (AuraModule.mc.field_1724.method_6128() && (Boolean)this.elytraOverride.getValue() != false ? (Float)this.elytraDistance.getValue() : (Float)this.distance.getValue()).floatValue();
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
        EventListener eventUpdate = UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> this.updateEventHandler()));
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(eventData -> {
            class_2663 pac;
            class_2596<?> patt0$temp = eventData.packet();
            if (patt0$temp instanceof class_2663 && (pac = (class_2663)patt0$temp).method_11470() == 3 && pac.method_11469((class_1937)AuraModule.mc.field_1687) == AuraModule.mc.field_1724 && ((Boolean)this.deathDisable.getValue()).booleanValue()) {
                this.disable();
            }
        }));
        EventListener disconnectEvent = DisconnectEvent.getInstance().subscribe(new Listener<DisconnectEvent.DisconnectData>(event -> this.disable()));
        this.addEvents(this.predictor.getEventListeners());
        this.addEvents(eventUpdate, packetEvent, disconnectEvent, RotationUpdateEvent.getInstance().subscribe(new Listener<RotationUpdateEvent>(e -> this.postRotMoveEventHandler())), AttackEvent.getInstance().subscribe(new Listener<AttackEvent.AttackEventData>(e -> AuraUtil.onAttack((String)this.aimMode.getValue()))), Render2DEvent.getInstance().subscribe(new Listener<Render2DEvent.Render2DEventData>(event -> {
            if (((Boolean)this.renderFov.getValue()).booleanValue() && AuraModule.mc.field_1724 != null) {
                float radius = ((Float)this.fov.getValue()).floatValue() / 180.0f * ((float)mc.method_22683().method_4486() / 2.0f);
                float x = (float)mc.method_22683().method_4486() / 2.0f;
                float y = (float)mc.method_22683().method_4502() / 2.0f;
                Render2DEngine.drawCircle(event.matrixStack(), x, y, radius, 1.0f, new Color(255, 255, 255, 100).getRGB());
            }
        })));
    }

    private void postRotMoveEventHandler() {
        if (this.target == null || AutoBuffModule.getInstance().isBuffing()) {
            return;
        }
        class_243 attackVector = this.getTargetVector(this.target);
        Rotation rotation = RotationUtil.fromVec3d(attackVector.method_1020(AuraModule.mc.field_1724.method_33571()));
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
            boolean isFalling;
            boolean hasMace = InventoryUtil.findMaceBetter() != -1;
            boolean bl = isFalling = !AuraModule.mc.field_1724.method_24828() && AuraModule.mc.field_1724.method_18798().field_1351 < -0.1;
            if (hasMace && isFalling && InventoryUtil.hasElytraEquipped()) {
                if (SlownessManager.isEnabled()) {
                    SlownessManager.applySlowness(15L, () -> ElytraSwapModule.getInstance().swapChestplate());
                } else {
                    ElytraSwapModule.getInstance().swapChestplate();
                }
            }
        }
        if (this.target == null) {
            this.combatExecutor.combatManager().configurable(null);
            return;
        }
        class_243 targetPosition = this.getTargetVector(this.target);
        if (targetPosition.method_1022(AuraModule.mc.field_1724.method_33571()) > (double)(this.getAttackDistance() + this.getPreDistance())) {
            this.targetManager.releaseTarget();
            this.combatExecutor.combatManager().configurable(null);
            return;
        }
        this.attackTarget(this.target);
    }

    private class_1309 updateTarget() {
        TargetManager.EntityFilter filter = new TargetManager.EntityFilter(this.targets.getList());
        this.targetManager.searchTargets(AuraModule.mc.field_1687.method_18112(), this.getAttackDistance() + this.getPreDistance());
        this.targetManager.validateTarget(entity -> {
            if (!filter.isValid((class_1309)entity)) {
                return false;
            }
            class_243 targetVec = this.getTargetVector((class_1309)entity);
            Rotation needed = RotationUtil.fromVec3d(targetVec.method_1020(AuraModule.mc.field_1724.method_33571()));
            float yawDiff = Math.abs(class_3532.method_15393((float)(AuraModule.mc.field_1724.method_36454() - needed.getYaw())));
            return !(yawDiff > ((Float)this.fov.getValue()).floatValue());
        });
        return this.targetManager.getCurrentTarget();
    }

    private void attackTarget(class_1309 target) {
        if (target == null) {
            return;
        }
        this.combatExecutor.combatManager().configurable(new CombatExecutor.CombatConfigurable(target, RotationManager.getInstance().getRotation(), this.getAttackDistance(), this.options.getList()).withMace(this.autoMaceOptions.isEnabled("Enabled"), Math.round(((Float)this.maceDelay.getValue()).floatValue()), ((Float)this.minFallDistance.getValue()).floatValue(), this.autoMaceOptions.isEnabled("Only Players"), this.autoMaceOptions.isEnabled("Swap Elytra")));
        class_243 targetPosition = this.getTargetVector(target);
        if (AuraModule.mc.field_1724.method_33571().method_1022(RotationUtil.rayCastBox((class_1297)target, targetPosition)) > (double)this.getAttackDistance()) {
            return;
        }
        this.combatExecutor.performAttack();
    }

    private void rotateToTarget(class_1309 target, class_243 targetVec, Rotation rotation) {
        boolean noHitRule;
        boolean isAimAssist = this.aimMode.is("Aim Assist");
        RotationMode mode = this.getRotationMode();
        if (((Boolean)this.clientLook.getValue()).booleanValue() && !isAimAssist) {
            mode = new ClientLookRotation(mode, 0.35f);
        }
        RotationStrategy configurable = new RotationStrategy(mode, MoveFixModule.enabled(), MoveFixModule.isFree()).clientLook((Boolean)this.clientLook.getValue() != false || isAimAssist);
        boolean bl = noHitRule = !this.combatExecutor.combatManager().canAttack();
        if (this.usingElytraTarget() && ((Boolean)ElytraTargetModule.getInstance().elytraRotationProcessor.customRotations.getValue()).booleanValue()) {
            return;
        }
        if (noHitRule && this.aimMode.is("Snap")) {
            if (!MoveFixModule.getInstance().isEnabled() || !((Boolean)MoveFixModule.getInstance().targeting.getValue()).booleanValue()) {
                return;
            }
            rotation = new Rotation(AuraModule.mc.field_1724.method_36454(), AuraModule.mc.field_1724.method_36455());
        }
        RotationManager.getInstance().addRotation(new Rotation.VecRotation(rotation, targetVec), target, configurable, TaskPriority.HIGH, this);
    }

    private RotationMode getRotationMode() {
        return switch ((String)this.aimMode.getValue()) {
            case "Snap" -> {
                if (this.snapMode.is("Legit")) {
                    yield new LegitSnapRotation(((Float)this.snapSpeed.getValue()).floatValue(), ((Float)this.snapSmoothness.getValue()).floatValue());
                }
                yield new SmoothRotation(((Float)this.smoothYawSpeed.getValue()).floatValue(), ((Float)this.smoothPitchSpeed.getValue()).floatValue());
            }
            case "HvH" -> new InstantRotation();
            case "Aim Assist" -> new AimAssistRotation(((Float)this.aimAssistYawMin.getValue()).floatValue(), ((Float)this.aimAssistYawMax.getValue()).floatValue(), ((Float)this.aimAssistPitchMin.getValue()).floatValue(), ((Float)this.aimAssistPitchMax.getValue()).floatValue(), ((Float)this.aimAssistTimeout.getValue()).floatValue());
            case "Spooky Time" -> new SpookyTimeRotation(this.combatExecutor.combatManager().clickScheduler());
            case "Holy World" -> new UniversalRotation(MathUtil.randomInRange(125, 155), MathUtil.randomInRange(30, 60), true, true);
            case "Vulcan" -> new UniversalRotation(120.0f, 60.0f, false, false);
            case "Lony Grief" -> new LonyGriefRotation();
            case "Fun Time" -> new FunTimeRotation();
            case "Matrix" -> new MatrixRotation();
            case "Neuro" -> new NeuroRotation(this.predictor, 70.0f, 10.0f);
            case "Neuro2" -> this.neuro2Rotation;
            default -> new SmoothRotation(((Float)this.smoothYawSpeed.getValue()).floatValue(), ((Float)this.smoothPitchSpeed.getValue()).floatValue());
        };
    }

    private class_243 getTargetVector(class_1309 target) {
        BacktrackModule.Position backtrackPos;
        IBacktrackable backtrackable;
        ArrayDeque<BacktrackModule.Position> tracks;
        if (target == null) {
            return class_243.field_1353;
        }
        if (this.usingElytraTarget()) {
            return ElytraTargetModule.getInstance().elytraRotationProcessor.getPredictedPos(target);
        }
        if (((Boolean)this.hitInBacktrack.getValue()).booleanValue() && BacktrackModule.getInstance().isEnabled() && target instanceof IBacktrackable && !(tracks = (backtrackable = (IBacktrackable)target).leonware$getBackTracks()).isEmpty() && (backtrackPos = tracks.peekFirst()) != null) {
            return backtrackPos.pos().method_1031(0.0, (double)(target.method_17682() / 2.0f), 0.0);
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
        return (Boolean)this.legitHits.getValue();
    }

    public float getLegitDelayMin() {
        return ((Float)this.legitDelayMin.getValue()).floatValue();
    }

    public float getLegitDelayMax() {
        return ((Float)this.legitDelayMax.getValue()).floatValue();
    }

    public boolean isHitInBacktrack() {
        return (Boolean)this.hitInBacktrack.getValue() != false && !BacktrackModule.getInstance().isDelayP();
    }

    public boolean isDontDropSprintInWater() {
        return (Boolean)this.dontDropSprintInWater.getValue();
    }

    @Generated
    public static AuraModule getInstance() {
        return instance;
    }

    @Generated
    public Neuro2Model getNeuro2Model() {
        return this.neuro2Model;
    }

    @Generated
    public ModeSetting getAimMode() {
        return this.aimMode;
    }
}

