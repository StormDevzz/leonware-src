package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import net.minecraft.class_1268;
import net.minecraft.class_1536;
import net.minecraft.class_1787;
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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/player/AutoFishModule.class */
@ModuleRegister(name = "Auto Fish", category = Category.PLAYER)
public class AutoFishModule extends Module {
    private static final AutoFishModule instance = new AutoFishModule();
    private final ModeSetting detectMode = new ModeSetting("Способ детекта").values("Звук", "Движение").value("Звук");
    private final SliderSetting castDelay = new SliderSetting("Задержка заброса").value(Float.valueOf(500.0f)).range(0.0f, 3000.0f).step(50.0f);
    private final SliderSetting velocityThreshold = new SliderSetting("Порог рывка").value(Float.valueOf(0.05f)).range(0.01f, 0.5f).step(0.01f).setVisible(() -> {
        return Boolean.valueOf(this.detectMode.is("Движение"));
    });
    private final TimerUtil timer = new TimerUtil();
    private boolean needToCast = false;

    @Generated
    public static AutoFishModule getInstance() {
        return instance;
    }

    /* JADX WARN: Type inference failed for: r1v11, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    public AutoFishModule() {
        addSettings(this.detectMode, this.castDelay, this.velocityThreshold);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener(this::onTick));
        EventListener packetEvent = PacketEvent.getInstance().subscribe(new Listener(this::onPacket));
        addEvents(tickEvent, packetEvent);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onEnable() {
        this.needToCast = false;
        this.timer.reset();
    }

    private void onTick(TickEvent event) {
        if (mc.field_1724 != null && this.needToCast && this.timer.finished(this.castDelay.getValue().floatValue())) {
            if ((mc.field_1724.method_6047().method_7909() instanceof class_1787) || (mc.field_1724.method_6079().method_7909() instanceof class_1787)) {
                mc.field_1761.method_2919(mc.field_1724, class_1268.field_5808);
            }
            this.needToCast = false;
        }
    }

    private void onPacket(PacketEvent.PacketEventData event) {
        class_1536 bobber;
        if (mc.field_1724 == null || mc.field_1687 == null || this.needToCast || (bobber = mc.field_1724.field_7513) == null) {
            return;
        }
        if (this.detectMode.is("Звук")) {
            class_2767 class_2767VarPacket = event.packet();
            if (class_2767VarPacket instanceof class_2767) {
                class_2767 soundPacket = class_2767VarPacket;
                if (((class_3414) soundPacket.method_11894().comp_349()).comp_3319().method_12832().equals("entity.fishing_bobber.splash")) {
                    double dist = bobber.method_5649(soundPacket.method_11890(), soundPacket.method_11889(), soundPacket.method_11893());
                    if (dist < 4.0d) {
                        catchFish();
                    }
                }
            }
        }
        if (this.detectMode.is("Движение")) {
            class_2743 class_2743VarPacket = event.packet();
            if (class_2743VarPacket instanceof class_2743) {
                class_2743 velocityPacket = class_2743VarPacket;
                if (velocityPacket.method_11818() == bobber.method_5628()) {
                    double velocityY = velocityPacket.method_11816();
                    if (velocityY < (-this.velocityThreshold.getValue().floatValue())) {
                        catchFish();
                    }
                }
            }
        }
    }

    private void catchFish() {
        mc.field_1761.method_2919(mc.field_1724, class_1268.field_5808);
        this.needToCast = true;
        this.timer.reset();
    }
}
