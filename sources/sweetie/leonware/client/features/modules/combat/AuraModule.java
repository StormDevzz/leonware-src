package sweetie.leonware.client.features.modules.combat;

import java.awt.Color;
import java.util.ArrayDeque;
import java.util.Objects;
import lombok.Generated;
import net.minecraft.class_1309;
import net.minecraft.class_243;
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
import sweetie.leonware.client.features.modules.combat.BacktrackModule;
import sweetie.leonware.client.features.modules.combat.elytratarget.ElytraTargetModule;
import sweetie.leonware.client.features.modules.movement.MoveFixModule;
import sweetie.leonware.client.features.modules.player.ElytraSwapModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/AuraModule.class */
@ModuleRegister(name = "Aura", category = Category.COMBAT)
public class AuraModule extends Module {
    private static final AuraModule instance = new AuraModule();
    private final AIPredictor predictor = new AIPredictor();
    private final TargetManager targetManager = new TargetManager();
    public final CombatExecutor combatExecutor = new CombatExecutor();
    private final Neuro2Model neuro2Model = new Neuro2Model();
    private final Neuro2Rotation neuro2Rotation = new Neuro2Rotation(this.neuro2Model);
    private final ModeSetting aimMode = new ModeSetting("Aim mode").value("Smooth").values("Smooth", "Snap", "HvH", "Aim Assist", "Matrix", "Vulcan", "Spooky Time", "Fun Time", "Holy World", "Lony Grief", "Neuro", "Neuro2").onAction2(() -> {
        if (getAimMode().is("Neuro")) {
            if (this.predictor.isLoaded()) {
                this.predictor.close();
            }
            loadModel();
            return;
        }
        this.predictor.close();
    });
    private final ModeSetting snapMode = new ModeSetting("Snap mode").values("Smooth", "Legit").value("Smooth").setVisible(() -> {
        return Boolean.valueOf(this.aimMode.is("Snap"));
    });
    private final SliderSetting distance = new SliderSetting("Distance").value(Float.valueOf(3.0f)).range(2.5f, 6.0f).step(0.1f);
    private final SliderSetting preDistance = new SliderSetting("Pre distance").value(Float.valueOf(0.3f)).range(0.0f, 3.0f).step(0.1f);
    private final SliderSetting fov = new SliderSetting("FOV").value(Float.valueOf(180.0f)).range(1.0f, 180.0f).step(1.0f);
    private final BooleanSetting renderFov = new BooleanSetting("Render FOV").value((Boolean) false);
    private final SliderSetting snapSpeed = new SliderSetting("Snap Speed").value(Float.valueOf(1.0f)).range(0.3f, 2.0f).step(0.05f).setVisible(() -> {
        return Boolean.valueOf(this.aimMode.is("Snap") && this.snapMode.is("Legit"));
    });
    private final SliderSetting snapSmoothness = new SliderSetting("Snap Smoothness").value(Float.valueOf(1.0f)).range(0.3f, 2.0f).step(0.05f).setVisible(() -> {
        return Boolean.valueOf(this.aimMode.is("Snap") && this.snapMode.is("Legit"));
    });
    private final SliderSetting smoothYawSpeed = new SliderSetting("Smooth Yaw Speed").value(Float.valueOf(20.0f)).range(1.0f, 180.0f).step(1.0f).setVisible(() -> {
        return Boolean.valueOf(this.aimMode.is("Smooth"));
    });
    private final SliderSetting smoothPitchSpeed = new SliderSetting("Smooth Pitch Speed").value(Float.valueOf(10.0f)).range(1.0f, 90.0f).step(1.0f).setVisible(() -> {
        return Boolean.valueOf(this.aimMode.is("Smooth"));
    });
    private final SliderSetting aimAssistYawMin = new SliderSetting("AA Yaw Min").value(Float.valueOf(10.0f)).range(1.0f, 180.0f).step(1.0f).setVisible(() -> {
        return Boolean.valueOf(this.aimMode.is("Aim Assist"));
    });
    private final SliderSetting aimAssistYawMax = new SliderSetting("AA Yaw Max").value(Float.valueOf(20.0f)).range(1.0f, 180.0f).step(1.0f).setVisible(() -> {
        return Boolean.valueOf(this.aimMode.is("Aim Assist"));
    });
    private final SliderSetting aimAssistPitchMin = new SliderSetting("AA Pitch Min").value(Float.valueOf(5.0f)).range(1.0f, 90.0f).step(1.0f).setVisible(() -> {
        return Boolean.valueOf(this.aimMode.is("Aim Assist"));
    });
    private final SliderSetting aimAssistPitchMax = new SliderSetting("AA Pitch Max").value(Float.valueOf(10.0f)).range(1.0f, 90.0f).step(1.0f).setVisible(() -> {
        return Boolean.valueOf(this.aimMode.is("Aim Assist"));
    });
    private final SliderSetting aimAssistTimeout = new SliderSetting("AA Timeout (ms)").value(Float.valueOf(500.0f)).range(0.0f, 1000.0f).step(10.0f).setVisible(() -> {
        return Boolean.valueOf(this.aimMode.is("Aim Assist"));
    });
    private final MultiBooleanSetting targets = new MultiBooleanSetting("Targets").value(new BooleanSetting("Players").value((Boolean) true), new BooleanSetting("Mobs").value((Boolean) true), new BooleanSetting("Animals").value((Boolean) true), new BooleanSetting("Ignore Teams").value((Boolean) false), new BooleanSetting("Ignore Staff").value((Boolean) false), new BooleanSetting("Ignore Bots").value((Boolean) false), new BooleanSetting("Ignore Naked").value((Boolean) false), new BooleanSetting("Ignore Simple Armor").value((Boolean) false));
    public final MultiBooleanSetting options = this.combatExecutor.options();
    public final ModeSetting pvpStyle = new ModeSetting("PvP Style").values("1.12+", "1.8.x").value("1.12+");
    public final SliderSetting minCps = new SliderSetting("Min APS").value(Float.valueOf(10.0f)).range(1.0f, 20.0f).step(1.0f).setVisible(() -> {
        return Boolean.valueOf(this.pvpStyle.is("1.8.x"));
    });
    public final SliderSetting maxCps = new SliderSetting("Max APS").value(Float.valueOf(14.0f)).range(1.0f, 20.0f).step(1.0f).setVisible(() -> {
        return Boolean.valueOf(this.pvpStyle.is("1.8.x"));
    });
    public final ModeSetting clickPattern = new ModeSetting("Click Pattern").values("Random", "Static", "Jitter", "Interpolation", "Gaussian", "Legit").value("Random").setVisible(() -> {
        return Boolean.valueOf(this.pvpStyle.is("1.8.x"));
    });
    private final ModeSetting critMode = new ModeSetting("Тип крита").values("Быстрый", "Легитный").value("Быстрый").setVisible(() -> {
        return Boolean.valueOf(this.options.isEnabled("Only Crits"));
    });
    private final BooleanSetting hitInBacktrack = new BooleanSetting("Hit in Backtrack").value((Boolean) false).setVisible(() -> {
        return Boolean.valueOf(BacktrackModule.getInstance().isEnabled() && !BacktrackModule.getInstance().isDelayP());
    });
    private final BooleanSetting legitHits = new BooleanSetting("Legit delay").value((Boolean) false);
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

