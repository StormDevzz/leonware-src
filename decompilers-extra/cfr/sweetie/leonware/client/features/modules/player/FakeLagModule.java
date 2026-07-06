/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_243
 *  net.minecraft.class_2596
 *  net.minecraft.class_2664
 *  net.minecraft.class_2743
 *  net.minecraft.class_2813
 *  net.minecraft.class_2824
 *  net.minecraft.class_2868
 *  net.minecraft.class_2879
 *  net.minecraft.class_2885
 *  net.minecraft.class_2886
 *  net.minecraft.class_746
 */
package sweetie.leonware.client.features.modules.player;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.Generated;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2664;
import net.minecraft.class_2743;
import net.minecraft.class_2813;
import net.minecraft.class_2824;
import net.minecraft.class_2868;
import net.minecraft.class_2879;
import net.minecraft.class_2885;
import net.minecraft.class_2886;
import net.minecraft.class_746;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.event.events.render.Render3DEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.color.ColorUtil;
import sweetie.leonware.api.utils.color.UIColors;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.utils.render.RenderUtil;
import sweetie.leonware.api.utils.render.display.BoxRender;

@ModuleRegister(name="Fake Lag", category=Category.PLAYER)
public class FakeLagModule
extends Module {
    private static final FakeLagModule instance = new FakeLagModule();
    private final SliderSetting resetTime = new SliderSetting("Reset time").value(Float.valueOf(100.0f)).range(0.0f, 1000.0f).step(10.0f);
    private final BooleanSetting render = new BooleanSetting("Render").value(true);
    private final BooleanSetting resetOnKnockback = new BooleanSetting("Reset on knockback").value(true);
    private class_243 lastPos = class_243.field_1353;
    private final Queue<class_2596<?>> packets = new ConcurrentLinkedQueue();
    private final TimerUtil timerUtil = new TimerUtil();
    private boolean isCancel;

    public FakeLagModule() {
        this.addSettings(this.resetTime, this.render, this.resetOnKnockback);
    }

    @Override
    public void onEvent() {
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener<TickEvent>(event -> {
            if (!this.cancelWork() && this.timerUtil.finished(((Float)this.resetTime.getValue()).longValue())) {
                this.resetFakeLag();
            }
        }));
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(event -> {
            if (this.cancelWork()) {
                return;
            }
            if (event.isReceive()) {
                class_2743 velocityPacket;
                class_2596<?> patt0$temp;
                if (event.packet() instanceof class_2664) {
                    this.resetFakeLag();
                }
                if ((patt0$temp = event.packet()) instanceof class_2743 && (velocityPacket = (class_2743)patt0$temp).method_11818() == FakeLagModule.mc.field_1724.method_5628() && ((Boolean)this.resetOnKnockback.getValue()).booleanValue()) {
                    this.resetFakeLag();
                }
            } else if (event.isSend()) {
                if (event.packet() instanceof class_2824 || event.packet() instanceof class_2868 || event.packet() instanceof class_2879 || event.packet() instanceof class_2885 || event.packet() instanceof class_2886 || event.packet() instanceof class_2813) {
                    this.resetFakeLag();
                    return;
                }
                if (!this.isCancel) {
                    this.packets.add(event.packet());
                    PacketEvent.getInstance().setCancel(true);
                }
            }
        }));
        EventListener renderEvent = Render3DEvent.getInstance().subscribe(new Listener<Render3DEvent.Render3DEventData>(event -> {
            if (this.cancelWork()) {
                return;
            }
            if (((Boolean)this.render.getValue()).booleanValue()) {
                class_746 player = FakeLagModule.mc.field_1724;
                float x = (float)this.lastPos.field_1352;
                float y = (float)this.lastPos.field_1351 + 1.0f;
                float z = (float)this.lastPos.field_1350;
                RenderUtil.BOX.drawBox(x, y, z, x + player.method_17681(), y + player.method_17682(), z + player.method_17681(), 3.0f, ColorUtil.setAlpha(UIColors.gradient(0), 200), BoxRender.Render.STRIPED, player.method_17681() / 5.5f);
            }
        }));
        this.addEvents(tickEvent, packetEvent, renderEvent);
    }

    private boolean cancelWork() {
        return mc.method_1542();
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
        return instance;
    }
}

