/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1268
 *  net.minecraft.class_1293
 *  net.minecraft.class_1294
 *  net.minecraft.class_1297
 *  net.minecraft.class_1303
 *  net.minecraft.class_1309
 *  net.minecraft.class_1511
 *  net.minecraft.class_1657
 *  net.minecraft.class_1713
 *  net.minecraft.class_1743
 *  net.minecraft.class_1774
 *  net.minecraft.class_1792
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_1810
 *  net.minecraft.class_1821
 *  net.minecraft.class_1829
 *  net.minecraft.class_2246
 *  net.minecraft.class_2248
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_2374
 *  net.minecraft.class_238
 *  net.minecraft.class_2382
 *  net.minecraft.class_239$class_240
 *  net.minecraft.class_243
 *  net.minecraft.class_2596
 *  net.minecraft.class_2604
 *  net.minecraft.class_2606
 *  net.minecraft.class_2664
 *  net.minecraft.class_2815
 *  net.minecraft.class_2824
 *  net.minecraft.class_2868
 *  net.minecraft.class_2879
 *  net.minecraft.class_3532
 *  net.minecraft.class_3959
 *  net.minecraft.class_3959$class_242
 *  net.minecraft.class_3959$class_3960
 *  net.minecraft.class_3965
 *  net.minecraft.class_640
 *  net.minecraft.class_746
 *  org.joml.Vector2f
 */
package sweetie.leonware.client.features.modules.combat.crystalaura;

import java.awt.Color;
import java.lang.invoke.CallSite;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.class_1268;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1303;
import net.minecraft.class_1309;
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
import net.minecraft.class_2374;
import net.minecraft.class_238;
import net.minecraft.class_2382;
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
import sweetie.leonware.client.features.modules.combat.crystalaura.CrystalAuraModule;
import sweetie.leonware.client.features.modules.movement.MoveFixModule;
import sweetie.leonware.client.features.modules.player.ClickPearlModule;

public class CrystalAuraProcessor {
    private static final float RENDER_BOX_SIZE = 2.0f;
    private final CrystalAuraModule module;
    public final ModeSetting page = new ModeSetting("Page").values("Main", "Place", "Break", "Damages", "Pause", "Render", "Switch", "FailSafe", "Info", "IDPredict").value("Main");
    public final BooleanSetting await = new BooleanSetting("Await").value(true).setVisible(() -> this.page.is("Main"));
    public final ModeSetting timing = new ModeSetting("Timing").values("NORMAL", "SEQUENTIAL").value("NORMAL").setVisible(() -> this.page.is("Main"));
    public final ModeSetting sequential = new ModeSetting("Sequential").values("Off", "Strict", "Strong").value("Strong").setVisible(() -> this.page.is("Main"));
    public final ModeSetting rotate = new ModeSetting("Rotate").values("Off", "Instant", "Smooth").value("Smooth").setVisible(() -> this.page.is("Main"));
    public final BooleanSetting yawStep = new BooleanSetting("YawStep").value(false).setVisible(() -> !this.rotate.is("Off") && this.page.is("Main"));
    public final SliderSetting yawAngle = new SliderSetting("YawAngle").value(Float.valueOf(180.0f)).range(1.0f, 180.0f).step(1.0f).setVisible(() -> !this.rotate.is("Off") && (Boolean)this.yawStep.getValue() != false && this.page.is("Main"));
    public final ModeSetting targetLogic = new ModeSetting("TargetLogic").values("Distance", "Health").value("Distance").setVisible(() -> this.page.is("Main"));
    public final SliderSetting targetRange = new SliderSetting("TargetRange").value(Float.valueOf(10.0f)).range(1.0f, 15.0f).step(0.1f).setVisible(() -> this.page.is("Main"));
    public final ModeSetting interact = new ModeSetting("Interact").values("Default", "Strict").value("Default").setVisible(() -> this.page.is("Place"));
    public final BooleanSetting strictCenter = new BooleanSetting("CCStrict").value(true).setVisible(() -> this.page.is("Place") && this.interact.is("Strict"));
    public final BooleanSetting rayTraceBypass = new BooleanSetting("RayTraceBypass").value(false).setVisible(() -> this.page.is("Place"));
    public final SliderSetting placeDelay = new SliderSetting("PlaceDelay").value(Float.valueOf(0.0f)).range(0.0f, 20.0f).step(1.0f).setVisible(() -> this.page.is("Place"));
    public final SliderSetting placeRange = new SliderSetting("PlaceRange").value(Float.valueOf(5.0f)).range(1.0f, 6.0f).step(0.1f).setVisible(() -> this.page.is("Place"));
    public final SliderSetting placeWallRange = new SliderSetting("PlaceWallRange").value(Float.valueOf(3.5f)).range(0.0f, 6.0f).step(0.1f).setVisible(() -> this.page.is("Place"));
    public final BooleanSetting inhibit = new BooleanSetting("Inhibit").value(true).setVisible(() -> this.page.is("Break"));
    public final SliderSetting breakDelay = new SliderSetting("BreakDelay").value(Float.valueOf(0.0f)).range(0.0f, 20.0f).step(1.0f).setVisible(() -> this.page.is("Break"));
    public final SliderSetting explodeRange = new SliderSetting("BreakRange").value(Float.valueOf(5.0f)).range(1.0f, 6.0f).step(0.1f).setVisible(() -> this.page.is("Break"));
    public final SliderSetting explodeWallRange = new SliderSetting("BreakWallRange").value(Float.valueOf(3.5f)).range(0.0f, 6.0f).step(0.1f).setVisible(() -> this.page.is("Break"));
    public final BooleanSetting mining = new BooleanSetting("Mining").value(true).setVisible(() -> this.page.is("Pause"));
    public final BooleanSetting eating = new BooleanSetting("Eating").value(true).setVisible(() -> this.page.is("Pause"));
    public final BooleanSetting surround = new BooleanSetting("Surround").value(true).setVisible(() -> this.page.is("Pause"));
    public final BooleanSetting middleClick = new BooleanSetting("MiddleClick").value(true).setVisible(() -> this.page.is("Pause"));
    public final BooleanSetting inventoryPause = new BooleanSetting("Inventory").value(false).setVisible(() -> this.page.is("Pause"));
    public final SliderSetting pauseHP = new SliderSetting("HP").value(Float.valueOf(8.0f)).range(2.0f, 10.0f).step(0.1f).setVisible(() -> this.page.is("Pause"));
    public final SliderSetting minDamage = new SliderSetting("MinDamage").value(Float.valueOf(6.0f)).range(2.0f, 20.0f).step(0.1f).setVisible(() -> this.page.is("Damages"));
    public final SliderSetting maxSelfDamage = new SliderSetting("MaxSelfDamage").value(Float.valueOf(10.0f)).range(2.0f, 20.0f).step(0.1f).setVisible(() -> this.page.is("Damages"));
    public final BooleanSetting efficiency = new BooleanSetting("Efficiency").value(false).setVisible(() -> this.page.is("Damages"));
    public final SliderSetting efficiencyFactor = new SliderSetting("EfficiencyFactor").value(Float.valueOf(1.0f)).range(0.1f, 5.0f).step(0.1f).setVisible(() -> this.page.is("Damages") && (Boolean)this.efficiency.getValue() != false);
    public final BooleanSetting protectFriends = new BooleanSetting("ProtectFriends").value(true).setVisible(() -> this.page.is("Damages"));
    public final BooleanSetting overrideSelfDamage = new BooleanSetting("OverrideSelfDamage").value(true).setVisible(() -> this.page.is("Damages"));
    public final BooleanSetting sacrificeTotem = new BooleanSetting("SacrificeTotem").value(true).setVisible(() -> this.page.is("Damages") && (Boolean)this.overrideSelfDamage.getValue() != false);
    public final BooleanSetting armorBreaker = new BooleanSetting("ArmorBreaker").value(true).setVisible(() -> this.page.is("Damages"));
    public final SliderSetting armorScale = new SliderSetting("Armor %").value(Float.valueOf(5.0f)).range(0.0f, 40.0f).step(0.1f).setVisible(() -> this.page.is("Damages") && (Boolean)this.armorBreaker.getValue() != false);
    public final SliderSetting facePlaceHp = new SliderSetting("FacePlaceHp").value(Float.valueOf(5.0f)).range(0.0f, 20.0f).step(0.1f).setVisible(() -> this.page.is("Damages"));
    public final BindSetting facePlaceButton = new BindSetting("FacePlaceBtn").value(-999).setVisible(() -> this.page.is("Damages"));
    public final BooleanSetting ignoreTerrain = new BooleanSetting("IgnoreTerrain").value(true).setVisible(() -> this.page.is("Damages"));
    public final ModeSetting autoSwitch = new ModeSetting("Switch").values("NONE", "NORMAL", "SILENT", "INVENTORY").value("NORMAL").setVisible(() -> this.page.is("Switch"));
    public final ModeSetting antiWeakness = new ModeSetting("AntiWeakness").values("NONE", "NORMAL", "SILENT", "INVENTORY").value("SILENT").setVisible(() -> this.page.is("Switch"));
    public final BooleanSetting placeFailsafe = new BooleanSetting("PlaceFailsafe").value(true).setVisible(() -> this.page.is("FailSafe"));
    public final BooleanSetting breakFailsafe = new BooleanSetting("BreakFailsafe").value(true).setVisible(() -> this.page.is("FailSafe"));
    public final SliderSetting attempts = new SliderSetting("MaxAttempts").value(Float.valueOf(5.0f)).range(1.0f, 30.0f).step(1.0f).setVisible(() -> this.page.is("FailSafe"));
    public final BooleanSetting idPredict = new BooleanSetting("IDPredict").value(false).setVisible(() -> this.page.is("IDPredict"));
    public final SliderSetting idAttacks = new SliderSetting("IDAttacks").value(Float.valueOf(3.0f)).range(1.0f, 10.0f).step(1.0f).setVisible(() -> this.page.is("IDPredict"));
    public final ModeSetting swingMode = new ModeSetting("Swing").values("Both", "Place", "Break", "ServerSide").value("Place").setVisible(() -> this.page.is("Render"));
    public final MultiBooleanSetting renderSettings = new MultiBooleanSetting("Render").value(new BooleanSetting("Block").value(true), new BooleanSetting("Rect").value(true), new BooleanSetting("Animate").value(true)).setVisible(() -> this.page.is("Render"));
    public final SliderSetting renderHoldMs = new SliderSetting("RenderHoldMs").value(Float.valueOf(500.0f)).range(100.0f, 1000.0f).step(10.0f).setVisible(() -> this.page.is("Render"));
    public final BooleanSetting confirmInfo = new BooleanSetting("ConfirmTime").value(true).setVisible(() -> this.page.is("Info"));
    public final BooleanSetting calcInfo = new BooleanSetting("CalcInfo").value(false).setVisible(() -> this.page.is("Info"));
    private final AnimationUtil blockRenderAnim = new AnimationUtil();
    private final CrystalTracker crystalTracker = new CrystalTracker();
    private final Map<class_2338, Long> renderPositions = new ConcurrentHashMap<class_2338, Long>();
    private class_1657 target;
    private PlaceData currentData;
    private class_3965 bestPosition;
    private class_1511 bestCrystal;
    private class_1511 secondaryCrystal;
    private RotationVec rotationVec;
    private State currentState = State.NoTarget;
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
    private int lastTargetId = -1;
    private long externalPauseUntilMs = 0L;
    private int placeTicks;
    private int breakTicks;
    private int calcTicks;
    private int placeSyncTicks;
    private class_2338 renderPos;
    private class_2338 prevRenderPos;