    /* JADX WARN: Type inference failed for: r1v100, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v107, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v112, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v12, types: [sweetie.leonware.api.module.setting.ModeSetting] */
    /* JADX WARN: Type inference failed for: r1v123, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v128, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v137, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v142, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v31, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v36, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v41, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v46, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v51, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v56, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v61, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v66, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v71, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v8, types: [sweetie.leonware.api.module.setting.ModeSetting] */
    /* JADX WARN: Type inference failed for: r1v84, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v89, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v93, types: [sweetie.leonware.api.module.setting.ModeSetting] */
    /* JADX WARN: Type inference failed for: r1v97, types: [sweetie.leonware.api.module.setting.ModeSetting] */
    public AuraModule() {
        SliderSetting sliderSettingStep = new SliderSetting("Legit Min").value(Float.valueOf(40.0f)).range(0.0f, 150.0f).step(1.0f);
        BooleanSetting booleanSetting = this.legitHits;
        Objects.requireNonNull(booleanSetting);
        this.legitDelayMin = sliderSettingStep.setVisible(booleanSetting::getValue);
        SliderSetting sliderSettingStep2 = new SliderSetting("Legit Max").value(Float.valueOf(120.0f)).range(0.0f, 150.0f).step(1.0f);
        BooleanSetting booleanSetting2 = this.legitHits;
        Objects.requireNonNull(booleanSetting2);
        this.legitDelayMax = sliderSettingStep2.setVisible(booleanSetting2::getValue);
        this.dontDropSprintInWater = new BooleanSetting("Save sprint in water").value((Boolean) false);
        this.deathDisable = new BooleanSetting("Disable on Death").value((Boolean) false);
        this.autoMaceOptions = new MultiBooleanSetting("Auto Mace").value(new BooleanSetting("Enabled").value((Boolean) false), new BooleanSetting("Only Players").value((Boolean) true), new BooleanSetting("Swap Elytra").value((Boolean) false));
        this.minFallDistance = new SliderSetting("Min Fall Distance").value(Float.valueOf(3.0f)).range(0.0f, 20.0f).step(0.5f).setVisible(() -> {
            return Boolean.valueOf(this.autoMaceOptions.isEnabled("Enabled"));
        });
        this.maceDelay = new SliderSetting("Mace Delay").value(Float.valueOf(0.0f)).range(0.0f, 5.0f).step(1.0f).setVisible(() -> {
            return Boolean.valueOf(this.autoMaceOptions.isEnabled("Enabled"));
        });
        this.clientLook = new BooleanSetting("Client look").value((Boolean) false);
        this.elytraOverride = new BooleanSetting("Elytra override").value((Boolean) false);
        SliderSetting sliderSettingStep3 = new SliderSetting("Elytra distance").value(Float.valueOf(3.0f)).range(2.5f, 6.0f).step(0.1f);
        BooleanSetting booleanSetting3 = this.elytraOverride;
        Objects.requireNonNull(booleanSetting3);
        this.elytraDistance = sliderSettingStep3.setVisible(booleanSetting3::getValue);
        SliderSetting sliderSettingStep4 = new SliderSetting("Elytra pre distance").value(Float.valueOf(16.0f)).range(0.0f, 32.0f).step(0.1f);
        BooleanSetting booleanSetting4 = this.elytraOverride;
        Objects.requireNonNull(booleanSetting4);
        this.elytraPreDistance = sliderSettingStep4.setVisible(booleanSetting4::getValue);
        addSettings(this.aimMode, this.snapMode, this.distance, this.preDistance, this.fov, this.renderFov, this.smoothYawSpeed, this.smoothPitchSpeed, this.snapSpeed, this.snapSmoothness, this.aimAssistYawMin, this.aimAssistYawMax, this.aimAssistPitchMin, this.aimAssistPitchMax, this.aimAssistTimeout, this.targets, this.options, this.critMode, this.pvpStyle, this.minCps, this.maxCps, this.clickPattern, this.hitInBacktrack, this.legitHits, this.legitDelayMin, this.legitDelayMax, this.dontDropSprintInWater, this.autoMaceOptions, this.minFallDistance, this.maceDelay, this.clientLook, this.elytraOverride, this.elytraDistance, this.elytraPreDistance, this.deathDisable);
    }

