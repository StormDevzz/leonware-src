package sweetie.leonware.client.features.modules.other;

import lombok.Generated;
import net.minecraft.class_1703;
import net.minecraft.class_1713;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1836;
import net.minecraft.class_2561;
import net.minecraft.class_3944;
import org.joml.Vector2i;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.system.client.GpsManager;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/other/WarpUtilsModule.class */
@ModuleRegister(name = "Warp Utils", category = Category.OTHER)
public class WarpUtilsModule extends Module {
    private static final WarpUtilsModule instance = new WarpUtilsModule();
    private static final int SLOT = 47;
    private final SliderSetting targetX = new SliderSetting("Target X").value(Float.valueOf(0.0f)).range(-20000.0f, 20000.0f).step(1.0f);
    private final SliderSetting targetZ = new SliderSetting("Target Z").value(Float.valueOf(0.0f)).range(-20000.0f, 20000.0f).step(1.0f);
    private final SliderSetting maxDistance = new SliderSetting("Max Distance").value(Float.valueOf(5000.0f)).range(100.0f, 50000.0f).step(100.0f);
    private final BooleanSetting setGps = new BooleanSetting("Set GPS").value((Boolean) true);
    private volatile boolean awaitingWarpChest = false;
    private volatile boolean stopThread = false;
    private volatile Thread searchThread = null;

    @Generated
    public static WarpUtilsModule getInstance() {
        return instance;
    }

