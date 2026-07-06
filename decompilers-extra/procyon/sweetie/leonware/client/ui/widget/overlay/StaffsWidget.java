// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.ui.widget.overlay;

import java.util.HashMap;
import net.minecraft.class_742;
import lombok.Generated;
import sweetie.leonware.api.utils.other.ReplaceUtil;
import net.minecraft.class_2583;
import net.minecraft.class_2561;
import java.util.Set;
import sweetie.leonware.api.system.configs.StaffManager;
import net.minecraft.class_268;
import net.minecraft.class_1934;
import sweetie.leonware.api.utils.player.PlayerUtil;
import net.minecraft.class_640;
import java.util.HashSet;
import java.util.Collection;
import sweetie.leonware.api.utils.framelimiter.IFrameCall;
import java.util.Iterator;
import sweetie.leonware.api.utils.color.UIColors;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.awt.Color;
import java.util.Map;
import sweetie.leonware.api.module.setting.ModeSetting;
import java.util.List;
import sweetie.leonware.api.utils.framelimiter.FrameLimiter;
import sweetie.leonware.client.ui.widget.ContainerWidget;

public class StaffsWidget extends ContainerWidget
{
    private final FrameLimiter frameLimiter;
    private List<Staff> cacheStaffs;
    public final ModeSetting mode;
    private static final Map<String, String> RANK_MAPPING;
    private static final Map<String, Color> RANK_COLORS;
    
    @Override
    public String getName() {
        return "Staffs";
    }
    
    public StaffsWidget() {
        super(100.0f, 100.0f);
        this.frameLimiter = new FrameLimiter(false);
        this.cacheStaffs = new ArrayList<Staff>();
        this.mode = new ModeSetting("Staffs Mode").values("\u041e\u0431\u044b\u0447\u043d\u044b\u0439", "\u0410\u0440\u0435\u0441\u041c\u0430\u0439\u043d").value("\u041e\u0431\u044b\u0447\u043d\u044b\u0439");
    }
    
    @Override
    protected Map<String, ContainerElement.ColoredString> getCurrentData() {
        final Map<String, ContainerElement.ColoredString> map = new LinkedHashMap<String, ContainerElement.ColoredString>();
        for (final Staff staff : this.getStaffList()) {
            if (this.mode.is("\u0410\u0440\u0435\u0441\u041c\u0430\u0439\u043d")) {
                final String[] parts = staff.name().split(":", 3);
                if (parts.length < 3) {
                    continue;
                }
                final String name = parts[0];
                final String statusStr = parts[1];
                final String role = parts[2];
                final String rankLabel = StaffsWidget.RANK_MAPPING.getOrDefault(role.toLowerCase(), role);
                final Color rankColor = role.equals("custom") ? UIColors.primary() : StaffsWidget.RANK_COLORS.getOrDefault(role.toLowerCase(), Color.WHITE);
                Color statusColor = Color.GREEN;
                String statusLabel = "";
                if (statusStr.equalsIgnoreCase("vanish")) {
                    statusColor = Color.RED;
                    statusLabel = "[V]";
                }
                else if (statusStr.equalsIgnoreCase("gm3")) {
                    statusColor = Color.YELLOW;
                    statusLabel = "[V]";
                }
                map.put(name, new ContainerElement.ColoredString(rankLabel, rankColor, name, Color.WHITE, statusLabel, statusColor));
            }
            else {
                map.put(staff.name(), new ContainerElement.ColoredString(staff.status().name(), UIColors.positiveColor()));
            }
        }
        return map;
    }
    
    public List<Staff> getStaffList() {
        this.frameLimiter.execute(15, () -> {
            final ArrayList<Staff> list = new ArrayList<Staff>();
            if (!StaffsWidget.mc.method_1542()) {
                if (this.mode.is("\u0410\u0440\u0435\u0441\u041c\u0430\u0439\u043d")) {
                    list.addAll((Collection<?>)this.getAresMineStaff());
                }
                else {
                    list.addAll((Collection<?>)this.getOnlineStaff());
                    list.addAll((Collection<?>)this.getVanishedPlayers());
                }
            }
            this.cacheStaffs = list;
            return;
        });
        return this.cacheStaffs;
    }
    
