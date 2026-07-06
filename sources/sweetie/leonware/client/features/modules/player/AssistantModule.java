package sweetie.leonware.client.features.modules.player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1304;
import net.minecraft.class_1707;
import net.minecraft.class_1713;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_268;
import net.minecraft.class_2680;
import net.minecraft.class_2767;
import net.minecraft.class_2848;
import net.minecraft.class_4587;
import net.minecraft.class_640;
import net.minecraft.class_7439;
import org.joml.Vector2f;
import org.joml.Vector2i;
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
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.MultiBooleanSetting;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.system.backend.Pair;
import sweetie.leonware.api.system.client.GpsManager;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.api.utils.math.ProjectionUtil;
import sweetie.leonware.api.utils.player.InventoryUtil;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.fonts.Fonts;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/player/AssistantModule.class */
@ModuleRegister(name = "Assistant", category = Category.PLAYER)
public class AssistantModule extends Module {
    private static final AssistantModule instance = new AssistantModule();
    boolean need;
    public boolean fireworkUse;
    private final Supplier<Boolean> isHotkeysEnabled = () -> {
        return Boolean.valueOf(getFunctions().isEnabled("Hotkeys"));
    };
    private final Supplier<Boolean> isHWKeys = () -> {
        return Boolean.valueOf(this.isHotkeysEnabled.get().booleanValue() && getMode().is("Holy World"));
    };
    private final Supplier<Boolean> isFTKeys = () -> {
        return Boolean.valueOf(this.isHotkeysEnabled.get().booleanValue() && getMode().is("Fun Time"));
    };
    private final Supplier<Boolean> isLGKeys = () -> {
        return Boolean.valueOf(this.isHotkeysEnabled.get().booleanValue() && getMode().is("Lony Grief"));
    };
    private final Supplier<Boolean> isRWKeys = () -> {
        return Boolean.valueOf(this.isHotkeysEnabled.get().booleanValue() && getMode().is("Reallyworld"));
    };
    private Mode currentMode = Mode.FUNTIME;
    private String targetRival = null;
    private final MultiBooleanSetting functions = new MultiBooleanSetting("Functions").value(new BooleanSetting("Hotkeys").value((Boolean) true), new BooleanSetting("Timers").value((Boolean) false));
    public final BooleanSetting autoPvpLony = new BooleanSetting("Auto-PvP").value((Boolean) true).setVisible(this.isLGKeys);
    private final MultiBooleanSetting lonyPrefixes = new MultiBooleanSetting("Prefixes").value(new BooleanSetting("Player").value((Boolean) true), new BooleanSetting("Legenda").value((Boolean) true), new BooleanSetting("Pravitel").value((Boolean) true), new BooleanSetting("Povelitel").value((Boolean) true), new BooleanSetting("D.Admin").value((Boolean) true), new BooleanSetting("Staff").value((Boolean) false), new BooleanSetting("Eternity").value((Boolean) true), new BooleanSetting("Luxe").value((Boolean) true)).setVisible(() -> {
        return Boolean.valueOf(this.isLGKeys.get().booleanValue() && this.autoPvpLony.getValue().booleanValue());
    });
    private final ModeSetting mode = new ModeSetting("Mode").value("Fun Time").values("Fun Time", "Holy World", "Lony Grief", "Reallyworld").setVisible(this.isHotkeysEnabled).onAction2(() -> {
        Mode mode;
        switch (getMode().getValue()) {
            case "Fun Time":
                mode = Mode.FUNTIME;
                break;
            case "Lony Grief":
                mode = Mode.LONYGRIEF;
                break;
            case "Reallyworld":
                mode = Mode.REALLYWORLD;
                break;
            default:
                mode = Mode.HOLYWORLD;
                break;
        }
        this.currentMode = mode;
    });
    private final BooleanSetting legit = new BooleanSetting("Legit").value((Boolean) true).setVisible(this.isHotkeysEnabled);
    public final BooleanSetting autoPlayerLony = new BooleanSetting("Auto-Player").value((Boolean) true).setVisible(this.isLGKeys);
    public final BooleanSetting antipolet = new BooleanSetting("Анти-полет обход").value((Boolean) false).setVisible(this.isRWKeys);
    private final Map<InventoryUtil.ItemUsage, Pair<BindSetting, Mode>> keyBindings = new LinkedHashMap();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final List<Pair<Long, class_243>> consumables = new ArrayList();
    private final Map<class_243, String> consumableNames = new HashMap();

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/player/AssistantModule$Mode.class */
    public enum Mode {
        FUNTIME,
        HOLYWORLD,
        LONYGRIEF,
        REALLYWORLD
    }

