/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.class_1799
 *  net.minecraft.class_2960
 *  net.minecraft.class_408
 *  net.minecraft.class_4587
 */
package sweetie.leonware.client.ui.widget.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.class_1799;
import net.minecraft.class_2960;
import net.minecraft.class_408;
import net.minecraft.class_4587;
import sweetie.leonware.api.event.events.render.Render2DEvent;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleManager;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.system.files.FileUtil;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.notification.NotificationType;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.fonts.Fonts;
import sweetie.leonware.client.ui.widget.Widget;

public class NotifWidget
extends Widget {
    private final class_2960 star = FileUtil.getImage("particles/star1");
    private final class_2960 icSuccess = FileUtil.getImage("notifications/success");
    private final class_2960 icInfo = FileUtil.getImage("notifications/info");
    private final class_2960 icError = FileUtil.getImage("notifications/error");
    private final class_2960 icWarning = FileUtil.getImage("notifications/warning");
    private boolean wasChatOpen;
    private long lastDuraCheck;
    private final List<Notif> notifs = new CopyOnWriteArrayList<Notif>();
    public final ModeSetting notifStyle = new ModeSetting("Notifications").values("\u0421\u0442\u0430\u043d\u0434\u0430\u0440\u0442", "Celestial").value("\u0421\u0442\u0430\u043d\u0434\u0430\u0440\u0442");
    public final MultiBooleanSetting notifTypes = new MultiBooleanSetting("Notification").value(new BooleanSetting("\u041f\u0440\u043e\u0441\u044c\u0431\u0430 \u043e \u043d\u0430\u0431\u043b\u044e\u0434\u0435\u043d\u0438\u0438").value(false), new BooleanSetting("\u0421\u043e\u0441\u0442\u043e\u044f\u043d\u0438\u0435 \u043c\u043e\u0434\u0443\u043b\u0435\u0439").value(false), new BooleanSetting("\u041d\u0438\u0437\u043a\u0430\u044f \u043f\u0440\u043e\u0447\u043d\u043e\u0441\u0442\u044c").value(false));
    private final Map<Notif, Float> slideX = new ConcurrentHashMap<Notif, Float>();
    private final List<Notif> reversedBuffer = new ArrayList<Notif>();
    private Map<String, Boolean> modStates = new HashMap<String, Boolean>();

    public NotifWidget() {
        super(0.0f, 0.0f);
        this.setEnabled(true);
    }

    @Override
    public String getName() {
        return "Notification";
    }

    public void addNotif(String text) {
        this.notifs.add(new Notif(text));
    }

    public void addNotif(String title, String content, NotificationType type) {
        this.notifs.add(new Notif(title, content, type));
    }

    @Override
    public void render(Render2DEvent.Render2DEventData event) {
        boolean chatOpen;
        if (this.getDraggable().getX() == 0.0f && this.getDraggable().getY() == 0.0f) {
            this.getDraggable().setX((float)mc.method_22683().method_4486() / 2.0f);
            this.getDraggable().setY((float)mc.method_22683().method_4502() / 2.0f);
        }
        class_4587 ms = event.matrixStack();
        this.notifs.removeIf(Notif::shouldRemove);
        if (this.notifStyle.is("Celestial")) {
            this.renderCelestial(ms);
        } else {
            this.renderStandard(ms);
        }
        if (this.notifTypes.isEnabled("\u041d\u0438\u0437\u043a\u0430\u044f \u043f\u0440\u043e\u0447\u043d\u043e\u0441\u0442\u044c") && System.currentTimeMillis() - this.lastDuraCheck > 5000L) {
            this.checkDura();
            this.lastDuraCheck = System.currentTimeMillis();
        }
        if (!(chatOpen = NotifWidget.mc.field_1755 instanceof class_408)) {
            return;
        }
        this.wasChatOpen = chatOpen;
        if (!this.notifStyle.is("Celestial")) {
            this.renderPreview(ms);
        }
    }

    private void renderCelestial(class_4587 ms) {
        int sw = mc.method_22683().method_4486();
        int sh = mc.method_22683().method_4502();
        float scaledWidth = (float)sw + this.scaled(10.0f);
        float anchorY = (float)sh - this.scaled(60.0f);
        float notifH = this.scaled(28.0f);
        float barW = this.scaled(2.0f);
        float padX = this.scaled(5.0f);
        float titleFs = this.scaled(7.0f);
        float contFs = this.scaled(6.0f);
        float iconSz = this.scaled(10.0f);
        float iconPad = this.scaled(4.0f);
        float round = 0.0f;
        float y = anchorY;
        List<Notif> reversed = this.reversedBuffer;
        reversed.clear();
        for (int i = this.notifs.size() - 1; i >= 0; --i) {
            reversed.add(this.notifs.get(i));
        }
        int total = reversed.size();
        for (int idx = 0; idx < total; ++idx) {
            Notif n = reversed.get(idx);
            float alpha = n.getAlpha();
            if (alpha <= 0.01f) {
                if (this.notifs.size() <= 1) continue;
                y -= this.scaled(35.0f);
                continue;
            }
            float titleW = Fonts.PS_MEDIUM.getWidth(n.title, titleFs);
            boolean hasCon = n.content != null && !n.content.isEmpty();
            float contW = hasCon ? Fonts.PS_MEDIUM.getWidth(n.content, contFs) : 0.0f;
            float notifW = Math.max(this.scaled(100.0f), Math.max(titleW, contW) + iconSz + iconPad + padX * 2.0f + barW + this.scaled(10.0f));
            float cur = this.slideX.getOrDefault(n, Float.valueOf(scaledWidth)).floatValue();
            float targetX = alpha > 0.15f ? scaledWidth - notifW : scaledWidth;
            cur += (targetX - cur) * 0.12f;
            this.slideX.put(n, Float.valueOf(cur));
            float rx = cur;
            float ry = y;
            int fullAlpha = (int)(alpha * 255.0f);
            Color typeColor = n.type.color;
            RenderUtil.RECT.draw(ms, rx, ry, barW, notifH, 0.0f, new Color(typeColor.getRed(), typeColor.getGreen(), typeColor.getBlue(), fullAlpha));
            RenderUtil.BLUR_RECT.draw(ms, rx + barW, ry, notifW - barW, notifH, round, UIColors.widgetBlur((int)((float)fullAlpha * 0.5f)));
            class_2960 icon = this.getIcon(n.type);
            float ix = rx + barW + padX;
            float iy = ry + notifH / 2.0f - iconSz / 2.0f;
            try {
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                int tex = mc.method_1531().method_4619(icon).method_4624();
                RenderUtil.TEXTURE_RECT.draw(ms, ix, iy, iconSz, iconSz, 0.0f, new Color(255, 255, 255, fullAlpha), 0.0f, 0.0f, 1.0f, 1.0f, tex);
            }
            catch (Exception e) {
                RenderUtil.RECT.draw(ms, ix, iy, iconSz, iconSz, this.scaled(1.0f), new Color(typeColor.getRed(), typeColor.getGreen(), typeColor.getBlue(), fullAlpha));
            }
            float textX = ix + iconSz + iconPad;
            float titleGradW = Fonts.PS_MEDIUM.getWidth(n.title, titleFs);
            if (hasCon) {
                Fonts.PS_MEDIUM.drawGradientText(ms, n.title, textX, ry + this.scaled(4.0f), titleFs, UIColors.primary(fullAlpha), UIColors.secondary(fullAlpha), titleGradW / 4.0f);
                Fonts.PS_MEDIUM.drawText(ms, n.content, textX, ry + this.scaled(17.0f), contFs, UIColors.textColor(fullAlpha));
            } else {
                Fonts.PS_MEDIUM.drawGradientText(ms, n.title, textX, ry + notifH / 2.0f - titleFs / 2.0f, titleFs, UIColors.primary(fullAlpha), UIColors.secondary(fullAlpha), titleGradW / 4.0f);
            }
            if (this.notifs.size() <= 1) continue;
            y -= this.scaled(35.0f);
        }
        this.slideX.keySet().retainAll(this.notifs);
        this.getDraggable().setWidth(this.scaled(200.0f));
        this.getDraggable().setHeight(this.scaled(60.0f));
    }

    private class_2960 getIcon(NotificationType type) {
        return switch (type) {
            case NotificationType.SUCCESS -> this.icSuccess;
            case NotificationType.ERROR -> this.icError;
            case NotificationType.WARNING -> this.icWarning;
            default -> this.icInfo;
        };
    }

    private void renderStandard(class_4587 ms) {
        if (this.notifs.size() > 7) {
            int excess = this.notifs.size() - 7;
            for (int i = 0; i < excess; ++i) {
                this.notifs.get(i).speedUp();
            }
        }
        float left = this.getDraggable().getX();
        float top = this.getDraggable().getY();
        float maxW = this.scaled(150.0f);
        for (Notif n : this.notifs) {
            if (n.getAlpha() <= 0.05f) continue;
            String txt = this.stripColor(n.text);
            float w = this.scaled(5.0f) + this.scaled(10.0f) + this.scaled(4.0f) + this.getMediumFont().getWidth(txt, this.scaled(7.0f)) + this.scaled(5.0f);
            if (!(w > maxW)) continue;
            maxW = w;
        }
        float cx = left + maxW / 2.0f;
        float yOff = 0.0f;
        for (Notif n : this.notifs) {
            float v = n.getAlpha();
            if (v <= 0.05f) continue;
            String txt = this.stripColor(n.text);
            float font = this.scaled(7.0f);
            float pad = this.scaled(5.0f);
            float icon = this.scaled(10.0f);
            float gap = this.scaled(4.0f);
            float r = this.scaled(3.0f);
            float txtW = this.getMediumFont().getWidth(txt, font);
            float w = pad + icon + gap + txtW + pad;
            float h = icon + pad * 2.0f;
            float x = cx - w / 2.0f;
            float y = top + yOff;
            ms.method_22903();
            ms.method_46416(cx, y + h / 2.0f, 0.0f);
            ms.method_22905(v, v, 1.0f);
            ms.method_46416(-cx, -(y + h / 2.0f), 0.0f);
            int bgAlpha = (int)(230.0f * v);
            boolean isEnabled = n.text.contains("\u00a7a") || n.text.contains("\u0432\u043a\u043b\u044e\u0447\u0435\u043d");
            Color iconColor = isEnabled ? new Color(100, 255, 100) : new Color(255, 100, 100);
            Color glowColor = new Color(iconColor.getRed(), iconColor.getGreen(), iconColor.getBlue(), (int)(255.0f * v));
            RenderUtil.GLOW_RECT.draw(ms, x, y, w, h, r, glowColor, 8.0f, 0.8f);
            RenderUtil.BLUR_RECT.draw(ms, x, y, w, h, r, new Color(20, 20, 30, bgAlpha));
            float ix = x + pad;
            float iy = y + h / 2.0f - icon / 2.0f;
            int iconAlpha = (int)(255.0f * v);
            try {
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                int tex = mc.method_1531().method_4619(this.star).method_4624();
                RenderUtil.TEXTURE_RECT.draw(ms, ix, iy, icon, icon, this.scaled(1.5f), new Color(iconColor.getRed(), iconColor.getGreen(), iconColor.getBlue(), iconAlpha), 0.0f, 0.0f, 1.0f, 1.0f, tex);
            }
            catch (Exception e) {
                RenderUtil.RECT.draw(ms, ix, iy, icon, icon, this.scaled(1.5f), new Color(iconColor.getRed(), iconColor.getGreen(), iconColor.getBlue(), iconAlpha));
            }
            this.getMediumFont().drawText(ms, txt, ix + icon + gap, y + h / 2.0f - font / 2.0f, font, new Color(255, 255, 255, (int)(255.0f * v)));
            ms.method_22909();
            yOff += (h + this.scaled(3.0f)) * v;
        }
        this.getDraggable().setWidth(maxW);
        this.getDraggable().setHeight(Math.max(yOff, this.scaled(30.0f)));
    }

    private void renderPreview(class_4587 ms) {
        float pad = this.scaled(5.0f);
        float icon = this.scaled(10.0f);
        float gap = this.scaled(4.0f);
        float font = this.scaled(7.0f);
        String txt = "\u0422\u0435\u0441\u0442\u043e\u0432\u043e\u0435 \u0443\u0432\u0435\u0434\u043e\u043c\u043b\u0435\u043d\u0438\u0435, \u0435\u0433\u043e \u043c\u043e\u0436\u043d\u043e \u043f\u0435\u0440\u0435\u043c\u0435\u0449\u0430\u0442\u044c";
        float txtW = this.getMediumFont().getWidth(txt, font);
        float w = pad + icon + gap + txtW + pad;
        float h = icon + pad * 2.0f;
        float r = this.scaled(3.0f);
        float cx = this.getDraggable().getX() + (this.getDraggable().getWidth() > 0.0f ? this.getDraggable().getWidth() / 2.0f : this.scaled(100.0f));
        float x = cx - w / 2.0f;
        float y = this.getDraggable().getY() + this.scaled(15.0f);
        RenderUtil.GLOW_RECT.draw(ms, x, y, w, h, r, new Color(255, 80, 80, 180), 8.0f, 0.8f);
        RenderUtil.BLUR_RECT.draw(ms, x, y, w, h, r, new Color(20, 20, 30, 230));
        float ix = x + pad;
        float iy = y + h / 2.0f - icon / 2.0f;
        try {
            int tex = mc.method_1531().method_4619(this.star).method_4624();
            RenderUtil.TEXTURE_RECT.draw(ms, ix, iy, icon, icon, this.scaled(1.5f), new Color(255, 80, 80), 0.0f, 0.0f, 1.0f, 1.0f, tex);
        }
        catch (Exception e) {
            RenderUtil.RECT.draw(ms, ix, iy, icon, icon, this.scaled(1.5f), new Color(255, 80, 80));
        }
        this.getMediumFont().drawText(ms, txt, ix + icon + gap, y + h / 2.0f - font / 2.0f, font, UIColors.textColor());
    }

    private String stripColor(String s) {
        return s.replace("\u00a7a", "").replace("\u00a7c", "").replace("\u00a7", "");
    }

    public void checkMods() {
        if (!this.notifTypes.isEnabled("\u0421\u043e\u0441\u0442\u043e\u044f\u043d\u0438\u0435 \u043c\u043e\u0434\u0443\u043b\u0435\u0439")) {
            return;
        }
        for (Module m : ModuleManager.getInstance().getModules()) {
            boolean now;
            String name = m.getName();
            boolean was = this.modStates.getOrDefault(name, m.isEnabled());
            if (was == (now = m.isEnabled())) continue;
            this.addNotif(name, now ? "\u0412\u043a\u043b\u044e\u0447\u0435\u043d" : "\u0412\u044b\u043a\u043b\u044e\u0447\u0435\u043d", now ? NotificationType.SUCCESS : NotificationType.ERROR);
            this.modStates.put(name, now);
        }
    }

    private void checkDura() {
        if (NotifWidget.mc.field_1724 == null) {
            return;
        }
        for (class_1799 stack : NotifWidget.mc.field_1724.method_31548().field_7547) {
            int left;
            if (stack.method_7960() || !stack.method_7963() || (left = stack.method_7936() - stack.method_7919()) <= 0 || left >= 35) continue;
            this.addNotif("\u041f\u0440\u043e\u0447\u043d\u043e\u0441\u0442\u044c", "\u041d\u0438\u0437\u043a\u0430\u044f: " + stack.method_7964().getString(), NotificationType.WARNING);
            break;
        }
    }

    @Override
    public void render(class_4587 matrixStack) {
    }

    private static class Notif {
        final String text;
        final String title;
        final String content;
        final NotificationType type;
        private final long start;
        private long dur = 4000L;
        private boolean expired;

        Notif(String text) {
            this(text, "", NotificationType.INFO);
        }

        Notif(String title, String content, NotificationType type) {
            this.text = title;
            this.title = title;
            this.content = content;
            this.type = type;
            this.start = System.currentTimeMillis();
        }

        void speedUp() {
            long elapsed = System.currentTimeMillis() - this.start;
            if (this.dur > elapsed + 300L) {
                this.dur = elapsed + 300L;
            }
        }

        float getAlpha() {
            long e = System.currentTimeMillis() - this.start;
            if (e < 300L) {
                return (float)e / 300.0f;
            }
            if (e < this.dur - 300L) {
                return 1.0f;
            }
            if (e < this.dur) {
                return 1.0f - (float)(e - (this.dur - 300L)) / 300.0f;
            }
            return 0.0f;
        }

        boolean shouldRemove() {
            if (!this.expired && System.currentTimeMillis() - this.start > this.dur) {
                this.expired = true;
            }
            return this.expired && this.getAlpha() <= 0.05f;
        }
    }
}

