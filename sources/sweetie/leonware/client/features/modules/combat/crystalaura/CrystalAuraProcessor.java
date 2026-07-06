package sweetie.leonware.client.features.modules.combat.crystalaura;

import java.awt.Color;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import net.minecraft.class_1268;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1303;
import net.minecraft.class_1511;
import net.minecraft.class_1657;
import net.minecraft.class_1713;
import net.minecraft.class_1743;
import net.minecraft.class_1774;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1810;
import net.minecraft.class_1821;
import net.minecraft.class_1829;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2604;
import net.minecraft.class_2606;
import net.minecraft.class_2664;
import net.minecraft.class_2815;
import net.minecraft.class_2824;
import net.minecraft.class_2868;
import net.minecraft.class_2879;
import net.minecraft.class_3532;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_640;
import net.minecraft.class_746;
import org.joml.Vector2f;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.event.events.render.Render2DEvent;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.setting.BindSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.system.backend.KeyStorage;
import sweetie.leonware.api.system.configs.FriendManager;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.math.PredictUtility;
import sweetie.leonware.api.utils.math.ProjectionUtil;
import sweetie.leonware.api.utils.player.InventoryUtil;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.display.BoxRender;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.utils.rotation.manager.RotationStrategy;
import sweetie.leonware.api.utils.rotation.rotations.InstantRotation;
import sweetie.leonware.api.utils.rotation.rotations.SmoothRotation;
import sweetie.leonware.api.utils.task.TaskPriority;
import sweetie.leonware.api.utils.world.ExplosionUtility;
import sweetie.leonware.client.features.modules.combat.SurroundModule;
import sweetie.leonware.client.features.modules.movement.MoveFixModule;
import sweetie.leonware.client.features.modules.player.ClickPearlModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor.class */
public class CrystalAuraProcessor {
    private static final float RENDER_BOX_SIZE = 2.0f;
    private final CrystalAuraModule module;
    private class_1657 target;
    private PlaceData currentData;
    private class_3965 bestPosition;
    private class_1511 bestCrystal;
    private class_1511 secondaryCrystal;
    private RotationVec rotationVec;
    private float renderDamage;
    private float renderSelfDamage;
    private float blockRenderFactor;
    private int rotationTicks;
    private boolean rotated;
    private boolean rotating;
    private boolean facePlacing;
    private boolean placedOnSpawn;
    private boolean initialized;
    private long confirmTime;
    private long calcTime;
    private long renderMultiplier;
    private long currentId;
    private int placeTicks;
    private int breakTicks;
    private int calcTicks;
    private int placeSyncTicks;
    private class_2338 renderPos;
    private class_2338 prevRenderPos;
    public final ModeSetting page = new ModeSetting("Page").values("Main", "Place", "Break", "Damages", "Pause", "Render", "Switch", "FailSafe", "Info", "IDPredict").value("Main");
    public final BooleanSetting await = new BooleanSetting("Await").value((Boolean) true).setVisible(() -> {
        return Boolean.valueOf(this.page.is("Main"));
    });
    public final ModeSetting timing = new ModeSetting("Timing").values("NORMAL", "SEQUENTIAL").value("NORMAL").setVisible(() -> {
        return Boolean.valueOf(this.page.is("Main"));
    });
    public final ModeSetting sequential = new ModeSetting("Sequential").values("Off", "Strict", "Strong").value("Strong").setVisible(() -> {
        return Boolean.valueOf(this.page.is("Main"));
    });
    public final ModeSetting rotate = new ModeSetting("Rotate").values("Off", "Instant", "Smooth").value("Smooth").setVisible(() -> {
        return Boolean.valueOf(this.page.is("Main"));
    });
    public final BooleanSetting yawStep = new BooleanSetting("YawStep").value((Boolean) false).setVisible(() -> {
        return Boolean.valueOf(!this.rotate.is("Off") && this.page.is("Main"));
    });
    public final SliderSetting yawAngle = new SliderSetting("YawAngle").value(Float.valueOf(180.0f)).range(1.0f, 180.0f).step(1.0f).setVisible(() -> {
        return Boolean.valueOf(!this.rotate.is("Off") && this.yawStep.getValue().booleanValue() && this.page.is("Main"));
    });
    public final ModeSetting targetLogic = new ModeSetting("TargetLogic").values("Distance", "Health").value("Distance").setVisible(() -> {
        return Boolean.valueOf(this.page.is("Main"));
    });
    public final SliderSetting targetRange = new SliderSetting("TargetRange").value(Float.valueOf(10.0f)).range(1.0f, 15.0f).step(0.1f).setVisible(() -> {
        return Boolean.valueOf(this.page.is("Main"));
    });
    public final ModeSetting interact = new ModeSetting("Interact").values("Default", "Strict").value("Default").setVisible(() -> {
        return Boolean.valueOf(this.page.is("Place"));
    });
    public final BooleanSetting strictCenter = new BooleanSetting("CCStrict").value((Boolean) true).setVisible(() -> {
        return Boolean.valueOf(this.page.is("Place") && this.interact.is("Strict"));
    });
    public final BooleanSetting rayTraceBypass = new BooleanSetting("RayTraceBypass").value((Boolean) false).setVisible(() -> {
        return Boolean.valueOf(this.page.is("Place"));
    });
    public final SliderSetting placeDelay = new SliderSetting("PlaceDelay").value(Float.valueOf(0.0f)).range(0.0f, 20.0f).step(1.0f).setVisible(() -> {
        return Boolean.valueOf(this.page.is("Place"));
    });
    public final SliderSetting placeRange = new SliderSetting("PlaceRange").value(Float.valueOf(5.0f)).range(1.0f, 6.0f).step(0.1f).setVisible(() -> {
        return Boolean.valueOf(this.page.is("Place"));
    });
    public final SliderSetting placeWallRange = new SliderSetting("PlaceWallRange").value(Float.valueOf(3.5f)).range(0.0f, 6.0f).step(0.1f).setVisible(() -> {
        return Boolean.valueOf(this.page.is("Place"));
    });
    public final BooleanSetting inhibit = new BooleanSetting("Inhibit").value((Boolean) true).setVisible(() -> {
        return Boolean.valueOf(this.page.is("Break"));
    });
    public final SliderSetting breakDelay = new SliderSetting("BreakDelay").value(Float.valueOf(0.0f)).range(0.0f, 20.0f).step(1.0f).setVisible(() -> {
        return Boolean.valueOf(this.page.is("Break"));
    });
    public final SliderSetting explodeRange = new SliderSetting("BreakRange").value(Float.valueOf(5.0f)).range(1.0f, 6.0f).step(0.1f).setVisible(() -> {
        return Boolean.valueOf(this.page.is("Break"));
    });
    public final SliderSetting explodeWallRange = new SliderSetting("BreakWallRange").value(Float.valueOf(3.5f)).range(0.0f, 6.0f).step(0.1f).setVisible(() -> {
        return Boolean.valueOf(this.page.is("Break"));
    });
    public final BooleanSetting mining = new BooleanSetting("Mining").value((Boolean) true).setVisible(() -> {
        return Boolean.valueOf(this.page.is("Pause"));
    });
    public final BooleanSetting eating = new BooleanSetting("Eating").value((Boolean) true).setVisible(() -> {
        return Boolean.valueOf(this.page.is("Pause"));
    });
    public final BooleanSetting surround = new BooleanSetting("Surround").value((Boolean) true).setVisible(() -> {
        return Boolean.valueOf(this.page.is("Pause"));
    });
    public final BooleanSetting middleClick = new BooleanSetting("MiddleClick").value((Boolean) true).setVisible(() -> {
        return Boolean.valueOf(this.page.is("Pause"));
    });
    public final BooleanSetting inventoryPause = new BooleanSetting("Inventory").value((Boolean) false).setVisible(() -> {
        return Boolean.valueOf(this.page.is("Pause"));
    });
    public final SliderSetting pauseHP = new SliderSetting("HP").value(Float.valueOf(8.0f)).range(RENDER_BOX_SIZE, 10.0f).step(0.1f).setVisible(() -> {
        return Boolean.valueOf(this.page.is("Pause"));
    });
    public final SliderSetting minDamage = new SliderSetting("MinDamage").value(Float.valueOf(6.0f)).range(RENDER_BOX_SIZE, 20.0f).step(0.1f).setVisible(() -> {
        return Boolean.valueOf(this.page.is("Damages"));
    });
    public final SliderSetting maxSelfDamage = new SliderSetting("MaxSelfDamage").value(Float.valueOf(10.0f)).range(RENDER_BOX_SIZE, 20.0f).step(0.1f).setVisible(() -> {
        return Boolean.valueOf(this.page.is("Damages"));
    });
    public final BooleanSetting efficiency = new BooleanSetting("Efficiency").value((Boolean) false).setVisible(() -> {
        return Boolean.valueOf(this.page.is("Damages"));
    });
    public final SliderSetting efficiencyFactor = new SliderSetting("EfficiencyFactor").value(Float.valueOf(1.0f)).range(0.1f, 5.0f).step(0.1f).setVisible(() -> {
        return Boolean.valueOf(this.page.is("Damages") && this.efficiency.getValue().booleanValue());
    });
    public final BooleanSetting protectFriends = new BooleanSetting("ProtectFriends").value((Boolean) true).setVisible(() -> {
        return Boolean.valueOf(this.page.is("Damages"));
    });
    public final BooleanSetting overrideSelfDamage = new BooleanSetting("OverrideSelfDamage").value((Boolean) true).setVisible(() -> {
        return Boolean.valueOf(this.page.is("Damages"));
    });
    public final BooleanSetting sacrificeTotem = new BooleanSetting("SacrificeTotem").value((Boolean) true).setVisible(() -> {
        return Boolean.valueOf(this.page.is("Damages") && this.overrideSelfDamage.getValue().booleanValue());
    });
    public final BooleanSetting armorBreaker = new BooleanSetting("ArmorBreaker").value((Boolean) true).setVisible(() -> {
        return Boolean.valueOf(this.page.is("Damages"));
    });
    public final SliderSetting armorScale = new SliderSetting("Armor %").value(Float.valueOf(5.0f)).range(0.0f, 40.0f).step(0.1f).setVisible(() -> {
        return Boolean.valueOf(this.page.is("Damages") && this.armorBreaker.getValue().booleanValue());
    });
    public final SliderSetting facePlaceHp = new SliderSetting("FacePlaceHp").value(Float.valueOf(5.0f)).range(0.0f, 20.0f).step(0.1f).setVisible(() -> {
        return Boolean.valueOf(this.page.is("Damages"));
    });
    public final BindSetting facePlaceButton = new BindSetting("FacePlaceBtn").value((Integer) (-999)).setVisible(() -> {
        return Boolean.valueOf(this.page.is("Damages"));
    });
    public final BooleanSetting ignoreTerrain = new BooleanSetting("IgnoreTerrain").value((Boolean) true).setVisible(() -> {
        return Boolean.valueOf(this.page.is("Damages"));
    });
    public final ModeSetting autoSwitch = new ModeSetting("Switch").values("NONE", "NORMAL", "SILENT", "INVENTORY").value("NORMAL").setVisible(() -> {
        return Boolean.valueOf(this.page.is("Switch"));
    });
    public final ModeSetting antiWeakness = new ModeSetting("AntiWeakness").values("NONE", "NORMAL", "SILENT", "INVENTORY").value("SILENT").setVisible(() -> {
        return Boolean.valueOf(this.page.is("Switch"));
    });
    public final BooleanSetting placeFailsafe = new BooleanSetting("PlaceFailsafe").value((Boolean) true).setVisible(() -> {
        return Boolean.valueOf(this.page.is("FailSafe"));
    });
    public final BooleanSetting breakFailsafe = new BooleanSetting("BreakFailsafe").value((Boolean) true).setVisible(() -> {
        return Boolean.valueOf(this.page.is("FailSafe"));
    });
    public final SliderSetting attempts = new SliderSetting("MaxAttempts").value(Float.valueOf(5.0f)).range(1.0f, 30.0f).step(1.0f).setVisible(() -> {
        return Boolean.valueOf(this.page.is("FailSafe"));
    });
    public final BooleanSetting idPredict = new BooleanSetting("IDPredict").value((Boolean) false).setVisible(() -> {
        return Boolean.valueOf(this.page.is("IDPredict"));
    });
    public final SliderSetting idAttacks = new SliderSetting("IDAttacks").value(Float.valueOf(3.0f)).range(1.0f, 10.0f).step(1.0f).setVisible(() -> {
        return Boolean.valueOf(this.page.is("IDPredict"));
    });
    public final ModeSetting swingMode = new ModeSetting("Swing").values("Both", "Place", "Break", "ServerSide").value("Place").setVisible(() -> {
        return Boolean.valueOf(this.page.is("Render"));
    });
    public final MultiBooleanSetting renderSettings = new MultiBooleanSetting("Render").value(new BooleanSetting("Block").value((Boolean) true), new BooleanSetting("Rect").value((Boolean) true), new BooleanSetting("Animate").value((Boolean) true)).setVisible(() -> {
        return Boolean.valueOf(this.page.is("Render"));
    });
    public final SliderSetting renderHoldMs = new SliderSetting("RenderHoldMs").value(Float.valueOf(500.0f)).range(100.0f, 1000.0f).step(10.0f).setVisible(() -> {
        return Boolean.valueOf(this.page.is("Render"));
    });
    public final BooleanSetting confirmInfo = new BooleanSetting("ConfirmTime").value((Boolean) true).setVisible(() -> {
        return Boolean.valueOf(this.page.is("Info"));
    });
    public final BooleanSetting calcInfo = new BooleanSetting("CalcInfo").value((Boolean) false).setVisible(() -> {
        return Boolean.valueOf(this.page.is("Info"));
    });
    private final AnimationUtil blockRenderAnim = new AnimationUtil();
    private final CrystalTracker crystalTracker = new CrystalTracker();
    private final Map<class_2338, Long> renderPositions = new ConcurrentHashMap();
    private State currentState = State.NoTarget;
    private int lastTargetId = -1;
    private long externalPauseUntilMs = 0;

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$State.class */
    private enum State {
        Active,
        Eating,
        LowHP,
        NoTarget,
        NoCrystalls,
        ExternalPause,
        Mining
    }

