// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.combat.crystalaura;

import java.util.Locale;
import sweetie.leonware.api.utils.animation.Easing;
import sweetie.leonware.api.utils.color.UIColors;
import net.minecraft.class_1821;
import net.minecraft.class_1743;
import net.minecraft.class_1810;
import net.minecraft.class_1829;
import net.minecraft.class_1792;
import net.minecraft.class_746;
import java.util.stream.Stream;
import java.util.function.ToDoubleFunction;
import java.util.Objects;
import java.util.Comparator;
import sweetie.leonware.client.features.modules.player.ClickPearlModule;
import sweetie.leonware.client.features.modules.combat.SurroundModule;
import net.minecraft.class_640;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import net.minecraft.class_2879;
import net.minecraft.class_2350;
import net.minecraft.class_239;
import net.minecraft.class_3959;
import net.minecraft.class_1799;
import sweetie.leonware.api.system.backend.KeyStorage;
import net.minecraft.class_1303;
import net.minecraft.class_2248;
import net.minecraft.class_2246;
import sweetie.leonware.api.utils.math.PredictUtility;
import net.minecraft.class_2374;
import net.minecraft.class_1268;
import net.minecraft.class_1774;
import net.minecraft.class_1802;
import sweetie.leonware.api.utils.player.InventoryUtil;
import sweetie.leonware.api.system.configs.FriendManager;
import sweetie.leonware.api.utils.world.ExplosionUtility;
import net.minecraft.class_1293;
import net.minecraft.class_2815;
import net.minecraft.class_1713;
import net.minecraft.class_2868;
import net.minecraft.class_2824;
import net.minecraft.class_1294;
import net.minecraft.class_1309;
import sweetie.leonware.api.utils.task.TaskPriority;
import sweetie.leonware.api.utils.rotation.rotations.SmoothRotation;
import sweetie.leonware.api.utils.rotation.manager.RotationMode;
import sweetie.leonware.api.utils.rotation.manager.RotationStrategy;
import sweetie.leonware.client.features.modules.movement.MoveFixModule;
import sweetie.leonware.api.utils.rotation.rotations.InstantRotation;
import sweetie.leonware.api.utils.rotation.manager.RotationManager;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import net.minecraft.class_238;
import org.joml.Vector2f;
import java.util.List;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import sweetie.leonware.api.utils.math.ProjectionUtil;
import sweetie.leonware.api.event.events.render.Render2DEvent;
import java.awt.Color;
import sweetie.leonware.api.utils.render.display.BoxRender;
import sweetie.leonware.api.utils.render.RenderUtil;
import net.minecraft.class_3532;
import java.util.Collection;
import java.util.ArrayList;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import java.util.Iterator;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_1297;
import net.minecraft.class_2664;
import net.minecraft.class_2604;
import net.minecraft.class_2606;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.module.setting.Setting;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.class_1511;
import net.minecraft.class_3965;
import net.minecraft.class_1657;
import net.minecraft.class_2338;
import java.util.Map;
import sweetie.leonware.api.utils.animation.AnimationUtil;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.setting.BindSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;

public class CrystalAuraProcessor
{
    private static final float RENDER_BOX_SIZE = 2.0f;
    private final CrystalAuraModule module;
    public final ModeSetting page;
    public final BooleanSetting await;
    public final ModeSetting timing;
    public final ModeSetting sequential;
    public final ModeSetting rotate;
    public final BooleanSetting yawStep;
    public final SliderSetting yawAngle;
    public final ModeSetting targetLogic;
    public final SliderSetting targetRange;
    public final ModeSetting interact;
    public final BooleanSetting strictCenter;
    public final BooleanSetting rayTraceBypass;
    public final SliderSetting placeDelay;
    public final SliderSetting placeRange;
    public final SliderSetting placeWallRange;
    public final BooleanSetting inhibit;
    public final SliderSetting breakDelay;
    public final SliderSetting explodeRange;
    public final SliderSetting explodeWallRange;
    public final BooleanSetting mining;
    public final BooleanSetting eating;
    public final BooleanSetting surround;
    public final BooleanSetting middleClick;
    public final BooleanSetting inventoryPause;
    public final SliderSetting pauseHP;
    public final SliderSetting minDamage;
    public final SliderSetting maxSelfDamage;
    public final BooleanSetting efficiency;
    public final SliderSetting efficiencyFactor;
    public final BooleanSetting protectFriends;
    public final BooleanSetting overrideSelfDamage;
    public final BooleanSetting sacrificeTotem;
    public final BooleanSetting armorBreaker;
    public final SliderSetting armorScale;
    public final SliderSetting facePlaceHp;
    public final BindSetting facePlaceButton;
    public final BooleanSetting ignoreTerrain;
    public final ModeSetting autoSwitch;
    public final ModeSetting antiWeakness;
    public final BooleanSetting placeFailsafe;
    public final BooleanSetting breakFailsafe;
    public final SliderSetting attempts;
    public final BooleanSetting idPredict;
    public final SliderSetting idAttacks;
    public final ModeSetting swingMode;
    public final MultiBooleanSetting renderSettings;
    public final SliderSetting renderHoldMs;
    public final BooleanSetting confirmInfo;
    public final BooleanSetting calcInfo;
    private final AnimationUtil blockRenderAnim;
    private final CrystalTracker crystalTracker;
    private final Map<class_2338, Long> renderPositions;
    private class_1657 target;
    private PlaceData currentData;
    private class_3965 bestPosition;
    private class_1511 bestCrystal;
    private class_1511 secondaryCrystal;
    private RotationVec rotationVec;
    private State currentState;
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
    private int lastTargetId;
    private long externalPauseUntilMs;
    private int placeTicks;
    private int breakTicks;
    private int calcTicks;
    private int placeSyncTicks;
    private class_2338 renderPos;
    private class_2338 prevRenderPos;
    
