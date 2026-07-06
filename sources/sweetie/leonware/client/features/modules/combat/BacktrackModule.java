package sweetie.leonware.client.features.modules.combat;

import java.awt.Color;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.Generated;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_2596;
import net.minecraft.class_4587;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.event.events.player.world.AttackEvent;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.interfaces.IBacktrackable;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.display.BoxRender;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/BacktrackModule.class */
@ModuleRegister(name = "Backtrack", category = Category.COMBAT)
public class BacktrackModule extends Module {
    private static final BacktrackModule instance = new BacktrackModule();
    private class_1297 target;
    private class_243 ghostPos;
    private class_243 realPos;
    private final ModeSetting mode = new ModeSetting("Mode").value("Default").values("Default", "DelayP");
    private final SliderSetting delay = new SliderSetting("Задержка (мс)").value(Float.valueOf(150.0f)).range(50.0f, 1000.0f).step(10.0f).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("Default"));
    });
    private final BooleanSetting visual = new BooleanSetting("Визуал").value((Boolean) true).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("Default"));
    });
    private final BooleanSetting applyToSpeed = new BooleanSetting("Apply to Speed").value((Boolean) false).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("Default"));
    });
    private final SliderSetting delayP = new SliderSetting("Задержка").value(Float.valueOf(150.0f)).range(50.0f, 500.0f).step(1.0f).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("DelayP"));
    });
    private final SliderSetting chance = new SliderSetting("Шанс").value(Float.valueOf(100.0f)).range(0.0f, 100.0f).step(1.0f).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("DelayP"));
    });
    private final BooleanSetting renderBoxes = new BooleanSetting("Отрисовка").value((Boolean) true).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("DelayP"));
    });
    private final BooleanSetting disableOnFlag = new BooleanSetting("Выкл. при флаге").value((Boolean) true).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("DelayP"));
    });
    private final BooleanSetting chatLog = new BooleanSetting("Чат лог 3.5+").value((Boolean) false).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("DelayP"));
    });
    private final ConcurrentLinkedQueue<DelayedPacket> delayedPackets = new ConcurrentLinkedQueue<>();
    private long lastPacketTime = 0;

    @Generated
    public static BacktrackModule getInstance() {
        return instance;
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/BacktrackModule$Position.class */
    public static final class Position extends Record {
        private final class_243 pos;
        private final long time;

        public Position(class_243 pos, long time) {
            this.pos = pos;
            this.time = time;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Position.class), Position.class, "pos;time", "FIELD:Lsweetie/leonware/client/features/modules/combat/BacktrackModule$Position;->pos:Lnet/minecraft/class_243;", "FIELD:Lsweetie/leonware/client/features/modules/combat/BacktrackModule$Position;->time:J").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Position.class), Position.class, "pos;time", "FIELD:Lsweetie/leonware/client/features/modules/combat/BacktrackModule$Position;->pos:Lnet/minecraft/class_243;", "FIELD:Lsweetie/leonware/client/features/modules/combat/BacktrackModule$Position;->time:J").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Position.class, Object.class), Position.class, "pos;time", "FIELD:Lsweetie/leonware/client/features/modules/combat/BacktrackModule$Position;->pos:Lnet/minecraft/class_243;", "FIELD:Lsweetie/leonware/client/features/modules/combat/BacktrackModule$Position;->time:J").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public class_243 pos() {
            return this.pos;
        }

        public long time() {
            return this.time;
        }
    }

    /* JADX WARN: Type inference failed for: r1v10, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v13, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v18, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v23, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v26, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v29, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v32, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    /* JADX WARN: Type inference failed for: r1v7, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    public BacktrackModule() {
        addSettings(this.mode, this.delay, this.visual, this.applyToSpeed, this.delayP, this.chance, this.renderBoxes, this.disableOnFlag, this.chatLog);
    }

    public boolean isApplyToSpeed() {
        return isEnabled() && this.mode.is("Default") && this.applyToSpeed.getValue().booleanValue();
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        if (this.mode.is("DelayP")) {
            onDisableDelayP();
            return;
        }
        if (mc.field_1687 == null) {
            return;
        }
        for (IBacktrackable iBacktrackable : mc.field_1687.method_18456()) {
            if (iBacktrackable != mc.field_1724 && (iBacktrackable instanceof IBacktrackable)) {
                IBacktrackable bt = iBacktrackable;
                bt.leonware$getBackTracks().clear();
            }
        }
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener(event -> {
            if (this.mode.is("DelayP")) {
                onTickDelayP();
                return;
            }
            if (mc.field_1687 == null || mc.field_1724 == null) {
                return;
            }
            long now = System.currentTimeMillis();
            long delayMs = this.delay.getValue().longValue();
            for (IBacktrackable iBacktrackable : mc.field_1687.method_18456()) {
                if (iBacktrackable != mc.field_1724 && (iBacktrackable instanceof IBacktrackable)) {
                    IBacktrackable bt = iBacktrackable;
                    ArrayDeque<Position> tracks = bt.leonware$getBackTracks();
                    while (!tracks.isEmpty() && now - tracks.peekFirst().time() > delayMs) {
                        tracks.pollFirst();
                    }
                }
            }
        }));
        EventListener render3DEvent = Render3DEvent.getInstance().subscribe(new Listener(event2 -> {
            if (this.mode.is("DelayP")) {
                onRender3DDelayP(event2.matrixStack());
                return;
            }
            if (!this.visual.getValue().booleanValue() || mc.field_1687 == null) {
                return;
            }
            class_4587 ms = event2.matrixStack();
            RenderUtil.WORLD.startRender(ms);
            for (IBacktrackable iBacktrackable : mc.field_1687.method_18456()) {
                if (iBacktrackable != mc.field_1724 && (iBacktrackable instanceof IBacktrackable)) {
                    IBacktrackable bt = iBacktrackable;
                    ArrayDeque<Position> tracks = bt.leonware$getBackTracks();
                    if (!tracks.isEmpty()) {
                        Position last = tracks.peekFirst();
                        class_243 offset = last.pos().method_1020(iBacktrackable.method_19538());
                        class_238 box = iBacktrackable.method_5829().method_997(offset);
                        Color fill = new Color(UIColors.primary().getRed(), UIColors.primary().getGreen(), UIColors.primary().getBlue(), 40);
                        Color outline = new Color(UIColors.primary().getRed(), UIColors.primary().getGreen(), UIColors.primary().getBlue(), 200);
                        RenderUtil.BOX.drawBox((float) box.field_1323, (float) box.field_1322, (float) box.field_1321, (float) box.field_1320, (float) box.field_1325, (float) box.field_1324, 1.5f, fill, BoxRender.Render.FILL, 0.0f);
                        RenderUtil.BOX.drawBox((float) box.field_1323, (float) box.field_1322, (float) box.field_1321, (float) box.field_1320, (float) box.field_1325, (float) box.field_1324, 1.5f, outline, BoxRender.Render.OUTLINE, 0.0f);
                    }
                }
            }
            RenderUtil.WORLD.endRender(ms);
        }));
        EventListener attackEvent = AttackEvent.getInstance().subscribe(new Listener(event3 -> {
            if (this.mode.is("DelayP")) {
                onAttackDelayP(event3.entity());
            }
        }));
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener(event4 -> {
            if (!this.mode.is("DelayP") || !event4.isReceive()) {
                return;
            }
            onPacketReceiveDelayP(event4.packet());
        }));
        addEvents(tickEvent, render3DEvent, attackEvent, packetEvent);
    }

    private void onAttackDelayP(class_1297 ent) {
        if (!isEnabled() || Math.random() * 100.0d > this.chance.getValue().floatValue() || ent == null || !(ent instanceof class_1309) || ent == mc.field_1724) {
            return;
        }
        if (this.target == null || this.target.method_5628() != ent.method_5628()) {
            this.target = ent;
            this.ghostPos = ent.method_19538();
            this.realPos = ent.method_19538();
        }
        if (this.chatLog.getValue().booleanValue() && this.realPos != null && mc.field_1724 != null) {
            double distance = mc.field_1724.method_19538().method_1022(this.realPos);
            if (distance > 3.5d) {
                mc.field_1724.method_7353(class_2561.method_43470(String.format("§7[§cBacktrack§7] §fHit from §c%.2f§f blocks", Double.valueOf(distance))), false);
            }
        }
    }

    private void onTickDelayP() {
        if (isEnabled()) {
            if (this.target != null && (this.target.method_31481() || !this.target.method_5805())) {
                if (this.realPos != null) {
                    this.target.method_5814(this.realPos.field_1352, this.realPos.field_1351, this.realPos.field_1350);
                    this.target.field_6014 = this.realPos.field_1352;
                    this.target.field_6036 = this.realPos.field_1351;
                    this.target.field_5969 = this.realPos.field_1350;
                    this.target.field_6038 = this.realPos.field_1352;
                    this.target.field_5971 = this.realPos.field_1351;
                    this.target.field_5989 = this.realPos.field_1350;
                }
                clear();
                flushAllPackets();
                return;
            }
            if (this.target != null && this.realPos != null) {
                this.target.field_6014 = this.realPos.field_1352;
                this.target.field_6036 = this.realPos.field_1351;
                this.target.field_5969 = this.realPos.field_1350;
                this.target.field_6038 = this.realPos.field_1352;
                this.target.field_5971 = this.realPos.field_1351;
                this.target.field_5989 = this.realPos.field_1350;
            }
            flushOldPackets();
        }
    }

    private void onRender3DDelayP(class_4587 matrixStack) {
        if (!this.renderBoxes.getValue().booleanValue() || this.target == null) {
            return;
        }
        double d = ((double) this.target.method_17681()) / 2.0d;
        double h = this.target.method_17682();
        RenderUtil.WORLD.startRender(matrixStack);
        if (this.realPos != null) {
            class_238 realBox = new class_238(-d, 0.0d, -d, d, h, d).method_997(this.realPos);
            RenderUtil.BOX.drawBox((float) realBox.field_1323, (float) realBox.field_1322, (float) realBox.field_1321, (float) realBox.field_1320, (float) realBox.field_1325, (float) realBox.field_1324, 1.5f, new Color(255, 50, 50, 180), BoxRender.Render.OUTLINE, 0.0f);
        }
        if (this.ghostPos != null && this.realPos != null && this.ghostPos.method_1022(this.realPos) > 0.1d) {
            class_238 ghostBox = new class_238(-d, 0.0d, -d, d, h, d).method_997(this.ghostPos);
            RenderUtil.BOX.drawBox((float) ghostBox.field_1323, (float) ghostBox.field_1322, (float) ghostBox.field_1321, (float) ghostBox.field_1320, (float) ghostBox.field_1325, (float) ghostBox.field_1324, 1.0f, new Color(100, 150, 255, 120), BoxRender.Render.OUTLINE, 0.0f);
        }
        RenderUtil.WORLD.endRender(matrixStack);
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x007d  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x00bf  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void onPacketReceiveDelayP(net.minecraft.class_2596<?> r14) {
        /*
            Method dump skipped, instruction units count: 417
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: sweetie.leonware.client.features.modules.combat.BacktrackModule.onPacketReceiveDelayP(net.minecraft.class_2596):void");
    }

    private void flushOldPackets() {
        long currentTime = System.currentTimeMillis();
        this.delayedPackets.removeIf(delayedPacket -> {
            if (currentTime - delayedPacket.timestamp >= this.delayP.getValue().floatValue()) {
                try {
                    processIncomingPacket(delayedPacket.packet);
                    return true;
                } catch (Exception e) {
                    return true;
                }
            }
            return false;
        });
    }

    private void flushAllPackets() {
        while (!this.delayedPackets.isEmpty()) {
            DelayedPacket delayedPacket = this.delayedPackets.poll();
            try {
                processIncomingPacket(delayedPacket.packet);
            } catch (Exception e) {
            }
        }
    }

    private void processIncomingPacket(class_2596<?> packet) {
        if (mc.method_1562() != null) {
            try {
                packet.method_65081(mc.method_1562());
            } catch (Exception e) {
            }
        }
    }

    private void clear() {
        this.target = null;
        this.ghostPos = null;
        this.realPos = null;
        this.lastPacketTime = 0L;
    }

    private void onDisableDelayP() {
        if (this.target != null && this.realPos != null) {
            this.target.method_5814(this.realPos.field_1352, this.realPos.field_1351, this.realPos.field_1350);
            this.target.field_6014 = this.realPos.field_1352;
            this.target.field_6036 = this.realPos.field_1351;
            this.target.field_5969 = this.realPos.field_1350;
            this.target.field_6038 = this.realPos.field_1352;
            this.target.field_5971 = this.realPos.field_1351;
            this.target.field_5989 = this.realPos.field_1350;
        }
        clear();
        flushAllPackets();
        if (mc.field_1687 != null) {
            mc.field_1687.method_18112().forEach(entity -> {
                if (entity instanceof class_1309) {
                    entity.field_6014 = entity.method_23317();
                    entity.field_6036 = entity.method_23318();
                    entity.field_5969 = entity.method_23321();
                }
            });
        }
    }

    @Override // sweetie.leonware.api.module.Module
    public String getDisplayInfo() {
        if (this.mode.is("DelayP")) {
            return this.delayedPackets.size();
        }
        return null;
    }

    public boolean isDelayP() {
        return this.mode.is("DelayP");
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/combat/BacktrackModule$DelayedPacket.class */
    private static class DelayedPacket {
        public final class_2596<?> packet;
        public final long timestamp;

        public DelayedPacket(class_2596<?> packet, long timestamp) {
            this.packet = packet;
            this.timestamp = timestamp;
        }
    }
}
