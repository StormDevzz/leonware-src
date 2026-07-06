package sweetie.leonware.client.features.modules.movement;

import lombok.Generated;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/movement/NoJumpDelayModule.class */
@ModuleRegister(name = "No Jump Delay", category = Category.MOVEMENT)
public class NoJumpDelayModule extends Module {
    private static final NoJumpDelayModule instance = new NoJumpDelayModule();

    @Generated
    public static NoJumpDelayModule getInstance() {
        return instance;
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            mc.field_1724.field_6228 = 0;
        }));
        addEvents(updateEvent);
    }
}
