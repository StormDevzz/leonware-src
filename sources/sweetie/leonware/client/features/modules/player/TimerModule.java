package sweetie.leonware.client.features.modules.player;

import lombok.Generated;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.client.TickEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.module.setting.SliderSetting;
import sweetie.leonware.api.system.client.TimerManager;
import sweetie.leonware.api.utils.task.TaskPriority;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/player/TimerModule.class */
@ModuleRegister(name = "Timer", category = Category.PLAYER)
public class TimerModule extends Module {
    private static final TimerModule instance = new TimerModule();
    private final SliderSetting multiplier = new SliderSetting("Multiplier").value(Float.valueOf(2.0f)).range(0.1f, 5.0f).step(0.1f);

    @Generated
    public static TimerModule getInstance() {
        return instance;
    }

    public TimerModule() {
        addSettings(this.multiplier);
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener(event -> {
            TimerManager.getInstance().addTimer(this.multiplier.getValue().floatValue(), TaskPriority.NORMAL, this, 1);
        }));
        addEvents(tickEvent);
    }
}