    public float getPreDistance() {
        return ((mc.field_1724.method_6128() && this.elytraOverride.getValue().booleanValue()) ? this.elytraPreDistance.getValue() : this.preDistance.getValue()).floatValue();
    }

    public float getAttackDistance() {
        return ((mc.field_1724.method_6128() && this.elytraOverride.getValue().booleanValue()) ? this.elytraDistance.getValue() : this.distance.getValue()).floatValue();
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        this.targetManager.releaseTarget();
        this.target = null;
        this.predictor.close();
        this.combatExecutor.combatManager().reset();
    }

    @Override // sweetie.leonware.api.module.Module
    public void onEnable() {
        this.targetManager.releaseTarget();
        this.target = null;
        this.combatExecutor.combatManager().reset();
        if (this.aimMode.is("Neuro") && !this.predictor.isLoaded()) {
            loadModel();
        }
    }

    public void loadModel() {
        this.predictor.loadModel("Default");
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        this.predictor.onEvent();
        EventListener eventUpdate = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            updateEventHandler();
        }));
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener(eventData -> {
            class_2663 class_2663VarPacket = eventData.packet();
            if (class_2663VarPacket instanceof class_2663) {
                class_2663 pac = class_2663VarPacket;
                if (pac.method_11470() == 3 && pac.method_11469(mc.field_1687) == mc.field_1724 && this.deathDisable.getValue().booleanValue()) {
                    disable();
                }
            }
        }));
        EventListener disconnectEvent = DisconnectEvent.getInstance().subscribe(new Listener(event2 -> {
            disable();
        }));
        addEvents(this.predictor.getEventListeners());
        addEvents(eventUpdate, packetEvent, disconnectEvent, RotationUpdateEvent.getInstance().subscribe(new Listener(e -> {
            postRotMoveEventHandler();
        })), AttackEvent.getInstance().subscribe(new Listener(e2 -> {
            AuraUtil.onAttack(this.aimMode.getValue());
        })), Render2DEvent.getInstance().subscribe(new Listener(event3 -> {
            if (this.renderFov.getValue().booleanValue() && mc.field_1724 != null) {
                float radius = (this.fov.getValue().floatValue() / 180.0f) * (mc.method_22683().method_4486() / 2.0f);
                float x = mc.method_22683().method_4486() / 2.0f;
                float y = mc.method_22683().method_4502() / 2.0f;
                Render2DEngine.drawCircle(event3.matrixStack(), x, y, radius, 1.0f, new Color(255, 255, 255, 100).getRGB());
            }
        })));
    }

    private void postRotMoveEventHandler() {
        if (this.target == null || AutoBuffModule.getInstance().isBuffing()) {
            return;
        }
        class_243 attackVector = getTargetVector(this.target);
        Rotation rotation = RotationUtil.fromVec3d(attackVector.method_1020(mc.field_1724.method_33571()));
        rotateToTarget(this.target, attackVector, rotation);
    }

    private void updateEventHandler() {
        if (AutoBuffModule.getInstance().isBuffing()) {
            this.targetManager.releaseTarget();
            this.target = null;
            this.combatExecutor.combatManager().configurable(null);
            return;
        }
        this.target = updateTarget();
        if (this.target != null && this.autoMaceOptions.isEnabled("Enabled") && this.autoMaceOptions.isEnabled("Swap Elytra")) {
            boolean hasMace = InventoryUtil.findMaceBetter() != -1;
            boolean isFalling = !mc.field_1724.method_24828() && mc.field_1724.method_18798().field_1351 < -0.1d;
            if (hasMace && isFalling && InventoryUtil.hasElytraEquipped()) {
                if (SlownessManager.isEnabled()) {
                    SlownessManager.applySlowness(15L, () -> {
                        ElytraSwapModule.getInstance().swapChestplate();
                    });
                } else {
                    ElytraSwapModule.getInstance().swapChestplate();
                }
            }
        }
        if (this.target == null) {
            this.combatExecutor.combatManager().configurable(null);
            return;
        }
        class_243 targetPosition = getTargetVector(this.target);
        if (targetPosition.method_1022(mc.field_1724.method_33571()) > getAttackDistance() + getPreDistance()) {
            this.targetManager.releaseTarget();
            this.combatExecutor.combatManager().configurable(null);
        } else {
            attackTarget(this.target);
        }
    }

    private class_1309 updateTarget() {
        TargetManager.EntityFilter filter = new TargetManager.EntityFilter(this.targets.getList());
        this.targetManager.searchTargets(mc.field_1687.method_18112(), getAttackDistance() + getPreDistance());
        this.targetManager.validateTarget(entity -> {
            if (!filter.isValid(entity)) {
                return false;
            }
            class_243 targetVec = getTargetVector(entity);
            Rotation needed = RotationUtil.fromVec3d(targetVec.method_1020(mc.field_1724.method_33571()));
            float yawDiff = Math.abs(class_3532.method_15393(mc.field_1724.method_36454() - needed.getYaw()));
            return yawDiff <= this.fov.getValue().floatValue();
        });
        return this.targetManager.getCurrentTarget();
    }

    private void attackTarget(class_1309 target) {
        if (target == null) {
            return;
        }
        this.combatExecutor.combatManager().configurable(new CombatExecutor.CombatConfigurable(target, RotationManager.getInstance().getRotation(), getAttackDistance(), this.options.getList()).withMace(this.autoMaceOptions.isEnabled("Enabled"), Math.round(this.maceDelay.getValue().floatValue()), this.minFallDistance.getValue().floatValue(), this.autoMaceOptions.isEnabled("Only Players"), this.autoMaceOptions.isEnabled("Swap Elytra")));
        class_243 targetPosition = getTargetVector(target);
        if (mc.field_1724.method_33571().method_1022(RotationUtil.rayCastBox(target, targetPosition)) > getAttackDistance()) {
            return;
        }
        this.combatExecutor.performAttack();
    }

    private void rotateToTarget(class_1309 target, class_243 targetVec, Rotation rotation) {
        boolean isAimAssist = this.aimMode.is("Aim Assist");
        RotationMode mode = getRotationMode();
        if (this.clientLook.getValue().booleanValue() && !isAimAssist) {
            mode = new ClientLookRotation(mode, 0.35f);
        }
        RotationStrategy configurable = new RotationStrategy(mode, MoveFixModule.enabled(), MoveFixModule.isFree()).clientLook(this.clientLook.getValue().booleanValue() || isAimAssist);
        boolean noHitRule = !this.combatExecutor.combatManager().canAttack();
        if (usingElytraTarget() && ElytraTargetModule.getInstance().elytraRotationProcessor.customRotations.getValue().booleanValue()) {
            return;
        }
        if (noHitRule && this.aimMode.is("Snap")) {
            if (!MoveFixModule.getInstance().isEnabled() || !MoveFixModule.getInstance().targeting.getValue().booleanValue()) {
                return;
            } else {
                rotation = new Rotation(mc.field_1724.method_36454(), mc.field_1724.method_36455());
            }
        }
        RotationManager.getInstance().addRotation(new Rotation.VecRotation(rotation, targetVec), target, configurable, TaskPriority.HIGH, this);
    }

    private RotationMode getRotationMode() {
        switch (this.aimMode.getValue()) {
            case "Snap":
                if (this.snapMode.is("Legit")) {
                    return new LegitSnapRotation(this.snapSpeed.getValue().floatValue(), this.snapSmoothness.getValue().floatValue());
                }
                return new SmoothRotation(this.smoothYawSpeed.getValue().floatValue(), this.smoothPitchSpeed.getValue().floatValue());
            case "HvH":
                return new InstantRotation();
            case "Aim Assist":
                return new AimAssistRotation(this.aimAssistYawMin.getValue().floatValue(), this.aimAssistYawMax.getValue().floatValue(), this.aimAssistPitchMin.getValue().floatValue(), this.aimAssistPitchMax.getValue().floatValue(), this.aimAssistTimeout.getValue().floatValue());
            case "Spooky Time":
                return new SpookyTimeRotation(this.combatExecutor.combatManager().clickScheduler());
            case "Holy World":
                return new UniversalRotation(MathUtil.randomInRange(125, 155), MathUtil.randomInRange(30, 60), true, true);
            case "Vulcan":
                return new UniversalRotation(120.0f, 60.0f, false, false);
            case "Lony Grief":
                return new LonyGriefRotation();
            case "Fun Time":
                return new FunTimeRotation();
            case "Matrix":
                return new MatrixRotation();
            case "Neuro":
                return new NeuroRotation(this.predictor, 70.0f, 10.0f);
            case "Neuro2":
                return this.neuro2Rotation;
            default:
                return new SmoothRotation(this.smoothYawSpeed.getValue().floatValue(), this.smoothPitchSpeed.getValue().floatValue());
        }
    }

    private class_243 getTargetVector(class_1309 target) {
        BacktrackModule.Position backtrackPos;
        if (target == null) {
            return class_243.field_1353;
        }
        if (usingElytraTarget()) {
            return ElytraTargetModule.getInstance().elytraRotationProcessor.getPredictedPos(target);
        }
        if (this.hitInBacktrack.getValue().booleanValue() && BacktrackModule.getInstance().isEnabled() && (target instanceof IBacktrackable)) {
            IBacktrackable backtrackable = (IBacktrackable) target;
            ArrayDeque<BacktrackModule.Position> tracks = backtrackable.leonware$getBackTracks();
            if (!tracks.isEmpty() && (backtrackPos = tracks.peekFirst()) != null) {
                return backtrackPos.pos().method_1031(0.0d, target.method_17682() / 2.0f, 0.0d);
            }
        }
        return target.method_19538().method_1031(0.0d, target.method_17682() / 2.0f, 0.0d);
    }

    private boolean usingElytraTarget() {
        return ElytraTargetModule.getInstance().isEnabled() && mc.field_1724.method_6128();
    }

    public boolean isLegitCrit() {
        return this.critMode.is("Легитный");
    }

    public boolean isLegitHits() {
        return this.legitHits.getValue().booleanValue();
    }

    public float getLegitDelayMin() {
        return this.legitDelayMin.getValue().floatValue();
    }

    public float getLegitDelayMax() {
        return this.legitDelayMax.getValue().floatValue();
    }

    public boolean isHitInBacktrack() {
        return this.hitInBacktrack.getValue().booleanValue() && !BacktrackModule.getInstance().isDelayP();
    }

    public boolean isDontDropSprintInWater() {
        return this.dontDropSprintInWater.getValue().booleanValue();
    }
}
