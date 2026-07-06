// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import sweetie.leonware.api.utils.other.TextUtil;
import net.minecraft.class_640;
import net.minecraft.class_243;
import org.joml.Vector2f;
import java.awt.Color;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import sweetie.leonware.api.utils.math.ProjectionUtil;
import net.minecraft.class_2561;
import sweetie.leonware.inject.accessors.ITextDisplayEntity;
import net.minecraft.class_8113;
import sweetie.leonware.api.utils.player.InventoryUtil;
import net.minecraft.class_3675;
import java.util.Collection;
import net.minecraft.class_1297;
import sweetie.leonware.api.utils.other.SlownessManager;
import net.minecraft.class_1657;
import net.minecraft.class_1713;
import net.minecraft.class_1802;
import net.minecraft.class_2680;
import net.minecraft.class_1799;
import net.minecraft.class_1268;
import net.minecraft.class_2246;
import net.minecraft.class_3965;
import net.minecraft.class_239;
import net.minecraft.class_1794;
import java.util.Iterator;
import net.minecraft.class_4587;
import java.util.regex.Matcher;
import net.minecraft.class_2596;
import net.minecraft.class_7439;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.events.render.Render2DEvent;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.module.setting.Setting;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.setting.BindSetting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import java.util.regex.Pattern;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Ares Mine Helper", category = Category.PLAYER)
public class AresMineModule extends Module
{
    private static final AresMineModule instance;
    private static final Pattern BAN_PATTERN;
    private static final Pattern WARP_PATTERN;
    private final BooleanSetting autoTill;
    private final BooleanSetting autoDropPaper;
    private final BooleanSetting notifyWarp;
    private final BooleanSetting notifyBan;
    private final BooleanSetting notifyHighlight;
    private final BooleanSetting scrollUse;
    private final BindSetting scrollBind;
    private final BooleanSetting renderChests;
    private final BooleanSetting renderGifts;
    private final BooleanSetting optimizeDisplays;
    private final BooleanSetting staffAlert;
    private final BooleanSetting staffEsp;
    private final MultiBooleanSetting staffPrefixes;
    private final Set<String> alreadyAlerted;
    private final List<SupplyItem> visualItems;
    private int lastDurabilityNotify;
    private long lastDurabilityTime;
    private long scrollLastUse;
    private long paperDropLastUse;
    
    public AresMineModule() {
        this.autoTill = new BooleanSetting("\u0410\u0432\u0442\u043e-\u0432\u0441\u043f\u0430\u0448\u043a\u0430").value(false);
        this.autoDropPaper = new BooleanSetting("\u0410\u0432\u0442\u043e-\u0434\u0440\u043e\u043f \u0431\u0443\u043c\u0430\u0433\u0438").value(false);
        this.notifyWarp = new BooleanSetting("\u0423\u0432\u0435\u0434\u043e\u043c\u043b\u0435\u043d\u0438\u0435 \u0432\u0430\u0440\u043f\u0430").value(true);
        this.notifyBan = new BooleanSetting("\u0423\u0432\u0435\u0434\u043e\u043c\u043b\u0435\u043d\u0438\u0435 \u0431\u0430\u043d\u0430").value(true);
        this.notifyHighlight = new BooleanSetting("\u0423\u0432\u0435\u0434\u043e\u043c\u043b\u0435\u043d\u0438\u0435 \u043f\u043e\u0434\u0441\u0432\u0435\u0442\u0430").value(true);
        this.scrollUse = new BooleanSetting("\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u0442\u044c \u0441\u0432\u0438\u0442\u043e\u043a").value(false);
        this.scrollBind = new BindSetting("\u0411\u0438\u043d\u0434 \u0441\u0432\u0438\u0442\u043a\u0430").value(-999).setVisible(() -> this.scrollUse.getValue());
        this.renderChests = new BooleanSetting("\u0421\u0443\u043d\u0434\u0443\u043a\u0438 \u0441 \u043f\u0440\u0438\u043f\u0430\u0441\u0430\u043c\u0438").value(true);
        this.renderGifts = new BooleanSetting("\u041d\u0435\u0431\u0435\u0441\u043d\u044b\u0435 \u0434\u0430\u0440\u044b").value(true);
        this.optimizeDisplays = new BooleanSetting("\u041e\u043f\u0442\u0438\u043c\u0438\u0437\u0430\u0446\u0438\u044f \u0434\u0438\u0441\u043f\u043b\u0435\u0435\u0432").value(false);
        this.staffAlert = new BooleanSetting("\u041e\u043f\u043e\u0432\u0435\u0449\u0435\u043d\u0438\u0435 \u0441\u0442\u0430\u0444\u0444\u0430").value(true);
        this.staffEsp = new BooleanSetting("ESP \u0441\u0442\u0430\u0444\u0444\u0430").value(true).setVisible(() -> this.staffAlert.getValue());
        this.staffPrefixes = new MultiBooleanSetting("\u041f\u0440\u0435\u0444\u0438\u043a\u0441\u044b \u0441\u0442\u0430\u0444\u0444\u0430").value(new BooleanSetting("Staff").value(true), new BooleanSetting("Admin").value(true), new BooleanSetting("Moder").value(true), new BooleanSetting("GM").value(true), new BooleanSetting("Helper").value(false)).setVisible(() -> this.staffAlert.getValue());
        this.alreadyAlerted = new HashSet<String>();
        this.visualItems = new ArrayList<SupplyItem>();
        this.lastDurabilityNotify = -1;
        this.lastDurabilityTime = 0L;
        this.scrollLastUse = 0L;
        this.paperDropLastUse = 0L;
        this.addSettings(this.autoTill, this.autoDropPaper, this.notifyWarp, this.notifyBan, this.notifyHighlight, this.scrollUse, this.scrollBind, this.renderChests, this.renderGifts, this.optimizeDisplays, this.staffAlert, this.staffPrefixes, this.staffEsp);
    }
    
