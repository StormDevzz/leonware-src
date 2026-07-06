// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.combat;

import lombok.Generated;
import net.minecraft.class_2547;
import net.minecraft.class_2743;
import net.minecraft.class_1937;
import net.minecraft.class_2684;
import net.minecraft.class_10264;
import net.minecraft.class_2777;
import net.minecraft.class_2661;
import net.minecraft.class_2708;
import net.minecraft.class_2596;
import net.minecraft.class_2561;
import net.minecraft.class_1309;
import net.minecraft.class_238;
import net.minecraft.class_4587;
import java.util.ArrayDeque;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.events.player.world.AttackEvent;
import sweetie.leonware.api.utils.render.display.BoxRender;
import java.awt.Color;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.TickEvent;
import java.util.Iterator;
import sweetie.leonware.api.interfaces.IBacktrackable;
import net.minecraft.class_1657;
import sweetie.leonware.api.module.setting.Setting;
import net.minecraft.class_243;
import net.minecraft.class_1297;
import java.util.concurrent.ConcurrentLinkedQueue;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Backtrack", category = Category.COMBAT)
public class BacktrackModule extends Module
{
    private static final BacktrackModule instance;
    private final ModeSetting mode;
    private final SliderSetting delay;
    private final BooleanSetting visual;
    private final BooleanSetting applyToSpeed;
    private final SliderSetting delayP;
    private final SliderSetting chance;
    private final BooleanSetting renderBoxes;
    private final BooleanSetting disableOnFlag;
    private final BooleanSetting chatLog;
    private final ConcurrentLinkedQueue<DelayedPacket> delayedPackets;
    private class_1297 target;
    private class_243 ghostPos;
    private class_243 realPos;
    private long lastPacketTime;
    
    public BacktrackModule() {
        this.mode = new ModeSetting("Mode").value("Default").values("Default", "DelayP");
        this.delay = new SliderSetting("\u0417\u0430\u0434\u0435\u0440\u0436\u043a\u0430 (\u043c\u0441)").value(150.0f).range(50.0f, 1000.0f).step(10.0f).setVisible(() -> this.mode.is("Default"));
        this.visual = new BooleanSetting("\u0412\u0438\u0437\u0443\u0430\u043b").value(true).setVisible(() -> this.mode.is("Default"));
        this.applyToSpeed = new BooleanSetting("Apply to Speed").value(false).setVisible(() -> this.mode.is("Default"));
        this.delayP = new SliderSetting("\u0417\u0430\u0434\u0435\u0440\u0436\u043a\u0430").value(150.0f).range(50.0f, 500.0f).step(1.0f).setVisible(() -> this.mode.is("DelayP"));
        this.chance = new SliderSetting("\u0428\u0430\u043d\u0441").value(100.0f).range(0.0f, 100.0f).step(1.0f).setVisible(() -> this.mode.is("DelayP"));
        this.renderBoxes = new BooleanSetting("\u041e\u0442\u0440\u0438\u0441\u043e\u0432\u043a\u0430").value(true).setVisible(() -> this.mode.is("DelayP"));
        this.disableOnFlag = new BooleanSetting("\u0412\u044b\u043a\u043b. \u043f\u0440\u0438 \u0444\u043b\u0430\u0433\u0435").value(true).setVisible(() -> this.mode.is("DelayP"));
        this.chatLog = new BooleanSetting("\u0427\u0430\u0442 \u043b\u043e\u0433 3.5+").value(false).setVisible(() -> this.mode.is("DelayP"));
        this.delayedPackets = new ConcurrentLinkedQueue<DelayedPacket>();
        this.lastPacketTime = 0L;
        this.addSettings(this.mode, this.delay, this.visual, this.applyToSpeed, this.delayP, this.chance, this.renderBoxes, this.disableOnFlag, this.chatLog);
    }
    
    public boolean isApplyToSpeed() {
        return this.isEnabled() && this.mode.is("Default") && this.applyToSpeed.getValue();
    }
    
    @Override
    public void onDisable() {
        if (this.mode.is("DelayP")) {
            this.onDisableDelayP();
            return;
        }
        if (BacktrackModule.mc.field_1687 == null) {
            return;
        }
        for (final class_1657 player : BacktrackModule.mc.field_1687.method_18456()) {
            if (player == BacktrackModule.mc.field_1724) {
                continue;
            }
            if (!(player instanceof IBacktrackable)) {
                continue;
            }
            final IBacktrackable bt = (IBacktrackable)player;
            bt.leonware$getBackTracks().clear();
        }
    }
    
