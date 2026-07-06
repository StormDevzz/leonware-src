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
import org.newsclub.net.unix.AFVSOCKSocketAddress;
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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/widget/overlay/NotifWidget.class */
public class NotifWidget extends Widget {
    private final class_2960 star;
    private final class_2960 icSuccess;
    private final class_2960 icInfo;
    private final class_2960 icError;
    private final class_2960 icWarning;
    private boolean wasChatOpen;
    private long lastDuraCheck;
    private final List<Notif> notifs;
    public final ModeSetting notifStyle;
    public final MultiBooleanSetting notifTypes;
    private final Map<Notif, Float> slideX;
    private final List<Notif> reversedBuffer;
    private Map<String, Boolean> modStates;

    public NotifWidget() {
        super(0.0f, 0.0f);
        this.star = FileUtil.getImage("particles/star1");
        this.icSuccess = FileUtil.getImage("notifications/success");
        this.icInfo = FileUtil.getImage("notifications/info");
        this.icError = FileUtil.getImage("notifications/error");
        this.icWarning = FileUtil.getImage("notifications/warning");
        this.notifs = new CopyOnWriteArrayList();
        this.notifStyle = new ModeSetting("Notifications").values("Стандарт", "Celestial").value("Стандарт");
        this.notifTypes = new MultiBooleanSetting("Notification").value(new BooleanSetting("Просьба о наблюдении").value((Boolean) false), new BooleanSetting("Состояние модулей").value((Boolean) false), new BooleanSetting("Низкая прочность").value((Boolean) false));
        this.slideX = new ConcurrentHashMap();
        this.reversedBuffer = new ArrayList();
        this.modStates = new HashMap();
        setEnabled(true);
    }

    @Override // sweetie.leonware.client.ui.widget.Widget
    public String getName() {
        return "Notification";
    }

    public void addNotif(String text) {
        this.notifs.add(new Notif(text));
    }

    public void addNotif(String title, String content, NotificationType type) {
        this.notifs.add(new Notif(title, content, type));
    }

    @Override // sweetie.leonware.client.ui.widget.Widget
    public void render(Render2DEvent.Render2DEventData event) {
        if (getDraggable().getX() == 0.0f && getDraggable().getY() == 0.0f) {
            getDraggable().setX(mc.method_22683().method_4486() / 2.0f);
            getDraggable().setY(mc.method_22683().method_4502() / 2.0f);
        }
        class_4587 ms = event.matrixStack();
        this.notifs.removeIf((v0) -> {
            return v0.shouldRemove();
        });
        if (this.notifStyle.is("Celestial")) {
            renderCelestial(ms);
        } else {
            renderStandard(ms);
        }
        if (this.notifTypes.isEnabled("Низкая прочность") && System.currentTimeMillis() - this.lastDuraCheck > 5000) {
            checkDura();
            this.lastDuraCheck = System.currentTimeMillis();
        }
        boolean chatOpen = mc.field_1755 instanceof class_408;
        if (chatOpen) {
            this.wasChatOpen = chatOpen;
            if (!this.notifStyle.is("Celestial")) {
                renderPreview(ms);
            }
        }
    }

