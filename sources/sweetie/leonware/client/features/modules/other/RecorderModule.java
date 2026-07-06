package sweetie.leonware.client.features.modules.other;

import lombok.Generated;
import net.minecraft.class_1297;
import sweetie.leonware.api.event.EventListener;
import sweetie.leonware.api.event.Listener;
import sweetie.leonware.api.event.events.player.other.UpdateEvent;
import sweetie.leonware.api.event.events.player.world.AttackEvent;
import sweetie.leonware.api.module.Category;
import sweetie.leonware.api.module.Module;
import sweetie.leonware.api.module.ModuleRegister;
import sweetie.leonware.api.utils.neuro.DataCollector;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/client/features/modules/other/RecorderModule.class */
@ModuleRegister(name = "Recorder", category = Category.OTHER)
public class RecorderModule extends Module {
    private static final RecorderModule instance = new RecorderModule();
    private final DataCollector dataCollector = new DataCollector();
    private class_1297 target;

    @Generated
    public static RecorderModule getInstance() {
        return instance;
    }

    @Override // sweetie.leonware.api.module.Module
    public void onDisable() {
        this.target = null;
        this.dataCollector.stopCollecting();
    }

    @Override // sweetie.leonware.api.module.Module
    public void onEnable() {
        this.dataCollector.startCollecting();
    }

    @Override // sweetie.leonware.api.module.Module, sweetie.leonware.api.system.backend.Configurable
    public void onEvent() {
        EventListener attackEvent = AttackEvent.getInstance().subscribe(new Listener(event -> {
            if (this.target != event.entity()) {
                this.target = event.entity();
            }
            if (this.target != null) {
                this.dataCollector.onAttack(event);
            }
        }));
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener(event2 -> {
            if (this.target != null) {
                this.dataCollector.onUpdate();
            }
        }));
        addEvents(attackEvent, updateEvent);
    }
}
