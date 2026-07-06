package sweetie.leonware.client.features.modules.other;

import lombok.Generated;
import net.minecraft.class_418;
import net.minecraft.class_437;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/other/AutoRespawnModule.class */
@ModuleRegister(name = "Auto Respawn", category = Category.OTHER)
public class AutoRespawnModule extends Module {
    private static final AutoRespawnModule instance = new AutoRespawnModule();

    @Generated
    public static AutoRespawnModule getInstance() {
        return instance;
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener(event -> {
            if ((mc.field_1755 instanceof class_418) && mc.field_1724.field_6213 > 2) {
                mc.field_1724.method_7331();
                mc.method_1507((class_437) null);
            }
        }));
        addEvents(tickEvent);
    }
}
