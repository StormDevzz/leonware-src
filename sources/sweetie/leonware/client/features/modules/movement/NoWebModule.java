package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.utils.player.MoveUtil;
import sweetie.leonware.api.utils.player.PlayerUtil;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/NoWebModule.class */
@ModuleRegister(name = "No Web", category = Category.MOVEMENT)
public class NoWebModule extends Module {
    private static final NoWebModule instance = new NoWebModule();
    private final ModeSetting mode = new ModeSetting("Режим").values("Фиксированный", "Кастомный").value("Фиксированный");
    private final SliderSetting customHorizontalSpeed = new SliderSetting("Горизонтальная скорость").value(Float.valueOf(0.19175f)).range(0.01f, 1.5f).step(0.001f).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("Кастомный"));
    });
    private final SliderSetting customVerticalSpeed = new SliderSetting("Вертикальная скорость").value(Float.valueOf(0.995f)).range(0.01f, 1.5f).step(0.001f).setVisible(() -> {
        return Boolean.valueOf(this.mode.is("Кастомный"));
    });

    @Generated
    public static NoWebModule getInstance() {
        return instance;
    }

    /* JADX WARN: Type inference failed for: r1v12, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    /* JADX WARN: Type inference failed for: r1v7, types: [sweetie.leonware.api.module.setting.SliderSetting] */
    public NoWebModule() {
        addSettings(this.mode, this.customHorizontalSpeed, this.customVerticalSpeed);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            double verticalSpeed;
            double horizontalSpeed;
            if (PlayerUtil.isInWeb()) {
                mc.field_1724.method_18800(0.0d, 0.0d, 0.0d);
                if (this.mode.is("Кастомный")) {
                    verticalSpeed = this.customVerticalSpeed.getValue().floatValue();
                    horizontalSpeed = this.customHorizontalSpeed.getValue().floatValue();
                } else {
                    verticalSpeed = 0.995d;
                    horizontalSpeed = 0.19175d;
                }
                if (mc.field_1690.field_1903.method_1434()) {
                    mc.field_1724.method_18798().field_1351 = verticalSpeed;
                } else if (mc.field_1690.field_1832.method_1434()) {
                    mc.field_1724.method_18798().field_1351 = -verticalSpeed;
                }
                MoveUtil.setSpeed(horizontalSpeed);
            }
        }));
        addEvents(updateEvent);
    }
}
