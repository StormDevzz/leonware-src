// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.widget.overlay;

import java.util.Arrays;
import org.jetbrains.annotations.Nullable;
import java.util.regex.Matcher;
import net.minecraft.class_1799;
import java.util.Set;
import net.minecraft.class_2561;
import net.minecraft.class_1657;
import net.minecraft.class_1836;
import net.minecraft.class_1792;
import java.util.function.Function;
import net.minecraft.class_1802;
import net.minecraft.class_1735;
import java.util.HashSet;
import java.util.Collection;
import java.awt.Color;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.color.UIColors;
import net.minecraft.class_4587;
import net.minecraft.class_2596;
import net.minecraft.class_437;
import java.util.Iterator;
import net.minecraft.class_7439;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.Listener;
import net.minecraft.class_465;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import sweetie.leonware.client.ui.widget.Widget;

public class AresMineWidget extends Widget
{
    private final Map<String, EventData> events;
    private final List<EventData> sortedCache;
    private final Comparator<EventData> eventComparator;
    private static final List<String> EVENT_NAMES;
    private static final Pattern P_START;
    private static final Pattern P_END;
    private static final Pattern P_DONE;
    private static final Pattern P_CHAT_NOW;
    private static final Pattern P_CHAT_SOON;
    private static final Pattern P_TIME_HOURS;
    private static final Pattern P_TIME_MINUTES;
    private static final Pattern P_TIME_SECONDS;
    private static final Pattern P_COLOR_CODES;
    private static final Pattern P_NEWLINES;
    private static final Pattern P_MULTI_SPACE;
    
