package sweetie.leonware.client.features.modules.player;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
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
import net.minecraft.class_1548;
import net.minecraft.class_1588;
import net.minecraft.class_1657;
import net.minecraft.class_1694;
import net.minecraft.class_1703;
import net.minecraft.class_1707;
import net.minecraft.class_1713;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2586;
import net.minecraft.class_2595;
import net.minecraft.class_2646;
import net.minecraft.class_2818;
import net.minecraft.class_304;
import net.minecraft.class_3532;
import net.minecraft.class_3965;
import net.minecraft.class_4587;
import net.minecraft.class_465;
import net.minecraft.class_7439;
import net.minecraft.class_746;
import net.minecraft.class_9304;
import net.minecraft.class_9334;
import org.newsclub.net.unix.AFVSOCKSocketAddress;
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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/player/KeyHunterModule.class */
@ModuleRegister(name = "Key Hunter", category = Category.PLAYER)
public class KeyHunterModule extends Module {
    private static final KeyHunterModule instance = new KeyHunterModule();
    private final SliderSetting searchRadius = new SliderSetting("Радиус поиска").value(Float.valueOf(160.0f)).range(50.0f, 400.0f).step(10.0f);
    private final SliderSetting playerFleeRadius = new SliderSetting("Радиус игроков").value(Float.valueOf(25.0f)).range(10.0f, 80.0f).step(5.0f);
    private final SliderSetting staffFleeRadius = new SliderSetting("Радиус стафа").value(Float.valueOf(50.0f)).range(10.0f, 100.0f).step(5.0f);
    private final SliderSetting minChests = new SliderSetting("Мин. сундуков в зоне").value(Float.valueOf(3.0f)).range(1.0f, 15.0f).step(1.0f);
    private final SliderSetting maxChestDist = new SliderSetting("Макс. дистанция до сундука").value(Float.valueOf(200.0f)).range(50.0f, 500.0f).step(10.0f);
    private final BooleanSetting autoEat = new BooleanSetting("Авто еда").value((Boolean) true);
    private final SliderSetting eatThreshold = new SliderSetting("Есть при голоде").value(Float.valueOf(14.0f)).range(1.0f, 20.0f).step(1.0f).setVisible(() -> {
        return this.autoEat.getValue();
    });
    private final SliderSetting eatUntil = new SliderSetting("Есть до голода").value(Float.valueOf(18.0f)).range(1.0f, 20.0f).step(1.0f).setVisible(() -> {
        return this.autoEat.getValue();
    });
    private final ModeSetting serverCmd = new ModeSetting("Сервер возврата").values("grief", "grief2").value("grief");
    private final BooleanSetting takeKeys = new BooleanSetting("Брать ключи (палки)").value((Boolean) true);
    private final BooleanSetting takeEGapples = new BooleanSetting("Брать зач. яблоки").value((Boolean) true);
    private final BooleanSetting takeGapples = new BooleanSetting("Брать золотые яблоки").value((Boolean) true);
    private final BooleanSetting takeBooks = new BooleanSetting("Брать зач. книги").value((Boolean) true);
    private final BooleanSetting landPriority = new BooleanSetting("Приоритет суши").value((Boolean) true);
    private final SliderSetting mobFightRadius = new SliderSetting("Радиус убийства мобов").value(Float.valueOf(6.0f)).range(2.0f, 16.0f).step(1.0f);
    private final BooleanSetting mobThroughWalls = new BooleanSetting("Мобы через стены").value((Boolean) false);
    private final SliderSetting creeperRadius = new SliderSetting("Радиус крипера").value(Float.valueOf(8.0f)).range(3.0f, 20.0f).step(1.0f);
    private final BooleanSetting creeperThroughWalls = new BooleanSetting("Криперы через стены").value((Boolean) false);
    private final SliderSetting weaponSwitchRange = new SliderSetting("Дистанция смены оружия").value(Float.valueOf(5.0f)).range(2.0f, 12.0f).step(1.0f);
    private final BooleanSetting autoStorage = new BooleanSetting("Авто выгрузка лута").value((Boolean) true);
    private State state = State.SCANNING;
    private class_2338 targetChest = null;
    private int targetCartId = -1;
    private boolean targetIsCart = false;
    private boolean targetLocked = false;
    private final Set<class_2338> visitedChests = new HashSet();
    private final Set<Integer> visitedCarts = new HashSet();
    private final List<class_2338> nearChests = new ArrayList();
    private final List<Integer> nearCarts = new ArrayList();
    private long hubSentTime = 0;
    private long rtpCooldownEnd = 0;
    private long pathTimeout = 0;
    private long stuckTimer = 0;
    private long lootTimer = 0;
    private long scanTimer = 0;
    private long commandDelay = 0;
    private long fightTimer = 0;
    private class_2338 lastPos = null;
    private int lootSlotIndex = 0;
    private long lootSlotTimer = 0;
    private boolean eatActive = false;
    private int keysLooted = 0;
    private int egapplesLooted = 0;
    private int gapplesLooted = 0;
    private int booksLooted = 0;
    private int staffFlees = 0;
    private int playerFlees = 0;
    private int creeperFlees = 0;
    private class_2338 homeChestPos = null;
    private final List<class_2338> knownHomeChests = new ArrayList();
    private final Random random = new Random();
    private long lastRandomLookTime = 0;

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/player/KeyHunterModule$State.class */
    private enum State {
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
        FLEEING_CREEPER
    }

    @Generated
    public static KeyHunterModule getInstance() {
        return instance;
    }