    @Override
    public void onEvent() {
        final EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener<TickEvent>(event -> {
            if (this.mode.is("DelayP")) {
                this.onTickDelayP();
                return;
            }
            else if (BacktrackModule.mc.field_1687 == null || BacktrackModule.mc.field_1724 == null) {
                return;
            }
            else {
                final long now = System.currentTimeMillis();
                final long delayMs = this.delay.getValue().longValue();
                BacktrackModule.mc.field_1687.method_18456().iterator();
                final Iterator iterator;
                while (iterator.hasNext()) {
                    final class_1657 player = iterator.next();
                    if (player == BacktrackModule.mc.field_1724) {
                        continue;
                    }
                    else if (player instanceof final IBacktrackable bt) {
                        final ArrayDeque<Position> tracks = bt.leonware$getBackTracks();
                        while (!tracks.isEmpty() && now - tracks.peekFirst().time() > delayMs) {
                            tracks.pollFirst();
                        }
                    }
                    else {
                        continue;
                    }
                }
                return;
            }
        }));
        final EventListener render3DEvent = Render3DEvent.getInstance().subscribe(new Listener<Render3DEvent.Render3DEventData>(event -> {
            if (this.mode.is("DelayP")) {
                this.onRender3DDelayP(event.matrixStack());
                return;
            }
            else if (!this.visual.getValue() || BacktrackModule.mc.field_1687 == null) {
                return;
            }
            else {
                final class_4587 ms = event.matrixStack();
                RenderUtil.WORLD.startRender(ms);
                BacktrackModule.mc.field_1687.method_18456().iterator();
                final Iterator iterator2;
                while (iterator2.hasNext()) {
                    final class_1657 player2 = iterator2.next();
                    if (player2 == BacktrackModule.mc.field_1724) {
                        continue;
                    }
                    else if (player2 instanceof final IBacktrackable bt2) {
                        final ArrayDeque<Position> tracks2 = bt2.leonware$getBackTracks();
                        if (tracks2.isEmpty()) {
                            continue;
                        }
                        else {
                            final Position last = tracks2.peekFirst();
                            final class_243 offset = last.pos().method_1020(player2.method_19538());
                            final class_238 box = player2.method_5829().method_997(offset);
                            final Color fill = new Color(UIColors.primary().getRed(), UIColors.primary().getGreen(), UIColors.primary().getBlue(), 40);
                            final Color outline = new Color(UIColors.primary().getRed(), UIColors.primary().getGreen(), UIColors.primary().getBlue(), 200);
                            RenderUtil.BOX.drawBox((float)box.field_1323, (float)box.field_1322, (float)box.field_1321, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324, 1.5f, fill, BoxRender.Render.FILL, 0.0f);
                            RenderUtil.BOX.drawBox((float)box.field_1323, (float)box.field_1322, (float)box.field_1321, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324, 1.5f, outline, BoxRender.Render.OUTLINE, 0.0f);
                        }
                    }
                    else {
                        continue;
                    }
                }
                RenderUtil.WORLD.endRender(ms);
                return;
            }
        }));
        final EventListener attackEvent = AttackEvent.getInstance().subscribe(new Listener<AttackEvent.AttackEventData>(event -> {
            if (this.mode.is("DelayP")) {
                this.onAttackDelayP(event.entity());
            }
            return;
        }));
        final EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(event -> {
            if (this.mode.is("DelayP") && event.isReceive()) {
                this.onPacketReceiveDelayP(event.packet());
            }
            return;
        }));
        this.addEvents(tickEvent, render3DEvent, attackEvent, packetEvent);
    }
    
    private void onAttackDelayP(final class_1297 ent) {
        if (!this.isEnabled()) {
            return;
        }
        if (Math.random() * 100.0 > this.chance.getValue()) {
            return;
        }
        if (ent == null || !(ent instanceof class_1309) || ent == BacktrackModule.mc.field_1724) {
            return;
        }
        if (this.target == null || this.target.method_5628() != ent.method_5628()) {
            this.target = ent;
            this.ghostPos = ent.method_19538();
            this.realPos = ent.method_19538();
        }
        if (this.chatLog.getValue() && this.realPos != null && BacktrackModule.mc.field_1724 != null) {
            final double distance = BacktrackModule.mc.field_1724.method_19538().method_1022(this.realPos);
            if (distance > 3.5) {
                BacktrackModule.mc.field_1724.method_7353((class_2561)class_2561.method_43470(String.format("§7[§cBacktrack§7] §fHit from §c%.2f§f blocks", distance)), false);
            }
        }
    }
    
    private void onTickDelayP() {
        if (!this.isEnabled()) {
            return;
        }
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
            this.clear();
            this.flushAllPackets();
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
        this.flushOldPackets();
    }
    
    private void onRender3DDelayP(final class_4587 matrixStack) {
        if (!this.renderBoxes.getValue() || this.target == null) {
            return;
        }
        final double d = this.target.method_17681() / 2.0;
        final double h = this.target.method_17682();
        RenderUtil.WORLD.startRender(matrixStack);
        if (this.realPos != null) {
            final class_238 realBox = new class_238(-d, 0.0, -d, d, h, d).method_997(this.realPos);
            RenderUtil.BOX.drawBox((float)realBox.field_1323, (float)realBox.field_1322, (float)realBox.field_1321, (float)realBox.field_1320, (float)realBox.field_1325, (float)realBox.field_1324, 1.5f, new Color(255, 50, 50, 180), BoxRender.Render.OUTLINE, 0.0f);
        }
        if (this.ghostPos != null && this.realPos != null && this.ghostPos.method_1022(this.realPos) > 0.1) {
            final class_238 ghostBox = new class_238(-d, 0.0, -d, d, h, d).method_997(this.ghostPos);
            RenderUtil.BOX.drawBox((float)ghostBox.field_1323, (float)ghostBox.field_1322, (float)ghostBox.field_1321, (float)ghostBox.field_1320, (float)ghostBox.field_1325, (float)ghostBox.field_1324, 1.0f, new Color(100, 150, 255, 120), BoxRender.Render.OUTLINE, 0.0f);
        }
        RenderUtil.WORLD.endRender(matrixStack);
    }
    
    private void onPacketReceiveDelayP(final class_2596<?> packet) {
        if (!this.isEnabled()) {
            return;
        }
        if (this.disableOnFlag.getValue() && (packet instanceof class_2708 || packet instanceof class_2661)) {
            this.clear();
            this.flushAllPackets();
            return;
        }
        if (this.target == null) {
            return;
        }
        boolean isMovePacket = false;
        class_243 newPos = null;
        Label_0373: {
            if (packet instanceof final class_2777 posPacket) {
                if (posPacket.comp_3237() == this.target.method_5628()) {
                    isMovePacket = true;
                    final class_243 pos = posPacket.comp_3238().comp_3148();
                    newPos = new class_243(pos.field_1352, pos.field_1351, pos.field_1350);
                    break Label_0373;
                }
            }
            if (packet instanceof final class_10264 syncPacket) {
                if (syncPacket.comp_3223() == this.target.method_5628()) {
                    isMovePacket = true;
                    final class_243 pos = syncPacket.comp_3224().comp_3148();
                    newPos = new class_243(pos.field_1352, pos.field_1351, pos.field_1350);
                    break Label_0373;
                }
            }
            if (packet instanceof final class_2684 entityPacket) {
                final class_1297 packetEntity = entityPacket.method_11645((class_1937)BacktrackModule.mc.field_1687);
                if (packetEntity != null && packetEntity.method_5628() == this.target.method_5628()) {
                    isMovePacket = true;
                    final class_243 base = (this.realPos != null) ? this.realPos : this.target.method_19538();
                    newPos = new class_243(base.field_1352 + entityPacket.method_36150() / 4096.0, base.field_1351 + entityPacket.method_36151() / 4096.0, base.field_1350 + entityPacket.method_36152() / 4096.0);
                }
            }
            else if (packet instanceof final class_2743 velPacket) {
                if (velPacket.method_11818() == this.target.method_5628()) {
                    isMovePacket = true;
                    newPos = ((this.realPos != null) ? this.realPos : this.target.method_19538());
                }
            }
        }
        if (!isMovePacket) {
            return;
        }
        this.realPos = newPos;
        PacketEvent.getInstance().setCancel(true);
        this.delayedPackets.add(new DelayedPacket(packet, System.currentTimeMillis()));
        this.lastPacketTime = System.currentTimeMillis();
    }
    
    private void flushOldPackets() {
        final long currentTime = System.currentTimeMillis();
        this.delayedPackets.removeIf(delayedPacket -> {
            if (currentTime - delayedPacket.timestamp >= this.delayP.getValue()) {
                try {
                    this.processIncomingPacket(delayedPacket.packet);
                }
                catch (final Exception ex) {}
                return true;
            }
            else {
                return false;
            }
        });
    }
    
    private void flushAllPackets() {
        while (!this.delayedPackets.isEmpty()) {
            final DelayedPacket delayedPacket = this.delayedPackets.poll();
            try {
                this.processIncomingPacket(delayedPacket.packet);
            }
            catch (final Exception ex) {}
        }
    }
    
    private void processIncomingPacket(final class_2596<?> packet) {
        if (BacktrackModule.mc.method_1562() != null) {
            try {
                packet.method_65081((class_2547)BacktrackModule.mc.method_1562());
            }
            catch (final Exception ex) {}
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
        this.clear();
        this.flushAllPackets();
        if (BacktrackModule.mc.field_1687 != null) {
            BacktrackModule.mc.field_1687.method_18112().forEach(entity -> {
                if (entity instanceof class_1309) {
                    entity.field_6014 = entity.method_23317();
                    entity.field_6036 = entity.method_23318();
                    entity.field_5969 = entity.method_23321();
                }
            });
        }
    }
    
    @Override
    public String getDisplayInfo() {
        if (this.mode.is("DelayP")) {
            return "" + this.delayedPackets.size();
        }
        return null;
    }
    
    public boolean isDelayP() {
        return this.mode.is("DelayP");
    }
    
    @Generated
    public static BacktrackModule getInstance() {
        return BacktrackModule.instance;
    }
    
    static {
        instance = new BacktrackModule();
    }
    
    record Position(class_243 pos, long time) {}
    
    private static class DelayedPacket
    {
        public final class_2596<?> packet;
        public final long timestamp;
        
        public DelayedPacket(final class_2596<?> packet, final long timestamp) {
            this.packet = packet;
            this.timestamp = timestamp;
        }
    }
}
