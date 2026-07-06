/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1268
 *  net.minecraft.class_1536
 *  net.minecraft.class_1657
 *  net.minecraft.class_1787
 *  net.minecraft.class_2596
 *  net.minecraft.class_2743
 *  net.minecraft.class_2767
 *  net.minecraft.class_3414
 */
package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1536;
import net.minecraft.class_1657;
import net.minecraft.class_1787;
import net.minecraft.class_2596;
import net.minecraft.class_2743;
import net.minecraft.class_2767;
import net.minecraft.class_3414;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.PacketEvent;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.math.TimerUtil;

@ModuleRegister(name="Auto Fish", category=Category.PLAYER)
public class AutoFishModule
extends Module {
    private static final AutoFishModule instance = new AutoFishModule();
    private final ModeSetting detectMode = new ModeSetting("\u0421\u043f\u043e\u0441\u043e\u0431 \u0434\u0435\u0442\u0435\u043a\u0442\u0430").values("\u0417\u0432\u0443\u043a", "\u0414\u0432\u0438\u0436\u0435\u043d\u0438\u0435").value("\u0417\u0432\u0443\u043a");
    private final SliderSetting castDelay = new SliderSetting("\u0417\u0430\u0434\u0435\u0440\u0436\u043a\u0430 \u0437\u0430\u0431\u0440\u043e\u0441\u0430").value(Float.valueOf(500.0f)).range(0.0f, 3000.0f).step(50.0f);
    private final SliderSetting velocityThreshold = new SliderSetting("\u041f\u043e\u0440\u043e\u0433 \u0440\u044b\u0432\u043a\u0430").value(Float.valueOf(0.05f)).range(0.01f, 0.5f).step(0.01f).setVisible(() -> this.detectMode.is("\u0414\u0432\u0438\u0436\u0435\u043d\u0438\u0435"));
    private final TimerUtil timer = new TimerUtil();
    private boolean needToCast = false;

    public AutoFishModule() {
        this.addSettings(this.detectMode, this.castDelay, this.velocityThreshold);
    }

    @Override
    public void onEvent() {
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener<TickEvent>(this::onTick));
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener<PacketEvent.PacketEventData>(this::onPacket));
        this.addEvents(tickEvent, packetEvent);
    }

    @Override
    public void onEnable() {
        this.needToCast = false;
        this.timer.reset();
    }

    private void onTick(TickEvent event) {
        if (AutoFishModule.mc.field_1724 == null) {
            return;
        }
        if (this.needToCast && this.timer.finished(((Float)this.castDelay.getValue()).floatValue())) {
            if (AutoFishModule.mc.field_1724.method_6047().method_7909() instanceof class_1787 || AutoFishModule.mc.field_1724.method_6079().method_7909() instanceof class_1787) {
                AutoFishModule.mc.field_1761.method_2919((class_1657)AutoFishModule.mc.field_1724, class_1268.field_5808);
            }
            this.needToCast = false;
        }
    }

    private void onPacket(PacketEvent.PacketEventData event) {
        double velocityY;
        class_2743 velocityPacket;
        class_2596<?> dist2;
        double dist2;
        class_2767 soundPacket;
        class_2596<?> class_25962;
        if (AutoFishModule.mc.field_1724 == null || AutoFishModule.mc.field_1687 == null || this.needToCast) {
            return;
        }
        class_1536 bobber = AutoFishModule.mc.field_1724.field_7513;
        if (bobber == null) {
            return;
        }
        if (this.detectMode.is("\u0417\u0432\u0443\u043a") && (class_25962 = event.packet()) instanceof class_2767 && ((class_3414)(soundPacket = (class_2767)class_25962).method_11894().comp_349()).comp_3319().method_12832().equals("entity.fishing_bobber.splash") && (dist2 = bobber.method_5649(soundPacket.method_11890(), soundPacket.method_11889(), soundPacket.method_11893())) < 4.0) {
            this.catchFish();
        }
        if (this.detectMode.is("\u0414\u0432\u0438\u0436\u0435\u043d\u0438\u0435") && (dist2 = event.packet()) instanceof class_2743 && (velocityPacket = (class_2743)dist2).method_11818() == bobber.method_5628() && (velocityY = velocityPacket.method_11816()) < (double)(-((Float)this.velocityThreshold.getValue()).floatValue())) {
            this.catchFish();
        }
    }

    private void catchFish() {
        AutoFishModule.mc.field_1761.method_2919((class_1657)AutoFishModule.mc.field_1724, class_1268.field_5808);
        this.needToCast = true;
        this.timer.reset();
    }

    @Generated
    public static AutoFishModule getInstance() {
        return instance;
    }
}

