package sweetie.leonware.client.features.modules.render;

import lombok.Generated;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/render/NightVisionModule.class */
@ModuleRegister(name = "Night Vision", category = Category.RENDER)
public class NightVisionModule extends Module {
    private static final NightVisionModule instance = new NightVisionModule();

    @Generated
    public static NightVisionModule getInstance() {
        return instance;
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        remove();
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener(event -> {
            add();
        }));
        addEvents(tickEvent);
    }

    private void remove() {
        if (mc.field_1724 == null) {
            return;
        }
        mc.field_1724.method_6016(class_1294.field_5925);
    }

    private void add() {
        if (mc.field_1724 == null) {
            return;
        }
        mc.field_1724.method_6092(new class_1293(class_1294.field_5925, -1, 0, false, false, false));
    }
}
