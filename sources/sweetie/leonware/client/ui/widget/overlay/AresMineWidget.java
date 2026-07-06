package sweetie.leonware.client.ui.widget.overlay;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.class_1735;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1836;
import net.minecraft.class_2371;
import net.minecraft.class_2561;
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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/widget/overlay/AresMineWidget.class */
public class AresMineWidget extends Widget {
    private final Map<String, EventData> events;
    private final List<EventData> sortedCache;
    private final Comparator<EventData> eventComparator;
    private static final List<String> EVENT_NAMES = Arrays.asList("Проклятый Одиссей", "Царь Олимпа", "Битва на PvP-Арене", "Битва на Арене", "Захват территории", "Битва Гладиаторов", "Минотавр");
    private static final Pattern P_START = Pattern.compile("Событие\\s+начн[её]тся\\s+через\\s+(.+)");
    private static final Pattern P_END = Pattern.compile("Событие\\s+законч[иится]+ся\\s+через\\s+(.+)");
    private static final Pattern P_DONE = Pattern.compile("Событие\\s+завершено");
    private static final Pattern P_CHAT_NOW = Pattern.compile("Проводится\\s+(.+)");
    private static final Pattern P_CHAT_SOON = Pattern.compile("(.+?)\\s+через\\s+(.+)");
    private static final Pattern P_TIME_HOURS = Pattern.compile("(\\d+)[чЧ]");
    private static final Pattern P_TIME_MINUTES = Pattern.compile("(\\d+)[мМ]");
    private static final Pattern P_TIME_SECONDS = Pattern.compile("(\\d+)[сС]");
    private static final Pattern P_COLOR_CODES = Pattern.compile("(?i)[§&][0-9a-fk-or]");
    private static final Pattern P_NEWLINES = Pattern.compile("\\\\n|\\n|\\r|\\\\r");
    private static final Pattern P_MULTI_SPACE = Pattern.compile("\\s{2,}");

    public AresMineWidget() {
        super(100.0f, 100.0f);
        this.events = new LinkedHashMap();
        this.sortedCache = new ArrayList();
        this.eventComparator = (a, b) -> {
            return Long.compare(b.timeRemMs, a.timeRemMs);
        };
        UpdateEvent.getInstance().subscribe(new Listener(event -> {
            if (isEnabled()) {
                for (EventData data : this.events.values()) {
                    data.updateCountdown();
                }
                class_437 patt0$temp = mc.field_1755;
                if (patt0$temp instanceof class_465) {
                    class_465<?> screen = (class_465) patt0$temp;
                    scanScreen(screen);
                }
            }
        }));
        PacketEvent.getInstance().subscribe(new Listener(event2 -> {
            if (isEnabled() && event2.isReceive()) {
                class_7439 class_7439VarPacket = event2.packet();
                if (class_7439VarPacket instanceof class_7439) {
                    class_7439 packet = class_7439VarPacket;
                    String msg = strip(packet.comp_763().getString());
                    if (!msg.isEmpty()) {
                        parseChat(msg);
                    }
                }
            }
        }));
    }

    @Override // sweetie.leonware.client.ui.widget.Widget
    public String getName() {
        return "AresMineEvent";
    }