    private List<Staff> getAresMineStaff() {
        final List<Staff> staff = new ArrayList<Staff>();
        if (StaffsWidget.mc.field_1724 == null || StaffsWidget.mc.field_1724.field_3944 == null) {
            return staff;
        }
        final Set<String> validRanks = new HashSet<String>(StaffsWidget.RANK_MAPPING.keySet());
        String name = null;
        for (class_640 entry : StaffsWidget.mc.field_1724.field_3944.method_2880()) {
            name = entry.method_2966().getName();
            if (!PlayerUtil.isValidName(name)) {
                continue;
            }
            final class_2561 displayName = entry.method_2971();
            String prefixString = null;
            if (displayName != null) {
                prefixString = this.parseRankFromText(displayName);
            }
            if (prefixString == null) {
                prefixString = this.parseRankFromTeam(entry);
            }
            if (prefixString == null || !validRanks.contains(prefixString.toLowerCase())) {
                continue;
            }
            final String status = (entry.method_2958() == class_1934.field_9219) ? "gm3" : "active";
            staff.add(new Staff(name + ":" + status + ":" + prefixString.toLowerCase(), Status.ONLINE));
        }
        if (StaffsWidget.mc.field_1687 != null) {
            for (class_268 team : StaffsWidget.mc.field_1687.method_8428().method_1159()) {
                final class_2561 prefix = team.method_1144();
                if (prefix != null) {
                    if (prefix.getString().isEmpty()) {
                        continue;
                    }
                    String rank = this.parseRankFromText(prefix);
                    if (rank == null) {
                        final String prefixStr = prefix.getString().toLowerCase();
                        for (final String r : validRanks) {
                            if (prefixStr.contains(r)) {
                                rank = r;
                                break;
                            }
                        }
                    }
                    if (rank == null || !validRanks.contains(rank.toLowerCase())) {
                        continue;
                    }
                    final String finalRank = rank;
                    for (String name2 : team.method_1204()) {
                        if (!PlayerUtil.isValidName(name2)) {
                            continue;
                        }
                        if (!staff.stream().noneMatch(s -> s.name().split(":")[0].equals(name))) {
                            continue;
                        }
                        staff.add(new Staff(name2 + ":gm3:" + finalRank.toLowerCase(), Status.GM3));
                    }
                }
            }
        }
        for (String manualName : StaffManager.getInstance().getData()) {
            if (staff.stream().noneMatch(s -> s.name().split(":")[0].equalsIgnoreCase(manualName))) {
                class_640 found = null;
                if (StaffsWidget.mc.field_1724.field_3944 != null) {
                    for (final class_640 entry2 : StaffsWidget.mc.field_1724.field_3944.method_2880()) {
                        if (entry2.method_2966().getName().equalsIgnoreCase(manualName)) {
                            found = entry2;
                            break;
                        }
                    }
                }
                String status2 = null;
                Label_0764: {
                    if (found != null) {
                        status2 = ((found.method_2958() == class_1934.field_9219) ? "gm3" : "active");
                    }
                    else if (StaffsWidget.mc.field_1687 != null) {
                        for (final class_268 team2 : StaffsWidget.mc.field_1687.method_8428().method_1159()) {
                            for (final String name3 : team2.method_1204()) {
                                if (name3.equalsIgnoreCase(manualName)) {
                                    status2 = "vanish";
                                    break Label_0764;
                                }
                            }
                        }
                    }
                }
                if (status2 == null) {
                    continue;
                }
                staff.add(new Staff(manualName + ":" + status2 + ":custom", Status.ONLINE));
            }
        }
        staff.sort((a, b) -> a.name().split(":")[0].compareTo(b.name().split(":")[0]));
        return staff;
    }
    
    private String parseRankFromText(final class_2561 text) {
        if (text == null) {
            return null;
        }
        final class_2583 style = text.method_10866();
        if (style != null && style.method_27708() != null) {
            final String s = style.method_27708().toString();
            if (s.contains("custom:groups/")) {
                final String rank = s.substring(s.lastIndexOf("/") + 1).toLowerCase();
                if (!rank.equals("default")) {
                    return rank;
                }
            }
        }
        final String s = text.getString();
        if (s != null && !s.isEmpty()) {
            for (final String rank2 : StaffsWidget.RANK_MAPPING.keySet()) {
                if (s.toLowerCase().contains(rank2)) {
                    return rank2;
                }
            }
        }
        for (final class_2561 sibling : text.method_10855()) {
            final String result = this.parseRankFromText(sibling);
            if (result != null) {
                return result;
            }
        }
        return null;
    }
    
    private String parseRankFromTeam(final class_640 entry) {
        final class_268 team = entry.method_2955();
        if (team == null) {
            return null;
        }
        final class_2561 prefix = team.method_1144();
        if (prefix == null) {
            return null;
        }
        final String prefixStr = prefix.getString();
        for (final String rank : StaffsWidget.RANK_MAPPING.keySet()) {
            if (prefixStr.toLowerCase().contains(rank)) {
                return rank;
            }
        }
        return this.parseRankFromText(prefix);
    }
    
