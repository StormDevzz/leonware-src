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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/player/BlockFlyModule.class */
@ModuleRegister(name = "Packet Blocks", category = Category.PLAYER)
public class BlockFlyModule extends Module {
    private static final BlockFlyModule instance = new BlockFlyModule();
    private final ModeSetting mode = new ModeSetting("Mode").value("Smart").values("Normal", "Smart");
    private final SliderSetting interval = new SliderSetting("Interval").value(Float.valueOf(500.0f)).range(50.0f, 5000.0f).step(50.0f).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("Normal"));
    });
    private final SliderSetting maxQueueSize = new SliderSetting("Max Queue Size").value(Float.valueOf(1000.0f)).range(100.0f, 5000.0f).step(100.0f);
    private final Queue<class_2596<?>> storedPackets = new LinkedList();
    private final AtomicBoolean sending = new AtomicBoolean(false);
    private final Random random = new Random();
    private long lastPulseTime = 0;
    private int currentPulseDelayMs = 0;
    private int totalPacketsSent = 0;
    private int totalPacketsQueued = 0;

    /* JADX WARN: Type inference failed for: r1v7, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    public BlockFlyModule() {
        addSettings(this.mode, this.interval, this.maxQueueSize);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onEnable() {
        if (mc.field_1724 == null || mc.field_1687 == null || mc.method_1562() == null) {
            setEnabled(false);
            return;
        }
        this.storedPackets.clear();
        this.sending.set(false);
        this.lastPulseTime = System.currentTimeMillis();
        this.currentPulseDelayMs = this.mode.is("Smart") ? rand(150, 250) : this.interval.getValue().intValue();
        this.totalPacketsSent = 0;
        this.totalPacketsQueued = 0;
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        if (mc.field_1724 == null || mc.field_1687 == null) {
            return;
        }
        this.sending.set(true);
        flushPackets();
        this.sending.set(false);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1724 == null || mc.field_1687 == null) {
                return;
            }
            long now = System.currentTimeMillis();
            if (now - this.lastPulseTime < this.currentPulseDelayMs || this.storedPackets.isEmpty()) {
                return;
            }
            flushPackets();
            this.lastPulseTime = now;
            this.currentPulseDelayMs = this.mode.is("Smart") ? rand(150, 250) : this.interval.getValue().intValue();
        }));
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener(event2 -> {
            if (mc.field_1724 == null || mc.field_1687 == null || this.sending.get()) {
                return;
            }
            class_2596<?> packet = event2.packet();
            if ((packet instanceof class_2799) || (packet instanceof class_2827) || this.storedPackets.size() >= this.maxQueueSize.getValue().intValue() || !event2.isSend()) {
                return;
            }
            PacketEvent.getInstance().setCancel(true);
            this.storedPackets.add(packet);
            this.totalPacketsQueued++;
        }));
        addEvents(tickEvent, packetEvent);
    }

    private void flushPackets() {
        if (mc.field_1724 == null) {
            return;
        }
        this.sending.set(true);
        int flushed = 0;
        while (!this.storedPackets.isEmpty()) {
            class_2596<?> packet = this.storedPackets.poll();
            sendPacket(packet);
            this.totalPacketsSent++;
            flushed++;
        }
        this.sending.set(false);
    }

    private int rand(int min, int max) {
        return min + this.random.nextInt((max - min) + 1);
    }

    @Generated
    public static BlockFlyModule getInstance() {
        return instance;
    }
}