    public CrystalAuraProcessor(final CrystalAuraModule module) {
        this.page = new ModeSetting("Page").values("Main", "Place", "Break", "Damages", "Pause", "Render", "Switch", "FailSafe", "Info", "IDPredict").value("Main");
        this.await = new BooleanSetting("Await").value(true).setVisible(() -> this.page.is("Main"));
        this.timing = new ModeSetting("Timing").values("NORMAL", "SEQUENTIAL").value("NORMAL").setVisible(() -> this.page.is("Main"));
        this.sequential = new ModeSetting("Sequential").values("Off", "Strict", "Strong").value("Strong").setVisible(() -> this.page.is("Main"));
        this.rotate = new ModeSetting("Rotate").values("Off", "Instant", "Smooth").value("Smooth").setVisible(() -> this.page.is("Main"));
        this.yawStep = new BooleanSetting("YawStep").value(false).setVisible(() -> !this.rotate.is("Off") && this.page.is("Main"));
        this.yawAngle = new SliderSetting("YawAngle").value(180.0f).range(1.0f, 180.0f).step(1.0f).setVisible(() -> !this.rotate.is("Off") && this.yawStep.getValue() && this.page.is("Main"));
        this.targetLogic = new ModeSetting("TargetLogic").values("Distance", "Health").value("Distance").setVisible(() -> this.page.is("Main"));
        this.targetRange = new SliderSetting("TargetRange").value(10.0f).range(1.0f, 15.0f).step(0.1f).setVisible(() -> this.page.is("Main"));
        this.interact = new ModeSetting("Interact").values("Default", "Strict").value("Default").setVisible(() -> this.page.is("Place"));
        this.strictCenter = new BooleanSetting("CCStrict").value(true).setVisible(() -> this.page.is("Place") && this.interact.is("Strict"));
        this.rayTraceBypass = new BooleanSetting("RayTraceBypass").value(false).setVisible(() -> this.page.is("Place"));
        this.placeDelay = new SliderSetting("PlaceDelay").value(0.0f).range(0.0f, 20.0f).step(1.0f).setVisible(() -> this.page.is("Place"));
        this.placeRange = new SliderSetting("PlaceRange").value(5.0f).range(1.0f, 6.0f).step(0.1f).setVisible(() -> this.page.is("Place"));
        this.placeWallRange = new SliderSetting("PlaceWallRange").value(3.5f).range(0.0f, 6.0f).step(0.1f).setVisible(() -> this.page.is("Place"));
        this.inhibit = new BooleanSetting("Inhibit").value(true).setVisible(() -> this.page.is("Break"));
        this.breakDelay = new SliderSetting("BreakDelay").value(0.0f).range(0.0f, 20.0f).step(1.0f).setVisible(() -> this.page.is("Break"));
        this.explodeRange = new SliderSetting("BreakRange").value(5.0f).range(1.0f, 6.0f).step(0.1f).setVisible(() -> this.page.is("Break"));
        this.explodeWallRange = new SliderSetting("BreakWallRange").value(3.5f).range(0.0f, 6.0f).step(0.1f).setVisible(() -> this.page.is("Break"));
        this.mining = new BooleanSetting("Mining").value(true).setVisible(() -> this.page.is("Pause"));
        this.eating = new BooleanSetting("Eating").value(true).setVisible(() -> this.page.is("Pause"));
        this.surround = new BooleanSetting("Surround").value(true).setVisible(() -> this.page.is("Pause"));
        this.middleClick = new BooleanSetting("MiddleClick").value(true).setVisible(() -> this.page.is("Pause"));
        this.inventoryPause = new BooleanSetting("Inventory").value(false).setVisible(() -> this.page.is("Pause"));
        this.pauseHP = new SliderSetting("HP").value(8.0f).range(2.0f, 10.0f).step(0.1f).setVisible(() -> this.page.is("Pause"));
        this.minDamage = new SliderSetting("MinDamage").value(6.0f).range(2.0f, 20.0f).step(0.1f).setVisible(() -> this.page.is("Damages"));
        this.maxSelfDamage = new SliderSetting("MaxSelfDamage").value(10.0f).range(2.0f, 20.0f).step(0.1f).setVisible(() -> this.page.is("Damages"));
        this.efficiency = new BooleanSetting("Efficiency").value(false).setVisible(() -> this.page.is("Damages"));
        this.efficiencyFactor = new SliderSetting("EfficiencyFactor").value(1.0f).range(0.1f, 5.0f).step(0.1f).setVisible(() -> this.page.is("Damages") && this.efficiency.getValue());
        this.protectFriends = new BooleanSetting("ProtectFriends").value(true).setVisible(() -> this.page.is("Damages"));
        this.overrideSelfDamage = new BooleanSetting("OverrideSelfDamage").value(true).setVisible(() -> this.page.is("Damages"));
        this.sacrificeTotem = new BooleanSetting("SacrificeTotem").value(true).setVisible(() -> this.page.is("Damages") && this.overrideSelfDamage.getValue());
        this.armorBreaker = new BooleanSetting("ArmorBreaker").value(true).setVisible(() -> this.page.is("Damages"));
        this.armorScale = new SliderSetting("Armor %").value(5.0f).range(0.0f, 40.0f).step(0.1f).setVisible(() -> this.page.is("Damages") && this.armorBreaker.getValue());
        this.facePlaceHp = new SliderSetting("FacePlaceHp").value(5.0f).range(0.0f, 20.0f).step(0.1f).setVisible(() -> this.page.is("Damages"));
        this.facePlaceButton = new BindSetting("FacePlaceBtn").value(-999).setVisible(() -> this.page.is("Damages"));
        this.ignoreTerrain = new BooleanSetting("IgnoreTerrain").value(true).setVisible(() -> this.page.is("Damages"));
        this.autoSwitch = new ModeSetting("Switch").values("NONE", "NORMAL", "SILENT", "INVENTORY").value("NORMAL").setVisible(() -> this.page.is("Switch"));
        this.antiWeakness = new ModeSetting("AntiWeakness").values("NONE", "NORMAL", "SILENT", "INVENTORY").value("SILENT").setVisible(() -> this.page.is("Switch"));
        this.placeFailsafe = new BooleanSetting("PlaceFailsafe").value(true).setVisible(() -> this.page.is("FailSafe"));
        this.breakFailsafe = new BooleanSetting("BreakFailsafe").value(true).setVisible(() -> this.page.is("FailSafe"));
        this.attempts = new SliderSetting("MaxAttempts").value(5.0f).range(1.0f, 30.0f).step(1.0f).setVisible(() -> this.page.is("FailSafe"));
        this.idPredict = new BooleanSetting("IDPredict").value(false).setVisible(() -> this.page.is("IDPredict"));
        this.idAttacks = new SliderSetting("IDAttacks").value(3.0f).range(1.0f, 10.0f).step(1.0f).setVisible(() -> this.page.is("IDPredict"));
        this.swingMode = new ModeSetting("Swing").values("Both", "Place", "Break", "ServerSide").value("Place").setVisible(() -> this.page.is("Render"));
        this.renderSettings = new MultiBooleanSetting("Render").value(new BooleanSetting("Block").value(true), new BooleanSetting("Rect").value(true), new BooleanSetting("Animate").value(true)).setVisible(() -> this.page.is("Render"));
        this.renderHoldMs = new SliderSetting("RenderHoldMs").value(500.0f).range(100.0f, 1000.0f).step(10.0f).setVisible(() -> this.page.is("Render"));
        this.confirmInfo = new BooleanSetting("ConfirmTime").value(true).setVisible(() -> this.page.is("Info"));
        this.calcInfo = new BooleanSetting("CalcInfo").value(false).setVisible(() -> this.page.is("Info"));
        this.blockRenderAnim = new AnimationUtil();
        this.crystalTracker = new CrystalTracker();
        this.renderPositions = new ConcurrentHashMap<class_2338, Long>();
        this.currentState = State.NoTarget;
        this.lastTargetId = -1;
        this.externalPauseUntilMs = 0L;
        this.module = module;
    }
    
    public void initSettings(final CrystalAuraModule module) {
        module.addSettings(this.page, this.await, this.timing, this.sequential, this.rotate, this.yawStep, this.yawAngle, this.targetLogic, this.targetRange, this.interact, this.strictCenter, this.rayTraceBypass, this.placeDelay, this.placeRange, this.placeWallRange, this.inhibit, this.breakDelay, this.explodeRange, this.explodeWallRange, this.mining, this.eating, this.surround, this.middleClick, this.inventoryPause, this.pauseHP, this.minDamage, this.maxSelfDamage, this.efficiency, this.efficiencyFactor, this.protectFriends, this.overrideSelfDamage, this.sacrificeTotem, this.armorBreaker, this.armorScale, this.facePlaceHp, this.facePlaceButton, this.ignoreTerrain, this.autoSwitch, this.antiWeakness, this.placeFailsafe, this.breakFailsafe, this.attempts, this.idPredict, this.idAttacks, this.swingMode, this.renderSettings, this.renderHoldMs, this.confirmInfo, this.calcInfo);
    }
    
    public boolean check() {
        return this.target != null || this.bestPosition != null || this.bestCrystal != null || !this.renderPositions.isEmpty();
    }
    
    public void onPacket(final PacketEvent.PacketEventData event) {
        if (!event.isReceive() || Module.mc.field_1724 == null || Module.mc.field_1687 == null) {
            return;
        }
        final class_2596<?> packet = event.packet();
        if (packet instanceof final class_2606 spawn) {
            this.processSpawnPacket(spawn.method_11183());
        }
        else {
            final class_2596<?> packet2 = event.packet();
            if (packet2 instanceof final class_2604 spawn2) {
                this.processSpawnPacket(spawn2.method_11167());
                this.confirmAwaitingBySpawn(spawn2.method_11167(), this.resolveEntitySpawnPos(spawn2));
            }
            else {
                final class_2596<?> packet3 = event.packet();
                if (packet3 instanceof final class_2664 explosion) {
                    final class_243 explosionPos = this.resolveExplosionPos(explosion);
                    for (final class_1297 ent : Module.mc.field_1687.method_18112()) {
                        if (ent instanceof final class_1511 crystal) {
                            if (crystal.method_5707(explosionPos) > 144.0 || this.crystalTracker.isDead(crystal.method_5628())) {
                                continue;
                            }
                            this.crystalTracker.setDead(crystal.method_5628(), System.currentTimeMillis());
                        }
                    }
                }
            }
        }
    }
    