    /* JADX WARN: Type inference failed for: r1v26, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v31, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    public KeyHunterModule() {
        addSettings(this.searchRadius, this.playerFleeRadius, this.staffFleeRadius, this.minChests, this.maxChestDist, this.autoEat, this.eatThreshold, this.eatUntil, this.serverCmd, this.takeKeys, this.takeEGapples, this.takeGapples, this.takeBooks, this.landPriority, this.mobFightRadius, this.mobThroughWalls, this.creeperRadius, this.creeperThroughWalls, this.weaponSwitchRange, this.autoStorage);
    }

    @Override // sweetie.leonware.api.module.Module
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
        applyBaritoneSettings();
        TextUtil.sendMessage("§aKeyHunter: модуль включён. Начинаю сканирование шахт.");
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        cancelBaritone();
        resetBaritoneSettings();
        class_304.method_1416(mc.field_1690.field_1904.method_1429(), false);
        this.eatActive = false;
        if (AuraModule.getInstance().isEnabled()) {
            AuraModule.getInstance().disable();
        }
        TextUtil.sendMessage("§cKeyHunter: модуль выключён.");
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        addEvents(UpdateEvent.getInstance().subscribe(new Listener(event -> {
            tick();
        })), PacketEvent.getInstance().subscribe(new Listener(event2 -> {
            if (event2.isReceive()) {
                class_7439 class_7439VarPacket = event2.packet();
                if (class_7439VarPacket instanceof class_7439) {
                    class_7439 p = class_7439VarPacket;
                    String msg = strip(p.comp_763().getString());
                    if (msg.contains("Вы не можете ломать в этом регионе") || msg.contains("Вы не можете взаимодействовать в этом регионе")) {
                        cancelBaritone();
                        if (this.targetChest != null) {
                            this.visitedChests.add(this.targetChest);
                        }
                        if (this.targetIsCart) {
                            this.visitedCarts.add(Integer.valueOf(this.targetCartId));
                        }
                        this.targetLocked = false;
                        TextUtil.sendMessage("§eKeyHunter: регион приватен, ищу другое место через /rtp s.");
                        forceRtp();
                    }
                }
            }
        })), Render2DEvent.getInstance().subscribe(new Listener(event3 -> {
            renderHud(event3.matrixStack());
        })));
    }

    private void tick() {
        int food;
        if (mc.field_1724 == null || mc.field_1687 == null) {
            return;
        }
        applyRandomLook();
        if (this.autoEat.getValue().booleanValue() && this.state != State.EATING && this.state != State.FLEEING_STAFF && this.state != State.FLEEING_PLAYER && this.state != State.FLEEING_CREEPER && this.state != State.WAITING_HUB && (food = mc.field_1724.method_7344().method_7586()) <= this.eatThreshold.getValue().intValue()) {
            cancelBaritone();
            class_304.method_1416(mc.field_1690.field_1904.method_1429(), false);
            this.eatActive = false;
            if (AuraModule.getInstance().isEnabled()) {
                AuraModule.getInstance().disable();
            }
            TextUtil.sendMessage("§eKeyHunter: голод " + food + " <= " + this.eatThreshold.getValue().intValue() + ", начинаю есть.");
            this.state = State.EATING;
        }
        boolean needsMobs = hasMobsNearby();
        if (needsMobs) {
            if (!AuraModule.getInstance().isEnabled()) {
                AuraModule.getInstance().enable();
            }
        } else if (AuraModule.getInstance().isEnabled() && this.state != State.FIGHTING_MOBS) {
            AuraModule.getInstance().disable();
        }
        if (this.state == State.FLEEING_STAFF || this.state == State.WAITING_HUB || this.state == State.FLEEING_PLAYER || this.state == State.FLEEING_CREEPER || !(checkStaff() || checkPlayers() || checkCreeper())) {
            if ((this.state == State.PATHING || this.state == State.SCANNING || this.state == State.FIGHTING_MOBS) && checkMobs()) {
                return;
            }
            switch (this.state.ordinal()) {
                case 0:
                    doScan();
                    break;
                case 1:
                    doPath();
                    break;
                case AFVSOCKSocketAddress.VMADDR_CID_HOST /* 2 */:
                    doFightMobs();
                    break;
                case 3:
                    doLoot();
                    break;
                case 4:
                    doEat();
                    break;
                case 5:
                    doFleeStaff();
                    break;
                case 6:
                    doWaitHub();
                    break;
                case 7:
                    doFleePlayer();
                    break;
                case 8:
                    doRtpCooldown();
                    break;
                case 9:
                    doGoHome();
                    break;
                case 10:
                    doStore();
                    break;
                case 11:
                    doFleeCreeper();
                    break;
            }
        }
    }

    private boolean checkCreeper() {
        if (mc.field_1724 == null || mc.field_1687 == null) {
            return false;
        }
        double r = this.creeperRadius.getValue().floatValue();
        class_238 box = mc.field_1724.method_5829().method_1014(r);
        List<class_1548> creepers = mc.field_1687.method_8390(class_1548.class, box, e -> {
            if (this.creeperThroughWalls.getValue().booleanValue()) {
                return true;
            }
            return mc.field_1724.method_6057(e);
        });
        if (!creepers.isEmpty()) {
            cancelBaritone();
            if (AuraModule.getInstance().isEnabled()) {
                AuraModule.getInstance().disable();
            }
            if (this.state != State.FLEEING_CREEPER) {
                this.creeperFlees++;
                TextUtil.sendMessage("§cKeyHunter: обнаружен крипер! Телепортируюсь через /rtp s.");
                this.state = State.FLEEING_CREEPER;
                this.commandDelay = System.currentTimeMillis() + 400;
                return true;
            }
            return true;
        }
        return false;
    }

    private void doFleeCreeper() {
        if (System.currentTimeMillis() < this.commandDelay) {
            return;
        }
        cancelBaritone();
        if (mc.field_1724 != null) {
            mc.field_1724.field_3944.method_45730("rtp s");
        }
        this.rtpCooldownEnd = System.currentTimeMillis() + 9000;
        this.visitedChests.clear();
        this.visitedCarts.clear();
        this.targetLocked = false;
        this.scanTimer = System.currentTimeMillis() + 5000;
        this.state = State.RTP_COOLDOWN;
    }

    private void doPath() {
        if (mc.field_1724 == null) {
            return;
        }
        if (this.autoStorage.getValue().booleanValue() && shouldGoHome()) {
            cancelBaritone();
            TextUtil.sendMessage("§eKeyHunter: инвентарь почти полон, иду домой складывать лут.");
            this.state = State.GOING_HOME;
            this.commandDelay = System.currentTimeMillis() + 400;
            return;
        }
        if (this.targetIsCart) {
            class_1694 cart = getCartById(this.targetCartId);
            if (cart == null) {
                this.visitedCarts.add(Integer.valueOf(this.targetCartId));
                this.targetLocked = false;
                this.state = State.SCANNING;
                return;
            } else {
                if (mc.field_1724.method_19538().method_1022(cart.method_19538()) <= 3.5d) {
                    cancelBaritone();
                    lookAt(cart.method_19538());
                    interactCart(cart);
                    this.state = State.LOOTING;
                    this.lootTimer = System.currentTimeMillis() + 500;
                    this.lootSlotIndex = 0;
                    TextUtil.sendMessage("§aKeyHunter: добрался до вагонетки, открываю и лутаю.");
                    return;
                }
                if (isStuck()) {
                    TextUtil.sendMessage("§eKeyHunter: застрял (меньше 4 блоков за 30 секунд), выполняю /rtp s.");
                    this.visitedCarts.add(Integer.valueOf(this.targetCartId));
                    this.targetLocked = false;
                    forceRtp();
                    return;
                }
            }
        } else {
            if (this.targetChest == null) {
                this.targetLocked = false;
                this.state = State.SCANNING;
                return;
            }
            if (Math.sqrt(mc.field_1724.method_24515().method_10262(this.targetChest)) <= 3.5d) {
                cancelBaritone();
                lookAt(class_243.method_24953(this.targetChest));
                interactChest(this.targetChest);
                this.state = State.LOOTING;
                this.lootTimer = System.currentTimeMillis() + 500;
                this.lootSlotIndex = 0;
                TextUtil.sendMessage("§aKeyHunter: добрался до сундука, открываю и лутаю.");
                return;
            }
            if (isStuck()) {
                TextUtil.sendMessage("§eKeyHunter: застрял (меньше 4 блоков за 30 секунд), выполняю /rtp s.");
                this.visitedChests.add(this.targetChest);
                this.targetLocked = false;
                forceRtp();
                return;
            }
        }
        if (!isBaritonePathing()) {
            if (!this.targetIsCart) {
                if (this.targetChest != null) {
                    startPathing(this.targetChest);
                }
            } else {
                class_1694 cart2 = getCartById(this.targetCartId);
                if (cart2 != null) {
                    startPathing(cart2.method_24515());
                }
            }
        }
    }

    private void doFightMobs() {
        if (mc.field_1724 == null || mc.field_1687 == null) {
            return;
        }
        double r = this.mobFightRadius.getValue().floatValue();
        class_238 box = mc.field_1724.method_5829().method_1014(r);
        List<class_1588> mobs = mc.field_1687.method_8390(class_1588.class, box, e -> {
            if ((e instanceof class_1548) || !e.method_5805()) {
                return false;
            }
            if (this.mobThroughWalls.getValue().booleanValue()) {
                return true;
            }
            return mc.field_1724.method_6057(e);
        });
        if (!mobs.isEmpty()) {
            if (System.currentTimeMillis() > this.fightTimer) {
                TextUtil.sendMessage("§eKeyHunter: таймер боя истёк, возвращаюсь к сканированию.");
                this.state = State.SCANNING;
                this.targetLocked = false;
                return;
            } else {
                class_1588 nearest = mobs.stream().min(Comparator.comparingDouble(e2 -> {
                    return mc.field_1724.method_5739(e2);
                })).orElse(null);
                if (nearest != null) {
                    lookAt(nearest.method_33571());
                }
                ensureBestWeapon();
                return;
            }
        }
        AuraModule.getInstance().disable();
        TextUtil.sendMessage("§aKeyHunter: все мобы уничтожены. Продолжаю движение к сундуку.");
        this.state = State.PATHING;
        if (this.targetIsCart) {
            class_1694 cart = getCartById(this.targetCartId);
            if (cart != null) {
                startPathing(cart.method_24515());
                return;
            }
            return;
        }
        if (this.targetChest != null) {
            startPathing(this.targetChest);
        } else {
            this.state = State.SCANNING;
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    private String stateLabel() throws MatchException {
        switch (this.state.ordinal()) {
            case 0:
                return "Поиск сундуков...";
            case 1:
                return "Движение к сундуку";
            case AFVSOCKSocketAddress.VMADDR_CID_HOST /* 2 */:
                return "Убиваю мобов...";
            case 3:
                return "Лутаю сундук";
            case 4:
                return "Ем еду...";
            case 5:
                return "Побег от стафа!";
            case 6:
                long left = ((this.hubSentTime + 300000) - System.currentTimeMillis()) / 1000;
                return "Хаб. Возврат через " + Math.max(0L, left) + "с";
            case 7:
                return "Побег от игрока!";
            case 8:
                long left2 = (this.rtpCooldownEnd - System.currentTimeMillis()) / 1000;
                return "RTP кулдаун: " + Math.max(0L, left2) + "с";
            case 9:
                return "Домой...";
            case 10:
                return "Складываю вещи";
            case 11:
                return "Побег от крипера!";
            default:
                throw new MatchException((String) null, (Throwable) null);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    private void renderHud(class_4587 ms) throws MatchException {
        if (mc.field_1724 == null || !isEnabled()) {
            return;
        }
        float cx = mc.method_22683().method_4486() / 2.0f;
        float cy = (mc.method_22683().method_4502() / 2.0f) + 20.0f;
        float lineH = 6.5f + 3.0f;
        String stateStr = stateLabel();
        String line1 = "Ключей: " + this.keysLooted;
        String line2 = "Зач. яблок: " + this.egapplesLooted;
        String line3 = "Золотых яблок: " + this.gapplesLooted;
        String line4 = "Зач. книг: " + this.booksLooted;
        String line5 = "Побег от стафа: " + this.staffFlees;
        String line6 = "Побег от игроков: " + this.playerFlees;
        String line7 = "Побег от криперов: " + this.creeperFlees;
        float maxW = 0.0f;
        for (String l : new String[]{stateStr, line1, line2, line3, line4, line5, line6, line7}) {
            maxW = Math.max(maxW, Fonts.PS_BOLD.getWidth(l, 6.5f));
        }
        float width = maxW + (5.0f * 2.0f);
        float height = (lineH * 8.0f) + (5.0f * 2.0f);
        float x = cx - (width / 2.0f);
        RenderUtil.BLUR_RECT.draw(ms, x, cy, width, height, 3.0f, UIColors.widgetBlur());
        float ty = cy + 5.0f;
        Fonts.PS_BOLD.drawGradientText(ms, stateStr, x + 5.0f, ty, 6.5f, UIColors.primary(), UIColors.secondary(), width / 4.0f);
        float ty2 = ty + lineH;
        Fonts.PS_BOLD.drawText(ms, line1, x + 5.0f, ty2, 6.5f, Color.WHITE);
        float ty3 = ty2 + lineH;
        Fonts.PS_BOLD.drawText(ms, line2, x + 5.0f, ty3, 6.5f, new Color(255, 215, 0));
        float ty4 = ty3 + lineH;
        Fonts.PS_BOLD.drawText(ms, line3, x + 5.0f, ty4, 6.5f, new Color(255, 165, 0));
        float ty5 = ty4 + lineH;
        Fonts.PS_BOLD.drawText(ms, line4, x + 5.0f, ty5, 6.5f, new Color(100, 180, 255));
        float ty6 = ty5 + lineH;
        Fonts.PS_BOLD.drawText(ms, line5, x + 5.0f, ty6, 6.5f, new Color(255, 80, 80));
        float ty7 = ty6 + lineH;
        Fonts.PS_BOLD.drawText(ms, line6, x + 5.0f, ty7, 6.5f, new Color(255, 140, 0));
        Fonts.PS_BOLD.drawText(ms, line7, x + 5.0f, ty7 + lineH, 6.5f, new Color(255, 100, 100));
    }

    private void applyRandomLook() {
        if (mc.field_1724 == null) {
            return;
        }
        long now = System.currentTimeMillis();
        if (now - this.lastRandomLookTime < 800 + this.random.nextInt(600)) {
            return;
        }
        if (this.state == State.PATHING || this.state == State.SCANNING) {
            this.lastRandomLookTime = now;
            float yawOffset = (this.random.nextFloat() - 0.5f) * 12.0f;
            float pitchOffset = (this.random.nextFloat() - 0.5f) * 6.0f;
            mc.field_1724.method_36456(mc.field_1724.method_36454() + yawOffset);
            mc.field_1724.method_36457(class_3532.method_15363(mc.field_1724.method_36455() + pitchOffset, -90.0f, 90.0f));
        }
    }

    private void lookAt(class_243 target) {
        if (mc.field_1724 == null) {
            return;
        }
        class_243 eyePos = mc.field_1724.method_33571();
        class_243 dir = target.method_1020(eyePos);
        double horizontalDist = Math.sqrt((dir.field_1352 * dir.field_1352) + (dir.field_1350 * dir.field_1350));
        float yaw = ((float) Math.toDegrees(Math.atan2(dir.field_1350, dir.field_1352))) - 90.0f;
        float pitch = (float) (-Math.toDegrees(Math.atan2(dir.field_1351, horizontalDist)));
        mc.field_1724.method_36456(yaw);
        mc.field_1724.method_36457(class_3532.method_15363(pitch, -90.0f, 90.0f));
    }

    private boolean hasMobsNearby() {
        if (mc.field_1724 == null || mc.field_1687 == null) {
            return false;
        }
        double r = this.mobFightRadius.getValue().floatValue();
        class_238 box = mc.field_1724.method_5829().method_1014(r);
        return !mc.field_1687.method_8390(class_1588.class, box, e -> {
            if ((e instanceof class_1548) || !e.method_5805()) {
                return false;
            }
            if (this.mobThroughWalls.getValue().booleanValue()) {
                return true;
            }
            return mc.field_1724.method_6057(e);
        }).isEmpty();
    }

    private boolean checkStaff() {
        StaffsWidget sw;
        if (mc.field_1724 == null || (sw = getStaffsWidget()) == null) {
            return false;
        }
        for (StaffsWidget.Staff staff : sw.getStaffList()) {
            String name = staff.name().split(":")[0];
            for (class_1657 p : mc.field_1687.method_18456()) {
                if (p.method_7334().getName().equalsIgnoreCase(name) && mc.field_1724.method_5739(p) <= this.staffFleeRadius.getValue().floatValue()) {
                    if (this.state != State.FLEEING_STAFF) {
                        cancelBaritone();
                        this.staffFlees++;
                        TextUtil.sendMessage("§cKeyHunter: стафф " + name + " рядом! Ухожу в хаб.");
                        this.state = State.FLEEING_STAFF;
                        this.commandDelay = System.currentTimeMillis() + 400;
                        return true;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkPlayers() {
        if (mc.field_1724 == null) {
            return false;
        }
        for (class_746 class_746Var : mc.field_1687.method_18456()) {
            if (class_746Var != mc.field_1724 && !FriendManager.getInstance().contains(class_746Var.method_7334().getName()) && !isStaff(class_746Var) && mc.field_1724.method_5739(class_746Var) <= this.playerFleeRadius.getValue().floatValue()) {
                if (this.state != State.FLEEING_PLAYER) {
                    cancelBaritone();
                    this.playerFlees++;
                    TextUtil.sendMessage("§cKeyHunter: игрок " + class_746Var.method_7334().getName() + " рядом! Выполняю /rtp s.");
                    this.state = State.FLEEING_PLAYER;
                    this.commandDelay = System.currentTimeMillis() + 400;
                    return true;
                }
                return true;
            }
        }
        return false;
    }

    private boolean checkMobs() {
        if (mc.field_1724 == null || mc.field_1687 == null) {
            return false;
        }
        double r = this.mobFightRadius.getValue().floatValue();
        class_238 box = mc.field_1724.method_5829().method_1014(r);
        List<class_1588> mobs = mc.field_1687.method_8390(class_1588.class, box, e -> {
            if ((e instanceof class_1548) || !e.method_5805()) {
                return false;
            }
            if (this.mobThroughWalls.getValue().booleanValue()) {
                return true;
            }
            return mc.field_1724.method_6057(e);
        });
        if (!mobs.isEmpty()) {
            cancelBaritone();
            ensureBestWeapon();
            if (!AuraModule.getInstance().isEnabled()) {
                AuraModule.getInstance().enable();
            }
            if (this.state != State.FIGHTING_MOBS) {
                TextUtil.sendMessage("§cKeyHunter: обнаружены враждебные мобы! Переключаюсь в режим боя.");
                this.state = State.FIGHTING_MOBS;
                this.fightTimer = System.currentTimeMillis() + 12000;
                return true;
            }
            return true;
        }
        return false;
    }

    private void doScan() {
        class_1694 cart;
        if (System.currentTimeMillis() < this.scanTimer) {
            return;
        }
        if (this.targetLocked) {
            if (!this.targetIsCart && this.targetChest != null && !this.visitedChests.contains(this.targetChest)) {
                this.state = State.PATHING;
                startPathing(this.targetChest);
                return;
            } else {
                if (this.targetIsCart && !this.visitedCarts.contains(Integer.valueOf(this.targetCartId)) && (cart = getCartById(this.targetCartId)) != null) {
                    this.state = State.PATHING;
                    startPathing(cart.method_24515());
                    return;
                }
                this.targetLocked = false;
            }
        }
        this.scanTimer = System.currentTimeMillis() + 3000;
        this.nearChests.clear();
        this.nearCarts.clear();
        int r = this.searchRadius.getValue().intValue();
        class_2338 center = mc.field_1724.method_24515();
        for (int cx = (center.method_10263() - r) >> 4; cx <= ((center.method_10263() + r) >> 4); cx++) {
            for (int cz = (center.method_10260() - r) >> 4; cz <= ((center.method_10260() + r) >> 4); cz++) {
                if (mc.field_1687.method_8393(cx, cz)) {
                    class_2818 chunk = mc.field_1687.method_8497(cx, cz);
                    for (Map.Entry<class_2338, class_2586> entry : chunk.method_12214().entrySet()) {
                        class_2586 be = entry.getValue();
                        if ((be instanceof class_2595) || (be instanceof class_2646)) {
                            class_2338 pos = entry.getKey();
                            if (!this.visitedChests.contains(pos) && center.method_10262(pos) <= ((double) r) * ((double) r)) {
                                this.nearChests.add(pos);
                            }
                        }
                    }
                }
            }
        }
        mc.field_1687.method_8390(class_1694.class, new class_238(center).method_1014(r), e -> {
            return !this.visitedCarts.contains(Integer.valueOf(e.method_5628()));
        }).forEach(e2 -> {
            this.nearCarts.add(Integer.valueOf(e2.method_5628()));
        });
        int total = this.nearChests.size() + this.nearCarts.size();
        if (total < this.minChests.getValue().intValue()) {
            TextUtil.sendMessage("§eKeyHunter: мало сундуков в зоне (" + total + "), ищу другую территорию через /rtp s.");
            forceRtp();
            return;
        }
        class_2338 nearest = findNearestChest();
        if (nearest != null) {
            double dist = Math.sqrt(center.method_10262(nearest));
            if (dist > this.maxChestDist.getValue().floatValue()) {
                TextUtil.sendMessage("§eKeyHunter: ближайший сундук слишком далеко (" + ((int) dist) + " блоков), /rtp s.");
                forceRtp();
                return;
            }
            TextUtil.sendMessage("§aKeyHunter: найден сундук на расстоянии " + ((int) dist) + " блоков. Начинаю движение.");
            this.targetChest = nearest;
            this.targetIsCart = false;
            this.targetLocked = true;
            startPathing(nearest);
            this.state = State.PATHING;
            return;
        }
        if (!this.nearCarts.isEmpty()) {
            TextUtil.sendMessage("§aKeyHunter: найден сундук в вагонетке. Начинаю движение.");
            this.targetCartId = this.nearCarts.get(0).intValue();
            this.targetIsCart = true;
            this.targetLocked = true;
            class_1694 cart2 = getCartById(this.targetCartId);
            if (cart2 != null) {
                startPathing(cart2.method_24515());
                this.state = State.PATHING;
                return;
            }
            return;
        }
        forceRtp();
    }

    private void doLoot() {
        if (System.currentTimeMillis() < this.lootTimer) {
            return;
        }
        class_465<?> class_465Var = mc.field_1755;
        if (class_465Var instanceof class_465) {
            class_465<?> screen = class_465Var;
            class_1707 class_1707VarMethod_17577 = screen.method_17577();
            if (class_1707VarMethod_17577 instanceof class_1707) {
                class_1707 handler = class_1707VarMethod_17577;
                int chestSize = handler.method_17388() * 9;
                if (System.currentTimeMillis() < this.lootSlotTimer) {
                    return;
                }
                while (this.lootSlotIndex < chestSize) {
                    class_1799 stack = handler.method_7611(this.lootSlotIndex).method_7677();
                    if (stack.method_7960() || !isWanted(stack)) {
                        this.lootSlotIndex++;
                    } else {
                        countItem(stack);
                        int slotToClick = this.lootSlotIndex;
                        if (SlownessManager.isEnabled()) {
                            SlownessManager.applySlowness(80L, 30L, () -> {
                                mc.field_1761.method_2906(handler.field_7763, slotToClick, 0, class_1713.field_7794, mc.field_1724);
                            });
                        } else {
                            mc.field_1761.method_2906(handler.field_7763, slotToClick, 0, class_1713.field_7794, mc.field_1724);
                        }
                        this.lootSlotIndex++;
                        this.lootSlotTimer = System.currentTimeMillis() + 60;
                        return;
                    }
                }
                this.lootSlotIndex = 0;
                mc.field_1724.method_7346();
                if (this.targetIsCart) {
                    this.visitedCarts.add(Integer.valueOf(this.targetCartId));
                } else if (this.targetChest != null) {
                    this.visitedChests.add(this.targetChest);
                }
                this.targetLocked = false;
                TextUtil.sendMessage("§aKeyHunter: лут завершён. Ищу следующий сундук.");
                this.state = State.SCANNING;
                this.scanTimer = 0L;
                return;
            }
        }
        if (System.currentTimeMillis() > this.lootTimer + 3000) {
            this.lootSlotIndex = 0;
            if (this.targetIsCart) {
                this.visitedCarts.add(Integer.valueOf(this.targetCartId));
            } else if (this.targetChest != null) {
                this.visitedChests.add(this.targetChest);
            }
            this.targetLocked = false;
            this.state = State.SCANNING;
            this.scanTimer = 0L;
            return;
        }
        if (!this.targetIsCart) {
            if (this.targetChest != null) {
                lookAt(class_243.method_24953(this.targetChest));
                interactChest(this.targetChest);
                return;
            }
            return;
        }
        class_1694 cart = getCartById(this.targetCartId);
        if (cart != null) {
            lookAt(cart.method_19538());
            interactCart(cart);
        }
    }

    private void doEat() {
        if (mc.field_1724 == null) {
            class_304.method_1416(mc.field_1690.field_1904.method_1429(), false);
            this.eatActive = false;
            this.state = State.SCANNING;
            return;
        }
        if (mc.field_1724.method_7344().method_7586() >= this.eatUntil.getValue().intValue()) {
            if (this.eatActive && mc.field_1724.method_6115()) {
                mc.field_1761.method_2897(mc.field_1724);
            }
            class_304.method_1416(mc.field_1690.field_1904.method_1429(), false);
            this.eatActive = false;
            TextUtil.sendMessage("§aKeyHunter: голод восстановлен, продолжаю работу.");
            this.state = State.SCANNING;
            return;
        }
        int foodSlot = findFoodSlot();
        if (foodSlot == -1) {
            class_304.method_1416(mc.field_1690.field_1904.method_1429(), false);
            this.eatActive = false;
            TextUtil.sendMessage("§eKeyHunter: еда не найдена, продолжаю работу.");
            this.state = State.SCANNING;
            return;
        }
        mc.field_1724.method_31548().field_7545 = foodSlot;
        if (!this.eatActive) {
            this.eatActive = true;
            mc.field_1761.method_2919(mc.field_1724, class_1268.field_5808);
            class_304.method_1416(mc.field_1690.field_1904.method_1429(), true);
            mc.field_1724.method_6019(class_1268.field_5808);
            return;
        }
        if (!mc.field_1724.method_6115()) {
            mc.field_1761.method_2919(mc.field_1724, class_1268.field_5808);
            class_304.method_1416(mc.field_1690.field_1904.method_1429(), true);
            mc.field_1724.method_6019(class_1268.field_5808);
        }
    }

    private void doFleeStaff() {
        if (System.currentTimeMillis() < this.commandDelay) {
            return;
        }
        cancelBaritone();
        if (mc.field_1724 != null) {
            mc.field_1724.field_3944.method_45730("hub");
        }
        this.hubSentTime = System.currentTimeMillis();
        this.state = State.WAITING_HUB;
    }

    private void doWaitHub() {
        if (System.currentTimeMillis() - this.hubSentTime >= 300000) {
            TextUtil.sendMessage("§aKeyHunter: 5 минут прошло, возвращаюсь на сервер через /srv.");
            cancelBaritone();
            if (mc.field_1724 != null) {
                mc.field_1724.field_3944.method_45730("srv " + this.serverCmd.getValue());
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
        cancelBaritone();
        if (mc.field_1724 != null) {
            mc.field_1724.field_3944.method_45730("rtp s");
        }
        this.rtpCooldownEnd = System.currentTimeMillis() + 9000;
        this.visitedChests.clear();
        this.visitedCarts.clear();
        this.targetLocked = false;
        this.scanTimer = System.currentTimeMillis() + 5000;
        this.state = State.RTP_COOLDOWN;
    }

    private void doRtpCooldown() {
        if (System.currentTimeMillis() >= this.rtpCooldownEnd) {
            TextUtil.sendMessage("§aKeyHunter: кулдаун /rtp s истёк, начинаю сканирование новой территории.");
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
        cancelBaritone();
        if (mc.field_1724 != null) {
            mc.field_1724.field_3944.method_45730("home default");
        }
        this.homeChestPos = null;
        this.knownHomeChests.clear();
        this.commandDelay = System.currentTimeMillis() + 5000;
        this.state = State.STORING;
    }

    private void doStore() {
        if (mc.field_1724 == null) {
            this.state = State.SCANNING;
            return;
        }
        if (System.currentTimeMillis() < this.commandDelay) {
            return;
        }
        if (this.homeChestPos == null) {
            scanHomeChests();
            if (this.homeChestPos == null) {
                this.state = State.SCANNING;
                return;
            }
        }
        if (Math.sqrt(mc.field_1724.method_24515().method_10262(this.homeChestPos)) > 3.5d) {
            if (!isBaritonePathing()) {
                TextUtil.sendMessage("§eKeyHunter: иду к домашнему сундуку для выгрузки лута.");
                startPathing(this.homeChestPos);
                return;
            }
            return;
        }
        class_465<?> class_465Var = mc.field_1755;
        if (class_465Var instanceof class_465) {
            class_465<?> screen = class_465Var;
            class_1703 class_1703VarMethod_17577 = screen.method_17577();
            if (class_1703VarMethod_17577 instanceof class_1707) {
                class_1707 handler = (class_1707) class_1703VarMethod_17577;
                if (isChestFull(handler)) {
                    mc.field_1724.method_7346();
                    this.homeChestPos = findNextHomeChest();
                    if (this.homeChestPos == null) {
                        TextUtil.sendMessage("§eKeyHunter: все домашние сундуки заполнены, продолжаю работу.");
                        this.state = State.SCANNING;
                        return;
                    } else {
                        startPathing(this.homeChestPos);
                        return;
                    }
                }
                int chestSize = handler.method_17388() * 9;
                for (int i = 9; i < handler.field_7761.size(); i++) {
                    class_1799 stack = handler.method_7611(i).method_7677();
                    if (!stack.method_7960() && isWanted(stack)) {
                        int j = 0;
                        while (true) {
                            if (j >= chestSize) {
                                break;
                            }
                            if (!handler.method_7611(j).method_7677().method_7960()) {
                                j++;
                            } else {
                                mc.field_1761.method_2906(handler.field_7763, i, 0, class_1713.field_7794, mc.field_1724);
                                break;
                            }
                        }
                    }
                }
                mc.field_1724.method_7346();
                TextUtil.sendMessage("§aKeyHunter: лут выгружен в домашний сундук. Возвращаюсь в шахты.");
                this.state = State.SCANNING;
                this.scanTimer = 0L;
                return;
            }
        }
        lookAt(class_243.method_24953(this.homeChestPos));
        interactChest(this.homeChestPos);
    }

    private void forceRtp() {
        cancelBaritone();
        if (mc.field_1724 != null) {
            mc.field_1724.field_3944.method_45730("rtp s");
        }
        this.rtpCooldownEnd = System.currentTimeMillis() + 9000;
        this.state = State.RTP_COOLDOWN;
        this.visitedChests.clear();
        this.visitedCarts.clear();
        this.targetLocked = false;
        this.scanTimer = System.currentTimeMillis() + 5000;
    }

    private void startPathing(class_2338 pos) {
        applyBaritoneSettings();
        IBaritone b = BaritoneAPI.getProvider().getPrimaryBaritone();
        b.getCustomGoalProcess().setGoalAndPath(new GoalNear(pos, 2));
        this.lastPos = mc.field_1724 != null ? mc.field_1724.method_24515() : pos;
        this.stuckTimer = System.currentTimeMillis();
        this.pathTimeout = System.currentTimeMillis() + 30000;
    }

    private void applyBaritoneSettings() {
        BaritoneAPI.getSettings().walkOnWaterOnePenalty.value = Double.valueOf(this.landPriority.getValue().booleanValue() ? 999.0d : 3.0d);
        BaritoneAPI.getSettings().allowWaterBucketFall.value = false;
        BaritoneAPI.getSettings().avoidance.value = true;
        BaritoneAPI.getSettings().mobAvoidanceCoefficient.value = Double.valueOf(0.2d);
        BaritoneAPI.getSettings().mobAvoidanceRadius.value = 5;
        BaritoneAPI.getSettings().mobSpawnerAvoidanceCoefficient.value = Double.valueOf(0.1d);
        BaritoneAPI.getSettings().mobSpawnerAvoidanceRadius.value = 4;
        BaritoneAPI.getSettings().blocksToAvoidBreaking.value = new ArrayList(Arrays.asList(class_2246.field_10597, class_2246.field_28675, class_2246.field_28676, class_2246.field_23078, class_2246.field_23079, class_2246.field_22123, class_2246.field_22124, class_2246.field_10382, class_2246.field_27097));
        BaritoneAPI.getSettings().sprintInWater.value = false;
    }

    private void resetBaritoneSettings() {
        BaritoneAPI.getSettings().walkOnWaterOnePenalty.value = Double.valueOf(3.0d);
        BaritoneAPI.getSettings().allowWaterBucketFall.value = true;
        BaritoneAPI.getSettings().avoidance.value = false;
        BaritoneAPI.getSettings().mobAvoidanceCoefficient.value = Double.valueOf(1.5d);
        BaritoneAPI.getSettings().mobAvoidanceRadius.value = 8;
        BaritoneAPI.getSettings().mobSpawnerAvoidanceCoefficient.value = Double.valueOf(2.0d);
        BaritoneAPI.getSettings().mobSpawnerAvoidanceRadius.value = 16;
        BaritoneAPI.getSettings().blocksToAvoidBreaking.value = new ArrayList(Arrays.asList(class_2246.field_9980, class_2246.field_10181, class_2246.field_10034, class_2246.field_10380));
        BaritoneAPI.getSettings().sprintInWater.value = true;
    }

    private boolean isStuck() {
        if (mc.field_1724 == null) {
            return false;
        }
        class_2338 current = mc.field_1724.method_24515();
        if (this.lastPos == null) {
            this.lastPos = current;
            this.stuckTimer = System.currentTimeMillis();
            return false;
        }
        if (System.currentTimeMillis() - this.stuckTimer > 30000) {
            double distanceMoved = Math.sqrt(current.method_10262(this.lastPos));
            if (distanceMoved <= 4.0d) {
                return true;
            }
            this.stuckTimer = System.currentTimeMillis();
            this.lastPos = current;
            return false;
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
        int tier;
        if (mc.field_1724 == null) {
            return;
        }
        int bestSlot = -1;
        int bestTier = -1;
        for (int i = 0; i < 9; i++) {
            class_1799 s = mc.field_1724.method_31548().method_5438(i);
            if (!s.method_7960() && (tier = getWeaponTier(s.method_7909())) > bestTier) {
                bestTier = tier;
                bestSlot = i;
            }
        }
        if (bestSlot != -1 && mc.field_1724.method_31548().field_7545 != bestSlot) {
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
        return item == class_1802.field_8475 ? 2 : 0;
    }

    private void interactChest(class_2338 pos) {
        if (mc.field_1724 == null || mc.field_1687 == null) {
            return;
        }
        mc.field_1761.method_2896(mc.field_1724, class_1268.field_5808, new class_3965(class_243.method_24953(pos), class_2350.field_11036, pos, false));
    }

    private void interactCart(class_1694 cart) {
        if (mc.field_1724 == null) {
            return;
        }
        mc.field_1761.method_2905(mc.field_1724, cart, class_1268.field_5808);
    }

    private boolean isWanted(class_1799 stack) {
        if (stack.method_7960()) {
            return false;
        }
        class_1792 item = stack.method_7909();
        if (this.takeKeys.getValue().booleanValue() && item == class_1802.field_8600) {
            return true;
        }
        if (this.takeEGapples.getValue().booleanValue() && item == class_1802.field_8367) {
            return true;
        }
        if (this.takeGapples.getValue().booleanValue() && item == class_1802.field_8463) {
            return true;
        }
        return this.takeBooks.getValue().booleanValue() && item == class_1802.field_8598 && stack.method_57824(class_9334.field_49643) != null && !((class_9304) stack.method_57824(class_9334.field_49643)).method_57543();
    }

    private void countItem(class_1799 stack) {
        class_1792 item = stack.method_7909();
        if (item != class_1802.field_8600) {
            if (item != class_1802.field_8367) {
                if (item != class_1802.field_8463) {
                    if (item == class_1802.field_8598) {
                        this.booksLooted += stack.method_7947();
                        return;
                    }
                    return;
                }
                this.gapplesLooted += stack.method_7947();
                return;
            }
            this.egapplesLooted += stack.method_7947();
            return;
        }
        this.keysLooted += stack.method_7947();
    }

    private boolean shouldGoHome() {
        if (mc.field_1724 == null) {
            return false;
        }
        int free = 0;
        for (int i = 0; i < 36; i++) {
            if (mc.field_1724.method_31548().method_5438(i).method_7960()) {
                free++;
            }
        }
        return free <= 1;
    }

    private int findFoodSlot() {
        if (mc.field_1724 == null) {
            return -1;
        }
        for (int i = 0; i < 9; i++) {
            class_1799 s = mc.field_1724.method_31548().method_5438(i);
            if (!s.method_7960() && s.method_57824(class_9334.field_50075) != null) {
                return i;
            }
        }
        for (int i2 = 9; i2 < 36; i2++) {
            class_1799 s2 = mc.field_1724.method_31548().method_5438(i2);
            if (!s2.method_7960() && s2.method_57824(class_9334.field_50075) != null) {
                mc.field_1724.method_31548().field_7545 = 0;
                mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, i2, 0, class_1713.field_7791, mc.field_1724);
                return 0;
            }
        }
        return -1;
    }

    private class_2338 findNearestChest() {
        if (mc.field_1724 == null || this.nearChests.isEmpty()) {
            return null;
        }
        return this.nearChests.stream().min(Comparator.comparingDouble(p -> {
            return mc.field_1724.method_24515().method_10262(p);
        })).orElse(null);
    }

    private class_1694 getCartById(int id) {
        if (mc.field_1687 == null) {
            return null;
        }
        class_1694 class_1694VarMethod_8469 = mc.field_1687.method_8469(id);
        if (!(class_1694VarMethod_8469 instanceof class_1694)) {
            return null;
        }
        class_1694 cart = class_1694VarMethod_8469;
        return cart;
    }

    private boolean isStaff(class_1657 p) {
        StaffsWidget sw = getStaffsWidget();
        if (sw == null) {
            return false;
        }
        String name = p.method_7334().getName();
        return sw.getStaffList().stream().anyMatch(s -> {
            return s.name().split(":")[0].equalsIgnoreCase(name);
        });
    }

    private StaffsWidget getStaffsWidget() {
        for (Widget w : WidgetManager.getInstance().getWidgets()) {
            if (w instanceof StaffsWidget) {
                StaffsWidget sw = (StaffsWidget) w;
                return sw;
            }
        }
        return null;
    }

    private void scanHomeChests() {
        if (mc.field_1724 == null) {
            return;
        }
        this.knownHomeChests.clear();
        class_2338 center = mc.field_1724.method_24515();
        for (int x = -8; x <= 8; x++) {
            for (int y = -4; y <= 4; y++) {
                for (int z = -8; z <= 8; z++) {
                    class_2338 pos = center.method_10069(x, y, z);
                    class_2586 be = mc.field_1687.method_8321(pos);
                    if (be instanceof class_2595) {
                        this.knownHomeChests.add(pos);
                    }
                }
            }
        }
        if (!this.knownHomeChests.isEmpty()) {
            this.homeChestPos = this.knownHomeChests.get(0);
        }
    }

    private class_2338 findNextHomeChest() {
        for (class_2338 pos : this.knownHomeChests) {
            if (!pos.equals(this.homeChestPos)) {
                return pos;
            }
        }
        return null;
    }

    private boolean isChestFull(class_1707 handler) {
        int size = handler.method_17388() * 9;
        for (int i = 0; i < size; i++) {
            if (handler.method_7611(i).method_7677().method_7960()) {
                return false;
            }
        }
        return true;
    }

    private static String strip(String s) {
        return s == null ? "" : s.replaceAll("(?i)[§&][0-9a-fk-or]", "").trim();
    }
}
