// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import net.minecraft.class_1297;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.utils.render.display.BoxRender;
import sweetie.leonware.api.utils.color.ColorUtil;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import net.minecraft.class_2813;
import net.minecraft.class_2886;
import net.minecraft.class_2885;
import net.minecraft.class_2879;
import net.minecraft.class_2868;
import net.minecraft.class_2824;
import net.minecraft.class_2743;
import net.minecraft.class_2664;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.module.setting.Setting;
import java.util.concurrent.ConcurrentLinkedQueue;
import sweetie.leonware.api.utils.math.TimerUtil;
import net.minecraft.class_2596;
import java.util.Queue;
import net.minecraft.class_243;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Fake Lag", category = Category.PLAYER)
public class FakeLagModule extends Module
{
    private static final FakeLagModule instance;
    private final SliderSetting resetTime;
    private final BooleanSetting render;
    private final BooleanSetting resetOnKnockback;
    private class_243 lastPos;
    private final Queue<class_2596<?>> packets;
    private final TimerUtil timerUtil;
    private boolean isCancel;
    
    public FakeLagModule() {
        this.resetTime = new SliderSetting("Reset time").value(100.0f).range(0.0f, 1000.0f).step(10.0f);
        this.render = new BooleanSetting("Render").value(true);
        this.resetOnKnockback = new BooleanSetting("Reset on knockback").value(true);
        this.lastPos = class_243.field_1353;
        this.packets = new ConcurrentLinkedQueue<class_2596<?>>();
        this.timerUtil = new TimerUtil();
        this.addSettings(this.resetTime, this.render, this.resetOnKnockback);
    }
    
    @Override
    public void onEvent() {
        final EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener<TickEvent>(event -> {
            if (!this.cancelWork() && this.timerUtil.finished(this.resetTime.getValue().longValue())) {
                this.resetFakeLag();
            }
            return;
        }));
        final EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(event -> {
            if (this.cancelWork()) {
                return;
            }
            else {
                if (event.isReceive()) {
                    if (event.packet() instanceof class_2664) {
                        this.resetFakeLag();
                    }
                    final class_2596 patt0$temp = event.packet();
                    if (patt0$temp instanceof final class_2743 velocityPacket) {
                        if (velocityPacket.method_11818() == FakeLagModule.mc.field_1724.method_5628() && this.resetOnKnockback.getValue()) {
                            this.resetFakeLag();
                        }
                    }
                }
                else if (event.isSend()) {
                    if (event.packet() instanceof class_2824 || event.packet() instanceof class_2868 || event.packet() instanceof class_2879 || event.packet() instanceof class_2885 || event.packet() instanceof class_2886 || event.packet() instanceof class_2813) {
                        this.resetFakeLag();
                    }
                    else if (!this.isCancel) {
                        this.packets.add(event.packet());
                        PacketEvent.getInstance().setCancel(true);
                    }
                }
                return;
            }
        }));
        final EventListener renderEvent = Render3DEvent.getInstance().subscribe(new Listener<Render3DEvent.Render3DEventData>(event -> {
            if (this.cancelWork()) {
                return;
            }
            else {
                if (this.render.getValue()) {
                    final class_1297 player = (class_1297)FakeLagModule.mc.field_1724;
                    final float x = (float)this.lastPos.field_1352;
                    final float y = (float)this.lastPos.field_1351 + 1.0f;
                    final float z = (float)this.lastPos.field_1350;
                    RenderUtil.BOX.drawBox(x, y, z, x + player.method_17681(), y + player.method_17682(), z + player.method_17681(), 3.0f, ColorUtil.setAlpha(UIColors.gradient(0), 200), BoxRender.Render.STRIPED, player.method_17681() / 5.5f);
                }
                return;
            }
        }));
        this.addEvents(tickEvent, packetEvent, renderEvent);
    }
    
    private boolean cancelWork() {
        return FakeLagModule.mc.method_1542();
    }
    
    private void resetFakeLag() {
        this.isCancel = true;
        while (!this.packets.isEmpty()) {
            this.sendPacket(this.packets.poll());
        }
        this.isCancel = false;
        this.timerUtil.reset();
        if (FakeLagModule.mc.field_1724 == null) {
            return;
        }
        this.lastPos = FakeLagModule.mc.field_1724.method_19538();
    }
    
    @Override
    public void onEnable() {
        this.timerUtil.reset();
        if (FakeLagModule.mc.field_1724 == null) {
            return;
        }
        this.lastPos = FakeLagModule.mc.field_1724.method_19538();
    }
    
    @Override
    public void onDisable() {
        this.resetFakeLag();
    }
    
    @Generated
    public static FakeLagModule getInstance() {
        return FakeLagModule.instance;
    }
    
    static {
        instance = new FakeLagModule();
    }
}
