package sweetie.leonware.client.features.modules.player;

import java.awt.Color;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_1713;
import net.minecraft.class_1794;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_2680;
import net.minecraft.class_3675;
import net.minecraft.class_3965;
import net.minecraft.class_4587;
import net.minecraft.class_640;
import net.minecraft.class_7439;
import net.minecraft.class_8113;
import org.joml.Vector2f;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.event.events.render.Render2DEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BindSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.math.ProjectionUtil;
import sweetie.leonware.api.utils.other.SlownessManager;
import sweetie.leonware.api.utils.other.TextUtil;
import sweetie.leonware.api.utils.player.InventoryUtil;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import sweetie.leonware.inject.accessors.ITextDisplayEntity;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/player/AresMineModule.class */
@ModuleRegister(name = "Ares Mine Helper", category = Category.PLAYER)
public class AresMineModule extends Module {
    private static final AresMineModule instance = new AresMineModule();
    private static final Pattern BAN_PATTERN = Pattern.compile("[^\\w]*(?:Игрок|Player) (.+?) забанил игрока (.+?)\\s*[^\\w]*По причине: (.+)");
    private static final Pattern WARP_PATTERN = Pattern.compile("Вы успешно телепортированы на варп (.+)");
    private final BooleanSetting autoTill = new BooleanSetting("Авто-вспашка").value((Boolean) false);
    private final BooleanSetting autoDropPaper = new BooleanSetting("Авто-дроп бумаги").value((Boolean) false);
    private final BooleanSetting notifyWarp = new BooleanSetting("Уведомление варпа").value((Boolean) true);
    private final BooleanSetting notifyBan = new BooleanSetting("Уведомление бана").value((Boolean) true);
    private final BooleanSetting notifyHighlight = new BooleanSetting("Уведомление подсвета").value((Boolean) true);
    private final BooleanSetting scrollUse = new BooleanSetting("Использовать свиток").value((Boolean) false);
    private final BindSetting scrollBind = new BindSetting("Бинд свитка").value((Integer) (-999)).setVisible(() -> {
        return this.scrollUse.getValue();
    });
    private final BooleanSetting renderChests = new BooleanSetting("Сундуки с припасами").value((Boolean) true);
    private final BooleanSetting renderGifts = new BooleanSetting("Небесные дары").value((Boolean) true);
    private final BooleanSetting optimizeDisplays = new BooleanSetting("Оптимизация дисплеев").value((Boolean) false);
    private final BooleanSetting staffAlert = new BooleanSetting("Оповещение стаффа").value((Boolean) true);
    private final BooleanSetting staffEsp = new BooleanSetting("ESP стаффа").value((Boolean) true).setVisible(() -> {
        return this.staffAlert.getValue();
    });
    private final MultiBooleanSetting staffPrefixes = new MultiBooleanSetting("Префиксы стаффа").value(new BooleanSetting("Staff").value((Boolean) true), new BooleanSetting("Admin").value((Boolean) true), new BooleanSetting("Moder").value((Boolean) true), new BooleanSetting("GM").value((Boolean) true), new BooleanSetting("Helper").value((Boolean) false)).setVisible(() -> {
        return this.staffAlert.getValue();
    });
    private final Set<String> alreadyAlerted = new HashSet();
    private final List<SupplyItem> visualItems = new ArrayList();
    private int lastDurabilityNotify = -1;
    private long lastDurabilityTime = 0;
    private long scrollLastUse = 0;
    private long paperDropLastUse = 0;

