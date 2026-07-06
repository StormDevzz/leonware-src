// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.other;

import lombok.Generated;
import net.minecraft.class_1713;
import java.util.Iterator;
import net.minecraft.class_1799;
import net.minecraft.class_1703;
import net.minecraft.class_2561;
import net.minecraft.class_1657;
import net.minecraft.class_1937;
import net.minecraft.class_1792;
import net.minecraft.class_1836;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import net.minecraft.class_3944;
import sweetie.leonware.api.event.events.client.PacketEvent;
import org.joml.Vector2i;
import sweetie.leonware.api.system.client.GpsManager;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Warp Utils", category = Category.OTHER)
public class WarpUtilsModule extends Module
{
    private static final WarpUtilsModule instance;
    private final SliderSetting targetX;
    private final SliderSetting targetZ;
    private final SliderSetting maxDistance;
    private final BooleanSetting setGps;
    private static final int SLOT = 47;
    private volatile boolean awaitingWarpChest;
    private volatile boolean stopThread;
    private volatile Thread searchThread;
    
    public WarpUtilsModule() {
        this.targetX = new SliderSetting("Target X").value(0.0f).range(-20000.0f, 20000.0f).step(1.0f);
        this.targetZ = new SliderSetting("Target Z").value(0.0f).range(-20000.0f, 20000.0f).step(1.0f);
        this.maxDistance = new SliderSetting("Max Distance").value(5000.0f).range(100.0f, 50000.0f).step(100.0f);
        this.setGps = new BooleanSetting("Set GPS").value(true);
        this.awaitingWarpChest = false;
        this.stopThread = false;
        this.searchThread = null;
        this.addSettings(this.targetX, this.targetZ, this.maxDistance, this.setGps);
    }
    
    @Override
    public void onEnable() {
        if (WarpUtilsModule.mc.field_1724 == null || WarpUtilsModule.mc.method_1562() == null) {
            return;
        }
        this.stopThread = true;
        if (this.searchThread != null && this.searchThread.isAlive()) {
            this.searchThread.interrupt();
        }
        this.stopThread = false;
        WarpUtilsModule.mc.method_1562().method_45731("warp");
        this.awaitingWarpChest = true;
        if (this.setGps.getValue()) {
            final int x = this.targetX.getValue().intValue();
            final int z = this.targetZ.getValue().intValue();
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
        final EventListener packetListener = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(data -> {
            if (this.awaitingWarpChest && data.isReceive() && data.packet() instanceof class_3944) {
                this.awaitingWarpChest = false;
                if (this.searchThread != null && this.searchThread.isAlive()) {
                    this.stopThread = true;
                    this.searchThread.interrupt();
                }
                this.stopThread = false;
                (this.searchThread = new Thread(() -> {
                    try {
                        Thread.sleep(48L);
                        if (WarpUtilsModule.mc.field_1724 != null && WarpUtilsModule.mc.field_1724.field_7512 != null && !this.stopThread) {
                            this.searchAndClickNearestWarp(47);
                        }
                    }
                    catch (final InterruptedException var2) {
                        this.sendMessage("\u041f\u0440\u0435\u0440\u0432\u0430\u043d\u043e");
                    }
                    catch (final Exception var3) {
                        this.sendMessage("\u041e\u0448\u0438\u0431\u043a\u0430: " + var3.getMessage());
                    }
                }, "WarpUtils-Search")).start();
            }
            return;
        }));
        this.addEvents(packetListener);
    }
    