    private List<Staff> getOnlineStaff() {
        final List<Staff> staff = new ArrayList<Staff>();
        if (StaffsWidget.mc.field_1724 == null || StaffsWidget.mc.field_1724.field_3944 == null || StaffsWidget.mc.field_1687 == null) {
            return staff;
        }
        for (class_640 player : StaffsWidget.mc.field_1724.field_3944.method_2880()) {
            final class_268 team = player.method_2955();
            if (team == null) {
                continue;
            }
            final String name = player.method_2966().getName();
            if (!PlayerUtil.isValidName(name)) {
                continue;
            }
            final String prefix = ReplaceUtil.replaceSymbols(team.method_1144().getString());
            if (!StaffManager.getInstance().contains(name) && !this.isStaffPrefix(prefix.toLowerCase())) {
                continue;
            }
            Status status = Status.ONLINE;
            if (player.method_2958() == class_1934.field_9219) {
                status = Status.GM3;
            }
            else if (StaffsWidget.mc.field_1687.method_18456().stream().anyMatch(p -> p.method_7334().getName().equals(name))) {
                status = Status.NEAR;
            }
            staff.add(new Staff(prefix + " " + name, status));
        }
        return staff;
    }
    
    private List<Staff> getVanishedPlayers() {
        final List<Staff> vanished = new ArrayList<Staff>();
        if (StaffsWidget.mc.field_1687 == null || StaffsWidget.mc.method_1562() == null) {
            return vanished;
        }
        final Set<String> onlineNames = new HashSet<String>();
        for (final class_640 entry : StaffsWidget.mc.method_1562().method_2880()) {
            onlineNames.add(entry.method_2966().getName());
        }
        for (final class_268 team : StaffsWidget.mc.field_1687.method_8428().method_1159()) {
            for (final String name : team.method_1204()) {
                if (!PlayerUtil.isValidName(name)) {
                    continue;
                }
                if (onlineNames.contains(name)) {
                    continue;
                }
                vanished.add(new Staff(name, Status.VANISH));
            }
        }
        return vanished;
    }
    
    private boolean isStaffPrefix(final String prefix) {
        return prefix.contains("helper") || prefix.contains("moder") || prefix.contains("admin") || prefix.contains("owner") || prefix.contains("developer") || prefix.contains("staff") || prefix.contains("curator") || prefix.contains("\u043a\u0443\u0440\u0430\u0442\u043e\u0440") || prefix.contains("\u0440\u0430\u0437\u0440\u0430\u0431") || prefix.contains("\u043c\u043e\u0434\u0435\u0440") || prefix.contains("\u0430\u0434\u043c\u0438\u043d") || prefix.contains("\u0441\u0442\u0430\u0436\u0435\u0440") || prefix.contains("\u0441\u0442\u0430\u0436\u0451\u0440") || prefix.contains("\u0445\u0435\u043b\u043f\u0435\u0440");
    }
    
    @Generated
    public ModeSetting getMode() {
        return this.mode;
    }
    
    static {
        RANK_MAPPING = new HashMap<String, String>();
        RANK_COLORS = new HashMap<String, Color>();
        StaffsWidget.RANK_MAPPING.put("admin", "\u0410\u0434\u043c\u0438\u043d");
        StaffsWidget.RANK_MAPPING.put("moder", "\u041c\u043e\u0434\u0435\u0440");
        StaffsWidget.RANK_MAPPING.put("smoder", "\u0421\u0442. \u041c\u043e\u0434\u0435\u0440");
        StaffsWidget.RANK_MAPPING.put("helper", "\u0425\u0435\u043b\u043f\u0435\u0440");
        StaffsWidget.RANK_MAPPING.put("shelper", "\u0421\u0442. \u0425\u0435\u043b\u043f\u0435\u0440");
        StaffsWidget.RANK_MAPPING.put("youtuber", "\u042e\u0442\u0443\u0431\u0435\u0440");
        StaffsWidget.RANK_MAPPING.put("custom", "\u041a\u0430\u0441\u0442\u043e\u043c");
        StaffsWidget.RANK_COLORS.put("admin", new Color(255, 0, 0));
        StaffsWidget.RANK_COLORS.put("moder", new Color(128, 0, 128));
        StaffsWidget.RANK_COLORS.put("smoder", new Color(0, 0, 255));
        StaffsWidget.RANK_COLORS.put("helper", new Color(0, 255, 0));
        StaffsWidget.RANK_COLORS.put("shelper", new Color(255, 255, 0));
        StaffsWidget.RANK_COLORS.put("youtuber", new Color(255, 175, 20));
        StaffsWidget.RANK_COLORS.put("custom", Color.WHITE);
    }
    
    record Staff(String name, Status status) {}
    
    public enum Status
    {
        ONLINE("Online"), 
        NEAR("Near"), 
        GM3("Gm3"), 
        VANISH("Vanish");
        
        private final String label;
        
        @Generated
        public String getLabel() {
            return this.label;
        }
        
        @Generated
        private Status(final String label) {
            this.label = label;
        }
    }
}