    @Override
    public void onEvent() {
        final EventListener tick = TickEvent.getInstance().subscribe(new Listener<TickEvent>(e -> this.onTick()));
        final EventListener render = Render2DEvent.getInstance().subscribe(new Listener<Render2DEvent.Render2DEventData>(this::onRender));
        final EventListener packet = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(this::onPacket));
        this.addEvents(tick, render, packet);
    }
    
    private void onPacket(final PacketEvent.PacketEventData event) {
        if (event.isSend() || AresMineModule.mc.field_1687 == null || AresMineModule.mc.field_1724 == null) {
            return;
        }
        final class_2596<?> packet = event.packet();
        if (packet instanceof final class_7439 p) {
            final String raw = p.comp_763().getString();
            final String clean = raw.replaceAll("(?i)§[0-9a-fklmnor]", "").replace("\n", " ").trim();
            if (this.notifyWarp.getValue()) {
                final Matcher wm = AresMineModule.WARP_PATTERN.matcher(clean);
                if (wm.find()) {
                    this.notify("§a\u0412\u0430\u0440\u043f: §f" + wm.group(1).trim());
                }
            }
            if (this.notifyBan.getValue() && clean.contains("\u0437\u0430\u0431\u0430\u043d\u0438\u043b") && clean.contains("\u041f\u043e \u043f\u0440\u0438\u0447\u0438\u043d\u0435:")) {
                final Matcher bm = AresMineModule.BAN_PATTERN.matcher(clean);
                if (bm.find()) {
                    this.notify("§c[\u0411\u0410\u041d] §9" + bm.group(1).trim() + " §f\u2192 §c" + bm.group(2).trim() + " §e\u041f\u0440\u0438\u0447\u0438\u043d\u0430: §c" + bm.group(3).trim());
                }
            }
            if (this.notifyHighlight.getValue() && clean.contains("\u041e\u043a\u043e\u043b\u043e \u0412\u0430\u0441 \u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u043b\u0441\u044f \u0438\u0433\u0440\u043e\u043a")) {
                this.notify("§c[\u0412\u041d\u0418\u041c\u0410\u041d\u0418\u0415] §f\u041e\u043a\u043e\u043b\u043e \u0432\u0430\u0441 \u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u043b\u0441\u044f \u0438\u0433\u0440\u043e\u043a!");
            }
        }
    }
    
    private void onTick() {
        if (AresMineModule.mc.field_1724 == null || AresMineModule.mc.field_1687 == null) {
            return;
        }
        if (this.autoTill.getValue()) {
            this.doAutoTill();
        }
        if (this.autoDropPaper.getValue()) {
            this.doDropPaper();
        }
        if (this.staffAlert.getValue()) {
            this.doStaffAlert();
        }
        if (this.scrollUse.getValue()) {
            this.doScrollUse();
        }
        if (this.optimizeDisplays.getValue()) {
            this.doOptimizeDisplays();
        }
    }
    
    private void onRender(final Render2DEvent.Render2DEventData event) {
        final class_4587 ms = event.matrixStack();
        if (this.renderChests.getValue() || this.renderGifts.getValue()) {
            this.updateVisualItems();
            for (final SupplyItem item : this.visualItems) {
                this.renderSupplyItem(ms, item);
            }
        }
        if (this.staffAlert.getValue() && this.staffEsp.getValue()) {
            this.doStaffEsp(ms);
        }
    }
    
    private void doAutoTill() {
        final class_1799 main = AresMineModule.mc.field_1724.method_6047();
        if (!(main.method_7909() instanceof class_1794)) {
            if (AresMineModule.mc.field_1690.field_1886.method_1434()) {
                AresMineModule.mc.field_1690.field_1886.method_23481(false);
            }
            return;
        }
        final int remaining = main.method_7936() - main.method_7919();
        final long now = System.currentTimeMillis();
        if (remaining <= 10) {
            if (remaining != this.lastDurabilityNotify || now - this.lastDurabilityTime > 3000L) {
                this.notify("§c\u041c\u043e\u0442\u044b\u0433\u0430 \u043f\u043e\u0447\u0442\u0438 \u0441\u043b\u043e\u043c\u0430\u043d\u0430! \u041e\u0441\u0442\u0430\u043b\u043e\u0441\u044c §f" + remaining + " §c\u0438\u0441\u043f.");
                this.lastDurabilityNotify = remaining;
                this.lastDurabilityTime = now;
            }
            AresMineModule.mc.field_1690.field_1886.method_23481(false);
            return;
        }
        this.lastDurabilityNotify = -1;
        final class_239 hit = AresMineModule.mc.field_1765;
        if (hit == null || hit.method_17783() != class_239.class_240.field_1332) {
            AresMineModule.mc.field_1690.field_1886.method_23481(true);
            return;
        }
        final class_3965 bhr = (class_3965)hit;
        final class_2680 state = AresMineModule.mc.field_1687.method_8320(bhr.method_17777());
        if (state.method_27852(class_2246.field_10362)) {
            AresMineModule.mc.field_1690.field_1886.method_23481(false);
        }
        else {
            AresMineModule.mc.field_1690.field_1886.method_23481(true);
            if (state.method_27852(class_2246.field_10566) || state.method_27852(class_2246.field_10219)) {
                AresMineModule.mc.field_1761.method_2896(AresMineModule.mc.field_1724, class_1268.field_5808, bhr);
            }
        }
    }
    
    private void doDropPaper() {
        if (System.currentTimeMillis() - this.paperDropLastUse < 10L) {
            return;
        }
        final class_1799 mainHand = AresMineModule.mc.field_1724.method_6047();
        final class_1799 offHand = AresMineModule.mc.field_1724.method_6079();
        boolean inHand = false;
        int handSlot = -1;
        if (!mainHand.method_7960() && mainHand.method_7909() == class_1802.field_8407) {
            final String name = mainHand.method_7964().getString();
            if (name.contains("\u0414\u043e\u043d\u0430\u0442") || name.contains("\u041f\u043e\u043c\u043e\u0449\u044c") || name.contains("\u041c\u0435\u043d\u044e")) {
                inHand = true;
                handSlot = AresMineModule.mc.field_1724.method_31548().field_7545 + 36;
            }
        }
        if (!offHand.method_7960() && offHand.method_7909() == class_1802.field_8407 && !inHand) {
            final String name = offHand.method_7964().getString();
            if (name.contains("\u0414\u043e\u043d\u0430\u0442") || name.contains("\u041f\u043e\u043c\u043e\u0449\u044c") || name.contains("\u041c\u0435\u043d\u044e")) {
                inHand = true;
                handSlot = 45;
            }
        }
        if (inHand) {
            this.paperDropLastUse = System.currentTimeMillis();
            return;
        }
        for (int i = 0; i < 36; ++i) {
            final class_1799 stack = AresMineModule.mc.field_1724.method_31548().method_5438(i);
            if (!stack.method_7960()) {
                if (stack.method_7909() == class_1802.field_8407) {
                    final String name2 = stack.method_7964().getString();
                    if (name2.contains("\u0414\u043e\u043d\u0430\u0442") || name2.contains("\u041f\u043e\u043c\u043e\u0449\u044c") || name2.contains("\u041c\u0435\u043d\u044e")) {
                        final int screenSlot = (i < 9) ? (i + 36) : i;
                        SlownessManager.applySlowness(100L, 1L, () -> {
                            if (AresMineModule.mc.field_1724 == null) {
                                return;
                            }
                            else {
                                AresMineModule.mc.field_1761.method_2906(AresMineModule.mc.field_1724.field_7512.field_7763, screenSlot, 1, class_1713.field_7795, (class_1657)AresMineModule.mc.field_1724);
                                return;
                            }
                        });
                        this.paperDropLastUse = System.currentTimeMillis();
                        break;
                    }
                }
            }
        }
    }
    
    private void doStaffAlert() {
        final Set<String> nearStaff = new HashSet<String>();
        for (class_1297 entity : AresMineModule.mc.field_1687.method_18112()) {
            if (!(entity instanceof class_1657)) {
                continue;
            }
            final String name = entity.method_5477().getString();
            if (name.equals(AresMineModule.mc.field_1724.method_5477().getString())) {
                continue;
            }
            if (!this.isStaffPlayer(name)) {
                continue;
            }
            final double dist = AresMineModule.mc.field_1724.method_5739(entity);
            if (dist > 50.0) {
                continue;
            }
            nearStaff.add(name);
            if (this.alreadyAlerted.contains(name)) {
                continue;
            }
            this.alreadyAlerted.add(name);
            this.notify("§c[\u0421\u0422\u0410\u0424\u0424] §f" + name + " \u0440\u044f\u0434\u043e\u043c! (" + (int)dist + " \u0431\u043b\u043e\u043a\u043e\u0432)");
        }
        this.alreadyAlerted.retainAll(nearStaff);
    }
    
    private void doScrollUse() {
        final int key = this.scrollBind.getValue();
        if (key < 0 || AresMineModule.mc.field_1755 != null) {
            return;
        }
        if (AresMineModule.mc.field_1705 != null && AresMineModule.mc.field_1705.method_1743() != null && AresMineModule.mc.field_1705.method_1743().method_1819()) {
            return;
        }
        if (!class_3675.method_15987(AresMineModule.mc.method_22683().method_4490(), key)) {
            return;
        }
        if (System.currentTimeMillis() - this.scrollLastUse < 500L) {
            return;
        }
        final int currentSlot = AresMineModule.mc.field_1724.method_31548().field_7545;
        for (int i = 0; i < 36; ++i) {
            final class_1799 stack = AresMineModule.mc.field_1724.method_31548().method_5438(i);
            if (!stack.method_7960()) {
                if (stack.method_7909() == class_1802.field_8600) {
                    if (stack.method_7964().getString().contains("\u0421\u0432\u0438\u0442\u043e\u043a")) {
                        this.scrollLastUse = System.currentTimeMillis();
                        if (i < 9) {
                            InventoryUtil.swapToSlot(i);
                            InventoryUtil.useItem(class_1268.field_5808);
                            InventoryUtil.swapToSlot(currentSlot);
                            return;
                        }
                        final int hotbar = InventoryUtil.findBestSlotInHotBar();
                        if (hotbar == -1) {
                            return;
                        }
                        final int fi = i;
                        final int fh = hotbar;
                        final int fc = currentSlot;
                        SlownessManager.applySlowness(150L, 1L, () -> {
                            InventoryUtil.swapSlots(fi, fh);
                            InventoryUtil.swapToSlot(fh);
                            InventoryUtil.useItem(class_1268.field_5808);
                            return;
                        });
                        SlownessManager.applySlowness(300L, 150L, () -> {
                            InventoryUtil.swapSlots(fi, fh);
                            InventoryUtil.swapToSlot(fc);
                        });
                        return;
                    }
                }
            }
        }
    }
    
    private void doOptimizeDisplays() {
        final List<Integer> toRemove = new ArrayList<Integer>();
        for (final class_1297 entity : AresMineModule.mc.field_1687.method_18112()) {
            if (entity instanceof final class_8113.class_8123 td) {
                final class_2561 text = ((ITextDisplayEntity)td).invokeGetText();
                if (text == null) {
                    toRemove.add(entity.method_5628());
                }
                else {
                    final String str = text.getString();
                    if (str.contains("\u0421\u0423\u041d\u0414\u0423\u041a \u0421 \u041f\u0420\u0418\u041f\u0410\u0421\u0410\u041c\u0418") || str.contains("\u041d\u0435\u0431\u0435\u0441\u043d\u044b\u0439 \u0434\u0430\u0440")) {
                        continue;
                    }
                    toRemove.add(entity.method_5628());
                }
            }
        }
        for (final int id : toRemove) {
            AresMineModule.mc.field_1687.method_2945(id, class_1297.class_5529.field_26999);
        }
    }
    
    private void updateVisualItems() {
        this.visualItems.clear();
        if (AresMineModule.mc.field_1687 == null) {
            return;
        }
        for (final class_1297 entity : AresMineModule.mc.field_1687.method_18112()) {
            if (entity instanceof final class_8113.class_8123 td) {
                final class_2561 text = ((ITextDisplayEntity)td).invokeGetText();
                if (text == null) {
                    continue;
                }
                final String full = text.getString();
                final boolean isChest = this.renderChests.getValue() && full.contains("\u0421\u0423\u041d\u0414\u0423\u041a \u0421 \u041f\u0420\u0418\u041f\u0410\u0421\u0410\u041c\u0418");
                final boolean isGift = this.renderGifts.getValue() && full.contains("\u041d\u0435\u0431\u0435\u0441\u043d\u044b\u0439 \u0434\u0430\u0440");
                if (!isChest && !isGift) {
                    continue;
                }
                String itemName = "";
                String timeLeft = "";
                for (final class_2561 sib : text.method_10855()) {
                    final String s = sib.getString();
                    if (s.contains("\u0421\u0423\u041d\u0414\u0423\u041a \u0421 \u041f\u0420\u0418\u041f\u0410\u0421\u0410\u041c\u0418") || s.contains("\u041d\u0435\u0431\u0435\u0441\u043d\u044b\u0439 \u0434\u0430\u0440")) {
                        itemName = s;
                    }
                    else {
                        if (!s.contains("\u0414\u043e \u043f\u043e\u043f\u043e\u043b\u043d\u0435\u043d\u0438\u044f:") && !s.contains("\u0414\u043e \u043e\u0442\u043a\u0440\u044b\u0442\u0438\u044f:")) {
                            continue;
                        }
                        timeLeft = s;
                    }
                }
                if (itemName.isEmpty()) {
                    itemName = full;
                }
                if (itemName.isEmpty() || timeLeft.isEmpty()) {
                    continue;
                }
                this.visualItems.add(new SupplyItem(entity.method_19538(), itemName, timeLeft));
            }
        }
    }
    
    private void renderSupplyItem(final class_4587 ms, final SupplyItem item) {
        final Vector2f screen = ProjectionUtil.project(item.position());
        if (screen.x == Float.MAX_VALUE || screen.y == Float.MAX_VALUE) {
            return;
        }
        final float padding = 4.0f;
        final float nameW = Fonts.PS_BOLD.getWidth(item.name(), 7.0f);
        final float timeW = Fonts.PS_BOLD.getWidth(item.timeLeft(), 7.0f);
        final float maxW = Math.max(nameW, timeW);
        final float totalW = maxW + padding * 2.0f;
        final float totalH = 20.0f;
        final float x = screen.x - totalW / 2.0f;
        final float y = screen.y - totalH / 2.0f;
        RenderUtil.BLUR_RECT.draw(ms, x, y, totalW, totalH, 2.0f, UIColors.blur());
        Fonts.PS_BOLD.drawText(ms, item.name(), x + padding + (maxW - nameW) / 2.0f, y + padding, 7.0f, new Color(-669631), 0.0f);
        Fonts.PS_BOLD.drawText(ms, item.timeLeft(), x + padding + (maxW - timeW) / 2.0f, y + padding + 9.0f, 7.0f, UIColors.textColor(), 0.0f);
    }
    
    private void doStaffEsp(final class_4587 ms) {
        for (class_1297 entity : AresMineModule.mc.field_1687.method_18112()) {
            if (!(entity instanceof class_1657)) {
                continue;
            }
            final String name = entity.method_5477().getString();
            if (name.equals(AresMineModule.mc.field_1724.method_5477().getString())) {
                continue;
            }
            if (!this.isStaffPlayer(name)) {
                continue;
            }
            final class_243 headPos = entity.method_19538().method_1031(0.0, entity.method_17682() + 0.35, 0.0);
            final Vector2f screen = ProjectionUtil.project(headPos);
            if (screen.x == Float.MAX_VALUE) {
                continue;
            }
            if (screen.y == Float.MAX_VALUE) {
                continue;
            }
            final String tag = "[Staff] " + name;
            final float tagW = Fonts.PS_BOLD.getWidth(tag, 7.0f);
            final float pad = 3.0f;
            RenderUtil.BLUR_RECT.draw(ms, screen.x - tagW / 2.0f - pad, screen.y - 5.0f, tagW + pad * 2.0f, 10.0f, 2.0f, UIColors.blur());
            Fonts.PS_BOLD.drawText(ms, tag, screen.x - tagW / 2.0f, screen.y - 2.0f, 7.0f, new Color(-43691), 0.0f);
        }
    }
    
    private boolean isStaffPlayer(final String playerName) {
        if (AresMineModule.mc.method_1562() == null) {
            return false;
        }
        for (final class_640 entry : AresMineModule.mc.method_1562().method_2880()) {
            if (!entry.method_2966().getName().equalsIgnoreCase(playerName)) {
                continue;
            }
            final StringBuilder sb = new StringBuilder();
            if (entry.method_2955() != null) {
                sb.append(entry.method_2955().method_1144().getString());
            }
            if (entry.method_2971() != null) {
                sb.append(entry.method_2971().getString());
            }
            final String upper = sb.toString().replaceAll("(?i)§[0-9a-fklmnor]", "").toUpperCase();
            if (this.staffPrefixes.isEnabled("Staff") && upper.contains("STAFF")) {
                return true;
            }
            if (this.staffPrefixes.isEnabled("Admin") && upper.contains("ADMIN")) {
                return true;
            }
            if (this.staffPrefixes.isEnabled("Moder") && (upper.contains("MODER") || upper.contains("\u041c\u041e\u0414\u0415\u0420"))) {
                return true;
            }
            if (this.staffPrefixes.isEnabled("GM") && upper.contains("GM")) {
                return true;
            }
            if (this.staffPrefixes.isEnabled("Helper") && upper.contains("HELPER")) {
                return true;
            }
            break;
        }
        return false;
    }
    
    private void notify(final String message) {
        TextUtil.sendMessage(message);
    }
    
    @Generated
    public static AresMineModule getInstance() {
        return AresMineModule.instance;
    }
    
    static {
        instance = new AresMineModule();
        BAN_PATTERN = Pattern.compile("[^\\w]*(?:\u0418\u0433\u0440\u043e\u043a|Player) (.+?) \u0437\u0430\u0431\u0430\u043d\u0438\u043b \u0438\u0433\u0440\u043e\u043a\u0430 (.+?)\\s*[^\\w]*\u041f\u043e \u043f\u0440\u0438\u0447\u0438\u043d\u0435: (.+)");
        WARP_PATTERN = Pattern.compile("\u0412\u044b \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u043d\u044b \u043d\u0430 \u0432\u0430\u0440\u043f (.+)");
    }
    
    record SupplyItem(class_243 position, String name, String timeLeft) {}
}