    @Override // sweetie.leonware.api.system.interfaces.IRenderer
    public void render(class_4587 ms) {
        float x = getDraggable().getX();
        float y = getDraggable().getY();
        float fontSize = scaled(6.5f);
        float rowH = fontSize + (getGap() * 1.5f);
        float gap = scaled(1.5f);
        float round = scaled(2.5f);
        float padH = getGap() * 1.5f;
        float padV = getGap() * 0.5f;
        List<EventData> rows = getSortedEvents();
        Color blurColor = UIColors.widgetBlur();
        Color textColor = UIColors.textColor();
        Color primary = UIColors.primary();
        Color secondary = UIColors.secondary();
        float maxW = getSemiBoldFont().getWidth("Нет событий", fontSize);
        int n = rows.size();
        for (int i = 0; i < n; i++) {
            EventData d = rows.get(i);
            float w = getSemiBoldFont().getWidth(d.name + " " + d.displayTime, fontSize);
            if (w > maxW) {
                maxW = w;
            }
        }
        float width = maxW + (padH * 2.0f);
        float cy = y;
        if (rows.isEmpty()) {
            RenderUtil.BLUR_RECT.draw(ms, x, cy, width, rowH, round, blurColor);
            getSemiBoldFont().drawText(ms, "Нет событий", x + padH, cy + padV, fontSize, textColor);
            cy += rowH + gap;
        } else {
            int n2 = rows.size();
            for (int i2 = 0; i2 < n2; i2++) {
                EventData d2 = rows.get(i2);
                RenderUtil.BLUR_RECT.draw(ms, x, cy, width, rowH, round, blurColor);
                float nameW = getSemiBoldFont().getWidth(d2.name + " ", fontSize);
                getSemiBoldFont().drawText(ms, d2.name + " ", x + padH, cy + padV, fontSize, textColor);
                getSemiBoldFont().drawGradientText(ms, d2.displayTime, x + padH + nameW, cy + padV, fontSize, primary, secondary, width / 4.0f);
                cy += rowH + gap;
            }
        }
        getDraggable().setWidth(width);
        getDraggable().setHeight((cy - gap) - y);
    }

    private List<EventData> getSortedEvents() {
        this.sortedCache.clear();
        this.sortedCache.addAll(this.events.values());
        this.sortedCache.sort(this.eventComparator);
        return this.sortedCache;
    }

