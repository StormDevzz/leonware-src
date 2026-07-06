/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_2596
 *  net.minecraft.class_2799
 *  net.minecraft.class_2827
 */
package sweetie.leonware.client.features.modules.player;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.Generated;
import net.minecraft.class_2596;
import net.minecraft.class_2799;
import net.minecraft.class_2827;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

@ModuleRegister(name="Packet Blocks", category=Category.PLAYER)
public class BlockFlyModule
extends Module {
    private static final BlockFlyModule instance = new BlockFlyModule();
    private final ModeSetting mode = new ModeSetting("Mode").value("Smart").values("Normal", "Smart");
    private final SliderSetting interval = new SliderSetting("Interval").value(Float.valueOf(500.0f)).range(50.0f, 5000.0f).step(50.0f).setVisible(() -> this.mode.is("Normal"));
    private final SliderSetting maxQueueSize = new SliderSetting("Max Queue Size").value(Float.valueOf(1000.0f)).range(100.0f, 5000.0f).step(100.0f);
    private final Queue<class_2596<?>> storedPackets = new LinkedList();
    private final AtomicBoolean sending = new AtomicBoolean(false);
    private final Random random = new Random();
    private long lastPulseTime = 0L;
    private int currentPulseDelayMs = 0;
    private int totalPacketsSent = 0;
    private int totalPacketsQueued = 0;

    public BlockFlyModule() {
        this.addSettings(this.mode, this.interval, this.maxQueueSize);
    }

    @Override
    public void onEnable() {
        if (BlockFlyModule.mc.field_1724 == null || BlockFlyModule.mc.field_1687 == null || mc.method_1562() == null) {
            this.setEnabled(false);
            return;
        }
        this.storedPackets.clear();
        this.sending.set(false);
        this.lastPulseTime = System.currentTimeMillis();
        this.currentPulseDelayMs = this.mode.is("Smart") ? this.rand(150, 250) : ((Float)this.interval.getValue()).intValue();
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
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener<TickEvent>(event -> {
            if (BlockFlyModule.mc.field_1724 == null || BlockFlyModule.mc.field_1687 == null) {
                return;
            }
            long now = System.currentTimeMillis();
            if (now - this.lastPulseTime >= (long)this.currentPulseDelayMs && !this.storedPackets.isEmpty()) {
                this.flushPackets();
                this.lastPulseTime = now;
                this.currentPulseDelayMs = this.mode.is("Smart") ? this.rand(150, 250) : ((Float)this.interval.getValue()).intValue();
            }
        }));
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(event -> {
            if (BlockFlyModule.mc.field_1724 == null || BlockFlyModule.mc.field_1687 == null || this.sending.get()) {
                return;
            }
            class_2596<?> packet = event.packet();
            if (packet instanceof class_2799 || packet instanceof class_2827) {
                return;
            }
            if (this.storedPackets.size() >= ((Float)this.maxQueueSize.getValue()).intValue()) {
                return;
            }
            if (event.isSend()) {
                PacketEvent.getInstance().setCancel(true);
                this.storedPackets.add(packet);
                ++this.totalPacketsQueued;
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
            class_2596<?> packet = this.storedPackets.poll();
            this.sendPacket(packet);
            ++this.totalPacketsSent;
            ++flushed;
        }
        this.sending.set(false);
    }

    private int rand(int min, int max) {
        return min + this.random.nextInt(max - min + 1);
    }

    @Generated
    public static BlockFlyModule getInstance() {
        return instance;
    }
}

