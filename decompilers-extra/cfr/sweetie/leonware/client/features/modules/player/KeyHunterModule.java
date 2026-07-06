/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  baritone.api.BaritoneAPI
 *  baritone.api.IBaritone
 *  baritone.api.pathing.goals.Goal
 *  baritone.api.pathing.goals.GoalNear
 *  lombok.Generated
 *  net.minecraft.class_1268
 *  net.minecraft.class_1297
 *  net.minecraft.class_1548
 *  net.minecraft.class_1588
 *  net.minecraft.class_1657
 *  net.minecraft.class_1694
 *  net.minecraft.class_1707
 *  net.minecraft.class_1713
 *  net.minecraft.class_1792
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_2246
 *  net.minecraft.class_2248
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_238
 *  net.minecraft.class_2382
 *  net.minecraft.class_243
 *  net.minecraft.class_2586
 *  net.minecraft.class_2595
 *  net.minecraft.class_2596
 *  net.minecraft.class_2646
 *  net.minecraft.class_2818
 *  net.minecraft.class_304
 *  net.minecraft.class_3532
 *  net.minecraft.class_3675$class_306
 *  net.minecraft.class_3965
 *  net.minecraft.class_437
 *  net.minecraft.class_4587
 *  net.minecraft.class_465
 *  net.minecraft.class_7439
 *  net.minecraft.class_9304
 *  net.minecraft.class_9334
 */
package sweetie.leonware.client.features.modules.player;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import baritone.api.pathing.goals.Goal;
import baritone.api.pathing.goals.GoalNear;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1548;
import net.minecraft.class_1588;
import net.minecraft.class_1657;
import net.minecraft.class_1694;
import net.minecraft.class_1707;
import net.minecraft.class_1713;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_2382;
import net.minecraft.class_243;
import net.minecraft.class_2586;
import net.minecraft.class_2595;
import net.minecraft.class_2596;
import net.minecraft.class_2646;
import net.minecraft.class_2818;
import net.minecraft.class_304;
import net.minecraft.class_3532;
import net.minecraft.class_3675;
import net.minecraft.class_3965;
import net.minecraft.class_437;
import net.minecraft.class_4587;
import net.minecraft.class_465;
import net.minecraft.class_7439;
import net.minecraft.class_9304;
import net.minecraft.class_9334;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.event.events.render.Render2DEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.system.configs.FriendManager;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.other.SlownessManager;
import sweetie.leonware.api.utils.other.TextUtil;
import sweetie.leonware.api.utils.player.InventoryUtil;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import sweetie.leonware.client.features.modules.combat.AuraModule;
import sweetie.leonware.client.ui.widget.Widget;
import sweetie.leonware.client.ui.widget.WidgetManager;
import sweetie.leonware.client.ui.widget.overlay.StaffsWidget;