    private void renderCelestial(class_4587 ms) {
        int sw = mc.method_22683().method_4486();
        int sh = mc.method_22683().method_4502();
        float scaledWidth = sw + scaled(10.0f);
        float anchorY = sh - scaled(60.0f);
        float notifH = scaled(28.0f);
        float barW = scaled(2.0f);
        float padX = scaled(5.0f);
        float titleFs = scaled(7.0f);
        float contFs = scaled(6.0f);
        float iconSz = scaled(10.0f);
        float iconPad = scaled(4.0f);
        float y = anchorY;
        List<Notif> reversed = this.reversedBuffer;
        reversed.clear();
        for (int i = this.notifs.size() - 1; i >= 0; i--) {
            reversed.add(this.notifs.get(i));
        }
        int total = reversed.size();
        for (int idx = 0; idx < total; idx++) {
            Notif n = reversed.get(idx);
            float alpha = n.getAlpha();
            if (alpha <= 0.01f) {
                if (this.notifs.size() > 1) {
                    y -= scaled(35.0f);
                }
            } else {
                float titleW = Fonts.PS_MEDIUM.getWidth(n.title, titleFs);
                boolean hasCon = (n.content == null || n.content.isEmpty()) ? false : true;
                float contW = hasCon ? Fonts.PS_MEDIUM.getWidth(n.content, contFs) : 0.0f;
                float notifW = Math.max(scaled(100.0f), Math.max(titleW, contW) + iconSz + iconPad + (padX * 2.0f) + barW + scaled(10.0f));
                float cur = this.slideX.getOrDefault(n, Float.valueOf(scaledWidth)).floatValue();
                float targetX = alpha > 0.15f ? scaledWidth - notifW : scaledWidth;
                float cur2 = cur + ((targetX - cur) * 0.12f);
                this.slideX.put(n, Float.valueOf(cur2));
                float ry = y;
                int fullAlpha = (int) (alpha * 255.0f);
                Color typeColor = n.type.color;
                RenderUtil.RECT.draw(ms, cur2, ry, barW, notifH, 0.0f, new Color(typeColor.getRed(), typeColor.getGreen(), typeColor.getBlue(), fullAlpha));
                RenderUtil.BLUR_RECT.draw(ms, cur2 + barW, ry, notifW - barW, notifH, 0.0f, UIColors.widgetBlur((int) (fullAlpha * 0.5f)));
                class_2960 icon = getIcon(n.type);
                float ix = cur2 + barW + padX;
                float iy = (ry + (notifH / 2.0f)) - (iconSz / 2.0f);
                try {
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    int tex = mc.method_1531().method_4619(icon).method_4624();
                    RenderUtil.TEXTURE_RECT.draw(ms, ix, iy, iconSz, iconSz, 0.0f, new Color(255, 255, 255, fullAlpha), 0.0f, 0.0f, 1.0f, 1.0f, tex);
                } catch (Exception e) {
                    RenderUtil.RECT.draw(ms, ix, iy, iconSz, iconSz, scaled(1.0f), new Color(typeColor.getRed(), typeColor.getGreen(), typeColor.getBlue(), fullAlpha));
                }
                float textX = ix + iconSz + iconPad;
                float titleGradW = Fonts.PS_MEDIUM.getWidth(n.title, titleFs);
                if (hasCon) {
                    Fonts.PS_MEDIUM.drawGradientText(ms, n.title, textX, ry + scaled(4.0f), titleFs, UIColors.primary(fullAlpha), UIColors.secondary(fullAlpha), titleGradW / 4.0f);
                    Fonts.PS_MEDIUM.drawText(ms, n.content, textX, ry + scaled(17.0f), contFs, UIColors.textColor(fullAlpha));
                } else {
                    Fonts.PS_MEDIUM.drawGradientText(ms, n.title, textX, (ry + (notifH / 2.0f)) - (titleFs / 2.0f), titleFs, UIColors.primary(fullAlpha), UIColors.secondary(fullAlpha), titleGradW / 4.0f);
                }
                if (this.notifs.size() > 1) {
                    y -= scaled(35.0f);
                }
            }
        }
        this.slideX.keySet().retainAll(this.notifs);
        getDraggable().setWidth(scaled(200.0f));
        getDraggable().setHeight(scaled(60.0f));
    }

    /* JADX INFO: renamed from: sweetie.leonware.client.ui.widget.overlay.NotifWidget$1, reason: invalid class name */
    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/widget/overlay/NotifWidget$1.class */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$sweetie$leonware$api$utils$notification$NotificationType = new int[NotificationType.values().length];