    private void searchAndClickNearestWarp(final int pageSlot) throws InterruptedException {
        if (WarpUtilsModule.mc.field_1724 != null && WarpUtilsModule.mc.field_1761 != null) {
            class_1703 handler = WarpUtilsModule.mc.field_1724.field_7512;
            if (handler != null && pageSlot >= 0 && pageSlot < handler.field_7761.size()) {
                final class_1836 type = (class_1836)class_1836.field_41070;
                final class_1792.class_9635 context = class_1792.class_9635.method_59528((class_1937)WarpUtilsModule.mc.field_1687);
                final double inputX = this.targetX.getValue();
                final double inputZ = this.targetZ.getValue();
                double bestDistance = Double.MAX_VALUE;
                int bestSlot = -1;
                int bestPage = 1;
                final int totalPages = this.getTotalPages(handler, context, type, pageSlot);
                if (totalPages <= 0) {
                    this.sendMessage("\u041d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u043e\u043f\u0440\u0435\u0434\u0435\u043b\u0438\u0442\u044c \u0441\u0442\u0440\u0430\u043d\u0438\u0446\u044b");
                }
                else {
                    int pagesScanned = 0;
                    while (pagesScanned < totalPages && !this.stopThread) {
                        handler = WarpUtilsModule.mc.field_1724.field_7512;
                        if (handler == null) {
                            break;
                        }
                        final int actualPage = this.getCurrentPage(handler, context, type, pageSlot);
                        for (int i = 9; i <= 44 && i < handler.field_7761.size(); ++i) {
                            final class_1799 stack = handler.method_7611(i).method_7677();
                            if (!stack.method_7960()) {
                                for (final class_2561 line : stack.method_7950(context, (class_1657)WarpUtilsModule.mc.field_1724, type)) {
                                    final String s = line.getString();
                                    if (s.contains("\u041a\u043e\u043e\u0440\u0434\u0438\u043d\u0430\u0442\u044b:")) {
                                        final String coords = s.replace("\u041a\u043e\u043e\u0440\u0434\u0438\u043d\u0430\u0442\u044b:", "").trim();
                                        final String[] xyz = coords.split(",");
                                        if (xyz.length < 3) {
                                            continue;
                                        }
                                        try {
                                            final double x = Double.parseDouble(xyz[0].trim());
                                            final double z = Double.parseDouble(xyz[2].trim());
                                            final double dist = Math.sqrt(Math.pow(x - inputX, 2.0) + Math.pow(z - inputZ, 2.0));
                                            if (dist > this.maxDistance.getValue() || dist >= bestDistance) {
                                                continue;
                                            }
                                            bestDistance = dist;
                                            bestSlot = i;
                                            bestPage = actualPage;
                                        }
                                        catch (final Exception ex) {}
                                    }
                                }
                            }
                        }
                        if (++pagesScanned >= totalPages || this.stopThread) {
                            continue;
                        }
                        this.clickSlot(handler.field_7763, pageSlot);
                        Thread.sleep(248L);
                        handler = WarpUtilsModule.mc.field_1724.field_7512;
                        if (handler == null) {
                            break;
                        }
                        int newPage = this.getCurrentPage(handler, context, type, pageSlot);
                        if (newPage != actualPage) {
                            continue;
                        }
                        Thread.sleep(148L);
                        this.clickSlot(handler.field_7763, pageSlot);
                        Thread.sleep(248L);
                        handler = WarpUtilsModule.mc.field_1724.field_7512;
                        if (handler != null) {
                            newPage = this.getCurrentPage(handler, context, type, pageSlot);
                        }
                        if (newPage == actualPage) {
                            this.sendMessage("\u041d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u043f\u0435\u0440\u0435\u043a\u043b\u044e\u0447\u0438\u0442\u044c \u0441\u0442\u0440\u0430\u043d\u0438\u0446\u0443");
                            break;
                        }
                    }
                    if (bestSlot != -1) {
                        this.sendMessage("\u0412\u0430\u0440\u043f \u043d\u0430\u0439\u0434\u0435\u043d! \u0411\u043b\u043e\u043a\u043e\u0432 \u0434\u043e \u043a\u043e\u043e\u0440\u0434\u0438\u043d\u0430\u0442: " + (int)bestDistance);
                        handler = WarpUtilsModule.mc.field_1724.field_7512;
                        if (handler == null) {
                            return;
                        }
                        int newPage2;
                        for (int actualPage = this.getCurrentPage(handler, context, type, pageSlot), i = 0; actualPage != bestPage && i < totalPages + 3 && !this.stopThread; actualPage = newPage2, ++i) {
                            this.clickSlot(handler.field_7763, pageSlot);
                            Thread.sleep(248L);
                            handler = WarpUtilsModule.mc.field_1724.field_7512;
                            if (handler == null) {
                                return;
                            }
                            newPage2 = this.getCurrentPage(handler, context, type, pageSlot);
                            if (newPage2 == actualPage) {
                                Thread.sleep(148L);
                                this.clickSlot(handler.field_7763, pageSlot);
                                Thread.sleep(248L);
                                handler = WarpUtilsModule.mc.field_1724.field_7512;
                                if (handler != null) {
                                    newPage2 = this.getCurrentPage(handler, context, type, pageSlot);
                                }
                            }
                        }
                        Thread.sleep(500L);
                        handler = WarpUtilsModule.mc.field_1724.field_7512;
                        if (handler != null && handler.field_7763 > 0) {
                            this.clickSlot(handler.field_7763, bestSlot);
                            this.sendMessage("\u0422\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0438\u0440\u0443\u044e...");
                        }
                    }
                    else {
                        this.sendMessage("\u0412\u0430\u0440\u043f \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d, \u043f\u043e\u043f\u0440\u043e\u0431\u0443\u0439\u0442\u0435 \u0443\u0432\u0435\u043b\u0438\u0447\u0438\u0442\u044c MaxDistance");
                    }
                }
            }
        }
    }
    