@ModuleRegister(name="Key Hunter", category=Category.PLAYER)
public class KeyHunterModule
extends Module {
    private static final KeyHunterModule instance = new KeyHunterModule();
    private final SliderSetting searchRadius = new SliderSetting("\u0420\u0430\u0434\u0438\u0443\u0441 \u043f\u043e\u0438\u0441\u043a\u0430").value(Float.valueOf(160.0f)).range(50.0f, 400.0f).step(10.0f);
    private final SliderSetting playerFleeRadius = new SliderSetting("\u0420\u0430\u0434\u0438\u0443\u0441 \u0438\u0433\u0440\u043e\u043a\u043e\u0432").value(Float.valueOf(25.0f)).range(10.0f, 80.0f).step(5.0f);
    private final SliderSetting staffFleeRadius = new SliderSetting("\u0420\u0430\u0434\u0438\u0443\u0441 \u0441\u0442\u0430\u0444\u0430").value(Float.valueOf(50.0f)).range(10.0f, 100.0f).step(5.0f);
    private final SliderSetting minChests = new SliderSetting("\u041c\u0438\u043d. \u0441\u0443\u043d\u0434\u0443\u043a\u043e\u0432 \u0432 \u0437\u043e\u043d\u0435").value(Float.valueOf(3.0f)).range(1.0f, 15.0f).step(1.0f);
    private final SliderSetting maxChestDist = new SliderSetting("\u041c\u0430\u043a\u0441. \u0434\u0438\u0441\u0442\u0430\u043d\u0446\u0438\u044f \u0434\u043e \u0441\u0443\u043d\u0434\u0443\u043a\u0430").value(Float.valueOf(200.0f)).range(50.0f, 500.0f).step(10.0f);
    private final BooleanSetting autoEat = new BooleanSetting("\u0410\u0432\u0442\u043e \u0435\u0434\u0430").value(true);
    private final SliderSetting eatThreshold = new SliderSetting("\u0415\u0441\u0442\u044c \u043f\u0440\u0438 \u0433\u043e\u043b\u043e\u0434\u0435").value(Float.valueOf(14.0f)).range(1.0f, 20.0f).step(1.0f).setVisible(() -> (Boolean)this.autoEat.getValue());
    private final SliderSetting eatUntil = new SliderSetting("\u0415\u0441\u0442\u044c \u0434\u043e \u0433\u043e\u043b\u043e\u0434\u0430").value(Float.valueOf(18.0f)).range(1.0f, 20.0f).step(1.0f).setVisible(() -> (Boolean)this.autoEat.getValue());
    private final ModeSetting serverCmd = new ModeSetting("\u0421\u0435\u0440\u0432\u0435\u0440 \u0432\u043e\u0437\u0432\u0440\u0430\u0442\u0430").values("grief", "grief2").value("grief");
    private final BooleanSetting takeKeys = new BooleanSetting("\u0411\u0440\u0430\u0442\u044c \u043a\u043b\u044e\u0447\u0438 (\u043f\u0430\u043b\u043a\u0438)").value(true);
    private final BooleanSetting takeEGapples = new BooleanSetting("\u0411\u0440\u0430\u0442\u044c \u0437\u0430\u0447. \u044f\u0431\u043b\u043e\u043a\u0438").value(true);
    private final BooleanSetting takeGapples = new BooleanSetting("\u0411\u0440\u0430\u0442\u044c \u0437\u043e\u043b\u043e\u0442\u044b\u0435 \u044f\u0431\u043b\u043e\u043a\u0438").value(true);
    private final BooleanSetting takeBooks = new BooleanSetting("\u0411\u0440\u0430\u0442\u044c \u0437\u0430\u0447. \u043a\u043d\u0438\u0433\u0438").value(true);
    private final BooleanSetting landPriority = new BooleanSetting("\u041f\u0440\u0438\u043e\u0440\u0438\u0442\u0435\u0442 \u0441\u0443\u0448\u0438").value(true);
    private final SliderSetting mobFightRadius = new SliderSetting("\u0420\u0430\u0434\u0438\u0443\u0441 \u0443\u0431\u0438\u0439\u0441\u0442\u0432\u0430 \u043c\u043e\u0431\u043e\u0432").value(Float.valueOf(6.0f)).range(2.0f, 16.0f).step(1.0f);
    private final BooleanSetting mobThroughWalls = new BooleanSetting("\u041c\u043e\u0431\u044b \u0447\u0435\u0440\u0435\u0437 \u0441\u0442\u0435\u043d\u044b").value(false);
    private final SliderSetting creeperRadius = new SliderSetting("\u0420\u0430\u0434\u0438\u0443\u0441 \u043a\u0440\u0438\u043f\u0435\u0440\u0430").value(Float.valueOf(8.0f)).range(3.0f, 20.0f).step(1.0f);
    private final BooleanSetting creeperThroughWalls = new BooleanSetting("\u041a\u0440\u0438\u043f\u0435\u0440\u044b \u0447\u0435\u0440\u0435\u0437 \u0441\u0442\u0435\u043d\u044b").value(false);
    private final SliderSetting weaponSwitchRange = new SliderSetting("\u0414\u0438\u0441\u0442\u0430\u043d\u0446\u0438\u044f \u0441\u043c\u0435\u043d\u044b \u043e\u0440\u0443\u0436\u0438\u044f").value(Float.valueOf(5.0f)).range(2.0f, 12.0f).step(1.0f);
    private final BooleanSetting autoStorage = new BooleanSetting("\u0410\u0432\u0442\u043e \u0432\u044b\u0433\u0440\u0443\u0437\u043a\u0430 \u043b\u0443\u0442\u0430").value(true);
    private State state = State.SCANNING;
    private class_2338 targetChest = null;
    private int targetCartId = -1;
    private boolean targetIsCart = false;
    private boolean targetLocked = false;
    private final Set<class_2338> visitedChests = new HashSet<class_2338>();
    private final Set<Integer> visitedCarts = new HashSet<Integer>();
    private final List<class_2338> nearChests = new ArrayList<class_2338>();
    private final List<Integer> nearCarts = new ArrayList<Integer>();
    private long hubSentTime = 0L;
    private long rtpCooldownEnd = 0L;
    private long pathTimeout = 0L;
    private long stuckTimer = 0L;
    private long lootTimer = 0L;
    private long scanTimer = 0L;
    private long commandDelay = 0L;
    private long fightTimer = 0L;
    private class_2338 lastPos = null;
    private int lootSlotIndex = 0;
    private long lootSlotTimer = 0L;
    private boolean eatActive = false;
    private int keysLooted = 0;
    private int egapplesLooted = 0;
    private int gapplesLooted = 0;
    private int booksLooted = 0;
    private int staffFlees = 0;
    private int playerFlees = 0;
    private int creeperFlees = 0;
    private class_2338 homeChestPos = null;
    private final List<class_2338> knownHomeChests = new ArrayList<class_2338>();
    private final Random random = new Random();
    private long lastRandomLookTime = 0L;

    public KeyHunterModule() {
        this.addSettings(this.searchRadius, this.playerFleeRadius, this.staffFleeRadius, this.minChests, this.maxChestDist, this.autoEat, this.eatThreshold, this.eatUntil, this.serverCmd, this.takeKeys, this.takeEGapples, this.takeGapples, this.takeBooks, this.landPriority, this.mobFightRadius, this.mobThroughWalls, this.creeperRadius, this.creeperThroughWalls, this.weaponSwitchRange, this.autoStorage);
    }

    @Override
    public void onEnable() {
        this.state = State.SCANNING;
        this.visitedChests.clear();
        this.visitedCarts.clear();
        this.targetChest = null;
        this.targetCartId = -1;
        this.targetLocked = false;
        this.scanTimer = 0L;
        this.eatActive = false;
        this.lastPos = null;
        this.stuckTimer = System.currentTimeMillis();
        this.applyBaritoneSettings();
        TextUtil.sendMessage("\u00a7aKeyHunter: \u043c\u043e\u0434\u0443\u043b\u044c \u0432\u043a\u043b\u044e\u0447\u0451\u043d. \u041d\u0430\u0447\u0438\u043d\u0430\u044e \u0441\u043a\u0430\u043d\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u0435 \u0448\u0430\u0445\u0442.");
    }

    @Override
    public void onDisable() {
        this.cancelBaritone();
        this.resetBaritoneSettings();
        class_304.method_1416((class_3675.class_306)KeyHunterModule.mc.field_1690.field_1904.method_1429(), (boolean)false);
        this.eatActive = false;
        if (AuraModule.getInstance().isEnabled()) {
            AuraModule.getInstance().disable();
        }
        TextUtil.sendMessage("\u00a7cKeyHunter: \u043c\u043e\u0434\u0443\u043b\u044c \u0432\u044b\u043a\u043b\u044e\u0447\u0451\u043d.");
    }

    @Override
    public void onEvent() {
        this.addEvents(UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> this.tick())), PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(event -> {
            if (!event.isReceive()) {
                return;
            }
            class_2596<?> patt0$temp = event.packet();
            if (!(patt0$temp instanceof class_7439)) {
                return;
            }
            class_7439 p = (class_7439)patt0$temp;
            String msg = KeyHunterModule.strip(p.comp_763().getString());
            if (msg.contains("\u0412\u044b \u043d\u0435 \u043c\u043e\u0436\u0435\u0442\u0435 \u043b\u043e\u043c\u0430\u0442\u044c \u0432 \u044d\u0442\u043e\u043c \u0440\u0435\u0433\u0438\u043e\u043d\u0435") || msg.contains("\u0412\u044b \u043d\u0435 \u043c\u043e\u0436\u0435\u0442\u0435 \u0432\u0437\u0430\u0438\u043c\u043e\u0434\u0435\u0439\u0441\u0442\u0432\u043e\u0432\u0430\u0442\u044c \u0432 \u044d\u0442\u043e\u043c \u0440\u0435\u0433\u0438\u043e\u043d\u0435")) {
                this.cancelBaritone();
                if (this.targetChest != null) {
                    this.visitedChests.add(this.targetChest);
                }
                if (this.targetIsCart) {
                    this.visitedCarts.add(this.targetCartId);
                }
                this.targetLocked = false;
                TextUtil.sendMessage("\u00a7eKeyHunter: \u0440\u0435\u0433\u0438\u043e\u043d \u043f\u0440\u0438\u0432\u0430\u0442\u0435\u043d, \u0438\u0449\u0443 \u0434\u0440\u0443\u0433\u043e\u0435 \u043c\u0435\u0441\u0442\u043e \u0447\u0435\u0440\u0435\u0437 /rtp s.");
                this.forceRtp();
            }
        })), Render2DEvent.getInstance().subscribe(new Listener<Render2DEvent.Render2DEventData>(event -> this.renderHud(event.matrixStack()))));
    }

    private void tick() {
        int food;
        if (KeyHunterModule.mc.field_1724 == null || KeyHunterModule.mc.field_1687 == null) {
            return;
        }
        this.applyRandomLook();
        if (((Boolean)this.autoEat.getValue()).booleanValue() && this.state != State.EATING && this.state != State.FLEEING_STAFF && this.state != State.FLEEING_PLAYER && this.state != State.FLEEING_CREEPER && this.state != State.WAITING_HUB && (food = KeyHunterModule.mc.field_1724.method_7344().method_7586()) <= ((Float)this.eatThreshold.getValue()).intValue()) {
            this.cancelBaritone();
            class_304.method_1416((class_3675.class_306)KeyHunterModule.mc.field_1690.field_1904.method_1429(), (boolean)false);
            this.eatActive = false;
            if (AuraModule.getInstance().isEnabled()) {
                AuraModule.getInstance().disable();
            }
            TextUtil.sendMessage("\u00a7eKeyHunter: \u0433\u043e\u043b\u043e\u0434 " + food + " <= " + ((Float)this.eatThreshold.getValue()).intValue() + ", \u043d\u0430\u0447\u0438\u043d\u0430\u044e \u0435\u0441\u0442\u044c.");
            this.state = State.EATING;
            return;
        }
        boolean needsMobs = this.hasMobsNearby();
        if (needsMobs) {
            if (!AuraModule.getInstance().isEnabled()) {
                AuraModule.getInstance().enable();
            }
        } else if (AuraModule.getInstance().isEnabled() && this.state != State.FIGHTING_MOBS) {
            AuraModule.getInstance().disable();
        }
        if (this.state != State.FLEEING_STAFF && this.state != State.WAITING_HUB && this.state != State.FLEEING_PLAYER && this.state != State.FLEEING_CREEPER) {
            if (this.checkStaff()) {
                return;
            }
            if (this.checkPlayers()) {
                return;
            }
            if (this.checkCreeper()) {
                return;
            }
        }
        if ((this.state == State.PATHING || this.state == State.SCANNING || this.state == State.FIGHTING_MOBS) && this.checkMobs()) {
            return;
        }
        switch (this.state.ordinal()) {
            case 0: {
                this.doScan();
                break;
            }
            case 1: {
                this.doPath();
                break;
            }
            case 2: {
                this.doFightMobs();
                break;
            }
            case 3: {
                this.doLoot();
                break;
            }
            case 4: {
                this.doEat();
                break;
            }
            case 5: {
                this.doFleeStaff();
                break;
            }
            case 6: {
                this.doWaitHub();
                break;
            }
            case 7: {
                this.doFleePlayer();
                break;
            }
            case 11: {
                this.doFleeCreeper();
                break;
            }
            case 8: {
                this.doRtpCooldown();
                break;
            }
            case 9: {
                this.doGoHome();
                break;
            }
            case 10: {
                this.doStore();
            }
        }
    }

    private boolean checkCreeper() {
        if (KeyHunterModule.mc.field_1724 == null || KeyHunterModule.mc.field_1687 == null) {
            return false;
        }
        double r = ((Float)this.creeperRadius.getValue()).floatValue();
        class_238 box = KeyHunterModule.mc.field_1724.method_5829().method_1014(r);
        List creepers = KeyHunterModule.mc.field_1687.method_8390(class_1548.class, box, e -> {
            if (!((Boolean)this.creeperThroughWalls.getValue()).booleanValue()) {
                return KeyHunterModule.mc.field_1724.method_6057((class_1297)e);
            }
            return true;
        });
        if (!creepers.isEmpty()) {
            this.cancelBaritone();
            if (AuraModule.getInstance().isEnabled()) {
                AuraModule.getInstance().disable();
            }
            if (this.state != State.FLEEING_CREEPER) {
                ++this.creeperFlees;
                TextUtil.sendMessage("\u00a7cKeyHunter: \u043e\u0431\u043d\u0430\u0440\u0443\u0436\u0435\u043d \u043a\u0440\u0438\u043f\u0435\u0440! \u0422\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0438\u0440\u0443\u044e\u0441\u044c \u0447\u0435\u0440\u0435\u0437 /rtp s.");
                this.state = State.FLEEING_CREEPER;
                this.commandDelay = System.currentTimeMillis() + 400L;
            }
            return true;
        }
        return false;
    }

    private void doFleeCreeper() {
        if (System.currentTimeMillis() < this.commandDelay) {
            return;
        }
        this.cancelBaritone();
        if (KeyHunterModule.mc.field_1724 != null) {
            KeyHunterModule.mc.field_1724.field_3944.method_45730("rtp s");
        }
        this.rtpCooldownEnd = System.currentTimeMillis() + 9000L;
        this.visitedChests.clear();
        this.visitedCarts.clear();
        this.targetLocked = false;
        this.scanTimer = System.currentTimeMillis() + 5000L;
        this.state = State.RTP_COOLDOWN;
    }

    private void doPath() {
        class_1694 cart;
        if (KeyHunterModule.mc.field_1724 == null) {
            return;
        }
        if (((Boolean)this.autoStorage.getValue()).booleanValue() && this.shouldGoHome()) {
            this.cancelBaritone();
            TextUtil.sendMessage("\u00a7eKeyHunter: \u0438\u043d\u0432\u0435\u043d\u0442\u0430\u0440\u044c \u043f\u043e\u0447\u0442\u0438 \u043f\u043e\u043b\u043e\u043d, \u0438\u0434\u0443 \u0434\u043e\u043c\u043e\u0439 \u0441\u043a\u043b\u0430\u0434\u044b\u0432\u0430\u0442\u044c \u043b\u0443\u0442.");
            this.state = State.GOING_HOME;
            this.commandDelay = System.currentTimeMillis() + 400L;
            return;
        }
        if (this.targetIsCart) {
            cart = this.getCartById(this.targetCartId);
            if (cart == null) {
                this.visitedCarts.add(this.targetCartId);
                this.targetLocked = false;
                this.state = State.SCANNING;
                return;
            }
            if (KeyHunterModule.mc.field_1724.method_19538().method_1022(cart.method_19538()) <= 3.5) {
                this.cancelBaritone();
                this.lookAt(cart.method_19538());
                this.interactCart(cart);
                this.state = State.LOOTING;
                this.lootTimer = System.currentTimeMillis() + 500L;
                this.lootSlotIndex = 0;
                TextUtil.sendMessage("\u00a7aKeyHunter: \u0434\u043e\u0431\u0440\u0430\u043b\u0441\u044f \u0434\u043e \u0432\u0430\u0433\u043e\u043d\u0435\u0442\u043a\u0438, \u043e\u0442\u043a\u0440\u044b\u0432\u0430\u044e \u0438 \u043b\u0443\u0442\u0430\u044e.");
                return;
            }
            if (this.isStuck()) {
                TextUtil.sendMessage("\u00a7eKeyHunter: \u0437\u0430\u0441\u0442\u0440\u044f\u043b (\u043c\u0435\u043d\u044c\u0448\u0435 4 \u0431\u043b\u043e\u043a\u043e\u0432 \u0437\u0430 30 \u0441\u0435\u043a\u0443\u043d\u0434), \u0432\u044b\u043f\u043e\u043b\u043d\u044f\u044e /rtp s.");
                this.visitedCarts.add(this.targetCartId);
                this.targetLocked = false;
                this.forceRtp();
                return;
            }
        } else {
            if (this.targetChest == null) {
                this.targetLocked = false;
                this.state = State.SCANNING;
                return;
            }
            if (Math.sqrt(KeyHunterModule.mc.field_1724.method_24515().method_10262((class_2382)this.targetChest)) <= 3.5) {
                this.cancelBaritone();
                this.lookAt(class_243.method_24953((class_2382)this.targetChest));
                this.interactChest(this.targetChest);
                this.state = State.LOOTING;
                this.lootTimer = System.currentTimeMillis() + 500L;
                this.lootSlotIndex = 0;
                TextUtil.sendMessage("\u00a7aKeyHunter: \u0434\u043e\u0431\u0440\u0430\u043b\u0441\u044f \u0434\u043e \u0441\u0443\u043d\u0434\u0443\u043a\u0430, \u043e\u0442\u043a\u0440\u044b\u0432\u0430\u044e \u0438 \u043b\u0443\u0442\u0430\u044e.");
                return;
            }
            if (this.isStuck()) {
                TextUtil.sendMessage("\u00a7eKeyHunter: \u0437\u0430\u0441\u0442\u0440\u044f\u043b (\u043c\u0435\u043d\u044c\u0448\u0435 4 \u0431\u043b\u043e\u043a\u043e\u0432 \u0437\u0430 30 \u0441\u0435\u043a\u0443\u043d\u0434), \u0432\u044b\u043f\u043e\u043b\u043d\u044f\u044e /rtp s.");
                this.visitedChests.add(this.targetChest);
                this.targetLocked = false;
                this.forceRtp();
                return;
            }
        }
        if (!this.isBaritonePathing()) {
            if (this.targetIsCart) {
                cart = this.getCartById(this.targetCartId);
                if (cart != null) {
                    this.startPathing(cart.method_24515());
                }
            } else if (this.targetChest != null) {
                this.startPathing(this.targetChest);
            }
        }
    }

    private void doFightMobs() {
        if (KeyHunterModule.mc.field_1724 == null || KeyHunterModule.mc.field_1687 == null) {
            return;
        }
        double r = ((Float)this.mobFightRadius.getValue()).floatValue();
        class_238 box = KeyHunterModule.mc.field_1724.method_5829().method_1014(r);
        List mobs = KeyHunterModule.mc.field_1687.method_8390(class_1588.class, box, e -> {
            if (e instanceof class_1548) {
                return false;
            }
            if (!e.method_5805()) {
                return false;
            }
            if (!((Boolean)this.mobThroughWalls.getValue()).booleanValue()) {
                return KeyHunterModule.mc.field_1724.method_6057((class_1297)e);
            }
            return true;
        });
        if (mobs.isEmpty()) {
            AuraModule.getInstance().disable();
            TextUtil.sendMessage("\u00a7aKeyHunter: \u0432\u0441\u0435 \u043c\u043e\u0431\u044b \u0443\u043d\u0438\u0447\u0442\u043e\u0436\u0435\u043d\u044b. \u041f\u0440\u043e\u0434\u043e\u043b\u0436\u0430\u044e \u0434\u0432\u0438\u0436\u0435\u043d\u0438\u0435 \u043a \u0441\u0443\u043d\u0434\u0443\u043a\u0443.");
            this.state = State.PATHING;
            if (this.targetIsCart) {
                class_1694 cart = this.getCartById(this.targetCartId);
                if (cart != null) {
                    this.startPathing(cart.method_24515());
                }
            } else if (this.targetChest != null) {
                this.startPathing(this.targetChest);
            } else {
                this.state = State.SCANNING;
            }
            return;
        }
        if (System.currentTimeMillis() > this.fightTimer) {
            TextUtil.sendMessage("\u00a7eKeyHunter: \u0442\u0430\u0439\u043c\u0435\u0440 \u0431\u043e\u044f \u0438\u0441\u0442\u0451\u043a, \u0432\u043e\u0437\u0432\u0440\u0430\u0449\u0430\u044e\u0441\u044c \u043a \u0441\u043a\u0430\u043d\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u044e.");
            this.state = State.SCANNING;
            this.targetLocked = false;
            return;
        }
        class_1588 nearest = mobs.stream().min(Comparator.comparingDouble(e -> KeyHunterModule.mc.field_1724.method_5739((class_1297)e))).orElse(null);
        if (nearest != null) {
            this.lookAt(nearest.method_33571());
        }
        this.ensureBestWeapon();
    }

    private String stateLabel() {
        return switch (this.state.ordinal()) {
            default -> throw new MatchException(null, null);
            case 0 -> "\u041f\u043e\u0438\u0441\u043a \u0441\u0443\u043d\u0434\u0443\u043a\u043e\u0432...";
            case 1 -> "\u0414\u0432\u0438\u0436\u0435\u043d\u0438\u0435 \u043a \u0441\u0443\u043d\u0434\u0443\u043a\u0443";
            case 2 -> "\u0423\u0431\u0438\u0432\u0430\u044e \u043c\u043e\u0431\u043e\u0432...";
            case 3 -> "\u041b\u0443\u0442\u0430\u044e \u0441\u0443\u043d\u0434\u0443\u043a";
            case 4 -> "\u0415\u043c \u0435\u0434\u0443...";
            case 5 -> "\u041f\u043e\u0431\u0435\u0433 \u043e\u0442 \u0441\u0442\u0430\u0444\u0430!";
            case 11 -> "\u041f\u043e\u0431\u0435\u0433 \u043e\u0442 \u043a\u0440\u0438\u043f\u0435\u0440\u0430!";
            case 6 -> {
                long left = (this.hubSentTime + 300000L - System.currentTimeMillis()) / 1000L;
                yield "\u0425\u0430\u0431. \u0412\u043e\u0437\u0432\u0440\u0430\u0442 \u0447\u0435\u0440\u0435\u0437 " + Math.max(0L, left) + "\u0441";
            }
            case 7 -> "\u041f\u043e\u0431\u0435\u0433 \u043e\u0442 \u0438\u0433\u0440\u043e\u043a\u0430!";
            case 8 -> {
                long left = (this.rtpCooldownEnd - System.currentTimeMillis()) / 1000L;
                yield "RTP \u043a\u0443\u043b\u0434\u0430\u0443\u043d: " + Math.max(0L, left) + "\u0441";
            }
            case 9 -> "\u0414\u043e\u043c\u043e\u0439...";
            case 10 -> "\u0421\u043a\u043b\u0430\u0434\u044b\u0432\u0430\u044e \u0432\u0435\u0449\u0438";
        };
    }

    private void renderHud(class_4587 ms) {
        if (KeyHunterModule.mc.field_1724 == null || !this.isEnabled()) {
            return;
        }
        float cx = (float)mc.method_22683().method_4486() / 2.0f;
        float cy = (float)mc.method_22683().method_4502() / 2.0f + 20.0f;
        float fontSize = 6.5f;
        float lineH = fontSize + 3.0f;
        float pad = 5.0f;
        float round = 3.0f;
        String stateStr = this.stateLabel();
        String line1 = "\u041a\u043b\u044e\u0447\u0435\u0439: " + this.keysLooted;
        String line2 = "\u0417\u0430\u0447. \u044f\u0431\u043b\u043e\u043a: " + this.egapplesLooted;
        String line3 = "\u0417\u043e\u043b\u043e\u0442\u044b\u0445 \u044f\u0431\u043b\u043e\u043a: " + this.gapplesLooted;
        String line4 = "\u0417\u0430\u0447. \u043a\u043d\u0438\u0433: " + this.booksLooted;
        String line5 = "\u041f\u043e\u0431\u0435\u0433 \u043e\u0442 \u0441\u0442\u0430\u0444\u0430: " + this.staffFlees;
        String line6 = "\u041f\u043e\u0431\u0435\u0433 \u043e\u0442 \u0438\u0433\u0440\u043e\u043a\u043e\u0432: " + this.playerFlees;
        String line7 = "\u041f\u043e\u0431\u0435\u0433 \u043e\u0442 \u043a\u0440\u0438\u043f\u0435\u0440\u043e\u0432: " + this.creeperFlees;
        float maxW = 0.0f;
        for (String l : new String[]{stateStr, line1, line2, line3, line4, line5, line6, line7}) {
            maxW = Math.max(maxW, Fonts.PS_BOLD.getWidth(l, fontSize));
        }
        float width = maxW + pad * 2.0f;
        float height = lineH * 8.0f + pad * 2.0f;
        float x = cx - width / 2.0f;
        RenderUtil.BLUR_RECT.draw(ms, x, cy, width, height, round, UIColors.widgetBlur());
        float ty = cy + pad;
        Fonts.PS_BOLD.drawGradientText(ms, stateStr, x + pad, ty, fontSize, UIColors.primary(), UIColors.secondary(), width / 4.0f);
        Fonts.PS_BOLD.drawText(ms, line1, x + pad, ty += lineH, fontSize, Color.WHITE);
        Fonts.PS_BOLD.drawText(ms, line2, x + pad, ty += lineH, fontSize, new Color(255, 215, 0));
        Fonts.PS_BOLD.drawText(ms, line3, x + pad, ty += lineH, fontSize, new Color(255, 165, 0));
        Fonts.PS_BOLD.drawText(ms, line4, x + pad, ty += lineH, fontSize, new Color(100, 180, 255));
        Fonts.PS_BOLD.drawText(ms, line5, x + pad, ty += lineH, fontSize, new Color(255, 80, 80));
        Fonts.PS_BOLD.drawText(ms, line6, x + pad, ty += lineH, fontSize, new Color(255, 140, 0));
        Fonts.PS_BOLD.drawText(ms, line7, x + pad, ty += lineH, fontSize, new Color(255, 100, 100));
    }

    private void applyRandomLook() {
        if (KeyHunterModule.mc.field_1724 == null) {
            return;
        }
        long now = System.currentTimeMillis();
        if (now - this.lastRandomLookTime < (long)(800 + this.random.nextInt(600))) {
            return;
        }
        if (this.state != State.PATHING && this.state != State.SCANNING) {
            return;
        }
        this.lastRandomLookTime = now;
        float yawOffset = (this.random.nextFloat() - 0.5f) * 12.0f;
        float pitchOffset = (this.random.nextFloat() - 0.5f) * 6.0f;
        KeyHunterModule.mc.field_1724.method_36456(KeyHunterModule.mc.field_1724.method_36454() + yawOffset);
        KeyHunterModule.mc.field_1724.method_36457(class_3532.method_15363((float)(KeyHunterModule.mc.field_1724.method_36455() + pitchOffset), (float)-90.0f, (float)90.0f));
    }

    private void lookAt(class_243 target) {
        if (KeyHunterModule.mc.field_1724 == null) {
            return;
        }
        class_243 eyePos = KeyHunterModule.mc.field_1724.method_33571();
        class_243 dir = target.method_1020(eyePos);
        double horizontalDist = Math.sqrt(dir.field_1352 * dir.field_1352 + dir.field_1350 * dir.field_1350);
        float yaw = (float)Math.toDegrees(Math.atan2(dir.field_1350, dir.field_1352)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(dir.field_1351, horizontalDist)));
        KeyHunterModule.mc.field_1724.method_36456(yaw);
        KeyHunterModule.mc.field_1724.method_36457(class_3532.method_15363((float)pitch, (float)-90.0f, (float)90.0f));
    }

    private boolean hasMobsNearby() {
        if (KeyHunterModule.mc.field_1724 == null || KeyHunterModule.mc.field_1687 == null) {
            return false;
        }
        double r = ((Float)this.mobFightRadius.getValue()).floatValue();
        class_238 box = KeyHunterModule.mc.field_1724.method_5829().method_1014(r);
        return !KeyHunterModule.mc.field_1687.method_8390(class_1588.class, box, e -> {
            if (e instanceof class_1548) {
                return false;
            }
            if (!e.method_5805()) {
                return false;
            }
            if (!((Boolean)this.mobThroughWalls.getValue()).booleanValue()) {
                return KeyHunterModule.mc.field_1724.method_6057((class_1297)e);
            }
            return true;
        }).isEmpty();
    }

    private boolean checkStaff() {
        if (KeyHunterModule.mc.field_1724 == null) {
            return false;
        }
        StaffsWidget sw = this.getStaffsWidget();
        if (sw == null) {
            return false;
        }
        for (StaffsWidget.Staff staff : sw.getStaffList()) {
            String name = staff.name().split(":")[0];
            for (class_1657 p : KeyHunterModule.mc.field_1687.method_18456()) {
                if (!p.method_7334().getName().equalsIgnoreCase(name) || !(KeyHunterModule.mc.field_1724.method_5739((class_1297)p) <= ((Float)this.staffFleeRadius.getValue()).floatValue())) continue;
                if (this.state != State.FLEEING_STAFF) {
                    this.cancelBaritone();
                    ++this.staffFlees;
                    TextUtil.sendMessage("\u00a7cKeyHunter: \u0441\u0442\u0430\u0444\u0444 " + name + " \u0440\u044f\u0434\u043e\u043c! \u0423\u0445\u043e\u0436\u0443 \u0432 \u0445\u0430\u0431.");
                    this.state = State.FLEEING_STAFF;
                    this.commandDelay = System.currentTimeMillis() + 400L;
                }
                return true;
            }
        }
        return false;
    }

    private boolean checkPlayers() {
        if (KeyHunterModule.mc.field_1724 == null) {
            return false;
        }
        for (class_1657 p : KeyHunterModule.mc.field_1687.method_18456()) {
            if (p == KeyHunterModule.mc.field_1724 || FriendManager.getInstance().contains(p.method_7334().getName()) || this.isStaff(p) || !(KeyHunterModule.mc.field_1724.method_5739((class_1297)p) <= ((Float)this.playerFleeRadius.getValue()).floatValue())) continue;
            if (this.state != State.FLEEING_PLAYER) {
                this.cancelBaritone();
                ++this.playerFlees;
                TextUtil.sendMessage("\u00a7cKeyHunter: \u0438\u0433\u0440\u043e\u043a " + p.method_7334().getName() + " \u0440\u044f\u0434\u043e\u043c! \u0412\u044b\u043f\u043e\u043b\u043d\u044f\u044e /rtp s.");
                this.state = State.FLEEING_PLAYER;
                this.commandDelay = System.currentTimeMillis() + 400L;
            }
            return true;
        }
        return false;
    }

    private boolean checkMobs() {
        if (KeyHunterModule.mc.field_1724 == null || KeyHunterModule.mc.field_1687 == null) {
            return false;
        }
        double r = ((Float)this.mobFightRadius.getValue()).floatValue();
        class_238 box = KeyHunterModule.mc.field_1724.method_5829().method_1014(r);
        List mobs = KeyHunterModule.mc.field_1687.method_8390(class_1588.class, box, e -> {
            if (e instanceof class_1548) {
                return false;
            }
            if (!e.method_5805()) {
                return false;
            }
            if (!((Boolean)this.mobThroughWalls.getValue()).booleanValue()) {
                return KeyHunterModule.mc.field_1724.method_6057((class_1297)e);
            }
            return true;
        });
        if (!mobs.isEmpty()) {
            this.cancelBaritone();
            this.ensureBestWeapon();
            if (!AuraModule.getInstance().isEnabled()) {
                AuraModule.getInstance().enable();
            }
            if (this.state != State.FIGHTING_MOBS) {
                TextUtil.sendMessage("\u00a7cKeyHunter: \u043e\u0431\u043d\u0430\u0440\u0443\u0436\u0435\u043d\u044b \u0432\u0440\u0430\u0436\u0434\u0435\u0431\u043d\u044b\u0435 \u043c\u043e\u0431\u044b! \u041f\u0435\u0440\u0435\u043a\u043b\u044e\u0447\u0430\u044e\u0441\u044c \u0432 \u0440\u0435\u0436\u0438\u043c \u0431\u043e\u044f.");
                this.state = State.FIGHTING_MOBS;
                this.fightTimer = System.currentTimeMillis() + 12000L;
            }
            return true;
        }
        return false;
    }

    private void doScan() {
        if (System.currentTimeMillis() < this.scanTimer) {
            return;
        }
        if (this.targetLocked) {
            class_1694 cart;
            if (!this.targetIsCart && this.targetChest != null && !this.visitedChests.contains(this.targetChest)) {
                this.state = State.PATHING;
                this.startPathing(this.targetChest);
                return;
            }
            if (this.targetIsCart && !this.visitedCarts.contains(this.targetCartId) && (cart = this.getCartById(this.targetCartId)) != null) {
                this.state = State.PATHING;
                this.startPathing(cart.method_24515());
                return;
            }
            this.targetLocked = false;
        }
        this.scanTimer = System.currentTimeMillis() + 3000L;
        this.nearChests.clear();
        this.nearCarts.clear();
        int r = ((Float)this.searchRadius.getValue()).intValue();
        class_2338 center = KeyHunterModule.mc.field_1724.method_24515();
        for (int cx = center.method_10263() - r >> 4; cx <= center.method_10263() + r >> 4; ++cx) {
            for (int cz = center.method_10260() - r >> 4; cz <= center.method_10260() + r >> 4; ++cz) {
                if (!KeyHunterModule.mc.field_1687.method_8393(cx, cz)) continue;
                class_2818 chunk = KeyHunterModule.mc.field_1687.method_8497(cx, cz);
                for (Map.Entry entry : chunk.method_12214().entrySet()) {
                    double dist;
                    class_2338 pos;
                    class_2586 be = (class_2586)entry.getValue();
                    if (!(be instanceof class_2595) && !(be instanceof class_2646) || this.visitedChests.contains(pos = (class_2338)entry.getKey()) || !((dist = center.method_10262((class_2382)pos)) <= (double)r * (double)r)) continue;
                    this.nearChests.add(pos);
                }
            }
        }
        KeyHunterModule.mc.field_1687.method_8390(class_1694.class, new class_238(center).method_1014((double)r), e -> !this.visitedCarts.contains(e.method_5628())).forEach(e -> this.nearCarts.add(e.method_5628()));
        int total = this.nearChests.size() + this.nearCarts.size();
        if (total < ((Float)this.minChests.getValue()).intValue()) {
            TextUtil.sendMessage("\u00a7eKeyHunter: \u043c\u0430\u043b\u043e \u0441\u0443\u043d\u0434\u0443\u043a\u043e\u0432 \u0432 \u0437\u043e\u043d\u0435 (" + total + "), \u0438\u0449\u0443 \u0434\u0440\u0443\u0433\u0443\u044e \u0442\u0435\u0440\u0440\u0438\u0442\u043e\u0440\u0438\u044e \u0447\u0435\u0440\u0435\u0437 /rtp s.");
            this.forceRtp();
            return;
        }
        class_2338 nearest = this.findNearestChest();
        if (nearest != null) {
            double dist = Math.sqrt(center.method_10262((class_2382)nearest));
            if (dist > (double)((Float)this.maxChestDist.getValue()).floatValue()) {
                TextUtil.sendMessage("\u00a7eKeyHunter: \u0431\u043b\u0438\u0436\u0430\u0439\u0448\u0438\u0439 \u0441\u0443\u043d\u0434\u0443\u043a \u0441\u043b\u0438\u0448\u043a\u043e\u043c \u0434\u0430\u043b\u0435\u043a\u043e (" + (int)dist + " \u0431\u043b\u043e\u043a\u043e\u0432), /rtp s.");
                this.forceRtp();
                return;
            }
            TextUtil.sendMessage("\u00a7aKeyHunter: \u043d\u0430\u0439\u0434\u0435\u043d \u0441\u0443\u043d\u0434\u0443\u043a \u043d\u0430 \u0440\u0430\u0441\u0441\u0442\u043e\u044f\u043d\u0438\u0438 " + (int)dist + " \u0431\u043b\u043e\u043a\u043e\u0432. \u041d\u0430\u0447\u0438\u043d\u0430\u044e \u0434\u0432\u0438\u0436\u0435\u043d\u0438\u0435.");
            this.targetChest = nearest;
            this.targetIsCart = false;
            this.targetLocked = true;
            this.startPathing(nearest);
            this.state = State.PATHING;
        } else if (!this.nearCarts.isEmpty()) {
            TextUtil.sendMessage("\u00a7aKeyHunter: \u043d\u0430\u0439\u0434\u0435\u043d \u0441\u0443\u043d\u0434\u0443\u043a \u0432 \u0432\u0430\u0433\u043e\u043d\u0435\u0442\u043a\u0435. \u041d\u0430\u0447\u0438\u043d\u0430\u044e \u0434\u0432\u0438\u0436\u0435\u043d\u0438\u0435.");
            this.targetCartId = this.nearCarts.get(0);
            this.targetIsCart = true;
            this.targetLocked = true;
            class_1694 cart = this.getCartById(this.targetCartId);
            if (cart != null) {
                this.startPathing(cart.method_24515());
                this.state = State.PATHING;
            }
        } else {
            this.forceRtp();
        }
    }

    private void doLoot() {
        class_465 screen;
        if (System.currentTimeMillis() < this.lootTimer) {
            return;
        }
        class_437 class_4372 = KeyHunterModule.mc.field_1755;
        if (class_4372 instanceof class_465 && (class_4372 = (screen = (class_465)class_4372).method_17577()) instanceof class_1707) {
            class_1707 handler = (class_1707)class_4372;
            int chestSize = handler.method_17388() * 9;
            if (System.currentTimeMillis() < this.lootSlotTimer) {
                return;
            }
            while (this.lootSlotIndex < chestSize) {
                class_1799 stack = handler.method_7611(this.lootSlotIndex).method_7677();
                if (!stack.method_7960() && this.isWanted(stack)) {
                    this.countItem(stack);
                    int slotToClick = this.lootSlotIndex;
                    if (SlownessManager.isEnabled()) {
                        SlownessManager.applySlowness(80L, 30L, () -> KeyHunterModule.mc.field_1761.method_2906(handler.field_7763, slotToClick, 0, class_1713.field_7794, (class_1657)KeyHunterModule.mc.field_1724));
                    } else {
                        KeyHunterModule.mc.field_1761.method_2906(handler.field_7763, slotToClick, 0, class_1713.field_7794, (class_1657)KeyHunterModule.mc.field_1724);
                    }
                    ++this.lootSlotIndex;
                    this.lootSlotTimer = System.currentTimeMillis() + 60L;
                    return;
                }
                ++this.lootSlotIndex;
            }
            this.lootSlotIndex = 0;
            KeyHunterModule.mc.field_1724.method_7346();
            if (this.targetIsCart) {
                this.visitedCarts.add(this.targetCartId);
            } else if (this.targetChest != null) {
                this.visitedChests.add(this.targetChest);
            }
            this.targetLocked = false;
            TextUtil.sendMessage("\u00a7aKeyHunter: \u043b\u0443\u0442 \u0437\u0430\u0432\u0435\u0440\u0448\u0451\u043d. \u0418\u0449\u0443 \u0441\u043b\u0435\u0434\u0443\u044e\u0449\u0438\u0439 \u0441\u0443\u043d\u0434\u0443\u043a.");
            this.state = State.SCANNING;
            this.scanTimer = 0L;
        } else if (System.currentTimeMillis() > this.lootTimer + 3000L) {
            this.lootSlotIndex = 0;
            if (this.targetIsCart) {
                this.visitedCarts.add(this.targetCartId);
            } else if (this.targetChest != null) {
                this.visitedChests.add(this.targetChest);
            }
            this.targetLocked = false;
            this.state = State.SCANNING;
            this.scanTimer = 0L;
        } else if (this.targetIsCart) {
            class_1694 cart = this.getCartById(this.targetCartId);
            if (cart != null) {
                this.lookAt(cart.method_19538());
                this.interactCart(cart);
            }
        } else if (this.targetChest != null) {
            this.lookAt(class_243.method_24953((class_2382)this.targetChest));
            this.interactChest(this.targetChest);
        }
    }

    private void doEat() {
        if (KeyHunterModule.mc.field_1724 == null) {
            class_304.method_1416((class_3675.class_306)KeyHunterModule.mc.field_1690.field_1904.method_1429(), (boolean)false);
            this.eatActive = false;
            this.state = State.SCANNING;
            return;
        }
        if (KeyHunterModule.mc.field_1724.method_7344().method_7586() >= ((Float)this.eatUntil.getValue()).intValue()) {
            if (this.eatActive && KeyHunterModule.mc.field_1724.method_6115()) {
                KeyHunterModule.mc.field_1761.method_2897((class_1657)KeyHunterModule.mc.field_1724);
            }
            class_304.method_1416((class_3675.class_306)KeyHunterModule.mc.field_1690.field_1904.method_1429(), (boolean)false);
            this.eatActive = false;
            TextUtil.sendMessage("\u00a7aKeyHunter: \u0433\u043e\u043b\u043e\u0434 \u0432\u043e\u0441\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d, \u043f\u0440\u043e\u0434\u043e\u043b\u0436\u0430\u044e \u0440\u0430\u0431\u043e\u0442\u0443.");
            this.state = State.SCANNING;
            return;
        }
        int foodSlot = this.findFoodSlot();
        if (foodSlot == -1) {
            class_304.method_1416((class_3675.class_306)KeyHunterModule.mc.field_1690.field_1904.method_1429(), (boolean)false);
            this.eatActive = false;
            TextUtil.sendMessage("\u00a7eKeyHunter: \u0435\u0434\u0430 \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u0430, \u043f\u0440\u043e\u0434\u043e\u043b\u0436\u0430\u044e \u0440\u0430\u0431\u043e\u0442\u0443.");
            this.state = State.SCANNING;
            return;
        }
        KeyHunterModule.mc.field_1724.method_31548().field_7545 = foodSlot;
        if (!this.eatActive) {
            this.eatActive = true;
            KeyHunterModule.mc.field_1761.method_2919((class_1657)KeyHunterModule.mc.field_1724, class_1268.field_5808);
            class_304.method_1416((class_3675.class_306)KeyHunterModule.mc.field_1690.field_1904.method_1429(), (boolean)true);
            KeyHunterModule.mc.field_1724.method_6019(class_1268.field_5808);
        } else if (!KeyHunterModule.mc.field_1724.method_6115()) {
            KeyHunterModule.mc.field_1761.method_2919((class_1657)KeyHunterModule.mc.field_1724, class_1268.field_5808);
            class_304.method_1416((class_3675.class_306)KeyHunterModule.mc.field_1690.field_1904.method_1429(), (boolean)true);
            KeyHunterModule.mc.field_1724.method_6019(class_1268.field_5808);
        }
    }

    private void doFleeStaff() {
        if (System.currentTimeMillis() < this.commandDelay) {
            return;
        }
        this.cancelBaritone();
        if (KeyHunterModule.mc.field_1724 != null) {
            KeyHunterModule.mc.field_1724.field_3944.method_45730("hub");
        }
        this.hubSentTime = System.currentTimeMillis();
        this.state = State.WAITING_HUB;
    }

    private void doWaitHub() {
        if (System.currentTimeMillis() - this.hubSentTime >= 300000L) {
            TextUtil.sendMessage("\u00a7aKeyHunter: 5 \u043c\u0438\u043d\u0443\u0442 \u043f\u0440\u043e\u0448\u043b\u043e, \u0432\u043e\u0437\u0432\u0440\u0430\u0449\u0430\u044e\u0441\u044c \u043d\u0430 \u0441\u0435\u0440\u0432\u0435\u0440 \u0447\u0435\u0440\u0435\u0437 /srv.");
            this.cancelBaritone();
            if (KeyHunterModule.mc.field_1724 != null) {
                KeyHunterModule.mc.field_1724.field_3944.method_45730("srv " + (String)this.serverCmd.getValue());
            }
            this.state = State.SCANNING;
            this.visitedChests.clear();
            this.visitedCarts.clear();
            this.targetLocked = false;
            this.scanTimer = 0L;
        }
    }

    private void doFleePlayer() {
        if (System.currentTimeMillis() < this.commandDelay) {
            return;
        }
        this.cancelBaritone();
        if (KeyHunterModule.mc.field_1724 != null) {
            KeyHunterModule.mc.field_1724.field_3944.method_45730("rtp s");
        }
        this.rtpCooldownEnd = System.currentTimeMillis() + 9000L;
        this.visitedChests.clear();
        this.visitedCarts.clear();
        this.targetLocked = false;
        this.scanTimer = System.currentTimeMillis() + 5000L;
        this.state = State.RTP_COOLDOWN;
    }

    private void doRtpCooldown() {
        if (System.currentTimeMillis() >= this.rtpCooldownEnd) {
            TextUtil.sendMessage("\u00a7aKeyHunter: \u043a\u0443\u043b\u0434\u0430\u0443\u043d /rtp s \u0438\u0441\u0442\u0451\u043a, \u043d\u0430\u0447\u0438\u043d\u0430\u044e \u0441\u043a\u0430\u043d\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u0435 \u043d\u043e\u0432\u043e\u0439 \u0442\u0435\u0440\u0440\u0438\u0442\u043e\u0440\u0438\u0438.");
            this.state = State.SCANNING;
            this.visitedChests.clear();
            this.visitedCarts.clear();
            this.targetLocked = false;
            this.scanTimer = 0L;
        }
    }

    private void doGoHome() {
        if (System.currentTimeMillis() < this.commandDelay) {
            return;
        }
        this.cancelBaritone();
        if (KeyHunterModule.mc.field_1724 != null) {
            KeyHunterModule.mc.field_1724.field_3944.method_45730("home default");
        }
        this.homeChestPos = null;
        this.knownHomeChests.clear();
        this.commandDelay = System.currentTimeMillis() + 5000L;
        this.state = State.STORING;
    }

    private void doStore() {
        class_465 screen;
        if (KeyHunterModule.mc.field_1724 == null) {
            this.state = State.SCANNING;
            return;
        }
        if (System.currentTimeMillis() < this.commandDelay) {
            return;
        }
        if (this.homeChestPos == null) {
            this.scanHomeChests();
            if (this.homeChestPos == null) {
                this.state = State.SCANNING;
                return;
            }
        }
        if (Math.sqrt(KeyHunterModule.mc.field_1724.method_24515().method_10262((class_2382)this.homeChestPos)) > 3.5) {
            if (!this.isBaritonePathing()) {
                TextUtil.sendMessage("\u00a7eKeyHunter: \u0438\u0434\u0443 \u043a \u0434\u043e\u043c\u0430\u0448\u043d\u0435\u043c\u0443 \u0441\u0443\u043d\u0434\u0443\u043a\u0443 \u0434\u043b\u044f \u0432\u044b\u0433\u0440\u0443\u0437\u043a\u0438 \u043b\u0443\u0442\u0430.");
                this.startPathing(this.homeChestPos);
            }
            return;
        }
        class_437 class_4372 = KeyHunterModule.mc.field_1755;
        if (class_4372 instanceof class_465 && (class_4372 = (screen = (class_465)class_4372).method_17577()) instanceof class_1707) {
            class_1707 handler = (class_1707)class_4372;
            if (this.isChestFull(handler)) {
                KeyHunterModule.mc.field_1724.method_7346();
                this.homeChestPos = this.findNextHomeChest();
                if (this.homeChestPos == null) {
                    TextUtil.sendMessage("\u00a7eKeyHunter: \u0432\u0441\u0435 \u0434\u043e\u043c\u0430\u0448\u043d\u0438\u0435 \u0441\u0443\u043d\u0434\u0443\u043a\u0438 \u0437\u0430\u043f\u043e\u043b\u043d\u0435\u043d\u044b, \u043f\u0440\u043e\u0434\u043e\u043b\u0436\u0430\u044e \u0440\u0430\u0431\u043e\u0442\u0443.");
                    this.state = State.SCANNING;
                    return;
                }
                this.startPathing(this.homeChestPos);
                return;
            }
            int chestSize = handler.method_17388() * 9;
            block0: for (int i = 9; i < handler.field_7761.size(); ++i) {
                class_1799 stack = handler.method_7611(i).method_7677();
                if (stack.method_7960() || !this.isWanted(stack)) continue;
                for (int j = 0; j < chestSize; ++j) {
                    if (!handler.method_7611(j).method_7677().method_7960()) continue;
                    KeyHunterModule.mc.field_1761.method_2906(handler.field_7763, i, 0, class_1713.field_7794, (class_1657)KeyHunterModule.mc.field_1724);
                    continue block0;
                }
            }
            KeyHunterModule.mc.field_1724.method_7346();
            TextUtil.sendMessage("\u00a7aKeyHunter: \u043b\u0443\u0442 \u0432\u044b\u0433\u0440\u0443\u0436\u0435\u043d \u0432 \u0434\u043e\u043c\u0430\u0448\u043d\u0438\u0439 \u0441\u0443\u043d\u0434\u0443\u043a. \u0412\u043e\u0437\u0432\u0440\u0430\u0449\u0430\u044e\u0441\u044c \u0432 \u0448\u0430\u0445\u0442\u044b.");
            this.state = State.SCANNING;
            this.scanTimer = 0L;
        } else {
            this.lookAt(class_243.method_24953((class_2382)this.homeChestPos));
            this.interactChest(this.homeChestPos);
        }
    }

    private void forceRtp() {
        this.cancelBaritone();
        if (KeyHunterModule.mc.field_1724 != null) {
            KeyHunterModule.mc.field_1724.field_3944.method_45730("rtp s");
        }
        this.rtpCooldownEnd = System.currentTimeMillis() + 9000L;
        this.state = State.RTP_COOLDOWN;
        this.visitedChests.clear();
        this.visitedCarts.clear();
        this.targetLocked = false;
        this.scanTimer = System.currentTimeMillis() + 5000L;
    }

    private void startPathing(class_2338 pos) {
        this.applyBaritoneSettings();
        IBaritone b = BaritoneAPI.getProvider().getPrimaryBaritone();
        b.getCustomGoalProcess().setGoalAndPath((Goal)new GoalNear(pos, 2));
        this.lastPos = KeyHunterModule.mc.field_1724 != null ? KeyHunterModule.mc.field_1724.method_24515() : pos;
        this.stuckTimer = System.currentTimeMillis();
        this.pathTimeout = System.currentTimeMillis() + 30000L;
    }

    private void applyBaritoneSettings() {
        BaritoneAPI.getSettings().walkOnWaterOnePenalty.value = (Boolean)this.landPriority.getValue() != false ? 999.0 : 3.0;
        BaritoneAPI.getSettings().allowWaterBucketFall.value = false;
        BaritoneAPI.getSettings().avoidance.value = true;
        BaritoneAPI.getSettings().mobAvoidanceCoefficient.value = 0.2;
        BaritoneAPI.getSettings().mobAvoidanceRadius.value = 5;
        BaritoneAPI.getSettings().mobSpawnerAvoidanceCoefficient.value = 0.1;
        BaritoneAPI.getSettings().mobSpawnerAvoidanceRadius.value = 4;
        BaritoneAPI.getSettings().blocksToAvoidBreaking.value = new ArrayList<class_2248>(Arrays.asList(class_2246.field_10597, class_2246.field_28675, class_2246.field_28676, class_2246.field_23078, class_2246.field_23079, class_2246.field_22123, class_2246.field_22124, class_2246.field_10382, class_2246.field_27097));
        BaritoneAPI.getSettings().sprintInWater.value = false;
    }

    private void resetBaritoneSettings() {
        BaritoneAPI.getSettings().walkOnWaterOnePenalty.value = 3.0;
        BaritoneAPI.getSettings().allowWaterBucketFall.value = true;
        BaritoneAPI.getSettings().avoidance.value = false;
        BaritoneAPI.getSettings().mobAvoidanceCoefficient.value = 1.5;
        BaritoneAPI.getSettings().mobAvoidanceRadius.value = 8;
        BaritoneAPI.getSettings().mobSpawnerAvoidanceCoefficient.value = 2.0;
        BaritoneAPI.getSettings().mobSpawnerAvoidanceRadius.value = 16;
        BaritoneAPI.getSettings().blocksToAvoidBreaking.value = new ArrayList<class_2248>(Arrays.asList(class_2246.field_9980, class_2246.field_10181, class_2246.field_10034, class_2246.field_10380));
        BaritoneAPI.getSettings().sprintInWater.value = true;
    }

    private boolean isStuck() {
        if (KeyHunterModule.mc.field_1724 == null) {
            return false;
        }
        class_2338 current = KeyHunterModule.mc.field_1724.method_24515();
        if (this.lastPos == null) {
            this.lastPos = current;
            this.stuckTimer = System.currentTimeMillis();
            return false;
        }
        if (System.currentTimeMillis() - this.stuckTimer > 30000L) {
            double distanceMoved = Math.sqrt(current.method_10262((class_2382)this.lastPos));
            if (distanceMoved <= 4.0) {
                return true;
            }
            this.stuckTimer = System.currentTimeMillis();
            this.lastPos = current;
        }
        return false;
    }

    private boolean isBaritonePathing() {
        return BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().isActive();
    }

    private void cancelBaritone() {
        BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().cancelEverything();
    }

    private void ensureBestWeapon() {
        if (KeyHunterModule.mc.field_1724 == null) {
            return;
        }
        int bestSlot = -1;
        int bestTier = -1;
        for (int i = 0; i < 9; ++i) {
            int tier;
            class_1799 s = KeyHunterModule.mc.field_1724.method_31548().method_5438(i);
            if (s.method_7960() || (tier = this.getWeaponTier(s.method_7909())) <= bestTier) continue;
            bestTier = tier;
            bestSlot = i;
        }
        if (bestSlot != -1 && KeyHunterModule.mc.field_1724.method_31548().field_7545 != bestSlot) {
            InventoryUtil.swapToSlot(bestSlot);
        }
    }

    private int getWeaponTier(class_1792 item) {
        if (item == class_1802.field_22022) {
            return 10;
        }
        if (item == class_1802.field_8802) {
            return 9;
        }
        if (item == class_1802.field_8371) {
            return 8;
        }
        if (item == class_1802.field_8528) {
            return 7;
        }
        if (item == class_1802.field_8845) {
            return 6;
        }
        if (item == class_1802.field_8091) {
            return 5;
        }
        if (item == class_1802.field_22025) {
            return 4;
        }
        if (item == class_1802.field_8556) {
            return 3;
        }
        if (item == class_1802.field_8475) {
            return 2;
        }
        return 0;
    }

    private void interactChest(class_2338 pos) {
        if (KeyHunterModule.mc.field_1724 == null || KeyHunterModule.mc.field_1687 == null) {
            return;
        }
        KeyHunterModule.mc.field_1761.method_2896(KeyHunterModule.mc.field_1724, class_1268.field_5808, new class_3965(class_243.method_24953((class_2382)pos), class_2350.field_11036, pos, false));
    }

    private void interactCart(class_1694 cart) {
        if (KeyHunterModule.mc.field_1724 == null) {
            return;
        }
        KeyHunterModule.mc.field_1761.method_2905((class_1657)KeyHunterModule.mc.field_1724, (class_1297)cart, class_1268.field_5808);
    }

    private boolean isWanted(class_1799 stack) {
        if (stack.method_7960()) {
            return false;
        }
        class_1792 item = stack.method_7909();
        if (((Boolean)this.takeKeys.getValue()).booleanValue() && item == class_1802.field_8600) {
            return true;
        }
        if (((Boolean)this.takeEGapples.getValue()).booleanValue() && item == class_1802.field_8367) {
            return true;
        }
        if (((Boolean)this.takeGapples.getValue()).booleanValue() && item == class_1802.field_8463) {
            return true;
        }
        return (Boolean)this.takeBooks.getValue() != false && item == class_1802.field_8598 && stack.method_57824(class_9334.field_49643) != null && !((class_9304)stack.method_57824(class_9334.field_49643)).method_57543();
    }

    private void countItem(class_1799 stack) {
        class_1792 item = stack.method_7909();
        if (item == class_1802.field_8600) {
            this.keysLooted += stack.method_7947();
        } else if (item == class_1802.field_8367) {
            this.egapplesLooted += stack.method_7947();
        } else if (item == class_1802.field_8463) {
            this.gapplesLooted += stack.method_7947();
        } else if (item == class_1802.field_8598) {
            this.booksLooted += stack.method_7947();
        }
    }

    private boolean shouldGoHome() {
        if (KeyHunterModule.mc.field_1724 == null) {
            return false;
        }
        int free = 0;
        for (int i = 0; i < 36; ++i) {
            if (!KeyHunterModule.mc.field_1724.method_31548().method_5438(i).method_7960()) continue;
            ++free;
        }
        return free <= 1;
    }

    private int findFoodSlot() {
        class_1799 s;
        int i;
        if (KeyHunterModule.mc.field_1724 == null) {
            return -1;
        }
        for (i = 0; i < 9; ++i) {
            s = KeyHunterModule.mc.field_1724.method_31548().method_5438(i);
            if (s.method_7960() || s.method_57824(class_9334.field_50075) == null) continue;
            return i;
        }
        for (i = 9; i < 36; ++i) {
            s = KeyHunterModule.mc.field_1724.method_31548().method_5438(i);
            if (s.method_7960() || s.method_57824(class_9334.field_50075) == null) continue;
            KeyHunterModule.mc.field_1724.method_31548().field_7545 = 0;
            KeyHunterModule.mc.field_1761.method_2906(KeyHunterModule.mc.field_1724.field_7512.field_7763, i, 0, class_1713.field_7791, (class_1657)KeyHunterModule.mc.field_1724);
            return 0;
        }
        return -1;
    }

    private class_2338 findNearestChest() {
        if (KeyHunterModule.mc.field_1724 == null || this.nearChests.isEmpty()) {
            return null;
        }
        return this.nearChests.stream().min(Comparator.comparingDouble(p -> KeyHunterModule.mc.field_1724.method_24515().method_10262((class_2382)p))).orElse(null);
    }

    private class_1694 getCartById(int id) {
        if (KeyHunterModule.mc.field_1687 == null) {
            return null;
        }
        class_1297 class_12972 = KeyHunterModule.mc.field_1687.method_8469(id);
        if (class_12972 instanceof class_1694) {
            class_1694 cart = (class_1694)class_12972;
            return cart;
        }
        return null;
    }

    private boolean isStaff(class_1657 p) {
        StaffsWidget sw = this.getStaffsWidget();
        if (sw == null) {
            return false;
        }
        String name = p.method_7334().getName();
        return sw.getStaffList().stream().anyMatch(s -> s.name().split(":")[0].equalsIgnoreCase(name));
    }

    private StaffsWidget getStaffsWidget() {
        for (Widget w : WidgetManager.getInstance().getWidgets()) {
            if (!(w instanceof StaffsWidget)) continue;
            StaffsWidget sw = (StaffsWidget)w;
            return sw;
        }
        return null;
    }

    private void scanHomeChests() {
        if (KeyHunterModule.mc.field_1724 == null) {
            return;
        }
        this.knownHomeChests.clear();
        class_2338 center = KeyHunterModule.mc.field_1724.method_24515();
        for (int x = -8; x <= 8; ++x) {
            for (int y = -4; y <= 4; ++y) {
                for (int z = -8; z <= 8; ++z) {
                    class_2338 pos = center.method_10069(x, y, z);
                    class_2586 be = KeyHunterModule.mc.field_1687.method_8321(pos);
                    if (!(be instanceof class_2595)) continue;
                    this.knownHomeChests.add(pos);
                }
            }
        }
        if (!this.knownHomeChests.isEmpty()) {
            this.homeChestPos = this.knownHomeChests.get(0);
        }
    }

    private class_2338 findNextHomeChest() {
        for (class_2338 pos : this.knownHomeChests) {
            if (pos.equals((Object)this.homeChestPos)) continue;
            return pos;
        }
        return null;
    }

    private boolean isChestFull(class_1707 handler) {
        int size = handler.method_17388() * 9;
        for (int i = 0; i < size; ++i) {
            if (!handler.method_7611(i).method_7677().method_7960()) continue;
            return false;
        }
        return true;
    }

    private static String strip(String s) {
        if (s == null) {
            return "";
        }
        return s.replaceAll("(?i)[\u00a7&][0-9a-fk-or]", "").trim();
    }

    @Generated
    public static KeyHunterModule getInstance() {
        return instance;
    }

    private static enum State {
        SCANNING,
        PATHING,
        FIGHTING_MOBS,
        LOOTING,
        EATING,
        FLEEING_STAFF,
        WAITING_HUB,
        FLEEING_PLAYER,
        RTP_COOLDOWN,
        GOING_HOME,
        STORING,
        FLEEING_CREEPER;

    }
}

