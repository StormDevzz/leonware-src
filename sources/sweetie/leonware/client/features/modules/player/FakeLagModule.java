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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/player/FakeLagModule.class */
@ModuleRegister(name = "Fake Lag", category = Category.PLAYER)
public class FakeLagModule extends Module {
    private static final FakeLagModule instance = new FakeLagModule();
    private final SliderSetting resetTime = new SliderSetting("Reset time").value(Float.valueOf(100.0f)).range(0.0f, 1000.0f).step(10.0f);
    private final BooleanSetting render = new BooleanSetting("Render").value((Boolean) true);
    private final BooleanSetting resetOnKnockback = new BooleanSetting("Reset on knockback").value((Boolean) true);
    private class_243 lastPos = class_243.field_1353;
    private final Queue<class_2596<?>> packets = new ConcurrentLinkedQueue();
    private final TimerUtil timerUtil = new TimerUtil();
    private boolean isCancel;

    @Generated
    public static FakeLagModule getInstance() {
        return instance;
    }

    public FakeLagModule() {
        addSettings(this.resetTime, this.render, this.resetOnKnockback);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener(event -> {
            if (!cancelWork() && this.timerUtil.finished(this.resetTime.getValue().longValue())) {
                resetFakeLag();
            }
        }));
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener(event2 -> {
            if (cancelWork()) {
                return;
            }
            if (event2.isReceive()) {
                if (event2.packet() instanceof class_2664) {
                    resetFakeLag();
                }
                class_2743 class_2743VarPacket = event2.packet();
                if (class_2743VarPacket instanceof class_2743) {
                    class_2743 velocityPacket = class_2743VarPacket;
                    if (velocityPacket.method_11818() == mc.field_1724.method_5628() && this.resetOnKnockback.getValue().booleanValue()) {
                        resetFakeLag();
                        return;
                    }
                    return;
                }
                return;
            }
            if (event2.isSend()) {
                if ((event2.packet() instanceof class_2824) || (event2.packet() instanceof class_2868) || (event2.packet() instanceof class_2879) || (event2.packet() instanceof class_2885) || (event2.packet() instanceof class_2886) || (event2.packet() instanceof class_2813)) {
                    resetFakeLag();
                } else if (!this.isCancel) {
                    this.packets.add(event2.packet());
                    PacketEvent.getInstance().setCancel(true);
                }
            }
        }));
        EventListener renderEvent = Render3DEvent.getInstance().subscribe(new Listener(event3 -> {
            if (!cancelWork() && this.render.getValue().booleanValue()) {
                class_746 class_746Var = mc.field_1724;
                float x = (float) this.lastPos.field_1352;
                float y = ((float) this.lastPos.field_1351) + 1.0f;
                float z = (float) this.lastPos.field_1350;
                RenderUtil.BOX.drawBox(x, y, z, x + class_746Var.method_17681(), y + class_746Var.method_17682(), z + class_746Var.method_17681(), 3.0f, ColorUtil.setAlpha(UIColors.gradient(0), 200), BoxRender.Render.STRIPED, class_746Var.method_17681() / 5.5f);
            }
        }));
        addEvents(tickEvent, packetEvent, renderEvent);
    }

    private boolean cancelWork() {
        return mc.method_1542();
    }

    private void resetFakeLag() {
        this.isCancel = true;
        while (!this.packets.isEmpty()) {
            sendPacket(this.packets.poll());
        }
        this.isCancel = false;
        this.timerUtil.reset();
        if (mc.field_1724 == null) {
            return;
        }
        this.lastPos = mc.field_1724.method_19538();
    }

    @Override // sweetie.leonware.api.module.Module
    public void onEnable() {
        this.timerUtil.reset();
        if (mc.field_1724 == null) {
            return;
        }
        this.lastPos = mc.field_1724.method_19538();
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        resetFakeLag();
    }
}