    public CrystalAuraProcessor(CrystalAuraModule module) {
        this.module = module;
    }

    public void initSettings(CrystalAuraModule module) {
        module.addSettings(this.page, this.await, this.timing, this.sequential, this.rotate, this.yawStep, this.yawAngle, this.targetLogic, this.targetRange, this.interact, this.strictCenter, this.rayTraceBypass, this.placeDelay, this.placeRange, this.placeWallRange, this.inhibit, this.breakDelay, this.explodeRange, this.explodeWallRange, this.mining, this.eating, this.surround, this.middleClick, this.inventoryPause, this.pauseHP, this.minDamage, this.maxSelfDamage, this.efficiency, this.efficiencyFactor, this.protectFriends, this.overrideSelfDamage, this.sacrificeTotem, this.armorBreaker, this.armorScale, this.facePlaceHp, this.facePlaceButton, this.ignoreTerrain, this.autoSwitch, this.antiWeakness, this.placeFailsafe, this.breakFailsafe, this.attempts, this.idPredict, this.idAttacks, this.swingMode, this.renderSettings, this.renderHoldMs, this.confirmInfo, this.calcInfo);
    }

    public boolean check() {
        return this.target != null || this.bestPosition != null || this.bestCrystal != null || !this.renderPositions.isEmpty();
    }

    public void onPacket(PacketEvent.PacketEventData event) {
        if (!event.isReceive() || Module.mc.field_1724 == null || Module.mc.field_1687 == null) {
            return;
        }
        class_2596<?> class_25962 = event.packet();
        if (class_25962 instanceof class_2606) {
            class_2606 spawn = (class_2606)class_25962;
            this.processSpawnPacket(spawn.method_11183());
        } else {
            class_25962 = event.packet();
            if (class_25962 instanceof class_2604) {
                class_2604 spawn = (class_2604)class_25962;
                this.processSpawnPacket(spawn.method_11167());
                this.confirmAwaitingBySpawn(spawn.method_11167(), this.resolveEntitySpawnPos(spawn));
            } else {
                class_25962 = event.packet();
                if (class_25962 instanceof class_2664) {
                    class_2664 explosion = (class_2664)class_25962;
                    class_243 explosionPos = this.resolveExplosionPos(explosion);
                    for (class_1297 ent : Module.mc.field_1687.method_18112()) {
                        class_1511 crystal;
                        if (!(ent instanceof class_1511) || !((crystal = (class_1511)ent).method_5707(explosionPos) <= 144.0) || this.crystalTracker.isDead(crystal.method_5628())) continue;
                        this.crystalTracker.setDead(crystal.method_5628(), System.currentTimeMillis());
                    }
                }
            }
        }
    }

