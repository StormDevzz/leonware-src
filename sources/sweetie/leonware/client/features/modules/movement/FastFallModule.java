package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import net.minecraft.class_243;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.SliderSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/FastFallModule.class */
@ModuleRegister(name = "Fast Fall", category = Category.MOVEMENT)
public class FastFallModule extends Module {
    private static final FastFallModule instance = new FastFallModule();
    private final SliderSetting speed = new SliderSetting("Скорость").value(Float.valueOf(1.0f)).range(0.1f, 5.0f).step(0.1f);

    @Generated
    public static FastFallModule getInstance() {
        return instance;
    }

    public FastFallModule() {
        addSettings(this.speed);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1724 != null && mc.field_1687 != null && !mc.field_1690.field_1903.method_1434() && !mc.field_1724.method_5771() && !mc.field_1724.method_5799() && mc.field_1724.method_24828()) {
                class_243 currentVel = mc.field_1724.method_18798();
                mc.field_1724.method_18800(currentVel.field_1352, -this.speed.getValue().doubleValue(), currentVel.field_1350);
            }
        }));
        addEvents(updateEvent);
    }
}