    public AresMineWidget() {
        super(100.0f, 100.0f);
        this.events = new LinkedHashMap<String, EventData>();
        this.sortedCache = new ArrayList<EventData>();
        this.eventComparator = ((a, b) -> Long.compare(b.timeRemMs, a.timeRemMs));
        UpdateEvent.getInstance().subscribe(new Listener<UpdateEvent>(event -> {
            if (!this.isEnabled()) {
                return;
            }
            else {
                this.events.values().iterator();
                final Iterator iterator;
                while (iterator.hasNext()) {
                    final EventData data = iterator.next();
                    data.updateCountdown();
                }
                final class_437 patt0$temp = AresMineWidget.mc.field_1755;
                if (patt0$temp instanceof final class_465 screen) {
                    this.scanScreen((class_465<?>)screen);
                }
                return;
            }
        }));
        PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(event -> {
            if (!(!this.isEnabled())) {
                if (event.isReceive()) {
                    final class_2596 patt0$temp2 = event.packet();
                    if (patt0$temp2 instanceof final class_7439 packet) {
                        final String msg = strip(packet.comp_763().getString());
                        if (!msg.isEmpty()) {
                            this.parseChat(msg);
                        }
                    }
                }
            }
        }));
    }
    
    @Override
    public String getName() {
        return "AresMineEvent";
    }
    
    @Override
    public void render(final class_4587 ms) {
        final float x = this.getDraggable().getX();
        final float y = this.getDraggable().getY();
        final float fontSize = this.scaled(6.5f);
        final float rowH = fontSize + this.getGap() * 1.5f;
        final float gap = this.scaled(1.5f);
        final float round = this.scaled(2.5f);
        final float padH = this.getGap() * 1.5f;
        final float padV = this.getGap() * 0.5f;
        final List<EventData> rows = this.getSortedEvents();
        final Color blurColor = UIColors.widgetBlur();
        final Color textColor = UIColors.textColor();
        final Color primary = UIColors.primary();
        final Color secondary = UIColors.secondary();
        float maxW = this.getSemiBoldFont().getWidth("\u041d\u0435\u0442 \u0441\u043e\u0431\u044b\u0442\u0438\u0439", fontSize);
        for (int i = 0, n = rows.size(); i < n; ++i) {
            final EventData d = rows.get(i);
            final float w = this.getSemiBoldFont().getWidth(d.name + " " + d.displayTime, fontSize);
            if (w > maxW) {
                maxW = w;
            }
        }
        final float width = maxW + padH * 2.0f;
        float cy = y;
        if (rows.isEmpty()) {
            RenderUtil.BLUR_RECT.draw(ms, x, cy, width, rowH, round, blurColor);
            this.getSemiBoldFont().drawText(ms, "\u041d\u0435\u0442 \u0441\u043e\u0431\u044b\u0442\u0438\u0439", x + padH, cy + padV, fontSize, textColor);
            cy += rowH + gap;
        }
        else {
            for (int j = 0, n2 = rows.size(); j < n2; ++j) {
                final EventData d2 = rows.get(j);
                RenderUtil.BLUR_RECT.draw(ms, x, cy, width, rowH, round, blurColor);
                final float nameW = this.getSemiBoldFont().getWidth(d2.name, fontSize);
                this.getSemiBoldFont().drawText(ms, d2.name, x + padH, cy + padV, fontSize, textColor);
                this.getSemiBoldFont().drawGradientText(ms, d2.displayTime, x + padH + nameW, cy + padV, fontSize, primary, secondary, width / 4.0f);
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
    
    private void scanScreen(final class_465<?> screen) {
        if (!this.isValidEventScreen(screen)) {
            return;
        }
        final Set<String> found = new HashSet<String>();
        for (int slot = 19; slot <= 25; ++slot) {
            if (slot < screen.method_17577().field_7761.size()) {
                final class_1799 stack = ((class_1735)screen.method_17577().field_7761.get(slot)).method_7677();
                if (stack.method_7909() == class_1802.field_8407) {
                    final String name = strip(stack.method_7964().getString());
                    if (!name.isEmpty()) {
                        String key = matchEvent(name);
                        if (key == null) {
                            key = name;
                        }
                        found.add(key);
                        final EventData data = this.events.computeIfAbsent(key, EventData::new);
                        final List<class_2561> tooltip = stack.method_7950(class_1792.class_9635.field_51353, (class_1657)AresMineWidget.mc.field_1724, (class_1836)class_1836.field_41070);
                        for (int i = 1; i < tooltip.size(); ++i) {
                            final String line = strip(tooltip.get(i).getString());
                            if (!line.isEmpty()) {
                                if (AresMineWidget.P_DONE.matcher(line).find()) {
                                    data.setCompleted();
                                }
                                else if (AresMineWidget.P_END.matcher(line).find()) {
                                    data.setNow();
                                }
                                else {
                                    final Matcher mStart = AresMineWidget.P_START.matcher(line);
                                    if (mStart.find()) {
                                        final long ms = parseTime(mStart.group(1));
                                        if (ms > 0L) {
                                            data.setTimeRemaining(ms);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        final class_1799 cursor = screen.method_17577().method_34255();
        if (cursor.method_7909() == class_1802.field_8407) {
            final String ev = matchEvent(strip(cursor.method_7964().getString()));
            if (ev != null) {
                found.add(ev);
            }
        }
        this.events.keySet().retainAll(found);
    }
    
    private boolean isValidEventScreen(final class_465<?> screen) {
        final List<class_1735> slots = (List<class_1735>)screen.method_17577().field_7761;
        int guiSlots = 0;
        for (final class_1735 slot : slots) {
            if (slot.field_7871 != AresMineWidget.mc.field_1724.method_31548()) {
                ++guiSlots;
            }
        }
        if (guiSlots != 36) {
            return false;
        }
        if (slots.size() <= 0 || slots.get(0).method_7677().method_7909() != class_1802.field_8407) {
            return false;
        }
        if (slots.size() <= 1 || slots.get(1).method_7677().method_7909() != class_1802.field_8407) {
            return false;
        }
        if (slots.size() <= 8 || slots.get(8).method_7677().method_7909() != class_1802.field_8407) {
            return false;
        }
        for (int slot2 = 19; slot2 <= 25; ++slot2) {
            if (slot2 < slots.size()) {
                if (slots.get(slot2).method_7677().method_7909() == class_1802.field_8407 && matchEvent(strip(slots.get(slot2).method_7677().method_7964().getString())) != null) {
                    return true;
                }
            }
        }
        final class_1799 cursor = screen.method_17577().method_34255();
        return cursor.method_7909() == class_1802.field_8407 && matchEvent(strip(cursor.method_7964().getString())) != null;
    }
    
    private void parseChat(final String msg) {
        final Matcher mNow = AresMineWidget.P_CHAT_NOW.matcher(msg);
        if (mNow.find()) {
            final String ev = matchEvent(mNow.group(1).trim());
            if (ev != null) {
                this.events.computeIfAbsent(ev, EventData::new).setNow();
            }
            return;
        }
        final Matcher mSoon = AresMineWidget.P_CHAT_SOON.matcher(msg);
        if (mSoon.find()) {
            final String ev2 = matchEvent(mSoon.group(1).trim());
            if (ev2 != null) {
                final long ms = parseTime(mSoon.group(2).trim());
                if (ms > 0L) {
                    this.events.computeIfAbsent(ev2, EventData::new).setTimeRemaining(ms);
                }
            }
        }
    }
    
    @Nullable
    private static String matchEvent(final String s) {
        for (int i = 0, n = AresMineWidget.EVENT_NAMES.size(); i < n; ++i) {
            final String ev = AresMineWidget.EVENT_NAMES.get(i);
            if (s.contains(ev)) {
                return ev;
            }
        }
        return null;
    }
    
    private static String strip(final String s) {
        if (s == null || s.isEmpty()) {
            return "";
        }
        String r = AresMineWidget.P_COLOR_CODES.matcher(s).replaceAll("");
        r = AresMineWidget.P_NEWLINES.matcher(r).replaceAll(" ");
        r = AresMineWidget.P_MULTI_SPACE.matcher(r).replaceAll(" ");
        return r.trim();
    }
    
    private static long parseTime(final String s) {
        long ms = 0L;
        final Matcher h = AresMineWidget.P_TIME_HOURS.matcher(s);
        final Matcher m = AresMineWidget.P_TIME_MINUTES.matcher(s);
        final Matcher sec = AresMineWidget.P_TIME_SECONDS.matcher(s);
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
    
    private static String fmtTime(final long ms) {
        if (ms <= 0L) {
            return "\u0421\u0435\u0439\u0447\u0430\u0441";
        }
        final long s = ms / 1000L;
        final long h = s / 3600L;
        final long mm = s % 3600L / 60L;
        final long ss = s % 60L;
        final StringBuilder sb = new StringBuilder();
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
    
    static {
        EVENT_NAMES = Arrays.asList("\u041f\u0440\u043e\u043a\u043b\u044f\u0442\u044b\u0439 \u041e\u0434\u0438\u0441\u0441\u0435\u0439", "\u0426\u0430\u0440\u044c \u041e\u043b\u0438\u043c\u043f\u0430", "\u0411\u0438\u0442\u0432\u0430 \u043d\u0430 PvP-\u0410\u0440\u0435\u043d\u0435", "\u0411\u0438\u0442\u0432\u0430 \u043d\u0430 \u0410\u0440\u0435\u043d\u0435", "\u0417\u0430\u0445\u0432\u0430\u0442 \u0442\u0435\u0440\u0440\u0438\u0442\u043e\u0440\u0438\u0438", "\u0411\u0438\u0442\u0432\u0430 \u0413\u043b\u0430\u0434\u0438\u0430\u0442\u043e\u0440\u043e\u0432", "\u041c\u0438\u043d\u043e\u0442\u0430\u0432\u0440");
        P_START = Pattern.compile("\u0421\u043e\u0431\u044b\u0442\u0438\u0435\\s+\u043d\u0430\u0447\u043d[\u0435\u0451]\u0442\u0441\u044f\\s+\u0447\u0435\u0440\u0435\u0437\\s+(.+)");
        P_END = Pattern.compile("\u0421\u043e\u0431\u044b\u0442\u0438\u0435\\s+\u0437\u0430\u043a\u043e\u043d\u0447[\u0438\u0438\u0442\u0441\u044f]+\u0441\u044f\\s+\u0447\u0435\u0440\u0435\u0437\\s+(.+)");
        P_DONE = Pattern.compile("\u0421\u043e\u0431\u044b\u0442\u0438\u0435\\s+\u0437\u0430\u0432\u0435\u0440\u0448\u0435\u043d\u043e");
        P_CHAT_NOW = Pattern.compile("\u041f\u0440\u043e\u0432\u043e\u0434\u0438\u0442\u0441\u044f\\s+(.+)");
        P_CHAT_SOON = Pattern.compile("(.+?)\\s+\u0447\u0435\u0440\u0435\u0437\\s+(.+)");
        P_TIME_HOURS = Pattern.compile("(\\d+)[\u0447\u0427]");
        P_TIME_MINUTES = Pattern.compile("(\\d+)[\u043c\u041c]");
        P_TIME_SECONDS = Pattern.compile("(\\d+)[\u0441\u0421]");
        P_COLOR_CODES = Pattern.compile("(?i)[§&][0-9a-fk-or]");
        P_NEWLINES = Pattern.compile("\\\\n|\\n|\\r|\\\\r");
        P_MULTI_SPACE = Pattern.compile("\\s{2,}");
    }
    
    private static class EventData
    {
        final String name;
        String displayTime;
        long timeRemMs;
        long lastTickMs;
        
        EventData(final String name) {
            this.displayTime = "\u041d\u0435\u0438\u0437\u0432\u0435\u0441\u0442\u043d\u043e";
            this.timeRemMs = -1L;
            this.lastTickMs = -1L;
            this.name = name;
        }
        
        void updateCountdown() {
            if (this.timeRemMs <= 0L || this.lastTickMs <= 0L) {
                return;
            }
            final long now = System.currentTimeMillis();
            this.timeRemMs = Math.max(0L, this.timeRemMs - (now - this.lastTickMs));
            this.lastTickMs = now;
            this.displayTime = ((this.timeRemMs == 0L) ? "\u0421\u0435\u0439\u0447\u0430\u0441" : AresMineWidget.fmtTime(this.timeRemMs));
        }
        
        void setTimeRemaining(final long ms) {
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
