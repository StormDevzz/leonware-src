/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1934
 *  net.minecraft.class_2561
 *  net.minecraft.class_2583
 *  net.minecraft.class_268
 *  net.minecraft.class_640
 */
package sweetie.leonware.client.ui.widget.overlay;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import net.minecraft.class_1934;
import net.minecraft.class_2561;
import net.minecraft.class_2583;
import net.minecraft.class_268;
import net.minecraft.class_640;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.system.configs.StaffManager;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.framelimiter.FrameLimiter;
import sweetie.leonware.api.utils.other.ReplaceUtil;
import sweetie.leonware.api.utils.player.PlayerUtil;
import sweetie.leonware.client.ui.widget.ContainerWidget;

public class StaffsWidget
extends ContainerWidget {
    private final FrameLimiter frameLimiter = new FrameLimiter(false);
    private List<Staff> cacheStaffs = new ArrayList<Staff>();
    public final ModeSetting mode = new ModeSetting("Staffs Mode").values("\u041e\u0431\u044b\u0447\u043d\u044b\u0439", "\u0410\u0440\u0435\u0441\u041c\u0430\u0439\u043d").value("\u041e\u0431\u044b\u0447\u043d\u044b\u0439");
    private static final Map<String, String> RANK_MAPPING = new HashMap<String, String>();
    private static final Map<String, Color> RANK_COLORS = new HashMap<String, Color>();

    @Override
    public String getName() {
        return "Staffs";
    }

    public StaffsWidget() {
        super(100.0f, 100.0f);
    }

    @Override
    protected Map<String, ContainerWidget.ContainerElement.ColoredString> getCurrentData() {
        LinkedHashMap<String, ContainerWidget.ContainerElement.ColoredString> map = new LinkedHashMap<String, ContainerWidget.ContainerElement.ColoredString>();
        for (Staff staff : this.getStaffList()) {
            if (this.mode.is("\u0410\u0440\u0435\u0441\u041c\u0430\u0439\u043d")) {
                String[] parts = staff.name().split(":", 3);
                if (parts.length < 3) continue;
                String name = parts[0];
                String statusStr = parts[1];
                String role = parts[2];
                String rankLabel = RANK_MAPPING.getOrDefault(role.toLowerCase(), role);
                Color rankColor = role.equals("custom") ? UIColors.primary() : RANK_COLORS.getOrDefault(role.toLowerCase(), Color.WHITE);
                Color statusColor = Color.GREEN;
                String statusLabel = "";
                if (statusStr.equalsIgnoreCase("vanish")) {
                    statusColor = Color.RED;
                    statusLabel = "[V]";
                } else if (statusStr.equalsIgnoreCase("gm3")) {
                    statusColor = Color.YELLOW;
                    statusLabel = "[V]";
                }
                map.put(name, new ContainerWidget.ContainerElement.ColoredString(rankLabel, rankColor, name, Color.WHITE, statusLabel, statusColor));
                continue;
            }
            map.put(staff.name(), new ContainerWidget.ContainerElement.ColoredString(staff.status().name(), UIColors.positiveColor()));
        }
        return map;
    }

    public List<Staff> getStaffList() {
        this.frameLimiter.execute(15, () -> {
            ArrayList<Staff> list = new ArrayList<Staff>();
            if (!mc.method_1542()) {
                if (this.mode.is("\u0410\u0440\u0435\u0441\u041c\u0430\u0439\u043d")) {
                    list.addAll(this.getAresMineStaff());
                } else {
                    list.addAll(this.getOnlineStaff());
                    list.addAll(this.getVanishedPlayers());
                }
            }
            this.cacheStaffs = list;
        });
        return this.cacheStaffs;
    }

    private List<Staff> getAresMineStaff() {
        ArrayList<Staff> staff = new ArrayList<Staff>();
        if (StaffsWidget.mc.field_1724 == null || StaffsWidget.mc.field_1724.field_3944 == null) {
            return staff;
        }
        HashSet<String> validRanks = new HashSet<String>(RANK_MAPPING.keySet());
        for (class_640 entry : StaffsWidget.mc.field_1724.field_3944.method_2880()) {
            String name = entry.method_2966().getName();
            if (!PlayerUtil.isValidName(name)) continue;
            class_2561 displayName = entry.method_2971();
            String prefixString = null;
            if (displayName != null) {
                prefixString = this.parseRankFromText(displayName);
            }
            if (prefixString == null) {
                prefixString = this.parseRankFromTeam(entry);
            }
            if (prefixString == null || !validRanks.contains(prefixString.toLowerCase())) continue;
            Iterator status = entry.method_2958() == class_1934.field_9219 ? "gm3" : "active";
            staff.add(new Staff(name + ":" + (String)((Object)status) + ":" + prefixString.toLowerCase(), Status.ONLINE));
        }
        if (StaffsWidget.mc.field_1687 != null) {
            for (class_268 team : StaffsWidget.mc.field_1687.method_8428().method_1159()) {
                class_2561 prefix = team.method_1144();
                if (prefix == null || prefix.getString().isEmpty()) continue;
                Object rank = this.parseRankFromText(prefix);
                if (rank == null) {
                    String prefixStr = prefix.getString().toLowerCase();
                    for (String r : validRanks) {
                        if (!prefixStr.contains(r)) continue;
                        rank = r;
                        break;
                    }
                }
                if (rank == null || !validRanks.contains(((String)rank).toLowerCase())) continue;
                Object finalRank = rank;
                for (String name : team.method_1204()) {
                    if (!PlayerUtil.isValidName(name) || !staff.stream().noneMatch(s -> s.name().split(":")[0].equals(name))) continue;
                    staff.add(new Staff(name + ":gm3:" + ((String)finalRank).toLowerCase(), Status.GM3));
                }
            }
        }
        for (String manualName : StaffManager.getInstance().getData()) {
            if (!staff.stream().noneMatch(s -> s.name().split(":")[0].equalsIgnoreCase(manualName))) continue;
            class_640 found = null;
            if (StaffsWidget.mc.field_1724.field_3944 != null) {
                for (class_640 entry : StaffsWidget.mc.field_1724.field_3944.method_2880()) {
                    if (!entry.method_2966().getName().equalsIgnoreCase(manualName)) continue;
                    found = entry;
                    break;
                }
            }
            String status = null;
            if (found != null) {
                status = found.method_2958() == class_1934.field_9219 ? "gm3" : "active";
            } else if (StaffsWidget.mc.field_1687 != null) {
                block6: for (class_268 team : StaffsWidget.mc.field_1687.method_8428().method_1159()) {
                    for (String name : team.method_1204()) {
                        if (!name.equalsIgnoreCase(manualName)) continue;
                        status = "vanish";
                        break block6;
                    }
                }
            }
            if (status == null) continue;
            staff.add(new Staff(manualName + ":" + status + ":custom", Status.ONLINE));
        }
        staff.sort((a, b) -> a.name().split(":")[0].compareTo(b.name().split(":")[0]));
        return staff;
    }

    private String parseRankFromText(class_2561 text) {
        String rank;
        String s;
        if (text == null) {
            return null;
        }
        class_2583 style = text.method_10866();
        if (style != null && style.method_27708() != null && (s = style.method_27708().toString()).contains("custom:groups/") && !(rank = s.substring(s.lastIndexOf("/") + 1).toLowerCase()).equals("default")) {
            return rank;
        }
        s = text.getString();
        if (s != null && !s.isEmpty()) {
            for (String rank2 : RANK_MAPPING.keySet()) {
                if (!s.toLowerCase().contains(rank2)) continue;
                return rank2;
            }
        }
        for (class_2561 sibling : text.method_10855()) {
            String result = this.parseRankFromText(sibling);
            if (result == null) continue;
            return result;
        }
        return null;
    }

    private String parseRankFromTeam(class_640 entry) {
        class_268 team = entry.method_2955();
        if (team == null) {
            return null;
        }
        class_2561 prefix = team.method_1144();
        if (prefix == null) {
            return null;
        }
        String prefixStr = prefix.getString();
        for (String rank : RANK_MAPPING.keySet()) {
            if (!prefixStr.toLowerCase().contains(rank)) continue;
            return rank;
        }
        return this.parseRankFromText(prefix);
    }

    private List<Staff> getOnlineStaff() {
        ArrayList<Staff> staff = new ArrayList<Staff>();
        if (StaffsWidget.mc.field_1724 == null || StaffsWidget.mc.field_1724.field_3944 == null || StaffsWidget.mc.field_1687 == null) {
            return staff;
        }
        for (class_640 player : StaffsWidget.mc.field_1724.field_3944.method_2880()) {
            String name;
            class_268 team = player.method_2955();
            if (team == null || !PlayerUtil.isValidName(name = player.method_2966().getName())) continue;
            String prefix = ReplaceUtil.replaceSymbols(team.method_1144().getString());
            if (!StaffManager.getInstance().contains(name) && !this.isStaffPrefix(prefix.toLowerCase())) continue;
            Status status = Status.ONLINE;
            if (player.method_2958() == class_1934.field_9219) {
                status = Status.GM3;
            } else if (StaffsWidget.mc.field_1687.method_18456().stream().anyMatch(p -> p.method_7334().getName().equals(name))) {
                status = Status.NEAR;
            }
            staff.add(new Staff(prefix + " " + name, status));
        }
        return staff;
    }

    private List<Staff> getVanishedPlayers() {
        ArrayList<Staff> vanished = new ArrayList<Staff>();
        if (StaffsWidget.mc.field_1687 == null || mc.method_1562() == null) {
            return vanished;
        }
        HashSet<String> onlineNames = new HashSet<String>();
        for (class_640 entry : mc.method_1562().method_2880()) {
            onlineNames.add(entry.method_2966().getName());
        }
        for (class_268 team : StaffsWidget.mc.field_1687.method_8428().method_1159()) {
            for (String name : team.method_1204()) {
                if (!PlayerUtil.isValidName(name) || onlineNames.contains(name)) continue;
                vanished.add(new Staff(name, Status.VANISH));
            }
        }
        return vanished;
    }

    private boolean isStaffPrefix(String prefix) {
        return prefix.contains("helper") || prefix.contains("moder") || prefix.contains("admin") || prefix.contains("owner") || prefix.contains("developer") || prefix.contains("staff") || prefix.contains("curator") || prefix.contains("\u043a\u0443\u0440\u0430\u0442\u043e\u0440") || prefix.contains("\u0440\u0430\u0437\u0440\u0430\u0431") || prefix.contains("\u043c\u043e\u0434\u0435\u0440") || prefix.contains("\u0430\u0434\u043c\u0438\u043d") || prefix.contains("\u0441\u0442\u0430\u0436\u0435\u0440") || prefix.contains("\u0441\u0442\u0430\u0436\u0451\u0440") || prefix.contains("\u0445\u0435\u043b\u043f\u0435\u0440");
    }

    @Generated
    public ModeSetting getMode() {
        return this.mode;
    }

    static {
        RANK_MAPPING.put("admin", "\u0410\u0434\u043c\u0438\u043d");
        RANK_MAPPING.put("moder", "\u041c\u043e\u0434\u0435\u0440");
        RANK_MAPPING.put("smoder", "\u0421\u0442. \u041c\u043e\u0434\u0435\u0440");
        RANK_MAPPING.put("helper", "\u0425\u0435\u043b\u043f\u0435\u0440");
        RANK_MAPPING.put("shelper", "\u0421\u0442. \u0425\u0435\u043b\u043f\u0435\u0440");
        RANK_MAPPING.put("youtuber", "\u042e\u0442\u0443\u0431\u0435\u0440");
        RANK_MAPPING.put("custom", "\u041a\u0430\u0441\u0442\u043e\u043c");
        RANK_COLORS.put("admin", new Color(255, 0, 0));
        RANK_COLORS.put("moder", new Color(128, 0, 128));
        RANK_COLORS.put("smoder", new Color(0, 0, 255));
        RANK_COLORS.put("helper", new Color(0, 255, 0));
        RANK_COLORS.put("shelper", new Color(255, 255, 0));
        RANK_COLORS.put("youtuber", new Color(255, 175, 20));
        RANK_COLORS.put("custom", Color.WHITE);
    }

    public record Staff(String name, Status status) {
    }

    public static enum Status {
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
        private Status(String label) {
            this.label = label;
        }
    }
}