        static {
            try {
                $SwitchMap$sweetie$leonware$api$utils$notification$NotificationType[NotificationType.SUCCESS.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$sweetie$leonware$api$utils$notification$NotificationType[NotificationType.ERROR.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$sweetie$leonware$api$utils$notification$NotificationType[NotificationType.WARNING.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    private class_2960 getIcon(NotificationType type) {
        switch (AnonymousClass1.$SwitchMap$sweetie$leonware$api$utils$notification$NotificationType[type.ordinal()]) {
            case 1:
                return this.icSuccess;
            case AFVSOCKSocketAddress.VMADDR_CID_HOST /* 2 */:
                return this.icError;
            case 3:
                return this.icWarning;
            default:
                return this.icInfo;
        }
    }

    private void renderStandard(class_4587 ms) {
        if (this.notifs.size() > 7) {
            int excess = this.notifs.size() - 7;
            for (int i = 0; i < excess; i++) {
                this.notifs.get(i).speedUp();
            }
        }
        float left = getDraggable().getX();
        float top = getDraggable().getY();
        float maxW = scaled(150.0f);
        for (Notif n : this.notifs) {
            if (n.getAlpha() > 0.05f) {
                float w = scaled(5.0f) + scaled(10.0f) + scaled(4.0f) + getMediumFont().getWidth(stripColor(n.text), scaled(7.0f)) + scaled(5.0f);
                if (w > maxW) {
                    maxW = w;
                }
            }
        }
        float cx = left + (maxW / 2.0f);
        float yOff = 0.0f;
        for (Notif n2 : this.notifs) {
            float v = n2.getAlpha();
            if (v > 0.05f) {
                String txt = stripColor(n2.text);
                float font = scaled(7.0f);
                float pad = scaled(5.0f);
                float icon = scaled(10.0f);
                float gap = scaled(4.0f);
                float r = scaled(3.0f);
                float txtW = getMediumFont().getWidth(txt, font);
                float w2 = pad + icon + gap + txtW + pad;
                float h = icon + (pad * 2.0f);
                float x = cx - (w2 / 2.0f);
                float y = top + yOff;
                ms.method_22903();
                ms.method_46416(cx, y + (h / 2.0f), 0.0f);
                ms.method_22905(v, v, 1.0f);
                ms.method_46416(-cx, -(y + (h / 2.0f)), 0.0f);
                int bgAlpha = (int) (230.0f * v);
                boolean isEnabled = n2.text.contains("§a") || n2.text.contains("включен");
                Color iconColor = isEnabled ? new Color(100, 255, 100) : new Color(255, 100, 100);
                Color glowColor = new Color(iconColor.getRed(), iconColor.getGreen(), iconColor.getBlue(), (int) (255.0f * v));
                RenderUtil.GLOW_RECT.draw(ms, x, y, w2, h, r, glowColor, 8.0f, 0.8f);
                RenderUtil.BLUR_RECT.draw(ms, x, y, w2, h, r, new Color(20, 20, 30, bgAlpha));
                float ix = x + pad;
                float iy = (y + (h / 2.0f)) - (icon / 2.0f);
                int iconAlpha = (int) (255.0f * v);
                try {
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    int tex = mc.method_1531().method_4619(this.star).method_4624();
                    RenderUtil.TEXTURE_RECT.draw(ms, ix, iy, icon, icon, scaled(1.5f), new Color(iconColor.getRed(), iconColor.getGreen(), iconColor.getBlue(), iconAlpha), 0.0f, 0.0f, 1.0f, 1.0f, tex);
                } catch (Exception e) {
                    RenderUtil.RECT.draw(ms, ix, iy, icon, icon, scaled(1.5f), new Color(iconColor.getRed(), iconColor.getGreen(), iconColor.getBlue(), iconAlpha));
                }
                getMediumFont().drawText(ms, txt, ix + icon + gap, (y + (h / 2.0f)) - (font / 2.0f), font, new Color(255, 255, 255, (int) (255.0f * v)));
                ms.method_22909();
                yOff += (h + scaled(3.0f)) * v;
            }
        }
        getDraggable().setWidth(maxW);
        getDraggable().setHeight(Math.max(yOff, scaled(30.0f)));
    }

    private void renderPreview(class_4587 ms) {
        float pad = scaled(5.0f);
        float icon = scaled(10.0f);
        float gap = scaled(4.0f);
        float font = scaled(7.0f);
        float txtW = getMediumFont().getWidth("Тестовое уведомление, его можно перемещать", font);
        float w = pad + icon + gap + txtW + pad;
        float h = icon + (pad * 2.0f);
        float r = scaled(3.0f);
        float cx = getDraggable().getX() + (getDraggable().getWidth() > 0.0f ? getDraggable().getWidth() / 2.0f : scaled(100.0f));
        float x = cx - (w / 2.0f);
        float y = getDraggable().getY() + scaled(15.0f);
        RenderUtil.GLOW_RECT.draw(ms, x, y, w, h, r, new Color(255, 80, 80, 180), 8.0f, 0.8f);
        RenderUtil.BLUR_RECT.draw(ms, x, y, w, h, r, new Color(20, 20, 30, 230));
        float ix = x + pad;
        float iy = (y + (h / 2.0f)) - (icon / 2.0f);
        try {
            int tex = mc.method_1531().method_4619(this.star).method_4624();
            RenderUtil.TEXTURE_RECT.draw(ms, ix, iy, icon, icon, scaled(1.5f), new Color(255, 80, 80), 0.0f, 0.0f, 1.0f, 1.0f, tex);
        } catch (Exception e) {
            RenderUtil.RECT.draw(ms, ix, iy, icon, icon, scaled(1.5f), new Color(255, 80, 80));
        }
        getMediumFont().drawText(ms, "Тестовое уведомление, его можно перемещать", ix + icon + gap, (y + (h / 2.0f)) - (font / 2.0f), font, UIColors.textColor());
    }

    private String stripColor(String s) {
        return s.replace("§a", "").replace("§c", "").replace("§", "");
    }

    public void checkMods() {
        if (this.notifTypes.isEnabled("Состояние модулей")) {
            for (Module m : ModuleManager.getInstance().getModules()) {
                String name = m.getName();
                boolean was = this.modStates.getOrDefault(name, Boolean.valueOf(m.isEnabled())).booleanValue();
                boolean now = m.isEnabled();
                if (was != now) {
                    addNotif(name, now ? "Включен" : "Выключен", now ? NotificationType.SUCCESS : NotificationType.ERROR);
                    this.modStates.put(name, Boolean.valueOf(now));
                }
            }
        }
    }

    private void checkDura() {
        int left;
        if (mc.field_1724 == null) {
            return;
        }
        for (class_1799 stack : mc.field_1724.method_31548().field_7547) {
            if (!stack.method_7960() && stack.method_7963() && (left = stack.method_7936() - stack.method_7919()) > 0 && left < 35) {
                addNotif("Прочность", "Низкая: " + stack.method_7964().getString(), NotificationType.WARNING);
                return;
            }
        }
    }

    @Override // sweetie.leonware.api.system.interfaces.IRenderer
    public void render(class_4587 matrixStack) {
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/widget/overlay/NotifWidget$Notif.class */
    private static class Notif {
        final String text;
        final String title;
        final String content;
        final NotificationType type;
        private final long start;
        private long dur;
        private boolean expired;

        Notif(String text) {
            this(text, "", NotificationType.INFO);
        }

        Notif(String title, String content, NotificationType type) {
            this.dur = 4000L;
            this.text = title;
            this.title = title;
            this.content = content;
            this.type = type;
            this.start = System.currentTimeMillis();
        }

        void speedUp() {
            long elapsed = System.currentTimeMillis() - this.start;
            if (this.dur > elapsed + 300) {
                this.dur = elapsed + 300;
            }
        }

        float getAlpha() {
            long e = System.currentTimeMillis() - this.start;
            if (e < 300) {
                return e / 300.0f;
            }
            if (e < this.dur - 300) {
                return 1.0f;
            }
            if (e < this.dur) {
                return 1.0f - ((e - (this.dur - 300)) / 300.0f);
            }
            return 0.0f;
        }

        boolean shouldRemove() {
            if (!this.expired && System.currentTimeMillis() - this.start > this.dur) {
                this.expired = true;
            }
            return this.expired && getAlpha() <= 0.05f;
        }
    }
}