    public void onUpdate(final UpdateEvent event) {
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
        this.target = this.findTarget(this.targetRange.getValue(), this.targetLogic.getValue());
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
        final boolean targetChanged = this.lastTargetId != this.target.method_5628();
        if (targetChanged) {
            this.lastTargetId = this.target.method_5628();
            this.calcTicks = Integer.MAX_VALUE;
        }
        final int pingGate = Math.max(1, Math.round(this.getPing() / 25.0f));
        if (targetChanged || this.bestPosition == null || !this.await.getValue() || this.passedTicks(this.placeTicks, 20) || this.passedTicks(this.calcTicks, pingGate)) {
            this.calcPosition(this.placeRange.getValue(), Module.mc.field_1724.method_19538());
        }
        this.getCrystalToExplode();
        if (this.timing.is("NORMAL") || this.timing.is("SEQUENTIAL")) {
            this.doAction();
        }
    }
    
    public void onRender3D(final Render3DEvent.Render3DEventData event) {
        this.cleanupRenderPositions();
        final boolean canRenderBlock = this.render("Block") && !this.renderPositions.isEmpty();
        final float blockAnim = this.animate(this.blockRenderAnim, canRenderBlock);
        this.blockRenderFactor = blockAnim;
        if (canRenderBlock && blockAnim > 0.01f) {
            final long now = System.currentTimeMillis();
            final long holdMs = this.renderHoldMs.getValue().longValue();
            for (final Map.Entry<class_2338, Long> entry : new ArrayList(this.renderPositions.entrySet())) {
                final class_2338 pos = entry.getKey();
                final long elapsed = now - entry.getValue();
                if (elapsed > holdMs) {
                    continue;
                }
                final float fade = 1.0f - class_3532.method_15363(elapsed / (float)holdMs, 0.0f, 1.0f);
                final Color fill = this.themed(0.68f, (int)(56.0f * blockAnim * fade));
                final Color rect = this.themed(0.82f, (int)(160.0f * blockAnim * fade));
                final float x = (float)pos.method_10263();
                final float y = (float)pos.method_10264();
                final float z = (float)pos.method_10260();
                RenderUtil.BOX.drawBox(x, y, z, x + 2.0f, y + 2.0f, z + 2.0f, 1.15f, fill, BoxRender.Render.FILL, 0.0f);
                if (!this.render("Rect")) {
                    continue;
                }
                RenderUtil.BOX.drawBox(x, y, z, x + 2.0f, y + 2.0f, z + 2.0f, 1.95f, rect, BoxRender.Render.OUTLINE, 0.0f);
            }
        }
    }
    
    public void onRender2D(final Render2DEvent.Render2DEventData event) {
        if (!this.confirmInfo.getValue() && !this.calcInfo.getValue()) {
            return;
        }
        final class_2338 infoPos = (this.renderPos != null) ? this.renderPos : ((this.bestPosition != null) ? this.bestPosition.method_17777() : null);
        if (infoPos == null) {
            return;
        }
        final List<String> lines = new ArrayList<String>();
        if (this.confirmInfo.getValue() && this.confirmTime > 0L) {
            lines.add("Confirm: " + this.confirmTime + "ms");
        }
        if (this.calcInfo.getValue()) {
            lines.add("Calc: " + this.calcTime + "ms");
        }
        if (lines.isEmpty()) {
            return;
        }
        final Vector2f projected = ProjectionUtil.project(new class_243((double)(infoPos.method_10263() + 1.0f), (double)(infoPos.method_10264() + 1.0f), (double)(infoPos.method_10260() + 1.0f)));
        if (projected.x == Float.MAX_VALUE && projected.y == Float.MAX_VALUE) {
            return;
        }
        final float fontSize = 6.5f;
        final float lineHeight = fontSize + 1.0f;
        float drawY = projected.y - (lines.size() - 1) * lineHeight / 2.0f;
        final Color textColor = new Color(160, 160, 160, 220);
        for (final String line : lines) {
            Fonts.SF_MEDIUM.drawCenteredText(event.matrixStack(), line, projected.x, drawY, fontSize, textColor);
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
            if (this.bestCrystal != null && this.passedTicks(this.breakTicks, this.breakDelay.getValue().intValue())) {
                this.attackCrystal(this.bestCrystal);
            }
            else if (this.bestPosition != null && this.passedTicks(this.placeTicks, this.placeDelay.getValue().intValue()) && !this.placedOnSpawn) {
                this.placeCrystal(this.bestPosition, false, false);
            }
        }
        else {
            if (this.bestCrystal != null && this.passedTicks(this.breakTicks, this.breakDelay.getValue().intValue())) {
                this.attackCrystal(this.bestCrystal);
            }
            if (this.bestPosition != null && this.passedTicks(this.placeTicks, this.placeDelay.getValue().intValue()) && !this.placedOnSpawn) {
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
        final Map<class_2338, CrystalTracker.Attempt> awaiting = this.crystalTracker.getAwaitingPositions();
        if (awaiting.isEmpty()) {
            return;
        }
        final long now = System.currentTimeMillis();
        for (final Map.Entry<class_2338, CrystalTracker.Attempt> entry : new ArrayList(awaiting.entrySet())) {
            final class_2338 bp = entry.getKey();
            final CrystalTracker.Attempt attempt = entry.getValue();
            final class_238 searchBox = new class_238(bp.method_10084()).method_1009(0.9, 0.8, 0.9);
            final List<class_1511> nearby = Module.mc.field_1687.method_8390((Class)class_1511.class, searchBox, entity -> entity != null && entity.method_5805() && entity.method_5707(bp.method_46558()) < 0.3);
            if (nearby.isEmpty()) {
                continue;
            }
            final class_1511 crystal = nearby.get(0);
            this.confirmTime = now - attempt.time();
            this.crystalTracker.confirmSpawn(bp);
            if (!this.passedTicks(this.breakTicks, this.breakDelay.getValue().intValue())) {
                continue;
            }
            this.handleSpawn(crystal);
        }
    }
    
    private void handleSpawn(final class_1511 crystal) {
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null) {
            return;
        }
        if (!this.canAttackCrystal(crystal)) {
            return;
        }
        this.attackCrystal(crystal);
        if (this.sequential.is("Strong") && this.passedTicks(this.placeTicks, this.placeDelay.getValue().intValue())) {
            this.calcPosition(this.placeRange.getValue(), Module.mc.field_1724.method_19538());
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
            vec = ((this.rotationVec.hitVec() == null) ? this.rotationVec.vec() : this.rotationVec.hitVec().method_17784());
        }
        else if (this.bestPosition != null) {
            vec = this.bestPosition.method_17784();
        }
        else if (this.bestCrystal != null) {
            vec = this.bestCrystal.method_19538();
        }
        if (vec == null) {
            this.rotated = true;
            this.rotating = false;
            return;
        }
        final Rotation desired = RotationUtil.rotationAt(vec);
        final Rotation limited = this.limitYaw(desired);
        this.applyRotation(vec, limited);
        this.rotated = this.isRotationAligned(desired);
        this.rotating = true;
        if (this.rotationVec != null && this.rotationTicks-- < 0) {
            this.rotationVec = null;
        }
    }
    
    private Rotation limitYaw(final Rotation desired) {
        if (!this.yawStep.getValue()) {
            return desired;
        }
        final Rotation active = RotationManager.getInstance().getRotation();
        final float baseYaw = (active != null) ? active.getYaw() : Module.mc.field_1724.method_36454();
        final float deltaYaw = class_3532.method_15393(desired.getYaw() - baseYaw);
        final float limitedDelta = class_3532.method_15363(deltaYaw, -this.yawAngle.getValue(), (float)this.yawAngle.getValue());
        return new Rotation(baseYaw + limitedDelta, desired.getPitch());
    }
    
    private boolean isRotationAligned(final Rotation targetRotation) {
        final Rotation activeRotation = RotationManager.getInstance().getRotation();
        final float yaw = (activeRotation != null) ? activeRotation.getYaw() : Module.mc.field_1724.method_36454();
        final float pitch = (activeRotation != null) ? activeRotation.getPitch() : Module.mc.field_1724.method_36455();
        final float deltaYaw = Math.abs(class_3532.method_15393(targetRotation.getYaw() - yaw));
        final float deltaPitch = Math.abs(class_3532.method_15393(targetRotation.getPitch() - pitch));
        return deltaYaw <= 8.0f && deltaPitch <= 8.0f;
    }
    
    private void applyRotation(final class_243 vec, final Rotation desired) {
        if (this.rotate.is("Off")) {
            return;
        }
        RotationStrategy strategy;
        if (this.rotate.is("Instant")) {
            strategy = new RotationStrategy(new InstantRotation(), MoveFixModule.enabled(), true).clientLook(false).ticksUntilReset(2);
        }
        else {
            strategy = new RotationStrategy(new SmoothRotation(), MoveFixModule.enabled(), true).clientLook(false).ticksUntilReset(3);
        }
        RotationManager.getInstance().addRotation(new Rotation.VecRotation(desired, vec), (class_1309)this.target, strategy, TaskPriority.CRITICAL, this.module);
    }
    
    private void attackCrystal(final class_1511 crystal) {
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null || Module.mc.field_1761 == null || crystal == null) {
            return;
        }
        if ((this.crystalTracker.isDead(crystal.method_5628()) && this.inhibit.getValue()) || this.shouldPause() || this.target == null) {
            return;
        }
        final class_1293 weakness = Module.mc.field_1724.method_6112(class_1294.field_5911);
        final class_1293 strength = Module.mc.field_1724.method_6112(class_1294.field_5910);
        int prevSlot = -1;
        final HotbarSearch antiWeaknessHotbar = this.findAntiWeaknessHotbar();
        final int antiWeaknessInv = this.findAntiWeaknessInventory();
        if (!this.antiWeakness.is("NONE") && weakness != null && (strength == null || strength.method_5578() < weakness.method_5578())) {
            prevSlot = this.switchTo(antiWeaknessHotbar, antiWeaknessInv, this.antiWeakness);
        }
        if (!this.rotate.is("Off")) {
            this.applyRotation(crystal.method_5829().method_1005(), RotationUtil.rotationAt(crystal.method_5829().method_1005()));
        }
        this.sendPacket((class_2596<?>)class_2824.method_34206((class_1297)crystal, Module.mc.field_1724.method_5715()));
        this.swingHand(false, true);
        this.breakTicks = 0;
        this.crystalTracker.onAttack(crystal, this.breakFailsafe.getValue(), this.attempts.getValue().intValue());
        this.rotationTicks = 10;
        for (final class_1297 entity : Module.mc.field_1687.method_18112()) {
            if (entity instanceof final class_1511 exCrystal) {
                if (exCrystal.method_5649(crystal.method_23317(), crystal.method_23318(), crystal.method_23321()) > 144.0 || this.crystalTracker.isDead(exCrystal.method_5628())) {
                    continue;
                }
                this.crystalTracker.setDead(exCrystal.method_5628(), System.currentTimeMillis());
            }
        }
        if (prevSlot != -1) {
            if (this.antiWeakness.is("SILENT")) {
                Module.mc.field_1724.method_31548().field_7545 = prevSlot;
                this.sendPacket((class_2596<?>)new class_2868(prevSlot));
            }
            else if (this.antiWeakness.is("INVENTORY")) {
                Module.mc.field_1761.method_2906(Module.mc.field_1724.field_7512.field_7763, prevSlot, Module.mc.field_1724.method_31548().field_7545, class_1713.field_7791, (class_1657)Module.mc.field_1724);
                this.sendPacket((class_2596<?>)new class_2815(Module.mc.field_1724.field_7512.field_7763));
            }
        }
    }
    
