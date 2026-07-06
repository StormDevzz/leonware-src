package sweetie.leonware.client.ui.widget.overlay;

import java.awt.Color;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/widget/overlay/StaffsWidget.class */
public class StaffsWidget extends ContainerWidget {
    private final FrameLimiter frameLimiter;
    private List<Staff> cacheStaffs;
    public final ModeSetting mode;
    private static final Map<String, String> RANK_MAPPING = new HashMap();
    private static final Map<String, Color> RANK_COLORS = new HashMap();

    @Generated
    public ModeSetting getMode() {
        return this.mode;
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/widget/overlay/StaffsWidget$Staff.class */
    public static final class Staff extends Record {
        private final String name;
        private final Status status;

        public Staff(String name, Status status) {
            this.name = name;
            this.status = status;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Staff.class), Staff.class, "name;status", "FIELD:Lsweetie/leonware/client/ui/widget/overlay/StaffsWidget$Staff;->name:Ljava/lang/String;", "FIELD:Lsweetie/leonware/client/ui/widget/overlay/StaffsWidget$Staff;->status:Lsweetie/leonware/client/ui/widget/overlay/StaffsWidget$Status;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Staff.class), Staff.class, "name;status", "FIELD:Lsweetie/leonware/client/ui/widget/overlay/StaffsWidget$Staff;->name:Ljava/lang/String;", "FIELD:Lsweetie/leonware/client/ui/widget/overlay/StaffsWidget$Staff;->status:Lsweetie/leonware/client/ui/widget/overlay/StaffsWidget$Status;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Staff.class, Object.class), Staff.class, "name;status", "FIELD:Lsweetie/leonware/client/ui/widget/overlay/StaffsWidget$Staff;->name:Ljava/lang/String;", "FIELD:Lsweetie/leonware/client/ui/widget/overlay/StaffsWidget$Staff;->status:Lsweetie/leonware/client/ui/widget/overlay/StaffsWidget$Status;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public String name() {
            return this.name;
        }

        public Status status() {
            return this.status;
        }
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/ui/widget/overlay/StaffsWidget$Status.class */
    public enum Status {
        ONLINE("Online"),
        NEAR("Near"),
        GM3("Gm3"),
        VANISH("Vanish");

        private final String label;

        @Generated
        Status(final String label) {
            this.label = label;
        }

        @Generated
        public String getLabel() {
            return this.label;
        }
    }

    static {
        RANK_MAPPING.put("admin", "Админ");
        RANK_MAPPING.put("moder", "Модер");
        RANK_MAPPING.put("smoder", "Ст. Модер");
        RANK_MAPPING.put("helper", "Хелпер");
        RANK_MAPPING.put("shelper", "Ст. Хелпер");
        RANK_MAPPING.put("youtuber", "Ютубер");
        RANK_MAPPING.put("custom", "Кастом");
        RANK_COLORS.put("admin", new Color(255, 0, 0));
        RANK_COLORS.put("moder", new Color(128, 0, 128));
        RANK_COLORS.put("smoder", new Color(0, 0, 255));
        RANK_COLORS.put("helper", new Color(0, 255, 0));
        RANK_COLORS.put("shelper", new Color(255, 255, 0));
        RANK_COLORS.put("youtuber", new Color(255, 175, 20));
        RANK_COLORS.put("custom", Color.WHITE);
    }

    @Override // sweetie.leonware.client.ui.widget.Widget
    public String getName() {
        return "Staffs";
    }

    public StaffsWidget() {
        super(100.0f, 100.0f);
        this.frameLimiter = new FrameLimiter(false);
        this.cacheStaffs = new ArrayList();
        this.mode = new ModeSetting("Staffs Mode").values("Обычный", "АресМайн").value("Обычный");
    }