    public WarpUtilsModule() {
        addSettings(this.targetX, this.targetZ, this.maxDistance, this.setGps);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onEnable() {
        if (mc.field_1724 == null || mc.method_1562() == null) {
            return;
        }
        this.stopThread = true;
        if (this.searchThread != null && this.searchThread.isAlive()) {
            this.searchThread.interrupt();
        }
        this.stopThread = false;
        mc.method_1562().method_45731("warp");
        this.awaitingWarpChest = true;
        if (this.setGps.getValue().booleanValue()) {
            int x = this.targetX.getValue().intValue();
            int z = this.targetZ.getValue().intValue();
            GpsManager.getInstance().setGps(new Vector2i(x, z), "Warp Target");
            sendMessage("Поставил метку на координаты X: " + x + " Z: " + z);
        }
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        this.stopThread = true;
        this.awaitingWarpChest = false;
        if (this.searchThread != null && this.searchThread.isAlive()) {
            this.searchThread.interrupt();
        }
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener packetListener = PacketEvent.getInstance().subscribe(new Listener(data -> {
            if (this.awaitingWarpChest && data.isReceive() && (data.packet() instanceof class_3944)) {
                this.awaitingWarpChest = false;
                if (this.searchThread != null && this.searchThread.isAlive()) {
                    this.stopThread = true;
                    this.searchThread.interrupt();
                }
                this.stopThread = false;
                this.searchThread = new Thread(() -> {
                    try {
                        Thread.sleep(48L);
                        if (mc.field_1724 == null || mc.field_1724.field_7512 == null || this.stopThread) {
                            return;
                        }
                        searchAndClickNearestWarp(SLOT);
                    } catch (InterruptedException e) {
                        sendMessage("Прервано");
                    } catch (Exception var3) {
                        sendMessage("Ошибка: " + var3.getMessage());
                    }
                }, "WarpUtils-Search");
                this.searchThread.start();
            }
        }));
        addEvents(packetListener);
    }

    private void searchAndClickNearestWarp(int pageSlot) throws InterruptedException {
        class_1703 handler;
        class_1703 handler2;
        if (mc.field_1724 != null && mc.field_1761 != null && (handler = mc.field_1724.field_7512) != null && pageSlot >= 0 && pageSlot < handler.field_7761.size()) {
            class_1836.class_1837 class_1837Var = class_1836.field_41070;
            class_1792.class_9635 context = class_1792.class_9635.method_59528(mc.field_1687);
            double inputX = this.targetX.getValue().doubleValue();
            double inputZ = this.targetZ.getValue().doubleValue();
            double bestDistance = Double.MAX_VALUE;
            int bestSlot = -1;
            int bestPage = 1;
            int totalPages = getTotalPages(handler, context, class_1837Var, pageSlot);
            if (totalPages <= 0) {
                sendMessage("Не удалось определить страницы");
                return;
            }
            int pagesScanned = 0;
            while (true) {
                if (pagesScanned >= totalPages || this.stopThread || (handler2 = mc.field_1724.field_7512) == null) {
                    break;
                }
                int actualPage = getCurrentPage(handler2, context, class_1837Var, pageSlot);
                for (int i = 9; i <= 44 && i < handler2.field_7761.size(); i++) {
                    class_1799 stack = handler2.method_7611(i).method_7677();
                    if (!stack.method_7960()) {
                        for (class_2561 line : stack.method_7950(context, mc.field_1724, class_1837Var)) {
                            String s = line.getString();
                            if (s.contains("Координаты:")) {
                                String coords = s.replace("Координаты:", "").trim();
                                String[] xyz = coords.split(",");
                                if (xyz.length >= 3) {
                                    try {
                                        double x = Double.parseDouble(xyz[0].trim());
                                        double z = Double.parseDouble(xyz[2].trim());
                                        double dist = Math.sqrt(Math.pow(x - inputX, 2.0d) + Math.pow(z - inputZ, 2.0d));
                                        if (dist <= this.maxDistance.getValue().doubleValue() && dist < bestDistance) {
                                            bestDistance = dist;
                                            bestSlot = i;
                                            bestPage = actualPage;
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                            }
                        }
                    }
                }
                pagesScanned++;
                if (pagesScanned < totalPages && !this.stopThread) {
                    clickSlot(handler2.field_7763, pageSlot);
                    Thread.sleep(248L);
                    class_1703 handler3 = mc.field_1724.field_7512;
                    if (handler3 == null) {
                        break;
                    }
                    int newPage = getCurrentPage(handler3, context, class_1837Var, pageSlot);
                    if (newPage == actualPage) {
                        Thread.sleep(148L);
                        clickSlot(handler3.field_7763, pageSlot);
                        Thread.sleep(248L);
                        class_1703 handler4 = mc.field_1724.field_7512;
                        if (handler4 != null) {
                            newPage = getCurrentPage(handler4, context, class_1837Var, pageSlot);
                        }
                        if (newPage == actualPage) {
                            sendMessage("Не удалось переключить страницу");
                            break;
                        }
                    } else {
                        continue;
                    }
                }
            }
            if (bestSlot != -1) {
                sendMessage("Варп найден! Блоков до координат: " + ((int) bestDistance));
                class_1703 handler5 = mc.field_1724.field_7512;
                if (handler5 == null) {
                    return;
                }
                int actualPage2 = getCurrentPage(handler5, context, class_1837Var, pageSlot);
                for (int i2 = 0; actualPage2 != bestPage && i2 < totalPages + 3 && !this.stopThread; i2++) {
                    clickSlot(handler5.field_7763, pageSlot);
                    Thread.sleep(248L);
                    handler5 = mc.field_1724.field_7512;
                    if (handler5 == null) {
                        return;
                    }
                    int newPage2 = getCurrentPage(handler5, context, class_1837Var, pageSlot);
                    if (newPage2 == actualPage2) {
                        Thread.sleep(148L);
                        clickSlot(handler5.field_7763, pageSlot);
                        Thread.sleep(248L);
                        handler5 = mc.field_1724.field_7512;
                        if (handler5 != null) {
                            newPage2 = getCurrentPage(handler5, context, class_1837Var, pageSlot);
                        }
                    }
                    actualPage2 = newPage2;
                }
                Thread.sleep(500L);
                class_1703 handler6 = mc.field_1724.field_7512;
                if (handler6 != null && handler6.field_7763 > 0) {
                    clickSlot(handler6.field_7763, bestSlot);
                    sendMessage("Телепортирую...");
                    return;
                }
                return;
            }
            sendMessage("Варп не найден, попробуйте увеличить MaxDistance");
        }
    }

    private void clickSlot(int syncId, int slotId) {
        mc.execute(() -> {
            if (mc.field_1761 != null && mc.field_1724 != null) {
                mc.field_1761.method_2906(syncId, slotId, 0, class_1713.field_7790, mc.field_1724);
            }
        });
    }

    private int getCurrentPage(class_1703 handler, class_1792.class_9635 context, class_1836 type, int slotId) {
        if (slotId >= 0 && slotId < handler.field_7761.size()) {
            class_1799 item = handler.method_7611(slotId).method_7677();
            if (item.method_7960()) {
                return 0;
            }
            for (class_2561 line : item.method_7950(context, mc.field_1724, type)) {
                String s = line.getString();
                if (s.contains("Страница") && s.contains("из")) {
                    try {
                        String[] parts = s.split("из");
                        String pageStr = parts[0].replaceAll("[^0-9]", "").trim();
                        if (!pageStr.isEmpty()) {
                            return Integer.parseInt(pageStr);
                        }
                        continue;
                    } catch (Exception e) {
                    }
                }
            }
            return 0;
        }
        return 0;
    }

    private int getTotalPages(class_1703 handler, class_1792.class_9635 context, class_1836 type, int slotId) {
        if (slotId >= 0 && slotId < handler.field_7761.size()) {
            class_1799 item = handler.method_7611(slotId).method_7677();
            if (item.method_7960()) {
                return 0;
            }
            for (class_2561 line : item.method_7950(context, mc.field_1724, type)) {
                String s = line.getString();
                if (s.contains("Страница") && s.contains("из")) {
                    try {
                        String[] parts = s.split("из");
                        if (parts.length > 1) {
                            String totalStr = parts[1].trim().replaceAll("[^0-9]", "");
                            if (!totalStr.isEmpty()) {
                                return Integer.parseInt(totalStr);
                            }
                            continue;
                        } else {
                            continue;
                        }
                    } catch (Exception e) {
                    }
                }
            }
            return 0;
        }
        return 0;
    }

    private void sendMessage(String msg) {
        if (mc.field_1724 != null) {
            mc.field_1724.method_7353(class_2561.method_30163("§6[WarpUtils] §f" + msg), false);
        }
    }
}
