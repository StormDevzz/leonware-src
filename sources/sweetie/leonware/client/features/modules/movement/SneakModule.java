package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import net.minecraft.class_2596;
import net.minecraft.class_2848;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/SneakModule.class */
@ModuleRegister(name = "Sneak", category = Category.MOVEMENT)
public class SneakModule extends Module {
    private static final SneakModule instance = new SneakModule();
    private final ModeSetting mode = new ModeSetting("Режим").values("Ванильный", "Пакетный").value("Ванильный");
    private final BooleanSetting twerk = new BooleanSetting("Тверк").value((Boolean) false).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("Ванильный"));
    });
    private final SliderSetting twerkDelay = new SliderSetting("Задержка тверка").value(Float.valueOf(200.0f)).range(50.0f, 1000.0f).step(10.0f).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("Ванильный") && this.twerk.getValue().booleanValue());
    });
    private long lastTwerkTime = 0;
    private boolean twerkState = false;

    @Generated
    public static SneakModule getInstance() {
        return instance;
    }

    /* JADX WARN: Type inference failed for: r1v10, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v5, types: [sweetie.leonware.api.module.setting.BooleanSetting] */
    public SneakModule() {
        addSettings(this.mode, this.twerk, this.twerkDelay);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onEnable() {
        this.twerkState = false;
        this.lastTwerkTime = System.currentTimeMillis();
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        if (mc.field_1724 == null) {
            return;
        }
        if (this.mode.is("Ванильный")) {
            mc.field_1724.method_5660(false);
            mc.field_1690.field_1832.method_23481(false);
        } else if (this.mode.is("Пакетный") && mc.method_1562() != null) {
            sendPacket((class_2596<?>) new class_2848(mc.field_1724, class_2848.class_2849.field_12984));
        }
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1724 == null) {
                return;
            }
            if (this.mode.is("Ванильный")) {
                handleVanilla();
            } else if (this.mode.is("Пакетный")) {
                handlePacket();
            }
        }));
        addEvents(updateEvent);
    }

    private void handleVanilla() {
        if (this.twerk.getValue().booleanValue()) {
            long now = System.currentTimeMillis();
            if (now - this.lastTwerkTime >= this.twerkDelay.getValue().longValue()) {
                this.twerkState = !this.twerkState;
                mc.field_1724.method_5660(this.twerkState);
                mc.field_1690.field_1832.method_23481(this.twerkState);
                this.lastTwerkTime = now;
                return;
            }
            return;
        }
        mc.field_1724.method_5660(true);
        mc.field_1690.field_1832.method_23481(true);
    }

    private void handlePacket() {
        sendPacket((class_2596<?>) new class_2848(mc.field_1724, class_2848.class_2849.field_12979));
    }
}
