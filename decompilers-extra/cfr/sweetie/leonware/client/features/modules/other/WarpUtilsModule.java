/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1657
 *  net.minecraft.class_1703
 *  net.minecraft.class_1713
 *  net.minecraft.class_1792$class_9635
 *  net.minecraft.class_1799
 *  net.minecraft.class_1836
 *  net.minecraft.class_1836$class_1837
 *  net.minecraft.class_1937
 *  net.minecraft.class_2561
 *  net.minecraft.class_3944
 *  org.joml.Vector2i
 */
package sweetie.leonware.client.features.modules.other;

import lombok.Generated;
import net.minecraft.class_1657;
import net.minecraft.class_1703;
import net.minecraft.class_1713;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1836;
import net.minecraft.class_1937;
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

@ModuleRegister(name="Warp Utils", category=Category.OTHER)
public class WarpUtilsModule
extends Module {
    private static final WarpUtilsModule instance = new WarpUtilsModule();
    private final SliderSetting targetX = new SliderSetting("Target X").value(Float.valueOf(0.0f)).range(-20000.0f, 20000.0f).step(1.0f);
    private final SliderSetting targetZ = new SliderSetting("Target Z").value(Float.valueOf(0.0f)).range(-20000.0f, 20000.0f).step(1.0f);
    private final SliderSetting maxDistance = new SliderSetting("Max Distance").value(Float.valueOf(5000.0f)).range(100.0f, 50000.0f).step(100.0f);
    private final BooleanSetting setGps = new BooleanSetting("Set GPS").value(true);
    private static final int SLOT = 47;
    private volatile boolean awaitingWarpChest = false;
    private volatile boolean stopThread = false;
    private volatile Thread searchThread = null;

    public WarpUtilsModule() {
        this.addSettings(this.targetX, this.targetZ, this.maxDistance, this.setGps);
    }

    @Override
    public void onEnable() {
        if (WarpUtilsModule.mc.field_1724 == null || mc.method_1562() == null) {
            return;
        }
        this.stopThread = true;
        if (this.searchThread != null && this.searchThread.isAlive()) {
            this.searchThread.interrupt();
        }
        this.stopThread = false;
        mc.method_1562().method_45731("warp");
        this.awaitingWarpChest = true;
        if (((Boolean)this.setGps.getValue()).booleanValue()) {
            int x = ((Float)this.targetX.getValue()).intValue();
            int z = ((Float)this.targetZ.getValue()).intValue();
            GpsManager.getInstance().setGps(new Vector2i(x, z), "Warp Target");
            this.sendMessage("\u041f\u043e\u0441\u0442\u0430\u0432\u0438\u043b \u043c\u0435\u0442\u043a\u0443 \u043d\u0430 \u043a\u043e\u043e\u0440\u0434\u0438\u043d\u0430\u0442\u044b X: " + x + " Z: " + z);
        }
    }

    @Override
    public void onDisable() {
        this.stopThread = true;
        this.awaitingWarpChest = false;
        if (this.searchThread != null && this.searchThread.isAlive()) {
            this.searchThread.interrupt();
        }
    }

    @Override
    public void onEvent() {
        EventListener packetListener = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(data -> {
            if (this.awaitingWarpChest && data.isReceive() && data.packet() instanceof class_3944) {
                this.awaitingWarpChest = false;
                if (this.searchThread != null && this.searchThread.isAlive()) {
                    this.stopThread = true;
                    this.searchThread.interrupt();
                }
                this.stopThread = false;
                this.searchThread = new Thread(() -> {
                    try {
                        Thread.sleep(48L);
                        if (WarpUtilsModule.mc.field_1724 == null || WarpUtilsModule.mc.field_1724.field_7512 == null || this.stopThread) {
                            return;
                        }
                        this.searchAndClickNearestWarp(47);
                    }
                    catch (InterruptedException var2) {
                        this.sendMessage("\u041f\u0440\u0435\u0440\u0432\u0430\u043d\u043e");
                    }
                    catch (Exception var3) {
                        this.sendMessage("\u041e\u0448\u0438\u0431\u043a\u0430: " + var3.getMessage());
                    }
                }, "WarpUtils-Search");
                this.searchThread.start();
            }
        }));
        this.addEvents(packetListener);
    }

    private void searchAndClickNearestWarp(int pageSlot) throws InterruptedException {
        class_1703 handler;
        if (WarpUtilsModule.mc.field_1724 != null && WarpUtilsModule.mc.field_1761 != null && (handler = WarpUtilsModule.mc.field_1724.field_7512) != null && pageSlot >= 0 && pageSlot < handler.field_7761.size()) {
            class_1836.class_1837 type = class_1836.field_41070;
            class_1792.class_9635 context = class_1792.class_9635.method_59528((class_1937)WarpUtilsModule.mc.field_1687);
            double inputX = ((Float)this.targetX.getValue()).doubleValue();
            double inputZ = ((Float)this.targetZ.getValue()).doubleValue();
            double bestDistance = Double.MAX_VALUE;
            int bestSlot = -1;
            int bestPage = 1;
            int totalPages = this.getTotalPages(handler, context, (class_1836)type, pageSlot);
            if (totalPages <= 0) {
                this.sendMessage("\u041d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u043e\u043f\u0440\u0435\u0434\u0435\u043b\u0438\u0442\u044c \u0441\u0442\u0440\u0430\u043d\u0438\u0446\u044b");
            } else {
                int i;
                int actualPage;
                int pagesScanned = 0;
                while (pagesScanned < totalPages && !this.stopThread && (handler = WarpUtilsModule.mc.field_1724.field_7512) != null) {
                    actualPage = this.getCurrentPage(handler, context, (class_1836)type, pageSlot);
                    for (i = 9; i <= 44 && i < handler.field_7761.size(); ++i) {
                        class_1799 stack = handler.method_7611(i).method_7677();
                        if (stack.method_7960()) continue;
                        for (class_2561 line : stack.method_7950(context, (class_1657)WarpUtilsModule.mc.field_1724, (class_1836)type)) {
                            String coords;
                            String[] xyz;
                            String s = line.getString();
                            if (!s.contains("\u041a\u043e\u043e\u0440\u0434\u0438\u043d\u0430\u0442\u044b:") || (xyz = (coords = s.replace("\u041a\u043e\u043e\u0440\u0434\u0438\u043d\u0430\u0442\u044b:", "").trim()).split(",")).length < 3) continue;
                            try {
                                double x = Double.parseDouble(xyz[0].trim());
                                double z = Double.parseDouble(xyz[2].trim());
                                double dist = Math.sqrt(Math.pow(x - inputX, 2.0) + Math.pow(z - inputZ, 2.0));
                                if (!(dist <= ((Float)this.maxDistance.getValue()).doubleValue()) || !(dist < bestDistance)) continue;
                                bestDistance = dist;
                                bestSlot = i;
                                bestPage = actualPage;
                            }
                            catch (Exception exception) {}
                        }
                    }
                    if (++pagesScanned >= totalPages || this.stopThread) continue;
                    this.clickSlot(handler.field_7763, pageSlot);
                    Thread.sleep(248L);
                    handler = WarpUtilsModule.mc.field_1724.field_7512;
                    if (handler == null) break;
                    int newPage = this.getCurrentPage(handler, context, (class_1836)type, pageSlot);
                    if (newPage != actualPage) continue;
                    Thread.sleep(148L);
                    this.clickSlot(handler.field_7763, pageSlot);
                    Thread.sleep(248L);
                    handler = WarpUtilsModule.mc.field_1724.field_7512;
                    if (handler != null) {
                        newPage = this.getCurrentPage(handler, context, (class_1836)type, pageSlot);
                    }
                    if (newPage != actualPage) continue;
                    this.sendMessage("\u041d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u043f\u0435\u0440\u0435\u043a\u043b\u044e\u0447\u0438\u0442\u044c \u0441\u0442\u0440\u0430\u043d\u0438\u0446\u0443");
                    break;
                }
                if (bestSlot != -1) {
                    this.sendMessage("\u0412\u0430\u0440\u043f \u043d\u0430\u0439\u0434\u0435\u043d! \u0411\u043b\u043e\u043a\u043e\u0432 \u0434\u043e \u043a\u043e\u043e\u0440\u0434\u0438\u043d\u0430\u0442: " + (int)bestDistance);
                    handler = WarpUtilsModule.mc.field_1724.field_7512;
                    if (handler == null) {
                        return;
                    }
                    actualPage = this.getCurrentPage(handler, context, (class_1836)type, pageSlot);
                    for (i = 0; actualPage != bestPage && i < totalPages + 3 && !this.stopThread; ++i) {
                        this.clickSlot(handler.field_7763, pageSlot);
                        Thread.sleep(248L);
                        handler = WarpUtilsModule.mc.field_1724.field_7512;
                        if (handler == null) {
                            return;
                        }
                        int newPage = this.getCurrentPage(handler, context, (class_1836)type, pageSlot);
                        if (newPage == actualPage) {
                            Thread.sleep(148L);
                            this.clickSlot(handler.field_7763, pageSlot);
                            Thread.sleep(248L);
                            handler = WarpUtilsModule.mc.field_1724.field_7512;
                            if (handler != null) {
                                newPage = this.getCurrentPage(handler, context, (class_1836)type, pageSlot);
                            }
                        }
                        actualPage = newPage;
                    }
                    Thread.sleep(500L);
                    handler = WarpUtilsModule.mc.field_1724.field_7512;
                    if (handler != null && handler.field_7763 > 0) {
                        this.clickSlot(handler.field_7763, bestSlot);
                        this.sendMessage("\u0422\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0438\u0440\u0443\u044e...");
                    }
                } else {
                    this.sendMessage("\u0412\u0430\u0440\u043f \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d, \u043f\u043e\u043f\u0440\u043e\u0431\u0443\u0439\u0442\u0435 \u0443\u0432\u0435\u043b\u0438\u0447\u0438\u0442\u044c MaxDistance");
                }
            }
        }
    }

    private void clickSlot(int syncId, int slotId) {
        mc.execute(() -> {
            if (WarpUtilsModule.mc.field_1761 != null && WarpUtilsModule.mc.field_1724 != null) {
                WarpUtilsModule.mc.field_1761.method_2906(syncId, slotId, 0, class_1713.field_7790, (class_1657)WarpUtilsModule.mc.field_1724);
            }
        });
    }

    private int getCurrentPage(class_1703 handler, class_1792.class_9635 context, class_1836 type, int slotId) {
        if (slotId >= 0 && slotId < handler.field_7761.size()) {
            class_1799 item = handler.method_7611(slotId).method_7677();
            if (item.method_7960()) {
                return 0;
            }
            for (class_2561 line : item.method_7950(context, (class_1657)WarpUtilsModule.mc.field_1724, type)) {
                String s = line.getString();
                if (!s.contains("\u0421\u0442\u0440\u0430\u043d\u0438\u0446\u0430") || !s.contains("\u0438\u0437")) continue;
                try {
                    String[] parts = s.split("\u0438\u0437");
                    String pageStr = parts[0].replaceAll("[^0-9]", "").trim();
                    if (pageStr.isEmpty()) continue;
                    return Integer.parseInt(pageStr);
                }
                catch (Exception exception) {
                }
            }
        }
        return 0;
    }

    private int getTotalPages(class_1703 handler, class_1792.class_9635 context, class_1836 type, int slotId) {
        if (slotId >= 0 && slotId < handler.field_7761.size()) {
            class_1799 item = handler.method_7611(slotId).method_7677();
            if (item.method_7960()) {
                return 0;
            }
            for (class_2561 line : item.method_7950(context, (class_1657)WarpUtilsModule.mc.field_1724, type)) {
                String s = line.getString();
                if (!s.contains("\u0421\u0442\u0440\u0430\u043d\u0438\u0446\u0430") || !s.contains("\u0438\u0437")) continue;
                try {
                    String totalStr;
                    String[] parts = s.split("\u0438\u0437");
                    if (parts.length <= 1 || (totalStr = parts[1].trim().replaceAll("[^0-9]", "")).isEmpty()) continue;
                    return Integer.parseInt(totalStr);
                }
                catch (Exception exception) {
                }
            }
        }
        return 0;
    }

    private void sendMessage(String msg) {
        if (WarpUtilsModule.mc.field_1724 != null) {
            WarpUtilsModule.mc.field_1724.method_7353(class_2561.method_30163((String)("\u00a76[WarpUtils] \u00a7f" + msg)), false);
        }
    }

    @Generated
    public static WarpUtilsModule getInstance() {
        return instance;
    }
}

