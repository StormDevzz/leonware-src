/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1268
 *  net.minecraft.class_1297
 *  net.minecraft.class_1304
 *  net.minecraft.class_1657
 *  net.minecraft.class_1703
 *  net.minecraft.class_1707
 *  net.minecraft.class_1713
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_1922
 *  net.minecraft.class_2246
 *  net.minecraft.class_2338
 *  net.minecraft.class_2382
 *  net.minecraft.class_243
 *  net.minecraft.class_2596
 *  net.minecraft.class_268
 *  net.minecraft.class_2680
 *  net.minecraft.class_2767
 *  net.minecraft.class_2848
 *  net.minecraft.class_2848$class_2849
 *  net.minecraft.class_4587
 *  net.minecraft.class_640
 *  net.minecraft.class_7439
 *  org.joml.Vector2f
 *  org.joml.Vector2i
 */
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
import net.minecraft.class_1297;
import net.minecraft.class_1304;
import net.minecraft.class_1657;
import net.minecraft.class_1703;
import net.minecraft.class_1707;
import net.minecraft.class_1713;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1922;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2382;
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

@ModuleRegister(name="Assistant", category=Category.PLAYER)
public class AssistantModule
extends Module {
    private static final AssistantModule instance = new AssistantModule();
    private final Supplier<Boolean> isHotkeysEnabled = () -> this.getFunctions().isEnabled("Hotkeys");
    private final Supplier<Boolean> isHWKeys = () -> this.isHotkeysEnabled.get() != false && this.getMode().is("Holy World");
    private final Supplier<Boolean> isFTKeys = () -> this.isHotkeysEnabled.get() != false && this.getMode().is("Fun Time");
    private final Supplier<Boolean> isLGKeys = () -> this.isHotkeysEnabled.get() != false && this.getMode().is("Lony Grief");
    private final Supplier<Boolean> isRWKeys = () -> this.isHotkeysEnabled.get() != false && this.getMode().is("Reallyworld");
    private Mode currentMode = Mode.FUNTIME;
    private String targetRival = null;
    private final MultiBooleanSetting functions = new MultiBooleanSetting("Functions").value(new BooleanSetting("Hotkeys").value(true), new BooleanSetting("Timers").value(false));
    public final BooleanSetting autoPvpLony = new BooleanSetting("Auto-PvP").value(true).setVisible((Supplier)this.isLGKeys);
    private final MultiBooleanSetting lonyPrefixes = new MultiBooleanSetting("Prefixes").value(new BooleanSetting("Player").value(true), new BooleanSetting("Legenda").value(true), new BooleanSetting("Pravitel").value(true), new BooleanSetting("Povelitel").value(true), new BooleanSetting("D.Admin").value(true), new BooleanSetting("Staff").value(false), new BooleanSetting("Eternity").value(true), new BooleanSetting("Luxe").value(true)).setVisible(() -> this.isLGKeys.get() != false && (Boolean)this.autoPvpLony.getValue() != false);
    private final ModeSetting mode = ((ModeSetting)new ModeSetting("Mode").value("Fun Time").values("Fun Time", "Holy World", "Lony Grief", "Reallyworld").setVisible((Supplier)this.isHotkeysEnabled)).onAction(() -> {
        this.currentMode = switch ((String)this.getMode().getValue()) {
            case "Fun Time" -> Mode.FUNTIME;
            case "Lony Grief" -> Mode.LONYGRIEF;
            case "Reallyworld" -> Mode.REALLYWORLD;
            default -> Mode.HOLYWORLD;
        };
    });
    private final BooleanSetting legit = new BooleanSetting("Legit").value(true).setVisible((Supplier)this.isHotkeysEnabled);
    public final BooleanSetting autoPlayerLony = new BooleanSetting("Auto-Player").value(true).setVisible((Supplier)this.isLGKeys);
    public final BooleanSetting antipolet = new BooleanSetting("\u0410\u043d\u0442\u0438-\u043f\u043e\u043b\u0435\u0442 \u043e\u0431\u0445\u043e\u0434").value(false).setVisible((Supplier)this.isRWKeys);
    boolean need;
    public boolean fireworkUse;
    private final Map<InventoryUtil.ItemUsage, Pair<BindSetting, Mode>> keyBindings = new LinkedHashMap<InventoryUtil.ItemUsage, Pair<BindSetting, Mode>>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final List<Pair<Long, class_243>> consumables = new ArrayList<Pair<Long, class_243>>();
    private final Map<class_243, String> consumableNames = new HashMap<class_243, String>();

    public AssistantModule() {
        this.keyBindings.put(new InventoryUtil.ItemUsage(class_1802.field_8449, this), new Pair<BindSetting, Mode>(new BindSetting("Disorientation").value(-999), Mode.FUNTIME));
        this.keyBindings.put(new InventoryUtil.ItemUsage(class_1802.field_22021, this), new Pair<BindSetting, Mode>(new BindSetting("Trap").value(-999), Mode.FUNTIME));
        this.keyBindings.put(new InventoryUtil.ItemUsage(class_1802.field_8479, this), new Pair<BindSetting, Mode>(new BindSetting("Clear dust").value(-999), Mode.FUNTIME));
        this.keyBindings.put(new InventoryUtil.ItemUsage(class_1802.field_8814, this), new Pair<BindSetting, Mode>(new BindSetting("Fire whirl").value(-999), Mode.FUNTIME));
        this.keyBindings.put(new InventoryUtil.ItemUsage(class_1802.field_8551, this), new Pair<BindSetting, Mode>(new BindSetting("Plast").value(-999), Mode.FUNTIME));
        this.keyBindings.put(new InventoryUtil.ItemUsage(class_1802.field_8614, this), new Pair<BindSetting, Mode>(new BindSetting("Divine aura").value(-999), Mode.FUNTIME));
        this.keyBindings.put(new InventoryUtil.ItemUsage(class_1802.field_8662, this), new Pair<BindSetting, Mode>(new BindSetting("Explosive trap").value(-999), Mode.HOLYWORLD));
        this.keyBindings.put(new InventoryUtil.ItemUsage(class_1802.field_8882, this), new Pair<BindSetting, Mode>(new BindSetting("Default trap").value(-999), Mode.HOLYWORLD));
        this.keyBindings.put(new InventoryUtil.ItemUsage(class_1802.field_8137, this), new Pair<BindSetting, Mode>(new BindSetting("Stun").value(-999), Mode.HOLYWORLD));
        this.keyBindings.put(new InventoryUtil.ItemUsage(class_1802.field_8814, this), new Pair<BindSetting, Mode>(new BindSetting("Explosive thing").value(-999), Mode.HOLYWORLD));
        this.keyBindings.put(new InventoryUtil.ItemUsage(class_1802.field_8543, this), new Pair<BindSetting, Mode>(new BindSetting("Snowball").value(-999), Mode.HOLYWORLD));
        this.keyBindings.put(new InventoryUtil.ItemUsage(class_1802.field_8696, this), new Pair<BindSetting, Mode>(new BindSetting("\u041b\u0438\u0432\u0430\u043b\u043e\u0447\u043a\u0430").value(-999), Mode.LONYGRIEF));
        this.keyBindings.put(new InventoryUtil.ItemUsage(class_1802.field_8639, this), new Pair<BindSetting, Mode>(new BindSetting("\u0424\u0435\u0439\u0435\u0440\u0432\u0435\u0440\u043a").value(-999), Mode.REALLYWORLD));
        this.addSettings(this.functions, this.mode, this.lonyPrefixes, this.legit, this.autoPvpLony, this.autoPlayerLony, this.antipolet);
        this.keyBindings.forEach((key, value) -> {
            if (value.right() == Mode.HOLYWORLD) {
                ((BindSetting)value.left()).setVisible((Supplier)this.isHWKeys);
            }
            if (value.right() == Mode.FUNTIME) {
                ((BindSetting)value.left()).setVisible((Supplier)this.isFTKeys);
            }
            if (value.right() == Mode.LONYGRIEF) {
                ((BindSetting)value.left()).setVisible((Supplier)this.isLGKeys);
            }
            if (value.right() == Mode.REALLYWORLD) {
                ((BindSetting)value.left()).setVisible((Supplier)this.isRWKeys);
            }
            this.addSettings((Setting)value.left());
        });
    }

    @Override
    public void onEvent() {
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener<TickEvent>(event -> this.handleTickEvent()));
        EventListener renderEvent = Render2DEvent.getInstance().subscribe(new Listener<Render2DEvent.Render2DEventData>(this::handleRenderEvent));
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(this::handlePacketEvent));
        this.addEvents(tickEvent, renderEvent, packetEvent);
    }

    private void handlePacketEvent(PacketEvent.PacketEventData event) {
        class_7439 p;
        class_2596<?> msg;
        if (event.isSend() || AssistantModule.mc.field_1687 == null) {
            return;
        }
        class_2596<?> class_25962 = event.packet();
        if (class_25962 instanceof class_2767) {
            class_2767 soundPacket = (class_2767)class_25962;
            this.handleSoundPackets(soundPacket);
        }
        if ((class_25962 = event.packet()) instanceof class_7439 && (msg = (p = (class_7439)class_25962).comp_763().getString()).contains((CharSequence)"\u0410\u043d\u0442\u0438 \u041f\u043e\u043b\u0435\u0442 \u00bb \u0412\u044b \u043d\u0435 \u043c\u043e\u0436\u0435\u0442\u0435 \u0432\u0437\u043b\u0435\u0442\u0435\u0442\u044c!")) {
            this.need = true;
        }
        if (((Boolean)this.autoPvpLony.getValue()).booleanValue() && this.currentMode == Mode.LONYGRIEF && (msg = event.packet()) instanceof class_7439) {
            Pattern pattern;
            Matcher matcher;
            class_7439 chatPacket = (class_7439)msg;
            String rawMsg = chatPacket.comp_763().getString();
            String cleanMsg = rawMsg.replaceAll("(?i)\u00a7[0-9a-fklmnor]", " ").replace("\u203a", " ").replace(">", " ").trim();
            Pattern pvpPattern = Pattern.compile("\u0418\u0433\u0440\u043e\u043a\\s+([a-zA-Z0-9_]{3,16})\\s+\u0438\u0449\u0435\u0442", 66);
            Matcher pvpMatcher = pvpPattern.matcher(cleanMsg);
            if (pvpMatcher.find()) {
                String nickname = pvpMatcher.group(1);
                if (nickname.equalsIgnoreCase(AssistantModule.mc.field_1724.method_5477().getString())) {
                    return;
                }
                String rawPrefix = this.getPlayerRankFromTab(nickname);
                if (this.shouldClickPvp(rawPrefix)) {
                    this.targetRival = nickname;
                }
            }
            if (((Boolean)this.autoPlayerLony.getValue()).booleanValue() && (matcher = (pattern = Pattern.compile("(?:\u0438\u0433\u0440\u043e\u043a\u043e\u043c|\u0438\u0433\u0440\u043e\u043a)\\s+([^\\s!]+).+?(?:\u043a\u043e\u043e\u0440\u0434\u0438\u043d\u0430\u0442\u044b|\u041a\u043e\u0440\u0434\u0438\u043d\u0430\u0442\u044b|X):?\\s*(-?\\d+)[^\\d-]+-?\\d+[^\\d-]+(-?\\d+)", 66)).matcher(cleanMsg)).find()) {
                String name = matcher.group(1);
                try {
                    int x = Integer.parseInt(matcher.group(2));
                    int z = Integer.parseInt(matcher.group(3));
                    GpsManager.getInstance().setGps(new Vector2i(x, z), name);
                }
                catch (NumberFormatException numberFormatException) {
                    // empty catch block
                }
            }
        }
    }

    private void handleAutoPvPLogic() {
        class_1703 class_17032;
        if (this.targetRival == null || !((class_17032 = AssistantModule.mc.field_1724.field_7512) instanceof class_1707)) {
            return;
        }
        class_1707 handler = (class_1707)class_17032;
        for (int i = 0; i < handler.field_7761.size(); ++i) {
            class_1799 stack = handler.method_7611(i).method_7677();
            if (stack.method_7960() || !stack.method_31574(class_1802.field_8288)) continue;
            AssistantModule.mc.field_1761.method_2906(handler.field_7763, i, 0, class_1713.field_7790, (class_1657)AssistantModule.mc.field_1724);
            this.targetRival = null;
            return;
        }
    }

    private String getPlayerRankFromTab(String name) {
        if (mc.method_1562() == null) {
            return "";
        }
        for (class_640 entry : mc.method_1562().method_2880()) {
            if (!entry.method_2966().getName().equalsIgnoreCase(name)) continue;
            StringBuilder fullText = new StringBuilder();
            class_268 team = entry.method_2955();
            if (team != null) {
                fullText.append(team.method_1144().getString());
            }
            if (entry.method_2971() != null) {
                fullText.append(entry.method_2971().getString());
            }
            return fullText.toString().replaceAll("(?i)\u00a7[0-9a-fklmnor]", "");
        }
        return "";
    }

    private boolean shouldClickPvp(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        String upper = text.toUpperCase();
        if (this.lonyPrefixes.isEnabled("Player") && (upper.contains("PLAYER") || text.contains("\u1d18\u029f\u1d00\u028f\u1d07\u0280"))) {
            return true;
        }
        if (this.lonyPrefixes.isEnabled("Legenda") && (upper.contains("LEGENDA") || text.contains("\u029f\u1d07\u0262\u1d07\u0274\u1d05\u1d00"))) {
            return true;
        }
        if (this.lonyPrefixes.isEnabled("Pravitel") && (upper.contains("PRAVITEL") || text.contains("\u1d18\u0280\u1d00\u1d20\u026a\u1d1b\u1d07\u029f"))) {
            return true;
        }
        if (this.lonyPrefixes.isEnabled("Povelitel") && (upper.contains("POVELITEL") || text.contains("\u1d18\u1d0f\u1d20\u1d07\u029f\u026a\u1d1b\u1d07\u029f"))) {
            return true;
        }
        if (this.lonyPrefixes.isEnabled("D.Admin") && (upper.contains("DADMIN") || text.contains("\u1d05\u1d00\u1d05\u1d0d\u026a\u0274"))) {
            return true;
        }
        if (this.lonyPrefixes.isEnabled("Staff") && (upper.contains("STAFF") || upper.contains("ADMIN") || text.contains("s\u1d1b\u1d00\ua730\ua730"))) {
            return true;
        }
        if (this.lonyPrefixes.isEnabled("Eternity") && (upper.contains("ETERNITY") || text.contains("\u1d07\u1d1b\u1d07\u0280\u0274\u026a\u1d1b\u028f"))) {
            return true;
        }
        return this.lonyPrefixes.isEnabled("Luxe") && (upper.contains("LUXE") || text.contains("\u029f\u1d1cx\u1d07"));
    }

    private void handleTickEvent() {
        if (AssistantModule.mc.field_1724 == null) {
            return;
        }
        if (((Boolean)this.antipolet.getValue()).booleanValue() && this.need) {
            if (!AssistantModule.mc.field_1724.method_24828() && AssistantModule.mc.field_1724.method_6118(class_1304.field_6174).method_7909() == class_1802.field_8833) {
                AssistantModule.mc.field_1724.field_3944.method_52787((class_2596)new class_2848((class_1297)AssistantModule.mc.field_1724, class_2848.class_2849.field_12982));
                AssistantModule.mc.field_1724.method_23669();
                if (this.fireworkUse) {
                    int fwSlot = InventoryUtil.findItem(class_1802.field_8639, true);
                    if (fwSlot == -1) {
                        fwSlot = InventoryUtil.findItem(class_1802.field_8639, false);
                    }
                    if (fwSlot != -1) {
                        int oldSlot = AssistantModule.mc.field_1724.method_31548().field_7545;
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
        if (((Boolean)this.autoPvpLony.getValue()).booleanValue() && this.currentMode == Mode.LONYGRIEF) {
            this.handleAutoPvPLogic();
        }
        if (!this.isHotkeysEnabled.get().booleanValue() || AssistantModule.mc.field_1755 != null) {
            return;
        }
        this.keyBindings.forEach((usage, pair) -> {
            if (pair.right() == this.currentMode) {
                usage.handleUse((Integer)((BindSetting)pair.left()).getValue(), (Boolean)this.legit.getValue());
            }
        });
    }

    private void handleSoundPackets(class_2767 soundPacket) {
        String soundPath = soundPacket.method_11894().method_55840();
        if (soundPath.equals("minecraft:block.piston.contract")) {
            class_243 pos = class_243.method_24953((class_2382)new class_2338((int)soundPacket.method_11890(), (int)soundPacket.method_11889(), (int)soundPacket.method_11893()));
            this.consumables.add(new Pair<Long, class_243>(System.currentTimeMillis() + 15000L, pos));
            this.consumableNames.put(pos, "Trap");
        } else if (soundPath.equals("minecraft:block.anvil.place")) {
            this.handleAnvilLogic(soundPacket);
        }
    }

    private void handleAnvilLogic(class_2767 soundPacket) {
        class_2338 soundPos = new class_2338((int)soundPacket.method_11890(), (int)soundPacket.method_11889(), (int)soundPacket.method_11893());
        this.scheduler.schedule(() -> this.getCube(soundPos, 4, 4).stream().filter(pos -> this.getDistance(soundPos, (class_2338)pos) > 2.0 && AssistantModule.mc.field_1687.method_8320(pos).method_26204() == class_2246.field_10445).min(Comparator.comparing(pos -> this.getDistance(soundPos, (class_2338)pos))).ifPresent(pos -> {
            if (this.getCube((class_2338)pos, 1, 1).stream().anyMatch(p -> AssistantModule.mc.field_1687.method_8320(p).method_26204() == class_2246.field_10535)) {
                return;
            }
            long solidCount = this.getCube((class_2338)pos, 1, 1).stream().filter(p -> {
                class_2680 s = AssistantModule.mc.field_1687.method_8320(p);
                return !s.method_26215() && s.method_26212((class_1922)AssistantModule.mc.field_1687, p);
            }).count();
            if (solidCount == 18L || solidCount == 15L || solidCount == 5L) {
                int time = solidCount == 18L || solidCount == 15L ? 20000 : 15000;
                class_243 addPos = class_243.method_24953((class_2382)pos).method_1031(0.0, solidCount == 5L ? -1.5 : 0.0, 0.0);
                this.consumables.add(new Pair<Long, class_243>(System.currentTimeMillis() + (long)time - 250L, addPos));
                this.consumableNames.put(addPos, solidCount == 18L || solidCount == 15L ? "Plast" : "Trap");
            }
        }), 250L, TimeUnit.MILLISECONDS);
    }

    private void handleRenderEvent(Render2DEvent.Render2DEventData event) {
        if (!this.functions.isEnabled("Timers")) {
            return;
        }
        class_4587 matrixStack = event.matrixStack();
        this.consumables.removeIf(cons -> (double)((Long)cons.left() - System.currentTimeMillis()) <= 0.0);
        for (Pair<Long, class_243> cons2 : this.consumables) {
            Vector2f screenPos = ProjectionUtil.project(cons2.right());
            if (screenPos.x == Float.MAX_VALUE || screenPos.y == Float.MAX_VALUE) continue;
            double time = MathUtil.round((double)(cons2.left() - System.currentTimeMillis()) / 1000.0, 1.0);
            String text = this.consumableNames.getOrDefault(cons2.right(), "Timer") + ": " + time + "s";
            float textWidth = Fonts.PS_BOLD.getWidth(text, 7.0f);
            RenderUtil.BLUR_RECT.draw(matrixStack, screenPos.x - textWidth / 2.0f, screenPos.y, textWidth + 6.0f, 10.0f, 2.0f, UIColors.blur());
            Fonts.PS_BOLD.drawText(matrixStack, text, screenPos.x - textWidth / 2.0f + 3.0f, screenPos.y + 3.0f, 7.0f, UIColors.textColor());
        }
    }

    private double getDistance(class_2338 pos1, class_2338 pos2) {
        return Math.sqrt(pos1.method_10262((class_2382)pos2));
    }

    private List<class_2338> getCube(class_2338 center, int xRadius, int yRadius) {
        ArrayList<class_2338> sphere = new ArrayList<class_2338>();
        for (int x = -xRadius; x <= xRadius; ++x) {
            for (int y = -yRadius; y <= yRadius; ++y) {
                for (int z = -xRadius; z <= xRadius; ++z) {
                    sphere.add(center.method_10069(x, y, z));
                }
            }
        }
        return sphere;
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

    public static enum Mode {
        FUNTIME,
        HOLYWORLD,
        LONYGRIEF,
        REALLYWORLD;

    }
}