    /* JADX WARN: Type inference failed for: r1v102, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v107, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v110, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v115, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v118, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v121, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v124, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v127, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v13, types: [sweetie.leonware.api.module.setting.ModeSetting] */
    /* JADX WARN: Type inference failed for: r1v132, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v137, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v140, types: [sweetie.leonware.api.module.setting.BindSetting] */
    /* JADX WARN: Type inference failed for: r1v143, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v147, types: [sweetie.leonware.api.module.setting.ModeSetting] */
    /* JADX WARN: Type inference failed for: r1v151, types: [sweetie.leonware.api.module.setting.ModeSetting] */
    /* JADX WARN: Type inference failed for: r1v154, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v157, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v162, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v165, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v17, types: [sweetie.leonware.api.module.setting.ModeSetting] */
    /* JADX WARN: Type inference failed for: r1v170, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v174, types: [sweetie.leonware.api.module.setting.ModeSetting] */
    /* JADX WARN: Type inference failed for: r1v177, types: [sweetie.leonware.api.module.setting.MultiBooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v182, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v185, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v188, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v20, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v25, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v29, types: [sweetie.leonware.api.module.setting.ModeSetting] */
    /* JADX WARN: Type inference failed for: r1v34, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v38, types: [sweetie.leonware.api.module.setting.ModeSetting] */
    /* JADX WARN: Type inference failed for: r1v41, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v44, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v49, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v5, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v54, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v59, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v62, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v67, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v72, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v77, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v80, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v83, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v86, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v89, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v9, types: [sweetie.leonware.api.module.setting.ModeSetting] */
    /* JADX WARN: Type inference failed for: r1v92, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v97, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    public CrystalAuraProcessor(CrystalAuraModule module) {
        this.module = module;
    }

    public void initSettings(CrystalAuraModule module) {
        module.addSettings(this.page, this.await, this.timing, this.sequential, this.rotate, this.yawStep, this.yawAngle, this.targetLogic, this.targetRange, this.interact, this.strictCenter, this.rayTraceBypass, this.placeDelay, this.placeRange, this.placeWallRange, this.inhibit, this.breakDelay, this.explodeRange, this.explodeWallRange, this.mining, this.eating, this.surround, this.middleClick, this.inventoryPause, this.pauseHP, this.minDamage, this.maxSelfDamage, this.efficiency, this.efficiencyFactor, this.protectFriends, this.overrideSelfDamage, this.sacrificeTotem, this.armorBreaker, this.armorScale, this.facePlaceHp, this.facePlaceButton, this.ignoreTerrain, this.autoSwitch, this.antiWeakness, this.placeFailsafe, this.breakFailsafe, this.attempts, this.idPredict, this.idAttacks, this.swingMode, this.renderSettings, this.renderHoldMs, this.confirmInfo, this.calcInfo);
    }

    public boolean check() {
        return (this.target == null && this.bestPosition == null && this.bestCrystal == null && this.renderPositions.isEmpty()) ? false : true;
    }

    public void onPacket(PacketEvent.PacketEventData event) {
        if (!event.isReceive() || Module.mc.field_1724 == null || Module.mc.field_1687 == null) {
            return;
        }
        class_2606 spawn = event.packet();
        if (spawn instanceof class_2606) {
            processSpawnPacket(spawn.method_11183());
            return;
        }
        class_2596<?> class_2596VarPacket = event.packet();
        if (class_2596VarPacket instanceof class_2604) {
            class_2604 spawn2 = (class_2604) class_2596VarPacket;
            processSpawnPacket(spawn2.method_11167());
            confirmAwaitingBySpawn(spawn2.method_11167(), resolveEntitySpawnPos(spawn2));
            return;
        }
        class_2596<?> class_2596VarPacket2 = event.packet();
        if (class_2596VarPacket2 instanceof class_2664) {
            class_2664 explosion = (class_2664) class_2596VarPacket2;
            class_243 explosionPos = resolveExplosionPos(explosion);
            for (class_1511 class_1511Var : Module.mc.field_1687.method_18112()) {
                if (class_1511Var instanceof class_1511) {
                    class_1511 crystal = class_1511Var;
                    if (crystal.method_5707(explosionPos) <= 144.0d && !this.crystalTracker.isDead(crystal.method_5628())) {
                        this.crystalTracker.setDead(crystal.method_5628(), System.currentTimeMillis());
                    }
                }
            }
        }
    }

    public void onUpdate(UpdateEvent event) {
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null || Module.mc.field_1761 == null) {
            return;
        }
        if (!this.initialized) {
            reset();
            this.initialized = true;
        }
        tickTimers();
        this.crystalTracker.update();
        cleanupRenderPositions();
        processAwaitingSpawnConfirm();
        this.target = findTarget(this.targetRange.getValue().floatValue(), this.targetLogic.getValue());
        if (this.target != null && (!this.target.method_5805() || this.target.method_6032() <= 0.0f)) {
            this.target = null;
            this.currentState = State.NoTarget;
            this.lastTargetId = -1;
            clearCombatContext();
            return;
        }
        if (this.target == null) {
            this.currentState = State.NoTarget;
            this.lastTargetId = -1;
            clearCombatContext();
            return;
        }
        if (passedTicks(this.placeTicks, 20)) {
            this.renderDamage = 0.0f;
        }
        calcRotations();
        boolean targetChanged = this.lastTargetId != this.target.method_5628();
        if (targetChanged) {
            this.lastTargetId = this.target.method_5628();
            this.calcTicks = Integer.MAX_VALUE;
        }
        int pingGate = Math.max(1, Math.round(getPing() / 25.0f));
        if (targetChanged || this.bestPosition == null || !this.await.getValue().booleanValue() || passedTicks(this.placeTicks, 20) || passedTicks(this.calcTicks, pingGate)) {
            calcPosition(this.placeRange.getValue().floatValue(), Module.mc.field_1724.method_19538());
        }
        getCrystalToExplode();
        if (this.timing.is("NORMAL") || this.timing.is("SEQUENTIAL")) {
            doAction();
        }
    }

    public void onRender3D(Render3DEvent.Render3DEventData event) {
        cleanupRenderPositions();
        boolean canRenderBlock = render("Block") && !this.renderPositions.isEmpty();
        float blockAnim = animate(this.blockRenderAnim, canRenderBlock);
        this.blockRenderFactor = blockAnim;
        if (canRenderBlock && blockAnim > 0.01f) {
            long now = System.currentTimeMillis();
            long holdMs = this.renderHoldMs.getValue().longValue();
            for (Map.Entry<class_2338, Long> entry : new ArrayList(this.renderPositions.entrySet())) {
                class_2338 pos = entry.getKey();
                long elapsed = now - entry.getValue().longValue();
                if (elapsed <= holdMs) {
                    float fade = 1.0f - class_3532.method_15363(elapsed / holdMs, 0.0f, 1.0f);
                    Color fill = themed(0.68f, (int) (56.0f * blockAnim * fade));
                    Color rect = themed(0.82f, (int) (160.0f * blockAnim * fade));
                    float x = pos.method_10263();
                    float y = pos.method_10264();
                    float z = pos.method_10260();
                    RenderUtil.BOX.drawBox(x, y, z, x + RENDER_BOX_SIZE, y + RENDER_BOX_SIZE, z + RENDER_BOX_SIZE, 1.15f, fill, BoxRender.Render.FILL, 0.0f);
                    if (render("Rect")) {
                        RenderUtil.BOX.drawBox(x, y, z, x + RENDER_BOX_SIZE, y + RENDER_BOX_SIZE, z + RENDER_BOX_SIZE, 1.95f, rect, BoxRender.Render.OUTLINE, 0.0f);
                    }
                }
            }
        }
    }

    public void onRender2D(Render2DEvent.Render2DEventData event) {
        if (!this.confirmInfo.getValue().booleanValue() && !this.calcInfo.getValue().booleanValue()) {
            return;
        }
        class_2338 infoPos = this.renderPos != null ? this.renderPos : this.bestPosition != null ? this.bestPosition.method_17777() : null;
        if (infoPos == null) {
            return;
        }
        List<String> lines = new ArrayList<>();
        if (this.confirmInfo.getValue().booleanValue() && this.confirmTime > 0) {
            lines.add("Confirm: " + this.confirmTime + "ms");
        }
        if (this.calcInfo.getValue().booleanValue()) {
            lines.add("Calc: " + this.calcTime + "ms");
        }
        if (lines.isEmpty()) {
            return;
        }
        Vector2f projected = ProjectionUtil.project(new class_243(infoPos.method_10263() + 1.0f, infoPos.method_10264() + 1.0f, infoPos.method_10260() + 1.0f));
        if (projected.x == Float.MAX_VALUE && projected.y == Float.MAX_VALUE) {
            return;
        }
        float lineHeight = 6.5f + 1.0f;
        float drawY = projected.y - (((lines.size() - 1) * lineHeight) / RENDER_BOX_SIZE);
        Color textColor = new Color(160, 160, 160, 220);
        for (String line : lines) {
            Fonts.SF_MEDIUM.drawCenteredText(event.matrixStack(), line, projected.x, drawY, 6.5f, textColor);
            drawY += lineHeight;
        }
    }

    public void reset() {
        this.facePlacing = false;
        this.rotated = false;
        this.rotating = false;
        this.renderDamage = 0.0f;
        this.renderSelfDamage = 0.0f;
        this.renderMultiplier = 0L;
        this.confirmTime = 0L;
        this.calcTime = 0L;
        this.currentId = 0L;
        this.lastTargetId = -1;
        this.externalPauseUntilMs = 0L;
        this.blockRenderFactor = 0.0f;
        this.placeTicks = 0;
        this.breakTicks = 0;
        this.calcTicks = 0;
        this.placeSyncTicks = 0;
        this.rotationTicks = 0;
        this.renderPositions.clear();
        this.crystalTracker.reset();
        this.bestCrystal = null;
        this.bestPosition = null;
        this.secondaryCrystal = null;
        this.currentData = null;
        this.renderPos = null;
        this.prevRenderPos = null;
        this.target = null;
        this.rotationVec = null;
        this.currentState = State.NoTarget;
        this.placedOnSpawn = false;
        this.initialized = false;
        this.blockRenderAnim.setValue(0.0d);
    }

    private void tickTimers() {
        this.placeTicks++;
        this.breakTicks++;
        this.calcTicks++;
        this.placeSyncTicks++;
    }

    private void doAction() {
        if (this.target == null) {
            return;
        }
        if (this.sequential.is("Off")) {
            if (this.bestCrystal != null && passedTicks(this.breakTicks, this.breakDelay.getValue().intValue())) {
                attackCrystal(this.bestCrystal);
            } else if (this.bestPosition != null && passedTicks(this.placeTicks, this.placeDelay.getValue().intValue()) && !this.placedOnSpawn) {
                placeCrystal(this.bestPosition, false, false);
            }
        } else {
            if (this.bestCrystal != null && passedTicks(this.breakTicks, this.breakDelay.getValue().intValue())) {
                attackCrystal(this.bestCrystal);
            }
            if (this.bestPosition != null && passedTicks(this.placeTicks, this.placeDelay.getValue().intValue()) && !this.placedOnSpawn) {
                placeCrystal(this.bestPosition, false, false);
            }
        }
        this.placedOnSpawn = false;
    }

    private void clearCombatContext() {
        this.bestCrystal = null;
        this.bestPosition = null;
        this.secondaryCrystal = null;
        this.currentData = null;
        this.rotationVec = null;
        this.facePlacing = false;
        this.renderPos = null;
    }

    private void processAwaitingSpawnConfirm() {
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null) {
            return;
        }
        Map<class_2338, CrystalTracker.Attempt> awaiting = this.crystalTracker.getAwaitingPositions();
        if (awaiting.isEmpty()) {
            return;
        }
        long now = System.currentTimeMillis();
        for (Map.Entry<class_2338, CrystalTracker.Attempt> entry : new ArrayList(awaiting.entrySet())) {
            class_2338 bp = entry.getKey();
            CrystalTracker.Attempt attempt = entry.getValue();
            class_238 searchBox = new class_238(bp.method_10084()).method_1009(0.9d, 0.8d, 0.9d);
            List<class_1511> nearby = Module.mc.field_1687.method_8390(class_1511.class, searchBox, entity -> {
                return entity != null && entity.method_5805() && entity.method_5707(bp.method_46558()) < 0.3d;
            });
            if (!nearby.isEmpty()) {
                class_1511 crystal = nearby.get(0);
                this.confirmTime = now - attempt.time();
                this.crystalTracker.confirmSpawn(bp);
                if (passedTicks(this.breakTicks, this.breakDelay.getValue().intValue())) {
                    handleSpawn(crystal);
                }
            }
        }
    }

    private void handleSpawn(class_1511 crystal) {
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null || !canAttackCrystal(crystal)) {
            return;
        }
        attackCrystal(crystal);
        if (this.sequential.is("Strong") && passedTicks(this.placeTicks, this.placeDelay.getValue().intValue())) {
            calcPosition(this.placeRange.getValue().floatValue(), Module.mc.field_1724.method_19538());
            if (this.bestPosition != null) {
                placeCrystal(this.bestPosition, false, true);
            }
        }
    }

    private void calcRotations() {
        if (this.rotate.is("Off") || shouldPause() || Module.mc.field_1724 == null) {
            this.rotated = true;
            this.rotating = false;
            return;
        }
        class_243 vec = null;
        if (this.rotationVec != null) {
            vec = this.rotationVec.hitVec() == null ? this.rotationVec.vec() : this.rotationVec.hitVec().method_17784();
        } else if (this.bestPosition != null) {
            vec = this.bestPosition.method_17784();
        } else if (this.bestCrystal != null) {
            vec = this.bestCrystal.method_19538();
        }
        if (vec == null) {
            this.rotated = true;
            this.rotating = false;
            return;
        }
        Rotation desired = RotationUtil.rotationAt(vec);
        Rotation limited = limitYaw(desired);
        applyRotation(vec, limited);
        this.rotated = isRotationAligned(desired);
        this.rotating = true;
        if (this.rotationVec != null) {
            int i = this.rotationTicks;
            this.rotationTicks = i - 1;
            if (i < 0) {
                this.rotationVec = null;
            }
        }
    }

    private Rotation limitYaw(Rotation desired) {
        if (!this.yawStep.getValue().booleanValue()) {
            return desired;
        }
        Rotation active = RotationManager.getInstance().getRotation();
        float baseYaw = active != null ? active.getYaw() : Module.mc.field_1724.method_36454();
        float deltaYaw = class_3532.method_15393(desired.getYaw() - baseYaw);
        float limitedDelta = class_3532.method_15363(deltaYaw, -this.yawAngle.getValue().floatValue(), this.yawAngle.getValue().floatValue());
        return new Rotation(baseYaw + limitedDelta, desired.getPitch());
    }

    private boolean isRotationAligned(Rotation targetRotation) {
        Rotation activeRotation = RotationManager.getInstance().getRotation();
        float yaw = activeRotation != null ? activeRotation.getYaw() : Module.mc.field_1724.method_36454();
        float pitch = activeRotation != null ? activeRotation.getPitch() : Module.mc.field_1724.method_36455();
        float deltaYaw = Math.abs(class_3532.method_15393(targetRotation.getYaw() - yaw));
        float deltaPitch = Math.abs(class_3532.method_15393(targetRotation.getPitch() - pitch));
        return deltaYaw <= 8.0f && deltaPitch <= 8.0f;
    }

    private void applyRotation(class_243 vec, Rotation desired) {
        RotationStrategy strategy;
        if (this.rotate.is("Off")) {
            return;
        }
        if (this.rotate.is("Instant")) {
            strategy = new RotationStrategy(new InstantRotation(), MoveFixModule.enabled(), true).clientLook(false).ticksUntilReset(2);
        } else {
            strategy = new RotationStrategy(new SmoothRotation(), MoveFixModule.enabled(), true).clientLook(false).ticksUntilReset(3);
        }
        RotationManager.getInstance().addRotation(new Rotation.VecRotation(desired, vec), this.target, strategy, TaskPriority.CRITICAL, this.module);
    }

    private void attackCrystal(class_1511 crystal) {
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null || Module.mc.field_1761 == null || crystal == null) {
            return;
        }
        if ((this.crystalTracker.isDead(crystal.method_5628()) && this.inhibit.getValue().booleanValue()) || shouldPause() || this.target == null) {
            return;
        }
        class_1293 weakness = Module.mc.field_1724.method_6112(class_1294.field_5911);
        class_1293 strength = Module.mc.field_1724.method_6112(class_1294.field_5910);
        int prevSlot = -1;
        HotbarSearch antiWeaknessHotbar = findAntiWeaknessHotbar();
        int antiWeaknessInv = findAntiWeaknessInventory();
        if (!this.antiWeakness.is("NONE") && weakness != null && (strength == null || strength.method_5578() < weakness.method_5578())) {
            prevSlot = switchTo(antiWeaknessHotbar, antiWeaknessInv, this.antiWeakness);
        }
        if (!this.rotate.is("Off")) {
            applyRotation(crystal.method_5829().method_1005(), RotationUtil.rotationAt(crystal.method_5829().method_1005()));
        }
        sendPacket(class_2824.method_34206(crystal, Module.mc.field_1724.method_5715()));
        swingHand(false, true);
        this.breakTicks = 0;
        this.crystalTracker.onAttack(crystal, this.breakFailsafe.getValue().booleanValue(), this.attempts.getValue().intValue());
        this.rotationTicks = 10;
        for (class_1511 class_1511Var : Module.mc.field_1687.method_18112()) {
            if (class_1511Var instanceof class_1511) {
                class_1511 exCrystal = class_1511Var;
                if (exCrystal.method_5649(crystal.method_23317(), crystal.method_23318(), crystal.method_23321()) <= 144.0d && !this.crystalTracker.isDead(exCrystal.method_5628())) {
                    this.crystalTracker.setDead(exCrystal.method_5628(), System.currentTimeMillis());
                }
            }
        }
        if (prevSlot != -1) {
            if (this.antiWeakness.is("SILENT")) {
                Module.mc.field_1724.method_31548().field_7545 = prevSlot;
                sendPacket(new class_2868(prevSlot));
            } else if (this.antiWeakness.is("INVENTORY")) {
                Module.mc.field_1761.method_2906(Module.mc.field_1724.field_7512.field_7763, prevSlot, Module.mc.field_1724.method_31548().field_7545, class_1713.field_7791, Module.mc.field_1724);
                sendPacket(new class_2815(Module.mc.field_1724.field_7512.field_7763));
            }
        }
    }

    private boolean canAttackCrystal(class_1511 crystal) {
        if (crystal == null || this.target == null || Module.mc.field_1724 == null || Module.mc.field_1687 == null) {
            return false;
        }
        double distanceSq = Module.mc.field_1724.method_33571().method_1025(crystal.method_5829().method_1005());
        double maxRangeSq = canSee(crystal.method_19538()) ? square(this.explodeRange.getValue().floatValue()) : square(this.explodeWallRange.getValue().floatValue());
        if (distanceSq > maxRangeSq || !crystal.method_5805()) {
            return false;
        }
        float damage = ExplosionUtility.getAutoCrystalDamage(crystal.method_19538(), this.target, getPredictTicks(), false);
        float selfDamage = ExplosionUtility.getSelfExplosionDamage(crystal.method_19538(), getSelfPredictTicks(), false);
        boolean overrideDamage = shouldOverrideMaxSelfDmg(damage, selfDamage);
        if (this.protectFriends.getValue().booleanValue()) {
            for (class_746 class_746Var : Module.mc.field_1687.method_18456()) {
                if (class_746Var != null && class_746Var != Module.mc.field_1724 && FriendManager.getInstance().contains(class_746Var.method_5477().getString())) {
                    float friendDamage = ExplosionUtility.getAutoCrystalDamage(crystal.method_19538(), class_746Var, getPredictTicks(), false);
                    if (friendDamage > selfDamage) {
                        selfDamage = friendDamage;
                    }
                }
            }
        }
        return selfDamage <= this.maxSelfDamage.getValue().floatValue() || overrideDamage;
    }

    private int switchTo(HotbarSearch hotbar, int invSlot, ModeSetting switchMode) {
        int prevSlot;
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null || Module.mc.field_1761 == null) {
            return -1;
        }
        prevSlot = Module.mc.field_1724.method_31548().field_7545;
        switch (switchMode.getValue()) {
            case "INVENTORY":
                if (invSlot != -1) {
                    prevSlot = invSlot;
                    Module.mc.field_1761.method_2906(Module.mc.field_1724.field_7512.field_7763, invSlot, Module.mc.field_1724.method_31548().field_7545, class_1713.field_7791, Module.mc.field_1724);
                    sendPacket(new class_2815(Module.mc.field_1724.field_7512.field_7763));
                    break;
                }
                break;
            case "NORMAL":
                if (hotbar.found()) {
                    InventoryUtil.swapToSlot(hotbar.slot(), true);
                    break;
                }
                break;
            case "SILENT":
                if (hotbar.found()) {
                    InventoryUtil.swapToSlot(hotbar.slot(), false);
                    break;
                }
                break;
        }
        return prevSlot;
    }

    private void placeCrystal(class_3965 bhr, boolean packetRotate, boolean onSpawn) {
        if (shouldPause() || Module.mc.field_1724 == null || bhr == null) {
            return;
        }
        int prevSlot = -1;
        HotbarSearch crystalResult = findInHotbar(class_1802.field_8301);
        int crystalResultInv = findInInventory(class_1802.field_8301);
        boolean offhand = Module.mc.field_1724.method_6079().method_7909() instanceof class_1774;
        boolean holdingCrystal = (Module.mc.field_1724.method_6047().method_7909() instanceof class_1774) || offhand;
        if (!this.rotate.is("Off")) {
            this.rotationVec = new RotationVec(bhr.method_17784(), bhr, true);
            applyRotation(bhr.method_17784(), RotationUtil.rotationAt(bhr.method_17784()));
            if (!packetRotate && !this.rotated) {
                return;
            }
        }
        if (isPositionBlockedByEntity(bhr.method_17777(), false)) {
            return;
        }
        if (!this.autoSwitch.is("NONE") && !holdingCrystal) {
            prevSlot = switchTo(crystalResult, crystalResultInv, this.autoSwitch);
        }
        if (!(Module.mc.field_1724.method_6047().method_7909() instanceof class_1774) && !offhand && !this.autoSwitch.is("SILENT")) {
            return;
        }
        class_1268 hand = offhand ? class_1268.field_5810 : class_1268.field_5808;
        boolean accepted = Module.mc.field_1761.method_2896(Module.mc.field_1724, hand, bhr).method_23665();
        if (!accepted) {
            return;
        }
        swingHand(offhand, false);
        if (passedTicks(this.breakTicks, this.breakDelay.getValue().intValue()) && this.idPredict.getValue().booleanValue()) {
            predictAttack();
        }
        this.placeTicks = 0;
        this.rotationTicks = 10;
        if (!bhr.method_17777().equals(this.renderPos)) {
            this.renderMultiplier = System.currentTimeMillis();
            this.prevRenderPos = this.renderPos;
            this.renderPos = bhr.method_17777();
        }
        this.crystalTracker.addAwaitingPos(bhr.method_17777(), this.placeFailsafe.getValue().booleanValue());
        this.renderPositions.put(bhr.method_17777().method_10062(), Long.valueOf(System.currentTimeMillis()));
        postPlaceSwitch(prevSlot);
        if (onSpawn) {
            this.placedOnSpawn = true;
            this.placeSyncTicks = 0;
        }
    }

    private void postPlaceSwitch(int slot) {
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null || Module.mc.field_1761 == null) {
            return;
        }
        if (this.autoSwitch.is("SILENT") && slot != -1) {
            Module.mc.field_1724.method_31548().field_7545 = slot;
            sendPacket(new class_2868(slot));
        }
        if (this.autoSwitch.is("INVENTORY") && slot != -1) {
            Module.mc.field_1761.method_2906(Module.mc.field_1724.field_7512.field_7763, slot, Module.mc.field_1724.method_31548().field_7545, class_1713.field_7791, Module.mc.field_1724);
            sendPacket(new class_2815(Module.mc.field_1724.field_7512.field_7763));
        }
    }

    private void calcPosition(float range, class_243 center) {
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null) {
            return;
        }
        long start = System.currentTimeMillis();
        this.calcTicks = 0;
        if (this.target == null) {
            this.renderPos = null;
            this.prevRenderPos = null;
            this.bestPosition = null;
            this.currentData = null;
            return;
        }
        List<PlaceData> list = getPossibleBlocks(this.target, center, range).stream().filter(data -> {
            return isSafe(data.damage(), data.selfDamage(), data.overrideDamage());
        }).toList();
        this.bestPosition = list.isEmpty() ? null : filterPositions(list);
        this.calcTime = System.currentTimeMillis() - start;
    }

    private List<PlaceData> getPossibleBlocks(class_1657 target, class_243 center, float range) {
        PlaceData data;
        List<PlaceData> blocks = new ArrayList<>();
        class_2338 playerPos = class_2338.method_49638(center);
        class_243 predictedPlayerPos = PredictUtility.predictPosition(Module.mc.field_1724, getSelfPredictTicks());
        if (predictedPlayerPos == null) {
            predictedPlayerPos = Module.mc.field_1724.method_19538();
        }
        int r = (int) Math.ceil(range);
        double scanRangeSq = square(range + 1.0f);
        for (int x = playerPos.method_10263() - r; x <= playerPos.method_10263() + r; x++) {
            for (int y = playerPos.method_10264() - r; y <= playerPos.method_10264() + r; y++) {
                for (int z = playerPos.method_10260() - r; z <= playerPos.method_10260() + r; z++) {
                    class_2338 bp = new class_2338(x, y, z);
                    if (bp.method_46558().method_1025(center) <= scanRangeSq && (data = getPlaceData(bp, target, predictedPlayerPos)) != null) {
                        blocks.add(data);
                    }
                }
            }
        }
        return blocks;
    }

    private List<CrystalData> getPossibleCrystals(class_1657 target) {
        List<CrystalData> crystals = new ArrayList<>();
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null) {
            return crystals;
        }
        for (class_1511 class_1511Var : Module.mc.field_1687.method_18112()) {
            if (class_1511Var instanceof class_1511) {
                class_1511 crystal = class_1511Var;
                if (!this.crystalTracker.isBlocked(crystal.method_5628()) && (!this.crystalTracker.isDead(crystal.method_5628()) || !this.inhibit.getValue().booleanValue())) {
                    double maxRangeSq = canSee(crystal.method_19538()) ? square(this.explodeRange.getValue().floatValue()) : square(this.explodeWallRange.getValue().floatValue());
                    if (Module.mc.field_1724.method_33571().method_1025(crystal.method_5829().method_1005()) <= maxRangeSq && crystal.method_5805()) {
                        float damage = ExplosionUtility.getAutoCrystalDamage(class_1511Var.method_19538(), target, getPredictTicks(), false);
                        float selfDamage = ExplosionUtility.getSelfExplosionDamage(class_1511Var.method_19538(), getSelfPredictTicks(), false);
                        boolean overrideDamage = shouldOverrideMaxSelfDmg(damage, selfDamage);
                        if (this.protectFriends.getValue().booleanValue()) {
                            for (class_746 class_746Var : Module.mc.field_1687.method_18456()) {
                                if (class_746Var != null && class_746Var != Module.mc.field_1724 && FriendManager.getInstance().contains(class_746Var.method_5477().getString())) {
                                    float friendDamage = ExplosionUtility.getAutoCrystalDamage(class_1511Var.method_19538(), class_746Var, getPredictTicks(), false);
                                    if (friendDamage > selfDamage) {
                                        selfDamage = friendDamage;
                                    }
                                }
                            }
                        }
                        if (damage >= 1.5f && (selfDamage <= this.maxSelfDamage.getValue().floatValue() || overrideDamage)) {
                            crystals.add(new CrystalData(crystal, damage, selfDamage, overrideDamage));
                        }
                    }
                }
            }
        }
        return crystals;
    }

    private void getCrystalToExplode() {
        if (this.target == null) {
            this.bestCrystal = null;
        } else if (this.secondaryCrystal != null) {
            this.bestCrystal = canAttackCrystal(this.secondaryCrystal) ? this.secondaryCrystal : null;
            this.secondaryCrystal = null;
        } else {
            List<CrystalData> list = getPossibleCrystals(this.target).stream().filter(data -> {
                return isSafe(data.damage(), data.selfDamage(), data.overrideDamage());
            }).toList();
            this.bestCrystal = list.isEmpty() ? null : filterCrystals(list);
        }
    }

    private boolean isSafe(float damage, float selfDamage, boolean overrideDamage) {
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null) {
            return false;
        }
        if (overrideDamage) {
            return true;
        }
        if (selfDamage + 0.5f > Module.mc.field_1724.method_6032() + Module.mc.field_1724.method_6067()) {
            return false;
        }
        return !this.efficiency.getValue().booleanValue() || damage / Math.max(0.1f, selfDamage) >= this.efficiencyFactor.getValue().floatValue();
    }

    private class_3965 filterPositions(List<PlaceData> clearedList) {
        PlaceData bestData = null;
        float bestVal = 0.0f;
        for (PlaceData data : clearedList) {
            if (shouldOverrideMinDmg(data.damage()) || data.damage() > this.minDamage.getValue().floatValue()) {
                if (bestData == null || !data.overrideDamage() || this.target.method_6067() + this.target.method_6032() >= bestData.damage() || bestData.selfDamage() >= data.selfDamage()) {
                    boolean shouldStopOverride = bestData != null && bestData.overrideDamage() && data.damage() > this.target.method_6032() + this.target.method_6067() && data.selfDamage() < bestData.selfDamage();
                    float safetyComparatorDelta = shouldStopOverride ? 10.0f : 1.0f;
                    if (bestData == null || Math.abs(bestData.damage() - data.damage()) >= safetyComparatorDelta || Math.abs(bestData.selfDamage() - data.selfDamage()) <= 1.0f) {
                        if (bestVal < data.damage()) {
                            bestData = data;
                            bestVal = data.damage();
                        }
                    } else if (bestData.selfDamage() >= data.selfDamage()) {
                        bestData = data;
                        bestVal = data.damage();
                    }
                }
            }
        }
        if (bestData == null) {
            return null;
        }
        this.facePlacing = bestData.damage() < this.minDamage.getValue().floatValue();
        this.renderDamage = bestData.damage();
        this.renderSelfDamage = bestData.selfDamage();
        this.currentData = bestData;
        return bestData.bhr();
    }

    private class_1511 filterCrystals(List<CrystalData> clearedList) {
        CrystalData bestData = null;
        float bestVal = 0.0f;
        for (CrystalData data : clearedList) {
            if (shouldOverrideMinDmg(data.damage()) || data.damage() > this.minDamage.getValue().floatValue()) {
                if (bestData == null || !data.overrideDamage() || this.target.method_6067() + this.target.method_6032() >= bestData.damage() || bestData.selfDamage() >= data.selfDamage()) {
                    boolean shouldStopOverride = bestData != null && bestData.overrideDamage() && data.damage() > this.target.method_6032() + this.target.method_6067() && data.selfDamage() < bestData.selfDamage();
                    float safetyComparatorDelta = shouldStopOverride ? 10.0f : 1.0f;
                    if (bestData == null || Math.abs(bestData.damage() - data.damage()) >= safetyComparatorDelta || Math.abs(bestData.selfDamage() - data.selfDamage()) <= 1.0f) {
                        if (bestVal < data.damage()) {
                            bestData = data;
                            bestVal = data.damage();
                        }
                    } else if (bestData.selfDamage() >= data.selfDamage()) {
                        bestData = data;
                        bestVal = data.damage();
                    }
                }
            }
        }
        if (bestData == null) {
            return null;
        }
        this.renderDamage = bestData.damage();
        this.renderSelfDamage = bestData.selfDamage();
        return bestData.crystal();
    }

    private PlaceData getPlaceData(class_2338 bp, class_1657 target, class_243 predictedPlayerPos) {
        class_243 crystalVec;
        class_3965 interactResult;
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null || this.crystalTracker.isPositionBlocked(bp, this.placeFailsafe.getValue().booleanValue(), this.attempts.getValue().intValue()) || !predictCrystalSpawn(bp, predictedPlayerPos)) {
            return null;
        }
        if (target != null && target.method_19538().method_1025(bp.method_46558().method_1031(0.0d, 0.5d, 0.0d)) > 144.0d) {
            return null;
        }
        class_2248 base = Module.mc.field_1687.method_8320(bp).method_26204();
        if (base != class_2246.field_10540 && base != class_2246.field_9987) {
            return null;
        }
        boolean freeSpace = Module.mc.field_1687.method_22347(bp.method_10084());
        if (!freeSpace || isPositionBlockedByEntity(bp, true) || (interactResult = getInteractResult(bp, (crystalVec = new class_243(((double) bp.method_10263()) + 0.5d, ((double) bp.method_10264()) + 1.0d, ((double) bp.method_10260()) + 0.5d)))) == null) {
            return null;
        }
        float damage = target == null ? 10.0f : ExplosionUtility.getAutoCrystalDamage(crystalVec, target, getPredictTicks(), false);
        if (damage < 1.5f) {
            return null;
        }
        float selfDamage = ExplosionUtility.getSelfExplosionDamage(crystalVec, getSelfPredictTicks(), false);
        boolean overrideDamage = shouldOverrideMaxSelfDmg(damage, selfDamage);
        if (this.protectFriends.getValue().booleanValue()) {
            for (class_746 class_746Var : Module.mc.field_1687.method_18456()) {
                if (class_746Var != null && class_746Var != Module.mc.field_1724 && FriendManager.getInstance().contains(class_746Var.method_5477().getString())) {
                    float friendDamage = ExplosionUtility.getAutoCrystalDamage(crystalVec, class_746Var, getPredictTicks(), false);
                    if (friendDamage > selfDamage) {
                        selfDamage = friendDamage;
                    }
                }
            }
        }
        if (selfDamage > this.maxSelfDamage.getValue().floatValue() && !overrideDamage) {
            return null;
        }
        return new PlaceData(interactResult, damage, selfDamage, overrideDamage);
    }

    private boolean predictCrystalSpawn(class_2338 bp, class_243 predictedPlayerPos) {
        class_243 predictedPos = bp.method_46558().method_1031(0.0d, 1.5d, 0.0d);
        class_243 predictedEyes = predictedPlayerPos.method_1031(0.0d, Module.mc.field_1724.method_18381(Module.mc.field_1724.method_18376()), 0.0d);
        double distanceSq = predictedEyes.method_1025(predictedPos);
        return canSee(predictedPos) ? distanceSq <= square((double) this.explodeRange.getValue().floatValue()) : distanceSq <= square((double) this.explodeWallRange.getValue().floatValue());
    }

    private class_3965 getInteractResult(class_2338 bp, class_243 crystalVec) {
        return this.interact.is("Strict") ? getStrictInteract(bp) : getDefaultInteract(crystalVec, bp);
    }

    private boolean isPositionBlockedByEntity(class_2338 base, boolean calcPhase) {
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null) {
            return false;
        }
        class_238 box = new class_238(base.method_10084()).method_1009(0.0d, 1.0d, 0.0d);
        for (class_1297 entity : Module.mc.field_1687.method_18112()) {
            if (entity != null && entity.method_5805() && entity.method_5829().method_994(box) && !(entity instanceof class_1303)) {
                if (entity instanceof class_1511) {
                    class_1511 crystal = (class_1511) entity;
                    if (this.crystalTracker.isDead(crystal.method_5628())) {
                        continue;
                    } else {
                        if (this.crystalTracker.isBlocked(crystal.method_5628())) {
                            return true;
                        }
                        if (!calcPhase) {
                            if (crystal.method_19538().method_1025(box.method_1005()) > 0.3d) {
                                this.secondaryCrystal = crystal;
                                return true;
                            }
                            return true;
                        }
                        if (!canAttackCrystal(crystal)) {
                            return true;
                        }
                    }
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean shouldOverrideMaxSelfDmg(float damage, float selfDamage) {
        if (!this.overrideSelfDamage.getValue().booleanValue() || this.target == null || Module.mc.field_1724 == null || Module.mc.field_1687 == null) {
            return false;
        }
        boolean targetSafe = this.target.method_6079().method_31574(class_1802.field_8288) || this.target.method_6047().method_31574(class_1802.field_8288);
        boolean playerSafe = Module.mc.field_1724.method_6079().method_31574(class_1802.field_8288) || Module.mc.field_1724.method_6047().method_31574(class_1802.field_8288);
        float targetHp = this.target.method_6032() + this.target.method_6067();
        float playerHp = Module.mc.field_1724.method_6032() + Module.mc.field_1724.method_6067();
        boolean canPop = damage > targetHp && targetSafe;
        boolean canKill = damage > targetHp && !targetSafe;
        boolean canPopSelf = selfDamage > playerHp && playerSafe;
        boolean canKillSelf = selfDamage > playerHp && !playerSafe;
        if (canPopSelf && canKill && this.sacrificeTotem.getValue().booleanValue()) {
            return true;
        }
        return selfDamage > this.maxSelfDamage.getValue().floatValue() && !((!canPop && !canKill) || canKillSelf || canPopSelf);
    }

    private boolean shouldOverrideMinDmg(float damage) {
        if (this.target == null) {
            return false;
        }
        if ((this.facePlaceButton.getValue().intValue() != -999 && KeyStorage.isPressed(this.facePlaceButton.getValue().intValue())) || (this.target.method_6032() + this.target.method_6067()) - damage < 0.0f) {
            return true;
        }
        if (this.armorBreaker.getValue().booleanValue()) {
            for (class_1799 armor : this.target.method_5661()) {
                if (armor != null && !armor.method_7960() && armor.method_7909() != class_1802.field_8162 && armor.method_7963()) {
                    float durabilityPercent = ((armor.method_7936() - armor.method_7919()) / armor.method_7936()) * 100.0f;
                    if (durabilityPercent < this.armorScale.getValue().floatValue()) {
                        return true;
                    }
                }
            }
        }
        return this.target.method_6032() + this.target.method_6067() <= this.facePlaceHp.getValue().floatValue();
    }

    private class_3965 getDefaultInteract(class_243 crystalVector, class_2338 bp) {
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null || Module.mc.field_1724.method_33571().method_1025(crystalVector) > square(this.placeRange.getValue().floatValue())) {
            return null;
        }
        class_3965 class_3965VarMethod_17742 = Module.mc.field_1687.method_17742(new class_3959(Module.mc.field_1724.method_33571(), crystalVector, class_3959.class_3960.field_17558, class_3959.class_242.field_1348, Module.mc.field_1724));
        if (class_3965VarMethod_17742 instanceof class_3965) {
            class_3965 blockHit = class_3965VarMethod_17742;
            if (blockHit.method_17783() == class_239.class_240.field_1332 && !blockHit.method_17777().equals(bp) && Module.mc.field_1724.method_33571().method_1025(crystalVector) > square(this.placeWallRange.getValue().floatValue())) {
                return null;
            }
        }
        class_2350 side = Module.mc.field_1687.method_24794(bp.method_10084()) ? class_2350.field_11036 : class_2350.field_11033;
        return new class_3965(crystalVector, side, bp, false);
    }

    private class_3965 getStrictInteract(class_2338 bp) {
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null) {
            return null;
        }
        float bestDistance = Float.MAX_VALUE;
        class_2350 bestDirection = null;
        class_243 bestVec = null;
        float upPoint = this.strictCenter.getValue().booleanValue() ? (float) bp.method_46558().method_10214() : bp.method_10084().method_10264();
        if (Module.mc.field_1724.method_33571().method_10214() > upPoint) {
            bestDirection = class_2350.field_11036;
        } else if (Module.mc.field_1724.method_33571().method_10214() < bp.method_10264() && Module.mc.field_1687.method_22347(bp.method_10074())) {
            bestDirection = class_2350.field_11033;
        }
        List<class_2350> directions = new ArrayList<>(List.of(class_2350.field_11043, class_2350.field_11035, class_2350.field_11034, class_2350.field_11039));
        if (bestDirection != null) {
            directions.add(bestDirection);
        }
        for (class_2350 dir : directions) {
            class_243 vec = bp.method_46558().method_1019(class_243.method_24954(dir.method_62675()).method_1021(0.5d));
            if (Module.mc.field_1687.method_8320(bp.method_10093(dir)).method_45474()) {
                double distanceSq = Module.mc.field_1724.method_33571().method_1025(vec);
                double maxDistanceSq = canSee(vec) ? square(this.placeRange.getValue().floatValue()) : square(this.placeWallRange.getValue().floatValue());
                if (distanceSq <= maxDistanceSq && distanceSq < bestDistance) {
                    bestDistance = (float) distanceSq;
                    bestDirection = dir;
                    bestVec = vec;
                }
            }
        }
        if (bestDirection == null || bestVec == null) {
            return null;
        }
        return new class_3965(bestVec, bestDirection, bp, false);
    }

    private void swingHand(boolean offHand, boolean attack) {
        class_1268 hand;
        hand = offHand ? class_1268.field_5810 : class_1268.field_5808;
        switch (this.swingMode.getValue()) {
            case "Both":
                Module.mc.field_1724.method_6104(hand);
                break;
            case "Break":
                if (attack) {
                    Module.mc.field_1724.method_6104(class_1268.field_5808);
                    break;
                } else {
                    sendPacket(new class_2879(class_1268.field_5808));
                    break;
                }
                break;
            case "Place":
                if (!attack) {
                    Module.mc.field_1724.method_6104(hand);
                    break;
                } else {
                    sendPacket(new class_2879(hand));
                    break;
                }
                break;
            case "ServerSide":
                sendPacket(new class_2879(hand));
                break;
            default:
                Module.mc.field_1724.method_6104(hand);
                break;
        }
    }

    private void predictAttack() {
        int attacks = this.idAttacks.getValue().intValue();
        for (int i = 1; i <= attacks; i++) {
            int id = (int) (this.currentId + ((long) i));
            class_1297 entity = Module.mc.field_1687.method_8469(id);
            if (entity == null || (entity instanceof class_1511)) {
                class_2824 attackPacket = class_2824.method_34206(Module.mc.field_1724, Module.mc.field_1724.method_5715());
                changeId(attackPacket, id);
                sendPacket(attackPacket);
                sendPacket(new class_2879(class_1268.field_5808));
            }
        }
    }

    private static void changeId(class_2824 packet, int id) {
        Field field;
        try {
            try {
                field = class_2824.class.getDeclaredField("entityId");
            } catch (NoSuchFieldException e) {
                field = class_2824.class.getDeclaredField("field_12870");
            }
            field.setAccessible(true);
            field.setInt(packet, id);
        } catch (Exception e2) {
        }
    }

    private void processSpawnPacket(int id) {
        if (id > this.currentId) {
            this.currentId = id;
        }
    }

    private void confirmAwaitingBySpawn(int entityId, class_243 spawnPos) {
        if (spawnPos == null) {
            return;
        }
        Map<class_2338, CrystalTracker.Attempt> awaiting = this.crystalTracker.getAwaitingPositions();
        if (awaiting.isEmpty()) {
            return;
        }
        long now = System.currentTimeMillis();
        for (Map.Entry<class_2338, CrystalTracker.Attempt> entry : new ArrayList(awaiting.entrySet())) {
            class_2338 bp = entry.getKey();
            if (spawnPos.method_1025(bp.method_46558()) <= 0.36d) {
                CrystalTracker.Attempt attempt = entry.getValue();
                this.confirmTime = now - attempt.time();
                this.crystalTracker.confirmSpawn(bp);
                if (passedTicks(this.breakTicks, this.breakDelay.getValue().intValue())) {
                    class_1297 entity = Module.mc.field_1687 == null ? null : Module.mc.field_1687.method_8469(entityId);
                    if (entity instanceof class_1511) {
                        class_1511 crystal = (class_1511) entity;
                        handleSpawn(crystal);
                        return;
                    } else {
                        attackSpawnById(entityId);
                        return;
                    }
                }
                return;
            }
        }
    }

    private void attackSpawnById(int entityId) {
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null || this.target == null || shouldPause()) {
            return;
        }
        class_2824 attackPacket = class_2824.method_34206(Module.mc.field_1724, Module.mc.field_1724.method_5715());
        changeId(attackPacket, entityId);
        sendPacket(attackPacket);
        swingHand(false, true);
        this.crystalTracker.setDead(entityId, System.currentTimeMillis());
        this.breakTicks = 0;
        this.rotationTicks = 10;
    }

    private class_243 resolveEntitySpawnPos(class_2604 packet) {
        Double x = tryReadCoordinate(packet, "getX", "x");
        Double y = tryReadCoordinate(packet, "getY", "y");
        Double z = tryReadCoordinate(packet, "getZ", "z");
        if (x != null && y != null && z != null) {
            return new class_243(x.doubleValue(), y.doubleValue(), z.doubleValue());
        }
        try {
            Method method = packet.getClass().getMethod("getPos", new Class[0]);
            Object value = method.invoke(packet, new Object[0]);
            if (value instanceof class_243) {
                class_243 vec = (class_243) value;
                return vec;
            }
            return null;
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }

    private class_243 resolveExplosionPos(class_2664 packet) {
        class_243 center = tryReadCenter(packet);
        if (center != null) {
            return center;
        }
        Double x = tryReadCoordinate(packet, "getX", "x");
        Double y = tryReadCoordinate(packet, "getY", "y");
        Double z = tryReadCoordinate(packet, "getZ", "z");
        if (x != null && y != null && z != null) {
            return new class_243(x.doubleValue(), y.doubleValue(), z.doubleValue());
        }
        if (Module.mc.field_1724 != null) {
            return Module.mc.field_1724.method_19538();
        }
        return class_243.field_1353;
    }

    private class_243 tryReadCenter(class_2664 packet) {
        try {
            Method method = packet.getClass().getMethod("center", new Class[0]);
            Object value = method.invoke(packet, new Object[0]);
            if (value instanceof class_243) {
                class_243 vec = (class_243) value;
                return vec;
            }
        } catch (ReflectiveOperationException e) {
        }
        try {
            Method method2 = packet.getClass().getMethod("getCenter", new Class[0]);
            Object value2 = method2.invoke(packet, new Object[0]);
            if (value2 instanceof class_243) {
                class_243 vec2 = (class_243) value2;
                return vec2;
            }
            return null;
        } catch (ReflectiveOperationException e2) {
            return null;
        }
    }

    private Double tryReadCoordinate(Object packet, String methodName, String fieldName) {
        try {
            Method method = packet.getClass().getMethod(methodName, new Class[0]);
            Object value = method.invoke(packet, new Object[0]);
            if (value instanceof Number) {
                Number number = (Number) value;
                return Double.valueOf(number.doubleValue());
            }
        } catch (ReflectiveOperationException e) {
        }
        try {
            Field field = packet.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value2 = field.get(packet);
            if (value2 instanceof Number) {
                Number number2 = (Number) value2;
                return Double.valueOf(number2.doubleValue());
            }
            return null;
        } catch (ReflectiveOperationException e2) {
            return null;
        }
    }

    private int getPredictTicks() {
        return 0;
    }

    public class_1657 getTarget() {
        return this.target;
    }

    private int getSelfPredictTicks() {
        return (int) Math.ceil((getPing() * 1.5f) / 50.0f);
    }

    private float getPing() {
        class_640 entry;
        if (Module.mc.method_1562() == null || Module.mc.field_1724 == null || (entry = Module.mc.method_1562().method_2871(Module.mc.field_1724.method_5667())) == null) {
            return 0.0f;
        }
        return entry.method_2959();
    }

    private boolean shouldPause() {
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null || Module.mc.field_1761 == null) {
            return true;
        }
        boolean offhand = Module.mc.field_1724.method_6079().method_7909() instanceof class_1774;
        boolean mainHand = Module.mc.field_1724.method_6047().method_7909() instanceof class_1774;
        if (Module.mc.field_1761.method_2923() && !offhand && this.mining.getValue().booleanValue()) {
            this.currentState = State.Mining;
            return true;
        }
        if (this.autoSwitch.is("NONE") && !offhand && !mainHand) {
            this.currentState = State.NoCrystalls;
            return true;
        }
        if ((this.autoSwitch.is("SILENT") || this.autoSwitch.is("NORMAL")) && !findInHotbar(class_1802.field_8301).found() && !offhand) {
            this.currentState = State.NoCrystalls;
            return true;
        }
        if (this.autoSwitch.is("INVENTORY") && findAny(class_1802.field_8301) == -1 && !offhand) {
            this.currentState = State.NoCrystalls;
            return true;
        }
        if (Module.mc.field_1724.method_6115() && this.eating.getValue().booleanValue()) {
            this.currentState = State.Eating;
            return true;
        }
        if (rotationMarkedDirty()) {
            this.currentState = State.ExternalPause;
            return true;
        }
        if (Module.mc.field_1724.method_6032() + Module.mc.field_1724.method_6067() < this.pauseHP.getValue().floatValue()) {
            this.currentState = State.LowHP;
            return true;
        }
        this.currentState = State.Active;
        return false;
    }

    private boolean rotationMarkedDirty() {
        ClickPearlModule clickPearl;
        SurroundModule surroundModule;
        if (this.surround.getValue().booleanValue() && (surroundModule = SurroundModule.getInstance()) != null && surroundModule.isEnabled()) {
            return true;
        }
        if (this.middleClick.getValue().booleanValue() && Module.mc.field_1690.field_1871.method_1434() && (clickPearl = ClickPearlModule.getInstance()) != null && clickPearl.isEnabled() && clickPearl.isThrowingPearl()) {
            return true;
        }
        if (this.inventoryPause.getValue().booleanValue() && Module.mc.field_1755 != null) {
            return true;
        }
        return false;
    }

    private class_1657 findTarget(float range, String logic) {
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null) {
            return null;
        }
        List<class_1657> candidates = new ArrayList<>();
        for (class_746 class_746Var : Module.mc.field_1687.method_18456()) {
            if (class_746Var != null && class_746Var != Module.mc.field_1724 && class_746Var.method_5805() && !class_746Var.method_7325() && !FriendManager.getInstance().contains(class_746Var.method_5477().getString()) && Module.mc.field_1724.method_5858(class_746Var) <= square(range)) {
                candidates.add(class_746Var);
            }
        }
        if (candidates.isEmpty()) {
            return null;
        }
        if ("Health".equalsIgnoreCase(logic)) {
            return candidates.stream().min(Comparator.comparingDouble(p -> {
                return p.method_6032() + p.method_6067();
            })).orElse(null);
        }
        Stream<class_1657> stream = candidates.stream();
        class_746 class_746Var2 = Module.mc.field_1724;
        Objects.requireNonNull(class_746Var2);
        return stream.min(Comparator.comparingDouble((v1) -> {
            return r1.method_5858(v1);
        })).orElse(null);
    }

    private HotbarSearch findInHotbar(class_1792 item) {
        int slot = InventoryUtil.findItem(item, true);
        return new HotbarSearch(slot != -1, slot);
    }

    private int findInInventory(class_1792 item) {
        return InventoryUtil.findItem(item, false);
    }

    private int findAny(class_1792 item) {
        int hotbar = InventoryUtil.findItem(item, true);
        if (hotbar != -1) {
            return hotbar;
        }
        return InventoryUtil.findItem(item, false);
    }

    private HotbarSearch findAntiWeaknessHotbar() {
        if (Module.mc.field_1724 == null) {
            return new HotbarSearch(false, -1);
        }
        for (int i = 0; i < 9; i++) {
            class_1792 item = ((class_1799) Module.mc.field_1724.method_31548().field_7547.get(i)).method_7909();
            if ((item instanceof class_1829) || (item instanceof class_1810) || (item instanceof class_1743) || (item instanceof class_1821)) {
                return new HotbarSearch(true, i);
            }
        }
        return new HotbarSearch(false, -1);
    }

    private int findAntiWeaknessInventory() {
        if (Module.mc.field_1724 == null) {
            return -1;
        }
        for (int i = 9; i < 36; i++) {
            class_1792 item = ((class_1799) Module.mc.field_1724.method_31548().field_7547.get(i)).method_7909();
            if ((item instanceof class_1829) || (item instanceof class_1810) || (item instanceof class_1743) || (item instanceof class_1821)) {
                return i;
            }
        }
        return -1;
    }

    private void sendPacket(class_2596<?> packet) {
        if (Module.mc.method_1562() != null && packet != null) {
            Module.mc.method_1562().method_52787(packet);
        }
    }

    private boolean passedTicks(int counter, int delay) {
        return counter >= Math.max(0, delay);
    }

    private void cleanupRenderPositions() {
        long now = System.currentTimeMillis();
        long holdMs = this.renderHoldMs.getValue().longValue();
        this.renderPositions.entrySet().removeIf(entry -> {
            return now - ((Long) entry.getValue()).longValue() > holdMs;
        });
    }

    private double square(double value) {
        return value * value;
    }

    private boolean render(String key) {
        return this.renderSettings.isEnabled(key);
    }

    private Color themed(float dim, int alpha) {
        Color c = UIColors.primary(class_3532.method_15340(alpha, 0, 255));
        int r = class_3532.method_15340((int) (c.getRed() * dim), 0, 255);
        int g = class_3532.method_15340((int) (c.getGreen() * dim), 0, 255);
        int b = class_3532.method_15340((int) (c.getBlue() * dim), 0, 255);
        return new Color(r, g, b, c.getAlpha());
    }

    private float animate(AnimationUtil animation, boolean state) {
        if (!render("Animate")) {
            animation.setValue(state ? 1.0d : 0.0d);
            return state ? 1.0f : 0.0f;
        }
        animation.run(state ? 1.0d : 0.0d, 260L, Easing.SINE_OUT, true);
        animation.update();
        return (float) animation.getValue();
    }

    private String round2(float value) {
        return String.format(Locale.US, "%.2f", Float.valueOf(value));
    }

    private boolean canSee(class_243 pos) {
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null) {
            return false;
        }
        class_3965 result = Module.mc.field_1687.method_17742(new class_3959(Module.mc.field_1724.method_33571(), pos, class_3959.class_3960.field_17558, class_3959.class_242.field_1348, Module.mc.field_1724));
        return result.method_17783() == class_239.class_240.field_1333;
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$PlaceData.class */
    public static final class PlaceData extends Record {
        private final class_3965 bhr;
        private final float damage;
        private final float selfDamage;
        private final boolean overrideDamage;

