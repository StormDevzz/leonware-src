package sweetie.leonware.client.features.modules.other;

import lombok.Generated;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2846;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.DisconnectEvent;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.SliderSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/other/KTLeaveModule.class */
@ModuleRegister(name = "KTLeave", category = Category.OTHER)
public class KTLeaveModule extends Module {
    private static final KTLeaveModule instance = new KTLeaveModule();
    public final SliderSetting packets = new SliderSetting("Кол-во пакетов").value(Float.valueOf(5.0f)).range(1.0f, 7.0f).step(1.0f);
    private int ticks = 0;

    @Generated
    public static KTLeaveModule getInstance() {
        return instance;
    }

    public KTLeaveModule() {
        addSettings(this.packets);
    }

    @Override // sweetie.leonware.api.module.Module
    public void onEnable() {
        this.ticks = 0;
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1724 == null || mc.field_1687 == null || mc.method_1562() == null) {
                disable();
                return;
            }
            this.ticks++;
            if (this.ticks >= 1) {
                this.ticks = 0;
                int count = this.packets.getValue().intValue();
                for (int i = 0; i < count; i++) {
                    mc.method_1562().method_52787(new class_2846(class_2846.class_2847.field_12971, class_2338.field_10980, class_2350.field_11033));
                }
            }
        }));
        EventListener disconnectListener = DisconnectEvent.getInstance().subscribe(new Listener(event2 -> {
            disable();
        }));
        addEvents(updateEvent, disconnectListener);
    }
}