    @Generated
    public static AssistantModule getInstance() {
        return instance;
    }

    @Generated
    public MultiBooleanSetting getFunctions() {
        return this.functions;
    }

    @Generated
    public MultiBooleanSetting getLonyPrefixes() {
        return this.lonyPrefixes;
    }

    @Generated
    public ModeSetting getMode() {
        return this.mode;
    }

    /* JADX WARN: Type inference failed for: r1v16, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v19, types: [sweetie.leonware.api.module.setting.MultiBooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v24, types: [sweetie.leonware.api.module.setting.ModeSetting] */
    /* JADX WARN: Type inference failed for: r1v27, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v30, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v33, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    public AssistantModule() {
        this.keyBindings.put(new InventoryUtil.ItemUsage(class_1802.field_8449, this), new Pair<>(new BindSetting("Disorientation").value((Integer) (-999)), Mode.FUNTIME));
        this.keyBindings.put(new InventoryUtil.ItemUsage(class_1802.field_22021, this), new Pair<>(new BindSetting("Trap").value((Integer) (-999)), Mode.FUNTIME));
        this.keyBindings.put(new InventoryUtil.ItemUsage(class_1802.field_8479, this), new Pair<>(new BindSetting("Clear dust").value((Integer) (-999)), Mode.FUNTIME));
        this.keyBindings.put(new InventoryUtil.ItemUsage(class_1802.field_8814, this), new Pair<>(new BindSetting("Fire whirl").value((Integer) (-999)), Mode.FUNTIME));
        this.keyBindings.put(new InventoryUtil.ItemUsage(class_1802.field_8551, this), new Pair<>(new BindSetting("Plast").value((Integer) (-999)), Mode.FUNTIME));
        this.keyBindings.put(new InventoryUtil.ItemUsage(class_1802.field_8614, this), new Pair<>(new BindSetting("Divine aura").value((Integer) (-999)), Mode.FUNTIME));
        this.keyBindings.put(new InventoryUtil.ItemUsage(class_1802.field_8662, this), new Pair<>(new BindSetting("Explosive trap").value((Integer) (-999)), Mode.HOLYWORLD));
        this.keyBindings.put(new InventoryUtil.ItemUsage(class_1802.field_8882, this), new Pair<>(new BindSetting("Default trap").value((Integer) (-999)), Mode.HOLYWORLD));
        this.keyBindings.put(new InventoryUtil.ItemUsage(class_1802.field_8137, this), new Pair<>(new BindSetting("Stun").value((Integer) (-999)), Mode.HOLYWORLD));
        this.keyBindings.put(new InventoryUtil.ItemUsage(class_1802.field_8814, this), new Pair<>(new BindSetting("Explosive thing").value((Integer) (-999)), Mode.HOLYWORLD));
        this.keyBindings.put(new InventoryUtil.ItemUsage(class_1802.field_8543, this), new Pair<>(new BindSetting("Snowball").value((Integer) (-999)), Mode.HOLYWORLD));
        this.keyBindings.put(new InventoryUtil.ItemUsage(class_1802.field_8696, this), new Pair<>(new BindSetting("Ливалочка").value((Integer) (-999)), Mode.LONYGRIEF));
        this.keyBindings.put(new InventoryUtil.ItemUsage(class_1802.field_8639, this), new Pair<>(new BindSetting("Фейерверк").value((Integer) (-999)), Mode.REALLYWORLD));
        addSettings(this.functions, this.mode, this.lonyPrefixes, this.legit, this.autoPvpLony, this.autoPlayerLony, this.antipolet);
        this.keyBindings.forEach((key, value) -> {
            if (value.right() == Mode.HOLYWORLD) {
                ((BindSetting) value.left()).setVisible(this.isHWKeys);
            }
            if (value.right() == Mode.FUNTIME) {
                ((BindSetting) value.left()).setVisible(this.isFTKeys);
            }
            if (value.right() == Mode.LONYGRIEF) {
                ((BindSetting) value.left()).setVisible(this.isLGKeys);
            }
            if (value.right() == Mode.REALLYWORLD) {
                ((BindSetting) value.left()).setVisible(this.isRWKeys);
            }
            addSettings((Setting) value.left());
        });
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener(event -> {
            handleTickEvent();
        }));
        EventListener renderEvent = Render2DEvent.getInstance().subscribe(new Listener(this::handleRenderEvent));
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener(this::handlePacketEvent));
        addEvents(tickEvent, renderEvent, packetEvent);
    }

    private void handlePacketEvent(PacketEvent.PacketEventData event) {
        if (event.isSend() || mc.field_1687 == null) {
            return;
        }
        class_2596<?> class_2596VarPacket = event.packet();
        if (class_2596VarPacket instanceof class_2767) {
            class_2767 soundPacket = (class_2767) class_2596VarPacket;
            handleSoundPackets(soundPacket);
        }
        class_7439 class_7439VarPacket = event.packet();
        if (class_7439VarPacket instanceof class_7439) {
            class_7439 p = class_7439VarPacket;
            String msg = p.comp_763().getString();
            if (msg.contains("Анти Полет » Вы не можете взлететь!")) {
                this.need = true;
            }
        }
        if (this.autoPvpLony.getValue().booleanValue() && this.currentMode == Mode.LONYGRIEF) {
            class_7439 class_7439VarPacket2 = event.packet();
            if (class_7439VarPacket2 instanceof class_7439) {
                class_7439 chatPacket = class_7439VarPacket2;
                String rawMsg = chatPacket.comp_763().getString();
                String cleanMsg = rawMsg.replaceAll("(?i)§[0-9a-fklmnor]", " ").replace("›", " ").replace(">", " ").trim();
                Pattern pvpPattern = Pattern.compile("Игрок\\s+([a-zA-Z0-9_]{3,16})\\s+ищет", 66);
                Matcher pvpMatcher = pvpPattern.matcher(cleanMsg);
                if (pvpMatcher.find()) {
                    String nickname = pvpMatcher.group(1);
                    if (nickname.equalsIgnoreCase(mc.field_1724.method_5477().getString())) {
                        return;
                    }
                    String rawPrefix = getPlayerRankFromTab(nickname);
                    if (shouldClickPvp(rawPrefix)) {
                        this.targetRival = nickname;
                    }
                }
                if (this.autoPlayerLony.getValue().booleanValue()) {
                    Pattern pattern = Pattern.compile("(?:игроком|игрок)\\s+([^\\s!]+).+?(?:координаты|Кординаты|X):?\\s*(-?\\d+)[^\\d-]+-?\\d+[^\\d-]+(-?\\d+)", 66);
                    Matcher matcher = pattern.matcher(cleanMsg);
                    if (matcher.find()) {
                        String name = matcher.group(1);
                        try {
                            int x = Integer.parseInt(matcher.group(2));
                            int z = Integer.parseInt(matcher.group(3));
                            GpsManager.getInstance().setGps(new Vector2i(x, z), name);
                        } catch (NumberFormatException e) {
                        }
                    }
                }
            }
        }
    }

    private void handleAutoPvPLogic() {
        if (this.targetRival != null) {
            class_1707 class_1707Var = mc.field_1724.field_7512;
            if (class_1707Var instanceof class_1707) {
                class_1707 handler = class_1707Var;
                for (int i = 0; i < handler.field_7761.size(); i++) {
                    class_1799 stack = handler.method_7611(i).method_7677();
                    if (!stack.method_7960() && stack.method_31574(class_1802.field_8288)) {
                        mc.field_1761.method_2906(handler.field_7763, i, 0, class_1713.field_7790, mc.field_1724);
                        this.targetRival = null;
                        return;
                    }
                }
            }
        }
    }

    private String getPlayerRankFromTab(String name) {
        if (mc.method_1562() == null) {
            return "";
        }
        for (class_640 entry : mc.method_1562().method_2880()) {
            if (entry.method_2966().getName().equalsIgnoreCase(name)) {
                StringBuilder fullText = new StringBuilder();
                class_268 team = entry.method_2955();
                if (team != null) {
                    fullText.append(team.method_1144().getString());
                }
                if (entry.method_2971() != null) {
                    fullText.append(entry.method_2971().getString());
                }
                return fullText.toString().replaceAll("(?i)§[0-9a-fklmnor]", "");
            }
        }
        return "";
    }

    private boolean shouldClickPvp(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        String upper = text.toUpperCase();
        if (this.lonyPrefixes.isEnabled("Player") && (upper.contains("PLAYER") || text.contains("ᴘʟᴀʏᴇʀ"))) {
            return true;
        }
        if (this.lonyPrefixes.isEnabled("Legenda") && (upper.contains("LEGENDA") || text.contains("ʟᴇɢᴇɴᴅᴀ"))) {
            return true;
        }
        if (this.lonyPrefixes.isEnabled("Pravitel") && (upper.contains("PRAVITEL") || text.contains("ᴘʀᴀᴠɪᴛᴇʟ"))) {
            return true;
        }
        if (this.lonyPrefixes.isEnabled("Povelitel") && (upper.contains("POVELITEL") || text.contains("ᴘᴏᴠᴇʟɪᴛᴇʟ"))) {
            return true;
        }
        if (this.lonyPrefixes.isEnabled("D.Admin") && (upper.contains("DADMIN") || text.contains("ᴅᴀᴅᴍɪɴ"))) {
            return true;
        }
        if (this.lonyPrefixes.isEnabled("Staff") && (upper.contains("STAFF") || upper.contains("ADMIN") || text.contains("sᴛᴀꜰꜰ"))) {
            return true;
        }
        if (this.lonyPrefixes.isEnabled("Eternity") && (upper.contains("ETERNITY") || text.contains("ᴇᴛᴇʀɴɪᴛʏ"))) {
            return true;
        }
        if (this.lonyPrefixes.isEnabled("Luxe")) {
            return upper.contains("LUXE") || text.contains("ʟᴜxᴇ");
        }
        return false;
    }

    private void handleTickEvent() {
        if (mc.field_1724 == null) {
            return;
        }
        if (this.antipolet.getValue().booleanValue() && this.need) {
            if (!mc.field_1724.method_24828() && mc.field_1724.method_6118(class_1304.field_6174).method_7909() == class_1802.field_8833) {
                mc.field_1724.field_3944.method_52787(new class_2848(mc.field_1724, class_2848.class_2849.field_12982));
                mc.field_1724.method_23669();
                if (this.fireworkUse) {
                    int fwSlot = InventoryUtil.findItem(class_1802.field_8639, true);
                    if (fwSlot == -1) {
                        fwSlot = InventoryUtil.findItem(class_1802.field_8639, false);
                    }
                    if (fwSlot != -1) {
                        int oldSlot = mc.field_1724.method_31548().field_7545;
                        if (fwSlot > 8) {
                            int hbSlot = InventoryUtil.findBestSlotInHotBar();
                            InventoryUtil.swapSlots(fwSlot, hbSlot);
                            InventoryUtil.swapToSlot(hbSlot);
                            InventoryUtil.useItem(class_1268.field_5808);
                            InventoryUtil.swapToSlot(oldSlot);
                            InventoryUtil.swapSlots(fwSlot, hbSlot);
                        } else {
                            InventoryUtil.swapToSlot(fwSlot);
                            InventoryUtil.useItem(class_1268.field_5808);
                            InventoryUtil.swapToSlot(oldSlot);
                        }
                    }
                    this.fireworkUse = false;
                }
            } else {
                this.need = false;
            }
        }
        if (this.autoPvpLony.getValue().booleanValue() && this.currentMode == Mode.LONYGRIEF) {
            handleAutoPvPLogic();
        }
        if (this.isHotkeysEnabled.get().booleanValue() && mc.field_1755 == null) {
            this.keyBindings.forEach((usage, pair) -> {
                if (pair.right() == this.currentMode) {
                    usage.handleUse(((BindSetting) pair.left()).getValue().intValue(), this.legit.getValue().booleanValue());
                }
            });
        }
    }

    private void handleSoundPackets(class_2767 soundPacket) {
        String soundPath = soundPacket.method_11894().method_55840();
        if (soundPath.equals("minecraft:block.piston.contract")) {
            class_243 pos = class_243.method_24953(new class_2338((int) soundPacket.method_11890(), (int) soundPacket.method_11889(), (int) soundPacket.method_11893()));
            this.consumables.add(new Pair<>(Long.valueOf(System.currentTimeMillis() + 15000), pos));
            this.consumableNames.put(pos, "Trap");
        } else if (soundPath.equals("minecraft:block.anvil.place")) {
            handleAnvilLogic(soundPacket);
        }
    }

    private void handleAnvilLogic(class_2767 soundPacket) {
        class_2338 soundPos = new class_2338((int) soundPacket.method_11890(), (int) soundPacket.method_11889(), (int) soundPacket.method_11893());
        this.scheduler.schedule(() -> {
            getCube(soundPos, 4, 4).stream().filter(pos -> {
                return getDistance(soundPos, pos) > 2.0d && mc.field_1687.method_8320(pos).method_26204() == class_2246.field_10445;
            }).min(Comparator.comparing(pos2 -> {
                return Double.valueOf(getDistance(soundPos, pos2));
            })).ifPresent(pos3 -> {
                if (getCube(pos3, 1, 1).stream().anyMatch(p -> {
                    return mc.field_1687.method_8320(p).method_26204() == class_2246.field_10535;
                })) {
                    return;
                }
                long solidCount = getCube(pos3, 1, 1).stream().filter(p2 -> {
                    class_2680 s = mc.field_1687.method_8320(p2);
                    return !s.method_26215() && s.method_26212(mc.field_1687, p2);
                }).count();
                if (solidCount == 18 || solidCount == 15 || solidCount == 5) {
                    int time = (solidCount == 18 || solidCount == 15) ? 20000 : 15000;
                    class_243 addPos = class_243.method_24953(pos3).method_1031(0.0d, solidCount == 5 ? -1.5d : 0.0d, 0.0d);
                    this.consumables.add(new Pair<>(Long.valueOf((System.currentTimeMillis() + ((long) time)) - 250), addPos));
                    this.consumableNames.put(addPos, (solidCount == 18 || solidCount == 15) ? "Plast" : "Trap");
                }
            });
        }, 250L, TimeUnit.MILLISECONDS);
    }

    private void handleRenderEvent(Render2DEvent.Render2DEventData event) {
        if (this.functions.isEnabled("Timers")) {
            class_4587 matrixStack = event.matrixStack();
            this.consumables.removeIf(cons -> {
                return ((double) (((Long) cons.left()).longValue() - System.currentTimeMillis())) <= 0.0d;
            });
            for (Pair<Long, class_243> cons2 : this.consumables) {
                Vector2f screenPos = ProjectionUtil.project(cons2.right());
                if (screenPos.x != Float.MAX_VALUE && screenPos.y != Float.MAX_VALUE) {
                    double time = MathUtil.round((cons2.left().longValue() - System.currentTimeMillis()) / 1000.0d, 1.0d);
                    String text = this.consumableNames.getOrDefault(cons2.right(), "Timer") + ": " + time + "s";
                    float textWidth = Fonts.PS_BOLD.getWidth(text, 7.0f);
                    RenderUtil.BLUR_RECT.draw(matrixStack, screenPos.x - (textWidth / 2.0f), screenPos.y, textWidth + 6.0f, 10.0f, 2.0f, UIColors.blur());
                    Fonts.PS_BOLD.drawText(matrixStack, text, (screenPos.x - (textWidth / 2.0f)) + 3.0f, screenPos.y + 3.0f, 7.0f, UIColors.textColor());
                }
            }
        }
    }

    private double getDistance(class_2338 pos1, class_2338 pos2) {
        return Math.sqrt(pos1.method_10262(pos2));
    }

    private List<class_2338> getCube(class_2338 center, int xRadius, int yRadius) {
        List<class_2338> sphere = new ArrayList<>();
        for (int x = -xRadius; x <= xRadius; x++) {
            for (int y = -yRadius; y <= yRadius; y++) {
                for (int z = -xRadius; z <= xRadius; z++) {
                    sphere.add(center.method_10069(x, y, z));
                }
            }
        }
        return sphere;
    }
}