    public void onUpdate(UpdateEvent event) {
        boolean targetChanged;
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null || Module.mc.field_1761 == null) {
            return;
        }
        if (!this.initialized) {
            this.reset();
            this.initialized = true;
        }
        this.tickTimers();
        this.crystalTracker.update();
        this.cleanupRenderPositions();
        this.processAwaitingSpawnConfirm();
        this.target = this.findTarget(((Float)this.targetRange.getValue()).floatValue(), (String)this.targetLogic.getValue());
        if (this.target != null && (!this.target.method_5805() || this.target.method_6032() <= 0.0f)) {
            this.target = null;
            this.currentState = State.NoTarget;
            this.lastTargetId = -1;
            this.clearCombatContext();
            return;
        }
        if (this.target == null) {
            this.currentState = State.NoTarget;
            this.lastTargetId = -1;
            this.clearCombatContext();
            return;
        }
        if (this.passedTicks(this.placeTicks, 20)) {
            this.renderDamage = 0.0f;
        }
        this.calcRotations();
        boolean bl = targetChanged = this.lastTargetId != this.target.method_5628();
        if (targetChanged) {
            this.lastTargetId = this.target.method_5628();
            this.calcTicks = Integer.MAX_VALUE;
        }
        int pingGate = Math.max(1, Math.round(this.getPing() / 25.0f));
        if (targetChanged || this.bestPosition == null || !((Boolean)this.await.getValue()).booleanValue() || this.passedTicks(this.placeTicks, 20) || this.passedTicks(this.calcTicks, pingGate)) {
            this.calcPosition(((Float)this.placeRange.getValue()).floatValue(), Module.mc.field_1724.method_19538());
        }
        this.getCrystalToExplode();
        if (this.timing.is("NORMAL") || this.timing.is("SEQUENTIAL")) {
            this.doAction();
        }
    }

    public void onRender3D(Render3DEvent.Render3DEventData event) {
        float blockAnim;
        this.cleanupRenderPositions();
        boolean canRenderBlock = this.render("Block") && !this.renderPositions.isEmpty();
        this.blockRenderFactor = blockAnim = this.animate(this.blockRenderAnim, canRenderBlock);
        if (canRenderBlock && blockAnim > 0.01f) {
            long now = System.currentTimeMillis();
            long holdMs = ((Float)this.renderHoldMs.getValue()).longValue();
            for (Map.Entry<class_2338, Long> entry : new ArrayList<Map.Entry<class_2338, Long>>(this.renderPositions.entrySet())) {
                class_2338 pos = entry.getKey();
                long elapsed = now - entry.getValue();
                if (elapsed > holdMs) continue;
                float fade = 1.0f - class_3532.method_15363((float)((float)elapsed / (float)holdMs), (float)0.0f, (float)1.0f);
                Color fill = this.themed(0.68f, (int)(56.0f * blockAnim * fade));
                Color rect = this.themed(0.82f, (int)(160.0f * blockAnim * fade));
                float x = pos.method_10263();
                float y = pos.method_10264();
                float z = pos.method_10260();
                RenderUtil.BOX.drawBox(x, y, z, x + 2.0f, y + 2.0f, z + 2.0f, 1.15f, fill, BoxRender.Render.FILL, 0.0f);
                if (!this.render("Rect")) continue;
                RenderUtil.BOX.drawBox(x, y, z, x + 2.0f, y + 2.0f, z + 2.0f, 1.95f, rect, BoxRender.Render.OUTLINE, 0.0f);
            }
        }
    }

    public void onRender2D(Render2DEvent.Render2DEventData event) {
        class_2338 infoPos;
        if (!((Boolean)this.confirmInfo.getValue()).booleanValue() && !((Boolean)this.calcInfo.getValue()).booleanValue()) {
            return;
        }
        Object object = this.renderPos != null ? this.renderPos : (infoPos = this.bestPosition != null ? this.bestPosition.method_17777() : null);
        if (infoPos == null) {
            return;
        }
        ArrayList<CallSite> lines = new ArrayList<CallSite>();
        if (((Boolean)this.confirmInfo.getValue()).booleanValue() && this.confirmTime > 0L) {
            lines.add((CallSite)((Object)("Confirm: " + this.confirmTime + "ms")));
        }
        if (((Boolean)this.calcInfo.getValue()).booleanValue()) {
            lines.add((CallSite)((Object)("Calc: " + this.calcTime + "ms")));
        }
        if (lines.isEmpty()) {
            return;
        }
        Vector2f projected = ProjectionUtil.project(new class_243((double)((float)infoPos.method_10263() + 1.0f), (double)((float)infoPos.method_10264() + 1.0f), (double)((float)infoPos.method_10260() + 1.0f)));
        if (projected.x == Float.MAX_VALUE && projected.y == Float.MAX_VALUE) {
            return;
        }
        float fontSize = 6.5f;
        float lineHeight = fontSize + 1.0f;
        float drawY = projected.y - (float)(lines.size() - 1) * lineHeight / 2.0f;
        Color textColor = new Color(160, 160, 160, 220);
        for (String string : lines) {
            Fonts.SF_MEDIUM.drawCenteredText(event.matrixStack(), string, projected.x, drawY, fontSize, textColor);
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
        this.blockRenderAnim.setValue(0.0);
    }

    private void tickTimers() {
        ++this.placeTicks;
        ++this.breakTicks;
        ++this.calcTicks;
        ++this.placeSyncTicks;
    }

    private void doAction() {
        if (this.target == null) {
            return;
        }
        if (this.sequential.is("Off")) {
            if (this.bestCrystal != null && this.passedTicks(this.breakTicks, ((Float)this.breakDelay.getValue()).intValue())) {
                this.attackCrystal(this.bestCrystal);
            } else if (this.bestPosition != null && this.passedTicks(this.placeTicks, ((Float)this.placeDelay.getValue()).intValue()) && !this.placedOnSpawn) {
                this.placeCrystal(this.bestPosition, false, false);
            }
        } else {
            if (this.bestCrystal != null && this.passedTicks(this.breakTicks, ((Float)this.breakDelay.getValue()).intValue())) {
                this.attackCrystal(this.bestCrystal);
            }
            if (this.bestPosition != null && this.passedTicks(this.placeTicks, ((Float)this.placeDelay.getValue()).intValue()) && !this.placedOnSpawn) {
                this.placeCrystal(this.bestPosition, false, false);
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
        for (Map.Entry<class_2338, CrystalTracker.Attempt> entry : new ArrayList<Map.Entry<class_2338, CrystalTracker.Attempt>>(awaiting.entrySet())) {
            class_2338 bp = entry.getKey();
            CrystalTracker.Attempt attempt = entry.getValue();
            class_238 searchBox = new class_238(bp.method_10084()).method_1009(0.9, 0.8, 0.9);
            List nearby = Module.mc.field_1687.method_8390(class_1511.class, searchBox, entity -> entity != null && entity.method_5805() && entity.method_5707(bp.method_46558()) < 0.3);
            if (nearby.isEmpty()) continue;
            class_1511 crystal = (class_1511)nearby.get(0);
            this.confirmTime = now - attempt.time();
            this.crystalTracker.confirmSpawn(bp);
            if (!this.passedTicks(this.breakTicks, ((Float)this.breakDelay.getValue()).intValue())) continue;
            this.handleSpawn(crystal);
        }
    }

    private void handleSpawn(class_1511 crystal) {
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null) {
            return;
        }
        if (!this.canAttackCrystal(crystal)) {
            return;
        }
        this.attackCrystal(crystal);
        if (this.sequential.is("Strong") && this.passedTicks(this.placeTicks, ((Float)this.placeDelay.getValue()).intValue())) {
            this.calcPosition(((Float)this.placeRange.getValue()).floatValue(), Module.mc.field_1724.method_19538());
            if (this.bestPosition != null) {
                this.placeCrystal(this.bestPosition, false, true);
            }
        }
    }

    private void calcRotations() {
        if (this.rotate.is("Off") || this.shouldPause() || Module.mc.field_1724 == null) {
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
        Rotation limited = this.limitYaw(desired);
        this.applyRotation(vec, limited);
        this.rotated = this.isRotationAligned(desired);
        this.rotating = true;
        if (this.rotationVec != null && this.rotationTicks-- < 0) {
            this.rotationVec = null;
        }
    }

    private Rotation limitYaw(Rotation desired) {
        if (!((Boolean)this.yawStep.getValue()).booleanValue()) {
            return desired;
        }
        Rotation active = RotationManager.getInstance().getRotation();
        float baseYaw = active != null ? active.getYaw() : Module.mc.field_1724.method_36454();
        float deltaYaw = class_3532.method_15393((float)(desired.getYaw() - baseYaw));
        float limitedDelta = class_3532.method_15363((float)deltaYaw, (float)(-((Float)this.yawAngle.getValue()).floatValue()), (float)((Float)this.yawAngle.getValue()).floatValue());
        return new Rotation(baseYaw + limitedDelta, desired.getPitch());
    }

    private boolean isRotationAligned(Rotation targetRotation) {
        Rotation activeRotation = RotationManager.getInstance().getRotation();
        float yaw = activeRotation != null ? activeRotation.getYaw() : Module.mc.field_1724.method_36454();
        float pitch = activeRotation != null ? activeRotation.getPitch() : Module.mc.field_1724.method_36455();
        float deltaYaw = Math.abs(class_3532.method_15393((float)(targetRotation.getYaw() - yaw)));
        float deltaPitch = Math.abs(class_3532.method_15393((float)(targetRotation.getPitch() - pitch)));
        return deltaYaw <= 8.0f && deltaPitch <= 8.0f;
    }

    private void applyRotation(class_243 vec, Rotation desired) {
        if (this.rotate.is("Off")) {
            return;
        }
        RotationStrategy strategy = this.rotate.is("Instant") ? new RotationStrategy(new InstantRotation(), MoveFixModule.enabled(), true).clientLook(false).ticksUntilReset(2) : new RotationStrategy(new SmoothRotation(), MoveFixModule.enabled(), true).clientLook(false).ticksUntilReset(3);
        RotationManager.getInstance().addRotation(new Rotation.VecRotation(desired, vec), (class_1309)this.target, strategy, TaskPriority.CRITICAL, this.module);
    }

    private void attackCrystal(class_1511 crystal) {
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null || Module.mc.field_1761 == null || crystal == null) {
            return;
        }
        if (this.crystalTracker.isDead(crystal.method_5628()) && ((Boolean)this.inhibit.getValue()).booleanValue() || this.shouldPause() || this.target == null) {
            return;
        }
        class_1293 weakness = Module.mc.field_1724.method_6112(class_1294.field_5911);
        class_1293 strength = Module.mc.field_1724.method_6112(class_1294.field_5910);
        int prevSlot = -1;
        HotbarSearch antiWeaknessHotbar = this.findAntiWeaknessHotbar();
        int antiWeaknessInv = this.findAntiWeaknessInventory();
        if (!(this.antiWeakness.is("NONE") || weakness == null || strength != null && strength.method_5578() >= weakness.method_5578())) {
            prevSlot = this.switchTo(antiWeaknessHotbar, antiWeaknessInv, this.antiWeakness);
        }
        if (!this.rotate.is("Off")) {
            this.applyRotation(crystal.method_5829().method_1005(), RotationUtil.rotationAt(crystal.method_5829().method_1005()));
        }
        this.sendPacket((class_2596<?>)class_2824.method_34206((class_1297)crystal, (boolean)Module.mc.field_1724.method_5715()));
        this.swingHand(false, true);
        this.breakTicks = 0;
        this.crystalTracker.onAttack(crystal, (Boolean)this.breakFailsafe.getValue(), ((Float)this.attempts.getValue()).intValue());
        this.rotationTicks = 10;
        for (class_1297 entity : Module.mc.field_1687.method_18112()) {
            class_1511 exCrystal;
            if (!(entity instanceof class_1511) || !((exCrystal = (class_1511)entity).method_5649(crystal.method_23317(), crystal.method_23318(), crystal.method_23321()) <= 144.0) || this.crystalTracker.isDead(exCrystal.method_5628())) continue;
            this.crystalTracker.setDead(exCrystal.method_5628(), System.currentTimeMillis());
        }
        if (prevSlot != -1) {
            if (this.antiWeakness.is("SILENT")) {
                Module.mc.field_1724.method_31548().field_7545 = prevSlot;
                this.sendPacket((class_2596<?>)new class_2868(prevSlot));
            } else if (this.antiWeakness.is("INVENTORY")) {
                Module.mc.field_1761.method_2906(Module.mc.field_1724.field_7512.field_7763, prevSlot, Module.mc.field_1724.method_31548().field_7545, class_1713.field_7791, (class_1657)Module.mc.field_1724);
                this.sendPacket((class_2596<?>)new class_2815(Module.mc.field_1724.field_7512.field_7763));
            }
        }
    }

    private boolean canAttackCrystal(class_1511 crystal) {
        double maxRangeSq;
        if (crystal == null || this.target == null || Module.mc.field_1724 == null || Module.mc.field_1687 == null) {
            return false;
        }
        double distanceSq = Module.mc.field_1724.method_33571().method_1025(crystal.method_5829().method_1005());
        double d = maxRangeSq = this.canSee(crystal.method_19538()) ? this.square(((Float)this.explodeRange.getValue()).floatValue()) : this.square(((Float)this.explodeWallRange.getValue()).floatValue());
        if (distanceSq > maxRangeSq) {
            return false;
        }
        if (!crystal.method_5805()) {
            return false;
        }
        float damage = ExplosionUtility.getAutoCrystalDamage(crystal.method_19538(), this.target, this.getPredictTicks(), false);
        float selfDamage = ExplosionUtility.getSelfExplosionDamage(crystal.method_19538(), this.getSelfPredictTicks(), false);
        boolean overrideDamage = this.shouldOverrideMaxSelfDmg(damage, selfDamage);
        if (((Boolean)this.protectFriends.getValue()).booleanValue()) {
            for (class_1657 player : Module.mc.field_1687.method_18456()) {
                float friendDamage;
                if (player == null || player == Module.mc.field_1724 || !FriendManager.getInstance().contains(player.method_5477().getString()) || !((friendDamage = ExplosionUtility.getAutoCrystalDamage(crystal.method_19538(), player, this.getPredictTicks(), false)) > selfDamage)) continue;
                selfDamage = friendDamage;
            }
        }
        return !(selfDamage > ((Float)this.maxSelfDamage.getValue()).floatValue()) || overrideDamage;
    }

    private int switchTo(HotbarSearch hotbar, int invSlot, ModeSetting switchMode) {
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null || Module.mc.field_1761 == null) {
            return -1;
        }
        int prevSlot = Module.mc.field_1724.method_31548().field_7545;
        switch ((String)switchMode.getValue()) {
            case "INVENTORY": {
                if (invSlot == -1) break;
                prevSlot = invSlot;
                Module.mc.field_1761.method_2906(Module.mc.field_1724.field_7512.field_7763, invSlot, Module.mc.field_1724.method_31548().field_7545, class_1713.field_7791, (class_1657)Module.mc.field_1724);
                this.sendPacket((class_2596<?>)new class_2815(Module.mc.field_1724.field_7512.field_7763));
                break;
            }
            case "NORMAL": {
                if (!hotbar.found()) break;
                InventoryUtil.swapToSlot(hotbar.slot(), true);
                break;
            }
            case "SILENT": {
                if (!hotbar.found()) break;
                InventoryUtil.swapToSlot(hotbar.slot(), false);
                break;
            }
        }
        return prevSlot;
    }

    private void placeCrystal(class_3965 bhr, boolean packetRotate, boolean onSpawn) {
        boolean holdingCrystal;
        if (this.shouldPause() || Module.mc.field_1724 == null || bhr == null) {
            return;
        }
        int prevSlot = -1;
        HotbarSearch crystalResult = this.findInHotbar(class_1802.field_8301);
        int crystalResultInv = this.findInInventory(class_1802.field_8301);
        boolean offhand = Module.mc.field_1724.method_6079().method_7909() instanceof class_1774;
        boolean bl = holdingCrystal = Module.mc.field_1724.method_6047().method_7909() instanceof class_1774 || offhand;
        if (!this.rotate.is("Off")) {
            this.rotationVec = new RotationVec(bhr.method_17784(), bhr, true);
            this.applyRotation(bhr.method_17784(), RotationUtil.rotationAt(bhr.method_17784()));
            if (!packetRotate && !this.rotated) {
                return;
            }
        }
        if (this.isPositionBlockedByEntity(bhr.method_17777(), false)) {
            return;
        }
        if (!this.autoSwitch.is("NONE") && !holdingCrystal) {
            prevSlot = this.switchTo(crystalResult, crystalResultInv, this.autoSwitch);
        }
        if (!(Module.mc.field_1724.method_6047().method_7909() instanceof class_1774 || offhand || this.autoSwitch.is("SILENT"))) {
            return;
        }
        class_1268 hand = offhand ? class_1268.field_5810 : class_1268.field_5808;
        boolean accepted = Module.mc.field_1761.method_2896(Module.mc.field_1724, hand, bhr).method_23665();
        if (!accepted) {
            return;
        }
        this.swingHand(offhand, false);
        if (this.passedTicks(this.breakTicks, ((Float)this.breakDelay.getValue()).intValue()) && ((Boolean)this.idPredict.getValue()).booleanValue()) {
            this.predictAttack();
        }
        this.placeTicks = 0;
        this.rotationTicks = 10;
        if (!bhr.method_17777().equals((Object)this.renderPos)) {
            this.renderMultiplier = System.currentTimeMillis();
            this.prevRenderPos = this.renderPos;
            this.renderPos = bhr.method_17777();
        }
        this.crystalTracker.addAwaitingPos(bhr.method_17777(), (Boolean)this.placeFailsafe.getValue());
        this.renderPositions.put(bhr.method_17777().method_10062(), System.currentTimeMillis());
        this.postPlaceSwitch(prevSlot);
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
            this.sendPacket((class_2596<?>)new class_2868(slot));
        }
        if (this.autoSwitch.is("INVENTORY") && slot != -1) {
            Module.mc.field_1761.method_2906(Module.mc.field_1724.field_7512.field_7763, slot, Module.mc.field_1724.method_31548().field_7545, class_1713.field_7791, (class_1657)Module.mc.field_1724);
            this.sendPacket((class_2596<?>)new class_2815(Module.mc.field_1724.field_7512.field_7763));
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
        List<PlaceData> list = this.getPossibleBlocks(this.target, center, range).stream().filter(data -> this.isSafe(data.damage(), data.selfDamage(), data.overrideDamage())).toList();
        this.bestPosition = list.isEmpty() ? null : this.filterPositions(list);
        this.calcTime = System.currentTimeMillis() - start;
    }

    private List<PlaceData> getPossibleBlocks(class_1657 target, class_243 center, float range) {
        ArrayList<PlaceData> blocks = new ArrayList<PlaceData>();
        class_2338 playerPos = class_2338.method_49638((class_2374)center);
        class_243 predictedPlayerPos = PredictUtility.predictPosition((class_1657)Module.mc.field_1724, this.getSelfPredictTicks());
        if (predictedPlayerPos == null) {
            predictedPlayerPos = Module.mc.field_1724.method_19538();
        }
        int r = (int)Math.ceil(range);
        double scanRangeSq = this.square(range + 1.0f);
        for (int x = playerPos.method_10263() - r; x <= playerPos.method_10263() + r; ++x) {
            for (int y = playerPos.method_10264() - r; y <= playerPos.method_10264() + r; ++y) {
                for (int z = playerPos.method_10260() - r; z <= playerPos.method_10260() + r; ++z) {
                    PlaceData data;
                    class_2338 bp = new class_2338(x, y, z);
                    if (bp.method_46558().method_1025(center) > scanRangeSq || (data = this.getPlaceData(bp, target, predictedPlayerPos)) == null) continue;
                    blocks.add(data);
                }
            }
        }
        return blocks;
    }

    private List<CrystalData> getPossibleCrystals(class_1657 target) {
        ArrayList<CrystalData> crystals = new ArrayList<CrystalData>();
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null) {
            return crystals;
        }
        for (class_1297 entity : Module.mc.field_1687.method_18112()) {
            double maxRangeSq;
            class_1511 crystal;
            if (!(entity instanceof class_1511) || this.crystalTracker.isBlocked((crystal = (class_1511)entity).method_5628()) || this.crystalTracker.isDead(crystal.method_5628()) && ((Boolean)this.inhibit.getValue()).booleanValue()) continue;
            double d = maxRangeSq = this.canSee(crystal.method_19538()) ? this.square(((Float)this.explodeRange.getValue()).floatValue()) : this.square(((Float)this.explodeWallRange.getValue()).floatValue());
            if (Module.mc.field_1724.method_33571().method_1025(crystal.method_5829().method_1005()) > maxRangeSq || !crystal.method_5805()) continue;
            float damage = ExplosionUtility.getAutoCrystalDamage(entity.method_19538(), target, this.getPredictTicks(), false);
            float selfDamage = ExplosionUtility.getSelfExplosionDamage(entity.method_19538(), this.getSelfPredictTicks(), false);
            boolean overrideDamage = this.shouldOverrideMaxSelfDmg(damage, selfDamage);
            if (((Boolean)this.protectFriends.getValue()).booleanValue()) {
                for (class_1657 player : Module.mc.field_1687.method_18456()) {
                    float friendDamage;
                    if (player == null || player == Module.mc.field_1724 || !FriendManager.getInstance().contains(player.method_5477().getString()) || !((friendDamage = ExplosionUtility.getAutoCrystalDamage(entity.method_19538(), player, this.getPredictTicks(), false)) > selfDamage)) continue;
                    selfDamage = friendDamage;
                }
            }
            if (damage < 1.5f || selfDamage > ((Float)this.maxSelfDamage.getValue()).floatValue() && !overrideDamage) continue;
            crystals.add(new CrystalData(crystal, damage, selfDamage, overrideDamage));
        }
        return crystals;
    }

    private void getCrystalToExplode() {
        if (this.target == null) {
            this.bestCrystal = null;
            return;
        }
        if (this.secondaryCrystal != null) {
            this.bestCrystal = this.canAttackCrystal(this.secondaryCrystal) ? this.secondaryCrystal : null;
            this.secondaryCrystal = null;
            return;
        }
        List<CrystalData> list = this.getPossibleCrystals(this.target).stream().filter(data -> this.isSafe(data.damage(), data.selfDamage(), data.overrideDamage())).toList();
        this.bestCrystal = list.isEmpty() ? null : this.filterCrystals(list);
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
        if (((Boolean)this.efficiency.getValue()).booleanValue()) {
            return damage / Math.max(0.1f, selfDamage) >= ((Float)this.efficiencyFactor.getValue()).floatValue();
        }
        return true;
    }

    private class_3965 filterPositions(List<PlaceData> clearedList) {
        PlaceData bestData = null;
        float bestVal = 0.0f;
        for (PlaceData data : clearedList) {
            float safetyComparatorDelta;
            if (!this.shouldOverrideMinDmg(data.damage()) && !(data.damage() > ((Float)this.minDamage.getValue()).floatValue()) || bestData != null && data.overrideDamage() && this.target.method_6067() + this.target.method_6032() < bestData.damage() && bestData.selfDamage() < data.selfDamage()) continue;
            boolean shouldStopOverride = bestData != null && bestData.overrideDamage() && data.damage() > this.target.method_6032() + this.target.method_6067() && data.selfDamage() < bestData.selfDamage();
            float f = safetyComparatorDelta = shouldStopOverride ? 10.0f : 1.0f;
            if (bestData != null && Math.abs(bestData.damage() - data.damage()) < safetyComparatorDelta && Math.abs(bestData.selfDamage() - data.selfDamage()) > 1.0f) {
                if (!(bestData.selfDamage() >= data.selfDamage())) continue;
                bestData = data;
                bestVal = data.damage();
                continue;
            }
            if (!(bestVal < data.damage())) continue;
            bestData = data;
            bestVal = data.damage();
        }
        if (bestData == null) {
            return null;
        }
        this.facePlacing = bestData.damage() < ((Float)this.minDamage.getValue()).floatValue();
        this.renderDamage = bestData.damage();
        this.renderSelfDamage = bestData.selfDamage();
        this.currentData = bestData;
        return bestData.bhr();
    }

    private class_1511 filterCrystals(List<CrystalData> clearedList) {
        CrystalData bestData = null;
        float bestVal = 0.0f;
        for (CrystalData data : clearedList) {
            float safetyComparatorDelta;
            if (!this.shouldOverrideMinDmg(data.damage()) && !(data.damage() > ((Float)this.minDamage.getValue()).floatValue()) || bestData != null && data.overrideDamage() && this.target.method_6067() + this.target.method_6032() < bestData.damage() && bestData.selfDamage() < data.selfDamage()) continue;
            boolean shouldStopOverride = bestData != null && bestData.overrideDamage() && data.damage() > this.target.method_6032() + this.target.method_6067() && data.selfDamage() < bestData.selfDamage();
            float f = safetyComparatorDelta = shouldStopOverride ? 10.0f : 1.0f;
            if (bestData != null && Math.abs(bestData.damage() - data.damage()) < safetyComparatorDelta && Math.abs(bestData.selfDamage() - data.selfDamage()) > 1.0f) {
                if (!(bestData.selfDamage() >= data.selfDamage())) continue;
                bestData = data;
                bestVal = data.damage();
                continue;
            }
            if (!(bestVal < data.damage())) continue;
            bestData = data;
            bestVal = data.damage();
        }
        if (bestData == null) {
            return null;
        }
        this.renderDamage = bestData.damage();
        this.renderSelfDamage = bestData.selfDamage();
        return bestData.crystal();
    }

    private PlaceData getPlaceData(class_2338 bp, class_1657 target, class_243 predictedPlayerPos) {
        float damage;
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null) {
            return null;
        }
        if (this.crystalTracker.isPositionBlocked(bp, (Boolean)this.placeFailsafe.getValue(), ((Float)this.attempts.getValue()).intValue())) {
            return null;
        }
        if (!this.predictCrystalSpawn(bp, predictedPlayerPos)) {
            return null;
        }
        if (target != null && target.method_19538().method_1025(bp.method_46558().method_1031(0.0, 0.5, 0.0)) > 144.0) {
            return null;
        }
        class_2248 base = Module.mc.field_1687.method_8320(bp).method_26204();
        if (base != class_2246.field_10540 && base != class_2246.field_9987) {
            return null;
        }
        boolean freeSpace = Module.mc.field_1687.method_22347(bp.method_10084());
        if (!freeSpace) {
            return null;
        }
        if (this.isPositionBlockedByEntity(bp, true)) {
            return null;
        }
        class_243 crystalVec = new class_243((double)bp.method_10263() + 0.5, (double)bp.method_10264() + 1.0, (double)bp.method_10260() + 0.5);
        class_3965 interactResult = this.getInteractResult(bp, crystalVec);
        if (interactResult == null) {
            return null;
        }
        float f = damage = target == null ? 10.0f : ExplosionUtility.getAutoCrystalDamage(crystalVec, target, this.getPredictTicks(), false);
        if (damage < 1.5f) {
            return null;
        }
        float selfDamage = ExplosionUtility.getSelfExplosionDamage(crystalVec, this.getSelfPredictTicks(), false);
        boolean overrideDamage = this.shouldOverrideMaxSelfDmg(damage, selfDamage);
        if (((Boolean)this.protectFriends.getValue()).booleanValue()) {
            for (class_1657 player : Module.mc.field_1687.method_18456()) {
                float friendDamage;
                if (player == null || player == Module.mc.field_1724 || !FriendManager.getInstance().contains(player.method_5477().getString()) || !((friendDamage = ExplosionUtility.getAutoCrystalDamage(crystalVec, player, this.getPredictTicks(), false)) > selfDamage)) continue;
                selfDamage = friendDamage;
            }
        }
        if (selfDamage > ((Float)this.maxSelfDamage.getValue()).floatValue() && !overrideDamage) {
            return null;
        }
        return new PlaceData(interactResult, damage, selfDamage, overrideDamage);
    }

    private boolean predictCrystalSpawn(class_2338 bp, class_243 predictedPlayerPos) {
        class_243 predictedPos = bp.method_46558().method_1031(0.0, 1.5, 0.0);
        class_243 predictedEyes = predictedPlayerPos.method_1031(0.0, (double)Module.mc.field_1724.method_18381(Module.mc.field_1724.method_18376()), 0.0);
        double distanceSq = predictedEyes.method_1025(predictedPos);
        if (this.canSee(predictedPos)) {
            return distanceSq <= this.square(((Float)this.explodeRange.getValue()).floatValue());
        }
        return distanceSq <= this.square(((Float)this.explodeWallRange.getValue()).floatValue());
    }

    private class_3965 getInteractResult(class_2338 bp, class_243 crystalVec) {
        return this.interact.is("Strict") ? this.getStrictInteract(bp) : this.getDefaultInteract(crystalVec, bp);
    }

    private boolean isPositionBlockedByEntity(class_2338 base, boolean calcPhase) {
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null) {
            return false;
        }
        class_238 box = new class_238(base.method_10084()).method_1009(0.0, 1.0, 0.0);
        for (class_1297 entity : Module.mc.field_1687.method_18112()) {
            if (entity == null || !entity.method_5805() || !entity.method_5829().method_994(box) || entity instanceof class_1303) continue;
            if (entity instanceof class_1511) {
                class_1511 crystal = (class_1511)entity;
                if (this.crystalTracker.isDead(crystal.method_5628())) continue;
                if (this.crystalTracker.isBlocked(crystal.method_5628())) {
                    return true;
                }
                if (calcPhase) {
                    if (this.canAttackCrystal(crystal)) {
                        continue;
                    }
                } else if (crystal.method_19538().method_1025(box.method_1005()) > 0.3) {
                    this.secondaryCrystal = crystal;
                }
            }
            return true;
        }
        return false;
    }

    private boolean shouldOverrideMaxSelfDmg(float damage, float selfDamage) {
        boolean canKillSelf;
        if (!((Boolean)this.overrideSelfDamage.getValue()).booleanValue() || this.target == null || Module.mc.field_1724 == null || Module.mc.field_1687 == null) {
            return false;
        }
        boolean targetSafe = this.target.method_6079().method_31574(class_1802.field_8288) || this.target.method_6047().method_31574(class_1802.field_8288);
        boolean playerSafe = Module.mc.field_1724.method_6079().method_31574(class_1802.field_8288) || Module.mc.field_1724.method_6047().method_31574(class_1802.field_8288);
        float targetHp = this.target.method_6032() + this.target.method_6067();
        float playerHp = Module.mc.field_1724.method_6032() + Module.mc.field_1724.method_6067();
        boolean canPop = damage > targetHp && targetSafe;
        boolean canKill = damage > targetHp && !targetSafe;
        boolean canPopSelf = selfDamage > playerHp && playerSafe;
        boolean bl = canKillSelf = selfDamage > playerHp && !playerSafe;
        if (canPopSelf && canKill && ((Boolean)this.sacrificeTotem.getValue()).booleanValue()) {
            return true;
        }
        return selfDamage > ((Float)this.maxSelfDamage.getValue()).floatValue() && (canPop || canKill) && !canKillSelf && !canPopSelf;
    }

    private boolean shouldOverrideMinDmg(float damage) {
        if (this.target == null) {
            return false;
        }
        if ((Integer)this.facePlaceButton.getValue() != -999 && KeyStorage.isPressed((Integer)this.facePlaceButton.getValue())) {
            return true;
        }
        if (this.target.method_6032() + this.target.method_6067() - damage < 0.0f) {
            return true;
        }
        if (((Boolean)this.armorBreaker.getValue()).booleanValue()) {
            for (class_1799 armor : this.target.method_5661()) {
                float durabilityPercent;
                if (armor == null || armor.method_7960() || armor.method_7909() == class_1802.field_8162 || !armor.method_7963() || !((durabilityPercent = (float)(armor.method_7936() - armor.method_7919()) / (float)armor.method_7936() * 100.0f) < ((Float)this.armorScale.getValue()).floatValue())) continue;
                return true;
            }
        }
        return this.target.method_6032() + this.target.method_6067() <= ((Float)this.facePlaceHp.getValue()).floatValue();
    }

    private class_3965 getDefaultInteract(class_243 crystalVector, class_2338 bp) {
        class_3965 blockHit;
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null) {
            return null;
        }
        if (Module.mc.field_1724.method_33571().method_1025(crystalVector) > this.square(((Float)this.placeRange.getValue()).floatValue())) {
            return null;
        }
        class_3965 wallCheck = Module.mc.field_1687.method_17742(new class_3959(Module.mc.field_1724.method_33571(), crystalVector, class_3959.class_3960.field_17558, class_3959.class_242.field_1348, (class_1297)Module.mc.field_1724));
        if (wallCheck instanceof class_3965 && (blockHit = wallCheck).method_17783() == class_239.class_240.field_1332 && !blockHit.method_17777().equals((Object)bp) && Module.mc.field_1724.method_33571().method_1025(crystalVector) > this.square(((Float)this.placeWallRange.getValue()).floatValue())) {
            return null;
        }
        class_2350 side = Module.mc.field_1687.method_24794(bp.method_10084()) ? class_2350.field_11036 : class_2350.field_11033;
        return new class_3965(crystalVector, side, bp, false);
    }

    private class_3965 getStrictInteract(class_2338 bp) {
        float upPoint;
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null) {
            return null;
        }
        float bestDistance = Float.MAX_VALUE;
        class_2350 bestDirection = null;
        class_243 bestVec = null;
        float f = upPoint = (Boolean)this.strictCenter.getValue() != false ? (float)bp.method_46558().method_10214() : (float)bp.method_10084().method_10264();
        if (Module.mc.field_1724.method_33571().method_10214() > (double)upPoint) {
            bestDirection = class_2350.field_11036;
        } else if (Module.mc.field_1724.method_33571().method_10214() < (double)bp.method_10264() && Module.mc.field_1687.method_22347(bp.method_10074())) {
            bestDirection = class_2350.field_11033;
        }
        ArrayList<class_2350> directions = new ArrayList<class_2350>(List.of(class_2350.field_11043, class_2350.field_11035, class_2350.field_11034, class_2350.field_11039));
        if (bestDirection != null) {
            directions.add(bestDirection);
        }
        for (class_2350 dir : directions) {
            double maxDistanceSq;
            class_243 vec = bp.method_46558().method_1019(class_243.method_24954((class_2382)dir.method_62675()).method_1021(0.5));
            if (!Module.mc.field_1687.method_8320(bp.method_10093(dir)).method_45474()) continue;
            double distanceSq = Module.mc.field_1724.method_33571().method_1025(vec);
            if (distanceSq > (maxDistanceSq = this.canSee(vec) ? this.square(((Float)this.placeRange.getValue()).floatValue()) : this.square(((Float)this.placeWallRange.getValue()).floatValue())) || !(distanceSq < (double)bestDistance)) continue;
            bestDistance = (float)distanceSq;
            bestDirection = dir;
            bestVec = vec;
        }
        if (bestDirection == null || bestVec == null) {
            return null;
        }
        return new class_3965(bestVec, bestDirection, bp, false);
    }

    private void swingHand(boolean offHand, boolean attack) {
        class_1268 hand = offHand ? class_1268.field_5810 : class_1268.field_5808;
        switch ((String)this.swingMode.getValue()) {
            case "Both": {
                Module.mc.field_1724.method_6104(hand);
                break;
            }
            case "Break": {
                if (attack) {
                    Module.mc.field_1724.method_6104(class_1268.field_5808);
                    break;
                }
                this.sendPacket((class_2596<?>)new class_2879(class_1268.field_5808));
                break;
            }
            case "Place": {
                if (!attack) {
                    Module.mc.field_1724.method_6104(hand);
                    break;
                }
                this.sendPacket((class_2596<?>)new class_2879(hand));
                break;
            }
            case "ServerSide": {
                this.sendPacket((class_2596<?>)new class_2879(hand));
                break;
            }
            default: {
                Module.mc.field_1724.method_6104(hand);
            }
        }
    }

    private void predictAttack() {
        int attacks = ((Float)this.idAttacks.getValue()).intValue();
        for (int i = 1; i <= attacks; ++i) {
            int id = (int)(this.currentId + (long)i);
            class_1297 entity = Module.mc.field_1687.method_8469(id);
            if (entity != null && !(entity instanceof class_1511)) continue;
            class_2824 attackPacket = class_2824.method_34206((class_1297)Module.mc.field_1724, (boolean)Module.mc.field_1724.method_5715());
            CrystalAuraProcessor.changeId(attackPacket, id);
            this.sendPacket((class_2596<?>)attackPacket);
            this.sendPacket((class_2596<?>)new class_2879(class_1268.field_5808));
        }
    }

    private static void changeId(class_2824 packet, int id) {
        try {
            Field field;
            try {
                field = class_2824.class.getDeclaredField("entityId");
            }
            catch (NoSuchFieldException ex) {
                field = class_2824.class.getDeclaredField("field_12870");
            }
            field.setAccessible(true);
            field.setInt(packet, id);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private void processSpawnPacket(int id) {
        if ((long)id > this.currentId) {
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
        for (Map.Entry<class_2338, CrystalTracker.Attempt> entry : new ArrayList<Map.Entry<class_2338, CrystalTracker.Attempt>>(awaiting.entrySet())) {
            class_1297 entity;
            class_2338 bp = entry.getKey();
            if (spawnPos.method_1025(bp.method_46558()) > 0.36) continue;
            CrystalTracker.Attempt attempt = entry.getValue();
            this.confirmTime = now - attempt.time();
            this.crystalTracker.confirmSpawn(bp);
            if (!this.passedTicks(this.breakTicks, ((Float)this.breakDelay.getValue()).intValue())) break;
            class_1297 class_12972 = entity = Module.mc.field_1687 == null ? null : Module.mc.field_1687.method_8469(entityId);
            if (entity instanceof class_1511) {
                class_1511 crystal = (class_1511)entity;
                this.handleSpawn(crystal);
                break;
            }
            this.attackSpawnById(entityId);
            break;
        }
    }

    private void attackSpawnById(int entityId) {
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null || this.target == null || this.shouldPause()) {
            return;
        }
        class_2824 attackPacket = class_2824.method_34206((class_1297)Module.mc.field_1724, (boolean)Module.mc.field_1724.method_5715());
        CrystalAuraProcessor.changeId(attackPacket, entityId);
        this.sendPacket((class_2596<?>)attackPacket);
        this.swingHand(false, true);
        this.crystalTracker.setDead(entityId, System.currentTimeMillis());
        this.breakTicks = 0;
        this.rotationTicks = 10;
    }

    private class_243 resolveEntitySpawnPos(class_2604 packet) {
        Double x = this.tryReadCoordinate(packet, "getX", "x");
        Double y = this.tryReadCoordinate(packet, "getY", "y");
        Double z = this.tryReadCoordinate(packet, "getZ", "z");
        if (x != null && y != null && z != null) {
            return new class_243(x.doubleValue(), y.doubleValue(), z.doubleValue());
        }
        try {
            Method method = packet.getClass().getMethod("getPos", new Class[0]);
            Object value = method.invoke((Object)packet, new Object[0]);
            if (value instanceof class_243) {
                class_243 vec = (class_243)value;
                return vec;
            }
        }
        catch (ReflectiveOperationException reflectiveOperationException) {
            // empty catch block
        }
        return null;
    }

    private class_243 resolveExplosionPos(class_2664 packet) {
        class_243 center = this.tryReadCenter(packet);
        if (center != null) {
            return center;
        }
        Double x = this.tryReadCoordinate(packet, "getX", "x");
        Double y = this.tryReadCoordinate(packet, "getY", "y");
        Double z = this.tryReadCoordinate(packet, "getZ", "z");
        if (x != null && y != null && z != null) {
            return new class_243(x.doubleValue(), y.doubleValue(), z.doubleValue());
        }
        if (Module.mc.field_1724 != null) {
            return Module.mc.field_1724.method_19538();
        }
        return class_243.field_1353;
    }

    private class_243 tryReadCenter(class_2664 packet) {
        Object value;
        Method method2;
        try {
            method2 = packet.getClass().getMethod("center", new Class[0]);
            value = method2.invoke((Object)packet, new Object[0]);
            if (value instanceof class_243) {
                class_243 vec = (class_243)value;
                return vec;
            }
        }
        catch (ReflectiveOperationException method2) {
            // empty catch block
        }
        try {
            method2 = packet.getClass().getMethod("getCenter", new Class[0]);
            value = method2.invoke((Object)packet, new Object[0]);
            if (value instanceof class_243) {
                class_243 vec = (class_243)value;
                return vec;
            }
        }
        catch (ReflectiveOperationException reflectiveOperationException) {
            // empty catch block
        }
        return null;
    }

    private Double tryReadCoordinate(Object packet, String methodName, String fieldName) {
        Object value;
        try {
            Method method = packet.getClass().getMethod(methodName, new Class[0]);
            value = method.invoke(packet, new Object[0]);
            if (value instanceof Number) {
                Number number = (Number)value;
                return number.doubleValue();
            }
        }
        catch (ReflectiveOperationException method) {
            // empty catch block
        }
        try {
            Field field = packet.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            value = field.get(packet);
            if (value instanceof Number) {
                Number number = (Number)value;
                return number.doubleValue();
            }
        }
        catch (ReflectiveOperationException reflectiveOperationException) {
            // empty catch block
        }
        return null;
    }

    private int getPredictTicks() {
        return 0;
    }

    public class_1657 getTarget() {
        return this.target;
    }

    private int getSelfPredictTicks() {
        return (int)Math.ceil(this.getPing() * 1.5f / 50.0f);
    }

    private float getPing() {
        if (Module.mc.method_1562() == null || Module.mc.field_1724 == null) {
            return 0.0f;
        }
        class_640 entry = Module.mc.method_1562().method_2871(Module.mc.field_1724.method_5667());
        return entry == null ? 0.0f : (float)entry.method_2959();
    }

    private boolean shouldPause() {
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null || Module.mc.field_1761 == null) {
            return true;
        }
        boolean offhand = Module.mc.field_1724.method_6079().method_7909() instanceof class_1774;
        boolean mainHand = Module.mc.field_1724.method_6047().method_7909() instanceof class_1774;
        if (Module.mc.field_1761.method_2923() && !offhand && ((Boolean)this.mining.getValue()).booleanValue()) {
            this.currentState = State.Mining;
            return true;
        }
        if (this.autoSwitch.is("NONE") && !offhand && !mainHand) {
            this.currentState = State.NoCrystalls;
            return true;
        }
        if ((this.autoSwitch.is("SILENT") || this.autoSwitch.is("NORMAL")) && !this.findInHotbar(class_1802.field_8301).found() && !offhand) {
            this.currentState = State.NoCrystalls;
            return true;
        }
        if (this.autoSwitch.is("INVENTORY") && this.findAny(class_1802.field_8301) == -1 && !offhand) {
            this.currentState = State.NoCrystalls;
            return true;
        }
        if (Module.mc.field_1724.method_6115() && ((Boolean)this.eating.getValue()).booleanValue()) {
            this.currentState = State.Eating;
            return true;
        }
        if (this.rotationMarkedDirty()) {
            this.currentState = State.ExternalPause;
            return true;
        }
        if (Module.mc.field_1724.method_6032() + Module.mc.field_1724.method_6067() < ((Float)this.pauseHP.getValue()).floatValue()) {
            this.currentState = State.LowHP;
            return true;
        }
        this.currentState = State.Active;
        return false;
    }

    private boolean rotationMarkedDirty() {
        ClickPearlModule clickPearl;
        SurroundModule surroundModule;
        if (((Boolean)this.surround.getValue()).booleanValue() && (surroundModule = SurroundModule.getInstance()) != null && surroundModule.isEnabled()) {
            return true;
        }
        if (((Boolean)this.middleClick.getValue()).booleanValue() && Module.mc.field_1690.field_1871.method_1434() && (clickPearl = ClickPearlModule.getInstance()) != null && clickPearl.isEnabled() && clickPearl.isThrowingPearl()) {
            return true;
        }
        return (Boolean)this.inventoryPause.getValue() != false && Module.mc.field_1755 != null;
    }

    private class_1657 findTarget(float range, String logic) {
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null) {
            return null;
        }
        ArrayList<class_1657> candidates = new ArrayList<class_1657>();
        for (class_1657 player : Module.mc.field_1687.method_18456()) {
            if (player == null || player == Module.mc.field_1724 || !player.method_5805() || player.method_7325() || FriendManager.getInstance().contains(player.method_5477().getString()) || Module.mc.field_1724.method_5858((class_1297)player) > this.square(range)) continue;
            candidates.add(player);
        }
        if (candidates.isEmpty()) {
            return null;
        }
        if ("Health".equalsIgnoreCase(logic)) {
            return candidates.stream().min(Comparator.comparingDouble(p -> p.method_6032() + p.method_6067())).orElse(null);
        }
        return candidates.stream().min(Comparator.comparingDouble(arg_0 -> ((class_746)Module.mc.field_1724).method_5858(arg_0))).orElse(null);
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
        for (int i = 0; i < 9; ++i) {
            class_1792 item = ((class_1799)Module.mc.field_1724.method_31548().field_7547.get(i)).method_7909();
            if (!(item instanceof class_1829) && !(item instanceof class_1810) && !(item instanceof class_1743) && !(item instanceof class_1821)) continue;
            return new HotbarSearch(true, i);
        }
        return new HotbarSearch(false, -1);
    }

    private int findAntiWeaknessInventory() {
        if (Module.mc.field_1724 == null) {
            return -1;
        }
        for (int i = 9; i < 36; ++i) {
            class_1792 item = ((class_1799)Module.mc.field_1724.method_31548().field_7547.get(i)).method_7909();
            if (!(item instanceof class_1829) && !(item instanceof class_1810) && !(item instanceof class_1743) && !(item instanceof class_1821)) continue;
            return i;
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
        long holdMs = ((Float)this.renderHoldMs.getValue()).longValue();
        this.renderPositions.entrySet().removeIf(entry -> now - (Long)entry.getValue() > holdMs);
    }

    private double square(double value) {
        return value * value;
    }

    private boolean render(String key) {
        return this.renderSettings.isEnabled(key);
    }

    private Color themed(float dim, int alpha) {
        Color c = UIColors.primary(class_3532.method_15340((int)alpha, (int)0, (int)255));
        int r = class_3532.method_15340((int)((int)((float)c.getRed() * dim)), (int)0, (int)255);
        int g = class_3532.method_15340((int)((int)((float)c.getGreen() * dim)), (int)0, (int)255);
        int b = class_3532.method_15340((int)((int)((float)c.getBlue() * dim)), (int)0, (int)255);
        return new Color(r, g, b, c.getAlpha());
    }

    private float animate(AnimationUtil animation, boolean state) {
        if (!this.render("Animate")) {
            animation.setValue(state ? 1.0 : 0.0);
            return state ? 1.0f : 0.0f;
        }
        animation.run(state ? 1.0 : 0.0, 260L, Easing.SINE_OUT, true);
        animation.update();
        return (float)animation.getValue();
    }

    private String round2(float value) {
        return String.format(Locale.US, "%.2f", Float.valueOf(value));
    }

    private boolean canSee(class_243 pos) {
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null) {
            return false;
        }
        class_3965 result = Module.mc.field_1687.method_17742(new class_3959(Module.mc.field_1724.method_33571(), pos, class_3959.class_3960.field_17558, class_3959.class_242.field_1348, (class_1297)Module.mc.field_1724));
        return result.method_17783() == class_239.class_240.field_1333;
    }

    private static final class CrystalTracker {
        private final Map<Integer, Long> deadCrystals = new ConcurrentHashMap<Integer, Long>();
        private final Map<Integer, Integer> attackAttempts = new ConcurrentHashMap<Integer, Integer>();
        private final Map<Integer, Long> blockedCrystals = new ConcurrentHashMap<Integer, Long>();
        private final Map<class_2338, Attempt> awaitingPositions = new ConcurrentHashMap<class_2338, Attempt>();

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
            this.deadCrystals.entrySet().removeIf(entry -> now - (Long)entry.getValue() > 1800L);
            this.blockedCrystals.entrySet().removeIf(entry -> now - (Long)entry.getValue() > 1800L);
            this.awaitingPositions.entrySet().removeIf(entry -> now - ((Attempt)entry.getValue()).time() > 2500L);
        }

        void onAttack(class_1511 crystal, boolean breakFailsafeEnabled, int maxAttempts) {
            if (crystal == null) {
                return;
            }
            int id = crystal.method_5628();
            this.setDead(id, System.currentTimeMillis());
            if (breakFailsafeEnabled) {
                int attempts = this.attackAttempts.getOrDefault(id, 0) + 1;
                this.attackAttempts.put(id, attempts);
                if (attempts >= Math.max(1, maxAttempts)) {
                    this.blockedCrystals.put(id, System.currentTimeMillis());
                }
            }
        }

        boolean isDead(int id) {
            return this.deadCrystals.containsKey(id);
        }

        void setDead(int id, long time) {
            this.deadCrystals.put(id, time);
        }

        boolean isBlocked(int id) {
            return this.blockedCrystals.containsKey(id);
        }

        void addAwaitingPos(class_2338 pos, boolean placeFailsafeEnabled) {
            Attempt previous = this.awaitingPositions.get(pos);
            int nextAttempt = previous == null ? 1 : previous.attempts() + 1;
            this.awaitingPositions.put(pos.method_10062(), new Attempt(System.currentTimeMillis(), placeFailsafeEnabled ? nextAttempt : 1));
        }

        boolean isPositionBlocked(class_2338 pos, boolean placeFailsafeEnabled, int maxAttempts) {
            if (!placeFailsafeEnabled) {
                return false;
            }
            Attempt attempt = this.awaitingPositions.get(pos);
            return attempt != null && attempt.attempts() >= Math.max(1, maxAttempts);
        }

        void confirmSpawn(class_2338 pos) {
            this.awaitingPositions.remove(pos);
        }

        Map<class_2338, Attempt> getAwaitingPositions() {
            return this.awaitingPositions;
        }

        record Attempt(long time, int attempts) {
        }
    }

    private static enum State {
        Active,
        Eating,
        LowHP,
        NoTarget,
        NoCrystalls,
        ExternalPause,
        Mining;

    }

    public record PlaceData(class_3965 bhr, float damage, float selfDamage, boolean overrideDamage) {
    }

    private record RotationVec(class_243 vec, class_3965 hitVec, boolean place) {
    }

    private record HotbarSearch(boolean found, int slot) {
    }

    private record CrystalData(class_1511 crystal, float damage, float selfDamage, boolean overrideDamage) {
    }
}

