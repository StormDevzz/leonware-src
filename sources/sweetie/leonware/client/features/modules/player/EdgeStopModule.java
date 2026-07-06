package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.MovementInputEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/player/EdgeStopModule.class */
@ModuleRegister(name = "Edge Stop", category = Category.PLAYER)
public class EdgeStopModule extends Module {
    private static final EdgeStopModule instance = new EdgeStopModule();

    @Generated
    public static EdgeStopModule getInstance() {
        return instance;
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener moveInputEvent = MovementInputEvent.getInstance().subscribe(new Listener(event -> {
            if (mc.field_1724 == null || mc.field_1687 == null) {
                return;
            }
            class_2338 pos = mc.field_1724.method_24515().method_10074();
            boolean atEdge = mc.field_1687.method_8320(pos).method_27852(class_2246.field_10124) && mc.field_1724.method_24828();
            if (atEdge) {
                event.setSneak(true);
            }
        }));
        addEvents(moveInputEvent);
    }
}