        public PlaceData(class_3965 bhr, float damage, float selfDamage, boolean overrideDamage) {
            this.bhr = bhr;
            this.damage = damage;
            this.selfDamage = selfDamage;
            this.overrideDamage = overrideDamage;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, PlaceData.class), PlaceData.class, "bhr;damage;selfDamage;overrideDamage", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$PlaceData;->bhr:Lnet/minecraft/class_3965;", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$PlaceData;->damage:F", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$PlaceData;->selfDamage:F", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$PlaceData;->overrideDamage:Z").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, PlaceData.class), PlaceData.class, "bhr;damage;selfDamage;overrideDamage", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$PlaceData;->bhr:Lnet/minecraft/class_3965;", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$PlaceData;->damage:F", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$PlaceData;->selfDamage:F", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$PlaceData;->overrideDamage:Z").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, PlaceData.class, Object.class), PlaceData.class, "bhr;damage;selfDamage;overrideDamage", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$PlaceData;->bhr:Lnet/minecraft/class_3965;", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$PlaceData;->damage:F", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$PlaceData;->selfDamage:F", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$PlaceData;->overrideDamage:Z").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public class_3965 bhr() {
            return this.bhr;
        }

        public float damage() {
            return this.damage;
        }

        public float selfDamage() {
            return this.selfDamage;
        }

        public boolean overrideDamage() {
            return this.overrideDamage;
        }
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$CrystalData.class */
    private static final class CrystalData extends Record {
        private final class_1511 crystal;
        private final float damage;
        private final float selfDamage;
        private final boolean overrideDamage;

        private CrystalData(class_1511 crystal, float damage, float selfDamage, boolean overrideDamage) {
            this.crystal = crystal;
            this.damage = damage;
            this.selfDamage = selfDamage;
            this.overrideDamage = overrideDamage;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, CrystalData.class), CrystalData.class, "crystal;damage;selfDamage;overrideDamage", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$CrystalData;->crystal:Lnet/minecraft/class_1511;", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$CrystalData;->damage:F", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$CrystalData;->selfDamage:F", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$CrystalData;->overrideDamage:Z").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, CrystalData.class), CrystalData.class, "crystal;damage;selfDamage;overrideDamage", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$CrystalData;->crystal:Lnet/minecraft/class_1511;", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$CrystalData;->damage:F", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$CrystalData;->selfDamage:F", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$CrystalData;->overrideDamage:Z").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, CrystalData.class, Object.class), CrystalData.class, "crystal;damage;selfDamage;overrideDamage", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$CrystalData;->crystal:Lnet/minecraft/class_1511;", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$CrystalData;->damage:F", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$CrystalData;->selfDamage:F", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$CrystalData;->overrideDamage:Z").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public class_1511 crystal() {
            return this.crystal;
        }

        public float damage() {
            return this.damage;
        }

        public float selfDamage() {
            return this.selfDamage;
        }

        public boolean overrideDamage() {
            return this.overrideDamage;
        }
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$RotationVec.class */
    private static final class RotationVec extends Record {
        private final class_243 vec;
        private final class_3965 hitVec;
        private final boolean place;

        private RotationVec(class_243 vec, class_3965 hitVec, boolean place) {
            this.vec = vec;
            this.hitVec = hitVec;
            this.place = place;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, RotationVec.class), RotationVec.class, "vec;hitVec;place", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$RotationVec;->vec:Lnet/minecraft/class_243;", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$RotationVec;->hitVec:Lnet/minecraft/class_3965;", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$RotationVec;->place:Z").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, RotationVec.class), RotationVec.class, "vec;hitVec;place", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$RotationVec;->vec:Lnet/minecraft/class_243;", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$RotationVec;->hitVec:Lnet/minecraft/class_3965;", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$RotationVec;->place:Z").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, RotationVec.class, Object.class), RotationVec.class, "vec;hitVec;place", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$RotationVec;->vec:Lnet/minecraft/class_243;", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$RotationVec;->hitVec:Lnet/minecraft/class_3965;", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$RotationVec;->place:Z").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public class_243 vec() {
            return this.vec;
        }

        public class_3965 hitVec() {
            return this.hitVec;
        }

        public boolean place() {
            return this.place;
        }
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$HotbarSearch.class */
    private static final class HotbarSearch extends Record {
        private final boolean found;
        private final int slot;

        private HotbarSearch(boolean found, int slot) {
            this.found = found;
            this.slot = slot;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, HotbarSearch.class), HotbarSearch.class, "found;slot", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$HotbarSearch;->found:Z", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$HotbarSearch;->slot:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, HotbarSearch.class), HotbarSearch.class, "found;slot", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$HotbarSearch;->found:Z", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$HotbarSearch;->slot:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, HotbarSearch.class, Object.class), HotbarSearch.class, "found;slot", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$HotbarSearch;->found:Z", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$HotbarSearch;->slot:I").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public boolean found() {
            return this.found;
        }

        public int slot() {
            return this.slot;
        }
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$CrystalTracker.class */
    private static final class CrystalTracker {
        private final Map<Integer, Long> deadCrystals = new ConcurrentHashMap();
        private final Map<Integer, Integer> attackAttempts = new ConcurrentHashMap();
        private final Map<Integer, Long> blockedCrystals = new ConcurrentHashMap();
        private final Map<class_2338, Attempt> awaitingPositions = new ConcurrentHashMap();

        private CrystalTracker() {
        }

        void reset() {
            this.deadCrystals.clear();
            this.attackAttempts.clear();
            this.blockedCrystals.clear();
            this.awaitingPositions.clear();
        }

        void update() {
            long now = System.currentTimeMillis();
            this.deadCrystals.entrySet().removeIf(entry -> {
                return now - ((Long) entry.getValue()).longValue() > 1800;
            });
            this.blockedCrystals.entrySet().removeIf(entry2 -> {
                return now - ((Long) entry2.getValue()).longValue() > 1800;
            });
            this.awaitingPositions.entrySet().removeIf(entry3 -> {
                return now - ((Attempt) entry3.getValue()).time() > 2500;
            });
        }

        void onAttack(class_1511 crystal, boolean breakFailsafeEnabled, int maxAttempts) {
            if (crystal == null) {
                return;
            }
            int id = crystal.method_5628();
            setDead(id, System.currentTimeMillis());
            if (breakFailsafeEnabled) {
                int attempts = this.attackAttempts.getOrDefault(Integer.valueOf(id), 0).intValue() + 1;
                this.attackAttempts.put(Integer.valueOf(id), Integer.valueOf(attempts));
                if (attempts >= Math.max(1, maxAttempts)) {
                    this.blockedCrystals.put(Integer.valueOf(id), Long.valueOf(System.currentTimeMillis()));
                }
            }
        }

        boolean isDead(int id) {
            return this.deadCrystals.containsKey(Integer.valueOf(id));
        }

        void setDead(int id, long time) {
            this.deadCrystals.put(Integer.valueOf(id), Long.valueOf(time));
        }

        boolean isBlocked(int id) {
            return this.blockedCrystals.containsKey(Integer.valueOf(id));
        }

        void addAwaitingPos(class_2338 pos, boolean placeFailsafeEnabled) {
            Attempt previous = this.awaitingPositions.get(pos);
            int nextAttempt = previous == null ? 1 : previous.attempts() + 1;
            this.awaitingPositions.put(pos.method_10062(), new Attempt(System.currentTimeMillis(), placeFailsafeEnabled ? nextAttempt : 1));
        }

        boolean isPositionBlocked(class_2338 pos, boolean placeFailsafeEnabled, int maxAttempts) {
            Attempt attempt;
            return placeFailsafeEnabled && (attempt = this.awaitingPositions.get(pos)) != null && attempt.attempts() >= Math.max(1, maxAttempts);
        }

        void confirmSpawn(class_2338 pos) {
            this.awaitingPositions.remove(pos);
        }

        Map<class_2338, Attempt> getAwaitingPositions() {
            return this.awaitingPositions;
        }

        /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$CrystalTracker$Attempt.class */
        static final class Attempt extends Record {
            private final long time;
            private final int attempts;

            Attempt(long time, int attempts) {
                this.time = time;
                this.attempts = attempts;
            }

            @Override // java.lang.Record
            public final String toString() {
                return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Attempt.class), Attempt.class, "time;attempts", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$CrystalTracker$Attempt;->time:J", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$CrystalTracker$Attempt;->attempts:I").dynamicInvoker().invoke(this) /* invoke-custom */;
            }

            @Override // java.lang.Record
            public final int hashCode() {
                return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Attempt.class), Attempt.class, "time;attempts", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$CrystalTracker$Attempt;->time:J", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$CrystalTracker$Attempt;->attempts:I").dynamicInvoker().invoke(this) /* invoke-custom */;
            }

            @Override // java.lang.Record
            public final boolean equals(Object o) {
                return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Attempt.class, Object.class), Attempt.class, "time;attempts", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$CrystalTracker$Attempt;->time:J", "FIELD:Lsweetie/leonware/client/features/modules/combat/crystalaura/CrystalAuraProcessor$CrystalTracker$Attempt;->attempts:I").dynamicInvoker().invoke(this, o) /* invoke-custom */;
            }

            public long time() {
                return this.time;
            }

            public int attempts() {
                return this.attempts;
            }
        }
    }
}