    private void scanScreen(class_465<?> screen) {
        String ev;
        if (isValidEventScreen(screen)) {
            Set<String> found = new HashSet<>();
            for (int slot = 19; slot <= 25; slot++) {
                if (slot < screen.method_17577().field_7761.size()) {
                    class_1799 stack = ((class_1735) screen.method_17577().field_7761.get(slot)).method_7677();
                    if (stack.method_7909() == class_1802.field_8407) {
                        String name = strip(stack.method_7964().getString());
                        if (!name.isEmpty()) {
                            String key = matchEvent(name);
                            if (key == null) {
                                key = name;
                            }
                            found.add(key);
                            EventData data = this.events.computeIfAbsent(key, EventData::new);
                            List<class_2561> tooltip = stack.method_7950(class_1792.class_9635.field_51353, mc.field_1724, class_1836.field_41070);
                            for (int i = 1; i < tooltip.size(); i++) {
                                String line = strip(tooltip.get(i).getString());
                                if (!line.isEmpty()) {
                                    if (P_DONE.matcher(line).find()) {
                                        data.setCompleted();
                                    } else if (P_END.matcher(line).find()) {
                                        data.setNow();
                                    } else {
                                        Matcher mStart = P_START.matcher(line);
                                        if (mStart.find()) {
                                            long ms = parseTime(mStart.group(1));
                                            if (ms > 0) {
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
            class_1799 cursor = screen.method_17577().method_34255();
            if (cursor.method_7909() == class_1802.field_8407 && (ev = matchEvent(strip(cursor.method_7964().getString()))) != null) {
                found.add(ev);
            }
            this.events.keySet().retainAll(found);
        }
    }

    private boolean isValidEventScreen(class_465<?> screen) {
        class_2371<class_1735> class_2371Var = screen.method_17577().field_7761;
        int guiSlots = 0;
        for (class_1735 slot : class_2371Var) {
            if (slot.field_7871 != mc.field_1724.method_31548()) {
                guiSlots++;
            }
        }
        if (guiSlots != 36 || class_2371Var.size() <= 0 || ((class_1735) class_2371Var.get(0)).method_7677().method_7909() != class_1802.field_8407 || class_2371Var.size() <= 1 || ((class_1735) class_2371Var.get(1)).method_7677().method_7909() != class_1802.field_8407 || class_2371Var.size() <= 8 || ((class_1735) class_2371Var.get(8)).method_7677().method_7909() != class_1802.field_8407) {
            return false;
        }
        for (int slot2 = 19; slot2 <= 25; slot2++) {
            if (slot2 < class_2371Var.size() && ((class_1735) class_2371Var.get(slot2)).method_7677().method_7909() == class_1802.field_8407 && matchEvent(strip(((class_1735) class_2371Var.get(slot2)).method_7677().method_7964().getString())) != null) {
                return true;
            }
        }
        class_1799 cursor = screen.method_17577().method_34255();
        return cursor.method_7909() == class_1802.field_8407 && matchEvent(strip(cursor.method_7964().getString())) != null;
    }

    private void parseChat(String msg) {
        String ev;
        Matcher mNow = P_CHAT_NOW.matcher(msg);
        if (mNow.find()) {
            String ev2 = matchEvent(mNow.group(1).trim());
            if (ev2 != null) {
                this.events.computeIfAbsent(ev2, EventData::new).setNow();
                return;
            }
            return;
        }
        Matcher mSoon = P_CHAT_SOON.matcher(msg);
        if (mSoon.find() && (ev = matchEvent(mSoon.group(1).trim())) != null) {
            long ms = parseTime(mSoon.group(2).trim());
            if (ms > 0) {
                this.events.computeIfAbsent(ev, EventData::new).setTimeRemaining(ms);
            }
        }
    }

    @Nullable
    private static String matchEvent(String s) {
        int n = EVENT_NAMES.size();
        for (int i = 0; i < n; i++) {
            String ev = EVENT_NAMES.get(i);
            if (s.contains(ev)) {
                return ev;
            }
        }
        return null;
    }

    private static String strip(String s) {
        if (s == null || s.isEmpty()) {
            return "";
        }
        String r = P_COLOR_CODES.matcher(s).replaceAll("");
        return P_MULTI_SPACE.matcher(P_NEWLINES.matcher(r).replaceAll(" ")).replaceAll(" ").trim();
    }

    private static long parseTime(String s) {
        long ms = 0;
        Matcher h = P_TIME_HOURS.matcher(s);
        Matcher m = P_TIME_MINUTES.matcher(s);
        Matcher sec = P_TIME_SECONDS.matcher(s);
        if (h.find()) {
            ms = 0 + (Long.parseLong(h.group(1)) * 3600000);
        }
        if (m.find()) {
            ms += Long.parseLong(m.group(1)) * 60000;
        }
        if (sec.find()) {
            ms += Long.parseLong(sec.group(1)) * 1000;
        }
        return ms;
    }

    private static String fmtTime(long ms) {
        if (ms <= 0) {
            return "Сейчас";
        }
        long s = ms / 1000;
        long h = s / 3600;
        long mm = (s % 3600) / 60;
        long ss = s % 60;
        StringBuilder sb = new StringBuilder();
        if (h > 0) {
            sb.append(h).append("ч ");
        }
        if (mm > 0) {
            sb.append(mm).append("м ");
        }
        if (ss > 0 || sb.length() == 0) {
            sb.append(ss).append("с");
        }
        return sb.toString().trim();
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/widget/overlay/AresMineWidget$EventData.class */
    private static class EventData {
        final String name;
        String displayTime = "Неизвестно";
        long timeRemMs = -1;
        long lastTickMs = -1;

        EventData(String name) {
            this.name = name;
        }

        void updateCountdown() {
            if (this.timeRemMs <= 0 || this.lastTickMs <= 0) {
                return;
            }
            long now = System.currentTimeMillis();
            this.timeRemMs = Math.max(0L, this.timeRemMs - (now - this.lastTickMs));
            this.lastTickMs = now;
            this.displayTime = this.timeRemMs == 0 ? "Сейчас" : AresMineWidget.fmtTime(this.timeRemMs);
        }

        void setTimeRemaining(long ms) {
            this.timeRemMs = ms;
            this.lastTickMs = System.currentTimeMillis();
            this.displayTime = AresMineWidget.fmtTime(ms);
        }

        void setNow() {
            this.displayTime = "Сейчас";
            this.timeRemMs = 0L;
            this.lastTickMs = -1L;
        }

        void setCompleted() {
            this.displayTime = "Завершено";
            this.timeRemMs = -1L;
            this.lastTickMs = -1L;
        }
    }
}