    @Generated
    public static AresMineModule getInstance() {
        return instance;
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/player/AresMineModule$SupplyItem.class */
    private static final class SupplyItem extends Record {
        private final class_243 position;
        private final String name;
        private final String timeLeft;

        private SupplyItem(class_243 position, String name, String timeLeft) {
            this.position = position;
            this.name = name;
            this.timeLeft = timeLeft;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, SupplyItem.class), SupplyItem.class, "position;name;timeLeft", "FIELD:Lsweetie/leonware/client/features/modules/player/AresMineModule$SupplyItem;->position:Lnet/minecraft/class_243;", "FIELD:Lsweetie/leonware/client/features/modules/player/AresMineModule$SupplyItem;->name:Ljava/lang/String;", "FIELD:Lsweetie/leonware/client/features/modules/player/AresMineModule$SupplyItem;->timeLeft:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, SupplyItem.class), SupplyItem.class, "position;name;timeLeft", "FIELD:Lsweetie/leonware/client/features/modules/player/AresMineModule$SupplyItem;->position:Lnet/minecraft/class_243;", "FIELD:Lsweetie/leonware/client/features/modules/player/AresMineModule$SupplyItem;->name:Ljava/lang/String;", "FIELD:Lsweetie/leonware/client/features/modules/player/AresMineModule$SupplyItem;->timeLeft:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, SupplyItem.class, Object.class), SupplyItem.class, "position;name;timeLeft", "FIELD:Lsweetie/leonware/client/features/modules/player/AresMineModule$SupplyItem;->position:Lnet/minecraft/class_243;", "FIELD:Lsweetie/leonware/client/features/modules/player/AresMineModule$SupplyItem;->name:Ljava/lang/String;", "FIELD:Lsweetie/leonware/client/features/modules/player/AresMineModule$SupplyItem;->timeLeft:Ljava/lang/String;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public class_243 position() {
            return this.position;
        }

        public String name() {
            return this.name;
        }

