/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1657
 *  net.minecraft.class_1735
 *  net.minecraft.class_1792$class_9635
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_1836
 *  net.minecraft.class_2371
 *  net.minecraft.class_2561
 *  net.minecraft.class_2596
 *  net.minecraft.class_437
 *  net.minecraft.class_4587
 *  net.minecraft.class_465
 *  net.minecraft.class_7439
 *  org.jetbrains.annotations.Nullable
 */
package sweetie.leonware.client.ui.widget.overlay;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.class_1657;
import net.minecraft.class_1735;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1836;
import net.minecraft.class_2371;
import net.minecraft.class_2561;
import net.minecraft.class_2596;
import net.minecraft.class_437;
import net.minecraft.class_4587;
import net.minecraft.class_465;
import net.minecraft.class_7439;
import org.jetbrains.annotations.Nullable;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.client.ui.widget.Widget;

public class AresMineWidget
extends Widget {
    private final Map<String, EventData> events = new LinkedHashMap<String, EventData>();
    private final List<EventData> sortedCache = new ArrayList<EventData>();
    private final Comparator<EventData> eventComparator = (a, b) -> Long.compare(b.timeRemMs, a.timeRemMs);
    private static final List<String> EVENT_NAMES = Arrays.asList("\u041f\u0440\u043e\u043a\u043b\u044f\u0442\u044b\u0439 \u041e\u0434\u0438\u0441\u0441\u0435\u0439", "\u0426\u0430\u0440\u044c \u041e\u043b\u0438\u043c\u043f\u0430", "\u0411\u0438\u0442\u0432\u0430 \u043d\u0430 PvP-\u0410\u0440\u0435\u043d\u0435", "\u0411\u0438\u0442\u0432\u0430 \u043d\u0430 \u0410\u0440\u0435\u043d\u0435", "\u0417\u0430\u0445\u0432\u0430\u0442 \u0442\u0435\u0440\u0440\u0438\u0442\u043e\u0440\u0438\u0438", "\u0411\u0438\u0442\u0432\u0430 \u0413\u043b\u0430\u0434\u0438\u0430\u0442\u043e\u0440\u043e\u0432", "\u041c\u0438\u043d\u043e\u0442\u0430\u0432\u0440");
    private static final Pattern P_START = Pattern.compile("\u0421\u043e\u0431\u044b\u0442\u0438\u0435\\s+\u043d\u0430\u0447\u043d[\u0435\u0451]\u0442\u0441\u044f\\s+\u0447\u0435\u0440\u0435\u0437\\s+(.+)");
    private static final Pattern P_END = Pattern.compile("\u0421\u043e\u0431\u044b\u0442\u0438\u0435\\s+\u0437\u0430\u043a\u043e\u043d\u0447[\u0438\u0438\u0442\u0441\u044f]+\u0441\u044f\\s+\u0447\u0435\u0440\u0435\u0437\\s+(.+)");
    private static final Pattern P_DONE = Pattern.compile("\u0421\u043e\u0431\u044b\u0442\u0438\u0435\\s+\u0437\u0430\u0432\u0435\u0440\u0448\u0435\u043d\u043e");
    private static final Pattern P_CHAT_NOW = Pattern.compile("\u041f\u0440\u043e\u0432\u043e\u0434\u0438\u0442\u0441\u044f\\s+(.+)");
    private static final Pattern P_CHAT_SOON = Pattern.compile("(.+?)\\s+\u0447\u0435\u0440\u0435\u0437\\s+(.+)");
    private static final Pattern P_TIME_HOURS = Pattern.compile("(\\d+)[\u0447\u0427]");
    private static final Pattern P_TIME_MINUTES = Pattern.compile("(\\d+)[\u043c\u041c]");
    private static final Pattern P_TIME_SECONDS = Pattern.compile("(\\d+)[\u0441\u0421]");
    private static final Pattern P_COLOR_CODES = Pattern.compile("(?i)[\u00a7&][0-9a-fk-or]");
    private static final Pattern P_NEWLINES = Pattern.compile("\\\\n|\\n|\\r|\\\\r");
    private static final Pattern P_MULTI_SPACE = Pattern.compile("\\s{2,}");

    public AresMineWidget() {
        super(100.0f, 100.0f);
        UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (!this.isEnabled()) {
                return;
            }
            for (EventData data : this.events.values()) {
                data.updateCountdown();
            }
            class_437 patt0$temp = AresMineWidget.mc.field_1755;
            if (patt0$temp instanceof class_465) {
                class_465 screen = (class_465)patt0$temp;
                this.scanScreen(screen);
            }
        }));
        PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(event -> {
            class_7439 packet;
            String msg;
            class_2596<?> patt0$temp;
            if (!this.isEnabled()) {
                return;
            }
            if (event.isReceive() && (patt0$temp = event.packet()) instanceof class_7439 && !(msg = AresMineWidget.strip((packet = (class_7439)patt0$temp).comp_763().getString())).isEmpty()) {
                this.parseChat(msg);
            }
        }));
    }

    @Override
    public String getName() {
        return "AresMineEvent";
    }

    @Override
    public void render(class_4587 ms) {
        float x = this.getDraggable().getX();
        float y = this.getDraggable().getY();
        float fontSize = this.scaled(6.5f);
        float rowH = fontSize + this.getGap() * 1.5f;
        float gap = this.scaled(1.5f);
        float round = this.scaled(2.5f);
        float padH = this.getGap() * 1.5f;
        float padV = this.getGap() * 0.5f;
        List<EventData> rows = this.getSortedEvents();
        Color blurColor = UIColors.widgetBlur();
        Color textColor = UIColors.textColor();
        Color primary = UIColors.primary();
        Color secondary = UIColors.secondary();
        float maxW = this.getSemiBoldFont().getWidth("\u041d\u0435\u0442 \u0441\u043e\u0431\u044b\u0442\u0438\u0439", fontSize);
        int n = rows.size();
        for (int i = 0; i < n; ++i) {
            EventData d = rows.get(i);
            float w = this.getSemiBoldFont().getWidth(d.name + " " + d.displayTime, fontSize);
            if (!(w > maxW)) continue;
            maxW = w;
        }
        float width = maxW + padH * 2.0f;
        float cy = y;
        if (rows.isEmpty()) {
            RenderUtil.BLUR_RECT.draw(ms, x, cy, width, rowH, round, blurColor);
            this.getSemiBoldFont().drawText(ms, "\u041d\u0435\u0442 \u0441\u043e\u0431\u044b\u0442\u0438\u0439", x + padH, cy + padV, fontSize, textColor);
            cy += rowH + gap;
        } else {
            int n2 = rows.size();
            for (int i = 0; i < n2; ++i) {
                EventData d = rows.get(i);
                RenderUtil.BLUR_RECT.draw(ms, x, cy, width, rowH, round, blurColor);
                float nameW = this.getSemiBoldFont().getWidth(d.name + " ", fontSize);
                this.getSemiBoldFont().drawText(ms, d.name + " ", x + padH, cy + padV, fontSize, textColor);
                this.getSemiBoldFont().drawGradientText(ms, d.displayTime, x + padH + nameW, cy + padV, fontSize, primary, secondary, width / 4.0f);
                cy += rowH + gap;
            }
        }
        this.getDraggable().setWidth(width);
        this.getDraggable().setHeight(cy - gap - y);
    }

    private List<EventData> getSortedEvents() {
        this.sortedCache.clear();
        this.sortedCache.addAll(this.events.values());
        this.sortedCache.sort(this.eventComparator);
        return this.sortedCache;
    }

    private void scanScreen(class_465<?> screen) {
        String ev;
        if (!this.isValidEventScreen(screen)) {
            return;
        }
        HashSet<String> found = new HashSet<String>();
        for (int slot = 19; slot <= 25; ++slot) {
            String name;
            class_1799 stack;
            if (slot >= screen.method_17577().field_7761.size() || (stack = ((class_1735)screen.method_17577().field_7761.get(slot)).method_7677()).method_7909() != class_1802.field_8407 || (name = AresMineWidget.strip(stack.method_7964().getString())).isEmpty()) continue;
            String key = AresMineWidget.matchEvent(name);
            if (key == null) {
                key = name;
            }
            found.add(key);
            EventData data = this.events.computeIfAbsent(key, EventData::new);
            List tooltip = stack.method_7950(class_1792.class_9635.field_51353, (class_1657)AresMineWidget.mc.field_1724, (class_1836)class_1836.field_41070);
            for (int i = 1; i < tooltip.size(); ++i) {
                long ms;
                String line = AresMineWidget.strip(((class_2561)tooltip.get(i)).getString());
                if (line.isEmpty()) continue;
                if (P_DONE.matcher(line).find()) {
                    data.setCompleted();
                    continue;
                }
                if (P_END.matcher(line).find()) {
                    data.setNow();
                    continue;
                }
                Matcher mStart = P_START.matcher(line);
                if (!mStart.find() || (ms = AresMineWidget.parseTime(mStart.group(1))) <= 0L) continue;
                data.setTimeRemaining(ms);
            }
        }
        class_1799 cursor = screen.method_17577().method_34255();
        if (cursor.method_7909() == class_1802.field_8407 && (ev = AresMineWidget.matchEvent(AresMineWidget.strip(cursor.method_7964().getString()))) != null) {
            found.add(ev);
        }
        this.events.keySet().retainAll(found);
    }

    private boolean isValidEventScreen(class_465<?> screen) {
        class_2371 slots = screen.method_17577().field_7761;
        int guiSlots = 0;
        for (class_1735 slot : slots) {
            if (slot.field_7871 == AresMineWidget.mc.field_1724.method_31548()) continue;
            ++guiSlots;
        }
        if (guiSlots != 36) {
            return false;
        }
        if (slots.size() <= 0 || ((class_1735)slots.get(0)).method_7677().method_7909() != class_1802.field_8407) {
            return false;
        }
        if (slots.size() <= 1 || ((class_1735)slots.get(1)).method_7677().method_7909() != class_1802.field_8407) {
            return false;
        }
        if (slots.size() <= 8 || ((class_1735)slots.get(8)).method_7677().method_7909() != class_1802.field_8407) {
            return false;
        }
        for (int slot = 19; slot <= 25; ++slot) {
            if (slot >= slots.size() || ((class_1735)slots.get(slot)).method_7677().method_7909() != class_1802.field_8407 || AresMineWidget.matchEvent(AresMineWidget.strip(((class_1735)slots.get(slot)).method_7677().method_7964().getString())) == null) continue;
            return true;
        }
        class_1799 cursor = screen.method_17577().method_34255();
        return cursor.method_7909() == class_1802.field_8407 && AresMineWidget.matchEvent(AresMineWidget.strip(cursor.method_7964().getString())) != null;
    }

    private void parseChat(String msg) {
        long ms;
        String ev;
        Matcher mNow = P_CHAT_NOW.matcher(msg);
        if (mNow.find()) {
            String ev2 = AresMineWidget.matchEvent(mNow.group(1).trim());
            if (ev2 != null) {
                this.events.computeIfAbsent(ev2, EventData::new).setNow();
            }
            return;
        }
        Matcher mSoon = P_CHAT_SOON.matcher(msg);
        if (mSoon.find() && (ev = AresMineWidget.matchEvent(mSoon.group(1).trim())) != null && (ms = AresMineWidget.parseTime(mSoon.group(2).trim())) > 0L) {
            this.events.computeIfAbsent(ev, EventData::new).setTimeRemaining(ms);
        }
    }

    @Nullable
    private static String matchEvent(String s) {
        int n = EVENT_NAMES.size();
        for (int i = 0; i < n; ++i) {
            String ev = EVENT_NAMES.get(i);
            if (!s.contains(ev)) continue;
            return ev;
        }
        return null;
    }

    private static String strip(String s) {
        if (s == null || s.isEmpty()) {
            return "";
        }
        String r = P_COLOR_CODES.matcher(s).replaceAll("");
        r = P_NEWLINES.matcher(r).replaceAll(" ");
        r = P_MULTI_SPACE.matcher(r).replaceAll(" ");
        return r.trim();
    }

    private static long parseTime(String s) {
        long ms = 0L;
        Matcher h = P_TIME_HOURS.matcher(s);
        Matcher m = P_TIME_MINUTES.matcher(s);
        Matcher sec = P_TIME_SECONDS.matcher(s);
        if (h.find()) {
            ms += Long.parseLong(h.group(1)) * 3600000L;
        }
        if (m.find()) {
            ms += Long.parseLong(m.group(1)) * 60000L;
        }
        if (sec.find()) {
            ms += Long.parseLong(sec.group(1)) * 1000L;
        }
        return ms;
    }

    private static String fmtTime(long ms) {
        if (ms <= 0L) {
            return "\u0421\u0435\u0439\u0447\u0430\u0441";
        }
        long s = ms / 1000L;
        long h = s / 3600L;
        long mm = s % 3600L / 60L;
        long ss = s % 60L;
        StringBuilder sb = new StringBuilder();
        if (h > 0L) {
            sb.append(h).append("\u0447 ");
        }
        if (mm > 0L) {
            sb.append(mm).append("\u043c ");
        }
        if (ss > 0L || sb.length() == 0) {
            sb.append(ss).append("\u0441");
        }
        return sb.toString().trim();
    }

    private static class EventData {
        final String name;
        String displayTime = "\u041d\u0435\u0438\u0437\u0432\u0435\u0441\u0442\u043d\u043e";
        long timeRemMs = -1L;
        long lastTickMs = -1L;

        EventData(String name) {
            this.name = name;
        }

        void updateCountdown() {
            if (this.timeRemMs <= 0L || this.lastTickMs <= 0L) {
                return;
            }
            long now = System.currentTimeMillis();
            this.timeRemMs = Math.max(0L, this.timeRemMs - (now - this.lastTickMs));
            this.lastTickMs = now;
            this.displayTime = this.timeRemMs == 0L ? "\u0421\u0435\u0439\u0447\u0430\u0441" : AresMineWidget.fmtTime(this.timeRemMs);
        }

        void setTimeRemaining(long ms) {
            this.timeRemMs = ms;
            this.lastTickMs = System.currentTimeMillis();
            this.displayTime = AresMineWidget.fmtTime(ms);
        }

        void setNow() {
            this.displayTime = "\u0421\u0435\u0439\u0447\u0430\u0441";
            this.timeRemMs = 0L;
            this.lastTickMs = -1L;
        }

        void setCompleted() {
            this.displayTime = "\u0417\u0430\u0432\u0435\u0440\u0448\u0435\u043d\u043e";
            this.timeRemMs = -1L;
            this.lastTickMs = -1L;
        }
    }
}