    private boolean canAttackCrystal(final class_1511 crystal) {
        if (crystal == null || this.target == null || Module.mc.field_1724 == null || Module.mc.field_1687 == null) {
            return false;
        }
        final double distanceSq = Module.mc.field_1724.method_33571().method_1025(crystal.method_5829().method_1005());
        final double maxRangeSq = this.canSee(crystal.method_19538()) ? this.square(this.explodeRange.getValue()) : this.square(this.explodeWallRange.getValue());
        if (distanceSq > maxRangeSq) {
            return false;
        }
        if (!crystal.method_5805()) {
            return false;
        }
        final float damage = ExplosionUtility.getAutoCrystalDamage(crystal.method_19538(), this.target, this.getPredictTicks(), false);
        float selfDamage = ExplosionUtility.getSelfExplosionDamage(crystal.method_19538(), this.getSelfPredictTicks(), false);
        final boolean overrideDamage = this.shouldOverrideMaxSelfDmg(damage, selfDamage);
        if (this.protectFriends.getValue()) {
            for (final class_1657 player : Module.mc.field_1687.method_18456()) {
                if (player != null && player != Module.mc.field_1724) {
                    if (!FriendManager.getInstance().contains(player.method_5477().getString())) {
                        continue;
                    }
                    final float friendDamage = ExplosionUtility.getAutoCrystalDamage(crystal.method_19538(), player, this.getPredictTicks(), false);
                    if (friendDamage <= selfDamage) {
                        continue;
                    }
                    selfDamage = friendDamage;
                }
            }
        }
        return selfDamage <= this.maxSelfDamage.getValue() || overrideDamage;
    }
    
