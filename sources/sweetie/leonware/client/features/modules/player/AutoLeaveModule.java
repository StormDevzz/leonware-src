package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import net.minecraft.class_746;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.system.configs.FriendManager;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/player/AutoLeaveModule.class */
@ModuleRegister(name = "Auto Leave", category = Category.PLAYER)
public class AutoLeaveModule extends Module {
    private static final AutoLeaveModule instance = new AutoLeaveModule();
    private final SliderSetting distance = new SliderSetting("Distance").value(Float.valueOf(50.0f)).range(1.0f, 100.0f).step(1.0f);
    private final ModeSetting action = new ModeSetting("Action").value("Spawn").values("Hub", "Spawn", "Home");

    @Generated
    public static AutoLeaveModule getInstance() {
        return instance;
    }

    public AutoLeaveModule() {
        addSettings(this.distance, this.action);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event -> {
            handleUpdateEvent();
        }));
        addEvents(updateEvent);
    }

    private void handleUpdateEvent() {
        for (class_746 class_746Var : mc.field_1687.method_18456()) {
            if (mc.field_1724 != class_746Var && !FriendManager.getInstance().contains(class_746Var.method_5477().getString()) && class_746Var.method_19538().method_1022(mc.field_1724.method_19538()) <= this.distance.getValue().floatValue()) {
                handleLeave();
                toggle();
                return;
            }
        }
    }

    private void handleLeave() {
        switch (this.action.getValue()) {
            case "Hub":
                mc.field_1724.field_3944.method_45730("hub");
                break;
            case "Spawn":
                mc.field_1724.field_3944.method_45730("spawn");
                break;
            case "Home":
                mc.field_1724.field_3944.method_45730("home home");
                break;
        }
    }
}