        public String timeLeft() {
            return this.timeLeft;
        }
    }

    /* JADX WARN: Type inference failed for: r1v14, types: [sweetie.leonware.api.module.setting.BindSetting] */
    /* JADX WARN: Type inference failed for: r1v25, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v28, types: [sweetie.leonware.api.module.setting.MultiBooleanSetting] */
    public AresMineModule() {
        addSettings(this.autoTill, this.autoDropPaper, this.notifyWarp, this.notifyBan, this.notifyHighlight, this.scrollUse, this.scrollBind, this.renderChests, this.renderGifts, this.optimizeDisplays, this.staffAlert, this.staffPrefixes, this.staffEsp);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener tick = TickEvent.getInstance().subscribe(new Listener(e -> {
            onTick();
        }));
        EventListener render = Render2DEvent.getInstance().subscribe(new Listener(this::onRender));
        EventListener packet = PacketEvent.getInstance().subscribe(new Listener(this::onPacket));
        addEvents(tick, render, packet);
    }

    private void onPacket(PacketEvent.PacketEventData event) {
        if (event.isSend() || mc.field_1687 == null || mc.field_1724 == null) {
            return;
        }
        class_7439 class_7439VarPacket = event.packet();
        if (class_7439VarPacket instanceof class_7439) {
            class_7439 p = class_7439VarPacket;
            String raw = p.comp_763().getString();
            String clean = raw.replaceAll("(?i)§[0-9a-fklmnor]", "").replace("\n", " ").trim();
            if (this.notifyWarp.getValue().booleanValue()) {
                Matcher wm = WARP_PATTERN.matcher(clean);
                if (wm.find()) {
                    notify("§aВарп: §f" + wm.group(1).trim());
                }
            }
            if (this.notifyBan.getValue().booleanValue() && clean.contains("забанил") && clean.contains("По причине:")) {
                Matcher bm = BAN_PATTERN.matcher(clean);
                if (bm.find()) {
                    notify("§c[БАН] §9" + bm.group(1).trim() + " §f→ §c" + bm.group(2).trim() + " §eПричина: §c" + bm.group(3).trim());
                }
            }
            if (this.notifyHighlight.getValue().booleanValue() && clean.contains("Около Вас телепортировался игрок")) {
                notify("§c[ВНИМАНИЕ] §fОколо вас телепортировался игрок!");
            }
        }
    }

    private void onTick() {
        if (mc.field_1724 == null || mc.field_1687 == null) {
            return;
        }
        if (this.autoTill.getValue().booleanValue()) {
            doAutoTill();
        }
        if (this.autoDropPaper.getValue().booleanValue()) {
            doDropPaper();
        }
        if (this.staffAlert.getValue().booleanValue()) {
            doStaffAlert();
        }
        if (this.scrollUse.getValue().booleanValue()) {
            doScrollUse();
        }
        if (this.optimizeDisplays.getValue().booleanValue()) {
            doOptimizeDisplays();
        }
    }

    private void onRender(Render2DEvent.Render2DEventData event) {
        class_4587 ms = event.matrixStack();
        if (this.renderChests.getValue().booleanValue() || this.renderGifts.getValue().booleanValue()) {
            updateVisualItems();
            for (SupplyItem item : this.visualItems) {
                renderSupplyItem(ms, item);
            }
        }
        if (this.staffAlert.getValue().booleanValue() && this.staffEsp.getValue().booleanValue()) {
            doStaffEsp(ms);
        }
    }

    private void doAutoTill() {
        class_1799 main = mc.field_1724.method_6047();
        if (!(main.method_7909() instanceof class_1794)) {
            if (mc.field_1690.field_1886.method_1434()) {
                mc.field_1690.field_1886.method_23481(false);
                return;
            }
            return;
        }
        int remaining = main.method_7936() - main.method_7919();
        long now = System.currentTimeMillis();
        if (remaining <= 10) {
            if (remaining != this.lastDurabilityNotify || now - this.lastDurabilityTime > 3000) {
                notify("§cМотыга почти сломана! Осталось §f" + remaining + " §cисп.");
                this.lastDurabilityNotify = remaining;
                this.lastDurabilityTime = now;
            }
            mc.field_1690.field_1886.method_23481(false);
            return;
        }
        this.lastDurabilityNotify = -1;
        class_3965 class_3965Var = mc.field_1765;
        if (class_3965Var == null || class_3965Var.method_17783() != class_239.class_240.field_1332) {
            mc.field_1690.field_1886.method_23481(true);
            return;
        }
        class_3965 bhr = class_3965Var;
        class_2680 state = mc.field_1687.method_8320(bhr.method_17777());
        if (state.method_27852(class_2246.field_10362)) {
            mc.field_1690.field_1886.method_23481(false);
            return;
        }
        mc.field_1690.field_1886.method_23481(true);
        if (state.method_27852(class_2246.field_10566) || state.method_27852(class_2246.field_10219)) {
            mc.field_1761.method_2896(mc.field_1724, class_1268.field_5808, bhr);
        }
    }

    private void doDropPaper() {
        if (System.currentTimeMillis() - this.paperDropLastUse < 10) {
            return;
        }
        class_1799 mainHand = mc.field_1724.method_6047();
        class_1799 offHand = mc.field_1724.method_6079();
        boolean inHand = false;
        if (!mainHand.method_7960() && mainHand.method_7909() == class_1802.field_8407) {
            String name = mainHand.method_7964().getString();
            if (name.contains("Донат") || name.contains("Помощь") || name.contains("Меню")) {
                inHand = true;
                int i = mc.field_1724.method_31548().field_7545 + 36;
            }
        }
        if (!offHand.method_7960() && offHand.method_7909() == class_1802.field_8407 && !inHand) {
            String name2 = offHand.method_7964().getString();
            if (name2.contains("Донат") || name2.contains("Помощь") || name2.contains("Меню")) {
                inHand = true;
            }
        }
        if (inHand) {
            this.paperDropLastUse = System.currentTimeMillis();
            return;
        }
        int i2 = 0;
        while (i2 < 36) {
            class_1799 stack = mc.field_1724.method_31548().method_5438(i2);
            if (!stack.method_7960() && stack.method_7909() == class_1802.field_8407) {
                String name3 = stack.method_7964().getString();
                if (name3.contains("Донат") || name3.contains("Помощь") || name3.contains("Меню")) {
                    int screenSlot = i2 < 9 ? i2 + 36 : i2;
                    SlownessManager.applySlowness(100L, 1L, () -> {
                        if (mc.field_1724 == null) {
                            return;
                        }
                        mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, screenSlot, 1, class_1713.field_7795, mc.field_1724);
                    });
                    this.paperDropLastUse = System.currentTimeMillis();
                    return;
                }
            }
            i2++;
        }
    }

    private void doStaffAlert() {
        Set<String> nearStaff = new HashSet<>();
        for (class_1297 entity : mc.field_1687.method_18112()) {
            if (entity instanceof class_1657) {
                String name = entity.method_5477().getString();
                if (!name.equals(mc.field_1724.method_5477().getString()) && isStaffPlayer(name)) {
                    double dist = mc.field_1724.method_5739(entity);
                    if (dist <= 50.0d) {
                        nearStaff.add(name);
                        if (!this.alreadyAlerted.contains(name)) {
                            this.alreadyAlerted.add(name);
                            notify("§c[СТАФФ] §f" + name + " рядом! (" + ((int) dist) + " блоков)");
                        }
                    }
                }
            }
        }
        this.alreadyAlerted.retainAll(nearStaff);
    }

    private void doScrollUse() {
        int key = this.scrollBind.getValue().intValue();
        if (key < 0 || mc.field_1755 != null) {
            return;
        }
        if ((mc.field_1705 == null || mc.field_1705.method_1743() == null || !mc.field_1705.method_1743().method_1819()) && class_3675.method_15987(mc.method_22683().method_4490(), key) && System.currentTimeMillis() - this.scrollLastUse >= 500) {
            int currentSlot = mc.field_1724.method_31548().field_7545;
            for (int i = 0; i < 36; i++) {
                class_1799 stack = mc.field_1724.method_31548().method_5438(i);
                if (!stack.method_7960() && stack.method_7909() == class_1802.field_8600 && stack.method_7964().getString().contains("Свиток")) {
                    this.scrollLastUse = System.currentTimeMillis();
                    if (i < 9) {
                        InventoryUtil.swapToSlot(i);
                        InventoryUtil.useItem(class_1268.field_5808);
                        InventoryUtil.swapToSlot(currentSlot);
                        return;
                    } else {
                        int hotbar = InventoryUtil.findBestSlotInHotBar();
                        if (hotbar == -1) {
                            return;
                        }
                        int fi = i;
                        SlownessManager.applySlowness(150L, 1L, () -> {
                            InventoryUtil.swapSlots(fi, hotbar);
                            InventoryUtil.swapToSlot(hotbar);
                            InventoryUtil.useItem(class_1268.field_5808);
                        });
                        SlownessManager.applySlowness(300L, 150L, () -> {
                            InventoryUtil.swapSlots(fi, hotbar);
                            InventoryUtil.swapToSlot(currentSlot);
                        });
                        return;
                    }
                }
            }
        }
    }

    private void doOptimizeDisplays() {
        List<Integer> toRemove = new ArrayList<>();
        for (ITextDisplayEntity iTextDisplayEntity : mc.field_1687.method_18112()) {
            if (iTextDisplayEntity instanceof class_8113.class_8123) {
                class_2561 text = ((class_8113.class_8123) iTextDisplayEntity).invokeGetText();
                if (text == null) {
                    toRemove.add(Integer.valueOf(iTextDisplayEntity.method_5628()));
                } else {
                    String str = text.getString();
                    if (!str.contains("СУНДУК С ПРИПАСАМИ") && !str.contains("Небесный дар")) {
                        toRemove.add(Integer.valueOf(iTextDisplayEntity.method_5628()));
                    }
                }
            }
        }
        Iterator<Integer> it = toRemove.iterator();
        while (it.hasNext()) {
            int id = it.next().intValue();
            mc.field_1687.method_2945(id, class_1297.class_5529.field_26999);
        }
    }

    private void updateVisualItems() {
        class_2561 text;
        this.visualItems.clear();
        if (mc.field_1687 == null) {
            return;
        }
        for (ITextDisplayEntity iTextDisplayEntity : mc.field_1687.method_18112()) {
            if ((iTextDisplayEntity instanceof class_8113.class_8123) && (text = ((class_8113.class_8123) iTextDisplayEntity).invokeGetText()) != null) {
                String full = text.getString();
                boolean isChest = this.renderChests.getValue().booleanValue() && full.contains("СУНДУК С ПРИПАСАМИ");
                boolean isGift = this.renderGifts.getValue().booleanValue() && full.contains("Небесный дар");
                if (isChest || isGift) {
                    String itemName = "";
                    String timeLeft = "";
                    for (class_2561 sib : text.method_10855()) {
                        String s = sib.getString();
                        if (s.contains("СУНДУК С ПРИПАСАМИ") || s.contains("Небесный дар")) {
                            itemName = s;
                        } else if (s.contains("До пополнения:") || s.contains("До открытия:")) {
                            timeLeft = s;
                        }
                    }
                    if (itemName.isEmpty()) {
                        itemName = full;
                    }
                    if (!itemName.isEmpty() && !timeLeft.isEmpty()) {
                        this.visualItems.add(new SupplyItem(iTextDisplayEntity.method_19538(), itemName, timeLeft));
                    }
                }
            }
        }
    }

    private void renderSupplyItem(class_4587 ms, SupplyItem item) {
        Vector2f screen = ProjectionUtil.project(item.position());
        if (screen.x == Float.MAX_VALUE || screen.y == Float.MAX_VALUE) {
            return;
        }
        float nameW = Fonts.PS_BOLD.getWidth(item.name(), 7.0f);
        float timeW = Fonts.PS_BOLD.getWidth(item.timeLeft(), 7.0f);
        float maxW = Math.max(nameW, timeW);
        float totalW = maxW + (4.0f * 2.0f);
        float x = screen.x - (totalW / 2.0f);
        float y = screen.y - (20.0f / 2.0f);
        RenderUtil.BLUR_RECT.draw(ms, x, y, totalW, 20.0f, 2.0f, UIColors.blur());
        Fonts.PS_BOLD.drawText(ms, item.name(), x + 4.0f + ((maxW - nameW) / 2.0f), y + 4.0f, 7.0f, new Color(-669631), 0.0f);
        Fonts.PS_BOLD.drawText(ms, item.timeLeft(), x + 4.0f + ((maxW - timeW) / 2.0f), y + 4.0f + 9.0f, 7.0f, UIColors.textColor(), 0.0f);
    }

    private void doStaffEsp(class_4587 ms) {
        for (class_1297 entity : mc.field_1687.method_18112()) {
            if (entity instanceof class_1657) {
                String name = entity.method_5477().getString();
                if (!name.equals(mc.field_1724.method_5477().getString()) && isStaffPlayer(name)) {
                    class_243 headPos = entity.method_19538().method_1031(0.0d, ((double) entity.method_17682()) + 0.35d, 0.0d);
                    Vector2f screen = ProjectionUtil.project(headPos);
                    if (screen.x != Float.MAX_VALUE && screen.y != Float.MAX_VALUE) {
                        String tag = "[Staff] " + name;
                        float tagW = Fonts.PS_BOLD.getWidth(tag, 7.0f);
                        RenderUtil.BLUR_RECT.draw(ms, (screen.x - (tagW / 2.0f)) - 3.0f, screen.y - 5.0f, tagW + (3.0f * 2.0f), 10.0f, 2.0f, UIColors.blur());
                        Fonts.PS_BOLD.drawText(ms, tag, screen.x - (tagW / 2.0f), screen.y - 2.0f, 7.0f, new Color(-43691), 0.0f);
                    }
                }
            }
        }
    }

    private boolean isStaffPlayer(String playerName) {
        if (mc.method_1562() == null) {
            return false;
        }
        for (class_640 entry : mc.method_1562().method_2880()) {
            if (entry.method_2966().getName().equalsIgnoreCase(playerName)) {
                StringBuilder sb = new StringBuilder();
                if (entry.method_2955() != null) {
                    sb.append(entry.method_2955().method_1144().getString());
                }
                if (entry.method_2971() != null) {
                    sb.append(entry.method_2971().getString());
                }
                String upper = sb.toString().replaceAll("(?i)§[0-9a-fklmnor]", "").toUpperCase();
                if (this.staffPrefixes.isEnabled("Staff") && upper.contains("STAFF")) {
                    return true;
                }
                if (this.staffPrefixes.isEnabled("Admin") && upper.contains("ADMIN")) {
                    return true;
                }
                if (this.staffPrefixes.isEnabled("Moder") && (upper.contains("MODER") || upper.contains("МОДЕР"))) {
                    return true;
                }
                if (this.staffPrefixes.isEnabled("GM") && upper.contains("GM")) {
                    return true;
                }
                return this.staffPrefixes.isEnabled("Helper") && upper.contains("HELPER");
            }
        }
        return false;
    }

    private void notify(String message) {
        TextUtil.sendMessage(message);
    }
}
