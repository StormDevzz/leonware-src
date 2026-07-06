// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import net.minecraft.class_2596;
import net.minecraft.class_1536;
import net.minecraft.class_2743;
import net.minecraft.class_3414;
import net.minecraft.class_2767;
import net.minecraft.class_1657;
import net.minecraft.class_1268;
import net.minecraft.class_1787;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.module.setting.Setting;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.Module;

@ModuleRegister(name = "Auto Fish", category = Category.PLAYER)
public class AutoFishModule extends Module
{
    private static final AutoFishModule instance;
    private final ModeSetting detectMode;
    private final SliderSetting castDelay;
    private final SliderSetting velocityThreshold;
    private final TimerUtil timer;
    private boolean needToCast;
    
    public AutoFishModule() {
        this.detectMode = new ModeSetting("\u0421\u043f\u043e\u0441\u043e\u0431 \u0434\u0435\u0442\u0435\u043a\u0442\u0430").values("\u0417\u0432\u0443\u043a", "\u0414\u0432\u0438\u0436\u0435\u043d\u0438\u0435").value("\u0417\u0432\u0443\u043a");
        this.castDelay = new SliderSetting("\u0417\u0430\u0434\u0435\u0440\u0436\u043a\u0430 \u0437\u0430\u0431\u0440\u043e\u0441\u0430").value(500.0f).range(0.0f, 3000.0f).step(50.0f);
        this.velocityThreshold = new SliderSetting("\u041f\u043e\u0440\u043e\u0433 \u0440\u044b\u0432\u043a\u0430").value(0.05f).range(0.01f, 0.5f).step(0.01f).setVisible(() -> this.detectMode.is("\u0414\u0432\u0438\u0436\u0435\u043d\u0438\u0435"));
        this.timer = new TimerUtil();
        this.needToCast = false;
        this.addSettings(this.detectMode, this.castDelay, this.velocityThreshold);
    }
    
    @Override
    public void onEvent() {
        final EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener<TickEvent>(this::onTick));
        final EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(this::onPacket));
        this.addEvents(tickEvent, packetEvent);
    }
    
    @Override
    public void onEnable() {
        this.needToCast = false;
        this.timer.reset();
    }
    
    private void onTick(final TickEvent event) {
        if (AutoFishModule.mc.field_1724 == null) {
            return;
        }
        if (this.needToCast && this.timer.finished(this.castDelay.getValue())) {
            if (AutoFishModule.mc.field_1724.method_6047().method_7909() instanceof class_1787 || AutoFishModule.mc.field_1724.method_6079().method_7909() instanceof class_1787) {
                AutoFishModule.mc.field_1761.method_2919((class_1657)AutoFishModule.mc.field_1724, class_1268.field_5808);
            }
            this.needToCast = false;
        }
    }
    
    private void onPacket(final PacketEvent.PacketEventData event) {
        if (AutoFishModule.mc.field_1724 == null || AutoFishModule.mc.field_1687 == null || this.needToCast) {
            return;
        }
        final class_1536 bobber = AutoFishModule.mc.field_1724.field_7513;
        if (bobber == null) {
            return;
        }
        if (this.detectMode.is("\u0417\u0432\u0443\u043a")) {
            final class_2596<?> packet = event.packet();
            if (packet instanceof final class_2767 soundPacket) {
                if (((class_3414)soundPacket.method_11894().comp_349()).comp_3319().method_12832().equals("entity.fishing_bobber.splash")) {
                    final double dist = bobber.method_5649(soundPacket.method_11890(), soundPacket.method_11889(), soundPacket.method_11893());
                    if (dist < 4.0) {
                        this.catchFish();
                    }
                }
            }
        }
        if (this.detectMode.is("\u0414\u0432\u0438\u0436\u0435\u043d\u0438\u0435")) {
            final class_2596<?> packet2 = event.packet();
            if (packet2 instanceof final class_2743 velocityPacket) {
                if (velocityPacket.method_11818() == bobber.method_5628()) {
                    final double velocityY = velocityPacket.method_11816();
                    if (velocityY < -this.velocityThreshold.getValue()) {
                        this.catchFish();
                    }
                }
            }
        }
    }
    
    private void catchFish() {
        AutoFishModule.mc.field_1761.method_2919((class_1657)AutoFishModule.mc.field_1724, class_1268.field_5808);
        this.needToCast = true;
        this.timer.reset();
    }
    
    @Generated
    public static AutoFishModule getInstance() {
        return AutoFishModule.instance;
    }
    
    static {
        instance = new AutoFishModule();
    }
}