    @Override // sweetie.leonware.client.ui.widget.ContainerWidget
    protected Map<String, ContainerWidget.ContainerElement.ColoredString> getCurrentData() {
        Map<String, ContainerWidget.ContainerElement.ColoredString> map = new LinkedHashMap<>();
        for (Staff staff : getStaffList()) {
            if (this.mode.is("АресМайн")) {
                String[] parts = staff.name().split(":", 3);
                if (parts.length >= 3) {
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
                }
            } else {
                map.put(staff.name(), new ContainerWidget.ContainerElement.ColoredString(staff.status().name(), UIColors.positiveColor()));
            }
        }
        return map;
    }

    public List<Staff> getStaffList() {
        this.frameLimiter.execute(15, () -> {
            List<Staff> list = new ArrayList<>();
            if (!mc.method_1542()) {
                if (this.mode.is("АресМайн")) {
                    list.addAll(getAresMineStaff());
                } else {
                    list.addAll(getOnlineStaff());
                    list.addAll(getVanishedPlayers());
                }
            }
            this.cacheStaffs = list;
        });
        return this.cacheStaffs;
    }

    private List<Staff> getAresMineStaff() {
        List<Staff> staff = new ArrayList<>();
        if (mc.field_1724 == null || mc.field_1724.field_3944 == null) {
            return staff;
        }
        Set<String> validRanks = new HashSet<>(RANK_MAPPING.keySet());
        for (class_640 entry : mc.field_1724.field_3944.method_2880()) {
            String name = entry.method_2966().getName();
            if (PlayerUtil.isValidName(name)) {
                class_2561 displayName = entry.method_2971();
                String prefixString = displayName != null ? parseRankFromText(displayName) : null;
                if (prefixString == null) {
                    prefixString = parseRankFromTeam(entry);
                }
                if (prefixString != null && validRanks.contains(prefixString.toLowerCase())) {
                    staff.add(new Staff(name + ":" + (entry.method_2958() == class_1934.field_9219 ? "gm3" : "active") + ":" + prefixString.toLowerCase(), Status.ONLINE));
                }
            }
        }
        if (mc.field_1687 != null) {
            for (class_268 team : mc.field_1687.method_8428().method_1159()) {
                class_2561 prefix = team.method_1144();
                if (prefix != null && !prefix.getString().isEmpty()) {
                    String rank = parseRankFromText(prefix);
                    if (rank == null) {
                        String prefixStr = prefix.getString().toLowerCase();
                        Iterator<String> it = validRanks.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            String r = it.next();
                            if (prefixStr.contains(r)) {
                                rank = r;
                                break;
                            }
                        }
                    }
                    if (rank != null && validRanks.contains(rank.toLowerCase())) {
                        String finalRank = rank;
                        for (String name2 : team.method_1204()) {
                            if (PlayerUtil.isValidName(name2) && staff.stream().noneMatch(s -> {
                                return s.name().split(":")[0].equals(name2);
                            })) {
                                staff.add(new Staff(name2 + ":gm3:" + finalRank.toLowerCase(), Status.GM3));
                            }
                        }
                    }
                }
            }
        }
        for (String manualName : StaffManager.getInstance().getData()) {
            if (staff.stream().noneMatch(s2 -> {
                return s2.name().split(":")[0].equalsIgnoreCase(manualName);
            })) {
                class_640 found = null;
                if (mc.field_1724.field_3944 != null) {
                    Iterator it2 = mc.field_1724.field_3944.method_2880().iterator();
                    while (true) {
                        if (!it2.hasNext()) {
                            break;
                        }
                        class_640 entry2 = (class_640) it2.next();
                        if (entry2.method_2966().getName().equalsIgnoreCase(manualName)) {
                            found = entry2;
                            break;
                        }
                    }
                }
                String status = null;
                if (found != null) {
                    status = found.method_2958() == class_1934.field_9219 ? "gm3" : "active";
                } else if (mc.field_1687 != null) {
                    Iterator it3 = mc.field_1687.method_8428().method_1159().iterator();
                    while (true) {
                        if (!it3.hasNext()) {
                            break;
                        }
                        Iterator it4 = ((class_268) it3.next()).method_1204().iterator();
                        while (it4.hasNext()) {
                            if (((String) it4.next()).equalsIgnoreCase(manualName)) {
                                status = "vanish";
                                break;
                            }
                        }
                    }
                }
                if (status != null) {
                    staff.add(new Staff(manualName + ":" + status + ":custom", Status.ONLINE));
                }
            }
        }
        staff.sort((a, b) -> {
            return a.name().split(":")[0].compareTo(b.name().split(":")[0]);
        });
        return staff;
    }

    private String parseRankFromText(class_2561 text) {
        if (text == null) {
            return null;
        }
        class_2583 style = text.method_10866();
        if (style != null && style.method_27708() != null) {
            String s = style.method_27708().toString();
            if (s.contains("custom:groups/")) {
                String rank = s.substring(s.lastIndexOf("/") + 1).toLowerCase();
                if (!rank.equals("default")) {
                    return rank;
                }
            }
        }
        String s2 = text.getString();
        if (s2 != null && !s2.isEmpty()) {
            for (String rank2 : RANK_MAPPING.keySet()) {
                if (s2.toLowerCase().contains(rank2)) {
                    return rank2;
                }
            }
        }
        for (class_2561 sibling : text.method_10855()) {
            String result = parseRankFromText(sibling);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private String parseRankFromTeam(class_640 entry) {
        class_2561 prefix;
        class_268 team = entry.method_2955();
        if (team == null || (prefix = team.method_1144()) == null) {
            return null;
        }
        String prefixStr = prefix.getString();
        for (String rank : RANK_MAPPING.keySet()) {
            if (prefixStr.toLowerCase().contains(rank)) {
                return rank;
            }
        }
        return parseRankFromText(prefix);
    }

    private List<Staff> getOnlineStaff() {
        List<Staff> staff = new ArrayList<>();
        if (mc.field_1724 == null || mc.field_1724.field_3944 == null || mc.field_1687 == null) {
            return staff;
        }
        for (class_640 player : mc.field_1724.field_3944.method_2880()) {
            class_268 team = player.method_2955();
            if (team != null) {
                String name = player.method_2966().getName();
                if (PlayerUtil.isValidName(name)) {
                    String prefix = ReplaceUtil.replaceSymbols(team.method_1144().getString());
                    if (StaffManager.getInstance().contains(name) || isStaffPrefix(prefix.toLowerCase())) {
                        Status status = Status.ONLINE;
                        if (player.method_2958() == class_1934.field_9219) {
                            status = Status.GM3;
                        } else if (mc.field_1687.method_18456().stream().anyMatch(p -> {
                            return p.method_7334().getName().equals(name);
                        })) {
                            status = Status.NEAR;
                        }
                        staff.add(new Staff(prefix + " " + name, status));
                    }
                }
            }
        }
        return staff;
    }

    private List<Staff> getVanishedPlayers() {
        List<Staff> vanished = new ArrayList<>();
        if (mc.field_1687 == null || mc.method_1562() == null) {
            return vanished;
        }
        Set<String> onlineNames = new HashSet<>();
        for (class_640 entry : mc.method_1562().method_2880()) {
            onlineNames.add(entry.method_2966().getName());
        }
        for (class_268 team : mc.field_1687.method_8428().method_1159()) {
            for (String name : team.method_1204()) {
                if (PlayerUtil.isValidName(name) && !onlineNames.contains(name)) {
                    vanished.add(new Staff(name, Status.VANISH));
                }
            }
        }
        return vanished;
    }

    private boolean isStaffPrefix(String prefix) {
        return prefix.contains("helper") || prefix.contains("moder") || prefix.contains("admin") || prefix.contains("owner") || prefix.contains("developer") || prefix.contains("staff") || prefix.contains("curator") || prefix.contains("куратор") || prefix.contains("разраб") || prefix.contains("модер") || prefix.contains("админ") || prefix.contains("стажер") || prefix.contains("стажёр") || prefix.contains("хелпер");
    }
}