    private void clickSlot(final int syncId, final int slotId) {
        WarpUtilsModule.mc.execute(() -> {
            if (WarpUtilsModule.mc.field_1761 != null && WarpUtilsModule.mc.field_1724 != null) {
                WarpUtilsModule.mc.field_1761.method_2906(syncId, slotId, 0, class_1713.field_7790, (class_1657)WarpUtilsModule.mc.field_1724);
            }
        });
    }
    
    private int getCurrentPage(final class_1703 handler, final class_1792.class_9635 context, final class_1836 type, final int slotId) {
        if (slotId >= 0 && slotId < handler.field_7761.size()) {
            final class_1799 item = handler.method_7611(slotId).method_7677();
            if (item.method_7960()) {
                return 0;
            }
            for (final class_2561 line : item.method_7950(context, (class_1657)WarpUtilsModule.mc.field_1724, type)) {
                final String s = line.getString();
                if (s.contains("\u0421\u0442\u0440\u0430\u043d\u0438\u0446\u0430") && s.contains("\u0438\u0437")) {
                    try {
                        final String[] parts = s.split("\u0438\u0437");
                        final String pageStr = parts[0].replaceAll("[^0-9]", "").trim();
                        if (!pageStr.isEmpty()) {
                            return Integer.parseInt(pageStr);
                        }
                        continue;
                    }
                    catch (final Exception ex) {}
                }
            }
        }
        return 0;
    }
    
    private int getTotalPages(final class_1703 handler, final class_1792.class_9635 context, final class_1836 type, final int slotId) {
        if (slotId >= 0 && slotId < handler.field_7761.size()) {
            final class_1799 item = handler.method_7611(slotId).method_7677();
            if (item.method_7960()) {
                return 0;
            }
            for (final class_2561 line : item.method_7950(context, (class_1657)WarpUtilsModule.mc.field_1724, type)) {
                final String s = line.getString();
                if (s.contains("\u0421\u0442\u0440\u0430\u043d\u0438\u0446\u0430") && s.contains("\u0438\u0437")) {
                    try {
                        final String[] parts = s.split("\u0438\u0437");
                        if (parts.length <= 1) {
                            continue;
                        }
                        final String totalStr = parts[1].trim().replaceAll("[^0-9]", "");
                        if (!totalStr.isEmpty()) {
                            return Integer.parseInt(totalStr);
                        }
                        continue;
                    }
                    catch (final Exception ex) {}
                }
            }
        }
        return 0;
    }
    
    private void sendMessage(final String msg) {
        if (WarpUtilsModule.mc.field_1724 != null) {
            WarpUtilsModule.mc.field_1724.method_7353(class_2561.method_30163("§6[WarpUtils] §f" + msg), false);
        }
    }
    
    @Generated
    public static WarpUtilsModule getInstance() {
        return WarpUtilsModule.instance;
    }
    
    static {
        instance = new WarpUtilsModule();
    }
}
