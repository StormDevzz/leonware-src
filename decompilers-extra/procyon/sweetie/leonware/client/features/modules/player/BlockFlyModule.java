// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import sweetie.leonware.api.event.EventListener;
import net.minecraft.class_2827;
import net.minecraft.class_2799;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.module.setting.Setting;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.class_2596;
import java.util.Queue;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Packet Blocks", category = Category.PLAYER)
public class BlockFlyModule extends Module
{
    private static final BlockFlyModule instance;
    private final ModeSetting mode;
    private final SliderSetting interval;
    private final SliderSetting maxQueueSize;
    private final Queue<class_2596<?>> storedPackets;
    private final AtomicBoolean sending;
    private final Random random;
    private long lastPulseTime;
    private int currentPulseDelayMs;
    private int totalPacketsSent;
    private int totalPacketsQueued;
    
    public BlockFlyModule() {
        this.mode = new ModeSetting("Mode").value("Smart").values("Normal", "Smart");
        this.interval = new SliderSetting("Interval").value(500.0f).range(50.0f, 5000.0f).step(50.0f).setVisible(() -> this.mode.is("Normal"));
        this.maxQueueSize = new SliderSetting("Max Queue Size").value(1000.0f).range(100.0f, 5000.0f).step(100.0f);
        this.storedPackets = new LinkedList<class_2596<?>>();
        this.sending = new AtomicBoolean(false);
        this.random = new Random();
        this.lastPulseTime = 0L;
        this.currentPulseDelayMs = 0;
        this.totalPacketsSent = 0;
        this.totalPacketsQueued = 0;
        this.addSettings(this.mode, this.interval, this.maxQueueSize);
    }
    
    @Override
    public void onEnable() {
        if (BlockFlyModule.mc.field_1724 == null || BlockFlyModule.mc.field_1687 == null || BlockFlyModule.mc.method_1562() == null) {
            this.setEnabled(false);
            return;
        }
        this.storedPackets.clear();
        this.sending.set(false);
        this.lastPulseTime = System.currentTimeMillis();
        this.currentPulseDelayMs = (this.mode.is("Smart") ? this.rand(150, 250) : this.interval.getValue().intValue());
        this.totalPacketsSent = 0;
        this.totalPacketsQueued = 0;
    }
    
    @Override
    public void onDisable() {
        if (BlockFlyModule.mc.field_1724 == null || BlockFlyModule.mc.field_1687 == null) {
            return;
        }
        this.sending.set(true);
        this.flushPackets();
        this.sending.set(false);
    }
    
    @Override
    public void onEvent() {
        final EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener<TickEvent>(event -> {
            if (BlockFlyModule.mc.field_1724 == null || BlockFlyModule.mc.field_1687 == null) {
                return;
            }
            else {
                final long now = System.currentTimeMillis();
                if (now - this.lastPulseTime >= this.currentPulseDelayMs && !this.storedPackets.isEmpty()) {
                    this.flushPackets();
                    this.lastPulseTime = now;
                    this.currentPulseDelayMs = (this.mode.is("Smart") ? this.rand(150, 250) : this.interval.getValue().intValue());
                }
                return;
            }
        }));
        final EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(event -> {
            if (BlockFlyModule.mc.field_1724 == null || BlockFlyModule.mc.field_1687 == null || this.sending.get()) {
                return;
            }
            else {
                final class_2596<?> packet = event.packet();
                if (packet instanceof class_2799 || packet instanceof class_2827) {
                    return;
                }
                else if (this.storedPackets.size() >= this.maxQueueSize.getValue().intValue()) {
                    return;
                }
                else {
                    if (event.isSend()) {
                        PacketEvent.getInstance().setCancel(true);
                        this.storedPackets.add(packet);
                        ++this.totalPacketsQueued;
                    }
                    return;
                }
            }
        }));
        this.addEvents(tickEvent, packetEvent);
    }
    
    private void flushPackets() {
        if (BlockFlyModule.mc.field_1724 == null) {
            return;
        }
        this.sending.set(true);
        int flushed = 0;
        while (!this.storedPackets.isEmpty()) {
            final class_2596<?> packet = this.storedPackets.poll();
            this.sendPacket(packet);
            ++this.totalPacketsSent;
            ++flushed;
        }
        this.sending.set(false);
    }
    
    private int rand(final int min, final int max) {
        return min + this.random.nextInt(max - min + 1);
    }
    
    @Generated
    public static BlockFlyModule getInstance() {
        return BlockFlyModule.instance;
    }
    
    static {
        instance = new BlockFlyModule();
    }
}