    private int switchTo(final HotbarSearch hotbar, final int invSlot, final ModeSetting switchMode) {
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null || Module.mc.field_1761 == null) {
            return -1;
        }
        int prevSlot = Module.mc.field_1724.method_31548().field_7545;
        final String s = switchMode.getValue();
        switch (s) {
            case "INVENTORY": {
                if (invSlot != -1) {
                    prevSlot = invSlot;
                    Module.mc.field_1761.method_2906(Module.mc.field_1724.field_7512.field_7763, invSlot, Module.mc.field_1724.method_31548().field_7545, class_1713.field_7791, (class_1657)Module.mc.field_1724);
                    this.sendPacket((class_2596<?>)new class_2815(Module.mc.field_1724.field_7512.field_7763));
                    break;
                }
                break;
            }
            case "NORMAL": {
                if (hotbar.found()) {
                    InventoryUtil.swapToSlot(hotbar.slot(), true);
                    break;
                }
                break;
            }
            case "SILENT": {
                if (hotbar.found()) {
                    InventoryUtil.swapToSlot(hotbar.slot(), false);
                    break;
                }
                break;
            }
        }
        return prevSlot;
    }
    
    private void placeCrystal(final class_3965 bhr, final boolean packetRotate, final boolean onSpawn) {
        if (this.shouldPause() || Module.mc.field_1724 == null || bhr == null) {
            return;
        }
        int prevSlot = -1;
        final HotbarSearch crystalResult = this.findInHotbar(class_1802.field_8301);
        final int crystalResultInv = this.findInInventory(class_1802.field_8301);
        final boolean offhand = Module.mc.field_1724.method_6079().method_7909() instanceof class_1774;
        final boolean holdingCrystal = Module.mc.field_1724.method_6047().method_7909() instanceof class_1774 || offhand;
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
        if (!(Module.mc.field_1724.method_6047().method_7909() instanceof class_1774) && !offhand && !this.autoSwitch.is("SILENT")) {
            return;
        }
        final class_1268 hand = offhand ? class_1268.field_5810 : class_1268.field_5808;
        final boolean accepted = Module.mc.field_1761.method_2896(Module.mc.field_1724, hand, bhr).method_23665();
        if (!accepted) {
            return;
        }
        this.swingHand(offhand, false);
        if (this.passedTicks(this.breakTicks, this.breakDelay.getValue().intValue()) && this.idPredict.getValue()) {
            this.predictAttack();
        }
        this.placeTicks = 0;
        this.rotationTicks = 10;
        if (!bhr.method_17777().equals((Object)this.renderPos)) {
            this.renderMultiplier = System.currentTimeMillis();
            this.prevRenderPos = this.renderPos;
            this.renderPos = bhr.method_17777();
        }
        this.crystalTracker.addAwaitingPos(bhr.method_17777(), this.placeFailsafe.getValue());
        this.renderPositions.put(bhr.method_17777().method_10062(), System.currentTimeMillis());
        this.postPlaceSwitch(prevSlot);
        if (onSpawn) {
            this.placedOnSpawn = true;
            this.placeSyncTicks = 0;
        }
    }
    
    private void postPlaceSwitch(final int slot) {
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
    
    private void calcPosition(final float range, final class_243 center) {
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null) {
            return;
        }
        final long start = System.currentTimeMillis();
        this.calcTicks = 0;
        if (this.target == null) {
            this.renderPos = null;
            this.prevRenderPos = null;
            this.bestPosition = null;
            this.currentData = null;
            return;
        }
        final List<PlaceData> list = this.getPossibleBlocks(this.target, center, range).stream().filter(data -> this.isSafe(data.damage(), data.selfDamage(), data.overrideDamage())).toList();
        this.bestPosition = (list.isEmpty() ? null : this.filterPositions(list));
        this.calcTime = System.currentTimeMillis() - start;
    }
    
    private List<PlaceData> getPossibleBlocks(final class_1657 target, final class_243 center, final float range) {
        final List<PlaceData> blocks = new ArrayList<PlaceData>();
        final class_2338 playerPos = class_2338.method_49638((class_2374)center);
        class_243 predictedPlayerPos = PredictUtility.predictPosition((class_1657)Module.mc.field_1724, this.getSelfPredictTicks());
        if (predictedPlayerPos == null) {
            predictedPlayerPos = Module.mc.field_1724.method_19538();
        }
        final int r = (int)Math.ceil(range);
        final double scanRangeSq = this.square(range + 1.0f);
        for (int x = playerPos.method_10263() - r; x <= playerPos.method_10263() + r; ++x) {
            for (int y = playerPos.method_10264() - r; y <= playerPos.method_10264() + r; ++y) {
                for (int z = playerPos.method_10260() - r; z <= playerPos.method_10260() + r; ++z) {
                    final class_2338 bp = new class_2338(x, y, z);
                    if (bp.method_46558().method_1025(center) <= scanRangeSq) {
                        final PlaceData data = this.getPlaceData(bp, target, predictedPlayerPos);
                        if (data != null) {
                            blocks.add(data);
                        }
                    }
                }
            }
        }
        return blocks;
    }
    
    private List<CrystalData> getPossibleCrystals(final class_1657 target) {
        final List<CrystalData> crystals = new ArrayList<CrystalData>();
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null) {
            return crystals;
        }
        for (final class_1297 entity : Module.mc.field_1687.method_18112()) {
            if (entity instanceof final class_1511 crystal) {
                if (this.crystalTracker.isBlocked(crystal.method_5628())) {
                    continue;
                }
                if (this.crystalTracker.isDead(crystal.method_5628()) && this.inhibit.getValue()) {
                    continue;
                }
                final double maxRangeSq = this.canSee(crystal.method_19538()) ? this.square(this.explodeRange.getValue()) : this.square(this.explodeWallRange.getValue());
                if (Module.mc.field_1724.method_33571().method_1025(crystal.method_5829().method_1005()) > maxRangeSq) {
                    continue;
                }
                if (!crystal.method_5805()) {
                    continue;
                }
                final float damage = ExplosionUtility.getAutoCrystalDamage(entity.method_19538(), target, this.getPredictTicks(), false);
                float selfDamage = ExplosionUtility.getSelfExplosionDamage(entity.method_19538(), this.getSelfPredictTicks(), false);
                final boolean overrideDamage = this.shouldOverrideMaxSelfDmg(damage, selfDamage);
                if (this.protectFriends.getValue()) {
                    for (final class_1657 player : Module.mc.field_1687.method_18456()) {
                        if (player != null && player != Module.mc.field_1724) {
                            if (!FriendManager.getInstance().contains(player.method_5477().getString())) {
                                continue;
                            }
                            final float friendDamage = ExplosionUtility.getAutoCrystalDamage(entity.method_19538(), player, this.getPredictTicks(), false);
                            if (friendDamage <= selfDamage) {
                                continue;
                            }
                            selfDamage = friendDamage;
                        }
                    }
                }
                if (damage < 1.5f) {
                    continue;
                }
                if (selfDamage > this.maxSelfDamage.getValue() && !overrideDamage) {
                    continue;
                }
                crystals.add(new CrystalData(crystal, damage, selfDamage, overrideDamage));
            }
        }
        return crystals;
    }
    
    private void getCrystalToExplode() {
        if (this.target == null) {
            this.bestCrystal = null;
            return;
        }
        if (this.secondaryCrystal != null) {
            this.bestCrystal = (this.canAttackCrystal(this.secondaryCrystal) ? this.secondaryCrystal : null);
            this.secondaryCrystal = null;
            return;
        }
        final List<CrystalData> list = this.getPossibleCrystals(this.target).stream().filter(data -> this.isSafe(data.damage(), data.selfDamage(), data.overrideDamage())).toList();
        this.bestCrystal = (list.isEmpty() ? null : this.filterCrystals(list));
    }
    
    private boolean isSafe(final float damage, final float selfDamage, final boolean overrideDamage) {
        return Module.mc.field_1724 != null && Module.mc.field_1687 != null && (overrideDamage || (selfDamage + 0.5f <= Module.mc.field_1724.method_6032() + Module.mc.field_1724.method_6067() && (!this.efficiency.getValue() || damage / Math.max(0.1f, selfDamage) >= this.efficiencyFactor.getValue())));
    }
    
    private class_3965 filterPositions(final List<PlaceData> clearedList) {
        PlaceData bestData = null;
        float bestVal = 0.0f;
        for (final PlaceData data : clearedList) {
            if (!this.shouldOverrideMinDmg(data.damage()) && data.damage() <= this.minDamage.getValue()) {
                continue;
            }
            if (bestData != null && data.overrideDamage() && this.target.method_6067() + this.target.method_6032() < bestData.damage() && bestData.selfDamage() < data.selfDamage()) {
                continue;
            }
            final boolean shouldStopOverride = bestData != null && bestData.overrideDamage() && data.damage() > this.target.method_6032() + this.target.method_6067() && data.selfDamage() < bestData.selfDamage();
            final float safetyComparatorDelta = shouldStopOverride ? 10.0f : 1.0f;
            if (bestData != null && Math.abs(bestData.damage() - data.damage()) < safetyComparatorDelta && Math.abs(bestData.selfDamage() - data.selfDamage()) > 1.0f) {
                if (bestData.selfDamage() < data.selfDamage()) {
                    continue;
                }
                bestData = data;
                bestVal = data.damage();
            }
            else {
                if (bestVal >= data.damage()) {
                    continue;
                }
                bestData = data;
                bestVal = data.damage();
            }
        }
        if (bestData == null) {
            return null;
        }
        this.facePlacing = (bestData.damage() < this.minDamage.getValue());
        this.renderDamage = bestData.damage();
        this.renderSelfDamage = bestData.selfDamage();
        this.currentData = bestData;
        return bestData.bhr();
    }
    
    private class_1511 filterCrystals(final List<CrystalData> clearedList) {
        CrystalData bestData = null;
        float bestVal = 0.0f;
        for (final CrystalData data : clearedList) {
            if (!this.shouldOverrideMinDmg(data.damage()) && data.damage() <= this.minDamage.getValue()) {
                continue;
            }
            if (bestData != null && data.overrideDamage() && this.target.method_6067() + this.target.method_6032() < bestData.damage() && bestData.selfDamage() < data.selfDamage()) {
                continue;
            }
            final boolean shouldStopOverride = bestData != null && bestData.overrideDamage() && data.damage() > this.target.method_6032() + this.target.method_6067() && data.selfDamage() < bestData.selfDamage();
            final float safetyComparatorDelta = shouldStopOverride ? 10.0f : 1.0f;
            if (bestData != null && Math.abs(bestData.damage() - data.damage()) < safetyComparatorDelta && Math.abs(bestData.selfDamage() - data.selfDamage()) > 1.0f) {
                if (bestData.selfDamage() < data.selfDamage()) {
                    continue;
                }
                bestData = data;
                bestVal = data.damage();
            }
            else {
                if (bestVal >= data.damage()) {
                    continue;
                }
                bestData = data;
                bestVal = data.damage();
            }
        }
        if (bestData == null) {
            return null;
        }
        this.renderDamage = bestData.damage();
        this.renderSelfDamage = bestData.selfDamage();
        return bestData.crystal();
    }
    
    private PlaceData getPlaceData(final class_2338 bp, final class_1657 target, final class_243 predictedPlayerPos) {
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null) {
            return null;
        }
        if (this.crystalTracker.isPositionBlocked(bp, this.placeFailsafe.getValue(), this.attempts.getValue().intValue())) {
            return null;
        }
        if (!this.predictCrystalSpawn(bp, predictedPlayerPos)) {
            return null;
        }
        if (target != null && target.method_19538().method_1025(bp.method_46558().method_1031(0.0, 0.5, 0.0)) > 144.0) {
            return null;
        }
        final class_2248 base = Module.mc.field_1687.method_8320(bp).method_26204();
        if (base != class_2246.field_10540 && base != class_2246.field_9987) {
            return null;
        }
        final boolean freeSpace = Module.mc.field_1687.method_22347(bp.method_10084());
        if (!freeSpace) {
            return null;
        }
        if (this.isPositionBlockedByEntity(bp, true)) {
            return null;
        }
        final class_243 crystalVec = new class_243(bp.method_10263() + 0.5, bp.method_10264() + 1.0, bp.method_10260() + 0.5);
        final class_3965 interactResult = this.getInteractResult(bp, crystalVec);
        if (interactResult == null) {
            return null;
        }
        final float damage = (target == null) ? 10.0f : ExplosionUtility.getAutoCrystalDamage(crystalVec, target, this.getPredictTicks(), false);
        if (damage < 1.5f) {
            return null;
        }
        float selfDamage = ExplosionUtility.getSelfExplosionDamage(crystalVec, this.getSelfPredictTicks(), false);
        final boolean overrideDamage = this.shouldOverrideMaxSelfDmg(damage, selfDamage);
        if (this.protectFriends.getValue()) {
            for (final class_1657 player : Module.mc.field_1687.method_18456()) {
                if (player != null && player != Module.mc.field_1724) {
                    if (!FriendManager.getInstance().contains(player.method_5477().getString())) {
                        continue;
                    }
                    final float friendDamage = ExplosionUtility.getAutoCrystalDamage(crystalVec, player, this.getPredictTicks(), false);
                    if (friendDamage <= selfDamage) {
                        continue;
                    }
                    selfDamage = friendDamage;
                }
            }
        }
        if (selfDamage > this.maxSelfDamage.getValue() && !overrideDamage) {
            return null;
        }
        return new PlaceData(interactResult, damage, selfDamage, overrideDamage);
    }
    
    private boolean predictCrystalSpawn(final class_2338 bp, final class_243 predictedPlayerPos) {
        final class_243 predictedPos = bp.method_46558().method_1031(0.0, 1.5, 0.0);
        final class_243 predictedEyes = predictedPlayerPos.method_1031(0.0, (double)Module.mc.field_1724.method_18381(Module.mc.field_1724.method_18376()), 0.0);
        final double distanceSq = predictedEyes.method_1025(predictedPos);
        if (this.canSee(predictedPos)) {
            return distanceSq <= this.square(this.explodeRange.getValue());
        }
        return distanceSq <= this.square(this.explodeWallRange.getValue());
    }
    
    private class_3965 getInteractResult(final class_2338 bp, final class_243 crystalVec) {
        return this.interact.is("Strict") ? this.getStrictInteract(bp) : this.getDefaultInteract(crystalVec, bp);
    }
    
    private boolean isPositionBlockedByEntity(final class_2338 base, final boolean calcPhase) {
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null) {
            return false;
        }
        final class_238 box = new class_238(base.method_10084()).method_1009(0.0, 1.0, 0.0);
        for (final class_1297 entity : Module.mc.field_1687.method_18112()) {
            if (entity != null && entity.method_5805()) {
                if (!entity.method_5829().method_994(box)) {
                    continue;
                }
                if (entity instanceof class_1303) {
                    continue;
                }
                if (entity instanceof final class_1511 crystal) {
                    if (this.crystalTracker.isDead(crystal.method_5628())) {
                        continue;
                    }
                    if (this.crystalTracker.isBlocked(crystal.method_5628())) {
                        return true;
                    }
                    if (calcPhase) {
                        if (this.canAttackCrystal(crystal)) {
                            continue;
                        }
                    }
                    else if (crystal.method_19538().method_1025(box.method_1005()) > 0.3) {
                        this.secondaryCrystal = crystal;
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    private boolean shouldOverrideMaxSelfDmg(final float damage, final float selfDamage) {
        if (!this.overrideSelfDamage.getValue() || this.target == null || Module.mc.field_1724 == null || Module.mc.field_1687 == null) {
            return false;
        }
        final boolean targetSafe = this.target.method_6079().method_31574(class_1802.field_8288) || this.target.method_6047().method_31574(class_1802.field_8288);
        final boolean playerSafe = Module.mc.field_1724.method_6079().method_31574(class_1802.field_8288) || Module.mc.field_1724.method_6047().method_31574(class_1802.field_8288);
        final float targetHp = this.target.method_6032() + this.target.method_6067();
        final float playerHp = Module.mc.field_1724.method_6032() + Module.mc.field_1724.method_6067();
        final boolean canPop = damage > targetHp && targetSafe;
        final boolean canKill = damage > targetHp && !targetSafe;
        final boolean canPopSelf = selfDamage > playerHp && playerSafe;
        final boolean canKillSelf = selfDamage > playerHp && !playerSafe;
        return (canPopSelf && canKill && this.sacrificeTotem.getValue()) || (selfDamage > this.maxSelfDamage.getValue() && (canPop || canKill) && !canKillSelf && !canPopSelf);
    }
    
    private boolean shouldOverrideMinDmg(final float damage) {
        if (this.target == null) {
            return false;
        }
        if (this.facePlaceButton.getValue() != -999 && KeyStorage.isPressed(this.facePlaceButton.getValue())) {
            return true;
        }
        if (this.target.method_6032() + this.target.method_6067() - damage < 0.0f) {
            return true;
        }
        if (this.armorBreaker.getValue()) {
            for (final class_1799 armor : this.target.method_5661()) {
                if (armor != null && !armor.method_7960() && armor.method_7909() != class_1802.field_8162) {
                    if (!armor.method_7963()) {
                        continue;
                    }
                    final float durabilityPercent = (armor.method_7936() - armor.method_7919()) / (float)armor.method_7936() * 100.0f;
                    if (durabilityPercent < this.armorScale.getValue()) {
                        return true;
                    }
                    continue;
                }
            }
        }
        return this.target.method_6032() + this.target.method_6067() <= this.facePlaceHp.getValue();
    }
    
    private class_3965 getDefaultInteract(final class_243 crystalVector, final class_2338 bp) {
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null) {
            return null;
        }
        if (Module.mc.field_1724.method_33571().method_1025(crystalVector) > this.square(this.placeRange.getValue())) {
            return null;
        }
        final class_239 wallCheck = (class_239)Module.mc.field_1687.method_17742(new class_3959(Module.mc.field_1724.method_33571(), crystalVector, class_3959.class_3960.field_17558, class_3959.class_242.field_1348, (class_1297)Module.mc.field_1724));
        if (wallCheck instanceof final class_3965 blockHit) {
            if (blockHit.method_17783() == class_239.class_240.field_1332 && !blockHit.method_17777().equals((Object)bp) && Module.mc.field_1724.method_33571().method_1025(crystalVector) > this.square(this.placeWallRange.getValue())) {
                return null;
            }
        }
        final class_2350 side = Module.mc.field_1687.method_24794(bp.method_10084()) ? class_2350.field_11036 : class_2350.field_11033;
        return new class_3965(crystalVector, side, bp, false);
    }
    
    private class_3965 getStrictInteract(final class_2338 bp) {
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null) {
            return null;
        }
        float bestDistance = Float.MAX_VALUE;
        class_2350 bestDirection = null;
        class_243 bestVec = null;
        final float upPoint = this.strictCenter.getValue() ? ((float)bp.method_46558().method_10214()) : ((float)bp.method_10084().method_10264());
        if (Module.mc.field_1724.method_33571().method_10214() > upPoint) {
            bestDirection = class_2350.field_11036;
        }
        else if (Module.mc.field_1724.method_33571().method_10214() < bp.method_10264() && Module.mc.field_1687.method_22347(bp.method_10074())) {
            bestDirection = class_2350.field_11033;
        }
        final List<class_2350> directions = new ArrayList<class_2350>(List.of(class_2350.field_11043, class_2350.field_11035, class_2350.field_11034, class_2350.field_11039));
        if (bestDirection != null) {
            directions.add(bestDirection);
        }
        for (final class_2350 dir : directions) {
            final class_243 vec = bp.method_46558().method_1019(class_243.method_24954(dir.method_62675()).method_1021(0.5));
            if (!Module.mc.field_1687.method_8320(bp.method_10093(dir)).method_45474()) {
                continue;
            }
            final double distanceSq = Module.mc.field_1724.method_33571().method_1025(vec);
            final double maxDistanceSq = this.canSee(vec) ? this.square(this.placeRange.getValue()) : this.square(this.placeWallRange.getValue());
            if (distanceSq > maxDistanceSq) {
                continue;
            }
            if (distanceSq >= bestDistance) {
                continue;
            }
            bestDistance = (float)distanceSq;
            bestDirection = dir;
            bestVec = vec;
        }
        if (bestDirection == null || bestVec == null) {
            return null;
        }
        return new class_3965(bestVec, bestDirection, bp, false);
    }
    
    private void swingHand(final boolean offHand, final boolean attack) {
        final class_1268 hand = offHand ? class_1268.field_5810 : class_1268.field_5808;
        final String s = this.swingMode.getValue();
        switch (s) {
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
                break;
            }
        }
    }
    
    private void predictAttack() {
        for (int attacks = this.idAttacks.getValue().intValue(), i = 1; i <= attacks; ++i) {
            final int id = (int)(this.currentId + i);
            final class_1297 entity = Module.mc.field_1687.method_8469(id);
            if (entity == null || entity instanceof class_1511) {
                final class_2824 attackPacket = class_2824.method_34206((class_1297)Module.mc.field_1724, Module.mc.field_1724.method_5715());
                changeId(attackPacket, id);
                this.sendPacket((class_2596<?>)attackPacket);
                this.sendPacket((class_2596<?>)new class_2879(class_1268.field_5808));
            }
        }
    }
    
    private static void changeId(final class_2824 packet, final int id) {
        try {
            Field field;
            try {
                field = class_2824.class.getDeclaredField("entityId");
            }
            catch (final NoSuchFieldException ex) {
                field = class_2824.class.getDeclaredField("field_12870");
            }
            field.setAccessible(true);
            field.setInt(packet, id);
        }
        catch (final Exception ex2) {}
    }
    
    private void processSpawnPacket(final int id) {
        if (id > this.currentId) {
            this.currentId = id;
        }
    }
    
    private void confirmAwaitingBySpawn(final int entityId, final class_243 spawnPos) {
        if (spawnPos == null) {
            return;
        }
        final Map<class_2338, CrystalTracker.Attempt> awaiting = this.crystalTracker.getAwaitingPositions();
        if (awaiting.isEmpty()) {
            return;
        }
        final long now = System.currentTimeMillis();
        for (final Map.Entry<class_2338, CrystalTracker.Attempt> entry : new ArrayList(awaiting.entrySet())) {
            final class_2338 bp = entry.getKey();
            if (spawnPos.method_1025(bp.method_46558()) > 0.36) {
                continue;
            }
            final CrystalTracker.Attempt attempt = entry.getValue();
            this.confirmTime = now - attempt.time();
            this.crystalTracker.confirmSpawn(bp);
            if (this.passedTicks(this.breakTicks, this.breakDelay.getValue().intValue())) {
                final class_1297 entity = (Module.mc.field_1687 == null) ? null : Module.mc.field_1687.method_8469(entityId);
                if (entity instanceof final class_1511 crystal) {
                    this.handleSpawn(crystal);
                }
                else {
                    this.attackSpawnById(entityId);
                }
                break;
            }
            break;
        }
    }
    
    private void attackSpawnById(final int entityId) {
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null || this.target == null || this.shouldPause()) {
            return;
        }
        final class_2824 attackPacket = class_2824.method_34206((class_1297)Module.mc.field_1724, Module.mc.field_1724.method_5715());
        changeId(attackPacket, entityId);
        this.sendPacket((class_2596<?>)attackPacket);
        this.swingHand(false, true);
        this.crystalTracker.setDead(entityId, System.currentTimeMillis());
        this.breakTicks = 0;
        this.rotationTicks = 10;
    }
    
    private class_243 resolveEntitySpawnPos(final class_2604 packet) {
        final Double x = this.tryReadCoordinate(packet, "getX", "x");
        final Double y = this.tryReadCoordinate(packet, "getY", "y");
        final Double z = this.tryReadCoordinate(packet, "getZ", "z");
        if (x != null && y != null && z != null) {
            return new class_243((double)x, (double)y, (double)z);
        }
        try {
            final Method method = packet.getClass().getMethod("getPos", (Class<?>[])new Class[0]);
            final Object value = method.invoke(packet, new Object[0]);
            if (value instanceof final class_243 vec) {
                return vec;
            }
        }
        catch (final ReflectiveOperationException ex) {}
        return null;
    }
    
    private class_243 resolveExplosionPos(final class_2664 packet) {
        final class_243 center = this.tryReadCenter(packet);
        if (center != null) {
            return center;
        }
        final Double x = this.tryReadCoordinate(packet, "getX", "x");
        final Double y = this.tryReadCoordinate(packet, "getY", "y");
        final Double z = this.tryReadCoordinate(packet, "getZ", "z");
        if (x != null && y != null && z != null) {
            return new class_243((double)x, (double)y, (double)z);
        }
        if (Module.mc.field_1724 != null) {
            return Module.mc.field_1724.method_19538();
        }
        return class_243.field_1353;
    }
    
    private class_243 tryReadCenter(final class_2664 packet) {
        try {
            final Method method = packet.getClass().getMethod("center", (Class<?>[])new Class[0]);
            final Object value = method.invoke(packet, new Object[0]);
            if (value instanceof final class_243 vec) {
                return vec;
            }
        }
        catch (final ReflectiveOperationException ex) {}
        try {
            final Method method = packet.getClass().getMethod("getCenter", (Class<?>[])new Class[0]);
            final Object value = method.invoke(packet, new Object[0]);
            if (value instanceof final class_243 vec) {
                return vec;
            }
        }
        catch (final ReflectiveOperationException ex2) {}
        return null;
    }
    
    private Double tryReadCoordinate(final Object packet, final String methodName, final String fieldName) {
        try {
            final Method method = packet.getClass().getMethod(methodName, (Class<?>[])new Class[0]);
            final Object value = method.invoke(packet, new Object[0]);
            if (value instanceof final Number number) {
                return number.doubleValue();
            }
        }
        catch (final ReflectiveOperationException ex) {}
        try {
            final Field field = packet.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            final Object value = field.get(packet);
            if (value instanceof final Number number) {
                return number.doubleValue();
            }
        }
        catch (final ReflectiveOperationException ex2) {}
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
        final class_640 entry = Module.mc.method_1562().method_2871(Module.mc.field_1724.method_5667());
        return (entry == null) ? 0.0f : ((float)entry.method_2959());
    }
    
    private boolean shouldPause() {
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null || Module.mc.field_1761 == null) {
            return true;
        }
        final boolean offhand = Module.mc.field_1724.method_6079().method_7909() instanceof class_1774;
        final boolean mainHand = Module.mc.field_1724.method_6047().method_7909() instanceof class_1774;
        if (Module.mc.field_1761.method_2923() && !offhand && this.mining.getValue()) {
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
        if (Module.mc.field_1724.method_6115() && this.eating.getValue()) {
            this.currentState = State.Eating;
            return true;
        }
        if (this.rotationMarkedDirty()) {
            this.currentState = State.ExternalPause;
            return true;
        }
        if (Module.mc.field_1724.method_6032() + Module.mc.field_1724.method_6067() < this.pauseHP.getValue()) {
            this.currentState = State.LowHP;
            return true;
        }
        this.currentState = State.Active;
        return false;
    }
    
    private boolean rotationMarkedDirty() {
        if (this.surround.getValue()) {
            final SurroundModule surroundModule = SurroundModule.getInstance();
            if (surroundModule != null && surroundModule.isEnabled()) {
                return true;
            }
        }
        if (this.middleClick.getValue() && Module.mc.field_1690.field_1871.method_1434()) {
            final ClickPearlModule clickPearl = ClickPearlModule.getInstance();
            if (clickPearl != null && clickPearl.isEnabled() && clickPearl.isThrowingPearl()) {
                return true;
            }
        }
        return this.inventoryPause.getValue() && Module.mc.field_1755 != null;
    }
    
    private class_1657 findTarget(final float range, final String logic) {
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null) {
            return null;
        }
        final List<class_1657> candidates = new ArrayList<class_1657>();
        for (final class_1657 player : Module.mc.field_1687.method_18456()) {
            if (player != null && player != Module.mc.field_1724 && player.method_5805()) {
                if (player.method_7325()) {
                    continue;
                }
                if (FriendManager.getInstance().contains(player.method_5477().getString())) {
                    continue;
                }
                if (Module.mc.field_1724.method_5858((class_1297)player) > this.square(range)) {
                    continue;
                }
                candidates.add(player);
            }
        }
        if (candidates.isEmpty()) {
            return null;
        }
        if ("Health".equalsIgnoreCase(logic)) {
            return candidates.stream().min(Comparator.comparingDouble(p -> p.method_6032() + p.method_6067())).orElse(null);
        }
        final Stream<Object> stream = candidates.stream();
        final class_746 field_1724 = Module.mc.field_1724;
        Objects.requireNonNull(field_1724);
        return stream.min((Comparator<? super class_1657>)Comparator.comparingDouble((ToDoubleFunction<? super Object>)field_1724::method_5858)).orElse(null);
    }
    
    private HotbarSearch findInHotbar(final class_1792 item) {
        final int slot = InventoryUtil.findItem(item, true);
        return new HotbarSearch(slot != -1, slot);
    }
    
    private int findInInventory(final class_1792 item) {
        return InventoryUtil.findItem(item, false);
    }
    
    private int findAny(final class_1792 item) {
        final int hotbar = InventoryUtil.findItem(item, true);
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
            final class_1792 item = ((class_1799)Module.mc.field_1724.method_31548().field_7547.get(i)).method_7909();
            if (item instanceof class_1829 || item instanceof class_1810 || item instanceof class_1743 || item instanceof class_1821) {
                return new HotbarSearch(true, i);
            }
        }
        return new HotbarSearch(false, -1);
    }
    
    private int findAntiWeaknessInventory() {
        if (Module.mc.field_1724 == null) {
            return -1;
        }
        for (int i = 9; i < 36; ++i) {
            final class_1792 item = ((class_1799)Module.mc.field_1724.method_31548().field_7547.get(i)).method_7909();
            if (item instanceof class_1829 || item instanceof class_1810 || item instanceof class_1743 || item instanceof class_1821) {
                return i;
            }
        }
        return -1;
    }
    
    private void sendPacket(final class_2596<?> packet) {
        if (Module.mc.method_1562() != null && packet != null) {
            Module.mc.method_1562().method_52787((class_2596)packet);
        }
    }
    
    private boolean passedTicks(final int counter, final int delay) {
        return counter >= Math.max(0, delay);
    }
    
    private void cleanupRenderPositions() {
        final long now = System.currentTimeMillis();
        final long holdMs = this.renderHoldMs.getValue().longValue();
        this.renderPositions.entrySet().removeIf(entry -> now - entry.getValue() > holdMs);
    }
    
    private double square(final double value) {
        return value * value;
    }
    
    private boolean render(final String key) {
        return this.renderSettings.isEnabled(key);
    }
    
    private Color themed(final float dim, final int alpha) {
        final Color c = UIColors.primary(class_3532.method_15340(alpha, 0, 255));
        final int r = class_3532.method_15340((int)(c.getRed() * dim), 0, 255);
        final int g = class_3532.method_15340((int)(c.getGreen() * dim), 0, 255);
        final int b = class_3532.method_15340((int)(c.getBlue() * dim), 0, 255);
        return new Color(r, g, b, c.getAlpha());
    }
    
    private float animate(final AnimationUtil animation, final boolean state) {
        if (!this.render("Animate")) {
            animation.setValue(state ? 1.0 : 0.0);
            return state ? 1.0f : 0.0f;
        }
        animation.run(state ? 1.0 : 0.0, 260L, Easing.SINE_OUT, true);
        animation.update();
        return (float)animation.getValue();
    }
    
    private String round2(final float value) {
        return String.format(Locale.US, "%.2f", value);
    }
    
    private boolean canSee(final class_243 pos) {
        if (Module.mc.field_1724 == null || Module.mc.field_1687 == null) {
            return false;
        }
        final class_3965 result = Module.mc.field_1687.method_17742(new class_3959(Module.mc.field_1724.method_33571(), pos, class_3959.class_3960.field_17558, class_3959.class_242.field_1348, (class_1297)Module.mc.field_1724));
        return result.method_17783() == class_239.class_240.field_1333;
    }
    
    record PlaceData(class_3965 bhr, float damage, float selfDamage, boolean overrideDamage) {}
    
    record CrystalData(class_1511 crystal, float damage, float selfDamage, boolean overrideDamage) {}
    
    record RotationVec(class_243 vec, class_3965 hitVec, boolean place) {}
    
    record HotbarSearch(boolean found, int slot) {}
    
    private enum State
    {
        Active, 
        Eating, 
        LowHP, 
        NoTarget, 
        NoCrystalls, 
        ExternalPause, 
        Mining;
    }
    
    private static final class CrystalTracker
    {
        private final Map<Integer, Long> deadCrystals;
        private final Map<Integer, Integer> attackAttempts;
        private final Map<Integer, Long> blockedCrystals;
        private final Map<class_2338, Attempt> awaitingPositions;
        
        private CrystalTracker() {
            this.deadCrystals = new ConcurrentHashMap<Integer, Long>();
            this.attackAttempts = new ConcurrentHashMap<Integer, Integer>();
            this.blockedCrystals = new ConcurrentHashMap<Integer, Long>();
            this.awaitingPositions = new ConcurrentHashMap<class_2338, Attempt>();
        }
        
        void reset() {
            this.deadCrystals.clear();
            this.attackAttempts.clear();
            this.blockedCrystals.clear();
            this.awaitingPositions.clear();
        }
        
        void update() {
            final long now = System.currentTimeMillis();
            this.deadCrystals.entrySet().removeIf(entry -> now - entry.getValue() > 1800L);
            this.blockedCrystals.entrySet().removeIf(entry -> now - entry.getValue() > 1800L);
            this.awaitingPositions.entrySet().removeIf(entry -> now - entry.getValue().time() > 2500L);
        }
        
        void onAttack(final class_1511 crystal, final boolean breakFailsafeEnabled, final int maxAttempts) {
            if (crystal == null) {
                return;
            }
            final int id = crystal.method_5628();
            this.setDead(id, System.currentTimeMillis());
            if (breakFailsafeEnabled) {
                final int attempts = this.attackAttempts.getOrDefault(id, 0) + 1;
                this.attackAttempts.put(id, attempts);
                if (attempts >= Math.max(1, maxAttempts)) {
                    this.blockedCrystals.put(id, System.currentTimeMillis());
                }
            }
        }
        
        boolean isDead(final int id) {
            return this.deadCrystals.containsKey(id);
        }
        
        void setDead(final int id, final long time) {
            this.deadCrystals.put(id, time);
        }
        
        boolean isBlocked(final int id) {
            return this.blockedCrystals.containsKey(id);
        }
        
        void addAwaitingPos(final class_2338 pos, final boolean placeFailsafeEnabled) {
            final Attempt previous = this.awaitingPositions.get(pos);
            final int nextAttempt = (previous == null) ? 1 : (previous.attempts() + 1);
            this.awaitingPositions.put(pos.method_10062(), new Attempt(System.currentTimeMillis(), placeFailsafeEnabled ? nextAttempt : 1));
        }
        
        boolean isPositionBlocked(final class_2338 pos, final boolean placeFailsafeEnabled, final int maxAttempts) {
            if (!placeFailsafeEnabled) {
                return false;
            }
            final Attempt attempt = this.awaitingPositions.get(pos);
            return attempt != null && attempt.attempts() >= Math.max(1, maxAttempts);
        }
        
        void confirmSpawn(final class_2338 pos) {
            this.awaitingPositions.remove(pos);
        }
        
        Map<class_2338, Attempt> getAwaitingPositions() {
            return this.awaitingPositions;
        }
        
        record Attempt(long time, int attempts) {}
    }
}
