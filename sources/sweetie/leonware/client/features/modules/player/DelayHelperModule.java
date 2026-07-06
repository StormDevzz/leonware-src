package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import net.minecraft.class_1747;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.BooleanSetting;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/player/DelayHelperModule.class */
@ModuleRegister(name = "Delay Helper", category = Category.PLAYER)
public class DelayHelperModule extends Module {
    private static final DelayHelperModule instance = new DelayHelperModule();
    private final BooleanSetting fastUse = new BooleanSetting("Быстрое использование").value((Boolean) true);
    private final BooleanSetting fastPlace = new BooleanSetting("Быстрая установка").value((Boolean) true);

    @Generated
    public static DelayHelperModule getInstance() {
        return instance;
    }

    public DelayHelperModule() {
        addSettings(this.fastUse, this.fastPlace);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1724 == null) {
                return;
            }
            if (this.fastUse.getValue().booleanValue() && !(mc.field_1724.method_6047().method_7909() instanceof class_1747) && !(mc.field_1724.method_6079().method_7909() instanceof class_1747)) {
                mc.field_1752 = 0;
            }
            if (this.fastPlace.getValue().booleanValue()) {
                if ((mc.field_1724.method_6047().method_7909() instanceof class_1747) || (mc.field_1724.method_6079().method_7909() instanceof class_1747)) {
                    mc.field_1752 = 0;
                }
            }
        }));
        addEvents(updateEvent);
    }
}
